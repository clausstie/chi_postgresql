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
package chemicalinventory.jReports.reportCreation;

import java.util.Iterator;
import java.util.Vector;

import chemicalinventory.jReports.reportCreation.reportObject.ReportObject;
import chemicalinventory.utility.Return_codes;

/**
 * Create the report objects to be displayed to the user. 
 * These information is html ready to be displayed on the page,
 * holding information about all reports in the database, with
 * general values and parameters to pass on to the report.
 * 
 * @author Dann Vestergaard
 *
 */
public class DefToHTML {

	public DefToHTML() {
	}

	private DisplayReportDefinition def = null;
	private ReportObject r_object = null;
	private String object_i = null;
	private Vector list = null;

	/**
	 * Create the html for an the page where all report creation is initiated.
	 * The result is in the local list, and can be obtained using the getter.
	 * 
	 * @return status of the operation.
	 */
	public int createDefToHtml()
	{
		/*
		 * Get the list of reports in the system.
		 */
		def = new DisplayReportDefinition();
		Vector tmp_list = def.listReports_idOnly();
		
		if(tmp_list.size() > 0)
		{
			/*
			 * Create the report object instance
			 */
			r_object = new ReportObject();
			list = new Vector();
			
			for (Iterator iter = tmp_list.iterator(); iter.hasNext();) {
				object_i = null;
				object_i = r_object.createReportObject((String) iter.next());

				if(object_i != null)
					list.add(object_i);
			}			
		}
		
		/*
		 * If the list is filled return ok, other wise empty result.
		 */
		if(this.list != null && this.list.size() > 0)
			return Return_codes.SUCCESS;
		else
			return Return_codes.EMPTY_RESULT;
	}

	/**
	 * @return Returns the list.
	 */
	public Vector getList() {
		return list;
	}	
}