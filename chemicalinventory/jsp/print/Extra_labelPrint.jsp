<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>
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
<title>
Print an extra label to a container
</title>
</head>
<body onload="document.check.container_id.focus()">
<%
String useragent = request.getHeader("User-Agent");
%>
   <h2>Label Print</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center>
   <p>Use this page to print an extra label for a container. Enter the container id 
   in the box and get a label with data retrived from the database.</p>
   
   <%
    if(useragent.indexOf("MSIE") == -1)
    {%>
    	<h3>WARNING: This functionality is available for Microsoft Internet Explorer browsers only!!</h3>
    <%}%>
    
    <form method="post" action="<%= Attributes.JSP_BASE %>?action=label_print2&code1=yes" target="blank" name="check" >
 		<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
			<TR><TH colspan="2" class="blue">Label Print</TH></TR>
		    <tr>
		        <th align="left" class="standard">Container ID:</th>
		        <td><input type="text" name="container_id" class="w200"></td>
		    </tr>
    	</table><br>
    	<%
    if(useragent.indexOf("MSIE") == -1)
    {%>
    	<input class="submit" type="submit" name="Submit" value="Submit" disabled>
    <%}
    else
    {%>
      <input class="submit" type="submit" name="Submit" value="Submit">
  <%}%>
    </form>
</center>
</body>
</html>