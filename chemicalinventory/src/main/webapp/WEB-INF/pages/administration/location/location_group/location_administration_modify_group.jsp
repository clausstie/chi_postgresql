<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes"%>
<%@ page import="chemicalinventory.utility.Util"%>
<%@ page import="java.util.Vector"%>
<%@ page import="chemicalinventory.utility.Return_codes"%>
<jsp:useBean id="location_group" class="chemicalinventory.groups.Location_group" scope="page" />
<jsp:setProperty name="location_group" property="*" />

<jsp:useBean id="location" class="chemicalinventory.beans.locationBean" scope="page" />
<%
int location_id = Util.getIntValue(request.getParameter("id"));
int status = 0;
%>

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
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
		<title>
			Chemicalinventory - Modify group relations for location level 2
		</title>

		<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
	</head>
	<body>
		<span class="posAdm1">
			<img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_location.png" height="55" width="820" usemap="#nav_bar" border="0">
		</span>
		<span class="posAdm2">
			|
			<a class="adm" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
				Register a new location
			</a>
			|
			<a class="adm" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location">
				Modify an existing location
			</a>
			|
			<a class="adm" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location&target=modify_group_location">
				Modify groups for location
			</a>
			|
		</span>
		<span class="textboxadm">
			<h2>
				Add location to user group(s)
			</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">

			<%if (request.getParameter("code1") != null) {

				location.getNameAndLevel(location_id);

				%>
			<FORM name="group_form" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_group_location&code2=yes&location_id=<%=location_id%>" method="POST">
				<CENTER>
					<TABLE class="box" border="0" width="600px">
						<TR>
							<TH class="blue" colspan="2">
								Select Groups for location
							</TH>
						</TR>
						<TR>
							<TH class="standard">
								Location Name:
							</TH>
							<TD>
								<%=location.getLocation()%>
							</TD>
						</TR>
						<TR>
							<TH class="standard">
								Location Level:
							</TH>
							<TD>
								<%=location.getLevel()%>
							</TD>
						</TR>
						<TR>
							<TH class="standard">
								Groups:
							</TH>
							<TD>
								<%//Get all groups from the group bean.
				location_group.find_location_groups_from_id(location_id);
				Vector list = location_group.getAll_groups();
				if (list == null || list.size() < 1) {

				%>
								<I>
									No groups registered
								</I>
								<%} else {
					for (int i = 0; i < list.size(); i++) {
						String data = (String) list.get(i);

						%>
								<%=data%>
								<BR>
								<%}
				}%>
							</TD>
						</TR>
					</TABLE>
					<BR>
					<input class="submit" type="submit" name="Submit" value="Submit">
					&nbsp;&nbsp;&nbsp;
					<input class="submit" type="Reset">
					&nbsp;&nbsp;&nbsp;
				</CENTER>
			</FORM>
			<BR>
			<%if (request.getParameter("rcode1") != null) {

				%>
			<HR>
			<P>
				...Error! Update of groups could not be performed, please try again.
			</P>
			<%}
			}//end code1 select groups. 

			//Modify the locations groups that the location 
			//is restricted to.
			if (request.getParameter("code2") != null) {
			%>
				Please wait updating.....
			<%
			
				location_id = location_group.getLocation_id();
				status = location_group.update_location_groups(location_group.getGroups(), location_id, request.getRemoteUser().toUpperCase());

				if (status == Return_codes.SUCCESS) {
					response.sendRedirect(Attributes.LOCATION_ADMINISTRATOR_BASE
									+ "?action=modify_group_location&code3=yes&location_id="
									+ location_id);
				} else {
					response.sendRedirect(Attributes.LOCATION_ADMINISTRATOR_BASE
									+ "?action=modify_group_location&rcode1=yes&code1=yes&id="
									+ location_id);
				}
			}

			if (request.getParameter("code3") != null) {
				location_id = location_group.getLocation_id();
				location.getNameAndLevel(location_id);

				%>
			<CENTER>
				<TABLE class="box" border="0" width="600px">
					<TR>
						<TH class="blue" colspan="2">
							Groups selected for location
						</TH>
					</TR>
					<TR>
						<TH class="standard">
							Location Name:
						</TH>
						<TD>
							<%=location.getLocation()%>
						</TD>
					</TR>
					<TR>
						<TH class="standard">
							Location Level:
						</TH>
						<TD>
							<%=location.getLevel()%>
						</TD>
					</TR>
					<TR>
						<TH class="standard">
							Groups:
						</TH>
						<TD>
				<%//Get all groups from the group bean.
				location_group.find_location_groups_readonly(location_group.getLocation_id());
				Vector list = location_group.getAll_groups();
				if (list == null || list.size() < 1) {

				%>
							<I>
								....No groups registered
							</I>
							<%} else {
					for (int i = 0; i < list.size(); i++) {
						String data = (String) list.get(i);

						%>
							<%=data%>
							<BR>
					<%}
				}%>
						</TD>
					</TR>
				</TABLE>
				<%}
			%>
		</span>

		<MAP NAME="nav_bar">
			<AREA SHAPE="rect" COORDS="3,2,90,23" href="<%=Attributes.ADMINISTRATOR_BASE%>?action=Adm">
			<AREA SHAPE="rect" COORDS="92,2,179,23" href="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=Supplier">
			<AREA SHAPE="rect" COORDS="181,2,268,23" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
			<AREA SHAPE="rect" COORDS="270,2,362,23" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">
			<AREA SHAPE="rect" COORDS="364,2,451,23" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">
			<AREA SHAPE="rect" COORDS="453,2,543,23" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=new_group">
			<AREA SHAPE="rect" COORDS="544,3,634,23" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis">
			<AREA SHAPE="rect" COORDS="637,3,727,23" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE %>?action=new_unit">
		</map>
	</body>
</html>