<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="java.net.URLDecoder" %>
<jsp:useBean id="check" class="chemicalinventory.beans.BorrowBean" scope="page"/>
<jsp:setProperty name="check" property="*"/>
<jsp:useBean id="userInf" class="chemicalinventory.beans.userInfoBean" scope="page"/>
<jsp:useBean id="Util" class="chemicalinventory.utility.Util" scope="page"/>
<jsp:useBean id="info" class="chemicalinventory.user.UserInfo" scope="page"/>
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
<script language="JavaScript" src="../script/overlib.js"></script>
<title>Check out a container.</title>
</head>
<body>
<div id="overDiv"
	style="position:absolute; visibility:hidden; z-index:1000;"></div>
<%
userInf.setBase(Attributes.JSP_BASE);
String user = request.getRemoteUser();
%>
<input type="hidden" name="user_name" value="<%=user%>">
<%

  if (request.getParameter("code2")!=null) 
  {
    String id = request.getParameter("id");
    String compound_id = request.getParameter("compound_id");
    String chemical_name = chemicalinventory.utility.Util.getChemicalName2(id);
    
    /*
    * Check out the container
    */
    boolean status = check.check_out(id, user, chemical_name);
    
    if(status)//the check out was a succes..
    {
    	userInf.shortUserInfo(user);

%>
<h2>The following container has been checked out!</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<center>
<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
	<TR><TH colspan="3" class="blue">Check Out - Receipt:</TH></TR>    
	<tr>
		<th align="left" width="160">Chemical name:</th>
		<td>&nbsp;<%=chemical_name%></td>
	</tr>
	<tr>
		<th align="left" width="160">Container id:</th>
		<td>&nbsp;<%= check.getId()%></td>
	</tr>
	<tr>
		<th align="left" width="160">New Location:</th>
		<td>&nbsp;<%= userInf.getUser_name() %></td>
	</tr>
	<tr>
		<th align="left" width="160">Current Quantity:</th>
		<td>&nbsp;<%check.setCurrent_quantity();%><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%></td>
	</tr>
</table>
<br />
</center>
<%
    }
    else//an error orcurred during check out..
    {%>

<h2>Error in check out of the container!</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<p>The container was not checked out, please try again</p>
<br />
<%
    }
    %>
<FIELDSET><LEGEND>ACTION</LEGEND>
<table class="action_noheader" width="790px">
	<tr>
		<td><a href="<%=Attributes.JSP_BASE%>?action=ResultPage&id=<%=compound_id%>"
			onmouseover="return overlib('Return to detailed information on <%=chemical_name%>.', BORDER, 2);"
			onmouseout="return nd();"><img
			src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a> <a
			href="<%=Attributes.JSP_BASE%>?action=check_out_direct"
			onmouseover="return overlib('Check-Out one more container.', BORDER, 2);"
			onmouseout="return nd();"><img
			src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a> <a
			href="<%= Attributes.JSP_BASE %>?action=Search&code1=yes&history=true"
			onmouseover="return overlib('Last Search Result.', BORDER, 2);"
			onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_search_30.png"
			border="0"></a> <a href="<%= Attributes.JSP_BASE %>"
			onmouseover="return overlib('HOME', BORDER, 2);"
			onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png"
			border="0"></a></td>
	</tr>
</table>
</FIELDSET>

<%
  }

if (request.getParameter("code2")==null && request.getParameter("code1")==null) 
   {
    check.find();
    check.setCurrent_quantity();
   %>
<h2>Check out a container</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<CENTER>
<p>The following data has been transferred from the search result.</p>
<br>
<form method="post"
	action="<%= Attributes.JSP_BASE %>?action=check_out&code2=yes&id=<%=check.getId()%>&chemical_name=<%=check.getChemical_name()%>&compound_id=<%= check.getCompound_id()%>">
<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
	<TR><TH colspan="3" class="blue">Check Out:</TH></TR>    
	<tr>
		<th align="left" width="160">Chemical name:</th>
		<td><%=Util.encodeTag(URLDecoder.decode(check.getChemical_name(), "UTF-8"))%></td>
	</tr>
	<tr>
		<th align="left" width="160">Container id:</th>
		<td><%=check.getId()%></td>
	</tr>
	<tr>
		<th align="left" width="160">Home location:</th>
		<td><%=check.getHome_location()%></td>
	</tr>
	<tr>
		<th align="left" width="160">Initial Quantitiy:</th>
		<td><%=check.getInitial_quantity()%>&nbsp;<%=check.getUnit()%></td>
	</tr>
	<tr>
		<th align="left" width="160">Current Quantity:</th>
		<td><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%></td>
	</tr>
	<tr>
		<th align="left" width="160">Tara Weight:</th>
		<td><%=check.getTara_weight()%>&nbsp;gram</td>
	</tr>
	<tr>
</table>
<br>

<table border="0" width="75%">
	<tr>
		<td align="center"><input class="submit" type="submit" name="Submit"
			value="Check Out" tabindex="1">&nbsp;&nbsp; <input class="submit"
			type="submit" name="cancel" value="Cancel"
			onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=ResultPage&id=<%= check.getCompound_id()%>'">
		</td>
	</tr>
</table>
</form>
</center>
<%}%>
</body>
</html>