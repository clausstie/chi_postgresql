<%@ page language="java"%>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.Vector" %>
<%@ page import="chemicalinventory.utility.Return_codes" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="display_report" class="chemicalinventory.jReports.reportCreation.DisplayReportDefinition" scope="page"/>
<jsp:useBean id="delete_report" class="chemicalinventory.jReports.reportCreation.DeleteReportDefinition" scope="page"/>
<jsp:useBean id="modify_report" class="chemicalinventory.jReports.reportCreation.ModifyReportDefinition" scope="page"/>
<jsp:setProperty name="modify_report" property="*"/>

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

<link rel="stylesheet" type="text/css"
	href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
</head>
<body>
<span class="posAdm1"> <img
	src="<%=Attributes.IMAGE_FOLDER%>/bar_report_info.png" height="55"
	width="820" usemap="#nav_bar" border="0"> </span>
<span class="posAdm2"> | <a class="adm"
	href="<%=Attributes.PRINT_BASE%>?action=printindex">Print Index</a> | <a
	class="adm"
	href="<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintDefinition&code1=yes">Display
Definition</a> | <a class="adm"
	href="<%=Attributes.PRINT_ADMINISTRATION%>?action=printCreation&code1=yes">Create
Print Definition</a> | <a class="adm"
	href="<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code1=yes">Modify
Print Definition</a> | </span>

<span class="textboxadm">
<h2>Modify Report Definition</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<%
if(request.getParameter("code1") != null)
{
%>
<FORM method="post" action="">
<table class="box" width="500px" cellpadding="1" cellspacing="1"
	border="0">
	<tr>
		<th colspan="2" class="blue">Find Report Data</th>
	</tr>
	<tr>
		<th class="blue" width="175">Report Name:&nbsp;</th>
		<td colspan="1"><SELECT name="report_name" STYLE="width: 200px">
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
		</SELECT></td>
	</tr>
</table>
<BR>
<input class="submit" type="submit" name="Ok" value="OK"
	onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code2=yes'" />
<input class="submit" type="submit" name="Cancel" value="Cancel"
	onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code1=yes'" />

<%if(request.getParameter("rcode1") != null)
		{
			%>
<HR>
<P>Could not display the selected report.</P>
<%
		}
		if(request.getParameter("rcode2") != null)
		{
			%>
<HR>
<P>The selected report has been deleted.</P>
<%
		}
		if(request.getParameter("rcode3") != null)
		{
			%>
<HR>
<P>The selected report could not be deleted.</P>
<%
		}
/*
*Here ends part enter report name
*/
}

/*
* Starts the part of the page for modifying data.
*/
if(request.getParameter("code2") != null)
{
	String file_name = request.getParameter("report_name");
	status = display_report.displayReportDef(file_name);
	
	if(status == Return_codes.SUCCESS)
	{

%>
<FORM method="post" action="">
<table class="box" width="500px" cellpadding="1" cellspacing="1"
	border="0">
	<tr>
		<th colspan="2" class="blue">Report Data</th>
	</tr>
	<tr>
		<th class="cell_header_nc" width="175">File Name:</th>
		<td colspan="1"><INPUT class="275" type="text" name="report_name"
			value="<%=display_report.getReport_name()%>"> <INPUT type="hidden"
			value="<%=display_report.getReport_id()%>" name="report_id"></td>
	</tr>
	<tr>
		<th class="cell_header_nc" width="175">Report Titel:</th>
		<td><INPUT class="275" type="text" name="display_name"
			value="<%=display_report.getDisplay_name()%>"></td>
	</tr>
	<tr>
		<th class="cell_header_nc" width="175">Description:</th>
		<td><TEXTAREA name="description" cols="32" rows="4"><%=display_report.getDescription()%></TEXTAREA>
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
		<th class="cell_header_nc" width="175">Parameter 1 - Name:</th>
		<td><INPUT class="275" type="text" name="parameter1_name"></td>
	</tr>
	<tr>
		<th class="cell_header_nc" width="175">Parameter 2 - Name:</th>
		<td><INPUT class="275" type="text" name="parameter2_name"></td>
	</tr>
	<tr>
		<th class="cell_header_nc" width="175">Parameter 3 - Name:</th>
		<td><INPUT class="275" type="text" name="parameter3_name"></td>
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
		<th class="cell_header_nc" width="175">Parameter <%=i+1%> - Name:</th>
		<td><INPUT class="275" type="text" value="<%=name%>"
			name="parameter<%=i+1%>_name"> <INPUT type="hidden" value="<%=name%>"
			name="parameter<%=i+1%>_name_o"></td>
	</tr>
	<%									
				}
				
				if(list.size() < 3)
				{
					/*
					*Display the rest of the fields.
					*/
					int n = list.size();
					n = 3-n;
					int nn = list.size();
					for(int m = 0; m<n; m++)
					{
					%>
	<tr>
		<th class="cell_header_nc" width="175">Parameter <%=nn+1+m%> - Name:</th>
		<td><INPUT class="275" type="text" name="parameter<%=nn+1+m%>_name"></td>
	</tr>
	<%
					}
				}								
			}
			
			%>
