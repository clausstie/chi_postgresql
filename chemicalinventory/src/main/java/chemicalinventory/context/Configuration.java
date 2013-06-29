/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesøe 2003-2006.
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
 *
 */
package chemicalinventory.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import chemicalinventory.file.ReadConfFile;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

/**
 * @author Dann Vestergaard
 */
public class Configuration {

	private ReadConfFile reader;
	private static Configuration instance = null;

	private String serverIP = "";
	private String serverPort = "";
	private String applicationName = "";
	private String data_source = "";
	private String db_port = "";
	private String driver_class_name = "";

	private String db = "";
	private String db_user = "";
	private String db_structure = "";
	private String db_structure_prop = "";
	private String db_structure_user = "";
	private String db_structure_user_password = "";
	private String db_structure_table = "";

	private String image_report_location = "";
	private String image_icons_location = "";
	private String image_disabled_location = "";
	private String image_folder = "";
	private String style_sheet_folder = "";
	private String style_sheet_folder_realpath = "";
	private String marvin_folder = "";
	private String marvin_js_file = "";
	private String qualified_application ="";

	private String ci_mail_box = "";
	private String ci_mail_webmaster = "";
	private String ci_mail_support = "";
	private String ci_smtp_server = "";

	private boolean status = false;
	private boolean use_custom_id = false;	

	private String label_template = "";
	private String label_template_path = "";
	private boolean use_sample_module = false;
	
	private String logo = "";

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
	 * Read all the configuration values and put them into
	 * the private variables, to be fetched using getters..
	 */
	private Configuration(String confFileLocation)
	{
		reader = new ReadConfFile();

		try {
			reader.setFileName(confFileLocation);
			status = reader.fileContent();

			if(status)
			{
				/*
				 * read all the attributes needed for this application..
				 */

				//data source
				this.data_source = reader.getData_source();

				//database
				this.db = reader.getDb();

				//jdbc driver class name
				this.driver_class_name = reader.getDriver_class_name();

				//database user
				this.db_user = reader.getDb_user();

				//struckture database
				this.db_structure = reader.getDb_structure();

				//structure db user
				this.db_structure_user = reader.getDb_structure_user();

				//structure db user password
				this.db_structure_user_password = reader.getDb_structure_user_password();

				//structure properties table
				this.db_structure_prop = reader.getDb_structure_prop();

				//structure table
				this.db_structure_table = reader.getDb_structure_table();

				//db port
				this.db_port = reader.getDb_port();

				//ci mail box
				this.ci_mail_box = reader.getCi_mail_box();

				//ci support mail address
				this.ci_mail_support = reader.getCi_mail_support();

				//ci webmaster mail address
				this.ci_mail_webmaster = reader.getCi_mail_webmaster();

				//ci smtp server
				this.ci_smtp_server = reader.getCi_smtp_server();
								
				try {
					//Use sample module
					this.use_sample_module = Boolean.parseBoolean(reader.getUse_sample_module());
				} catch (Exception e) {
					this.use_sample_module = false;
				}
				
				try {
					//Get the logo name
					this.logo = reader.getLogo();
				} catch (Exception e) {
					this.logo = "";
				}
				
				//Read configuration from database
				readDBConfiguration();

				this.status = true;				
			}
			else
				this.status = false;
		}
		catch (Exception e) {
			this.status = false;
			e.printStackTrace();
		}
	}

	/**
	 * Create a new instance of the configuration object
	 * but only if none exists.
	 * @return
	 */
	public synchronized static Configuration getInstance()
	{
		if(instance == null)
		{
			instance = new Configuration(null);
		}
		return instance;
	}


