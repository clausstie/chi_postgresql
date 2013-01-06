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
package chemicalinventory.analysis;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.utility.ScriptBean;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class AnalysisBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7978468321188213125L;
	//misc fields
	private String analysis_name = ""; 
	private String analysis_id = "";
	private String version = "";
	private String remark = "";
	private String original_remark = "";
	private String user = "";
	private String base = "";
	private String normalbase = "";
	private String reportbase = "";
	private String reason_for_change = "";
	private String active = "";
	
	private final static String TEXT_ID_KEY = "text_id_";
	private final static String RESULT_MIN_KEY = "result_min_";
	private final static String RESULT_MAX_KEY = "result_max_";
	private final static String RESULT_TYPE_KEY = "result_type_";
	private final static String UNIT_KEY = "unit_";
	private final static String SPEC_ID_KEY = "use_spec_";
		
	//the following fields is for generating fields for a new analysis
	private String text_id1 = "";
	private String text_id2 = "";
	private String text_id3 = "";
	private String text_id4 = "";
	private String text_id5 = "";
	private String text_id6 = "";
	private String text_id7 = "";
	private String text_id8 = "";
	private String text_id9 = "";
	private String text_id10 = "";
	
	private String result_min1 = "";
	private String result_min2 = "";
	private String result_min3 = "";
	private String result_min4 = "";
	private String result_min5 = "";
	private String result_min6 = "";
	private String result_min7 = "";
	private String result_min8 = "";
	private String result_min9 = "";
	private String result_min10 = "";
	
	private String result_max1 = "";
	private String result_max2 = "";
	private String result_max3 = "";
	private String result_max4 = "";
	private String result_max5 = "";
	private String result_max6 = "";
	private String result_max7 = "";
	private String result_max8 = "";
	private String result_max9 = "";
	private String result_max10 = "";
	
	private String result_type1 = "";
	private String result_type2 = "";
	private String result_type3 = "";
	private String result_type4 = "";
	private String result_type5 = "";
	private String result_type6 = "";
	private String result_type7 = "";
	private String result_type8 = "";
	private String result_type9 = "";
	private String result_type10 = "";
	
	private String unit1 = "";
	private String unit2 = "";
	private String unit3 = "";
	private String unit4 = "";
	private String unit5 = "";
	private String unit6 = "";
	private String unit7 = "";
	private String unit8 = "";
	private String unit9 = "";
	private String unit10 = "";

	private String use_spec1 = "";
	private String use_spec2 = "";
	private String use_spec3 = "";
	private String use_spec4 = "";
	private String use_spec5 = "";
	private String use_spec6 = "";
	private String use_spec7 = "";
	private String use_spec8 = "";
	private String use_spec9 = "";
	private String use_spec10 = "";
	
	//the following fields is for ADDING fields to an EXISTING analysis
	private String new_text_id1 = "";
	private String new_text_id2 = "";
	private String new_text_id3 = "";
	private String new_text_id4 = "";
	private String new_text_id5 = "";
	private String new_text_id6 = "";
	private String new_text_id7 = "";
	private String new_text_id8 = "";
	private String new_text_id9 = "";
	private String new_text_id10 = "";
	
	private String new_result_min1 = "";
	private String new_result_min2 = "";
	private String new_result_min3 = "";
	private String new_result_min4 = "";
	private String new_result_min5 = "";
	private String new_result_min6 = "";
	private String new_result_min7 = "";
	private String new_result_min8 = "";
	private String new_result_min9 = "";
	private String new_result_min10 = "";
	
	private String new_result_max1 = "";
	private String new_result_max2 = "";
	private String new_result_max3 = "";
	private String new_result_max4 = "";
	private String new_result_max5 = "";
	private String new_result_max6 = "";
	private String new_result_max7 = "";
	private String new_result_max8 = "";
	private String new_result_max9 = "";
	private String new_result_max10 = "";
	

	private String new_result_type1 = "";
	private String new_result_type2 = "";
	private String new_result_type3 = "";
	private String new_result_type4 = "";
	private String new_result_type5 = "";
	private String new_result_type6 = "";
	private String new_result_type7 = "";
	private String new_result_type8 = "";
	private String new_result_type9 = "";
	private String new_result_type10 = "";
	
	private String new_unit1 = "";
	private String new_unit2 = "";
	private String new_unit3 = "";
	private String new_unit4 = "";
	private String new_unit5 = "";
	private String new_unit6 = "";
	private String new_unit7 = "";
	private String new_unit8 = "";
	private String new_unit9 = "";
	private String new_unit10 = "";
	
	private String new_use_spec1 = "";
	private String new_use_spec2 = "";
	private String new_use_spec3 = "";
	private String new_use_spec4 = "";
	private String new_use_spec5 = "";
	private String new_use_spec6 = "";
	private String new_use_spec7 = "";
	private String new_use_spec8 = "";
	private String new_use_spec9 = "";
	private String new_use_spec10 = "";
	
	//other misc fields
	private boolean isActiveAnalysis = false;
	
	private int autoIncKey = -1;
	private int errorCode = 900;
	private int[] updateCounts = null;
	private Vector elements = new Vector();
	private Vector elements_id = new Vector();
	private Vector scripts = new Vector();
	Vector version_list = new Vector();
	Vector data_list = new Vector();
	Vector html_data_list = new Vector();
	private Map value_list = new HashMap();
	private Map value_list2;
	private Map value_list_new; 
	private Map value_list_hidden; 
	private Map value_list_remove; 
	
	ScriptBean scriptBean = new ScriptBean();
	
	/**
	 * This method creates the inital data for an analysis
	 * @return state of the action true - false
	 */
	public boolean createAnalysis_step1()
	{
		if(!analysis_name.equals("") && analysis_name != null)
		{
			//make sure that the entered name is not in use allready
			Vector names = getAnalysisNamesList();
			
			if(names != null)
			{
				if(names.contains(analysis_name))
				{
					errorCode = 1;//name taken.
					return false;
				}
			}
			else
			{
				errorCode = 2;
				return false;
			}
			
			try{
				//Connection from the pool
					Connection con = Database.getDBConnection();
					if(con != null)  
					{
						con.setAutoCommit(false);
						
						Statement stmt = con.createStatement();
						
						//register the values received from the client
						String create_analysis = "INSERT INTO analysis" +
						" (analysis_name, created_by, created_date, remark, active, removed)" +
						" VALUES('"+analysis_name+"','"+user+"','"+Util.getDate()+"', '"+Util.double_q(remark)+"', 'F', 'F')";
						
						stmt.executeUpdate(create_analysis, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet keyset = stmt.getGeneratedKeys();
						
						if(keyset.next())
						{
							autoIncKey = keyset.getInt(1);
							analysis_id = String.valueOf(autoIncKey);
						}
						
						String insert_id = "UPDATE analysis" +
						" SET analysis_id ="+autoIncKey+
						" WHERE id = "+autoIncKey;
						
						stmt.executeUpdate(insert_id);
						
						con.commit();//commit the changes to the db.
						con.close();
						errorCode = 0;
						return true;
					}
					
					return false;


			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				errorCode = 2;				
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				errorCode = 2;
				return false;
			}
		}
		else
		{
			errorCode = 3;
			return false;
		}
	}
	
	/**
	 * Deletes all data about an analysis
	 * @param id - the id of the analysis to delete
	 */
	public void deleteAnalysis(String id)
	{
		try{
			//Connection from the pool
				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					
					//Delete the analysis
					String delete_analysis = "DELETE FROM analysis WHERE id = "+ id;
					
					stmt.executeUpdate(delete_analysis);
					
					con.commit();//commit the changes to the db.
					con.close();
				}
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove an analysis from the system = set the analysis to inactive
	 * @param id
	 * @param user
	 * @return true/false
	 */
	public boolean removeAnalysis(String id, String user)
	{
		try{
			//Connection from the pool

				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					
					//get the active version, this is the one to remove:
					
					int active_verison = getActiveVersions(id);
					
					stmt.clearBatch();
					
					//Delete the analysis
					String remove_analysis = "UPDATE analysis" +
							" SET removed = 'T'," +
							" active = 'F'" +
							" WHERE analysis_id = "+id+
							" AND version = "+active_verison;
					
					stmt.addBatch(remove_analysis);
					
					//update the history table
					String update_history = "INSERT INTO analysis_history (analysis_id, analysis_version, remark, changed_date, changed_by) " +
					" VALUES("+id+", "+active_verison+", 'ANALYSIS REMOVED', '"+Util.getDate()+"', '"+user+"');";
						
					stmt.addBatch(update_history);
					
					String remove_from_map = "DELETE FROM analysis_map_link WHERE analysis_id = "+id;
					
					stmt.addBatch(remove_from_map);
					
					try {
						stmt.executeBatch();
						con.commit();
						con.close();
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return false;
					}
				}
				
				return false;

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
	}
	
	/**
	 * Reactivate an de-activated (removed) analysis
	 * @param id
	 * @param user
	 * @param ver
	 * @return true false
	 */
	public boolean reactivateAnalysis(String id, String user, String ver)
	{
		try{
			//Connection from the pool
				Connection con = Database.getDBConnection();;
				if(con != null)  
				{
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					stmt.clearBatch();
					
					//Delete the analysis
					String activate_analysis = "UPDATE analysis" +
							" SET removed = 'F'," +
							" active = 'T'" +
							" WHERE analysis_id = "+id+
							" AND version = '"+ver+"';";
					
					stmt.addBatch(activate_analysis);
					
					//now make sure that only one version of this analyis is the active one....
					activateNewestVersion(id, ver);
					
					//update the history table
					String update_history = "INSERT INTO analysis_history (analysis_id, analysis_version, remark, changed_date, changed_by) " +
					" VALUES("+id+", "+ver+", 'ANALYSIS RE-ACTIVATED', '"+Util.getDate()+"', '"+user+"');";
				
					stmt.addBatch(update_history);
					
					try {
						stmt.executeBatch();
						con.commit();
						con.close();
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return false;
					}
				}
		
				return false;
			
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
	}
	
	/**
	 * Step 2 of the creation of an analysis.
	 * Here the individual fields in an analysis is created.
	 * Each field is the definition of a data entry field in this analysis.
	 * @return
	 */
	public boolean createAnalysis_step2()
	{
		// at least one field has to be created...
		if((!text_id1.equals("") && text_id1 != null) ||
				(!text_id2.equals("") && text_id2 != null) ||
				(!text_id3.equals("") && text_id3 != null) ||
				(!text_id4.equals("") && text_id4 != null) ||
				(!text_id5.equals("") && text_id5 != null) ||
				(!text_id6.equals("") && text_id6 != null) ||
				(!text_id7.equals("") && text_id7 != null) ||
				(!text_id8.equals("") && text_id8 != null) ||
				(!text_id9.equals("") && text_id9 != null) ||
				(!text_id10.equals("") && text_id10 != null))
		{
			errorCode = 1;//there is something to register
			
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
						
						/*Insert the user id in the link table between user and
						 *user group.*/
						stmt.clearBatch();
						String insertGroup = null;
						
						if((!text_id1.equals("") && text_id1 != null) && (!result_type1.equals("") && result_type1 != null))
						{
//							validate the min max values:
							if(result_type1.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min1 != null && !result_min1.equals(""))
								{
									result_min1 = result_min1.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min1))
									{
										result_min1 = "0.0";
									}
								}
								if(result_max1 != null && !result_max1.equals(""))//there is a max value
								{
									result_max1 = result_max1.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max1))
									{
										result_max1 = "0.0";
									}
								}
							}
							else if (result_type1.equalsIgnoreCase("text"))
							{
							}
							
							//create the element for use this line in batch or not
							if(this.use_spec1.equalsIgnoreCase("on"))
								this.use_spec1 = "T";
							else
								this.use_spec1 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, " +
									"result_max, unit, created_by, created_date, result_type, result_for_spec) " +
									"VALUES("+Integer.parseInt(analysis_id)+", '"+text_id1+"', '"+result_min1+"', " +
									"'"+result_max1+"', '"+unit1+"', '"+user+"', '"+Util.getDate()+"', '"+result_type1+"', '"+this.use_spec1+"');";

							stmt.addBatch(insertGroup);
						}
						
						if((!text_id2.equals("") && text_id2 != null) && (!result_type2.equals("") && result_type2 != null))
						{
//							validate the min max values:
							if(result_type2.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min2 != null && !result_min2.equals(""))
								{
									result_min2 = result_min2.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min2))
									{
										result_min2 = "0.0";
									}
								}
								if(result_max2 != null && !result_max2.equals(""))//there is a max value
								{
									result_max2 = result_max2.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max2))
									{
										result_max2 = "0.0";
									}
								}
							}
							else if (result_type2.equalsIgnoreCase("text"))
							{
							}
							
							//create the element for use this line in batch or not
							if(this.use_spec2.equalsIgnoreCase("on"))
								this.use_spec2 = "T";
							else
								this.use_spec2 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) " +
									"VALUES("+Integer.parseInt(analysis_id)+", '"+text_id2+"', '"+result_min2+"', " +
									"'"+result_max2+"', '"+unit2+"', '"+user+"', '"+Util.getDate()+"', '"+result_type2+"', '"+this.use_spec2+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id3.equals("") && text_id3 != null) && (!result_type3.equals("") && result_type3 != null))
						{
//							validate the min max values:
							if(result_type3.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min3 != null && !result_min3.equals(""))
								{
									result_min3 = result_min3.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min3))
									{
										result_min3 = "0.0";
									}
								}
								if(result_max3 != null && !result_max3.equals(""))//there is a max value
								{
									result_max3 = result_max3.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max3))
									{
										result_max3 = "0.0";
									}
								}
							}
							else if (result_type3.equalsIgnoreCase("text"))
							{
							}
							
