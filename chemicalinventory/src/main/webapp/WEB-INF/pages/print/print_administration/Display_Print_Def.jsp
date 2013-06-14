<%@ page language="java"%>
<%@ page import="chemicalinventory.utility.Return_codes" %>
<%@ page import="chemicalinventory.context.Attributes" %><
<%@ page import="java.util.*" %>
<jsp:useBean id="display_report" class="chemicalinventory.jReports.reportCreation.DisplayReportDefinition" scope="page"/>
<jsp:setProperty name="display_report" property="*"/>

<%
int status = 0;
int i = 0;
String data = "";
%>

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
  <h2>View Report Definition</h2>
  <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
  <br>
	<%
	if(request.getParameter("code1") != null)
	{
	%>
	<FORM method="post" action="">
		<table class="box" width="500px" cellpadding="1" cellspacing="1"
			border="0">
			<tr>
				<th colspan="2" class="blue">View Report Data</th>
			</tr>
			<tr>
				<th class="blue" width="175">Report Title:&nbsp;</th>
				<td colspan="1">
					<SELECT name="report_name" STYLE="width: 200px">
									<OPTION value="--" selected="selected">[-- SELECT --]</OPTION>
						<%
						Vector list2 = display_report.listReports();
						
						for (i = 0; i<list2.size(); i++)
						{
							data = (String) list2.get(i);
							
							StringTokenizer tokenizer = new StringTokenizer(data, "|");
							while(tokenizer.hasMoreTokens())
							{
								String id = tokenizer.nextToken();
								String name  = tokenizer.nextToken();
								%>
									<OPTION value="<%=id%>"><%=name%></OPTION>
								<%			
							}//end while
						}//end for
						%>
						</SELECT>
				</td>
			</tr>
		</table>
		<BR>
		<input class="submit" type="submit" name="Ok" value="OK"
			onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintDefinition&code2=yes'"/>
		<input class="submit" type="submit" name="Cancel" value="Cancel"
			onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintDefinition&code1=yes'"/> 
		</FORM>						

		<%if(request.getParameter("rcode1") != null)
		{
			%>
			<HR>
			<P>Den valgte rapport kunen ikke vises...</P>
			<%
		}
	}
	//end code 1
		/*
	* Show data stored for a report definition.
	*/
	if(request.getParameter("code2") != null)
	{
		String file_name = request.getParameter("report_name");					
		status = display_report.displayReportDef(file_name);
		
		if(status == Return_codes.SUCCESS)
		{
	%>	
			<table class="box" width="500px" cellpadding="1" cellspacing="1"
				border="0">
				<tr>
					<th colspan="2" class="blue">Report Data</th>
				</tr>
				<tr>
					<th class="blue" width="175">File Name:</th>
					<td colspan="1"><%=display_report.getReport_name()%></td>
				</tr>
				<tr>
					<th class="blue" width="175">Titel:</th>
					<td><%=display_report.getDisplay_name()%></td>
				</tr>
				<tr>
					<th class="blue" width="175">Description:</th>
					<td><%=display_report.getDescription()%>
					</td>
				</tr>
				<tr>
					<th colspan="2" class="blue">Report Parameters</th>
				</tr>
				<%
				Vector list = display_report.getList();
				if(list == null || list.size() == 0)
				{
				%>
				<tr>
					<td colspan="2"><i>No parameters for this report.</i></td>
				</tr>
				<%
				}
				else
				{
					for(i = 0; i<list.size(); i++)
					{
						String name = (String) list.get(i);
						%>
				<tr>
					<th class="blue" width="175">Parameter:</th>
					<td><%=name%></td>
				</tr>
				<%									
					}
				}
				%>
			</table>
			<%
			}//end if success...
			else
			{
				response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintDefinition&code1=yes&rcode1=yes");
			}
	/*
	*Here ends part with display of report data.
	*/
	}//end code 2
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