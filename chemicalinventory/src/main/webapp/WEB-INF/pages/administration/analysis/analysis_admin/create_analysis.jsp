<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:useBean id="unit" class="chemicalinventory.unit.UnitBean" scope="page"/>
<jsp:setProperty name="analysis" property="*"/>

<%
/*creation has been cancelled delete the results created so far!
The deletion is placed at the top of the file, to make sure that
the name is removed before the list of used names is created*/
if(request.getParameter("code4") != null)
{
	String the_id = analysis.getAnalysis_id();
	analysis.deleteAnalysis(the_id);
}
%>

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
<script language="JavaScript" src="../../script/inventoryScript.js"></script>
<script LANGUAGE="JavaScript">  
var name=new Array();
<%  
    Vector taken = new Vector();
    taken.clear();
	taken = analysis.getAnalysisNamesList();

    for(int i=0; i<taken.size(); i++)
    {
       String name = (String) taken.elementAt(i);
      %>name[<%= i %>] = "<%= name %>";<%
    }  
%>

function name_validator()
{
	var ana_name = document.analysis.analysis_name.value;
    var checkno = 0;

	for(i = 0; i <= name.length; i++)
	{
		if(name[i] == ana_name)
		{
            checkno = 1;
			return checkno;
		}
	}
        return checkno;
}


function validateForm(form) 
{
 if(trim(form.analysis_name.value)== "")
 {
    alert("Please fill in a name for the analysis");
    form.analysis_name.focus();
    return false;
 }
 if (name_validator() == 1)
 {
   alert("The entered analysis name cannot be used as it has allready been taken!");
   form.analysis_name.focus();
   return false; 
 }
 return true;
}

