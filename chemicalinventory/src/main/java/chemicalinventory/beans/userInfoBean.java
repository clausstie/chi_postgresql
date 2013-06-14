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
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

package chemicalinventory.beans;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.*;
import java.net.*;
import java.text.*;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;
import chemicalinventory.user.UserInfo;

/**
 *Class containing methods to get information about users, locations and 
 *and containers.
 **/

public class userInfoBean implements java.io.Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -5549446052737504370L;

public userInfoBean()
  {
  }

  private String user_name = "";
  private String user_id = "";
  private String container_id = "";
  private String current_location= "";
  private String home_location= "";
  private String room = "";
  private String email = "";
  private String telephone = "";
  private String fullname = "";
  private String base = "";
  private String current_quantity = "";
  private String unit = "";
  String loc_id;
  public Vector userContainer = new Vector();
  public Vector conContainer = new Vector();
  public Vector container = new Vector();
  public Vector location = new Vector();
  public Vector container_ids = new Vector();
  Vector id_location_list = new Vector();
  
  //location variables
  private String firstChoice =  "0";
  private String secondChoice =  "0";
  private String thirdChoice =  "0";
  
  /*an instance of the UserInfo class, used to get information on 
  *a username in the owners field*/
  UserInfo ui = new UserInfo();
  
  /** Set the user name.
   * @param un String.
   */  
  public void setUser_name(String un)
  { 
    user_name = un;
  }

  /** Getter for username.
   * @return String.
   */  
  public String getUser_name()
  {
    return user_name;
  }

  /** Setter for container id.
   * @param cid String.
   */  
  public void setContainer_id(String cid)
  { 
    container_id = cid;
  }

  /** Get the container id.
   * @return String.
   */  
  public String getContainer_id()
  {
    return container_id;
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
  /** Getter for the room.
   * @return String.
   */  
  public String getRoom()
  {
    return room;
  }
  
  /** Getter for the email.
   * @return String.
   */  
  public String getEmail()
  {
    return email;
  }

  /** Getter for the telephone number.
   * @return String.
   */  
  public String getTelephone()
  {
    return telephone;
  }
  
  /** Set the first choice box value.
   * @param one String.
   */  
  public void setFirstChoice(String one )
  {
    firstChoice = one;
  }
  
  /** Set the second choice box value.
   * @param two String.
   */  
  public void setSecondChoice(String two)
  {
    secondChoice = two;
  }
  
  /** Setter for the third choice box.
   * @param three String.
   */  
  public void setThirdChoice(String three)
  {
    thirdChoice = three;
  } 
  
/**
 * @return Returns the fullname.
 */
public String getFullname() {
	return fullname;
}

  /**
   *This method gets info about a specific user.
   *@param un User name of the user, you want to get infomation about.
   **/
  public void shortUserInfo(String un)
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

                String sql = "SELECT id, user_name, first_name, last_name, room_number, telephone, email FROM user" +
                             " WHERE user_name='"+un+"'";

                ResultSet rs = stmt.executeQuery(sql);
                
                //if there exist a user with the user name entered - continue.
                if(rs.next())
                {
                	if(!base.equals(""))
                      user_name = ui.display_owner_data_base(user_name = rs.getString("user_name").toUpperCase(), base);
                	else
                		user_name = ui.display_owner_data(user_name = rs.getString("user_name").toUpperCase());
                		
                      room = rs.getString("room_number");
                      telephone = rs.getString("telephone");
                      email = rs.getString("email");

                      if(user_name == null || user_name.equals(""))
                      {
                        user_name = "-";
                      }
                      if(room == null || room.equals(""))
                      {
                        room = "-";
                      }
                      if(telephone == null || telephone.equals(""))
                      {
                        telephone = "";
                      }
                      if(email == null || email.equals(""))
                      {
                        email = "-";
                      }
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
      System.out.println(e);
    }
  }
  
  /** Method that retrieves information about a user, and displays the containers
   * that this user has checked out.
   **/
  public void userInfo(String orderby)
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

                String sql = "SELECT id, user_name, first_name, last_name, room_number, telephone, email FROM user" +
                             " WHERE user_name='"+user_name+"'";

                ResultSet rs = stmt.executeQuery(sql);

                //if there exist a user with the user name entered - continue.
                if(rs.next() && rs.getInt("id") != 0)
                {              	  
                      String name = rs.getString("user_name").toUpperCase();
                      String f_name = rs.getString("first_name");
                      String l_name = rs.getString("last_name");
                      String room = rs.getString("room_number");
                      String tele = rs.getString("telephone");
                      String mail = rs.getString("email");

                      if(name == null || name.equals(""))
                      {
                        name = "-";
                      }
                      else
                    	  setUser_name(name);
                      if(f_name == null || f_name.equals(""))
                      {
                        f_name = "-";
                      }
                      if(l_name == null || l_name.equals(""))
                      {
                        l_name = "-";
                      }
                      if(room == null || room.equals(""))
                      {
                        room = "-";
                      }
                      if(tele == null || tele.equals(""))
                      {
                        tele = "-";
                      }
                      if(mail == null || mail.equals(""))
                      {
                        mail = "-";
                      }
                      
                  userContainer.addElement(name+"|"+f_name+" "+l_name+"|"+room+"|"+tele+"|"+mail);

            	  //set the full name attribute
                  this.fullname = f_name.toUpperCase()+" "+l_name.toUpperCase();
                  this.email = mail;
                  this.user_name = name;
                  
                  this.user_id = rs.getString("id");

                  /*
                   * Get the containers for the user.
                   */
                  
                  int order_by = 1;

                  try {
                	  order_by = Integer.parseInt(orderby);
				} catch (Exception e) {
					order_by = 1;
				}
				String sql0 = "SELECT c.id, com.chemical_name, c.user_id, u.user_name, l.id"+
                                " FROM location l, compound com, container c LEFT JOIN user u" +
                                " ON c.user_id = u.id"+ 
                                " WHERE c.user_id="+user_id+"" +
                                " AND c.compound_id = com.id" +
                                " AND c.location_id = l.id ORDER BY "+order_by;

                  ResultSet rs0 = stmt.executeQuery(sql0);

                  while(rs0.next())
                  {
                    //Here is selected all the container which has a location id
                    //matching the user id.
                 	container.addElement(rs0.getString("c.id")+"|"+rs0.getString("com.chemical_name")+"|"+Util.getLocation(rs0.getString("l.id")));
                    //container.addElement(rs0.getString("c.id")+"|"+rs0.getString("com.chemical_name")+"|"+rs0.getString("u.user_name").toUpperCase());
                    container_ids.addElement(rs0.getString("c.id"));
                  }
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
      System.out.println(e);
    }
  }
  
  /**
   * Create a vector holding all the containers checked out by a user.
   * @return
   */
  public Vector userContainerInfo(String u_id)
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

                /*get the information about the container checked-out by the user in question*/
                
                  String sql0 = "SELECT c.id, com.chemical_name, c.location_id"+
                                " FROM location l, compound com, container c LEFT JOIN user u" +
                                " ON c.user_id = u.id"+ 
                                " WHERE c.user_id="+u_id+"" +
                                " AND c.compound_id = com.id" +
                                " AND c.location_id = l.id" +
                                " ORDER  BY c.id, com.chemical_name;";

                  ResultSet rs0 = stmt.executeQuery(sql0);

                  container.clear();
                  
                  while(rs0.next())
                  {
                    //Here is selected all the container which has a location id
                    //matching the user id.
                    container.addElement(rs0.getString("c.id")+"|"+rs0.getString("com.chemical_name")+"|"+Util.getLocation(rs0.getString("c.location_id")));
                  }
                  
                  con.close();
                  return container;
                }
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
   * Get a list of containers registered by a spcific user.
   * @param u_id
   * @return
   */
  public Vector userContainerRegistrationList(String u_id, String orderby)
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

                /*get the information about the container checked-out by the user in question*/
                
                if(orderby == null || orderby.equals(""))
                	orderby = "4";
                
                  String sql0 = "SELECT c.id, com.chemical_name, c.location_id, c.register_date"+
                                " FROM location l, compound com, container c LEFT JOIN user u" +
                                " ON c.register_by = u.id"+ 
                                " WHERE c.register_by='"+u_id+"'" +
                                " AND c.compound_id = com.id" +
                                " AND c.location_id = l.id" +
                                " AND c.empty = 'F'"+
                                " ORDER BY "+orderby+";";

                  ResultSet rs0 = stmt.executeQuery(sql0);
                  
                  Vector containerlist = new Vector();
                  
                  while(rs0.next())
                  {
                    //Here is selected all the container which has a location id
                    //matching the user id.
                    containerlist.addElement(rs0.getString("c.id")+"|"+rs0.getString("com.chemical_name")+"|"+Util.getLocation(rs0.getString("c.location_id"))+"|"+rs0.getString("register_date"));
                  }
                  
                  con.close();
                  return containerlist;
                }
             }
    }//end of try

    catch (Exception e)
    {
    	e.printStackTrace();
    	return null;
    }
    
    return null;
  }  
  
  /**
   * Create a list of compounds registered by a specific user.
   * @param u_name
   * @param orderby
   * @return
   */
  public Vector userCompoundRegistrationList(String u_name, String orderby)
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

                /*get the information about the container checked-out by the user in question*/
                
                if(orderby == null || orderby.equals(""))
                	orderby = "2";
                
                  String sql0 = "SELECT id, chemical_name, cas_number, density, remark, register_date FROM compound"+
					" WHERE deleted = 0 AND register_by = '"+u_name+"'"+
					" ORDER BY "+orderby+";";

                  ResultSet rs0 = stmt.executeQuery(sql0);
                  
                  Vector list = new Vector();
                  
                  while(rs0.next())
                  {
                	  String c_name = Util.encodeTagAndNull(rs0.getString("chemical_name"));
                	  String c_cas = Util.encodeTagAndNull(rs0.getString("cas_number"));
                	  String c_density = Util.encodeTagAndNull(rs0.getString("density"));
                	  String c_reamrk = Util.encodeTagAndNull(rs0.getString("remark"));
                	  String c_regdate = Util.encodeTagAndNull(rs0.getString("register_date"));

                	  //Here is selected all the container which has a location id
                    //matching the user id.
                    list.addElement(c_name+"|"+c_cas+"|"+c_density+"|"+c_reamrk+"|"+c_regdate);
                  }
                  
                  con.close();
                  return list;
                }
             }
    }//end of try

    catch (Exception e)
    {
    	e.printStackTrace();
    	return null;
    }
    
    return null;
  }  

  
  /**
   * 
   * @param username
   * @return
   */
  public String mailInfo(String username)
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

                ui.retrieveNameId(username);
                  int u_id = ui.getUser_id();
                  /*get the information about the container checked-out by the user in question*/
            
                  String sql0 = "SELECT c.id, com.chemical_name, c.user_id, u.user_name, l.id"+
                                " FROM location l, compound com, container c LEFT JOIN user u" +
                                " ON c.user_id = u.id"+ 
                                " WHERE c.user_id="+u_id+"" +
                                " AND c.compound_id = com.id" +
                                " AND c.location_id = l.id";

                  ResultSet rs0 = stmt.executeQuery(sql0);

                  while(rs0.next())
                  {
                    //Here is selected all the container which has a location id
                    //matching the user id.
                    container.addElement(rs0.getString("c.id")+"|"+rs0.getString("com.chemical_name")+"|"+rs0.getString("u.user_name").toUpperCase());
                  }
                }
             con.close();
             }
         
         /*
          * Now we have the information - start to build the html
          * tags to make up the body of an email. This must consist
          * of header plus the rows of data.
          */
         String table_body = "";
         String table_data_header = "<table cellspacing=\"1\" cellpadding=\"1\" border=\"1\" width=\"100%\" rules=\"rows\" align=\"center\">"+
		    "<thead>"+
		        "<tr>"+
		             "<th class=\"blue\">Container Id:</th>"+
		             "<th class=\"blue\">Chemical Name:</th>"+
		             "<th class=\"blue\">Location:</th>"+
		        "</tr>"+
		    "</thead>"+
		  "<tbody>";
                  
		     for(int i=0; i<container.size(); i++)
		     {
		        String color = null;
		         if(i % 2 != 0)
		          {
		            color = "blue";
		          }
		          else
		            color = "normal";
		         
		        table_body = table_body + "<tr class=\""+color+ "\">";
		        String data = (String) container.elementAt(i);
		        StringTokenizer tokens = new StringTokenizer(data, "|", false);
		        while(tokens.hasMoreElements())
		        {
		          String token = tokens.nextToken();
		          token.trim();
		          table_body = table_body +
		          "<td align=\"center\">"+token+"</td>";
		       }
		      table_body = table_body + "</tr>";
		   }
		  table_body = table_body + "</tbody>" +
		  "</table>";
		  
		  String complete_table_data = table_data_header+table_body;

         return complete_table_data;
         
    }//end of try

    catch (ClassNotFoundException e) 
    {
      System.out.println(e);
      return null;
    }
    catch (SQLException e)
    {
     System.out.println(e);
     return null;
    }
    catch (Exception e)
    {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Get the current location of a container
   * @param containerId
   * @return
   */
  public String getCurrentcontainerLocation(int containerId)
  {
    try{
    	String container_location = "";
    	String theHome = "";
    	
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

               String sql_con = "SELECT c.user_id," +
               					" u.user_name, l.id"+
                                " FROM location l, container c" +
                                " LEFT JOIN user u ON c.user_id = u.id" +
                                " WHERE c.id="+containerId+"" +
                                " AND c.location_id = l.id" +
                                " AND c.empty = 'F'";

               ResultSet res = stmt.executeQuery(sql_con);
               
               if(res.next())
               {   
            	   theHome = Util.getLocation(res.getString("l.id")); 
                   //if the container is at its home location 
                   //get this location from the location table.
                  if(res.getString("c.user_id").equals("0"))
                  {
                	  container_location = theHome;
                  }
                  else//the container is checked out by a user
                  {
                  	if(!base.equals(""))
                  		container_location = ui.display_owner_data_base(res.getString("u.user_name").toUpperCase(), base);
                  	else
                  		container_location = ui.display_owner_data(res.getString("u.user_name").toUpperCase());
                  }
                }
             }
             con.close();
             
             return container_location;
         }
         
         return "";
    }//end of try

    catch (Exception e)
    {
      e.printStackTrace();
      return "";
    }
  }
  
  /**
   *Find all the containers at a location. if the location
   *is a level 0, go through the tree and find all containers, at the appropriate
   *sub locations.
   *If the location i a level 1 location find all sub locations here, and get the
   *containers at this location.
   *
   *@param type Set type to 1 if a vector with all ids is needed.
   **/
  public void container_at_location_Info(int type)
  {
    try{        
         /*Create the decimal formatter to make the quantity string look nice*/
         DecimalFormat format = new DecimalFormat(Util.PATTERN);
        
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
                String sql = "";
                String sql_location = "";
                String select_id = "";
              
                //get info about a container dependent on which level is selected.
                //if only level_0 is selected get info for all containers in that 'building'
                //else if level_1 is selected get info for all containers on that level etc.
                if(!firstChoice.equals("0") && !secondChoice.equals("0") && !thirdChoice.equals("0"))
                {
                    select_id = "= "+thirdChoice;
                } else 
                	if(!secondChoice.equals("0") && !firstChoice.equals("0") && thirdChoice.equals("0"))
                       {
                           sql_location = "SELECT id" +
                           		" FROM location" +
                           		" WHERE location_id = "+secondChoice+";";
                           
                           ResultSet rs = stmt.executeQuery(sql_location);
                           
                           while (rs.next())
                           {
                               if (!rs.isLast())
                               {
                                 select_id = select_id+rs.getString("id")+", ";
                               }
                               else
                               {
                                 select_id = select_id+rs.getString("id");
                               }
                           }                       
                           
                           select_id = "in ("+select_id+")";
                       } else 
                       	 if(thirdChoice.equals("0") && secondChoice.equals("0") && !firstChoice.equals("0"))
                         {
                           sql_location = "SELECT l3.id"+
                                          " FROM location l1, location l2, location l3"+
                                          " WHERE l2.id = l3.location_id"+
                                          " AND l1.id = l2.location_id"+
                                          " AND l1.id = "+firstChoice+";";
                           
                           ResultSet rs1 = stmt.executeQuery(sql_location);
                           
                           while (rs1.next())
                           {
                               if (!rs1.isLast())
                               {
                               		select_id = select_id + rs1.getString("l3.id") +", ";
                               }
                               else
                               {
                                   select_id = select_id + rs1.getString("l3.id");
                               }
                           }
                           select_id = "in ("+select_id+")";
                         }

                sql = "SELECT c.id, chem.chemical_name, c.location_id, c.empty, c.owner,"+
                      " c.remark, c.current_quantity, c.unit, c.register_by, c.register_date,"+
                      " c.user_id, u.user_name, u.room_number, u.telephone"+
                      " FROM location l, compound chem, container c LEFT JOIN user u"+
                      " ON c.user_id = u.id"+
                      " WHERE c.compound_id = chem.id"+
                      " AND c.location_id = l.id"+
                      " AND c.current_quantity > 0"+
                      " AND c.empty = 'F'"+
                      " AND c.location_id "+select_id+
                      " ORDER BY c.id, chem.chemical_name;";

                ResultSet rs2 = stmt.executeQuery(sql);
                
                while (rs2.next())
                {
                     String unit = rs2.getString("c.unit");
                     String owner = rs2.getString("c.owner");
//                     String room = rs2.getString("u.room_number");
//                     String phone = rs2.getString("u.telephone");
                     String mark = rs2.getString("c.remark");
                     String cur_quant = rs2.getString("c.current_quantity");
                     String loc_id = rs2.getString("c.location_id");
                     String home_location = Util.getLocation(loc_id);
                     String name = rs2.getString("chem.chemical_name");;
                     String cur_loc = "";

                     if(rs2.getString("c.user_id").equals("0"))
                     {
                       cur_loc = "Home location";
                     }
                      else
                      {
                      	if (!base.equals(""))
                      		cur_loc = ui.display_owner_data_base(rs2.getString("u.user_name"), base);
                     	else
                     		cur_loc = ui.display_owner_data(rs2.getString("u.user_name"));
                      }

                     //replace empty fields with "-", and encode where nessecary.
                     if(name == null || name.equals(""))
                     {
                       name = "--";
                     }
                     else
                     {
                         name = Util.encodeTag(name);
                         name = URLEncoder.encode(name, "UTF-8");
                     }
                     if(unit == null || unit.equals(""))
                     {
                         unit = "--";
                     }
                     if(home_location == null || home_location.equals(""))
                     {
                         home_location = "--";
                     }
                     if(owner == null || owner.equals("") || owner.equals("null"))
                     {
                         owner = "--";
                     }
                     else 
                     {                     	
                     	if (!base.equals(""))
                     		owner = ui.display_owner_data_base(owner, base);
                     	else
                     		owner = ui.display_owner_data(owner);
                     }
                     
                     if(cur_quant == null || cur_quant.equals(""))
                     {
                         cur_quant = "--";
                     }
                     else
                         cur_quant = format.format(rs2.getDouble("c.current_quantity"));
                     
                     if(mark == null || mark.equals(""))
                     {
                         mark = "--";
                     }
                     else
                     {
                         mark = Util.encodeTag(mark);
                         mark = URLEncoder.encode(mark, "UTF-8");
                     }
                     //fill a vector with the results ready to be displayed on the jsp page.
                     location.addElement(name+"|"+rs2.getString("c.id")+"|"+cur_quant+unit+"|"+home_location+"|"+cur_loc+"|"+owner+"|"+mark);
                     
                     /*If the search is required to include a vector with the container ids... do that*/
                     if (type == 1)
                     {
                         container_ids.addElement(rs2.getString("c.id"));
                     }
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
      System.out.println(e);
    }
  }
  
  /**
   *Find all the containers at a location. if the location
   *is a level 0, go through the tree and find all containers, at the appropriate
   *sub locations.
   *If the location i a level 1 location find all sub locations here, and get the
   *containers at this location. THIS METHOD IS USED FOR CREATING PDF REPORT!!
   *
   *@param type Set type to 1 if a vector with all ids is needed.
   **/
  public void container_at_location_Info2()
  {
    try{        
         /*Create the decimal formatter to make the quantity string look nice*/
         DecimalFormat format = new DecimalFormat(Util.PATTERN);
        
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
                String sql = "";
                String sql_location = "";
                String select_id = "";
              
                //get info about a container dependent on which level is selected.
                //if only level_0 is selected get info for all containers in that 'building'
                //else if level_1 is selected get info for all containers on that level etc.
                if(!firstChoice.equals("0") && !secondChoice.equals("0") && !thirdChoice.equals("0"))
                {
                    select_id = "= "+thirdChoice;
                } else 
                	if(!secondChoice.equals("0") && !firstChoice.equals("0") && thirdChoice.equals("0"))
                       {
                           sql_location = "SELECT id FROM location WHERE location_id = "+secondChoice+";";
                           ResultSet rs = stmt.executeQuery(sql_location);
                           
                           while (rs.next())
                           {
                               if (!rs.isLast())
                               {
                                 select_id = select_id+rs.getString("id")+", ";
                               }
                               else
                               {
                                 select_id = select_id+rs.getString("id");
                               }
                           }                       
                           
                           select_id = "in ("+select_id+")";
                       } else 
                       	 if(thirdChoice.equals("0") && secondChoice.equals("0") && !firstChoice.equals("0"))
                         {
                           sql_location = "SELECT l3.id"+
                                          " FROM location l1, location l2, location l3"+
                                          " WHERE l2.id = l3.location_id"+
                                          " AND l1.id = l2.location_id"+
                                          " AND l1.id = "+firstChoice+";";
                           
                           ResultSet rs1 = stmt.executeQuery(sql_location);
                           
                           while (rs1.next())
                           {
                               if (!rs1.isLast())
                               {
                               		select_id = select_id + rs1.getString("l3.id") +", ";
                               }
                               else
                               {
                                   select_id = select_id + rs1.getString("l3.id");
                               }
                           }
                           select_id = "in ("+select_id+")";
                         }

                sql = "SELECT c.id, chem.chemical_name, c.location_id, c.empty, c.owner,"+
                      " c.remark, c.current_quantity, c.unit, c.register_by, c.register_date,"+
                      " c.user_id, u.user_name, u.room_number, u.telephone"+
                      " FROM location l, compound chem, container c LEFT JOIN user u"+
                      " ON c.user_id = u.id"+
                      " WHERE c.compound_id = chem.id"+
                      " AND c.location_id = l.id"+
                      " AND c.current_quantity > 0"+
                      " AND c.empty = 'F'"+
                      " AND c.location_id "+select_id+
                      " ORDER BY c.location_id, c.id, chem.chemical_name;";

                ResultSet rs2 = stmt.executeQuery(sql);

                int current_location = 0;
                id_location_list.clear();
                location.clear();
                
                while (rs2.next())
                {
                	int location_id_int = rs2.getInt("c.location_id");
                	
                	if(current_location != location_id_int)
                	{
                		current_location = location_id_int;
                		id_location_list.add(current_location+"|"+Util.getLocation_no_br(rs2.getString("c.location_id")));
                		
//                		String loc_id = rs2.getString("c.location_id");
//                      String home_location = Util.getLocation(loc_id);
                        
                	}
                	
                     String unit = rs2.getString("c.unit");
                     String owner = rs2.getString("c.owner");
//                     String room = rs2.getString("u.room_number");
//                     String phone = rs2.getString("u.telephone");
                     String mark = rs2.getString("c.remark");
                     String cur_quant = rs2.getString("c.current_quantity");
                     
                     String name = rs2.getString("chem.chemical_name");;
                     String cur_loc = "";

                     if(rs2.getString("c.user_id").equals("0"))
                     {
                       cur_loc = "Home location";
                     }
	                  else
	                  {
	               		cur_loc = rs2.getString("u.user_name");
	                  }

                     //replace empty fields with "-", and encode where nessecary.
                     if(name == null || name.equals(""))
                     {
                       name = "-";
                     }
                     else
                     {
                         name = Util.encodeTag(name);
                         name = URLEncoder.encode(name, "UTF-8");
                     }
                     if(unit == null || unit.equals(""))
                     {
                         unit = "-";
                     }
                     if(owner == null || owner.equals("") || owner.equals("null"))
                     {
                         owner = "-";
                     }
                     
                     if(cur_quant == null || cur_quant.equals(""))
                     {
                         cur_quant = "-";
                     }
                     else
                         cur_quant = format.format(rs2.getDouble("c.current_quantity"));
                     
                     if(mark == null || mark.equals(""))
                     {
                         mark = "-";
                     }
                     else
                     {
                         mark = Util.encodeTag(mark);
                         mark = URLEncoder.encode(mark, "UTF-8");
                     }
                     //fill a vector with the results ready to be displayed in the report
                     location.addElement(current_location+"|"+name+"|"+rs2.getString("c.id")+"|"+cur_quant+" "+unit+"|"+cur_loc+"|"+owner+"|"+mark);
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
 * @return Returns the id_location_list.
 */
public Vector getId_location_list() {
	return id_location_list;
}
/**
 * @return Returns the location.
 */
public Vector getLocation() {
	return location;
}
/**
 * @return Returns the user_id.
 */
public String getUser_id() {
	return user_id;
}
}