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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.batch.Batch;
import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.groups.Container_group;
import chemicalinventory.groups.Location_group;
import chemicalinventory.groups.User_group;
import chemicalinventory.history.History;
import chemicalinventory.utility.Util;

/**
 *Control the business logic around modifying and updating a container.
 **/

public class modifyContainerBean implements java.io.Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5165658091805549878L;
	private String chemical_name = "";
	private int compound_id = 0; 
	private String container_id = "";
	private int supplierID = 0;
	private String supplier = "";
	private String remark = "";
	private String locationID = "";
	private String location = "";
	private double quantity = 0.00;
	private double tara_weight = 0.00;
	private String unit = "";
	private boolean searchOk;
	private String owner = null;
	private String owner_1 = "";
	private String owner_2 = "";
	private String user = "";
	private String rem_owner = "";
	private String register_by = "";
	private String register_date = ""; 
	private String thirdChoice = "";
	private String group = null;
	private String procurement_date = "";
	private String expiry_date = "";
	private String reason_for_change = "";
	private boolean empty = false;
	private boolean update = false;

	private String o_remark = "";
	private String o_group = "";
	private String o_owner = "";
	private double o_tara = 0.00;
	private String o_unit = "";
	private double o_quantity = 0.00;
	private String o_location = "";
	private String o_procurement_date = "";
	private String o_expiry_date = "";
	private int o_supplier = 0;

	Container_group bean = new Container_group();
	User_group u_bean = new User_group();
	supplierBean supplierClass = new supplierBean();
	History history = new History();

	/** Select data for a specific container.
	 * @param id String.
	 */
	public void setContainerInfo(String id)
	{
		try{
			searchOk = false;

			//Connection from the pool

			Connection con = Database.getDBConnection();
			if(con != null)  
			{
				Statement stmt = con.createStatement();
				//Selects data from the container, omits empty containers.
				String sql1 = "SELECT com.chemical_name, s.supplier_name, c.id, c.current_quantity, c.unit," +
				" c.tara_weight, c.location_id, c.register_date, c.register_by, c.supplier_id, c.owner, c.remark," +
				" c.expiry_date, c.procurement_date"+
				" FROM compound com, container c LEFT JOIN supplier s"+
				" ON (c.supplier_id = s.id)"+
				" WHERE c.id = '"+id+"' "+
				" AND c.compound_id = com.id"+
				" AND c.current_quantity > 0"+
				" AND c.empty = 'F'";

				ResultSet rs = stmt.executeQuery(sql1);

				if(rs.next())
				{
					this.chemical_name = rs.getString("com.chemical_name");
					this.supplier = rs.getString("s.supplier_name");
					this.quantity = rs.getDouble("c.current_quantity");
					this.tara_weight = rs.getDouble("c.tara_weight");
					this.unit = rs.getString("c.unit");
					this.register_by = rs.getString("c.register_by");
					this.register_date = rs.getString("c.register_date");
					this.supplierID = rs.getInt("c.supplier_id");
					this.locationID = rs.getString("c.location_id");

					this.owner = rs.getString("c.owner");

					this.remark = rs.getString("c.remark");

					this.group = bean.find_container_groups_readonly2(Integer.parseInt(id));

					this.location = Util.getLocation(locationID);

					setExpiry_date(rs.getString("c.expiry_date"));
					setProcurement_date(rs.getString("c.procurement_date"));

					if(getExpiry_date() == null || getExpiry_date().equals("0001-01-01"))
						setExpiry_date("");

					if(getProcurement_date() == null || getProcurement_date().equals("0001-01-01"))
						setProcurement_date("");

					/*There was an container and is was not empty*/
					searchOk = true;
				}
			}
			con.close();
		}

		catch (SQLException e)
		{ 
			e.printStackTrace();
			searchOk = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			searchOk = false;
		}
	}

	/**
	 *Method to handle the update of the data, on the container.
	 *dublicate data will be omitted by the db engine.
	 **/
	public void modifyContainer()
	{
		/*
		 * If the user has set the current quantity to 0 delete the container
		 */
		if(this.quantity <= 0)
		{
			this.empty = false;
			this.update = false;

			this.quantity = o_quantity;
			//empty the container
			emptyContainer();

			//make sure the deletion was ok!
			if(!this.empty)
				update = false;
			else
				update = true;

			//return from the method, history is handled in the emptyContainer method.
			return;	
		}

		/*
		 * Find out if any modifications has been done to the registered values:
		 * And which changes.
		 */
		boolean c_supplier = false;
		boolean c_location = false;
		boolean c_quantity = false;
		boolean c_unit = false;
		boolean c_tara = false;
		boolean c_owner = false;
		boolean c_remark = false;
		boolean c_groups = false;
		boolean c_expiry_date = false;
		boolean c_procurement_date = false;

		remark = Util.encodeNullValue(remark);
		locationID = Util.encodeNullValue(locationID);

		o_owner = Util.encodeNullValue(o_owner);
		o_remark = Util.encodeNullValue(o_remark);
		o_location = Util.encodeNullValue(o_location);

		try{

			/*If the checkbox for removing owners has not been checked proceed
			 *to either add one / two or no new owners....*/
			if(!rem_owner.equals("on"))
			{
				//generate the owner data in the structure "username1/username"
				if(!owner_1.equals("X"))
				{
					c_owner = true;
					owner = owner_1;
				}
				if(!owner_2.equals("X"))
				{
					c_owner = true;

					if(!owner_1.equals("X"))
					{
						owner = owner +"/"+owner_2;
					}
					else
					{
						owner = owner_2;
					}
				}
				if(owner_1.equals("X") && owner_2.equals("X"))
				{
					owner = o_owner;
					c_owner = false;
				}
			}
			else //remove all owners..
			{
				c_owner = true;
				owner = "-";
			}

			//check the location id if set in the dropdown box on the page.
			if(!thirdChoice.equals(""))
			{
				locationID = thirdChoice;
			}

			//supplier change check
			if(supplierID == o_supplier)
				c_supplier = false;
			else 
				c_supplier = true;	

			//location change check
			if(locationID.equals(o_location))
				c_location = false;
			else
				c_location = true;

			//quantity change check
			if(quantity == o_quantity)
				c_quantity = false;
			else
				c_quantity = true;

			//unit change check
			if(unit.equals(o_unit))
				c_unit = false;
			else
				c_unit = true;

			//tara change check
			if(tara_weight == o_tara)
				c_tara = false;
			else
				c_tara = true;

			//remark change check
			if(remark.equals(o_remark))
				c_remark = false;
			else
				c_remark = true;

			//expiry date change
			if(getExpiry_date().equals( getO_expiry_date()))
				c_expiry_date = false;
			else
				c_expiry_date = true;

			//procurement date change
			if(getProcurement_date().equals( getO_procurement_date()))
				c_procurement_date = false;
			else
				c_procurement_date = true;

			/*
			 * Update the user groups the container is a member of...
			 */
			int u_id = Integer.parseInt(container_id);

			/*
			 * If the location is changed, examine if the new location
			 * is part of a user group. If the new location is part of a user
			 * group, connect the container with the same user groups
			 * as the location, an override any user selected groups. 
			 */
			if(c_location == true)
			{
				Location_group location_group = new Location_group();
				if(location_group.isLocationInGroup(Util.getIntValue(locationID)))
				{
					/*
					 * The selected group for the container
					 * is in the location_group_link,
					 * the container must be added to all 
					 * the groups, that the container is part of.
					 */
					location_group.find_location_groups_list(Util.getIntValue(locationID));
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
			}

			/*
			 * Perform the update of groups.
			 */
			update = bean.update_container_groups(group, u_id);

			if (update == false)
			{
				update = false;				
				return;
			}

//			was the container-group link updated:
			c_groups = bean.isUpdatePerformed();

			//only update if some of the above fields has changed... (here is not included a check for update in groups
			if(c_supplier == true ||
					c_location == true ||
					c_quantity == true ||
					c_unit == true ||
					c_tara == true ||
					c_owner == true ||
					c_remark == true ||
					c_groups == true ||
					c_expiry_date == true ||
					c_procurement_date == true)
			{

				update = false;

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
						stmt.clearBatch();

						//Update the container with the new info.
						//If any values is the same as the privious stored
						//this is detected by MySQL and the update of 
						//these data is ignored.
						String sql = "UPDATE container"+
						" SET supplier_id = "+supplierID+","+
						" location_id = "+locationID+","+
						" current_quantity = "+quantity+","+
						" unit = '"+unit+"',"+
						" tara_weight = "+tara_weight+","+
						" remark = '"+remark+"',"+
						" owner = '"+owner+"'";

						if(c_procurement_date)
						{
							if(getProcurement_date().equals("")) 
								sql = sql + ", procurement_date = null";
							else
								sql = sql + ", procurement_date = '"+getProcurement_date()+"'";
						}

						if(c_expiry_date) {
							if(getExpiry_date().equals(""))
								sql = sql + ", expiry_date = null";
							else
								sql = sql + ", expiry_date = '"+getExpiry_date()+"'";
						}


						sql = sql + " WHERE id = "+container_id+"";

						stmt.addBatch(sql);

						/*
						 * Update the history for modifying a container.
						 */						
						if(c_supplier == true ||
								c_location == true ||
								c_quantity == true ||
								c_unit == true ||
								c_tara == true ||
								c_owner == true ||
								c_remark == true ||
								c_groups == true ||
								c_expiry_date == true ||
								c_procurement_date)
						{
							/*
							 * Now create the entry in the history table:
							 * The changes is registered in the change_details long_text fields.
							 * a change will have the following syntax
							 * field; old_value; new_value | field; old_value; new_value..... etc
							 * current_quantity; 12.0; 16.5 | unit; g; ml..... etc
							 */                  	

							String change_details = "--";

							if(c_supplier)
							{							
								if(change_details.equals("--"))
								{
									change_details = "Supplier; "+supplierClass.getSupplier_name(String.valueOf(o_supplier))+"; "+supplierClass.getSupplier_name(String.valueOf(supplierID));
								}
								else
								{
									change_details = change_details + "| Supplier; "+supplierClass.getSupplier_name(String.valueOf(o_supplier))+"; "+supplierClass.getSupplier_name(String.valueOf(supplierID));
								}
							}

							if(c_location)
							{
								if(change_details.equals("--"))
								{
									change_details = "Location; "+Util.getLocation_notHtml(o_location)+"; "+Util.getLocation_notHtml(locationID);
								}
								else
								{
									change_details = change_details + "| Location; "+Util.getLocation_notHtml(o_location)+"; "+Util.getLocation_notHtml(locationID);
								}
							}

							if(c_quantity)
							{	
								if(change_details.equals("--"))
								{
									change_details = "Quantity; "+o_quantity+"; "+quantity;
								}
								else
								{
									change_details = change_details + "| Quantity; "+o_quantity+"; "+quantity;
								}
							}

							if(c_unit)
							{	
								if(change_details.equals("--"))
								{
									change_details = "Unit; "+o_unit+"; "+unit;
								}
								else
								{
									change_details = change_details + "| Unit; "+o_unit+"; "+unit;
								}
							}

							if(c_tara)
							{								
								if(change_details.equals("--"))
								{
									change_details = "Tara Weight; "+o_tara+"; "+tara_weight;
								}
								else
								{
									change_details = change_details + "| Tara Weight; "+o_tara+"; "+tara_weight;
								}
							}

							if(c_owner)
							{								
								if(change_details.equals("--"))
								{
									change_details = "Owner; "+o_owner+"; "+owner;
								}
								else
								{
									change_details = change_details + "| Owner; "+o_owner+"; "+owner;
								}
							}

							if(c_remark)
							{	
								if(change_details.equals("--"))
								{
									change_details = "Remark; "+o_remark+"; "+remark;
								}
								else
								{
									change_details = change_details + "| Remark; "+o_remark+"; "+remark;
								}
							}

							if(c_groups)
							{                  		
								if(change_details.equals("--"))
								{
									change_details = "Groups; "+Util.double_q(u_bean.find_groups_from_id_list(o_group))+"; "+Util.double_q(u_bean.find_groups_from_id_list(group));
								}
								else
								{
									change_details = change_details + "| Groups; "+Util.double_q(u_bean.find_groups_from_id_list(o_group))+"; "+Util.double_q(u_bean.find_groups_from_id_list(group));
								}
							}

							if(c_expiry_date)
							{
								if(change_details.equals("--"))
								{
									change_details = "Expiry Date; "+getO_expiry_date()+"; "+getExpiry_date();
								}
								else
								{
									change_details = change_details + "| Expiry Date; "+getO_expiry_date()+"; "+getExpiry_date();
								}
							}

							if(c_procurement_date)
							{
								if(change_details.equals("--"))
								{
									change_details = "Procurement Date; "+getO_procurement_date()+"; "+getProcurement_date();
								}
								else
								{
									change_details = change_details + "| Procurement Date; "+getO_procurement_date()+"; "+getProcurement_date();
								}
							}


							//Insert the history element in the table.
							//create the text remark as a standard messege plus any comment entered by the user.
							String text = History.MODIFY;

							if(reason_for_change != null && !reason_for_change.equals(""))
							{
								text = text + ".\n" + reason_for_change;
							}

							stmt.addBatch(history.insertHistory_string(History.CONTAINER_TABLE, Integer.parseInt(container_id), Util.double_q(this.chemical_name), text, user.toUpperCase(), change_details));
						}

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

						setContainerInfo(container_id);
					}
				}//end inline check if any changes at all.
			}//end if check of changes inital check, not for group changes.
			else
				update = true;
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

	/**
	 * Emptying a container is consequently the same as deleting the container
	 * form the system.
	 **/
	public void emptyContainer()
	{
		empty = false;
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
					stmt.clearBatch();

					con.setAutoCommit(false);
					//Update the container with the new info.

					String sql = "DELETE FROM container "+
					" WHERE id = "+this.container_id+"";

					try
					{
						int con_id = Integer.parseInt(this.container_id);

						stmt.addBatch(sql);

						//update the history for this container, with a deleted in message.
						stmt.addBatch(history.insertHistory_string(History.CONTAINER_TABLE, con_id, this.chemical_name, History.DELETE, user.toUpperCase(), this.unit, String.valueOf(this.quantity), "0.00"));       	

						//fire the update
						stmt.executeBatch();

						//commit the deletion of the container.
						con.commit();
						con.close();

						empty = true;     	
					}
					catch (Exception e) {
						e.printStackTrace();
						con.rollback();
						con.close();

						empty = false;
					}
				}
			}
		}

		catch (Exception e)
		{
			empty = false;
			e.printStackTrace();
		}
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return Returns the reason_for_change.
	 */
	public String getReason_for_change() {
		return reason_for_change;
	}
	/**
	 * @param reason_for_change The reason_for_change to set.
	 */
	public void setReason_for_change(String reason_for_change) {
		this.reason_for_change = reason_for_change;
	}
	/**
	 * @return Returns the group.
	 */
	public String getGroup() {
		return group;
	}

	/** Setter for the chemical name.
	 * @param cn String.
	 */  
	public void setChemical_name(String cn)
	{
		chemical_name = cn;
	}

	/** Getter for the chemical name.
	 * @return String.
	 */  
	public String getChemical_name()
	{
		return chemical_name;
	}

	/** Setter for the compound id.
	 * @param id int.
	 */  
	public void setCompound_id(int id)
	{
		compound_id = id;
	}

	/** Getter for the compound id.
	 * @return int.
	 */  
	public int getCompound_id()
	{
		return compound_id;
	}

	/** Setter for the container id.
	 * @param id String.
	 */  
	public void setContainer_id(String id)
	{
		container_id = id;
	}

	/** Getter for the id of a container.
	 * @return String.
	 */  
	public String getContainer_id()
	{
		return container_id;
	}

	/** Set the supplier id.
	 * @param sup int.
	 */  
	public void setSupplierID(int sup)
	{
		supplierID = sup;
	}

	/** Get the supplier id.
	 * @return int.
	 */  
	public int getSupplierID()
	{
		return supplierID;
	}

	/** Get the supplier name.
	 * @return String.
	 */  
	public String getSupplier()
	{
		return supplier;
	}

	/** Get the remark.
	 * @return String.
	 */  
	public String getRemark()
	{
		return remark;
	}

	/** Set the remark.
	 * @param rem String.
	 */  
	public void setRemark(String rem)
	{
		remark = rem;
	}

	/** Set the value.
	 * @param tc String.
	 */  
	public void setThirdChoice(String tc)
	{
		thirdChoice = tc;
	}

	/** Get the value of the third choice box.
	 * @return String.
	 */  
	public String getThirdChoice()
	{
		return thirdChoice;
	}

	/** Getter for the location.
	 * @return String.
	 */  
	public String getLocation()
	{
		return location;
	}

	/** Set the location id.
	 * @param l String.
	 */  
	public void setLocationID(String l)
	{
		locationID = l;
	}

	/** Getter for the location id.
	 * @return String.
	 */  
	public String getLocationID()
	{
		return locationID;
	}

	/** Set the quantity.
	 * @param q Double.
	 */  
	public void setQuantity(double q)
	{
		quantity = q;
	}

	/** Get the quantity.
	 * @return Double.
	 */  
	public double getQuantity()
	{
		return quantity;
	}

	/** Set the tara weight.
	 * @param tara Double.
	 */  
	public void setTara_weight(double tara)
	{ 
		tara_weight = tara;
	}

	/** Getter for the tara weight.
	 * @return double.
	 */  
	public double getTara_weight()
	{
		return tara_weight;
	}

	/** Setter for the unit.
	 * @param u String.
	 */  
	public void setUnit(String u)
	{
		unit = u;
	}

	/** Getter for the unit.
	 * @return String.
	 */  
	public String getUnit()
	{
		return unit;
	}

	/** Setter for register by.
	 * @param user String.
	 */  
	public void setRegister_by(String user)
	{
		register_by = user;
	}

	/** Getter for the user.
	 * @return String.
	 */  
	public String getRegister_by()
	{
		return register_by;
	}

	/** Setter used if all owners are to be removed.
	 * @param rem String.
	 */  
	public void setRem_owner(String rem)
	{
		rem_owner = rem;
	}

	/** Setter for the owner
	 * @param own String.
	 */  
	public void setOwner(String own)
	{
		owner = own;
	}

	/** Getter for the owner.
	 * @return String.
	 */  
	public String getOwner()
	{
		return owner;
	}

	/** Setter for owner number 1.
	 * @param o String.
	 */  
	public void setOwner_1(String o)
	{
		owner_1 = o.toUpperCase();
	}

	/** Getter for the first owner.
	 * @return String.
	 */   
	public String getOwner_1()
	{
		return owner_1;
	}

	/** Setter for owner 2.
	 * @param o String.
	 */  
	public void setOwner_2(String o)
	{
		owner_2 = o.toUpperCase();
	}

	/** Getter for owner number 2.
	 * @return String.
	 */  
	public String getOwner_2()
	{
		return owner_2;
	}

	/** Set the date of registration.
	 * @param date String.
	 */  
	public void setRegister_date(String date)
	{
		register_date = date;
	}

	/** Getter for the date registered.
	 * @return String.
	 */  
	public String getRegister_date()
	{
		return register_date;
	}

	/** Getter for boolean value empty to set the container empty.
	 * @return Boolean.
	 */  
	public boolean isEmpty()
	{
		return empty;
	}

	/** Getter for the check of a search.
	 * @return Boolean.
	 */  
	public boolean getSearchOk()
	{
		return searchOk;
	}

	/** Set the group.
	 * @param st String.
	 */  
	public void setGroup(String st)
	{       
		group = st;
	}

	/**
	 * @param o_location The o_location to set.
	 */
	public void setO_location(String o_location) {
		this.o_location = o_location;
	}
	/**
	 * @param o_owner The o_owner to set.
	 */
	public void setO_owner(String o_owner) {
		this.o_owner = o_owner;
	}
	/**
	 * @param o_group The o_group to set.
	 */
	public void setO_group(String o_group) {
		this.o_group = o_group;
	}
	/**
	 * @param o_quantity The o_quantity to set.
	 */
	public void setO_quantity(double o_quantity) {
		this.o_quantity = o_quantity;
	}
	/**
	 * @param o_unit The o_unit to set.
	 */
	public void setO_unit(String o_unit) {
		this.o_unit = o_unit;
	}
	/**
	 * @param o_remark The o_remark to set.
	 */
	public void setO_remark(String o_remark) {
		this.o_remark = o_remark;
	}
	/**
	 * @param o_supplier The o_supplier to set.
	 */
	public void setO_supplier(int o_supplier) {
		this.o_supplier = o_supplier;
	}
	/**
	 * @param o_tara The o_tara to set.
	 */
	public void setO_tara(double o_tara) {
		this.o_tara = o_tara;
	}
	/** Check if the update was ok.
	 * @return Boolean.
	 */  
	public boolean isUpdate()
	{
		return update;
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
	 * @return the o_expiry_date
	 */
	public String getO_expiry_date() {
		return o_expiry_date;
	}

	/**
	 * @param o_expiry_date the o_expiry_date to set
	 */
	public void setO_expiry_date(String o_expiry_date) {
		this.o_expiry_date = o_expiry_date;
	}

	/**
	 * @return the o_procurement_date
	 */
	public String getO_procurement_date() {
		return o_procurement_date;
	}

	/**
	 * @param o_procurement_date the o_procurement_date to set
	 */
	public void setO_procurement_date(String o_procurement_date) {
		this.o_procurement_date = o_procurement_date;
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