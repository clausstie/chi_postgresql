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
package chemicalinventory.search;

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

import chemaxon.jchem.db.JChemSearch;
import chemaxon.jchem.db.MaxSearchFrequencyExceededException;
import chemaxon.util.ConnectionHandler;
import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class SampleSearch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//	variable to hold the structure from the drawer
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
	
	//from analysis fields in the search pictures to search for samples in analysis
	private String search_field1 = "";
	private String id_field1 = "";
	private String type1 = "";
	private String option1 = "";
	private String search_field2 = "";
	private String id_field2 = "";
	private String type2 = "";
	private String option2 = "";
	private String search_field3 = "";
	private String id_field3 = "";
	private String type3 = "";
	private String option3 = "";
	
	//servlet context base
	private String base = "";
	
	//used to create the result table
	public Vector result = new Vector();
	public Vector result_id = new Vector();
	public Vector result_name = new Vector();
	private Vector noAnaResults = new Vector();
	
	//used to create sample result table
	private HashMap compound_map;
	private Vector analysis_list;
	private Vector sample_list;
	private Vector details_list;
	private Vector sample_result = new Vector();
	private Vector noAnaResultsId = new Vector();
	
	public boolean hit;
	
	private String sql1 = "";
	
	//default order by chemical_name.
	private String ord_by = "c.chemical_name";
	
	private String operator1 = null;
	private String operator2 = null;
	private String operator3 = null;
	
	private String empty = "IS NULL";
	private String notempty = "IS NOT NULL";
	
	private int countHit = 0;
	private String statement = "";
	private boolean history = false;
	private boolean noValues = true;
	private boolean exceedFrq = false;
	private boolean molSearch = false;
	
	
	/**
	 * Search for structure.
	 * @return String a list of compound id's matching the structures.
	 */
	public String searchStructure()
	{
		if(molfile != null && !molfile.equals(""))//if molfile holds a value perform a search in the structure table.
		{
			try{
				
				ConnectionHandler conHandler = new ConnectionHandler();
				conHandler.setUrl(Attributes.DB_NAME);
				conHandler.setDriver(Attributes.DB_DRIVER);
				conHandler.setPropertyTable(Attributes.J_PROP_TABLE);
				conHandler.setLoginName(Attributes.DB_USER);
				conHandler.setPassword(Attributes.DB_PWD);
				
				conHandler.connect();
				
				String structureTableName = "structures";
				
				JChemSearch searcher = new JChemSearch(); // Create searcher object
				searcher.setQueryStructure(molfile);
				searcher.setConnectionHandler(conHandler);
				searcher.setStructureTable(structureTableName);
				searcher.setWaitingForResult(true);         
				searcher.setResultTableMode(0);
				//searcher.setStructureCaching(true);//use structure caching	          
				
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
					sql1 = ids;
					
//					sql1 = "and c.cd_id IN (" + ids + ")"; 
					molSearch = true;
				}
				else 
				{ 
					molSearch = false;
				}
			}//End of try
			
			catch (MaxSearchFrequencyExceededException e)
			{
				System.out.println(Util.getDateTime()+" search frequence exceeded");
				exceedFrq = true;
			}
			catch (Exception e)
			{
				System.out.println(e);
			}    
		}//End of if
		
		return sql1;
	}
	
	/**
	 * Search for compound.
	 * 
	 * 
	 * @param cd_id_data 
	 * 		this data must be in specific format: '1,2,3....'.
	 */
	public void searchCompound(String cd_id_data)
	{
		//empty the used lists
		
		result.clear();
		result_id.clear();
		result_name.clear();
		
		
		//code the structure data:
		if(cd_id_data != null && !cd_id_data.equals(""))
		{
			cd_id_data = "and c.cd_id IN (" + cd_id_data + ")";
		}
		
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
						/*if(register_by.length() > 0)
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
						  }*/
						
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
						sql = sql + cd_id_data +" ORDER BY c.chemical_name";
						
						statement = sql; 
						
						//A String paramter used in a second search to select all 
						//records. 
						ResultSet rs = stmt.executeQuery(sql);
						
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
								
								//create the name in an overlib box
								name = encodeNameToBox(name, rs.getString("id"), 0);
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
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//this indicates where the entire search result for compound was empty!
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
			else 
			{
				noValues = true; //No values entered at all!!!
			}
		}
	}//End of method searchCompound	
	
	
	/**
	 * The method performs a search for samples.
	 * The search is limited by compound ids, found in the search for compounds.
	 * There is created a list of compound ids and names
	 * and a second list of details on individual samples.
	 *
	 */
	public void searchSampleDataNoAnalysisFields()
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
					
					String sql = "SELECT" +
					" compound.chemical_name," +
					" compound.id," +
					" sample.id," +
					" sample.batch," +
					" sample.remark," +
					" sample.created_date,"+
					" sample.created_by"+
					" FROM" +
					" sample" +
					" LEFT JOIN compound ON sample.compound_id = compound.id" +
					" AND compound.id in ("+createListOfCompoundIds(result_id)+")" +
					" " + sampleRegDate() +
					" " + sampleRegBy() +
					" order by 2, 1, 3";
					
					ResultSet result = stmt.executeQuery(sql);
					
					noAnaResults.clear();
					noAnaResultsId.clear();
					int workingComp = 0;
					String compound_name = "";
					
					while(result.next())
					{
						int dbComp = result.getInt("compound.id");
						
						if(workingComp != dbComp)
						{
							workingComp = dbComp;
							compound_name = Util.encodeNullValue(result.getString("compound.chemical_name"));
							
							//fill entry into the list containing id and name of the compound
							noAnaResultsId.add(result.getString("compound.id")+"|"+compound_name);
						}
						
						//fetch and encode the string values of the result
						String batch = Util.encodeNullValue(result.getString("sample.batch"));
						String remark = Util.encodeNullValue(result.getString("sample.remark"));
						
						//create lists holding information about each compound
						noAnaResults.add(result.getString("compound.id")+"|"+result.getString("sample.id")+"|"+result.getString("sample.created_date")+"|"+result.getString("sample.created_by")+"|"+batch+"|"+remark);
						
					}//end while loop running through the result set.
				}
				con.close();
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
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
	 * Create the html ready to be displayed. This method
	 * is invoked when a search does not include any analysis data.
	 *
	 */
	public Vector createHtmlNoAnalysis()
	{
		Vector html_list = new Vector();
		
		for (int i = 0; i<noAnaResultsId.size(); i++)
		{
			String comp_data = (String) noAnaResultsId.get(i);
			String comp_id = "";
			String comp_name = "";
			String the_html = "";
			
			StringTokenizer tokens1 = new StringTokenizer(comp_data, "|");
			int comp_counter = 0;
			
			while(tokens1.hasMoreTokens())
			{
				comp_counter++;
				String comp_element = tokens1.nextToken().trim();
				
				if(comp_counter == 1)
				{
					comp_id = comp_element;
				}
				if(comp_counter == 2)
				{
					comp_name = comp_element;
				}
			}
			
			the_html = "<tr class=\"blue\"><th class=\"blue\" colspan=\"6\">"+encodeNameToBox(comp_name, comp_id, 1)+"</td></tr>";
			the_html = the_html + "<tr>" +
			" <th width=\"15%\" class=\"blue\">Sample id</th>" +
			" <th width=\"15%\" class=\"blue\">Created date</th>" +
			" <th width=\"15%\" class=\"blue\">Created by</th>" +
			" <th width=\"15%\" class=\"blue\">Batch</th>" +
			" <th width=\"40%\" class=\"blue\">Remark</th>" +
			" </tr>";
			
			for (Iterator iter = noAnaResults.iterator(); iter.hasNext();) {
				String data = (String) iter.next();
				
				StringTokenizer tokens = new StringTokenizer(data, "|");
				int counter = 0;
				
				while(tokens.hasMoreTokens())
				{
					counter++;
					String element = tokens.nextToken().trim();
					
					//first test if the element in the second list is of the same compound as the one we are working on!
					if(counter == 1)
					{
						if(!element.equals(comp_id))
						{
							break;
						}		
						else
						{
							the_html = the_html + "<tr>";
						}
					}
					if(counter==2)
					{
						the_html = the_html + "<td align=\"center\"><a class=\"black_u\" href=\"#\" onclick=\"openWindow(\'"+base+"?action=display_sample&sample_id="+element+"\')\">"+element+"</a></td>";
					}
					if(counter==3)
					{
						the_html = the_html + "<td>"+element+"</td>";
					}
					if(counter==4)
					{
						the_html = the_html + "<td>"+element+"</td>";
					}
					if(counter==5)
					{
						the_html = the_html + "<td>"+element+"</td>";
					}
					if(counter==6)
					{
						the_html = the_html + "<td>"+element+"</td></tr>";
						iter.remove();
						break;
					}					
				}
			}//end for list of samples..
			
			html_list.add(the_html);
			
		}//end for list of compounds
		
		return html_list;
	}
	
	
	
	/**
	 * The sample data is here retrieved...
	 *
	 *The sql is created based on incoming search criteria, the compounds found in the compound search,
	 *and finally list is created to be used in creation of the html to show on the page.
	 */
	public void searchSampleData(boolean useCompounds)
	{
		
		/*
		 * Variables to hold values during the method
		 */
		String r_chemical_name = "";
		String r_compound_id = "";
		String r_text_id = "";
		String r_field_id = "";
		String r_sample_id = "";
		String r_analysis_id = "";
		String r_analysis_name = "";
		String r_analysis_version = "";
		String r_result = "";
		String r_unit = "";
		String order_by = 	" ORDER BY" +
		" 1, 6, 5, 3, 9;";
		
		String main_query = " SELECT" +
		" compound.chemical_name," +
		" compound.id," +
		" analysis_fields.text_id," +
		" analysis_fields.id," +
		" sample.id," +
		" analysis.analysis_id," +
		" analysis.analysis_name," +
		" analysis.version," +
		" result.reported_value," +
		" result.unit," +
		" analysis_fields.result_type" +
		" FROM " +
		" analysis_fields," +
		" sample," +
		" analysis," +
		" result" +
		" LEFT JOIN compound ON sample.compound_id = compound.id" +
		" WHERE analysis_fields.analysis_id = analysis.analysis_id" +
		" AND analysis_fields.analysis_version = analysis.version" +
		" AND result.sample_id = sample.id" +
		" AND result.analysis_field_id = analysis_fields.id"+
		" " + sampleRegDate() +
		" " + sampleRegBy();
		
		if(useCompounds)
			main_query = main_query + " AND compound.id in ("+createListOfCompoundIds(result_id)+")";
		
		//lists to hold the results
		compound_map = new HashMap();
		analysis_list = new Vector();
		sample_list = new Vector();
		details_list = new Vector();
		
		//booleans used to determine what analysis fields are filled in for search
		boolean q1 = false;
		boolean q2 = false;
		boolean q3 = false;
		
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
					
					String query1 = null;
					String query2 = null;
					String query3 = null;
					
					//create sql for the analysis in box 1
					if(search_field1 != null && !search_field1.equals(""))
					{
						query1 = main_query;
						
						//perform the search in the text values					
						if(type1.equals("text"))
						{
							query1 = query1 + " AND result.text_value like ('%"+search_field1+"%')";
							
//							perform the search on specific fields
							if(id_field1 != null || !id_field1.equals(""))
							{
								query1 = query1 + " AND analysis_fields.id in ("+id_field1+")";
							}
						}
						else if(type1.equals("numeric"))//only for numeric values
						{
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field1))
							{
								query1 = query1 + " AND result.numeric_value "+Util.decodeOperator(option1)+" "+search_field1;
								query1 = query1 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields
								if(id_field1 != null || !id_field1.equals(""))
								{
									query1 = query1 + " AND analysis_fields.id in ("+id_field1+")";
								}
							}
						}
						else//for both numeric and text HERE a union has to be used to get correct result
						{
							query1 = query1 + " AND result.text_value like ('%"+search_field1+"%')";
							
//							perform the search on specific fields
							if(id_field1 != null || !id_field1.equals(""))
							{
								query1 = query1 + " AND analysis_fields.id in ("+id_field1+")";
							}
							
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field1))
							{
								/*
								 * To make it possible to combine numeric and text search create an union here.
								 */
								query1 = query1 + " UNION"+ main_query + " AND result.numeric_value "+Util.decodeOperator(option1)+" "+search_field1;
								query1 = query1 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields
								if(id_field1 != null || !id_field1.equals(""))
								{
									query1 = query1 + " AND analysis_fields.id in ("+id_field1+")";
								}
							}
							
						}//end else, search for both text and numeric
						
						q1 = true;
					}
					
					//create sql for second box
					if(search_field2 != null && !search_field2.equals(""))
					{						
						query2 = main_query;
						
						//perform the search in the text values					
						if(type2.equals("text"))
						{
							query2 = query2 + " AND result.text_value like ('%"+search_field2+"%')";
							
//							perform the search on specific fields
							if(id_field2 != null || !id_field2.equals(""))
							{
								query2 = query2 + " AND analysis_fields.id in ("+id_field2+")";
							}
						}
						else if(type2.equals("numeric"))//only for numeric values
						{
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field2))
							{
								query2 = query2 + " AND result.numeric_value "+Util.decodeOperator(option2)+" "+search_field2;
								query2 = query2 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields
								if(id_field2 != null || !id_field2.equals(""))
								{
									query2 = query2 + " AND analysis_fields.id in ("+id_field2+")";
								}
							}
						}
						else//for both numeric and text
						{
							query2 = query2 + " AND result.text_value like ('%"+search_field2+"%')";
							
//							perform the search on specific fields
							if(id_field2 != null || !id_field2.equals(""))
							{
								query2 = query2 + " AND analysis_fields.id in ("+id_field2+")";
							}
							
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field2))
							{
								query2 = query2 + " UNION"+ main_query + " AND result.numeric_value "+Util.decodeOperator(option2)+" "+search_field2;
								query2 = query2 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields
								if(id_field2 != null || !id_field2.equals(""))
								{
									query2 = query2 + " AND analysis_fields.id in ("+id_field2+")";
								}
							}
						}
						
						q2 = true;
					}
					
					//create the sql for the third search box
					if(search_field3 != null && !search_field3.equals(""))
					{
						query3 = main_query;
						
						//perform the search in the text values					
						if(type3.equals("text"))
						{
							query3 = query3 + " AND result.text_value like ('%"+search_field3+"%')";
							
//							perform the search on specific fields						
							if(id_field3 != null || !id_field3.equals(""))
							{
								query3 = query3 + " AND analysis_fields.id in ("+id_field3+")";
							}
						}
						else if(type3.equals("numeric"))//only for numeric values
						{
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field3))
							{
								query3 = query3 + " AND result.numeric_value "+Util.decodeOperator(option3)+" "+search_field3;
								query3 = query3 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields						
								if(id_field3 != null || !id_field3.equals(""))
								{
									query3 = query3 + " AND analysis_fields.id in ("+id_field3+")";
								}
							}
						}
						else//for both numeric and text
						{
							query3 = query3 + " AND result.text_value like ('%"+search_field3+"%')";
							
//							perform the search on specific fields						
							if(id_field3 != null || !id_field3.equals(""))
							{
								query3 = query3 + " AND analysis_fields.id in ("+id_field3+")";
							}
							
							//the search for numeric values is only performed of the entered value is actually a number
							if(Util.isValidNumber(search_field3))
							{
								query3 = query3 + " UNION"+ main_query + " AND result.numeric_value "+Util.decodeOperator(option3)+" "+search_field3;
								query3 = query3 + " AND result.text_value = ''";//make sure that no text values is included
								
//								perform the search on specific fields						
								if(id_field3 != null || !id_field3.equals(""))
								{
									query3 = query3 + " AND analysis_fields.id in ("+id_field3+")";
								}
							}
						}
						
						q3 = true;
					}
					
					//build the final query
					//this step is combining the above three queries and performing union and order by
					//Only using analysis search data from selected analyis
					String query = "";
					
					if(q1 == true)
					{
						query = query1;
						
						if(q2 == true)
						{
							query = query + " UNION " + query2;
						}
						
						if(q3 == true)
						{
							query = query + " UNION " +query3;
						}					
					}
					else if(q2 == true && q1 == false)
					{
						query = query2;
						
						if(q3 == true)
						{
							query = query + " UNION " +query3;
						}					
						
					}
					if(q1 == false && q2 == false && q3 == true)
					{
						query = query3;
					}					
					
