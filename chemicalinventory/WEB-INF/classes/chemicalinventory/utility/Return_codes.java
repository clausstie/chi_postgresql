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
package chemicalinventory.utility;

public class Return_codes {
	
	public static final int SUCCESS = 1000;
	
	public static final int CREATION_FAILED = 99;
	public static final int MISSING_NAME = 98;
	public static final int EMPTY_RESULT = 98;
	public static final int SQL_ERROR = 97;
	public static final int GENERAL_ERROR = 96;
	public static final int CONNECTION_ERROR = 95;
	
	public static final int WRONG_FORMAT = 94;
	public static final int VALUE_TOO_HIGH = 93;
	public static final int EMPTY_SEARCH = 92;
	public static final int VALUE_TOO_LOW = 91;
	public static final int ILLEGAL_DATE_FORMAT = 90;
	public static final int MISSING_TITLE = 89;
	public static final int MISSING_ID = 88;
	public static final int TABLE_CREATION_ERROR = 87;
	public static final int TABLE_MISSING_DATA = 86;
	public static final int MISSING_PRIVILEGES = 85;
	public static final int UPDATE_ERROR = 84;
	public static final int USER_TYPE_ERROR = 83;
	public static final int CUSTOM_ID_NOT_UNIQUE = 82;
}
