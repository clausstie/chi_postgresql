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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import chemicalinventory.db.Database;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

/**
 * Collection of methods used to display information
 * about one or all reportdefinitions stored in the database.
 * 
 * 
 * @author Dann Vestergaard
 *
 */
public class DisplayReportDefinition {

	public DisplayReportDefinition() {
		super();
	}
	
	private String report_name = "";
	private String display_name = "";
	private String description = "";
	private int report_id = 0;
	private Vector list = new Vector();
		
	/**
	 * Get the values stored for the report
	 * 
	 * @return status of the operation.
	 */
	public int displayReportDef(String id)
	{
		/*
		 * Validate input
		 */
		if(!Util.isValueEmpty(id))
			return Return_codes.MISSING_ID;
		
		
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			ResultSet set = Database.performQuery(stmt, con, "SELECT r.report_id, r.report_name, r.display_name, r.description, p.parameter_name" +
					" FROM reports r" +
					" LEFT JOIN report_parameters p on r.report_Id = p.report_id" +
					" AND p.report_id = r.report_id" +
					" WHERE r.report_name = '"+id+"';");
			
			if(set != null)
			{
				this.list.clear();
				set.beforeFirst();
				
				while(set.next())
				{
					if(set.isFirst())
					{
						this.report_id = set.getInt("r.report_id");
						this.report_name = set.getString("r.report_name");
						this.display_name = set.getString("r.display_name");
						this.description = set.getString("r.description");
					}
					
					if(Util.isValueEmpty(set.getString("p.parameter_name")))
						this.list.add(set.getString("p.parameter_name"));		
				}
				
				con.close();
				return Return_codes.SUCCESS;
			}
			else
			{
				con.close();
				return Return_codes.EMPTY_SEARCH;
			}
			
		} catch (SQLException e) {
			return Return_codes.GENERAL_ERROR;
		}
	}

	/**
	 * Create a list of all available reports in the system.
	 * @return list of reports.
	 */
	public Vector listReports()
	{
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			ResultSet set = Database.performQuery(stmt, con, "SELECT r.report_name, r.display_name" +
					" FROM reports r" +
					" ORDER BY r.display_name;");
			
			if(set != null)
			{
				this.list.clear();
				set.beforeFirst();
				
				while(set.next())
				{
					this.list.add(set.getString("r.report_name")+"|"+set.getString("r.display_name"));	
				}
				
				con.close();
				return this.list;
			}
			else
			{
				con.close();
				return null;
			}
			
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * Create a list of all the reports in the system, 
	 * this is put into a vector that is returned.
	 * @return
	 */
	public Vector listReports_idOnly()
	{
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			ResultSet set = Database.performQuery(stmt, con, "SELECT r.report_name" +
					" FROM reports r" +
					" ORDER BY r.display_name;");
			
			this.list.clear();
			
			if(set != null)
			{
				set.beforeFirst();
				
				while(set.next())
				{
					this.list.add(set.getString("r.report_name"));	
				}
				
				con.close();
				return this.list;
			}
			else
			{
				con.close();
				return null;
			}
			
		} catch (SQLException e) {
			return null;
		}
	}
		
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the display_name.
	 */
	public String getDisplay_name() {
		return display_name;
	}
	/**
	 * @param display_name The display_name to set.
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	/**
	 * @return Returns the report_name.
	 */
	public String getReport_name() {
		return report_name;
	}
	/**
	 * @param report_name The report_name to set.
	 */
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	/**
	 * @return Returns the report_id.
	 */
	public int getReport_id() {
		return report_id;
	}

	/**
	 * @return Returns the list.
	 */
	public Vector getList() {
		return list;
	}
}