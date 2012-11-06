<%@ page language="java" %>
<%@ page import="chemicalinventory.context.Configuration" %>

<%
/*
 * Set up the configuraion of the application.
 * Read all the configuration parameters from the configuration file and request
 */
 
 Configuration.initInstance(request, application);
 Configuration conf = Configuration.getInstance(); 
 boolean status = conf.isStatus();
 String app = request.getContextPath();
%>
<HTML>
<HEAD>
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
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=app%>/styles/Style.css">
<TITLE>Login</TITLE>
</HEAD>
<body onload="document.login.j_username.focus()">
<H2>Please enter username and password</H2>
<img src="<%=app%>/images/Divider1a800.png">
<center>
<FORM METHOD="POST" ACTION="j_security_check" name="login" target="Main">
	<TABLE class="box" cellpadding="1" cellspacing="1" width="300" bgcolor="white">
		<TR><TH colspan="3" class="blue">Login:</TH></TR>
		<TR><TD>&nbsp;</TD></TR>
		<TR>
			<TD width="25">&nbsp;</TD>
			<TD align="left">User Name:</TD>
			<TD><INPUT type="TEXT" NAME="j_username" tabindex="1" style="width: 160;"></TD>
		<TR>
			<TD width="25">&nbsp;</TD>
			<TD align="left">Password:</TD>
			<TD><INPUT TYPE="PASSWORD" NAME="j_password" tabindex="2" style="width: 160;"></TD>
		</TR>
		<%
		if(request.getParameter("login_error") != null)
		{
		%>
		<TR>
			<TD colspan="3">
				<SPAN class="red">
					<B>Login failed, please try again!</B>
				</SPAN>
			</TD>
		</TR>
		<%
		}
		else
		{
		%><TR><TD>&nbsp;</TD></TR><%
		}
		%>
	</TABLE>
	<br>
<%
String disabled = "";
if(!status)
{
disabled = "disabled=\"disabled\"";
}
%>
	<INPUT TYPE="SUBMIT" VALUE="Login" <%=disabled%>
		style="width: 105px; height: 19; background-color: #F5F5F5; font-size: 9;"
		tabindex="3">&nbsp;&nbsp;&nbsp; 
	<INPUT type="reset" value="Reset" <%=disabled%>
		style="width: 105px; height: 19; background-color: #F5F5F5; font-size: 9;"
		tabindex="4">
	<%
%>
</FORM>
</CENTER>
<%
if(!status)
{
	%>
	<BR>
	<HR>
	<P style="color: red;">
		<B>
			<I>WARNING!</I> This application is not properly configured!!<BR>
			                Reload this page!<BR>
			                If the problem persits contact your administrator.
		</B>
	</P>
	<%
}
%>
</BODY>
</HTML>