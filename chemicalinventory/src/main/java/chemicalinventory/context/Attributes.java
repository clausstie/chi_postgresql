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
package chemicalinventory.context;

public class Attributes {
	
	/*
	 * Read all the configuration parameters from the configuration file...
	 */
	static Configuration conf = Configuration.getInstance();
			
	//ip address of the server:
	public static final String IP_ADDRESS = conf.getServerIP();
	
	//port on the server
	public static final String PORT = conf.getServerPort();
	
	//mysql server port
	public static final String DB_PORT = conf.getDb_port();

	//mysql database server to use
	public static final String DATABASE = conf.getDb();
	
	//Application name
	public static final String APPLICATION = conf.getApplicationName();
	
	//Qualified application name
	public static final String QUALIFIED_APPLICATIOM = conf.getQualified_application();
	
	//Date source for the connection pool
	//The connection pool is configured in the Server.xml on the Server.
	public static final String DATA_SOURCE = conf.getData_source();
	
	//Versin of this deployment
	public static final String CI_VERSION = "20081010";
	
	//Use custom created ids for containers
	public static boolean USE_CUSTOM_ID = conf.isUse_custom_id();
	
	//The label template to use
	public static String LABEL_TEMPLATE = conf.getLabel_template();
		
	//The path to label templates
	public static String LABEL_TEMPLATE_PATH = conf.getLabel_template_path(); 
	
	//Use Sample Module
	public static boolean USE_SAMPLE_MODULE = conf.isUse_sample_module();
	
	//The logo to use for the installation.
	public static String LOGO = conf.getLogo();
	
	//Context attributes
	public static final String JSP_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/jsp/Controller.jsp";
	public static final String CONTAINER_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/container/containerController.jsp";
	public static final String ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/adminController.jsp";
	public static final String LOCATION_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/location/locationController.jsp";
	public static final String USER_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/user/userController.jsp";
	public static final String ANALYSIS_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/analysis/analysisController.jsp";
	public static final String UNIT_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/unit/unitController.jsp";
	public static final String GROUP_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/group/groupController.jsp";
	public static final String SUPPLIER_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/supplier/supplierController.jsp";
	public static final String COMPOUND_ADMINISTRATOR_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/administration/compound/compoundController.jsp";
	public static final String SAMPLEAPPROVER_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/sampleApproval/sampController.jsp";
	public static final String SAMPLE_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/sample/sampleController.jsp";
	public static final String HISTORY_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/history/historyController.jsp";
	public static final String RESULT_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/result/resultController.jsp";
	public static final String BATCH_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/batch/batchController.jsp";
	public static final String D_BATCH_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/batch_display/d_batchController.jsp";
	public static final String PRINT_BASE = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/print/Print_Controller.jsp";
	public static final String PRINT_ADMINISTRATION = "http://"+IP_ADDRESS+":"+PORT+APPLICATION+"/print/print_administration/Print_Admin_Controller.jsp";

	//Database definition for structure access
    //should be changed so type of database isnt hardcoded here. for now just added a line for postgresql
    //public static final String DB_NAME = "jdbc:mysql://"+IP_ADDRESS+":"+DB_PORT+"/"+conf.getDb();//database to use
    public static final String DB_NAME = "jdbc:postgresql://"+IP_ADDRESS+":"+DB_PORT+"/"+conf.getDb();//database to use
    public static final String DB_USER = conf.getDb_structure_user(); // db user to use
    public static final String DB_PWD = conf.getDb_structure_user_password(); //password for the db user
    public static final String DB_STUCTURE_TABLE = conf.getDb_structure_table();//table for structures.
    public static final String DB_DRIVER = conf.getDriver_class_name(); //the jdbc driver
    public static final String J_PROP_TABLE = conf.getDb_structure_prop(); // Properties table for JChem
    
    //Parameters for mail setup
    public static final String CI_MAIL_BOX = conf.getCi_mail_box();
    public static final String CI_MAIL_WEBMASTER = conf.getCi_mail_webmaster();
    public static final String CI_MAIL_SUPPORT = conf.getCi_mail_support();
    public static final String CI_SMTP_SERVER = conf.getCi_smtp_server();
    
    //definition for image location
    public static final String IMAGE_REPORT = conf.getImage_report_location();  
    public static final String IMAGE = conf.getImage_icons_location(); 
    public static final String IMAGE_DISABLED = conf.getImage_disabled_location(); 
    
    //definitions used for report generation using jasperreports
	/*
	 * report folder - folder where the report templates are placed.
	 */
	public static final String REPORT_FOLDER = "/reports/";
	
	/*
	 * Report parameter first name (must be 7 characters)
	 */
	public static final String REPORT_PARAM = "CIParam";
    	
	/*
	 * Folder for the stylesheets
	 */
	public static String STYLE_SHEET_FOLDER = conf.getStyle_sheet_folder(); 

	/*
 	 * Folder for the stylesheets with the complete path
	 */
	public static String STYLE_SHEET_FOLDER_REALPATH = conf.getStyle_sheet_folder_realpath(); 
	
	/*
	 * Folder for the marvin files
	 */
	public static String MARVIN_FOLDER = conf.getMarvin_folder(); 

	/*
 	 * Folder for the marvin js file
	 */
	public static String MARVIN_JS_FILE = conf.getMarvin_js_file(); 
	
	/*
	 * Folder for the images in the system.
	 */
	public static final String IMAGE_FOLDER = conf.getImage_folder(); 
	
	/*
	 * Pages in the for the administraion controller/part.
	 */
	public static final String REGISTER_CONTAINER = "newContainer_reg";
	public static final String MODIFY_COMPOUND = "Chemical";
		
	/*
	 * Pages in the jsp/normal part
	 */
	public static final String COMPOUND_SAMPLE_LIST = "display_sample_compound";
	
	/*
	 * Pages in the sample part
	 */
	public static final String CREATE_COMPOUND_SAMPLE = "create_sample";
	
	/*
	 * History attributes
	 */
	public static final String COMPOUND_HISTORY = "compound_history";
	
	/**
	 * Using the attribute for internal resource get the base element that matches
	 */
	public static String getBaseForResource(String resource)
	{
		String base = "";
		
		if(resource.equals(Attributes.REGISTER_CONTAINER) ||
		   resource.equals(Attributes.MODIFY_COMPOUND))
			base = Attributes.CONTAINER_ADMINISTRATOR_BASE;
		
		if(resource.equals(Attributes.MODIFY_COMPOUND))
			base = Attributes.COMPOUND_ADMINISTRATOR_BASE;
		
				
		if (resource.equals(Attributes.CREATE_COMPOUND_SAMPLE))
			base = Attributes.SAMPLE_BASE;

		if(resource.equals(Attributes.COMPOUND_HISTORY))
			base = Attributes.HISTORY_BASE;
		
		return base;
	}
	
	
	/* **************************************************************************************
	 * ************ Attributes used as include for the creation of internal resources. ******
	 * **************************************************************************************/
	public static final String COMPOUND_NAME = "chemical_name";
	public static final String COMPOUND_ID = "compound_id";
	public static final String COMPOUND_CAS = "cas_number";	
}
