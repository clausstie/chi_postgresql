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
package chemicalinventory.analysis;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import chemicalinventory.db.Database;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class AnalysisHistory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -73490813168853992L;
	private Vector elements = new Vector();
	private String analysis_name = "";
	private String analysis_id = "";
	
	/**
	 * Here the history data for a single analysis is selected from the db.
	 * The data is stored into a list to be run through on the jsp page for display.
	 * @param id
	 * @return true false of the operation status.
	 */
	public boolean analysis_history(String id)
	{
		try{
			//Connection from the pool
				Connection con = Database.getDBConnection();;
				if(con != null)  
				{	
					Statement stmt = con.createStatement();
					
					String query = "SELECT analysis_history.remark, analysis_history.analysis_version," +
							" analysis_history.analysis_fields, analysis_history.changed_date," +
							" analysis_history.changed_by, analysis_history.timestamp, analysis.analysis_name" +
							" FROM analysis_history, analysis" +
							" WHERE analysis.analysis_id = analysis_history.analysis_id" +
							" AND analysis.version = analysis_history.analysis_version" +
							" AND analysis_history.analysis_id = "+ id +
							" ORDER BY analysis_history.analysis_version, analysis_history.timestamp;";
					
					ResultSet rs2 = stmt.executeQuery(query);
					
					elements.clear();
					
					while(rs2.next())
					{
						//find and encode results
						String name = rs2.getString("analysis.analysis_name");
						String remark = rs2.getString("analysis_history.remark");
						String version = rs2.getString("analysis_history.analysis_version");
						String timestamp = rs2.getString("analysis_history.timestamp");
						String user = rs2.getString("analysis_history.changed_by");
						
						//encode tag and null
						name = Util.encodeTagAndNull(name);
						remark = Util.encodeTagAndNull(remark);
						user = Util.encodeTagAndNull(user);
							
						this.analysis_name = name;
						
						elements.add(timestamp+"|"+user+"|"+version+"|"+remark);
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

	/**
	 * @return Returns the elements.
	 */
	public Vector getElements() {
		return elements;
	}
	/**
	 * @return Returns the analysis_name.
	 */
	public String getAnalysis_name() {
		return analysis_name;
	}
	/**
	 * @return Returns the analysis_id.
	 */
	public String getAnalysis_id() {
		return analysis_id;
	}
	/**
	 * @param analysis_id The analysis_id to set.
	 */
	public void setAnalysis_id(String analysis_id) {
		this.analysis_id = analysis_id;
	}
}