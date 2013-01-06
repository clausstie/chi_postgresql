/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2004-2009.
 *				  All rights reserved.
 *
 *   overLIB:     overLIB 3.51  -- Copyright Erik Bosrup 1998-2002. All rights reserved.
 *
 *   This file is part of chemicalinventory.
 *
 *   chemicalinventory is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   any later version.
 *
 *   chemicalinventory is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package chemicalinventory.batch;

import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.history.History;
import chemicalinventory.sample.SampleBean;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class Batch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6943466355469120361L;
	private int compound_id = 0;
	private int batch_id = 0;
	private int status = 0;
	private String chemical_name = "";
	private String production_location = "";
	private String purity = "";
	private String description = "";
	private String notebook_reference = "";
	private String created_date = "";
	private String created_by = "";
	private String user = "";
	private String sample_list = "";
	private String unlocked_list = "";
	private String locked = "";
	private String samples[];

	private String o_production_location = "";
	private String o_purity = "";
	private String o_description = "";
	private String o_notebook_reference = "";
	private String o_samples = "";
	private String reason_for_change = "";
	private String remove_samples = "";
	
	private Vector analysis_lines  = new Vector();
	
	History history = new History();
	SampleBean sample = new SampleBean();
	
	/**
	 * Create a new batch
	 * @return
	 */
	public boolean create_batch()
	{
		if(validate())
		{
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();
						con.setAutoCommit(false);
						
						String sql = "INSERT INTO batch (compound_id, production_location, notebook_reference, purity, description, created_by, created_date)"+
						" VALUES("+this.compound_id+", '"+Util.double_q(this.production_location)+"', '"+Util.double_q(this.notebook_reference)+"', '"+this.purity+"', '"+Util.double_q(this.description)+"', '"+this.user.toUpperCase()+"', '"+Util.getDate()+"')";
						
						stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
						ResultSet result = stmt.getGeneratedKeys();
						
						if(result.next())
						{
							this.batch_id = result.getInt(1);
							
							//if any samples selected connect them to the batch.
							if(this.samples != null && this.samples.length > 0)
							{
								updateSamples(this.samples, this.batch_id);
							}
							
							//create an element in the history table for the batch
							//create the list of sample from the array
							this.sample_list = "--";
							
							if(this.samples != null)
							{
								for (int i = 0; i < this.samples.length; i++) {
									if(this.sample_list.equals("--"))
										this.sample_list = this.samples[i];
									else
										this.sample_list = this.sample_list + ", " +this.samples[i];
								}
							}
							else
							{
								this.sample_list = "--";
							}
							
							//Create the details about the compound
							String change_details = "--";
							
							change_details = "Chemical name; --; "+Util.double_q(Util.getChemicalName3(this.compound_id))+"|\n";
							change_details = change_details + "Prod. loc.; --; "+Util.encodeNullValue(this.production_location)+"|\n";
							change_details = change_details + "Note. ref.; --; "+Util.encodeNullValue(this.notebook_reference)+"|\n";
							change_details = change_details + "Purity; --; "+Util.encodeNullValue(this.purity)+"|\n";
							change_details = change_details + "Samples; --; "+this.sample_list+"|\n";
							change_details = change_details + "Desc.; --; "+Util.encodeNullValue(this.description)+"\n";
							
							String insert_history = history.insertHistory_string(History.BATCH_TABLE, this.batch_id, Util.double_q(Util.getChemicalName3(this.compound_id)), History.CREATE_BATCH, this.user.toUpperCase(), change_details);
													
							try{
								stmt.executeUpdate(insert_history);
								con.commit();
								con.close();
								return true;
							}
							catch(SQLException e)
							{
								e.printStackTrace();
								con.rollback();
								con.close();
								return false;
							}
						}
						else
						{
							con.rollback();
							con.close();
							return false;
						}
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			} 
			return false;
		}
		else
			return false;
	}
	
	/**
	 * Validate user input.
	 * @return true = input ok, false = input not ok.
	 */
	public boolean validate()
	{
		if(this.compound_id > 0)
			return true;
		
		return false;
	}

	/**
	 * Validate the input values when modifying a batch.
	 *
	 * !! this method does no validation as no mandatory fields can be changed.. !! 
	 *
	 * @return true
	 */
	public boolean validate_modify()
	{
		return true;
	}

	/**
	 * Status = 1: samples could not be modified on the batch return in error.
	 * Status = 2: Unexpected error..
	 * Status = 3: Input validation failed.
	 * @param b_id
	 * @return
	 */
	public boolean modifyBatch(int b_id)
	{
		if(validate_modify())
		{
			//has any values changed??
			boolean prod_change = false;
			boolean note_change = false;
			boolean sample_change = false;
			boolean desc_change = false;
			boolean purity_change = false;
					
			String sql = "";
			
			//has the production location changed
			if(!this.production_location.equals(this.o_production_location))
			{
				prod_change = true;
				
				sql = " batch.production_location = '"+Util.double_q(this.production_location)+"'";
			}
			
			//has the notebooke reference changed??
			if(!this.notebook_reference.equals(this.o_notebook_reference))
			{
				note_change = true;
				String note = " batch.notebook_reference = '"+Util.double_q(this.notebook_reference)+"'";
				if (sql.equals(""))
					sql = note;
				else
					sql = sql + "," + note;
			}
			
			//change in the description.
			if(!this.description.equals(this.o_description))
			{
				desc_change = true;
				String desc = " batch.description = '"+Util.double_q(this.description)+"'";
				if (sql.equals(""))
					sql = desc;
				else
					sql = sql + "," + desc;

			}
			//change in purity
			if(!this.purity.equals(o_purity))
			{
				purity_change = true;
				String py = " batch.purity = '"+this.purity+"'";
				if (sql.equals(""))
					sql = py;
				else
					sql = sql + "," + py;

			}
			
			//has the samples attached change...??
			String n_samples = "";
			
			if(this.o_samples.equals("--"))
				this.o_samples = "";
			
			if(!this.o_samples.equals("") && this.remove_samples.equalsIgnoreCase("on"))
			{
				sample_change = true;
				this.samples = null;
			}
			
			if(this.samples != null)
			{
				for (int i = 0; i < this.samples.length; i++) 
				{
					if(n_samples.equals(""))
						n_samples = samples[i];
					else
						n_samples = n_samples +", " + samples[i];   
				}
			}
			if(!n_samples.equalsIgnoreCase(o_samples))
				sample_change = true;
			
			//if any values has changed, now update the batch.
			if(prod_change || note_change || sample_change || desc_change || purity_change)
			{				
				//update the batch.. and maybe samples...
				try{
					//Connection from the pool
					Context init = new InitialContext();
					if(init == null ) 
						throw new Exception("No Context");
					
					Context ctx = (Context) init.lookup("java:comp/env");
					DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
					if(ds != null) 
					{
						Connection con = ds.getConnection();
						if(con != null)  
						{
							Statement stmt = con.createStatement();
							con.setAutoCommit(false);
							stmt.clearBatch();
							
							sql = "UPDATE batch set" + sql + " WHERE id = "+b_id;
							
							stmt.addBatch(sql);
			
							//update the samples connected to the batch.
							if(sample_change)
							{
								if(!modifySamplesOnBatch(this.samples, b_id))
								{
									con.close();
									this.status = 1;
									return false;
								}
							}
					
							try {
								stmt.executeBatch();
								con.commit();
							} catch (Exception e1) {
								e1.printStackTrace();
								prod_change = false;
								note_change = false;
								desc_change = false;
								purity_change = false;
							}
							
							/*
							 * Now update the history element on this batch..
				               	
								 * Now create the entry in the history table:
								 * The changes is registered in the change_details long_text fields.
								 * a change will have the following syntax
								 * field; old_value; new_value | field; old_value; new_value..... etc
								 * current_quantity; 12.0; 16.5 | unit; g; ml..... etc
								 */                  	
								
								String change_details = "--";
								
								if(prod_change)
								{							
									if(change_details.equals("--"))
									{
										change_details = "Production Loc.; "+o_production_location+"; "+this.production_location;
									}
									else
									{
										change_details = change_details + "| Production; "+o_production_location+"; "+this.production_location;
									}
								}
								
								if(note_change)
								{							
									if(change_details.equals("--"))
									{
										change_details = "Notebook Ref.; "+o_notebook_reference+"; "+this.notebook_reference;
									}
									else
									{
										change_details = change_details + "| Notebook Ref.; "+o_notebook_reference+"; "+this.notebook_reference;
									}
								}
								
								if(purity_change)
								{							
									if(change_details.equals("--"))
									{
										change_details = "Purity; "+o_purity+"; "+purity;
									}
									else
									{
										change_details = change_details + "| Purity; "+o_purity+"; "+this.purity;
									}
								}
								
								if(desc_change)
								{							
									if(change_details.equals("--"))
									{
										change_details = "Desc.; "+o_description+"; "+this.description;
									}
									else
									{
										change_details = change_details + "| Desc.; "+o_description+"; "+this.description;
									}
								}
								
								if(sample_change)
								{						
									if(change_details.equals("--"))
									{
										change_details = "Samples; "+o_samples+"; "+n_samples;
									}
									else
									{
										change_details = change_details + "| Samples.; "+o_samples+"; "+n_samples;
									}
								}

				               	
								//Insert the history element in the table.
								//create the text remark as a standard messege plus any comment entered by the user.
								String text = History.MODIFY_BATCH;
								
								if(reason_for_change != null && !reason_for_change.equals(""))
								{
									text = text + "\n" + reason_for_change;
								}
								
								stmt.executeUpdate(history.insertHistory_string(History.BATCH_TABLE, b_id, Util.double_q(getChemicalNameFromBatch(b_id)), text, user.toUpperCase(), change_details));

							con.commit();
							con.close();
							return true;
						}
						con.close();
					}
				}//end of try
				
				catch (SQLException e)
				{
					e.printStackTrace();
					this.status = 2;
					return false;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					this.status = 2;
					return false;
				}				
			}
			
			return true;
		}
		else
		{
			this.status = 3;
			return false;
		}
	}
	
	/**
	 * Create a comma separeted list of samples connected
	 * to a specific batch.
	 * @param b_id
	 * @return
	 */
	public String listSamplesOnBatch(int b_id)
	{
		String list = "";
		
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "SELECT id" +
							" FROM sample" +
							" WHERE batch = "+b_id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					while(rs.next())
					{
						if(list.equals(""))
							list = rs.getString("id");
						else
							list = list +", " + rs.getString("id");
					}
					
					con.close();
					return list;
				}
				con.close();
			}
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return "";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
		return "";
	}
	
	/**
	 * Create a list (=vector) of samples connected
	 * to a specific batch.
	 * @param b_id
	 * @return
	 */
	public Vector listSamplesOnBatch_vector(int b_id)
	{
		Vector list = new Vector();
		
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "SELECT id" +
							" FROM sample" +
							" WHERE batch = "+b_id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					while(rs.next())
					{
						list.add(rs.getString("id"));
					}
					
					con.close();
					return list;
				}
				con.close();
			}
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	
	
	/**
	 * using a list of samples and a batch id, 
	 * update the samples in the list, connecting
	 * sample and batch.
	 * @param samp array of samples to be connected to the batch
	 * @param id the batch id.
	 * @return true only if the samples is connected, else false.
	 */
	public boolean updateSamples(String samp[], int id)
	{
		if(samp.length > 0 && samp != null)
		{
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();
						stmt.clearBatch();
						
						String sql = "";
						
						for (int i = 0; i < samp.length; i++) 
						{
							sql = "UPDATE sample" +
							" SET batch = "+id+ 
							" WHERE id = "+samp[i]+";";
							
							stmt.addBatch(sql);					    
						}
						
						
						try {
							stmt.executeBatch();
							con.close();
							return true;
						} catch (Exception e1) {
							e1.printStackTrace();
							con.close();
							return false;
						}
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Update the samples conntected to a batch.
	 * the samp[] is the list of samples that has to be conntected
	 * to a batch after the update...
	 * if the list is empty all samples currently conntected to 
	 * the batch is removed.
	 * @param samp
	 * @param id
	 * @return
	 */
	public boolean modifySamplesOnBatch(String samp[], int id)
	{
		Vector current_list = listSamplesOnBatch_vector(id);
		Vector new_list = new Vector();
		new_list.clear();
		
		if(samp != null)
		{
			for (int i = 0; i < samp.length; i++) {
				
				new_list.add(samp[i]);			
			}
		}
		
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();
						con.setAutoCommit(false);
						stmt.clearBatch();
						
						/*
						 * The new list is empty, remove all samples that is currently connected to
						 * this batch id.
						 */
						if(new_list.isEmpty())
						{
							String sql = "UPDATE sample" +
									" SET batch = 0" +
									" where id in ("+listSamplesOnBatch(id)+");";
							
							stmt.addBatch(sql);
						}
						else//modify current sample(s) on the batch.
						{
							/*
							 * First add samples if not currently selected
							 */
							for (Iterator iter = new_list.iterator(); iter.hasNext();) {
								String element = (String) iter.next();
								
								if(!current_list.contains(element))
								{
									//this element is not currently a conncted sample
									//now create sql to connect it..
									String sql_add = "UPDATE sample" +
									" SET batch = " +id+
									" where id = "+element+";";
									
									stmt.addBatch(sql_add);
								}
							}
							
							/*
							 * Remove samples that has been de-selected.
							 */
							for (Iterator iter = current_list.iterator(); iter.hasNext();) {
								String element = (String) iter.next();
								
								if(!new_list.contains(element))
								{
									//remove this element from the sample
									String sql_remove = "UPDATE sample" +
									" SET batch = 0" +
									" where id = "+element+";";
									
									stmt.addBatch(sql_remove);
								}
							}
						}
						
						try {
							stmt.executeBatch();
							con.commit();
							con.close();
							return true;
						} catch (Exception e1) {
							e1.printStackTrace();
							con.rollback();
							con.close();
							return false;
						}
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return false;
	}
	
	
	/**
	 * get information about a specific batch, giving batch id
	 * and getting all registered information about the batch
	 * in question.
	 * @param batch_id
	 * @return true = a valid batch, false = not a valid batch.
	 */
	public boolean getBatchInfo(int batch_id)
	{
		if(batch_id > 0 && isBatch(batch_id))
		{
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();
						
						String sql = "SELECT batch.id, batch.created_date, batch.created_by, batch.compound_id," +
						" batch.production_location, batch.notebook_reference, batch.description, batch.purity," +
						" batch.locked, compound.chemical_name" +
						" FROM batch, compound" +
						" WHERE batch.compound_id = compound.id" +
						" AND batch.id = "+batch_id+";";
						
						ResultSet result = stmt.executeQuery(sql);
						
						if(result.next())
						{
							this.batch_id = result.getInt("batch.id");
							this.created_date = result.getString("batch.created_date");
							this.created_by = result.getString("batch.created_by");
							this.chemical_name = result.getString("compound.chemical_name");
							this.description = result.getString("batch.description");
							this.notebook_reference = result.getString("batch.notebook_reference");
							this.purity = result.getString("batch.purity");
							this.production_location = result.getString("batch.production_location");
							this.compound_id = result.getInt("batch.compound_id");
							this.locked = result.getString("batch.locked");
							
							//get the list of samples for the batch
							this.sample_list = listSamplesOnBatch(batch_id);
							
							//encode null values for all elements..
							this.sample_list = Util.encodeNullValue(this.sample_list);
							this.created_by = Util.encodeNullValue(this.created_by);
							this.created_by = Util.encodeNullValue(this.created_by);
							this.description = Util.encodeTagAndNull(this.description);
							this.notebook_reference = Util.encodeNullValue(this.notebook_reference);
							this.purity = Util.encodeNullValue(this.purity);
							this.production_location = Util.encodeNullValue(this.production_location);
							
							this.chemical_name = Util.encodeTagAndNull(this.chemical_name);
							
							//url encode the chemicalname *(remember to decode on jsp page.)
							this.chemical_name = URLEncoder.encode(this.chemical_name, "UTF-8");
							
							con.close();
							return true;
						}
						else
						{
							con.close();
							return false;
						}
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			} 
			return false;
		}
		else
			return false;
	}

	
	/**
	 * Create all the data for the samples connected to this batch.
	 * all data finally stored in the vector 'analysis_lines' (use the getter method).
	 * 
	 * the parameter replacer indicates if a grafic or textual replacement
	 * for the status of a result line should be used.
	 * 1 = grafic;
	 * 2 = textual;
	 * 
	 * @param sampleList
	 * @param replacer
	 * @return
	 */
	public boolean listSamplesOnBatch(String sampleList, int replacer)
	{
		if(Util.isValueEmpty(sampleList))
		{
			//td width defenitions
			String analysis_w = "width=\"200px\"";
			String sample_id_w = "width=\"50px\"";
			String text_id_w = "width=\"200px\"";
			String result_w = "width=\"100px\"";
			String unit_w = "width=\"50px\"";
			String status_w = "width=\"50px\"";
			String color = "";
			int color_count = 0;
			
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();

						//get the analysis data.
						String sql = "SELECT distinct(analysis.analysis_name), analysis.analysis_id" +
								" FROM sample_analysis_link, analysis" +
								" WHERE sample_analysis_link.analysis_id = analysis.analysis_id" +
								" AND sample_analysis_link.sample_id in ("+sampleList+")" +
								" ORDER BY analysis.analysis_name;";
						
						//this is a list of distinct analysis' connected to all
						//the samples that is connected to this batch
						ResultSet rs = stmt.executeQuery(sql);
						
						analysis_lines.clear();
						
						HashMap map = new HashMap();
						
						//put the analysis data in a map and close the connection.
						while(rs.next())
						{
							//run through the list and find results for all the analysis'
							//and ordering them for display.
							String analysis_name_temp = rs.getString("analysis.analysis_name");
							String analysis_id_temp = rs.getString("analysis.analysis_id");
							
							map.put(analysis_id_temp, analysis_name_temp);
						}
						rs.close();
						
						//run through the map creating the html to be displayed.
						for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) 
						{
							Map.Entry e = (Map.Entry) iter.next();
							
							String analysis_id = (String) e.getKey();
							String analysis_name = (String) e.getValue();
								
							//deceide which color to use for the text lines
							color_count++;
							
							if(color_count % 2 == 0)
								color = "class=\"blue\"";
							else
								color = "class=\"normal\"";
							
							String the_sample_html = "";
							int number_of_lines = 0;
							boolean is_first_analysis_line = true;
							
							StringTokenizer tokens = new StringTokenizer(sampleList, ",");
							
							while(tokens.hasMoreTokens())
							{
								String sample = tokens.nextToken().trim();
								int sample_line_counter = 0;
								
								//get the number of lines for this sample - count
								String counter_sql = "SELECT count(r.sample_id)" +
								" FROM sample s, analysis_fields af, sample_analysis_link sal" +
								" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
								" WHERE s.id = sal.sample_id" +
								" AND af.analysis_id = sal.analysis_id" +
								" AND af.analysis_version = sal.analysis_version" +
								" AND af.result_for_spec = 'T'" +
								" AND s.id in ("+sample+")" +
								" AND r.analysis_id = "+analysis_id+
								" ORDER BY r.sample_id, af.text_id;";
								
								ResultSet count_set = stmt.executeQuery(counter_sql);
															
								if(count_set.next())
									sample_line_counter = count_set.getInt(1);
								
								//close the result set for the counter.
								count_set.close();
								
								//if there is sample lines for the sample now get them
								//other wise a no results message will be displayed.
								if(sample_line_counter > 0)
								{
									//get the sample data.
									String details_sql = "SELECT r.sample_id, r.analysis_field_id, r.analysis_id, af.text_id, af.unit, r.status, r.reported_value" +
											" FROM sample s, analysis_fields af, sample_analysis_link sal" +
											" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
											" WHERE s.id = sal.sample_id" +
											" AND af.analysis_id = sal.analysis_id" +
											" AND af.analysis_version = sal.analysis_version" +
											" AND af.result_for_spec = 'T'" +
											" AND s.id in ("+sample+")" +
											" AND r.analysis_id = "+analysis_id+
											" ORDER BY r.sample_id, af.text_id;";
															
									ResultSet d_rs = stmt.executeQuery(details_sql);
															
									int sample_id = 0;
									String sample_html = "";
									String html = "";
									String html_first = "";
									boolean is_first_sample_line = true;
									
									Vector data_lines = new Vector();
									
									//run trough the list of samples.
									while(d_rs.next())
									{
										//get the sample id.
										sample_id = d_rs.getInt("r.sample_id");
										
										//if the first line of a sample create html.
										if(is_first_sample_line)
										{
											sample_html = "<td "+sample_id_w+" rowspan=\""+sample_line_counter+"\" align=\"center\">"+sample_id+"</td>\n";
											is_first_sample_line = false;
										}
										else
											sample_html = "";
																			
										//get the values for this sample line.
										String f_text = d_rs.getString("text_id");
										f_text = Util.encodeTagAndNull(f_text);
										
										String f_value = d_rs.getString("r.reported_value");
										f_value = Util.encodeTagAndNull(f_value);
										
										String f_unit = d_rs.getString("unit");
										f_unit = Util.encodeNullValue(f_unit);
										
										String f_status = d_rs.getString("r.status");
										
										//create more html for the details
										//these data is allways present.
										if(is_first_analysis_line)
										{										
											html_first = "<td "+text_id_w+">"+f_text+"</td>\n";
											html_first = html_first + "<td "+result_w+" align=\"right\">"+f_value+"</td>\n";
											html_first = html_first + "<td "+unit_w+">"+f_unit+"</td>\n";
											html_first = html_first + "<td "+status_w+" align=\"center\">"+Util.replaceCheckMark(f_status, replacer)+"</td>\n";
											
											//if this is the first line for the sample
											//add the sample data
											if(!sample_html.equals(""))
											{
												html_first = sample_html + html_first;
											}
										}
										else
										{
											html = "<td "+text_id_w+">"+f_text+"</td>\n";
											html = html + "<td "+result_w+" align=\"right\">"+f_value+"</td>\n";
											html = html + "<td "+unit_w+">"+f_unit+"</td>\n";
											html = html + "<td "+status_w+" align=\"center\">"+Util.replaceCheckMark(f_status, replacer)+"</td>\n";
	
											//if this is the first line for the sample
											//add the sample data
											if(!sample_html.equals(""))
											{
												html = sample_html + html;
											}
	
											//add the data to the list of data lines.
											data_lines.add(html);
										}
										
										is_first_analysis_line = false;
									}//while loop on sample data
									
									
									if(!data_lines.isEmpty() || !html_first.equals(""))
									{
										//is the first line for an analysis in this data
										if(!html_first.equals(""))
										{
											//create the html for a single sample...
											the_sample_html = html_first + "</tr>";
											
											//the total number of lines for the analysis
											number_of_lines++;
										}
										//is there other lines for this analysis, that is not the first.
										if(!data_lines.isEmpty())
										{
											for (int i = 0; i<data_lines.size(); i++)
											{
												String data = (String) data_lines.get(i);
												
												the_sample_html = the_sample_html + "<tr valign=\"top\" "+color+">" + data + "</tr>\n";
												
												//the total number of lines for the analysis
												number_of_lines++;
											}
										}
									}
								}
								
							}//tokenizer loop through the list of samples.
								
							//if there is no lines of data for this analysis, display a message telling this...
//							there was not any sample lines for this sample, 
							//create the sample line message 'no results'.
							if(number_of_lines == 0)
							{
								//the total number of lines for the analysis
								number_of_lines++;

								the_sample_html = the_sample_html + "<td colspan=\"5\"><i>..no results</i></td></tr>\n";								
							}
							
							//finish the html for the first row of data
							//stating the rowspan for the td with analysis name
							the_sample_html = "<tr valign=\"top\" "+color+"><td "+analysis_w+" rowspan=\""+number_of_lines+"\">"+analysis_name+"</td>" + the_sample_html +"\n";
														
							//the list holding alle data.
							analysis_lines.add(the_sample_html);
						}//while loop on analysis data
						
					con.close();
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Create all the data for the samples connected to this batch.
	 * all data finally stored in the vector 'analysis_lines' (use the getter method).
	 * 
	 * the parameter replacer indicates if a grafic or textual replacement
	 * for the status of a result line should be used.
	 * 1 = grafic;
	 * 2 = textual;
	 * <br/>
	 * This method is made for pdf report generation...!! 
	 * 
	 * @param sampleList
	 * @param replacer
	 * @return
	 */
	public boolean listSamplesOnBatch_report(String sampleList, int replacer)
	{
		if(Util.isValueEmpty(sampleList))
		{
			//td width defenitions
			String analysis_w = "width=\"20%\"";
			String sample_id_w = "width=\"10%\"";
			String text_id_w = "width=\"29%\"";
			String result_w = "width=\"20%\"";
			String unit_w = "width=\"9%\"";
			String status_w = "width=\"12%\"";
			String color = "";
			int color_count = 0;
			
			try{
				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");
				
				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null) 
				{
					Connection con = ds.getConnection();
					if(con != null)  
					{
						Statement stmt = con.createStatement();

						//get the analysis data.
						String sql = "SELECT distinct(analysis.analysis_name), analysis.analysis_id" +
								" FROM sample_analysis_link, analysis" +
								" WHERE sample_analysis_link.analysis_id = analysis.analysis_id" +
								" AND sample_analysis_link.sample_id in ("+sampleList+")" +
								" ORDER BY analysis.analysis_name;";
						
						//this is a list of distinct analysis' connected to all
						//the samples that is connected to this batch
						ResultSet rs = stmt.executeQuery(sql);
						
						analysis_lines.clear();
						
						HashMap map = new HashMap();
						
						//put the analysis data in a map and close the connection.
						while(rs.next())
						{
							//run through the list and find results for all the analysis'
							//and ordering them for display.
							String analysis_name_temp = rs.getString("analysis.analysis_name");
							String analysis_id_temp = rs.getString("analysis.analysis_id");
							
							map.put(analysis_id_temp, analysis_name_temp);
						}
						rs.close();
						
						//run through the map creating the html to be displayed.
						for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) 
						{
							Map.Entry e = (Map.Entry) iter.next();
							
							String analysis_id = (String) e.getKey();
							String analysis_name = (String) e.getValue();
								
							//deceide which color to use for the text lines
							color_count++;
							
							if(color_count % 2 == 0)
								color = "class=\"smoke\"";
							else
								color = "class=\"normal\"";
							
							String the_sample_html = "";
							int number_of_lines = 0;
							boolean is_first_analysis_line = true;
							
							StringTokenizer tokens = new StringTokenizer(sampleList, ",");
							
							while(tokens.hasMoreTokens())
							{
								String sample = tokens.nextToken().trim();
								int sample_line_counter = 0;
								
								//get the number of lines for this sample - count
								String counter_sql = "SELECT count(r.sample_id)" +
								" FROM sample s, analysis_fields af, sample_analysis_link sal" +
								" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
								" WHERE s.id = sal.sample_id" +
								" AND af.analysis_id = sal.analysis_id" +
								" AND af.analysis_version = sal.analysis_version" +
								" AND af.result_for_spec = 'T'" +
								" AND s.id in ("+sample+")" +
								" AND r.analysis_id = "+analysis_id+
								" ORDER BY r.sample_id, af.text_id;";
								
								ResultSet count_set = stmt.executeQuery(counter_sql);
															
								if(count_set.next())
									sample_line_counter = count_set.getInt(1);
								
								//close the result set for the counter.
								count_set.close();
								
								//if there is sample lines for the sample now get them
								//other wise a no results message will be displayed.
								if(sample_line_counter > 0)
								{
									//get the sample data.
									String details_sql = "SELECT r.sample_id, r.analysis_field_id, r.analysis_id, af.text_id, af.unit, r.status, r.reported_value" +
											" FROM sample s, analysis_fields af, sample_analysis_link sal" +
											" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
											" WHERE s.id = sal.sample_id" +
											" AND af.analysis_id = sal.analysis_id" +
											" AND af.analysis_version = sal.analysis_version" +
											" AND af.result_for_spec = 'T'" +
											" AND s.id in ("+sample+")" +
											" AND r.analysis_id = "+analysis_id+
											" ORDER BY r.sample_id, af.text_id;";
															
									ResultSet d_rs = stmt.executeQuery(details_sql);
															
									int sample_id = 0;
									String sample_html = "";
									String html = "";
									String html_first = "";
									boolean is_first_sample_line = true;
									
									Vector data_lines = new Vector();
									
									//run trough the list of samples.
									while(d_rs.next())
									{
										//get the sample id.
										sample_id = d_rs.getInt("r.sample_id");
										
										//if the first line of a sample create html.
										if(is_first_sample_line)
										{
											sample_html = "<td "+sample_id_w+" rowspan=\""+sample_line_counter+"\" align=\"center\">"+sample_id+"</td>\n";
											is_first_sample_line = false;
										}
										else
											sample_html = "";
																			
										//get the values for this sample line.
										String f_text = d_rs.getString("text_id");
										f_text = Util.encodeTagAndNull(f_text);
										
										String f_value = d_rs.getString("r.reported_value");
										f_value = Util.encodeTagAndNull(f_value);
										
										String f_unit = d_rs.getString("unit");
										f_unit = Util.encodeNullValue(f_unit);
										
										String f_status = d_rs.getString("r.status");
										
										//create more html for the details
										//these data is allways present.
										if(is_first_analysis_line)
										{										
											html_first = "<td "+text_id_w+">"+f_text+"</td>\n";
											html_first = html_first + "<td "+result_w+" align=\"right\">"+f_value+"</td>\n";
											html_first = html_first + "<td "+unit_w+">"+f_unit+"</td>\n";
											html_first = html_first + "<td "+status_w+" align=\"center\">"+Util.replaceCheckMark(f_status, replacer)+"</td>\n";
											
											//if this is the first line for the sample
											//add the sample data
											if(!sample_html.equals(""))
											{
												html_first = sample_html + html_first;
											}
										}
										else
										{
											html = "<td "+text_id_w+">"+f_text+"</td>\n";
											html = html + "<td "+result_w+" align=\"right\">"+f_value+"</td>\n";
											html = html + "<td "+unit_w+">"+f_unit+"</td>\n";
											html = html + "<td "+status_w+" align=\"center\">"+Util.replaceCheckMark(f_status, replacer)+"</td>\n";
	
											//if this is the first line for the sample
											//add the sample data
											if(!sample_html.equals(""))
											{
												html = sample_html + html;
											}
	
											//add the data to the list of data lines.
											data_lines.add(html);
										}
										
										is_first_analysis_line = false;
									}//while loop on sample data
									
									
									if(!data_lines.isEmpty() || !html_first.equals(""))
									{
										//is the first line for an analysis in this data
										if(!html_first.equals(""))
										{
											//create the html for a single sample...
											the_sample_html = html_first + "</tr>";
											
											//the total number of lines for the analysis
											number_of_lines++;
										}
										//is there other lines for this analysis, that is not the first.
										if(!data_lines.isEmpty())
										{
											for (int i = 0; i<data_lines.size(); i++)
											{
												String data = (String) data_lines.get(i);
												
												the_sample_html = the_sample_html + "<tr valign=\"top\" "+color+">" + data + "</tr>\n";
												
												//the total number of lines for the analysis
												number_of_lines++;
											}
										}
									}
								}
								
							}//tokenizer loop through the list of samples.
								
							//if there is no lines of data for this analysis, display a message telling this...
//							there was not any sample lines for this sample, 
							//create the sample line message 'no results'.
							if(number_of_lines == 0)
							{
								//the total number of lines for the analysis
								number_of_lines++;

								the_sample_html = the_sample_html + "<td colspan=\"5\"><i>..no results</i></td></tr>\n";								
							}
							
							//finish the html for the first row of data
							//stating the rowspan for the td with analysis name
							the_sample_html = "<tr valign=\"top\" "+color+"><td "+analysis_w+" rowspan=\""+number_of_lines+"\">"+analysis_name+"</td>" + the_sample_html +"\n";
														
							//the list holding alle data.
							analysis_lines.add(the_sample_html);
						}//while loop on analysis data
						
					con.close();
					}
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Handle the operation of locking a batch.
	 * A batch can only be locked if all samples is locked.
	 * Otherwise the method returns false and the variable
	 * unlocked-list will contain a list of unlocked samples.
	 * @param b_id id of the batch.
	 * @return
	 */
	public boolean lockBatch(int b_id)
	{
		/*
		 * is the batch locked.. else return false
		 */
		if(isBatchLocked(b_id))
			return false;
		
		/*
		 * First check if all the samples included in the batch is locked.
		 * A batch can only be locked if all samples is locked.. otherwise
		 * send a message about the samples that is not locked to the user.
		 */
		
		//get the list of samples for this batch
		String s_list = listSamplesOnBatch(b_id);
		boolean all_is_locked = true;
		this.unlocked_list = "";
		
		if(!s_list.equals(""))
		{
			//run through the list and check each sample.
			StringTokenizer tokenizer = new StringTokenizer(s_list, ",");
			while(tokenizer.hasMoreTokens())
			{
				String data = tokenizer.nextToken().trim();
				
				//check the sample
				if(!sample.isSampleLocked(data))
				{
					all_is_locked = false;
								
					if(this.unlocked_list.equals(""))
						this.unlocked_list = data;
					else
						this.unlocked_list = this.unlocked_list+","+data;
				}
			}
		}

		//perform the lock sample operation 
		if(all_is_locked)
		{
			if(performLockBatch(b_id))
			{
				//update the batch history with lock statement.
				String sql = history.insertHistory_string(History.BATCH_TABLE, b_id, Util.double_q(getChemicalNameFromBatch(b_id)), History.LOCK_BATCH, this.user.toUpperCase(), History.LOCK_BATCH);
				executeUpdateDB(sql);				
				return true;
			}
			else
				return false;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Unlock a batch.
	 * status 1 = could not be locked - general error
	 * status 2 = batch not locked.
	 * @param b_id
	 * @return
	 */
	public boolean unlockBatch(int b_id)
	{
		//perform the lock sample operation 
		if(isBatchLocked(b_id))
		{
			if(performUnLockBatch(b_id))
			{
				//update the batch history with lock statement.
				String sql = history.insertHistory_string(History.BATCH_TABLE, b_id, Util.double_q(getChemicalNameFromBatch(b_id)), History.UNLOCK_BATCH, this.user.toUpperCase(), History.UNLOCK_BATCH);
				executeUpdateDB(sql);				
				return true;
			}
			else
			{
				this.status = 1;
				return false;
			}
		}
		else
		{
			this.status = 2;
			return false;
		}
	}
	
	/**
	 * Giving an sql statement, an update is performed.
	 * @param sql
	 * @return
	 */
	public boolean executeUpdateDB(String sql)
	{	
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
					
					stmt.executeUpdate(sql);
					
					con.close();
					return true;
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * Perform the update of the batch in the db setting the locked flag.
	 * @param b_id
	 * @return
	 */
	public boolean performLockBatch(int b_id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "UPDATE batch SET locked = 'T'" +
							" WHERE id = "+b_id+";";
					
					stmt.executeUpdate(sql);
					
					con.close();
					return true;
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	
	/**
	 * From the batch id get the chemical name that this batch
	 * belongs to.
	 * @param b_id
	 * @return
	 */
	public String getChemicalNameFromBatch(int b_id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "SELECT chemical_name" +
							" FROM batch, compound" +
							" WHERE batch.compound_id = compound.id" +
							" AND batch.id = "+b_id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						String name = rs.getString("chemical_name");
						con.close();
						return name.trim();
					}
					else
					{
						con.close();
						return "--";
					}
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return "--";
		}
		
		return "--";
	}
	
	/**
	 * Is the batch id a valid batch existing in the database...??
	 * @param b_id
	 * @return
	 */
	public boolean isBatch(int b_id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "SELECT batch.id" +
							" FROM batch" +
							" WHERE batch.id = "+b_id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						con.close();
						return true;
					}
					else
					{
						con.close();
						return false;
					}
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * Set a batch as unlocked.
	 * @param b_id
	 * @return
	 */
	public boolean performUnLockBatch(int b_id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "UPDATE batch SET locked = 'F'" +
							" WHERE id = "+b_id+";";
					
					stmt.executeUpdate(sql);
					
					con.close();
					return true;
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * Is the batch locked??
	 * @param b_id
	 * @return
	 */
	public boolean isBatchLocked(int b_id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
		
					String sql = "SELECT locked FROM batch" +
							" WHERE id = "+b_id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						String lock = rs.getString("locked");
						
						if(lock.equalsIgnoreCase("T"))
						{
							con.close();
							return true;
						}
						if(lock.equalsIgnoreCase("F"))
						{
							con.close();
							return false;
						}
					}
					
					con.close();
				}
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * @return Returns the compound_id.
	 */
	public int getCompound_id() {
		return compound_id;
	}
	/**
	 * @param compound_id The compound_id to set.
	 */
	public void setCompound_id(int compound_id) {
		this.compound_id = compound_id;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the notebook_reference.
	 */
	public String getNotebook_reference() {
		return notebook_reference;
	}
	/**
	 * @param notebook_reference The notebook_reference to set.
	 */
	public void setNotebook_reference(String notebook_reference) {
		this.notebook_reference = notebook_reference;
	}
	/**
	 * @return Returns the production_location.
	 */
	public String getProduction_location() {
		return production_location;
	}
	/**
	 * @param production_location The production_location to set.
	 */
	public void setProduction_location(String production_location) {
		this.production_location = production_location;
	}
	/**
	 * @return Returns the purity.
	 */
	public String getPurity() {
		return purity;
	}
	/**
	 * @param purity The purity to set.
	 */
	public void setPurity(String purity) {
		this.purity = purity;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return Returns the created_by.
	 */
	public String getCreated_by() {
		return created_by;
	}
	/**
	 * @return Returns the created_date.
	 */
	public String getCreated_date() {
		return created_date;
	}
	/**
	 * @return Returns the batch_id.
	 */
	public int getBatch_id() {
		return batch_id;
	}
	/**
	 * @return Returns the chemical_name.
	 */
	public String getChemical_name() {
		return chemical_name;
	}
	/**
	 * @param batch_id The batch_id to set.
	 */
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	/**
	 * @return Returns the samples.
	 */
	public String[] getSamples() {
		return samples;
	}
	/**
	 * @param samples The samples to set.
	 */
	public void setSamples(String[] samples) {
		this.samples = samples;
	}
	/**
	 * @return Returns the sample_list.
	 */
	public String getSample_list() {
		return sample_list;
	}
	/**
	 * @return Returns the analysis_lines.
	 */
	public Vector getAnalysis_lines() {
		return analysis_lines;
	}
	/**
	 * @return Returns the unlocked_list.
	 */
	public String getUnlocked_list() {
		return unlocked_list;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return Returns the o_description.
	 */
	public String getO_description() {
		return o_description;
	}
	/**
	 * @param o_description The o_description to set.
	 */
	public void setO_description(String o_description) {
		this.o_description = o_description;
	}
	/**
	 * @return Returns the o_notebook_reference.
	 */
	public String getO_notebook_reference() {
		return o_notebook_reference;
	}
	/**
	 * @param o_notebook_reference The o_notebook_reference to set.
	 */
	public void setO_notebook_reference(String o_notebook_reference) {
		this.o_notebook_reference = o_notebook_reference;
	}
	/**
	 * @return Returns the o_production_location.
	 */
	public String getO_production_location() {
		return o_production_location;
	}
	/**
	 * @param o_production_location The o_production_location to set.
	 */
	public void setO_production_location(String o_production_location) {
		this.o_production_location = o_production_location;
	}
	/**
	 * @return Returns the o_purity.
	 */
	public String getO_purity() {
		return o_purity;
	}
	/**
	 * @param o_purity The o_purity to set.
	 */
	public void setO_purity(String o_purity) {
		this.o_purity = o_purity;
	}
	/**
	 * @return Returns the o_samples.
	 */
	public String getO_samples() {
		return o_samples;
	}
	/**
	 * @param o_samples The o_samples to set.
	 */
	public void setO_samples(String o_samples) {
		this.o_samples = o_samples;
	}
	/**
	 * @return Returns the reason_for_change.
	 */
	public String getReason_for_change() {
		return reason_for_change;
	}
	/**
	 * @param reason_for_change The reason_for_change to set.
	 */
	public void setReason_for_change(String reason_for_change) {
		this.reason_for_change = reason_for_change;
	}
	/**
	 * @return Returns the locked.
	 */
	public String getLocked() {
		return locked;
	}
	/**
	 * @return Returns the remove_samples.
	 */
	public String getRemove_samples() {
		return remove_samples;
	}
	/**
	 * @param remove_samples The remove_samples to set.
	 */
	public void setRemove_samples(String remove_samples) {
		this.remove_samples = remove_samples;
	}
}
