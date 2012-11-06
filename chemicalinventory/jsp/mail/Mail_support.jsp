<%@ page language="java" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="user" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:setProperty name="mail" property="*"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
 * Copyright: 	2004, 2005 Dann Vestergaard and Claus Stie Kallesøe
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
 *   along with Foobar; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
<title>Send mail to the chemicalInventory team</title>
</head>
<body>
  
  <%
		  String from_username = request.getRemoteUser();
		  String from_mail = user.getEmail(from_username);
		  String from_full_name = user.getFullName(from_username);
  %>
  
<span class="posAdm1">
	<img src="<%=Attributes.IMAGE_FOLDER%>/bar_mail_support.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="textboxadm">
<%if (request.getParameter("code1")==null) 
  {%>
	<h2>Send message to the team developing chemicalInventory</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
  <center>
	<p>From this page you will be able to send a message to the people 
	developing and supporting the chemicalInventory application. Use this 
	posibility to request technical support, report bugs discovered, or if you have 
	wishes or ideas to new features or improvement of existing features.
			This message will be send as a plain text message.</P>
 	<FORM METHOD=POST name="send_message" >
		<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
  		  <TR><TH colspan="3" class="blue">Message:</TH></TR>
    	<tr>
    		<td align="center" >
		      <TABLE BORDER="0" CELLPADDING="4">
		        <TR>
		              <TH ALIGN="LEFT" class="standard">From:</TH>
		              <TD ALIGN="LEFT" width="298">
		                <INPUT TYPE="TEXT" SIZE="60" MAXLENGTH="60" NAME="from_name" value="<%=from_username.toUpperCase()%> - <%=from_full_name%>" readonly="readonly"/>
		                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="from" value="<%=from_mail%>">
		                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="from_fullname" value="<%=from_full_name%>">
		          </TD>
		        </TR>
		        <TR>
		              <TH ALIGN="LEFT" class="standard">Subject:</TH>
		              <TD ALIGN="LEFT" width="298">
 		              	<select name="subject" style="width: 383px">
 		              		<option value="chemicalInventory DFU Support Request">Support Request</option>
 		              		<option value="chemicalInventory DFU Bug Report">Bug Report</option> 
 		              		<option value="chemicalInventory DFU New Feature">Suggest New Feature</option>		              		
 		              		<option value="chemicalInventory DFU Improvements">Improvements of Features</option>
 		              		<option value="chemicalInventory DFU Misc.">Miscellaneous</option>
 		              	</select>
		          </TD>
		        </TR>
		        <TR>
		        	<TH colspan="2" align="center" class="blue">Message Text</TH>
		        </TR>
		        <TR>
		        	<TD colspan="2">
		        	     <TEXTAREA NAME="message" ROWS="15" COLS="62"></TEXTAREA>
		        	</TD>
		        </TR>		        		        
		      </TABLE>
		   </td>
			</tr>
	  </table>     
		      <br>
		      <input class="submit" type="submit" value="Send Mail" onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=support&code1=yes'">
		      <input class="submit" type="reset" value="Reset Fields">
    </FORM>
			    
     </center>
	  <%
	  }
  if (request.getParameter("code1")!=null) 
  {
  	//send the mail...
	   mail.sendMail("support");
	   boolean status = mail.isStatus();

	  if (status)//Message sent successfully
	  {
	  	out.print("<FONT SIZE=4 COLOR=\"blue\"><hr><B>Message successfully sent!</B><hr>");
	  }
	  else//message could not be sent
	  {
	  	out.print("<FONT SIZE=4 COLOR=\"red\"><hr><B>Message could not be sent, please try again later!</B><hr>");
 	  }
  }%>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="2,1,145,21" href="<%=Attributes.JSP_BASE%>?action=mail_webmaster">
  <AREA SHAPE="rect" COORDS="147,1,292,21" href="<%=Attributes.JSP_BASE%>?action=mail_news">
  <AREA SHAPE="rect" COORDS="294,1,437,21" href="<%=Attributes.JSP_BASE%>?action=support">
</map>
</body>
</html>