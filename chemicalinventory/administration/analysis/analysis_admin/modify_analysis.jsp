<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis"
	class="chemicalinventory.analysis.AnalysisBean" scope="page" />
<jsp:useBean id="unit" class="chemicalinventory.unit.UnitBean"
	scope="page" />
<jsp:setProperty name="analysis" property="*" />
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
 if(trim(form.analysis_name.value)== "")
 {
    alert("Please fill a name for the analysis");
    form.analysis_name.focus();
    return false;
 }
 
 if(trimAll(form.reason_for_change.value) == "")
 {
 	alert("Modification of an analysis requires entry of a reason!");
 	return false;
 }
 
 if (validateForm_Newfields(form) == false)
 {
 	return false;
 }
 
 <%
 //Create javascript check on the existing fields
if (request.getParameter("code1") != null)
{
	//get the analysis data...
	analysis.getAnalysisInfo2();
	
	Vector script = analysis.getScripts();
	
	for (int m=0; m<script.size(); m++)
	{
		String s = (String) script.get(m);
		%>
			<%=s%>
		<%		
	}
}
%>
 return true;
}

var ie4 = false; if(document.all) { ie4 = true; }
function getObject(id) { if (ie4) { return document.all[id]; } else { return document.getElementById(id); } }
function toggle(link, divId) { var lText = link.innerHTML; var d = getObject(divId);
 if (lText == '+') { link.innerHTML = '&#45;'; d.style.display = 'block'; }
 else { link.innerHTML = '+'; d.style.display = 'none'; } }
</script>
<title>Modify analysis</title>
</head>
<body>
<%
//initial values to be used on this page
String user = request.getRemoteUser().toUpperCase();
%>
<span class="posAdm1"> <img
	src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_analysis.png" height="55" width="820"
	usemap="#nav_bar" border="0"> </span>
<span class="posAdm2"> | <a class="adm"
	href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">Create Analysis</a> | <a
	class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis">Display Analysis</a>
| <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_analysis">Modify Analysis</a>
| <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=remove_analysis">Remove Analysis</a>
| <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=reactivate_analysis">Reactivate
Analysis</a> | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">Create
Analysis Map</a> | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">Modify
Analysis Map</a> | </span>
<span class="textboxadm"> <%