//							create the element for use this line in batch or not
							if(this.use_spec3.equalsIgnoreCase("on"))
								this.use_spec3 = "T";
							else
								this.use_spec3 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id3+"', '"+result_min3+"', '"+result_max3+"', '"+unit3+"', '"+user+"', " +
									"'"+Util.getDate()+"', '"+result_type3+"', '"+this.use_spec3+"');";
							
							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id4.equals("") && text_id4 != null) && (!result_type4.equals("") && result_type4 != null))
						{
//							validate the min max values:
							if(result_type4.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min4 != null && !result_min4.equals(""))
								{
									result_min4 = result_min4.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min4))
									{
										result_min4 = "0.0";
									}
								}
								if(result_max4 != null && !result_max4.equals(""))//there is a max value
								{
									result_max4 = result_max4.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max4))
									{
										result_max4 = "0.0";
									}
								}
							}
							else if (result_type4.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec4.equalsIgnoreCase("on"))
								this.use_spec4 = "T";
							else
								this.use_spec4 = "F";

							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id4+"', '"+result_min4+"', '"+result_max4+"', '"+unit4+"', '"+user+"', " +
									"'"+Util.getDate()+"', '"+result_type4+"', '"+this.use_spec4+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id5.equals("") && text_id5 != null) && (!result_type5.equals("") && result_type5 != null))
						{
//							validate the min max values:
							if(result_type5.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min5 != null && !result_min5.equals(""))
								{
									result_min5 = result_min5.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min5))
									{
										result_min5 = "0.0";
									}
								}
								if(result_max5 != null && !result_max5.equals(""))//there is a max value
								{
									result_max5 = result_max5.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max5))
									{
										result_max5 = "0.0";
									}
								}
							}
							else if (result_type5.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec5.equalsIgnoreCase("on"))
								this.use_spec5 = "T";
							else
								this.use_spec5 = "F";

							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id5+"', '"+result_min5+"', '"+result_max5+"', '"+unit5+"', '"+user+"', " +
									"'"+Util.getDate()+"', '"+result_type5+"', '"+this.use_spec5+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id6.equals("") && text_id6 != null) && (!result_type6.equals("") && result_type6 != null))
						{
//							validate the min max values:
							if(result_type6.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min6 != null && !result_min6.equals(""))
								{
									result_min6 = result_min6.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min6))
									{
										result_min6 = "0.0";
									}
								}
								if(result_max6 != null && !result_max6.equals(""))//there is a max value
								{
									result_max6 = result_max6.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max6))
									{
										result_max6 = "0.0";
									}
								}
							}
							else if (result_type6.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec6.equalsIgnoreCase("on"))
								this.use_spec6 = "T";
							else
								this.use_spec6 = "F";

							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id6+"', '"+result_min6+"', '"+result_max6+"', '"+unit6+"', '"+user+"', " +
									"'"+Util.getDate()+"', '"+result_type6+"', '"+this.use_spec6+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id7.equals("") && text_id7 != null) && (!result_type7.equals("") && result_type7 != null))
						{
//							validate the min max values:
							if(result_type7.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min7 != null && !result_min7.equals(""))
								{
									result_min7 = result_min7.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min7))
									{
										result_min7 = "0.0";
									}
								}
								if(result_max7 != null && !result_max7.equals(""))//there is a max value
								{
									result_max7 = result_max7.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max7))
									{
										result_max7 = "0.0";
									}
								}
							}
							else if (result_type7.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec7.equalsIgnoreCase("on"))
								this.use_spec7 = "T";
							else
								this.use_spec7 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, " +
									"unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id7+"', '"+result_min7+"', '"+result_max7+"', '"+unit7+"', '"+user+"', '"+Util.getDate()+"', " +
									"'"+result_type7+"', '"+this.use_spec7+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id8.equals("") && text_id8 != null) && (!result_type8.equals("") && result_type8 != null))
						{
//							validate the min max values:
							if(result_type8.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min8 != null && !result_min8.equals(""))
								{
									result_min8 = result_min8.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min8))
									{
										result_min8 = "0.0";
									}
								}
								if(result_max8 != null && !result_max8.equals(""))//there is a max value
								{
									result_max8 = result_max8.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max8))
									{
										result_max8 = "0.0";
									}
								}
							}
							else if (result_type8.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec8.equalsIgnoreCase("on"))
								this.use_spec8 = "T";
							else
								this.use_spec8 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max," +
									" unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id8+"', '"+result_min8+"', '"+result_max8+"', '"+unit8+"', '"+user+"', '"+Util.getDate()+"', " +
									"'"+result_type8+"', '"+this.use_spec8+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id9.equals("") && text_id9 != null) && (!result_type9.equals("") && result_type9 != null))
						{
//							validate the min max values:
							if(result_type9.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min9 != null && !result_min9.equals(""))
								{
									result_min9 = result_min9.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min9))
									{
										result_min9 = "0.0";
									}
								}
								if(result_max9 != null && !result_max9.equals(""))//there is a max value
								{
									result_max9 = result_max9.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max9))
									{
										result_max9 = "0.0";
									}
								}
							}
							else if (result_type9.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec9.equalsIgnoreCase("on"))
								this.use_spec9 = "T";
							else
								this.use_spec9 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max," +
									" unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", " +
									"'"+text_id9+"', '"+result_min9+"', '"+result_max9+"', '"+unit9+"', '"+user+"', '"+Util.getDate()+"', " +
									"'"+result_type9+"', '"+this.use_spec9+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!text_id10.equals("") && text_id10 != null) && (!result_type10.equals("") && result_type10 != null))
						{
//							validate the min max values:
							if(result_type10.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(result_min10 != null && !result_min10.equals(""))
								{
									result_min10 = result_min10.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_min10))
									{
										result_min10 = "0.0";
									}
								}
								if(result_max10 != null && !result_max10.equals(""))//there is a max value
								{
									result_max10 = result_max10.replaceAll(",", ".");
									
									if(!Util.isValidNumber(result_max10))
									{
										result_max10 = "0.0";
									}
								}
							}
							else if (result_type10.equalsIgnoreCase("text"))
							{
							}
