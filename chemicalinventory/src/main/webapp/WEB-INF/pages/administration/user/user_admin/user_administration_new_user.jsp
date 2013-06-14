<%@ page language="java" import="java.util.*" import="java.net.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="userinfo" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="reg" class="chemicalinventory.beans.UserRegBean" scope="page"/>
<jsp:setProperty name="reg" property="*"/>
<jsp:useBean id="u_group" class="chemicalinventory.groups.User_group"/>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:useBean id="userTypeBean" class="chemicalinventory.user.UserTypeBean" scope="page"/>

<%String pageBase = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_new_user.jsp";%>

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
<script language="JavaScript" src="../../script/overlib.js"></script>
<title>
Register a new user.
</title>
<script LANGUAGE="JavaScript">
var user_name=new Array();
<%  userinfo.userName_used();
    Vector un_taken = userinfo.getUn_taken();

    for(int i=0; i<un_taken.size(); i++)
    {
       String name = (String) un_taken.elementAt(i);
      %>user_name[<%= i %>] = "<%= name %>";<%
    }  
%>

function user_validator()
{
	var un = document.user.userName.value;
	un = un.toUpperCase();
        var checkno = 0;

	for(i = 0; i <= user_name.length; i++)
	{
		if(user_name[i] == un)
		{
            checkno = 1;
			return checkno;
		}
	}
        return checkno;
}

function validateForm(form) 
{
 if (trim(form.firstName.value) == "") 
 {
  alert("Please fill in a valid first name!");
  form.firstName.focus();
  return false;
 }
 if (trim(form.lastName.value) == "") 
 {
  alert("Please fill in a valid last name!");
  form.lastName.focus();
  return false;
 }
 if (trim(form.telephone.value) == "") 
 {
  alert("Please fill in a valid telephone number!");
  form.telephone.focus();
  return false;
 }
 else
 {
   if(isValidPhoneNumber(form.telephone.value)==false)
   {
    alert("Please enter a valid phone number. (use numbers and ()-+ characters only!)");
    form.telephone.focus();
    return false;
   }
 }
 if (trim(form.userName.value) == "") 
 {
  alert("Please fill in a valid username!");
  form.userName.focus();
  return false;
 }
 if (user_validator() == 1)
 {
   alert("The entered username cannot be used as it has allready been taken!");
   form.userName.focus();
   return false; 
 }
 if(isValidEmail(form.email.value)==1)
 {
    alert("Please fill in a valid email adress, the format is xxxx@xxx.xx (ex chemical@inventory.com)");
    form.email.focus();
    return false;
 }
 if(trim(form.password.value).length <5)
 {
  alert("Please fill in a password with more than 5 characters!");
  form.password.focus();
  return false;
 }
return true;
}
</script>
</head>
<%if(request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("errorcode2")==null && request.getParameter("errorcode2")==null)
{%>
<body onload="document.user.firstName.focus()">
<%}
else
{%>
<body>
<% 
}

String user = request.getRemoteUser();
int id = 0;
int tab = 0;
Vector privileges = null;

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
if(request.getParameter("code1")!=null) 
{
  String firstName = request.getParameter("firstName");
  String lastName = request.getParameter("lastName");
  String userName = request.getParameter("userName");
  String telephone = request.getParameter("telephone");
  String email = request.getParameter("email");
  
try 
{
  if(!firstName.equals("") && !lastName.equals("") && !userName.equals("") &&  !telephone.equals("") && !email.equals(""))
  {
  	reg.setGroups(request.getParameterValues("groups"));
    int tal = reg.regCheck();
    
    if(tal == 2)//alles ok
    {
      response.sendRedirect(pageBase+"?code2=yes&id="+reg.getAutoIncKey()+"&password_returner="+reg.getPassword_returner());
    }
    else if(tal == 1)//Username taken
    {
      response.sendRedirect(pageBase+"?errorcode1=yes");
    }
    else if(tal == 0)//Error
    {
      response.sendRedirect(pageBase+"?errorcode2=yes");
    }
    else
    {
      response.sendRedirect(pageBase+"?errorcode2=yes");
    }
    
  }
  else
  {
    response.sendRedirect(pageBase+"?errorcode2=yes");
  }
}
  catch (Exception e)
  {
     response.sendRedirect(pageBase+"?errorcode2=yes");
  }
}

