<%@ page language="java" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.table.CITable" %>
<%@ page import="chemicalinventory.table.HTMLTable" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="unit" class="chemicalinventory.unit.UnitBean" scope="page"/>
<jsp:setProperty name="unit" property="*"/>

<%
	//page to show
	int page_no = Util.getIntValue(request.getParameter(CITable.PAGE_NO));
	//no of results per. page
	int no_pr_page = Util.getIntValue(request.getParameter(CITable.NO_PER_PAGE));
%>

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
<script LANGUAGE="JavaScript">  
function validateForm(form) 
{
 if (trim(form.unit_name.value) == "") 
 {
  alert("Please fill in a valid name for the new unit!");
  form.unit_name.focus();
  return false;
 }
return true;
}

</script>
<title>Modification or deletion of allready registered units</title>
</head>
<%
if(request.getParameter("code1") != null)
{%>
<body onload="document.unit.unit_name.focus()">
<%
}
else
{%>
<body>
<%
}
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_unit.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
	| <a class="adm" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=new_unit">Create Unit</a> |
   <a class="adm" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=modify_unit">Modify Unit</a> |
</span>
<span class="textboxadm">

<%
//Display a list of all units in the sytem.
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{
	Vector units = new Vector();
	units = unit.listOfUnits();
	Vector ids = new Vector();

	//get all units from the db.
	ids = unit.getUnit_ids();
	
%>
	<h2>Modify units</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<h3>Select the unit to modify.</h3>
	<center>
	<%


	 //new tabel function start
	 HTMLTable table = new HTMLTable();
	 //Create the vector for the table.
	 Vector for_table = new Vector();
	 if(units != null && units.size() > 0)
	 {
		 for(int n=0; n<units.size(); n++)
		 {
		 	String[] temp = {"&unit_id="+(String)ids.get(n), (String) units.get(n)};
		 	for_table.add(temp);
		 }
	 }
	 
	 String[] headers = {"Units"};
	 int[] header_w = {200};
	 
	 //make sure the current page is displayed, on order by.
	 String page_number = "";
	 if(page_no > 0)
	 	page_number = "&" + HTMLTable.PAGE_NO + "=" + page_no;
	 
	 table.createTable(no_pr_page, for_table, 200, "box", "center", "center", "blue", 
	 				"normal", "blue", "", 1, headers, header_w, CITable.LINK_MODE, "Modify",
	 				Attributes.UNIT_ADMINISTRATOR_BASE+"?action=modify_unit&code1=yes", 
	 				Attributes.UNIT_ADMINISTRATOR_BASE+"?action=modify_unit",
	 				Attributes.UNIT_ADMINISTRATOR_BASE+"?action=modify_unit"+page_number);
	 %>
	 
	 <%=table.writeTable(page_no)%>
	 </center><%
	
}//end list of units
	
if(request.getParameter("errorcode1") != null)
{%>
	<br/>
	<hr/>
	<h3>Could not find unit, please try again!</h3><%
}
	
if (request.getParameter("code1") != null)
{
	//get the name of the unit
	boolean check_unit = unit.getUnitNameFromDb();
	if(check_unit)
	{
  %>
	  	<h2>Modify units</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br>
	    <p>Modify the selected unit</p>	
		<center>
	
	  <form method="post" action="<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=modify_unit&code2=yes" name="unit" onSubmit="return validateForm(this)">
		<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
			<TR><TH colspan="4" class="blue">Unit</TH></TR>
	        <tr>
	            <th align="left" class="standard">Unit value:</th>
	            <td><input class="w200" type="text" name="unit_name" value="<%=unit.getUnit_name()%>">
	                <input type="hidden" name="unit_id" value="<%=unit.getUnit_id()%>"></td>
	        </tr>
	    </table><br>
	    <input class="submit" align="left" type="submit" name="Submit" value="Submit">&nbsp;
	    <input class="submit" type="Reset" value="Reset" align="right">&nbsp;
	    <input class="submit_nowidth" align="left" type="submit" name="Delete" value="Delete Unit" onclick="this.form.action='<%=Attributes.UNIT_ADMINISTRATOR_BASE%>?action=modify_unit&code3=yes'">
	 </form>
	 </center><%
    }//end the unit was NOT found
	else
	{
		response.sendRedirect(Attributes.UNIT_ADMINISTRATOR_BASE+"?action=modify_unit&errorcode1=yes");
	}
}//  end code 1

//modify the name
if (request.getParameter("code2") != null)
{
	boolean check = unit.updateUnit();
	%>
	<h2>Modify units</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<%
	
	if(check)
	{
		%>
		<br/>
		<h3>Unit has been modified to: <%=unit.getUnit_name()%></h3>
		<%
	}
	else
	{
	%>
		<br/>
		<h3>Unit could not be modified!</h3>
	<%
	}
}

//delte the unit
if (request.getParameter("code3") != null)
{
	boolean check = unit.deleteUnit();
	%>
	<h2>Modify units</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<%
		
	if(check)
	{
		%>
		<br/>
		<h3>Unit has been deleted</h3>
		<%
	}
	else
	{
	%>
		<br/>
		<h3>Unit could NOT be deleted!</h3>
	<%
	}
}
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