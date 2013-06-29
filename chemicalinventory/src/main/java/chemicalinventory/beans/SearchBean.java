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

package chemicalinventory.beans;

import java.sql.*;
import java.util.*;
import chemaxon.jchem.db.*;
import chemaxon.util.*;

import javax.naming.*;
import javax.sql.*;

import java.net.*;

import chemicalinventory.context.Attributes;
import chemicalinventory.history.History;
import chemicalinventory.utility.Util;

/**
 *SearchBean() is concentrating on the task of perfoming a search in the 
 *database for compounds. The search criteria (struckture, name etc.) is 
 *entered on the view, and is processed in this bean.
 **/

public class SearchBean implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6487341442036157153L;
	
	public SearchBean()
	{
	}
	//variable to hold the structure from the drawer
	private String molfile = "";
	
	//from the structures table
	private String cd_formula = "";
	private String cd_molweight = "";
	
	//from the compound table
	private String chemicalName = ""; 
	private String casNumber = "";
	private String density = ""; 
	private String register_date = ""; 
	private String register_by = "";
	private String search_method = "";
	private String value = "";//search value from the quicksearch box in the top frame.
	
	//from the container table.
	private String registerUser = ""; 
	
	//used to create the result table
	public Vector result = new Vector();
	public Vector result_id = new Vector();
	public Vector result_name = new Vector();
	
	public boolean hit;
	
	private String sql1 = "";
	//default order by chemical_name.
	private String ord_by = "c.chemical_name";
	
	private String operator1 = null;
	private String operator2 = null;
	
	private String empty = "IS NULL";
	private String notempty = "IS NOT NULL";
	
	private int countHit = 0;
	private String statement = "";
	private boolean noValues = true;
	private boolean exceedFrq = false;
	private boolean showDeletedCompounds = false;