//first display a list of all analysis and select one to modify
if(request.getParameter("code1") == null && request.getParameter("code3") == null)
{%>
<h2>Modify analysis</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<p>Select the analysis to modify</p>
<center>
<table class="box" cellspacing="0" cellpadding="1" width="750">
	<thead>
		<tr>
			<th class="blue" width="198">Analysis name</th>
			<th class="blue" width="454">Description</th>
			<th class="blue" width="53">Version</th>
			<th class="blue" width="60">&nbsp;</th>
			<th width="15">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td colspan="6"><iframe id="list_frame" frameborder="0"
				scrolling="yes" src="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=analysis_list2" width="750"
				height="500" marginwidth="0" name="list_frame"> </iframe></td>
		</tr>
	</tbody>
</table>
</center>
<%
}
//second display information about the analysis..
if (request.getParameter("code1") != null)
{
%>
<h2>Modify analysis</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<CENTER><%
//Display an error message if the textfield in the bottom is not filled in with a reason
if(request.getParameter("errorcode1") != null)
{
	%><br>
<h3>This page is for modifying an analysis! Modification can only be
perfomed if a reason for the change is stated in the text area at the
bottom of the page!</h3>
<br>
<%
}

//Display an error message - the registration failed...
if(request.getParameter("errorcode2") != null)
{
	String errorcode = request.getParameter("int");
	String errormessage = "";
	
	if (errorcode.equals("1"))
		errormessage = "Update of analysis data could not be performed.";
	else if (errorcode.equals("3"))
		errormessage = "Update of analysis data could not be performed.";
	else if (errorcode.equals("5"))
		errormessage = "Nothing has been changed, the analysis has not been modified.";
	else
		errormessage = "An unexpected error orcurred, please try again!";
		
	%><br>
<hr>
<h3><%=errormessage%></h3>
<hr>
<br>
<%
}%>

<form method="post"
	action="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=modify_analysis&code2=yes"
	onsubmit="return validateForm(this)" name="analysis">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="460">
		<TR><TH colspan="4" class="blue">Analysis</TH></TR>
	<tr>
		<th align="left" class="standard">Analysis Name</th>
		<td><%=analysis.getAnalysis_name()%>
			<input type="hidden" name="analysis_name"
			value="<%=analysis.getAnalysis_name()%>">
		</td>
	</tr>
	<tr>
		<th align="left" class="standard">Version</th>
		<td><%=analysis.getVersion()%>
			<input type="hidden" name="version"
			value="<%=analysis.getVersion()%>"></td>
	</tr>
	<tr>
		<th align="left" class="standard">Description</th>
		<td><textarea rows="5" style="width: 200" name="remark"><%=analysis.getRemark()%></textarea>
		<INPUT type="hidden" value="<%=analysis.getRemark()%>"
			name="original_remark"> <INPUT type="hidden"
			value="<%=analysis.getAnalysis_id()%>" name="analysis_id"></td>
	</tr>
</table>
<br>
<hr>
<p>The following fields are part of the analysis:</p>
<br>

<table cellspacing="1" cellpadding="1" border="1" rules="rows"
	width="700">
	<thead>
		<tr>
			<th class="blue">#</th>
			<th class="blue">Text Id</th>
			<th class="blue">Result Min.</th>
			<th class="blue">Result Max.</th>
			<th class="blue">Result Type</th>
			<th class="blue">Unit</th>
			<th class="blue">Use For<br />
			Spec</th>
			<th class="blue">Rem.</th>
		</tr>
	</thead>
	<tbody>
		<%
    		Vector units = unit.listOfUnits();
			Vector elements = analysis.getElements();
			Vector elements_id = analysis.getElements_id();
			int token_counter = 0;

			if(elements_id.size()>0)
			{					
				for (int i = 0; i<elements_id.size(); i++)
				{%>
		<tr>
			<td align="center" width="20"><%=String.valueOf(i+1)%></td>
			<%
					String field_id = (String) elements_id.get(i);
					String data = (String) elements.get(i);
		            StringTokenizer tokens = new StringTokenizer(data, "|");
		            token_counter = 0;
		            while (tokens.hasMoreTokens())
		            {
	   	            	String token = chemicalinventory.utility.Util.encodeTag(tokens.nextToken().trim());
		            
		            	if(token_counter == 0)
		            	{%>
			<td align="center"><input type="text" name="text_id_<%=field_id%>"
				size="30" value="<%=token%>"> <input type="hidden"
				name="text_id_<%=field_id%>_hidden" value="<%=token%>"></td>
			<%
		            	}
		            	if(token_counter == 1)
		            	{%>
			<td align="center"><input type="text" name="result_min_<%=field_id%>"
				size="12" value="<%=token%>"> <input type="hidden"
				name="result_min_<%=field_id%>_hidden" value="<%=token%>"></td>
			<%
		            	}
		            	if(token_counter == 2)
		            	{%>
			<td align="center"><input type="text" name="result_max_<%=field_id%>"
				size="12" value="<%=token%>"> <input type="hidden"
				name="result_max_<%=field_id%>_hidden" value="<%=token%>"></td>
			<%
		            	}
		            	if(token_counter == 3)
		            	{%>
			<td align="center"><select name="result_type_<%=field_id%>">
				<%
								if(token.equals("numeric"))
								{%>
				<option value="numeric" selected="selected">numeric</option>
				<option value="text">text</option>
				<%
								}
								else
								{%>
				<option value="numeric">numeric</option>
				<option value="text" selected="selected">text</option>
				<%
								}%>
			</select> <input type="hidden"
				name="result_type_<%=field_id%>_hidden" value="<%=token%>"></td>
			<%
		            	}
		            	if(token_counter == 4)
		            	{%>
			<td align="center"><select name="unit_<%=field_id%>">
				<%
								String current_unit = "";
								
								if(units.contains(token) && token != null && !token.equals(""))
								{
									for(int n=0; n<units.size(); n++)
									{
										String data2 = (String) units.get(n);
										if(data2.equalsIgnoreCase(token))
										{
											current_unit = data2;
											%>
				<option value="<%=data2%>" selected="selected"><%=data2%></option>
				<%
										}
										else
										{
											%>
				<option value="<%=data2%>"><%=data2%></option>
				<%	
										}
									}
								}
								else
								{
									for(int m=0; m<units.size(); m++)
									{
										String data2 = (String) units.get(m);
										if(m==0)
										{
											current_unit = data2;
											%>
				<option value="<%=data2%>" selected="selected"><%=data2%></option>
				<%
										}
										else
										{
											%>
				<option value="<%=data2%>"><%=data2%></option>
				<%	
										}
									}
								}%>
			</select> <INPUT type="hidden" name="unit_<%=field_id%>_hidden"
				value="<%=current_unit%>"></td>
			<%
		            	}
		            	if(token_counter == 5)
		            	{%>
			<td align="center"><%
								if(token.equalsIgnoreCase("F"))
								{%> <input type="checkbox" name="use_spec_<%=field_id%>"> <%
								}
								else
								{%> <input type="checkbox" name="use_spec_<%=field_id%>"
				checked="checked"> <%
								}%> <input type="hidden" name="use_spec_<%=field_id%>_hidden"
				value="<%=token%>"></td>
			<%
		            	}
		            			            	
		            	token_counter++;
		            }%>
			<td align="center"><input type="checkbox" name="remove_<%=field_id%>"></td>
		</tr>
		<%
				}
			}
			else
			{%>
		<tr>
			<td align="center" colspan="8"><i>NO FIELDS</i></td>
		</tr>
		<%
			}
				%>
		<tr>
		<tr>
			<td colspan="8"><!-- Expandable Content box start -->
			<div
				style="border: 1px none #000000; padding: 1px; background: #FFFFFF; ">
			<table border="0" cellspacing="1" cellpadding="1" width="100%"
				class="class=\\\\" style="background: #CDDDFF; color: #000000; ">
				<tr>
					<th class="blue" align="left">Add Fields</th>
					<th class="blue" align="right">[ <a title="show/hide"
						id="exp1098695543_link" href="javascript: void(0);"
						onclick="toggle(this, 'exp1098695543');" class="class=\\\\"
						style="text-decoration: none; color: #000000; ">&#45;</a> ]</th>
				</tr>
			</table>
			<div id="exp1098695543" style="padding: 3px;">
			<table cellspacing="1" cellpadding="1" border="0" rules="rows"
				width="100%">
				<thead>
					<tr>
						<th class="blue">#</th>
						<th class="blue">Text Id</th>
						<th class="blue">Result Min.</th>
						<th class="blue">Result Max.</th>
						<th class="blue">Result Type</th>
						<th class="blue">Unit</th>
						<th class="blue">Spec</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td align="center">1</td>
						<td align="center"><input type="text" name="new_text_id1"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min1"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max1"
							size="12"></td>
						<td align="center"><select name="new_result_type1">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit1">
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec1"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">2</td>
						<td align="center"><input type="text" name="new_text_id2"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min2"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max2"
							size="12"></td>
						<td align="center"><select name="new_result_type2">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit2">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec2"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">3</td>
						<td align="center"><input type="text" name="new_text_id3"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min3"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max3"
							size="12"></td>
						<td align="center"><select name="new_result_type3">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit3">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec3"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">4</td>
						<td align="center"><input type="text" name="new_text_id4"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min4"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max4"
							size="12"></td>
						<td align="center"><select name="new_result_type4">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit4">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec4"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">5</td>
						<td align="center"><input type="text" name="new_text_id5"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min5"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max5"
							size="12"></td>
						<td align="center"><select name="new_result_type5">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit5">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec5"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">6</td>
						<td align="center"><input type="text" name="new_text_id6"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min6"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max6"
							size="12"></td>
						<td align="center"><select name="new_result_type6">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit6">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec6"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">7</td>
						<td align="center"><input type="text" name="new_text_id7"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min7"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max7"
							size="12"></td>
						<td align="center"><select name="new_result_type7">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit7">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec7"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">8</td>
						<td align="center"><input type="text" name="new_text_id8"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min8"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max8"
							size="12"></td>
						<td align="center"><select name="new_result_type8">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit8">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec8"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">9</td>
						<td align="center"><input type="text" name="new_text_id9"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min9"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max9"
							size="12"></td>
						<td align="center"><select name="new_result_type9">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit9">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select></td>
						<td align="center"><input type="checkbox" name="new_use_spec9"
							checked="checked" /></td>
					</tr>
					<tr>
						<td align="center">10</td>
						<td align="center"><input type="text" name="new_text_id10"
							size="30"></td>
						<td align="center"><input type="text" name="new_result_min10"
							size="12"></td>
						<td align="center"><input type="text" name="new_result_max10"
							size="12"></td>
						<td align="center"><select name="new_result_type10">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select></td>
						<td align="center"><select name="new_unit10">
							<!--Create dynamiccaly-->
							<%
								for(int n=0; n<units.size(); n++)
								{
									String data = (String) units.get(n);
									%>
							<option value="<%=data%>"><%=data%></option>
							<%
								}%>
						</select> <input type="hidden" name="analysis_id" value="23"></td>
						<td align="center"><input type="checkbox" name="new_use_spec10"
							checked="checked" /></td>
					</tr>
				</tbody>
			</table>
			</div>
			</div>
			<noscript>IF YOU SEE THIS TEXT IT IS STRONGLY ADVISED THAT YOU GET
			YOURSELF A NEW BROWSER. IMMIDIATELY STOP WORKING IN CI AND CONTACT
			YOUR ADMINISTRATOR</noscript>
			<script language="javascript">toggle(getObject('exp1098695543_link'), 'exp1098695543');</script>
			<!-- Expandable Content box end  --></td>
		</tr>
		<tr>
			<td colspan="8">
			<table border="0" width="100%">
				<tr>
					<th class="blue" align="center">Reason For Change</th>
				</tr>
				<tr>
					<td><TEXTAREA name="reason_for_change" cols="84" rows="4"></TEXTAREA>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</tbody>
</table>
<br>

<input class="submit" type="submit" name="Modify" value="Modify"
	onclick="this.form.action='<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=modify_analysis&code2=yes'">
<input class="submit" type="reset" name="reset" value="Reset"></form>
</center>
<%
}//end code 1 = null (entering of data for step 1)

if (request.getParameter("code2") != null)
{
	//first check the reason for change field
	String reason = request.getParameter("reason_for_change").trim();
	
	if(reason == null || reason.equalsIgnoreCase("") || reason.equalsIgnoreCase("null"))//no comment return and enter the values again!
	{
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code1=yes&analysis_id="+analysis.getAnalysis_id()+"&errorcode1=yes");
	}
	else//reason for change has been stated, now register
	{
		analysis.setUser(user);
		Map value_list = new HashMap(); //create a list for the result data
		value_list = request.getParameterMap(); //fill the list with the result data
		analysis.setValue_list(value_list);
		int code = analysis.performModify();//see the bean for desciption of values...
		
		if(code == 1)
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code1=yes&analysis_id="+analysis.getAnalysis_id()+"&errorcode2=yes&int=1");
		}
		else if (code == 2)
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code3=yes&analysis_id="+analysis.getAnalysis_id());
		}
		else if (code == 3)
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code1=yes&analysis_id="+analysis.getAnalysis_id()+"&errorcode2=yes&int=3");
		}
		else if (code == 4)
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code3=yes&analysis_id="+analysis.getAnalysis_id());
		}
		else if (code == 5)
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code1=yes&analysis_id="+analysis.getAnalysis_id()+"&errorcode2=yes&int=5");
		}
		else
		{
			response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=modify_analysis&code1=yes&analysis_id="+analysis.getAnalysis_id()+"&errorcode2=yes&int=900");
		}
	}
}//end code 2 (redirection)

