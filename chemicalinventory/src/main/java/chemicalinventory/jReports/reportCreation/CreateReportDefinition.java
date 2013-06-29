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
import chemicalinventory.context.Attributes;

import chemicalinventory.db.Database;
import chemicalinventory.utility.*;

/**
 * Create a new report definition in the database. The
 * definition has a list of general value fields, and 
 * a list of parameters, to be filled in by the user at time
 * of generation of the report. The parameters must
 * be the parameters defined in the report.
 * 
 * @author Dann Vestergaard
 *
 */
public class CreateReportDefinition {

	public CreateReportDefinition() {
		super();
	}
	
	private String report_name = "";
	private String display_name = "";
	private String description = "";
	private String parameter1_name = "";
	private String parameter2_name = "";
	private String parameter3_name = "";
	private int report_id = 0;
	
	/**
	 * Create a definition of a report in the database.
	 * 
	 * @return status of the operation.
	 */
	public int createReportDef()
	{
		/*
		 * Validate input
		 */
		int status = validateInput(); 
		if(status != Return_codes.SUCCESS)
			return status;
		
		/*
		 * Register the values entered
		 */
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			con.setAutoCommit(false);
						
			if(con != null && stmt != null)
			{
				/*
				 * Insert into the report folder...
				 */
				String sql = "INSERT INTO "+Attributes.DATABASE+".reports" +
						" (report_name, display_name, description)" +
						" VALUES ('"+Util.double_q(this.report_name)+"', " +
						" '"+Util.double_q(this.display_name)+"'," +
					    " '"+Util.double_q(this.description)+"');";
				
				System.out.println("sql: "+sql);
												
				try {
					stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
					ResultSet key1 = stmt.getGeneratedKeys();
					if(key1.next())
					{
						this.report_id = key1.getInt(1);
					}
					else
					{
						con.rollback();
						con.close();
						return Return_codes.CREATION_FAILED;
					}
				} catch (RuntimeException e) {
					con.rollback();
					con.close();
					return Return_codes.CREATION_FAILED;
				}
				
				/*
				 * Perform insert into the parameter table.
				 * using the key created from the insertion
				 * into the reports tabel.
				 */
				if(Util.isValueEmpty(this.parameter1_name) ||
				   Util.isValueEmpty(this.parameter2_name) ||
				   Util.isValueEmpty(this.parameter3_name))
				{
					sql = "";
					stmt.clearBatch();
					if(Util.isValueEmpty(this.parameter1_name))
					{
						sql = "INSERT INTO "+Attributes.DATABASE+".report_parameters" +
						" (report_id, parameter_name)" +
						" VALUES ("+this.report_id+", " +
						" '"+Util.double_q(this.parameter1_name)+"');";
						
						stmt.addBatch(sql);
					}
					
					if(Util.isValueEmpty(this.parameter2_name))
					{
						sql = "INSERT INTO "+Attributes.DATABASE+".report_parameters" +
						" (report_id, parameter_name)" +
						" VALUES ("+this.report_id+", " +
						" '"+Util.double_q(this.parameter2_name)+"');";
						
						stmt.addBatch(sql);
					}
					
					if(Util.isValueEmpty(this.parameter3_name))
					{
						sql = "INSERT INTO "+Attributes.DATABASE+".report_parameters" +
						" (report_id, parameter_name)" +
						" VALUES ("+this.report_id+", " +
						" '"+Util.double_q(this.parameter3_name)+"');";
						
						System.out.println("sql2: "+sql);
						
						stmt.addBatch(sql);
					}
					
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
				}
				con.commit();
				con.close();
				return Return_codes.SUCCESS;
			}
			else
				return Return_codes.CONNECTION_ERROR;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Return_codes.GENERAL_ERROR;
	}
	
	/**
	 * Validate user input
	 * @return
	 */
	private int validateInput()
	{
		if(!Util.isValueEmpty(this.report_name))
			return Return_codes.MISSING_NAME;
		if(!Util.isValueEmpty(this.display_name))
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
	 * @return Returns the parameter1_name.
	 */
	public String getParameter1_name() {
		return this.parameter1_name;
	}
	/**
	 * @param parameter1_name The parameter1_name to set.
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
	 * @param parameter2_name The parameter2_name to set.
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
	 * @param parameter3_name The parameter3_name to set.
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
}