package chemicalinventory.groups;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

public class Create_group {

	
	private String name = null;
	private boolean group_ok = false;
	private int autoinckey = -1;
	private String id = null;

	public Create_group()
	{
		
	}
	
	
	/** This method defines a new group in the tabel user_groups **/
	public void new_group()
	{
		if(name.equals("") || name == null)
		{
			group_ok = false;
		}
		else
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
						
						//code singequotes ' to double quotes ''.
						name = Util.double_q(name);
						
						String sql = "INSERT INTO user_groups (name) VALUES('"+name+"');";
						
						stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet key = stmt.getGeneratedKeys();
						
						if (key.next())
						{
							autoinckey = key.getInt(1);
							id = ""+autoinckey;
							group_ok = true;
						}
					}
					con.close();
				}
			}//end of try
			
			catch (ClassNotFoundException e) 
			{
				group_ok = false;
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				group_ok = false;
				e.printStackTrace();
			}
			catch (Exception e)
			{
				group_ok = false;
				e.printStackTrace();
			}
		}
	}//end of method new group!!    


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
	 * @return Returns the group_ok.
	 */
	public boolean isGroup_ok() {
		return group_ok;
	}


	/**
	 * @param group_ok The group_ok to set.
	 */
	public void setGroup_ok(boolean group_ok) {
		this.group_ok = group_ok;
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
