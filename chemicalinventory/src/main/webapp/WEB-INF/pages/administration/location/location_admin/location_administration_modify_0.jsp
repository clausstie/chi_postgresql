<%@ page language="java" import="java.util.*" import="java.net.*" import="chemicalinventory.utility.Util"%>
<%@ page import="chemicalinventory.context.Attributes"%>
<jsp:useBean id="location" class="chemicalinventory.beans.locationBean" scope="page" />
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
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
		<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
		<script language="JavaScript" src="../../script/inventoryScript.js"></script>
		<title>
			Modify a registrered location (level_0)
		</title>
		<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.level_0.value) == "") 
 {
  alert("Please fill in a valid name for the location!");
  form.level_0.focus();
  return false;
 } 
   return true;
}
</script>
	</head>
	<body>
		<%String user = request.getRemoteUser().toUpperCase();
%>
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
				Use this page to modify information about locations
			</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<center>

				<%/*Display the level 0 to modify*/
			if (request.getParameter("code1") != null) {%>
				<p>
					On this page you can modify the location for the level 0 location: '
					<%=location.getLevel_0().toUpperCase()%>
					'.
				</p>
				<form name="locationChoices" method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0&code2=yes" onsubmit="return validateForm(this)">
					<table class="box" width="460" border="0" cellpadding="1" cellspacing="1">
						<TR>
							<TH colspan="4" class="blue">
								Location
							</TH>
						</TR>
						<tr>
							<TH align="left" class="standard">
								Location:
							</TH>
							<td align="center">
								<input type="text" name="level_0" value="<%=location.getLevel_0()%>" class="w290">
								<input type="hidden" name="level_0_number" value="<%=location.getLevel_0_number()%>" READONLY>
							</td>
						</tr>
					</table>
					<br>
					<input class="submit_width125" type="submit" value="Update Location" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0&code2=yes'">
					&nbsp;&nbsp;
					<input class="submit_width125" type="submit" value="Delete Location" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0&code3=yes&level_0=<%=location.getLevel_0()%>&level_0_number=<%=location.getLevel_0_number()%>'">
					&nbsp;&nbsp;
					<input class="submit" type="submit" value="Cancel" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location'">
				</form>

				<table style="border-style: solid; border-width: 1px; border-color: blue;" cellspacing="7" width="500">
					<tr>
						<td align="center">
							NOTE: This option will reorganize your storage locations.
							<br>
							The sub locations (level 1 and 2) attached to the above selected level 1 location, will still be attached to the same level 0 location. Deleting will have recursive effect.
						</td>
					</tr>
				</table>
				<%}
			/*perform the update of the name of the level 0 location and display a receipt.*/
			if (request.getParameter("code2") != null) {
				location.updateLevel0_name();

				if (location.isUpdate()) {%>
				<h3>
					The location has now been updated. New name: '
					<%=location.getLevel_0()%>
					'.
				</h3>
				<%} else {%>
				<h3>
					ERROR! The location could not be updated
				</h3>
				<%}
			}

			/*The user wants to delete the location
			 *show a page where information about the number of containers and
			 *other locations that will be deleted, if chosen to delete this location.*/
			if (request.getParameter("code3") != null) {
				location.updateLevel_delete_info(0);

				if (location.isUpdate()) {%>
				<h3>
					You want to delete the level_0 location: '
					<%=location.getLevel_0()%>
					'.
				</h3>
				<p>
					Deleting this location will delete the following data:
				</p>
				<form method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0_list1&code1=yes&firstChoice=<%=location.getLevel_0_number()%>" target="blank">
					<table>
						<tr>
							<td>
								<%=location.getNo_cont()%>
								Container(s).&nbsp;&nbsp;
							</td>
							<td>
								<input class="submit" type="submit" value="Show List" <%if(location.getNo_cont() == 0) out.print("disabled=true");%>
									onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0_list1&code1=yes&firstChoice=<%=location.getLevel_0_number()%>'">
							</td>
						</tr>
						<tr>
							<td>
								<%=location.getNo_level1()%>
								Level 1 location(s).&nbsp;&nbsp;
							</td>
							<td>
								<input class="submit" type="submit" value="Show List" disabled="true" onclick="this.form.action='<%=Attributes.ADMINISTRATOR_BASE%>?action=modify_location_0_list2&code2=yes'">
							</td>
						</tr>
						<tr>
							<td>
								<%=location.getNo_level2()%>
								Level 2 location(s).&nbsp;&nbsp;
							</td>
							<td>
								<input class="submit" type="submit" value="Show List" disabled="disabled" onclick="this.form.action='<%=Attributes.ADMINISTRATOR_BASE%>?action=modify_location_0_list3&code3=yes'">
							</td>
						</tr>
					</table>
				</form>
				<hr>
				<form method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0&code4=yes&firstChoice=<%=location.getLevel_0_number()%>&level_0_number=<%=location.getLevel_0_number()%>">
					<input type="submit" class="submit_width130" value="Delete Values">
				</form>

				<%} else {%>
				<h3>
					ERROR! Please try again, an exception orcurred.
				</h3>
				<%}
			}

			/*Delete containers and locations */
			if (request.getParameter("code4") != null) {
				location.setUser(user);
				location.delete_container_at_location(0);

				if (location.isUpdate()) {%>
				<h3>
					The location and all related data has been deleted.
				</h3>
				<%} else {%>
				<h3>
					ERROR! The location could not be deleted
				</h3>
				<%}
			}%>
			</center>
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
