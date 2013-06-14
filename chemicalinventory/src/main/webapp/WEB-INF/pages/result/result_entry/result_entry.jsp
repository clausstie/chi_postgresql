<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="result" class="chemicalinventory.sample.SampleResultBean" scope="page"/>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
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
<script language="JavaScript" src="../script/overlib.js"></script>
<script LANGUAGE="JavaScript">
function openWindow(url, number)
{
	if(number == 1)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=850, height=600")
	}
	else if(number == 2)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=600, height=600")
	}
}

function validateForm(form) 
{<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code4") == null)
{%>
 if(trim(form.sample_id.value)== "")
 {
    alert("Please fill a valid sample id");
    form.sample_id.focus();
    return false;
 }
 <%
 }%>

 if(trim(form.sample_id.value) != "")
 {
 	if(isNumber(form.sample_id.value)==false)
    {
        alert("Please fill a valid sample id");
        form.sample_id.focus();
        return false;
    }
 }
  
<%
//demand for reason of change removed due to user request
//if(request.getParameter("code1") != null)
//{%>
// if(trimAll(form.reason_for_change.value) == "")
// {
// 	alert("This is a started Sample! To enter/modify data for this sample, you have to enter a 'Reason For Change' in the text field.");
// 	return false;
// }
 <%
// }%>
 
 return true;
}
</script>

<title>Result Entry for a sample</title>
</head><%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code4") == null)
{%>
<body onload="document.sample.sample_id.focus()">
<%
}
else
{%>
<body>
<%
}%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%
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

if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code4") == null)
{%>
<h2>Result entry</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<center>
<form method="post" action="<%= Attributes.RESULT_BASE %>?action=result_entry&code1=yes" name="sample" onsubmit="return validateForm(this)">
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">Sample</TH></TR>
		<tr>
			<th align="left" class="standard">Sample id:</th>
			<td><input type="text" name="sample_id" class="w200"></td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.RESULT_BASE%>?action=result_entry&code1=yes'">&nbsp;
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}//end initial entry of sample id


if(request.getParameter("code1") != null)
{
	/*if the sample is locked, do not show result
	entry but show a link to have data displayed as
	readoly*/
	String samp_id = sample.getSample_id();
	boolean isSampleLocked = sample.isSampleLocked(samp_id);
	if(!isSampleLocked)//the sample is NOT locked
	{
		boolean check = false;
		check = sample.getSampleInfo2();
		boolean isStarted = sample.isSampleStarted();
		String checkForm = "";
		if(isStarted)
			checkForm = "onsubmit=\"return validateForm(this)\"";
		
		if(check)
		{
			%>
			<h2>Result entry</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<br>
			<!-- First show the initial screen for entering result for a sample -->
			<CENTER>
			
			<%//the user did not enter a reason for the change of this sample display warning
			if(request.getParameter("errorcode1") != null)
			{
				%><br><h3>This sample has been started, resultentry can only be perfomed
					  if a reason for the change is stated in the text area at the bottom
					  of the page!</h3><br><%
			}
			
			if(request.getParameter("codeNoChange") != null)
			{
				%><br><h3>No additional resultentry performed, the sample has not been changed.</h3><br><%
			}%>
			
			<form method="post" action="<%= Attributes.RESULT_BASE %>?action=create_sample&code1=yes" <%=checkForm%> name="result">
				<table class="box" cellspacing="1" cellpadding="1" border="0" width="600px">
					<tr>
						<td>
							<table border="0" width="100%">
								<tr>
									<th colspan="5" class="blue">General Sample Data</th>
								</tr>
								<tr>
									<th align="left" width="160">Sample id:</th>
									<td colspan="4"><%=sample.getSample_id()%>
										<input type="hidden" name="sample_id" value="<%=sample.getSample_id()%>"></td>
								</tr>
								<tr>
									<th align="left" width="160">Compound:</th>
									<td colspan="4"><%=Util.encodeTag(sample.getChemical_name())%></td>
								</tr>
								<tr>
									<th align="left" width="160">Container Id:</th>
									<td colspan="4"><%=sample.getContainer_id()%></td>
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
						analysis.getAnalysisData3(analysis_id, samp_id);
						
						//boolean isActiveAnalysis = analysis.isActiveAnalysis();
						//if(isActiveAnalysis)//use only active analysis's
						
						//{
						%>
							<table border="0" width="100%">
								<tr>
									<th colspan="4" class="blue"><%=analysis.getAnalysis_name()%></th>
								</tr>
								<tr>
									<th width="225px" class="blue">Text id</th>
									<th width="225px" class="blue">Result</th>
									<th width="75px" class="blue">Unit</th>
									<th width="75px" class="blue">Type</th>
								</tr><%
								Vector elements = analysis.getElements();
								if(elements.isEmpty())
								{%>
									<tr><td align="center" colspan="4"><i>NO FIELDS</i></td></tr><%
								}
								else
								{
									for (int n = 0; n<elements.size(); n++)
									{
									  out.print((String) elements.get(n));
									}//end for list of fields in analysis
								}%>
							</table>
				<%		//}
					}%> 
					   </td>
					</tr><%
					if(isStarted)
					{%>
					<tr>
						<td>
							<table border="0" width="100%">
								<tr>
									<th class="blue" align="center">Reason For Change</th>
								</tr>
								<tr>
									<td>
										<TEXTAREA name="reason_for_change" cols="72" rows="4"></TEXTAREA>	
										<INPUT type="hidden" name="isStarted" value="true">
									</td>
								</tr>
							</table>
						</td>
					</tr><%
					}%>
				</table><br>
				<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.RESULT_BASE%>?action=result_entry&code3=yes'">
				<input class="submit" type="reset" name="reset" value="Reset">
			</form>
			</center>
	  <%}
		else
		{
			int errorCode = sample.getErrorCode();
		%>
		    <h2>Result entry</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
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
	  }
	  else//the sample is locked display warning
	  {%>
	  	    <h2>Result entry</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<br>
			<center>
				<h3>This sample is locked, and no result entry can be perfomed.<BR>
					To display the sample as readonly click <a href="<%= Attributes.JSP_BASE %>?action=display_single_sample&code1=yes&sample_id=<%=samp_id%>">here.</a></h3>
			</center><%
			
	  
	  }
}//end code 1

