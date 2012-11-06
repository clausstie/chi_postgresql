<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:useBean id="userinfo" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyUserBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>

<%String pageBase = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_reset_pwd.jsp";%>

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
<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 var str = trim(form.newPassword.value);
 if (str.length < 5) 
 {
  alert("Please fill in a valid password! (min. length is 5 characters)");
  form.newPassword.focus();
  return false;
 }
return true
}
</script>
</head>
<%
if(request.getParameter("code2")==null)
{
%>
<body onload="document.pwd.user_name.focus()">
<%
}
else if (request.getParameter("code2")!=null)
{%>
<body onload="document.pwd2.newPassword.focus()">
<%
}
%>

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
if (request.getParameter("errorcode1")!=null) 
{
  %>
  An error orcurred - the password has not been changed.
  <BR><HR>
  <%    
}

if(request.getParameter("code2")==null)
{%> 
</center>
<h2>Reset the password for an existing user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
To reset an existing user's password enter user name of the user.
<form method="post" action="<%=pageBase%>?code1=yes" name="pwd">
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
    <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
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
   <table class="box" cellspacing="1" cellpadding="1" width="100%" align="center">
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
            <td align="center" ><input type="hidden" name="id" value="<%= id %>">
								<input type="hidden" name="user_name" value="<%= modify.getUser_name() %>">            
            					<input class="submit_width85" type="submit" name="Select" value="Modify"></td>
          </form>  
        </tr>            
     <%}%>
   </tbody>
   </table>
   <br>
   <hr>
<%
  }
}
}

if(request.getParameter("code2")!= null)
{
	  modify.getUserInfo(2, request.isUserInRole("adm"));
	%>
	</center>
	<h2>Reset password</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<center>
	<p>Press 'submit' to generate a randomized new password for the user. The value in the password box, cannot be changed!</p>
	<form method="post" action="<%=pageBase%>?code3=yes" onSubmit="return validateForm(this)" name="pwd2">
	  <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
	 		<TR><TH colspan="4" class="blue">User Data</TH></TR>
			<tr>
			    <th align="left">User Name</th>
			    <td colspan="2"><input type="text" name="user_name" value="<%= modify.getUser_name() %>" readonly></td>
			</tr>
			<tr>
			    <th align="left">Password</th>
			    <td colspan="2"><input type="password" name="newPassword" value="password" readonly="readonly">
			    <input type="hidden" name="id" value="<%= modify.getId() %>"></td>
			</tr>
		</table><br>
	  <input class="submit" type="submit" name="submit" value="Submit Form">&nbsp;&nbsp;&nbsp;
	  <input class="submit" type="Reset" value="Reset">
	</form>
<%
}

if(request.getParameter("code3")!= null)
{
  modify.resetPassword();

    %><br><hr><br><h3>Result of your update</h3><%

    if(modify.pwdUpdate == true)
    {
      %><p>The password for <%= modify.getUser_name() %> has been changed. <%
      
      //Send the mail to the new user with information on the profile.
		  mail.setTo_fullname(userinfo.getFullName(modify.getUser_name()));//Set the full name of the user..
		  mail.setMessage(mail.composeNewPasswordMail(modify.getNewPassword()));//compose the message to the user with the new password.
		  mail.setTo(userinfo.getEmail(modify.getUser_name()));//Set the to mail address
		  mail.setFrom(userinfo.getEmail((String) request.getRemoteUser()));//Set the from mail address
		  mail.sendMail("password");//Send the mail to the user
		  boolean status = mail.isStatus();
			if (status == true)//message sendt succesfully
			{
				%>
						Message sent to the user with the new password!</p>
				<%
			}
			else//error in sending mail message.
			{
				%>
						<p>Message could NOT be sent to the user!<br>
						   New password value: <%=modify.getNewPassword()%></p>
				<%		
			}
    }
    else
    {
      %><p>ERROR  -  The password for <%= modify.getUser_name() %> could NOT be changed</p><%
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