//							create the element for use this line in batch or not
							if(this.use_spec10.equalsIgnoreCase("on"))
								this.use_spec10 = "T";
							else
								this.use_spec10 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max," +
									" unit, created_by, created_date, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+"," +
									" '"+text_id10+"', '"+result_min10+"', '"+result_max10+"', '"+unit10+"', '"+user+"', '"+Util.getDate()+"'," +
									" '"+result_type10+"', '"+this.use_spec10+"');";

							stmt.addBatch(insertGroup);
						}  
						
						String activate_analysis = "UPDATE analysis SET active = 'T' WHERE analysis_id = '"+analysis_id+"';";
						stmt.addBatch(activate_analysis);
						
						try {
							updateCounts = stmt.executeBatch();
						} catch (Exception e1) {
							e1.printStackTrace();
							con.rollback();
							con.close();
							errorCode = 2;//the registration failed..
							return false;
						}
						
						con.commit();//commit the changes to the db.
					}
					con.close();
					
					return true;
				}
			}//end of try
			
			catch (BatchUpdateException b)
			{   
				System.err.println("SQLException: " + b.getMessage());
				System.err.println("SQLState:  " + b.getSQLState());
				System.err.println("Message:  " + b.getMessage());
				System.err.println("Vendor:  " + b.getErrorCode());
				updateCounts = b.getUpdateCounts();
				for (int i = 0; i < updateCounts.length; i++) 
				{
					System.err.print(updateCounts[i] + "   ");
				}
				
				errorCode = 2;//the registration failed..
				
				return false;
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				errorCode = 2;//the registration failed..
				return false;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				errorCode = 2;//the registration failed..
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				errorCode = 2;//the registration failed..
				return false;
			}
			
			return false;
		}
		else
		{
			errorCode = 3;//No data 
			return false;
		}
	}
	
	/**
	 * Get analysis info, main part of the information is stored in the vector
	 * elements
	 * @return boolean stat of the action.
	 */
	public boolean getAnalysisInfo()
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
					
					String query = "SELECT analysis_id, analysis_name, version, remark, active FROM analysis WHERE analysis_id = "+analysis_id+" AND active = 'T'";
										
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						this.analysis_id = rs1.getString("analysis_id");
						this.analysis_name = rs1.getString("analysis_name");
						this.version = rs1.getString("version");
						this.remark = rs1.getString("remark");
						this.active = rs1.getString("active");
					}
					else//no analysis found with the id..it has not been created ... ERROR
					{
						con.close();
						return false;
					}
					
					//get the infomation about the fields.
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id, analysis_fields.text_id," +
					" analysis_fields.result_min, analysis_fields.result_max, analysis_fields.unit, analysis_fields.result_type, analysis_fields.result_for_spec" +
					" FROM analysis_fields, analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id+
					" AND analysis.version = "+version;
					
					ResultSet rs2 = stmt.executeQuery(query_fields);
					
					while(rs2.next())
					{
						String text = rs2.getString("analysis_fields.text_id");
						String result_min = rs2.getString("analysis_fields.result_min");
						String result_max = rs2.getString("analysis_fields.result_max");
						String unit = rs2.getString("analysis_fields.unit");
						String result_type = rs2.getString("analysis_fields.result_type");
						String spec = rs2.getString("analysis_fields.result_for_spec");

						if(text.equals("") || text == null)
							text = "--";
						
						if(result_min.equals("") || result_min == null)
							result_min = "--";
						
						if(result_max.equals("") || result_max == null)
							result_max = "--";
						
						if(unit.equals("") || unit == null)
							unit = "--";
						
						if(result_type.equals("") || result_type == null)
							result_type = "--";

						if(spec.equals("") || spec == null)
							result_type = "--";

						
						elements.add(text+"|"+result_min+"|"+result_max+"|"+result_type+"|"+unit+"|"+spec);
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
	 * Create a list of all inactive and removed analysis.
	 * @return true/false
	 */
	public boolean getAnalysisInfo_INACTIVE()
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
					
					String query = "SELECT analysis_id, analysis_name, version, remark, active" +
					" FROM analysis WHERE analysis_id = "+analysis_id+" AND active = 'F' AND removed ='T'";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						analysis_id = rs1.getString("analysis_id");
						analysis_name = rs1.getString("analysis_name");
						version = rs1.getString("version");
						remark = rs1.getString("remark");
						active = rs1.getString("active");
						
					}
					else//no analysis found with the id..it has not been created ... ERROR
					{
						con.close();
						return false;
					}
					
					//get the infomation about the fields.
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id," +		
					" analysis_fields.text_id, analysis_fields.result_min, analysis_fields.result_max," +
					" analysis_fields.unit, analysis_fields.result_type, analysis_fields.result_for_spec" +
					" FROM analysis_fields, analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id+
					" AND analysis.version = "+version;
					
					ResultSet rs2 = stmt.executeQuery(query_fields);
					
					while(rs2.next())
					{
						String text = rs2.getString("analysis_fields.text_id");
						String result_min = rs2.getString("analysis_fields.result_min");
						String result_max = rs2.getString("analysis_fields.result_max");
						String unit = rs2.getString("analysis_fields.unit");
						String result_type = rs2.getString("analysis_fields.result_type");
						String spec = rs2.getString("analysis_fields.result_for_spec");
						
						if(text.equals("") || text == null)
							text = "--";
						
						if(result_min.equals("") || result_min == null)
							result_min = "--";
						
						if(result_max.equals("") || result_max == null)
							result_max = "--";
						
						if(unit.equals("") || unit == null)
							unit = "--";
						
						if(result_type.equals("") || result_type == null)
							result_type = "--";

						elements.add(text+"|"+result_min+"|"+result_max+"|"+result_type+"|"+unit+"|"+spec);
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
	 * Create a list of allready used analysis names
	 * @return List of all analysis names stored in a vector
	 */
	public Vector getAnalysisNamesList()
	{
		Vector names = new Vector();
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
					
					String query = "SELECT DISTINCT(analysis_name)FROM analysis;";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					while(rs1.next())
					{
						names.add(rs1.getString("analysis_name"));					
					}					
				}
				con.close();
				
				return names;
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
	 * Get the analysis name from the db taking the analysis id as a parameter
	 * @param id
	 * @return
	 */
	public String getAnalysisNamesDb(String id)
	{
		String name = "";
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
					
					String query = "SELECT DISTINCT(analysis_name)FROM analysis WHERE analysis.analysis_id = "+id+";";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						name = rs1.getString("analysis_name");					
					}					
				}
				con.close();
				
				return name;
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
	 * Create a list of all versions for a specific analysis
	 * @param id of the analysis
	 * @return true/false
	 */
	public boolean getListOfVersionsFields(String id)
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
					
					//first create a list of versions for this analysis
					//get these names from the fields, as we do not want
					//to include versions that does not have any fields.
					
					String getVersions = "SELECT DISTINCT(analysis_version)" +
							" FROM analysis_fields" +
							" WHERE analysis_id = " +id+
							" ORDER BY analysis_version;";
					
					ResultSet rs = stmt.executeQuery(getVersions);

					version_list.clear();
					
					while(rs.next())
					{
						version_list.add(rs.getString("analysis_version"));
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
	 * Create a list of analysis fields for an analysis.
	 * 
	 * Used ?? Dann 16-dec-2004
	 * 
	 * @param id of the analysis
	 * @return true/false
	 */
	public boolean getAnalysisFieldForSearch(String id)
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
					
					//first create a list of versions for this analysis
					//get these names from the fields, as we do not want
					//to include versions that does not have any fields.
					
					String getVersions = "SELECT DISTINCT(analysis_version)" +
							" FROM analysis_fields" +
							" WHERE analysis_id = 1" +
							" ORDER BY analysis_version;";
					
					ResultSet rs = stmt.executeQuery(getVersions);

					version_list.clear();
					
					while(rs.next())
					{
						version_list.add(rs.getString("analysis_version"));
					}
					
					rs.close();
					
					//only proceed of there is any versions else return error
					if(!version_list.isEmpty())
					{
						String query = "SELECT id, analysis_id, analysis_version, text_id, unit, result_type" +
								" FROM analysis_fields" +
								" WHERE analysis_id = 1" +
								" ORDER BY analysis_id, analysis_version, text_id;";
						
						ResultSet rs1 = stmt.executeQuery(query);
						
						data_list.clear();
						data_list = new Vector();
						
	//					get all the fields
						while(rs1.next())
						{
							String f_id = rs1.getString("id");
							String f_version = rs1.getString("analysis_version");
							
							String f_text = rs1.getString("text_id");
							f_text = Util.encodeNullValue(f_text);
							
							String f_unit = rs1.getString("unit");
							f_unit = Util.encodeNullValue(f_unit);
							
							String f_type = rs1.getString("result_type");
							f_type = Util.encodeNullValue(f_type);
							
							data_list.add(f_version+"|"+f_text+"|"+f_type+"|"+f_unit+"|"+f_id);							
						}
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
	 * Create a list of analysis fields for a specific analysis, in a specific version.
	 *
	 *@return String the html elements.
	 */
	public String createFieldsForSearch(String f_version, String a_id, String color)
	{				
		String html = "";
		String html_start = "";
		String html_final = "";
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
					
					String query = "SELECT id, analysis_id, analysis_version, text_id, unit, result_type" +
					" FROM analysis_fields" +
					" WHERE analysis_id = " +a_id+
					" AND analysis_version = "+f_version+
					" ORDER BY analysis_id, analysis_version, text_id;";
			
					ResultSet rs1 = stmt.executeQuery(query);
						
					data_list.clear();
					
		//			get all the fields
					while(rs1.next())
					{
						html = "";
						
						String f_id = rs1.getString("id");
						
						String f_text = rs1.getString("text_id");
						f_text = Util.encodeTag(f_text);
						f_text = Util.encodeNullValue(f_text);
						
						String f_unit = rs1.getString("unit");
						f_unit = Util.encodeNullValue(f_unit);
						
						String f_type = rs1.getString("result_type");
						f_type = Util.encodeNullValue(f_type);
						
						// now create the html to return for this version of the analysis
						html = html + "<td width=\"252\">"+f_text+"</td>\n";
						html = html + "<td width=\"80\">"+f_type+"</td>\n";
						html = html + "<td width=\"82\">"+f_unit+"</td>\n";
						html = html +"<td width=\"56\" align=\"center\"><input type=\"checkbox\" name=\"idArray\" value=\""+f_id+"\" checked=\"checked\"></td>";
												
						data_list.add(html);
					}
				}
				con.close();
				
				//the elements has been created now finsh them.		
				
				if(!data_list.isEmpty())
				{
					html_start = "<td width=\"81\" align=\"center\" rowspan=\""+data_list.size()+"\"><b>"+f_version+"</b></td>";
					
					for (int n = 0; n<data_list.size(); n++)
					{
						String data = (String) data_list.get(n);
						
						if(n == 0)
						{
							html_final = "<tr class=\""+color+"\">" + html_start + data + "</tr>";
						}
						else
						{
							html_final = html_final + "<tr class=\""+color+"\">" + data + "</tr>";
						}
					}
					
					return html_final;
				}
				else
					return null;
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
	 * Display a specific version of an analysis. The version is a parameter, and the
	 * id is set in the analysis_id setter method.
	 * 
	 * @param the_version
	 * @return true/false
	 */
	public boolean getAnalysisInfo_version(String the_version)
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
					
					String query = "SELECT analysis_id, analysis_name, version, remark, active FROM analysis WHERE analysis_id = "+analysis_id+" AND version = "+the_version;
					
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						this.analysis_id = rs1.getString("analysis_id");
						this.analysis_name = rs1.getString("analysis_name");
						this.version = rs1.getString("version");
						this.remark = rs1.getString("remark");
						this.active = rs1.getString("active");
					}
					else//no analysis found with the id..it has not been created ... ERROR
					{
						con.close();
						return false;
					}
					
					//get the infomation about the fields.
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id," +
					" analysis_fields.text_id, analysis_fields.result_min, analysis_fields.result_max," +
					" analysis_fields.unit, analysis_fields.result_type, analysis_fields.result_for_spec" +
					" FROM analysis_fields, analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id+
					" AND analysis.version = "+version;
					
					ResultSet rs2 = stmt.executeQuery(query_fields);
					
					while(rs2.next())
					{
						String text = rs2.getString("analysis_fields.text_id");
						String result_min = rs2.getString("analysis_fields.result_min");
						String result_max = rs2.getString("analysis_fields.result_max");
						String unit = rs2.getString("analysis_fields.unit");
						String result_type = rs2.getString("analysis_fields.result_type");
						String spec = rs2.getString("analysis_fields.result_for_spec");
						
						if(text.equals("") || text == null)
							text = "--";
						
						if(result_min.equals("") || result_min == null)
							result_min = "--";
						
						if(result_max.equals("") || result_max == null)
							result_max = "--";
						
						if(unit.equals("") || unit == null)
							unit = "--";
						
						if(result_type.equals("") || result_type == null)
							result_type = "--";

						elements.add(text+"|"+result_min+"|"+result_max+"|"+result_type+"|"+unit+"|"+spec);
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
	 * get information about an analysis in a specific version.
	 * 
	 * The id and version is set in the setter methods.
	 * Furthermore a javascript is created for earch field making sure that
	 * entering min and max values for the fields is within the valid parameters
	 * like min lower than max value, . is dec sep, no text in numeric fields etc.
	 * 
	 * @return true/false
	 */
	public boolean getAnalysisInfo2()
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
					
					String query = "SELECT analysis_id, analysis_name, version, remark FROM analysis WHERE analysis_id = "+analysis_id+" AND active = 'T'";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						analysis_id = rs1.getString("analysis_id");
						analysis_name = rs1.getString("analysis_name");
						version = rs1.getString("version");
						remark = rs1.getString("remark");
					}
					else//no analysis found with the id..it has not been created ... ERROR
					{
						con.close();
						return false;
					}
					
					//get the infomation about the fields.
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id, analysis_fields.text_id, " +
					"analysis_fields.result_min, analysis_fields.result_max, analysis_fields.unit, " +
					"analysis_fields.id, analysis_fields.result_type, analysis_fields.result_for_spec" +
					" FROM analysis_fields, analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id+
					" AND analysis.version = "+version;
					
					ResultSet rs2 = stmt.executeQuery(query_fields);
					
					elements.clear();
					elements_id.clear();
					scripts.clear();
					
					while(rs2.next())
					{
						String text = rs2.getString("analysis_fields.text_id");
						String result_min = rs2.getString("analysis_fields.result_min");
						String result_max = rs2.getString("analysis_fields.result_max");
						String unit = rs2.getString("analysis_fields.unit");
						String result_type = rs2.getString("analysis_fields.result_type");
						String field_id = rs2.getString("analysis_fields.id");
						String spec = rs2.getString("analysis_fields.result_for_spec");
						
						if(text.equals("") || text == null)
							text = "--";
						
						if(result_min.equals("") || result_min == null)
							result_min = " ";
						
						if(result_max.equals("") || result_max == null)
							result_max = " ";
						
						if(unit.equals("") || unit == null)
							unit = "--";
												
						if(result_type.equals("") || result_type == null)
							result_type = "--";

						scripts.add(scriptBean.ModifyAnalysisScript(field_id));
						elements_id.add(field_id);
						elements.add(text+"|"+result_min+"|"+result_max+"|"+result_type+"|"+unit+"|"+spec);
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
	 * 
	 * Get a vector list of versions on an analysis,
	 * where the analysis id is set by the analysis_id setter method
	 * 
	 * @return Vector holding all versions of the analysis
	 */
	public Vector getListOfVersions()
	{
		Vector versions = new Vector();
		
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
					
					String query = "SELECT version FROM analysis WHERE analysis_id = "+analysis_id+" ORDER by version";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					while(rs1.next())
					{
						versions.add(rs1.getString("version"));
					}
					
					if(versions.isEmpty())
					{
						con.close();
						return null;
					}
					
				}
				con.close();
				
				return versions;
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
	 * Get the active version of an analysis, takinng the id of the analysis
	 * and returning the (int) value of the active analysis
	 * 
	 * @return int active number for the analysis version
	 */
	public int getActiveVersions(String id)
	{
		int version = 0;
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
					
					String query = "SELECT version FROM analysis" +
							" WHERE analysis_id = "+id+
							" AND active = 'T'"; 
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					//make sure the analysis has been created
					if(rs1.next())
					{
						version = rs1.getInt("version");
					}					
				}
				con.close();
				
				return version;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
		return 0;
	}	
	
	/**
	 * The initial history of an analysis is inserted in the history table.
	 * 
	 * @param id analysis id
	 * @param remark_text remark for the transaction
	 * @param a_version version of the analysi
	 * @param a_user user performing the change
	 * @return true/false
	 */
	public boolean insertInitialHistory(String id, String remark_text, String a_version, String a_user)
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
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id, analysis_fields.text_id, analysis_fields.result_min, analysis_fields.result_max, analysis_fields.unit, analysis_fields.result_type" +
					" FROM analysis_fields,  analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id;
					
					ResultSet rs2 = stmt.executeQuery(query_fields);
					/*
					 * The data for the fields is inserted into a text field
					 * in the database.
					 * Each row of data from the analysis_fields is inserted
					 * row 1: data|data|data|data||
					 * row 2; data, ..... |
					 * 
					 * the data in each row is separeted by a single |
					 * and each row is separeted by double || 
					 */
					
					String fields_data = "";
					
					while(rs2.next())
					{
						fields_data = fields_data.concat(rs2.getString("analysis_fields.id"));
						fields_data = fields_data.concat("|");
						
						fields_data = fields_data.concat(rs2.getString("analysis_fields.text_id"));
						fields_data = fields_data.concat("|");
						
						String res_min = rs2.getString("analysis_fields.result_min");
						if(res_min == null || res_min.equals(""))
							res_min = "--";
						fields_data = fields_data.concat(res_min);
						fields_data = fields_data.concat("|");
						
						String res_max = rs2.getString("analysis_fields.result_max");
						if(res_max == null || res_max.equals(""))
							res_max = "--";
						
						fields_data = fields_data.concat(res_max);
						fields_data = fields_data.concat("|");

						fields_data = fields_data.concat(rs2.getString("analysis_fields.result_type"));
						fields_data = fields_data.concat("|");
						
						fields_data = fields_data.concat(rs2.getString("analysis_fields.unit"));
						fields_data = fields_data.concat("||");
					}
					
					String insert = "INSERT INTO analysis_history (analysis_id, remark, analysis_version, changed_date, changed_by, analysis_fields) " +
					" VALUES("+id+", '"+remark_text+"', "+a_version+", '"+Util.getDate()+"', '"+a_user+"', '"+fields_data+"');";
										
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
	 * Create a list of analysis, ready to be displayed on an html page
	 * 
	 * The list uses the parameter 'list_of_analysis' which is not
	 * included in the search result.
	 * 
	 * @return state of the action.
	 */
	public boolean listAnalysis_sample(String list_of_analysis)
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" AND analysis.analysis_id not in("+list_of_analysis+")"+
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"25\"><input type=\"checkbox\" name=\"idArray\" value=\""+rs2.getString("analysis.analysis_id")+" \"></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"200\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"475\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Create a list of analysis, ready to be displayed on an html page
	 * 
	 * List all active analysis in the db.
	 * @return state of the action.
	 */
	public boolean listAnalysis()
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"25\"><input type=\"checkbox\" name=\"idArray\" value=\""+rs2.getString("analysis.analysis_id")+" \"></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"200\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"475\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Create a list of analysis, ready to be displayed on an html page
	 * 
	 * This list incluedes all analysis in the system. Both active and in-active.
	 * Distinct analysis name is put in the list.
	 * 
	 * @return state of the action.
	 */
	public boolean listAnalysis_forSearch()
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
					
					String query = "SELECT DISTINCT(analysis.analysis_name), analysis.analysis_id" +
							" FROM analysis" +
							" ORDER BY 1;";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
												
						data = data.concat("<td><a class=\"black\" href=\""+normalbase+"?action=analysis_list_search&code1=yes&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("<td><a class=\"black\" href=\"javascript:void(0);\" onclick=\"openWindow(\'"+normalbase+"?action=display_analysis&analysis_id="+rs2.getString("analysis.analysis_id")+"\', 1)\">[ Details ]</a></td>");
													
						elements.add(data);
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
	 * Create a list of analysis' in the system. All analysis is included also
	 * if the analysis is no longer active...
	 * @return
	 */
	public boolean listAnalysis_history()
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
					
					String query = "SELECT DISTINCT(analysis.analysis_name), analysis.analysis_id, analysis.remark" +
							" FROM analysis" +
							" GROUP BY analysis.analysis_id" +
							" ORDER BY 1;";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					elements.clear();
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"190\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"435\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=analysis_history&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Display\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Create a list of active analysis.
	 * 
	 * The list returned also inclued a button for modifying an analysis.
	 * 
	 * @return true/false of the operation
	 */
	public boolean listAnalysis2()
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"190\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"435\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=modify_analysis&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Modify\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Create a list of analysis, where the analysis is active.
	 * 
	 * Result includes a button for removing analysis'
	 * 
	 * @return true/false of the operation
	 */
	public boolean listAnalysis_remove()
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"190\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"435\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=remove_analysis&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Remove\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Here is created a list of removed analysis.
	 * 
	 * Result includes button for reactivation of the analysis
	 * 
	 * @return true/false
	 */
	public boolean listRemovedAnalysis()
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
					
						String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, MAX(analysis.version)" +
						" FROM analysis" +
						" WHERE analysis.removed = 'T'" +
						" GROUP BY analysis.analysis_id" +
						" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"190\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&inactive=yes&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"435\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("MAX(analysis.version)")+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=reactivate_analysis&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Reactivate\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Creates a list of active analysis.
	 * 
	 * The list received as parameter is a list of analysis
	 * that will be returned as checked in the checkbox created by this method.
	 * 
	 * @param list selected analysis.
	 * @return true/false
	 */
	public boolean listAnalysis3(Vector list)
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						if(list.contains(rs2.getString("analysis.analysis_id")))
						{
							data = data.concat("<td width=\"25\"><input checked=\"checked\" type=\"checkbox\" name=\"idArray\" value=\""+rs2.getString("analysis.analysis_id")+" \"></td>");
							data = data.concat("|");
						}
						else
						{
							data = data.concat("<td width=\"25\"><input type=\"checkbox\" name=\"idArray\" value=\""+rs2.getString("analysis.analysis_id")+" \"></td>");
							data = data.concat("|");
						}
						
						data = data.concat("<td width=\"200\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"475\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Version for display of the analysis. Here is created a button
	 * that will display the analysis.
	 * 
	 * @return true/false
	 */
	public boolean listAnalysis4()
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"180px\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"270px\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50px\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50px\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=display_analysis&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Display\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * 
	 * create a list of analysis. Will create a button that 
	 * will generate a pdf report of this analyis.
	 * 
	 * @return true/false
	 */
	public boolean listAnalysis_report()
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
					
					String query = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.remark, analysis.version" +
					" FROM analysis" +
					" WHERE analysis.active = 'T'" +
					" ORDER BY analysis.analysis_name";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					String data = "";
					
					while(rs2.next())
					{
						data = "";
						
						data = data.concat("<td width=\"180px\"><a class=\"black\" target=\"blank\" href=\""+normalbase+"?action=analysis_detail&analysis_id="+rs2.getString("analysis.analysis_id")+"\">"+Util.encodeTag(rs2.getString("analysis.analysis_name"))+"</a></td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"270px\">"+Util.encodeTag(rs2.getString("analysis.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50px\" align=\"center\">"+rs2.getString("analysis.version")+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50px\" align=\"center\">"+
								"<form method=\"post\" action=\""+reportbase+"?action=report_analysis&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs2.getString("analysis.analysis_id")+"\" name=\"analysis_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Report\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
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
	 * Get the name of an analysis
	 * 
	 * @param ana_id id of the analysis
	 * @return String name of the analysis
	 */
	public String getAnalysisData(String ana_id)
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
					
					String query = "SELECT analysis.analysis_name" +
					" FROM analysis" +
					" WHERE analysis.analysis_id ="+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					
					if(rs2.next())
					{
						String analysis_name_to_return = rs2.getString("analysis.analysis_name"); 
						con.close();
						return analysis_name_to_return;
						
					}
					else
					{
						con.close();
						return null;
					}
					
				}
				con.close();
			}
			return null;
			
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
	}
	
	
	/**
	 * This method creates an overlib box with min and max values for an analysis field. The 
	 * text id for the sample fields is shown to the user on the page
	 * @param min_value
	 * @param max_value
	 * @param units
	 * @param text_value
	 * @return the code for the overlib box.
	 */
	public String createLimitBox(String min_value, String max_value, String units, String text_value)
	{
		String box = "";
		
		box = "<a class=\"table_link\" href=\"javascript:void(0);\" onmouseover=\"return overlib('Result min: "+min_value+" "+units+" <br> Result max: "+max_value+" "+units+"', LEFT, BORDER, 2, CAPTION, 'Result limits: "+text_value+"');\" onmouseout=\"return nd();\">"+text_value+"</a>";
		
		return box;
	}
	
	/**
	 * Create a list of analysis' linked to a sample.
	 * 
	 * @param samp_id id of a sample
	 * @return list of analysis
	 */
	public Vector getAnalysisLinkedToSample(String samp_id)
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
					
					String query = "SELECT analysis.analysis_id" +
					" FROM sample_analysis_link, analysis" +
					" WHERE sample_analysis_link.analysis_id = analysis.analysis_id" +
					" AND sample_analysis_link.sample_id = "+samp_id+
					" AND sample_analysis_link.analysis_version = analysis.version";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					elements.clear();
					
					while(rs2.next())
					{
						elements.add(rs2.getString("analysis.analysis_id"));
					}					
					
					if(elements.isEmpty())
					{
						con.close();
						return null;
					}
					else
					{
						con.close();
						return elements;
					}
				}
				con.close();
			}
			return null;
			
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
	}
	
	
	/**
	 * Get analysis info stored in private variables.
	 * Only information about active analysis is used
	 * 
	 * @param ana_id id of the analysis
	 * @return the name of the analysis
	 */
	public String getAnalysisData2(String ana_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		
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
					
					elements.clear();
					
					String query = "SELECT a.id, a.analysis_id, a.analysis_name, af.id, af.text_id, af.unit, a.active " +
					" FROM analysis a" +
					" LEFT JOIN analysis_fields af on (a.analysis_id = af.analysis_id and a.version = af.analysis_version)" +
					" WHERE a.analysis_id = "+ana_id+
					" ORDER BY af.text_id";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())
					{
						if(rs2.getString("a.active").equals("T"))
						{
							//it is an active analysis
							this.isActiveAnalysis = true;
							
							if(rs2.isFirst())//only get the name once
							{
								analysis_name_to_return = Util.encodeTag(rs2.getString("a.analysis_name"));
								this.analysis_name = analysis_name_to_return;
							}
							
							fields_data = "<tr>" +
							"<td width=\"250px\">"+Util.encodeTag(rs2.getString("af.text_id"))+"</td> \n" +
							"<td width=\"250px\"><input type=\"text\" size=\"41\" name=\"field_id"+rs2.getString("a.analysis_id")+","+rs2.getString("af.id")+","+rs2.getString("af.unit")+"\"></td> \n" +
							"<td width=\"100px\">&nbsp;"+rs2.getString("af.unit")+"</td> \n" +
							"</tr> \n";
							
							elements.add(fields_data);
							
						}
						else
						{
							//inactive analysis, do not use...
							this.isActiveAnalysis = false;
							analysis_name_to_return = null;
							break;
						}
					}											
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Benyttes blandt andet fra result entry til at finde alle felter
	 *  inden der kan indtastes resultater.
	 * @param ana_id
	 * @param samp_id
	 * @return
	 */
	public String getAnalysisData3(String ana_id, String samp_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		isActiveAnalysis = false;
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = anaRS.getString("analysis.analysis_name");
						this.analysis_name = Util.encodeTag(analysis_name_to_return);
					}
					
					//now get the information about the fields, result and sample
					String query ="SELECT s.locked," +
							" s.id," +
							" af.id," +
							" af.text_id," +
							" af.unit," +
							" af.result_type," +
							" af.result_min," +
							" af.result_max, " +
							" r.reported_value," +
							" r.locked, r.id" +
							" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
							" AND af.analysis_version = sal.analysis_version)" +
							" JOIN sample s ON s.id = sal.sample_id)" +
							" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
							" WHERE s.id = "+samp_id+
							" AND af.analysis_id = "+ana_id+";"; 
						
						
