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
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import java.net.*;
import java.text.*;

import chemicalinventory.context.Attributes;
import chemicalinventory.groups.Container_group;
import chemicalinventory.history.History;
import chemicalinventory.user.UserInfo;
import chemicalinventory.utility.Util;

/**
 * The ResultBean is an implementation of buisiness logic, ensuring
 * that result can be properly displayed. This result is concentrated
 * to all chemical data and container data for a single compound.
 **/

public class ResultBean implements java.io.Serializable
{

	public ResultBean()
	{
	}

	private static final long serialVersionUID = -7464317651910127820L;

	private String id = "";
	private String cd_id = "";
	private String compound_id = "";
	private String chemical_name = "";
	private String cas_number = "";
	private String density = "";
	private String remark = "";
	private String register_by = "";
	private String register_date = "";
	private String container_id = "";
	private String dbMolfile = "";
	private String chemicalFormula = "";
	private String molWeight = "";
	private String user = null;
	private String base = "";
	private int user_id = 0;
	public Vector stat = new Vector();
	public Vector container = new Vector();
	public Vector container_ID = new Vector();
	private boolean structure;
	private boolean encode = false;

	History history = new History();

	/*an instance of the UserInfo class, used to get information on 
	 *a username in the owners field*/
	UserInfo ui = new UserInfo();

	/*an instace of the group bean*/
	Container_group grBean = new Container_group();

	/** Setter for the id.
	 * @param i String.
	 */  
	public void setId(String i)
	{ 
		id = i;
	}

	/** Getter for the id.
	 * @return String.
	 */  
	public String getId()
	{
		return id;
	}

	/**
	 * @return Returns the user_id.
	 */
	public String getUser_id() {
		/*conver the integer to a string and return that.*/
		return String.valueOf(user_id);
	}

	/** Setter for the cd_id.
	 * @param i String.
	 */  
	public void setCd_id(String i)
	{ 
		cd_id = i;
	}

	/** Getter for the cd_id.
	 * @return String cd_id.
	 */  
	public String getCd_id()
	{
		return cd_id;
	}

	/** Getter for the compound id.
	 * @return String.
	 */  
	public String getCompound_id()
	{
		return compound_id;
	}

	/** Get the chemical name and if not encoded allready perform the
	 * encoding (URL) of the name.
	 * @return String.
	 */
	public String getChemical_name()
	{
		chemical_name = chemical_name.toUpperCase();
		if(encode == false)//Make sure that the name is only encoded once.
		{
			try{
				chemical_name = Util.encodeTag(chemical_name);
				chemical_name = URLEncoder.encode(chemical_name, "UTF-8");
				encode = true;
			}
			catch (Exception e)
			{
				System.out.println("Error in url encode");
			}
		}
		return chemical_name;
	}

	/** Getter for cas number.
	 * @return String.
	 */  
	public String getCas_number()
	{
		return cas_number;
	}

	/** Getter for density.
	 * @return String
	 */  
	public String getDensity()
	{
		return density;
	}

	/** Getter for remark.
	 * @return String.
	 */  
	public String getRemark()
	{
		return remark;
	}

	/** Getter for registered by.
	 * @return String.
	 */  
	public String getRegister_by()
	{
		return register_by;
	}

	/** Getter for the date registered.
	 * @return String.
	 */  
	public String getRegister_date()
	{
		return register_date;
	}

	/** Getter for the container id.
	 * @return String.
	 */  
	public String getContainer_id()
	{
		return container_id;
	}

	/** Setter for the molfile.
	 * @param mol String.
	 */  
	public void setDbMolfile(String mol)
	{
		dbMolfile = mol;
	}

	/** Getter for the db molfile.
	 * @return String.
	 */  
	public String getDbMolfile()
	{
		return dbMolfile;
	}

	/** Getter for the chemical formula.
	 * @return String.
	 */  
	public String getChemicalFormula()
	{
		return chemicalFormula;
	}

	/** Getter for the molweight.
	 * @return String.
	 */  
	public String getMolWeight()
	{
		return molWeight;
	}

