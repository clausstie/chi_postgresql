/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2003-2006.
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
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package chemicalinventory.utility.registration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;

public class RegistrationHandler {

	private String organisation_name;
	private String organisation_department;
	private String address1;
	private String address2;
	private String zip;
	private String city;
	private String country;
	private String state;
	private String contact_person;
	private String contact_email;
	private String contact_telephone;
	private String remark;
	private String no_users;
	private String contact;	

	/**
	 * Perform registration
	 * @return true registration performed, and db updated. else false.
	 */
	public boolean performRegistration(){

		if(getOrganisation_name() != null || !getOrganisation_name().equals("")) {

			try {
				String response = "";
				URL url = new URL("http://www.ci.dfuni.dk/ci_registration/servlet/Registration?");
				URLConnection conn = url.openConnection();
//				Set connection parameters.
				conn.setDoInput (true);
				conn.setDoOutput (true);
				conn.setUseCaches (false);
//				Make server believe we are form data...
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				DataOutputStream out = new DataOutputStream (conn.getOutputStream ());
//				Write out the bytes of the content string to the stream.

				if(organisation_department == null || organisation_department.equals(""))
					organisation_department  ="--";
				if(address1 == null || address1.equals(""))
					address1  ="--";
				if(address2 == null || address2.equals(""))
					address2  ="--";
				if(zip == null || zip.equals(""))
					zip  = "0000";
				if(city == null || city.equals(""))
					city  ="--";
				if(country == null || country.equals(""))
					country  ="--";
				if(state == null || state.equals(""))
					state  ="--";
				if(contact_person == null || contact_person.equals(""))
					contact_person  ="--";
				if(contact_email == null || contact_email.equals(""))
					contact_email  ="--";
				if(contact_telephone == null || contact_telephone.equals(""))
					contact_telephone  ="--";
				if(remark == null || remark.equals(""))
					remark  ="--";
				if(no_users == null || no_users.equals(""))
					no_users  ="--";
				if(contact == null || contact.equals(""))
					contact  ="--";

				StringBuilder parameterBuilder = new StringBuilder();
				parameterBuilder.append("organisation_name=" + URLEncoder.encode(getOrganisation_name(), "UTF-8"));
				parameterBuilder.append("&organisation_department=" + URLEncoder.encode(getOrganisation_department(), "UTF-8"));
				parameterBuilder.append("&address1=" + URLEncoder.encode(getAddress1(), "UTF-8"));
				parameterBuilder.append("&address2=" + URLEncoder.encode(getAddress2(), "UTF-8"));
				parameterBuilder.append("&city=" + URLEncoder.encode(getCity(), "UTF-8"));
				parameterBuilder.append("&contact=" + URLEncoder.encode(getContact(), "UTF-8"));
				parameterBuilder.append("&contact_email=" + URLEncoder.encode(getContact_email(), "UTF-8"));
				parameterBuilder.append("&contact_person=" + URLEncoder.encode(getContact_person(), "UTF-8"));
				parameterBuilder.append("&contact_telephone=" + URLEncoder.encode(getContact_telephone(), "UTF-8"));
				parameterBuilder.append("&country=" + URLEncoder.encode(getCountry(), "UTF-8"));
				parameterBuilder.append("&no_users=" + URLEncoder.encode(getNo_users(), "UTF-8"));
				parameterBuilder.append("&remark=" + URLEncoder.encode(getRemark(), "UTF-8"));
				parameterBuilder.append("&state=" + URLEncoder.encode(getState(), "UTF-8"));
				parameterBuilder.append("&zip=" + URLEncoder.encode(getZip(), "UTF-8"));
				parameterBuilder.append("&ci_version=" + URLEncoder.encode(Attributes.CI_VERSION, "UTF-8"));

				out.writeBytes(parameterBuilder.toString());
				out.flush ();
				out.close ();
//				Read response from the input stream.
				BufferedReader in = new BufferedReader (new InputStreamReader(conn.getInputStream ()));
				String temp;
				while ((temp = in.readLine()) != null){
					response += temp;;
				}
				temp = null;
				in.close ();

				if(response.equals("OK")) {
					/*
					 * Registration received, change the db for this instance.
					 */
					if(setInstanceToRegistered())
						return true;
					else
						return false;
				}
				else {
					/*
					 * Registration failed
					 */
					return false;
				}
			} 
			catch (ConnectException ce){
				ce.printStackTrace();
				return false;
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
	}

	/**
	 * Set this instance as registered.
	 * @return
	 */
	public boolean setInstanceToRegistered() {
		try {
			Connection con = Database.getDBConnection();

			if(con != null) {
				String sql = "UPDATE ci_configuration SET reg_value = '1' WHERE reg_key = 'isRegistered';";
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
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
	 * Is this instance of ci registered...??
	 * @return true = the instance is registered, False = not yet registered, please do so.
	 */
	public static boolean isInstanceRegistered() {
		try {
			Connection con = Database.getDBConnection();

			if(con != null) {

				String sql = "SELECT reg_value FROM ci_configuration WHERE reg_key = 'isRegistered';";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				if(rs.next()) {

					if(rs.getString("reg_value").equals("1"))
					{
						con.close();
						return true;
					}
					else
					{
						con.close();
						return false;
					}
				}
				else {
					con.close();
					return false;					
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the contact_email
	 */
	public String getContact_email() {
		return contact_email;
	}

	/**
	 * @param contact_email the contact_email to set
	 */
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	/**
	 * @return the contact_person
	 */
	public String getContact_person() {
		return contact_person;
	}

	/**
	 * @param contact_person the contact_person to set
	 */
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	/**
	 * @return the contact_telephone
	 */
	public String getContact_telephone() {
		return contact_telephone;
	}

	/**
	 * @param contact_telephone the contact_telephone to set
	 */
	public void setContact_telephone(String contact_telephone) {
		this.contact_telephone = contact_telephone;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the no_users
	 */
	public String getNo_users() {
		return no_users;
	}

	/**
	 * @param no_users the no_users to set
	 */
	public void setNo_users(String no_users) {
		this.no_users = no_users;
	}

	/**
	 * @return the organisation_department
	 */
	public String getOrganisation_department() {
		return organisation_department;
	}

	/**
	 * @param organisation_department the organisation_department to set
	 */
	public void setOrganisation_department(String organisation_department) {
		this.organisation_department = organisation_department;
	}

	/**
	 * @return the organisation_name
	 */
	public String getOrganisation_name() {
		return organisation_name;
	}

	/**
	 * @param organisation_name the organisation_name to set
	 */
	public void setOrganisation_name(String organisation_name) {
		this.organisation_name = organisation_name;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

}
