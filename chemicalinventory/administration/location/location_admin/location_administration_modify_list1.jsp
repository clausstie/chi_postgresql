<%@ page language="java" import="java.util.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes"%>
<%@ page import="chemicalinventory.utility.Util"%>
<jsp:useBean id="container" class="chemicalinventory.beans.ContainerRegBean" scope="page" />
<jsp:setProperty name="container" property="*" />
<jsp:useBean id="location" class="chemicalinventory.beans.userInfoBean" scope="page" />
<jsp:setProperty name="location" property="*" />
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
		<script language="JavaScript" src="../../script/overlib.js"></script>
		<title>
			Information about the containers in a specific location.
		</title>
	</head>
	<body onload="window.resizeTo(900, 600)">
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

		<center>
			<%if (request.getParameter("code1") != null) {
				//Search for containers..
				location.container_at_location_Info(0);

				%>
			<h3>
				These containers will be deleted
			</h3>
			<%if (location.location.isEmpty()) {%>
			<table cellspacing="1" cellpadding="1" border="1" width="65%" rules="rows" align="center">
				<thead>
					<tr>
						<th class="blue">
							Chemical Name:
						</th>
						<th class="blue">
							Container Id::
						</th>
						<th class="blue">
							Quantity:
						</th>
						<th class="blue">
							Home location:
						</th>
						<th class="blue">
							Current location:
						</th>
						<th class="blue">
							Owned by:
						</th>
						<th class="blue">
							Remark:
						</th>
					</tr>
				</thead>
				<tbody>
					<tr align="center">
						<td colspan="9">
							No containers registered for the location selected, please try again.
						</td>
					</tr>
				</tbody>
			</table>
			<%} else {

					%>
			<p>
				This search result contains
				<%=location.location.size()%>
				containers.
			</p>
			<table cellspacing="1" cellpadding="1" border="1" width="100%" rules="rows" align="center">
				<thead>
					<tr>
						<th class="blue">
							Chemical Name:
						</th>
						<th class="blue">
							Container Id::
						</th>
						<th class="blue">
							Quantity:
						</th>
						<th class="blue">
							Home location:
						</th>
						<th class="blue">
							Current location:
						</th>
						<th class="blue">
							Owned by:
						</th>
						<th class="blue">
							Remark:
						</th>
					</tr>
				</thead>
				<tbody>
					<%String color = "normal";
					for (int i = 0; i < location.location.size(); i++) {
						if (i % 2 != 0) {
							color = "blue";
						} else
							color = "normal";

						%>
					<tr class="<%= color %>">
						<%String data = (String) location.location.elementAt(i);
						System.out.println("data: " + data);
						StringTokenizer tokens = new StringTokenizer(data, "|",
								false);
						while (tokens.hasMoreElements()) {
							String token = tokens.nextToken();
							token.trim();

							%>
						<td align="center">
							<%out.println(URLDecoder.decode(token, "UTF-8"));

						%>
						</td>
						<%}%>
					</tr>
					<%}%>
				</tbody>
			</table>
			<%}
			}

		%>
		</center>
	</body>
</html>
