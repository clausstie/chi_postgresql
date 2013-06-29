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

package chemicalinventory.beans;

import java.sql.*;

import javax.sql.*;
import javax.naming.*;
import java.util.*;

import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;
import chemicalinventory.beans.userInfoBean;
import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.history.History;

/**
 * The location is in the database implementet as a tree. This means
 * that this class must be able to handle creation, modification, and 
 * search in the location table.
 **/

public class locationBean implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 186168038048886382L;
	private String level = "";
	private String level_0 = "";
	private String level_1 = "";
	private String level_2 = "";
	private String level_0_select = "X";
	private String level_1_select = "X";
	private String level_2_select = "X";
	private String level_0_number = "X";
	private String level_1_number = "X";
	private String level_2_number = "X";
	private String thirdChoice = "0";
	private String secondChoice = "0";
	private String firstChoice = "0";
	private int incKey1 = -1;
	private int incKey2 = -1;
	private int incKey3 = -1;
	private int no_cont = 0;
	private int no_level1 = 0;
	private int no_level2 = 0;  
	private String location = "";
	private String user = "";
	private boolean register = false;
	private boolean update = false;

	private String moveLocationId = "";

	public Vector list_0_id = new Vector();
	public Vector list_0_name = new Vector();
	public Vector list_1_id = new Vector();
	public Vector list_1_name = new Vector();
	public Vector list_2_id = new Vector();
	public Vector list_2_name = new Vector();
	public Vector l0_vector = new Vector();
	public Vector l1_vector = new Vector();
	public Vector l2_vector = new Vector();

	/*Variables to modify a location */
	public Vector l0_id = new Vector();
	public Vector l0_name = new Vector();

	public Vector l1_id = new Vector();
	public Vector l1_name = new Vector();
	public Vector l1_loc_id = new Vector();

	public Vector l2_id = new Vector();
	public Vector l2_name = new Vector();
	public Vector l2_loc_id = new Vector();

	/*instace of the userinfobean class*/
	userInfoBean uib = new userInfoBean();

	/*
	 * instance of the history bean
	 */
	History history = new History();

	/**
	 * @return Returns the level.
	 */
	public String getLevel() {
		return level;
	}

	/** Set the location name of the level 0 parameter.
	 * @param select_0 String.
	 */
	public void setLevel_0_select(String select_0)
	{ 
		level_0_select = select_0;

		//If a selection has been done, set this value to the selected.
		if(!level_0_select.equals("X") && level_0_select != null)
		{
			String level_0_name = "";
			try{

				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id, location_name FROM location"+
					" WHERE id="+level_0_select+"";

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next())
					{
						level_0_name = rs.getString("location_name");
					}
					stmt.close();
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

			level_0 = level_0_name;
			level_0_number = level_0_select;
		}
	}

	/** Getter for the level 0.
	 * @return String.
	 */  
	public String getLevel_0_select()
	{
		return level_0_select;
	}
	/** If level 1 has been selected, find the name for the level 1 selection.
	 * @param select_1 String.
	 */
	public void setLevel_1_select(String select_1)
	{ 
		level_1_select = select_1;

		//If a selection has been done, set this value to the selected.
		if(!level_1_select.equals("X") && level_1_select != null)
		{
			String level_1_name = "";
			try{
				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id, location_name FROM location"+
					" WHERE id="+level_1_select+"";

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next())
					{
						level_1_name = rs.getString("location_name");
					}
					stmt.close();
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

			level_1 = level_1_name;
			level_1_number = level_1_select;
		}
	}

	/** Getter for level 1.
	 * @return String.
	 */  
	public String getLevel_1_select()
	{
		return level_1_select;
	}

	/** find the location name of location level 2.
	 * @param select_2 String.
	 */
	public void setLevel_2_select(String select_2)
	{ 
		level_2_select = select_2;

		//If a selection has been done, set this value to the selected.
		if(!level_0_select.equals("X") && level_0_select != null)
		{
			String level_2_name = "";
			try{
				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id, location_name FROM location"+
					" WHERE id="+level_2_select+"";

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next())
					{
						level_2_name = rs.getString("location_name");
					}
					stmt.close();
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

			level_2 = level_2_name;
			level_2_number = level_2_select;
		}
	}


	/** Getter for the level 2 value.
	 * @return String.
	 */  
	public String getLevel_2_select()
	{
		return level_2_select;
	}

	/**
	 * @param ln0 String.
	 */  
	public void setLevel_0_number(String ln0)
	{
		level_0_number = ln0;
	}

	/**
	 * @return String.
	 */  
	public String getLevel_0_number()
	{
		return level_0_number;
	}

	/**
	 * @param ln1 String.
	 */  
	public void setLevel_1_number(String ln1)
	{
		level_1_number = ln1;
	}

	/**
	 * @return String.
	 */  
	public String getLevel_1_number()
	{
		return level_1_number;
	}

	/**
	 * @param ln2 String.
	 */  
	public void setLevel_2_number(String ln2)
	{
		level_2_number = ln2;
	}

	/**
	 * @return String.
	 */  
	public String getLevel_2_number()
	{
		return level_2_number;
	}

	/** Setter for the level 0.
	 * @param l0 String.
	 */  
	public void setLevel_0(String l0)
	{ 
		level_0 = l0.trim();
	}

	/** Getter for level 0.
	 * @return String.
	 */  
	public String getLevel_0()
	{
		return level_0;
	}

	/** Setter for level 1.
	 * @param l1 String.
	 */  
	public void setLevel_1(String l1)
	{
		l1 = l1.trim();
		level_1 = l1;
	}

	/** Getter for level 1.
	 * @return String.
	 */  
	public String getLevel_1()
	{
		return level_1;
	}

	/** Setter for level 2.
	 * @param l2 String.
	 */  
	public void setLevel_2(String l2)
	{ 
		l2 = l2.trim();
		level_2 = l2;
	}

	/** Getter for level 2.
	 * @return String.
	 */  
	public String getLevel_2()
	{
		return level_2;
	}

	/** Getter for location.
	 * @return String.
	 */  
	public String getLocation()
	{
		return location;
	}

	/** Getter for the second choice box.
	 * @return String.
	 */  
	public String getSecondChoice()
	{
		return secondChoice;
	}

	/** Setter for the second choice.
	 * @param sc String.
	 */  
	public void setSecondChoice(String sc)
	{
		secondChoice = sc;
	}

	/** Getter for the first choice.
	 * @return String.
	 */  
	public String getFirstChoice()
	{
		return firstChoice;
	}

	/** Setter for the first choice.
	 * @param fc String.
	 */  
	public void setFirstChoice(String fc)
	{
		firstChoice = fc;
	}

	/** Setter for the third choice.
	 * @param tc String.
	 */  
	public void setThirdChoice(String tc)
	{
		thirdChoice = tc;
	}

	/** Getter for the level 1 number.
	 * @return int.
	 */  
	public int getNo_level1()
	{
		return no_level1;
	}

	/** Getter for the level 2 number.
	 * @return int.
	 */  
	public int getNo_level2()
	{
		return no_level2;
	}

	/** Getter for no_count int.
	 * @return String.
	 */  
	public int getNo_cont()
	{
		return no_cont;
	}

	/** Getter for the is correct update boolean.
	 * @return Boolean.
	 */  
	public boolean isUpdate()
	{
		return update;
	}

	/** Perform a registration and return the succes status.
	 * @return boolean.
	 */  
	public boolean registerControl()
	{
		registerLocation();

		return register;
	}

	/** The new location entered on the .jsp page will in this method be registrered.
	 * The system behavior is controlled to act appropriately when registrering
	 * new locations and combining old locations with new.
	 **/
	public void registerLocation()
	{
		register = false;
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				//Insert Data into level0
				if(level_0_number.equals("X"))//If it is new values and not an old from the selection box, go into here.
				{
					String level_zero = "INSERT INTO location (level, location_name)"+
					" VALUES(0, '"+level_0+"')";

					stmt.executeUpdate(level_zero, Statement.RETURN_GENERATED_KEYS);

					ResultSet key1 = stmt.getGeneratedKeys();

					if (key1.next())
					{
						incKey1 = key1.getInt(1);
					}       
					key1.close();
				}

				//Insert Data into level1
				//If the values in level 1 is new enter here.
				if(level_1_number.equals("X"))
				{
					String level_one = "";

					//If the value from level 0 was new and new key was generated, 
					//otherwise the id to be put into location_id = level_0_number.
					if(level_0_number.equals("X"))
					{
						level_one = "INSERT INTO location (level, location_name, location_id)"+
						" VALUES(1, '"+level_1+"', "+incKey1+")";
					}

					if(!level_0_number.equals("X"))
					{
						level_one = "INSERT INTO location (level, location_name, location_id)"+
						" VALUES(1, '"+level_1+"', "+level_0_number+")";
					}

					stmt.executeUpdate(level_one, Statement.RETURN_GENERATED_KEYS);

					ResultSet key2 = stmt.getGeneratedKeys();

					if (key2.next())
					{
						incKey2 = key2.getInt(1);
					}       
					key2.close();
				}

				//Insert Data into level2
				String level_two = "";

				//If the value from level 0 was new an new key was generated, 
				//otherwise the id to be put into location_id = level_0_number.
				if(level_1_number.equals("X"))
				{
					level_two = "INSERT INTO location (level, location_name, location_id)"+
					" VALUES(2, '"+level_2+"', "+incKey2+")";
				}

				if(!level_1_number.equals("X"))
				{
					level_two = "INSERT INTO location (level, location_name, location_id)"+
					" VALUES(2, '"+level_2+"', "+level_1_number+")";
				}

				stmt.executeUpdate(level_two, Statement.RETURN_GENERATED_KEYS);

				ResultSet key3 = stmt.getGeneratedKeys();

				if (key3.next())
				{
					incKey3 = key3.getInt(1);
				}       
				key3.close();

				//find the registered location.
				location = Util.getLocation_no_br(""+incKey3);

				stmt.close();
				con.close();

				register = true;
			}

		}//end of try

		catch (SQLException e)
		{
			register = false;
			e.printStackTrace();
		}
		catch (Exception e)
		{
			register = false;
			e.printStackTrace();
		}
	}

	/** Method to put data into a drop down box on the .jsp page.
	 * get the location information for level_0.
	 **/
	public void level0List()
	{
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT id, location_name FROM location"+
				" WHERE level=0 ORDER BY location_name";

				ResultSet rs = stmt.executeQuery(sql);

				list_0_id.clear();
				list_0_name.clear();

				while(rs.next())
				{
					list_0_id.addElement(rs.getString("id"));
					list_0_name.addElement(rs.getString("location_name"));        
				}
				stmt.close();
				con.close();
			}

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

	/** Method to put data into a drop down box on the .jsp page.
	 * get the location information for level_1.
	 **/
	public void level1List()
	{
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "";

				if(!level_0_number.equals("X") && level_0_number != null)
				{
					sql = "SELECT id, location_name FROM location"+
					" WHERE level=1"+
					" AND location_id = "+level_0_number+""+
					" ORDER BY location_name";
				}
				else
				{
					sql = "SELECT id, location_name FROM location"+
					" WHERE level=1 ORDER BY location_name";
				}

				ResultSet rs = stmt.executeQuery(sql);

				list_1_id.clear();
				list_1_name.clear();

				while(rs.next())
				{
					list_1_id.addElement(rs.getString("id"));
					list_1_name.addElement(rs.getString("location_name"));        
				}
				stmt.close();
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

	/** Method to put data into a drop down box on the .jsp page.
	 *get the location information for level_2.  **/
	/*CURRENTLY NOT USED*/
	public void level2List()
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "";

				if(!level_0_number.equals("X") && level_0_number != null)
				{
					sql = "SELECT id, location_name FROM location"+
					" WHERE level=2"+
					" AND location_id = "+level_1_number+""+
					" ORDER BY location_name";
				}
				else
				{
					sql = "SELECT id, location_name FROM location"+
					" WHERE level=2 ORDER BY location_name";
				}

				ResultSet rs = stmt.executeQuery(sql);

				list_2_id.clear();
				list_2_name.clear();

				while(rs.next())
				{
					list_2_id.addElement(rs.getString("id"));
					list_2_name.addElement(rs.getString("location_name"));        
				}

				stmt.close();
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
	/** Count the level 2 locations, associated with a level 0 location. or a
	 * a level 1 location. This is controlled by the parameter type.
	 * @param number String.
	 * @param type int.
	 * @return int.
	 */
	public int count_level2(String number, int type)
	{
		int count = 0;
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "";
				/*Count the number of level2 locations using
				 *a level 0 location to find all sub locations*/
				if(type == 0)
				{
					sql = "SELECT count(l3.id)"+
					" FROM location l1, location l2, location l3"+
					" WHERE l2.id = l3.location_id"+
					" AND l1.id = l2.location_id"+
					" AND l1.id = "+number+";";
				}
				/*find all the level 2 locations using a level 1
				 *location to find all sub locations*/
				else if (type == 1)
				{
					sql = "SELECT count(l3.id)"+
					" FROM location l1, location l2, location l3"+
					" WHERE l2.id = l3.location_id"+
					" AND l1.id = l2.location_id"+
					" AND l2.id = "+number+";";                  
				}

				ResultSet rs = stmt.executeQuery(sql);

				if(rs.next())
				{
					count = rs.getInt("count(l3.id)");
				}

				stmt.close();
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

		return count;
	}

	/** This method stores all the locations from the db, into vectors.
	 * After that the information, shall be used to display a view of the location
	 * tree.
	 **/
	public void locationView()
	{
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				//Select all entries from level 0 and place them in a container.
				String sql0 = "SELECT id, location name FROM location WHERE level = 1";   

				ResultSet rs0 = stmt.executeQuery(sql0);

				while(rs0.next())
				{
					if(!rs0.isAfterLast())
					{
						l0_vector.addElement(rs0.getString("id")+";"+rs0.getString("location_name")+",");
					}
					else
					{
						l0_vector.addElement(rs0.getString("id")+";"+rs0.getString("location_name"));
					}
				}
				rs0.close();

				//Select all entries from level 1 and place them in a container.
				String sql1 = "SELECT id, location name, lcoation_id FROM location WHERE level = 1";   

				ResultSet rs1 = stmt.executeQuery(sql1);

				while(rs1.next())
				{
					if(!rs1.isAfterLast())
					{
						l1_vector.addElement(rs1.getString("id")+";"+rs1.getString("location_name")+rs1.getString("location_id")+",");
					}
					else
					{
						l1_vector.addElement(rs1.getString("id")+";"+rs1.getString("location_name")+rs1.getString("location_id"));
					}
				}
				rs1.close();


				//Select all entries from level 2 and place them in a container.
				String sql2 = "SELECT id, location name, location_id FROM location WHERE level = 2";   

				ResultSet rs2 = stmt.executeQuery(sql2);

				while(rs2.next())
				{
					if(!rs2.isAfterLast())
					{
						l2_vector.addElement(rs2.getString("id")+";"+rs2.getString("location_name")+rs2.getString("location_id")+",");
					}
					else
					{
						l2_vector.addElement(rs2.getString("id")+";"+rs2.getString("location_name")+rs2.getString("location_id"));
					}
				}
				rs2.close();
				stmt.close();
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

	/** Here starts method to modify a location.
	 * This one is used in the process of displaying all locations correct.
	 **/
	public void showAllLocations()
	{
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				/*here we generate vector holding all the entries from the location table
				 * (level 0) in a way, that we can extract the data from the jsp page.*/
				Statement stmt = con.createStatement();

				String sql0 = "SELECT id, location_name FROM location WHERE level = 0 ORDER BY location_name";

				ResultSet rs0 = stmt.executeQuery(sql0);

				l0_id.clear();
				l0_name.clear();

				while(rs0.next())
				{
					l0_id.addElement(rs0.getString("id"));
					l0_name.addElement(rs0.getString("location_name"));  
				}
				rs0.close();

				/*here we generate vector holding all the entries from the location table
				 * (level 1) in a way, that we can extract the data from the jsp page.*/  
				String sql1 = "SELECT id, location_name, location_id FROM location WHERE level = 1 ORDER BY location_name";

				ResultSet rs1 = stmt.executeQuery(sql1);

				l1_id.clear();
				l1_name.clear();
				l1_loc_id.clear();

				while(rs1.next())
				{        
					l1_id.addElement(rs1.getString("id"));
					l1_name.addElement(rs1.getString("location_name"));
					l1_loc_id.addElement(rs1.getString("location_id"));  
				}
				rs1.close();

				/*here we generate vector holding all the entries from the location table
				 * (level 2) in a way, that we can extract the data from the jsp page.*/
				String sql2 = "SELECT id, location_name, location_id FROM location WHERE level = 2 ORDER BY location_name";

				ResultSet rs2 = stmt.executeQuery(sql2);

				l2_id.clear();
				l2_name.clear();
				l2_loc_id.clear();

				while(rs2.next())
				{        
					l2_id.addElement(rs2.getString("id"));
					l2_name.addElement(rs2.getString("location_name"));
					l2_loc_id.addElement(rs2.getString("location_id"));  
				}
				rs2.close();
				stmt.close();
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

	/** Make the update of a level 2 location. this is changing the level 1
	 * location connected.
	 * @param type int.
	 */
	public void updateLevel2(int type)
	{
		update = false;

		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				con.setAutoCommit(false);

				String sql = null;

				//update the location, add the location to another upper location
				if(type == 0)
				{
					sql = "UPDATE location SET location_id = "+secondChoice+" where id="+level_2_number;
					stmt.executeUpdate(sql);
				}
				//update the location name
				if(type == 1)
				{
					level_2 = Util.double_q(level_2);//set double '.
					sql = "UPDATE location SET location_name = '"+level_2+"' WHERE id="+level_2_number;
					stmt.executeUpdate(sql);
				}
				else if(type != 0 && type != 1)//...error
				{
					update = false;
					return;
				}

				con.commit();
				update = true;
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
	}

	/** Make the update of a level 1 location. this is changing the level 0
	 * location connected.
	 * @param type int.
	 */
	public void updateLevel1(int type)
	{
		update = false;

		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				con.setAutoCommit(false);

				String sql = null;

				//update the location, add the location to another upper location
				if(type == 0)
				{
					sql = "UPDATE location SET location_id = "+firstChoice+" where id="+level_1_number;
					stmt.executeUpdate(sql);
				}
				//update the location name
				if(type == 1)
				{
					level_1 = Util.double_q(level_1);//set double '.
					sql = "UPDATE location SET location_name = '"+level_1+"' WHERE id="+level_1_number;
					stmt.executeUpdate(sql);
				}
				else if(type != 0 && type != 1)//...error
				{
					update = false;
					return;
				}

				stmt.executeUpdate(sql);

				con.commit();
				update = true;
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
	}

	/** Make the update of a level 0 location - change the name. **/
	public void updateLevel0_name()
	{
		update = false;

		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				con.setAutoCommit(false);

				level_0 = Util.double_q(level_0);
				String sql = "UPDATE location SET location_name = '"+level_0+"' where id="+level_0_number;

				stmt.executeUpdate(sql);

				con.commit();
				update = true;
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
	}

	/** Create information to let the user know the effects when deleting a location
	 * <b>This method retrives information only!!<b>
	 * @param type int.
	 */
	public void updateLevel_delete_info(int type)
	{        
		update = false;  

		/*Create a list of all the containers at the specific location.*/
		if(type == 0)//level_0 data
		{
			uib.setFirstChoice(level_0_number);
			uib.container_at_location_Info(1);
			no_cont = uib.container_ids.size();

			/*Create a list of all the level 1 locations attached*/
			level1List();
			no_level1 = list_1_id.size();

			/*Get number of level 2 locations*/
			no_level2 = count_level2(level_0_number, 0);

			update = true;
		}
		if(type == 1)//level_1 data
		{
			//count containers
			uib.setFirstChoice("x");
			uib.setSecondChoice(level_1_number);
			uib.container_at_location_Info(1);
			no_cont = uib.container_ids.size();

			/*Get number of level 2 locations*/
			no_level2 = count_level2(level_1_number, 1);

			update = true;   
		}
		if(type == 2)//level_2 data
		{
			//Count containers
			uib.setFirstChoice("x");
			uib.setSecondChoice("x");
			uib.setThirdChoice(level_2_number);
			uib.container_at_location_Info(1);
			no_cont = uib.container_ids.size();

			update = true;
		}
	}

	/** Find and delete all the containers at a location. if the location
	 * is a level 0, go through the tree and find all containers.
	 * The same is the case for the other locations (deleting means
	 * setting empty = true and current_quantity=0.
	 * @param type int.
	 */
	public void delete_container_at_location(int type)
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

					String sql = "";
					String sql_location = "";
					String select_id = "";//the level 2 locations selected.
					String sql_delete_0 = "";
					String sql_delete_1 = "";
					String sql_delete = "";
					Vector containers = new Vector();

					//get info about a container dependent on which level is selected.
					//if only level_0 is selected get info for all containers in that 'building'
					//else if level_1 is selected get info for all containers on that level etc.
					if(!firstChoice.equals("0") && !secondChoice.equals("0") && !thirdChoice.equals("0"))
					{
						select_id = "= "+thirdChoice;
					} 
					else if(!secondChoice.equals("0") && !firstChoice.equals("0") && thirdChoice.equals("0"))
					{
						sql_location = "SELECT id FROM location WHERE location_id = "+secondChoice+";";
						ResultSet rs = stmt.executeQuery(sql_location);

						while (rs.next())
						{
							if (!rs.isLast())//is it the last record in the result??
							{
								select_id = select_id+rs.getString("id")+", ";
							}
							else
							{
								select_id = select_id+rs.getString("id");
							}
						}                       

						select_id = "in ("+select_id+")";
					} 
					else if(thirdChoice.equals("0") && secondChoice.equals("0") && !firstChoice.equals("0"))
					{
						sql_location = "SELECT l3.id"+
						" FROM location l1, location l2, location l3"+
						" WHERE l2.id = l3.location_id"+
						" AND l1.id = l2.location_id"+
						" AND l1.id = "+firstChoice+";";

						ResultSet rs1 = stmt.executeQuery(sql_location);

						while (rs1.next())
						{
							if (!rs1.isLast())
							{
								select_id = select_id + rs1.getString("l3.id") +", ";
							}
							else
							{
								select_id = select_id + rs1.getString("l3.id");
							}
						}
						select_id = "in ("+select_id+")";
					}

					/*
					 * Check if level 2 locations has been found. If there is level 2 locations
					 * delete these locations, and if only level 2 is selected on the
					 * page delete all level 2 locations and all containers at the locations
					 */
					if(!select_id.equals("in ()") && !select_id.equals("= "))
					{
						/*
						 * History for container:
						 * 
						 * Select all containers that have the location id, that is going to
						 * be deleted. for these containers, create prepare the sql for history
						 * update.
						 */                	
						String sql_select = "SELECT container.id, compound.chemical_name, container.current_quantity, container.unit" +
						" FROM container, compound" +
						" WHERE compound.id = container.compound_id" +
						" AND container.location_id "+select_id;

						ResultSet result = stmt.executeQuery(sql_select);

						while(result.next())
						{
							int h_id = result.getInt("container.id");
							String h_name = result.getString("compound.chemical_name");
							String h_quantity = result.getString("container.current_quantity");
							String h_unit = result.getString("container.unit");

							String his = history.insertHistory_string(History.CONTAINER_TABLE, h_id, Util.double_q(h_name), History.DELETE, user.toUpperCase(), h_unit, h_quantity, "0.0");

							containers.add(his);
						}

						/*Delete level_2 locations*/
						sql = "DELETE FROM location"+
						" WHERE location.id "+select_id+";";

						/*Delete / empty containers..*/

						/*
						 * Two options here either use a system where containers are not delted
						 * then use this sql:
						 */
//						sql_delete = "UPDATE container SET current_quantity = 0,"+
//						" empty = 'T' WHERE location_id "+select_id;

						/*
						 * else delte containers from db use the following sql:
						 */
						sql_delete = "DELETE FROM container WHERE container.location_id "+select_id+";"; 		

						/*If only level 2 is to be deleted.*/
						if(type == 2)
						{
							try {
								con.setAutoCommit(false);
								stmt.clearBatch();

								stmt.executeUpdate(sql);//delete level 2 locations
								stmt.executeUpdate(sql_delete);//delete containers

								/*Update the history*/
								for (int i = 0; i <containers.size(); i++)
								{
									String history_insert = (String) containers.get(i);
									stmt.addBatch(history_insert);
								}

								stmt.executeBatch();                    

								con.commit();
								update = true;
							} catch (SQLException e) {
								e.printStackTrace();
								con.rollback();
								con.close();
								return;
							}//end try/catch
						}
					}

					//only perform this part for level 1 locations
					if(type == 1)
					{
						stmt.clearBatch();

						sql_delete_1 = "DELETE FROM location WHERE id = "+level_1_number+";";

						try {
							con.setAutoCommit(false);

							if(sql != null && !sql.equals(""))
								stmt.executeUpdate(sql); 						
							if(sql_delete_1 != null && !sql_delete_1.equals(""))
								stmt.executeUpdate(sql_delete_1);
							if(sql_delete != null && !sql_delete.equals(""))
								stmt.executeUpdate(sql_delete);

							/*Update the history*/
							for (int i = 0; i <containers.size(); i++)
							{
								String history_insert = (String) containers.get(i);
								stmt.addBatch(history_insert);
							}

							stmt.executeBatch();                    

							con.commit();
							update = true;
						} catch (SQLException e) {
							e.printStackTrace();
							con.rollback();
							con.close();
							return;
						}//end try/catch           
					}//end delete level 1 only            

					//only for level_0 locations
					if(type == 0)
					{
						/*Delete level_1 locations*/
						level1List();

						/*clear the batch*/
						stmt.clearBatch();

						for (int i = 0; i < list_1_id.size(); i++)
						{
							String del_id = (String) list_1_id.get(i);
							sql_delete_0 = "DELETE FROM location WHERE id = "+del_id+";";

							stmt.addBatch(sql);
						}

						/*Delete level_0 location*/
						sql_delete_0 = "DELETE FROM location WHERE id = "+level_0_number+";";

						stmt.addBatch(sql_delete_0);

						try {
							con.setAutoCommit(false);

							if(sql != null && !sql.equals(""))
								stmt.executeUpdate(sql); 						
							if(sql_delete != null && !sql_delete.equals(""))
								stmt.executeUpdate(sql_delete);

							/*update the history*/
							for (int i = 0; i <containers.size(); i++)
							{
								String history_insert = (String) containers.get(i);
								stmt.addBatch(history_insert);
							}

							stmt.executeBatch();

							con.commit();
							update = true;
						} catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();
							return;
						}//end try/catch        
					}//end delete level 0 only
				}
				con.close();
			}
			update = true;
		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			update = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			update = false;
		}//end catch
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Get the level and the name of a location from the id.
	 * @param location_id
	 * @return status of the operation.
	 */
	public int getNameAndLevel(int location_id)
	{
		try{
			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT level, id, location_name" +
				" FROM location" +
				" WHERE id = "+location_id+";";

				ResultSet set = stmt.executeQuery(sql);

				if(set.next())
				{
					this.level = Util.encodeTagAndNull(set.getString("level"));
					this.location = Util.encodeNullValue(set.getString("location_name"));

					con.close();
					return Return_codes.SUCCESS;
				}
				else
				{
					con.close();
					return Return_codes.EMPTY_RESULT;
				}

			}

			return Return_codes.CONNECTION_ERROR;
		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
	}

	/**
	 * Get all the level 2 sublocations connectected to a level 0 location id
	 * @param level_0_id
	 * @return list as vector of all the level 2 ids
	 */
	public Vector getLevel2FromLevel0(int level_0_id)
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT distinct l3.id" +
				" FROM location l1, location l2, location l3" +
				" WHERE l2.id = l3.location_id" +
				" AND l1.id = l2.location_id" +
				" AND l1.id = "+level_0_id+";";

				ResultSet rs = stmt.executeQuery(sql);
				Vector vector = new Vector();		

				while(rs.next())
				{
					vector.add(rs.getString("l3.id"));
				}

				if(vector == null || vector.size() < 1)
				{
					con.close();
					return null;
				}
				else
				{
					con.close();
					return vector;
				}

			}

			return null;

		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Vector getLevel1FromLevel0(int level_0_id)
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT distinct l2.id" +
				" FROM location l1, location l2, location l3" +
				" WHERE l2.id = l3.location_id" +
				" AND l1.id = l2.location_id" +
				" AND l1.id = "+level_0_id+";";

				ResultSet rs = stmt.executeQuery(sql);
				Vector vector = new Vector();		

				while(rs.next())
				{
					vector.add(rs.getString("l2.id"));
				}

				if(vector == null || vector.size() < 1)
				{
					con.close();
					return null;
				}
				else
				{
					con.close();
					return vector;
				}

			}

			return null;

		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get all the level 2 sublocations connectected to a level 1 location id
	 * @param level_1_id
	 * @return list as vector of all the level 2 ids
	 */
	public Vector getLevel2FromLevel1(int level_1_id)
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT distinct l3.id" +
				" FROM location l1, location l2, location l3" +
				" WHERE l2.id = l3.location_id" +
				" AND l1.id = l2.location_id" +
				" AND l2.id = "+level_1_id+";";

				ResultSet rs = stmt.executeQuery(sql);
				Vector vector = new Vector();		

				while(rs.next())
				{
					vector.add(rs.getString("l3.id"));
				}

				if(vector == null || vector.size() < 1)
				{
					con.close();
					return null;
				}
				else
				{
					con.close();
					return vector;
				}
			}

			return null;

		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	public Vector getLevelsAbove_level2(int id)
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = "SELECT distinct l2.id, l2.location_name, l3.id, l3.location_name" +
				" FROM location l, location l2, location l3" +
				" WHERE l.location_id = l2.id" +
				" AND l2.location_id = l3.id" +
				" AND l.location_id = (SELECT l4.location_id from location l4 WHERE id = "+id+");";

				ResultSet rs = stmt.executeQuery(sql);
				Vector vector = new Vector();		

				while(rs.next())
				{
					vector.add(rs.getString("l2.id"));
					vector.add(rs.getString("l3.id"));
				}

				if(vector == null || vector.size() < 1)
				{
					con.close();
					return null;
				}
				else
				{
					con.close();
					return vector;
				}
			}

			return null;

		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the level 0 location above a level 1 location.
	 * @param id
	 * @return 
	 */
	public Vector getLevelsAbove_level1(int id)
	{
		try{

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();

				String sql = " SELECT distinct l2.id, l2.location_name" +
				" FROM location l, location l2" +
				" WHERE l.location_id = l2.id" +
				" AND l.location_id = (SELECT l3.location_id from location l3 WHERE id = "+id+");";

				ResultSet rs = stmt.executeQuery(sql);
				Vector vector = new Vector();		

				while(rs.next())
				{
					vector.add(rs.getString("l2.id"));
				}

				if(vector == null || vector.size() < 1)
				{
					con.close();
					return null;
				}
				else
				{
					con.close();
					return vector;
				}
			}

			return null;

		}//end of try

		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Update the location id for all containers at a specific location.
	 * @return
	 */
	public boolean moveContainers(String user)
	{

		if(this.thirdChoice != null && !this.thirdChoice.equals("") && !this.thirdChoice.equals("") &&
				this.getMoveLocationId() != null && !this.getMoveLocationId().equals("") && !this.getMoveLocationId().equals("")) {

			try{

				Connection con = Database.getDBConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
					con.setAutoCommit(false);

					String sql = "SELECT id FROM container c where location_id = "+getMoveLocationId()+";";

					ResultSet rs = stmt.executeQuery(sql);
					String containerIdsAtLocation = "";
					String historySql = "";
					String old_location = Util.getLocation(this.thirdChoice);
					String new_locatin = Util.getLocation(getMoveLocationId());

					while(rs.next())
					{
						int the_id = rs.getInt("id");

						historySql = history.insertHistory_string(History.CONTAINER_TABLE, the_id, "--", History.MOVE_CONTAINER, user.toUpperCase(), old_location+"|"+new_locatin);
						stmt.addBatch(historySql);

						if(containerIdsAtLocation.equals(""))
							containerIdsAtLocation = ""+the_id;
						else 
							containerIdsAtLocation += ", " + the_id;
					}

					if(containerIdsAtLocation != null && !containerIdsAtLocation.equals(""))
					{
						/*
						 * Update the location id on the containers.
						 */
						sql = "UPDATE container SET location_id = "+this.thirdChoice+" WHERE id IN ("+containerIdsAtLocation+");";
						stmt.addBatch(sql);


						try {
							stmt.executeBatch();
							con.commit();
							con.close();
							return true;

						} catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();
							return true;
						}
					}
					else
					{
						con.close();
						return false;
					}
				}

				return false;


			}//end of try

			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else 
			return false;
	}

	/**
	 * @return the moveLocationId
	 */
	public String getMoveLocationId() {
		return moveLocationId;
	}

	/**
	 * @param moveLocationId the moveLocationId to set
	 */
	public void setMoveLocationId(String moveLocationId) {
		this.moveLocationId = moveLocationId;
	}
}