	/**
	 * Create a new instance of the configuration object, using attributes from the 
	 * servlet request object.
	 * @param request
	 * @param context
	 */
	public static void initInstance(HttpServletRequest request, ServletContext context)
	{
		/*
		 * Read the configuration file..
		 * This is done each time this method is called, to avoid, obsolete
		 * instances of this object to interfer.
		 */
		instance = new Configuration(context.getRealPath("/WEB-INF/configuration/CIConfiguration.txt"));

		instance.serverIP = request.getServerName();
		instance.serverPort = String.valueOf(request.getServerPort());
		instance.applicationName = request.getContextPath();

		instance.image_report_location = instance.getApplicationName()+"/temp";
		instance.image_folder = instance.getApplicationName()+"/images";
		instance.style_sheet_folder = instance.getApplicationName()+"/styles";
		instance.style_sheet_folder = instance.getApplicationName()+"/styles";
		instance.style_sheet_folder_realpath = context.getRealPath("/styles");
		instance.marvin_folder = instance.getApplicationName()+"/script/marvin/";
		instance.marvin_js_file = instance.getApplicationName()+"/script/marvin/marvin.js";

		instance.image_icons_location = context.getRealPath("/images/icons")+"/";
		instance.image_disabled_location = context.getRealPath("/images/icons/disabled/")+"/";
		instance.qualified_application = context.getRealPath("");
	}


	/**
	 * Read configuration parametersfrom the database.
	 */
	private void readDBConfiguration() {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(this.data_source);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con!= null) {
					Statement stmt = con.createStatement();
					String sql = "SELECT reg_key, reg_value FROM ci_configuration;";
					ResultSet rs = stmt.executeQuery(sql);

					while(rs.next())
					{
						String reg_value = rs.getString("reg_value");
						String reg_key = rs.getString("reg_key");

						if(reg_key.equals(ConfigurationAttributes.USECUSTOMCONTAINERID)) {

							if(reg_value.equals("1"))
								this.use_custom_id = true;
							else
								this.use_custom_id = false;
						}
						else if(reg_key.equals(ConfigurationAttributes.LABELTEMPLATE)) {
							this.label_template = reg_value;
						}
						else if(reg_key.equals(ConfigurationAttributes.LABELTEMPLATEPATH)) {
							this.label_template_path = reg_value;
						}
					}
				}
				con.close();
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
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
	 * @return Returns the image_disabled_location.
	 */
	public String getImage_disabled_location() {
		return image_disabled_location;
	}
	/**
	 * @return Returns the image_icons_location.
	 */
	public String getImage_icons_location() {
		return image_icons_location;
	}
	/**
	 * @return Returns the image_report_location.
	 */
	public String getImage_report_location() {
		return image_report_location;
	}
	/**
	 * @return Returns the serverIP.
	 */
	public String getServerIP() {
		return serverIP;
	}
	/**
	 * @return Returns the serverPort.
	 */
	public String getServerPort() {
		return serverPort;
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
	 * @return Returns the status.
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @return Returns the image_folder.
	 */
	public String getImage_folder() {
		return image_folder;
	}

	/**
	 * @param image_folder The image_folder to set.
	 */
	public void setImage_folder(String image_folder) {
		this.image_folder = image_folder;
	}

	/**
	 * @return Returns the style_sheet_folder.
	 */
	public String getStyle_sheet_folder() {
		return style_sheet_folder;
	}

	/**
	 * @return Returns the style_sheet_folder_realpath.
	 */
	public String getStyle_sheet_folder_realpath() {
		return style_sheet_folder_realpath;
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
	 * @return Returns the marvin_folder.
	 */
	public String getMarvin_folder() {
		return marvin_folder;
	}

	/**
	 * @return Returns the marvin_js_file.
	 */
	public String getMarvin_js_file() {
		return marvin_js_file;
	}

	/**
	 * @return the qualified_application
	 */
	public String getQualified_application() {
		return qualified_application;
	}

	/**
	 * @return the use_custom_id
	 */
	public boolean isUse_custom_id() {
		return use_custom_id;
	}

	/**
	 * @param use_custom_id the use_custom_id to set
	 */
	public void setUse_custom_id(boolean use_custom_id) {
		this.use_custom_id = use_custom_id;
	}

	/**
	 * @return the label_template
	 */
	public String getLabel_template() {
		return label_template;
	}

	/**
	 * @return the label_template_path
	 */
	public String getLabel_template_path() {
		return label_template_path;
	}

	/**
	 * @param use_sample_module the use_sample_module to set
	 */
	public void setUse_sample_module(boolean use_sample_module) {
		this.use_sample_module = use_sample_module;
	}

	/**
	 * @return the use_sample_module
	 */
	public boolean isUse_sample_module() {
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
