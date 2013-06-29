<%@ page language="java" import="java.util.*, java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="info" class="chemicalinventory.beans.userInfoBean" scope="page"/>
<jsp:setProperty name="info" property="*"/>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:useBean id="group" class="chemicalinventory.groups.User_group" scope="page"/>
<html>
<%
String current_user = request.getRemoteUser();
if (request.getParameter("code1")!=null) 
  {
    info.userInfo();
  }
  
//the id is set in ghe userInfo() method.
String id = info.getUser_id();
%>

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
<%if (request.getParameter("code1")!=null) 
  {%>
<script LANGUAGE="JavaScript">
function openWindow(url, number)
{
	if(number == 1)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=600, height=400")
	}
	else if(number == 2)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=600, height=600")
	}
}
</script>
<%
	}%>
<title>
Information about the user.
</title>
</head>
<body onload="document.user.user_name.focus()">

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_user_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="textboxadm">
<h2>Search for user</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<p>Entering a username below will display information about the user,
   and it will show a list of containers checked out by that user.</p>

<table cellspacing="2" cellpadding="1" border="0" width="90%">
    <tr>
        <td>
        <CENTER>
            <form action="<%=Attributes.JSP_BASE%>?action=User&code1=yes" method="post" name="user"> 
                <!--enter user name to search -->
                <table class="box" cellspacing="2" cellpadding="1" border="0" width="370">
					<TR><TH colspan="9" class="blue">User Information</TH></TR>
                    <tr>
                        <th align="left" class="standard">User Name:</th>
                        <TD><input type="text" name="user_name" class="w200"></td>
                    </tr>
                </table>
				<BR>
				<input class="submit" type="submit" name="Submit" value="Submit">
				<input class="submit" type="reset" value="Reset">
            </form>
         </CENTER>
        </td>
    </tr>
<%
  if (request.getParameter("code1")!=null) 
  {%>
    <tr>
         <td  colspan="2">
<!--Print out info about the user search-->        
<%

if(info.userContainer.isEmpty())
{
%>
  <table class="box" cellspacing="1" cellpadding="1" width="100%" align="center">
    <thead><tr> <th class="blue">User Name:</th>
                <th class="blue">Full Name:</th>
                <th class="blue">Room No.:</th>
                <th class="blue">Telephone:</th>
                <th class="blue">Email:</th>
            </tr>
    </thead>
    <tbody>
     <tr align="center">
        <td colspan="5">No user with this user name!</td>
     </tr>
    </tbody>
  </table>
<%
}
else
{
%>
  <table class="box" cellspacing="1" cellpadding="1" width="100%" align="center">
    <thead>
        <tr> <th class="blue">User Name:</th>
             <th class="blue">Full Name:</th>
             <th class="blue">Room No.:</th>
             <th class="blue">Telephone:</th>
             <th class="blue">Email:</th>
        </tr>
    </thead>
    <tbody>
  <%
     for(int i=0; i<info.userContainer.size(); i++)
     {
    %><tr>
   <%   String data = (String) info.userContainer.elementAt(i);
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
      </tr>
   <%}%>
   <tr>
   	<th class="blue" align="center" colspan="5">
   		Member of the following group(s)
   	</th>
   </tr>
   <tr>
   	<td align="center" colspan="5">
   		 <%
             group.find_groups_readonly(Integer.parseInt(id));
             Vector list = group.getAll_groups();
             String tag = null;
             for (int i=0; i<list.size(); ++i)
             {
                 tag = (String) list.elementAt(i);
                 out.println(tag+"<br/>");
             }
          %>
   	</td>
   </tr>
    </tbody>
  </table>
<%
}
%>
         </td>
    </tr>
</table>

<br><hr><center>
<h3>Container(s) checked out by <%=info.getUser_name().toUpperCase()%></h3></center>
<!--Print out info about the container held by user = xx-->
<%
if(info.container.isEmpty())
{
%>
  <table class="box" cellspacing="1" cellpadding="1" width="95%" align="center">
      <thead>
        <tr> <th class="blue">No.</th>
             <th class="blue">Container Id:</th>
             <th class="blue">Chemical Name:</th>
             <th class="blue">Location:</th>
        </tr> 
      </thead>
      <tbody>
        <tr align="center">
            <td colspan="4">No container registered for this user.</td>
        </tr>
      </tbody>
  </table>
<%
}
else
{
%>
<form method="post">
  <table class="box" cellspacing="1" cellpadding="1" width="95%" align="center">
    <thead>
        <tr> <th class="blue">No.</th>
             <th class="blue">Container Id:</th>
             <th class="blue">Chemical Name:</th>
             <th class="blue">Location:</th>
            <%
           if(current_user.equalsIgnoreCase(info.getUser_name()))
      		 {%>
             <th class="blue">&nbsp;</th>
				<% }%>
        </tr>
    </thead>
  <tbody>
  <%
     for(int i=0; i<info.container.size(); i++)
     {
        String color = null;
         if(i % 2 != 0)
          {
            color = "blue";
          }
          else
            color = "normal";
    %><tr class="<%=color%>">
     <td align="center"> <% out.print(i+1); %></td><%
        String data = (String) info.container.elementAt(i);
        StringTokenizer tokens = new StringTokenizer(data, "|", false);
        while(tokens.hasMoreElements())
        {
          String token = tokens.nextToken();
          token.trim();
          %><td align="center"><%              
                  out.println(token);
                %>
            </td>
      <%}
      if(current_user.equalsIgnoreCase(info.getUser_name()))
      {
      //You are allowed to check in the container if you are looking up yourself
      	String container_id = (String) info.container_ids.get(i);%>
     			<td align="center" >
						<INPUT class="submit_width95" type="submit" value="Check In" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=check_in&queryuser=<%=info.getUser_name()%>&id=<%=container_id%>'">
				  </td>
     <%}%>
      </tr>
   <%}%>
  </tbody>
  </table><br><br><hr>
 </form>
<%if(!info.container.isEmpty())
	{
%>
  <!-- Show button to notify the user about the containers
       that this person has currently checked out-->
  <form method="post" name="notify">
	  <table border="0" width="100%" align="center">
	  	<tr>
	  		<td>To notify the user about the containers checked out, press the send mail button:
	  		</td>
	  		<td align="center"> <INPUT class="submit_width175" align="right" type="button" value="Send Mail" onclick="openWindow('<%=Attributes.JSP_BASE%>?action=mail&info_user=<%=info.getUser_name()%>&fullname=<%=URLEncoder.encode(info.getFullname(), "UTF-8")%>&mail=<%=info.getEmail()%>', '1')">
	  		<td>
	  	</tr>
	  </table>
  </form>
<%
	}
}
  }//end of request with code1=yes
  %>
</center>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
</map>
</body>
</html>