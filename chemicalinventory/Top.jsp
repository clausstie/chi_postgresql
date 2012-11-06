<%@ page language="java"%>

<%
	String ip_address = request.getServerName();
	String app = request.getContextPath();
	String port = String.valueOf(request.getServerPort());
	String useragent = request.getHeader("User-Agent");
	String label_print = "| PRINT LABEL";
	if (useragent.indexOf("MSIE") != -1)
	{
		label_print = "| <a target=\"Main\" class=\"special\" href=	\"//"+ip_address+":"+port+app+"/jsp/Controller.jsp?action=label_print\">PRINT LABEL</a>";
	}   
%>

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
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
<link rel="stylesheet" type="text/css"
	href="<%=app%>/styles/Style.css">
<script language="Javascript">
 <!--
 function doClear(theText) 
{
  if (theText.value == theText.defaultValue)
  {
       theText.value = "";
  }
}
 //-->
 </script>
<title>Top frame</title>
</head>
<body>
<form
	action="http://<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=Search&code1=yes"
	method="post" target="Main">
<table width="90%" border="0">
	<tr>
		<td colspan="2"><img border="0" vspace="0"
			src="images/TopSplit-withtext.png" width="985" height="67"></td>
	</tr>
	<tr>
		<td width="800" align="left">
		<p class="special"><%=label_print%> | <a target="Main" class="special"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp">HOME</a>
		| <a target="Main" class="special" accesskey="M"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=mail_webmaster">
		MESSAGES</a> | <a target="Main" class="special" accesskey="C"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=ChangePwd">
		CHANGE PASSWORD</a> | <a target="Main" class="special"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=Search">
		SEARCH</a> | <a target="Main" class="special" accesskey="Q"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=User">
		INFO</a> | <a target="Main" class="special"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=check_out_direct">
		CHECK-OUT</a> | <a target="Main" class="special"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=check_in_direct">
		CHECK-IN</a> | <a target="Main" class="special"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=trans">
		TRANSFER</a> | <a target="Main" class="special" accesskey="X"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=Logout">
		LOG-OUT</a> |</p>
		</td>
		<%
		if(useragent.indexOf("MSIE") != -1) {%>
		<td align="right"><INPUT class="slimtext" type="text" name="value"
			onFocus="doClear(this)">&nbsp;<input class="submit_nowidth_slim"
			type="submit" value="search">&nbsp;
		<td><% }%>
	</tr>
</table>
</form>
</body>
</html>