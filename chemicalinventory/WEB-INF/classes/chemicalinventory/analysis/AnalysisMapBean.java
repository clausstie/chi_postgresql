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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class AnalysisMapBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3310611559376711784L;
	private String map_name = "";
	private String map_name_original = "";
	private String map_id = "";
	private String user = "";
	private String remark = "";
	private String remark_original = "";
	private String list_of_a = "";
	private String base = "";
	private int code = 900;
	
	private String[] idArray = null;
	private String id_field = "";
	
	private int autoIncKey = -1;
	
	private Vector elements = new Vector();
	private Vector id_elements = new Vector();
	
	AnalysisBean analysis = new AnalysisBean();
	
	/**
	 * 
	 * @return
	 */
	public boolean createMap()
	{
		if(!map_name.equals("") && map_name != null)
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
						
						//register the values received from the client
						String create_map = "INSERT INTO analysis_map" +
						" (map_name, created_by, created_date, remark)" +
						" VALUES('"+map_name+"','"+user+"','"+Util.getDate()+"', '"+remark+"')";
												
						stmt.executeUpdate(create_map, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet keyset = stmt.getGeneratedKeys();
						
						if(keyset.next())
						{
							autoIncKey = keyset.getInt(1);
							map_id = String.valueOf(autoIncKey);
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
		}
		else
			return false;
		
		return false;
	}
	
	/**
	 * Creates the link between a list of analysis (in setter id_field) 
	 * and a analysis_map
	 *  
	 * @return true/false
	 */
	public boolean analysisToMap()
	{		
		if(id_field != null && !id_field.equals(""))
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
						stmt.clearBatch();
						
						String insert = "";
						
						StringTokenizer tokens = new StringTokenizer(id_field, ",");
						
						while(tokens.hasMoreTokens())
						{
							insert = "";
							String token = tokens.nextToken().trim();
							
							insert = "INSERT INTO analysis_map_link (analysis_id, map_id) VALUES ("+token+", "+this.map_id+");";
							stmt.addBatch(insert);	
						}
						
						try {
							stmt.executeBatch();
						} catch (Exception e) {
							con.rollback();
							con.close();
							return false;
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
		}
		else
			return false;
		
		return false;
	}
	
	/**
	 * Set a selected map as active.
	 * 
	 * @param id
	 * @return true/false
	 */
	public boolean activateMap(String id)
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
						
						String update = "UPDATE analysis_map" +
								" SET active = 'T'" +
								" WHERE id = "+id;
						
						stmt.executeUpdate(update);
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
	 * Set the analysis_map as inactive (F).
	 * 
	 * @param id
	 * @return true/False
	 */
	public boolean deActivateMap(String id)
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
						
						String update = "UPDATE analysis_map" +
								" SET active = 'F'" +
								" WHERE id = "+id;			
						
						stmt.executeUpdate(update);
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
	 * Perform the modification of an analysis_map.
	 * 
	 * This is adding or removing analysis' linked to this partiular map.
	 * 
	 * @return true/false
	 */
	public boolean modifyMap()
	{
		if(id_field != null && map_name != null && list_of_a != null)
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
						stmt.clearBatch();
						
						String insert = "";
						
						//insert a new name if the name has changed
						if(!map_name.equals(map_name_original))
						{
							insert = "UPDATE analysis_map SET map_name = '"+map_name+"' WHERE id = "+map_id;
							stmt.addBatch(insert);
						}
						
						//insert the new remark if the remark has changed
						if(!remark.equals(remark_original))
						{
							insert = "UPDATE analysis_map SET remark = '"+remark+"' WHERE id = "+map_id;
							stmt.addBatch(insert);
						}
						
//						update the list of analysis' for the map
//						create two vectors for the insert of new analyis in the list.. or delete old
						
						Vector list_original = new Vector();
						
						StringTokenizer tokens2 = new StringTokenizer(list_of_a, ",");
						while(tokens2.hasMoreTokens())
						{
							String token = tokens2.nextToken().trim();
							list_original.add(token);
						}		
						
						Vector list_new = new Vector();
						
						StringTokenizer tokens = new StringTokenizer(id_field, ",");
						
						while(tokens.hasMoreTokens())
						{
							String token = tokens.nextToken().trim();
							list_new.add(token);
						}
						
						//insert the analysis' that is not there now
		    			for (int n = 0; n < list_new.size(); n++)
	                    {
	                        if(!list_original.contains(list_new.get(n)))
	                        {
	                            //Insert
	                        	insert = "INSERT INTO analysis_map_link (map_id, analysis_id) VALUES ("+map_id+", "+list_new.elementAt(n)+");";
	                            stmt.addBatch(insert);
	                        }
	                    }

		    			//delete analysis' that has been removed.
	                    for (int i = 0; i <list_original.size(); i++)
	                    {
	                        if(!list_new.contains(list_original.elementAt(i)))
	                        {
	                            //Delete
	                            insert = "DELETE FROM analysis_map_link WHERE analysis_id = "+list_original.elementAt(i)+" AND map_id = "+map_id;
	                            stmt.addBatch(insert);
	                        }
	                    }
						
	                    //perform the update of the map.
						try {
							stmt.executeBatch();
						} catch (Exception e) {
							con.rollback();
							con.close();
							return false;
						}
						
//						commit the changes to the db.
						con.commit();
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
		}
		else
			return false;
		
		return false;
	}
	
	/**
	 * Delete the map from the db.
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteMap(String id)
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
					stmt.clearBatch();
					
					String delete = "DELETE FROM analysis_map WHERE id = "+id;
					stmt.addBatch(delete);
					
					delete = "DELETE FROM analysis_map_link WHERE map_id = "+id;
					
					
                    //perform the update of the map.
					try {
						stmt.executeBatch();
					} catch (Exception e) {
						con.rollback();
						con.close();
						return false;
					}
					
//					commit the changes to the db.
					con.commit();					
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
	 * Create the information about a single map.
	 * Map id is received in the setter.
	 * 
	 * @return
	 */
	public boolean getMapInfo()
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
					
						String query = "SELECT analysis_map.id, analysis_map.map_name, analysis_map.remark, analysis.analysis_name, analysis.analysis_id, MAX(analysis.version)" +
								" FROM analysis_map, analysis_map_link, analysis" +
								" WHERE analysis_map.id = analysis_map_link.map_id" +
								" AND analysis_map_link.analysis_id = analysis.analysis_id" +
								" AND analysis_map_link.map_id = "+map_id+
								" AND analysis_map.active = 'T'"+
								" GROUP BY analysis.analysis_name;";
											
					ResultSet rs1 = stmt.executeQuery(query);
				
					int counter = 0;
					elements.clear();
					list_of_a = "";
					
					while(rs1.next())
					{
						if(counter == 0)
						{
							map_name = rs1.getString("analysis_map.map_name");
							remark = rs1.getString("analysis_map.remark");
							
							list_of_a = rs1.getString("analysis.analysis_id");
							counter++;
						}
						else
							list_of_a = list_of_a + "," +rs1.getString("analysis.analysis_id"); 
						
						//fill the lists with the values...
						elements.add(rs1.getString("analysis.analysis_name"));
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
	 * Create a list of all active maps in the system,
	 * adding data to the list id_elements and elements.
	 * 	 
	 * @return true/false 
	 */
	public boolean getMapInfo2()
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
					
					String query = "SELECT distinct(analysis_map.id), analysis_map.map_name" +
							" FROM analysis_map, analysis_map_link" +
							" WHERE analysis_map.id = analysis_map_link.map_id" +
							" AND analysis_map.active = 'T'"+
							" ORDER BY analysis_map.map_name;";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					elements.clear();
					id_elements.clear();
					
					while(rs1.next())
					{
						map_id = rs1.getString("analysis_map.id");
						
						id_elements.add(map_id);
						
						map_name = rs1.getString("analysis_map.map_name");
						
						elements.add(map_name);
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
	 * Create a list of active maps in the system.
	 * Here additionally is created the html code to 
	 * display the list on the jsp page, including
	 * a button to modify the selected map.
	 * 
	 * @return true/false
	 */
	public boolean getMapInfo3()
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
					
					String query = "SELECT distinct(analysis_map.id), analysis_map.map_name, analysis_map.remark" +
							" FROM analysis_map, analysis_map_link" +
							" WHERE analysis_map.id = analysis_map_link.map_id" +
							" AND analysis_map.active = 'T'"+
							" ORDER BY analysis_map.map_name;";
					
									
					ResultSet rs1 = stmt.executeQuery(query);
					
					elements.clear();
					String data = "";
					
					while(rs1.next())
					{
						data = "";
						
						data = data.concat("<td width=\"191\">"+Util.encodeTag(rs1.getString("analysis_map.map_name"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"350\">"+Util.encodeTag(rs1.getString("analysis_map.remark"))+"</td>");
						data = data.concat("|");
						
						data = data.concat("<td width=\"50\" align=\"center\">"+
								"<form method=\"post\" action=\""+base+"?action=modify_map&code1=yes\" target=\"Main\">" +
								"<input type=\"hidden\" value=\""+rs1.getString("analysis_map.id")+"\" name=\"map_id\"><br>" +
								"<input class=\"submit_nowidth\" type=\"submit\" value=\"Modify\">" +
								"</form>" +
						"</td>");
						data = data.concat("|");
						
						elements.add(data);
						
						map_id = rs1.getString("analysis_map.id");
						
						id_elements.add(map_id);
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
	 * @return Returns the map_id.
	 */
	public String getMap_id() {
		return map_id;
	}
	/**
	 * @return Returns the analysis_ids.
	 */
	public String[] getIdArray() {
		return idArray;
	}
	/**
	 * @param analysis_ids The analysis_ids to set.
	 */
	public void setIdArray(String[] idArray) {
		this.idArray = idArray;
	}
	/**
	 * @param map_id The map_id to set.
	 */
	public void setMap_id(String map_id) {
		this.map_id = map_id;
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
	 * @return Returns the elements.
	 */
	public Vector getElements() {
		return elements;
	}
	/**
	 * @return Returns the id_elements.
	 */
	public Vector getId_elements() {
		return id_elements;
	}
	/**
	 * @param id_elements The id_elements to set.
	 */
	public void setId_elements(Vector id_elements) {
		this.id_elements = id_elements;
	}
	/**
	 * @param elements The elements to set.
	 */
	public void setElements(Vector elements) {
		this.elements = elements;
	}
	/**
	 * @return Returns the id_value.
	 */
	public String getId_field() {
		return id_field;
	}
	/**
	 * @param id_value The id_value to set.
	 */
	public void setId_field(String id_value) {
		this.id_field = id_value;
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
	 * @return Returns the list_of_a.
	 */
	public String getList_of_a() {
		return list_of_a;
	}
	/**
	 * @return Returns the remark_original.
	 */
	public String getRemark_original() {
		return remark_original;
	}
	/**
	 * @param remark_original The remark_original to set.
	 */
	public void setRemark_original(String remark_original) {
		this.remark_original = remark_original;
	}
	/**
	 * @return Returns the map_name_original.
	 */
	public String getMap_name_original() {
		return map_name_original;
	}
	/**
	 * @param map_name_original The map_name_original to set.
	 */
	public void setMap_name_original(String map_name_original) {
		this.map_name_original = map_name_original;
	}
	/**
	 * @param list_of_a The list_of_a to set.
	 */
	public void setList_of_a(String list_of_a) {
		this.list_of_a = list_of_a;
	}
	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