if (request.getParameter("code2")!=null) 
{
  String uid = request.getParameter("id");
  boolean _status = reg.userCred(uid);
  
%><h3>Status of user creation</h3><%
  if(!_status)
  {
  %>
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="450">
  		<TR><TH colspan="4" class="blue">User Creation Status</TH></TR>
	    <tr>
	        <td><i>...User not created</i></td>
	    </tr>
  </table>
  <%
  }
  else
  {
  %>
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="450">
  		<TR><TH colspan="4" class="blue">User Creation Status</TH></TR>
	    <tr>
	        <th align="left" class="standard">First Name</th>
	        <td colspan="2"><%=reg.getFirstName()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Last Name</th>
	        <td colspan="2"><%=reg.getLastName() %></td>
	    </tr>
	    <tr>
	         <th align="left" class="standard">Room Number</th>
	         <td colspan="2"><%=reg.getRoom_number()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Telephone Number</th>
	        <td colspan="2"><%= reg.getTelephone()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">E-mail</th>
	        <td colspan="2"><%=reg.getEmail() %></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User Name</th>
	        <td colspan="2"><%=reg.getUserName() %></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User Type</th>
   	        <td colspan="2"><%=reg.getUserType() %></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User groups</th>
	        <td colspan="2"><%
	        String groups[] = reg.getGroups();
	        if(groups != null) {
	        		for (int i = 0; i < groups.length; i++) {
	        			String output = (String) groups[i];
	        			%><%=output%><%        									
					}
	        }
	        %></td>
	    </tr>
    </table><br>
<%
  }
  //Send the mail to the new user with information on the profile.
  String new_password = request.getParameter("password_returner");
  mail.setFilepath(Attributes.STYLE_SHEET_FOLDER_REALPATH+"/Style.css");
  mail.setMessage(mail.composeNewUserMail(reg, new_password));
  mail.setTo(reg.getEmail());
  mail.setFrom(userinfo.getEmail(user));
  mail.sendMail("new_user"); //send the mail
  boolean status = mail.isStatus();
  if(status)//Message sent succesfully to the new user display message
  {
  	%>
  	  <hr>
  		<p>Message sent to the entered e-mail address.</p>
  	<%
  }
  else//message could not be sent to the new user.. display error message.
  {
  	%>
  		<hr>
  		<p>Message could NOT be sent to the entered e-mail address.<br>
  		   Password for the user profile: <b><%=reg.getPassword_returner()%></b></p>
  	<%
  }
}

if (request.getParameter("errorcode1")!=null) 
{
  %>
  Creation of new user failed. The username is taken.
  <BR><HR>
  <%    
}

if (request.getParameter("errorcode2")!=null) 
{
  %>
  Creation of new user failed. Error in form.
  <BR><HR>
  <%    
}
if (request.getParameter("errorcode2")==null && request.getParameter("errorcode1")==null && request.getParameter("code2")==null && request.getParameter("code1")==null) 
{%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
</center>
<h2>User Registration</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
Enter the data for the new user.
<form method="post" action="<%=pageBase%>?code1=yes" onSubmit="return validateForm(this)" name="user">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="450">
  		<TR><TH colspan="4" class="blue">User Data</TH></TR>
	    <tr>
	        <th align="left" class="standard">First Name</th>
	        <td colspan="2"><input type="text" class="w290" name="firstName" tabindex="1"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Last Name</th>
	        <td colspan="2"><input type="text" class="w290" name="lastName"  tabindex="2"></td>
	    </tr>
	    <tr>
	         <th align="left" class="standard">Room Number</th>
	         <td colspan="2"><input type="text" class="w290" name="room_number"  tabindex="3"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Telephone Number</th>
	        <td colspan="2"><input type="text" class="w290" name="telephone"  tabindex="4"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">E-mail</th>
	        <td colspan="2"><input type="text" class="w290" name="email"  tabindex="5"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User Name</th>
	        <td colspan="2"><input type="text" class="w290" name="userName"  tabindex="6"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Password&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onmouseover="return overlib('This is a readonly value! A random password is generated and sent to the entered e-mail address.', LEFT, BORDER, 2, CAPTION, 'CHEMICAL NAME');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></th>
	        <td colspan="2"><input type="password" class="w290" name="password" value="password" readonly></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Type</th>
	        <td>
		        <select name="user_type_id" tabindex="7">
		        	<option value="0">[--- SELECT ---]</option>
		        
		        <%
		        	Hashtable userTypeTable = userTypeBean.listUserTypes_NO_HTML(request.isUserInRole("adm"));
		        	
		        	for (Enumeration e = userTypeTable.keys() ; e.hasMoreElements() ;) {
				         
						String _id = (String) e.nextElement();
						String value = (String) userTypeTable.get(_id);
						
						%> <option value="<%=_id%>"><%=value%></option><%
				     }
		         %>
		        	
		        </select>       
	        </td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User groups</th>
	        <td colspan="2">
	                   <%
	                        u_group.find_groups(8);
	                        Vector groups = u_group.getAll_groups();
	                        tab = groups.size();
	                        tab = tab + 8;
	                        String tag = null;
	                        for (int i=0; i<groups.size(); ++i)
	                        {
	                            tag = (String) groups.get(i);
	                            out.println(tag);%><br><%
	                        }
	                    %>
	        </td>
	    </tr>
    </table><br>
    <input class="submit" type="submit" name="submit" value="Submit Form">&nbsp;&nbsp;&nbsp;
    <input class="submit" type="Reset" value="Reset">
</form>
<%
}%>
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