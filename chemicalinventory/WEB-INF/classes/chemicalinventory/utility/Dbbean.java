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
package chemicalinventory.utility;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;

/**
 * @author Dann Vestergaard
 */
public class Dbbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6836078296416962754L;

	/**
	 * Perform a single update in the database.
	 * @param sql
	 * @return
	 */
	public static boolean performUpdate(String sql)
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
		               
		               stmt.executeUpdate(sql);
		            }
		            con.close();
		            return true;
		         }
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
		  return false;
	}
}