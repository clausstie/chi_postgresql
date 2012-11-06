<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="resource" class="chemicalinventory.resource.Resources" scope="page"/>
<jsp:setProperty name="resource" property="*"/>

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
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
    <title>Create an internal or external ressource for a compound</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
    <script language="JavaScript" src="../../script/overlib.js"></script>
    <script language="JavaScript" src="../../script/inventoryScript.js"></script>
    <script LANGUAGE="JavaScript">
    function openWindow(url)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=250, height=350")
	}
    
    function replaceChars(entry) 
    {
		out = " "; // replace this
		add = "_"; // with this
		temp = "" + entry; // temporary holder
	
		while (temp.indexOf(out)>-1) 
		{
			pos= temp.indexOf(out);
			temp = "" + (temp.substring(0, pos) + add + 
			temp.substring((pos + out.length), temp.length));
		}
		document.resource_form.name.value = temp;
	}
    
    var name=new Array();
<%  
    Vector taken = new Vector();
    taken.clear();
	taken = resource.getResourceNameList();

    for(int i=0; i<taken.size(); i++)
    {
       String name = (String) taken.elementAt(i);
      %>name[<%= i %>] = "<%= name %>";<%
    }  
%>

	function name_validator()
	{
		var resource_name = document.resource_form.name.value;
	    var checkno = 0;
	
		for(i = 0; i <= name.length; i++)
		{
			if(name[i] == resource_name)
			{
	            checkno = 1;
				return checkno;
			}
		}
	        return checkno;
	}
    
	function validateForm(form) 
	{
	 if (trim(form.name.value) == "") 
	 {
	  alert("Please fill in a valid, uniq name for this resource!");
	  form.name.focus();
	  return false;
	 }
	 if (name_validator() == 1)
	 {
	   alert("The entered resource name cannot be used as it has allready been taken!");
	   form.name.focus();
	   return false; 
	 } 
	 return true;
	}
	</script>
    
    
  </head>
    
  <body>
  <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
  
  <span class="posAdm1">
 	<img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_compound.png" height="55" width="820" usemap="#nav_bar" border="0">
  </span> 
  <span class="posAdm2">
	   | <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">Register a new chemical</a> |
	     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical">Modify a exsisting chemical</a> |
	     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=create_com_resource">Create resource</a> |
	     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify">Modify resource</a> |
  </span>
  <span class="textboxadm">
	
   <%
    if (request.getParameter("code1") == null)
 	{%>
    <h2>Create internal or external resource</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
 	  <center>
		<form method="post" action="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=create_com_resource&code1=yes" name="resource_form" onsubmit="return validateForm(this)">
			<table class="box" width="650px" cellspacing="0"><!-- the box sourrounding the form-->
				<tr>
					<th class="blue" align="center" colspan="2">Define Resource</th>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<th align="left" style="width: 130px">Name</th>
								<td><input type="text" name="name" style="width: 260px" onkeyup="javascript:this.value=this.value.toUpperCase();"/></td>
							</tr>
							<tr valign="top">
								<th align="left" style="width: 130px">Mouse Over Text</th>
								<td>
									<textarea name="mouse_text" cols="30" rows="10" style="width: 260px"></textarea>
								</td>
								<td>
									<a href="javascript:void(0);" onmouseover="return overlib('For the mouse over fields and url input and id fields, you can include the following parameters: Chemical Name, Compound Id, and CAS Number.<br/><br/>To include the parameter include the following codes: <br/><br/>Chemical Name: !1!<br/>Compound Id: !2!<br/>CAS Number: !3!', RIGHT, BORDER, 2);" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
								</td>
							</tr>
							<tr>
								<th align="left" style="width: 130px">Sticky</th>
								<td><input type="checkbox" name="sticky_text""/></td>
							</tr>
							<tr>
								<th align="left" style="width: 130px">Icon</th>
								<td><!--create me dynamically...-->
									<select name="icon" style="width: 260px">
										<%=resource.listIcons()%>																		
									</select>
								</td>
								<td>
									<a class="black_u" href="#" onclick="openWindow('<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=showIcons')">Show Icon(s)</a>
								</td>
							</tr>						
							<tr>
								<th align="left" style="width: 130px">Position</th>
								<td><!--create me dynamically...-->
									<select name="position" style="width: 260px">
										<%=resource.listPositions()%>																	
									</select>
								</td>
							</tr>						
						</table>
					</td>
				</tr>
				<tr>
					<th class="blue" align="center" colspan="2">Define Location</th>
				</tr>
				<tr>
					<td>Internal&nbsp;<input type="radio" value="internal" name="select_dest" checked="checked"/>&nbsp;External&nbsp;<input type="radio" value="external" name="select_dest"/><br/>
					<p><b><i>Fill in only the appropriate section, either INTERNAL or EXTERNAL.</i></b></p>
					</td>
				</tr>
				<tr>
					<th class="blue" align="center">INTERNAL</th>
					<th class="blue">
						<a href="javascript:void(0);" onmouseover="return overlib('Define a resource inside of the CI application.', LEFT, BORDER, 2);" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
					</th>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<th align="left" style="width: 130px">Visibility</th>
								<td><!--create me dynamically...-->
									<select name="visibility" style="width: 260px">
										<%=resource.listVisibilityOptions()%>
									</select>
								</td>
							</tr>
							<tr>
								<th align="left" style="width: 130px">Icon Disabled</th>
								<td><!--create me dynamically...-->
									<select name="disabled" style="width: 260px">
										<%=resource.listIcons_disabled()%>
									</select>
								</td>
								<td>
									<a class="black_u" href="#" onclick="openWindow('<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=showIcons&disabled=yes')">Show Icon(s)</a>
								</td>
							</tr>
							<tr valign="top">
								<th align="left" style="width: 130px">Alternative Text</th>
								<td>
									<textarea name="alternative_mouse_text" cols="30" rows="5" style="width: 260px"></textarea>
								</td>
							</tr>
							<tr>
								<th align="left" style="width: 130px">Ressource</th>
								<td><!--create me dynamically...-->
									<select name="resource" style="width: 260px">
										<%=resource.listAvailablePages()%>																
									</select>
								</td>
							</tr>											
							<tr>
								<th align="left" style="width: 130px">Include</th>
								<td>
								<%=resource.internal_include()%>									
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th class="blue" align="center">EXTERNAL</th>
					<th class="blue">
						<a href="javascript:void(0);" onmouseover="return overlib('Here define a resource outside of the CI application.', LEFT, BORDER, 2);" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
					</th>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<th align="left" style="width: 130px">URL</th>
								<td><input type="text" name="url" style="width: 260px"/></td>
							</tr>						
							<tr>
								<th align="left" style="width: 130px">Input 1</th>
								<td><input type="text" name="url_input_1" style="width: 260px"/></td>
								<th align="left" style="width: 70px">Id 1</th>
								<td><input type="text" name="id_1" style="width: 130px"/></td>
							</tr>						
							<tr>
								<th align="left" style="width: 130px">Input 2</th>
								<td><input type="text" name="url_input_2" style="width: 260px"/></td>
								<th align="left" style="width: 70px">Id 2</th>
								<td><input type="text" name="id_2" style="width: 130px"/></td>
								
							</tr>						
							<tr>
								<th align="left" style="width: 130px">Input 3</th>
								<td><input type="text" name="url_input_3" style="width: 260px"/></td>
								<th align="left" style="width: 70px">Id 3</th>
								<td><input type="text" name="id_3" style="width: 130px"/></td>								
							</tr>
							<tr>
								<th align="left" style="width: 130px">Input 4</th>
								<td><input type="text" name="url_input_4" style="width: 260px"/></td>
								<th align="left" style="width: 70px">Id 4</th>
								<td><input type="text" name="id_4" style="width: 130px"/></td>								
							</tr>	
							<tr>
								<th align="left" style="width: 130px">CAS required</th>
								<td><input type="checkbox" name="cas_required"/></td>
							</tr>
							<tr>
								<th align="left" style="width: 130px">Icon Disabled</th>
								<td><!--create me dynamically...-->
									<select name="disabled_url" style="width: 260px">
										<%=resource.listIcons_disabled()%>
									</select>
								</td>
								<td>
									<a class="black_u" href="#" onclick="openWindow('<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=showIcons&disabled=yes')">Show Icon(s)</a>
								</td>									
							</tr>
							<tr valign="top">
								<th align="left" style="width: 130px">Alternative Text</th>
								<td>
									<textarea name="alternative_mouse_text_url" cols="20" rows="5" style="width: 260px"></textarea>
								</td>
							</tr>
							
						</table>
					</td>
				</tr>			
			</table><br/>
		    <input class="submit" type="submit" value="Submit" onClick="replaceChars(document.resource_form.name.value);">&nbsp;&nbsp;&nbsp;
    		<input class="submit" type="reset" value="Reset">
		</form>
	</center>
	<%
	}
	if(request.getParameter("code1") != null)
	{
		//register the values
		boolean check = resource.createResource();
		int status = resource.getStatus();
		
		/*new resource successfully created*/
		if(check)
		{
		%>
			<h2>Create resource - status</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<h3>The new resource has been succesfully created.</h3>
		<%
		}
		else//error in creation of new resource
		{
			%>
			<h2>Create resource - Error</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<%
			if(status == 1)
			{
				%><h3>The new resource could not be created, error in input fields.</h3>
			<%
			}
			else if(status == 2)
			{%>
				<h3>The new resource could not be created, the selected name is taken.</h3>
			<%
			}
			else
			{
				%><h3>The new resource could not be created, an error orcurred.</h3><%
			}
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