package chemicalinventory.groups;

import java.net.URLEncoder;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.user.UserInfo;
import chemicalinventory.utility.Util;

public class User_group {


	private Vector all_groups = new Vector();
	private int[] updateCounts = null;
	boolean update = false;
	private String name = null;
	private String id = null;
	private Vector all_groups_id = new Vector();
	private String base = "";
	private Vector users_in_group = new Vector();

	UserInfo userInfo = new UserInfo();

	public User_group()
	{

	}

	/** 
	 * Method to find all groups in user groups and create html to display. 
	 * @param starttab the first value for the tabindex
	 */
	public void find_groups(int starttab)
	{
		//the tabindex for the page..
		int i = starttab;

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

					String sql = "SELECT id, name FROM user_groups;";

					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next())
					{
						String tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs.getString("id")+"\" tabindex=\""+i+"\"\">"+rs.getString("name");
						all_groups.addElement(tag);
						i++;
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Method to find all groups + set the groups
	 * the user is a part of as checked
	 * @param id ID of the user.
	 */
	public void find_groups_from_id(int id)
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

					Vector checked = new Vector();
					int i = 0;
					String tag = null;
					boolean wasThere = false;

					//get the groups that the user is a part of.
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_user_link l, user u"+
					" WHERE g.id = l.group_id"+
					" AND l.user_id = u.id"+
					" AND l.user_id = "+id+";";

					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next())
					{
						checked.addElement(rs.getString("g.id"));
						i++;
					}

					//Get all groups.
					String sql2 = "SELECT id, name  FROM user_groups";

					ResultSet rs2 = stmt.executeQuery(sql2);

					/** Compare the all groups with the groups the user is a part of.
					 * If the user is part of a group, this shall be displayed as
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
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public Vector getUpdateStatements(String[] groups, int id)
	{
		Vector returnGroups = new Vector();
		try {
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				Vector checked = new Vector();//member of current groups.

				//get the groups that the user is a part of.
				String sql = "SELECT g.id, g.name"+
				" FROM user_groups g, user_group_user_link l, user u"+
				" WHERE g.id = l.group_id"+
				" AND l.user_id = u.id"+
				" AND l.user_id = "+id+";";

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					checked.addElement(rs.getString("g.id"));
				}

				/*insert or delete groups while some groups is picked.*/
				if(groups != null)
				{
					//the groups the user has entered as the new groups.
					Vector new2 = new Vector();
					stmt.clearBatch();
					String insertGroup = null;
					
					for (int i = 0; i < groups.length; i++) {
						
						String token = groups[i];
						token = token.trim();
						new2.add(token);
					}

					/*insert the groups selcted, that are not selected before*/
					for (int n = 0; n < new2.size(); n++)
					{
						if(!checked.contains(new2.get(n)))
						{
							//Insert
							insertGroup = "INSERT INTO user_group_user_link (user_id, group_id) VALUES("+id+", "+new2.elementAt(n)+")";
							returnGroups.add(insertGroup);
						}
					}

					/*Delete groups that are deleted...*/
					for (int i = 0; i <checked.size(); i++)
					{
						if(!new2.contains(checked.elementAt(i)))
						{
							//Delete
							insertGroup = "DELETE FROM user_group_user_link WHERE user_id = "+id+" AND group_id = "+checked.elementAt(i);
							returnGroups.add(insertGroup);
						}
					}
				}
				else if(groups == null && checked.size() > 0)//else delete all currently checked groups..
				{
					sql = "DELETE FROM user_group_user_link WHERE user_id = "+id;
					returnGroups.add(sql);
				}

