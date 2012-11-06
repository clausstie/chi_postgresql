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
 * Copyright: 	2004-2005 Dann Vestergaard and Claus Stie Kallesøe
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
<title>Information about the sample module</title>
</head>
<body>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_login.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.SAMPLE_BASE %>?action=create_sample">Compound Dependent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLE_BASE%>?action=create_sample_ind">Independent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_sample">Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=unlock_sample">Un-Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result">Lock/Un-Lock Result</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample">Add/Remove Analysis</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=dis_sample">List samples</a> |
</span>
<span class="textboxadm">
<h2>Sample module</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>
In the sample module of this application you can perform various actions on samples and results.<br/>
</p>
<p>
<b>Here is a short list of some of the main functionalities in this module.</b>
</p>
<ul>
	<li>Create samples linked to compounds.</li>
	<li>Create samples, which are independent of compounds in the system.</li>
	<li>Perform result entry and modification of priviously entered results.</li>
	<li>Modify samples, change the analysis connected to a sample.</li>
	<li>Display sample as readonly.</li>
	<li>Lock sample - Stop all result entry on the sample</li>
	<li>Lock results - Stop entry of results on specific selected results.</li>
	<li>List samples for a specific compound.</li>
	<li>Search for samples.</li>
</ul>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>