//					there was not any values in the 3 text boxes, if there is any values in the id fields include these
					if(q1== false && q2 == false && q3 == false)
					{
						//test to see if any of the id_fields is filled out
						if(id_field1 != null && !id_field1.equals(""))//values from ids 1
						{
							q1 = true;
						}
						if(id_field2 != null && !id_field2.equals(""))//values from ids 2
						{
							q2 = true;
						}
						if(id_field3 != null && !id_field3.equals(""))//values from ids 3
						{
							q3 = true;
						}
						
						//using the information about the idfields create the html
						if(q1 == true)
						{
							query = main_query + " AND analysis_fields.id in ("+id_field1+")";
							
							if(q2 == true)
							{
								query = query + " UNION " + main_query + " AND analysis_fields.id in ("+id_field2+")";
							}
							
							if(q3 == true)
							{
								query = query + "UNION " + main_query + " AND analysis_fields.id in ("+id_field3+")";
							}					
						}
						else if(q2 == true && q1 == false)
						{
							query = main_query + " AND analysis_fields.id in ("+id_field2+")";
							
							if(q3 == true)
							{
								query = query + " UNION " + main_query + " AND analysis_fields.id in ("+id_field3+")";
							}					
							
						}
						if(q1 == false && q2 == false && q3 == true)
						{
							query = main_query + " AND analysis_fields.id in ("+id_field3+")";
						}
					}
					
					query = query + order_by;
					
					/*execute the query in the database*/
					ResultSet rs = stmt.executeQuery(query);
					
					int counter = 0;
					
					int working_id = 0;
					int working_analysis_id = 0;
					boolean new_compound = false;
					boolean new_analysis = false;
					
					//loop through the received results
					while (rs.next())
					{
						counter++;
						
						int new_id = rs.getInt("compound.id");
						int new_analysis_id = rs.getInt("analysis.analysis_id");
						
						//only include information on samples linked to a compound:
						
						if(new_id != 0)
						{
							/*
							 * Find out if we are working 
							 * on the same compound as the 
							 * privious loop, or we have
							 * come to a new compound
							 */
							if(new_id != working_id)//we have started gathering data from a new compound
							{
								working_id = new_id;
								new_compound = true;
							}
							else // we are still working on the same compound
							{
								new_compound = false;
							}
							
							/*
							 * Find out if we are working 
							 * on the same analysis as the 
							 * privious loop, or we have
							 * come to a new analysis
							 */
							if(new_analysis_id != working_analysis_id)//we have started gathering data from a new analysis
							{
								working_analysis_id = new_analysis_id;
								new_analysis = true;
							}
							else // we are still working on the same analysis
							{
								new_analysis = false;
							}
							
							if(new_compound)//new compound get all fields
							{
								r_chemical_name = rs.getString("compound.chemical_name");
								r_compound_id = rs.getString("compound.id");
								
								//encode tag and null value
								r_chemical_name = Util.encodeTag(r_chemical_name);
								r_chemical_name = Util.encodeNullValue(r_chemical_name);
								
								//add this entry to the list of compounds
								compound_map.put(r_compound_id, r_chemical_name);
							}
							
//							if(new_analysis)//working on a new analysis
//							{
							r_analysis_id = rs.getString("analysis.analysis_id");
							r_analysis_name = rs.getString("analysis.analysis_name");
							
							//encode tag and null value
							r_analysis_name = Util.encodeTag(r_analysis_name);
							r_analysis_name = Util.encodeNullValue(r_analysis_name);
							
							if(!analysis_list.contains(r_compound_id+"|"+r_analysis_id+"|"+r_analysis_name))
							{
								//add the analysis info to a list for this info
								analysis_list.add(r_compound_id+"|"+r_analysis_id+"|"+r_analysis_name);
							}
//							}
							
							
							r_sample_id = rs.getString("sample.id");
							String sample_insert = r_compound_id+"|"+r_analysis_id+"|"+r_sample_id;
							
							if(!sample_list.contains(sample_insert))
							{
								//add the sample information to a sample list
								sample_list.add(sample_insert);
								
							}								
							
							//the rest of the fields is different in each loop
							r_text_id = rs.getString("analysis_fields.text_id");
							r_field_id = rs.getString("analysis_fields.id");
							r_analysis_version = rs.getString("analysis.version");
							r_result = rs.getString("result.reported_value");
							r_unit = rs.getString("result.unit");			
							
							//encode text values
							r_text_id = Util.encodeTag(r_text_id);
							r_result = Util.encodeTag(r_result);
							
							//encode any null values
							r_text_id = Util.encodeNullValue(r_text_id);
							r_result = Util.encodeNullValue(r_result);
							
							//add the rest of the information to a list of details
							details_list.add(r_compound_id+"|"+r_analysis_id+"|"+r_sample_id+"|"+r_text_id+"|"+r_field_id+"|"+r_result+"|"+r_unit);
						}
						else
						{
							//here handle samples not connected to a compound
						}
					}
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
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("error 3: "+e);
		}
	}
	
	/**
	 * This method creates the html based on the serch performed for samples.
	 * @return Vector holding the serarch result ready to be displayed
	 */
	public Vector createTheHtml()
	{			
		try {
			
			Vector search_result = new Vector();
			
			//start to run through the list of compounds on the map
			for (Iterator iter = compound_map.entrySet().iterator(); iter.hasNext();) {
				
				Map.Entry e = (Map.Entry) iter.next();
				
				String html_compound = "";
				String html_analysis = "";
				String html_detail_header = "<tr>\n" +
				"<th width=\"20%\" class=\"blue\">Sample Id</th>\n" +
				"<th width=\"40%\" class=\"blue\">Text Id</th>\n" +
				"<th width=\"25%\" class=\"blue\">Result</th>\n" +
				"<th width=\"15%\" class=\"blue\">Unit</th>\n" +
				"</tr>\n";
				
				String html_details_start = "<tr><td rowspan=\"";
				String html_details_end = "";					
				String compound_key = (String) e.getKey();
				String compound_value = (String) e.getValue();//value_array[0];
				
				/* ***** The html is started... here is the name of the compound displayed ****** */
				html_compound = "<tr><th class=\"blue\" colspan=\"4\">"+encodeNameToBox(compound_value, compound_key, 1)+"</th></tr>";
				
				//run through the map of analysis' and find analysis that is connected to the compound
				for (Iterator iterator = analysis_list.iterator(); iterator.hasNext();) {
					
					int classCounter = 0;
					String a_element = (String) iterator.next();
					String a_name = "";
					String a_id = "";
					
					String element_list_builder = "";
					
					boolean results = false;
					
					boolean same_comp = false;
					int token_counter = 0;
					StringTokenizer tokenizer = new StringTokenizer(a_element, "|");
					
					while(tokenizer.hasMoreTokens())
					{
						token_counter++;
						String token = tokenizer.nextToken().trim();
						
						if(token_counter == 1)//the firste element is the compound id
						{
							//check to see if the compound id in the map and analysis list is equal
							if(token.equalsIgnoreCase(compound_key))//the same compound as reg. to this analysis
							{
								same_comp = true;
							}
							else//compound and analysis, not a match.
							{
								same_comp = false;
								break;//break out of the while and go to the next analysis entry
							}
						}
						
						//compound id is identical on the compound and analysis
						if(same_comp)
						{
							if(token_counter == 2)//the second element is the analysis_id;
							{
								a_id = token;
							}
							if(token_counter == 3)//the third element is the name of the analysis
							{
								a_name = token;
								
								//link to show information about the analysis
								String link = "<a class=\"black\" target=\"blank\" href=\""+base+"?action=display_analysis&analysis_id="+a_id+"\">"+Util.encodeTag(a_name)+"</a>";
								
//								*****************create the html header holding the name of the analyis*****************
								html_analysis = "<tr><th class=\"blue\" colspan=\"4\">"+link+"</th></tr>";
								html_compound = html_compound + html_analysis + html_detail_header;
							}
							
							if(same_comp && token_counter == 3)
							{
								//run through the list of samples and see if there is any samples for this analysis...
								for (Iterator itr = sample_list.iterator(); itr.hasNext();) {
									
									String s_element = (String) itr.next();
									
									String s_id = "";
									boolean same_ana = false;
									boolean same_ana_comp = false;
									int token_counter_sample = 0;
									StringTokenizer nizer = new StringTokenizer(s_element, "|");
									
									while(nizer.hasMoreTokens())
									{
										token_counter_sample++;
										String sample_token = nizer.nextToken().trim();
										
										//is the analysis id equeal to the analysis id on the sample list?
										if(token_counter_sample == 1)//the firste element is the compound id
										{
											//check to see if the compound id in the map and analysis list is equal
											
											if(sample_token.equalsIgnoreCase(compound_key))//the same compound as reg. to this analysis
											{	same_ana_comp = true;
											}
											else
											{
												same_ana_comp = false;
												break;
											}
										}
										
										if(token_counter_sample == 2)
										{
											if(sample_token.equalsIgnoreCase(a_id))//is the analysis ids identical
											{
												//we are here because the compound id and analysis ids are identical now proceed
												same_ana = true;
											}
											else
											{
												same_ana = false;
												break;
											}
										}
										
										//compound id is  and the analysis id's identical on the compound, analysis and sample
										if(same_ana && same_ana_comp && token_counter_sample == 3)
										{
											classCounter++;
											
											String color = "normal";
											if(classCounter % 2 == 0)
											{
												color = "smoke";
											}
											
											if(token_counter_sample == 3)
											{
												s_id = sample_token;//now we also has the sample id
											}
											
											//html for a sample detial row started here
											html_details_end = "\"><a class=\"black_u\" href=\"#\" onclick=\"openWindow(\'"+base+"?action=display_sample&sample_id="+s_id+"\')\">"+s_id+"</a></td>";
											
											int rowspan = 0;
											
											//now get the rest of the information from the details list:
											for (Iterator ite = details_list.iterator(); ite.hasNext();) {
												
												String d_element = (String) ite.next();
												
												boolean details_comp = false;
												boolean details_ana = false;
												boolean details_sample = false;
												
												int token_counter_details = 0;
												StringTokenizer str_tokenizer = new StringTokenizer(d_element, "|");
												
												while(str_tokenizer.hasMoreTokens())
												{
													token_counter_details++;
													
													html_details_start = "<tr class=\""+color+"\"><td align=\"center\" rowspan=\"";												
													
													String details_token = str_tokenizer.nextToken().trim();
													
													//is the analysis id equeal to the analysis id on the sample list?
													if(token_counter_details == 1)//the first element is the compound id
													{												
														//check to see if the compound ids are the same
														if(details_token.equalsIgnoreCase(compound_key))//the same compound as reg. to this analysis
														{
															details_comp = true;
														}
														else
														{
															details_comp = false;
															break;
														}
													}
													
													//now check to see if the two analysis ids are the same
													if(token_counter_details == 2)//second elemement is analysis id
													{	
														//check to see if the analysis ids are the same
														if(details_token.equalsIgnoreCase(a_id))//the same analysis ids
														{
															details_ana = true;
														}
														else
														{
															details_ana = false;
															break;
														}
													}
													
													//now check to see if the two sample ids are the same
													if(token_counter_details == 3)//third element is sample id
													{								
														//check to see if the sample ids are the same
														if(details_token.equalsIgnoreCase(s_id))//the same sample ids
														{
															details_sample = true;
														}
														else
														{
															details_sample = false;
															break;
														}
													}
													
													//if all the boolean checks was ok create the html....
													if(details_ana && details_comp && details_sample)
													{
														//get the text id from the token
														if(token_counter_details == 4)
														{	
															html_details_end = html_details_end + "<td>"+details_token+"</td>\n";
														}
														//												get the field from the token
														if(token_counter_details == 5)
														{
															String d_field_id = details_token;
														}
														//												get the result from the token
														if(token_counter_details == 6)
														{	
															html_details_end = html_details_end + "<td>"+details_token+"</td>\n";
														}
//														get the unit from the token
														if(token_counter_details == 7)
														{													
															rowspan++;
															
															if(rowspan > 1)
															{
																//first reset the element list
																element_list_builder = "";
															}
															
															html_details_end = html_details_end + "<td>"+details_token+"</td>";
															html_details_end = html_details_end + "</tr>\n<tr class=\""+color+"\">";
															
															html_details_start = html_details_start + rowspan + html_details_end +"\n";
															
															element_list_builder = element_list_builder + html_details_start;
															
															results = true;
															
															ite.remove();
															
															break;
														}
														
													}//end create details for a new result line on this sample
													
												}//end while running through a details string
												
											}//end for loop DETAILS
											
										}//end if from analysis check on compound and analysis ids
										
									}//end while loop in analysis token
									
									/*
									 * Here the header = chemical name
									 * and analysis name 
									 * and the current sample info (details row) is
									 * added, in the final run through
									 * creating a fill table row
									 * with the chemical name
									 * 			analysis name
									 * 			sample data
									 * 			sample data
									 * 			sample data
									 * 			.
									 * 			.	
									 * 			.
									 */
									if(results)
									{
										html_compound = html_compound + element_list_builder;
										element_list_builder = "";
									}
									
								}//end for loop analysis
								
							}//if same compound and token 3...
							
						}//if we are using the same compound and analysis...
						
					}//while string tokenizer in analysis
					
					
				}//for loop analysis_list
				
				//System.out.println("HER KOMMER EN gang details: "+html_compound + html_details_start);
				
				//add one list of search result to the list
				search_result.add(html_compound);
				
			}//for loop on compounds...
			
			return search_result;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	/**
	 * Create the sql for selection of sample search criteria in register date
	 * @return
	 */
	public String sampleRegDate()
	{
		String sql = "";
		
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
				sql = sql + "AND sample.created_date IS NULL ";
			else
			{
				if(register_date.toUpperCase().equals(notempty))
					sql = sql + "AND sample.created_date IS NOT NULL ";
				else
					sql = sql + "AND sample.created_date "+operator1+" '"+register_date.toUpperCase()+"' ";
			}  
		}
		
		return sql;
	}
	
	
	/**
	 * Create the sql for sample created by user...
	 * @return
	 */
	public String sampleRegBy()
	{
		String sql = "";
		
		if(register_by.length() > 0)
		{			
			sql = " AND sample.created_by like '%"+register_by+"%'";
		}
		
		return sql;
	}
	
	/**
	 * Combining the different methods, a search for compounds, samples and results is performed.
	 * The final result is entered into the vector "sample_result", which
	 * holds, rows of data ready to be displayed on the html search page.
	 */
	public void performTheSearch()
	{
		//Perform the search in the structure table
		
		String structure_result = searchStructure();
		
		//Perform a search in the compound table, with text based
		//information and the result from the structure data.
		
		searchCompound(structure_result);
		
		//the rest of the sample search is done here...
		
		//check if there is any results in the compound search
		//else search only for samples not connected to a compound
		
		/*
		 * If no search criteria for analysis' has been entered:
		 * 
		 * Perform the search for all samples, all fields, all versions.
		 */
		
		sample_result.clear();
		
		if(hit)
		{
			if((id_field1 != null && !id_field1.equals("")) || (id_field2 != null && !id_field2.equals("")) ||(id_field3 != null && !id_field3.equals("")))
			{
				//search for samples connected to compound
				searchSampleData(true);
				
				//create the html for the individual tables with data
				sample_result = createTheHtml();
			}
			else
			{
				/*
				 * No criteria for the search of analysis has been entered.
				 * In this case we show a list of compounds, and the
				 * samples connected to the individual compound.
				 * Here selecting a sample id (a link)
				 * should open up a new window to the user with the
				 * details of this sample
				 */
				
				//Search for samples connected to one of the compounds in the compound list
				searchSampleDataNoAnalysisFields();
				
				//create the html
				sample_result = createHtmlNoAnalysis();
			}
		}
		else
		{
			/*
			 * there was no hits for compounds
			 * Is there entered any values to search in analysis, samples or results??
			 * 
			 * yes: perform the search in all compounds....
			 * 
			 * no: this means no compound found, and no analysis_fields
			 * to search in... abort..
			 */
			
			if((id_field1 != null && !id_field1.equals("")) || (id_field2 != null && !id_field2.equals("")) ||(id_field3 != null && !id_field3.equals("")))
			{
				
				//Search for samples and details.
				//we have received specific analyis' to search for, but there was no hits in compounds
				//(or no compound search data entered....
				//therefore the compound id is to be omitted from the search for sample/analysis data
				searchSampleData(false);
				
				//create the html for the individual tables with data
				sample_result = createTheHtml();
			}
			else
			{
				//no hits for compounds, and no analysis data entered....
			}
		}
	}
	
	
	/**
	 * Create an overLib box with option to 
	 * show normal details or advanced details
	 * on the compound.
	 * @param name
	 * @param compund_id
	 * @return The string with the box code
	 */
	public String encodeNameToBox(String name, String compound_id, int underline)
	{
		String box = "";
		String css_class = "black_u";
		
		if(underline == 0)//UNDERLINE the link
			css_class = "black_u";
		else
			css_class = "black";
		
		
		if(name != null && !name.equals("") && compound_id != null && !compound_id.equals(""))
		{
			box = "<a class=\""+css_class+"\" href=\"javascript:void(0);\" onmouseover=\"return overlib(\'Display information on this compound: <br/> <a href="+base+"?action=details&id="+compound_id+"" +
			" target=blank>Simple Info</a><br/><a target=Main href="+base+"?action=ResultPage&id="+compound_id+">Detailed Info</a>\', ABOVE, STICKY, BORDER, 2, CAPTION, \'COMPOUND INFO');\" onmouseout=\"return nd();\">"+name+"</a>";
		}
		else 
			box = name;
		
		return box;
	}
	
	
	/**
	 * Create a string list using a vector of ids
	 * @param ids
	 * @return String the list of compound ids..
	 */
	public String createListOfCompoundIds(Vector ids)
	{
		String list = "";
		
		for (int i = 0; i<ids.size(); i++) {
			String element = (String) ids.get(i);
			element = element.trim();
			
			if(i == 0)
			{
				list = element; 
			}
			else
			{
				list = list + ", "+element;
			}
		}
		
		return list;
	}
	
	
	/**
	 * Create a list of all samples in the system that is not connected to a compound.
	 */
	public void findIndependentSamples()
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
					
					String sql = "SELECT sample.id,  sample.batch,  sample.remark, sample.created_date,  sample.created_by" +
					" FROM sample" +
					" WHERE sample.compound_id = 0" +
					" ORDER BY sample.id";
					
					ResultSet result = stmt.executeQuery(sql);
					
					sample_list = new Vector();
					
					while(result.next())
					{
						String html = "";
						String batch = Util.encodeNullValue(result.getString("sample.batch"));
						String rem = Util.encodeNullValue(result.getString("sample.remark"));
						rem = Util.encodeTag(rem);
						
						html = "<td width=\"100px\" valign=\"center\" align=\"center\">"+result.getString("sample.id")+"</td>";
						html = html + "<td width=\"125px\" valign=\"center\">"+rem+"</td>";
						html = html + "<td width=\"50px\" align=\"center\" valign=\"center\"><br><form method=\"post\" action=\"\" name=\"sample\"><input class=\"submit_nowidth\" type=\"submit\" value=\"Show\" onclick=\"openWindow(\'"+base+"?action=display_sample&sample_id="+result.getString("sample.id")+"\')\"/></form></td>";
						
						sample_list.add(html);
					}
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
			System.out.println(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}  
	}
	
	/**
	 * Create a list of all samples that is attached to a compound
	 */
	public void findDependentSamples()
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
					
					String sql = "SELECT sample.id," +
					" sample.batch," +
					" sample.remark," +
					" sample.created_date," +
					" sample.created_by" +
					" FROM sample" +
					" WHERE sample.compound_id <> 0" +
					" ORDER BY sample.id";
					
					ResultSet result = stmt.executeQuery(sql);
					
					sample_list = new Vector();
					
					while(result.next())
					{
						String html = "";
						String batch = Util.encodeNullValue(result.getString("sample.batch"));
						String rem = Util.encodeNullValue(result.getString("sample.remark"));
						rem = Util.encodeTag(rem);
						
						html = "<td width=\"100px\" valign=\"center\" align=\"center\">"+result.getString("sample.id")+"</td>";
						html = html + "<td width=\"125px\" valign=\"center\">"+rem+"</td>";
						html = html + "<td width=\"50px\" align=\"center\" valign=\"center\"><br><form method=\"post\" action=\"\" name=\"sample\"><input class=\"submit_nowidth\" type=\"submit\" value=\"Show\" onclick=\"openWindow(\'"+base+"?action=display_sample&sample_id="+result.getString("sample.id")+"\')\"/></form></td>";
						
						sample_list.add(html);
					}
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
			System.out.println(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}  
	}
	
	/**
	 * @return Returns the casNumber.
	 */
	public String getCasNumber() {
		return casNumber;
	}
	/**
	 * @param casNumber The casNumber to set.
	 */
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	/**
	 * @return Returns the cd_formula.
	 */
	public String getCd_formula() {
		return cd_formula;
	}
	/**
	 * @param cd_formula The cd_formula to set.
	 */
	public void setCd_formula(String cd_formula) {
		this.cd_formula = cd_formula;
	}
	/**
	 * @return Returns the cd_molweight.
	 */
	public String getCd_molweight() {
		return cd_molweight;
	}
	/**
	 * @param cd_molweight The cd_molweight to set.
	 */
	public void setCd_molweight(String cd_molweight) {
		this.cd_molweight = cd_molweight;
	}
	/**
	 * @return Returns the chemicalName.
	 */
	public String getChemicalName() {
		return chemicalName;
	}
	/**
	 * @param chemicalName The chemicalName to set.
	 */
	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}
	/**
	 * @return Returns the density.
	 */
	public String getDensity() {
		return density;
	}
	/**
	 * @param density The density to set.
	 */
	public void setDensity(String density) {
		this.density = density;
	}
	/**
	 * @return Returns the empty.
	 */
	public String getEmpty() {
		return empty;
	}
	/**
	 * @param empty The empty to set.
	 */
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	/**
	 * @return Returns the id_field1.
	 */
	public String getId_field1() {
		return id_field1;
	}
	/**
	 * @param id_field1 The id_field1 to set.
	 */
	public void setId_field1(String id_field1) {
		this.id_field1 = id_field1;
	}
	/**
	 * @return Returns the id_field2.
	 */
	public String getId_field2() {
		return id_field2;
	}
	/**
	 * @param id_field2 The id_field2 to set.
	 */
	public void setId_field2(String id_field2) {
		this.id_field2 = id_field2;
	}
	/**
	 * @return Returns the id_field3.
	 */
	public String getId_field3() {
		return id_field3;
	}
	/**
	 * @param id_field3 The id_field3 to set.
	 */
	public void setId_field3(String id_field3) {
		this.id_field3 = id_field3;
	}
	/**
	 * @return Returns the molfile.
	 */
	public String getMolfile() {
		return molfile;
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
	/**
	 * @return Returns the operator1.
	 */
	public String getOperator1() {
		return operator1;
	}
	/**
	 * @param operator1 The operator1 to set.
	 */
	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}
	/**
	 * @return Returns the operator2.
	 */
	public String getOperator2() {
		return operator2;
	}
	/**
	 * @param operator2 The operator2 to set.
	 */
	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}
	/**
	 * @return Returns the operator3.
	 */
	public String getOperator3() {
		return operator3;
	}
	/**
	 * @param operator3 The operator3 to set.
	 */
	public void setOperator3(String operator3) {
		this.operator3 = operator3;
	}
	/**
	 * @return Returns the ord_by.
	 */
	public String getOrd_by() {
		return ord_by;
	}
	/**
	 * @param ord_by The ord_by to set.
	 */
	public void setOrd_by(String ord_by) {
		this.ord_by = ord_by;
	}
	/**
	 * @return Returns the register_by.
	 */
	public String getRegister_by() {
		return register_by;
	}
	/**
	 * @param register_by The register_by to set.
	 */
	public void setRegister_by(String register_by) {
		this.register_by = register_by;
	}
	/**
	 * @return Returns the register_date.
	 */
	public String getRegister_date() {
		return register_date;
	}
	/**
	 * @param register_date The register_date to set.
	 */
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	/**
	 * @return Returns the result_name.
	 */
	public Vector getResult_name() {
		return result_name;
	}
	/**
	 * @param result_name The result_name to set.
	 */
	public void setResult_name(Vector result_name) {
		this.result_name = result_name;
	}
	/**
	 * @return Returns the search_field1.
	 */
	public String getSearch_field1() {
		return search_field1;
	}
	/**
	 * @param search_field1 The search_field1 to set.
	 */
	public void setSearch_field1(String search_field1) {
		
		this.search_field1 = search_field1;
	}
	/**
	 * @return Returns the search_field2.
	 */
	public String getSearch_field2() {
		return search_field2;
	}
	/**
	 * @param search_field2 The search_field2 to set.
	 */
	public void setSearch_field2(String search_field2) {
		
		this.search_field2 = search_field2;
	}
	/**
	 * @return Returns the search_field3.
	 */
	public String getSearch_field3() {
		return search_field3;
	}
	/**
	 * @param search_field3 The search_field3 to set.
	 */
	public void setSearch_field3(String search_field3) {	
		this.search_field3 = search_field3;
	}
	/**
	 * @return Returns the search_method.
	 */
	public String getSearch_method() {
		return search_method;
	}
	/**
	 * @param search_method The search_method to set.
	 */
	public void setSearch_method(String search_method) {
		this.search_method = search_method;
	}
	/**
	 * @return Returns the statement.
	 */
	public String getStatement() {
		return statement;
	}
	/**
	 * @param statement The statement to set.
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	/**
	 * @return Returns the countHit.
	 */
	public int getCountHit() {
		return countHit;
	}
	/**
	 * @return Returns the exceedFrq.
	 */
	public boolean isExceedFrq() {
		return exceedFrq;
	}
	/**
	 * @return Returns the history.
	 */
	public boolean isHistory() {
		return history;
	}
	/**
	 * @return Returns the hit.
	 */
	public boolean isHit() {
		return hit;
	}
	/**
	 * @return Returns the molSearch.
	 */
	public boolean isMolSearch() {
		return molSearch;
	}
	/**
	 * @return Returns the notempty.
	 */
	public String getNotempty() {
		return notempty;
	}
	/**
	 * @return Returns the noValues.
	 */
	public boolean isNoValues() {
		return noValues;
	}
	/**
	 * @return Returns the result.
	 */
	public Vector getResult() {
		return result;
	}
	/**
	 * @return Returns the result_id.
	 */
	public Vector getResult_id() {
		return result_id;
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
	 * @return Returns the sample_result.
	 */
	public Vector getSample_result() {
		return sample_result;
	}
	
	/**
	 * @return Returns the type1.
	 */
	public String getType1() {
		return type1;
	}
	/**
	 * @param type1 The type1 to set.
	 */
	public void setType1(String type1) {
		this.type1 = type1;
	}
	/**
	 * @return Returns the type2.
	 */
	public String getType2() {
		return type2;
	}
	/**
	 * @param type2 The type2 to set.
	 */
	public void setType2(String type2) {
		this.type2 = type2;
	}
	/**
	 * @return Returns the type3.
	 */
	public String getType3() {
		return type3;
	}
	/**
	 * @param type3 The type3 to set.
	 */
	public void setType3(String type3) {
		this.type3 = type3;
	}
	/**
	 * @return Returns the option1.
	 */
	public String getOption1() {
		return option1;
	}
	/**
	 * @param option1 The option1 to set.
	 */
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	/**
	 * @return Returns the option2.
	 */
	public String getOption2() {
		return option2;
	}
	/**
	 * @param option2 The option2 to set.
	 */
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	/**
	 * @return Returns the option3.
	 */
	public String getOption3() {
		return option3;
	}
	/**
	 * @param option3 The option3 to set.
	 */
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	/**
	 * @return Returns the noAnaResults.
	 */
	public Vector getNoAnaResults() {
		return noAnaResults;
	}
	/**
	 * @return Returns the sample_list.
	 */
	public Vector getSample_list() {
		return sample_list;
	}
}
