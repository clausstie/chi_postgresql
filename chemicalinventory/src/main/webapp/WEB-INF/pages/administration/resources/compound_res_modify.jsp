<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="resource" class="chemicalinventory.resource.Resources" scope="page"/>
<jsp:useBean id="modify" class="chemicalinventory.resource.Resources_modify" scope="page"/>
<jsp:setProperty name="modify" property="*"/>

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
    <title>Modify an internal or external ressource for a compound</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
    <script language="JavaScript" src="../../script/overlib.js"></script>
    <script language="JavaScript" src="../../script/inventoryScript.js"></script>    
 	<SCRIPT type="text/javascript">
	    function openWindow(url)
		{
			window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=250, height=350")
		}
	</SCRIPT>    
    
  </head>
  <%
  String text_id = modify.getName();
  String selected_sticky = "";
  String internal = "";
  String external = "";
  String chemical_name = "";
  String cas_no = "";
  String compound_id = "";
  String selected_cas = "";
  %>
  
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
   	if(request.getParameter("code1") == null && request.getParameter("code2") == null)
	{
	//display a list of all currently registered resources.
		Vector list = resource.getResourceNameList();
		
		%>
		<h2>Modify internal or external resource</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<%
		
		if(list.isEmpty())
		{%>
		<center>
			<table class="box" width="450px">
				<tr>
					<th class="blue" align="left">#</th>
					<th class="blue" align="left">Name</th>
					<th class="blue" align="left">&nbsp;</th>
				</tr>
				<tr>
					<td colspan="2"><i>No Resources Registered...</i></td>
				</tr>
			</table>
		</center>
			<%
		}
		else
		{
		%>
		<center>
			<table class="box" width="450px" cellspacing="1" cellpadding="2">
				<thead>
					<tr>
						<th class="blue" align="center" style="width: 100px">#</th>
						<th class="blue" align="center" style="width: 350px">Name</th>
						<th class="blue" align="center" style="width: 350px">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
				<%
				for(int res = 0; res<list.size(); res++)
				{
					String name = (String) list.get(res);
					String color = "normal";
					
					if(res % 2 != 0)
					{
						color = "blue";
					}
					%>
					<tr class="<%=color%>" valign="center">
						<td align="center"><%=res+1%></td>
						<td align="center"><%=chemicalinventory.utility.Util.encodeTag(name)%></td>
						<form action="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify&code1=yes&name=<%=name%>" method="post">
							<td align="center">
								<input class="submit" type="submit" value="Modify"/>
							</td>
						</form>
					</tr>
					<%
				}%>
				</tbody>
			</table>
		</center>
			<%
		}
	}
	if (request.getParameter("errorcode1") != null)
 	{
 		%>
 		<br/>
 		<h3>The selected resource could not be displayed, please try again.</h3>
 		<%
	}	   
	if (request.getParameter("deletecode") != null)
 	{
 		//delete the resource.
 		
 		if(modify.deleteResource(modify.getName()))
 		{
 		%>
 		<br/>
 		<h3>The selected resource has been deleted.</h3>
 		<%
 		}
 		else
 		{
 		%>
 		<br/>
 		<h3>The selected resource could not be deleted, please try again.</h3>
 		<%
 		}
	}	   
    if (request.getParameter("code1") != null)
 	{
 		if(modify.getResourceInfo(text_id))
 		{ 	
	 	%>
	    <h2>Modify internal or external resource</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	 	  <center>
			<form method="post" action="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify&code2=yes&name=<%=modify.getName()%>" name="resource_form">
				<table class="box" width="650px"><!-- the box sourrounding the form-->
					<tr>
						<th class="blue" align="center">Define Resource</th>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<th align="left" style="width: 130px">Name</th>
									<td>
										<p><%=modify.getName()%></p>
									</td>
								</tr>
								<tr valign="top">
									<th align="left" style="width: 130px">Mouse Over Text</th>
									<td>
										<textarea name="mouse_text" cols="30" rows="10" style="width: 260px"><%=modify.getMouse_text()%></textarea>
									</td>
									<td>
										<a href="javascript:void(0);" onmouseover="return overlib('For the mouse over fields and url input and id fields, you can include the following parameters: Chemical Name, Compound Id, and CAS Number.<br/><br/>To include the parameter include the following codes: <br/><br/>Chemical Name: !1!<br/>Compound Id: !2!<br/>CAS Number: !3!', RIGHT, BORDER, 2);" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
									</td>			
								</tr>
								<tr>
									<th align="left" style="width: 130px">Sticky</th>
									<td>
										<%
											//is the sticky text check box selected...
											if(modify.getSticky_text().equalsIgnoreCase("Y"))
												selected_sticky = "checked=\"checked\"";
										%>
										<input type="checkbox" <%=selected_sticky%> name="sticky_text"/>
									</td>
									<td>
										<input type="hidden" name="sticky_text_original" value="<%=modify.getSticky_text()%>" readonly="readonly"/>																	
									</td>
								</tr>
								<tr>
									<th align="left" style="width: 130px">Current Icon</th>
									<td>
										<p><%=modify.getIcon()%></p>
										<img src="<%=Attributes.IMAGE_FOLDER%>/icons/<%=modify.getIcon()%>" border="0"/>
									</td>
									<td>
										<input type="hidden" name="icon_original" value="<%=modify.getIcon()%>" readonly="readonly"/>								
									</td>
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
								<tr valign="middle">
									<th align="left" style="width: 130px">Current Position</th>
									<td>
										<p><%=modify.getPosition()%></p>
									</td>
									<td>	
										<input type="hidden" name="position_original" value="<%=modify.getPosition()%>" readonly="readonly"/>																		
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
						<th class="blue" align="center">Define Location</th>
					</tr>
					<tr>
						<%
							//is the resource currently external or internal
							if(modify.getSelect_dest().equalsIgnoreCase("internal"))
							{
								internal = "checked=\"checked\"";
								external = "";
							}
							else
							{
								external = "checked=\"checked\"";
								internal = "";
							}
						%>
					
						<td>Internal&nbsp;
							<input type="radio" value="internal" <%=internal%> name="select_dest"/>&nbsp;
							External&nbsp;
							<input type="radio" value="external" <%=external%> name="select_dest"/><br/>
							<input type="hidden" name="destination" value="<%=modify.getSelect_dest()%>" readonly="readonly"/>									
							<p><b><i>Fill in only the appropriate section, either INTERNAL or EXTERNAL.</i></b></p>
						</td>
					</tr>
					<tr>
						<th class="blue" align="center">INTERNAL</th>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<th align="left" style="width: 130px">Current Visibility</th>
									<td>
										<p><%=modify.getVisibility_display()%></p>
									</td>
									<td>	
										<input type="hidden" name="visibility_original" value="<%=modify.getVisibility()%>" readonly="readonly"/>									
									</td>
								</tr>
								<tr>
									<th align="left" style="width: 130px">Visibility</th>
									<td><!--create me dynamically...-->
										<select name="visibility" style="width: 260px">
											<%=resource.listVisibilityOptions()%>
										</select>
									</td>
								</tr>
								<tr>
									<th align="left" style="width: 130px">Current Dis. Icon</th>
									<td>
										<p><%=modify.getDisabled()%></p>
										<%
										if(chemicalinventory.utility.Util.isValueEmpty(modify.getDisabled()))
										{%>
										<img src="<%=Attributes.IMAGE_FOLDER%>/icons/disabled/<%=modify.getDisabled()%>" border="0"/>
										<%
										}
										%>
									</td>
									<td>
										<input type="hidden" name="disabled_icon_original" value="<%=modify.getDisabled()%>" readonly="readonly"/>
									</td>
								</tr>							
								<tr>
									<th align="left" style="width: 130px">Icon Disabled</th>
									<td><!--create me dynamically...-->
										<select name="disabled" style="width: 260px">
											<%=resource.listIcons_disabled()%>
										</select>
									<td>
										<a class="black_u" href="#" onclick="openWindow('<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=showIcons&disabled=yes')">Show Icon(s)</a>
									</td>
								</tr>
								<tr valign="top">
									<th align="left" style="width: 130px">Alternative Text</th>
									<td>
										<textarea name="alternative_mouse_text" cols="30" rows="5" style="width: 260px"><%=modify.getAlternative_mouse_text()%></textarea>
									</td>
									<td>	
										<input type="hidden" name="alternative_mouse_text_o" value="<%=modify.getAlternative_mouse_text()%>" readonly="readonly"/>																		
									</td>
								</tr>
								<tr>
									<th align="left" style="width: 130px">Current Resource</th>
									<td>
										<p><%=modify.getResource_display()%></p>
										<input type="hidden" name="resource_original" value="" readonly="readonly"/>																		
									</td>
								</tr>							
								<tr>
									<th align="left" style="width: 130px">Resource</th>
									<td><!--create me dynamically...-->
										<select name="resource" style="width: 260px">
											<%=resource.listAvailablePages()%>																
										</select>
									</td>
								</tr>											
								<tr>
									<th align="left" style="width: 130px">Include</th>
									<td>
									<%=modify.internal_include_modify(modify.getChemical_name(), modify.getCompound_id(), modify.getCas_number())%>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th class="blue" align="center">EXTERNAL</th>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<th align="left" style="width: 130px">URL</th>
									<td>
										<input type="text" name="url" style="width: 260px" value="<%=modify.getUrl()%>"/>
									</td>
									<td>
										<input type="hidden" name="url_original" value="<%=modify.getUrl()%>" readonly="readonly"/>
									</td>
								</tr>						
								<tr>
									<th align="left" style="width: 130px">Input 1</th>
									<td>
										<input type="text" name="url_input_1" style="width: 260px" value="<%=modify.getUrl_input_1()%>"/>
										<input type="hidden" name="url_input_1_original" value="<%=modify.getUrl_input_1()%>" readonly="readonly"/>									
									</td>
									<th align="left" style="width: 50px">Id 1</th>
									<td>
										<input type="text" name="id_1" style="width: 150px" value="<%=modify.getId_1()%>"/>
										<input type="hidden" name="id_1_original" value="<%=modify.getId_1()%>" readonly="readonly"/>									
									</td>
								</tr>						
								<tr>
									<th align="left" style="width: 130px">Input 2</th>
									<td>
										<input type="text" name="url_input_2" style="width: 260px" value="<%=modify.getUrl_input_2()%>"/>
										<input type="hidden" name="url_input_2_original" value="<%=modify.getUrl_input_2()%>" readonly="readonly"/>									
									</td>
									<th align="left" style="width: 50px">Id 2</th>
									<td>
										<input type="text" name="id_2" style="width: 150px" value="<%=modify.getId_2()%>"/>
										<input type="hidden" name="id_2_original" value="<%=modify.getId_2()%>" readonly="readonly"/>									
									</td>
								</tr>						
								<tr>
									<th align="left" style="width: 130px">Input 3</th>
									<td>
										<input type="text" name="url_input_3" style="width: 260px" value="<%=modify.getUrl_input_3()%>"/>
										<input type="hidden" name="url_input_3_original" value="<%=modify.getUrl_input_3()%>" readonly="readonly"/>						
									</td>
									<th align="left" style="width: 50px">Id 3</th>
									<td>
										<input type="text" name="id_3" style="width: 150px" value="<%=modify.getId_3()%>"/>
										<input type="hidden" name="id_3_original" value="<%=modify.getId_3()%>" readonly="readonly"/>
									</td>								
								</tr>
								<tr>
									<th align="left" style="width: 130px">Input 4</th>
									<td>
										<input type="text" name="url_input_4" style="width: 260px" value="<%=modify.getUrl_input_4()%>"/>
										<input type="hidden" name="url_input_4_original" value="<%=modify.getUrl_input_4()%>" readonly="readonly"/>						
									</td>
									<th align="left" style="width: 50px">Id 4</th>
									<td>
										<input type="text" name="id_4" style="width: 150px" value="<%=modify.getId_4()%>"/>
										<input type="hidden" name="id_4_original" value="<%=modify.getId_4()%>" readonly="readonly"/>
									</td>								
								</tr>	
								<tr>
									<th align="left" style="width: 130px">CAS required</th>
									<td>
								    	<%
											//is the cas requirede check box selected...
											if(modify.getCas_required().equalsIgnoreCase("Y"))
												selected_cas = "checked=\"checked\"";
										%>
										<input type="checkbox" <%=selected_cas%> name="cas_required"/>
									</td>
								</tr>
								<tr>
									<th align="left" style="width: 130px">Current Dis. Icon</th>
									<td>
										<p><%=modify.getDisabled_url()%></p>
										<%
										if(chemicalinventory.utility.Util.isValueEmpty(modify.getDisabled_url()))
										{%>
										<img src="<%=Attributes.IMAGE_FOLDER%>/icons/disabled/<%=modify.getDisabled_url()%>" border="0"/>
										<%
										}%>
									</td>
									<td>
										<input type="hidden" name="icon_disabled_url_original" value="<%=modify.getDisabled_url()%>" readonly="readonly"/>
									</td>
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
										<textarea name="alternative_mouse_text_url" cols="20" rows="5" style="width: 260px"><%=modify.getAlternative_mouse_text_url()%></textarea>
									</td>
									<td>
										<input type="hidden" name="alternative_text_url_original" value="<%=modify.getAlternative_mouse_text_url()%>" readonly="readonly"/>									
									</td>
								</tr>							
							</table>
						</td>
					</tr>			
				</table><br/>
			    <input class="submit" type="submit" value="Submit">&nbsp;&nbsp;&nbsp;
	    		<input class="submit" type="reset" value="Reset">&nbsp;&nbsp;&nbsp;
	    		<input class="submit" type="submit" value="Delete Resource" onclick="this.form.action='<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify&name=<%=modify.getName()%>&deletecode=yes'">
			</form>
		</center>
		<%
		}
		else
		{
			response.sendRedirect(Attributes.ADMINISTRATOR_BASE+"?action=resource_modify&errorcode1=yes");
		}
	}
	if(request.getParameter("code2") != null)
	{
		//register the values
		boolean check = modify.modifyResource(modify.getName());
		int status = modify.getStatus();
		
		/*new successfully modified*/
		if(check)
		{
		%>
			<h2>Modify resource - status</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<h3>The resource has been succesfully modified.</h3>
		<%
		}
		else//error in modificaton of new resource
		{
			%>
			<h2>Modify resource - Error</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<%
			if(status == 1)
			{
				%><h3>The resource could not be modified, error in input fields.</h3>
			<%
			}
			else if(status == 2)
			{%>
				<h3>The resource could not be modified, an error orcurred.</h3>
			<%
			}
			else
			{
				%><h3>The resource could not be modified, an error orcurred</h3><%
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