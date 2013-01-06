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
import chemicalinventory.user.UserInfo;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class SampleBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6817187564055216199L;
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
	private String list_of_analysis = "";
	private String locked = "";
	private String locked_by = "";
	private String locked_date = "";
	private String base = "";
	private String analysis_name = "";
	private int errorCode = 900;
	private int container_id = 0;
	private String id_field = "";
	private int current_no_analysis = 0;
	private String reason_for_change = "";
	
	private int autoIncKey = -1;
	
	private Vector elements = new Vector();
	private Map value_list = new HashMap();
	private Map value_list2;
	
	AnalysisBean analysis = new AnalysisBean();
	UserInfo info = new UserInfo();
	
	/**
	 * Register a new compound dependend sample. Here the sample is linked to 
	 * selected analysis'.
	 * @return true/false
	 */
	public boolean registerSample()
	{
		//validate the compound.
		if(!compound_id.equals("") && compound_id != null)
		{
			//Validate analysis or map id.
			 if((map_id.equals("") || map_id == null) && (id_field == null || id_field.equals("")))
			 {
			 	errorCode = 4;//not map or analysis selected.
			 	return false;
			 }
			 
			 //Validate the container id
			 if(this.container_id <= 0)
			 {
				 errorCode = 5;
				 return false;
			 }

			/*determine wheter a map or individually selected analysis is to be user*/			
			boolean useMap = false;
						
			if(!map_id.equals("") && map_id != null)
			 {
			 	useMap = true;
			 }
			 else if(id_field != null && !id_field.equals(""))
			 {
			 	useMap = false;
			 }
			
			//code the input
			if(batch==null || batch.equals(""))
				batch = "--";
			if(remark==null || remark.equals(""))
				remark = "--";
			
			errorCode = 1;//OK
			
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
						con.setAutoCommit(false);
						
						Statement stmt = con.createStatement();
						String insert = "";
						
						if(useMap)
						{
							insert = "INSERT INTO sample" +
							" (compound_id, remark, batch, created_by," +
							" created_date, analysis_map_id, container_id)" +
							" VALUES("+compound_id+"," +
									"'"+Util.double_q(remark)+"'," +
									" '"+batch+"'," +
									" '"+user+"'," +
									" '"+Util.getDate()+"'," +
									" " +map_id+"," +
									" "+this.container_id+");";
						}					
						else
						{
							insert = "INSERT INTO sample" +
							" (compound_id, remark, batch, created_by, created_date, container_id)" +
							" VALUES("+compound_id+"," +
									"'"+Util.double_q(remark)+"'," +
									" '"+batch+"'," +
									" '"+user+"'," +
									" '"+Util.getDate()+"'," +
									" "+this.container_id+");";
						}
						
						stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet keyset = stmt.getGeneratedKeys();
						
						if(keyset.next())
						{
							autoIncKey = keyset.getInt(1);
							sample_id = String.valueOf(autoIncKey);
						}
						
						//Here map the analysis from the map and the sample toghether in the sample_analysis_table
						
						String insertAnalysis = "";
						stmt.clearBatch();
						
						//a map is used proceed with this
						if(useMap)
						{
							//first get the analysis and the max version of the analysis
							String query_analysis = "SELECT analysis.analysis_id, analysis.version " +
													" FROM analysis, analysis_map, analysis_map_link" +
													" WHERE analysis_map.id = "+map_id+
													" AND analysis.analysis_id = analysis_map_link.analysis_id" +
													" AND analysis_map.id = analysis_map_link.map_id" +
													" AND analysis.active = 'T'" +
													" ORDER BY analysis.analysis_name;";
	
							ResultSet rs = stmt.executeQuery(query_analysis);
							
							//run through the elements from the query and register them in the link table.
							while(rs.next())
							{
								//now link each of the analysis with the sample in the table
								insertAnalysis = "";
								insertAnalysis = "INSERT INTO sample_analysis_link" +
												" (sample_id, analysis_id, analysis_version)" +
												" VALUES("+sample_id+","+rs.getInt("analysis.analysis_id")+", "+rs.getInt("analysis.version")+")";
								
								stmt.addBatch(insertAnalysis);
							}
						}
						else//use individually selected analysis
						{
							StringTokenizer tokens = new StringTokenizer(id_field, ",");
							while(tokens.hasMoreTokens())
							{
								String token = tokens.nextToken().trim();
								int version = analysis.getActiveVersions(token);
								
								//only insert if the active version is greater than 0. else an error orcurred!!
								if(version != 0)
								{
									insertAnalysis = "INSERT INTO sample_analysis_link" +
									" (sample_id, analysis_id, analysis_version)" +
									" VALUES("+sample_id+","+token+", "+version+")";
									
									stmt.addBatch(insertAnalysis);
								}
							}
						}
						
						/*
						 * Handle the insertion of history.
						 */
						insert ="INSERT INTO sample_history (sample_id, changed_by, changed_date, change_remark, remark, batch, compound_id, container_id)"+
						" VALUES("+sample_id+",'"+user+"', '"+Util.getDate()+"', 'New Sample Created', '"+Util.double_q(remark)+"', "+batch+", "+compound_id+", "+container_id+");";
						
						stmt.addBatch(insert);
						
						//execute the batch statement
						try {
							stmt.executeBatch();
							con.commit();
							con.close();
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();
							return false;
						}
					}
				}
			}//end of try
			
			catch (Exception e)
			{
				System.out.println("Error 3 register sample: "+e);
				e.printStackTrace();
				return false;
			}
			
			return false;
		}
		else if (compound_id == null || compound_id.equals("") || compound_id.equals("null"))
		{
			errorCode = 2;//no compound id entered
			return false;
		}
		else
		{
			errorCode = 3;//another error
			return false;
		}
	}
	
	/**
	 * Register a sample linking analysis. This method is used for a compound independet sample
	 * 
	 * 
	 * @return true/false
	 */
	public boolean registerSample_NoCompound()
	{
			/*determine wheter a map or individually selected analysis is to be user*/
			
			boolean useMap = false;
			
			 if(!map_id.equals("") && map_id != null)
			 {
			 	useMap = true;
			 }
			 else if(id_field != null && !id_field.equals(""))
			 {
			 	useMap = false;
			 }
			 if((map_id.equals("") || map_id == null) && (id_field == null || id_field.equals("")))
			 {
			 	errorCode = 4;//not map or analysis selected.
			 	return false;
			 }
			
			//code the input
			if(batch==null || batch.equals(""))
				batch = "--";
			if(remark==null || remark.equals(""))
				remark = "--";
			
			errorCode = 1;//OK
			
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
						con.setAutoCommit(false);
						
						Statement stmt = con.createStatement();
						
						String insert = "";
						
						if(useMap)
						{
							insert = "INSERT INTO sample" +
							" (compound_id, remark, batch, created_by, created_date, analysis_map_id)" +
							" VALUES(0,'"+Util.double_q(remark)+"', 0, '"+user+"', '"+Util.getDate()+"', "+map_id+")";
						}
						else
						{
							insert = "INSERT INTO sample" +
							" (compound_id, remark, batch, created_by, created_date)" +
							" VALUES(0,'"+Util.double_q(remark)+"', 0, '"+user+"', '"+Util.getDate()+"')";
						}
						
						stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet keyset = stmt.getGeneratedKeys();
						
						if(keyset.next())
						{
							autoIncKey = keyset.getInt(1);
							sample_id = String.valueOf(autoIncKey);
						}
						
						//Here map the analysis from the map and the sample toghether in the sample_analysis_table
						
						String insertAnalysis = "";
						stmt.clearBatch();
						
						if(useMap)
						{
							//first get the analysis and the max version of the analysis
							String query_analysis = "SELECT analysis.analysis_id, analysis.version " +
													" FROM analysis, analysis_map, analysis_map_link" +
													" WHERE analysis_map.id = "+map_id+
													" AND analysis.analysis_id = analysis_map_link.analysis_id" +
													" AND analysis_map.id = analysis_map_link.map_id" +
													" AND analysis.active = 'T'" +
													" ORDER BY analysis.analysis_name;";
	
							ResultSet rs = stmt.executeQuery(query_analysis);
							
							//run through the elements from the query and register them in the link table.
							while(rs.next())
							{
								//now link each of the analysis with the sample in the table
								insertAnalysis = "";
								insertAnalysis = "INSERT INTO sample_analysis_link" +
												" (sample_id, analysis_id, analysis_version)" +
												" VALUES("+sample_id+","+rs.getInt("analysis.analysis_id")+", "+rs.getInt("analysis.version")+")";
								
								stmt.addBatch(insertAnalysis);
							}
						}
						else//use individually selecte analysis
						{
							StringTokenizer tokens = new StringTokenizer(id_field, ",");
							while(tokens.hasMoreTokens())
							{
								String token = tokens.nextToken().trim();
								int version = analysis.getActiveVersions(token);
								
								//only insert if the active version is greater than 0. else an error orcurred!!
								if(version != 0)
								{
									insertAnalysis = "INSERT INTO sample_analysis_link" +
									" (sample_id, analysis_id, analysis_version)" +
									" VALUES("+sample_id+","+token+", "+version+")";
									
									stmt.addBatch(insertAnalysis);
								}
							}
						}
						
						/*
						 * Handle the insertion of history.
						 */
						insert ="INSERT INTO sample_history (sample_id, changed_by, changed_date, change_remark, remark, batch, compound_id, container_id)"+
						" VALUES("+sample_id+",'"+user+"', '"+Util.getDate()+"', 'New Sample Created', '"+Util.double_q(remark)+"', 0, 0, 0);";
						
						stmt.addBatch(insert);					
						
						//execute the batch statement
						try {
							stmt.executeBatch();
							con.commit();
							con.close();							
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							con.rollback();
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
	 * Link analysis to a sample
	 * 
	 * 
	 * @param sample_id
	 * @param listOfA
	 * @return true/false
	 */
	public boolean linkAnalysisAndSample(String sample_id, String analysis_id)
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
					
					//get the max analysis versin for the analysis:
					String the_max_version = "";
					String mx_q = "SELECT analysis_id, max(version)" +
							" FROM analysis" +
							" WHERE active = 'T'" +
							" AND analysis_id = "+analysis_id+
							" GROUP BY analysis_id;";
					
					ResultSet rs = stmt.executeQuery(mx_q);
					
					if(rs.next())
					{
						the_max_version = rs.getString("max(version)");
					}
					else
					{
						//an unexpecte error
						con.close();
						return false;
					}
				
					
					String insertAnalysis = "INSERT INTO sample_analysis_link" +
											" (sample_id, analysis_id, analysis_version)" +
											" VALUES("+sample_id+","+analysis_id+", "+the_max_version+")";
							
						
					stmt.executeUpdate(insertAnalysis);
				}
				con.close();
				return true;
			}
		}
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1 link analysis to sample: "+e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2 link analysis to sample: "+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3 link analysis to sample: "+e);
			return false;
		}
		
		return false;
	}
	
	/**
	 * Create a list of analysis' that is linked to the 
	 * sample id received in the setter for sample_id
	 * 
	 * @return true/false
	 */
	public boolean getSampleInfo()
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
					
					String query = "SELECT sample.id," +
							" sample.batch," +
							" sample.created_by," +
							" sample.created_date," +
							" sample.remark," +
							" sample.container_id," +
							" compound.chemical_name," +
							" analysis.analysis_name," +
							" analysis_map.map_name" +
							" FROM (((sample JOIN sample_analysis_link ON sample.id = sample_analysis_link.sample_id)" +
							" JOIN analysis ON sample_analysis_link.analysis_id = analysis.analysis_id AND sample_analysis_link.analysis_version = analysis.version)" +
							" LEFT JOIN analysis_map ON sample.analysis_map_id = analysis_map.id)" +
							" LEFT JOIN compound ON sample.compound_id = compound.id" +
							" WHERE sample.id = "+sample_id+";";							
					
					ResultSet rs = stmt.executeQuery(query);
					elements.clear();
					
					while(rs.next())
					{
						if(rs.isFirst())
						{
							if(rs.getString("compound.chemical_name")==null || rs.getString("compound.chemical_name").equals(""))
								this.chemical_name = "--";
							else
								this.chemical_name = Util.encodeTag(rs.getString("compound.chemical_name"));
							
							if(rs.getString("analysis_map.map_name")==null || rs.getString("analysis_map.map_name").equals(""))
								this.map_name = "--";
							else
								this.map_name = Util.encodeTag(rs.getString("analysis_map.map_name"));
							
							this.remark = Util.encodeTag(rs.getString("sample.remark"));
							this.batch = rs.getString("sample.batch");
							this.created_by = rs.getString("sample.created_by");
							this.created_date = rs.getString("sample.created_date");
							this.sample_id = rs.getString("sample.id");
							this.container_id = rs.getInt("sample.container_id");							
						}
						analysis_name = rs.getString("analysis.analysis_name");		
						
						elements.add(analysis_name);
					}
				}
				con.close();
				
				return true;
			}
		}//end of try
				
		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return false;
		}
		
		return false;
	}
	
	/**
	 * This gets the sample information but does not create analysis data tags.
	 * 
	 * A list of analysis id is stored in a list.
	 * 
	 * @return true/false
	 */
	public boolean getSampleInfo2()
	{
		if(sample_id != null && !sample_id.equals("") && Util.isValidInt(sample_id))
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
						
						String query = "SELECT sample.locked," +
							" sample.locked_by," +
							" sample.locked_date," +
							" sample.id, sample.batch," +
							" sample.created_by," +
							" sample.created_date," +
							" sample.remark," +
							" sample.container_id," +
							" compound.chemical_name," +
							" analysis.analysis_name," +
							" analysis_map.map_name," +
							" analysis.analysis_id" +
							" FROM (((sample JOIN sample_analysis_link ON sample.id = sample_analysis_link.sample_id)" +
							" JOIN analysis ON sample_analysis_link.analysis_id = analysis.analysis_id AND sample_analysis_link.analysis_version = analysis.version)" +
							" LEFT JOIN analysis_map ON sample.analysis_map_id = analysis_map.id)" +
							" LEFT JOIN compound ON sample.compound_id = compound.id"+
							" WHERE sample.id = "+sample_id+";";
									
						ResultSet rs = stmt.executeQuery(query);
						elements.clear();
						
						while(rs.next())
						{
							if (rs.isFirst())
							{
								if(rs.getString("compound.chemical_name")==null || rs.getString("compound.chemical_name").equals(""))
									this.chemical_name = "--";
								else
									this.chemical_name = Util.encodeTag(rs.getString("compound.chemical_name"));
								
								if(rs.getString("analysis_map.map_name")==null || rs.getString("analysis_map.map_name").equals(""))
									this.map_name = "--";
								else
									this.map_name = Util.encodeTag(rs.getString("analysis_map.map_name"));
								
								this.remark = Util.encodeTag(rs.getString("sample.remark"));
								this.batch = rs.getString("sample.batch");
								this.created_by = rs.getString("sample.created_by");
								this.created_date = rs.getString("sample.created_date");
								this.sample_id = rs.getString("sample.id");
								this.locked = rs.getString("sample.locked");
								this.locked_by = rs.getString("sample.locked_by");
								this.locked_date = rs.getString("sample.locked_date");
								this.container_id = rs.getInt("sample.container_id");
							}
							
							this.elements.add(rs.getString("analysis_id"));
						}
						
						if(elements.isEmpty())
						{
							errorCode = 1;//there is no samples
							con.close();
							return false;
						}
					}
					con.close();
					
					return true;
				}
			}//end of try
			
			catch (ClassNotFoundException e) 
			{
				System.out.println("Error 1: "+e);
				errorCode = 2;//unexpected error
				return false;
			}
			catch (SQLException e)
			{
				System.out.println("Error 2: "+e);
				errorCode = 2;//unexpected error
				return false;
			}
			catch (Exception e)
			{
				System.out.println("Error 3: "+e);
				errorCode = 2;//unexpected error
				return false;
			}
			
			return false;
		}
		else
		{
			errorCode = 1;//no sample with the id
			return false;
		}
	}	
	
	
	/**
	 * 
	 * @return
	 */
	public boolean getSampleInfo3()
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
					
					String query = "SELECT sample.id," +
					" sample.batch," +
					" sample.created_by," +
					" sample.created_date," +
					" sample.remark," +
					" compound.chemical_name," +
					" analysis.analysis_name," +
					" analysis.analysis_id," +					
					" analysis_map.map_name" +
					" FROM (((sample JOIN sample_analysis_link ON sample.id = sample_analysis_link.sample_id)" +
					" JOIN analysis ON sample_analysis_link.analysis_id = analysis.analysis_id AND sample_analysis_link.analysis_version = analysis.version)" +
					" LEFT JOIN analysis_map ON sample.analysis_map_id = analysis_map.id)" +
					" LEFT JOIN compound ON sample.compound_id = compound.id"+
					" WHERE sample.id = "+sample_id+";";
										
					ResultSet rs = stmt.executeQuery(query);
					
					int counter = 0;
					elements.clear();
					list_of_analysis = "";
					
					while(rs.next())
					{
						if(counter == 0)
						{
							if(rs.getString("compound.chemical_name")==null || rs.getString("compound.chemical_name").equals(""))
								this.chemical_name = "--";
							else
								this.chemical_name = Util.encodeTag(rs.getString("compound.chemical_name"));
							
							if(rs.getString("analysis_map.map_name")==null || rs.getString("analysis_map.map_name").equals(""))
								this.map_name = "--";
							else
								this.map_name = Util.encodeTag(rs.getString("analysis_map.map_name"));
							
							this.remark = Util.encodeTag(rs.getString("sample.remark"));
							this.batch = rs.getString("sample.batch");
							this.created_by = rs.getString("sample.created_by");
							this.created_date = rs.getString("sample.created_date");
							this.sample_id = rs.getString("sample.id");
							
							//start the list of analysis
							list_of_analysis = rs.getString("analysis.analysis_id");
							
							counter++;
						}
						
						//check if the sample and analysis combination is started..
						boolean isStartedAnalysis = isSample_Analysis_Started(sample_id, rs.getString("analysis.analysis_id"));
						
						analysis_name = "<td>"+Util.encodeTag(rs.getString("analysis.analysis_name"))+"</td>";//the displayed name for the analysis
						
						if(isStartedAnalysis)//the analysis has been started for this sample, and this analysis can therefore not be removed
						{
							analysis_name = analysis_name + "<td align=\"center\"><input type=\"checkbox\" name=\"remove_"+rs.getString("analysis.analysis_id")+"\" value=\""+rs.getString("analysis.analysis_id")+"\" disabled=\"disabled\"></td>";//the check box to remove the analysis	
						}
						else
						{
							analysis_name = analysis_name + "<td align=\"center\"><input type=\"checkbox\" name=\"remove_"+rs.getString("analysis.analysis_id")+"\" value=\""+rs.getString("analysis.analysis_id")+"\"></td>";//the check box to remove the analysis
						}  
						
						elements.add(analysis_name);
						
						//continue the list of analysis'
						list_of_analysis = list_of_analysis+","+rs.getString("analysis.analysis_id");
					}
					
					if(elements.isEmpty())
					{
						errorCode = 1;//there is no samples
						con.close();
						return false;
					}
					
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1 get sample info 3: "+e);
			errorCode = 2;//unexpected error
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2 get sample info 3: "+e);
			e.printStackTrace();
			errorCode = 2;//unexpected error
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3 get sample info 3: "+e);
			errorCode = 2;//unexpected error
			return false;
		}
		
		errorCode = 2;//unexpected error
		return false;
	}
	
	/**
	 * Insert initial sample history when a sample is first created.
	 * 
	 * @param s_id
	 * @return
	 */
	public boolean insertInitialHistory(String s_id)
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
					
					String insert ="INSERT INTO sample_history (sample_id, changed_by, changed_date, remark, batch, compound_id, container_id)"+
					" SELECT id, created_by, created_date, remark, batch, compound_id, container_id" +
					" FROM sample WHERE id = "+s_id+";";
										
					stmt.executeUpdate(insert);
					
					//insert an initial remark
					String insert2 ="UPDATE sample_history set change_remark = 'New Sample Created'"+
					" WHERE sample_id = "+s_id+";";
					
					stmt.executeUpdate(insert2);
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1: "+e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return false;
		}
		
		return false;
	}
		
	/**
	 * 
	 * Insert remark into the sample history table, giving id of the sample, the remark jusitfying the change
	 * and the user perfoming the update.
	 * 
	 * @param s_id
	 * @param remark
	 * @param user
	 * @return status of the action
	 */
	public boolean insertSampleHistory(String s_id, String remark, String user)
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
					
					String s_batch = "";
					String s_compound = "";
					String s_remark = "";
					
					String sample_info = "SELECT batch, compound_id, remark FROM sample where id = "+s_id;
					
					ResultSet rs = stmt.executeQuery(sample_info);
					
					if(rs.next())
					{
						s_batch = rs.getString("batch");
						s_compound = rs.getString("compound_id");
						s_remark = rs.getString("remark");
					}
					
					String insert ="INSERT INTO sample_history (sample_id, changed_by, changed_date, change_remark, batch, compound_id, remark)" +
							" VALUES ("+s_id+", '"+user+"', '"+Util.getDate()+"', '"+remark+"', '"+s_batch+"', '"+s_compound+"', '"+s_remark+"');";
					
					stmt.executeUpdate(insert);
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1: "+e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return false;
		}
		
		return false;
	}
	
	/**
	 * Check if the result entry has been started in the specific analysis
	 * on the specific sample.
	 * 
	 * @return 	true - started sample
	 * 			false - not started sample
	 */
	public boolean isSample_Analysis_Started(String sample_id, String analysis_id)
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
					
					String query = "SELECT sample_id" +
					" FROM result" +
					" WHERE sample_id = "+sample_id+
					" AND analysis_id = "+analysis_id;
					
					ResultSet rs = stmt.executeQuery(query);
									
					if(rs.next())
					{
						con.close();
						return true;//the sample has been started - results entered for analysis
					}
					else
					{
						con.close();
						return false;
					}
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1: "+e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return false;
		}
		
		return false;
	}

	/**
	 * Check if result entry has been performed for a sample
	 * 
	 * @return  true - the sample has been started
	 * 			false - the sample has NOT been started.
	 */
	public boolean isSampleStarted()
	{
		System.out.println("is started kaldt: "+sample_id);
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
					
					String query = "SELECT sample_id" +
					" FROM result" +
					" WHERE sample_id = "+sample_id;
					
					ResultSet rs = stmt.executeQuery(query);
									
					if(rs.next())
					{
						con.close();
						return true;//the sample has been started
					}
					else
					{
						con.close();
						return false;
					}
				}
				con.close();
				
				return true;
			}
		}//end of try

		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return false;
		}
		
		return false;
	}
	
	/**
	 * Lock the sample and locking all sample results.
	 * 
	 * @param sample_id
	 * @param user
	 * @return
	 */
	public boolean lockSample(String sample_id, String user)
	{
		if(!isSampleLocked(sample_id))//if allready locked do not lock again
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
						//lock the sample
						//lock all the results
						//insert history..
						
						stmt.clearBatch();
						
						String update = "";
						String listResults = listResultFromSample(sample_id);
						if(listResults == null)// an error orcurred in the getting of list of results
						{
							con.close();
							return false;
						}
						
						//update the sample
						update = "UPDATE sample" +
								" SET sample.locked = 'T'," +
								" sample.locked_by = '"+user+"'," +
								" sample.locked_date = '"+Util.getDate()+"'," +
								" sample.comment = '"+reason_for_change+"'" +
								" WHERE sample.id = "+sample_id;
												
						stmt.addBatch(update);
						
						//update the results if there is any..
						if(!listResults.equals(""))
						{
							update = "UPDATE result " +
									" SET result.locked = 'T'," +
									" result.locked_by = '"+user+"'," +
									" result.locked_date = '"+Util.getDate()+"'" +
									" WHERE result.id in ("+listResults+");";
									
							stmt.addBatch(update);
						}
						
						try {
							stmt.executeBatch();
						} catch (SQLException e) {
							con.rollback();
							con.close();
							
							return false;
						}
						
						//create the history elements
						insertSampleHistory(sample_id, "SAMPLE LOCKED.", user);
					}
					con.commit();
					con.close();
					return true;
				}
			}//end of try
			
			catch (SQLException e)
			{
				System.out.println("Error 2: "+e);
				return false;
			}
			catch (Exception e)
			{
				System.out.println("Error 3: "+e);
				return false;
			}
			
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	/**
	 * Unlock sample... 
	 * 
	 * @param sample_id
	 * @param user
	 * @return
	 */
	public boolean unLockSample(String sample_id, String user)
	{
		if(isSampleLocked(sample_id))
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
						//lock the sample
						//lock all the results
						//insert history..
						
						stmt.clearBatch();
						
						
						String update = "";
						
						//update the sample
						update = "UPDATE sample" +
								" SET sample.locked = 'F'," +
								" sample.locked_by = ''," +
								" sample.locked_date = '0001-01-01'" +
								" WHERE sample.id = "+sample_id;
						
						stmt.addBatch(update);
											
						try {
							stmt.executeBatch();
						} catch (SQLException e) {
							con.rollback();
							con.close();
							
							return false;
						}
						
						//create the history elements
						insertSampleHistory(sample_id, "SAMPLE UN-LOCKED", user);
					}
					con.commit();
					con.close();
					return true;
				}
			}//end of try
			
			catch (SQLException e)
			{
				System.out.println("Error 2: "+e);
				return false;
			}
			catch (Exception e)
			{
				System.out.println("Error 3: "+e);
				return false;
			}
			
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 
	 * Create list of result ids, for a specific sample.
	 * 
	 * @param sample_id
	 * @return comma separeted list of result ids for the sample.
	 */
	public String listResultFromSample(String sample_id)
	{
		String list = "";
		int counter = 0;
		
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
					
					String query = "SELECT result.id" +
							" FROM sample" +
							" LEFT JOIN result on result.sample_id = sample.id" +
							" WHERE sample.id = "+sample_id;
					
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next())
					{
						if(counter==0)
						{
							list = rs.getString("result.id");
							counter++;
						}
						else
						{
							list = list+","+rs.getString("result.id");
						}
					}
						
				}
				con.close();
				
				return list;
			}
		}//end of try
		
		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
			return null;
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
			return null;
		}
		
		return null;
	}
	
	/**
	 * Check to see if a compound has any sampled linked.
	 * 
	 * 
	 * @return
	 */
	public boolean hasCompoundSamples(String comp_id, boolean showUser)
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
					
					String query = "SELECT  sample.id, sample.container_id, sample.compound_id, sample.remark, sample.batch, sample.created_date, sample.created_by, sample.locked, compound.chemical_name" +
									" FROM sample" +
									" LEFT JOIN compound ON sample.compound_id = compound.id" +
									" WHERE sample.compound_id = "+comp_id+
									" ORDER BY sample.container_id, sample.id;";
					
					ResultSet rs = stmt.executeQuery(query);
									
					this.elements.clear();
					
					while(rs.next())
					{
						this.chemical_name = Util.encodeTag(rs.getString("compound.chemical_name"));
						this.compound_id = rs.getString("sample.compound_id");
						this.container_id = rs.getInt("sample.container_id");
						this.sample_id = rs.getString("sample.id");
						this.remark = rs.getString("sample.remark");
						this.remark = Util.encodeTag(remark);
						this.batch = rs.getString("sample.batch");
						this.created_date = rs.getString("sample.created_date");
						this.created_by = rs.getString("sample.created_by");
						
						//show user data in overlib box??
						if(showUser)
							this.created_by = info.display_owner_data_base(created_by, this.base);
						
						this.locked = rs.getString("sample.locked");						
						
						this.elements.add(this.sample_id+"|"+this.container_id+"|"+this.created_date+"|"+this.created_by+"|"+this.batch+"|"+this.locked+"|"+this.remark);
					}
					
					if(this.elements.size()<1)
					{
						errorCode = 1;//no samples for the compound
						con.close();
						return false;
					}
				}
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (Exception e)
		{
			errorCode = 2;//unexpected error
			e.printStackTrace();
			return false;
		}
		
		errorCode = 2;//unexpected error
		return false;
	}
	
	
	/**
	 * Create a list of available batches for the compound selected
	 * when creating a sample.
	 * @param comp_id
	 * @return
	 */
	public Vector batchsOnCompoundList(String comp_id)
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
					
					String query = "SELECT batch.id" +
									" FROM batch" +
									" LEFT JOIN compound ON batch.compound_id = compound.id" +
									" WHERE batch.compound_id = "+comp_id+
									" AND batch.locked = 'F'"+
									" ORDER BY batch.id;";
					
					ResultSet rs = stmt.executeQuery(query);
					
					Vector list = new Vector();
					
					while(rs.next())
					{						
						list.add(rs.getString("batch.id"));
					}
					
					con.close();
					return list;
				}
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
	 * Create a list of all the samples linked to a specific compound
	 * defined in the parameter comp_id.
	 * The list a list om sample ids.
	 * Only samples not currently connected to a batch is selected!!
	 * @param comp_id
	 * @return Vector.
	 */
	public Vector samplesOnCompoundList(String comp_id)
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
					
					String query = "SELECT sample.id" +
									" FROM sample" +
									" LEFT JOIN compound ON sample.compound_id = compound.id" +
									" WHERE sample.compound_id = "+comp_id+
									" AND sample.batch = 0" +
									" AND locked = 'F'"+
									" ORDER BY sample.id;";
					
					ResultSet rs = stmt.executeQuery(query);
					
					Vector list = new Vector();
					
					while(rs.next())
					{						
						list.add(rs.getString("sample.id"));
					}
					
					con.close();
					return list;
				}
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
	 * Create a list of all the samples linked to a specific compound
	 * defined in the parameter comp_id.
	 * The list a list om sample ids.
	 * Samples is selected including those currently connected to a specific batch !!
	 * @param comp_id
	 * @param batch_id
	 * @return Vector.
	 */	
	public Vector samplesOnCompoundList_mod(String comp_id, int batch_id)
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
					
					String query = "SELECT sample.id" +
									" FROM sample" +
									" LEFT JOIN compound ON sample.compound_id = compound.id" +
									" WHERE sample.compound_id = "+comp_id+
									" AND sample.batch in (0, " + batch_id +")"+
									" AND locked = 'F'"+
									" ORDER BY sample.id;";
					
					ResultSet rs = stmt.executeQuery(query);
					
					Vector list = new Vector();
					
					while(rs.next())
					{						
						list.add(rs.getString("sample.id"));
					}
					
					con.close();
					return list;
				}
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
	 * Controls if a sample is locked...
	 * @param samp_id
	 * @return true = the sample is locked <br>
	 * 		   false = the sample is not locked.
	 */
	public boolean isSampleLocked(String samp_id)
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
					
					String query = "SELECT id, locked" +
									" FROM sample" +
									" WHERE id = "+samp_id;
					
					ResultSet rs = stmt.executeQuery(query);
									
					if(rs.next())
					{
						this.locked = rs.getString("locked");						
					}
					
					if(this.locked.equalsIgnoreCase("T"))
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
				con.close();
				
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			errorCode = 2;//unexpected error
			System.out.println("Error 1: "+e);
			return false;
		}
		catch (SQLException e)
		{
			errorCode = 2;//unexpected error
			System.out.println("Error 2: "+e);
			return false;
		}
		catch (Exception e)
		{
			errorCode = 2;//unexpected error
			System.out.println("Error 3: "+e);
			return false;
		}
		
		errorCode = 2;//unexpected error
		return false;
	}
	
	/**
	 * check if the id is an existing sample id...
	 * @param id
	 * @return
	 */
	public boolean isSample(String id)
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
					
					String query = "SELECT id" +
									" FROM sample" +
									" WHERE id = "+id;
					
					ResultSet rs = stmt.executeQuery(query);
									
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
				con.close();
				
				return false;
			}
		}//end of try
		
		catch (SQLException e)
		{
			return false;
		}
		catch (Exception e)
		{
			return false;
		}
		
		return false;
	}
	
	/**
	 * Modify the analysis linked to a sample.
	 * 
	 * @param sample_id
	 * @return
	 */
	public boolean modifyAnalysisOnSample(String sample_id)
	{
		//first make sure that an update has been performed.
		//then make sure that there will still be analysis'
		//after the update.

		boolean add_new = false;
		boolean remove_old = false;
		
		//check to see if new analysis has been added
		if(this.id_field == null || this.id_field.equals(""))//there has NOT been added any new analysis'
		{
			add_new = false;
		}
		else//yes, new analysis' added..
		{
			add_new = true;
		}
		
		//check to see if any currently connected analysis has been removed.
		value_list2 = cleanMap_remove(value_list, "remove_", 6);
		
		if(value_list2.isEmpty() || value_list2 == null)//no analysis' has been removed
		{
			remove_old = false;
		}
		else//yes, some of the existing has been removed.
		{
			remove_old = true;
		}
		
		if(remove_old == false && add_new == false)//no update has been performed
		{
			errorCode = 1;//1 = no modifications performed.
			return false;
		}
		
		//make sure that not all analysis' has been removed.
		if(add_new == false && remove_old == true)
		{
			//get the number of analysis removed from the analysis
			//StringTokenizer tokens = new StringTokenizer(id_field, ",");
			int number_of_analysis_removed = value_list2.size();//tokens.countTokens();
			
			if(number_of_analysis_removed >= current_no_analysis)//all the previously linked analysis has been removed, and no new has been added ERROR
			{
				errorCode = 2;//no analysis' is linked to the sample
				return false;
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
					stmt.clearBatch();
					con.setAutoCommit(false);
					
					//if any analysis is to be removed removed these
					if(remove_old)
					{
						String remove_analysis = "";
						
						//run through the map and remove the analysis' in there...
						for (Iterator iter = value_list2.entrySet().iterator(); iter.hasNext();) {						
							Map.Entry e = (Map.Entry) iter.next();
														
							String key = (String) e.getKey();
							String number = key.substring(key.indexOf("_")+1);
																	
							remove_analysis = "DELETE FROM sample_analysis_link" +
								" WHERE sample_id = " + sample_id+
								" AND analysis_id = " + number;
							
							stmt.addBatch(remove_analysis);
							}
					}
					
					//add any newly selected analysis
					if(add_new)
					{
						String add_new_analysis = "";
						
						StringTokenizer tokens = new StringTokenizer(id_field, ",");//run through the selected analysis.
						while(tokens.hasMoreTokens())
						{
							//token is the analysis id
							String token = tokens.nextToken().trim();
							
							//first get the active version
							int active_version = analysis.getActiveVersions(token);
							
							add_new_analysis = "INSERT INTO sample_analysis_link" +
							" (sample_id, analysis_id, analysis_version)" +
							" VALUES("+sample_id+","+token+", "+active_version+")";
							
							stmt.addBatch(add_new_analysis);
						}
					}
					
					try {
						stmt.executeBatch();
						
						//update the sample history
						this.reason_for_change = "MODIFICATION OF ANALYSIS LINKED TO SAMPLE! " + this.reason_for_change;
						insertSampleHistory(this.sample_id, reason_for_change, user);

						errorCode = 4; // OK OK OK!!
						con.commit();
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						con.rollback();
						errorCode = 3;//unexpected error
						return false;
					}
				}
				con.close();
			}
		}
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1 modify sample add/remove analysis: "+e);
			errorCode = 3;//unexpected error
			return false;
		}
		catch (SQLException e)
		{
			System.out.println("Error 2 modify sample add/remove analysis: "+e);
			errorCode = 3;//unexpected error
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error 3 modify sample add/remove analysis: "+e);
			errorCode = 3;//unexpected error
			return false;
		}
		
		errorCode = 3;//unexpected error
		return false;
	}
	
	
	
	/**
	 * Clean the map, leaving only elements that match the String parameter, and has a min length as the min_length parameter.
	 * @param map
	 * @param parameter
	 * @param int
	 * @return
	 */
	public Map cleanMap_remove(Map map, String parameter, int min_length)
	{		
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if (element.length()<min_length)//remove items that does not have the correct length
			{
				iter.remove();
			}
			else
			{
				if(element.indexOf(parameter) == -1)//remove items that does not match the parameter
				{
					iter.remove();
				}
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
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
	 * @return Returns the list_of_analysis.
	 */
	public String getList_of_analysis() {
		return list_of_analysis;
	}
	/**
	 * @param list_of_analysis The list_of_analysis to set.
	 */
	public void setList_of_analysis(String list_of_analysis) {
		this.list_of_analysis = list_of_analysis;
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
	 * @return Returns the errorCode.
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @return Returns the locked.
	 */
	public String getLocked() {
		return locked;
	}
	/**
	 * @param locked The locked to set.
	 */
	public void setLocked(String locked) {
		this.locked = locked;
	}
	/**
	 * @return Returns the locked_by.
	 */
	public String getLocked_by() {
		return locked_by;
	}
	/**
	 * @param locked_by The locked_by to set.
	 */
	public void setLocked_by(String locked_by) {
		this.locked_by = locked_by;
	}
	/**
	 * @return Returns the locked_date.
	 */
	public String getLocked_date() {
		return locked_date;
	}
	/**
	 * @param locked_date The locked_date to set.
	 */
	public void setLocked_date(String locked_date) {
		this.locked_date = locked_date;
	}
	/**
	 * @param base The base to set.
	 */
	public void setBase(String base) {
		this.base = base;
	}
	/**
	 * @return Returns the id_field.
	 */
	public String getId_field() {
		return id_field;
	}
	/**
	 * @param id_field The id_field to set.
	 */
	public void setId_field(String id_field) {
		this.id_field = id_field;
	}
	/**
	 * @param value_list The value_list to set.
	 */
	public void setValue_list(Map value_list) {
		this.value_list = value_list;
	}
	/**
	 * @return Returns the current_no_analysis.
	 */
	public int getCurrent_no_analysis() {
		return current_no_analysis;
	}
	/**
	 * @param current_no_analysis The current_no_analysis to set.
	 */
	public void setCurrent_no_analysis(int current_no_analysis) {
		this.current_no_analysis = current_no_analysis;
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
	 * @return Returns the container_id.
	 */
	public int getContainer_id() {
		return container_id;
	}

	/**
	 * @param container_id The container_id to set.
	 */
	public void setContainer_id(int container_id) {
		this.container_id = container_id;
	}
}
