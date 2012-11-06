<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:setProperty name="analysis" property="*"/>
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
<script type="text/javascript">
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
<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{%>
 if(trim(form.sample_id.value)== "")
 {
    alert("Please fill a valid sample id");
    form.sample_id.focus();
    return false;
 }
 else
 { 
	 if(isNumber(form.sample_id.value)==false)
	 {
	    alert("Please fill in a valid sample id");
	    form.sample_id.focus();
	    return false;
	 }
 }
<%
}
//the check for comment is removed to make is voluntary
if(request.getParameter("code1") != null)
{%>

 //get the value from the analysis list:
 getValue(form);
 <%
}%>
 //if(trimAll(form.reason_for_change.value) == "")
 //{
 //	alert("To modify data for this sample, you have to enter a 'Reason For Change' in the text field.");
 //	return false;
 //}
 <%
//}%>

 return true;
}
</script>
<title>Modification of a sample - add or remove analysis'</title>
</head>
<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{%>
<body onload="document.sample.sample_id.focus()"><%
}
else
{%>
<body><%
}

//initial values to be used on this page
String samp_id = sample.getSample_id();
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

if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null)
{%>
<h2>Enter sample to modify</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<center>
<form method="post" action="<%= Attributes.SAMPLEAPPROVER_BASE %>?action=modify_sample&code1=yes" name="sample" onsubmit="return validateForm(this)">
  <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
 		<TR><TH colspan="4" class="blue">Sample</TH></TR>
		<tr>
			<th align="left" class="standard">Sample id:</th>
			<td><input type="text" name="sample_id" class="w200"></td>
		</tr>
	</table><br>
	<input class="submit" type="submit" name="Submit" value="Submit" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample&code1=yes'">&nbsp;
	<input class="submit" type="reset" name="reset" value="Reset">
</form>
</center>
<%
}//end initial entry of sample id

//show error message, sample can not be modified, because it has been locked.
if (request.getParameter("startedSample") != null )
{%>
	<br>
	<center>
	<h3>This sample is locked, and connot be modified.<BR>
		To display the sample as readonly click <a href="<%= Attributes.JSP_BASE %>?action=display_single_sample&code1=yes&sample_id=<%=samp_id%>">here.</a></h3>
	</center><%
}