	/** Setter for the user.
	 * @param u String.
	 */  
	public void setUser(String u)
	{
		user = u;
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

	/** Getter for the the structure, present/absent.
	 * @return Boolean.
	 */  
	public boolean getStructure()
	{
		return structure;
	}
	/**
	 * The result() method retrieves all information about at single 
	 * chemical compound. This includes the structure and all else specific
	 * to this compound. Furthermore, the containers for this compound is also
	 * retrieved and displayed. If the contaiener is associated to one/more 
	 * specific groups, the containers will only be displayed to the user
	 * if he/she is also a member of one of these groups.
	 **/
	public void result()
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

					String sql = "";
					int struc = 0;

					ResultSet res = stmt.executeQuery("Select cd_id from compound where id="+id+"");

					if(res.next())
					{
						struc = res.getInt("cd_id");
					}

					if(struc != 0)
					{
						sql = "SELECT c.id, s.cd_structure, s.cd_formula, s.cd_molweight, c.cd_id, c.chemical_name, c.cas_number, c.remark, c.density, c.register_by, c.register_date FROM compound c, structures s" +
						" WHERE c.id="+id+""+
						" AND c.cd_id = s.cd_id";

						structure = true;
					}
					else
					{
						sql = "SELECT c.id, c.cd_id, c.chemical_name, c.cas_number, c.remark, c.density, c.register_by, c.register_date FROM compound c" +
						" WHERE c.id="+id+"";

						structure = false;
					}

					res.close();

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next())
					{
						dbMolfile = new String(rs.getBytes(2),"ASCII");
						chemical_name = rs.getString("c.chemical_name");
						cas_number = Util.encodeNullValue(rs.getString("c.cas_number"));
						density = rs.getString("c.density");
						remark = Util.encodeNullValue(rs.getString("c.remark"));
						register_by = rs.getString("c.register_by");
						register_date = rs.getString("c.register_date");
						if(structure == false)
						{
							chemicalFormula = "--";
							molWeight = "--"; 
						}
						else
						{
							chemicalFormula = rs.getString("s.cd_formula");
							molWeight = rs.getString("s.cd_molweight"); 
							cd_id = rs.getString("c.cd_id");
						}
					}

					rs.close();

					//Get information about the containers....

					String sql1 = "SELECT c.id, c.compound_id, c.location_id, c.empty, c.owner, c.remark, c.current_quantity, c.unit, c.register_by, c.register_date, c.user_id, u.user_name, u.room_number, u.telephone"+
					" FROM location l, container c LEFT JOIN user u"+
					" ON (c.user_id = u.id)"+
					" LEFT JOIN supplier s"+
					" ON (c.supplier_id = s.id)"+
					" WHERE c.compound_id = "+id+"" +
					" AND c.current_quantity > 0"+
					" AND c.empty = 'F'"+
					" AND c.location_id = l.id ORDER BY c.id;";

					ResultSet rs1 = stmt.executeQuery(sql1);

					container.clear();
					container_ID.clear();
					String current_location = "";
					String home_location = "";

					/*find the user id*/
					ui.retrieveNameId(user);
					user_id = ui.getUser_id();

					/*Create the decimal formatter to make the quantity string look nice*/
					DecimalFormat format = new DecimalFormat(Util.PATTERN);
					DecimalFormatSymbols dec = new DecimalFormatSymbols();
					dec.setDecimalSeparator('.');
					format.setDecimalFormatSymbols(dec);

