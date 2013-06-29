/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2003-2006.
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
 */
package chemicalinventory.jReports.reportCreation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import chemicalinventory.db.Database;
import chemicalinventory.utility.Return_codes;

/**
 * Delete the selected report definition from the
 * database.
 * 
 * @author Dann Vestergaard
 *
 */
public class DeleteReportDefinition {
	
	public DeleteReportDefinition() {
	}
	
	
	/**
	 * Create a definition of a report in the database.
	 * 
	 * @return status of the operation.
	 */
	public int deleteReportDef(int id) {
		
		/*
		 * Register the values entered
		 */
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			con.setAutoCommit(false);
			stmt.clearBatch();
			
			if (con != null && stmt != null) {
				String sql = "DELETE FROM report_parameters" +
				" WHERE report_id = "+id+";";

				stmt.addBatch(sql);
				
				sql = "DELETE FROM reports" +
				" WHERE report_id = "+id+";";
				
				stmt.addBatch(sql);

				try {
					stmt.executeBatch();
					con.commit();
					con.close();
					return Return_codes.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					con.rollback();
					con.close();
					return Return_codes.CREATION_FAILED;
				}
				
			} else
				return Return_codes.CONNECTION_ERROR;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Return_codes.GENERAL_ERROR;
	}
}