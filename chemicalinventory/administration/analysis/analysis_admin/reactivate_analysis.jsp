<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:useBean id="unit" class="chemicalinventory.unit.UnitBean" scope="page"/>
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
-->
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
<title>Reactivate analysis (no longer removed!)</title>
</head>
<body>
<%
//initial values to be used on this page
String user = request.getRemoteUser().toUpperCase();
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_analysis.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">Create Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis">Display Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_analysis">Modify Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=remove_analysis">Remove Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=reactivate_analysis">Reactivate Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">Create Analysis Map</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">Modify Analysis Map</a> |
</span>
<span class="textboxadm">
<%

if (request.getParameter("code2") != null)
{
	//remove the analysis
	String id = analysis.getAnalysis_id();
	String version = analysis.getVersion();
	boolean check = analysis.reactivateAnalysis(id, user, version);

	if(check)
	{%>
		<center>
			<h3>The analysis has been reactivated!</h3>
		</center>
	<%	
	}
	else
	{%>
		<center>
			<h3>ERROR the analysis could not be reactivated!</h3>
		</center>
	<%
	}
}//end code 2 remove the analysis

//first display a list of all analysis and select one to modify
if(request.getParameter("code1") == null)
{%>
<h2>Re-activate analysis</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>Select the analysis to re-activate</p>
<center>
	<table class="box" cellspacing="0" cellpadding="1" width="750">
		<thead>
			<tr>
				<th class="blue" width="198">Analysis name</th>
				<th class="blue" width="454">Description</th>
				<th class="blue" width="53">Version</th>
				<th class="blue" width="60">&nbsp;</th>
				<th width="15">&nbsp;</th>
			</tr>		
		</thead>
		<tbody>
			<tr>
				<td colspan="6">
					<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=analysis_list_reactivate" width="750" height="600" marginwidth="0" name="list_frame">
					</iframe>
				</td>
			</tr>				
		</tbody>
	</table>
</center>
<%
}

//second display information about the analysis..
if (request.getParameter("code1") != null)
{
	analysis.getAnalysisInfo_INACTIVE();	//get information about the analysis created
	
%>
	<h2>Data for the selected analysis</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
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
							<td colspan="2"><%=analysis.getVersion()%></td>
						</tr>
						<tr>
							<th align="left" width="160">Active:</th>
							<td colspan="4"><%=analysis.getActive()%></td>
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
							<th class="blue">Result Type</th>
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
		</table><br>
		<form method="post" action="">
			<input class="submit" type="submit" name="Reactivate" value="Reactivate" onclick="this.form.action='<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=reactivate_analysis&code2=yes'">
			<input class="submit" type="submit" name="Cancel" value="Cancel" onclick="this.form.action='<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=reactivate_analysis'">
			<input type="hidden" name="analysis_id" value="<%=analysis.getAnalysis_id()%>">						
			<input type="hidden" name="version" value="<%=analysis.getVersion()%>">			
		</form>
	</center>
<%
}
//end code 1 %>

</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="3,2,90,23" href="<%=Attributes.ADMINISTRATOR_BASE%>?action=Adm">
  <AREA SHAPE="rect" COORDS="92,2,179,23" href="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=Supplier">
  <AREA SHAPE="rect" COORDS="181,2,268,23" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
  <AREA SHAPE="rect" COORDS="270,2,362,23" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">
  <AREA SHAPE="rect" COORDS="364,2,451,23" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">
  <AREA SHAPE="rect" COORDS="453,2,543,23" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=new_group">
  <AREA SHAPE="rect" COORDS="544,3,634,23" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis">
  <AREA SHAPE="rect" COORDS="637,3,727,23" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE %>?action=new_unit">
</map>
</body>
</html>  