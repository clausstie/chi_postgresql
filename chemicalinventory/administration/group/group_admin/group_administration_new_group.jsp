<%@ page language="java" import="java.util.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="group" class="chemicalinventory.groups.Create_group" scope="page"/>
<jsp:setProperty name="group" property="*"/>
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
<title>
Register a new user group.
</title>
<script LANGUAGE="JavaScript">

function validateForm(form) 
{
 if (trim(form.name.value) == "") 
 {
  alert("Please fill in a valid group name!");
  form.name.focus();
  return false;
 }
return true;
}
</script>
</head>
<body onload="document.group.name.focus()">
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_group.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=new_group">New Group</a> |
     <a class="adm" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=modify_group">Modify / Delete Group</a> |
     <a class="adm" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=overwiew_user">Overview of Users</a> |
     <a class="adm" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=overwiew_container">Overview of Containers</a> |
</span>
<span class="textboxadm">
<h2>Registration of a new group</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
Enter the name for the new group.
<form method="post" action="<%= Attributes.GROUP_ADMINISTRATOR_BASE %>?action=new_group&code1=yes" onSubmit="return validateForm(this)" name="group">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="4" class="blue">Group</TH></TR>
	    <tr>
	        <th align="left" class="standard">Group Name:</th>
	   	     <td><input type="text" class="w200" name="name"></td>
	    </tr>
    </table><br>
    <input class="submit" type="submit" name="submit" value="Submit Form">&nbsp;&nbsp;&nbsp;
    <input class="submit" type="Reset" value="Reset">
</form>
<%
if(request.getParameter("code1")!=null) 
{
    group.new_group();
%><hr><%
    if(group.isGroup_ok() == true)
    {%>
       <h3>One new group added. Group name: <%=group.getName().toUpperCase()%></h3>
    <%}
    else
    {
      %><h3>Registration of the new group (<%=group.getName()%>) failed, please try again.</h3><%
    }
}
%>

</center>
</span>
<map name="nav_bar">
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