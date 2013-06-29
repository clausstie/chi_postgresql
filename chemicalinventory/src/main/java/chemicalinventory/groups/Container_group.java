package chemicalinventory.groups;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.utility.Util;

public class Container_group {

	private String group_list = "";
	private Vector all_groups = new Vector();
	boolean update = false;
	boolean updatePerformed = false;
	private int[] updateCounts = null;
	private Vector containers_in_group = new Vector();
	private String name = null;
	private String id = null;
	
	public Container_group()
	{
		
	}

	
	/** 
	 * Insert the CONTAINER in the the link table, and add the
	 * CONTAINER id and the id for the group
	 * @param group List of groups the container shall be a member of.
	 * @param key Container id.
	 **/
	public void insert_container_in_link(String group, int key)
	{
		try{
			//Connection from the pool
				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					con.setAutoCommit(false);
					
					Statement stmt = con.createStatement();
					
					/*Insert the CONTAINER id in the link table between 
					 *CONTAIENER and user group.*/
					stmt.clearBatch();
					String insertGroup = null;
					StringTokenizer tokens = new StringTokenizer(group, ",", false);
					while(tokens.hasMoreElements())
					{
						String token = tokens.nextToken();
						token = token.trim();
						insertGroup = "INSERT INTO user_group_container_link (container_id, group_id) VALUES("+key+", "+token+")";
						stmt.addBatch(insertGroup);
					}
					
//					commit the changes to the db.
					try {
						stmt.executeBatch();
						con.commit();
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
						con.rollback();
						con.close();
					}
				}
			
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
		
	/** Method to find all groups + set the groups
	 * that the CONTAINER is a part of as checked on the html page.
	 * @param id Container id.
	 * @param readonly boolean- show check box as readonly or not.
	 **/
	public void find_container_groups_from_id(int id, boolean readonly)
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
					int n = 0;
					String tag = null;
					this.group_list = "";
					boolean wasThere = false;
					
					/*
					 * Should the check boxes be displayed as readonly...??
					 */
					String s_read_only = "disabled=\"disabled\"";
						if(readonly == false)
							s_read_only = "";			
					
					
					//get the groups that the user is a part of.
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_container_link l, container c"+
					" WHERE g.id = l.group_id"+
					" AND l.container_id = c.id"+
					" AND l.container_id = "+id+";";
					
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
							tag = "<input type=\"checkbox\" name=\"groups\" value=\""+rs2.getString("id")+"\" checked=\"checked\""+s_read_only+">"+rs2.getString("name");
							
							//create a list of currently selected groups as a comma separeted list.
							n++;
							if( n==1 )
							{
								group_list = rs2.getString("id");
							}
							else
							{
								group_list = group_list + "," +rs2.getString("id"); 
							}
						}
						else
						{
							tag = "<input type=\"checkbox\" name=\"groups\" "+s_read_only+" value=\""+rs2.getString("id")+"\">"+rs2.getString("name");
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
	 * Modify user groups, Update the CONTAINER_link table
	 * with the new values for membership of groups
	 * @param group List of groups the container is now a member of.
	 * @param id Container id
	 **/
	public boolean update_container_groups(String group, int id)
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
					this.updatePerformed = false;
					
					//get the groups that the CONTAINER is a part of, before the update.
					String sql = "SELECT g.id, g.name"+
					" FROM user_groups g, user_group_container_link l, container c"+
					" WHERE g.id = l.group_id"+
					" AND l.container_id = c.id"+
					" AND l.container_id = "+id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					while (rs.next())
					{
						checked.addElement(rs.getString("g.id"));
						i++;
					}
					
					/*insert or delete groups while some groups is picked.*/
					if(group != null)
					{
						//the groups the user has entered as the new groups for the CONTAINER.
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
						
						for (int n = 0; n < new2.size(); n++)
						{
							if(!checked.contains(new2.get(n)))
							{
								//Insert
								insertGroup = "INSERT INTO user_group_container_link (container_id, group_id) VALUES("+id+", "+new2.elementAt(n)+")";
								stmt.addBatch(insertGroup);
								
								//An update of the link to the container has been performed
								this.updatePerformed = true;
							}
						}
						
						for (i = 0; i <checked.size(); i++)
						{
							if(!new2.contains(checked.elementAt(i)))
							{
								//Delete
								insertGroup = "DELETE FROM user_group_container_link WHERE container_id = "+id+" AND group_id = "+checked.elementAt(i);
								stmt.addBatch(insertGroup);
								
								//                          An update of the link to the container has been performed
								this.updatePerformed = true;
							}
						}
						
						updateCounts = stmt.executeBatch();
						con.commit();
						
						update = true;
					}
					else if(group == null && checked.size() > 0)//else delete all currently checked groups..
					{
						sql = "DELETE FROM user_group_container_link WHERE container_id = "+id;
						stmt.executeUpdate(sql);
						con.commit();
						update = true;
						
						//An update of the link to the container has been performed
						this.updatePerformed = true;
					}
					else 
					{
						//An update of the link to the container has been NOT performed
						this.updatePerformed = false;
						update = true;
					}
					
				}
				con.close();
			}
		}//end of try
		
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
	
	/** Method to find all groups + set the groups
	 * that the CONTAINER is a part of as checked
	 * and as readonly for receipt purpose
	 * @param id Container id.
	 **/
	public void find_container_groups_readonly(int id)
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
					" FROM user_groups g, user_group_container_link l, container c"+
					" WHERE g.id = l.group_id"+
					" AND l.container_id = c.id"+
					" AND l.container_id = "+id+";";
					
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
	 * Create a String separeted by <br/> and listing the groups 
	 * that a container is part of.
	 * @param id
	 * @return string list of all the groups. if none an empty string.
	 */
	public String find_container_groups_readonly2(int id)
	{
		String value = "";
		
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
					
					//get the groups that the user is a part of.
					String sql = "SELECT g.name"+
					" FROM user_groups g, user_group_container_link l, container c"+
					" WHERE g.id = l.group_id"+
					" AND l.container_id = c.id"+
					" AND l.container_id = "+id+";";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					while (rs.next())
					{
						if(value.equals(""))
							value = Util.encodeTagAndNull(rs.getString("g.name"));
						else
							value = value + "<br/>" + Util.encodeTagAndNull(rs.getString("g.name"));
					}
				}
				con.close();
				return value;
			}
		}//end of try
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return "";
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
		return "";
	}

	
	/** 
	 * Method to verify if the container is a member of af group.
	 * If not return show = true. If it is check to see if the user is
	 * also a member of this same group.
	 * @param user_id User id.
	 * @param container_id Container id.
	 * @return boolean OK if the container and user is part of the same
	 * group.
	 **/
	public boolean group_relations(int user_id, int container_id)
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
					
					//Firts check if the container is a part of any old group.
					String sql = "SELECT container_id, group_id FROM user_group_container_link"+
					" WHERE container_id = "+container_id;
					
					ResultSet rs = stmt.executeQuery(sql);
					
					//if it is in a group check to see if the user is also in the same group.
					if (rs.next())
					{
						sql = "SELECT ll.group_id, ul.group_id FROM user_group_container_link ul, user_group_user_link ll"+
						" WHERE ul.container_id = "+container_id+
						" AND ll.user_id = "+user_id+
						" AND ll.group_id = ul.group_id;";
						
						ResultSet rs2 = stmt.executeQuery(sql);
						
						/*if the user and container is part of the same group
						 *show the container for the user...*/
						if (rs2.next())
						{
							con.close();
							return true;
						}
						
						/*else the user and container is not in the 
						 *same group, return false*/
						con.close();
						return false;
					}
					/*return that this containr can be showed, because containers that are not 
					 *a member of a group is to be seen by all*/
					else
					{
						con.close();
						return true;             
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
		
		//if we get down here something went very wrong!!
		return false;
	}
	
	
	/**
	 * Show groups and contaienrs in a list.
	 * 
	 * @param id
	 */
	public void show_groups_and_containers(String id)
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
					
					//now get information about all containers that is a member of this group
					String container_sql = "SELECT c.id, com.chemical_name, c.location_id, c.initial_quantity, c.current_quantity"+
					" FROM user_group_container_link g, container c, compound com"+
					" WHERE g.container_id = c.id"+
					" AND c.compound_id = com.id"+
					" AND g.group_id = "+id+
					" ORDER BY com.chemical_name;";
					
					ResultSet container_rs = stmt.executeQuery(container_sql);
					
					/*Create the decimal formatter to make the quantity string look nice*/
					DecimalFormat format = new DecimalFormat(Util.PATTERN);
					DecimalFormatSymbols dec = new DecimalFormatSymbols();
					dec.setDecimalSeparator('.');
					format.setDecimalFormatSymbols(dec);
					
					containers_in_group.clear();
					
					while (container_rs.next())
					{
						String location = Util.getLocation(container_rs.getString("c.location_id"));
						String chemical_name = container_rs.getString("com.chemical_name").toUpperCase();
						chemical_name = URLEncoder.encode(chemical_name, "UTF-8");
						String container_id = container_rs.getString("c.id");
						String current_quant = format.format(container_rs.getDouble("c.current_quantity"));
						String init_quantity = format.format(container_rs.getDouble("c.initial_quantity"));
						
						//add the user to the list
						containers_in_group.add(container_id+"|"+chemical_name+"|"+location+"|"+current_quant+"|"+init_quantity);                	
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
	 * @param all_groups The all_groups to set.
	 */
	public void setAll_groups(Vector all_groups) {
		this.all_groups = all_groups;
	}

	/**
	 * @return Returns the group_list.
	 */
	public String getGroup_list() {
		return group_list;
	}

	/**
	 * @param group_list The group_list to set.
	 */
	public void setGroup_list(String group_list) {
		this.group_list = group_list;
	}

	/**
	 * @return Returns the update.
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update The update to set.
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return Returns the updatePerformed.
	 */
	public boolean isUpdatePerformed() {
		return updatePerformed;
	}

	/**
	 * @param updatePerformed The updatePerformed to set.
	 */
	public void setUpdatePerformed(boolean updatePerformed) {
		this.updatePerformed = updatePerformed;
	}

	/**
	 * @return Returns the containers_in_group.
	 */
	public Vector getContainers_in_group() {
		return containers_in_group;
	}

	/**
	 * @param containers_in_group The containers_in_group to set.
	 */
	public void setContainers_in_group(Vector containers_in_group) {
		this.containers_in_group = containers_in_group;
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

}
