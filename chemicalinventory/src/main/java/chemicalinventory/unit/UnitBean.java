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
package chemicalinventory.unit;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class UnitBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5310370697753098616L;
	private String unit_name = "";
	private String unit_id = "";
	private String order_by = "";
	
	private Vector unit_names = new Vector();
	private Vector unit_ids = new Vector();
	
	public Vector listOfUnits()
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
					
					if(!Util.isValueEmpty(this.order_by))
						this.order_by = "1"; 
					
					String query = "SELECT unit.value, unit.id FROM unit ORDER BY "+this.order_by+";";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					unit_names.clear();
					
					while(rs1.next())
					{
						unit_names.add(Util.encodeTag(rs1.getString("unit.value")));
						unit_ids.add(rs1.getString("unit.id"));
					}					
				}
				con.close();
				
				return unit_names;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1: "+e);
			return null;
		}
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
	 * List of units - the name is not html encoded
	 * @return
	 */
	public Vector listOfUnits2()
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
					
					String query = "SELECT unit.value FROM unit ORDER BY VALUE;";
					
					ResultSet rs1 = stmt.executeQuery(query);
					
					unit_names.clear();
					
					while(rs1.next())
					{
						unit_names.add(rs1.getString("unit.value").trim().toUpperCase());
						
					}					
				}
				con.close();
				
				return unit_names;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error 1: "+e);
			return null;
		}
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
	 * Register a new unit, boolean returns the status of the operation
	 * @return
	 */
	public boolean registerUnit()
	{
		if(unit_name != null && !unit_name.equals(""))
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
						
						String register = "INSERT INTO unit (value) VALUES('"+unit_name+"');";
						
						stmt.executeUpdate(register);						
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
		else
			return false;
	}
	
	/**
	 * Update the unit name to a new value
	 * @return
	 */
	public boolean updateUnit()
	{
		if(unit_name != null && !unit_name.equals(""))
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
						
						String register = "UPDATE unit set value ='"+unit_name+"'" +
						" WHERE id = "+unit_id+";";
						
						stmt.executeUpdate(register);						
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
		else
			return false;
	}
	
	/**
	 * Delete a unit!
	 * @return
	 */
	public boolean deleteUnit()
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
					
					String delete = "DELETE FROM unit WHERE id = "+unit_id+";";
					
					stmt.executeUpdate(delete);						
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
	
	
	public boolean getUnitNameFromDb()
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
					
					String query = "SELECT value FROM unit WHERE id = "+unit_id+";";
					
					ResultSet rs = stmt.executeQuery(query);
					
					if(rs.next())
					{
						this.unit_name = rs.getString(1);
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
	 * @return Returns the unit_name.
	 */
	public String getUnit_name() {
		return unit_name;
	}
	/**
	 * @param unit_name The unit_name to set.
	 */
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	/**
	 * @return Returns the unit_ids.
	 */
	public Vector getUnit_ids() {
		return unit_ids;
	}
	/**
	 * @return Returns the unit_id.
	 */
	public String getUnit_id() {
		return unit_id;
	}
	/**
	 * @param unit_id The unit_id to set.
	 */
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	/**
	 * @return Returns the unit_names.
	 */
	public Vector getUnit_names() {
		return unit_names;
	}
	/**
	 * @param unit_names The unit_names to set.
	 */
	public void setUnit_names(Vector unit_names) {
		this.unit_names = unit_names;
	}

	/**
	 * @return Returns the order_by.
	 */
	public String getOrder_by() {
		return order_by;
	}

	/**
	 * @param order_by The order_by to set.
	 */
	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}
}