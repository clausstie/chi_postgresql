<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="history" class="chemicalinventory.sample.SampleHistory" scope="page"/>
<jsp:setProperty name="history" property="*"/>
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<script LANGUAGE="JavaScript">

function validateForm(form) 
{<%
if(request.getParameter("code1") == null)
{%>
 if(trim(form.sample_id.value)== "")
 {
    alert("Please fill a valid sample id");
    form.sample_id.focus();
    return false;
 }
 <%
 }%> 
 return true;
}
</script>
<title>Sample History</title>
</head>
<%
if(request.getParameter("code1") == null)
{%>
<body onload="document.sample.sample_id.focus();">
<%
}
else
{
	%>
<body>
<%
}
%>
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
<h2>Sample History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<center>
<form method="post" action="<%=Attributes.HISTORY_BASE%>?action=sample_history&code1=yes" onsubmit="return validateForm(this)" name="sample">
    <table class="box" cellpadding="1" cellspacing="2" border="0" width="350">
  		<TR><TH colspan="4" class="blue">Search</TH></TR>
		<tr>
			<th align="left" class="standard">Sample id:</th>
			<td><input type="text" name="sample_id" class="w200"></td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.HISTORY_BASE%>?action=sample_history&code1=yes'">&nbsp;
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}
//second display information about the sample
if (request.getParameter("code1") != null)
{
	String id = history.getSample_id();
	boolean check = history.sample_history(id);

	if(check)
	{
%>
<h2>Sample History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<CENTER>

	<table class="box" cellpadding="1" cellspacing="1" width="90%">
		<tr>
			<th colspan="1" align="left">Sample id</th>
			<td colspan="2"><b><%=history.getSample_id()%></b></td>
		</tr>
		<tr>
			<th colspan="1" align="left">Chemical Name</th>
			<td colspan="2"><b><%=URLDecoder.decode(history.getChemical_name(), "UTF-8")%></b></td>
		</tr>
		<tr>
			<!--spacer cell-->
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<th class="blue" align="center" colspan="4">History Lines</th>
		</tr>
		<tr>
			<th align="center" class="blue" width="20%">Changed Date</th>
			<th align="center" class="blue"  width="13%">Changed by</th>
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
  {
  	int statusCode = history.getStatuscode();
  %>
  <h2>Sample History - Error</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<CENTER>
		<%
			if(statusCode == 1)
			{
				%><h3>Error in display of history for the sample, not a valid sample id.</h3><%
			}
			else if(statusCode == 2)
			{
				%><h3>Error in display of history for the sample, id <%=history.getSample_id()%> does not exist.</h3><%
			}
			else if(statusCode == 1)
			{
				%><h3>Error in display of history for the sample, please try again.</h3><%
			}
		%>
	</CENTER> 
	<%
  }
}//end code 1. Show sample history
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