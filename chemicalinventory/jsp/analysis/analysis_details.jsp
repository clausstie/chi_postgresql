<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<html>
<head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              neded to manage a chemical storage, based on the java technology.
 *				In addition it includes a sample module. This module, is used
 *				to create samples, store results etc.
 *
 * Copyright: 	2004, 2005 Dann Vestergaard and Claus Stie Kallesøe
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
 *   along with Foobar; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
<link rel="stylesheet" type="text/css" href="../styles/Style.css">
<title>details about an analysis</title>
</head>
<body>
<%
//initial values to be used on this page
String user = request.getRemoteUser().toUpperCase();
String id = request.getParameter("analysis_id");
String inactive = "";
inactive = request.getParameter("inactive");

	analysis.setAnalysis_id(id);

	if(inactive != null && !inactive.equals(""))
	{
		analysis.getAnalysisInfo_INACTIVE();
	}
	else
	{
		analysis.getAnalysisInfo();	//get information about the analysis
	}
%>
	<h2>Data for the selected analysis</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	<br>
	  <center>
		<table class="box" cellspacing="1" cellpadding="1" border="0" width="75%" frame="box">
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis Data</th>
						</tr>
						<tr>
							<th align="left" width="160">Analysis Name:</th>
							<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(analysis.getAnalysis_name())%></td>
						</tr>
						<tr>
							<th align="left" width="160">Analysis Version:</th>
							<td colspan="4"><%=analysis.getVersion()%></td>
						</tr>
						<tr>
							<th align="left" width="160">Description:</th>
							<td colspan="4"><%=analysis.getRemark()%></td>
						</tr>								
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="7" class="blue">Analysis Fields</th>
						</tr>
						<tr>
							<th class="blue">#</th>
							<th class="blue">Text Id</th>
							<th class="blue">Result Min</th>
							<th class="blue">Resullt Max</th>
							<th class="blue">Resullt Type</th>
 							<th class="blue">Unit</th>
							<th class="blue">Use For<br/>Spec</th>
						</tr>
						
						<%//show receipt for the creation
						Vector elements = analysis.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{%>
							<tr>
								<td align="center" width="20"><%=String.valueOf(i+1)%></td><%
							String data = (String) elements.get(i);
				            StringTokenizer tokens = new StringTokenizer(data, "|");
				            
				            while (tokens.hasMoreTokens())
				            {
				            	String token = chemicalinventory.utility.Util.encodeTag(tokens.nextToken().trim());%>
				            	<td><%=token%></td><%
				            }%>
				            </tr><%
						}%>				
						</table>
					</td>
			</tr>	
		</table>
	</center>
</body>
</html>  