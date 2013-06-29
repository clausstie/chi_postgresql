<%@ page language="java" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="registration" class="chemicalinventory.utility.registration.RegistrationHandler" scope="page"/>
<jsp:setProperty property="*" name="registration"/>
<%@page import="chemicalinventory.context.Attributes"%>
<%
if(request.getParameter("register") != null) {

	boolean status = registration.performRegistration();

	if(status) {
		response.sendRedirect("http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/registration/RegisterCI.jsp?success=yes");
	}
	else {
		response.sendRedirect("http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/registration/RegisterCI.jsp?error=yes");
	}
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
  
  	<title>Register chemicalInventory Instance</title>
	<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
  </head>
  <body>
  <%
  	if(request.getParameter("error")!= null) {
  		%>
  		<hr>
  		<h2 style="color:red">Registration failed, please try again.</h2>
  		<h3 style="color:red">For help or support write to info@chemicalinventory.org</h3>
  		<hr>
  		<br>
  		<%
  	}
  	if(request.getParameter("success") != null) {
  		%>
  		<hr>
  		<h2 style="color:blue">Registration successful, thank you very much for using chemicalInventory</h2>
  		<a href="javascript:window.close();" style="color:black;">Close Window</a>
  		<hr>
  		<br>
  		<%
  	} else { %>  
  <center>  
	<form action="http://<%=Attributes.IP_ADDRESS%>:<%=Attributes.PORT%><%=Attributes.APPLICATION%>/registration/RegisterCI.jsp?register=yes" method="post">
  		<TABLE class="box" cellpadding="1" cellspacing="1" width="650" bgcolor="#ffffff">
			<TR><TH colspan="2" class="blue">Registration</TH></TR>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><p>Please register your version of chemicalInventory. Your data will under no circumstances
				be shared, sold or handed over to any third party.
				The data is used for statistics purposes only... We will not contact you unless you you give permission in the 
				checkbox below...</p>
				<p>Headings in red are mandatory.</p>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<TR>
				<TH align="left" style="width:240"><p style="color:red">Organisation Name *</p></TH>
				<TD><input class="w400" type="text" name="organisation_name"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Department</TH>
				<TD><input class="w400" type="text" name="organisation_department"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Address</TH>
				<TD><input class="w400" type="text" name="address1"></TD>
			</TR>
				<TR>
				<TH align="left" style="width:240">Address</TH>
				<TD><input class="w400" type="text" name="address2"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Zip</TH>
				<TD><input class="w400" type="text" name="zip"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">City</TH>
				<TD><input class="w400" type="text" name="city"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Country</TH>
				<TD><input class="w400" type="text" name="country"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">State</TH>
				<TD><input class="w400" type="text" name="state"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Contact Person</TH>
				<TD><input class="w400" type="text" name="contact_person"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Contact Email</TH>
				<TD><input class="w400" type="text" name="contact_email"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Contact Telephone</TH>
				<TD><input class="w400" type="text" name="contact_telephone"></TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Expected # Users</TH>
				<TD>
					<select name="no_users" style="width=200">
						<option value="0-10">0-10</option>
						<option value="10-20">10-20</option>
						<option value="20-30">20-30</option>
						<option value="30-50">30-50</option>
						<option value="50-100">50-100</option>
						<option value="100+">100+</option>						
					</select>
				</TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Allow Contact??</TH>
				<TD><input type="checkbox" name="contact" value="allow">Yes, chemicalInventory team may contact us...??</TD>
			</TR>
			<TR>
				<TH align="left" style="width:240">Remark</TH>
				<TD><textarea cols="48" rows="10" name="remark"></textarea></TD>
			</TR>
		</TABLE>
		<br>
		<input class="submit" type="submit" name="Register Now" value="Register Now">&nbsp;&nbsp;&nbsp;
		<input class="submit" type="submit" value="Register later" onclick="this.form.action='';javascript:window.close();">
	</form>
  </center>
  <% 
  }
  %>
  </body>
</html>