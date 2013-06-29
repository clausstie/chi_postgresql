/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kalles�e 2004-20075.
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
package chemicalinventory.dbupdate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import chemicalinventory.db.Database;
import chemicalinventory.history.History;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class DbUpdate {
	
	History history = new History();
	
	/**
	 * Update the container table with statment in the history table for each container about the
	 * date they were created.
	 * This is primarily for the DFU implementation, where the history functionality was
	 * not part of the initial development.
	 */
	public void updateContainerHistory()
	{	
		try {
			//Connection from the pool
			
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				stmt.clearBatch();
				
				//find all data on containers
				String sql = "SELECT container.id, compound.chemical_name, container.initial_quantity, container.unit, container.register_date, container.register_by" +
				" FROM container" +
				" LEFT JOIN compound" +
				" ON (compound.id = container.compound_id);";
				
				ResultSet rs = stmt.executeQuery(sql);;
				
				while(rs.next())
				{
					int h_id = rs.getInt("container.id");
					if(!isRecordCreatedAllReady(h_id, History.CONTAINER_TABLE,
							History.CREATE_CONTAINER, con))
					{							
						/*
						 * Make sure that the record does not exist in the db
						 * as create item all ready, if it does, the skip
						 */
						String h_name = Util.encodeNullValue(rs.getString("compound.chemical_name"));
						h_name = Util.double_q(h_name);
						
						String h_quantity = Util.encodeNullValue(rs.getString("container.initial_quantity"));
						String h_unit = Util.encodeNullValue(rs.getString("container.unit"));
						String h_date = Util.encodeNullValue(rs.getString("container.register_date"));
						String h_by = Util.encodeNullValue(rs.getString("container.register_by"));
						
						//create the insert statement in the history table....
						stmt.addBatch("INSERT INTO `history` (`table`, `table_id`, `text_id`," +
								" `text`, `changed_by`, `changed_date`, `unit`, `new_value`," +
								" `old_value`, `change_details`, timestamp)" +
								" VALUES ('"+History.CONTAINER_TABLE+"', "+h_id+", '"+h_name+"'," +
								" '"+History.CREATE_CONTAINER+"', '"+h_by+"', '"+h_date+"', '"+h_unit+"'," +
								" '"+h_quantity+"', '--', '--', '"+h_date+" 00:00:00');");
					}
				}						
				
				try {
					stmt.executeBatch();								
				} catch (Exception e) {
					e.printStackTrace();
				}
				con.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Update the compound table with statment in the history table for each compound about the
	 * date they were created.
	 * This is primarily for the DFU implementation, where the history functionality was
	 * not part of the initial development.
	 */
	public void updateCompoundHistory()
	{	
		try {
			//Connection from the pool
			
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				stmt.clearBatch();
				
				String receipt = "SELECT compound.id, compound.chemical_name, compound.cas_number," +
				" compound.density, compound.register_by, compound.register_date, compound.remark," +
				" structures.cd_formula, structures.cd_molweight" +//, structures.cd_structure" +
				" FROM compound" +
				" LEFT JOIN structures" +
				" ON compound.cd_id = structures.cd_id";
				
				ResultSet rs = stmt.executeQuery(receipt);
				
				while(rs.next())
				{
					int compound_id = rs.getInt("compound.id");
					if(!isRecordCreatedAllReady(compound_id, History.COMPOUND_TABLE,
							History.CREATE_COMPOUND, con))
					{
						String name = Util.encodeNullValue(rs.getString("compound.chemical_name"));
						name = Util.double_q(name);
						String reg_date = rs.getString("compound.register_date");
						String cas = Util.encodeNullValue(rs.getString("compound.cas_number"));
						String note = Util.encodeNullValue(rs.getString("compound.remark"));
						note = Util.double_q(note);
						String reg_by = rs.getString("compound.register_by");
						String dens = Util.encodeNullValue(rs.getString("compound.density"));
						String formula = Util.encodeNullValue(rs.getString("structures.cd_formula"));
						String mw = Util.encodeNullValue(rs.getString("structures.cd_molweight"));
						
						//Create the details about the compound
						String change_details = "--";
						
						change_details = "Chemical name; --; "+name+"|\n";
						change_details = change_details + "Cas No.; --; "+cas+"|\n";
						change_details = change_details + "Density; --; "+dens+"|\n";
						change_details = change_details + "Formula; --; "+formula+"|\n";
						change_details = change_details + "Mw; --; "+mw+"\n";
						change_details = change_details + "Remark; --; "+note+"\n";
						
						//Create the entire sql for inserting a line in the history table
						//this does not include the blob for the structure..
						stmt.addBatch("INSERT INTO `history`  (`table`, `table_id`, `text_id`, `text`, `changed_by`, `changed_date`, `unit`, `new_value`, `old_value`, `change_details`, timestamp)" +
								" VALUES ('"+History.COMPOUND_TABLE+"', "+compound_id+", '"+name+"', '"+History.CREATE_COMPOUND+"', '"+reg_by+"', '"+reg_date+"', '--', '--', '--', '"+change_details+"', '"+reg_date+" 00:00:00');");
					}
				}
				
				try {
					stmt.executeBatch();								
				} catch (Exception e) {
					e.printStackTrace();
				}
				con.close();
			}
		}//end try 
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Make sure that the create statement is not in the history
	 * table as created allready.
	 * 
	 * if the record is in the history table return true.
	 * 
	 * @param id
	 * @param table
	 * @param createText
	 * @param con
	 * @param stmt
	 * @return true if the record is previously in the history table as created.
	 */
	private boolean isRecordCreatedAllReady(int id, String table, String createText, Connection con)
	{
		String sql = "SELECT history.id FROM history" +
		" WHERE history.table ='"+table+"'" +
		" AND history.table_id ="+id+"" +
		" AND history.text ='"+createText+"';";
		
		try {
			Statement stmt2 = con.createStatement();
			ResultSet set = stmt2.executeQuery(sql);
			
			if(set.next())
			{
				set.close();
				return true;
			}
			else
			{
				set.close();
				return false;
			}
			
		} catch (SQLException e) {
			
			return false;
		}
	}
}