//						"SELECT s.locked, s.id, af.id, af.text_id, af.unit, af.result_type,
					// af.result_min, af.result_max, r.reported_value, r.locked, r.id" +
//					" FROM sample s, analysis_fields af, sample_analysis_link sal" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+samp_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())
					{
						//if(rs2.getString("a.active").equals("T"))
						if(active_analysis.equals("T"))
						{
							//it is an active analysis
							this.isActiveAnalysis = true;
							
						}
						if(rs2.isFirst())//only get the name once
						{
							//								analysis_name_to_return = Util.encodeTag(rs2.getString("a.analysis_name"));
							analysis_name_to_return = Util.encodeTag(analysis_name_to_return);
							this.analysis_name = analysis_name_to_return;
						}
						
//						now get all the field data information and present it in html format
						String reported_data = rs2.getString("r.reported_value");
						String db_unit = rs2.getString("af.unit");
						//code the units
						if (db_unit == null || db_unit.equals(""))
						{
							db_unit = "-";
						}
						
						//get the min and max values
						String db_min = rs2.getString("af.result_min");
						String db_max = rs2.getString("af.result_max");
						
						//get the text id:
						String db_text_id = Util.encodeTag(rs2.getString("af.text_id"));
						
						//get the result type
						String db_type = rs2.getString("af.result_type");
						
						//javaScript to check numeric
						String js = "";
												
						//Name of the text fields
						String field_name = "";
						
						//create the overlib box with the min and max values for the analysis field
						db_text_id = createLimitBox(db_min, db_max, db_unit, db_text_id);
						
						//create the html
						if (reported_data == null || reported_data.equals(""))
						{
							reported_data = "--";
							field_name = "field_id"+id_of_the_analysis+","+rs2.getString("af.id")+","+db_unit+",X,"+db_type;
						
							if(db_type.equalsIgnoreCase("numeric"))
								js = "onblur=\"if(!this.value.match(/^[ \\+-]?\\d+(\\.\\d+)?$/)&&this.value!=\'\'){alert(\'Du kan kun indtaste tal (0-9)\');this.focus()}\"";
							else
								js = "";

							
							fields_data = "<tr>" +
							"<td width=\"225px\">"+db_text_id+"</td> \n" +
							"<td width=\"225px\"><input type=\"text\" size=\"41\" name=\""+field_name+"\" "+js+"></td> \n" +
							"<td width=\"75px\">&nbsp;"+db_unit+"</td> \n" +
							"<td width=\"75px\">"+db_type+"</td>\n"+
							"</tr> \n";
						}
						else
						{
							reported_data = Util.encodeTag(reported_data);
						
							field_name = "field_id"+id_of_the_analysis+","+rs2.getString("af.id")+","+db_unit+","+rs2.getString("r.id")+","+db_type;
							
							if(db_type.equalsIgnoreCase("numeric"))
								js = "onblur=\"if(!this.value.match(/^[ \\+-]?\\d+(\\.\\d+)?$/)&&this.value!=\'\'){alert(\'Du kan kun indtaste tal (0-9)\');this.focus()}\"";
							else
								js = "";
							
							if(rs2.getString("r.locked").equals("F"))
							{
								fields_data = "<tr>" +
								"<td width=\"250px\">"+db_text_id+"</td> \n" +
								"<td width=\"250px\"><input type=\"text\" size=\"41\" name=\""+field_name+"\" value=\""+reported_data+"\" "+js+"></td> \n" +
								"<td width=\"100px\">&nbsp;"+db_unit+"</td> \n" +
								"<td width=\"75px\">"+db_type+"</td>\n"+
								"</tr> \n";	
							}
							else//the data has been locked
							{
								fields_data = "<tr>" +
								"<td width=\"250px\">"+db_text_id+"</td> \n" +
								"<td width=\"250px\"><input class=\"readonly\" type=\"text\" size=\"41\" name=\"field_id"+id_of_the_analysis+","+rs2.getString("af.id")+","+db_unit+","+rs2.getString("r.id")+","+db_type+"\" value=\""+reported_data+"\" readonly=\"readonly\"></td> \n" +
								"<td width=\"100px\">&nbsp;"+db_unit+"</td> \n" +
								"<td width=\"75px\">"+db_type+"</td>\n"+
								"</tr> \n";
							}
							
						}
						
						elements.add(fields_data);
					}											
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Get analysis data for display of result for an analysis in a sample
	 * 
	 * Here the name of the analysis is returned. And a list of all fields
	 * is generated and stored in the list 'elements'.
	 * 
	 * @param ana_id
	 * @param sample_id
	 * @return name of the analysis.
	 */
	public String getAnalysisData2Readonly(String ana_id, String sample_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		analysis_name = "";
		isActiveAnalysis = false;
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.id, analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = anaRS.getString("analysis.analysis_name");
					} 
					
					//					now get the information about the fields, result and sample
					String query = "SELECT s.locked," +
							" s.id," +
							" af.id," +
							" af.text_id," +
							" af.unit," +
							" r.status," +
							" r.reported_value," +
							" r.locked," +
							" r.id" +
							" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
							" AND af.analysis_version = sal.analysis_version)" +
							" JOIN sample s ON s.id = sal.sample_id)" +
							" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
							" WHERE s.id = "+sample_id+
							" AND af.analysis_id = "+ana_id+";";
					
//						"SELECT s.locked," +
//							" s.id," +
//							" af.id," +
//							" af.text_id," +
//							" af.unit," +
//							" r.reported_value," +
//							" r.locked," +
//							" r.id," +
//							" r.status" +
//					" FROM sample s, analysis_fields af, sample_analysis_link sal" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+sample_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())//get all the analysis data and run them through
					{
						//if(rs2.getString("a.active").equals("T"))//use only active analysis
						if(active_analysis.equals("T"))
						{
							//it is an active analysis
							this.isActiveAnalysis = true;
						}	
						if(rs2.isFirst())//only get the name once
						{
							//								analysis_name_to_return = Util.encodeTag(rs2.getString("a.analysis_name"));
							analysis_name_to_return = Util.encodeTag(analysis_name_to_return);
							this.analysis_name = analysis_name_to_return;
						}
						
						//now get all the field data information and present it in html format
						String reported_data = rs2.getString("r.reported_value");
						if (reported_data == null || reported_data.equals(""))
							reported_data = "--";
						else
							reported_data = Util.encodeTag(reported_data);
						
						String status_data = rs2.getString("r.status");
						if (status_data == null || status_data.equals(""))
						{
							status_data = "--";
						}
						else
						{
							if(status_data.equals("T"))
							{
								status_data =  Util.CHECK_MARK;
							}
							else
							{
								status_data = Util.X_MARK;
							}
						}
						
						fields_data = "<td width=\"250px\">"+Util.encodeTag(rs2.getString("af.text_id"))+"</td> \n" +
						"<td width=\"250px\">"+reported_data+"</td> \n" +
						"<td width=\"100px\">&nbsp;"+rs2.getString("af.unit")+"</td> \n"+
						"<td width=\"100px\">&nbsp;"+status_data+"</td> \n";
						
						//add the data to the list...
						elements.add(fields_data);
					}											
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Return the analysis name, and create a list of fields with result data.
	 * 
	 * @param ana_id
	 * @param sample_id
	 * @return
	 */
	public String getAnalysisDataAll(String ana_id, String sample_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		analysis_name = "";
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.id, analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = Util.encodeTag(anaRS.getString("analysis.analysis_name"));
						this.analysis_name = analysis_name_to_return;
					} 
					
					//now get the information about the fields, result and sample					
					String query = "SELECT s.locked," +
							" s.id," +
							" af.id," +
							" af.text_id," +
							" af.unit," +
							" r.status," +
							" r.reported_value," +
							" r.locked," +
							" r.id" +
							" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
							" AND af.analysis_version = sal.analysis_version)" +
							" JOIN sample s ON s.id = sal.sample_id)" +
							" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
							" WHERE s.id = "+sample_id+
							" AND af.analysis_id = "+ana_id+";";
													
						
						
//						"SELECT s.locked," +
//						" s.id," +
//						" af.id," +
//						" af.text_id," +
//						" af.unit," +
//						" r.status," +
//						" r.reported_value," +
//						" r.locked," +
//						" r.id" +
//					" FROM sample_analysis_link sal,analysis_fields af, sample s" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+sample_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())//get all the analysis data and run them through
					{						
						String reported_data = rs2.getString("r.reported_value");
						String locked_data = rs2.getString("r.locked");
						String text_data = rs2.getString("af.text_id");
						String unit_data = rs2.getString("af.unit");
						String status_data = rs2.getString("r.status");
						//now get all the field data information and present it in html format
						
						if (reported_data == null || reported_data.equals(""))
						{
							reported_data = "--";
						}
						else
						{
							reported_data = Util.encodeTag(reported_data);
						}
						
						if (locked_data == null || locked_data.equals(""))//this will happen if there is no result for a field!!!
						{
							locked_data = "--";
						}
						
						if (text_data == null || text_data.equals(""))
						{
							text_data = "--";
						}
						else
						{
							text_data = Util.encodeTag(rs2.getString("af.text_id"));
						}
						
						
						if (unit_data == null || unit_data.equals(""))
						{
							unit_data = "--";
						}
						
						if (status_data == null || status_data.equals(""))
						{
							status_data = "--";
						}
						else
						{
							if(status_data.equals("T"))
							{
								status_data =  Util.CHECK_MARK;
							}
							else
							{
								status_data = Util.X_MARK;
							}
						}
						
						fields_data = "<td width=\"250px\">"+text_data+"</td> \n" +
						"<td width=\"250px\">"+reported_data+"</td> \n" +
						"<td width=\"75px\">&nbsp;"+unit_data+"</td> \n" +
						"<td width=\"75px\">&nbsp;"+status_data+"</td> \n" +
						"<td width=\"50px\">&nbsp;"+locked_data+"</td> \n";
						
						//add the data to the list...
						elements.add(fields_data);
					}//end while
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Create a list of all analysis fields and returning the name of the analysis.
	 * 
	 * @param ana_id
	 * @param sample_id
	 * @return name of the analysis.
	 */
	public String getAnalysisDataAll_report(String ana_id, String sample_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		analysis_name = "";
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.id, analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = Util.encodeTag(anaRS.getString("analysis.analysis_name"));
						this.analysis_name = analysis_name_to_return;
					} 
					
					//					now get the information about the fields, result and sample					
					String query = "SELECT s.locked," +
					" s.id," +
					" af.id," +
					" af.text_id," +
					" af.unit," +
					" r.status," +
					" r.reported_value," +
					" r.locked," +
					" r.id" +
					" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
					" AND af.analysis_version = sal.analysis_version)" +
					" JOIN sample s ON s.id = sal.sample_id)" +
					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
					" WHERE s.id = "+sample_id+
					" AND af.analysis_id = "+ana_id+";";				
						
						
//						"SELECT s.locked," +
//						" s.id," +
//						" af.id," +
//						" af.text_id," +
//						" af.unit," +
//						" r.status," +
//						" r.reported_value," +
//						" r.locked," +
//						" r.id" +
//					" FROM sample s, analysis_fields af, sample_analysis_link sal" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+sample_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())//get all the analysis data and run them through
					{						
						String reported_data = rs2.getString("r.reported_value");
						String locked_data = rs2.getString("r.locked");
						String text_data = rs2.getString("af.text_id");
						String unit_data = rs2.getString("af.unit");
						String status_data = rs2.getString("r.status");
						//now get all the field data information and present it in html format
						
						if (reported_data == null || reported_data.equals(""))
						{
							reported_data = "--";
						}
						else
						{
							reported_data = Util.encodeTag(reported_data);
						}
						
						if (locked_data == null || locked_data.equals(""))//this will happen if there is no result for a field!!!
						{
							locked_data = "--";
						}
						
						if (text_data == null || text_data.equals(""))
						{
							text_data = "--";
						}
						else
						{
							text_data = Util.encodeTag(rs2.getString("af.text_id"));
						}
						
						
						if (unit_data == null || unit_data.equals(""))
						{
							unit_data = "--";
						}
						
						if (status_data == null || status_data.equals(""))
						{
							status_data = "--";
						}
						else
						{
							if(status_data.equals("T"))
							{
								status_data =  Util.OK_MARK;
							}
							else
							{
								status_data = Util.NOTOK_MARK;
							}
						}
						
						fields_data = "<td width=\"250px\">"+text_data+"</td> \n" +
						"<td width=\"250px\">"+reported_data+"</td> \n" +
						"<td width=\"75px\">&nbsp;"+unit_data+"</td> \n" +
						"<td width=\"75px\">&nbsp;"+status_data+"</td> \n" +
						"<td width=\"50px\">&nbsp;"+locked_data+"</td> \n";
						
						//add the data to the list...
						elements.add(fields_data);
					}//end while
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Get analysis data for a sample. The analysis fields is created as 
	 * locked, modifiable or empty.
	 * 
	 * @param ana_id
	 * @param sample_id
	 * @return
	 */
	public String getAnalysisDataAll_lock(String ana_id, String sample_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		analysis_name = "";
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.id, analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = Util.encodeTag(anaRS.getString("analysis.analysis_name"));
						this.analysis_name = analysis_name_to_return;
					} 
					
					//					now get the information about the fields, result and sample									
					String query = "SELECT s.locked," +
					" s.id," +
					" af.id," +
					" af.text_id," +
					" af.unit," +
					" r.status," +
					" r.reported_value," +
					" r.locked," +
					" r.id" +
					" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
					" AND af.analysis_version = sal.analysis_version)" +
					" JOIN sample s ON s.id = sal.sample_id)" +
					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
					" WHERE s.id = "+sample_id+
					" AND af.analysis_id = "+ana_id+";"; 
								
						
