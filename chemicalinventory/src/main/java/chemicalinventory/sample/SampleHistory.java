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
package chemicalinventory.sample;

import java.io.Serializable;
import java.net.URLEncoder;
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
public class SampleHistory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4730642401962068860L;
	private Vector elements = new Vector();
	private String chemical_name = "";
	private String sample_id = "";
	private int statuscode = 0;
	
	SampleBean sampleBean = new SampleBean();
	
	/**
	 * Here the history data for a single analysis is selected from the db.
	 * The data is stored into a list to be run through on the jsp page for display.
	 * @param id
	 * @return true false of the operation status.
	 */
	public boolean sample_history(String id)
	{
		//is the sample id a valid number
		if(!Util.isValidInt(id))
		{
			statuscode = 1;//not a valid sample number
			return false;
		}
		
		//make sure that the received sample id is a valid sample
		if(!sampleBean.isSample(id))
		{
			statuscode = 2;
			return false;
		}
		
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
					
					String query = "SELECT sample_history.remark, sample_history.timestamp," +
							" sample_history.changed_by, sample_history.change_remark, compound.chemical_name" +
							" FROM sample_history" +
							" LEFT JOIN compound on sample_history.compound_id = compound.id" +
							" WHERE sample_history.sample_id = " + id +
							" ORDER BY sample_history.sample_id, sample_history.timestamp;";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					elements.clear();
					
					while(rs2.next())
					{
						//find and encode results
						String name = rs2.getString("compound.chemical_name");
						String remark = rs2.getString("sample_history.change_remark");
						String timestamp = rs2.getString("sample_history.timestamp");
						String user = rs2.getString("sample_history.changed_by");
						
						//encode tag and null
						name = Util.encodeTagAndNull(name);
						remark = Util.encodeTagAndNull(remark);
						user = Util.encodeTagAndNull(user);
							
						this.chemical_name = URLEncoder.encode(name, "UTF-8");
						
						elements.add(timestamp+"|"+user+"|"+remark);
					}
				}
				con.close();
				
				statuscode = 500;
				return true;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			statuscode = 3;
			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			statuscode = 3;
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			statuscode = 3;
			return false;
		}
		
		statuscode = 3;
		return false;
	}

	/**
	 * @return Returns the elements.
	 */
	public Vector getElements() {
		return elements;
	}
	/**
	 * @return Returns the chemical_name.
	 */
	public String getChemical_name() {
		return chemical_name;
	}
	/**
	 * @return Returns the sample_id.
	 */
	public String getSample_id() {
		return sample_id;
	}
	/**
	 * @param sample_id The sample_id to set.
	 */
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}
	/**
	 * @return Returns the statuscode.
	 */
	public int getStatuscode() {
		return statuscode;
	}
}