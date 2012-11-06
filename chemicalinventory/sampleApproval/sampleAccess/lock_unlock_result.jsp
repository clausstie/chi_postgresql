<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean" scope="page"/>
<jsp:setProperty name="sample" property="*"/>
<jsp:useBean id="result" class="chemicalinventory.sample.SampleResultBean" scope="page"/>
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
function validateForm(form) 
{
 if(trim(form.sample_id.value)== "")
 {
    alert("Please fill in a valid sample id");
    form.sample_id.focus();
    return false;
 }

 if(trim(form.sample_id.value) != "")
 {
 	if(isNumber(form.sample_id.value)==false)
    {
        alert("Please fill in a valid sample id");
        form.sample_id.focus();
        return false;
    }
 }
 return true;
}
</script>
<title>Lock or unlock on result level</title>
</head>
<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null)
{%>
<body onload="document.sample.sample_id.focus()">
<%
}
else
{%>
<body>
<%
}
//initial values to be used on this page
String user = request.getRemoteUser().toUpperCase();
String sample_id = "";
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_result.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
	| <a class="adm" href="<%=Attributes.RESULT_BASE%>?action=result_entry">Result Entry</a> |
	  <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result">Lock/Un-Lock Result</a> |
</span>
<span class="textboxadm">
<%

if(request.getParameter("code1") == null && request.getParameter("code2") == null)
{%>
<h2>Select sample to lock/unlock</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<center>
<form method="post" action="<%= Attributes.SAMPLEAPPROVER_BASE %>?action=lock_result&code1=yes" name="sample" onsubmit="return validateForm(this)">
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">Sample</TH></TR>
		<tr>
			<th align="left" class="standard">Sample id:</th>
			<td><input type="text" name="sample_id" class="w200"></td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result&code1=yes'">&nbsp;
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}//end initial entry of sample id

//success
if(request.getParameter("code3") != null)//receipt for succes on locking a sample
{
	sample_id = (String) request.getParameter("sample_id");
	%><h3>The selected results for sample (id: <%=sample_id%>) has been locked!</h3><%
}

//locked sample display errormessage
if(request.getParameter("errorcode2") != null)//receipt for succes on locking a sample
{
	sample_id = (String) request.getParameter("sample_id");
	%><h3>The selected sample (id: <%=sample_id%>) is locked, and results cannot be modified.</h3><%
}

if(request.getParameter("code1") != null)
{	
	sample_id = sample.getSample_id();
	boolean islockedSample = sample.isSampleLocked(sample_id);
	
	if(!islockedSample)//the sample is not locked... display possibility to lock results
	{
		boolean showResult = sample.getSampleInfo2();
		
		if(showResult)
		{	
		//display the entered results...
		%>
		<h2>Display sample information - Lock/Unlock result</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br>	
		<center>
		<%if(request.getParameter("errorcode1")!=null)
		{
			%><h3>An error orcurred, results could not be locked, please try again.</h3><%
		}%>
		<form method="post" action="<%= Attributes.SAMPLEAPPROVER_BASE %>?action=lock_sample&code2=yes" name="sample">
			<table class="box" cellspacing="1" cellpadding="1" border="0" width="650px">
				<tr>
					<td>
						<table border="0" width="100%">
							<tr>
								<th colspan="5" class="blue">General Sample Data</th>
							</tr>
							<tr>
								<th align="left" width="160">Sample id:</th>
								<td colspan="4"><%=sample.getSample_id()%>
									<input type="hidden" name="sample_id" value="<%=sample.getSample_id()%>">
								</td>
							</tr>
							<tr>
								<th align="left" width="160">Compound:</th>
								<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(sample.getChemical_name())%></td>
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
								<th align="left" width="160">Locked:</th>
								<td colspan="4"><%=sample.getLocked()%></td>
							</tr>
							<%if(sample.getLocked().equals("T"))
							  {%>
								<tr>
									<th align="left" width="160">Locked by:</th>
									<td colspan="4"><%=sample.getLocked_by()%></td>
								</tr>				  
								<tr>
									<th align="left" width="160">Locked date:</th>
									<td colspan="4"><%=sample.getLocked_date()%></td>
								</tr>					  
							<%}%>
							<tr>
								<th align="left" width="160">Batch:</th>
								<td colspan="4"><%=sample.getBatch()%></td>
							</tr>
							<tr>
								<th align="left" width="160">Analysis map:</th>
								<td colspan="4"><%=sample.getMap_name()%></td>
							</tr>
							<tr>
								<th align="left" width="180">Description:</th>
								<td colspan="4"><%=chemicalinventory.utility.Util.encodeTag(sample.getRemark())%></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
				<%
					Vector elements1 = sample.getElements();
					String analysis_id = "";
					
					for (int m = 0; m<elements1.size(); m++)
					{
						analysis_id = (String) elements1.get(m);
						
						//get information about the single analysis
						analysis.getAnalysisDataAll_lock(analysis_id, sample_id);
						%>
						<table border="0" width="100%">
							<tr>
								<th colspan="5" class="blue"><%=analysis.getAnalysis_name()%></th>
							</tr>
							<tr>
								<th width="250px" class="blue">Text id</th>
								<th width="250px" class="blue">Result</th>
								<th width="100px" class="blue">Unit</th>
								<th width="100px" class="blue">Status</th>
								<th width="50px" class="blue">Locked</th>
							</tr><%
							Vector elements = analysis.getElements();
							if(elements.size()>0)
							{
								for (int n = 0; n<elements.size(); n++)
								{
								  String color = "normal";
						          if(n % 2 != 0)
						          {
						            color = "smoke";
						          }
						          %>
						          <tr class="<%=color%>"><%
									  out.print((String) elements.get(n));%>
								  </tr><%
								}//end for list of fields in analysis
							}//end if
							else//no fields for this analysis
							{%>
							 <tr>
								<td colspan="4" align="center"><i>NO FIELDS</i></td>
							 </tr><%
							}//end for list of fields in analysis%>
						</table><%
					}//end for list of analysis%>
					 </td>
				</tr>
			</table>
			<br>
			<input class="submit_width125" type="submit" name="Submit" value="Lock Selected Results" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result&code2=yes&sample_id=<%=sample_id%>'">
			<input class="submit" type="submit" name="Cancel" value="Cancel" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result'">
		</form><br>
		</center>
		  <%}
			else
			{
			int errorCode = sample.getErrorCode();
				%>
				<h2>Display sample information - ERROR</h2>
				<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
				<br>
					<%
					if(errorCode == 1)// no sample for the id
					{%>
					<center>
						<h3>No sample with id (<%=sample.getSample_id()%>) registered.</h3>
					</center><%
					}
					else if(errorCode == 2)
					{%>
					<center>
						<h3>An error orcurred please try again...</h3>
					</center><%
					}
			}
	}//the sample is not locked
	else//the sample is locked cancel...
	{
		response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=lock_result&errorcode2=yes&sample_id="+sample_id);
	}
}

//here lock the entire sample
if(request.getParameter("code2")!=null)
{

	Map value_list = new HashMap(); //create a list for the results to lock
	value_list = request.getParameterMap(); //fill the list with the results to lock
	result.setValue_list(value_list);
	result.setUser(user);//set the user
	sample_id = sample.getSample_id();
	result.setSample_id(sample_id);
	
	boolean checkLock = result.lockResults(sample_id, user);
	
	if(checkLock)
	{
		response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=lock_result&code3=yes&sample_id="+sample_id);
	}
	else//error sample could not be locked..
	{
		response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=lock_result&code1=yes&errorcode1=yes&sample_id="+sample_id);
	}
}
%>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>