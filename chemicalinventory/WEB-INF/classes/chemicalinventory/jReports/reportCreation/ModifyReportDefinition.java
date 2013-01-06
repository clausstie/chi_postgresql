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

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

/**
 * Perform modification of an existing report definition
 * stored in the database.
 * 
 * General values can be modified and parameters can be
 * added, deletet or modified.
 * 
 * 
 * @author Dann Vestergaard
 *
 */
public class ModifyReportDefinition {
	
	public ModifyReportDefinition() {
		super();
	}
	
	private String report_name = "";
	private String display_name = "";
	private String description = "";
	private String parameter1_name = "";
	private String parameter2_name = "";
	private String parameter3_name = "";
	private String parameter1_name_o = "";
	private String parameter2_name_o = "";
	private String parameter3_name_o = "";
	
	private int report_id = 0;
	
	/**
	 * Create a definition of a report in the database.
	 * 
	 * @return status of the operation.
	 */
	public int modifyReportDef() {
		/*
		 * Validate input
		 */
		int status = validateInput();
		if (status != Return_codes.SUCCESS)
			return status;
		
		/*
		 * Register the values entered
		 */
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			con.setAutoCommit(false);
			stmt.clearBatch();
			
			if (con != null && stmt != null) {
				/*
				 * Update into the report folder values.
				 */
				String sql = "UPDATE " + Attributes.DATABASE + ".reports"
				+ " SET report_name = '"
				+ Util.double_q(this.report_name) + "',"
				+ " display_name = '"
				+ Util.double_q(this.display_name) + "',"
				+ " description = '" + Util.double_q(this.description)
				+ "'" + " WHERE report_id = " + this.report_id + ";";
				
				stmt.addBatch(sql);
				
				/*
				 * Handle parameter values
				 */
				handleParameterValues(this.report_id, this.parameter1_name, this.parameter1_name_o, stmt);
				handleParameterValues(this.report_id, this.parameter2_name, this.parameter2_name_o, stmt);
				handleParameterValues(this.report_id, this.parameter3_name, this.parameter3_name_o, stmt);
				
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
	
	/**
	 * Update, delete or insert parameter values.
	 * 
	 * @param key
	 * @param value
	 * @param value_o
	 */
	private void handleParameterValues(int key, String value, String value_o, Statement stmt) { 
		try {
			if (key > 0) {
				String sql = "";
				
				if (!value.equalsIgnoreCase("") && value_o.equalsIgnoreCase("")) {
					/*
					 * No value priviously registered now register this one. perform
					 * insert
					 */
					sql = "INSERT INTO " + Attributes.DATABASE
					+ ".report_parameters" + " (report_id, parameter_name)"
					+ " VALUES (" + key + ", " + " '"
					+ Util.double_q(value) + "');";
					
					stmt.addBatch(sql);
					
				} else if (!value.equalsIgnoreCase("") && !value_o.equalsIgnoreCase("")) {
					if (!value.equals(value_o)) {
						/*
						 * The two values where NOT equal perform update
						 */
						sql = "UPDATE " + Attributes.DATABASE
						+ ".report_parameters" + " SET parameter_name = '"
						+ Util.double_q(value) + "'"
						+ " WHERE report_id = " + key + ""
						+ " AND parameter_name = '" + value_o+ "';";
						
						stmt.addBatch(sql);
					}
				} else if (value.equalsIgnoreCase("") && !value_o.equalsIgnoreCase("")) {
					/*
					 * Value deleted now perform deletion
					 */
					sql = "DELETE FROM report_parameters" +
					" WHERE report_id = "+key+
					" AND parameter_name = '"+value_o+"';";
					
					stmt.addBatch(sql);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Validate user input
	 * 
	 * @return
	 */
	private int validateInput() {
		if (!Util.isValueEmpty(this.report_name))
			return Return_codes.MISSING_NAME;
		if (!Util.isValueEmpty(this.display_name))
			return Return_codes.MISSING_TITLE;
		
		return Return_codes.SUCCESS;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description
	 *            The description to set.
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
	 * @param display_name
	 *            The display_name to set.
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	
	/**
	 * @return Returns the parameter1_name.
	 */
	public String getParameter1_name() {
		return this.parameter1_name;
	}
	
	/**
	 * @param parameter1_name
	 *            The parameter1_name to set.
	 */
	public void setParameter1_name(String parameter1_name) {
		this.parameter1_name = parameter1_name;
	}
	
	/**
	 * @return Returns the parameter2_name.
	 */
	public String getParameter2_name() {
		return this.parameter2_name;
	}
	
	/**
	 * @param parameter2_name
	 *            The parameter2_name to set.
	 */
	public void setParameter2_name(String parameter2_name) {
		this.parameter2_name = parameter2_name;
	}
	
	/**
	 * @return Returns the parameter3_name.
	 */
	public String getParameter3_name() {
		return this.parameter3_name;
	}
	
	/**
	 * @param parameter3_name
	 *            The parameter3_name to set.
	 */
	public void setParameter3_name(String parameter3_name) {
		this.parameter3_name = parameter3_name;
	}
	
	/**
	 * @return Returns the report_name.
	 */
	public String getReport_name() {
		return report_name;
	}
	
	/**
	 * @param report_name
	 *            The report_name to set.
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
	 * @param parameter1_name_o
	 *            The parameter1_name_o to set.
	 */
	public void setParameter1_name_o(String parameter1_name_o) {
		this.parameter1_name_o = parameter1_name_o;
	}
	
	/**
	 * @param parameter2_name_o
	 *            The parameter2_name_o to set.
	 */
	public void setParameter2_name_o(String parameter2_name_o) {
		this.parameter2_name_o = parameter2_name_o;
	}
	
	/**
	 * @param parameter3_name_o
	 *            The parameter3_name_o to set.
	 */
	public void setParameter3_name_o(String parameter3_name_o) {
		this.parameter3_name_o = parameter3_name_o;
	}
	
	/**
	 * @param report_id
	 *            The report_id to set.
	 */
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}
}