//show the data entered for the analysis
if (request.getParameter("code3") != null)
{
	analysis.getAnalysisInfo();	//get information about the analysis created
%>
<h2>Modify analysis - result</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"> <br>
<P>This is the data registered for the analysis.</P>
<center>
<table class="box" cellspacing="1" cellpadding="1" border="0"
	width="75%" frame="box">
	<tr>
		<td>
		<table border="0" width="100%">
			<tr>
				<th colspan="5" class="blue">Analysis Data</th>
			</tr>
			<tr>
				<th align="left" width="160">Analysis Name:</th>
				<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(analysis.getAnalysis_name())%></td>
			</tr>
			<tr>
				<th align="left" width="160">Analysis Version:</th>
				<td colspan="4"><%=analysis.getVersion()%></td>
			</tr>
			<tr>
				<th align="left" width="160">Description:</th>
				<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(analysis.getRemark())%></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" width="100%">
			<tr>
				<th colspan="7" class="blue">Analysis Fields</th>
			</tr>
			<tr>
				<th class="blue">#</th>
				<th class="blue">Text Id</th>
				<th class="blue">Result Min</th>
				<th class="blue">Resullt Max</th>
				<th class="blue">Resullt Type</th>
				<th class="blue">Unit</th>
				<th class="blue">Use For<br />
				Spec</th>
			</tr>

			<%//show receipt for the creation
						Vector elements = analysis.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{%>
			<tr>
				<td align="center" width="20"><%=String.valueOf(i+1)%></td>
				<%
							String data = (String) elements.get(i);
				            StringTokenizer tokens = new StringTokenizer(data, "|");
				            
				            while (tokens.hasMoreTokens())
				            {
				            	String token = chemicalinventory.utility.Util.encodeTag(tokens.nextToken().trim());%>
				<td><%=token%></td>
				<%
				            }%>
			</tr>
			<%
						}%>
		</table>
		</td>
	</tr>
</table>
<br />
</center>
<%
}//end code 3
%> </span>
<MAP NAME="nav_bar">
	<AREA SHAPE="rect" COORDS="3,2,90,23" href="<%=Attributes.ADMINISTRATOR_BASE%>?action=Adm">
	<AREA SHAPE="rect" COORDS="92,2,179,23"
		href="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=Supplier">
	<AREA SHAPE="rect" COORDS="181,2,268,23"
		href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
	<AREA SHAPE="rect" COORDS="270,2,362,23"
		href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">
	<AREA SHAPE="rect" COORDS="364,2,451,23"
		href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">
	<AREA SHAPE="rect" COORDS="453,2,543,23"
		href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=new_group">
	<AREA SHAPE="rect" COORDS="544,3,634,23"
		href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis">
	<AREA SHAPE="rect" COORDS="637,3,727,23"
		href="<%=Attributes.UNIT_ADMINISTRATOR_BASE %>?action=new_unit">
</map>
</body>
</html>