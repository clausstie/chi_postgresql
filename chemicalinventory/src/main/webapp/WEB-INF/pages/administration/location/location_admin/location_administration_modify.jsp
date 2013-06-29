<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes"%>
<%@ page import="chemicalinventory.utility.Util"%>
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
			Modify a registrered location
		</title>
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
				Use this page to modify information about locations.
			</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<center>

				<p>
					Click on a location name to modify that location
				</p>
				<table class="textsize" cellspacing="1" cellpadding="1" width="485px" frame="box">
					<tr>
					<thead>
						<th class="blue">
							Level 0:
						</th>
						<th class="blue">
							Level 1:
						</th>
						<th class="blue">
							Level 2:
						</th>
					</thead>
					</tr>
				</table>
				<table border="0" cellspacing="0" width="480px" rules="rows">
					<%
    String target = request.getParameter("target");
    /*
    * Determine the action to take when the user presses a link in the list
    * of registered locations. 
    * 1: Modify names, connections to other locations etc.
    * 2. Modify the groups the location is member of..
    */
    if(!Util.isValueEmpty(target))
    	target = "modify_location";
    	
    /* Get the locations from the db.*/
    location.showAllLocations();
    for (int i=0; i<location.l0_id.size(); i++)
    {
      String level_0_id = (String) location.l0_id.elementAt(i);
      String level_0_name = (String) location.l0_name.elementAt(i);
      String level_1_id = "";
      String color = "FFFFFF";
      if(i % 2 != 0)
      {
        color = "#CDDDFF";
      }
    %>
					<tr>
						<td width="480" class="top_divider" colspan="2" style="font-size: 0.8em"></td>
					</tr>
					<tr style="font-size: 0.8 em" bgcolor="<%= color %>" valign="top">
						<!-- Tabel row holding level_0 data -->
						<td width="163">
							<a class="res" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=<%=target%>_0&code1=yes&level_0=<%=level_0_name%>&level_0_number=<%=level_0_id%>&id=<%=level_0_id%>">
								<%out.println(level_0_name);%>
							</a>
							<input type="hidden" name="id" value="<%=level_0_id%>">
						</td>

						<td width="320">
							<table border="0">
								<%for (int n=0; n<location.l1_id.size(); n++)
                {
                  level_1_id = (String) location.l1_id.elementAt(n);
                  String level_1_name = (String) location.l1_name.elementAt(n);
                  String level_1_loc_id = (String) location.l1_loc_id.elementAt(n);
                  if(level_1_loc_id.equals(level_0_id))
                  {%>
								<tr valign="top" style="font-size: 0.8em">
									<td width="167" class="LR_divider" colspan="2">
										<a class="res" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=<%=target%>_1&code1=yes&level_1=<%=level_1_name%>&level_1_number=<%=level_1_id%>&id=<%=level_1_id%>">
											<%out.println(level_1_name);%>
										</a>
										<input type="hidden" name="id" value="<%= level_1_id %>">
									</td>
									<td>
										<table>
											<%for (int m=0; m<location.l2_id.size(); m++)
                              {
                                String level_2_id = (String) location.l2_id.elementAt(m);
                                String level_2_name = (String) location.l2_name.elementAt(m);
                                String level_2_loc_id = (String) location.l2_loc_id.elementAt(m);
                                if(level_2_loc_id.equals(level_1_id))
                                {%>
											<tr valign="top" style="font-size: 0.8em">
												<td width="150">
													<a class="res" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=<%=target%>_2&code1=yes&level_2=<%=level_2_name%>&level_2_number=<%=level_2_id%>&id=<%=level_2_id%>">
														<%out.println(level_2_name);%>
													</a>
													<input type="hidden" name="id" value="<%= level_2_id %>">
												</td>
											</tr>
											<%    }
                              }%>

										</table>
									</td>
								</tr>
								<%}%>
								<% }%>
							</table>
						</td>
					</tr>
					<tr>
						<td width="480" class="spacer" colspan="2"></td>
					</tr>
					<%
    }%>
					<tr>
						<td width="480" class="bot_divider" colspan="2"></td>
					</tr>
				</table>
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
