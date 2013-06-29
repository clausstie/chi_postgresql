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
import chemaxon.jchem.db.*;
import chemaxon.util.*;

import javax.naming.*;
import javax.sql.*;
import java.net.*;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.history.History;
import chemicalinventory.utility.Util;

/**
 * This bean controls the logic around registerning a new chemical substance.
 * <br>
 */

public class ChemicalRegBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -863024031040407776L;

	/** variable to hold the structure from the drawer * */
	private String molfile = "";

	// variable til registrering af chemical.
	private int cd_id = 0;

	private String id = "";

	private String chemicalName = "";

	private String casNumber = "";

	private double density = 0.00;

	private String remark = "";

	private String registerUser = "";

	private String registerDate = "";

	private String user = "";

	private boolean checkOK;

	private int incKey1 = -1;

	public Vector result = new Vector();

	History history = new History();

	/**
	 * Setter for the id.
	 * 
	 * @param i
	 *            String.
	 */
	public void setId(String i) {
		id = i;
	}

	/**
	 * Getter for the id.
	 * 
	 * @return String id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter for the chemical name.
	 * 
	 * @param cn
	 *            String.
	 */
	public void setChemicalName(String cn) {
		cn.trim();
		chemicalName = cn.toUpperCase();
	}

	/**
	 * Getter for the chemicl name.
	 * 
	 * @return String chemical name.
	 */
	public String getChemicalName() {
		return chemicalName;
	}

	/**
	 * Setter for the cas number.
	 * 
	 * @param casn
	 *            String cas number.
	 */
	public void setCasNumber(String casn) {
		casNumber = casn.trim();
	}

	/**
	 * Getter for the cas number.
	 * 
	 * @return String cas number.
	 */
	public String getCasNumber() {
		return casNumber;
	}

	/**
	 * Setter for the density.
	 * 
	 * @param dt
	 *            double.
	 */
	public void setDensity(double dt) {
		density = dt;
	}

	/**
	 * Getter for the density.
	 * 
	 * @return double density.
	 */
	public double getDensity() {
		return density;
	}

	/**
	 * Setter for a remark.
	 * 
	 * @param rm
	 *            String.
	 */
	public void setRemark(String rm) {
		remark = rm.trim();
	}

	/**
	 * Getter for a remark.
	 * 
	 * @return String.
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Setter for the user.
	 * 
	 * @param ru
	 *            String.
	 */
	public void setRegisterUser(String ru) {
		registerUser = ru;
	}

	/**
	 * Getter for the user registering.
	 * 
	 * @return String.
	 */
	public String getRegisterUser() {
		return registerUser;
	}

	/**
	 * Setter for the date registering a container.
	 * 
	 * @param rd
	 *            String.
	 */
	public void setRegisterDate(String rd) {
		registerDate = rd;
	}

	/**
	 * Getter for the date registering a container.
	 * 
	 * @return String date.
	 */
	public String getRegisterDate() {
		return registerDate;
	}

	/**
	 * Set the molfile received from the applet. Check if a molfile is recieved
	 * that is empty.
	 * 
	 * @param mf
	 *            String.
	 */
	public void setmolfile(String mf) {
		molfile = mf;
		// This string is the value returned by marvin in case there is no
		// structure
		// drawn in the applet on the search page.
		String molchecker = "0  0  0  0  0  0            999 V2000";

		if (mf.substring(40, 77).equals(molchecker)) {
			molfile = "";
		}
	}

	/**
	 * Getter for the molfile.
	 * 
	 * @return String.
	 */
	public String getmolfile() {
		return molfile;
	}

	/**
	 * If a valid chemical name is entered, proceed to the registration.
	 * 
	 * @return boolean.
	 */
	public boolean regOK() {
		if (chemicalName.equals("")) {
			checkOK = false;
			return checkOK;
		} else {
			checkChemicalRegistration();

			/*
			 * If the check was succesfull create a history element in the db.
			 */
			if (checkOK) {
				initialCompoundHistoryInfo(id);
			}

			return checkOK;
		}
	}

	/**
	 * The new chemical substance is registered in the db. This is a process of
	 * first registering the chemical structure, and afterwards registering the
	 * other values.
	 */
	public void checkChemicalRegistration() {
		checkOK = false;

		/*
		 * Make sure the cas number is not allready registered.
		 */
		if (Util.isValueEmpty(casNumber)) {
			if (isCasNumberUsed(casNumber, 0)) {
				checkOK = false;

				return;
			}
		}

		/*
		 * Start registration of the new compound.
		 */
		try {
			if (molfile != null && !molfile.equals("")) {
				ConnectionHandler ch = new ConnectionHandler();
				ch.setDriver(Attributes.DB_DRIVER);
				ch.setUrl(Attributes.DB_NAME+"?autoReconnect=true");
				ch.setLoginName(Attributes.DB_USER);
				ch.setPassword(Attributes.DB_PWD);
				ch.setPropertyTable(Attributes.J_PROP_TABLE);

				ch.connect();

				// Checking if the structure already exists in the
				// structure table
				JChemSearch searcher = new JChemSearch();
				searcher.setConnectionHandler(ch);
				searcher.setQueryStructure(molfile);
				searcher.setSearchType(JChemSearch.PERFECT);
				searcher.setStructureTable(Attributes.DB_STUCTURE_TABLE);
				searcher.setWaitingForResult(true);

				searcher.run();
				int foundItemsCount = searcher.getResultCount();

				if (foundItemsCount > 0)
					throw new Exception("Structure already exists (cd_id="
							+ searcher.getResult(0) + ")");

				// Updating the structure table
				boolean isInsertion = true;
				UpdateHandler uh = new UpdateHandler(ch,
						(isInsertion ? UpdateHandler.INSERT
								: UpdateHandler.UPDATE),
						Attributes.DB_STUCTURE_TABLE, "");
				try {
					uh.setValuesForFixColumns(molfile);
					uh.execute();

					// getting the id from the newly registered structure
					// JChemSearch searcher = new JChemSearch();
					searcher.setConnectionHandler(ch);
					searcher.setQueryStructure(molfile);
					searcher.setSearchType(JChemSearch.PERFECT);
					searcher.setStructureTable(Attributes.DB_STUCTURE_TABLE);
					searcher.setWaitingForResult(true);
					searcher.run();
					cd_id = searcher.getResult(0);
				} finally {
					uh.close();
				}
			}

			// ---------------- Registration of data in the compound table
			// ----------------//
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					chemicalName = Util.double_q(chemicalName);
					remark = Util.double_q(remark);

					String sql1 = "INSERT INTO compound (cd_id, chemical_name, cas_number, remark, density,"
							+ " register_by, register_date, modified_by)"
							+ " VALUES("
							+ cd_id
							+ ",'"
							+ chemicalName
							+ "', '"
							+ casNumber
							+ "', '"
							+ remark
							+ "',"
							+ " "
							+ density
							+ ", '"
							+ registerUser
							+ "', '"
							+ registerDate + "', '" + registerUser + "')";

					stmt.executeUpdate(sql1, Statement.RETURN_GENERATED_KEYS);

					ResultSet key1 = stmt.getGeneratedKeys();

					if (key1.next()) {
						incKey1 = key1.getInt(1);
						id = "" + incKey1;
					}
					key1.close();

					stmt.close();
					checkOK = true;
				}
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			checkOK = false;
		} catch (Exception e) {
			e.printStackTrace();
			checkOK = false;
		}
	}

	/**
	 * Create the data neede to print out a receipt after succesfull
	 * registration of a new compound.
	 * 
	 * @param id
	 *            String.
	 */
	public void compoundReceipt(String id) {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					String receipt = "SELECT chemical_name, cas_number, density, register_by,"
							+ " register_date, remark FROM compound"
							+ " WHERE id = " + id + "";

					ResultSet rs = stmt.executeQuery(receipt);

					if (rs.next()) {
						String name = rs.getString("chemical_name");
						String reg_date = rs.getString("register_date");
						String cas = rs.getString("cas_number");
						String note = rs.getString("remark");
						String reg_by = rs.getString("register_by");
						String dens = rs.getString("density");

						if (name == null || name.equals("")) {
							name = "-";
						} else {
							name = URLEncoder.encode(name, "UTF-8");
							chemicalName = name;
						}
						if (cas == null || cas.equals("")) {
							cas = "-";
						}
						if (reg_by == null || reg_by.equals("")) {
							reg_by = "-";
						}
						if (reg_date == null || reg_date.equals("")) {
							reg_date = "-";
						}
						if (dens == null || dens.equals("")) {
							dens = "-";
						}
						if (note == null || note.equals("")) {
							note = "-";
						} else {
							note = URLEncoder.encode(note, "UTF-8");
						}

						result.clear();
						result.addElement(name + "|" + cas + "|" + note + "|"
								+ dens + "|" + reg_date + "|"
								+ reg_by.toUpperCase());
					}
				}
				con.close();
			}
		}// end of try

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check the compounds in the db if the cas_number is registered, return
	 * true.
	 * 
	 * @param cas_number
	 *            value to find.
	 * @return true if and only if the cas_number is allready registered.
	 */
	public boolean isCasNumberUsed(String cas_number, int compoundID) {

		Connection con = Database.getDBConnection();

		try {

			if (con != null) {
				String sql = "SELECT compound.cas_number" + " FROM compound"
						+ " WHERE compound.cas_number = '" + cas_number + "'";
				
				/*
				 * check that the cas is not registered on any
				 * other compound than the received compoundid.
				 */
				if(compoundID > 0)
					sql = sql +" AND compound.id != "+compoundID+";";
				else
					sql = sql +";";
								
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(sql);

				if (rs.next()) {
					con.close();
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Register the initial history upon creation of a new compound
	 * 
	 * @param id
	 */
	public void initialCompoundHistoryInfo(String id) {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					String receipt = "SELECT compound.chemical_name, compound.cas_number,"
							+ " compound.density, compound.register_by, compound.register_date,"
							+ " compound.remark, structures.cd_formula, structures.cd_molweight"
							+ // , structures.cd_structure" +
							" FROM compound"
							+ " LEFT JOIN structures"
							+ " ON compound.cd_id = structures.cd_id"
							+ " WHERE compound.id = " + id + "";

					ResultSet rs = stmt.executeQuery(receipt);

					if (rs.next()) {
						String name = Util.encodeNullValue(rs
								.getString("compound.chemical_name"));
						name = Util.double_q(name);
						// String reg_date =
						// rs.getString("compound.register_date");
						String cas = Util.encodeNullValue(rs
								.getString("compound.cas_number"));
						String note = Util.encodeNullValue(rs
								.getString("compound.remark"));
						note = Util.double_q(note);
						// String reg_by = rs.getString("compound.register_by");
						String dens = Util.encodeNullValue(rs
								.getString("compound.density"));
						// Blob structure =
						// rs.getBlob("structures.cd_structure");
						String formula = Util.encodeNullValue(rs
								.getString("structures.cd_formula"));
						String mw = Util.encodeNullValue(rs
								.getString("structures.cd_molweight"));

						// Create the details about the compound
						String change_details = "--";

						change_details = "Chemical name; --; " + name + "|\n";
						change_details = change_details + "Cas No.; --; " + cas
								+ "|\n";
						change_details = change_details + "Density; --; "
								+ dens + "|\n";
						change_details = change_details + "Formula; --; "
								+ formula + "|\n";
						change_details = change_details + "Mw; --; " + mw
								+ "\n";
						change_details = change_details + "Remark; --; " + note
								+ "\n";

						// Create the entire sql for inserting a line in the
						// history table
						// this does not include the blob for the structure..
						String insert_history = history.insertHistory_string(
								History.COMPOUND_TABLE, Integer.parseInt(id),
								name, History.CREATE_COMPOUND, user
										.toUpperCase(), change_details);

						stmt.executeUpdate(insert_history);
						/*
						 * This is excluded, because saving the structure will
						 * be information overflow...
						 */
						// int history_key = -1;
						// 						
						// //send it to the db
						// stmt.executeUpdate(insert_history,
						// Statement.RETURN_GENERATED_KEYS);
						// 	 					
						// ResultSet history = stmt.getGeneratedKeys();
						// 	 					 	 					
						// if (history.next() && structure != null)
						// {
						// //the id for the history line
						// history_key = history.getInt(1);
						// 	 						
						// String update = "UPDATE history" +
						// " SET history.structure = (SELECT
						// structures.cd_structure FROM compound LEFT JOIN
						// structures ON compound.cd_id = structures.cd_id WHERE
						// compound.id = "+id+")"+
						// " WHERE history.id = "+history_key+";";
						// 	 						
						// stmt.executeUpdate(update);
						// }
					}
				}
				con.close();
			}
		}// end of try

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
}