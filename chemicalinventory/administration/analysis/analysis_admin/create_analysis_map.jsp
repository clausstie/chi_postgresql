<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="map" class="chemicalinventory.analysis.AnalysisMapBean" scope="page"/>
<jsp:setProperty name="map" property="*"/>
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

 if(trim(f.id_field.value)== "")
 {
    alert("A minimum of one analysis must be selected!");
    return false;
 }
 return true;
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
<title>Creation of an analysis map - linking analysis to compound</title>
</head>
<%
if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{
%>
<body onload="document.map.map_name.focus()">
<%
}
else
{
%>
<body>
<%
}
//initial values to be used on this page
String user = request.getRemoteUser().toUpperCase();
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_analysis.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">Create Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis">Display Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_analysis">Modify Analysis</a> |
   <a class="adm" href="<%=Attributes.ADMINISTRATOR_BASE%>?action=remove_analysis">Remove Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=reactivate_analysis">Reactivate Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">Create Analysis Map</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">Modify Analysis Map</a> |
</span>
<span class="textboxadm">
<%
if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{
%>
<h2>Create a new analysis map - Step 1/3</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<!-- First show the initial screen to create a new analysis map-->
<CENTER>
<form method="post" action="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map&code1a=yes" onsubmit="return validateForm(this)" name="map">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="4" class="blue">Analysis Map</TH></TR>
		<tr>
			<th align="left" class="standard">Analysis Map Name</th>
			<td>
				<input class="w200" type="text" name="map_name">
			</td>
		</tr>
		<tr>
			<th align="left" class="standard">Description</th>		
			<td>
				<textarea rows="5" style="width: 200;" name="remark" type="_moz">--</textarea>
			</td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Continue" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map&code1a=yes'">
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}//end code 1 = null (entering of data for step 1)

if (request.getParameter("code1a") != null)
{
	map.setUser(user);
	boolean check = map.createMap();
	String id = map.getMap_id();
	if(check)
	{
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=new_map&code1=yes&map_id="+id);
	}
	else
	{
	%>
		<br>
		<center>
			<H3>The registration failed, please try agiain</H3>
		<center>
	<%
	}
}//end code 1a (redirection)


if (request.getParameter("code1") != null)
{
	String map_id = map.getMap_id();
%>
	<h2>Create a new analysis map - Step 2/3</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<p>Here you must define the analysis, to include in the analysis map</p>
	<center>
	<form method="post" action="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_map&code2=yes&map_id=<%=map_id%>" name="list_analysis" onsubmit="return getValue(this);">
		<table class="box" cellspacing="0" cellpadding="1" width="750">
			<thead>
				<tr>
					<th class="blue" width="31">&#63;</th>
					<th class="blue" width="198">Analysis name</th>
					<th class="blue" width="464">Description</th>
					<th class="blue" width="53">Version</th>
					<th width="15">&nbsp;</th>
				</tr>		
			</thead>
			<tbody>
				<tr>
					<td colspan="6">
						<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list" width="750" height="300" marginwidth="0" name="list_frame">
						</iframe>
						
						<input type="hidden" name="id_field" value="">
					</td>
				</tr>				
			</tbody>
		</table>		
		
		<br>
			<input class="submit" type="submit" name="Submit" value="Continue">
	    	<input class="submit" type="reset" name="reset" value="Reset">
	</FORM>
	</center>
	<%
}//end code 1 (entering data for step 2)

if(request.getParameter("code2") != null)
{%>
	<h2>Create a new analysis map - ERROR</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
<%
	boolean check2 = map.analysisToMap();
	String id1 = map.getMap_id();

	if(check2)
	{
		//activate the map and show info:
		map.activateMap(id1);
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=new_map&code3=yes&map_id="+id1);
	}
	else
	{
		//make sure the map is removed from the dbb:
		map.deleteMap(id1);
	
		%>
		<H3>Registration failed try again <a href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">click here</a></H3>
<%	}
}//end code 2

//show the data entered for the analysis
if (request.getParameter("code3") != null)
{
	map.getMapInfo();
%>
	<h2>Create a new analysis map - Step 3/3</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<P>This is the data registered for your new analysis map.</P>
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