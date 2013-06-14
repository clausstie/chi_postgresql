<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyUserBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>

<%String pageBase = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_remove.jsp";%>

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
Remove user.
</title>
</head>
<%
if(request.getParameter("code2")==null)
{%> 
<body onload="document.user.user_name.focus()">
<%
}
else
{
%>
<body>
<%
}%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_user.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_new_user.jsp">Register a new user</a> |
     <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_modify_user.jsp">Modify an existing user</a> |
     <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_reset_pwd.jsp">Reset an users password</a> |
     <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_remove.jsp">Remove an user</a> |
     <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_create_type.jsp?code1=yes">Create user type</a> |
     <a class="adm" href="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/administration/user/user_admin/user_administration_modify_type.jsp?select=yes">Modify user type</a> |
</span>

<span class="textboxadm">
<%
if (request.getParameter("errorcode1")!=null) 
{
  %>
  The user could not be remved. An error orcurred.
  <BR><HR>
  <%    
}

if(request.getParameter("code2")==null)
{%> 
<h2>Remove a user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
To remove a existing user from the system use this page. 
<form method="post" action="<%=pageBase%>?&code1=yes" name="user">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">User Data</TH></TR>
		<tr>
		    <th align="left" class="standard">User Name:</th>
		    <td colspan="2"><input type="text" name="user_name" class="w200"></td>
		</tr>
	</table><br>
  <input class="submit" type="submit" name="submit" value="Submit Form">&nbsp;&nbsp;&nbsp;
  <input class="submit" type="Reset" value="Reset">
</form>

<%
if(request.getParameter("code1")!= null)
{
  modify.getUserInfo(1, request.isUserInRole("adm"));
  %><br><hr><br><h3>Result of your search for users</h3><%
  if(modify.result.isEmpty())
  {
  %>
    <table cellspacing="1" cellpadding="1" border="1" width="65%" rules="rows" align="center">
    <thead><tr> <th class="blue">User Name:</th>
                <th class="blue">Full Name</th>
                <th class="blue">Telephone:</th>
                <th class="blue">E-mail:</th>
                </tr> </thead>
    <tbody>
       <tr align="center">
       <td colspan="9">NO USERS MATCHING YOUR SEARCH CRITERIA.</td>
       </tr>
    </tbody>
    </table>
  <%
  }
  else
  {
  %>
   <table class="box" cellspacing="1" cellpadding="1" border="0" width="100%" rules="rows" align="center">
    <thead><tr class="special"> 
                <th class="blue">User Name:</th>
                <th class="blue">Full Name</th>
                <th class="blue">Telephone:</th>
                <th class="blue">E-mail:</th>
                <th class="blue"></th>
               </tr> </thead>
    <tbody>
    <%
       for(int i=0; i<modify.result.size(); i++)
       {
          String color = "normal";
          if(i % 2 != 0)
          {
            color = "blue";
          }
       %><tr class="<%= color %>"><%
          String id = (String) modify.result_id.elementAt(i);
          String data = (String) modify.result.elementAt(i);
          StringTokenizer tokens = new StringTokenizer(data, "|", false);
          while(tokens.hasMoreElements())
          {
            String token = tokens.nextToken();
            token.trim();
            %><td align="center"><%              
                    out.println(token);
                  %>
              </td>
        <%}%>
          <form method="post" action="<%=pageBase%>?code2=yes">
            <td align="center">
            	<input class="submit_width85" type="submit" name="Select" value="Modify">
            	<input type="hidden" name="id" value="<%= id %>">
            </td>
        </tr>
          </form>                   
     <%}%>
   </tbody>
   </table>
   <br>
<%
  }
}
}

if(request.getParameter("code2")!= null)
{
  modify.getUserInfo(2, request.isUserInRole("adm"));
%>
</center>
<h2>Remove a user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
Use this page to remove a user from the system.
<form method="post" action="<%=pageBase%>?code3=yes" name="user">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">User Data</TH></TR>
		<tr>
		    <th align="left" class="standard">User Name</th>
		    <td colspan="2"><input type="text" class="w200" name="user_name" value="<%= modify.getUser_name() %>" readonly></td>
		</tr>
		<tr>
		    <th align="left" class="standard">Full name</th>
		    <td colspan="2"><input type="text" class="w200" name="full_name" value="<%= modify.getFirst_name() %> <%= modify.getLast_name() %>" readonly></td>
		</tr>
		<tr>
		    <th align="left" class="standard">Remove user??</th>
		    <td colspan="2"><input type="checkbox" name="remove">
		    <input type="hidden" name="id" value="<%= modify.getId() %>"></td>
		</tr>
	</table><br>
	<input class="submit_nowidth" type="submit" name="submit" value="REMOVE USER">
</form>
<%}
if(request.getParameter("code3")!= null)
{
  modify.removeUser();
  if(modify.delete==true)
  {
    %><h4> the user <%= modify.getUser_name()%>, <%= modify.getFirst_name()%> <%=modify.getLast_name()%> has been removed from the system.</h4><%
  }
  else
  {
    %><h4> Error the user could not be removed!!</h4><%
  }
}%>
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