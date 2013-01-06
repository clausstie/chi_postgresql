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
import javax.naming.*;
import javax.sql.*;

import chemicalinventory.context.Attributes;

/** 
 * This bean is used to hold logic when a user changes his password.<br>
 *
 **/

public class ChangePasswordBean implements java.io.Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6743610608840403113L;

	public ChangePasswordBean()
    {  }
  
  private String username = ""; 
  private String password = "";
  private String newPassword = "";
  private String verifyPassword = "";
  private boolean changepwdAccepted;
  
  /** Setter for the user name.
   * @param un String
   */  
  public void setUsername(String un)
  {
    username = un.toUpperCase();
  }

  /** Getter for user name.
   * @return String user name.
   */  
  public String getUsername()
  {
    return username.toUpperCase();
  }

  /** Setter for password.
   * @param pwd String password.
   */  
  public void setPassword(String pwd)
  {
    password = pwd;
  }

  /** Setter for a new password.
   * @param newpwd String.
   */  
  public void setNewPassword(String newpwd)
  {
    newPassword = newpwd;
  }

  /** Getter for the new password.
   * @return String.
   */  
  public String getNewPassword()
  {
    return newPassword;
  }

  /** Setter to verify a password.
   * @param vp String.
   */  
   public void setVerifyPassword(String vp)
  {
    verifyPassword = vp;
  }
  
   /**
    * @return Boolean.
    */   
  public boolean newPwdOK()
  {    
    changepwd();

    return changepwdAccepted;
  }
    
  /** Change the users password stored in the db. **/
  public void changepwd()
  {
    changepwdAccepted = false;
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
      
                  String UnPwd = "SELECT user_name, password FROM user" +
                               " WHERE user_name='"+username+"'";

                  ResultSet rs = stmt.executeQuery(UnPwd);

                    if(rs.next())
                    {
                      //Check that the current password is different from the new one.
                      //Requirements for the new password need to be fullfilled:
                      //The two new passwords must match
                      //The new password cannot be the same as the old one
                      //Must be more than 5 characters long
                      
                      if(!verifyPassword.equals(password) && newPassword.equals(verifyPassword) && verifyPassword.length()>4)
                      {                          
                          String sql = "UPDATE user" +
                                       " SET password=MD5('"+verifyPassword+"')"+
                                       " WHERE user_name='"+username+"'";
                          
                          stmt.executeUpdate(sql);

                          stmt.close();
                          changepwdAccepted = true;
                      }
                      else
                      {
                        changepwdAccepted = false;
                      }
                   rs.close();
                   }
                   else
                   {
                     changepwdAccepted = false;
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
}