</script>
<title>Creation of an analysis</title>
</head>
<%
if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{
%>
<body onload="document.analysis.analysis_name.focus();">
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
   | <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis">Create Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=display_analysis">Display Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_analysis">Modify Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=remove_analysis">Remove Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=reactivate_analysis">Reactivate Analysis</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_map">Create Analysis Map</a> |
   <a class="adm" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=modify_map">Modify Analysis Map</a> |
</span>
<span class="textboxadm">
<%
if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{
%>
<h2>Create a new analysis - Step 1/3</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<!-- First show the initial screen to create a new analysis -->
<CENTER>
<form method="post" action="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis&code1a=yes" onsubmit="return validateForm(this)" name="analysis">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="4" class="blue">Analysis</TH></TR>
		<tr>
			<th align="left" class="standard">Analysis Name</th>
			<td>
				<input class="w200" type="text" name="analysis_name">
			</td>
		</tr>
		<tr>
			<th align="left" class="standard">Version</th>
			<td>
				<input class="w200" type="text" name="version" value="1" readonly="readonly">
			</td>
		</tr>
		<tr>
			<th align="left" class="standard">Description</th>		
			<td>
				<textarea rows="5" style="width: 200" name="remark" type="_moz">--</textarea>
			</td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Continue" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis&code1a=yes'">
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}//end code 1 = null (entering of data for step 1)

if (request.getParameter("code1a") != null)
{
	analysis.setUser(user);
	boolean check =	analysis.createAnalysis_step1();
	int errorCode = analysis.getErrorCode();
	String id1 = analysis.getAnalysis_id();
	//get the id, name, version etc from step one and display... 
	//display them as modifiable text fields, but keep the id
	//as an hidden field.
	System.out.println("error code : "+errorCode);
	
	if(check && errorCode == 0)
	{
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=new_analysis&code1=yes&analysis_id="+id1);
	}
	else
	{
		String errorMessage = "The registration failed, please try agiain";
		if(errorCode == 1)
			errorMessage = "The registration failed, the analysis name is allready in use, please use a different name";
		if(errorCode == 2)
			errorMessage = "The registration failed, please try agiain";
		if(errorCode == 3)
			errorMessage = "The registration failed, please enter a valid name";
	%>
		<br>
			<H3><%=errorMessage%></H3>
	<%
	}
}//end code 1a (redirection)


if (request.getParameter("code1") != null)
{
 //get all the units as a list
 Vector units = unit.listOfUnits();
%>
	<h2>Create a new analysis - Step 2/3</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<center>
	<form method="post" action="<%= Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis&code2=yes" onsubmit="return validateForm_fields(this)">
		<table cellspacing="1" cellpadding="1" border="1" rules="rows" width="650">
			<thead>
				<tr>
					<th class="blue">#</th>
					<th class="blue">Text Id</th>
					<th class="blue">Result Min.</th>
					<th class="blue">Result Max.</th>
					<th class="blue">Result Type</th>
					<th class="blue">Unit</th>
					<th class="blue">Use For<br/>Spec.</th>
				</tr>		
			</thead>
			<tbody>
				<tr>
					<td align="center">1<input type="hidden" name="id1" value="1"></td>
					<td align="center"><input type="text" name="text_id1" size="30"></td>
					<td align="center"><input type="text" name="result_min1" size="12"></td>
					<td align="center"><input type="text" name="result_max1" size="12"></td>
					<td align="center">
						<select name="result_type1">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>
					<td align="center">
						<select name="unit1">
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec1" checked="checked"/></td>
				</tr>
				<tr>
					<td align="center">2<input type="hidden" name="id2" value="2"></td>
					<td align="center"><input type="text" name="text_id2" size="30"></td>
					<td align="center"><input type="text" name="result_min2" size="12"></td>
					<td align="center"><input type="text" name="result_max2" size="12"></td>
					<td align="center">
						<select name="result_type2">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>
					<td align="center">
						<select name="unit2"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
    				   </select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec2" checked="checked"/></td>					
				</tr>
				<tr>
					<td align="center">3<input type="hidden" name="id3" value="3"></td>
					<td align="center"><input type="text" name="text_id3" size="30"></td>
					<td align="center"><input type="text" name="result_min3" size="12"></td>
					<td align="center"><input type="text" name="result_max3" size="12"></td>
					<td align="center">
						<select name="result_type3">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>
					<td align="center">
						<select name="unit3"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec3" checked="checked"/></td>					
				</tr>
				<tr>
					<td align="center">4<input type="hidden" name="id4" value="4"></td>
					<td align="center"><input type="text" name="text_id4" size="30"></td>
					<td align="center"><input type="text" name="result_min4" size="12"></td>
					<td align="center"><input type="text" name="result_max4" size="12"></td>
					<td align="center">
						<select name="result_type4">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit4"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec4" checked="checked"/></td>
				</tr>
				<tr>
					<td align="center">5<input type="hidden" name="id5" value="5"></td>
					<td align="center"><input type="text" name="text_id5" size="30"></td>
					<td align="center"><input type="text" name="result_min5" size="12"></td>
					<td align="center"><input type="text" name="result_max5" size="12"></td>
					<td align="center">
						<select name="result_type5">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit5"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec5" checked="checked"/></td>
				</tr>
				<tr>
					<td align="center">6<input type="hidden" name="id6" value="6"></td>
					<td align="center"><input type="text" name="text_id6" size="30"></td>
					<td align="center"><input type="text" name="result_min6" size="12"></td>
					<td align="center"><input type="text" name="result_max6" size="12"></td>
					<td align="center">
						<select name="result_type6">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit6"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec6" checked="checked"/></td>					
				</tr>
				<tr>
					<td align="center">7<input type="hidden" name="id7" value="7"></td>
					<td align="center"><input type="text" name="text_id7" size="30"></td>
					<td align="center"><input type="text" name="result_min7" size="12"></td>
					<td align="center"><input type="text" name="result_max7" size="12"></td>
					<td align="center">
						<select name="result_type7">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit7"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec7" checked="checked"/></td>					
				</tr>
				<tr>
					<td align="center">8<input type="hidden" name="id8" value="8"></td>
					<td align="center"><input type="text" name="text_id8" size="30"></td>
					<td align="center"><input type="text" name="result_min8" size="12"></td>
					<td align="center"><input type="text" name="result_max8" size="12"></td>
					<td align="center">
						<select name="result_type8">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit8"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec8" checked="checked"/></td>
				</tr>
				<tr>
					<td align="center">9<input type="hidden" name="id9" value="9"></td>
					<td align="center"><input type="text" name="text_id9" size="30"></td>
					<td align="center"><input type="text" name="result_min9" size="12"></td>
					<td align="center"><input type="text" name="result_max9" size="12"></td>
					<td align="center">
						<select name="result_type9">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit9"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
					</td>
					<td align="center"><input type="checkbox" name="use_spec9" checked="checked"/></td>		
				</tr>
				<tr>
					<td align="center">10<input type="hidden" name="id10" value="10"></td>
					<td align="center"><input type="text" name="text_id10" size="30"></td>
					<td align="center"><input type="text" name="result_min10" size="12"></td>
					<td align="center"><input type="text" name="result_max10" size="12"></td>
					<td align="center">
						<select name="result_type10">
							<option value="numeric">numeric</option>
							<option value="text">text</option>
						</select>			
					</td>					
					<td align="center">
						<select name="unit10"><!--Create dynamiccaly-->
						<%
						for(int n=0; n<units.size(); n++)
						{
							String data = (String) units.get(n);
							%><option value="<%=data%>"><%=data%></option><%
						}%>
						</select>			
						<input type="hidden" name="analysis_id" value="<%=analysis.getAnalysis_id()%>">
					</td>
					<td align="center"><input type="checkbox" name="use_spec10" checked="checked"/></td>					
				</tr>			
			</tbody>
		</table><br>
			<input class="submit" type="submit" name="Submit" value="Continue" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis&code2=yes'">
			<input class="submit" type="submit" name="Cancel" value="Cancel" onclick="this.form.action='<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis&code4=yes'">
	    	<input class="submit" type="reset" name="reset" value="Reset">
	</FORM>
	</center>
	<%
}//end code 1 (entering data for step 2)

if(request.getParameter("code2") != null)
{%>
	<h2>Create a new analysis - ERROR</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
<%
	analysis.setUser(user);
	boolean check2 = analysis.createAnalysis_step2();
	String id = analysis.getAnalysis_id();
	if(check2)
	{
		response.sendRedirect(Attributes.ANALYSIS_ADMINISTRATOR_BASE+"?action=new_analysis&code3=yes&analysis_id="+id);
	}
	else
	{
		int code = analysis.getErrorCode();
		analysis.deleteAnalysis(id);//delete the initial analysis data
		%>
		<br><%
		if(code == 2)
		{
			%><H3>Registration failed try again <a href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">click here</a></H3><%
		}
		else if(code == 3)
		{
			%><H3>Registration failed try again <a href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">click here.</a> At least one field must be registered!</H3><%
		}
		else
		{
			%><H3>Registration failed try again <a href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE%>?action=new_analysis">click here</a></H3><%
		}
	}
}//end code 2

//show the data entered for the analysis
if (request.getParameter("code3") != null)
{
	analysis.getAnalysisInfo();	//get information about the analysis created
	analysis.insertInitialHistory(analysis.getAnalysis_id(), "INITIAL DATA ENTRY", analysis.getVersion(), user);//insert data in the history table
%>
	<h2>Create a new analysis - Step 3/3</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
		<P>This is the data registered for your new analysis.</P>
		<center>
		<table class="box" cellspacing="1" cellpadding="1" border="0" width="85%" frame="box">
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
							<th class="blue">Use For<br/> Spec</th>
						</tr>
						
						<%//show receipt for the creation
						Vector elements = analysis.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{%>
							<tr>
								<td align="center" width="20"><%=String.valueOf(i+1)%></td><%
							String data = (String) elements.get(i);
				            StringTokenizer tokens = new StringTokenizer(data, "|");
				            
				            while (tokens.hasMoreTokens())
				            {
				            	String token = chemicalinventory.utility.Util.encodeTag(tokens.nextToken().trim());%>
				            	<td><%=token%></td><%
				            }%>
				            </tr><%
						}%>				
						</table>
					</td>
			</tr>	
		</table><br/>
		</center>
<%
}
//end code 3%>
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