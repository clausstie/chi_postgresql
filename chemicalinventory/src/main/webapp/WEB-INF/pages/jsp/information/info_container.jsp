<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:directive.page import="chemicalinventory.beans.modifyContainerBean"/>
<jsp:directive.page import="chemicalinventory.utility.Util"/>
<jsp:useBean id="info" class="chemicalinventory.beans.userInfoBean" scope="page"/>
<jsp:setProperty name="info" property="*"/>
<jsp:useBean id="userinfo" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="group" class="chemicalinventory.groups.Container_group" scope="page"/>
<html>
<head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              neded to manage a chemical storage, based on the java technology.
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
<script language="JavaScript" src="../script/overlib.js"></script>
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<SCRIPT LANGUAGE="JavaScript">
// Begin
function validateForm(form) 
{
 if (trim(form.container_id.value) == "") 
 {
  alert("Please fill in a valid container id!");
  form.container_id.focus();
  return false;
 }
 else
 {
   if(isNumber(form.container_id.value)==false)
   {
    alert("Please fill a valid container id, numbers only accepted.");
    form.container_id.focus();
    return false;
   }
 }
return true;
}
//End
</SCRIPT>
<title>
Container information
</title>
</head>
<body onload="document.container.container_id.focus()">
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%
info.setBase(Attributes.JSP_BASE);
String user = request.getRemoteUser();
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_container_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="textboxadm">
<h2>Search for container</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<p>Enter the container id below to display information about that container.</p>
<form method="post" action="<%=Attributes.JSP_BASE%>?action=Container&code2=yes" method="post" name="container" onSubmit="return validateForm(this)">
 	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="2" class="blue">Container Information</TH></TR>
        <tr>
            <th align="left" class="standard">Container id:</th>
            <TD><input type="text" name="container_id" class="w200"></TD>
        </tr>
    </TABLE>
    <br>
    <input class="submit" type="submit" value="Submit">
    <input class="submit" type="reset" value="Reset">
</form>
<%
if(request.getParameter("code2") != null) {

	modifyContainerBean modify = new modifyContainerBean();
	
    /*A user can view a container that is not a part of a group, 
      *or a he/she can view the container, if they are both part
      *of the same group*/
     userinfo.retrieveNameId(user);
     int con_id = Integer.parseInt(info.getContainer_id());
     if(group.group_relations(userinfo.getUser_id(), con_id)) {
	
		//Get container info for the entered container.
		modify.setContainerInfo(con_id+"");
		
		if(modify.getSearchOk()) {
	%>
	<br>
	<hr>
	<br>
	  	<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center" colspan="2">General Container Data</th>
	  		</tr>
		  	<tr>
	  			<th align="left" class="standard">Container Id</th>
	  			<td><%=con_id%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Compound</th>
	  			<td><%=modify.getChemical_name()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Supplier</th>
	  			<td><%=modify.getSupplier()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Home Location</th>
	  			<td><%=modify.getLocation()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Current Location</th>
	  			<td><%=info.getCurrentcontainerLocation(con_id)%></td>
	  		</tr>
	  		
	  		<tr>
	  			<th align="left" class="standard">Quantity</th>
	  			<td><%=modify.getQuantity()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Unit</th>
	  			<td><%=modify.getUnit()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Tara Weight</th>
	  			<td><%=modify.getTara_weight()%>&nbsp;g</td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Owner(s)</th>
	  			<td><%=Util.encodeNullValue(modify.getOwner()) %></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Group(s)</th>
	  			<td><%=Util.encodeNullValue(modify.getGroup())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Procurement Date</th>
	  			<td><%=Util.encodeNullValue(modify.getProcurement_date())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Expiry Date</th>
	  			<td><%=Util.encodeNullValue(modify.getExpiry_date())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Registered By</th>
	  			<td><%=modify.getRegister_by()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Registered Date</th>
	  			<td><%=modify.getRegister_date()%></td>
	  		</tr>
	        <tr>
	           <th align="left" class="standard">Remark:</th>
	           <td>
	           	<%= Util.encodeTagAndNull(modify.getRemark())%>
	           </td>
	        </tr>	  		
	  	</table><br>
	  	<% 
	  	}
	  	else {
	  		//no container for this id
	   %>	
	   	<br>
		<hr>
		<br>
  		<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center">General Container Data</th>
	  		</tr>
		  	<tr>
	  			<td><i>Not a valid container, please try again.</i></td>
	  		</tr>
  		
  		</table>
	  	<%
	  	}
	}
	else
	{
		//The user does not have acces to view this container, not in
		//the same group.
			   %>		
		<br>
		<hr>
		<br>	   
  		<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center">General Container Data</th>
	  		</tr>
		  	<tr>
	  			<td><i>Your do not have permission to view the entered container.</i></td>
	  		</tr>
  		</table>
	  	<%
	}
}
%>
</center>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
</map>
</body>
</html>