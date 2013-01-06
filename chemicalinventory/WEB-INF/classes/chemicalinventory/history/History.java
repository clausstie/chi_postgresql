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
package chemicalinventory.history;

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
public class History implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2316605769210732346L;
	/*
	 * tables allowed for operations in this class
	 */
	public final static String CONTAINER_TABLE 	= "CONTAINER";
	public final static String SUPPLIER_TABLE 	= "SUPPLIER";
	public final static String USER_TABLE 		= "USER";
	public final static String GROUP_TABLE 		= "GROUP";
	public final static String UNIT_TABLE 		= "UNIT";
	public final static String COMPOUND_TABLE 	= "COMPOUND";
	public final static String LOCATION_TABLE 	= "LOCATION";
	public final static String BATCH_TABLE 		= "BATCH";
	public final static String SEARCH_COMPOUND	 = "SEARCH_COMPOUND";

	/*
	 * Statements to insert
	 */
	public final static String CREATE_CONTAINER = "CONTAINER CREATED";
	public final static String MOVE_CONTAINER 	= "CONTAINER MOVED";
	public final static String CREATE_COMPOUND  = "COMPOUND CREATED";
	public final static String DELETE_COMPOUND  = "COMPOUND DELETED";
	public final static String DISPLAY_COMPOUND = "DISPLAY COMPOUND";
	public final static String CHECK_IN 		= "CONTAINER CHECKED IN";
	public final static String CHECK_OUT 		= "CONTAINER CHECKED OUT";
	public final static String TRANSFER_BY 		= "CONTAINER TRANSFERRED BY: ";
	public final static String TRANSFER_TO 		= "CONTAINER TRANSFERRED TO: ";
	public final static String UPDATE 			= "RECORD UPDATED";
	public final static String MODIFY 			= "RECORD MODIFIED";
	public final static String DELETE 			= "RECORD DELETED";
	public final static String REMOVE 			= "RECORD REMOVED";

	public final static String CREATE_BATCH 	= "BATCH CREATED";
	public final static String MODIFY_BATCH 	= "BATCH MODIFIED";
	public final static String LOCK_BATCH	 	= "BATCH LOCKED";
	public final static String UNLOCK_BATCH	 	= "BATCH UN-LOCKED";
	public final static String SEARCH_PERFORMED = "SEARCH PERFORMED";
	public final static String SEARCH_QUICK 	= "SEARCH QUICK";
	public final static String SEARCH_REPEATED 	= "SEARCH REPEATED";	

	private Vector table_list = null;

	private String container_id = "";
	private String chemical_name = "";
	private String batch_id = "";
	private int status = 0;
	private String user_name = "";
	private String type = "";
	private String user_id = "";

	private String id = "";

	/**
	 * Add the tables to a list.
	 */
	private Vector fill_table_list()
	{
		Vector list = new Vector();

		list.add(CONTAINER_TABLE);
		list.add(SUPPLIER_TABLE);
		list.add(USER_TABLE);
		list.add(GROUP_TABLE);
		list.add(UNIT_TABLE);
		list.add(COMPOUND_TABLE);
		list.add(LOCATION_TABLE);
		list.add(BATCH_TABLE);
		list.add(SEARCH_COMPOUND);

		return list;
	}

	/**
	 * Insert data into the history table.
	 * 
	 * @param table Must be part of the predefined table values
	 * @param text - reason for the change
	 * @param user - user perfoming the change
	 * @param old_value - value before the change
	 * @param new_value - value after the change
	 * @param unit - unit
	 * 
	 * @return true/false
	 */
	public boolean insertHistory(String table, int table_id, String text_id, String text, String user, String unit, String old_value, String new_value)
	{
		table_list = fill_table_list();

		/*
		 * Check to see if the information we are entering is beeing entered into a valid table name
		 * else abort.
		 */
		if(!table_list.contains(table))
		{
			return false;
		}

		try {
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

					String sql = "INSERT INTO `history`  (`table`, `table_id`, `text_id`, `text`, `changed_by`," +
					" `changed_date`, `unit`, `new_value`, `old_value`)" +
					" VALUES ('"+table+"', "+table_id+", '"+text_id+"', '"+text+"', '"+user+"'," +
					" '"+Util.getDate()+"', '"+unit+"', '"+new_value+"', '"+old_value+"')";

					try{
						stmt.executeUpdate(sql);
						con.commit();
						return true;
					}
					catch (SQLException e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return false;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}


	/**
	 * Create the string to perform the update.
	 * In this version the change details is allways set to '--'
	 * @param table
	 * @param table_id
	 * @param text_id
	 * @param text
	 * @param user
	 * @param unit
	 * @param old_value
	 * @param new_value
	 * @return The sql string to perform the insert of history data
	 */
	public String insertHistory_string(String table, int table_id, String text_id, String text, String user, String unit, String old_value, String new_value)
	{
		/*
		 * Check to see if the information we are entering is beeing entered into a valid table name
		 * else abort.
		 */
		table_list = fill_table_list();

		if(!table_list.contains(table))
		{
			return null;
		}

		//create the sql string.
		String sql = "INSERT INTO history (history.table, history.table_id, history.text_id, history.text," +
		" history.changed_by, history.changed_date, history.unit, history.new_value," +
		" history.old_value, history.change_details)" +
		" VALUES ('"+table+"', "+table_id+", '"+text_id+"', '"+text+"', '"+user+"', '"+Util.getDate()+"'," +
		" '"+unit+"', '"+new_value+"', '"+old_value+"', '--')";

		return sql;
	}

	/**
	 * Create insert statement for the history table, all coloums has to be filled!!
	 * @param table
	 * @param table_id
	 * @param text_id
	 * @param text
	 * @param user
	 * @param unit
	 * @param old_value
	 * @param new_value
	 * @param change_details
	 * @param Structure
	 * @return The insert sql statement, or NULL if error.
	 */
	public String insertHistory_stringComplete(String table, int table_id, String text_id, String text, String user, String unit, String old_value, String new_value, String change_details, String Structure)
	{		
		//create the sql string.
		String sql = "INSERT INTO history (history.table, history.table_id, history.text_id, history.text," +
		" history.changed_by, history.changed_date, history.unit, history.new_value," +
		" history.old_value, history.change_details, history.structure)" +
		" VALUES ('"+table+"', "+table_id+", '"+text_id+"', '"+text+"', '"+user+"', '"+Util.getDate()+"'," +
		" '"+unit+"', '"+new_value+"', '"+old_value+"', '"+change_details+"', '"+Structure+"')";

		return sql;
	}
	/**
	 * Insert history element in the table, with change details
	 * 
	 * In this version the unit, new value, and old value
	 * is allways set to '--'.
	 * 
	 * @param table
	 * @param table_id
	 * @param text_id
	 * @param text
	 * @param user
	 * @param change_details
	 * @return
	 */
	public String insertHistory_string(String table, int table_id, String text_id, String text, String user, String change_details)
	{
		/*
		 * Check to see if the information we are entering is beeing entered into a valid table name
		 * else abort.
		 */
		table_list = fill_table_list();

		if(!table_list.contains(table))
		{
			return null;
		}

		//create the sql string.
		String sql = "INSERT INTO history (history.table, history.table_id, history.text_id, history.text," +
		" history.changed_by, history.changed_date, history.unit, history.new_value," +
		" history.old_value, history.change_details)" +
		" VALUES ('"+table+"', "+table_id+", '"+text_id+"', '"+text+"', '"+user+"', '"+Util.getDate()+"'" +
		", '--', '--', '--', '"+change_details+"');";

		return sql;
	}

	public boolean getSearchHistory() {

		try {
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

					if(this.user_id != null && !this.user_id.equals("0") && !this.user_id.equals("")) {

						String sql = "SELECT id, user_name FROM user WHERE id ='"+this.user_id+"'";

						ResultSet rs = stmt.executeQuery(sql);

						while(rs.next())
						{
							user_name = rs.getString("user_name");
						}
						rs.close();
					}

					String sql = "SELECT history.id, history.table, history.table_id, history.text_id," +
					" history.change_details, history.text, history.changed_by, " +
					" history.structure, history.timestamp" +
					" FROM history" +
					" WHERE history.table = '"+History.SEARCH_COMPOUND+"'";

					if(user_id != null && !user_id.equals("0") && !user_id.equals("")) {

						sql += " AND history.table_id = '"+user_id+"'";
					}

					if(type != null && !type.equals("")) {

						sql += " AND history.text_id = '"+this.type+"'";
					}

					sql += " ORDER BY history.changed_by, history.timestamp;";

					java.sql.ResultSet rs = stmt.executeQuery(sql);

					table_list = new Vector();

					while(rs.next())
					{
						this.user_name =  Util.encodeNullValue(this.user_name).toUpperCase();
						String change_details = Util.encodeNullValue(rs.getString("history.change_details"));
						String text = Util.encodeNullValue(rs.getString("history.text"));
						String text_id = Util.encodeNullValue(rs.getString("history.text_id"));
						String timestamp = Util.encodeNullValue(rs.getString("history.timestamp"));
						String changed_by = Util.encodeNullValue(rs.getString("history.changed_by"));
						String h_id = rs.getString("history.id");
						String table_id = rs.getString("history.table_id");
						String table = Util.encodeNullValue(rs.getString("history.table"));
						String structure = Util.encodeNullValue(rs.getString("history.structure"));

						table_list.add(new HistoryLine(h_id, table, table_id, text_id, change_details.replace("|", "#"), text, changed_by, timestamp, "", "", "", structure));
					}
				}
				con.close();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;

	}

	/**
	 * Retrieve history data for a container from the database.
	 * Here is created a vector holding HistoryLine objects with
	 * individual hisotry lines. Set the status to 1 if an invalid id entered.
	 * @param id container id.
	 * @return true/False
	 */
	public boolean getContainerHistory(String id)
	{		
		/*
		 * check the received id.
		 */
		if(Util.isValidInt(id))
		{
			/*
			 * Check if the id is an existing container or in the history table
			 */
			if(Util.isContainerId_history(id))
			{
				try {
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
							this.chemical_name = URLEncoder.encode(Util.getChemicalName2(id), "UTF-8");

							Statement stmt = con.createStatement();

							String sql = "SELECT history.id, history.table, history.table_id, history.text_id," +
							" history.change_details, history.text, history.changed_by, history.unit," +
							" history.new_value, history.old_value, history.timestamp" +
							" FROM history" +
							" WHERE history.table = '"+History.CONTAINER_TABLE+"'" +
							" AND history.table_id = "+id+
							" ORDER BY history.timestamp;";

							java.sql.ResultSet rs = stmt.executeQuery(sql);

							table_list = new Vector();

							while(rs.next())
							{
								String change_details = Util.encodeNullValue(rs.getString("history.change_details"));
								String text = Util.encodeNullValue(rs.getString("history.text"));
								String unit = Util.encodeNullValue(rs.getString("history.unit"));
								String old_v = Util.encodeNullValue(rs.getString("history.old_value"));
								String new_v = Util.encodeNullValue(rs.getString("history.new_value"));
								String timestamp = Util.encodeNullValue(rs.getString("history.timestamp"));
								String user = Util.encodeNullValue(rs.getString("history.changed_by"));
								String h_id = rs.getString("history.id");	           

								table_list.add(new HistoryLine(h_id, History.CONTAINER_TABLE, id, chemical_name, change_details, text, user, timestamp, unit, old_v, new_v, ""));
							}
						}
						con.close();
						return true;
					}

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				return false;
			}
			else
			{
				status = 2;
				return false;
			}
		}
		else
		{
			status = 1;
			return false;
		}
	}

	/**
	 * Create the list of history lines for a specific compound
	 * @param id the compound id.
	 * @return true false of the operation.
	 */
	public boolean getCompoundHistory(String id)
	{		
		/*
		 * check the received id.
		 */
		if(Util.isValidInt(id))
		{
			try {
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

						String sql = "SELECT history.id, history.table, history.table_id, history.text_id," +
						" history.change_details, history.text, history.changed_by, history.timestamp" +
						" FROM history" +
						" WHERE history.table = '"+History.COMPOUND_TABLE+"'" +
						" AND history.table_id = "+id+
						" ORDER BY history.timestamp;";

						java.sql.ResultSet rs = stmt.executeQuery(sql);

						table_list = new Vector();

						while(rs.next())
						{
							this.chemical_name = Util.encodeNullValue(rs.getString("history.text_id"));
							String change_details = Util.encodeNullValue(rs.getString("history.change_details"));
							String text = Util.encodeNullValue(rs.getString("history.text"));
							String timestamp = Util.encodeNullValue(rs.getString("history.timestamp"));
							String user = Util.encodeNullValue(rs.getString("history.changed_by"));
							String h_id = rs.getString("history.id");	           

							table_list.add(new HistoryLine(h_id, History.COMPOUND_TABLE, id, chemical_name, change_details, text, user, timestamp, null, null, null, ""));
						}
					}
					con.close();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return false;
		}
		else
		{
			status = 1;
			return false;
		}
	}

	/**
	 * Get a history lise for batch history, taking the batch id as parameter..
	 * @param id
	 * @return
	 */
	public boolean getBatchHistory(String id)
	{		
		/*
		 * check the received id.
		 */
		if(Util.isValidInt(id))
		{
			/*
			 * Check if the id is an existing batch or in the history table
			 */
			if(Util.isBatchId_history(id))
			{
				try {
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

							String sql = "SELECT history.id, history.table, history.table_id, history.text_id," +
							" history.change_details, history.text, history.changed_by, history.unit," +
							" history.new_value, history.old_value, history.timestamp" +
							" FROM history" +
							" WHERE history.table = '"+History.BATCH_TABLE+"'" +
							" AND history.table_id = "+id+
							" ORDER BY history.timestamp;";

							java.sql.ResultSet rs = stmt.executeQuery(sql);

							table_list = new Vector();

							while(rs.next())
							{
								this.chemical_name = Util.encodeNullValue(rs.getString("history.text_id"));
								this.chemical_name = URLEncoder.encode(this.chemical_name, "UTF-8");
								String change_details = Util.encodeNullValue(rs.getString("history.change_details"));
								String text = Util.encodeNullValue(rs.getString("history.text"));
								String timestamp = Util.encodeNullValue(rs.getString("history.timestamp"));
								String user = Util.encodeNullValue(rs.getString("history.changed_by"));
								String h_id = rs.getString("history.id");	           

								table_list.add(new HistoryLine(h_id, History.BATCH_TABLE, id, chemical_name, change_details, text, user, timestamp, null, null, null, ""));
								//table_list.add(new HistoryLine(h_id, History.CONTAINER_TABLE, id, chemical_name, change_details, text, user, timestamp, unit, old_v, new_v));
							}
						}
						con.close();
						return true;
					}

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				return false;
			}
			else
			{
				status = 2;
				return false;
			}
		}
		else
		{
			status = 1;
			return false;
		}
	}

	/**
	 * Get the sql saved in a search history.
	 * @param search_id
	 * @return
	 */
	public String getSQLFromSearchHistory(String search_id)
	{		
		/*
		 * check the received id.
		 */
		if(Util.isValidInt(search_id))
		{

			String search_sql = null;

			try {
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

						String sql = "SELECT text FROM history where id = "+search_id+";";

						java.sql.ResultSet rs = stmt.executeQuery(sql);


						if(rs.next())
						{
							search_sql = rs.getString("text");

							con.close();
							return search_sql;
						}
					}
					con.close();
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return null;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Get the molfile saved in a search history line.
	 *
	 * @param search_id
	 * @return
	 */
	public String getMolFileFromSearchHistory(String search_id)
	{		
		/*
		 * check the received id.
		 */
		if(Util.isValidInt(search_id))
		{

			String search_mol = "";

			try {
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

						String sql = "SELECT structure FROM history where id = "+search_id+";";

						java.sql.ResultSet rs = stmt.executeQuery(sql);


						if(rs.next())
						{
							search_mol = rs.getString("structure");

							con.close();
							return search_mol;
						}
					}
					con.close();
					return "";
				}

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

			return "";
		}
		else
		{
			return "";
		}
	}

	/**
	 * @return Returns the container_id.
	 */
	public String getContainer_id() {
		return container_id;
	}
	/**
	 * @param container_id The container_id to set.
	 */
	public void setContainer_id(String container_id) {
		this.container_id = container_id;
	}
	/**
	 * @return Returns the table_list.
	 */
	public Vector getTable_list() {
		return table_list;
	}
	/**
	 * @return Returns the chemical_name.
	 */
	public String getChemical_name() {
		return chemical_name;
	}
	/**
	 * @param chemical_name The chemical_name to set.
	 */
	public void setChemical_name(String chemical_name) {
		this.chemical_name = chemical_name;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the batch_id.
	 */
	public String getBatch_id() {
		return batch_id;
	}
	/**
	 * @param batch_id The batch_id to set.
	 */
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}