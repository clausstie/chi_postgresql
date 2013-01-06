package chemicalinventory.container;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import chemicalinventory.db.Database;
import chemicalinventory.groups.Container_group;
import chemicalinventory.user.UserInfo;
import chemicalinventory.utility.Return_codes;

public class ContainerList {
	
	private Vector list = new Vector();
	
	/**
	 * Generate a list of containers registered on a compound.
	 * @param user
	 * @param compound_id
	 * @return
	 */
	public int getContainerList(String user, int compound_id)
	{
		if(compound_id > 0 && user != null)
		{
			try{
				//Connection from the pool
				Connection con = Database.getDBConnection();
				if(con != null)  
				{					
					Statement stmt = con.createStatement();

					/*
					 * Get the all containers.
					 */
					String sql = "SELECT c.id"+
					" FROM container c "+
					" WHERE c.compound_id = "+compound_id+"" +
					" AND c.current_quantity > 0"+
					" AND c.empty = 'F';";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					/*
					 * find the user id
					 */
					UserInfo ui = new UserInfo();
					Container_group grBean = new Container_group();
					int user_id = 0;
					if(ui.retrieveNameId(user))
					{
						user_id = ui.getUser_id();  
					}
					else
					{
						con.close();
						return Return_codes.GENERAL_ERROR;
					}		              
					
					while(rs.next())
					{
						/*
						 * Only add the container to the list if the user and container
						 * is in the same group, the container is not in any gorups.
						 */
						if (grBean.group_relations(user_id, rs.getInt("c.id")))
						{
							list.add(rs.getString("c.id"));
						}
					}
					
					if(list != null && list.size()<=0)
					{
						con.close();
						return Return_codes.EMPTY_RESULT;
					}
					else
					{
						con.close();
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
		
		return Return_codes.MISSING_ID;
	}

	/**
	 * @return Returns the list.
	 */
	public Vector getList() {
		return list;
	}
}
