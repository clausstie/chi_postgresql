<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="map" class="chemicalinventory.analysis.AnalysisMapBean" scope="page"/>
<jsp:setProperty name="map" property="*"/>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
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
<script language="JavaScript" src="../../script/inventoryScript.js"></script>
<script type="text/javascript">
function getValue(f){

  var val = "";
  ifrChecks = window.frames['list_frame'].document.analysis.idArray;
  if(ifrChecks.length){
    for(i=0;ifrChecks.length>i;i++){
      if(ifrChecks[i].checked)
        val += ", " + ifrChecks[i].value;
    }
  }else if(ifrChecks)
    val = ", " + ifrChecks.value;
  
  f.id_field.value = (val)?val.substring(2):"";
}

function validateForm(form) 
{
 if(trim(form.map_name.value)== "")
 {
    alert("Please fill a name for the map");
    form.map_name.focus();
    return false;
 }
 return true;
}
</script>
<title>Modification of an analysis map - linking analysis to compound</title>
</head>
<body>
<%
//initial values to be used on this page
boolean check = false;
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_analysis.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">Create Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis">Display Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_analysis">Modify Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=remove_analysis">Remove Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=reactivate_analysis">Reactivate Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">Create Analysis Map</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">Modify Analysis Map</a> |
</span>
<span class="textboxadm">
<%

//Deactivate the map
if(request.getParameter("code5") != null)
{
	check = map.deActivateMap(map.getMap_id());	
}

//first display a list of all analysis and select one to modify
if(request.getParameter("code1") == null && request.getParameter("code3") == null && request.getParameter("code4") == null)
{%>
<h2>Modify analysis map</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>Select the map to modify</p>
<center>
	<table class="box" cellspacing="0" cellpadding="1" width="606">
		<thead>
			<tr>
				<th class="blue" width="196">Map name</th>
				<th class="blue" width="335">Remark:</th>
				<th class="blue" width="60">&nbsp;</th>
				<th width="15">&nbsp;</th>
			</tr>		
		</thead>
		<tbody>
			<tr>
				<td colspan="6">
					<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=map_list" width="600" height="400" marginwidth="0" name="list_frame">
					</iframe>
					
					<input type="hidden" name="id_field" value="">
				</td>
				
			</tr>				
		</tbody>
	</table>
</center>
<%

	//display text after having de activated a map
	if(request.getParameter("code5") != null)
	{	
		if(check)
		{%>
			<h3>The selected map has been removed!</h3><%
		}
		else
		{%>
			<h3>The selected map could not be removed!</h3><%
		}	
	}

}//end display list of active maps


if (request.getParameter("code1") != null )
{
	map.getMapInfo();
	//get the list of analysis allready connected to this map.
	String list_of_ana = map.getList_of_a();
	String id = map.getMap_id();
	 //create a session element containing the latest search result.
	HttpSession mapSession = request.getSession(true);
	mapSession.setAttribute("list", list_of_ana);

%>
<h2>Modify analysis map</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<P>This is the data registered for the selected map</P>
<center>
	<form method="post" action="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map&code3=yes&map_id=<%=id%>" onsubmit="getValue(this);">
		<table class="box" cellspacing="1" cellpadding="1" border="0" width="75%" frame="box">
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis Map Data</th>
						</tr>
						<tr>
							<th align="left" width="160">Map Name:</th>
							<td colspan="4">
								<INPUT type="text" size="50" name="map_name" value="<%=chemicalinventory.utility.Util.encodeTag(map.getMap_name())%>">
								<INPUT type="hidden" size="50" name="map_name_original" value="<%=map.getMap_name()%>">
								<input type="hidden" name="list_of_a" value="<%=list_of_ana%>">
							</td>
						</tr>
						<tr>
							<th align="left" width="180">Description:</th>
							<td colspan="4">
								<textarea rows="5" cols="38" name="remark"><%=chemicalinventory.utility.Util.encodeTag(map.getRemark())%></textarea>							
								<INPUT type="hidden" size="50" name="remark_original" value="<%=map.getRemark()%>">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis linked in map</th>
						</tr>
						<tr>
							<th class="blue">#</th>
							<th class="blue">Analysis Name</th>
						</tr>
						
						<%//show receipt for the creation
						Vector elements = map.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{
							String data = (String) elements.get(i);
							data = chemicalinventory.utility.Util.encodeTag(data);
						%>
							<tr>
								<td align="center" width="15"><%=String.valueOf(i+1)%></td>
				            	<td><%=data%></td>
				            </tr><%
						}%>		
		    		</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" cellspacing="0" cellpadding="1" width="100%">
						<tr>
							<th colspan="5" class="blue">Add or remove analysis</th>
						</tr>
						<tr height="2">
							<td colspan="5"></td>
						</tr>
						<tr>
							<th class="blue" width="31">&#63;</th>
							<th class="blue" width="198">Analysis name</th>
							<th class="blue" width="464">Description</th>
							<th class="blue" width="53">Version</th>
							<th width="15">&nbsp;</th>
						</tr>	
						<tr>
							<td colspan="6">
								<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=analysis_list3&map_id=400" width="750" height="300" marginwidth="0" name="list_frame">
								</iframe>
								
								<input type="hidden" name="id_field" value="">
							</td>
						</tr>
		    		</table>
				</td>
			</tr>
		</table><br>
		<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map&code3=yes&map_id=<%=id%>'">
		<input class="submit" type="submit" name="Remove" value="Remove Map" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map&code5=yes&map_id=<%=id%>'">
		<input class="submit" type="reset" name="reset" value="Reset">
	</form>
</center><%
}

if(request.getParameter("code3") != null)
{%>
	<h2>Modify an analysis map - ERROR</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
<%
	boolean check2 = map.modifyMap();
	String id1 = map.getMap_id();

	if(check2)
	{
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_map&code4=yes&map_id="+id1);
	}
	else
	{
		%>
		<H3>Registration failed try again <a href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">click here</a></H3>
<%	}
}//end code 2

//show the data entered for the analysis
if (request.getParameter("code4") != null)
{
	map.getMapInfo();
%>
	<h2>Modify map - result</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<P>This is the data registered for your map</P>
	<center>
		<table class="box" cellspacing="1" cellpadding="1" border="0" width="75%" frame="box">
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis Map Data</th>
						</tr>
						<tr>
							<th align="left" width="160">Map Name:</th>
							<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(map.getMap_name())%></td>
						</tr>
						<tr>
							<th align="left" width="180">Description:</th>
							<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(map.getRemark())%></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis linked in map</th>
						</tr>
						<tr>
							<th class="blue">#</th>
							<th class="blue">Analysis Name</th>
						</tr>
						
						<%//show receipt for the creation
						Vector elements = map.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{
							String data = (String) elements.get(i);
							data = chemicalinventory.utility.Util.encodeTag(data);
						%>
							<tr>
								<td align="center" width="15"><%=String.valueOf(i+1)%></td>
				            	<td><%=data%></td>
				            </tr><%
						}%>		
		    		</table>
				</td>
			</tr>
		</table>
	</center><%
}//end code 3
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