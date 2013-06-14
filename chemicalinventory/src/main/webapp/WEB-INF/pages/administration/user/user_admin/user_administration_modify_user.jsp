<%@ page language="java" import="java.util.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:directive.page import="chemicalinventory.beans.UserRegBean"/>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyUserBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>
<jsp:useBean id="group" class="chemicalinventory.groups.User_group" scope="page"/>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:useBean id="userinfo" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="privileges" class="chemicalinventory.user.PrivilegesBean" scope="page"/>
<jsp:useBean id="userTypeBean" class="chemicalinventory.user.UserTypeBean" scope="page"/>

<%String pageBase = "http://"+Attributes.IP_ADDRESS+":"+Attributes.PORT+Attributes.APPLICATION+"/administration/user/user_admin/user_administration_modify_user.jsp";%>

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
Register a new user.
</title>
<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.first_name.value) == "") 
 {
  alert("Please fill in a valid first name!");
  form.first_name.focus();
  return false;
 }
 if (trim(form.last_name.value) == "") 
 {
  alert("Please fill in a valid last name!");
  form.last_name.focus();
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
 if(isValidEmail(form.email.value)==1)
 {
    alert("Please fill in a valid email adress, the format is xxxx@xxx.xx (ex chemical@inventory.com)");
    form.email.focus();
    return false;
 }
return true;
}
</script>
</head>
<%
if(request.getParameter("code2")==null && request.getParameter("code3")==null && request.getParameter("errorcode1")==null) 
{%> 
<body onload="document.user.user_name.focus()">
<%
}
else
{%>
<body>
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
  An error orcured.
  <BR><HR>
  <%    
}

if(request.getParameter("code2")==null && request.getParameter("code3")==null && request.getParameter("success")==null)
{%> 
</center>
<h2>Modify an existing user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
To modify an existing user enter user name of the user. 
<form method="post" action="<%=pageBase%>?code1=yes" name="user">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">User Data</TH></TR>
		<tr>
		    <th align="left" class="standard">User Name:</th>
		    <td colspan="2"><input type="text" name="user_name" class="w200"></td>
		</tr>
	</table><br>
  <input class="submit" type="submit" name="submit" value="Submit Form"/>&nbsp;&nbsp;&nbsp;
  <input class="submit" type="Reset" value="Reset"/>
</form>
<%
}

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
            <td align="center">
            	<input class="submit_width85" type="submit" name="Select" value="Modify"/>
            	<input type="hidden" name="id" value="<%=id%>"/>
            </td>
         </form>
        </tr>                   
     <%}%>
   </tbody>
   </table>
   <br>
<%
  }
}

if(request.getParameter("code2")!= null)
{
  modify.getUserInfo(2, request.isUserInRole("adm"));
%>
</center>
<h2>Modify user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
Modify the data in the form to the correct values
<form method="post" action="<%=pageBase%>?code3=yes" name="user" onSubmit="return validateForm(this)">
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="450">
 		<TR><TH colspan="4" class="blue">User Data</TH></TR>
		<tr>
		    <th align="left" class="standard">User Name</th>
		    <td><%= modify.getUser_name()%>
		    	<input type="hidden" class="w290" name="user_name" value="<%=modify.getUser_name()%>" readonly>
		    	<input type="hidden" name="org_user_name" value="<%= modify.getUser_name() %>" readonly></td>
		</tr>
		<tr>
		    <th align="left" class="standard">First Name</th>
		    <td><input type="text" class="w290" name="first_name" value="<%= modify.getFirst_name() %>"></td>
		</tr>
		<tr>
		    <th align="left" class="standard">Last Name</th>
		    <td><input type="text" class="w290" name="last_name" value="<%= modify.getLast_name() %>"></td>
		</tr>
		<tr>
		    <th align="left" class="standard">Room Number</th>
		    <td><input type="text" class="w290" name="room_number" value="<%= modify.getRoom_number() %>"></td>
		</tr>
		<tr>
		    <th align="left" class="standard">Telephone Number</th>
		    <td><input type="text" class="w290" name="telephone" value="<%= modify.getTelephone() %>"></td>
		</tr>
		<tr>
		    <th align="left" class="standard">E-mail</th>
		    <td><input type="text" class="w290" name="email" value="<%= modify.getEmail() %>"></td>
		</tr>
		<tr>
			<th align="left" class="standard">Current User Type</th>
			<td><%=modify.getUserType() %></td>
		</tr>	
		<tr>
		    <th align="left" class="standard">Change User Type</th>
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
		    <th align="left" class="standard">User Groups</th>
		    <td>
		 <%
			  int id = Integer.parseInt(modify.getId()); 	
		      group.find_groups_from_id(id);
		      Vector groups = group.getAll_groups();
		      String tag = null;
		      for (int i=0; i<groups.size(); ++i)
		      {
		          tag = (String) groups.elementAt(i);
		          out.println(tag);%><br/><%
		      }
		 %>
		    </td>
		</tr>
		<tr>
			<th align="left" class="standard">
				Mail to the user:
			</th>
			<td>
				<input type="checkbox" name="mail" CHECKED>
				<input type="hidden" name="id" value="<%= modify.getId() %>">
			</td>
		</tr>
	</table><br>
  <input class="submit" type="submit" name="submit" value="Submit Form"/>&nbsp;&nbsp;&nbsp;
  <input class="submit" type="Reset" value="Reset"/>
</form>

<%
	if(request.getParameter("error")!= null)
	{	
		%>
			<hr>
			<br>
			<h3>Error user not updated, please try again!</h3>
		<%
	}
}

if(request.getParameter("code3")!= null)
{
  //Perform modification
  modify.setGroups(request.getParameterValues("groups"));    
  modify.getUserInfo(3, request.isUserInRole("adm"));
    
  if(modify.isUpdate() == false)
  {
  	//Error
  	response.sendRedirect(pageBase+"?code2=yes&error=yes&id="+modify.getId());
  }
  else
  {
  	//Success
  	response.sendRedirect(pageBase+"?success=yes&sendMail="+modify.getMail()+"&id="+modify.getId());
  }
 }
  
  if(request.getParameter("success")!= null)
  {
  //display modified user.
  
    String uid = request.getParameter("id");
  	UserRegBean reg = new UserRegBean();
  	reg.userCred(uid);
  	String sendMail = request.getParameter("sendMail");
  
  %>
  </center>
	<h2>Modify user</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<center>
	<br>
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="450">
  		<TR><TH colspan="4" class="blue">Modified User Data</TH></TR>
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
	     if("on".equals(sendMail))
	     {
	     %>
	          <br><hr><%
	     
	     	//Send the mail to the new user with information on the profile.
	     	String user = request.getRemoteUser();
			  mail.setFilepath(Attributes.STYLE_SHEET_FOLDER_REALPATH+"/Style.css");
			  mail.setMessage(mail.composeUpdateUserMail(reg));
			  mail.setTo(reg.getEmail());
			  mail.setFrom(userinfo.getEmail(user));
			  mail.sendMail("update_user"); //send the mail
			  boolean status = mail.isStatus();
			  
				  if(status)//Message sendt succesfully to the new user display message
				  {
				  	%>
				  		<p>Message sent to the entered e-mail address.</p>
				  	<%
				  }
				  else//message could not be sendt to the new user.. display error message.
				  {
				  	%>
				  		<p>Message could NOT be sent to the entered e-mail address.</p>
				  	<%
				  }
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