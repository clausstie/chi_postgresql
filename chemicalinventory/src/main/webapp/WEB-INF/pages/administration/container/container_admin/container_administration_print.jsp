<%@ page language="java" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="container" class="chemicalinventory.beans.ContainerRegBean" scope="page"/>
<jsp:setProperty name="container" property="*"/>

<%
//get the array of new containers.
String[] label = request.getParameterValues("label");
%>

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
<title>
print label for created container
</title>
<%
if(label != null)
{%>
<SCRIPT LANGUAGE="VBScript">
<!--
<%=container.createVBScript(label)%>
-->
</SCRIPT>
<%
}
%>
</head>
<%
if(label != null)
{%>
<body onload='DoPrint ""'>
<%
}
else
{
%>
<body onload="window.close();">
<%
}
//create forms holding the information about the containers.
if(label != null)
{
	for (int i = 0; i<label.length; i++)
	{
		String s_id = label[i];
		int id = Integer.parseInt(s_id);
		%>
		<%=container.labelData(id, 1)%>
		<%
	}
}
%>
<h2>IF THIS PAGE DOES NOT CLOSE AUTOMATICALLY YOU DO NOT HAVE ACCESS TO THE LABEL PRINT FEATURE!!!</h2>
</body>
</html>