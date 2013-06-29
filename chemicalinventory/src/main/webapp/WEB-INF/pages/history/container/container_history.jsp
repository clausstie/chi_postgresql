<%@ page language="java" import="java.net.*" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.history.HistoryLine" %>
<jsp:useBean id="history" class="chemicalinventory.history.History" scope="page"/>
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
  <SCRIPT type="text/javascript">
    function validateForm(form) 
	{
	 if (trim(form.container_id.value) == "")
	 {
	  alert("Please enter a valid container id");
	  form.container_id.focus();
	  return false;
	 }
	 else if (trim(form.container_id.value) != "")
	 {
	 	if(isNumber(form.container_id.value)==false)
    	{
	        alert("Please fill a valid container id");
    	    form.container_id.focus();
       		return false;
    	}
	 }
	return true;
	}
  </SCRIPT>
  <title>Container History</title>
</head>
<%
if(request.getParameter("code1") == null)
{%>
<body onload="document.container.container_id.focus();">
<%
}
else
{%>
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

//select the container..
if(request.getParameter("code1") == null)
{%>
<h2>Container History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<center>
	<form method="post" action="<%= Attributes.HISTORY_BASE %>?action=container_history&code1=yes" name="container" onSubmit="return validateForm(this)">
    <table class="box" cellpadding="1" cellspacing="2" border="0" width="350">
  		<TR><TH colspan="4" class="blue">Search</TH></TR>
	        <tr>
	            <th align="left" class="standard">Container Id:</th>
	            <td><input type="text" name="container_id" tabindex="1" class="w200"></td>
	        </tr>
	    </table>
	    <br>
	    <input class="submit" type="Submit" name="Submit" value="Submit">
	</form>	
</center>
<%
}
//display error message if invalid container id.
if(request.getParameter("errorCode1") != null)
{%>
	<h3>Not a valid container id entered, please try again.</h3>
<%
}

//display error message if invalid container id.
if(request.getParameter("errorCode2") != null)
{%>
	<h3>Container id is not registered in the system.</h3>
<%
}

//second display information about the analysis..
if (request.getParameter("code1") != null)
{
	String id = history.getContainer_id();
	boolean check = history.getContainerHistory(id);

	if(check)
	{
%>
<h2>Container History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<CENTER>
	<table class="box" cellpadding="1" cellspacing="1" width="99%">
		<tr>
			<th colspan="2" align="left">Container id</th>
			<td colspan="6"><b><%=id%></b></td>
		</tr>
		<tr>
			<th colspan="2" align="left">Chemical Name</th>
			<td colspan="6"><b><%=URLDecoder.decode(history.getChemical_name(), "UTF-8")%></b></td>
		</tr>
		<tr>
			<!--spacer cell-->
			<td colspan="8">&nbsp;</td>
		</tr>
		<tr>
			<th class="blue" align="center" colspan="9">History Lines</th>
		</tr>
		<tr>
			<th align="center" class="blue" width="5%">#</th>
			<th align="center" class="blue" width="15%">Change</th>
			<th align="center" class="blue" width="7%">User</th>
			<th align="center" class="blue" width="14%">Timestamp</th>
			<th align="center" class="blue" width="34%" valign="top">Change Remark</th>
			<th align="center" class="blue" width="10%">Old Value</th>
			<th align="center" class="blue" width="10%">New Value</th>
			<th align="center" class="blue" width="5%">Unit</th>
		</tr>
		
		<!--show the lines in the history result-->
		<%
			String color = "";
			Vector elements = history.getTable_list();
			
			if(!elements.isEmpty())
			{
				for(int i=0; i<elements.size(); i++)
				{
			      color = "normal";
		          if(i % 2 != 0)
		          {
		            color = "blue";
		          }
				%>
				<tr class="<%=color%>">
					<td>
						<%=i+1%>
					</td><%
					HistoryLine line = (HistoryLine) elements.get(i);
					String data = line.getHtml_line();
					
					%><%=data%><%
		      %></tr><%
				}
			}
			else
			{
				%>
				<tr>
					<td colspan="8">
						<i>No history lines for the container</i>
					</td>
				</tr>
				<%
			}%>
	</table>
</CENTER>

<%}//check was ok
  else//check not ok
  {
   int status = history.getStatus();
   
   if(status == 1)
   {
   	response.sendRedirect(Attributes.HISTORY_BASE+"?action=container_history&errorCode1=yes");
   }
   else if(status == 2)
   {
   	response.sendRedirect(Attributes.HISTORY_BASE+"?action=container_history&errorCode2=yes");
   }
   else
   {  
  %>
  <h2>Container History - Error</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<CENTER>
	<h3>Error in display of history for the container, please try again.</h3>
	</CENTER> 
	<%
	}
  }
}//end code 1. Show history
%>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.JSP_BASE%>?action=analysis_history">
</map>
</body>
</html>  