<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="unit" class="chemicalinventory.unit.UnitBean" scope="page"/>
<jsp:setProperty name="unit" property="*"/>

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
<script LANGUAGE="JavaScript">  
 var unit_name=new Array();
<%  
    Vector un_taken = unit.listOfUnits2();

    for(int i=0; i<un_taken.size(); i++)
    {
       String name = (String) un_taken.elementAt(i);
      %>unit_name[<%= i %>] = "<%= name %>";<%
    }  
%>

function unit_validator()
{
	var un = document.unit.unit_name.value;
	un = un.toUpperCase();
    var checkno = 0;

	for(i = 0; i <= unit_name.length; i++)
	{
		if(unit_name[i] == un)
		{
            checkno = 1;
			return checkno;
		}
	}
        return checkno;
}

function validateForm(form) 
{
 if (trim(form.unit_name.value) == "") 
 {
  alert("Please fill in a valid name for the new unit!");
  form.unit_name.focus();
  return false;
 }
 if (unit_validator() == 1)
 {
   alert("The entered name for the unit cannot be used as it has allready been taken!");
   form.unit_name.focus();
   return false; 
 }

return true;
}

</script>
<title>Creation of a new unit</title>
</head>
<body onload="document.unit.unit_name.focus()">

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_unit.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
	| <a class="adm" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=new_unit">Create Unit</a> |
   <a class="adm" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=modify_unit">Modify Unit</a> |
</span>
<span class="textboxadm">

<h2>Define units</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<CENTER>
<form method="post" action="<%= Attributes.UNIT_ADMINISTRATOR_BASE %>?action=new_unit&code1=yes" name="unit" onSubmit="return validateForm(this)">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="4" class="blue">Unit</TH></TR>
		<tr>
			<th align="left" class="standard">Unit Name:</th>
			<td>
				<input class="w200" type="text" name="unit_name">
			</td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Register">
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
if (request.getParameter("code1") != null)
{
	boolean check =	unit.registerUnit();
	
	if(check)
	{
		%>
		<br/>
		<hr/>
		<h3>New unit registered: <%=unit.getUnit_name()%></h3>
		<%
	}
	else
	{
	%>
		<br/>
		<hr/>
		<h3>New unit registered could not be registered!</h3>
	<%
	}
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