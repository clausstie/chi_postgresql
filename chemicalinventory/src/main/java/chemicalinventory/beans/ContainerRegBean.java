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

import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;
import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.groups.Container_group;
import chemicalinventory.groups.Location_group;
import chemicalinventory.history.History;

/** 
 * This bean is used to control the buisniss logic around creating
 * a new container of a specific compound.
 **/ 

public class ContainerRegBean implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5826531338164790687L;
	
	public ContainerRegBean()  {
	}
	
	private String chemical_name = "";
	private int compound_id = 0;
	private int custom_id;
	private String container_id = "";
	private String procurement_date = "";
	private String expiry_date = "";
	private int supplierID = 0;
	private String supplier = "";
	private String location = "";
	private double quantity = 0.00;
	private double tara_weight = 0.00;
	private String unit = "";
	private boolean checkOK;
	private int incKey1 = -1;
	private int no_containers = 1;
	private int i = 0;
	private int status = 0;
	private String list = "";
	private String registerUser = "";
	private String registerDate = ""; 
	private String thirdChoice = "";
	private String remark = "";
	private String owner_1 = "";
	private String owner_2 = "";
	private String group = null;
	private String label = null; //get and set the labelname if the selection box is used..
	
	//The following vectors are created with public access. This is done to 
	//be able to access them form the .jsp page.
	public Vector list_id = new Vector(); 
	public Vector list_name = new Vector();
	public Vector result = new Vector();
	private Vector list_of_new = new Vector();
	
	Container_group bean = new Container_group();
	History history = new History();
	
	
	/**
	 * Status values
	 * 1: selected loction error
	 * 2: invalid number of containers to create
	 * 3: unexpected error orcurred
	 * 
	 * the integer i is set to a value equeal to the number of created contaienrs.
	 * @return
	 */
	public boolean registerMultipleContainers()
	{
		// validate the input received
		if(!thirdChoice.equals("") && !thirdChoice.equals("0"))
		{
			
			/*
			 * support for costum created container ids
			 */
			if(Attributes.USE_CUSTOM_ID && this.custom_id > 0) {
				this.no_containers = 1;
				
				/*
				 * Validate if the enterede custom id is unique
				 */
				
				boolean test = isCustomIdUnique(this.custom_id);
				
				if(!test) {
					this.status = Return_codes.CUSTOM_ID_NOT_UNIQUE;
					return false;
				}
			}
			
			if(this.no_containers > 0 && this.no_containers <= 100)
			{
				//generate the owner data in the structure "username1 / username"
				String owner = "-";
				
				if(!owner_1.equals("X"))
				{
					owner = owner_1;
				}
				if(!owner_2.equals("X"))
				{
					if(owner != null)
					{
						owner = owner +"/"+owner_2;
					}
					else
					{
						owner = owner_2;
					}
				}
				
				//encode values ' to ''
				remark = Util.double_q(remark);
				remark = Util.encodeNullValue(remark);
				
				//Create the sql insert statment for the container table
				String sql = "INSERT INTO container";
				
				if(Attributes.USE_CUSTOM_ID && this.custom_id > 0) 
					sql = sql + " (id, compound_id,";
				else
					sql = sql + " (compound_id,";
				
				
				sql = sql + " supplier_id," +
				" location_id," +
				" initial_quantity," +
				" current_quantity," +
				" unit," +
				" register_by," +
				" register_date," +
				" tara_weight," +
				" owner," +
				" remark";

				if(Util.isValueEmpty(getProcurement_date()))
					sql = sql + " , procurement_date";
				
				if(Util.isValueEmpty(getExpiry_date()))
					sql = sql + " , expiry_date";
				
				sql = sql + ")";
				
				if(Attributes.USE_CUSTOM_ID && this.custom_id > 0) 
					sql = sql + " VALUES("+custom_id+", '"+compound_id+"',";
				else
					sql = sql + " VALUES('"+compound_id+"',";
				
				sql = sql + " '"+supplierID+"'," +
				" '"+thirdChoice+"'," +
				" '"+quantity+"'," +
				" '"+quantity+"'," +
				" '"+unit+"'," +
				" '"+registerUser.toUpperCase()+"'," +
				" '"+registerDate+"'," +
				" '"+tara_weight+"'," +
				" '"+owner.toUpperCase()+"'," +
				" '"+remark+"'";

				if(Util.isValueEmpty(getProcurement_date()))
					sql = sql + ", '"+getProcurement_date()+"'";
				
				if(Util.isValueEmpty(getExpiry_date()))
					sql = sql + ", '"+getExpiry_date()+"'";
				
				sql = sql + ");";
				
				//reset the list of containers
				this.list_of_new.clear();
				
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
							
							/*
							 * Create the number of containers requested.
							 */
							for (i = 1; i <= this.no_containers; i++)
							{
								incKey1 = -1;
								
								//First create the entry into the contaienr table.
								stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
								ResultSet key1 = stmt.getGeneratedKeys();
								
								if (key1.next())
								{
									incKey1 = key1.getInt(1);
									container_id = ""+incKey1;
								}
								
								/*
								 * Handle insertion of groups for the container.
								 */
								Location_group location_group = new Location_group();
								if(location_group.isLocationInGroup(Util.getIntValue(thirdChoice)))
								{
									/*
									 * The selected group for the container
									 * is in the location_group_link,
									 * the container must be added to all 
									 * the groups, that the container is part of.
									 */
									location_group.find_location_groups_list(Util.getIntValue(thirdChoice));
									Vector list = location_group.getAll_groups();
									if(list != null && list.size()>0)
									{
										group = "";
										for (int i = 0; i < list.size(); i++) {
											String element = (String) list.get(i);
											
											if(i==0)
												group = element;
											else
												group= group + "," + element;
										}
									}
									else
										group = null;
								}
								
								/*
								 * Register groups for the container, based on 
								 * either the list from the clien or the list
								 * from the location groups.
								 */
								if(group != null)
								{
									bean.insert_container_in_link(group, incKey1);
								}
								
								/*
								 * Create entry into the history table for this container.
								 */
								stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, incKey1, Util.double_q(this.chemical_name), History.CREATE_CONTAINER, registerUser.toUpperCase(), this.unit, "--", String.valueOf(this.quantity)));
								
								//create a list of all the containers created
								this.list_of_new.add(container_id);
							}
						}
						con.close();
						status = 0;
						return true;
					}
				}//end of try
				
				catch (SQLException e)
				{
					e.printStackTrace();
					this.status = 3;
					return false;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					this.status = 3;
					return false;
				}    
			}
			else
			{
				//not a valid number of containers selected for creation
				this.status = 2;
				return false;
			}
		}
		else
		{
			//error in locaitons
			this.status = 1;
			return false;
		}
		status = 3;
		return false;
	}
	
	/**
	 * Check if the received custom container is not currently in user
	 * and has not priviously been used.
	 * @param check_id
	 * @return true if and only if the id has never priviously been used.
	 */
	public static boolean isCustomIdUnique(int check_id) {
		try {
			Connection con = Database.getDBConnection();

			if(con != null) {

				String sql = "SELECT c.id FROM container c WHERE c.id = "+check_id+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				if(rs.next()) {
					con.close();
					return false;
				}
				else {
					/*
					 * Check the history table, and make sure that the container
					 * id is not part of historical data.
					 */
					sql = "SELECT h.table FROM history h" +
							" WHERE h.table = '"+History.CONTAINER_TABLE+"'"+
							" AND h.table_id = " +check_id+";";
					
					rs = stmt.executeQuery(sql);

					if(rs.next()) {
						con.close();
						return false;
					}					
				}
				
				con.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Register all relevant information when creating a new container
	 * of a specific chemical substance.   
	 * @deprecated
	 **/
	public void regContainerCheck()
	{
		if(!thirdChoice.equals("") && !thirdChoice.equals("0"))
		{
			try{
				checkOK = false;
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
						con.setAutoCommit(false);
						
						Statement stmt = con.createStatement();
						
						remark = Util.double_q(remark);
						remark = Util.encodeNullValue(remark);
						
						//generate the owner data in the structure "username1 / username"
						String owner = "-";
						
						if(!owner_1.equals("X"))
						{
							owner = owner_1;
						}
						if(!owner_2.equals("X"))
						{
							if(owner != null)
							{
								owner = owner +"/"+owner_2;
							}
							else
							{
								owner = owner_2;
							}
						}
						
						String sql1 = "INSERT INTO container (compound_id, supplier_id, location_id, initial_quantity, current_quantity, unit, register_by, register_date, tara_weight, owner, remark)"+
						" VALUES('"+compound_id+"', '"+supplierID+"', '"+thirdChoice+"', '"+quantity+"', '"+quantity+"', '"+unit+"', '"+registerUser.toUpperCase()+"', '"+registerDate+"', '"+tara_weight+"', '"+owner.toUpperCase()+"', '"+remark+"')";
						
						stmt.executeUpdate(sql1, Statement.RETURN_GENERATED_KEYS);
						
						ResultSet key1 = stmt.getGeneratedKeys();
						
						if (key1.next())
						{
							incKey1 = key1.getInt(1);
							container_id = ""+incKey1;
						}
						
						/*insert the container in the link table between
						 *user group and container, insert all the selected
						 *groups.*/
						if(group != null)
						{
							bean.insert_container_in_link(group, incKey1);
						}
						
						/*
						 * Insert history information about creation of the container.
						 */
						stmt.executeUpdate(history.insertHistory_string(History.CONTAINER_TABLE, incKey1, Util.double_q(URLDecoder.decode(this.chemical_name, "UTF-8")), History.CREATE_CONTAINER, registerUser.toUpperCase(), this.unit, "--", String.valueOf(this.quantity)));
						
						checkOK = true;
					}
					con.commit();
					con.close();
				}
			}//end of try
			
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
				checkOK = false;
			}
			catch (SQLException e)
			{ 
				e.printStackTrace();
				checkOK = false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				checkOK = false;
			}
		}
	}
	
	/**
	 * Create a receipt for the creation of a list of containers..
	 * the list can be 1, 2 or more containers.
	 * It is checked if every id in the list is a valid container id
	 * existing in the database.
	 * @param list
	 * @return 
	 */
	public boolean containerReceipt_multiple(String list)
	{
		if(Util.isValueEmpty(list))
		{
			boolean check = false;
			
			//get the general container information, that will be identical for every container in the list.
			if(list.indexOf(",") == -1)//there is only one container in the list.
			{
				//set the general data for the containers
				if(!getContainerInfo(Integer.parseInt(list.trim())))
				{
					return false;//a single container and the id is no good
				}
				else
				{
					this.list_of_new.add(list);
					return true;
				}
			}
			else//there is multiple containers in the list
			{
				StringTokenizer tokens = new StringTokenizer(list, ",");
				while(tokens.hasMoreTokens())
				{
					if(getContainerInfo(Integer.parseInt(tokens.nextToken().trim())))
					{
						check = true;
						break;
					}
					else
						check = false;
				}
				
				if(!check)
					return false;
			}
			
			//now validate the entire list of container ids to make sure it is valid ids  		
			StringTokenizer tokens = new StringTokenizer(list, ",");
			while(tokens.hasMoreTokens())
			{
				String token = tokens.nextToken().trim();
				
				if(Util.isValidInt(token))
				{
					//make sure that the container id is a valid container
					if(Util.isContainerId(token))
					{
						this.list_of_new.add(token);
					}
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Get all the information on a container and set the private 
	 * variables on this object.
	 * @param id
	 * @return true only if the information could be set correctly.
	 */
	private boolean getContainerInfo(int id)
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
					
					String sql = "SELECT container.id, supplier.supplier_name, container.initial_quantity, " +
					"container.current_quantity, container.unit, container.tara_weight, container.register_by, container.expiry_date, container.procurement_date," +
					"container.register_date, container.owner, container.remark, container.location_id, compound.chemical_name"+
					" FROM container, compound, supplier"+
					" WHERE container.compound_id = compound.id"+
					" AND container.id = "+id+""+
					" AND container.supplier_id = supplier.id";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						this.supplier = rs.getString("supplier.supplier_name");
						this.supplier = Util.encodeNullValue(this.supplier);
						
						this.quantity = rs.getDouble("container.initial_quantity");
						this.unit = rs.getString("container.unit");
						this.tara_weight = rs.getDouble("container.tara_weight");
						this.registerUser = rs.getString("container.register_by");
						this.registerDate = rs.getString("container.register_date");
						
						this.owner_1 = rs.getString("container.owner");
						this.owner_1 = Util.encodeNullValue(this.owner_1);
						
						this.remark = rs.getString("container.remark");
						this.remark = Util.encodeNullValue(this.remark);
						
						this.chemical_name = rs.getString("compound.chemical_name");
						this.chemical_name = Util.encodeTag(this.chemical_name);
						this.chemical_name = URLEncoder.encode(this.chemical_name, "UTF-8");
						
						this.location = Util.getLocation(rs.getString("container.location_id"));
						this.group = bean.find_container_groups_readonly2(id);
						this.group = Util.encodeNullValue(this.group);
						
						setExpiry_date(rs.getString("container.expiry_date"));
						setProcurement_date(rs.getString("container.procurement_date"));
						
						if(getExpiry_date() == null || getExpiry_date().equals("0001-01-01"))
							setExpiry_date("");
						
						if(getProcurement_date() == null || getProcurement_date().equals("0001-01-01"))
							setProcurement_date("");
					}
					else
					{
						con.close();
						return false;
					}
				}
				con.close();
				return true;
			}
		}//end of try
		
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}   
		return false;
	}
	
	
	/** Create the data neede to print out a receipt after succesfull registration
	 * of a new containeruser.
	 * @param id String.
	 * @param tc String third choice.
	 * @deprecated
	 */
	public void containerReceipt(String id, String tc)
	{
		if(tc!=null && id!=null)//if these values is null no container has been registered above..
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
						
						//from the registration form the id for thirdChoice is registrered
						//based on that information, the job is now to dertermin the other two
						//levels.
						location = Util.getLocation(tc);
						
						String receipt = "SELECT c.id, s.supplier_name, c.initial_quantity, c.current_quantity, c.unit, c.tara_weight, c.register_by, c.register_date, c.owner, c.remark, com.chemical_name"+
						" FROM container c, compound com, supplier s"+
						" WHERE c.compound_id = com.id"+
						" AND c.id = "+id+""+
						" AND c.supplier_id = s.id";
						
						ResultSet rs2 = stmt.executeQuery(receipt);
						
						if(rs2.next())
						{ 
							String nr = rs2.getString("c.id");                 
							String suppl = rs2.getString("s.supplier_name");
							String cur_quant = rs2.getString("c.current_quantity");
							String u = rs2.getString("c.unit");
							String tara = rs2.getString("c.tara_weight");
							tara_weight = rs2.getDouble("c.tara_weight");
							String reg_by = rs2.getString("c.register_by");
							String reg_date = rs2.getString("c.register_date");
							String own = rs2.getString("owner");
							String mark = rs2.getString("c.remark");
							chemical_name = rs2.getString("com.chemical_name");
							
							if(nr == null || nr.equals(""))
							{
								nr = "-";
							}
							if(suppl == null || suppl.equals(""))
							{
								suppl = "-";
							}
							if(cur_quant == null || cur_quant.equals(""))
							{
								cur_quant = "-";
							}
							if(u == null || u.equals(""))
							{
								u = "-";
							}
							if(tara == null || tara.equals(""))
							{
								tara = "-";
							}
							if(reg_by == null || reg_by.equals(""))
							{
								reg_by = "-";
							}
							if(reg_date == null || reg_date.equals(""))
							{
								reg_date = "-";
							}
							if(own == null || own.equals(""))
							{
								own = "-";
							}
							if(mark == null || mark.equals(""))
							{
								mark = "-";
							}
							else
							{
								mark = Util.encodeTag(mark);
								mark = URLEncoder.encode(mark, "UTF-8");
							}
							if(chemical_name == null || chemical_name.equals(""))
							{
								chemical_name = "-";
							}
							else
							{
								chemical_name = Util.encodeTag(chemical_name);
								chemical_name = URLEncoder.encode(chemical_name, "UTF-8");
							}
							
							//data to print out a label.
							container_id = nr;
							
							result.clear();
							result.addElement(nr+"|"+location+"|"+Util.encodeTag(suppl)+"|"+cur_quant+" "+u+"|"+tara+" g"+"|"+reg_date+"|"+reg_by.toUpperCase()+"|"+own+"|"+mark);
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
		else //select the chemical name 
		{
		}
	}
	
	/** Create the data in the correct format, and
	 * prepare it to be set on the page.
	 * @return String HTML to print a label.
	 * @param cid int container id.
	 * @param int mode 1 = include container id in the name of the html
	 * form and elements
	 * 0 = no container id in the names.
	 */
	public String labelData(int cid, int mode)
	{
		String label_data = "";
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
					
					String location = "";
					String container_id = "";
					String compound_name = "";
					String tara_w = "";
					String cas_no = "";
					String supplier = "";
					String initial_quantity = "";
					String p_date = "";
					String e_date = "";
					String unit = "";
					String owner = "";
					
					String sql_con = "SELECT com.chemical_name, com.cas_number, s.supplier_name, c.procurement_date, c.expiry_date, c.initial_quantity, c.location_id, c.tara_weight, c.id, c.unit, c.owner" +
							" FROM container c, compound com, supplier s" +
							" where c.id = " +cid+
							" and com.id = c.compound_id" +
							" and c.supplier_id = s.id;";
					
					//String sql_con = "SELECT id, compound_id, location_id, tara_weight FROM container WHERE id = "+cid+";";
					
					ResultSet rs = stmt.executeQuery(sql_con);
					
					if(rs.next())
					{
						location = rs.getString("c.location_id");
						container_id = rs.getString("c.id");
						compound_name = rs.getString("com.chemical_name");
						tara_w = rs.getString("c.tara_weight");
						cas_no = rs.getString("com.cas_number");
						supplier = rs.getString("s.supplier_name");
						initial_quantity = rs.getString("c.initial_quantity");
						p_date = rs.getString("c.procurement_date");
						e_date = rs.getString("c.expiry_date");
						unit = rs.getString("c.unit");
						owner = rs.getString("c.owner");
						
						if(owner == null)
							owner = "";
						
					}
					else//no container with this number return error message
					{
						con.close();
						return "<h3>Error, no container with container id: "+cid+".</h3>";
					}
										
					String sql_location = "SELECT l1.location_name, l2.location_name, l3.location_name"+
					" FROM location l1, location l2, location l3"+
					" WHERE l3.id = l2.location_id"+
					" AND l2.id = l1.location_id"+
					" AND l1.id ="+location+"";
					
					ResultSet rs1 = stmt.executeQuery(sql_location);
					
					if(rs1.next())
					{
						location = Util.encodeTag(rs1.getString("l3.location_name"))+", "+Util.encodeTag(rs1.getString("l2.location_name"))+", "+Util.encodeTag(rs1.getString("l1.location_name"));
					}
					rs1.close();
					
					if(mode == 1)
					{
						label_data = "<form name=\"ValidForm"+container_id+"\">\n" +
						" <INPUT type=\"hidden\" VALUE=\"ID: "+container_id+"\" NAME=\"container_id"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"P: "+location+ " T:"+tara_w+" gram\" NAME=\"con_div"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"Loc.: "+location+"\" NAME=\"location"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"Tara: "+tara_w+" gram\" NAME=\"tara_w"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+compound_name+"\" NAME=\"compound_name"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+cas_no+"\" NAME=\"casno"+container_id+"\">\n " +						
						" <INPUT type=\"hidden\" VALUE=\""+supplier+"\" NAME=\"supplier"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+initial_quantity+"\" NAME=\"initialquantity"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+p_date+"\" NAME=\"p_date"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+e_date+"\" NAME=\"e_date"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+unit+"\" NAME=\"unit"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+owner+"\" NAME=\"owner"+container_id+"\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+container_id+ "\" NAME=\"barcode"+container_id+"\">\n" +
						"</form>\n";
					}
					else
					{
						label_data = "<form name=\"ValidForm\">\n" +
						" <INPUT type=\"hidden\" VALUE=\"ID: "+container_id+"\" NAME=\"container_id\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"P: "+location+ " T:"+tara_w+" gram\" NAME=\"con_div\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"Loc.: "+location+"\" NAME=\"location\">\n " +
						" <INPUT type=\"hidden\" VALUE=\"Tara: "+tara_w+" gram\" NAME=\"tara_w\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+compound_name+"\" NAME=\"compound_name\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+cas_no+"\" NAME=\"casno\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+supplier+"\" NAME=\"supplier\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+initial_quantity+"\" NAME=\"initialquantity\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+p_date+"\" NAME=\"p_date\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+e_date+"\" NAME=\"e_date\">\n " +						
						" <INPUT type=\"hidden\" VALUE=\""+unit+"\" NAME=\"unit\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+owner+"\" NAME=\"owner\">\n " +
						" <INPUT type=\"hidden\" VALUE=\""+container_id+ "\" NAME=\"barcode\">\n" +
						" <INPUT TYPE=\"button\" VALUE=\"   Print Label   \" onClick='DoPrint \"\"'>\n"+
						" </form>\n";
						
/*						label_data = "<form name=\"ValidForm\"> <INPUT type=\"hidden\" VALUE=\""+con_id+"\""+ 
						" NAME=\"con_id\"> <INPUT type=\"hidden\" VALUE=\"P: "+con_loc+ " T:"+con_tara+" gram\""+
						" NAME=\"con_div\"> <INPUT type=\"hidden\" VALUE=\""+barcode+ "\""+
						" NAME=\"barcd\"> <INPUT TYPE=\"button\" VALUE=\"   Print Label   \" onClick='DoPrint \"\"'></form>";                  	
*/
					}
				}
				con.close();
			}
		}//end of try
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return label_data;
	}
	
	/**
	 * 
	 * @param the_id
	 * @return
	 */
	public String createVBScript(String[] the_id)
	{
		String the_script = "";
		
		the_script = "" +
		"  \' Data Folder\n" +
		"  Const sDataFolder = \""+Attributes.LABEL_TEMPLATE_PATH+"\"" +
		"\n"+
		"  \'*******************************************************************\n" +
		"  \'	Replaces Image, Prints or Preview Label\n" +
		"  \'*******************************************************************\n" +
		"  Sub DoPrint(strExport)\n" +
		"    Dim TheForm\n";
		
		for (int i = 0; i < the_id.length; i++) 
		{
			String con_id = the_id[i];
			
			the_script = the_script + 
			"    Set TheForm = Document.ValidForm"+con_id+"\n" +
			"    Set ObjDoc = CreateObject(\"BrssCom.Document\")\n" +
			"    bRet = ObjDoc.Open(sDataFolder & \""+Attributes.LABEL_TEMPLATE+"\")\n" +
			"    If (bRet <> False) Then\n" +
			
			"          nIndex = ObjDoc.GetTextIndex(\"container_id\")\n" +
			"     ObjDoc.SetText nIndex, TheForm.container_id"+con_id+".Value\n" +
			
			"          nIndex2 = ObjDoc.GetTextIndex(\"location\")\n" +
			"     ObjDoc.SetText nIndex2, TheForm.location"+con_id+".Value\n" +
			
			"          nIndex3 = ObjDoc.GetTextIndex(\"compound_name\")\n" +
			"     ObjDoc.SetText nIndex3, TheForm.compound_name"+con_id+".Value\n" +
			
			"          nIndex4 = ObjDoc.GetTextIndex(\"tara_w\")\n" +
			"     ObjDoc.SetText nIndex4, TheForm.tara_w"+con_id+".Value\n" +
			
	        "	  nIndex5 = ObjDoc.GetTextIndex(\"con_div\")\n" +
		    "		ObjDoc.SetText nIndex5, TheForm.con_div"+con_id+".Value\n" +

		    "     nIndex6 = ObjDoc.GetTextIndex(\"location\")\n" +
		    "		ObjDoc.SetText nIndex6, TheForm.location"+con_id+".Value\n" +

		    "     nIndex7 = ObjDoc.GetTextIndex(\"casno\")\n" +
		    "		ObjDoc.SetText nIndex8, TheForm.casno"+con_id+".Value\n" +

		    "     nIndex8 = ObjDoc.GetTextIndex(\"supplier\")\n" +
		    "		ObjDoc.SetText nIndex8, TheForm.supplier"+con_id+".Value\n" +

		    "     nIndex9 = ObjDoc.GetTextIndex(\"initialquantity\")\n" +
		    "		 ObjDoc.SetText nIndex9, TheForm.initialquantity"+con_id+".Value\n" +

		    "     nIndex10 = ObjDoc.GetTextIndex(\"p_date\")\n" +
		    "		 ObjDoc.SetText nIndex10, TheForm.p_date"+con_id+".Value\n" +

		    "     nIndex11 = ObjDoc.GetTextIndex(\"e_date\")\n" +
		    "		 ObjDoc.SetText nInde11, TheForm.e_date"+con_id+".Value\n" +

		    "     nIndex12 = ObjDoc.GetTextIndex(\"unit\")\n" +
		    "		 ObjDoc.SetText nIndex12, TheForm.unit"+con_id+".Value\n" +
		    
		    "     nIndex13 = ObjDoc.GetTextIndex(\"owner\")\n" +
		    "		 ObjDoc.SetText nIndex13, TheForm.owner"+con_id+".Value\n" +
			
			"     ObjDoc.SetBarcodeData 0, TheForm.barcode"+con_id+".Value\n" +
			"     If (strExport = \"\") Then\n" +
			"       ObjDoc.DoPrint 0, \"0\"			'Print\n" +
			"     Else\n" +
			"       strExport = sDataFolder & strExport\n" +
			"	    ObjDoc.Export 2, strExport, 180	'Export\n" +
			"	    window.navigate strExport\n" +
			"     End If\n" +
			"     End If\n" +
			"     Set ObjDoc = Nothing\n" +
			"\n" +
			"\n";		
		}
		
		the_script = the_script + 
		"    window.close\n" +
		"  End Sub";
		
		return the_script; 
	}
	/**
	 * @return Returns the no_containers.
	 */
	public int getNo_containers() {
		return no_containers;
	}
	/**
	 * @param no_containers The no_containers to set.
	 */
	public void setNo_containers(int no_containers) {
		this.no_containers = no_containers;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return Returns the i.
	 */
	public int getI() {
		return i;
	}
	/**
	 * @return Returns the list_of_new.
	 */
	public Vector getList_of_new() {
		return list_of_new;
	}
	/**
	 * @param list_of_new The list_of_new to set.
	 */
	public void setList_of_new(Vector list_of_new) {
		this.list_of_new = list_of_new;
	}
	/**
	 * @return Returns the group.
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @return Returns the list.
	 */
	public String getList() {
		return list;
	}
	/**
	 * @param list The list to set.
	 */
	public void setList(String list) {
		this.list = list;
	}
	
	/** Setter for the chemical name.
	 * @param cn String
	 */  
	public void setChemical_name(String cn)
	{
		chemical_name = cn;
	}
	
	/** Getter for the chemical name.
	 * @return String chemical name.
	 */  
	public String getChemical_name()
	{
		return chemical_name;
	}
	
	/** Set the id of a compound.
	 * @param id int.
	 */  
	public void setCompound_id(int id)
	{
		compound_id = id;
	}
	
	/** Getter for the id of a comound.
	 * @return int compound id.
	 */  
	public int getCompound_id()
	{
		return compound_id;
	}
	
	/** Getter for the container id.
	 * @return String container id.
	 */  
	public String getContainer_id()
	{
		return container_id;
	}
	
	/** Set the supplier id.
	 * @param sup int id of a supplier.
	 */  
	public void setSupplierID(int sup)
	{
		supplierID = sup;
	}
	
	/** Getter for the supplier.
	 * @return String.
	 */  
	public String getSupplier()
	{
		return supplier;
	}
	
	/** Setter for the third choice box in a location.
	 * @param tc String.
	 */  
	public void setThirdChoice(String tc)
	{
		thirdChoice = tc;
	}
	
	/** Getter for the third choice value.
	 * @return String.
	 */  
	public String getThirdChoice()
	{
		return thirdChoice;
	}
	
	/** Getter for the location.
	 * @return String location adress.
	 */  
	public String getLocation()
	{
		return location;
	}
	
	/** Setter for the quantity.
	 * @param q double.
	 */  
	public void setQuantity(double q)
	{
		quantity = q;
	}
	
	/** Getter for the quantity.
	 * @return Double.
	 */  
	public double getQuantity()
	{
		return quantity;
	}
	
	/** Setter for the tara weight.
	 * @param tara double.
	 */  
	public void setTara_Weight(double tara)
	{ 
		tara_weight = tara;
	}
	
	/** Getter for the tara weight.
	 * @return double.
	 */  
	public double getTara_Weight()
	{
		return tara_weight;
	}
	
	/** Setter for unit.
	 * @param u String.
	 */  
	public void setUnit(String u)
	{
		unit = u;
	}
	
	/** Getter for the unit.
	 * @return String unit.
	 */  
	public String getUnit()
	{
		return unit;
	}
	
	/** Setter for the user registering a container.
	 * @param user String user name.
	 */  
	public void setRegisterUser(String user)
	{
		registerUser = user;
	}
	
	/** Getter for the user registering a new container.
	 * @return String the user.
	 */  
	public String getRegisterUser()
	{
		return registerUser;
	}
	
	/** Setter for owner number 1.
	 * @param o String.
	 */  
	public void setOwner_1(String o)
	{
		owner_1 = o.toUpperCase();
	}
	
	/** Getter for owner no 1.
	 * @return String user name.
	 */  
	public String getOwner_1()
	{
		return owner_1;
	}
	
	/** Setter for owner nr. 2.
	 * @param o String.
	 */  
	public void setOwner_2(String o)
	{
		owner_2 = o.toUpperCase();
	}
	
	/** Getter for owner number 2.
	 * @return String user name.
	 */  
	public String getOwner_2()
	{
		return owner_2;
	}
	
	/** Setter for the date.
	 * @param date String.
	 */  
	public void setRegisterDate(String date)
	{
		registerDate = date;
	}
	
	/** Getter for the date.
	 * @return String date.
	 */  
	public String getRegisterDate()
	{
		return registerDate;
	}
	
	/** Setter for the remark associated with a container.
	 * @param r String.
	 */  
	public void setRemark(String r)
	{
		remark = r.trim();
	}
	
	/** Getter for the remark associated with a container.
	 * @return String remark.
	 */  
	public String getRemark()
	{
		return remark;
	}
	
	/** Set label information.
	 * @param l String.
	 */  
	public void setLabel(String l)
	{
		label = l.trim();
	}
	
	/** Getter for the label information.
	 * @return String.
	 */  
	public String getLabel()
	{
		return label;
	}
	
	/** Set the group.
	 * @param st String.
	 */  
	public void setGroup(String st)
	{       
		group = st;
	}

	/**
	 * @return Returns the checkOK.
	 */
	public boolean isCheckOK() {
		return checkOK;
	}

	/**
	 * @return the custom_id
	 */
	public int getCustom_id() {
		return custom_id;
	}

	/**
	 * @param custom_id the custom_id to set
	 */
	public void setCustom_id(int custom_id) {
		this.custom_id = custom_id;
	}

	/**
	 * @return the expiry_date
	 */
	public String getExpiry_date() {
		return expiry_date;
	}

	/**
	 * @param expiry_date the expiry_date to set
	 */
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	/**
	 * @return the procurement_date
	 */
	public String getProcurement_date() {
		return procurement_date;
	}

	/**
	 * @param procurement_date the procurement_date to set
	 */
	public void setProcurement_date(String procurement_date) {
		this.procurement_date = procurement_date;
	}
}