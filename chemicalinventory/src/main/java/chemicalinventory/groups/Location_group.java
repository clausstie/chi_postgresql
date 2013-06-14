package chemicalinventory.groups;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

import chemicalinventory.beans.locationBean;
import chemicalinventory.db.Database;
import chemicalinventory.history.History;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

public class Location_group {
	
	
	private String[] groups = null;
	private int location_id = 0;
	private Vector all_groups = new Vector();
	private Vector deleted_groups = new Vector();
	public Vector added_groups = new Vector();
	private Vector added_history = new Vector();
	private boolean isRemove = false;
	
	User_group bean = new User_group();
	History history = new History();
	
	public Location_group()
	{
		
	}
	
	/** 
	 * 
	 * Method to find all groups + set the groups
	 * the location is a part of as checked.
	 * 
	 * The html is added to the html in the vector all_groups. 
	 * @param id ID of the location.
	 */
	public void find_location_groups_from_id(int id)
	{
		try{
			//Connection from the pool
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				Vector checked = new Vector();
				int i = 0;
				String tag = null;
				boolean wasThere = false;
				
				//get the groups that the location is a part of.
				String sql = "SELECT g.id, g.name"+
				" FROM user_groups g, user_group_location_link l, location loc"+
				" WHERE g.id = l.group_id"+
				" AND l.location_id = loc.id"+
				" AND l.location_id = "+id+";";
				
				ResultSet rs = stmt.executeQuery(sql);
				
				while (rs.next())
				{
					checked.addElement(rs.getString("g.id"));
					i++;
				}
				
				//Get all groups.
				String sql2 = "SELECT id, name FROM user_groups";
				
				ResultSet rs2 = stmt.executeQuery(sql2);
				
				/* Compare the all groups with the groups the location is a part of.
				 * If the location is part of a group, this shall be displayed as
				 * checked on the page on the html page.
				 **/
				while (rs2.next())
				{
					wasThere = false;
					for (i=0; i < checked.size(); i++)
					{
						if(rs2.getString("id").equals(checked.elementAt(i)))
						{
							wasThere = true;
							break;
						}
					}
					if(wasThere)
					{
						tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs2.getString("id")+"\" checked>"+rs2.getString("name");
					}
					else
					{
						tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs2.getString("id")+"\">"+rs2.getString("name");
					}
					
					all_groups.addElement(tag);
				}
			}
			con.close();
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Find and create checkboxes as readonly for selected 
	 * groups for this location (id)
	 * 
	 * @param id
	 */
	public void find_location_groups_readonly(int id)
	{
		try{
			//Connection from the pool
			
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				String tag = null;
				
				//get the groups that the location is a part of.
				String sql = "SELECT g.id, g.name"+
				" FROM user_groups g, user_group_location_link l, location loc"+
				" WHERE g.id = l.group_id"+
				" AND l.location_id = loc.id"+
				" AND l.location_id = "+id+";";
				
				ResultSet rs = stmt.executeQuery(sql);
				all_groups.clear();
				
				while (rs.next())
				{
					tag = "<input type=\"checkbox\" name=\"groups\" checked=\"checked\" disabled=\"disabled\">"+rs.getString("g.name");					
					all_groups.add(tag);
				}
			}
			con.close();
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Find the groups that the location is a part of. if the location
	 * has groups attached, enter these into the global list all_groups.
	 * @param location_id
	 */	
	public void find_location_groups_list(int location_id)
	{
		try{
			//Connection from the pool
			
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				//get the groups that the location is a part of.
				String sql = "SELECT g.id, g.name"+
				" FROM user_groups g, user_group_location_link l, location loc"+
				" WHERE g.id = l.group_id"+
				" AND l.location_id = loc.id"+
				" AND l.location_id = "+location_id+";";
				
				ResultSet rs = stmt.executeQuery(sql);
				all_groups.clear();
				
				while (rs.next())
				{
					all_groups.add(rs.getString("g.id"));
				}
			}
			con.close();
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Is the location in the location group link table.
	 * @param location_id
	 * @return Return true if and only if the location is in the link table.
	 */
	public boolean isLocationInGroup(int location_id)
	{
		try{
			//Connection from the pool
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				//get the groups that the location is a part of.
				String sql = "SELECT g.id"+
				" FROM user_groups g, user_group_location_link l, location loc"+
				" WHERE g.id = l.group_id"+
				" AND l.location_id = loc.id"+
				" AND l.location_id = "+location_id+";";
				
				ResultSet rs = stmt.executeQuery(sql);
				
				if(rs.next())
				{
					con.close();
					return true;
				}
				else
				{
					con.close();
					return false;
				}
			}
			
			return true;
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * Insert or delete the selected and deselected groups for the location.
	 * @param group, array of selected groups.
	 * @param id, id of the location at work.
	 * @return true if, and only if the update was succesfull.
	 */
	public int update_location_groups(String[] group, int id, String user)
	{
		if(id > 0)
		{
			/*
			 * Find out if the location is a level 2 location or not.
			 */
			locationBean bean = new locationBean();
			bean.getNameAndLevel(id);
			int level = Util.getIntValue(bean.getLevel());
			Vector level2_list = new Vector();
			Vector level1_list = new Vector();
			this.isRemove = false;
			
			if(level < 2)
			{
				/*
				 * The selected level is either 0 or 1, find 
				 * the level 2 sublocations for the selected id.
				 */
				if(level == 0)
				{
					/*
					 * If a level 0 get all level2 and level1 below this level in
					 * the hirachy.
					 */
					level2_list = bean.getLevel2FromLevel0(id);
					level1_list = bean.getLevel1FromLevel0(id);
				}
				else if (level == 1)
				{
					level2_list = bean.getLevel2FromLevel1(id);
					level1_list.add(String.valueOf(id));
				}
			}
			else if(level == 2)
			{
				level2_list.add(String.valueOf(id));
			}
			
			/*
			 * Create the list of selected groups.
			 */
			Vector newGroups = new Vector();		
			if(group != null && groups.length > 0)
			{
				for (int i = 0; i < group.length; i++) {
					newGroups.add((String) group[i]);				
				}
			}
			else
				newGroups = null;
			
			
			/*
			 * Now loop through the list, registering all the 
			 * groups that must be added or deleted from the group.
			 */
			try{
				Connection con = Database.getDBConnection();
				Statement stmt = con.createStatement();
				
				con.setAutoCommit(false);
				
				if(con != null)  
				{
					if(level == 0)
					{
						/*
						 * Register the selected location in the link table LEVEL 0!
						 */
						if(update_single_location_groups(newGroups, id, user, con, stmt) != Return_codes.SUCCESS)
						{
							con.rollback();
							con.close();
							
							return Return_codes.CREATION_FAILED; 
						}
					}
					
					/*
					 * Loop through the list of level 1 ids, registerering individually.
					 */
					for (Iterator iter = level1_list.iterator(); iter.hasNext();) {
						int level1_id = Util.getIntValue((String) iter.next());
						
						/*
						 * Register and deregister in the location_group_link table
						 */
						if(update_single_location_groups(newGroups, level1_id, user, con, stmt) != Return_codes.SUCCESS)
						{
							con.rollback();
							con.close();
							
							return Return_codes.CREATION_FAILED;
						}
					}
					
					/*
					 * Loop through the list of level 2 ids, registerering individually.
					 */
					for (Iterator iter = level2_list.iterator(); iter.hasNext();) {
						int level2_id = Util.getIntValue((String) iter.next());
						
						/*
						 * Register and deregister in the location_group_link table
						 */
						if(update_single_location_groups(newGroups, level2_id, user, con, stmt) != Return_codes.SUCCESS)
						{
							con.rollback();
							con.close();
							
							return Return_codes.CREATION_FAILED;
						}
					}
					
					/*
					 * If the level is 1 or 2, and some locations has been removed,
					 * check if any of the locations above is registerede, and if 
					 * yes remove the levels above.
					 */
					if(this.isRemove && (level == 1 || level == 2))
					{
						Vector list_above = null;
						String sql = "";
						
						if(level == 1)
						{
							list_above = bean.getLevelsAbove_level1(id);
						}
						else if(level == 2)
						{
							list_above = bean.getLevelsAbove_level2(id);
						}
						
						/*
						 * only part of the priviously connected groups deleted.
						 */
						if(this.deleted_groups != null)
						{
							for (Iterator iter = this.deleted_groups.iterator(); iter
							.hasNext();) {
								String group_id = (String) iter.next();
								
								if(list_above != null)
								{
									for (Iterator iter2 = list_above.iterator(); iter2
									.hasNext();) {
										String element = (String) iter2.next();
										sql = "DELETE FROM user_group_location_link WHERE location_id = "+element+" AND group_id = "+group_id+";"; 
										stmt.addBatch(sql);
									}
								}							
								
							}
						}
						else
						{
							/*
							 * All groups deleted.
							 */
							if(list_above != null)
							{
								for (Iterator iter = list_above.iterator(); iter
								.hasNext();) {
									String element = (String) iter.next();
									sql = "DELETE FROM user_group_location_link WHERE location_id = "+element+";"; 
									stmt.addBatch(sql);
								}
							}							
						}
					}					
					
					try {
						stmt.executeBatch();					
						con.commit();						
						con.close();
						return Return_codes.SUCCESS;
					} 
					catch (Exception e) {
						e.printStackTrace();
						con.rollback();
						con.close();
						return Return_codes.CREATION_FAILED;
					}
				}
				
				return Return_codes.CONNECTION_ERROR;
			}
			catch (Exception e) {
				e.printStackTrace();
				return Return_codes.CREATION_FAILED;
			}
		}
		else
			return Return_codes.MISSING_ID;
	}	
	
	/**
	 * Insert the location in the location_group_link table,
	 * inserting a row for earch selected group, removing
	 * a row for each de-selected group, and completely 
	 * removing the location from the table if the
	 * selected groups are emty.
	 * @param new2 Vector of the groups the location must be part of after update. 
	 * 	If null remove the location from the table.
	 * @param level_2_id id if the level_2 location
	 * @param conn Connection instance
	 * @param state Statement instance.
	 * @return the status of the operation.
	 */
	private int update_single_location_groups(Vector new2, 
			int level_2_id,
			String user,
			Connection conn,
			Statement state)
	{
		try{				
			Vector checked = new Vector();//member of current groups.
			String insertGroup= ""; //sql place holder
			
			//get the groups that the location is a part of.
			String sql = "SELECT g.id"+
			" FROM user_groups g, user_group_location_link l, location loc"+
			" WHERE g.id = l.group_id"+
			" AND l.location_id = loc.id"+
			" AND l.location_id = "+level_2_id+";";
			
			ResultSet rs = state.executeQuery(sql);
			
			while (rs.next())
			{
				checked.addElement(rs.getString("g.id"));
			}
			
			rs.close();
			
			/*insert or delete groups while some groups is picked.*/
			if(new2 != null && new2.size() > 0)
			{						
				/*insert the groups selcted, that are NOT selected before*/
				for (int n = 0; n < new2.size(); n++)
				{
					if(!checked.contains(new2.get(n)))
					{
						//Insert
						insertGroup = "INSERT INTO user_group_location_link (location_id, group_id) VALUES("+level_2_id+", "+new2.get(n)+")";
						state.addBatch(insertGroup);
						
						/*
						 * Handle the containers
						 */
						if(add_container_as_location_link(level_2_id, Util.getIntValue((String)new2.get(n)), new2, user, state, conn) != Return_codes.SUCCESS)
						{
							return Return_codes.CREATION_FAILED;
						}
					}
				}
				
				/*Delete groups that are deleted...*/
				for (int i = 0; i <checked.size(); i++)
				{
					if(!new2.contains(checked.get(i)))
					{
						//Delete
						insertGroup = "DELETE FROM user_group_location_link WHERE location_id = "+level_2_id+" AND group_id = "+checked.get(i);
						state.addBatch(insertGroup);
						this.isRemove = true;
						
						/*
						 * Handle the containers
						 */
						if(remove_container_at_location_link(level_2_id, new2, Util.getIntValue((String) checked.get(i)), user, state, conn) != Return_codes.SUCCESS)
						{
							return Return_codes.CREATION_FAILED;
						}
					}
				}
				
				return Return_codes.SUCCESS;
			}
			else if(new2 == null && checked.size() > 0)//else delete all currently checked groups..
			{
				sql = "DELETE FROM user_group_location_link WHERE location_id = "+level_2_id;
				state.addBatch(sql);
				/*
				 * Handle containers
				 */
				if(remove_all_container_as_location_link(level_2_id, new2, user, state, conn) != Return_codes.SUCCESS)
				{
					return Return_codes.CREATION_FAILED;
				}
				this.isRemove = true;
				this.deleted_groups = null;
				return Return_codes.SUCCESS;
			}
			else 
			{
				return Return_codes.SUCCESS;
			}
			
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.CREATION_FAILED;
		}
		
	}
	
	/**
	 * Get all the container id's stored at a specific location 2 id. 
	 * From the created list, subtract all the containers that is allready
	 * registered in the container_link table, leaving a list of 
	 * containers at a level 2 location, that is not registered in the container
	 * link table, with the group.
	 * @param level_2_id
	 * @param group_id
	 * @param new_groups
	 * @param user
	 * @param stmt_in
	 * @param conn
	 * @return
	 */	
	private int add_container_as_location_link(int level_2_id, 
			int group_id, 
			Vector new_groups,
			String user,
			Statement stmt_in,
			Connection conn)
	{  
		try{
			if(conn != null)
			{
				/*
				 * Get all containers at a level 2 location
				 */
				String sql = "SELECT c.id" +
				" FROM container c, location l" +
				" WHERE c.location_id = l.id" +
				" AND c.current_quantity > 0" +
				" AND c.empty = 'F'" +
				" AND c.location_id = "+level_2_id+
				" ORDER BY c.id;";
				
				ResultSet rs = stmt_in.executeQuery(sql);
				Vector vector = new Vector();		
				
				while(rs.next())
				{
					vector.add(rs.getString("c.id"));
				}
				
				if(vector == null || vector.size() < 1)
				{
					return Return_codes.SUCCESS;
				}
				else
				{
					/*
					 * Get all the containers in the link table
					 * from the selected level2 and the selected group id.
					 */
					rs.close();
					
					sql = "SELECT c.id" +
					" FROM container c, location l, user_group_container_link lnk, user_groups ug" +
					" WHERE c.location_id = l.id" +
					" AND c.id = lnk.container_id" +
					" AND ug.id = lnk.group_id" +
					" AND c.current_quantity > 0" +
					" AND c.empty = 'F'" +
					" AND c.location_id = "+level_2_id+
					" AND lnk.group_id = "+group_id+";";
					
					rs = null;
					rs = stmt_in.executeQuery(sql);
					Vector vector_all_ready_reg = new Vector();		
					
					while(rs.next())
					{
						vector_all_ready_reg.add(rs.getString("c.id"));
					}
					
					/*
					 * Run through the vector with all the containers removing 
					 * the ones allready registered.
					 */
					for (int i = 0; i<vector.size(); i++)
					{
						String element = (String) vector.get(i);
						
						if(vector_all_ready_reg.contains(element))
							vector.remove(i);
						else
						{
							/*
							 * Register the container in the link table, and
							 * update the history for the container.
							 */
							sql = "";
							sql = "INSERT INTO user_group_container_link (container_id, group_id) VALUES("+element+", "+group_id+");";
							stmt_in.addBatch(sql);
							
							/*
							 * Handle history 
							 */
							String chemical_name = Util.getChemicalName4(element, conn, stmt_in);
							if(!Util.isValueEmpty(chemical_name))
								return Return_codes.MISSING_NAME;
							else
								chemical_name = Util.double_q(chemical_name);
							
							if(handle_history_at_location_update(new_groups, element, user, chemical_name, true, conn, stmt_in) != Return_codes.SUCCESS)
							{
								return Return_codes.CREATION_FAILED;
							}
						}
					}
					
					return Return_codes.SUCCESS;
				}
			}
			
			return Return_codes.CONNECTION_ERROR;
			
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
	}
	
	/**
	 * Handle history for a container.
	 * @param new1
	 * @param container_id
	 * @param user
	 * @param chemical_name
	 * @param conn
	 * @param state_in
	 * @return
	 */
	private int handle_history_at_location_update(Vector new1,
			String container_id,
			String user,
			String chemical_name,
			boolean insert,
			Connection conn, Statement state_in)
	{
		if(this.added_history.contains(container_id))
			return Return_codes.SUCCESS;
				
		try {
			/*
			 * First get a list of all currently connected groups for the container
			 */			
			Vector current_group_list = new Vector();
			StringBuffer current_list = new StringBuffer();
			StringBuffer new_list = new StringBuffer();
			
			String sql = "SELECT group_id" +
					" FROM user_group_container_link" +
					" WHERE container_id = "+container_id+";";
			
			ResultSet set = state_in.executeQuery(sql);
			
			while(set.next())
			{

				if(insert == true)
					current_group_list.add(set.getString("group_id"));
				
				if(set.isFirst())
					current_list.append(set.getString("group_id"));
				else
					current_list.append(","+set.getString("group_id"));
			}
			
			String s_c_list = Util.encodeNullValue(current_list.toString());
			
			
			/*
			 * Make sure that the container is only part of the groups
			 * selected for the location, and not member of any previously
			 * selected groups.
			 * 
			 * This is only handled for insert of a location.
			 */
			if(insert == true)
			{
				for (Iterator iter = current_group_list.iterator(); iter.hasNext();) {
					String element = (String) iter.next();
					
					if(!new1.contains(element))
					{
						state_in.addBatch("DELETE FROM user_group_container_link WHERE container_id = "+container_id+" AND group_id = "+element+";");
					}
				}
			}
			
			/*
			 * Create a comma separeted list of the new groups
			 */
			if(new1 != null)
			{
				for (int i=0; i<new1.size(); i++) {
					if(i == 0)
						new_list.append((String) new1.get(i));
					else
						new_list.append(","+(String) new1.get(i));
				}
			}
			
			String s_n_list = Util.encodeNullValue(new_list.toString());
			
			/*
			 * Create the update line for the container.
			 */
			String change_details = "Groups; "+Util.double_q(bean.find_groups_from_id_list(s_c_list))+"; "+Util.double_q(bean.find_groups_from_id_list(s_n_list));
			
			/*
			 * Add the history element to the statement object.
			 */
			state_in.addBatch(history.insertHistory_string(History.CONTAINER_TABLE,
					Integer.parseInt(container_id),
					chemical_name,
					History.MODIFY,
					user.toUpperCase(),
					change_details));
						
			/*
			 * Add the container id to the list of containers where history has been handled.
			 */
			added_history.add(container_id);
			
			return Return_codes.SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			return Return_codes.CREATION_FAILED;
		}
	}
	
	/**
	 * Remove a container from the container link table, if it is registered there.
	 * @param level_2_id
	 * @param new_groups
	 * @param group_id
	 * @param user
	 * @param stmt_in
	 * @param conn
	 * @return
	 */
	private int remove_container_at_location_link(int level_2_id, Vector new_groups, 
			int group_id,
			String user,
			Statement stmt_in, Connection conn)
	{  
		try{
			if(conn != null)  
			{					
				/*
				 * Get all the containers in the link table
				 * from the selected level2 and the selected group id.
				 */
				String sql = "SELECT c.id" +
				" FROM container c, location l, user_group_container_link lnk, user_groups ug" +
				" WHERE c.location_id = l.id" +
				" AND c.id = lnk.container_id" +
				" AND ug.id = lnk.group_id" +
				" AND c.current_quantity > 0" +
				" AND c.empty = 'F'" +
				" AND c.location_id = "+level_2_id+
				" AND lnk.group_id = "+group_id+";";
				
				ResultSet rs = stmt_in.executeQuery(sql);
				Vector vector_all_ready_reg = new Vector();		
				
				while(rs.next())
				{
					vector_all_ready_reg.add(rs.getString("c.id"));
				}
				
				/*
				 * Run through the vector with all the containers removing 
				 * the ones allready registered.
				 */
				for (int i = 0; i<vector_all_ready_reg.size(); i++)
				{
					String element = (String) vector_all_ready_reg.get(i);
					
					/*
					 * Remove the selected containers.
					 */
					sql = "";
					sql = "DELETE FROM user_group_container_link WHERE container_id = "+element+" AND group_id = "+group_id+";";
					stmt_in.addBatch(sql);
					sql = "";
					
					/*
					 * Handle history 
					 */
					String chemical_name = Util.getChemicalName2(element);
					if(!Util.isValueEmpty(chemical_name))
						return Return_codes.MISSING_NAME;
					else
						chemical_name = Util.double_q(chemical_name);
					
					if(handle_history_at_location_update(new_groups, element, user, chemical_name, false, conn, stmt_in) != Return_codes.SUCCESS)
					{
						return Return_codes.CREATION_FAILED;
					}					
				}
				
				return Return_codes.SUCCESS;
				
			}
			
			return Return_codes.CONNECTION_ERROR;
			
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
	}
	
	/**
	 * Remove all containers registered in the container link table, from a specific
	 * location.
	 * @param level_2_id
	 * @param stmt_in
	 * @param conn
	 * @return status of the operation.
	 */
	private int remove_all_container_as_location_link(int level_2_id,
			Vector new_groups,
			String user,
			Statement stmt_in, 
			Connection conn)
	{  
		try{
			if(conn != null)  
			{
				/*
				 * Get all the containers in the link table
				 * from the selected level2 and the selected group id.
				 */
				String sql = "SELECT lnk.container_id, lnk.group_id" +
				" FROM container c, location l, user_group_container_link lnk, user_groups ug" +
				" WHERE c.location_id = l.id" +
				" AND c.id = lnk.container_id" +
				" AND ug.id = lnk.group_id" +
				" AND c.current_quantity > 0" +
				" AND c.empty = 'F'" +
				" AND c.location_id = "+level_2_id+";";
				
				ResultSet rs = stmt_in.executeQuery(sql);
				Vector vector = new Vector();
				
				while(rs.next())
				{
					vector.add(rs.getString("lnk.container_id"));
				}
				
				/*
				 * Run through the vector with all the containers removing all;
				 */
				for (int i = 0; i<vector.size(); i++)
				{
					String element = (String) vector.get(i);
					
					/*
					 * Delete the containers.
					 */
					sql = "";
					sql = "DELETE FROM user_group_container_link WHERE container_id = "+element+";";
					stmt_in.addBatch(sql);
					sql = "";
					
					/*
					 * Handle history 
					 */
					String chemical_name = Util.getChemicalName2(element);
					if(!Util.isValueEmpty(chemical_name))
						return Return_codes.MISSING_NAME;
					else
						chemical_name = Util.double_q(chemical_name);
					
					if(handle_history_at_location_update(new_groups, element, user, chemical_name, false, conn, stmt_in) != Return_codes.SUCCESS)
					{
						return Return_codes.CREATION_FAILED;
					}						
				}
				
				return Return_codes.SUCCESS;
			}
			
			return Return_codes.CONNECTION_ERROR;
			
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
	}
	
	
	/**
	 * @return Returns the groups.
	 */
	public String[] getGroups() {
		return groups;
	}
	
	/**
	 * @param groups The groups to set.
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	
	/**
	 * @return Returns the location_id.
	 */
	public int getLocation_id() {
		return location_id;
	}
	
	/**
	 * @param location_id The location_id to set.
	 */
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	
	/**
	 * @return Returns the all_groups.
	 */
	public Vector getAll_groups() {
		return all_groups;
	}
}
