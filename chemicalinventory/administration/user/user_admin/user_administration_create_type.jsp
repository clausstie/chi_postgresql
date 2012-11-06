<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:directive.page import="chemicalinventory.utility.Return_codes"/>
<jsp:useBean id="privl" class="chemicalinventory.user.PrivilegesBean" scope="page"/>
<jsp:useBean id="userType" class="chemicalinventory.user.UserTypeBean" scope="page"/>
<jsp:setProperty name="userType" property="*"/>

<%String pageBase = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_create_type.jsp";%>

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
Reset a users password
</title>
</head>
<body >

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
<center>
<%
if(request.getParameter("code1")!=null)
{%> 
</center>
<h2>Create definition for new user type</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<form method="post" action="<%=pageBase%>?code2=yes" name="type">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="560">
 		<TR><TH colspan="2" class="blue">User Type Definition</TH></TR>
		<tr>
		    <th align="left" class="standard">User Type Name:</th>
		    <td ><input type="text" name="name" class="w400" tabindex="1"></td>
		</tr>
		<TR><TH colspan="2" class="blue">Add privileges</TH></TR>
		<tr>
	        <th align="left" class="standard">Priviliges:</th>
	        <td >
	        	 <%
	                        privl.find_privileges(2);
	                        Vector p_list = privl.getAll_privileges();
	                        String p_tag = null;
	                        for (int n=0; n<p_list.size(); ++n)
	                        {
	                            p_tag = (String) p_list.get(n);
	                            %><%=p_tag%><br><%
	                        }
	              %>
	         </td>
	    </tr>
	</table><br>
  <input class="submit" type="submit" name="submit" value="Submit Form">&nbsp;&nbsp;&nbsp;
  <input class="submit" type="Reset" value="Reset">
</form>
<%
}

if(request.getParameter("errorcode1")!=null) 
{
  %>
	<BR><HR>
	<h3>Creation failed, please try again!</h3>
  <%    
}
if (request.getParameter("errorcode2")!=null) 
{
  %>
	<BR><HR>
	<h3>Creation failed, please fill in a valid name for the new usertype!</h3>
  <%    
}

if (request.getParameter("errorcode3")!=null) 
{
  %>
	<BR><HR>
	<h3>Creation failed, please select one or more privileges!</h3>
  <%    
}
if (request.getParameter("errorcode4")!=null) 
{
  %>
	<BR><HR>
	<h3>Creation failed, No database connection!</h3>
  <%    
}

if(request.getParameter("code2")!= null)
{
 //Create the new user type.
 
 String[] p_selected = request.getParameterValues("privileges");
 userType.setPrivileges(p_selected);
 int status = userType.registerUserType();
 int user_type_id = userType.getUser_type_id();
 
 if(status == Return_codes.SUCCESS) {	
 	response.sendRedirect(pageBase+"?user_type_id="+user_type_id+"&success=yes");
 }
 else if(status == Return_codes.CREATION_FAILED) {
	 response.sendRedirect(pageBase+"?errorcode1&code1=yes");
 }
 else if(status == Return_codes.MISSING_NAME) {
 	 response.sendRedirect(pageBase+"?errorcode2&code1=yes");
 }
 else if(status == Return_codes.MISSING_PRIVILEGES) {
 	 response.sendRedirect(pageBase+"?errorcode3&code1=yes");
 }
 else if(status == Return_codes.CONNECTION_ERROR) {
 	 response.sendRedirect(pageBase+"?errorcode4&code1=yes");
 }
}

if (request.getParameter("success")!=null) {
		//get information about the usertype
	int user_type_id = Integer.parseInt(request.getParameter("user_type_id"));
	userType.displayUserTypeReadOnly(user_type_id);
%> 
</center>
<h2>New user type added</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="560">
 		<TR><TH colspan="2" class="blue">New User Type Definition</TH></TR>
		<tr>
		    <th align="left" class="standard">User Type Name:</th>
		    <td><%= userType.getName() %></td>
		</tr>
		<TR><TH colspan="2" class="blue">Selected Privileges</TH></TR>
		<tr>
	        <th align="left" class="standard">Priviliges:</th>
	        <td >
	        	 <%
	                        Vector p_list = userType.getPrivileges_list();
	                        String p_tag = null;
	                        for (int n=0; n<p_list.size(); ++n)
	                        {
	                            p_tag = (String) p_list.get(n);
	                            %><%=p_tag%><br><%
	                        }
	              %>
	         </td>
	    </tr>
	</table><br>
<%
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