//	--------------------------------------------------------------------------//
	
	
	/** Setter for chemical name.
	 * @param cn String.
	 */  
	public void setChemicalName(String cn)
	{
		chemicalName = cn.trim();
	}
	
	/** Getter for chemical name.
	 * @return String.
	 */  
	public String getChemicalName()
	{
		return chemicalName.toUpperCase();
	}
	
	/** Setter of the formula.
	 * @param cd String.
	 */  
	public void setCd_formula(String cd)
	{
		cd_formula = cd.trim();
	}
	
	/** Getter for the chemical formula.
	 * @return String.
	 */  
	public String getCd_formula()
	{
		return cd_formula;
	}
	
	/** Setter for the mol weight.
	 * @param cm String.
	 */  
	public void setCd_molweight(String cm)
	{
		cd_molweight = cm.trim();
	}
	
	/** Getter for the molweight.
	 * @return String.
	 */  
	public String getCd_molweight()
	{
		return cd_molweight;
	}
	
	/** Setter for the cas number.
	 * @param cas String.
	 */  
	public void setCasNumber(String cas)
	{
		casNumber = cas.trim();
	}
	
	/** Getter for the cas number.
	 * @return String.
	 */  
	public String getCasNumber()
	{
		return casNumber;
	}
	
	/** Setter for the density.
	 * @param d String.
	 */  
	public void setDensity(String d)
	{
		density = d.trim();
	}
	
	/** Getter for the density.
	 * @return String.
	 */  
	public String getDensity()
	{
		return density;
	}
	
	/** Set the date registered.
	 * @param rd String.
	 */  
	public void setRegister_date(String rd)
	{
		register_date = rd.trim();
	}
	
	/** Getter for the date registered.
	 * @return String.
	 */  
	public String getRegister_date()
	{
		return register_date;
	}
	
	/** Set registered by.
	 * @param rb String.
	 */  
	public void setRegister_by(String rb)
	{
		register_by = rb.trim();
	}
	
	/** Getter for registered by.
	 * @return String.
	 */  
	public String getRegister_by()
	{
		return register_by;
	}
	
	/** Setter for register user.
	 * @param ru String.
	 */  
	public void setRegisterUser(String ru)
	{
		registerUser = ru.trim();
	}
	
	/** Getter for register user.
	 * @return String.
	 */  
	public String getRegisterUser()
	{
		return registerUser;
	}
	
	/** Setter for the molfile.
	 * @param mf String.
	 */  
	public void setMolfile(String mf)
	{
		molfile = mf;
		//This string is the value returned by marvin in case there is no structure
		//drawn in the applet on the search page.
		String molchecker = "0  0  0  0  0  0            999 V2000";
		
		if (mf.substring(40, 77).equals(molchecker))
		{
			molfile = "";
		}
	}
	
	/** Getter for the molfile.
	 * @return String.
	 */  
	public String getMolfile()
	{
		return molfile;
	}
	
	/** Getter for sql.
	 * @return String.
	 */  
	public String getSql1()
	{
		return sql1;
	}
	
	/** Getter for the number of counts in a search.
	 * @return int - number of hits.
	 */  
	public int getCountHit()
	{
		return countHit;
	}
	
	/** Setter for the method of search.
	 * @param method String.
	 */  
	public void setSearch_method(String method)
	{
		search_method = method;
	}
	
	/** Setter for the statement string.
	 * @param state String.
	 */  
	public void setStatement(String state)
	{
		statement = state;
	}
	
	/** Getter for the statement.
	 * @return String.
	 */  
	public String getStatement()
	{
		return statement;
	}
	
	/** Getter for the no of values.
	 * @return Boolean.
	 */  
	public boolean getNoValues()
	{
		return noValues;
	}
	
	/** Getter for the ExceedFrg boolean. Boolean to indicate whether the maximum number
	 * of searches pr. minute has been violated.
	 * @return Boolean.
	 */  
	public boolean getExceedFrq()
	{
		return exceedFrq;
	}
	
	/** Set the coloumn to order result by.
	 * @param ob String.
	 */  
	public void setOrd_by(String ob)
	{
		ord_by = ob;
	}
	
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/** Getter for the boolean to indicate the status of a search.
	 * @return Boolean.
	 */  
	public boolean searchOK(String user, boolean saveSearchHistory)
	{
		SearchCheck(user, saveSearchHistory);
		
		return hit;
	}
	
	/** Getter to indicate the status of a repeated search.
	 * @return Boolean.
	 */  
	public boolean searchStatementOK(String user, boolean saveSearchHistory)
	{
		searchFromStatement(user, saveSearchHistory);
		
		return hit;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isQuickSearchOK(String user, boolean saveSearchHistory)
	{
		quickSearch(user, saveSearchHistory);
		
		return hit;
	}
	
	/** 
	 * The primary method of this class. Here the actual search is performed.
	 * If a structure has been drawn, the first action is searching in the table
	 * holding the structures. After that the search is performed including
	 *the rest of the search criterias.
	 **/
	public void SearchCheck(String user, boolean saveSearchHistory)
	{
		hit = false;
		
		boolean molSearch = false;
		
		if(molfile != null && !molfile.equals(""))//if molfile holds a value perform a search in the structure table.
		{
			try{
				ConnectionHandler conHandler = new ConnectionHandler(); 
				conHandler.setDriver(Attributes.DB_DRIVER);
				conHandler.setUrl(Attributes.DB_NAME);
				conHandler.setLoginName(Attributes.DB_USER);
				conHandler.setPassword(Attributes.DB_PWD);
				conHandler.setPropertyTable(Attributes.J_PROP_TABLE);
				
				conHandler.connect();
				
				JChemSearch searcher = new JChemSearch(); // Create searcher object
				searcher.setQueryStructure(molfile);
				searcher.setConnectionHandler(conHandler);
				searcher.setStructureTable(Attributes.DB_STUCTURE_TABLE);
				searcher.setWaitingForResult(true);         
				searcher.setResultTableMode(0);
				// searcher.setStructureCaching(true);//cache structures in memory
				
				if(search_method.equals("SUBSTRUCTURE"))
				{
					searcher.setSearchType(JChemSearch.SUBSTRUCTURE);
				}
				else if(search_method.equals("EXACT"))
				{
					searcher.setSearchType(JChemSearch.EXACT);
				}
				else if(search_method.equals("SIMILARITY"))
				{
					searcher.setSearchType(3);
				}
				
				searcher.run();
				
				//get the id's from the substructuresearch
				String ids = null;
				if(searcher.getResultCount() > 0)
				{          	
					for(int i=0; i<searcher.getResultCount(); i++) 
					{
						int id = searcher.getResult(i);
						ids = ids + ", " + id;
					}
					ids = ids.substring(6);
					sql1 = "and c.cd_id IN (" + ids + ")"; 
					molSearch = true;
				}
				else 
				{ 
					molSearch = false;
				}
			}//End of try
			
			catch (MaxSearchFrequencyExceededException e)
			{
				System.out.println("Date: "+Util.getDate()+" Time: "+Util.getTime()+":  Search frequency exceeded.");
				exceedFrq = true;
			}
			catch (Exception e)
			{
				System.out.println(e);
			}    
		}//End of if
		
		/* check to see if a value has been entered into one of the remaining
		 *fields in the search form.*/    
		if(!(chemicalName.equals("")) || !(casNumber.equals("")) || !(density.equals("")) || !(register_date.equals("")) || !(register_by.equals("")) || (!cd_formula.equals("")) || (!cd_molweight.equals("")))
		{
			noValues = false;
			
			if(exceedFrq == true && noValues == false)
			{
				molSearch = true;
			}
		}
		
		//make the conenction to the database to search the compound table,
		//The condition is, if a search has been performed, with a 
		//molfile, this search criteria has to return something.
		if(noValues==false || molSearch==true)
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
						
						/*build the beginning of the sql. Created as a left join,
						 *to make sure that compounds which does not have a structure
						 *associated is also shown.*/
						String sql = "";
						
						sql = "SELECT c.id, c.chemical_name, c.cas_number, s.cd_formula, s.cd_molweight"+
						" FROM compound c LEFT JOIN structures s"+
						" ON c.cd_id = s.cd_id"+
						" WHERE 1 = 1 ";
						
						if(!showDeletedCompounds)
							sql += " AND deleted = 0 ";
						
						//Check the chemical name field.
						if(chemicalName.length() > 0)
						{
							chemicalName = Util.double_q(chemicalName);
							int i = 0;
							String parameterList = "";
							StringTokenizer tokens = new StringTokenizer(chemicalName, "+");
							while (tokens.hasMoreTokens())
							{                             
								String token = tokens.nextToken().trim().toUpperCase();
								if (i == 0)
								{
									parameterList = "LIKE '%"+token+"%' ";
									i++;
								}
								else
									parameterList = parameterList + "OR c.chemical_name LIKE '%"+token+"%' "; 
							}
							
							sql = sql + "AND c.chemical_name "+parameterList;
						}
						
						//check the casNumber field
						if(casNumber.length() > 0)
						{
							if(casNumber.indexOf("%") == -1)
							{
								operator2 = "=";
								if(casNumber.indexOf("!=") != -1)
								{
									operator2 = "!=";
									casNumber = casNumber.substring(2);
								}    
							}     
							if(casNumber.indexOf("%") != -1)
							{
								operator2 = "like";
								if(casNumber.indexOf("!=") != -1)
								{
									operator2 = "NOT LIKE";
									casNumber = casNumber.substring(2);
								}  
							}
							
							if(casNumber.toUpperCase().equals(empty))
								sql = sql + "AND c.cas_number IS NULL ";
							else
							{
								if(casNumber.toUpperCase().equals(notempty))
									sql = sql + "AND c.cas_number IS NOT NULL ";
								else
								{
									if(casNumber.indexOf("<") != -1 || casNumber.indexOf(">") != -1)
									{
										sql = sql + "AND c.cas_number " +casNumber.toUpperCase();
									}
									else
									{
										sql = sql + "AND c.cas_number "+operator2+" '"+casNumber.toUpperCase()+"' ";
									}
								}
							}   
						}
						
						
						//check the density field
						if(density.length() > 0)
						{
							operator1 = "=";
							
							if(density.indexOf("!=") != -1)
							{  
								operator1 = "!=";
								density = density.substring(2);
							}
							if(density.indexOf("<") != -1 || density.indexOf(">") != -1)
							{
								operator1 = "";
							}  
							if(density.toUpperCase().equals(empty))
								sql = sql + "AND c.density IS NULL ";
							else
							{
								if(density.toUpperCase().equals(notempty))
									sql = sql + "AND c.density IS NOT NULL ";
								else
									sql = sql + "AND c.density "+operator1+" "+density.toUpperCase()+" ";
							}  
						}
						
						//check the register_by field
						if(register_by.length() > 0)
						{
							if(register_by.indexOf("%") == -1)
							{
								operator2 = "=";
								if(register_by.indexOf("!=") != -1)
								{
									operator2 = "!=";
									register_by = register_by.substring(2);
								}    
							}     
							else 
							{
								operator2 = "like";
								if(register_by.indexOf("!=") != -1)
								{
									operator2 = "NOT LIKE";
									register_by = register_by.substring(2);
								}  
							}
							if(register_by.toUpperCase().equals(empty))
								sql = sql + "AND c.register_by IS NULL ";
							else
							{
								if(register_by.toUpperCase().equals(notempty))
									sql = sql + "AND c.register_by IS NOT NULL ";
								else
									sql = sql + "AND c.register_by "+operator2+" '"+register_by.toUpperCase()+"' "; 
							}   
						}
						
						//check the register_date field(date is treated as a string value in MySQL)
						if(register_date.length() > 0)
						{
							operator1 = "=";
							
							if(register_date.indexOf("!=") != -1)
							{  
								operator1 = "!=";
								register_date = register_date.substring(2);
							}
							if(register_date.indexOf("<") != -1)
							{
								operator1 = "<";
								register_date = register_date.substring(1);
							}
							if(register_date.indexOf(">") != -1)
							{
								operator1 = ">";
								register_date = register_date.substring(1);
							}
							if(register_date.toUpperCase().equals(empty))
								sql = sql + "AND c.register_date IS NULL ";
							else
							{
								if(register_date.toUpperCase().equals(notempty))
									sql = sql + "AND c.register_date IS NOT NULL ";
								else
									sql = sql + "AND c.register_date "+operator1+" '"+register_date.toUpperCase()+"' ";
							}  
						}
						
						//check the cd_formula field
						if(cd_formula.length() > 0)
						{   
							if(cd_formula.indexOf("%") == -1)
							{
								operator2 = "=";
								if(cd_formula.indexOf("!=") != -1)
								{
									operator2 = "!=";
									cd_formula = cd_formula.substring(2);
								}    
							}     
							else 
							{
								operator2 = "like";
								if(cd_formula.indexOf("!=") != -1)
								{
									operator2 = "NOT LIKE";
									cd_formula = cd_formula.substring(2);
								}  
							}
							if(cd_formula.toUpperCase().equals(empty))
								sql = sql + "AND s.cd_formula IS NULL ";
							else
							{
								if(cd_formula.toUpperCase().equals(notempty))
									sql = sql + "AND s.cd_formula IS NOT NULL ";
								else
									sql = sql + "AND s.cd_formula "+operator2+" '"+cd_formula.toUpperCase()+"' "; 
							}   
						}
						
						//check the cd_molweigth field
						if(cd_molweight.length() > 0)
						{   
							operator1 = "=";
							
							if(cd_molweight.indexOf("!=") != -1)
							{  
								operator1 = "!=";
								cd_molweight = cd_molweight.substring(2);
							}
							if(cd_molweight.indexOf("<") != -1 || cd_molweight.indexOf(">") != -1)
							{
								operator1 = "";
							}  
							if(cd_molweight.toUpperCase().equals(empty))
								sql = sql + "AND s.cd_molweight IS NULL ";
							else
							{
								if(cd_molweight.toUpperCase().equals(notempty))
									sql = sql + "AND s.cd_molweight IS NOT NULL ";
								else
									sql = sql + "AND s.cd_molweight "+operator1+" "+cd_molweight.toUpperCase()+" ";
							}
						}       
						
						//run the query
						sql = sql + sql1+" ORDER BY c.chemical_name";
						
						statement = sql; 
						
						if(saveSearchHistory) {
							//Save the search in the history table
							WriteSearchHistory(History.SEARCH_PERFORMED, statement, "", user);
						}
						
						//A String paramter used in a second search to select all 
						//records. 
						ResultSet rs = stmt.executeQuery(sql);
						
						result.clear();
						result_id.clear();
						
						while(rs.next()) 
						{
							String name = rs.getString("chemical_name").toUpperCase();
							String cas = rs.getString("c.cas_number");
							String form = rs.getString("s.cd_formula");
							String molw = rs.getString("s.cd_molweight");
							
							if(name == null || name.equals(""))
							{
								name = "-";
							}
							else
							{
								name = Util.encodeTag(name);
								name = URLEncoder.encode(name, "UTF-8");
							}                      
							if(cas == null || cas.equals(""))
							{
								cas = "-";
							}
							if(form == null || form.equals(""))
							{
								form = "-";
							}
							if(molw == null || molw.equals(""))
							{
								molw = "-";
							}
							
							result.addElement(name+"|"+cas+"|"+form+"|"+molw);
							result_id.addElement(rs.getString("id"));
							result_name.addElement(name);
							
							countHit = countHit + 1;
						}
						rs.close();
						stmt.close();
					}
					con.close();
				}
			}//end of try
			
			catch (ClassNotFoundException e) 
			{
				System.out.println("error 1: "+e);
			}
			catch (SQLException e)
			{
				System.out.println("error 2: "+e);
			}
			catch (Exception e)
			{
				System.out.println("error 3: "+e);
			}
			
			if(result_id.isEmpty())
			{
				hit = false;
			}
			else
			{
				hit = true;
			}
		}
		else
		{
			if(!molfile.equals(""))//There was a value in molfile so it was not an empty search
			{
				noValues = false;
			}
			else noValues = true; //No values entered at all!!!
		}
	}//End of method
	
	
	/** Perform a search based on the saved search result  
	 * The sql from a privious search is here used to perform 
	 * the search.
	 **/
	public void searchFromStatement(String user, boolean saveSearchHistory)
	{
		hit = false;
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
					
					//make sure that order by is performed as wanted.
					if(!ord_by.equals("c.chemical_name") || ord_by != null)
					{
						statement = statement.substring(0, (statement.lastIndexOf("ORDER BY")));
						statement = statement+"ORDER BY "+ord_by;
					}
					
					/*
					 * Save to search history
					 */
					if(saveSearchHistory) {
						WriteSearchHistory(History.SEARCH_REPEATED, statement, "", user);
					}
					
					ResultSet rs = stmt.executeQuery(statement);
					
					result.clear();
					result_id.clear();
					
					while(rs.next()) 
					{
						String name = rs.getString("chemical_name").toUpperCase();
						String cas = rs.getString("c.cas_number");
						String form = rs.getString("s.cd_formula");
						String molw = rs.getString("s.cd_molweight");
						
						if(name == null || name.equals(""))
						{
							name = "-";
						}
						else
						{
							name = Util.encodeTag(name);
							name = URLEncoder.encode(name, "UTF-8");
						}                      
						if(cas == null || cas.equals(""))
						{
							cas = "-";
						}
						if(form == null || form.equals(""))
						{
							form = "-";
						}
						if(molw == null || molw.equals(""))
						{
							molw = "-";
						}
						
						result.addElement(name+"|"+cas+"|"+form+"|"+molw);
						result_id.addElement(rs.getString("id"));
						result_name.addElement(name);
						
						countHit = countHit + 1;
					}
					rs.close();
					stmt.close();
				}
				con.close();
			}
		}//end of try        
		
		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println("SQL exception: " +e);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " +e);
		}
		
		if(result_id.isEmpty())
		{
			hit = false;
		}
		else
		{
			hit = true;
		}
	}
	
	/**
	 * When the user enters a search value in the quicksearch box in the top
	 * frame, this method is invoked. The value entered is compared with
	 * chemical names and CAS numbers in the db.   *
	 */
	public void quickSearch(String user, boolean saveSearchHistory)
	{
		hit = false;
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
					
					//the search string can legally be delimitered by + to
					//include multiple search options
					
					String originalValue = value;
					
					value = Util.double_q(value);
					int i = 0;
					String parameterList = "";
					String parameterList2 = "";
					StringTokenizer tokens = new StringTokenizer(value, "+");
					while (tokens.hasMoreTokens())
					{                             
						String token = tokens.nextToken().trim().toUpperCase();
						if (i == 0)
						{
							parameterList = "LIKE '%"+token+"%' ";
							parameterList2 = "LIKE '%"+token+"%' ";
							i++;
						}
						else
						{
							parameterList = parameterList + "OR c.chemical_name LIKE '%"+token+"%' "; 
							parameterList2 = parameterList2 + "OR c.cas_number LIKE '%"+token+"%' "; 
						}
						
					}
					
					String sql = "SELECT c.id, c.chemical_name, c.cas_number, s.cd_formula, s.cd_molweight"+
					" FROM compound c LEFT JOIN structures s"+
					" ON c.cd_id = s.cd_id"+
					" WHERE 1=1";
					
					if(!showDeletedCompounds)
						sql += " AND c.deleted = 0";
					
					sql += " AND c.chemical_name "+parameterList +
					" OR c.cas_number "+parameterList2 +
					" ORDER BY c.chemical_name";      
					
					statement = sql; 
					
					/*
					 * Save to search history
					 */
					if(saveSearchHistory) {
						WriteSearchHistory(History.SEARCH_QUICK, statement, originalValue, user);
					}
					
					ResultSet rs = stmt.executeQuery(sql);
					
					result.clear();
					result_id.clear();
					
					while(rs.next()) 
					{
						String name = rs.getString("chemical_name").toUpperCase();
						String cas = rs.getString("c.cas_number");
						String form = rs.getString("s.cd_formula");
						String molw = rs.getString("s.cd_molweight");
						
						if(name == null || name.equals(""))
						{
							name = "-";
						}
						else
						{
							name = Util.encodeTag(name);
							name = URLEncoder.encode(name, "UTF-8");
						}                      
						if(cas == null || cas.equals(""))
						{
							cas = "-";
						}
						if(form == null || form.equals(""))
						{
							form = "-";
						}
						if(molw == null || molw.equals(""))
						{
							molw = "-";
						}
						
						result.addElement(name+"|"+cas+"|"+form+"|"+molw);
						result_id.addElement(rs.getString("id"));
						result_name.addElement(name);
						
						countHit = countHit + 1;
					}
					rs.close();
					stmt.close();
				}
				con.close();
			}
		}//end of try        
		
		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println("SQL exception: " +e);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " +e);
		}
		
		if(result_id.isEmpty())
		{
			hit = false;
		}
		else
		{
			hit = true;
		}
	}
	
	/**
	 * Insert the history when performing a search.
	 * Normal Search:
	 * Change details:
	 * chemical name | formula | CAS | mol wgt. | density | registered_date | registered_by | search method
	 * 
	 * Quick search:
	 * Change details:
	 * The search text entered.
	 * 
	 * Repeated text:
	 * The sql is in the text field.
	 * 
	 * @param search_type
	 * @param sql_statement
	 * @param quickSearchValue
	 * @param user
	 * @return
	 */
	private boolean WriteSearchHistory(String search_type, String sql_statement, String quickSearchValue, String user) {

		String table = search_type;
		String text_id = search_type;
		String change_details = "";

		if(search_type.equals(History.SEARCH_QUICK))
			change_details = quickSearchValue;
		else if(search_type.equals(History.SEARCH_PERFORMED)) {
			change_details = chemicalName+ "|"+cd_formula+"|"+casNumber+"|"+cd_molweight+"|"+density+"|"+register_date+"|"+register_by+"|"+search_method;
		}

		String text = sql_statement;
		String change_by = user;
		String unit = "--";
		String newv = "--";
		String oldv = "--";
		String structure = molfile;
		String date = Util.getDate();

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

					String sql = "SELECT id FROM user WHERE user_name ='"+user+"'";

					ResultSet rs = stmt.executeQuery(sql);

					while(rs.next())
					{
						text_id = rs.getString("id");
					}
					rs.close();

					History historyObj = new History();

					String sql1 = historyObj.insertHistory_stringComplete(History.SEARCH_COMPOUND, Integer.parseInt(text_id), table, Util.double_q(text), change_by.toUpperCase(), unit, oldv, newv, change_details, structure);

					if(sql1 != null) {
						try
						{
							//update the history for the search!
							stmt.executeUpdate(sql1);

							con.commit();
							con.close();

							return true;
						}
						catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();

							return false;
						}
					}
				}              
				con.close();
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
	 * @return the showDeletedCompounds
	 */
	public boolean isShowDeletedCompounds() {
		return showDeletedCompounds;
	}

	/**
	 * @param showDeletedCompounds the showDeletedCompounds to set
	 */
	public void setShowDeletedCompounds(boolean showDeletedCompounds) {
		this.showDeletedCompounds = showDeletedCompounds;
	}
}