//						"SELECT s.locked," +
//						" s.id," +
//						" af.id," +
//						" af.text_id," +
//						" af.unit," +
//						" r.status," +
//						" r.reported_value," +
//						" r.locked," +
//						" r.id" +
//					" FROM sample s, analysis_fields af, sample_analysis_link sal" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+sample_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())//get all the analysis data and run them through
					{						
						String reported_data = rs2.getString("r.reported_value");
						String locked_data = rs2.getString("r.locked");
						String text_data = rs2.getString("af.text_id");
						String unit_data = rs2.getString("af.unit");
						String status_data = rs2.getString("r.status");
						//now get all the field data information and present it in html format
						
						if (reported_data == null || reported_data.equals(""))
						{
							reported_data = "--";
						}
						else
						{
							reported_data = Util.encodeTag(reported_data);
						}
						
						if (locked_data == null || locked_data.equals(""))//this will happen if there is no result for a field!!!
						{
							locked_data = "--";
						}
						
						if (text_data == null || text_data.equals(""))
						{
							text_data = "--";
						}
						else
						{
							text_data = Util.encodeTag(rs2.getString("af.text_id"));
						}
						
						if (unit_data == null || unit_data.equals(""))
						{
							unit_data = "--";
						}
						
						if (status_data == null || status_data.equals(""))
						{
							status_data = "--";
						}
						else
						{
							if(status_data.equals("T"))
							{
								status_data =  Util.CHECK_MARK;
							}
							else
							{
								status_data = Util.X_MARK;
							}
						}
						
						if(locked_data.equals("T"))
						{
							fields_data = "<td width=\"250px\">"+text_data+"</td> \n" +
							"<td width=\"250px\">"+reported_data+"</td> \n" +
							"<td width=\"100px\">&nbsp;"+unit_data+"</td> \n" +
							"<td width=\"75px\">&nbsp;"+status_data+"</td> \n" +
							"<td align=\"center\" width=\"50px\"><input type=\"checkbox\" name=\"resultid_"+rs2.getString("r.id")+"\" checked=\"checked\"></td> \n";
						}
						else if(locked_data.equals("F"))
						{
							fields_data = "<td width=\"250px\">"+text_data+"</td> \n" +
							"<td width=\"250px\">"+reported_data+"</td> \n" +
							"<td width=\"100px\">&nbsp;"+unit_data+"</td> \n" +
							"<td width=\"75px\">&nbsp;"+status_data+"</td> \n" +
							"<td align=\"center\" width=\"50px\"><input type=\"checkbox\" name=\"resultid_"+rs2.getString("r.id")+"\"></td> \n";
						}
						else if (locked_data.equals("--"))
						{
							fields_data = "<td width=\"250px\">"+text_data+"</td> \n" +
							"<td width=\"250px\">"+reported_data+"</td> \n" +
							"<td width=\"100px\">&nbsp;"+unit_data+"</td> \n" +
							"<td width=\"75px\">&nbsp;"+status_data+"</td> \n" +
							"<td align=\"center\" width=\"50px\">"+locked_data+"</td> \n";
						}
						
						//add the data to the list...
						elements.add(fields_data);
					}//end while
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Get analysis/sample data for display at the pdf page.
	 * 
	 * @param ana_id
	 * @param sample_id
	 * @return
	 */
	public String getAnalysisDataAll_PDF(String ana_id, String sample_id)
	{
		String analysis_name_to_return = "";
		String fields_data = "";
		analysis_name = "";
		
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
					
					elements.clear();
					
					String active_analysis = "";
					String id_of_the_analysis = "";
					
					//first get the information from the analysis table...
					String query_analysis = "SELECT analysis.id, analysis.analysis_id, analysis.analysis_name, analysis.active, analysis.version FROM analysis WHERE analysis.analysis_id = "+ana_id;
					
					ResultSet anaRS = stmt.executeQuery(query_analysis);
					
					if(anaRS.next())
					{
						active_analysis = anaRS.getString("analysis.active");
						id_of_the_analysis = anaRS.getString("analysis.analysis_id");
						analysis_name_to_return = Util.encodeTag(anaRS.getString("analysis.analysis_name"));
						this.analysis_name = analysis_name_to_return;
					} 
					
					//now get the information about the fields, result and sample					
					String query = "SELECT s.locked," +
					" s.id," +
					" af.id," +
					" af.text_id," +
					" af.unit," +
					" r.reported_value," +
					" r.locked," +
					" r.id" +
					" FROM ((sample_analysis_link sal JOIN analysis_fields af ON af.analysis_id = sal.analysis_id" +
					" AND af.analysis_version = sal.analysis_version)" +
					" JOIN sample s ON s.id = sal.sample_id)" +
					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
					" WHERE s.id = "+sample_id+
					" AND af.analysis_id = "+ana_id+";"; 
												
//						"SELECT s.locked," +
//						" s.id," +
//						" af.id," +
//						" af.text_id," +
//						" af.unit," +
//						" r.reported_value," +
//						" r.locked," +
//						" r.id" +
//					" FROM sample s, analysis_fields af, sample_analysis_link sal" +
//					" LEFT OUTER JOIN result r ON s.id = r.sample_id AND af.id = r.analysis_field_id" +
//					" WHERE s.id = sal.sample_id" +
//					" AND af.analysis_id = sal.analysis_id" +
//					" AND af.analysis_version = sal.analysis_version" +
//					" AND s.id = "+sample_id+
//					" AND af.analysis_id = "+ana_id;
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					while(rs2.next())//get all the analysis data and run them through
					{						
						String reported_data = rs2.getString("r.reported_value");
						String locked_data = rs2.getString("r.locked");
						String text_data = rs2.getString("af.text_id");
						String unit_data = rs2.getString("af.unit");
						//now get all the field data information and present it in html format
						
						if (reported_data == null || reported_data.equals(""))
						{
							reported_data = "--";
						}
						else
						{
							reported_data = Util.encodeTag(reported_data);
						}
						
						if (locked_data == null || locked_data.equals(""))
						{
							locked_data = "--";
						}
						
						if (text_data == null || text_data.equals(""))
						{
							text_data = "--";
						}
						else
						{
							text_data = Util.encodeTag(rs2.getString("af.text_id"));
						}
						
						
						if (unit_data == null || unit_data.equals(""))
						{
							unit_data = "--";
						}
						
						fields_data = reported_data+"|"+unit_data+"|"+locked_data;
						
						//add the data to the list...
						elements.add(fields_data);
					}//end while
				}
				con.close();
				
				return analysis_name_to_return;
			}
			return null;
			
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
	}
	
	/**
	 * Modify an analysis. 
	 * 
	 * Remove or add fields, edit values of min max values etc...
	 * 
	 * 
	 * @return code int. The status of the update, can obtain the following values:
	 * 1: Error in updating remark of the analysis, the update has been aborted..
	 * 2: The remark was updated successfully. The analysis is stil in the same version,
	 * 		and history has been updated.
	 * 3: error in updating both fields and analysis to a new version
	 * 4: success with updating to a new verison with new fields and new analysis and history
	 * 5: No update has been performed... 
	 */
	public int performModify()
	{
		boolean isUpdate_analysis = false;
		boolean isUpdate_fields = false;
		boolean isNewFields = false;
		boolean isRemoveFields = false;
		
		//initialize the maps
		value_list2 = new HashMap(value_list);
		value_list_hidden = new HashMap(value_list);
		value_list_remove = new HashMap(value_list);
		value_list_new = new HashMap(value_list);
		
		//clean the maps
		value_list2 = cleanMap(value_list2);
		value_list_hidden = cleanMap_hidden(value_list_hidden);
		value_list_remove = cleanMap_remove(value_list_remove);
		value_list_new = cleanMap_new(value_list_new);
		
		/*
		 * compare the old values with the new values.
		 * The update will only be performed if there is a difference..
		 */
		
		//Remove fields that is to be removed
		for (Iterator iter = value_list_remove.entrySet().iterator(); iter.hasNext();) {					
			Map.Entry e = (Map.Entry) iter.next();
			
			String key = (String) e.getKey();
			
			String number = key.substring(key.indexOf("_")+1);
						
			if(value_list2.containsKey(TEXT_ID_KEY+number)) 
			{
				value_list2.remove(TEXT_ID_KEY+number);
				value_list_hidden.remove(TEXT_ID_KEY+number+"_hidden");
				
				isUpdate_fields = true;
				isRemoveFields = true;
			}
			
			if(value_list2.containsKey(RESULT_MIN_KEY+number)) 
			{
				value_list2.remove(RESULT_MIN_KEY+number);
				value_list_hidden.remove(RESULT_MIN_KEY+number+"_hidden");
				
				isUpdate_fields = true;
				isRemoveFields = true;
			}
			
			if(value_list2.containsKey(RESULT_MAX_KEY+number)) 
			{
				value_list2.remove(RESULT_MAX_KEY+number);
				value_list_hidden.remove((RESULT_MAX_KEY+number+"_hidden"));
				
				isUpdate_fields = true;
				isRemoveFields = true;
			}
			
			if(value_list2.containsKey(RESULT_TYPE_KEY+number)) 
			{
				value_list2.remove(RESULT_TYPE_KEY+number);
				value_list_hidden.remove((RESULT_TYPE_KEY+number+"_hidden"));
				
				isUpdate_fields = true;
				isRemoveFields = true;
			}
			
			if(value_list2.containsKey(UNIT_KEY+number))
			{
				value_list2.remove(UNIT_KEY+number);
				value_list_hidden.remove(UNIT_KEY+number+"_hidden");
				
				isUpdate_fields = true;
				isRemoveFields = true;
			}
		}
		
		//add new fields if there is any..
		if((!new_text_id1.equals("") && new_text_id1 != null) ||
				(!new_text_id2.equals("") && new_text_id2 != null) ||
				(!new_text_id3.equals("") && new_text_id3 != null) ||
				(!new_text_id4.equals("") && new_text_id4 != null) ||
				(!new_text_id5.equals("") && new_text_id5 != null) ||
				(!new_text_id6.equals("") && new_text_id6 != null) ||
				(!new_text_id7.equals("") && new_text_id7 != null) ||
				(!new_text_id8.equals("") && new_text_id8 != null) ||
				(!new_text_id9.equals("") && new_text_id9 != null) ||
				(!new_text_id10.equals("") && new_text_id10 != null))
		{
			isUpdate_fields = true;
			isNewFields = true;
		}
		
		//First check the description
		if(!(remark.trim()).equals(original_remark.trim()))
		{
			//register the new remark...
			isUpdate_analysis = true;
		}
		
		//now run through the two trees comparing values....
		for (Iterator iter = value_list2.entrySet().iterator(); iter.hasNext();) {						
			Map.Entry e = (Map.Entry) iter.next();
			
			String key = (String) e.getKey();
			String[] value = (String[]) e.getValue();
			
			String hidden_key = key+"_hidden";
			String[] original_value = (String[]) value_list_hidden.get(hidden_key);
			
			//compare the two values...
			if(!original_value[0].equals(value[0]))
			{
				//if one of the values is not the same as the previous ones, register all again
				isUpdate_fields = true;
				break;
			}
		}
		
		//If only the description of the analysis has changed, this does not
		//provoke updating the version
		//Here the fields are not updated.. only the remark and other info fields.
		if(isUpdate_analysis==true && isUpdate_fields==false)
		{
			String change = "DESCRIPTION CHANGED: "+reason_for_change;
			
			//Here update the remark on the analysis....
			if(updateAnalysis(analysis_id, remark, version, change, user))
			{
				return 2;
			}
			else//..remark could not be updated...
				return 1;
		}
		else if(isUpdate_fields==true)//update to a new versin of the analysis, create new fields
		{
			//first create new verson of the analysis....
			//second create the modified fields for the new version
			//set the old version to not active
			//third update the history
			
			//here the modification of existing fields is performed
			//this includes modifiying existing fields or removing fields.
			boolean check = updateAnalysisVersion(analysis_id, this.analysis_name, user, remark, version, reason_for_change, value_list2);
			
			if(check)
			{
				if(isNewFields)//if there is new fields to enter then do so...
				{
					addNewFields(this.version);
				}

				//Create the standard intro to the reason of change
				String change = "";
				
				if(isNewFields && !isRemoveFields)//new fields added
				{
					change = "NEW ANALYSIS FIELDS ADDED. ";
				}
				
				if(isRemoveFields && !isNewFields)//existing fields removed.
				{
					change = "EXISTING FIELDS REMOVED. ";
				}
				
				if(isRemoveFields && isNewFields)//existing fields removed and new added
				{
					change = "EXISTING FIELDS REMOVED AND NEW ADDED. ";
				}
				
				if(!isRemoveFields && !isNewFields)//existing values in existing fields modified, no new added and no removed.
				{
					change = "VALUES IN EXISTING FIELDS MODIFIED. ";
				}
				
				change = change + reason_for_change;
				
				//insert data into the history table
				boolean check2 = insertUpdatedHistory(analysis_id, Util.double_q(change), this.version, user);
								
				//update was ok now set the new analysis to active 
				activateNewestVersion(analysis_id, this.version);
				return 4;
			}
			else
			{
				return 3;				
			}
		}
		else if(isUpdate_analysis==false || isUpdate_fields==false)//nothing to update, return code
		{
			//do not perform any update....return some code...
			return 5;
		}
		else
			return 5;
	}	
	
	
	/**
	 * Add new fields to an existing analysis..
	 * 
	 * @param new_version
	 * @return true/false of the operation
	 */
	public boolean addNewFields(String new_version)
	{
		// at least one field has to be created...
		if((!new_text_id1.equals("") && new_text_id1 != null) ||
				(!new_text_id2.equals("") && new_text_id2 != null) ||
				(!new_text_id3.equals("") && new_text_id3 != null) ||
				(!new_text_id4.equals("") && new_text_id4 != null) ||
				(!new_text_id5.equals("") && new_text_id5 != null) ||
				(!new_text_id6.equals("") && new_text_id6 != null) ||
				(!new_text_id7.equals("") && new_text_id7 != null) ||
				(!new_text_id8.equals("") && new_text_id8 != null) ||
				(!new_text_id9.equals("") && new_text_id9 != null) ||
				(!new_text_id10.equals("") && new_text_id10 != null))
		{
			errorCode = 1;//there is something to register
			
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
						
						/*Insert the user id in the link table between user and
						 *user group.*/
						stmt.clearBatch();
						String insertGroup = null;
						
						if((!new_text_id1.equals("") && new_text_id1 != null))
						{
							//validate the min max values:
							if(new_result_type1.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min1 != null && !new_result_min1.equals(""))
								{
									new_result_min1 = Util.formatNumericResult(new_result_min1);
									
									if(!Util.isValidNumber(new_result_min1))
									{
										new_result_min1 = "0.0";
									}
								}
								if(new_result_max1 != null && !new_result_max1.equals(""))//there is a max value
								{
									new_result_max1 = Util.formatNumericResult(new_result_max1);
									
									if(!Util.isValidNumber(new_result_max1))
									{
										new_result_max1 = "0.0";
									}
								}
							}
							else if (new_result_type1.equalsIgnoreCase("text"))
							{
							}
							
							//create the element for use this line in batch or not
							if(this.new_use_spec1.equalsIgnoreCase("on"))
								this.new_use_spec1 = "T";
							else
								this.new_use_spec1 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max," +
									" unit, created_by, created_date, analysis_version, result_type, result_for_spec)" +
									" VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id1+"'," +
									" '"+new_result_min1+"', '"+new_result_max1+"', '"+new_unit1+"', '"+user+"'," +
									" '"+Util.getDate()+"', "+new_version+", '"+new_result_type1+"', '"+new_use_spec1+"');";
							stmt.addBatch(insertGroup);
						}
						
						if((!new_text_id2.equals("") && new_text_id2 != null))
						{
//							validate the min max values:
							if(new_result_type2.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min2 != null && !new_result_min2.equals(""))
								{
									new_result_min2 = Util.formatNumericResult(new_result_min2);
									
									if(!Util.isValidNumber(new_result_min2))
									{
										new_result_min2 = "0.0";
									}
								}
								if(new_result_max2 != null && !new_result_max2.equals(""))//there is a max value
								{
									new_result_max2 = Util.formatNumericResult(new_result_max2);
									
									if(!Util.isValidNumber(new_result_max2))
									{
										new_result_max2 = "0.0";
									}
								}
							}
							else if (new_result_type2.equalsIgnoreCase("text"))
							{
							}
							
							//create the element for use this line in batch or not
							if(this.new_use_spec2.equalsIgnoreCase("on"))
								this.new_use_spec2 = "T";
							else
								this.new_use_spec2 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id2+"', '"+new_result_min2+"', '"+new_result_max2+"', '"+new_unit2+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type2+"', '"+new_use_spec2+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id3.equals("") && new_text_id3 != null))
						{
//							validate the min max values:
							if(new_result_type3.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min3 != null && !new_result_min3.equals(""))
								{
									new_result_min3 = Util.formatNumericResult(new_result_min3);
									
									if(!Util.isValidNumber(new_result_min3))
									{
										new_result_min3 = "0.0";
									}
								}
								if(new_result_max3 != null && !new_result_max3.equals(""))//there is a max value
								{
									new_result_max3 = Util.formatNumericResult(new_result_max3);
									
									if(!Util.isValidNumber(new_result_max3))
									{
										new_result_max3 = "0.0";
									}
								}
							}
							else if (new_result_type3.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec3.equalsIgnoreCase("on"))
								this.new_use_spec3 = "T";
							else
								this.new_use_spec3 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id3+"', '"+new_result_min3+"', '"+new_result_max3+"', '"+new_unit3+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type3+"', '"+new_use_spec3+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id4.equals("") && new_text_id4 != null))
						{
//							validate the min max values:
							if(new_result_type4.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min4 != null && !new_result_min4.equals(""))
								{
									new_result_min4 = Util.formatNumericResult(new_result_min4);
									
									if(!Util.isValidNumber(new_result_min4))
									{
										new_result_min4 = "0.0";
									}
								}
								if(new_result_max4 != null && !new_result_max4.equals(""))//there is a max value
								{
									new_result_max4 = Util.formatNumericResult(new_result_max4);
									
									if(!Util.isValidNumber(new_result_max4))
									{
										new_result_max4 = "0.0";
									}
								}
							}
							else if (new_result_type4.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec4.equalsIgnoreCase("on"))
								this.new_use_spec4 = "T";
							else
								this.new_use_spec4 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id4+"', '"+new_result_min4+"', '"+new_result_max4+"', '"+new_unit4+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type4+"', '"+new_use_spec4+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id5.equals("") && new_text_id5 != null))
						{
//							validate the min max values:
							if(new_result_type5.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min5 != null && !new_result_min5.equals(""))
								{
									new_result_min5 = Util.formatNumericResult(new_result_min5);
									
									if(!Util.isValidNumber(new_result_min5))
									{
										new_result_min5 = "0.0";
									}
								}
								if(new_result_max5 != null && !new_result_max5.equals(""))//there is a max value
								{
									new_result_max5 = Util.formatNumericResult(new_result_max5);
									
									if(!Util.isValidNumber(new_result_max5))
									{
										new_result_max5 = "0.0";
									}
								}
							}
							else if (new_result_type5.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec5.equalsIgnoreCase("on"))
								this.new_use_spec5 = "T";
							else
								this.new_use_spec5 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id5+"', '"+new_result_min5+"', '"+new_result_max5+"', '"+new_unit5+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type5+"', '"+new_use_spec5+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id6.equals("") && new_text_id6 != null))
						{
//							validate the min max values:
							if(new_result_type6.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min6 != null && !new_result_min6.equals(""))
								{
									new_result_min6 = Util.formatNumericResult(new_result_min6);
									
									if(!Util.isValidNumber(new_result_min6))
									{
										new_result_min6 = "0.0";
									}
								}
								if(new_result_max6 != null && !new_result_max6.equals(""))//there is a max value
								{
									new_result_max6 = Util.formatNumericResult(new_result_max6);
									
									if(!Util.isValidNumber(new_result_max6))
									{
										new_result_max6 = "0.0";
									}
								}
							}
							else if (new_result_type6.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec6.equalsIgnoreCase("on"))
								this.new_use_spec6 = "T";
							else
								this.new_use_spec6 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id6+"', '"+new_result_min6+"', '"+new_result_max6+"', '"+new_unit6+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type6+"', '"+new_use_spec6+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id7.equals("") && new_text_id7 != null))
						{
//							validate the min max values:
							if(new_result_type7.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min7 != null && !new_result_min7.equals(""))
								{
									new_result_min7 = Util.formatNumericResult(new_result_min7);
									
									if(!Util.isValidNumber(new_result_min7))
									{
										new_result_min7 = "0.0";
									}
								}
								if(new_result_max7 != null && !new_result_max7.equals(""))//there is a max value
								{
									new_result_max7 = Util.formatNumericResult(new_result_max7);
									
									if(!Util.isValidNumber(new_result_max7))
									{
										new_result_max7 = "0.0";
									}
								}
							}
							else if (new_result_type7.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec7.equalsIgnoreCase("on"))
								this.new_use_spec7 = "T";
							else
								this.new_use_spec7 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id7+"', '"+new_result_min7+"', '"+new_result_max7+"', '"+new_unit7+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type7+"', '"+new_use_spec7+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id8.equals("") && new_text_id8 != null))
						{
//							validate the min max values:
							if(new_result_type8.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min8 != null && !new_result_min8.equals(""))
								{
									new_result_min8 = Util.formatNumericResult(new_result_min8);
									
									if(!Util.isValidNumber(new_result_min8))
									{
										new_result_min8 = "0.0";
									}
								}
								if(result_max8 != null && !result_max8.equals(""))//there is a max value
								{
									new_result_max8 = Util.formatNumericResult(new_result_max8);
									
									if(!Util.isValidNumber(new_result_max8))
									{
										new_result_max8 = "0.0";
									}
								}
							}
							else if (new_result_type8.equalsIgnoreCase("text"))
							{
							}

							//create the element for use this line in batch or not
							if(this.new_use_spec8.equalsIgnoreCase("on"))
								this.new_use_spec8 = "T";
							else
								this.new_use_spec8 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id8+"', '"+new_result_min8+"', '"+new_result_max8+"', '"+new_unit8+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type8+"', '"+new_use_spec8+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id9.equals("") && new_text_id9 != null))
						{
//							validate the min max values:
							if(new_result_type9.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min9 != null && !new_result_min9.equals(""))
								{
									new_result_min9 = Util.formatNumericResult(new_result_min9);
									
									if(!Util.isValidNumber(new_result_min9))
									{
										new_result_min9 = "0.0";
									}
								}
								if(new_result_max9 != null && !new_result_max9.equals(""))//there is a max value
								{
									new_result_max9 = Util.formatNumericResult(new_result_max9);
									
									if(!Util.isValidNumber(new_result_max9))
									{
										new_result_max9 = "0.0";
									}
								}
							}
							else if (new_result_type9.equalsIgnoreCase("text"))
							{
							}
							
