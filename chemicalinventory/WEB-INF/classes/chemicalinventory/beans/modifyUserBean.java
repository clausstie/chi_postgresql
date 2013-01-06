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
import javax.naming.*;
import javax.sql.*;
import chemicalinventory.user.PrivilegesBean;
import chemicalinventory.user.UserTypeBean;
import chemicalinventory.utility.Util;
import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.groups.User_group;

/**
 *Get information about a user, modify and update.
 **/

public class modifyUserBean implements java.io.Serializable
{


	public modifyUserBean()
	{
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2882949110565354325L;
	private String user_name = "";
	private String id = "";
	private String first_name = "";
	private String last_name = "";
	private String userType = "";
	private String room_number = "";
	private String telephone = "";
	private String email = "";
	private String org_user_name = "";
	private String newPassword = "";
	private int user_type_id = 0;
	private String remove = "";
	private String[] groups;
	private String mail = "";//field that indicates if there has to be sent a mail to the user informing about the update

	public Vector result_id = new Vector();
	public Vector result = new Vector();
	private String privilege[] = null;
	public boolean nameTaken = false;
	public boolean pwdUpdate;
	private boolean update;
	public boolean delete = false;

	//object to do some work for us in here..
	User_group bean = new User_group();
	UserRegBean userRegBean = new UserRegBean();
	PrivilegesBean privilegesBean = new PrivilegesBean();


	/**
	 * Get info on a user...
	 * @param isAdministrator
	 * @return boolean status.
	 */
	public boolean infoOnUser(boolean isAdministrator) {

		try {
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT u.id, u.user_name, u.first_name, u.last_name, u.telephone," +
				" u.email FROM `user` u" +
				" LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id" +
				" WHERE u.user_name like '%"+user_name+"%'";

				if(!isAdministrator)
					sql = sql + " AND ut.isAdministrator = 0";
				
				sql = sql + " ORDER BY u.user_name;";

				ResultSet rs1 = stmt.executeQuery(sql);

				result.clear();
				result_id.clear();

				while(rs1.next())
				{     
					result_id.addElement(rs1.getString("u.id"));
					result.addElement(rs1.getString("u.user_name").toUpperCase()+"|"+Util.encodeTag(rs1.getString("u.first_name"))+" "+Util.encodeTag(rs1.getString("u.last_name"))+"|"+rs1.getString("u.telephone")+"|"+rs1.getString("u.email"));
				}
				rs1.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/** Was update a succes.
	 * @return Boolean.
	 */  
	public boolean isUpdate()
	{
		return update;
	}

	/** This method has different senarios, the value of the parameter for this method
	 * decides which method to run.<br>
	 * 1: Find all users through a search for %user_name%.<br>
	 * 2: Find information about a specific user.<br>
	 * 3: Perfom modification of user data and display a receipt.<br>
	 * @param i int.
	 */
	public void getUserInfo(int i, boolean isAdministrator)
	{
		try{       
			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				if(user_name.indexOf("'") != -1)
				{
					user_name = user_name.replaceAll("'", "''");
				}

				if(i==1)
				{
					update = infoOnUser(isAdministrator);
					return ;
				}

				if( i == 2)//Search for the data on the selected user to modify
				{
					String sql1 = "SELECT u.id, u.user_name, u.first_name, u.last_name, u.password, u.room_number, u.telephone," +
					" u.email, ut.name FROM `user` u" +
					" LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id" +
					" WHERE u.id = "+id+";";

					ResultSet rs2 = stmt.executeQuery(sql1);

					if(rs2.next())
					{ 
						id = rs2.getString("u.id");
						user_name = rs2.getString("u.user_name").toUpperCase();
						userType = rs2.getString("ut.name");
						if(Util.isValueEmpty(userType))
							Util.encodeNullValue(userType);
						first_name = rs2.getString("u.first_name");
						last_name = rs2.getString("u.last_name");
						room_number = rs2.getString("u.room_number");
						telephone = rs2.getString("u.telephone");
						email = rs2.getString("u.email");
					}
					rs2.close();
				}
				if( i == 3)//Perfom modification of user data and display a receipt.
				{
					update = false;


					if (!Util.isValueEmpty(first_name) || !Util.isValueEmpty(last_name) || !Util.isValueEmpty(user_name) || 
							!Util.isValueEmpty(telephone) || !Util.isValueEmpty(email))
						return;

					con.setAutoCommit(false);

					String control = "SELECT id, user_name FROM user WHERE id = '"+user_name+"'";

					ResultSet res = stmt.executeQuery(control);

					//If username is taken and is different from the original user_name - get out of here.
					if(res.next() && !org_user_name.equals(user_name))
					{ 
						nameTaken = true;
						con.close();
						return;
					}
					else//else update the user with the values received from the jsp page.
					{                     
						first_name = Util.double_q(first_name);
						last_name = Util.double_q(last_name);

						//update general user data
						String sql = "UPDATE user"+
						" SET user_name = '"+user_name.toUpperCase()+"',"+
						" first_name = '"+first_name+"',"+
						" last_name = '"+last_name+"',"+
						" room_number = '"+room_number+"',"+
						" telephone = '"+telephone+"',"+
						" email = '"+email+"'";
						if(user_type_id > 0)
							sql = sql + ", user_type_id = " +user_type_id;
						
						sql = sql + " WHERE id = '"+id+"'";

						stmt.clearBatch();
						stmt.addBatch(sql);

						if(user_type_id > 0) {
							/*
							 * update the roles table.
							 */
							UserTypeBean utBean = new UserTypeBean();
							Hashtable currentPrivileges = utBean.getPrivilegesInRolesForUser(Integer.parseInt(id));
							Hashtable privilegesForType = utBean.getPrivilegesForType(user_type_id);

							if(currentPrivileges != null && privilegesForType != null) {

								for (Enumeration e = currentPrivileges.keys(); e.hasMoreElements();) {
									/*
									 * Remove privileges from the roles table, that is not a part of the selected usertype
									 */

									String currentKey = (String) e.nextElement();

									if (!privilegesForType.containsKey(currentKey)) {

										String deleteSql = "DELETE FROM roles WHERE privileges_id = "+currentKey+
										" AND user_name = '"+user_name.toUpperCase()+"';";

										stmt.addBatch(deleteSql);
									}
									else {
										privilegesForType.remove(currentKey);
									}
								}

								/*
								 * The remaining element in the privilegesForType is now entered into the roles table.
								 */
								if(privilegesForType != null && privilegesForType.size() > 0) {

									for(Enumeration e = privilegesForType.keys(); e.hasMoreElements();) {
										String key = (String) e.nextElement();
										String value = (String) privilegesForType.get(key);

										String insertSql = "INSERT INTO roles (user_name, role, privileges_id, user_type_id)" +
										" VALUES ('"+user_name.toUpperCase()+"', '"+value+"', "+key+", "+user_type_id+");";

										stmt.addBatch(insertSql);
									}
								}

							}
							else {
								con.rollback();
								con.close();
								update = false;
								return;
							}
						}

						/*
						 * Update the user groups the user is a member of...
						 */
						int u_id = Integer.parseInt(id);

						Vector updateGroups = bean.getUpdateStatements(groups, u_id);
						if (updateGroups != null)
						{
							for(Enumeration e = updateGroups.elements(); e.hasMoreElements();) {
								String updateStatement = (String) e.nextElement();

								stmt.addBatch(updateStatement);
							}
						}

						/*
						 * Commit to the database.
						 */
						try {
							stmt.executeBatch();
							con.commit();
							con.close();
							update = true;
							return;
						} catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();
							update = false;
							return;
						}
					}
				}
			}

			update = false;
			return;

		}//end of try

		catch (SQLException e)
		{
			update = false;
			e.printStackTrace();
		}
		catch (Exception e)
		{
			update = false;
			e.printStackTrace();
		}
	}

	/**
	 * Method used to change a users password from the administration page.
	 **/
	public void resetPassword()
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

					//generate a random password
					newPassword = Util.generatePassword();

					String sql = "UPDATE user" +
					" SET password=MD5('"+newPassword+"')"+
					" WHERE user_name='"+user_name+"'"+
					" AND id = "+id+"";

					stmt.executeUpdate(sql);
					pwdUpdate = true;       
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			pwdUpdate = false;
			System.out.println(""+e);
		}
		catch (SQLException e)
		{
			pwdUpdate = false;
			System.out.println(""+e);  
		}
		catch (Exception e)
		{
			pwdUpdate = false;
			System.out.println(""+e);
		}
	}

	/**Method used to remove a user from the system. in the user table the flag
	 *removed is set to T, and the user's data is completely removed from the
	 *table roles*/
	public void removeUser()
	{ 
		delete = false;
		if(remove.equals("on"))
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

						String sql1 = "SELECT r.user_name FROM user u, roles r"+
						" WHERE u.id ="+id+""+  
						" AND u.user_name = '"+user_name+"'"+ 
						" AND u.user_name = r.user_name";

						ResultSet rs = stmt.executeQuery(sql1);

						if(rs.next())//delete the user from the roles table to prevent acces to the system.
						{
							String sql2 = "DELETE FROM roles WHERE user_name = '"+rs.getString("r.user_name")+"'";
							stmt.executeUpdate(sql2);
							con.commit();
							delete = true;
						}
						else
						{
							delete = false;
							con.rollback();
						}          

						/*
						 * Use this sql if you want to have a system, where
						 * users are not deleted from the db, but simply inactivated.
						 */
//						String sql = "UPDATE user" +
//						" SET removed='T'"+
//						" WHERE user_name='"+user_name+"'"+
//						" AND id = "+id+"";

						/*
						 * Use this sql if you want a system, where users
						 * when deleted are removed from the database!!
						 */
						//delete from the user table:
						String sql = "DELETE FROM user "+
						" WHERE user_name='"+user_name+"'"+
						" AND id = "+id+"";

						stmt.executeUpdate(sql);
						con.commit();
						delete = true;

						// next delete the user from any user groups ...
						String sql_groups = "DELETE FROM user_group_user_link "+
						" WHERE user_id='"+id+"'";

						stmt.executeUpdate(sql_groups);
						con.commit();
						delete = true;

						stmt.close();
					}
					con.close();

					//update the container table, remove the owner from containers where he/she is the owner
					if(delete = true)
					{
						delete = removeUserFromContainer(user_name);
					}
				}
			}//end of try

			catch (ClassNotFoundException e) 
			{
				delete = false;
				System.out.println(""+e);
			}
			catch (SQLException e)
			{
				delete = false;
				System.out.println(""+e);
			}
			catch (Exception e)
			{
				delete = false;
				System.out.println(""+e);
			}
		}
	}


	/**
	 * This method removes a user from the container table, where the user
	 * could be registered as OWNER of one or more containers.
	 * @param username
	 * @return
	 */
	public boolean removeUserFromContainer(String username)
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

					String sql1 = "SELECT c.id, c.owner FROM container c"+
					" WHERE owner like '%"+username+"%'";  

					ResultSet rs = stmt.executeQuery(sql1);
					username = username.toUpperCase();
					String updater = "";

					/*
					 * Update the individual containers...
					 */
					while (rs.next())
					{
						//get the id
						String id = rs.getString("c.id");

						String owner = rs.getString("c.owner").toUpperCase();
						if(owner.indexOf("/") != -1)//there is two owners
						{                			
							int un = owner.indexOf(username);
							int sl = owner.indexOf("/");

							if (un == 0)
								owner = owner.substring(sl+1, owner.length());
							else
								owner = owner.substring(0, sl);

							owner = owner.trim();

							//add an update statement to the batch:
							updater = "UPDATE container" +
							" SET owner='"+owner+"'"+
							" WHERE id = "+id+"";

							stmt.addBatch(updater);                      	   	
						}
						else//the user is the only owner of the container, remove the name
						{
							//add an update statement to the batch:
							updater = "UPDATE container" +
							" SET owner=''"+
							" WHERE id = "+id+"";

							stmt.addBatch(updater);
						}

					}

					/*
					 * If containers are checked out by the user reset these..
					 */
					updater = "UPDATE container SET user_id = 0 WHERE user_id = "+id+";";
					stmt.addBatch(updater);

					/*
					 * Perform the batch update in the db...
					 */
					stmt.executeBatch();
					con.commit();
					stmt.close();
				}
				con.close();
				return true;
			}
		}//end of try

		catch (SQLException e)
		{
			System.out.println(""+e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println(""+e);
			return false;
		}
		return false;
	}

	/** Setter for the first name.
	 * @param fn String.
	 */  
	public void setFirst_name(String fn)
	{
		first_name = fn;
	}

	/** Getter for the first name.
	 * @return String.
	 */  
	public String getFirst_name()
	{
		return first_name;
	}

	/** Setter for id.
	 * @param u_id String.
	 */  
	public void setId(String u_id)
	{
		id = u_id;
	}

	/** Getter for id.
	 * @return String.
	 */  
	public String getId()
	{
		return id;
	}

	/** Setter for last name.
	 * @param ln String.
	 */  
	public void setLast_name(String ln)
	{
		last_name = ln;
	}

	/** Getter for last name.
	 * @return String.
	 */  
	public String getLast_name()
	{
		return last_name;
	}

	/** Setter for user name.
	 * @param un String.
	 */  
	public void setUser_name(String un)
	{
		user_name = un;
	}

	/** Get the user name.
	 * @return String.
	 */  
	public String getUser_name()
	{
		return user_name;
	}

	/** Set the room number.
	 * @param room String.
	 */  
	public void setRoom_number(String room)
	{
		room_number = room;
	}

	/** Getter for room number.
	 * @return String.
	 */  
	public String getRoom_number()
	{
		return room_number;
	}

	/** Setter for telephone.
	 * @param tel String.
	 */  
	public void setTelephone(String tel)
	{
		telephone = tel;
	}

	/** Get the phone number.
	 * @return String.
	 */  
	public String getTelephone()
	{
		return telephone;
	}

	/** Set the email.
	 * @param mail String.
	 */  
	public void setEmail(String mail)
	{
		email = mail;
	}

	/** Get the email.
	 * @return String.
	 */  
	public String getEmail()
	{
		return email;
	}

	public void setOrg_user_name(String org)
	{
		org_user_name = org;
	}

	public String getOrg_user_name()
	{
		return org_user_name;
	}


	/**
	 * @return Returns the newPassword.
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/** Remove user?
	 * @param rem String.
	 */  
	public void setRemove(String rem)
	{
		remove = rem;
	}

	/**
	 * @return Returns the mail.
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail The mail to set.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return Returns the privilege.
	 */
	public String[] getPrivilege() {
		return privilege;
	}
	/**
	 * @param privilege The privilege to set.
	 */
	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the user_type_id
	 */
	public int getUser_type_id() {
		return user_type_id;
	}

	/**
	 * @param user_type_id the user_type_id to set
	 */
	public void setUser_type_id(int user_type_id) {
		this.user_type_id = user_type_id;
	}


	/**
	 * @return the groups
	 */
	public String[] getGroups() {
		return groups;
	}


	/**
	 * @param groups the groups to set
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
}