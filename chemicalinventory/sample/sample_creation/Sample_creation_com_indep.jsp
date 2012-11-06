<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="map" class="chemicalinventory.analysis.AnalysisMapBean" scope="page"/>
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean" scope="page"/>
<jsp:setProperty name="sample" property="*"/>
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<script LANGUAGE="JavaScript">
function openWindow(url, number)
{
	if(number == 1)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=850, height=600")
	}
	else if(number == 2)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=600, height=600")
	}
}

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
	//get the analysis selected individually
	 getValue(form);

 if ((trim(form.map_id.value) == "") && (trim(form.id_field.value) == "")) 
 {
  alert("Please select an analysis map or individual analysis to complete the sample creation");
  form.map_id.focus();
  return false;
 }
return true;
}

var ie4 = false; if(document.all) { ie4 = true; }
function getObject(id) { if (ie4) { return document.all[id]; } else { return document.getElementById(id); } }
function toggle(link, divId) { var lText = link.innerHTML; var d = getObject(divId);
 if (lText == '+') { link.innerHTML = '&#45;'; d.style.display = 'block'; }
 else { link.innerHTML = '+'; d.style.display = 'none'; } }
</script>
<title>Login of a sample not depending on a compound</title>
</head>
<body>
<%
//initial values to be used on this page
String com_name = request.getParameter("chemical_name");
String com_id = request.getParameter("compound_id");
String user = request.getRemoteUser().toUpperCase();
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_login.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.SAMPLE_BASE %>?action=create_sample">Compound Dependent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLE_BASE%>?action=create_sample_ind">Independent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_sample">Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=unlock_sample">Un-Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result">Lock/Un-Lock Result</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample">Add/Remove Analysis</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=dis_sample">List samples</a> |
</span>
<span class="textboxadm">
<%
if(com_name != null && com_id != null)
{
	com_name = URLDecoder.decode(com_name, "UTF-8");
}
else
	com_name = "Search For Compound!";


if(request.getParameter("code1") == null && request.getParameter("code2") == null)
{%>
<h2>Create a new sample</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<!-- First show the initial screen for creating a new sample -->
<CENTER>
<form method="post" action="<%= Attributes.SAMPLE_BASE %>?action=create_sample_ind&code1=yes" onSubmit="return validateForm(this)" name="sample">
   <table class="box" cellpadding="1" cellspacing="2" border="0" width="660">
 		<TR><TH colspan="4" class="blue">Sample Creation</TH></TR>
 		<tr>
			<th align="left">Analysis Map</th>
			<td>
			
			<%
			//get the information about maps for this compound
			map.getMapInfo2();
			Vector names = map.getElements();
			Vector ids = map.getId_elements();		
			%>
				<select name="map_id">
					<option value="" selected="selected">[--- SELECT ---]</option>
				<%
				for (int i=0; i<ids.size(); i++)
				{%>
				  <option value="<%=ids.get(i)%>"><%=names.get(i)%></option><%
				}%>
				</select>
				<B>..Or select the analysis below</B>
			</td>
		</tr>
		<tr>
		<td colspan="2">
		<!-- insert a list of available analysisø -->
				<!-- Expandable Content box start -->
				<div style="border: 1px none #000000; padding: 1px; background: #FFFFFF; ">
					<table border="0" cellspacing="1" cellpadding="1" width="100%" class="class=\\\\" style="background: #E8E6EC; color: #000000; ">
						<tr>
							<th align="left">Add Analysis</th>
							<th align="right">[
								<a title="show/hide" id="exp1098695543_link" href="javascript: void(0);" onclick="toggle(this, 'exp1098695543');"  class="class=\\\\" style="text-decoration: none; color: #000000; ">&#45;</a>
								]</th>
						</tr>
					</table>
				<div id="exp1098695543" style="padding: 3px;">
					<table class="special" cellspacing="1" cellpadding="1" border="0" rules="rows" width="100%">
						<thead>
							<tr>
								<th width="31">&#63;</th>
								<th width="198">Analysis name</th>
								<th width="464">Description</th>
								<th width="53">Version</th>
								<th width="15">&nbsp;</th>
							</tr>		
						</thead>
					<tbody>
						<tr>
							<td colspan="6">
								<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list" width="650" height="300" marginwidth="0" name="list_frame">
								</iframe>
								
								<input type="hidden" name="id_field" value="">
							</td>
						</tr>	
					</tbody>
				</table>
			</div>
	</div>
	<noscript>IF YOU SEE THIS TEXT IT IS STRONGLY ADVISED THAT YOU GET YOURSELF A NEW BROWSER.
				IMMIDIATELY STOP WORKING IN CI AND CONTACT YOUR ADMINISTRATOR</noscript>
	<script language="javascript">toggle(getObject('exp1098695543_link'), 'exp1098695543');</script>
							<!-- Expandable Content box end  -->
		
			</td>
		</tr>
		<tr>
			<th align="left">Description</th>		
			<td colspan="2"><textarea rows="5" cols="66" name="remark"></textarea>
			</td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.SAMPLE_BASE%>?action=create_sample_ind&code1=yes'">
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</CENTER>
<%
}//end initial screen

