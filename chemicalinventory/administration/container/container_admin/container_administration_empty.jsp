<%@ page language="java" import="java.text.*" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="container" class="chemicalinventory.beans.ContainerRegBean" scope="page"/>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyContainerBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>
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
<title>
This page is for modifying information stored about a container. (empty container)
</title>
</head>
<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null)
{
%>
<body onload="document.container.container_id.focus()">
<%
}
else
{%>
<body>
<%
}
%>
<%
  String user = request.getRemoteUser();
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_container.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer&code1=yes">Register a new container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">Modify an existing container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=emptyContainer">Register a container as empty</a> |
</span>
<span class="textboxadm">
<h2>Register a container as empty</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>

<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null)
{
%>
<form method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=emptyContainer&code1=yes" name="container">
 	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="2" class="blue">Container</TH></TR>
        <tr>
            <th align="left" class="standard">Container id:</th>
            <TD><input type="text" name="container_id" class="w200"></TD>
        </tr>
    </TABLE>
    <br>
    <input class="submit" type="Submit" name="Submit" value="Submit">
</form>
<%
}

if(request.getParameter("code1")!=null)
{
  modify.setContainerInfo(modify.getContainer_id());//get the initial information about the container to alter.

    String sup_display = modify.getSupplier();

    if (sup_display == null)
            sup_display = "-";


  if(modify.getSearchOk() == true)//a valid container - display the info.
  {
    %>
    <form name="locationChoices" method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=emptyContainer&code2=yes">
 	<TABLE class="box" cellpadding="1" cellspacing="2" width="500">
		<TR><TH colspan="2" class="blue">Container</TH></TR>
            <tr>
                <th align="left" class="standard">Chemical name:</th>
                <td><%= modify.getChemical_name() %>
                	<input type="hidden"  value="<%= modify.getChemical_name() %>" name="chemical_name" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Container id:</th>
                <td><%= modify.getContainer_id() %>
                	<input type="hidden" value="<%= modify.getContainer_id() %>" name="container_id" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Supplier:</th>
                <td><%= sup_display %></td>
            <tr>
                <th align="left" class="standard">Location:</th>
                <td><%= modify.getLocation()%></td>
            </tr>
            <tr>
                <th align="left" class="standard">Quantity:</th>
                <td><%= modify.getQuantity()%>&nbsp;<%=modify.getUnit()%>
                 <input type="hidden" value="<%= modify.getQuantity()%>" name="quantity" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Tara Weight:</th>
                <td><%= modify.getTara_weight() %>&nbsp;gram</td>
            </tr>
            <tr>
                <th align="left" class="standard">Register date:</th>
                <td><%= modify.getRegister_date() %></td>
            </tr>
            <tr>
                <th align="left" class="standard">Register by:</th>
                <td><%= modify.getRegister_by()%></td>
            </tr>
        </table><br>
        <input class="submit_nowidth" type="submit" name="Register as empty" value="Register as empty" tabindex="1">
        <input class="submit" type="Submit" name="Cancel" value="Cancel" onclick="this.form.action='<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=emptyContainer'">
     </form>
<%}
  else
  {
    %> <h3>An error orcurred. Please try again, and make sure the entered container
           id is valid, and it is not an empty container.</h3>
       
        <form method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=emptyContainer&code1=yes" name="container">
            <table class="special" cellspacing="1" cellpadding="1" border="1">
                <tr>
                    <th>Container Id:</th>
                    <td><input type="text" name="container_id" tabindex="1"></td>
                </tr>
            </table><br>
            <input class="submit" type="Submit" name="Submit" value="Submit">
        </form>
<%
  }
}
%>

<%
if(request.getParameter("code2")!=null)
{
	modify.setUser(user);
    modify.emptyContainer();
%>
<h3>The result of your update:</h3>

   <%if(modify.isEmpty())
     {
        %><h4>The container has been registered as empty. This container is no longer
              available in the system.</h4><%
      }
      else
      {
       %><h4>The container could not be registered as empty.
             Try agian later, and if the error persists, contact the 
             system administrator.</h4><%
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