//modify the sample...
if (request.getParameter("code1") != null )
{
	//firste check if the sample is locked, if yes do not modify...
	boolean isSampleLocked = sample.isSampleLocked(samp_id);
	if(isSampleLocked)//the sample is locked
	{
		response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&startedSample=yes&sample_id="+samp_id);
	}
	else
	{
		//get informatin about the sample, that is not locked.
		boolean check2 = sample.getSampleInfo3();
		String current_list = sample.getList_of_analysis();
		
		if(check2)
		{
	%>
		<h2>Modify sample</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br>
			<%//error messages.
			if(request.getParameter("errorcode1") != null)
			{%>
				<center>
					<h3>Nothing has been changed, and sample has not been updated</h3>
				</center>
			<%	
			}
			if(request.getParameter("errorcode2") != null)
			{
			%>
				<center>
					<h3>At least one analysis must be connected to the sample</h3>
				</center>
			<%
			}
			if(request.getParameter("errorcode3") != null)
			{
			%>
				<center>
					<h3>Error, the sample could not be updated</h3>
				</center>
			<%
			}
			if(request.getParameter("errorcode4") != null)
			{
			
			//this check is removed to make the entry of comment volountary
			%>
				<center>
					<h3>Error, to modify the sample a reason for change has to be entered</h3>
				</center>
			<%
			}
		%>
		<br>
		<P>This is the data currently registered for this sample</P>
		<center>
		<form method="post" action="<%= Attributes.SAMPLEAPPROVER_BASE %>?action=modify_sample&code2=yes" name="sample" onsubmit="return validateForm(this)">
			<table class="box" cellspacing="1" cellpadding="1" border="0" width="75%" frame="box">
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
						<table border="0" width="100%">
							<tr>
								<th colspan="5" class="blue">Analysis mapped for sample</th>
							</tr>
							<tr>
								<th class="blue">#</th>
								<th class="blue">Analysis Name</th>
								<th class="blue">Remove</th>
							</tr>
							
							<%//show receipt for the creation
							Vector elements = sample.getElements();
							int current_number_of_analysis = elements.size();
							
							for (int i = 0; i<current_number_of_analysis; i++)
							{
								String data = (String) elements.get(i);
							%>
								<tr>
									<td align="center" width="15"><%=String.valueOf(i+1)%>
									<input type="hidden" name="current_no_analysis" value="<%=current_number_of_analysis%>"></td>
					            	<%=data%>
					            </tr><%
							}%>		
			    		</table>
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<tr>
								<th colspan="5" class="blue">Add analysis</th>
							</tr>
							<tr height="2">
								<td></td>
							</tr>
							<tr>
								<th class="blue" width="31">&#63;</th>
								<th class="blue" width="198">Analysis name</th>
								<th class="blue" width="464">Description</th>
								<th class="blue" width="53">Version</th>
								<th width="15">&nbsp;</th>
							</tr>	
							<tr>
								<td colspan="6">
									<iframe id="list_frame" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list_sample&list_of_analysis=<%=current_list%>" width="750" height="300" marginwidth="0" name="list_frame">
									</iframe>
									
									<input type="hidden" name="id_field" value="">
								</td>
							</tr>
			    		</table>
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" width="100%">
							<tr>
								<th class="blue" align="center">Reason For Change</th>
							</tr>
							<tr>
								<td>
									<TEXTAREA name="reason_for_change" cols="92" rows="4"></TEXTAREA>	
									<INPUT type="hidden" name="isStarted" value="true">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
	<br>
				<input class="submit_width125" type="submit" name="Submit" value="Modify" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample&code2=yes'">
				<input class="submit" type="submit" name="Submit" value="Cancel" onclick="this.form.action='<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample'">
		</form>
		</center><%
		}
		else
		{
	    	int errorCode = sample.getErrorCode();
			%>
			<h2>Modify sample - ERROR</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<br>
				<%
				if(errorCode == 1)// no sample for the id
				{%>
				<center>
					<h3>No sample with id (<%=sample.getSample_id()%>) registered</h3>
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
}//end code 1 = yes

//perform the update of the analysis.. modify the analysis' linked to this sample.
if(request.getParameter("code2") != null)
{
		//REASON FOR CHANGE IS NOT MANDETORY!!
		//make sure that a reason for change has been added:
	/*	String reason = request.getParameter("reason_for_change");
		
		if(reason == null || reason.equalsIgnoreCase(""))//no comment return and enter results over again!
		{
			response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&code1=yes&sample_id="+sample.getSample_id()+"&errorcode4=yes");
		}
		else//reason for change has been stated, now register
		{*/
			sample.setUser(user);
			Map value_list = new HashMap(); //create a list for the result data
			value_list = request.getParameterMap(); //fill the list with the result data
			sample.setValue_list(value_list);
			
			//perform the update and control the status
			boolean check = sample.modifyAnalysisOnSample(sample.getSample_id());
			int statusCode = sample.getErrorCode(); 
			
			if(check && statusCode == 4)//update OK!
			{
				response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&code3=yes&sample_id="+sample.getSample_id());
			}
			else
			{
				if(statusCode == 1)//no update performed
				{
					response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&code1=yes&errorcode1=yes&sample_id="+sample.getSample_id());
				}
				else if(statusCode == 2)//trying to remove all analyiss
				{
					response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&code1=yes&&errorcode2=yes&sample_id="+sample.getSample_id());
				}
				else if(statusCode == 3)//error orcurred
				{
					response.sendRedirect(Attributes.SAMPLEAPPROVER_BASE+"?action=modify_sample&code1=yes&&errorcode3=yes&sample_id="+sample.getSample_id());
				}
			}
//		}
}//end code 2 == yes

//show data for modified sample
if(request.getParameter("code3") != null)
{
	boolean check2 = sample.getSampleInfo();
	
	if(check2)
	{
%>
	<h2>The sample has been modified</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<P>This is the data registered for the sample</P>
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
		<form method="post" action="<%=Attributes.RESULT_BASE%>?action=result_entry&code1=yes&sample_id=<%=sample.getSample_id()%>">
			<input class="submit_width125" type="submit" name="Submit" value="Enter Results">
		</form>
	</center><%
	}
	else
	{%>
		<h2>Display new sample - error</h2>
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