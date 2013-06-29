<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:setProperty name="analysis" property="*"/>
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
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
<script LANGUAGE="JavaScript">
function disableField()
{
	document.form.criteria.disabled=true;
}

function openWindow(url, number)
{
	if(number == 1)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=820, height=600")
	}
	else if(number == 2)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=600, height=600")
	}
}
 
</script>
<title>Create a list of analysis</title>
</head>
<body>
<%
if(request.getParameter("code1") == null)
{
%>
<form method="post" name="analysis">
<table class="special" border="0" cellpadding="2" cellspacing="1" width="100%">

	<tr>
		<th align="left" colspan="2">
			Select analysis:
			<hr>
		</th>
	</tr>
	<%
		analysis.setBase(Attributes.ADMINISTRATOR_BASE);
		analysis.setNormalbase(Attributes.JSP_BASE);
		analysis.listAnalysis_forSearch();
		Vector elements = analysis.getElements();
		String color = "normal";
		
		for(int i=0; i<elements.size(); i++)
		{
	      color = "normal";
          if(i % 2 != 0)
          {
            color = "blue";
          }
		%>
		<tr class="<%=color%>"><%
			String data = (String) elements.get(i);
			
			StringTokenizer tokens = new StringTokenizer(data, "|");
			
			while(tokens.hasMoreTokens())
			{
				String token = tokens.nextToken().trim();

				out.print(token);
			}
      %></tr><%
		}
	%>
</table>
<INPUT type="hidden" name="idArray" readonly="readonly" disabled="disabled">
</form>
<%
}
//display the result of the users request
//here shall be displayed detailed information on a single analysis.
//this includes analysis name as a header
//each field text id, unit, type(numeric or text), and
//a text field to enter search criteria on the individual fields.
if(request.getParameter("code1") != null)
{
	String analysis_id = request.getParameter("analysis_id");
	analysis.getListOfVersionsFields(analysis_id);
	Vector list_versions = analysis.getListOfVersions();
	%>
<form method="post" name="analysis">
	<table class="special" width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="blue" align="center" width="33%"><b><%=analysis.getAnalysisNamesDb(analysis_id)%></b></td>
			<td class="blue" align="center" width="10%"><b>Version</b></td>
			<td class="blue" align="center" width="30%"><b>Field Name</b></td>
			<td class="blue" align="center" width="10%"><b>Type</b></td>
			<td class="blue" align="center" width="10%"><b>Unit</b></td>
			<td class="blue" align="center" width="7%"><b>Incl.</b></td>
		</tr>
		<tr>
			<td valign="top" width="260">
				<TABLE>
					<tr valign="top">
						<th align="left">Search for result:</th>
					</tr>
					<tr valign="top">
						<td>
							<select name="option">
								<option value="EQ" selected="selected">=</option>
								<option value="LES">&#60;</option>
								<option value="GRE">&#62;</option>
								<option value="LESEQ">&#60;=</option>
								<option value="GREEQ">&#62;=</option>
							</select>
							<input size="28" type="text" name="criteria"/>
						</td>
					</tr>
					<tr valign="top">
						<th align="left">
							Search in type
						</th>
					</tr>	
					<tr valign="top">					
						<td>
							<input type="radio" name="type" value="numeric" checked="checked"/>Numeric
							<input type="radio" name="type" value="text"/>Text
							<input type="radio" name="type" value="both"/>Both
						</td>
					</tr>				
					<tr>
						<td><a href="<%=Attributes.JSP_BASE%>?action=analysis_list_search">Return To List</a>
						</td>
					</tr>
				</TABLE>
			</td>
			<td colspan="5" width="540" valign="top">
				<table width="100%" border="0">
					<%
					int color_counter = 0;
					boolean there_was_results = false;								
					
					for (int i = 0; i<list_versions.size(); i++)
					{
						String color = "special";
						  if(color_counter % 2 != 0)
				          {
				            color = "blue";
				          }
					
						String f_version = (String) list_versions.get(i);
						
						String html = analysis.createFieldsForSearch(f_version, analysis_id, color);
						
						if(html != null && !html.equals(""))
						{
							color_counter++;
							there_was_results = true;
						%>
							<%=html%>
						<%
						}
					}
					if(!there_was_results)
					{
						%>
							<tr>
								<td colspan="5" align="center"><i>No Fields for this analysis...</i></td>					
							</tr>
						<%
					}
					%>
				</table>
			</td>
	</table>
</form>
<%	
}
%>
</body>
</html>  