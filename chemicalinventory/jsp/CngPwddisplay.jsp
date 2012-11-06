<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="pwd" class="chemicalinventory.beans.ChangePasswordBean"
	scope="page" />
<jsp:setProperty name="pwd" property="*" />
<html>
<head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              neded to manage a chemical storage, based on the java technology.
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
<title>Changing your password</title>
<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.username.value) == "") 
 {
  alert("Please fill in a valid user name!");
  form.username.focus();
  return false;
 }
 if(trim(form.password.value).length <5)
 {
  alert("Please fill in a password with more than 5 characters!");
  form.password.focus();
  return false;
 }
 if(trim(form.newPassword.value).length <5)
 {
  alert("Please fill in a password with more than 5 characters!");
  form.newPassword.focus();
  return false;
 }
 if(trim(form.verifyPassword.value).length <5)
 {
  alert("Please fill in a password with more than 5 characters!");
  form.verifyPassword.focus();
  return false;
 }
return true;
}
</script>
</head>
<%
if (request.getParameter("code1")==null)
{ %>
<body onload="document.pwd.password.focus()">
<%
}
else
{%>
<body>
<%
}

String user = request.getRemoteUser().toUpperCase();

if (request.getParameter("code1")!=null)
{ 
  if(pwd.newPwdOK() == true)
  {
    response.sendRedirect(Attributes.JSP_BASE+"?action=ChangePwd&code2=yes");
  }
  else
  {
    response.sendRedirect(Attributes.JSP_BASE+"?action=ChangePwd&errorcode1=yes");
  }
}
%>
<h2>Change password</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<center>

<FORM METHOD="POST" ACTION="<%= Attributes.JSP_BASE %>?action=ChangePwd&code1=yes"
	onSubmit="return validateForm(this)" name="pwd">
<TABLE class="box" cellpadding="1" cellspacing="1" width="360" bgcolor="#ffffff">
<TR><TH colspan="3" class="blue">Change Password:</TH></TR>
	<tr>
		<th align="left" class="standard">User Name</th>
		<td><INPUT class="w200" TYPE="TEXT" NAME="username" value="<%= user %>"
			readonly="readonly"></td>
	</tr>
	<tr>
		<th align="left" class="standard">Current Password</th>
		<td><INPUT class="w200" TYPE="PASSWORD" NAME="password" tabindex="1"></td>
	</tr>
	<tr>
		<th align="left" class="standard">New Password</th>
		<td><INPUT class="w200" TYPE="PASSWORD" NAME="newPassword" tabindex="2"></td>
	</tr>
	<tr>
		<th align="left" class="standard">Verify new Password</th>
		<td><INPUT class="w200" TYPE="PASSWORD" NAME="verifyPassword" tabindex="3"></td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
<br>
<INPUT class="submit_width125" TYPE="SUBMIT" VALUE="Change password"
	tabindex="4">&nbsp;&nbsp;&nbsp; <input class="submit_width125"
	type="Reset" tabindex="5">
</form>

<%

if (request.getParameter("code2")!=null) 
{
  %> <br>
<h3>Your password has now been changed.</h3>
<BR>
<%    
}

if (request.getParameter("errorcode1")!=null) 
{
  %> <br>
<h3>Attempt to change password failed. Please try again</h3>
<BR>
<%    
}
%></center>
</body>
</html>