if(request.getParameter("error2") != null)
{%>
<br>
	<center>
		<H3>The registration failed, please try agiain</H3>
	<center><%
}

if(request.getParameter("error4") != null)
{%>
<br>
	<center>
		<H3>The registration failed, you have to select either a analysis map or individual analysis to complete creation</H3>
	<center><%
}

if(request.getParameter("code1") != null)
{
	sample.setUser(user);
	boolean check =	sample.registerSample_NoCompound();
	String id1 = sample.getSample_id();

	if(check)
	{				
		//send the user on...
		response.sendRedirect(Attributes.SAMPLE_BASE+"?action=create_sample_ind&code2=yes&sample_id="+id1);
	}
	else
	{
		int code = sample.getErrorCode();
		
		if(code == 4)
				response.sendRedirect(Attributes.SAMPLE_BASE+"?action=create_sample_ind&error4=yes");
		else//compound selected but another error happend
				response.sendRedirect(Attributes.SAMPLE_BASE+"?action=create_sample_ind&error2=yes");
	}
}//end code 1

//show data for the newly generated sample
if(request.getParameter("code2") != null)
{
	boolean check2 = sample.getSampleInfo();
	
	if(check2)
	{
%>
	<h2>New sample created</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<P>This is the data registered for the new sample.</P>
	<center>
		<table class="box" cellspacing="1" cellpadding="1" border="0" width="75%" frame="box">
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">General Sample Data</th>
						</tr>
						<tr>
							<th align="left" width="160">Sample id:</th>
							<td colspan="4"><%=sample.getSample_id()%></td>
						</tr>
						<tr>
							<th align="left" width="160">Created date:</th>
							<td colspan="4"><%=sample.getCreated_date()%></td>
						</tr>
						<tr>
							<th align="left" width="160">Created by:</th>
							<td colspan="4"><%=sample.getCreated_by()%></td>
						</tr>
						<tr>
							<th align="left" width="160">Analysis map:</th>
							<td colspan="4"><%=sample.getMap_name()%></td>
						</tr>
						<tr>
							<th align="left" width="180">Description:</th>
							<td colspan="4"><%=Util.encodeTag(sample.getRemark())%></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<th colspan="5" class="blue">Analysis mapped for sample</th>
						</tr>
						<tr>
							<th class="blue">#</th>
							<th class="blue">Analysis Name</th>
						</tr>
						
						<%//show receipt for the creation
						Vector elements = sample.getElements();
						
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
		</table><br>
		<form method="post" action="<%= Attributes.RESULT_BASE %>?action=result_entry&code1=yes&sample_id=<%=sample.getSample_id()%>">
			<input class="submit_width125" type="submit" name="Submit" value="Enter Results">
		</form>
	</center><%
	}
	else
	{%>
		<h2>Display new sample - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br>
		<center>
			<H3>The sample was registered correctly, but receipt could not be generated</H3>
		<center><%
	}
	
}//end code 2
%>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>