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

import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import java.net.*;
import java.text.*;

import chemicalinventory.context.Attributes;
import chemicalinventory.history.History;
import chemicalinventory.user.UserInfo;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

/** This bean is part of the chemicalinventory software. 
 * It takes care of the buisiness logic concentrated around lending and 
 * returning containers. These operations is primarily retrieving data, 
 * checking in or checking out a container.
 **/

public class BorrowBean implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5360054948008728124L;
	
	public BorrowBean()
	{
	}
	
	private String id = "";
	private String compound_id = "";
	private String chemical_name = "";
	private String home_location = "";
	private String initial_quantity = "";
	private double current_quantity = 0.00;
	private String unit = "";
	private String user_id = "";
	private String tara_weight = "";
	private String user_name = "";
	private double used_quantity = 0.00;
	private double intermediate_quantity = 0.00;
	private String current_location = "";
	private String empty_container = "";
	private boolean check;
	public boolean empty;
	public boolean empty_flag;
	private String location_id = "";
	private String owner = null;
	private String base = "";
	private String transfer_to = "";
	private String transfer_name = "";
	private int status = 0;
	
	UserInfo info = new UserInfo();
	History history = new History();
	
	/**
	 * @param i String
	 */  
	public void setId(String i)
	{ 
		id = i;
	}
	
	/** Getter method for the id.
	 * @return The id value.
	 */  
	public String getId()
	{
		return id;
	}
	
	/** Getter for the compound id.
	 * @return The compound id.
	 */  
	public String getCompound_id()
	{
		return compound_id;
	}
	
	/** Getter for the chemical name.
	 * @return The chemical name.
	 */  
	public String getChemical_name()
	{
		return chemical_name;
	}
	
	/** Getter for home location.
	 * @return The home location of this container.
	 */  
	public String getHome_location()
	{ 
		return home_location;
	}
	
	/** Getter for the initial quantity.
	 * @return The quantity before check in of a container.
	 */  
	public String getInitial_quantity()
	{
		return initial_quantity;
	}
	
	/** Getter for the owner.
	 * @return The owner(s) of this container.
	 */  
	public String getOwner()
	{
		return owner;
	}
	
	/** Finds the current quantity on a container, based on the container id. */  
	public void setCurrent_quantity()
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
					
					/*Create the decimal formatter to make the quantity string look nice*/
					DecimalFormat format = new DecimalFormat(Util.PATTERN_mg);
					DecimalFormatSymbols dec = new DecimalFormatSymbols();
					dec.setDecimalSeparator('.');
					format.setDecimalFormatSymbols(dec);
					
					String sql = "SELECT c.initial_quantity, c.current_quantity, c.unit" +
					" FROM container c" +
					" WHERE c.id="+id+"";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						String quantity_string = format.format(rs.getDouble("current_quantity"));
						this.current_quantity = Double.parseDouble(quantity_string);
						this.unit = rs.getString("c.unit");
					}
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
	
	/** Getter for the current quantity of a container.
	 * @return The qurrent quantity as registeret in the db.
	 */  
	public double getCurrent_quantity()
	{
		return current_quantity;
	}
	
	/** Getter for the unit.
	 * @return The unit of measurement. g, kg, ml, or l.
	 */  
	public String getUnit()
	{
		return unit;
	}
	
	/** Getter for the user id.
	 * @return The user id of a user.
	 */  
	public String getUser_id()
	{
		return user_id;
	}
	
	/** Getter for the tara weight of a container.
	 * @return The tara weight of as registered in the db.
	 */  
	public String getTara_weight()
	{
		return tara_weight;
	}
	
	/** Setter for the user name.
	 * @param un String
	 */  
	public void setUser_name(String un)
	{ 
		user_name = un;
	}
	
	/** Getter for the user name.
	 * @return The user name.
	 */  
	public String getUser_name()
	{
		return user_name;
	}
	
	/** Set the used quantity.
	 * @param nq double
	 */  
	public void setUsed_quantity(double nq)
	{ 
		if(empty_container.equals("") || empty_container==null)
		{
			used_quantity = nq;  
		}
		else
		{
			used_quantity = current_quantity;
		}
	}
	
	/** Getter for the used quantity.
	 * @return The quantity used by the user.
	 */  
	public double getUsed_quantity()
	{
		return used_quantity;
	}
	
	/** Getter for the current location.
	 * @return The current location of a container.
	 */  
	public String getCurrent_location()
	{
		return current_location;
	}
	
	/** Setter for the empty container flag.
	 * @param e String
	 */  
	public void setEmpty_container(String e)
	{ 
		empty_container = e;
	}
	
	/** Getter for the empty container flag.
	 * @return To see if the container has been marked to be set as empty.
	 */  
	public String getEmpty_container()
	{
		return empty_container;
	}
	
	/** Setter for the location id.
	 * @param loc_id String
	 */  
	public void setLocation_id(String loc_id)
	{
		location_id = loc_id;
	}
	
	/** Getter for the location id.
	 * @return The location id.
	 */  
	public String getLocation_id()
	{
		return location_id;
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
	 * @return Returns the transfer_to.
	 */
	public String getTransfer_to() {
		return transfer_to;
	}
	
	/**
	 * @param transfer_to The transfer_to to set.
	 */
	public void setTransfer_to(String transfer_to) {
		this.transfer_to = transfer_to;
	}
	/**
	 * @return Returns the transfer_name.
	 */
	public String getTransfer_name() {
		return transfer_name;
	}
	/**
	 * @param transfer_name The transfer_name to set.
	 */
	public void setTransfer_name(String transfer_name) {
		this.transfer_name = transfer_name;
	}
	/** Getter for the check value.
	 * @return Return the boolean value of the check to find informaion of on a container.
	 */  
	public boolean getCheck()
	{
		return check;
	}
	
	/** Intended for use in process of checking a container either in or out.
	 * This method retrieves information to be displayed. Using booleans to
	 * indicate whether the container is empty or not, and to indicate the
	 * status of the db select.
	 */  
	public void find()
	{
		try{
			check = false;
			
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
					
					String sql = "SELECT c.id, c.compound_id, c.location_id, c.empty, com.chemical_name," +
					" c.supplier_id, c.owner, l.location_name, c.initial_quantity, c.current_quantity," +
					" c.unit, c.user_id, c.tara_weight, u.user_name"+
					" FROM compound com, location l, container c LEFT JOIN user u"+
					" ON c.user_id= u.id"+
					" WHERE c.compound_id = com.id"+
					" AND c.location_id = l.id"+
					" AND c.id="+id+"";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					/*Create the decimal formatter to make the quantity string look nice*/
					DecimalFormat format = new DecimalFormat(Util.PATTERN);
					DecimalFormatSymbols dec = new DecimalFormatSymbols();
					dec.setDecimalSeparator('.');
					format.setDecimalFormatSymbols(dec);
					
					if(rs.next())
					{
						if(rs.getString("c.empty").equals("F"))
						{
							//Set a bunch of variables to a value from the search
							owner = Util.encodeNullValue(rs.getString("c.owner"));
							compound_id = rs.getString("c.compound_id");
							chemical_name = URLEncoder.encode(rs.getString("com.chemical_name"), "UTF-8");
							initial_quantity = format.format(rs.getDouble("c.initial_quantity"));
							unit = rs.getString("unit");
							user_id = rs.getString("user_id");
							current_location = rs.getString("u.user_name");
							tara_weight = rs.getString("tara_weight");
							try {current_quantity = Double.parseDouble(rs.getString("current_quantity")); }
							catch (NumberFormatException n)
							{
								empty=true;
							}
							
							setIntermediate_quantity();
							
							//The user has checked the box 'The container is empty'
							if(empty_container.equals("on") || intermediate_quantity <= 0)
							{
								home_location = "EMPTY CONTAINER";
								empty_container = "on";
							}
							else 
							{
								home_location = Util.getLocation(rs.getString("c.location_id"));
								location_id = rs.getString("c.location_id");
							}         
							
							if(current_quantity != 0) //is the container empty??
							{
								empty = true; //not empty
							}
							else
							{
								empty = false; //container is empty
							}
							
							check=true;
							empty_flag=false;
							
							con.close();
						}
						else
						{
							empty_flag=true;
						}
					}
				}
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			check=false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			check=false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			check=false;
		}
	}
	
	/** Check out a container. Mark the container with the user that
	 * has checked out this container.
	 * @param con_id
	 * @param user
	 * @param chem_name
	 */
	public boolean check_out(String con_id, String user, String chem_name)
	{
		/*
		 * Validate input
		 */
		if(!Util.isValueEmpty(con_id) || Util.getIntValue(con_id) == 0)
			return false;
		
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
					
					chemical_name = chem_name;
					id = con_id;
					String u_id = "";
					String sql = "SELECT id FROM user WHERE user_name ='"+user+"'";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					while(rs.next())
					{
						u_id = rs.getString("id");
					}
					rs.close();
					
					String sql1 = "UPDATE container SET user_id ='"+u_id+"', modified_by='"+user.toUpperCase()+"' WHERE id = '"+id+"'";
					
					try
					{
						int container_id = Integer.parseInt(id);
						
						//update the history for this container, with a checed out message.
						stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, container_id, Util.double_q(chem_name), History.CHECK_OUT, user.toUpperCase(), "--", "--", "--"));
						
						//Update the container as checked out
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
	 * Check in a container.
	 * update the quantity used.
	 * Set the container to empty if this is variable is checked.
	 * @param con_id
	 * @param user
	 * @param chemical_name
	 * @return
	 */
	public boolean check_in(String con_id, String user, String chemical_name)
	{
		/*Get the current quantity on the container.*/
		setCurrent_quantity();
		
		/*Check that the entered quantity is less or equal to the current*/
		if(this.used_quantity <= getCurrent_quantity())
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
						
						id = con_id;
						home_location = Util.getLocation(id);
						
						//check in the container, the container is NOT empty
						if((this.empty_container==null || this.empty_container.equals("")) &&
								this.used_quantity < this.current_quantity)
						{
							String sql1 = "UPDATE container" +
							" SET user_id ='0', " +
							" modified_by='"+user.toUpperCase()+"'," +
							" current_quantity=current_quantity - "+this.used_quantity+"" +
							" WHERE id = '"+id+"'";
							
							try
							{
								setCurrent_quantity();
								
								int container_id = Integer.parseInt(id);
								
//								update the history for this container, with a checed in message.
								stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, container_id, Util.double_q(chemical_name), History.CHECK_IN, user.toUpperCase(), this.unit, String.valueOf(this.current_quantity), String.valueOf(this.current_quantity - this.used_quantity)));       	
								
								//Update the container as checked in
								stmt.executeUpdate(sql1);
								
								con.commit();
								con.close();
								
								return true;
							}
							catch (Exception e) {
								con.rollback();
								con.close();
								return false;
							}
						}
						else//check in an empty container
						{
							/*
							 * Use this sql if you want the system, to update the container instead of removing them from the db
							 * when they are empty. 
							 */
//							String sql1 = "UPDATE container SET user_id ='0', modified_by='"+user+"', location_id=0, current_quantity=0.0, empty = 'T' WHERE id = '"+id+"'";
							
							
							/*
							 * Use this sql to remove a container from the database when it is empty! 
							 */
							
							/* Set the empty container as on*/
							this.empty_container = "on";
							
							String sql1 = "DELETE FROM container WHERE id = '"+id+"'";
							
							home_location = "EMPTY CONTAINER";            
							
							try
							{
								setCurrent_quantity();
								
								int container_id = Integer.parseInt(id);
								
//								update the history for this container, with a deleted in message.
								stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, container_id, Util.double_q(chemical_name), History.DELETE, user.toUpperCase(), this.unit, String.valueOf(this.current_quantity), "0.0"));       	
								
								//Update the container as checked in
								stmt.executeUpdate(sql1);
								
								con.commit();
								con.close();
								
								return true;
							}
							catch (Exception e) {
								con.rollback();
								con.close();
								return false;
							}
						}
					}
					con.close();
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
		{
			this.status = Return_codes.VALUE_TOO_HIGH;
			return false;
		}
		
		return false;
	}
	
	/**
	 * Tranfer a container from one user to another.
	 * Here the used quantity is registered, the new user set
	 * as current loction.
	 */
	public boolean transfer(String chemical_name)
	{
		if(!id.equals("") && id != null)
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
						
						String sql1 = "UPDATE container SET user_id ='"+transfer_to+"'," +
								" modified_by='"+user_name.toUpperCase()+"'," +
								" current_quantity="+intermediate_quantity+" WHERE id = '"+id+"'";
						
						try
						{
							setCurrent_quantity();
							
							int container_id = Integer.parseInt(id);
							
//							update the history for this container, with a deleted in message.
							stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, container_id, Util.double_q(chemical_name), History.TRANSFER_BY+" "+this.transfer_name.toUpperCase()+"\n"+History.TRANSFER_TO+" "+info.getUserNameFromID(transfer_to), this.user_name.toUpperCase(), this.unit, String.valueOf(this.current_quantity), String.valueOf(this.current_quantity - this.used_quantity)));       	
							
							//Update the container as checked in
							stmt.executeUpdate(sql1);
							
							con.commit();
							con.close();
							
							return true;
						}
						catch (Exception e) {
							con.rollback();
							con.close();
							return false;
						}
					}            
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
		else
			return false;
	}
	
	/**
	 * Get the username for the user that has a container checked out.
	 * @param c_id
	 * @return the username.
	 */
	public String containerAtUser(int c_id)
	{
		if(c_id > 0)
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
						
						String sql1 = "SELECT u.user_name FROM user u, container c" +
						" WHERE c.id = " + id +
						" AND c.user_id = u.id;";
						
						ResultSet rs = stmt.executeQuery(sql1);       	
						
						if(rs.next())
						{
							String name = Util.encodeTagAndNull(rs.getString("u.user_name"));
							
							con.close();
							return name.toUpperCase();
						}
						
						con.close();
						return "--";
					}            
				}
			}//end of try
			
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				return "--";
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return "--";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return "--";
			}
			
			return "--";
		}
		else
			return "--";
	}
	
	/**
	 * find out if a container is checked out by a specific user.
	 * @param user
	 * @param con_id
	 * @return true/false
	 */
	public boolean isCheckedOutByUser(String user, String con_id)
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
					
					info.retrieveNameId(user);
					
					String user_id = String.valueOf(info.getUser_id());
					String id_container = "";
					
					String sql = "SELECT user_id from container where id = "+con_id;
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						id_container = rs.getString("user_id");
					}
					
					if(id_container.equals(user_id))
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
		
		return false;
	}
	
	/** Get the intermediate quantity from the db.
	 * The intermediate quantity is calculated as the
	 * current quantity - quantity used during use.
	 */
	public void setIntermediate_quantity()
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
					
					double quantity = 0.00;
					
					String sql = "SELECT current_quantity FROM container WHERE id = '"+id+"'";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						try { quantity = Double.parseDouble(rs.getString("current_quantity")); }
						catch (NumberFormatException n)
						{
							System.out.println("Error in parse to double: " +n);
						}
						
						/*Create the decimal formatter to make the quantity string look nice*/
						DecimalFormat format = new DecimalFormat(Util.PATTERN_mg);
						DecimalFormatSymbols dec = new DecimalFormatSymbols();
						dec.setDecimalSeparator('.');
						format.setDecimalFormatSymbols(dec);
						
						intermediate_quantity = quantity - used_quantity;
						
						String quantity_string = format.format(intermediate_quantity);
						intermediate_quantity = Double.parseDouble(quantity_string);
						
						if(intermediate_quantity <= 0)
						{
							empty_container = "on";
						}
					}
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
	 * @param intermediate_quantity The intermediate_quantity to set.
	 */
	public void setIntermediate_quantity(double intermediate_quantity) {
		this.intermediate_quantity = intermediate_quantity;
	}
	/** Getter for the intermediate quantity.
	 * @return Returns the intermediate quantity calculated.
	 */  
	public double getIntermediate_quantity()
	{
		return intermediate_quantity;
	}
	/**
	 * @param chemical_name The chemical_name to set.
	 */
	public void setChemical_name(String chemical_name) {
		this.chemical_name = chemical_name;
	}
	
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
}