//register the results and continue to display a receipt.
if(request.getParameter("code3") != null)
{
	String sampleStarted = request.getParameter("isStarted");
	String reason = request.getParameter("reason_for_change");
	sample_id = request.getParameter("sample_id");//get the sample id
	
	Map value_list = new HashMap(); //create a list for the result data
	value_list = request.getParameterMap(); //fill the list with the result data
	result.setValue_list(value_list);
	result.setUser(user);//set the user
	result.setSample_id(sample_id);//set the sample id
	
	if(sampleStarted != null && sampleStarted.equalsIgnoreCase("true"))//the sample is started, so a comment is mandatory
	{
		/*The requireement for reason of changed removed due to user wish!*/
		//if(reason == null || reason.equalsIgnoreCase(""))//no comment return and enter results over again!
		//{
		//	response.sendRedirect(Attributes.JSP_BASE+"?action=result_entry&code1=yes&sample_id="+sample_id+"&errorcode1=yes");
		//}
		//else//reason for change has been stated, now register
		//{
			result.setReason_for_change(reason);
			boolean checkresult = result.registerResultData();//register the data in the db...
			int statusCode = result.getStatusCode();
			System.out.println("status code client: "+statusCode);
			
			if(checkresult && statusCode == 1)
			{		
				if(reason == null || reason.equalsIgnoreCase(""))//no comment return and enter results over again!
				{
					//add a remark to sample history
					sample.insertSampleHistory(sample_id, "SAMPLE/RESULT MODIFIED", user);
				}
				else
				{		
					//add a remark to sample history
					sample.insertSampleHistory(sample_id, "SAMPLE/RESULT MODIFIED. "+reason, user);	
				}
			
				response.sendRedirect(Attributes.RESULT_BASE+"?action=result_entry&code4=yes&sample_id="+sample_id);
			}
			else if(checkresult && statusCode == 2)
			{		
				response.sendRedirect(Attributes.RESULT_BASE+"?action=result_entry&code1=yes&codeNoChange=yes&sample_id="+sample_id);
			}
			else
			{%>
					<CENTER>
						<H3>Registration of results failed, please try again.</H3>
					</center>
		<%	}
		//}
	}
	else//not a previously started sample... no reason for change needed register...
	{
		boolean checkresult = result.registerResultData();//register the data in the db...
	
		if(checkresult)
		{		
			//the results has been correctly registered, now register a remark stating that this is a initial data entry
			sample.insertSampleHistory(sample_id, "INITIAL RESULT ENTRY", user);

		
			response.sendRedirect(Attributes.RESULT_BASE+"?action=result_entry&code4=yes&sample_id="+sample_id);
		}
		else
		{%>
			<br><br>
				<CENTER>
					<H3>Registration of results failed, please try again.</H3>
				</center>
	<%	}
	}
}//end code3

//show the data registeret...
if(request.getParameter("code4") != null)
{%>
	<h2>Result entry</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	<br>
	<p>Here you see the data entered for the sample</p>
<%	
	sample_id = sample.getSample_id();
	boolean showResult = sample.getSampleInfo2();
	if(showResult)
	{	
	//display the entered results...
%>	<center>
	<table class="box" cellspacing="1" cellpadding="1" border="0" width="600px">
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
						<td colspan="4"><%=Util.encodeTag(sample.getChemical_name())%></td>
					</tr>
					<tr>
						<th align="left" width="160">Container Id:</th>
						<td colspan="4"><%=sample.getContainer_id()%></td>
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
		<%		Vector elements1 = sample.getElements();
		System.out.println("elementer fra sample: "+elements1);
				String analysis_id = "";
				
				for (int m = 0; m<elements1.size(); m++)
				{
					analysis_id = (String) elements1.get(m);
					
					//get information about the single analysis
					analysis.getAnalysisData2Readonly(analysis_id, sample_id);
				//	boolean isActiveAnalysis = analysis.isActiveAnalysis();	
					//if(isActiveAnalysis)//use only active analysis's
					//{
					Vector elements = analysis.getElements();
					if(!elements.isEmpty())
					{
					%>
						<table border="0" width="100%">
							<tr>
								<th colspan="4" class="blue"><%=analysis.getAnalysis_name()%></th>
							</tr>
							<tr>
								<th width="250px" class="blue">Text id</th>
								<th width="250px" class="blue">Result</th>
								<th width="100px" class="blue">Unit</th>
								<th width="100px" class="blue">Status</th>
							</tr><%
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
							}//end for list of fields in analysis%>
						</table>
			<%		}//end if...			
				}//end for list of analysis%>
			 </td>
		</tr>
	</table><br>
</center>
		
  <%}
	else
	{%>
		<CENTER>
			<H3>Registration OK, result could not be displayed.</H3>
		</center><%
	}
}//end code 4
%>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>