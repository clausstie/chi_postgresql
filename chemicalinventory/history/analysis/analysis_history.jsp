<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="history" class="chemicalinventory.analysis.AnalysisHistory" scope="page"/>
<jsp:setProperty name="history" property="*"/>
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
<title>Analysis History</title>
</head>
<body>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_history_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">Analysis History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=sample_history">Sample History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=container_history">Container History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=compound_history">Compound History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=batch_history">Batch History</a> |   
</span>
<span class="textboxadm">
<%


//first display a list of all analysis and select one to modify
if(request.getParameter("code1") == null)
{%>
<h2>Analysis History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>Select Analysis</p>
<center>
	<table class="box" cellspacing="0" cellpadding="1" width="697">
		<thead>
			<tr>
				<th class="blue" width="200">Analysis name</th>
				<th class="blue" width="452">Description</th>
				<th class="blue" width="60">&nbsp;</th>
				<th width="15">&nbsp;</th>
			</tr>		
		</thead>
		<tbody>
			<tr>
				<td colspan="6">
					<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.HISTORY_BASE %>?action=analysis_list_history" width="697" height="500" marginwidth="0" name="list_frame">
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
	String id = history.getAnalysis_id();
	boolean check = history.analysis_history(id);

	if(check)
	{
%>
<h2>Analysis History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<CENTER>

	<table class="box" cellpadding="1" cellspacing="1" width="90%">
		<tr>
			<th colspan="1" align="left">Analysis Name</th>
			<td colspan="3"><b><%=history.getAnalysis_name()%></b></td>
		</tr>
		<tr>
			<!--spacer cell-->
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<th class="blue" align="center" colspan="4">History Lines</th>
		</tr>
		<tr>
			<th align="center" class="blue">Changed Date</th>
			<th align="center" class="blue">Changed by</th>
			<th align="center" class="blue">Version</th>
			<th align="center" class="blue">Change Remark</th>
		</tr>
		
		<!--show the lines in the history result-->
		<%
			String color = "";
			Vector elements = history.getElements();
			
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
	
					%>
						<td><%=token%></td>
					<%
				}
	      %></tr><%
			}%>
	</table>
</CENTER>

<%}//check was ok
  else//check not ok
  {%>
  <h2>Analysis History - Error</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<CENTER>
	<h3>Error in display of history for the analysis, please try again.</h3>
	</CENTER> 
	<%
  }
}//end code 1. Show analysis history
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