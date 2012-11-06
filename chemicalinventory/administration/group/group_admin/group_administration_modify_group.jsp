<%@ page language="java" import="java.util.*" import="java.net.*" import="chemicalinventory.utility.Util"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="modify_group" class="chemicalinventory.groups.Modify_group" scope="page"/>
<jsp:useBean id="display_group" class="chemicalinventory.groups.User_group" scope="page"/>
<jsp:setProperty name="modify_group" property="*"/>
<jsp:setProperty name="display_group" property="*"/>
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
Modify an existing user group.
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
<body>
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
<h2>Modification of an existing group</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("code3")==null) 
{
/*find all groups and put them in the container.*/
 display_group.show_all_groups();
 
 if(display_group.getAll_groups().isEmpty())
    {
    %>
      <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
      <thead><tr> <th class="blue">Id:</th>
                  <th class="blue">Group Name:</th>
             </tr>
      </thead>
      <tbody>
         <tr align="center">
         <td colspan="9">Error the list of groups is empty.</td>
         </tr>
      </tbody>
      </table>
    <%
    }
    else
    {
    %>
     <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
        <thead>
            <tr class="special"> 
                  <th class="blue">Id:</th>
                  <th class="blue">Group Name:</th>
                  <th class="blue"></th>
             </tr> 
        </thead>
        <tbody>
      <%
        Vector groups = display_group.getAll_groups();
        Vector ids = display_group.getAll_groups_id();
         for(int i=0; i<groups.size(); i++)
         {
            String id = (String) ids.elementAt(i);
            String gr_name = (String) groups.elementAt(i);
            String color = "normal";
            if(i % 2 != 0)
            {
                 color = "blue";
            }
         %><tr class="<%= color %>">
               <td align="center"><%= id %></td>
               <td align="center"><%= URLDecoder.decode(gr_name, "UTF-8") %></td>
               <form method="post" action="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=modify_group&code1=yes">
                   <td align="center">
                   	<input class="submit_width80" type="submit" name="Select" value="Modify">
                    <input type="hidden" name="id" value="<%= id %>">
                   	<input type="hidden" name="name" value="<%= URLDecoder.decode(gr_name, "UTF-8") %>">
                   </td>
               </form>
           </tr>                    
       <%}%>
     </tbody>
     </table>
<% }
}
/*Show information on the selected group, and get ready to modify or delete*/
if(request.getParameter("code1")!=null) 
{
%>
  <form method="post" action="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=modify_group&code2=yes" name="modify" onsubmit='return validateForm(this)'>
	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="4" class="blue">Group</TH></TR>
            <tr>
                <th align="left" class="standard">Group Name:</th>
                <td><input type="text" name="name" value="<%= Util.encodeTag(modify_group.getName()) %>" class="w200">
                    <input type="hidden" name="id" value="<%= modify_group.getId()%>"></td>
            </tr>
     </table><br>
    <input class="submit" align="left" type="submit" name="Submit" value="Submit">&nbsp;
    <input class="submit" type="Reset" align="right">&nbsp;
    <input align="left" class="submit_nowidth" type="submit" name="Delete" value="Delete Group" onclick="this.form.action='<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=modify_group&code3=yes'">
 </form>
<%
}
/*Modify the name of the groou*/
if(request.getParameter("code2")!=null) 
{
    modify_group.modify_group_name();
    if(modify_group.isUpdate())
    {
        %><h3>The group name has been succesfully modified to '<%=modify_group.getName()%>'</h3><%
    }
    else
    {
        %><h3>Error! The group name could not be modified.</h3><%
     }
}
/*Delete the group, and delete all entries in the user and
container link table with this group...*/
if(request.getParameter("code3")!=null) 
{
    modify_group.delete_group();
    if(modify_group.isUpdate())
    {
        %><h3>The group name has been deleted</h3><%
    }
    else
    {
        %><h3>Error! The group name could not be deleted.</h3><%
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
