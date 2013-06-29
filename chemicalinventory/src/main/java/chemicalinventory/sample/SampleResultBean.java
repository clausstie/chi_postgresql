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
package chemicalinventory.sample;

import java.io.Serializable;
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


import chemicalinventory.analysis.AnalysisBean;
import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class SampleResultBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6493421302250664654L;
	private String compound_id = "";
	private String map_id = "";
	private String map_name = "";
	private String remark = "";
	private String batch = "";
	private String user = "";
	private String sample_id = "";
	private String chemical_name = "";
	private String created_date = "";
	private String created_by = "";
	private Map value_list = new HashMap();
	private String resultIDS = "";
	private String reason_for_change = "";
	
	private int autoIncKey = -1;
	private int statusCode = 0;
	
	private Vector elements = new Vector();
	private Vector result_id_list = new Vector();
		
	AnalysisBean analysis = new AnalysisBean();
	
	
	/**
	 * This method cleans the received map, making sure 
	 * that only elements that is actually results
	 * are used in the process of registering 
	 * result data.
	 * 
	 * @param map
	 * @return return the cleaned map.
	 */
	public Map cleanMap(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if(!(element.substring(0, 5)).equals("field"))
				iter.remove();
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public Map cleanMap_result(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();

			if(element.length()<8)
			{
				iter.remove();
			}
			else
			{
				if(!(element.substring(0, 8)).equals("resultid"))
					iter.remove();
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 *This method registrers initial data for result entry,
	 * the resulthistory table is updated with initial data at the same time.
	 * 
	 * @return result of the operation (true/false)
	 */
	public boolean registerResultData()
	{
		//the connection object
		Connection con = null;
		statusCode = 900;
		
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				con = ds.getConnection();
				
				if(con != null)  
				{	
					Statement stmt = con.createStatement();
					
					con.setAutoCommit(false);
					
					String insert_result = "";
					stmt.clearBatch();
					
					//the data from the map, clean the map, so that on elements
					//to the data fields is used.
					value_list = cleanMap(value_list);
					
					for (Iterator iter = value_list.entrySet().iterator(); iter.hasNext();) {		
						Map.Entry e = (Map.Entry) iter.next();
						//use only non-null values(the values is stored in the map as string arrays from 
						//the request.
						String[] value_array = (String[]) e.getValue();
						String value = value_array[0];
						value = value.trim();
						if(value != null && !value.equals(""))
						{
							//get the values from the first entry
							String key = (String) e.getKey();
							key = key.trim();
							String ids_only = key.substring(8);//the first value is analysis id, the second fields id
							
							StringTokenizer tokens = new StringTokenizer(ids_only, ",");
							int token_counter = 0;
							String the_analysis_id = "";
							String the_field_id = "";
							String the_unit = "";
							String the_result = "";
							String the_type = "";
							
							while(tokens.hasMoreTokens())
							{
								if(token_counter == 0)
								{
									the_analysis_id = tokens.nextToken().trim();
									token_counter++;
								}
								else if(token_counter == 1)
								{
									the_field_id = tokens.nextToken().trim();
									token_counter++;
								}
								else if(token_counter == 2)
								{
									the_unit = tokens.nextToken().trim();
									token_counter++;
								}
								else if(token_counter == 3)
								{
									the_result = tokens.nextToken().trim();
									token_counter++;
								}
								else if(token_counter == 4)
								{
									the_type = tokens.nextToken().trim();
								}
							}
							
							if(the_result.equals("X"))//it is initial data entry for this data field, create...
							{
								//Validate the result against the limit in the db:
								String validator = "F";
								
								validator = validateResult(the_field_id, value);
								
								//insert the result in the db.
								//if the result is type text insert the value into the coloumn text_value and reported_value
								//if the result is type numeric insert into numeric_value and reported_value
								
								if(the_type.equalsIgnoreCase("numeric"))//insert numrics here others is text
								{
									//numeric results is first validatet, if not correct, the value is changed
									//ex: .09 changed to 0.09
									//ex: 90. changed to 90.0
									//ex: bla bla (text value) changed to 0.0;
									value = Util.formatNumericResult(value);
									
									insert_result = "INSERT INTO result (sample_id, analysis_id," +
											" analysis_field_id, entered_date, entered_by, numeric_value," +
											" reported_value, unit, status) " +
											" VALUES("+sample_id+", "+the_analysis_id+"," +
											" "+the_field_id+", '"+Util.getDate()+"', '"+user+"', "+value+"," +
											" '"+value+"', '"+the_unit+"', '"+validator+"');";
								}
								else 
								{
									insert_result = "INSERT INTO result (sample_id, analysis_id, analysis_field_id," +
													" entered_date, entered_by, text_value, reported_value, unit," +
													" status) " +
													" VALUES("+sample_id+", "+the_analysis_id+", "+the_field_id+"," +
													" '"+Util.getDate()+"', '"+user+"', '"+Util.double_q(value)+"'," +
													" '"+Util.double_q(value)+"', '"+the_unit+"', '"+validator+"');";									
								}
																
								try {
									stmt.executeUpdate(insert_result, Statement.RETURN_GENERATED_KEYS);
									
									ResultSet rs = stmt.getGeneratedKeys();
									
									if(rs.next())
									{
										autoIncKey = rs.getInt(1);									
									}
									else
									{
										con.rollback();
										con.close();
										return false;
									}
									
									String insert_history = "INSERT INTO result_history (result_id, remark, " +
											"changed_date, changed_by, value, unit) " +
											" VALUES("+autoIncKey+", 'Initial Result Entry', '"+Util.getDate()+"'," +
											" '"+user+"', '"+value+"', '"+the_unit+"');";
									
									stmt.executeUpdate(insert_history);
								
									statusCode = 1;
									
								} catch (Exception e1) {
									e1.printStackTrace();
									con.rollback();
									con.close();
									statusCode = 3;
									return false;
								}
							}
							else//it is an update of existing data.
							{
								String value_from_db = getSingleResult(the_result);//get the allready registered value!
																
								//make sure that the new value is different from the old
								if(value_from_db != null && !value_from_db.equalsIgnoreCase(value))
								{
									//Validate the result against the limit in the db:
//									Validate the result against the limit in the db:
									String validator = "F";
									
									validator = validateResult(the_field_id, value);

									//insert the result in the db.
									//if the result is type text insert the value into the coloumn text_value and reported_value
									//if the result is type numeric insert into numeric_value and reported_value
									
									if(the_type.equalsIgnoreCase("numeric"))//insert numrics here others is text
									{
										//numeric results is first validatet, if not correct, the value is changed
										//ex: .09 changed to 0.09
										//ex: 90. changed to 90.0
										//ex: bla bla (text value) changed to 0.0;
										value = Util.formatNumericResult(value);
										
										insert_result = "UPDATE result set entered_date = '"+Util.getDate()+"'," +
										" entered_by = '"+user+"'," +
										" numeric_value = "+value+"," +
										" reported_value = '"+value+"'," +
										" status = '"+validator+"'" +
										" WHERE id = "+the_result;										
									}
									else 
									{
										insert_result = "UPDATE result set entered_date = '"+Util.getDate()+"'," +
										" entered_by = '"+user+"'," +
										" text_value = '"+value+"'," +
										" reported_value = '"+Util.double_q(value)+"'," +
										" status = '"+validator+"'" +
										" WHERE id = "+the_result;
									}
								
									try {
										stmt.executeUpdate(insert_result);
										
										String update_history = "INSERT INTO result_history (result_id, remark," +
																" changed_date, changed_by, value, unit) " +
																" VALUES("+the_result+"," +
																" 'RESULT CHANGED "+this.reason_for_change+"'," +
																" '"+Util.getDate()+"', '"+user+"', '"+value+"'," +
																" '"+the_unit+"');";
										
										stmt.executeUpdate(update_history);

										statusCode = 1;
										
									} catch (Exception e1) {
										e1.printStackTrace();
										con.rollback();
										con.close();
										statusCode = 3;
										return false;
									}//end catch
									
								}//end if
								else
								{
									if(statusCode == 900)
										statusCode = 2;
								}
								
							}//end update of existing data
							
						}//end if value != null
						
					}//end for loop
					
				}//end try
				con.commit();
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			statusCode = 3;
			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			statusCode = 3;
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			statusCode = 3;
			return false;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param field_id
	 * @param entered_result
	 * @return
	 */
	public String validateResult(String field_id, String entered_result)
	{
		String returner = "";
		String db_result_type = "";
		String db_value_min = "";
		String db_value_max = "";
		double db_double_min = 0.0;
		double db_double_max = 0.0;
		double d_value = 0.0;
		
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
					
					String select ="SELECT analysis_fields.result_min, analysis_fields.result_max," +
							" analysis_fields.result_type" +
							" FROM analysis_fields" +
							" WHERE analysis_fields.id = "+field_id;
					
					ResultSet rs = stmt.executeQuery(select);
					
					if(rs.next())
					{
						db_result_type = rs.getString("analysis_fields.result_type").trim();
						db_value_min = rs.getString("analysis_fields.result_min").trim();
						db_value_max = rs.getString("analysis_fields.result_max").trim();
						
						//there is no min or max value skip the validation
						if((db_value_min != null || !db_value_min.equals("")) && (db_value_max != null || !db_value_max.equals("")))
						{							
							//it is an numeric result, validate against upper and lower limit
							if(db_result_type.equals("numeric"))
							{
								db_double_min = rs.getDouble("analysis_fields.result_min");
								db_double_max = rs.getDouble("analysis_fields.result_max");
																
								//first make sure that the result is actually  a valid number.
								if(Util.isValidNumber(entered_result))
								{
									//parse the value to a double
									d_value = Double.parseDouble(entered_result);

									//if only min value registered
									if(db_value_max == null || db_value_max.equals(""))
									{
										con.close();
										
										if(d_value >= db_double_min)//the entered result must be equal to or greater than the min value
										{
											return "T";
										}
										else
										{
											return "F";
										}
									}	
									
									// if only max value registered
									if(db_value_min == null || db_value_min.equals(""))
									{
										con.close();
										
										if(d_value <= db_double_max)
										{
											return "T";
										}
										else
										{
											return "F";
										}
									}
									
									//else both min and max value registered.
									/*
									 * Make sure that the entered number is equal to or greater than the min value,
									 * and the entered number is equal to or greater than the max value
									 */
									
									if(d_value >= db_double_min && d_value <= db_double_max)
									{
										con.close();
										return "T";
									}
									else
									{
										con.close();
										return "F";
									}
								}
								else//not a valid number entered
								{
									con.close();
									return "F";
								}							
							}
							else if(db_result_type.equals("text"))//text type result
							{
								//Here we compare with the min value, if this is empty there will be no validation
								if(db_value_min == null || db_value_min.equals(""))
								{
									con.close();
									return "T";
								}
								else//there was a value now validate
								{
									if(entered_result.equalsIgnoreCase(db_value_min))
									{
										con.close();
										return "T";
									}
									else
									{
										con.close();
										return "F";
									}
								}
							}
							else//not a valid result type!!
							{
								con.close();
								return null;
							}
						}
						else
						{
							//there wasn't any min or max value = no validation done!
							con.close();
							return "T";
						}
					}
					else//no analysis_field 
						returner = null;
				}
				con.close();
				
				return returner;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
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
	 * Get a single reported result.
	 * 
	 * @param r_id
	 * @return
	 */
	public String getSingleResult(String r_id)
	{
		String returner = "";
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
					
					String select ="SELECT reported_value from result WHERE id = "+r_id;
					
					ResultSet rs = stmt.executeQuery(select);
					
					if(rs.next())
					{
						returner = rs.getString("reported_value");
					}
					else 
						returner = null;
				}
				con.close();
				
				return returner;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
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
	 * 
	 * Create a list of locked result, on the sample, return as a vector.
	 * 
	 * @param sample_id
	 * @return
	 */
	public Vector getListOfLockedResults(String sample_id)
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
					
					String select = "SELECT result.id, result.locked" +
									" FROM sample" +
									" LEFT JOIN result on result.sample_id = sample.id" +
									" AND result.locked = 'T'" +
									" WHERE sample.id = "+sample_id;
					
					ResultSet rs = stmt.executeQuery(select);
					
					String id = "";
					
					while(rs.next())
					{
						id = rs.getString("result.id");
						if(id != null || !id.equals(""))
							list.add(rs.getString("result.id"));
					}
					
					if(list.isEmpty())
					{
						con.close();
						return null;
					}
				}
				con.close();
				
				return list;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
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
	 * Update the result history table.
	 * 
	 * 
	 * @param res_id
	 * @param remark
	 * @param user
	 * @return
	 */
	public boolean insertResultHistory(String res_id, String remark, String user)
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
					
					String r_result = "";
					String r_unit = "";
					
					String result_info = "SELECT result.reported_value, result.unit" +
							" FROM result" +
							" where result.id = "+res_id;
					
					ResultSet rs = stmt.executeQuery(result_info);
					
					if(rs.next())
					{
						r_result = rs.getString("result.reported_value");
						r_unit = rs.getString("result.unit");
					}
					
					String insert = "INSERT INTO result_history (result_id, remark, changed_by, changed_date," +
									" value, unit)" +
									" VALUES ("+res_id+", '"+remark+"', '"+user+"', '"+Util.getDate()+"'," +
									" '"+r_result+"', '"+r_unit+"');";
					
					stmt.executeUpdate(insert);
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return false;
		}
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
	 * Lock results on the sample.
	 * 
	 * @param sample_id
	 * @param user
	 * @return
	 */
	public boolean lockResults(String sample_id, String user)
	{
		/*get the all the lists..
		 * 1: list of checked result lines (these are to be locked
		 * 2: list of currently locked results
		 * 
		 * next compare the list from the client with the two lists
		 * 
		 * 1. run through the list of currently locked results
		 * compare with the elements from the list from the clien:
		 * 
		 * if two entries are the same delete from both lists
		 * if an entry is not in the list 2 lock the result and update history and remove the entry from list 2
		 * the rest of the entries in list 2 is results that has to be unlocked, perform this and update history for each
		 */
		
		//clean the map returned from the client to only contain result data
		this.value_list = cleanMap_result(value_list);
		Vector list_of_locked = new Vector();
		
			//get a list of all the results that is currently locked for this sample
			list_of_locked = getListOfLockedResults(sample_id);

			if(list_of_locked != null && !list_of_locked.isEmpty())//there was'ent any locked results.
			{
				for (Iterator iter = list_of_locked.iterator(); iter.hasNext();) {
					String locked_result = (String) iter.next();//this id is currently locked...
					
					//does the list of locked from the client contain the same Key
					if(value_list.containsKey("resultid_"+locked_result))
					{
						//the result was locked and must continue to be
						//remove from both lists
						iter.remove();
						
						value_list.remove("resultid_"+locked_result);
					}
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
					//lock the sample
					//lock all the results
					//insert history..
					
					stmt.clearBatch();
					
					String update = "";
								
					//the rest of the elements in the list of currently locked must now be unlocked		
					if(list_of_locked != null && !list_of_locked.isEmpty())
					{
						for (Iterator iter = list_of_locked.iterator(); iter.hasNext();) {
							String locked_result = (String) iter.next();//this id is currently locked must be unlocked
							
							update = "";
							
							update = "UPDATE result" +
									" SET result.locked = 'F'," +
									" result.locked_by = ''," +
									" result.locked_date ='0001-01-01'" +
									" WHERE result.id = "+locked_result;
							
							stmt.addBatch(update);
										
							//update the history for this result
							insertResultHistory(locked_result, "Result Unlocked", this.user);
						}
					}
					
					//the rest of the elements in the list of locked from the client must now be locked
					if(value_list != null && !value_list.isEmpty())
					{
						for (Iterator iter = value_list.entrySet().iterator(); iter.hasNext();) {				
							Map.Entry e = (Map.Entry) iter.next();
							
							String key = (String) e.getKey();
							String number = key.substring(key.indexOf("_")+1);
						
							update = "";
							
							update = "UPDATE result" +
							" SET result.locked = 'T'," +
							" result.locked_by = '"+this.user+"'," +
							" result.locked_date ='"+Util.getDate()+"'" +
							" WHERE result.id = "+number;
					
							stmt.addBatch(update);
					
							//update the history for the result
							insertResultHistory(number, "Result Locked", this.user);
						}
					}
					
					if((value_list != null && !value_list.isEmpty()) || (list_of_locked != null && !list_of_locked.isEmpty()))
					{
						try {
							stmt.executeBatch();
							con.commit();
						} catch (SQLException e) {
							con.rollback();
							con.close();
							
							return false;
						}
					}
				}
				con.close();
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return false;
		}
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
	 * @return Returns the batch.
	 */
	public String getBatch() {
		return batch;
	}
	/**
	 * @param batch The batch to set.
	 */
	public void setBatch(String batch) {
		this.batch = batch;
	}
	/**
	 * @return Returns the compound_id.
	 */
	public String getCompound_id() {
		return compound_id;
	}
	/**
	 * @param compound_id The compound_id to set.
	 */
	public void setCompound_id(String compound_id) {
		this.compound_id = compound_id;
	}
	/**
	 * @return Returns the map_id.
	 */
	public String getMap_id() {
		return map_id;
	}
	/**
	 * @param map_id The map_id to set.
	 */
	public void setMap_id(String map_id) {
		this.map_id = map_id; 
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the sample_id.
	 */
	public String getSample_id() {
		return sample_id;
	}
	/**
	 * @param sample_id The sample_id to set.
	 */
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @return Returns the map_name.
	 */
	public String getMap_name() {
		return map_name;
	}
	/**
	 * @param map_name The map_name to set.
	 */
	public void setMap_name(String map_name) {
		this.map_name = map_name;
	}
	/**
	 * @return Returns the created_by.
	 */
	public String getCreated_by() {
		return created_by;
	}
	/**
	 * @return Returns the resultIDS.
	 */
	public String getResultIDS() {
		return resultIDS;
	}
	/**
	 * @param created_by The created_by to set.
	 */
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	/**
	 * @return Returns the created_date.
	 */
	public String getCreated_date() {
		return created_date;
	}
	/**
	 * @param created_date The created_date to set.
	 */
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return Returns the elements.
	 */
	public Vector getElements() {
		return elements;
	}
	/**
	 * @return Returns the chemical_name.
	 */
	public String getChemical_name() {
		return chemical_name;
	}
	
	
	/**
	 * @return Returns the value_list.
	 */
	public Map getValue_list() {
		return value_list;
	}
	/**
	 * @param value_list The value_list to set.
	 */
	public void setValue_list(Map value_list) {
		this.value_list = value_list;
	}
	/**
	 * @return Returns the result_id_list.
	 */
	public Vector getResult_id_list() {
		return result_id_list;
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
	 * @return Returns the statusCode.
	 */
	public int getStatusCode() {
		return statusCode;
	}
}