<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean"
	scope="page" />
<jsp:setProperty name="sample" property="*" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<title>Display information on sample</title>

<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
</head>

<body>
<span class="posAdm1"> <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_login.png"
	height="55" width="820" usemap="#nav_bar" border="0"> </span>
<span class="posAdm2"> | <a class="adm"
	href="<%=Attributes.SAMPLE_BASE %>?action=create_sample">Compound Dependent Sample</a>
| <a class="adm" href="<%=Attributes.SAMPLE_BASE%>?action=create_sample_ind">Independent
Sample</a> | <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_sample">Lock
Sample</a> | <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=unlock_sample">Un-Lock
Sample</a> | <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result">Lock/Un-Lock
Result</a> | <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample">Add/Remove
Analysis</a> | <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a>
| <a class="adm" href="<%=Attributes.JSP_BASE%>?action=dis_sample">List samples</a> | </span>
<span class="textboxadm">
<h2>Select sample to display</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<center>
<table class="box" cellspacing="0" cellpadding="1" border="0"
	width="530px">
	<tr>
		<th colspan="4" class="blue">Independent samples</th>
		<th colspan="4" class="blue">Compound Samples</th>
	</tr>
	<tr height="2">
		<td colspan="8"></td>
	</tr>
	<tr>
		<th width="100" class="blue">Sample id</th>
		<th width="125" class="blue">Description</th>
		<th width="50" class="blue">&nbsp;</th>
		<th width="15" class="blue">&nbsp;</th>

		<th width="100" class="blue">Sample id</th>
		<th width="125" class="blue">Description</th>
		<th width="50" class="blue">&nbsp;</th>
		<th width="15" class="blue">&nbsp;</th>
	</tr>
	<tr>
		<td colspan="4"><%=sample.getSample_id()%> <iframe id="list_frame"
			frameborder="0" scrolling="yes"
			src="<%= Attributes.JSP_BASE %>?action=dis_sample_list" width="300" height="500"
			marginwidth="0" name="list_frame"> </iframe></td>
		<td colspan="4"><%=sample.getSample_id()%> <iframe id="list_frame_2"
			frameborder="0" scrolling="yes"
			src="<%= Attributes.JSP_BASE %>?action=dis_sample_list2" width="300" height="500"
			marginwidth="0" name="list_frame"> </iframe></td>
	</tr>
</table>
</center>
</span>
<jsp:include page="/text/sample_nav_bar.jsp" />
</body>
</html>