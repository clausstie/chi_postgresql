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
package chemicalinventory.jReports.reportChooser;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import chemicalinventory.context.Attributes;


/**
 * Create the url to activate the PdfServlet, requesting the report.
 * 
 * The url includes the type of report (.jrxml filename of the report)
 * and the list of parameters.
 * 
 * 
 * @author Dann Vestergaard
 *
 */
public class ReportChooser {

	private String report_type = "";

	private StringBuffer url = new StringBuffer();

	/**
	 * Choose the report to create, by creating the url to point at
	 * the opdfservlet.
	 * 
	 * @param request
	 * @param response
	 */
	public ReportChooser(HttpServletRequest request, HttpServletResponse response) {
		
		report_type = request.getParameter("report_type");
		
		/*
		* create the url
		*/
		this.url.append("http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+""+Attributes.APPLICATION+"/servlet/PdfServlet?report_type=");
		this.url.append(report_type);
		
		addParameters(request.getParameterMap());
	}
	
	/**
	 * Add parameters to the url.
	 * @param map
	 */
	private void addParameters(Map map)
	{
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if(element.indexOf(Attributes.REPORT_PARAM) != -1)
			{
				String[] value = (String[]) e.getValue();
				
				this.url.append("&");
				this.url.append(element);
				this.url.append("=");
				this.url.append(value[0]);
			}
		}
	}

	/**
	 * @return Returns the url.
	 */
	public StringBuffer getUrl() {
		return url;
	}
}