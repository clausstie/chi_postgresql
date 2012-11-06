<%@ page language="java" import="chemaxon.util.*, java.util.*, java.net.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="result" class="chemicalinventory.beans.ResultBean" scope="page"/>
<jsp:useBean id="resources" class="chemicalinventory.resource.DisplayResources" scope="page"/>
<jsp:setProperty name="result" property="*"/>

<%
  String new_compound = request.getParameter("new");
  
  //Is the user in the role who can register/modify containers..
  String role = "container_admin";
  boolean inRole = request.isUserInRole(role);
  
  //get the result data
  String user = request.getRemoteUser();
  result.setBase(Attributes.JSP_BASE);
  result.setUser(user);
  result.result();
   
  //get the resource elements to create internal and external links/resources.
  resources.display(inRole, result.getId());
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
<script language="JavaScript" src="../script/overlib.js"></script>
<title>Search result</title>
<SCRIPT language="JavaScript">
<%=resources.getJava_script()%>
</SCRIPT>
</head>
<body>
<div id="overDiv"
	style="position:absolute; visibility:hidden; z-index:1000;"></div>
<h2>Selected Compound: <%= URLDecoder.decode(result.getChemical_name(), "UTF-8") %></h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	<TABLE class="box" cellpadding="0" cellspacing="0" width="805" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Result</TH></TR>
		<TR>
		<!-- Side for structure search input -->
			<TD>
				<TABLE width="440" cellpadding="0" cellspacing="1" border="0">
					<TR>
					  <TD>
						<%if(result.getStructure()==true)
						  {%>

							<CENTER>
								<script LANGUAGE="JavaScript1.1" SRC="<%=Attributes.MARVIN_JS_FILE%>"></script>
								<script LANGUAGE="JavaScript1.1">
							    <!--
							    marvin_jvm = "builtin"; // "builtin" or "plugin"
							    marvin_gui = "swing"; // "awt" or "swing"
							    mview_begin("<%=Attributes.MARVIN_FOLDER%>", 440, 294);
							    mview_param("mol", "<%= HTMLTools.convertForJavaScript(result.getDbMolfile())%>");
							    mview_param("background", "#FFFFFF");
							    mview_param("animate", "none");
							    mview_param("navmode", "rot3d");
							    mview_param("rendering", "wireframe");
							    mview_param("colorScheme", "mono");
							    mview_param("animFPS", "20");
							    mview_end();
							    //-->
							    </script>
						    </CENTER>
								<%}
						else
						{%>
								<h3>&nbsp;&nbsp;&nbsp;...No structure available.</h3>
								<%
						}%>						
						</TD>
					</TR>
				</TABLE>
			</TD>
					<!-- Side for text search input -->
			<TD>
				<TABLE width="365" cellpadding="0" cellspacing="0" border="1" rules="rows" bordercolor="white">
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Formula:</TH>
						<TD><%= result.getChemicalFormula()%></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD><%= result.getCas_number() %></TD>												
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Mol Weight:</TH>
						<TD><%= result.getMolWeight()%></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Density:</TH>
						<TD><%= result.getDensity() %></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Register Date:</TH>
						<TD><%= result.getRegister_date() %></TD>		
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Registered By:</TH>
						<TD><%= result.getRegister_by() %></TD>												
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Remarks:</TH>
						<TD><%if(result.getRemark()!= null){%><%=result.getRemark()%><%}else{%>No comments.<% }%>
						</TD>												
					</TR>
				</TABLE><!-- Table for text input end -->
			</TD>															
		</TR>				
	</TABLE><!-- Main table end. -->
<br>

<%=resources.getForm_elements()%>

<!--Display action icons-->
<FIELDSET><LEGEND>ACTION</LEGEND>
<table class="action_noheader" width="790px">
	<tr>
		<td><%=resources.getLink_element()%></td>
		<td align="right"><%
		if(new_compound == null || new_compound.equals(""))
		{%> <a href="<%= Attributes.JSP_BASE %>?action=Search&code1=yes&history=true"
			onmouseover="return overlib('Return to the last search result', BORDER, 2);"
			onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_search_30.png"
			border="0"></a> <%
		}%> <a href="<%= Attributes.JSP_BASE %>"
			onmouseover="return overlib('HOME', BORDER, 2);"
			onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png"
			border="0"></a></td>
	</tr>
</table>
</FIELDSET>

<h3>For compound <%= URLDecoder.decode(result.getChemical_name(), "UTF-8") %>,
the following containers exist:</h3>

<%
if(result.container_ID.isEmpty())
{
%>
<table class="box" cellspacing="1" cellpadding="1" width="790">
	<thead>
		<tr>
			<th class="blue">No.</th>
			<th class="blue">Contaier ID:</th>
			<th class="blue">Owner:</th>
			<th class="blue">Home Location:</th>
			<th class="blue">Current Location:</th>
			<th class="blue">Remark:</th>
			<th class="blue">Current Quantity:</th>
		</tr>
	</thead>
	<tbody>
		<tr align="center">
			<td colspan="9">No container registered for this compound.</td>
		</tr>
	</tbody>
</table>
<%
}
else
{
%>
<table class="box" cellspacing="1" cellpadding="1" width="810">
	<thead>
		<tr>
			<th class="blue">No.</th>
			<th class="blue">Container Id:</th>
			<th class="blue">Owner:</th>
			<th class="blue">Home Location:</th>
			<th class="blue">Current Location:</th>
			<th class="blue">Remark:</th>
			<th class="blue">Current Quantity:</th>
			<th class="blue"></th>
		</tr>
	</thead>
	<tbody>
		<%
     for(int i=0; i<result.container.size(); i++)
     {
        String color = "normal";
        if(i % 2 != 0)
        {
          color = "blue";
        }
        String con_id = (String) result.container_ID.elementAt(i);
     %>
		<tr class="<%= color %>">
			<td align="center"><% out.print(i+1); %></td>
			<%
        String data = (String) result.container.elementAt(i);
        StringTokenizer tokens = new StringTokenizer(data, "|", false);
        while(tokens.hasMoreElements())
        {
          String token = tokens.nextToken().trim();

          %>
			<td align="center"><%              
                  out.println(URLDecoder.decode(token, "UTF-8"));
                %></td>
			<%}
          if(result.stat.elementAt(i).equals("0"))
          {%>
			<form method="post" action="<%= Attributes.JSP_BASE %>?action=check_out">
			<td align="center"><input type="hidden" name="id"
				value="<%= con_id %>" /> <input class="submit_width90" type="submit"
				name="Check-Out" value="Check-Out" /></td>
			</form>
			<%}
          else
          {
          	//If the container is checked out show a button to check the container in...
          	//But if the user is not the person that has this container checked out
          	//it can only be checked in by this person if he/she is an administrator!!!
          	String disabled = "";
          	
          	//This bit can be disabled if the check is not wanted!!
          	if(!result.getUser_id().equals(result.stat.elementAt(i)) && inRole == false)
	          	disabled = "disabled=\"disabled\"";
			else
				disabled = "";
				%>
			<form method="post" action="<%=Attributes.JSP_BASE%>?action=check_in">
				<td align="center">
					<input class="submit_width90" type="submit"
							name="Check-In" value="Check-In" style="width: 95px;" <%=disabled%> />
					<input type="hidden" name="id" value="<%= con_id %>" />
				</td>
			</form>
			<%}%>
		</tr>
		<%}%>
	</tbody>
</table>
<%
}
%>
</body>
</html>