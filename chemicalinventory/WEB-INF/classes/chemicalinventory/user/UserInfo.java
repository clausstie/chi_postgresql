/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesøe 2003-2006.
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
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package chemicalinventory.user;


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
 *This class holds method used across other classes.
 *The method is primarily for getting information of various kinds
 *about a user.
 **/
public class UserInfo implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9027012622485281167L;

	public UserInfo() 
	{
	}
	
	private Vector un_taken = new Vector();
	private Vector un_id = new Vector();
	private Vector id_list = new Vector();
	private Vector email = new Vector();
	private Vector name_list = new Vector();
	private boolean ok = false;
	private int code = 0;
	private int user_id = 0;
	private String base = "";
	private String userOmit = "";
	private String last_name = "";
	private String first_name = "";
	private String user_name = "";
	private String room = "";
	private String telephone = "";
	private String mail = "";
	private String role = "";
	private String privileges = "";
	private String groups = "";
	
	
	
	/**
	 * @return Returns the id_list.
	 */
	public Vector getId_list() {
		return id_list;
	}
	
	/** Getter for user id.
	 * @return int.
	 */
	public int getUser_id()
	{
		return user_id;
	}
	
	
	/** Getter for a vector holding all user names.
	 * @return Vector.
	 */
	public Vector getUn_taken()
	{
		return un_taken;
	}
	
	/**
	 * @return Returns the userOmit.
	 */
	public String getUserOmit() {
		return userOmit;
	}
	/**
	 * @param userOmit The userOmit to set.
	 */
	public void setUserOmit(String userOmit) {
		this.userOmit = userOmit;
	}
	/**
	 * @return Returns the email.
	 */
	public Vector getEmail() {
		return email;
	}
	/**
	 * @return Returns the name_list.
	 */
	public Vector getName_list() {
		return name_list;
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
	 * @return Returns the groups.
	 */
	public String getGroups() {
		return groups;
	}
	/**
	 * @param groups The groups to set.
	 */
	public void setGroups(String groups) {
		this.groups = groups;
	}
	/** 
	 *create a list of usernames used,
	 **/
	public void userName_used()
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
					
					String un = "SELECT id, user_name FROM user ORDER BY user_name";
					
					ResultSet rs = stmt.executeQuery(un);
					
					un_taken.clear();
					un_id.clear();
					
					while(rs.next())
					{
						un_id.addElement(rs.getString("id"));
						un_taken.addElement(rs.getString("user_name"));
					}
					
					stmt.close();
				}
				con.close();
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			System.out.println(""+e);
		}
		catch (SQLException e)
		{
			System.out.println(""+e);  
		}
		catch (Exception e)
		{
			System.out.println(""+e);
		}
	}
	
	/** Retrieve the username, and id...
	 * @param name String. the user name of the user
	 * @return Boolean.
	 */
	public boolean retrieveNameId(String name)
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
					
					String un = "SELECT id, user_name FROM user WHERE user_name = '"+name+"'";
					
					ResultSet rs = stmt.executeQuery(un);
					
					if(rs.next())
					{
						user_id = rs.getInt("id");
					}
					
					stmt.close();
					ok = true;
				}
				con.close();
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			ok = false;
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			ok = false;
			e.printStackTrace();  
		}
		catch (Exception e)
		{
			ok = false;
			e.printStackTrace();
		}
		
		return ok;
	}
	
	/** Method used to get information on a user, and from that information
	 * create the html code used to display an overlib box with data on that
	 * particular user. Primarily used to display information on owners of
	 * a container. Takes a string owner which consits of either one,
	 * or two user names separeted by a single /.
	 * @param own String.
	 * @return String.
	 */
	public String display_owner_data(String own)
	{
		String owner_info = null;
		
		if(own != null)
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
						
						String owners = null;
						
						/*Add single ping ' to the user names...*/
						if (own.indexOf("/") != -1)
						{
							owners ="'"+own.substring(0, (own.indexOf("/")))+"', '"+own.substring((own.indexOf("/")+1), own.length())+"'";
						}
						else       
							owners = "'"+own+"'";
						
						String sql = "SELECT user_name, first_name, last_name, room_number, telephone, email FROM user WHERE user_name in ("+owners+");";
						
						ResultSet result = stmt.executeQuery(sql);
						
						int i = 0;
						String html_info_1 = null;
						String info_1 = null;
						String info_2 = null;
						
						while(result.next())
						{
							html_info_1 = "User: "+result.getString("user_name")+
							"<br>Name: "+result.getString("first_name")+" "+result.getString("last_name")+
							"<br>Tel: "+result.getString("telephone")+
							"<br>Room: "+result.getString("room_number")+
							"<br>E-mail: "+result.getString("email");
							
							info_1 = "<a class=\"table_link\" href=\"mailto:"+result.getString("email")+"\" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+"</a>";
							
							if (i == 0)
							{
								info_2 = info_1;
							}
							
							i++;
						}
						
						if (i == 2)
						{
							owner_info = info_2+" / "+info_1;                  
						}
						else
							owner_info = info_1;
						
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
		
		if(owner_info == null)
			return own;
		else
			return owner_info;
	}
	/**
	 * This method creates the html code to display an overlib box with various information
	 * about a user. The link will activate an internal CI mail form.
	 * @param own User name
	 * @param base base from servlet context
	 * @return returns the html code.
	 */
	public String display_owner_data_base(String own, String base)
	{   
		String owner_info = null;
		
		/* Make sure a value has been received*/
		if(own != null)
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
						
						String owners = "";
						
						/*Add single ping ' to the user names...*/
						if (own.indexOf("/") != -1)
						{
							owners ="'"+own.substring(0, (own.indexOf("/")))+"', '"+own.substring((own.indexOf("/")+1), own.length())+"'";
						}
						else       
							owners = "'"+own+"'";
						
						String sql = "SELECT user_name, first_name, last_name, room_number, telephone, email FROM user WHERE user_name in ("+owners+");";
						
						ResultSet result = stmt.executeQuery(sql);
						
						int i = 0;
						String html_info_1 = null;
						String info_1 = null;
						String info_2 = null;
						
						while(result.next())
						{
							html_info_1 = "User: "+result.getString("user_name")+
							"<br>Name: "+result.getString("first_name")+" "+result.getString("last_name")+
							"<br>Tel: "+result.getString("telephone")+
							"<br>Room: "+result.getString("room_number")+
							"<br>E-mail: "+result.getString("email");
							
							info_1 = "<a class=\"table_link\" href=\""+base+"?action=mail2&mail="+result.getString("email")+"&fullname="+result.getString("first_name")+" "+result.getString("last_name")+"&user="+result.getString("user_name")+"\" target=\"blank\" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+"</a>";                      
							if (i == 0)
							{
								info_2 = info_1;
							}
							
							i++;
						}
						
						if (i == 2)
						{
							owner_info = info_2+" / "+info_1;                  
						}
						else
							owner_info = info_1;
						
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
		
		if(owner_info == null)
			return own;
		else
			return owner_info;
	}
	
	/** Method used to get information on a user, and from that information
	 * create the html code used to display an overlib box with data on that
	 * particular user. Primarily used to display information on owners of
	 * a container. Takes a string owner which consits of either one,
	 * or two user names separeted by a single /.
	 * 
	 * This variant of the method shows both user name and full name in the display string
	 * 
	 * @param own String.
	 * @return String.
	 */
	public String display_owner_data_fullName(String own)
	{   
		String owner_info = null;
		
		if(own != null)
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
						
						String owners = null;
						
						/*Add single ping ' to the user names...*/
						if (own.indexOf("/") != -1)
						{
							owners ="'"+own.substring(0, (own.indexOf("/")))+"', '"+own.substring((own.indexOf("/")+1), own.length())+"'";
						}
						else       
							owners = "'"+own+"'";
						
						String sql = "SELECT user_name, first_name, last_name, room_number, telephone, email FROM user WHERE user_name in ("+owners+");";
						
						ResultSet result = stmt.executeQuery(sql);
						
						int i = 0;
						String html_info_1 = null;
						String info_1 = null;
						String info_2 = null;
						
						while(result.next())
						{
							html_info_1 = "User: "+result.getString("user_name")+
							"<br>Name: "+result.getString("first_name")+" "+result.getString("last_name")+
							"<br>Tel: "+result.getString("telephone")+
							"<br>Room: "+result.getString("room_number")+
							"<br>E-mail: "+result.getString("email");
							
							
							info_1 = "<a class=\"table_link\" href=\"mailto:"+result.getString("email")+"\" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+" - "+result.getString("first_name")+" "+result.getString("last_name")+"</a>";
							//info_1 = "<a class=\"table_link\" href=\" xx \" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+" - "+result.getString("first_name")+" "+result.getString("last_name")+"</a>";
							
							if (i == 0)
							{
								info_2 = info_1;
							}
							
							i++;
						}
						
						if (i == 2)
						{
							owner_info = info_2+" / "+info_1;                  
						}
						else
							owner_info = info_1;
						
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
		
		if(owner_info == null)
			return own;
		else
			return owner_info;
	}
	
	/** Method used to get information on a user, and from that information
	 * create the html code used to display an overlib box with data on that
	 * particular user. Primarily used to display information on owners of
	 * a container. Takes a string owner which consits of either one,
	 * or two user names separeted by a single /.
	 * 
	 * This variant of the method shows both user name and full name in the display string
	 * And the method will activate an internal CI window to send mail when pressed
	 * @param own String.
	 * @return String.
	 */
	public String display_owner_data_fullName2(String own, String base)
	{   
		String owner_info = null;
		
		if(own != null)
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
						
						String owners = null;
						
						/*Add single ping ' to the user names...*/
						if (own.indexOf("/") != -1)
						{
							owners ="'"+own.substring(0, (own.indexOf("/")))+"', '"+own.substring((own.indexOf("/")+1), own.length())+"'";
						}
						else       
							owners = "'"+own+"'";
						
						String sql = "SELECT user_name, first_name, last_name, room_number, telephone, email FROM user WHERE user_name in ("+owners+");";
						
						ResultSet result = stmt.executeQuery(sql);
						
						int i = 0;
						String html_info_1 = null;
						String info_1 = null;
						String info_2 = null;
						
						while(result.next())
						{
							html_info_1 = "User: "+result.getString("user_name")+
							"<br>Name: "+result.getString("first_name")+" "+result.getString("last_name")+
							"<br>Tel: "+result.getString("telephone")+
							"<br>Room: "+result.getString("room_number")+
							"<br>E-mail: "+result.getString("email");
							
							info_1 = "<a class=\"table_link\" href=\""+base+"?action=mail2&mail="+result.getString("email")+"&fullname="+result.getString("first_name")+" "+result.getString("last_name")+"&user="+result.getString("user_name")+"\" target=\"blank\" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+" - "+result.getString("first_name")+" "+result.getString("last_name")+"</a>";
							//info_1 = "<a class=\"table_link\" href=\"mailto:"+result.getString("email")+"\" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+" - "+result.getString("first_name")+" "+result.getString("last_name")+"</a>";
							//info_1 = "<a class=\"table_link\" href=\" xx \" onmouseover=\"return overlib('"+html_info_1+"', BORDER, 2, CAPTION, 'USER INFO', ABOVE);\" onmouseout=\"return nd();\">"+result.getString("user_name")+" - "+result.getString("first_name")+" "+result.getString("last_name")+"</a>";
							
							if (i == 0)
							{
								info_2 = info_1;
							}
							
							i++;
						}
						
						if (i == 2)
						{
							owner_info = info_2+" / "+info_1;                  
						}
						else
							owner_info = info_1;
						
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
		
		if(owner_info == null)
			return own;
		else
			return owner_info;
	}
	
	
	/**
	 * Get the full name of a user in the system.
	 * @param name - The user name of the user, on whom to return the full name
	 * @return
	 */
	public String getFullName(String name)
	{
		String full_name = "";
		
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
					
					String un = "SELECT first_name, last_name FROM user WHERE user_name = '"+name+"'";
					
					ResultSet rs = stmt.executeQuery(un);
					
					if(rs.next())
					{
						full_name = rs.getString("first_name") +" "+rs.getString("last_name");
					}
					
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
		
		return full_name.toUpperCase();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getEmail(String name)
	{
		String mail = "";
		
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
					
					String un = "SELECT email FROM user WHERE user_name = '"+name+"'";
					
					ResultSet rs = stmt.executeQuery(un);
					
					if(rs.next())
					{
						mail = rs.getString("email");
					}
					
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
		
		return mail;
	}
	
	/**
	 * 
	 *
	 */
	public void getUserAndName()
	{
		String name_string = "";
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
					
					String un = "SELECT user_name, first_name, last_name, email FROM user ORDER BY user_name";
					
					ResultSet rs = stmt.executeQuery(un);
					
					name_list.clear();
					email.clear();
					
					while(rs.next())
					{
						name_string = rs.getString("user_name")+" - "+rs.getString("first_name")+" "+rs.getString("last_name");
						
						name_list.add(name_string);
						email.add(rs.getString("email"));
					}
					
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
	}
	
	/**
	 * Mehtod gets all usernames and full names from the db. The parameter user_to_omit
	 * must be a username, and that user will not be part of the search result.
	 * The vector name_list is filled with the names, and the vector id_list is filled
	 * with id's of users.
	 * 
	 * @param user_to_omit String user will not be part of the result.
	 */
	public void getUserNameAndID(String user_to_omit)
	{
		String name_string = "";
		String id = "";
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
					
					String un = "SELECT id, user_name, first_name, last_name" +
							" FROM user WHERE user_name != '"+user_to_omit+"' " +
							" AND ID != 0 ORDER BY user_name;";
					
					ResultSet rs = stmt.executeQuery(un);
					
					name_list.clear();
					id_list.clear();
					
					while(rs.next())
					{
						name_string = rs.getString("user_name")+" - "+rs.getString("first_name")+" "+rs.getString("last_name");
						id = rs.getString("id");
						
						name_list.add(name_string);
						id_list.add(id);
					}
					
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
	}
	
	/**
	 * create a list of usernames and ids...
	 *
	 */
	public void getUserNameAndIDList()
	{
		String name_string = "";
		String id = "";
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
					
					String un = "SELECT DISTINCT user.id, user.user_name, user.first_name, user.last_name FROM user, roles" +
					" WHERE user.user_name like '%"+user_name+"%'"+
					" AND user.removed = 'F'"+
					" AND user.user_name <> 'home'"+
					" AND user.id <> 0"+
					" AND roles.user_name = user.user_name" +
					" AND roles.role in ('normal', 'adm')"+
					" ORDER BY user_name"; 
					
					//"SELECT id, user_name, first_name, last_name FROM user WHERE ID != 0 ORDER BY user_name;";
					
					ResultSet rs = stmt.executeQuery(un);
					
					name_list.clear();
					id_list.clear();
					
					while(rs.next())
					{
						name_string = rs.getString("user_name")+" - "+rs.getString("first_name")+" "+rs.getString("last_name");
						id = rs.getString("id");
						
						name_list.add(name_string);
						id_list.add(id);
					}
					
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
	}
	
	/**
	 * Taking a userid an returning a username
	 * @param user_id
	 */
	public String getUserNameFromID(String user_id)
	{
		String name_string = "";
		
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
					
					String un = "SELECT user_name FROM user WHERE id = "+user_id+";";
					
					ResultSet rs = stmt.executeQuery(un);
					
					if(rs.next())
					{
						name_string = rs.getString("user_name");
					}
					
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
		
		return name_string;
	}
	
	
	/**
	 * Select a lot of information about a user from the db...
	 * store them in local variables...
	 * @param user_id
	 */
	public boolean getUserInfo_report(String user_id)
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
					
					
					//get the general user data incl role
					String un = "SELECT user.first_name," +
					" user.last_name," +
					" user.user_name," +
					" user.telephone," +
					" user.email," +
					" user.room_number," +
					" roles.role" +
					" FROM user, roles" +
					" WHERE user.id = "+user_id+
					" AND user.user_name = roles.user_name" +
					" AND roles.type = 'r';";
					
					ResultSet rs = stmt.executeQuery(un);
					
					if(rs.next())
					{
						this.user_name = rs.getString("user.user_name");
						this.first_name = rs.getString("user.first_name");
						this.last_name = rs.getString("user.last_name");
						this.room = rs.getString("user.room_number");
						this.mail = rs.getString("user.email");
						this.telephone = rs.getString("user.telephone");
						this.role = rs.getString("roles.role");
						
						//encode blank values
						this.user_name = Util.encodeNullValue(this.user_name);
						this.first_name = Util.encodeNullValue(this.first_name);
						this.last_name =Util.encodeNullValue(this.last_name);
						this.room = Util.encodeNullValue(this.room);
						this.mail = Util.encodeNullValue(this.mail);
						this.telephone = Util.encodeNullValue(this.telephone);
						this.role = Util.encodeNullValue(this.role);
						
						//encode html characters
						this.user_name = Util.encodeTag(this.user_name);
						this.first_name = Util.encodeTag(this.first_name);
						this.last_name = Util.encodeTag(this.last_name);
					}
					else
					{
						con.close();
						return false;
					}
					
					
					//get the privileges the user has been appointet
					un = "SELECT roles.role FROM roles, user" +
					" WHERE user.id = "+user_id+"" +
					" AND user.user_name = roles.user_name" +
					" AND roles.type = 'p';";
					
					ResultSet rs1 = stmt.executeQuery(un);
					
					this.privileges = Util.encodeNullValue(privileges);
					int i = 0;
					
					while(rs1.next())
					{
						i++;
						if(i == 1)
						{
							this.privileges = rs1.getString("roles.role");
						}
						else
						{
							this.privileges = this.privileges + "<br/>" + rs1.getString("roles.role"); 
						}
					}
					
					//get the groups for the user.
					un = "SELECT user_groups.name" +
					" FROM user_group_user_link, user_groups, user" +
					" WHERE user_group_user_link.user_id = user.id" +
					" AND user_group_user_link.group_id = user_groups.id" +
					" AND user_group_user_link.user_id = "+user_id+";"; 
					
					ResultSet rs2 = stmt.executeQuery(un);
					
					this.groups = Util.encodeNullValue(groups);
					int j = 0;
					
					while(rs2.next())
					{
						j++;
						if(j == 1)
						{
							this.groups = rs2.getString("user_groups.name");
						}
						else
						{
							this.groups = this.groups + "<br/>" + rs2.getString("user_groups.name"); 
						}
					}
					
					stmt.close();
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
		
		return true;
	}
	
	/**
	 * Create a list of all users in the db.
	 * @return
	 */
	public Vector getUserInfo_list()
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
					
					String sql = "SELECT user.user_name, " +
							"user.first_name, " +
							"user.last_name, " +
							"user.room_number, " +
							"user.telephone, " +
							"user.email " +
							"FROM user " +
							"ORDER BY user_name";
					
					ResultSet rs1 = stmt.executeQuery(sql);
					
					name_list.clear();
					
					while(rs1.next())
					{                     	
						name_list.add(rs1.getString("user_name").toUpperCase()+"|"+Util.encodeTag(rs1.getString("first_name"))+" "+Util.encodeTag(rs1.getString("last_name"))+"|"+rs1.getString("telephone")+"|"+rs1.getString("email")+"|"+rs1.getString("room_number"));
					}
				}
				con.close();
				this.code = 1;
				return name_list;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			this.code = 2;
			return null;
		}
		
		this.code = 2;
		return null;
	}
	/**
	 * @return Returns the first_name.
	 */
	public String getFirst_name() {
		return first_name;
	}
	/**
	 * @return Returns the last_name.
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * @return Returns the mail.
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @return Returns the room.
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * @return Returns the telephone.
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @return Returns the user_name.
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @return Returns the un_id.
	 */
	public Vector getUn_id() {
		return un_id;
	}
	/**
	 * @return Returns the privileges.
	 */
	public String getPrivileges() {
		return privileges;
	}
	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}
}