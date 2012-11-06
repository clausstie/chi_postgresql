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
<script language="JavaScript" src="../../script/inventoryScript.js"></script>
<title>Display an analysis</title>
</head>
<body>
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


//first display a list of all analysis and select one to modify
if(request.getParameter("code1") == null && request.getParameter("code3") == null)
{%>
<h2>Display analysis</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>Select the analysis to display</p>
<center>
	<table class="box" cellspacing="0" cellpadding="1" width="750">
		<thead>
			<tr>
				<th class="blue" width="245px">Analysis name</th>
				<th class="blue" width="374px">Description</th>
				<th class="blue" width="57px">Version</th>
				<th class="blue" width="63px">&nbsp;</th>
				<th width="17px">&nbsp;</th>
			</tr>		
		</thead>
		<tbody>
			<tr>
				<td colspan="6">
					<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=analysis_list4" width="750" height="500" marginwidth="0" name="list_frame">
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
	String ver = analysis.getVersion();
	if(ver!=null && !ver.equals(""))
	{
		analysis.getAnalysisInfo_version(ver);	//get information about the analysis created	
	}
	else
	{
		analysis.getAnalysisInfo();	//get information about the analysis created

	}
	
	Vector versions = analysis.getListOfVersions();
%>
	<h2>Data for the selected analysis</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	  <center>
	  <FORM name="versions"> 
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
							<th align="right">Display version:</th>
							<th align="center">
    							<select name="diff_version" onChange="parent.Main.location=document.versions.diff_version.options[document.versions.diff_version.selectedIndex].value"> 
									<option>-- ? --</option>
									<%
									for (int i = 0; i<versions.size(); i++)
									{
										String option = (String) versions.get(i);%>
									<option value="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis&code1=yes&version=<%=option%>&analysis_id=<%=analysis.getAnalysis_id()%>"><%=option%></option><%										
									}
									%>
								</select>
							</th>
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
		</table><br/>
     </FORM>
	</center>
<%
}
%>
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