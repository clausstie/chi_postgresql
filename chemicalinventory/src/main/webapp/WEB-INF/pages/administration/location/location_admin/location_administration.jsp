<%@ page language="java" import="java.util.*"%>
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
			The main page for location administration
		</title>
		<script LANGUAGE="JavaScript">
function textBox_0(form)
{
   if(form.level_0_select.value != "X")
   {
       form.level_0.value = "";
       form.level_0.disabled = true;
   }
   if(form.level_0_select.value == "X")
   {
       form.level_0.disabled = false;
   }
}

function textBox_1(form)
{
   if(form.level_1_select.value != "X")
   {
       form.level_1.value = "";
       form.level_1.disabled = true;
   }
   if(form.level_1_select.value == "X")
   {
       form.level_1.disabled = false;
   }
}
</script>
	</head>
	<body>
		<%
  String code2 = "";
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
				Register a new storage location
			</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<center>
				<p>
					The registration of a new location requires input to 3 different levels.
					<br>
				<ul>
					<li>
						On Level 0 you register the building where a container can be stored.
					</li>
					<li>
						On level 1 you register the room or similar that is located in the building from level 0.
					</li>
					<li>
						On Level 2 you specify the row/fridge/shelf in the room from level 1.
					</li>
				</ul>
				</p>
						<%
