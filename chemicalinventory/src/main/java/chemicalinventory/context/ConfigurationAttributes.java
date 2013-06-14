/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kalles�e 2004-2007.
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
package chemicalinventory.context;

/**
 * File attributes to read from the configuration file.
 */
public class ConfigurationAttributes {
	
	/*
	 * Static values used to read configuration 
	 * parameters from the file.
	 */
	public static final String DATA_SOURCE = "DATA_SOURCE";
	public static final String DRIVER_CLASS_NAME = "DRIVER_CLASS_NAME";
	public static final String DB_PORT = "DB_PORT";
	
	public static final String DB = "DB";
	public static final String DB_USER = "DB_USER";
	public static final String DB_STRUCTURE = "DB_STRUCTURE";
	public static final String DB_STRUCTURE_PROP = "DB_STRUCTURE_PROP";
	public static final String DB_STRUCTURE_USER = "DB_STRUCTURE_USER";
	public static final String DB_STRUCTURE_TABLE = "DB_STRUCTURE_TABLE";
	public static final String DB_STRUCTURE_USER_PASSWORD = "DB_STRUCTURE_USER_PASSWORD";
    public static final String CI_MAIL_BOX = "CI_MAIL_BOX";
    public static final String CI_MAIL_WEBMASTER = "CI_MAIL_WEBMASTER";
    public static final String CI_MAIL_SUPPORT = "CI_MAIL_SUPPORT";
    public static final String CI_SMTP_SERVER = "CI_SMTP_SERVER";
    
    //DB configuration parameters
    public static final String USECUSTOMCONTAINERID = "useCustomContainerId";
    public static final String LABELTEMPLATE = "labelTemplate";
    public static final String LABELTEMPLATEPATH = "labelTemplatePath";
    
    public static final String USE_SAMPLE_MODULE = "USE_SAMPLE_MODULE";
    public static final String LOGO = "LOGO";
}