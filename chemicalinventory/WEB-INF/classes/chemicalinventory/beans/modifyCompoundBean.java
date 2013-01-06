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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemaxon.jchem.db.JChemSearch;
import chemaxon.jchem.db.UpdateHandler;
import chemaxon.reaction.Standardizer;
import chemaxon.util.ConnectionHandler;

import chemicalinventory.context.Attributes;
import chemicalinventory.history.History;
import chemicalinventory.utility.Util;

/**
 *This bean is used to modify the information about a specific compound.
 **/

public class modifyCompoundBean implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8274093871379364590L;

//	variable to hold the structure from the drawer
	private String molfile = "";	

	//variables for registration of the chemical.
	private int cd_id = 0;
	private int cd_id_new = 0;
	private String id = "";
	private String chemical_name = "";
	private String cas_number = "";
	private double density = 0.00;
	private String remark = "";
	private String register_user = "";
	private String register_date = "";
	private String user = "";
	public boolean checkOK;
	public boolean exist = false;

	private String o_name = "";
	private double o_density = 0.00;
	private String o_cas = "";
	private String o_formula = "";
	private String o_mw = "";
	private String o_remark = "";
	private String reason_for_change = "";

	History history = new History();
	/** Setter for id.
	 * @param i String.
	 */  
	public void setId(String i)
	{
		id = i;
	}

	/** Getter for id.
	 * @return String.
	 */  
	public String getId()
	{
		return id;
	}

	/** Setter for the cd_id.
	 * @param ci int.
	 */  
	public void setCd_id(int ci)
	{
		cd_id = ci;
	}

	/** Getter for the cd_id.
	 * @return String.
	 */  
	public String getCd_id()
	{
		return ""+cd_id;
	}

	/** Setter for chemical name.
	 * @param cn String.
	 */  
	public void setchemical_name(String cn)
	{
		cn.trim();
		chemical_name = cn.toUpperCase();
	}

	/** Getter for the chemical name.
	 * @return String.
	 */  
	public String getchemical_name()
	{
		return chemical_name;
	}

	/** Setter for the cas number.
	 * @param casn String.
	 */  
	public void setcas_number(String casn)
	{
		cas_number = casn.trim();
	}

	/** Getter for the cas number.
	 * @return String.
	 */  
	public String getcas_number()
	{
		return cas_number;
	}

	/** Setter for the density.
	 * @param dt Double.
	 */  
	public void setDensity(double dt)
	{
		density = dt;
	}

	/** Getter for density.
	 * @return Double.
	 */   
	public double getDensity()
	{ 
		return density;
	}

	/** Setter for remark.
	 * @param rm String.
	 */  
	public void setRemark(String rm)
	{
		remark = rm.trim();
	}

	/** Getter for remark.
	 * @return String.
	 */  
	public String getRemark()
	{
		return remark;
	}

	/** Setter for the user.
	 * @param u String.
	 */  
	public void setUser(String u)
	{
		user = u;
	}

	/** Set the user registering.
	 * @param ru String.
	 */  
	public void setregister_user(String ru)
	{
		register_user = ru;
	}

	/** Getter for the user performing the registration.
	 * @return String.
	 */  
	public String getregister_user()
	{
		return register_user;
	}

	/** Setter for the date.
	 * @param rd String.
	 */  
	public void setregister_date(String rd)
	{
		register_date = rd;
	}

	/** Getter for the date.
	 * @return String.
	 */  
	public String getregister_date()
	{
		return register_date;
	}

	/** Setter for the molfile.
	 * @param mf String.
	 */  
	public void setmolfile(String mf)
	{
		molfile = mf;
		//This string is the value returned by marvin in case there is no structure
		//drawn in the applet on the search page.
		String molchecker = "0  0  0  0  0  0            999 V2000";

		if (mf.substring(40, 77).equals(molchecker))
		{
			molfile = "";
		}
	}

	/** Getter for the molfile.
	 * @return String.
	 */  
	public String getmolfile()
	{
		return molfile;
	}

	/**
	 *Find the cd_id (structure id) from the compound table.
	 *@param i Id of the compound.
	 **/
	public void setCd_id_db(String i)
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

					String receipt = "SELECT cd_id FROM compound" +
					" WHERE id = "+i+"";

					ResultSet rs = stmt.executeQuery(receipt);

					if(rs.next())
					{
						cd_id = rs.getInt("cd_id");
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(""+e);
		}
		catch (SQLException e)
		{
			System.out.println(""+e);  
		}
		catch (Exception e)
		{
			System.out.println(""+e);
		}
	}

	/**
	 * The cd_id and molfile indicates which situation has orcurrecd.
	 * Either update, delete, new or no structure at all.<br><br>
	 * '+' = present<br>
	 * '-' = not present<br><br>
	 *
	 *<table border="1">
	 *    <tr>
	 *        <th>situation</th>
	 *        <th>cd_id present</th>
	 *        <th>molfile</th>
	 *    </tr>
	 *    <tr>
	 *        <td>Update</td>
	 *        <td align="center">+</td>
	 *        <td align="center">+</td>
	 *    </tr>
	 *    <tr>
	 *        <td>Delete</td>
	 *        <td align="center">+</td>
	 *        <td align="center">-</td>
	 *    </tr>
	 *    <tr>
	 *        <td>New</td>
	 *        <td align="center">-</td>
	 *        <td align="center">+</td>
	 *    </tr>
	 *    <tr>
	 *        <td>NOT</td>
	 *        <td align="center">-</td>
	 *        <td align="center">-</td>
	 *    </tr>
	 * </table><br>
	 *
	 *
	 * If there is neither cd_id value nor molfile
	 * there was no structure registeret and no 
	 * structure is to be registeret...
	 **/
	public void modifyCompound()
	{ 
		checkOK = false;
		boolean isStructureUpdate = false;//false = no update performed, true = update of the structure performed.

		if (!this.chemical_name.equals("") && this.chemical_name != null)
		{
			/*
			 * Make sure that changes to the cas number is not a
			 * cas number allready in use.
			 */
			/*
			 * Make sure the cas number is not allready registered.
			 */
			if(Util.isValueEmpty(this.cas_number))
			{
				ChemicalRegBean bean = new ChemicalRegBean();
				if(bean.isCasNumberUsed(this.cas_number, Integer.parseInt(this.id)))
				{
					checkOK = false;

					return;
				}
			}

			try{  
				setCd_id_db(id);//get the value of current cd_id for this compound from the db.

				if(cd_id != 0 || !molfile.equals(""))
				{
					boolean isInsertion = false;
//					if the cd_id value is null and the molfile is not null - add a structure where there was none before.
					if(cd_id == 0 && !molfile.equals(""))
					{
						isInsertion = true;
					}
					else
						if(cd_id != 0 && !molfile.equals(""))//..else update an existing compound. (..or no change)
						{
							isInsertion = false;
						}
					ConnectionHandler ch = new ConnectionHandler(); 
					ch.setDriver(Attributes.DB_DRIVER);
					ch.setUrl(Attributes.DB_NAME);
					ch.setLoginName(Attributes.DB_USER);
					ch.setPassword(Attributes.DB_PWD);
					ch.setPropertyTable(Attributes.J_PROP_TABLE);

					ch.connect();

					UpdateHandler uh = new UpdateHandler(ch,
							(isInsertion? 
									UpdateHandler.INSERT :
										UpdateHandler.UPDATE),
										Attributes.DB_STUCTURE_TABLE, "");

					try {
//						if cd_id is not null a structure was priviously registrered, 
//						but now molfile is empty, thus delete the entry in the structures table.
						if(cd_id != 0 && molfile.equals(""))
						{
//							deletion of a row in the structures table.
							UpdateHandler.deleteRows(ch, Attributes.DB_STUCTURE_TABLE, "WHERE cd_id="+cd_id);
						}
						else
						{
							// Checking if the structure already exists in the
							// structure table
							JChemSearch searcher = new JChemSearch();
							searcher.setConnectionHandler(ch); 
							searcher.setQueryStructure(molfile); 
							searcher.setSearchType(JChemSearch.PERFECT);
							searcher.setStructureTable(Attributes.DB_STUCTURE_TABLE); 
							searcher.setWaitingForResult(true); 
							//searcher.setStructureCaching(true);//cache structures in memory
							searcher.run();
							int foundItemsCount = searcher.getResultCount();

							int result_id = 0;

//							We are trying to modify a structure and change it to an allready exising value... error
							if(foundItemsCount > 0 && isInsertion)
							{
								exist = true;
								throw new Exception("Structure already exists (cd_id="+ searcher.getResult(0)+")");
							}
							else if (foundItemsCount > 0 && isInsertion == false)
							{
								//This is the cd_id (structure to be updated.)
								result_id = searcher.getResult(0);

								/*
								 * if the cd_id and the result id is the same
								 * no update to the existing structure has been performed
								 */
								if(cd_id == result_id)
								{                                    	
									isStructureUpdate = false;//the structure has not been updated.
								}
								else
									isStructureUpdate = true;//the structure has been modified.
							}

//							create a new record in the structures table
							if(isInsertion)
							{
								/*
								 * Create a new entry in the structure table 
								 * based on the user input
								 */
								uh.setValuesForFixColumns(molfile);	 
								uh.execute();

								//getting the id from the newly registered structure
								JChemSearch searcher2 = new JChemSearch();
								searcher2.setConnectionHandler(ch); 
								searcher2.setQueryStructure(molfile); 
								searcher2.setSearchType(JChemSearch.PERFECT);
								searcher2.setStructureTable(Attributes.DB_STUCTURE_TABLE); 
								searcher2.setWaitingForResult(true); 
								//searcher2.setStructureCaching(true);//cache structures in memory
								searcher2.run();

								cd_id_new = searcher2.getResult(0);
							}
							else
							{//update an existing record in the structures table 
								if(isInsertion == false && foundItemsCount<=0)//we are updating the structure to a NOT existing compound.
								{
									uh.setValuesForFixColumns(cd_id, molfile);
									uh.execute();

									isStructureUpdate = true;
								}
								else
								{//if the user is trying to update an existing structure to be the same as another existing structure abort. 
									if(isInsertion == false && foundItemsCount > 0 && cd_id != result_id) //if the user is trying to update an existing structure to be the same as another existing structure abort.
									{
										exist = true;
										throw new Exception("Structure already exists (cd_id="+ searcher.getResult(0)+")");
									}//end if error in update
								}//end else
							}//end else
						}
					} 
					finally {
						uh.close();
					}
				}

				//--------------update of the compound table.------------------

				//Connection from the pool
				Context init = new InitialContext();
				if(init == null ) 
					throw new Exception("No Context");

				Context ctx = (Context) init.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
				if(ds != null)
				{
					Connection conn = ds.getConnection();
					if(conn != null)  
					{
						/*this int can take 3 values
						 * 1 = update of existing structure
						 * 2 = add structure where none was before
						 * 3 = delete structure
						 */

						int structure_update_state = 0;
						Statement stmt = conn.createStatement();

						chemical_name = Util.double_q(chemical_name);

						String sql1 = "";
						/* three types of update
						 *1. update of the existing structure
						 *2. add an structure to a compound where none was recorded
						 *3. deletion of structure
						 *4. no update of structure data*/
						if (cd_id == 0 && !molfile.equals("")) // (type 2) There has been added an structure to a compound where there was none before.
						{
							sql1 = "UPDATE compound"+ 
							" SET chemical_name = '"+chemical_name+"',"+
							" cas_number = '"+cas_number+"',"+
							" remark = '"+remark+"',"+
							" density = '"+density+"',"+
							" modified_by = '"+user.toUpperCase()+"',"+
							" cd_id = '"+cd_id_new+"'"+
							" WHERE id = '"+id+"'";

							isStructureUpdate = true;
							structure_update_state = 2;
						}
						else if((cd_id != 0 && !molfile.equals("")) || (cd_id == 0 && molfile.equals("")))// (type 1 and 4)regular update of an existing structure. Or just update to the compound table.
						{
							sql1 = "UPDATE compound"+ 
							" SET chemical_name = '"+chemical_name+"',"+
							" cas_number = '"+cas_number+"',"+
							" remark = '"+remark+"',"+
							" density = '"+density+"',"+
							" modified_by = '"+user.toUpperCase()+"'"+
							" WHERE id = '"+id+"'";
						}
						else if(cd_id != 0 && molfile.equals(""))//delete a structure from a compound.
						{
							sql1 = "UPDATE compound"+ 
							" SET chemical_name = '"+chemical_name+"',"+
							" cas_number = '"+cas_number+"',"+
							" remark = '"+remark+"',"+
							" density = '"+density+"',"+
							" modified_by = '"+user.toUpperCase()+"',"+
							" cd_id = '0'"+
							" WHERE id = '"+id+"'";

							isStructureUpdate = true;
							structure_update_state = 3;
						}

						//we have now performed the update of the compound table, 
						//here after we have to create an entry into the history table
						//describing the new values....
						stmt.executeUpdate(sql1);
						checkOK = true;

						//--------------now perform the insertion into the history table------------------

						boolean u_name = false;
						boolean u_cas = false;
						boolean u_density = false;
						boolean u_remark = false;

						if(!chemical_name.equalsIgnoreCase(o_name))
							u_name = true;

						if(!cas_number.equalsIgnoreCase(o_cas))
							u_cas = true;

						if(density != o_density)
							u_density = true;

						if(!remark.equalsIgnoreCase(o_remark))
							u_remark = true;

						/*
						 * if an update of one of the elements has been performed
						 * create an entry into the history table.
						 */
						if(u_name || u_cas || u_density || u_remark || isStructureUpdate || structure_update_state >= 2)
						{
							/*
							 * Now create the entry in the history table:
							 * The changes is registered in the change_details long_text fields.
							 * a change will have the following syntax
							 * field; old_value; new_value | field; old_value; new_value..... etc
							 * current_quantity; 12.0; 16.5 | unit; g; ml..... etc
							 */                  	

							String change_details = "--";

							if(u_name)
							{							
								if(change_details.equals("--"))
								{
									change_details = "Name; "+Util.double_q(o_name)+"; "+chemical_name;
								}
								else
								{
									change_details = change_details + "| Name; "+Util.double_q(o_name)+"; "+chemical_name;
								}
							}

							if(u_cas)
							{							
								if(change_details.equals("--"))
								{
									change_details = "Cas; "+o_cas+"; "+cas_number;
								}
								else
								{
									change_details = change_details + "| Cas; "+o_cas+"; "+cas_number;
								}
							}

							if(u_density)
							{							
								if(change_details.equals("--"))
								{
									change_details = "Density; "+o_density+"; "+density;
								}
								else
								{
									change_details = change_details + "| Density; "+o_density+"; "+density;
								}
							}

							if(u_remark)
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

							if(isStructureUpdate)
							{										
								/*
								 * a new structure has been added, to a compound where there was none before,
								 * or a compound has been altered.
								 */					

								/*
								 * Get the registered formula and molweight
								 */

								String sql = "SELECT structures.cd_formula, structures.cd_molweight" +
								" FROM compound, structures" +
								" WHERE compound.id = " + id +
								" AND compound.cd_id = structures.cd_id;";

								ResultSet rs = stmt.executeQuery(sql);
								String formula = "--";
								String mw = "--";

								if(rs.next())
								{
									formula = rs.getString("structures.cd_formula");
									mw = rs.getString("structures.cd_molweight");
								}


								if(change_details.equals("--"))
								{
									change_details = "Formula; "+o_formula+"; "+formula;
								}
								else
								{
									change_details = change_details + "| Formula; "+o_formula+"; "+formula;
								}

								if(change_details.equals("--"))
								{
									change_details = "Mw; "+o_mw+"; "+mw;
								}
								else
								{
									change_details = change_details + "| Mw; "+o_mw+"; "+mw;
								}
							}

							//Insert the history element in the table.
							//create the text remark as a standard messege plus any comment entered by the user.
							String text = History.MODIFY;

							if(reason_for_change != null && !reason_for_change.equals(""))
							{
								text = text + "\n" + reason_for_change;
							}

							stmt.executeUpdate(history.insertHistory_string(History.COMPOUND_TABLE, Integer.parseInt(id), this.chemical_name.toUpperCase(), text, user.toUpperCase(), change_details));
						}
					}
					conn.close();
				}
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
	 * Delete a compound.
	 * @param username
	 */
	public int deleteCompound(String username)
	{
		/*
		 * Check if the compound has containers.
		 */
		boolean hasCons = Util.hasCompoundContainers(this.id);
		
		int status = 0;

		if(!hasCons) {
			checkOK = false;
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

						try
						{
							String compound_cd_id = "0";
							//Get the structure id for the compound.
							if(this.id != null && !this.id.equals("0") && !this.id.equals("")) {

								String sql_1 = "SELECT cd_id, chemical_name FROM compound WHERE id ='"+this.id+"'";

								ResultSet rs = stmt.executeQuery(sql_1);

								while(rs.next())
								{
									compound_cd_id = rs.getString("cd_id");
									this.chemical_name = rs.getString("chemical_name");
								}
								rs.close();
							}

							//Delete the compound
							String delsql = "UPDATE compound SET deleted = 1, cd_id = 0 WHERE id = "+this.id+";";

							stmt.addBatch(delsql);

							//Delete the structure
							if(compound_cd_id != null && !compound_cd_id.equals("0") && !compound_cd_id.equals("")) {
								delsql = "DELETE FROM structures WHERE cd_id = "+compound_cd_id+";";

								stmt.addBatch(delsql);
							}

							//update the history for this container, with a deleted in message.
							stmt.addBatch(history.insertHistory_string(History.COMPOUND_TABLE, Integer.parseInt(id), this.chemical_name, History.DELETE_COMPOUND, username.toUpperCase(), "--"));       	

							//fire the update
							stmt.executeBatch();

							//commit the deletion of the container.
							con.commit();
							con.close();

							status = 1000;
							checkOK = true;     	
						}
						catch (Exception e) {
							e.printStackTrace();
							con.rollback();
							con.close();

							status = -200;
							checkOK = false;
						}
					}
				}
			}

			catch (Exception e)
			{
				checkOK = false;
				status = -200;
				e.printStackTrace();
			}
			
			return status;
		}
		else
			return -100;
	}

	public boolean CleanAllStructures() {
		try {
			ConnectionHandler ch = new ConnectionHandler();
			ch.setDriver(Attributes.DB_DRIVER);
			ch.setUrl(Attributes.DB_NAME);
			ch.setLoginName(Attributes.DB_USER);
			ch.setPassword(Attributes.DB_PWD);
			ch.setPropertyTable(Attributes.J_PROP_TABLE);
			ch.connect();


		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * @param o_cas The o_cas to set.
	 */
	public void setO_cas(String o_cas) {
		this.o_cas = o_cas;
	}
	/**
	 * @param o_density The o_density to set.
	 */
	public void setO_density(double o_density) {
		this.o_density = o_density;
	}
	/**
	 * @param o_formula The o_formula to set.
	 */
	public void setO_formula(String o_formula) {
		this.o_formula = o_formula;
	}
	/**
	 * @param o_mw The o_mw to set.
	 */
	public void setO_mw(String o_mw) {
		this.o_mw = o_mw;
	}
	/**
	 * @param o_name The o_name to set.
	 */
	public void setO_name(String o_name) {
		this.o_name = o_name;
	}
	/**
	 * @param o_remark The o_remark to set.
	 */
	public void setO_remark(String o_remark) {
		this.o_remark = o_remark;
	}
	/**
	 * @param reason_for_change The reason_for_change to set.
	 */
	public void setReason_for_change(String reason_for_change) {
		this.reason_for_change = reason_for_change;
	}
}