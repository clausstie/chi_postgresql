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
package chemicalinventory.jReports.compile;

import java.io.IOException;

import javax.servlet.ServletContext;

import chemicalinventory.context.Attributes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * 
 * @author Dann Vestergaard
 *
 */
public class JPdfCompiler {

	private String file = "";
	
	/**
	 * Perform compilation of the file from the template jrxml file.
	 * 
	 * @param context
	 * @param fileName
	 * @throws IOException
	 */
	public JPdfCompiler(ServletContext context, 
						String fileName,
						String destName
		) throws IOException
	{	
		JRProperties.setProperty(
			JRProperties.COMPILER_CLASSPATH, 
			context.getRealPath("/WEB-INF/lib/jasperreports-1.1.0.jar") +
			System.getProperty("path.separator") + 
			context.getRealPath("/WEB-INF/classes/")
			);
	
		JRProperties.setProperty(
			JRProperties.COMPILER_TEMP_DIR, 
			context.getRealPath(Attributes.REPORT_FOLDER)
			);

		try
		{
			this.file = JasperCompileManager.compileReportToFile(context.getRealPath(Attributes.REPORT_FOLDER+fileName));
		}
		catch (JRException e)
		{
			e.printStackTrace();
			return;
		}		
	}

	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}
}