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
package chemicalinventory.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import chemicalinventory.context.ConfigurationAttributes;

/**
 * @author Dann Vestergaard
 */
public class ReadConfFile {
	
	private String fileName = null;
	private String data_source = "";
	private String driver_class_name = "";
	private String db_port = "";
	private String db = "";
	private String db_user = "";
	private String db_structure = "";
	private String db_structure_prop = "";
	private String db_structure_user = "";
	private String db_structure_user_password = "";
	private String db_structure_table = "";
    private String ci_mail_box = "";
    private String ci_mail_webmaster = "";
    private String ci_mail_support = "";
    private String ci_smtp_server = "";
    private String use_sample_module = "";
    private String logo = "";
	
	public ReadConfFile(String fileToRead)
	{
		this.fileName = fileToRead;
	}
	
	public ReadConfFile()
	{		
	}

	/**
	 * Read the contents of a given file
	 * @return return the contents represented as a string
	 */
	public boolean fileContent()
	{	
		File inputFile = new File(this.fileName);
						
		try {
			FileReader in = new FileReader(inputFile);
			BufferedReader reader = new BufferedReader(in);
			
			String line = "";
			
			while (line != null)
			{			
				line = reader.readLine();			
				if (line != null && !line.equals("null"))
				{
					StringTokenizer tokens = new StringTokenizer(line, "$");
					while(tokens.hasMoreTokens())
					{
						String token = tokens.nextToken().trim();
						
						if(token.equals(ConfigurationAttributes.DATA_SOURCE))
							this.data_source = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DRIVER_CLASS_NAME))
							this.driver_class_name = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_PORT))
							this.db_port = tokens.nextToken();					
						else if(token.equals(ConfigurationAttributes.DB))
							this.db = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_USER))
							this.db_user = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_STRUCTURE))
							this.db_structure = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_STRUCTURE_USER))
							this.db_structure_user = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_STRUCTURE_USER_PASSWORD))
							this.db_structure_user_password = tokens.nextToken();						
						else if(token.equals(ConfigurationAttributes.DB_STRUCTURE_PROP))
							this.db_structure_prop = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.DB_STRUCTURE_TABLE))
							this.db_structure_table = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.CI_MAIL_BOX))
							this.ci_mail_box = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.CI_MAIL_SUPPORT))
							this.ci_mail_support = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.CI_MAIL_WEBMASTER))
							this.ci_mail_webmaster = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.CI_SMTP_SERVER))
							this.ci_smtp_server = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.USE_SAMPLE_MODULE))
							this.use_sample_module = tokens.nextToken();
						else if(token.equals(ConfigurationAttributes.LOGO))
							this.logo = tokens.nextToken();	
					}
				}
			}
			
			in.close();
			reader.close();
			
			return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return Returns the data_source.
	 */
	public String getData_source() {
		return data_source;
	}
	/**
	 * @return Returns the db_port.
	 */
	public String getDb_port() {
		return db_port;
	}

	/**
	 * @return Returns the db.
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @return Returns the db_structure.
	 */
	public String getDb_structure() {
		return db_structure;
	}

	/**
	 * @return Returns the db_structure_prop.
	 */
	public String getDb_structure_prop() {
		return db_structure_prop;
	}

	/**
	 * @return Returns the db_structure_user.
	 */
	public String getDb_structure_user() {
		return db_structure_user;
	}

	/**
	 * @return Returns the db_user.
	 */
	public String getDb_user() {
		return db_user;
	}

	/**
	 * @return Returns the ci_mail_box.
	 */
	public String getCi_mail_box() {
		return ci_mail_box;
	}

	/**
	 * @return Returns the ci_mail_support.
	 */
	public String getCi_mail_support() {
		return ci_mail_support;
	}

	/**
	 * @return Returns the ci_mail_webmaster.
	 */
	public String getCi_mail_webmaster() {
		return ci_mail_webmaster;
	}

	/**
	 * @return Returns the ci_smtp_server.
	 */
	public String getCi_smtp_server() {
		return ci_smtp_server;
	}

	/**
	 * @return Returns the db_structure_user_password.
	 */
	public String getDb_structure_user_password() {
		return db_structure_user_password;
	}

	/**
	 * @return Returns the driver_class_name.
	 */
	public String getDriver_class_name() {
		return driver_class_name;
	}

	/**
	 * @return Returns the db_structure_table.
	 */
	public String getDb_structure_table() {
		return db_structure_table;
	}

	/**
	 * @return the use_sample_module
	 */
	public String getUse_sample_module() {
		return use_sample_module;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
}