<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis"
	class="chemicalinventory.analysis.AnalysisBean" scope="page" />
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean"
	scope="page" />
<jsp:setProperty name="sample" property="*" />
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<script LANGUAGE="JavaScript">
function openWindow(url, number)
{
	if(number == 1)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=850, height=600")
	}
	else if(number == 2)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=600, height=600")
	}
}

function validateForm(form) 
{
 if(trim(form.compound_id.value)== "" || trim(form.compound_id.value) == "null")
 {
    alert("Please search and find the compound for which, you want to view samples");
    form.compound.focus();
    return false;
 }
 return true;
}
</script>

<title>Display list of samples for a compound</title>
</head>
<%if (request.getParameter("code1") == null
				&& request.getParameter("code2") == null
				&& request.getParameter("code4") == null) {%>
<body onload="document.sample.compound.focus()">
<%
		} else {%>
<body>
<%
		}

		//initial values to be used on this page
		sample.setBase(Attributes.JSP_BASE);
		String com_name = request.getParameter("chemical_name");
		String com_id = request.getParameter("compound_id");
%>

<div id="overDiv"
	style="position:absolute; visibility:hidden; z-index:1000;"></div>

<span class="posAdm1"> <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_display.png"
	height="55" width="820" usemap="#nav_bar" border="0"> </span>
<span class="posAdm2"> | <a class="adm"
	href="<%=Attributes.JSP_BASE %>?action=display_single_sample">Display sample
information</a> | <a class="adm"
	href="<%=Attributes.JSP_BASE%>?action=display_sample_list">List samples for compound</a>
| <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a> | </span>
<span class="textboxadm"> <%
		if (com_name != null && com_id != null) {
			com_name = URLDecoder.decode(com_name, "UTF-8");
		} else
			com_name = "Search For Compound!";

		if (request.getParameter("code1") == null
				&& request.getParameter("code2") == null
				&& request.getParameter("code4") == null) {
			// show the inital screen to enter a compound name to search for samples on..
			%>
<h2>Display list of samples</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<center>
<form method="post" action="<%= Attributes.JSP_BASE %>?action=display_sample_compound"
	name="sample" onsubmit="return validateForm(this)">
	 <table class="box" cellpadding="1" cellspacing="2" border="0" width="600">
 		<TR><TH colspan="4" class="blue">Sample</TH></TR>
	<tr>
		<th align="left" class="standard">Compound:</th>
		<td><input type="text" name="compound" value="<%=com_name%>" class="w400">
		<input type="hidden" name="compound_id" value="<%=com_id%>"></td>
		<td><a href="#"
			onclick="openWindow('<%=Attributes.JSP_BASE%>?action=s_search&return_to=display_sample_list', 1)">&nbsp;<img
			src="<%=Attributes.IMAGE_FOLDER%>/plus_mark.png" height="15" width="15" border="0"></a>
		</td>
	</tr>
</table>
<br>
<input class="submit" type="submit" name="Submit" value="Submit"
	onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=display_sample_compound'">&nbsp;
<input class="submit" type="reset" name="reset" value="Reset"></form>
</center>
<%
		}//end initial entry of sample id
	%> </span>
<jsp:include page="/text/sample_nav_bar.jsp" />
</body>
</html>