/*The first image to be displayed = no values entered
* here the user has the choice between writing a new value or picking 
* an old location from the selection box */
if(request.getParameter("code1") == null && request.getParameter("code2") == null)
{%>
						<form method="post" name="location" action="<%= Attributes.LOCATION_ADMINISTRATOR_BASE %>?action=Location&code1=yes">
							<table name="level_zero" class="special" cellspacing="1" cellpadding="1" border="1">
								<tr>
									<th align="left">
										Level 0 (Building/Area):
									</th>
									<td>
										<input type="text" name="level_0">
									</td>
									<td>
										<select name="level_0_select" onchange="textBox_0(this.form)">
											<option value="X" selected>
												[SELECT]
											</option>
											<%  
                      location.level0List();
                      for(int i=0; i<location.list_0_id.size(); i++)
                      {
                         String loc_id = (String) location.list_0_id.elementAt(i);
                         String location_name = (String) location. list_0_name.elementAt(i);
                         %>
											<option value="<%= loc_id %>">
												<%= location_name %>
											</option>
											<%
                      }
                  %>
										</select>
									</td>
									<td>
										<input class="submit" type="Submit" name="Submit" value="Submit">
									</td>
								</tr>
							</table>
						</form>
						<%
}
    /*Values entered in level_0.
    * After values has been entered into level_0, the box for level_1 is diplayed
    * Depending if the level_0 value is new or old an selection box is shown.*/
    if(request.getParameter("code1") != null && (!location.getLevel_0().equals("") || location.getLevel_0()==null))
    {%>
						<form method="post" name="location_1" action="<%= Attributes.LOCATION_ADMINISTRATOR_BASE %>?action=Location&code1=yes">
							<table class="blue" cellspacing="2" cellpadding="1" border="1" width="400">
								<tr>
									<th class="blue" width="225" align="left">
										Level 0 (Building/Area):
									</th>
									<td>
										<input type="text" name="level_0" value="<%= location.getLevel_0() %>" readonly>
										<input type="hidden" name="level_0_number" value="<%= location.getLevel_0_number() %>">
									</td>
								</tr>
							</table>
							<br>
							<%
        //Values entered in level_0 but not in level_1
        if(location.getLevel_1() == null || location.getLevel_1().equals(""))
        {%>
							<table class="special" cellspacing="1" cellpadding="1" border="1">
								<tr>
									<th align="left">
										Level 1 (Room/Area):
									</th>
									<td>
										<input type="text" name="level_1">
									</td>
									<%if(!location.getLevel_0_number().equals("X"))
                        {%>
									<td>
										<select name="level_1_select" onchange="textBox_1(form)">
											<option value="X" selected>
												[SELECT]
											</option>
											<%  
                                  location.level1List();
                                  for(int i=0; i<location.list_1_id.size(); i++)
                                  {
                                     String loc_id = (String) location.list_1_id.elementAt(i);
                                     String location_name = (String) location. list_1_name.elementAt(i);
                                     %>
											<option value="<%= loc_id %>">
												<%= location_name %>
											</option>
											<%
                                  }
                              %>
										</select>
									</td>
									<%}%>
									<td>
										<input class="submit" type="Submit" name="Submit" value="Submit">
									</td>
								</tr>
							</table>
							<br>
						</form>
						<%
         /*Values entered in level_0 and in level_1.
         *The user always has to enter values into level_2.*/              
          } else if(location.getLevel_1() != null || !location.getLevel_1().equals(""))
                 {%>
						<table class="blue" cellspacing="1" cellpadding="1" border="1" width="400">
							<tr>
								<th class="blue" width="225" align="left">
									Level 1 (Room/Area):
								</th>
								<td>
									<input type="text" name="level_1" value="<%= location.getLevel_1() %>" readonly>
									<input type="hidden" name="level_1_number" value="<%= location.getLevel_1_number() %>">
								</td>
							</tr>
						</table>
						<br>
						<%//Values entered in level_0 and in level_1 but not in level_2
                      if(location.getLevel_2() == null || location.getLevel_2().equals(""))
                      {%>
						<table class="special" cellspacing="1" cellpadding="1" border="1" width="400">
							<tr>
								<th width="225" align="left">
									Level 2 (Cabinet/Fridge):
								</th>
								<td>
									<input type="text" name="level_2">
								</td>
								<td>
									<input class="submit" type="Submit" name="Submit" value="Submit">
								</td>
							</tr>
						</table>
						</form>
						<%                       
                      }                         
                        //Values entered in level_0 and in level_1 and in level_2
                        else if(location.getLevel_2() != null || !location.getLevel_2().equals(""))
                             {%>
						</form>
						<table class="blue" cellspacing="1" cellpadding="1" border="1" width="400">
							<tr>
								<th class="blue" width="225" align="left">
									Level 2 (Cabinet/Fridge):
								</th>
								<td>
									<input type="text" name="level_2" value="<%= location.getLevel_2() %>" readonly>
									<input type="hidden" name="level_2_number" value="<%= location.getLevel_2_number() %>">
								</td>
							</tr>
						</table>
						<form method="post" action="<%= Attributes.LOCATION_ADMINISTRATOR_BASE %>?action=Location&code2=yes">
							<br>
							<input class="submit_nowidth" type="Submit" name="Submit" value="Submit the location values">
							<input type="hidden" name="level_0" value="<%= location.getLevel_0() %>">
							<input type="hidden" name="level_1" value="<%= location.getLevel_1() %>">
							<input type="hidden" name="level_2" value="<%= location.getLevel_2() %>">
							<input type="hidden" name="level_0_number" value="<%= location.getLevel_0_number() %>">
							<input type="hidden" name="level_1_number" value="<%= location.getLevel_1_number() %>">
							<input type="hidden" name="level_2_number" value="<%= location.getLevel_2_number() %>">
						</form>
						<br />
						<%}
                 }
    }
   else if(request.getParameter("code1") != null && (location.getLevel_0().equals("") || location.getLevel_0()==null))
        {
            response.sendRedirect(Attributes.LOCATION_ADMINISTRATOR_BASE+"?action=Location");
        }

//Register the values...
if(request.getParameter("code2") != null)
{
    boolean check = location.registerControl();

    if(check == true)
    {
%>
						<hr>
						<h3>
							The registration has been completed
						</h3>
						<p>
							The following values has been registrered ( Level 0 | Level 1 | Level 2 ):
						</p>
						<h4>
							<%= location.getLocation() %>
						</h4>
						<%
    }
    else
    {
        %>
						<h3>
							The registration failed, return to
							<a href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
								location registration
							</a>
						</h3>
						-->
						<%
    }

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