//							create the element for use this line in batch or not
							if(this.new_use_spec9.equalsIgnoreCase("on"))
								this.new_use_spec9 = "T";
							else
								this.new_use_spec9 = "F";						
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id9+"', '"+new_result_min9+"', '"+new_result_max9+"', '"+new_unit9+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type9+"', '"+new_use_spec9+"');";

							stmt.addBatch(insertGroup);
						}  
						
						if((!new_text_id10.equals("") && new_text_id10 != null))
						{
//							validate the min max values:
							if(new_result_type10.equalsIgnoreCase("numeric"))
							{
								//if there is a an entry in either min or max value validate to be a double
								if(new_result_min10 != null && !new_result_min10.equals(""))
								{
									new_result_min10 = Util.formatNumericResult(new_result_min10);
									
									if(!Util.isValidNumber(new_result_min10))
									{
										new_result_min10 = "0.0";
									}
								}
								if(new_result_max10 != null && !new_result_max10.equals(""))//there is a max value
								{
									new_result_max10 = Util.formatNumericResult(new_result_max10);
									
									if(!Util.isValidNumber(new_result_max10))
									{
										new_result_max10 = "0.0";
									}
								}
							}
							else if (new_result_type10.equalsIgnoreCase("text"))
							{
							}
							
//							create the element for use this line in batch or not
							if(this.new_use_spec10.equalsIgnoreCase("on"))
								this.new_use_spec10 = "T";
							else
								this.new_use_spec10 = "F";
							
							insertGroup = "INSERT INTO analysis_fields (analysis_id, text_id, result_min, result_max, unit, created_by, created_date, analysis_version, result_type, result_for_spec) VALUES("+Integer.parseInt(analysis_id)+", '"+new_text_id10+"', '"+new_result_min10+"', '"+new_result_max10+"', '"+new_unit10+"', '"+user+"', '"+Util.getDate()+"', "+new_version+", '"+new_result_type10+"', '"+new_use_spec10+"');";

							stmt.addBatch(insertGroup);
						}  
						
						//perform the update..
						try {
							updateCounts = stmt.executeBatch();
						} catch (Exception e1) {
							e1.printStackTrace();
							con.rollback();
							con.close();
							errorCode = 2;//the registration failed..
							return false;
						}
						
						con.commit();//commit the changes to the db.
					}
					con.close();
					
					return true;
				}
			}//end of try
			
			catch (SQLException e)
			{
				e.printStackTrace();
				errorCode = 2;//the registration failed..
				return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				errorCode = 2;//the registration failed..
				return false;
			}
			
			return false;
		}
		else
		{
			errorCode = 3;//No data 
			return false;
		}
	}
	
	/**
	 * Get the max id number in an analysis.
	 * 
	 * @param ana_id
	 * @return the max number for the analysis.
	 */
	public int getCounter(String ana_id)
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
					
					//Set to the next version
					int number = 0;

					String query = "SELECT MAX(id) FROM analysis_fields WHERE analysis_id = "+ana_id;

					ResultSet rs = stmt.executeQuery(query);
					
					if(rs.next())
					{
						number = rs.getInt(1);
					}
					else 
					{
						number = Integer.MAX_VALUE;
					}
					
					con.close();
					return number;
						
					
				}
				con.close();
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return Integer.MAX_VALUE;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return Integer.MAX_VALUE;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Integer.MAX_VALUE;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * 
	 * Perform some update actions on an analysis.
	 * 
	 * Here the next version of a given analysis is created.
	 * 
	 * @param a_id
	 * @param ana_name
	 * @param user
	 * @param remark
	 * @param version
	 * @param reason
	 * 
	 * @return true false of the operation
	 */
	public boolean updateAnalysisVersion(String a_id, String ana_name, String user, String remark, String version, String reason, Map list)
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
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					
					//Set the correct version. this is not nessecerily the next, but must be
					//the highest verison in the db + 1
					// first find the highest version from the db:
					String find_version = "SELECT MAX(analysis.version) FROM analysis WHERE analysis.analysis_id = "+a_id;
					
					int version_number = 0;
					
					ResultSet rs_ver = stmt.executeQuery(find_version);
					if(rs_ver.next())
						version_number = rs_ver.getInt(1);
					
					version_number++;
					this.version = ""+version_number;
					
					//register the values received from the client
					String create_analysis = "INSERT INTO analysis" +
					" (analysis_id, analysis_name, created_by, created_date, remark, active, version)" +
					" VALUES("+a_id+", '"+ana_name+"','"+user+"','"+Util.getDate()+"', '"+Util.double_q(remark)+"', 'F', "+this.version+")";
					
					stmt.executeUpdate(create_analysis);
					
					//create fields
					if(list.size()>0)
					{
						boolean check = createAnalysisFields(a_id, this.version, user, list);
					}
					
					con.commit();//commit the changes to the db.
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
	 * Create fields in an analysis.
	 * 
	 * 
	 * @param a_id
	 * @param a_version
	 * @param user
	 * @param fields
	 * @return
	 */
	public boolean createAnalysisFields(String a_id, String a_version, String user, Map fields)
	{
		/*
		 * Firste create a list holding all the insert statements for the fields.
		 */
		int i;
		String number = "";
		String use_spec = "";
		Vector list_of_fields = new Vector();
		list_of_fields.clear();
		
		String[] text_id_value;
		String[] result_min_value;
		String[] result_max_value;
		String[] result_type_value;
		String[] spec_value;
		String[] unit_value;
		String insert_sql = "";
		
		for(i = 1; i<=getCounter(a_id); i++)
		{
			insert_sql = "";
			number = String.valueOf(i);
			
			//if there is a text id field for a field the other elements must be in the map.
			if(fields.containsKey(TEXT_ID_KEY+number))
			{
				text_id_value = (String[]) fields.get(TEXT_ID_KEY+number);
				result_min_value = (String[]) fields.get(RESULT_MIN_KEY+number);
				result_max_value = (String[]) fields.get(RESULT_MAX_KEY+number);
				result_type_value = (String[]) fields.get(RESULT_TYPE_KEY+number);
				spec_value = (String[]) fields.get(SPEC_ID_KEY+number);
				unit_value = (String[]) fields.get(UNIT_KEY+number);
								
//				validate the min max values:
				if(result_type_value[0].equalsIgnoreCase("numeric"))
				{
					//if there is a an entry in either min or max value validate to be a double
					if(result_min_value[0] != null && !result_min_value[0].equals("") && !result_min_value[0].equals("--"))
					{
						result_min_value[0] = Util.formatNumericResult(result_min_value[0]);
						
						if(!Util.isValidNumber(result_min_value[0]))
						{
							result_min_value[0] = "0.0";
						}
					}
					else
					{
						result_min_value[0] = "";
					}
					if(result_max_value[0] != null && !result_max_value[0].equals("") && !result_max_value[0].equals("--"))//there is a max value
					{
						result_max_value[0] = Util.formatNumericResult(result_max_value[0]);
						
						if(!Util.isValidNumber(result_max_value[0]))
						{
							result_max_value[0] = "0.0";
						}
					}
					else
					{
						result_max_value[0] = "";
					}
				}
				else if (result_type_value[0].equalsIgnoreCase("text"))
				{
				}
				
				use_spec = "";
								
				if(spec_value == null)
				{
					use_spec = "F";
				}
				else
				{
					if(spec_value[0].equals("on"))
						use_spec = "T";
					else
						use_spec = "F";
				}
				
				insert_sql = "INSERT INTO analysis_fields (analysis_id, analysis_version, text_id, result_min, result_max, unit, created_by, created_date, result_type, result_for_spec)" +
				" VALUES("+a_id+", "+a_version+", '"+text_id_value[0]+"', '"+result_min_value[0]+"', '"+result_max_value[0]+"', '"+unit_value[0]+"', '"+user+"', '"+Util.getDate()+"', '"+result_type_value[0]+"', '"+use_spec+"');";
				
				list_of_fields.add(insert_sql);
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
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					stmt.clearBatch();
					
					for(int n=0; n<list_of_fields.size(); n++)
					{
						String field_to_add = (String) list_of_fields.get(n);
						stmt.addBatch(field_to_add);
					}
					
					//insert all the fields
					
					try {
						stmt.executeBatch();
						con.commit();
						
					} catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
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
	 * Insert history when performing an update.
	 * 
	 * @param id
	 * @param remark_text
	 * @param a_version
	 * @param a_user
	 * 
	 * @return true/false.
	 */
	public boolean insertUpdatedHistory(String id, String remark_text, String a_version, String a_user)
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
					
					String query_fields = "SELECT analysis_fields.id, analysis_fields.analysis_id, analysis_fields.text_id, analysis_fields.result_min, analysis_fields.result_max, analysis_fields.unit, analysis_fields.result_type" +
					" FROM analysis_fields,  analysis" +
					" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
					" AND analysis_fields.analysis_version = analysis.version" +
					" AND analysis_fields.analysis_id = "+analysis_id+
					" AND analysis_fields.analysis_version ="+a_version;
										
					ResultSet rs2 = stmt.executeQuery(query_fields);
					/*
					 * The data for the fields is inserted into a text field
					 * in the database.
					 * Each row of data from the analysis_fields is inserted
					 * row 1: data|data|data|data||
					 * row 2; data, ..... |
					 * 
					 * the data in each row is separeted by a single |
					 * and each row is separeted by double || 
					 */
					
					String fields_data = "";
					
					while(rs2.next())
					{
						fields_data = fields_data.concat(rs2.getString("analysis_fields.id"));
						fields_data = fields_data.concat("|");
						
						fields_data = fields_data.concat(rs2.getString("analysis_fields.text_id"));
						fields_data = fields_data.concat("|");
						
						String res_min = rs2.getString("analysis_fields.result_min");
						if(res_min == null || res_min.equals(""))
							res_min = "--";
						fields_data = fields_data.concat(res_min);
						fields_data = fields_data.concat("|");
						
						String res_max = rs2.getString("analysis_fields.result_max");
						if(res_max == null || res_max.equals(""))
							res_max = "--";
						
						fields_data = fields_data.concat(res_max);
						fields_data = fields_data.concat("|");						
						
						fields_data = fields_data.concat(rs2.getString("analysis_fields.result_type"));
						fields_data = fields_data.concat("|");

						fields_data = fields_data.concat(rs2.getString("analysis_fields.unit"));
						fields_data = fields_data.concat("||");
					}
					
					String insert = "INSERT INTO analysis_history (analysis_id, remark, analysis_version, changed_date, changed_by, analysis_fields) " +
					" VALUES("+id+", '"+Util.double_q(remark_text)+"', "+a_version+", '"+Util.getDate()+"', '"+a_user+"', '"+fields_data+"');";
					
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
	 * This method updates the remark of a specific analysis , specific version.
	 * and updates the analysis history.
	 * 
	 * @param a_id
	 * @param remark
	 * @param version
	 * @return
	 */
	public boolean updateAnalysis(String a_id, String remark, String version, String reason, String user)
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
					
					stmt.clearBatch();
					
					String update= "UPDATE analysis" +
					" SET remark = '"+Util.double_q(remark)+"'" +
					" WHERE analysis_id = "+a_id+
					" AND version = "+version+";";
								
					stmt.addBatch(update);
					
					String insert = "INSERT INTO analysis_history (analysis_id, remark, analysis_version, changed_date, changed_by, analysis_fields) " +
					" VALUES("+a_id+", '"+Util.double_q(reason)+"', "+version+", '"+Util.getDate()+"', '"+user+"', '---');";
					
					stmt.addBatch(insert);
					
					try
					{
						stmt.executeBatch();
					}
					catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return false;
					}
				}
				con.commit();
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
	 * Here the newest (max version number) of the analysis is set to active.
	 * All other versions is set as inactive.
	 * 
	 * 
	 * @param a_id
	 * @param version
	 * @return
	 */
	public boolean activateNewestVersion(String a_id, String version)
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
					
					stmt.clearBatch();
					
					String update= "UPDATE analysis" +
					" SET active = 'F'" +
					" WHERE analysis_id = "+a_id+
					" AND version <> "+version+";";
										
					stmt.addBatch(update);
					
					update= "UPDATE analysis" +
					" SET active = 'T'" +
					" WHERE analysis_id = "+a_id+
					" AND version = "+version+";";
					
					stmt.addBatch(update);
					
					try
					{
						stmt.executeBatch();
					}
					catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return false;
					}
				}
				con.commit();
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
	 * Cleans the map of all entries not relevant for analysis fields
	 * @param map
	 * @return The cleaned map
	 */
	public Map cleanMap(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if (element.length()<4)//the string has to at least 4 char long
			{
				iter.remove();
			}
			else
			{
				if((element.indexOf("hidden") == -1) && (element.indexOf("new") == -1))//if the element is hidden - remove
				{
					if(!(element.substring(0, 4)).equals("unit"))
					{
						if (element.length()<7)//next the length has to be at least 6 char
						{
							iter.remove();
						}
						else
						{
							if(!(element.substring(0, 7)).equals("text_id"))
							{
								if (element.length()<8)//next the length has to be at least 8 char
								{
									iter.remove();
								}
								else
								{
									if(!(element.substring(0, 8)).equals("use_spec"))
									{
										if (element.length()<11)//next the lenght has to be at least 9 char..
										{
											iter.remove();
										}
										else
										{
											if(!(element.substring(0, 10)).equals("result_min"))
											{
												if(!(element.substring(0, 10)).equals("result_max"))
												{
													if(!(element.substring(0, 11)).equals("result_type"))
													{
														iter.remove();
													}
												}
											}
										}
									}
								}
							}//end text_id
						}
					}
				}
				else//remove hidden elements.
				{
					iter.remove();
				}
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 * Cleans the map of all entries not relevant for analysis fields THIS METHOD HOLDING ORIGINAL VALUES!!
	 * @param map
	 * @return The cleaned map
	 */
	public Map cleanMap_hidden(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if (element.length()<7)
			{
				iter.remove();
			}
			else
			{
				if(element.indexOf("hidden") == -1)//remove items that is not set to hidden...
				{
					iter.remove();
				}
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 * Cleans the map, keeping only elements that has to be removed...
	 * @param map
	 * @return
	 */
	public Map cleanMap_remove(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if (element.length()<7)
			{
				iter.remove();
			}
			else
			{
				if(element.indexOf("remove") == -1)//remove items that is not set to hidden...
				{
					iter.remove();
				}
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 * Create the list of the new analysis_fields
	 * @param map
	 * @return
	 */
	public Map cleanMap_new(Map map)
	{	
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if (element.length()<7)
			{
				iter.remove();
			}
			else
			{
				if(element.indexOf("new_") == -1)//remove items that is not set to hidden...
				{
					iter.remove();
				}
			}
		}
		
		Map values = new HashMap(map);
		
		return values;
	}
	
	/**
	 * @return Returns the analysis_id.
	 */
	public String getAnalysis_id() {
		return analysis_id;
	}
	/**
	 * @param analysis_id The analysis_id to set.
	 */
	public void setAnalysis_id(String analysis_id) {
		this.analysis_id = analysis_id;
	}
	/**
	 * @return Returns the analysis_name.
	 */
	public String getAnalysis_name() {
		return analysis_name;
	}
	/**
	 * @param analysis_name The analysis_name to set.
	 */
	public void setAnalysis_name(String analysis_name) {
		this.analysis_name = analysis_name;
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
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return Returns the result_max1.
	 */
	public String getResult_max1() {
		return result_max1;
	}
	/**
	 * @param result_max1 The result_max1 to set.
	 */
	public void setResult_max1(String result_max1) {
		this.result_max1 = result_max1;
	}
	/**
	 * @return Returns the result_max10.
	 */
	public String getResult_max10() {
		return result_max10;
	}
	/**
	 * @param result_max10 The result_max10 to set.
	 */
	public void setResult_max10(String result_max10) {
		this.result_max10 = result_max10;
	}
	/**
	 * @return Returns the result_max2.
	 */
	public String getResult_max2() {
		return result_max2;
	}
	/**
	 * @param result_max2 The result_max2 to set.
	 */
	public void setResult_max2(String result_max2) {
		this.result_max2 = result_max2;
	}
	/**
	 * @return Returns the result_max3.
	 */
	public String getResult_max3() {
		return result_max3;
	}
	/**
	 * @param result_max3 The result_max3 to set.
	 */
	public void setResult_max3(String result_max3) {
		this.result_max3 = result_max3;
	}
	/**
	 * @return Returns the result_max4.
	 */
	public String getResult_max4() {
		return result_max4;
	}
	/**
	 * @param result_max4 The result_max4 to set.
	 */
	public void setResult_max4(String result_max4) {
		this.result_max4 = result_max4;
	}
	/**
	 * @return Returns the result_max5.
	 */
	public String getResult_max5() {
		return result_max5;
	}
	/**
	 * @param result_max5 The result_max5 to set.
	 */
	public void setResult_max5(String result_max5) {
		this.result_max5 = result_max5;
	}
	/**
	 * @return Returns the result_max6.
	 */
	public String getResult_max6() {
		return result_max6;
	}
	/**
	 * @param result_max6 The result_max6 to set.
	 */
	public void setResult_max6(String result_max6) {
		this.result_max6 = result_max6;
	}
	/**
	 * @return Returns the result_max7.
	 */
	public String getResult_max7() {
		return result_max7;
	}
	/**
	 * @param result_max7 The result_max7 to set.
	 */
	public void setResult_max7(String result_max7) {
		this.result_max7 = result_max7;
	}
	/**
	 * @return Returns the result_max8.
	 */
	public String getResult_max8() {
		return result_max8;
	}
	/**
	 * @param result_max8 The result_max8 to set.
	 */
	public void setResult_max8(String result_max8) {
		this.result_max8 = result_max8;
	}
	/**
	 * @return Returns the result_max9.
	 */
	public String getResult_max9() {
		return result_max9;
	}
	/**
	 * @param result_max9 The result_max9 to set.
	 */
	public void setResult_max9(String result_max9) {
		this.result_max9 = result_max9;
	}
	/**
	 * @return Returns the result_min1.
	 */
	public String getResult_min1() {
		return result_min1;
	}
	/**
	 * @param result_min1 The result_min1 to set.
	 */
	public void setResult_min1(String result_min1) {
		this.result_min1 = result_min1;
	}
	/**
	 * @return Returns the result_min10.
	 */
	public String getResult_min10() {
		return result_min10;
	}
	/**
	 * @param result_min10 The result_min10 to set.
	 */
	public void setResult_min10(String result_min10) {
		this.result_min10 = result_min10;
	}
	/**
	 * @return Returns the result_min2.
	 */
	public String getResult_min2() {
		return result_min2;
	}
	/**
	 * @param result_min2 The result_min2 to set.
	 */
	public void setResult_min2(String result_min2) {
		this.result_min2 = result_min2;
	}
	/**
	 * @return Returns the result_min3.
	 */
	public String getResult_min3() {
		return result_min3;
	}
	/**
	 * @param result_min3 The result_min3 to set.
	 */
	public void setResult_min3(String result_min3) {
		this.result_min3 = result_min3;
	}
	/**
	 * @return Returns the result_min4.
	 */
	public String getResult_min4() {
		return result_min4;
	}
	/**
	 * @param result_min4 The result_min4 to set.
	 */
	public void setResult_min4(String result_min4) {
		this.result_min4 = result_min4;
	}
	/**
	 * @return Returns the result_min5.
	 */
	public String getResult_min5() {
		return result_min5;
	}
	/**
	 * @param result_min5 The result_min5 to set.
	 */
	public void setResult_min5(String result_min5) {
		this.result_min5 = result_min5;
	}
	/**
	 * @return Returns the result_min6.
	 */
	public String getResult_min6() {
		return result_min6;
	}
	/**
	 * @param result_min6 The result_min6 to set.
	 */
	public void setResult_min6(String result_min6) {
		this.result_min6 = result_min6;
	}
	/**
	 * @return Returns the result_min7.
	 */
	public String getResult_min7() {
		return result_min7;
	}
	/**
	 * @param result_min7 The result_min7 to set.
	 */
	public void setResult_min7(String result_min7) {
		this.result_min7 = result_min7;
	}
	/**
	 * @return Returns the result_min8.
	 */
	public String getResult_min8() {
		return result_min8;
	}
	/**
	 * @param result_min8 The result_min8 to set.
	 */
	public void setResult_min8(String result_min8) {
		this.result_min8 = result_min8;
	}
	/**
	 * @return Returns the result_min9.
	 */
	public String getResult_min9() {
		return result_min9;
	}
	/**
	 * @param result_min9 The result_min9 to set.
	 */
	public void setResult_min9(String result_min9) {
		this.result_min9 = result_min9;
	}
	/**
	 * @return Returns the text_id1.
	 */
	public String getText_id1() {
		return text_id1;
	}
	/**
	 * @param text_id1 The text_id1 to set.
	 */
	public void setText_id1(String text_id1) {
		this.text_id1 = text_id1;
	}
	/**
	 * @return Returns the text_id10.
	 */
	public String getText_id10() {
		return text_id10;
	}
	/**
	 * @param text_id10 The text_id10 to set.
	 */
	public void setText_id10(String text_id10) {
		this.text_id10 = text_id10;
	}
	/**
	 * @return Returns the text_id2.
	 */
	public String getText_id2() {
		return text_id2;
	}
	/**
	 * @param text_id2 The text_id2 to set.
	 */
	public void setText_id2(String text_id2) {
		this.text_id2 = text_id2;
	}
	/**
	 * @return Returns the text_id3.
	 */
	public String getText_id3() {
		return text_id3;
	}
	/**
	 * @param text_id3 The text_id3 to set.
	 */
	public void setText_id3(String text_id3) {
		this.text_id3 = text_id3;
	}
	/**
	 * @return Returns the text_id4.
	 */
	public String getText_id4() {
		return text_id4;
	}
	/**
	 * @param text_id4 The text_id4 to set.
	 */
	public void setText_id4(String text_id4) {
		this.text_id4 = text_id4;
	}
	/**
	 * @return Returns the text_id5.
	 */
	public String getText_id5() {
		return text_id5;
	}
	/**
	 * @param text_id5 The text_id5 to set.
	 */
	public void setText_id5(String text_id5) {
		this.text_id5 = text_id5;
	}
	/**
	 * @return Returns the text_id6.
	 */
	public String getText_id6() {
		return text_id6;
	}
	/**
	 * @param text_id6 The text_id6 to set.
	 */
	public void setText_id6(String text_id6) {
		this.text_id6 = text_id6;
	}
	/**
	 * @return Returns the text_id7.
	 */
	public String getText_id7() {
		return text_id7;
	}
	/**
	 * @param text_id7 The text_id7 to set.
	 */
	public void setText_id7(String text_id7) {
		this.text_id7 = text_id7;
	}
	/**
	 * @return Returns the text_id8.
	 */
	public String getText_id8() {
		return text_id8;
	}
	/**
	 * @param text_id8 The text_id8 to set.
	 */
	public void setText_id8(String text_id8) {
		this.text_id8 = text_id8;
	}
	/**
	 * @return Returns the text_id9.
	 */
	public String getText_id9() {
		return text_id9;
	}
	/**
	 * @param text_id9 The text_id9 to set.
	 */
	public void setText_id9(String text_id9) {
		this.text_id9 = text_id9;
	}
	/**
	 * @return Returns the unit1.
	 */
	public String getUnit1() {
		return unit1;
	}
	/**
	 * @param unit1 The unit1 to set.
	 */
	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}
	/**
	 * @return Returns the unit10.
	 */
	public String getUnit10() {
		return unit10;
	}
	/**
	 * @param unit10 The unit10 to set.
	 */
	public void setUnit10(String unit10) {
		this.unit10 = unit10;
	}
	/**
	 * @return Returns the unit2.
	 */
	public String getUnit2() {
		return unit2;
	}
	/**
	 * @param unit2 The unit2 to set.
	 */
	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}
	/**
	 * @return Returns the unit3.
	 */
	public String getUnit3() {
		return unit3;
	}
	/**
	 * @param unit3 The unit3 to set.
	 */
	public void setUnit3(String unit3) {
		this.unit3 = unit3;
	}
	/**
	 * @return Returns the unit4.
	 */
	public String getUnit4() {
		return unit4;
	}
	/**
	 * @param unit4 The unit4 to set.
	 */
	public void setUnit4(String unit4) {
		this.unit4 = unit4;
	}
	/**
	 * @return Returns the unit5.
	 */
	public String getUnit5() {
		return unit5;
	}
	/**
	 * @param unit5 The unit5 to set.
	 */
	public void setUnit5(String unit5) {
		this.unit5 = unit5;
	}
	/**
	 * @return Returns the unit6.
	 */
	public String getUnit6() {
		return unit6;
	}
	/**
	 * @param unit6 The unit6 to set.
	 */
	public void setUnit6(String unit6) {
		this.unit6 = unit6;
	}
	/**
	 * @return Returns the unit7.
	 */
	public String getUnit7() {
		return unit7;
	}
	/**
	 * @param unit7 The unit7 to set.
	 */
	public void setUnit7(String unit7) {
		this.unit7 = unit7;
	}
	/**
	 * @return Returns the unit8.
	 */
	public String getUnit8() {
		return unit8;
	}
	/**
	 * @param unit8 The unit8 to set.
	 */
	public void setUnit8(String unit8) {
		this.unit8 = unit8;
	}
	/**
	 * @return Returns the unit9.
	 */
	public String getUnit9() {
		return unit9;
	}
	/**
	 * @param unit9 The unit9 to set.
	 */
	public void setUnit9(String unit9) {
		this.unit9 = unit9;
	}
	/**
	 * @return Returns the elements.
	 */
	public Vector getElements() {
		return elements;
	}
	/**
	 * @return Returns the isActiveAnalysis.
	 */
	public boolean isActiveAnalysis() {
		return isActiveAnalysis;
	}
	/**
	 * @return Returns the errorCode.
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @return Returns the base.
	 */
	public String getBase() {
		return base;
	}
	/**
	 * @param base The base to set.
	 */
	public void setBase(String base) {
		this.base = base;
	}
	/**
	 * @return Returns the elements_id.
	 */
	public Vector getElements_id() {
		return elements_id;
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
	 * @return Returns the original_remark.
	 */
	public String getOriginal_remark() {
		return original_remark;
	}
	/**
	 * @param original_remark The original_remark to set.
	 */
	public void setOriginal_remark(String original_remark) {
		this.original_remark = original_remark;
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
	 * @param new_result_max1 The new_result_max1 to set.
	 */
	public void setNew_result_max1(String new_result_max1) {
		this.new_result_max1 = new_result_max1;
	}
	/**
	 * @param new_result_max10 The new_result_max10 to set.
	 */
	public void setNew_result_max10(String new_result_max10) {
		this.new_result_max10 = new_result_max10;
	}
	/**
	 * @param new_result_max2 The new_result_max2 to set.
	 */
	public void setNew_result_max2(String new_result_max2) {
		this.new_result_max2 = new_result_max2;
	}
	/**
	 * @param new_result_max3 The new_result_max3 to set.
	 */
	public void setNew_result_max3(String new_result_max3) {
		this.new_result_max3 = new_result_max3;
	}
	/**
	 * @param new_result_max4 The new_result_max4 to set.
	 */
	public void setNew_result_max4(String new_result_max4) {
		this.new_result_max4 = new_result_max4;
	}
	/**
	 * @param new_result_max5 The new_result_max5 to set.
	 */
	public void setNew_result_max5(String new_result_max5) {
		this.new_result_max5 = new_result_max5;
	}
	/**
	 * @param new_result_max6 The new_result_max6 to set.
	 */
	public void setNew_result_max6(String new_result_max6) {
		this.new_result_max6 = new_result_max6;
	}
	/**
	 * @param new_result_max7 The new_result_max7 to set.
	 */
	public void setNew_result_max7(String new_result_max7) {
		this.new_result_max7 = new_result_max7;
	}
	/**
	 * @param new_result_max8 The new_result_max8 to set.
	 */
	public void setNew_result_max8(String new_result_max8) {
		this.new_result_max8 = new_result_max8;
	}
	/**
	 * @param new_result_max9 The new_result_max9 to set.
	 */
	public void setNew_result_max9(String new_result_max9) {
		this.new_result_max9 = new_result_max9;
	}
	/**
	 * @param new_result_min1 The new_result_min1 to set.
	 */
	public void setNew_result_min1(String new_result_min1) {
		this.new_result_min1 = new_result_min1;
	}
	/**
	 * @param new_result_min10 The new_result_min10 to set.
	 */
	public void setNew_result_min10(String new_result_min10) {
		this.new_result_min10 = new_result_min10;
	}
	/**
	 * @param new_result_min2 The new_result_min2 to set.
	 */
	public void setNew_result_min2(String new_result_min2) {
		this.new_result_min2 = new_result_min2;
	}
	/**
	 * @param new_result_min3 The new_result_min3 to set.
	 */
	public void setNew_result_min3(String new_result_min3) {
		this.new_result_min3 = new_result_min3;
	}
	/**
	 * @param new_result_min4 The new_result_min4 to set.
	 */
	public void setNew_result_min4(String new_result_min4) {
		this.new_result_min4 = new_result_min4;
	}
	/**
	 * @param new_result_min5 The new_result_min5 to set.
	 */
	public void setNew_result_min5(String new_result_min5) {
		this.new_result_min5 = new_result_min5;
	}
	/**
	 * @param new_result_min6 The new_result_min6 to set.
	 */
	public void setNew_result_min6(String new_result_min6) {
		this.new_result_min6 = new_result_min6;
	}
	/**
	 * @param new_result_min7 The new_result_min7 to set.
	 */
	public void setNew_result_min7(String new_result_min7) {
		this.new_result_min7 = new_result_min7;
	}
	/**
	 * @param new_result_min8 The new_result_min8 to set.
	 */
	public void setNew_result_min8(String new_result_min8) {
		this.new_result_min8 = new_result_min8;
	}
	/**
	 * @param new_result_min9 The new_result_min9 to set.
	 */
	public void setNew_result_min9(String new_result_min9) {
		this.new_result_min9 = new_result_min9;
	}
	/**
	 * @param new_text_id1 The new_text_id1 to set.
	 */
	public void setNew_text_id1(String new_text_id1) {
		this.new_text_id1 = new_text_id1;
	}
	/**
	 * @param new_text_id10 The new_text_id10 to set.
	 */
	public void setNew_text_id10(String new_text_id10) {
		this.new_text_id10 = new_text_id10;
	}
	/**
	 * @param new_text_id2 The new_text_id2 to set.
	 */
	public void setNew_text_id2(String new_text_id2) {
		this.new_text_id2 = new_text_id2;
	}
	/**
	 * @param new_text_id3 The new_text_id3 to set.
	 */
	public void setNew_text_id3(String new_text_id3) {
		this.new_text_id3 = new_text_id3;
	}
	/**
	 * @param new_text_id4 The new_text_id4 to set.
	 */
	public void setNew_text_id4(String new_text_id4) {
		this.new_text_id4 = new_text_id4;
	}
	/**
	 * @param new_text_id5 The new_text_id5 to set.
	 */
	public void setNew_text_id5(String new_text_id5) {
		this.new_text_id5 = new_text_id5;
	}
	/**
	 * @param new_text_id6 The new_text_id6 to set.
	 */
	public void setNew_text_id6(String new_text_id6) {
		this.new_text_id6 = new_text_id6;
	}
	/**
	 * @param new_text_id7 The new_text_id7 to set.
	 */
	public void setNew_text_id7(String new_text_id7) {
		this.new_text_id7 = new_text_id7;
	}
	/**
	 * @param new_text_id8 The new_text_id8 to set.
	 */
	public void setNew_text_id8(String new_text_id8) {
		this.new_text_id8 = new_text_id8;
	}
	/**
	 * @param new_text_id9 The new_text_id9 to set.
	 */
	public void setNew_text_id9(String new_text_id9) {
		this.new_text_id9 = new_text_id9;
	}
	/**
	 * @param new_unit1 The new_unit1 to set.
	 */
	public void setNew_unit1(String new_unit1) {
		this.new_unit1 = new_unit1;
	}
	/**
	 * @param new_unit10 The new_unit10 to set.
	 */
	public void setNew_unit10(String new_unit10) {
		this.new_unit10 = new_unit10;
	}
	/**
	 * @param new_unit2 The new_unit2 to set.
	 */
	public void setNew_unit2(String new_unit2) {
		this.new_unit2 = new_unit2;
	}
	/**
	 * @param new_unit3 The new_unit3 to set.
	 */
	public void setNew_unit3(String new_unit3) {
		this.new_unit3 = new_unit3;
	}
	/**
	 * @param new_unit4 The new_unit4 to set.
	 */
	public void setNew_unit4(String new_unit4) {
		this.new_unit4 = new_unit4;
	}
	/**
	 * @param new_unit5 The new_unit5 to set.
	 */
	public void setNew_unit5(String new_unit5) {
		this.new_unit5 = new_unit5;
	}
	/**
	 * @param new_unit6 The new_unit6 to set.
	 */
	public void setNew_unit6(String new_unit6) {
		this.new_unit6 = new_unit6;
	}
	/**
	 * @param new_unit7 The new_unit7 to set.
	 */
	public void setNew_unit7(String new_unit7) {
		this.new_unit7 = new_unit7;
	}
	/**
	 * @param new_unit8 The new_unit8 to set.
	 */
	public void setNew_unit8(String new_unit8) {
		this.new_unit8 = new_unit8;
	}
	/**
	 * @param new_unit9 The new_unit9 to set.
	 */
	public void setNew_unit9(String new_unit9) {
		this.new_unit9 = new_unit9;
	}
	/**
	 * @param updateCounts The updateCounts to set.
	 */
	public void setUpdateCounts(int[] updateCounts) {
		this.updateCounts = updateCounts;
	}
	/**
	 * @param value_list_hidden The value_list_hidden to set.
	 */
	public void setValue_list_hidden(Map value_list_hidden) {
		this.value_list_hidden = value_list_hidden;
	}
	/**
	 * @param value_list_remove The value_list_remove to set.
	 */
	public void setValue_list_remove(Map value_list_remove) {
		this.value_list_remove = value_list_remove;
	}
	/**
	 * @param value_list2 The value_list2 to set.
	 */
	public void setValue_list2(Map value_list2) {
		this.value_list2 = value_list2;
	}
	/**
	 * @return Returns the active.
	 */
	public String getActive() {
		return active;
	}
	
	/**
	 * @param result_type11 The result_type10 to set.
	 */
	public void setResult_type1(String result_type1) {
		this.result_type1 = result_type1;
	}
	
	/**
	 * @param result_type2 The result_type2 to set.
	 */
	public void setResult_type2(String result_type2) {
		this.result_type2 = result_type2;
	}
	
	/**
	 * @param result_type3 The result_type3 to set.
	 */
	public void setResult_type3(String result_type3) {
		this.result_type3 = result_type3;
	}
	
	/**
	 * @param result_type4 The result_type4 to set.
	 */
	public void setResult_type4(String result_type4) {
		this.result_type4 = result_type4;
	}
	
	/**
	 * @param result_type5 The result_type5 to set.
	 */
	public void setResult_type5(String result_type5) {
		this.result_type5 = result_type5;
	}
	
	/**
	 * @param result_type6 The result_type6 to set.
	 */
	public void setResult_type6(String result_type6) {
		this.result_type6 = result_type6;
	}
	
	/**
	 * @param result_type7 The result_type7 to set.
	 */
	public void setResult_type7(String result_type7) {
		this.result_type7 = result_type7;
	}
	
	/**
	 * @param result_type8 The result_type8 to set.
	 */
	public void setResult_type8(String result_type8) {
		this.result_type8 = result_type8;
	}
	
	/**
	 * @param result_type9 The result_type9 to set.
	 */
	public void setResult_type9(String result_type9) {
		this.result_type9 = result_type9;
	}
	
	/**
	 * @param result_type10 The result_type10 to set.
	 */
	public void setResult_type10(String result_type10) {
		this.result_type10 = result_type10;
	}
	
	/**
	 * @param new_result_type1 The new_result_type1 to set.
	 */
	public void setNew_result_type1(String new_result_type1) {
		this.new_result_type1 = new_result_type1;
	}
	/**
	 * @param new_result_type10 The new_result_type10 to set.
	 */
	public void setNew_result_type10(String new_result_type10) {
		this.new_result_type10 = new_result_type10;
	}
	/**
	 * @param new_result_type2 The new_result_type2 to set.
	 */
	public void setNew_result_type2(String new_result_type2) {
		this.new_result_type2 = new_result_type2;
	}
	/**
	 * @param new_result_type3 The new_result_type3 to set.
	 */
	public void setNew_result_type3(String new_result_type3) {
		this.new_result_type3 = new_result_type3;
	}
	/**
	 * @param new_result_type4 The new_result_type4 to set.
	 */
	public void setNew_result_type4(String new_result_type4) {
		this.new_result_type4 = new_result_type4;
	}
	/**
	 * @param new_result_type5 The new_result_type5 to set.
	 */
	public void setNew_result_type5(String new_result_type5) {
		this.new_result_type5 = new_result_type5;
	}
	/**
	 * @param new_result_type6 The new_result_type6 to set.
	 */
	public void setNew_result_type6(String new_result_type6) {
		this.new_result_type6 = new_result_type6;
	}
	/**
	 * @param new_result_type7 The new_result_type7 to set.
	 */
	public void setNew_result_type7(String new_result_type7) {
		this.new_result_type7 = new_result_type7;
	}
	/**
	 * @param new_result_type8 The new_result_type8 to set.
	 */
	public void setNew_result_type8(String new_result_type8) {
		this.new_result_type8 = new_result_type8;
	}
	/**
	 * @param new_result_type9 The new_result_type9 to set.
	 */
	public void setNew_result_type9(String new_result_type9) {
		this.new_result_type9 = new_result_type9;
	}
	/**
	 * @return Returns the normalbase.
	 */
	public String getNormalbase() {
		return normalbase;
	}
	/**
	 * @param normalbase The normalbase to set.
	 */
	public void setNormalbase(String normalbase) {
		this.normalbase = normalbase;
	}
	/**
	 * @return Returns the version_list.
	 */
	public Vector getVersion_list() {
		return version_list;
	}

	/**
	 * @return Returns the html_data_list.
	 */
	public Vector getHtml_data_list() {
		return html_data_list;
	}
	/**
	 * @return Returns the data_list.
	 */
	public Vector getData_list() {
		return data_list;
	}
	/**
	 * @return Returns the scripts.
	 */
	public Vector getScripts() {
		return scripts;
	}
	/**
	 * @param reportbase The reportbase to set.
	 */
	public void setReportbase(String reportbase) {
		this.reportbase = reportbase;
	}
	/**
	 * @return Returns the use_spec1.
	 */
	public String getUse_spec1() {
		return use_spec1;
	}
	/**
	 * @param use_spec1 The use_spec1 to set.
	 */
	public void setUse_spec1(String use_spec1) {
		this.use_spec1 = use_spec1;
	}
	/**
	 * @return Returns the use_spec10.
	 */
	public String getUse_spec10() {
		return use_spec10;
	}
	/**
	 * @param use_spec10 The use_spec10 to set.
	 */
	public void setUse_spec10(String use_spec10) {
		this.use_spec10 = use_spec10;
	}
	/**
	 * @return Returns the use_spec2.
	 */
	public String getUse_spec2() {
		return use_spec2;
	}
	/**
	 * @param use_spec2 The use_spec2 to set.
	 */
	public void setUse_spec2(String use_spec2) {
		this.use_spec2 = use_spec2;
	}
	/**
	 * @return Returns the use_spec3.
	 */
	public String getUse_spec3() {
		return use_spec3;
	}
	/**
	 * @param use_spec3 The use_spec3 to set.
	 */
	public void setUse_spec3(String use_spec3) {
		this.use_spec3 = use_spec3;
	}
	/**
	 * @return Returns the use_spec4.
	 */
	public String getUse_spec4() {
		return use_spec4;
	}
	/**
	 * @param use_spec4 The use_spec4 to set.
	 */
	public void setUse_spec4(String use_spec4) {
		this.use_spec4 = use_spec4;
	}
	/**
	 * @return Returns the use_spec5.
	 */
	public String getUse_spec5() {
		return use_spec5;
	}
	/**
	 * @param use_spec5 The use_spec5 to set.
	 */
	public void setUse_spec5(String use_spec5) {
		this.use_spec5 = use_spec5;
	}
	/**
	 * @return Returns the use_spec6.
	 */
	public String getUse_spec6() {
		return use_spec6;
	}
	/**
	 * @param use_spec6 The use_spec6 to set.
	 */
	public void setUse_spec6(String use_spec6) {
		this.use_spec6 = use_spec6;
	}
	/**
	 * @return Returns the use_spec7.
	 */
	public String getUse_spec7() {
		return use_spec7;
	}
	/**
	 * @param use_spec7 The use_spec7 to set.
	 */
	public void setUse_spec7(String use_spec7) {
		this.use_spec7 = use_spec7;
	}
	/**
	 * @return Returns the use_spec8.
	 */
	public String getUse_spec8() {
		return use_spec8;
	}
	/**
	 * @param use_spec8 The use_spec8 to set.
	 */
	public void setUse_spec8(String use_spec8) {
		this.use_spec8 = use_spec8;
	}
	/**
	 * @return Returns the use_spec9.
	 */
	public String getUse_spec9() {
		return use_spec9;
	}
	/**
	 * @param use_spec9 The use_spec9 to set.
	 */
	public void setUse_spec9(String use_spec9) {
		this.use_spec9 = use_spec9;
	}
	/**
	 * @param new_use_spec1 The new_use_spec1 to set.
	 */
	public void setNew_use_spec1(String new_use_spec1) {
		this.new_use_spec1 = new_use_spec1;
	}
	/**
	 * @param new_use_spec10 The new_use_spec10 to set.
	 */
	public void setNew_use_spec10(String new_use_spec10) {
		this.new_use_spec10 = new_use_spec10;
	}
	/**
	 * @param new_use_spec2 The new_use_spec2 to set.
	 */
	public void setNew_use_spec2(String new_use_spec2) {
		this.new_use_spec2 = new_use_spec2;
	}
	/**
	 * @param new_use_spec3 The new_use_spec3 to set.
	 */
	public void setNew_use_spec3(String new_use_spec3) {
		this.new_use_spec3 = new_use_spec3;
	}
	/**
	 * @param new_use_spec4 The new_use_spec4 to set.
	 */
	public void setNew_use_spec4(String new_use_spec4) {
		this.new_use_spec4 = new_use_spec4;
	}
	/**
	 * @param new_use_spec5 The new_use_spec5 to set.
	 */
	public void setNew_use_spec5(String new_use_spec5) {
		this.new_use_spec5 = new_use_spec5;
	}
	/**
	 * @param new_use_spec6 The new_use_spec6 to set.
	 */
	public void setNew_use_spec6(String new_use_spec6) {
		this.new_use_spec6 = new_use_spec6;
	}
	/**
	 * @param new_use_spec7 The new_use_spec7 to set.
	 */
	public void setNew_use_spec7(String new_use_spec7) {
		this.new_use_spec7 = new_use_spec7;
	}
	/**
	 * @param new_use_spec8 The new_use_spec8 to set.
	 */
	public void setNew_use_spec8(String new_use_spec8) {
		this.new_use_spec8 = new_use_spec8;
	}
	/**
	 * @param new_use_spec9 The new_use_spec9 to set.
	 */
	public void setNew_use_spec9(String new_use_spec9) {
		this.new_use_spec9 = new_use_spec9;
	}
}