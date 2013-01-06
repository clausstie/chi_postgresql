/**<ul>
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
import java.net.*;

import chemaxon.jchem.db.DatabaseOptions;
import chemicalinventory.user.PrivilegesBean;
import chemicalinventory.user.UserTypeBean;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;
import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.groups.User_group;

/**
 *Ccreate a new user in the db. And afterwards create a receipt with the result.
 **/

public class UserRegBean implements java.io.Serializable
{
	public UserRegBean()
	{
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5217533718115947551L;

	//initialisering af variabler
	private String firstName = "";
	private String lastName = "";
	private String userName = "";
	private String password = "";
	private String password_returner = "";
	private String userType = "";
	private String room_number = "";
	private String telephone = "";
	private String email = "";
	private String[] groups;
	private int check = 0;
	private int autoIncKey = -1;
	private int user_type_id;

	User_group groupBean = new User_group();
	PrivilegesBean privilegesBean = new PrivilegesBean();

	/** Perform the user registration the int returned indicated the status of the
	 * registration.
	 * @return int
	 */  
	public int regCheck()
	{
		userRegistration();

		return check;
	}

	/** 
	 * Perform the registration of a new user.
	 **/
	public void userRegistration()
	{
		check = 0;

		if(getUser_type_id() <= 0) {
			check = Return_codes.USER_TYPE_ERROR;
			return;
		}

		if(!firstName.equals("") && !lastName.equals("") && !userName.equals("") && !email.equals(""))
		{
			try{
				//Connection from the pool

				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT user_name FROM user" +
					" WHERE user_name='"+userName+"'";

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next()) {
						check = 1;
						con.close();
						return;
					}

					if(!rs.next()) {

						rs.close();
						con.setAutoCommit(false);

						//replace single ping ' with double ping ''.
						lastName = Util.double_q(lastName);
						firstName = Util.double_q(firstName);

						//get a random value for the password
						password = Util.generatePassword();

						setPassword_returner(password);

						/*Create the user information in the user table*/
						String insertUser = "INSERT INTO user (user_name, first_name, last_name, password, room_number, telephone, email, user_type_id)"+
						" VALUES('"+userName.toUpperCase()+"', '"+Util.double_q(firstName.toUpperCase())+"', '"+Util.double_q(lastName.toUpperCase())+"', MD5('"+password+"'), '"+room_number+"', '"+telephone+"', '"+email+"', "+getUser_type_id()+");";

						try {
							stmt.executeUpdate(insertUser, Statement.RETURN_GENERATED_KEYS);
						} catch (Exception e) {
							con.rollback();
							con.close();
							check = 2;
							return;
						}

						ResultSet key = stmt.getGeneratedKeys();

						/*If creation of the user in the user table was a succes, continue*/
						if(key.next())
						{
							autoIncKey = key.getInt(1);
						}

						key.close();

						/*
						 * Use the usertype to insert privileges in the roles table for the user.
						 */
						UserTypeBean userType = new UserTypeBean();
						Hashtable privileges = userType.getPrivilegesForType(getUser_type_id());
						stmt.clearBatch();

						if(privileges != null)	{

							for (Enumeration e = privileges.keys(); e.hasMoreElements();) {
								String p_id = (String) e.nextElement();
								String p_name = (String) privileges.get(p_id);									

								String insertP = "INSERT INTO roles (user_name, role, privileges_id, user_type_id)"+
								" VALUES('"+userName.toUpperCase()+"', '"+p_name+"', "+p_id+", "+getUser_type_id()+");";

								stmt.addBatch(insertP);
							}
						}

						/*
						 * Insert the user id in the link table between user and
						 * user group.
						 */
						if(groups != null)
						{
							for (int i = 0; i < groups.length; i++) {
								
								String token = groups[i];
								token = token.trim();
								stmt.addBatch("INSERT INTO user_group_user_link (user_id, group_id) VALUES("+autoIncKey+", "+token+");");
							}
						}

						try {
							stmt.executeBatch();							
							check=2;
							con.commit();//commit the changes to the db.
							con.close();
							return;
						} catch (Exception e) {
							con.rollback();
							e.printStackTrace();
							check = 2;
							return;
						}
					}
				}
			}//end of try

			catch (SQLException e)
			{
				check=0;
				e.printStackTrace();
			}
			catch (Exception e)
			{
				check=0;
				e.printStackTrace();
			}
		}
		else
		{ 
			check=0;
		}
	}

	/** 
	 * Create the data neede to print out a receipt after succesfull registration
	 * of a new user.
	 * @param uid String.
	 */
	public boolean userCred(String uid)
	{
		try{
			//Connection from the pool
			Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String receipt = "SELECT u.id, u.user_name, u.first_name, u.last_name, u.password, u.room_number, u.removed," +
					" u.telephone, u.organisation, u.department, u.email, u.user_type_id, ut.name FROM `user` u, user_types ut" +
					" WHERE u.id = "+uid+" AND ut.user_type_id = u.user_type_id;";

					ResultSet rs1 = stmt.executeQuery(receipt);

					if(rs1.next())
					{ 
						String name = rs1.getString("u.user_name").toUpperCase();
						String f_name = rs1.getString("u.first_name");
						String l_name = rs1.getString("u.last_name");
						String room = rs1.getString("u.room_number");
						String tele = rs1.getString("u.telephone");
						String mail = rs1.getString("u.email");
						String type = rs1.getString("ut.name");

						setFirstName(Util.encodeTagAndNull(f_name));
						setLastName(Util.encodeTagAndNull(l_name));
						setUserName(Util.encodeTagAndNull(name));
						setUserType(Util.encodeTagAndNull(type));
						setRoom_number(Util.encodeTagAndNull(room));
						setTelephone(Util.encodeTagAndNull(tele));
						setEmail(Util.encodeTagAndNull(mail));
					}
					
					/*
					 * Get the groups
					 */
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_user_link l, user u"+
					" WHERE g.id = l.group_id"+
					" AND l.user_id = u.id"+
					" AND l.user_id = "+uid+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					Vector groups_v = new Vector();
					
					while (rs.next())
					{
						groups_v.add(rs.getString("g.name") + "<br>");
					}
					
					this.groups = new String[groups_v.size()];
					for (int i = 0; i < groups_v.size(); i++) {
					
						String element = (String) groups_v.get(i);
						this.groups[i] = element;						
					}
															
					con.close();
					
					return true;
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

	/** Setter for the first name.
	 * @param fn String.
	 */  
	public void setFirstName(String fn)
	{
		firstName = fn;
	}

	/** Getter for the first name.
	 * @return String.
	 */  
	public String getFirstName()
	{
		return firstName;
	}

	/** Setter for the last name.
	 * @param ln String.
	 */  
	public void setLastName(String ln)
	{
		lastName = ln;
	}

	/** Getter for the last name.
	 * @return String.
	 */  
	public String getLastName()
	{
		return lastName;
	}

	/** Setter for the user name.
	 * @param un String.
	 */  
	public void setUserName(String un)
	{
		userName = un;
	}

	/** Getter for the user name.
	 * @return String.
	 */  
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/** Setter for the type of user.
	 * @param ut String.
	 */  
	public void setUserType(String ut)
	{
		userType = ut;
	}

	/** Getter the user type.
	 * @return String.
	 */  
	public String getUserType()
	{
		return userType;
	}

	/** Setter for the room number.
	 * @param rn String.
	 */  
	public void setRoom_number(String rn)
	{
		if(rn.equals(""))
		{
			rn = "-";
		}
		room_number = rn;
	}

	/** Getter for the room number.
	 * @return String.
	 */  
	public String getRoom_number()
	{
		return room_number;
	}

	/** Setter for the telephone number.
	 * @param tel String.
	 */  
	public void setTelephone(String tel)
	{
		telephone = tel;
	}

	/** Getter for the telephone number.
	 * @return String.
	 */  
	public String getTelephone()
	{
		return telephone;
	}

	/** Set the email value.
	 * @param mail String.
	 */  
	public void setEmail(String mail)
	{
		email = mail;
	}

	/** Getter for the value of the email
	 * @return String.
	 */  
	public String getEmail()
	{
		return email;
	}

	/**
	 * @return Returns the password_returner.
	 */
	public String getPassword_returner() {
		return password_returner;
	}
	/**
	 * @param password_returner The password_returner to set.
	 */
	public void setPassword_returner(String password_returner) {
		this.password_returner = password_returner;
	}

	/** Getter for the key generated when registering a new user.
	 * @return String.
	 */  
	public String getAutoIncKey()
	{
		return ""+autoIncKey;
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