					while(rs1.next())
					{ 
						/*make sure that only the containers which is either a
						 *part of the user group the user is a part of, or the 
						 *container is not, in any group, is displayed*/
						if (grBean.group_relations(user_id, rs1.getInt("c.id")))
						{
							String unit = rs1.getString("c.unit");
							String owner = rs1.getString("c.owner");
//							String room = rs1.getString("u.room_number");
//							String phone = rs1.getString("u.telephone");
							String mark = rs1.getString("c.remark");
							String cur_quant = rs1.getString("c.current_quantity");
							home_location = Util.getLocation(rs1.getString("c.location_id"));
							String name = chemical_name;

							if(rs1.getString("c.user_id").equals("0"))
							{
								current_location = "Home location";
							}
							else
							{
								current_location = ui.display_owner_data_base(rs1.getString("u.user_name"), base);
								//current_location = rs1.getString("u.user_name").toUpperCase() + "<br>" + room + "<br>" + phone;
							}

							//replace empty fields with "-".
							if(name == null || name.equals(""))
							{
								name = "-";
							}
							else
							{
								name = Util.encodeTag(name);
								name = URLEncoder.encode(name, "UTF-8");
							}
							if(unit == null || unit.equals(""))
							{
								unit = "-";
							}
							/*If there is one or two owners, build HTML code to show information
                     about the users that is registeret as owners*/
							if(owner == null || owner.equals("") || owner.equals("null"))
							{
								owner = "-";
							}
							else 
							{
								owner = ui.display_owner_data_base(owner, base);
							}
							if(cur_quant == null || cur_quant.equals(""))
							{
								cur_quant = "-";
							}
							else
								cur_quant = format.format(rs1.getDouble("c.current_quantity"));                  

							if(mark == null || mark.equals(""))
							{
								mark = "-";
							}
							else
							{
								mark = Util.encodeTag(mark);
								mark = URLEncoder.encode(mark, "UTF-8");
							}

							container.addElement(rs1.getString("c.id")+"|"+owner+"|"+home_location+"|"+current_location+"|"+mark+"|"+cur_quant+" "+unit);
							container_ID.addElement(rs1.getString("c.id"));
							stat.addElement(rs1.getString("user_id"));
						}
					}
					rs1.close();
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println("Result "+e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Display the information about a compound.
	 *
	 */
	public void getCompoundInfo()
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

					String sql = "";
					sql = "SELECT c.id, c.cd_id, c.chemical_name, c.cas_number, c.remark, c.density," +
					" c.register_by, c.register_date, s.cd_formula, s.cd_molweight " +
					" FROM compound c LEFT JOIN structures s" +
					" ON c.cd_id = s.cd_id" +
					" WHERE c.id="+id+"";

					ResultSet rs = stmt.executeQuery(sql);

					if(rs.next())
					{
						chemical_name = rs.getString("c.chemical_name");
						cas_number = rs.getString("c.cas_number");
						density = rs.getString("c.density");
						remark = rs.getString("c.remark");
						register_by = rs.getString("c.register_by");
						register_date = rs.getString("c.register_date");

						chemicalFormula = rs.getString("s.cd_formula");
						molWeight = rs.getString("s.cd_molweight"); 

						//encode the values if null
						chemical_name = Util.encodeNullValue(chemical_name);
						cas_number = Util.encodeNullValue(cas_number);
						density = Util.encodeNullValue(density);
						remark = Util.encodeNullValue(remark);
						register_by = Util.encodeNullValue(register_by);
						register_date = Util.encodeNullValue(register_date);
						chemicalFormula = Util.encodeNullValue(chemicalFormula);
						molWeight = Util.encodeNullValue(molWeight);
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println("Result "+e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public void showContainers(String comp_id)
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

					//Get information about the containers....

					String sql1 = "SELECT c.id, c.compound_id, c.location_id, c.empty, c.owner, c.remark, c.current_quantity, c.unit, c.register_by, c.register_date, c.user_id, u.user_name, u.room_number, u.telephone"+
					" FROM container c, location l LEFT JOIN user u"+
					" ON (c.user_id = u.id)"+
					" LEFT JOIN supplier s"+
					" ON (c.supplier_id = s.id)"+
					" WHERE c.compound_id = "+id+"" +
					" AND c.current_quantity > 0"+
					" AND c.empty = 'F'"+
					" AND c.location_id = l.id ORDER BY c.id;";

					ResultSet rs1 = stmt.executeQuery(sql1);

					container.clear();
					container_ID.clear();
					String current_location = "";
					String home_location = "";

					/*Create the decimal formatter to make the quantity string look nice*/
					DecimalFormat format = new DecimalFormat(Util.PATTERN);
					DecimalFormatSymbols dec = new DecimalFormatSymbols();
					dec.setDecimalSeparator('.');
					format.setDecimalFormatSymbols(dec);

					while(rs1.next())
					{ 
						String unit = rs1.getString("c.unit");
						String owner = rs1.getString("c.owner");
//						String room = rs1.getString("u.room_number");
//						String phone = rs1.getString("u.telephone");
						String mark = rs1.getString("c.remark");
						String cur_quant = rs1.getString("c.current_quantity");
						home_location = Util.getLocation(rs1.getString("c.location_id"));

						if(rs1.getString("c.user_id").equals("0"))
						{
							current_location = "Home location";
						}
						else
						{
							current_location = rs1.getString("u.user_name");
						}

						if(unit == null || unit.equals(""))
						{
							unit = "-";
						}

						if(owner == null || owner.equals("") || owner.equals("null"))
						{
							owner = "-";
						}

						if(cur_quant == null || cur_quant.equals(""))
						{
							cur_quant = "-";
						}
						else
							cur_quant = format.format(rs1.getDouble("c.current_quantity"));                  

						if(mark == null || mark.equals(""))
						{
							mark = "-";
						}
						else
						{
							mark = Util.encodeTag(mark);
							mark = URLEncoder.encode(mark, "UTF-8");
						}

						container.addElement(rs1.getString("c.id")+"|"+owner+"|"+home_location+"|"+current_location+"|"+mark+"|"+cur_quant+" "+unit);
						container_ID.addElement(rs1.getString("c.id"));
					}
					rs1.close();
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println("Result "+e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Write the history line for the displayed compound
	 * @param user
	 * @param compound_name
	 * @param compound_id
	 * @return
	 */
	public boolean writeCompoundDisplayHistory(String user, String compound_name, String compound_id)
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

					String historySql = history.insertHistory_string(History.COMPOUND_TABLE, Integer.parseInt(compound_id), compound_name, History.DISPLAY_COMPOUND, user.toUpperCase(), History.DISPLAY_COMPOUND);
					stmt.executeUpdate(historySql);

					stmt.close();
					con.close();

					return true;
				}

				con.close();
				return false;
			}
			
			return false;

		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

}