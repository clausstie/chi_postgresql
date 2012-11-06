<%@ page language="java" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="mail" class="chemicalinventory.mail.MailComposer" scope="page"/>
<jsp:setProperty name="mail" property="*"/>
<jsp:useBean id="user" class="chemicalinventory.user.UserInfo" scope="page"/>
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
	<LINK REL="stylesheet" TYPE="text/css" HREF="../styles/Style.css">
	<TITLE>chemicalInventory - Send mail</TITLE>
  </head>
  <%
  	  String csspath = Attributes.STYLE_SHEET_FOLDER_REALPATH+"/Style.css";
	  String from_username = request.getRemoteUser();
	  String from_mail = user.getEmail(from_username);
	  String from_full_name = user.getFullName(from_username);
	  String to_mail = request.getParameter("mail");
	  String to_full_name = request.getParameter("fullname");
	  String to_username = request.getParameter("info_user");

  %>
  <body onload="window.resizeTo(600,550);">
  
   <!-- CREATE THE MAIL -->
    <%
  if (request.getParameter("code1")==null) 
  {%>
    <CENTER>
    	<FORM METHOD=POST>
     	<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
  		  <TR><TH colspan="3" class="blue">Message:</TH></TR>
    	<tr>
    		<td align="center">
	      <TABLE BORDER=0 CELLPADDING=4>
	        <TR>
	              <TH ALIGN="LEFT" class="standard">From:</TH>
	              <TD ALIGN="LEFT" width="298">
	                <INPUT TYPE="TEXT" SIZE="60" MAXLENGTH="60" NAME="from_name" value="<%=from_username.toUpperCase()%> - <%=from_full_name%>">
	                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="from" value="<%=from_mail%>">
	                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="from_fullname" value="<%=from_full_name%>">
	          </TD>
	        </TR>
	        <TR>
	              <TH ALIGN="LEFT" class="standard">To:</TH>
	              <TD ALIGN="LEFT" width="298">
	                <INPUT TYPE="TEXT" SIZE="60" MAXLENGTH="200" NAME="to_name" value="<%=to_username%> - <%=to_full_name%>">
	                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="to" value="<%=to_mail%>">
	                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="username" value="<%=to_username%>">
	                <INPUT TYPE="hidden" SIZE="60" MAXLENGTH="60" NAME="to_fullname" value="<%=to_full_name%>">
	          </TD>
	        </TR>

	        <TR>
	              <TH ALIGN="LEFT" class="standard">Subject:</TH>
	              <TD ALIGN="LEFT" width="298">
	                <INPUT TYPE="TEXT" SIZE="60" MAXLENGTH="100" NAME="subject" value="Container(s) checked out by <%=to_full_name%>" >
	          </TD>
	        </TR>
	        <TR>
	        	<TH colspan="2" align="center" class="blue">Message Text</TH>
	        </TR>
	        <TR>
	        	<TD colspan="2">
	        	     <TEXTAREA NAME="message" ROWS="10" COLS="60"></TEXTAREA>
	        	</TD>
	        </TR>			        
	      </TABLE>
	     </td>
	    </tr>
	   </table><br>
	      <input class="submit" type="submit" value="Send Mail" onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=mail&code1=yes'">
	      <input class="submit" type="reset" value="Reset Fields">
	    </FORM>
    </CENTER>
    <%
    }
  //    SEND THE MAIL
  if (request.getParameter("code1")!=null) 
  {
	  mail.setFilepath(csspath);
	  String status = mail.sendMail("info_mail");
	  out.print(status);
	  %><br>
	  	<input class="submit" align="right" type="submit" value="Close window" onclick="window.close()">
	  <%	  
  }//end of request code2= yes.
  %>
  </body>
</html>