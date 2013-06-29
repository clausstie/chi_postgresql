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
package chemicalinventory.jReports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.*;
import chemicalinventory.user.UserInfo;

import chemicalinventory.jReports.compile.JPdfCompiler;

import net.sf.jasperreports.engine.JasperRunManager;

/**
 * @author Dann Vestergaard
 */
public class PdfServlet extends HttpServlet
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		ServletContext context = this.getServletConfig().getServletContext();

		/*
		 * The report to create
		 */
		String report_type = request.getParameter("report_type");
		String jasper_name = report_type+".jasper";
		String jrxml_name = report_type+".jrxml";
		
		/*
		 * Get the user name
		 */
		String user_name = request.getRemoteUser();
		
		/*
		 * The connection for the db
		 * and the stream of bytes
		 */
		Connection con = null;
		byte[] bytes = null;
		
		/*
		 * The hashmap holding parameters to pass to the report.
		 */
		UserInfo userinfo = new UserInfo();
		Map parameters = request.getParameterMap();
		Map pdfParameters = new HashMap();
		
		/*
		 * Add some default values to the parameter map
		 */
		getPdfParameters(parameters, pdfParameters);
			
		pdfParameters.put("user_name", userinfo.getFullName(user_name));
		pdfParameters.put("folder_base", context.getRealPath(Attributes.REPORT_FOLDER));
		
		/*
		 * Check that the report file is an existing report in the system.
		 */
		File x_reportFile = new File(context.getRealPath(Attributes.REPORT_FOLDER + jrxml_name));
		if(x_reportFile.exists())
		{
			/*
			 * Check that the .jasper report exist otherwise compile a new version.
			 */
			File reportFile = new File(context.getRealPath(Attributes.REPORT_FOLDER + jasper_name));
			if (!reportFile.exists())
			{			
				/*
				 * Compile the report, there was no compiled report available.
				 */
				JPdfCompiler compile = new JPdfCompiler(context, jrxml_name, jasper_name);
				reportFile = new File(compile.getFile());
			}
			else
			{
				/*
				 * If a .jasper file exist make sure it has the same 
				 * version as the .jrxml template.
				 */
				long date_jasper = reportFile.lastModified();				
				long date_jrxml = x_reportFile.lastModified();
				
				if(date_jasper < date_jrxml)
				{
					/*
					 * The .jasper file is older than
					 * the jrxml, file, now compile a new one. 
					 */
					if(reportFile.delete())
					{
						JPdfCompiler compile = new JPdfCompiler(context, jrxml_name, jasper_name);
						reportFile = new File(compile.getFile());					
					}
				}
			}
									
			try
			{
				/*
				 * Get the connection to the database
				 */
				con = Database.getDBConnection();
				
				/*
				 * Create the bytes for the pdf
				 */
				bytes = JasperRunManager.runReportToPdf(
						reportFile.getPath(), 
						pdfParameters, 
						con);
				
				con.close();
			}
			catch (Exception e)
			{
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<head>");
				out.println("<title>chemicalinventory - Error</title>");
				out.println("</head>");
				
				out.println("<body bgcolor=\"white\">");

				out.println("<span>The following error is reported:</span>");
				out.println("<pre>");

				e.printStackTrace(out);

				out.println("</pre>");

				out.println("</body>");
				out.println("</html>");

				return;
			}
		}
		else
		{
			/*
			 * The requested report does not exist display a warining to the user.
			 */
			createErrorMessage(response, "This report does not exists");
		}
			
		/*
		 * Create the pdf file and send it to the client
		 */
		if (bytes != null && bytes.length > 0)
		{
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(bytes, 0, bytes.length);
			ouputStream.flush();
			ouputStream.close();
		}
		else
		{
			/*
			 * Display error message in html format.
			 */
			createErrorMessage(response, "Could not display report..");
		}
	}
	
	/**
	 * Create the errormessage
	 */
	private void createErrorMessage(HttpServletResponse res, String errorMessage)
	{
		try {
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>chemicalinventory - Fejl</title>");
			out.println("</head>");
			
			out.println("<body bgcolor=\"white\">");

			out.println("<span>"+errorMessage+"</span>");

			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The recived map is cleaned to make sure that only
	 * parameters that is relevant to the report is used.
	 * @param map
	 * @param map2 the map to be returned holding pdf parameters.
	 * @return The cleaned map.
	 */
	private Map getPdfParameters(Map map, Map map2)
	{
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			
			String element = (String) e.getKey();
			
			if(element.indexOf(Attributes.REPORT_PARAM) != -1)
			{
				String[] value = (String[]) e.getValue();
				map2.put(element.substring(7), value[0]);
			}
		}
		
		return map2;
	}
}