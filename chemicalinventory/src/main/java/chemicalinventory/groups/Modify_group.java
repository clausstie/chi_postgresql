package chemicalinventory.groups;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;

public class Modify_group {
	

	private String name = null;
	private String id = null;
	boolean update = false;

	
	public Modify_group()
	{
		
	}
	
	/** 
	 * Modify the name of a group. 
	 */
	public void modify_group_name()
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
					
					//update the name of the group.
					String sql = "UPDATE user_groups SET name = '"+name+"' WHERE id = "+id;
					
					stmt.executeUpdate(sql);
					
					con.commit();
					update = true;
				}
				con.close();
			}
		}//end of try
		
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
	}
	
	/** 
	 * Delete a group. 
	 */
	public void delete_group()
	{
		try {
			update = false;
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				
				con.setAutoCommit(false);
				stmt.clearBatch();
				
				//Delete the group from the user_group table
				String sql = "DELETE FROM user_groups WHERE id ="+id;
				stmt.addBatch(sql);
				
				//delete entries in user_link table where the group is used
				sql = "DELETE FROM user_group_user_link WHERE group_id = "+id;
				stmt.addBatch(sql);    
				
				//delete the entries in the container_link table where the group is used
				sql = "DELETE FROM user_group_container_link WHERE group_id = "+id;
				stmt.addBatch(sql);    
				
				//delete the entries in the location_link table where the group is used
				sql = "DELETE FROM user_group_location_link WHERE group_id = "+id;
				stmt.addBatch(sql);
				
				try {
					stmt.executeBatch();
					con.commit();
					con.close();
					update = true;
				} catch (Exception e) {
					e.printStackTrace();
					con.rollback();
					con.close();
					update = false;
				}
			}
			update = false;
			
		}//end of try
		
		catch (Exception e)
		{
			update = false;
			e.printStackTrace();
		}
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
}