				con.close();
				return returnGroups;
			}
			return returnGroups;
		}//end of try

		catch (SQLException ee)
		{
			ee.printStackTrace();
			return returnGroups;
		}
		catch (Exception e)
		{
			update = false;
			return returnGroups;
		}
	}

	/** 
	 * Modify user groups. Update the user_link table
	 * with the new values for membership of groups.
	 * @param group List of groups.
	 * @param id User id.
	 * @return boolean succes or not.
	 */
	public boolean update_groups(String group, int id)
	{
		update = false;
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

					con.setAutoCommit(false);

					Vector checked = new Vector();//member of current groups.
					int i = 0;

					//get the groups that the user is a part of.
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_user_link l, user u"+
					" WHERE g.id = l.group_id"+
					" AND l.user_id = u.id"+
					" AND l.user_id = "+id+";";

					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next())
					{
						checked.addElement(rs.getString("g.id"));
						i++;
					}

					/*insert or delete groups while some groups is picked.*/
					if(group != null)
					{
						//the groups the user has entered as the new groups.
						Vector new2 = new Vector();
						stmt.clearBatch();
						String insertGroup = null;
						StringTokenizer tokens = new StringTokenizer(group, ",", false);
						while(tokens.hasMoreElements())
						{
							String token = tokens.nextToken();
							token = token.trim();
							new2.add(token);
						}

						/*insert the groups selcted, that are not selected before*/
						for (int n = 0; n < new2.size(); n++)
						{
							if(!checked.contains(new2.get(n)))
							{
								//Insert
								insertGroup = "INSERT INTO user_group_user_link (user_id, group_id) VALUES("+id+", "+new2.elementAt(n)+")";
								stmt.addBatch(insertGroup);
							}
						}

						/*Delete groups that are deleted...*/
						for (i = 0; i <checked.size(); i++)
						{
							if(!new2.contains(checked.elementAt(i)))
							{
								//Delete
								insertGroup = "DELETE FROM user_group_user_link WHERE user_id = "+id+" AND group_id = "+checked.elementAt(i);
								stmt.addBatch(insertGroup);
							}
						}

						updateCounts = stmt.executeBatch();
						con.commit();

						update = true;
					}
					else if(group == null && checked.size() > 0)//else delete all currently checked groups..
					{
						sql = "DELETE FROM user_group_user_link WHERE user_id = "+id;
						stmt.executeUpdate(sql);
						con.commit();
						update = true;
					}
					else 
					{
						update = true;
					}
				}
				con.close();
			}
		}//end of try

		catch (BatchUpdateException b)
		{   
			update = false;

			System.err.println("SQLException: " + b.getMessage());
			System.err.println("SQLState:  " + b.getSQLState());
			System.err.println("Message:  " + b.getMessage());
			System.err.println("Vendor:  " + b.getErrorCode());
			System.err.print("Update counts:  ");
			updateCounts = b.getUpdateCounts();
			for (int i = 0; i < updateCounts.length; i++) 
			{
				System.err.print(updateCounts[i] + "   ");
			}
		}
		catch (ClassNotFoundException e) 
		{
			update = false;
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			update = false;
			e.printStackTrace();
		}
		catch (Exception e)
		{
			update = false;
			e.printStackTrace();
		}

		return update;
	}

	/**
	 * Create a list of group names from a list of group ids.
	 * 
	 * @param id_list
	 * 
	 * @return String the names of the groups.
	 */
	public String find_groups_from_id_list(String id_list)
	{
		if (id_list == null || id_list.equals("--"))
			id_list = "";

		id_list = id_list.trim();

		if(id_list != null && !id_list.equals("") && !id_list.equalsIgnoreCase("null"))
		{
			String g_list = "--";

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

						//get the groups that the user 
						String sql = "SELECT g.name"+
						" FROM user_groups g"+
						" WHERE g.id in ("+id_list+");";

						ResultSet rs = stmt.executeQuery(sql);

						while (rs.next())
						{
							if(g_list.equals("--"))
							{
								g_list = rs.getString("g.name");
							}
							else
							{
								g_list = g_list + " / " + rs.getString("g.name");
							}
						}
					}
					con.close();

					return g_list;
				}
			}//end of try

			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				return "--";
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return "--";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return "--";
			}

			return "--";
		}
		else
			return "--";
	}

	/** 
	 * Method to find all groups + set the groups
	 * that the USER is a part of as checked
	 * and as readonly for receipt purpose
	 * @param id User id
	 **/
	public void find_groups_readonly(int id)
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

					Vector checked = new Vector();
					int i = 0;
					String tag = null;
					boolean wasThere = false;

					//get the groups that the user is a part of.
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_user_link l, user u"+
					" WHERE g.id = l.group_id"+
					" AND l.user_id = u.id"+
					" AND l.user_id = "+id+";";

					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next())
					{
						checked.addElement(rs.getString("g.id"));
						i++;
					}

					//Get all groups.
					String sql2 = "SELECT id, name FROM user_groups";

					ResultSet rs2 = stmt.executeQuery(sql2);

					/*compare the all groups with the groups the CONTAINEr is a part of.
					 *If the CONTAINER is part of a group, this shall be displayed as
					 *checked on the page*/
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
							tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs2.getString("id")+"\" checked=\"checked\" DISABLED>"+rs2.getString("name");
						}
						else 
						{
							tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs2.getString("id")+"\" DISABLED>"+rs2.getString("name");
						}

						all_groups.addElement(tag);
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
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
	 * Method to find and show all groups registered in the db. 
	 */
	public void show_all_groups()
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

					//Select all groups from the db.
					String sql = "SELECT id, name FROM user_groups";

					ResultSet rs = stmt.executeQuery(sql);

					all_groups.clear();
					all_groups_id.clear();

					while (rs.next())
					{
						id = rs.getString("id");
						name = rs.getString("name");

						if(name == null || name.equals(""))
						{
							name = "-";
						}
						else
						{
							name = Util.encodeTag(name);
							name = URLEncoder.encode(name, "UTF-8");
						}

						/*put the values into an vector to be used on the jsp page.*/
						all_groups_id.add(id);
						all_groups.add(name);
					}

				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
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
	 * 
	 * @param id
	 */
	public void show_groups_and_users(String id)
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

					//Get the name of the group.
					String sql = "SELECT id, name FROM user_groups WHERE id = "+id+";";

					ResultSet rs = stmt.executeQuery(sql);

					if (rs.next())
					{
						name = rs.getString("name");
					}

					//now get information about all users that is a member of this group
					String user_sql = "SELECT u.id, u.user_name"+ 
					" FROM user_group_user_link g, user u"+
					" WHERE g.user_id = u.id"+
					" AND g.group_id = "+id+";";

					ResultSet user_rs = stmt.executeQuery(user_sql);

					users_in_group.clear();
					String username = "";

					while (user_rs.next())
					{              	
						if (base.equals(""))
							username = userInfo.display_owner_data_fullName(user_rs.getString("u.user_name"));
						else
							username = userInfo.display_owner_data_fullName2(user_rs.getString("u.user_name"), base);

						//add the user to the list
						users_in_group.add(username);                	
					}                 
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
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
	 * @return Returns the all_groups.
	 */
	public Vector getAll_groups() {
		return all_groups;
	}

	/**
	 * @return Returns the update.
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @return Returns the all_groups_id.
	 */
	public Vector getAll_groups_id() {
		return all_groups_id;
	}

	/**
	 * @param all_groups_id The all_groups_id to set.
	 */
	public void setAll_groups_id(Vector all_groups_id) {
		this.all_groups_id = all_groups_id;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param all_groups The all_groups to set.
	 */
	public void setAll_groups(Vector all_groups) {
		this.all_groups = all_groups;
	}

	/**
	 * @param update The update to set.
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return Returns the base.
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base The base to set.
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return Returns the userInfo.
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo The userInfo to set.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * @return Returns the users_in_group.
	 */
	public Vector getUsers_in_group() {
		return users_in_group;
	}

	/**
	 * @param users_in_group The users_in_group to set.
	 */
	public void setUsers_in_group(Vector users_in_group) {
		this.users_in_group = users_in_group;
	}

}