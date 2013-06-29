<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="Util" class="chemicalinventory.utility.Util" scope="page"/>
<jsp:useBean id="supplier" class="chemicalinventory.beans.supplierBean" scope="page"/>
<jsp:setProperty name="supplier" property="*"/>
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
Administration of supplier.
</title>
<script language="JavaScript">
function validateForm(form) 
{
 if (trim(form.supplier_name.value) == "") 
 {
  alert("Please fill in a valid name!");
  form.supplier_name.focus();
  return false;
 }

return true;
}
</script>
</head>
<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null)
{
%>
<body onload="document.supplier.supplier_name.focus()">
<%
}
else
{%>
<body>
<%
}
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_supplier.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%= Attributes.SUPPLIER_ADMINISTRATOR_BASE %>?action=Supplier">Register a new supplier</a> |
     <a class="adm" href="<%= Attributes.SUPPLIER_ADMINISTRATOR_BASE %>?action=modify_supplier">Modify an existing supplier</a> |
</span>
<span class="textboxadm">

<h2>Enter the name of the new supplier you want to register</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<form action="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=Supplier&code1=yes" method="post" name="supplier" onSubmit="return validateForm(this)">
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="460">
	<TR><TH colspan="4" class="blue">Supplier</TH></TR>
    <tr>
      <th align="left" class="standard">Supplier Name:</th>
      <td><input type="text" name="supplier_name" class="w290"></td>
    </tr>
  </table><br>
  <input class="submit" type="submit" name="Submit" value="Submit">
</form>
<%
if(request.getParameter("code1")!= null)
{
  supplier.registerSupplier();
  
  if(supplier.control==true)
  {
    %><p>One new supplier named <BIG><%=chemicalinventory.utility.Util.encodeTag(supplier.getSupplier_name())%></BIG> has been added.</p><%
  }
  else
  {
    %><p>ERROR - Registration of new supplier failed!</p><%
  }
}
%>
</center>
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