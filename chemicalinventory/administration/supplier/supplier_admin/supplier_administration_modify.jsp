<%@ page language="java" import="java.util.*" import="java.net.*"%>
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
<body>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_supplier.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%= Attributes.SUPPLIER_ADMINISTRATOR_BASE %>?action=Supplier">Register a new supplier</a> |
     <a class="adm" href="<%= Attributes.SUPPLIER_ADMINISTRATOR_BASE %>?action=modify_supplier">Modify an existing supplier</a> |
</span>
<span class="textboxadm">

<h2>Modify an existing supplier</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>

<%
if(request.getParameter("code1")== null && request.getParameter("code2")== null && request.getParameter("code3")== null)
{
supplier.getSuppliers();
%>
<p>This is the total list of suppliers registered in the system. Press modify to do that.</p>
<%
    if(supplier.supplier.isEmpty())
    {
    %>
      <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
      <thead><tr> <th>Id:</th>
                  <th>Supplier Name:</th>
             </tr>
      </thead>
      <tbody>
         <tr align="center">
         <td colspan="9">Error the list of suppliers is empty.</td>
         </tr>
      </tbody>
      </table>
    <%
    }
    else
    {
    %>
     <table class="box" cellspacing="1" cellpadding="1" width="50%" align="center">
        <thead>
            <tr class="special"> 
                  <th class="blue">Id:</th>
                  <th class="blue">Supplier Name:</th>
                  <th class="blue"></th>
             </tr> 
        </thead>
        <tbody>
      <%
         for(int i=0; i<supplier.supplier.size(); i++)
         {
            String id = (String) supplier.supplier_id.elementAt(i);
            String supplier_name = (String) supplier.supplier.elementAt(i);
            String color = "normal";
            if(i % 2 != 0)
            {
                 color = "blue";
            }
         %><tr class="<%= color %>">
               <td align="center"><%= id %></td>
               <td align="center"><%= chemicalinventory.utility.Util.encodeTag(URLDecoder.decode(supplier_name, "UTF-8")) %></td>
               <form method="post" action="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=modify_supplier&code1=yes">
                   <td align="center"><input class="submit_width80" type="submit" name="Select" value="Modify">
                   	<input type="hidden" name="id" value="<%= id %>">
                   	<input type="hidden" name="supplier_name" value="<%= URLDecoder.decode(supplier_name, "UTF-8") %>">
                   </td>
               </form>
           </tr>                    
       <%}%>
     </tbody>
     </table>
<% }
}

if(request.getParameter("code1")!= null)
{
    %>
  <form method="post" action="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=modify_supplier&code2=yes" onsubmit='return validateForm(this)'>
       <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">Modify Supplier</TH></TR>
        <tr>
            <th align="left"  class="standard">Supplier Name:</th>
            <td ><input type="text" class="w200" name="supplier_name" value="<%= Util.encodeTag(supplier.getSupplier_name()) %>">
                 <input type="hidden" name="id" value="<%= supplier.getId() %>"></td>               
        </tr>
    </table><br>
    <input class="submit" align="left" type="submit" name="Submit" value="Submit">&nbsp;
    <input class="submit" type="Reset" align="right">&nbsp;
    <input class="submit_nowidth" align="left" type="submit" name="Delete" value="Delete Supplier" onclick="this.form.action='<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=modify_supplier&code3=yes'">
 </form>
<%  
}

if(request.getParameter("code2")!= null)
{
    supplier.modifySupplier();

    if(supplier.isControl()==true)
    {
      %>  <h3>You have successfully modified the supplier name to <%= chemicalinventory.utility.Util.encodeTag(supplier.getSupplier_name())%>.</h3><%
    }
    else
    {
       %> <h3>ERROR the supplier could not been modified!</h3> <% 
    }
}

if(request.getParameter("code3")!= null)
{
    supplier.deleteSupplier();

    if(supplier.isControl()==true)
    {
      %>  <h3>The supplier is now deleted from the system.</h3><%
    }
    else
    {
       %> <h3>ERROR the supplier could not be deleted!</h3> <% 
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