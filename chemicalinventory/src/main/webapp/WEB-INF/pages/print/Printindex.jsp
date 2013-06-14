<%@ page language="java"%>
<%@ page import="chemicalinventory.utility.Return_codes" %>
<%@ page import="java.util.*" %>
<%@ page import="chemicalinventory.jReports.reportChooser.ReportChooser" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="display_report" class="chemicalinventory.jReports.reportCreation.DefToHTML" scope="page"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              needed to manage a chemical storage, based on the java technology.
 *				In addition it includes a sample module. This module, is used
 *				to create samples, store results etc.
 *
 * Copyright: 	2004-2007 Dann Vestergaard and Claus Stie Kallesøe
 *
 * overLIB:     overLIB 3.51  -- Copyright Erik Bosrup 1998-2002. All rights reserved.
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
 *
-->
    <title>CI print module index</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
  </head>
  <body>
<span class="posAdm1">
	 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_report_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
	   | <a class="adm" href="<%=Attributes.PRINT_BASE%>?action=printindex">Print Index</a> |
	   <a class="adm" href="<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintDefinition&code1=yes">Display Definition</a> |
	   <a class="adm" href="<%=Attributes.PRINT_ADMINISTRATION%>?action=printCreation&code1=yes">Create Print Definition</a> |
   	   <a class="adm" href="<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code1=yes">Modify Print Definition</a> |
</span>
<span class="textboxadm">
  <h2>CI Report Module</h2>
  <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
  <br>
  <h3>Print reports defined in the system.</h3>
	<%
	int status = display_report.createDefToHtml();
	if(status != Return_codes.SUCCESS)
	{
	%>
		<TABLE class="box" width="500">
			<TR>
				<TH class="blue" width="100">Rapport</TH>
			</TR>
			<TR>
				<TD><I>No reports defined in the system to display.</I></TD> 
			</TR>
		</TABLE>							
	<%
	}
	else
	{
		/*
		*Display the list of reports defined, and registered.
		*/
		Vector list = display_report.getList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String obj = (String) iter.next();
			%><%=obj%><%	
			}				
	}						
	%>
	<%
	/*
	* Here ends part of data display for the delivery
	*/
	if(request.getParameter("print") != null && request.getParameter("print").equalsIgnoreCase("yes"))
	{	
		ReportChooser reportChooser = new ReportChooser(request, response);
		String url = reportChooser.getUrl().toString();
		
		session.setAttribute("PrintURL", url);
	
		%>
		<IFRAME width="0" 
				height="0" 
				src="<%=Attributes.PRINT_BASE%>?action=choosePrint">
		</IFRAME>
		<%
	}
	%>
  </span>

  	<MAP NAME="nav_bar">
	  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
	  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
	  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
	  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
	  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
	</map>
  </body>
</html>