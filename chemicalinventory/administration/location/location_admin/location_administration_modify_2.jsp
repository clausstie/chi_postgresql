<%@ page language="java" import="java.util.*" import="java.net.*"%>
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
		<SCRIPT LANGUAGE="JavaScript">
// Begin
<%
if(request.getParameter("code1")!=null)
{%>
var arrItems1 = new Array();
var arrItemsGrp1 = new Array();
var arrItemsLocID1 = new Array();

<%  location.showAllLocations();
    for(int i=0; i<location.l1_id.size(); i++)
    {
       String id = (String) location.l1_id.elementAt(i);
       String loc_id = (String) location.l1_loc_id.elementAt(i);
       String location_name = (String) location.l1_name.elementAt(i);
       %>
        arrItemsLocID1[<%= i %>] = "<%= loc_id %>";
        arrItems1[<%= i %>] = "<%= Util.encodeTag(location_name) %>";
        arrItemsGrp1[<%= i %>] = "<%= id %>";
       <%
    }  
%>

function selectChange(control, controlToPopulate, ItemArray, GroupArray)
{
  var myEle ;
  var x ;
  // Empty the second drop down box of any choices
  for (var q=controlToPopulate.options.length;q>=0;q--) controlToPopulate.options[q]=null;

  // ADD Default Choice - in case there are no values
  myEle = document.createElement("option") ;
  myEle.value = 0 ;
  myEle.text = "[SELECT]" ;
    try {
        /* this will work for IE, Gecko will throw an Exception... */
         controlToPopulate.add(myEle);
      }
    catch (ex) {
    /* ... catch it and do it the right way */
        controlToPopulate.add(myEle, null);
    }
  // Now loop through the array of individual items
  // Any containing the same child id are added to
  // the second dropdown box
  for ( x = 0 ; x < ItemArray.length  ; x++ )
    {
      if(control.name == "firstChoice")
      {
        if ( arrItemsLocID1[x] == control.value )
          {
            myEle = document.createElement("option") ;
            myEle.value = GroupArray[x];
            myEle.text = ItemArray[x] ;
           try {
              /* this will work for IE, Gecko will throw an Exception... */
              controlToPopulate.add(myEle);
            }
            catch (ex) {
              /* ... catch it and do it the right way */
              controlToPopulate.add(myEle, null);
            }
          }
      }
    }
}
<%
}
%>
//  End
</script>
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

				<%/*Display the level 2 to modify
			 * and display dropdown boxes to perform the modification*/
			if (request.getParameter("code1") != null) {%>
				<p>
					On this page you can modify the location for the level 2 location: '
					<%=location.getLevel_2().toUpperCase()%>
					'.
					<br>
					The current location is:
					<%=Util.getLocation_no_br(location.getLevel_2_number())%>
				</p>

				<form name="locationChoices" method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_2&code2=yes">
					<table class="box" width="720" border="0" cellpadding="1" cellspacing="1">
						<TR>
							<TH colspan="5" class="blue">
								Location
							</TH>
						</TR>
						<tr>
							<th align="left" class="standard">
								Move:
							</th>
							<td width="200">
								<input class="w200" type="text" name="level_2" value="<%=location.getLevel_2()%>">
								<input type="hidden" name="level_2_number" value="<%=location.getLevel_2_number()%>" readonly="readonly">
							</td>
							<th align="left">
								To:
							</th>
							<td>
								<select style="width:200;" id="firstChoice" name="firstChoice" onchange="selectChange(this, locationChoices.secondChoice, arrItems1, arrItemsGrp1);">
									<option value="X" selected>
										[SELECT]
									</option>
									<%location.showAllLocations();
				for (int i = 0; i < location.l0_id.size(); i++) {
					String loc_id = (String) location.l0_id.elementAt(i);
					String location_name = (String) location.l0_name.elementAt(i);

					%>
									<option value="<%= loc_id %>">
										<%=Util.encodeTag(location_name)%>
									</option>
									<%}%>
								</select>
							</td>
							<TD>
								<select id="secondChoice" name="secondChoice" style="width:200;">
									<option>
										[SELECT]
									</option>
							</td>
						</tr>
					</table>
					<br>
					<input class="submit" type="submit" value="Modify Location" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_2&code2=yes'">
					<input class="submit" type="submit" value="Modify Name" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_2&code3=yes'">
					<input class="submit" type="submit" value="Delete Location" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_2&code4=yes'">
					<input class="submit" type="submit" value="Cancel" onclick="this.form.action='<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location'">
				</form>

				<table style="border-style: solid; border-width: 1px; border-color: blue;" cellspacing="7" width="500">
					<tr>
						<td align="center">
							NOTE: This option will reorganize your storage locations.
							<br>
							The containers at the level 2 location will be connected to the same level 2
							<br>
							location as before the modification.
						</td>
					</tr>
				</table>
				<%}
			/*perform the update and display a receipt.*/
			if (request.getParameter("code2") != null) {
				location.updateLevel2(0);

				if (location.isUpdate()) {%>
				<h3>
					The location has now been updated. Modified location:
					<%=Util.getLocation_no_br(location
									.getLevel_2_number())%>
				</h3>
				<%} else {%>
				<h3>
					ERROR! The location could not be updated
				</h3>
				<%}
			}

			/*perform the update of the name.*/
			if (request.getParameter("code3") != null) {
				location.updateLevel2(1);

				if (location.isUpdate()) {%>
				<h3>
					The location has now been updated. New name for the location:
					<%=Util.encodeTag(location.getLevel_2())%>
				</h3>
				<%} else {%>
				<h3>
					ERROR! The name of the location could not be updated
				</h3>
				<%}
			}

			/*Delete the location*/
			if (request.getParameter("code4") != null) {
				location.updateLevel_delete_info(2);
				if (location.isUpdate()) {%>
				<h3>
					You want to delete the level_2 location: '
					<%=location.getLevel_2()%>
					'.
				</h3>
				<p>
					Deleting this location will delete the following data:
				</p>
				<form method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_0_list1&code1=yes&firstChoice=x&secondChoice=x&thirdChoice=<%=location.getLevel_2_number()%>" target="blank">
					<table>
						<tr>
							<td>
								<%=location.getNo_cont()%>
								Container(s).&nbsp;&nbsp;
							</td>
							<td>
								<input class="submit" type="submit" value="Show List" <%if(location.getNo_cont() == 0) out.print("disabled=true");%>>
							</td>
						</tr>
					</table>
				</form>
				<hr>
				<form method="post" action="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=modify_location_2&code5=yes&firstChoice=x&secondChoice=x&thirdChoice=<%=location.getLevel_2_number()%>">
					<input class="submit_width130" type="submit" value="Delete Values">
				</form>
				<%} else {%>
				<h3>
					ERROR! The location could not be deleted
				</h3>
				<%}
			}

			/*Delete containers and locations */
			if (request.getParameter("code5") != null) {
				location.delete_container_at_location(2);

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
