
package chemicalinventory.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import java.util.Vector;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

public class UserTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private int user_type_id;
	private String privileges[];
	private Vector privileges_list;
	private boolean isTypeAdministrator;

	/**
	 * Register a new user type
	 * @return the status of the operation.
	 */
	public int registerUserType() {

		/*
		 * Validate input.
		 */
		if(getName() == null || getName().equals(""))
			return Return_codes.MISSING_NAME;

		if(getPrivileges() == null || getPrivileges().length <= 0)
			return Return_codes.MISSING_PRIVILEGES;

		try {

			Connection con = Database.getDBConnection();

			if(con != null) {
				Statement stmt = con.createStatement();
				con.setAutoCommit(false);

				/*
				 * Register the name of the usertype, and get the id.
				 */
				String sql = "INSERT INTO user_types (name) VALUES('"+Util.double_q(getName().toUpperCase())+"');";
				stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();

				if(rs.next())
					setUser_type_id(rs.getInt(1));
				else
				{
					con.rollback();
					con.close();
					return Return_codes.CREATION_FAILED;
				}

				/*
				 * Use the generated key and enter into the privileges_usertype_link table.
				 */
				for (int i = 0; i < this.privileges.length; i++) {
					String privil = this.privileges[i];

					sql = "INSERT INTO user_types_privileges_link (user_type_id, privileges_id) VALUES ("+getUser_type_id()+", "+privil+");";

					stmt.addBatch(sql);
				}

				/*
				 * Insert the contents of the batch.
				 */
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

			/*
			 * No connection to db.
			 */
			return Return_codes.CONNECTION_ERROR;

		} catch (Exception e) {

			e.printStackTrace();
			return Return_codes.CREATION_FAILED;
		}
	}

	/**
	 * Create a list of available usertypes, not html encoded
	 * @param isAdministrator
	 * @return Hashtable key = id , value = name
	 */
	public Hashtable listUserTypes_NO_HTML(boolean isAdministrator) {

		Hashtable userTypeTable = new Hashtable();

		try {

			Connection con = Database.getDBConnection();

			if(con != null) {
				Statement stmt = con.createStatement();
				con.setAutoCommit(false);

				/*
				 * Get all the usertypes, but only show admin if required.
				 */
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT u.user_type_id, u.name FROM user_types u");

				if(!isAdministrator)
					sql.append(" WHERE isAdministrator = 0");

				sql.append(" ORDER BY u.name;");

				ResultSet rs = stmt.executeQuery(sql.toString());			

				while(rs.next())
				{				
					userTypeTable.put(rs.getString("u.user_type_id"), rs.getString("u.name"));
				}	

				con.close();

				return userTypeTable;
			}

			return userTypeTable;				

		} catch (Exception e) {

			e.printStackTrace();
			return userTypeTable;
		}
	}

	/**
	 * Is the user of type administrator.
	 * @param user_name
	 * @return
	 */
	public boolean isUserAdministrator(String user_name) {

		boolean isAdmin = false;

		try {

			Connection con = Database.getDBConnection();

			if(con != null) {
				Statement stmt = con.createStatement();
				con.setAutoCommit(false);

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT ut.isAdministrator FROM user u, user_types ut WHERE u.user_type_id = ut.user_type_id AND user_name = '"+user_name.toUpperCase()+"';");

				ResultSet rs = stmt.executeQuery(sql.toString());			

				if(rs.next())
				{				
					isAdmin = Boolean.parseBoolean(rs.getString("isAdministrator"));
				}	

				con.close();

				return isAdmin;
			}

			return isAdmin;				

		} catch (Exception e) {

			e.printStackTrace();
			return isAdmin;
		}
	}

	/**
	 * Create a list of existing usertypes.
	 * @return list of exsiting usertypes.
	 */
	public Vector listUserTypes(boolean isAdministrator) {

		Vector userTypeList = new Vector();

		try {

			Connection con = Database.getDBConnection();

			if(con != null) {
				Statement stmt = con.createStatement();

				/*
				 * Register the name of the usertype, and get the id.
				 */
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT u.user_type_id, u.name FROM user_types u");

				if(!isAdministrator)
					sql.append(" WHERE u.isAdministrator = 0");

				sql.append(" ORDER BY u.name;");

				ResultSet rs = stmt.executeQuery(sql.toString());

				int i = 0;
				String href = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_modify_type.jsp?code1=yes&user_type_id=";
				String tr_class = "class=\"normal\"";

				while(rs.next())
				{
					i++;
					if(i%2 == 0)
						tr_class = "class=\"blue\"";
					else
						tr_class = "class=\"normal\"";

					String entry = "<tr "+tr_class+">" +
					"<td align=\"center\"><a class=\"table_link\" href=\""+href+rs.getInt("u.user_type_id")+"\">"+i+"</a></td>" +
					"<td align=\"center\"><a class=\"table_link\" href=\""+href+rs.getInt("u.user_type_id")+"\">"+rs.getString("u.name")+"</a></td>" +
					"<tr>";

					userTypeList.add(entry);
				}

				con.close();

				if(userTypeList != null || userTypeList.size() > 0)
				{
					return userTypeList;
				}
				else
				{
					String entry = "<tr>" +
					"<td colspan=\"2\"><i>...No user types registered</i></td>" +
					"<tr>";

					userTypeList.add(entry);

					return userTypeList;
				}
			}

			String entry = "<tr>" +
			"<td colspan=\"2\"><i>...No db connection, please try again.</i></td>" +
			"<tr>";

			userTypeList.add(entry);
			return userTypeList;				

		} catch (Exception e) {

			String entry = "<tr>" +
			"<td colspan=\"2\"><i>...Search error, please try again.</i></td>" +
			"<tr>";

			userTypeList.add(entry);

			e.printStackTrace();
			return userTypeList;
		}
	}

	/**
	 * Display current registered information about a single usertype.
	 * @param user_type_id
	 */
	public void displayUserTypeForModification(int user_type_id) {

		try{
			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				Vector checked = new Vector();
				String tag = null;
				boolean wasThere = false;

				//find all the privileges that the user has registered in the roles table.
				String sql = "SELECT u.name, p.name, p.id FROM `privileges` p, user_types u, user_types_privileges_link up" +
				" WHERE u.user_type_id = up.user_type_id" +
				" AND p.id = up.privileges_id" +
				" AND u.user_type_id = "+user_type_id+";";

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					if(getName() == null || getName().equals(""))
						setName(rs.getString("u.name"));

					checked.addElement(rs.getString("p.name"));
				}

				//Get all privileges
				String sql2 = "SELECT id, name, description FROM `privileges`" +
				" WHERE display = 1 order by order_by, description;";

				ResultSet rs2 = stmt.executeQuery(sql2);

				privileges_list = new Vector();
				/* 
				 * Compare the priveliges with the privileges the user has assigned.
				 */
				while (rs2.next())
				{
					wasThere = false;

					if(checked.contains(rs2.getString("name")))
					{
						wasThere = true;
					}

					if(wasThere)
					{
						tag = "<input type=\"checkbox\" name=\"privileges\" value=\""+rs2.getString("name")+"\" checked>"+rs2.getString("description");
					}
					else
					{
						tag = "<input type=\"checkbox\" name=\"privileges\" value=\""+rs2.getString("name")+"\">"+rs2.getString("description");
					}

					this.privileges_list.add(tag);
				}

				/*
				 * Is the usertype an admnistrator
				 */
				sql2 = "SELECT isAdministrator FROM `user_types`" +
				" WHERE user_type_id = "+user_type_id+";";

				ResultSet rs_adm = stmt.executeQuery(sql2);

				if(rs_adm.next()) {

					try {
						boolean isAdm = rs_adm.getBoolean("isAdministrator");
						setTypeAdministrator(isAdm);
					} catch (Exception e) {
						e.printStackTrace();
						setTypeAdministrator(false);
					}
				}
				else
					setTypeAdministrator(false);

			}
			con.close();
		}//end of try

		catch (SQLException e)
		{
			System.out.println("Error 2: "+e);
		}
		catch (Exception e)
		{
			System.out.println("Error 3: "+e);
		}
	}

	/**
	 * Display a registered usertype with  readonly values for checkboxes.
	 * @param user_type_id
	 */
	public void displayUserTypeReadOnly(int user_type_id) {

		try{
			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				Vector checked = new Vector();
				String tag = null;
				boolean wasThere = false;

				//find all the privileges that the user has registered in the roles table.
				String sql = "SELECT u.name, p.name, p.id, p.description FROM `privileges` p, user_types u, user_types_privileges_link up" +
				" WHERE u.user_type_id = up.user_type_id" +
				" AND p.id = up.privileges_id" +
				" AND u.user_type_id = "+user_type_id+";";

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					if(getName() == null || getName().equals(""))
						setName(rs.getString("u.name"));

					checked.addElement(rs.getString("p.name"));
				}

				//Get all privileges
				String sql2 = "SELECT id, name, description FROM `privileges`" +
				" WHERE display = 1 order by order_by, description;";

				ResultSet rs2 = stmt.executeQuery(sql2);

				privileges_list = new Vector();
				/* 
				 * Compare the priveliges with the privileges the user has assigned.
				 */
				while (rs2.next())
				{
					wasThere = false;

					if(checked.contains(rs2.getString("name")))
					{
						wasThere = true;
					}

					if(wasThere)
					{
						tag = "<input disabled=\"disabled\" type=\"checkbox\" name=\"privileges\" value=\""+rs2.getString("name")+"\" checked>"+rs2.getString("description");
						privileges_list.add(tag);
					}                  
				}
			}
			con.close();
		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Modify an existing usertype:
	 * 1. update the exinting entries in the usertype table.
	 * 2. update all existing users attached to this usertype.
	 * @param user_type_id
	 * @return status of the operation.
	 */
	public int modify_usertype(int user_type_id)
	{
		/*
		 * Validate input.
		 */
		if(getName() == null || getName().equals(""))
			return Return_codes.MISSING_NAME;

		if(getPrivileges() == null || getPrivileges().length <= 0)
			return Return_codes.MISSING_PRIVILEGES;

		try {
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				/*
				 * Make sure that users only have a single type usertype
				 * privileges attached in the roles table.
				 */
				String user_sql = "SELECT DISTINCT user_name FROM user" +
				" WHERE user_type_id = "+user_type_id+";";

				ResultSet update_user_rs = stmt.executeQuery(user_sql);
				stmt.clearBatch();
				con.setAutoCommit(false);

				while(update_user_rs.next()) {

					String update = "UPDATE roles set user_type_id = "+user_type_id+
					" WHERE user_name = '"+update_user_rs.getString("user_name")+"';";
					stmt.addBatch(update);
				}

				try {
					stmt.executeBatch();
					con.commit();
				} catch (Exception e) {
					e.printStackTrace();
					con.rollback();
					return Return_codes.UPDATE_ERROR;
				}

				stmt.clearBatch();
				con.setAutoCommit(false);

				Hashtable privileges_table = new Hashtable();

				//Find entire list of privileges.
				String sql = "SELECT id, name FROM `privileges`" +
				" WHERE display = 1 order by order_by, description;";

				ResultSet priv_set = stmt.executeQuery(sql);

				while (priv_set.next()) {
					privileges_table.put(priv_set.getString("name"), priv_set.getString("id"));
				}

				Vector checked = new Vector();//member of current groups.

				//find all the privileges for the current user type and create a list.
				sql = "SELECT p.name FROM `privileges` p, user_types u, user_types_privileges_link up" +
				" WHERE u.user_type_id = up.user_type_id" +
				" AND p.id = up.privileges_id" +
				" AND u.user_type_id = "+user_type_id+";";

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					checked.add(rs.getString("p.name"));
				}

				/*
				 * Select all users currently attached to this usertype
				 */
				sql = "SELECT user_name, user_type_id FROM user" +
				" WHERE user_type_id = "+user_type_id+";";

				ResultSet user_rs = stmt.executeQuery(sql);
				Vector user_list = new Vector();

				while (user_rs.next()) {
					user_list.add(user_rs.getString("user_name"));
				}

				/*insert or delete privileges.*/
				if(getPrivileges() != null)
				{
					//the privileges the user has selected from the page
					Vector new2 = new Vector();
					String insertPriv = null;

					for (int j = 0; j < getPrivileges().length; j++) {
						new2.add(getPrivileges()[j]);
					}

					/*insert the privileges selected*/
					for (int n = 0; n < new2.size(); n++)
					{
						if(!checked.contains(new2.get(n)))
						{
							//Insert into the user_type_privileges table
							insertPriv ="INSERT INTO user_types_privileges_link (user_type_id, privileges_id) VALUES ("+user_type_id+", "+privileges_table.get(new2.get(n))+");";
							stmt.addBatch(insertPriv);

							for (Iterator iter = user_list.iterator(); iter	.hasNext();) {
								String user = (String) iter.next();

								//insert into the roles table for each user currently connected to the user_type
								insertPriv = "INSERT INTO roles (user_name, role, privileges_id, user_type_id ) VALUES('"+user.toUpperCase()+"', '"+new2.get(n)+"', "+privileges_table.get(new2.get(n))+", "+user_type_id+");";
								stmt.addBatch(insertPriv);
							}
						}
					}

					/*Delete privileges that are deleted...*/
					for (int i = 0; i <checked.size(); i++)
					{
						if(!new2.contains(checked.elementAt(i)))
						{
							//Delete
							insertPriv = "DELETE FROM user_types_privileges_link WHERE user_type_id = "+user_type_id+" AND privileges_id = "+privileges_table.get( checked.get(i))+";";
							stmt.addBatch(insertPriv);

							//delete from the roles table all the entries for the current user type with the current privilege.
							insertPriv = "DELETE FROM roles WHERE user_type_id = "+user_type_id+" AND privileges_id = "+privileges_table.get( checked.get(i))+";";
							stmt.addBatch(insertPriv);
						}
					}

					/*
					 * Update the name
					 */
					sql = "UPDATE user_types set name = '"+Util.double_q(getName().toUpperCase())+"' WHERE user_type_id = "+user_type_id+";";
					stmt.addBatch(sql);

					try {
						stmt.executeBatch();
						con.commit();
						con.close();
						return Return_codes.SUCCESS;
					} catch (Exception e) {
						con.rollback();
						e.printStackTrace();
						con.close();
						return Return_codes.UPDATE_ERROR;
					}
				}
			}
			return Return_codes.CONNECTION_ERROR;
		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return Return_codes.UPDATE_ERROR;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.UPDATE_ERROR;
		}
	}

	/**
	 * Delete a user type
	 * 
	 * This will alsp delete all user privileges registered in the roles table, and
	 * set tye user_type for the user to 0;
	 * @param user_type_id
	 * @return int status of the operation.
	 */
	public int deleteUserType(int user_type_id) {

		try {
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				stmt.clearBatch();
				con.setAutoCommit(false);

				//Delete the usertype
				String sql = "DELETE FROM user_types WHERE user_type_id = "+user_type_id+";";
				stmt.addBatch(sql);

				//delete entries in the link table
				sql = "DELETE FROM user_types_privileges_link WHERE user_type_id = "+user_type_id+";";
				stmt.addBatch(sql);

				//Delete from the roles table
				sql = "DELETE FROM roles WHERE user_type_id = "+user_type_id+";";
				stmt.addBatch(sql);				

				//Update the users, set their user type to 0
				sql = "UPDATE user SET user_type_id = 0 WHERE user_type_id = "+user_type_id+";";


				try {
					stmt.executeBatch();
					con.commit();
					con.close();
					return Return_codes.SUCCESS;
				} catch (Exception e) {
					con.rollback();
					e.printStackTrace();
					con.close();
					return Return_codes.UPDATE_ERROR;
				}

			}
			return Return_codes.CONNECTION_ERROR;
		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return Return_codes.UPDATE_ERROR;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.UPDATE_ERROR;
		}
	}

	/**
	 * Get all privileges for a usertype.
	 * @param user_type_id
	 * @return table key = privileges id, value = privileges name.
	 */
	public Hashtable getPrivilegesForType(int user_type_id) {

		try{
			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				//find all the privileges that the user has registered in the roles table.
				String sql = "SELECT p.name, p.id FROM `privileges` p, user_types_privileges_link l" +
				" WHERE l.user_type_id = " + user_type_id +
				" AND p.id = l.privileges_id" +
				" ORDER BY p.name;";

				ResultSet rs = stmt.executeQuery(sql);

				Hashtable privilegesTable = new Hashtable();

				while (rs.next())
				{
					privilegesTable.put(rs.getString("p.id"), rs.getString("p.name"));
				}

				con.close();
				return privilegesTable;
			}

			return null;
		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}    	
	}

	/**
	 * Get all the privileges currently connected to a user in the roles table
	 * @param user_id
	 * @return Hashtable key = privileges_id, value = privileges_name, or null if error.
	 */
	public Hashtable getPrivilegesInRolesForUser(int user_id) {
		try{
			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				//find all the privileges that the user has registered in the roles table.
				String sql = "SELECT r.privileges_id, p.name  FROM roles r, user u, `privileges` p" +
				" WHERE u.user_name = r.user_name" +
				" AND r.privileges_id = p.id" +
				" AND u.id = "+user_id+";";

				ResultSet rs = stmt.executeQuery(sql);

				Hashtable privilegesTable = new Hashtable();

				while (rs.next())
				{
					privilegesTable.put(rs.getString("r.privileges_id"), rs.getString("p.name"));
				}

				con.close();
				return privilegesTable;
			}
			return null;
		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the user_type_id
	 */
	public int getUser_type_id() {
		return user_type_id;
	}
	/**
	 * @param user_type_id the user_type_id to set
	 */
	public void setUser_type_id(int user_type_id) {
		this.user_type_id = user_type_id;
	}

	/**
	 * @return the privileges
	 */
	public String[] getPrivileges() {
		return privileges;
	}

	/**
	 * @param privileges the privileges to set
	 */
	public void setPrivileges(String[] privileges) {
		this.privileges = privileges;
	}

	/**
	 * @return the privileges_list
	 */
	public Vector getPrivileges_list() {
		return privileges_list;
	}

	/**
	 * @param privileges_list the privileges_list to set
	 */
	public void setPrivileges_list(Vector privileges_list) {
		this.privileges_list = privileges_list;
	}

	/**
	 * @return the isTypeAdministrator
	 */
	public boolean isTypeAdministrator() {
		return isTypeAdministrator;
	}

	/**
	 * @param isTypeAdministrator the isTypeAdministrator to set
	 */
	public void setTypeAdministrator(boolean isTypeAdministrator) {
		this.isTypeAdministrator = isTypeAdministrator;
	}
}