</table>
<br>
<input class="submit" type="submit" name="Ok" value="Ok"
	onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code3=yes'" />
<input class="submit" type="submit" name="Cancel" value="Cancel"
	onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code1=yes'" />
<input class="submit" type="reset" name="Reset" value="Reset" /> <input
	class="submit" type="submit" name="Remove" value="Remove Report"
	onclick="this.form.action='<%=Attributes.PRINT_ADMINISTRATION%>?action=PrintModification&code4=yes'" />
</FORM>

<%if(request.getParameter("rcode1") != null)
{
	%>
<HR>
<P>Error! report name is mandatory.</P>
<%
}
if(request.getParameter("rcode2") != null)
{
	%>
<HR>
<P>Error! report title is mandatory..</P>
<%
}
if(request.getParameter("rcode3") != null)
{
	%>
<HR>
<P>An error orcurred, please try again.</P>
<%
}
%> <%
				}
				else
				{
					response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code1=yes&rcode1=yes");
				}
				/*
				*Here ends part of entering data
				*/
				}
				
				/*
				* Start validation and registration of data
				*/
				if(request.getParameter("code3") != null)
				{				
					status = modify_report.modifyReportDef();

					if(status == Return_codes.SUCCESS)
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintDefinition&code2=yes&report_name="+modify_report.getReport_name());
					}
					else if (status == Return_codes.MISSING_NAME)
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code2=yes&rcode1=yes&report_name="+modify_report.getReport_name());
					}
					else if (status == Return_codes.MISSING_TITLE)
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code2=yes&rcode2=yes&report_name="+modify_report.getReport_name());
					}
					else
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code2=yes&rcode3=yes&report_name="+modify_report.getReport_name());
					}
				}
				
				/*
				* Delete the report
				*/
				if(request.getParameter("code4") != null)
				{				
					i = modify_report.getReport_id();
					status = delete_report.deleteReportDef(i);

					if(status == Return_codes.SUCCESS)
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code1=yes&rcode2=yes");
					}
					else
					{
						response.sendRedirect(Attributes.PRINT_ADMINISTRATION+"?action=PrintModification&code1=yes&rcode3=yes");
					}
				}
%>
</span>
<MAP NAME="nav_bar">
	<AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
	<AREA SHAPE="rect" COORDS="134,1,252,21"
		href="<%=Attributes.JSP_BASE%>?action=location">
	<AREA SHAPE="rect" COORDS="262,1,381,21"
		href="<%=Attributes.JSP_BASE%>?action=Container">
	<AREA SHAPE="rect" COORDS="388,1,514,21"
		href="<%=Attributes.PRINT_BASE%>?action=printindex">
	<AREA SHAPE="rect" COORDS="518,1,644,21"
		href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
</map>
</body>
</html>