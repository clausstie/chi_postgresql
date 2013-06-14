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

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 *Control the logic concerning suppliers: Add, delete and modify.
 **/

public class supplierBean implements java.io.Serializable
{	

  public supplierBean()
  {
  }
  
  private static final long serialVersionUID = 2983283716702142640L;
  
  private String supplier_name = "";
  private String id = "";
  public Vector supplier = new Vector();
  public Vector supplier_id = new Vector();
  public boolean control;
  
  /** Setter for name of a supplier.
   * @param sn String.
   */  
  public void setSupplier_name(String sn)
  {
    supplier_name = sn.trim();
  }

  /** Get the supplier name.
   * @return String.
   */  
  public String getSupplier_name()
  {
    return supplier_name;
  }
  
  /** Set the id of a supplier.
   * @param i String.
   */  
  public void setId(String i)
  {
    id = i;
  }

  /** Getter for the id.
   * @return String.
   */  
  public String getId()
  {
    return id;
  }
  
  /** Getter to check if control was ok.
   * @return Boolean.
   */  
  public boolean isControl()
  {
      return control;
  }
    
  /**
   * Method used to get all suppliers from the db.
   **/
  public void getSuppliers()
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

                 String sql = "SELECT id, supplier_name FROM supplier ORDER BY supplier_name";

                 ResultSet rs = stmt.executeQuery(sql);

                 supplier.clear();
                 supplier_id.clear();
                 String supplier_name = "";

                 while(rs.next())
                 {
                   if(rs.getString("supplier_name").trim().equals(""))
                   {
                       supplier_name = "-";
                   }
                   else
                   {
                       supplier_name = URLEncoder.encode(rs.getString("supplier_name"), "UTF-8");
                   }

                   supplier_id.addElement(rs.getString("id"));
                   supplier.addElement(""+supplier_name);
                 }
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
  
  
  /**
   * Get the name of a single supplier from the id.
   * @param id
   * @return name of the supplier
   */
  public String getSupplier_name(String id)
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

                 String sql = "SELECT supplier_name FROM supplier WHERE id = "+id;

                 ResultSet rs = stmt.executeQuery(sql);

                 if(rs.next())
                 {
                 	String name = rs.getString("supplier_name"); 
                 	con.close();
                 	return name;
                 }
                 else
                 {
                 	con.close();
                 	return "--";
                 }
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

  /**
   *Modify a supplier... means modify the name.
   **/
  public void modifySupplier()
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

                 String register_name = supplier_name;
                 register_name = Util.double_q(register_name);
                 
                 String sql = "UPDATE supplier SET supplier_name='"+register_name+"' WHERE id = "+id+"";
                 stmt.executeUpdate(sql);
                 control = true;
             }
             con.close();
         }
    }//end of try
      catch (ClassNotFoundException e) 
      {
        System.out.println(""+e);
        control = false;
      }
      catch (SQLException e)
      {
        System.out.println(""+e);  
        control = false;
      }
      catch (Exception e)
      {
        System.out.println(""+e);
        control = false;
      }
  }

  /**
   * Register a new supplier in the system.
   **/
  public void registerSupplier()
  {
    if(!supplier_name.equals("") && supplier_name != null)
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

                 String register_name = supplier_name;
                 register_name = Util.double_q(register_name);
                               
                 String sql = "INSERT INTO supplier (supplier_name) VALUES ('"+register_name+"')";
                 stmt.executeUpdate(sql);
                 control = true;
             }
             con.close();
         }
     }//end of try
      catch (ClassNotFoundException e) 
      {
        control = false;
        System.out.println(""+e);
      }
      catch (SQLException e)
      {
        control = false;
        System.out.println(""+e);  
      }
      catch (Exception e)
      {
        control = false;
        System.out.println(""+e);
      }
    }
    else
    {
      control = false;
    }
  }
  
  /** 
   * Method used to delte a supplier. This has the effect that 
   *all containers with the deleted supplier attached is set to null.
   *and not deleting the containers.
   **/
  public void deleteSupplier()
  {
      control = false;
      
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
                 
                 //Update the containers with the deleted supplier
                 String updateContaiener = "UPDATE container SET supplier_id = 0 where supplier_id = "+id;
                 stmt.executeUpdate(updateContaiener);

                 //Delete the supplier from the supplier table.
                 String deleteSupplier = "DELETE FROM supplier WHERE id = "+id;
                 stmt.executeUpdate(deleteSupplier);
                 
                 con.commit();
                 
                 control = true;
             }
             con.close();
         }
     }//end of try
      catch (ClassNotFoundException e) 
      {
        control = false;
        System.out.println(""+e);
      }
      catch (SQLException e)
      {
        control = false;
        System.out.println(""+e);  
      }
      catch (Exception e)
      {
        control = false;
        System.out.println(""+e);
      }
    }
}