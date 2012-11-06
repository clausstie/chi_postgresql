<%@ page language="java" import="java.util.*" import="java.net.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean" scope="page"/>
<jsp:useBean id="batch" class="chemicalinventory.batch.Batch" scope="page"/>
<jsp:setProperty name="batch" property="*"/>

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
    <title>Create a batch</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
    <script language="JavaScript" src="../script/overlib.js"></script>
    <script language="JavaScript" src="../script/inventoryScript.js"></script>
    <script LANGUAGE="JavaScript">
    function openWindow(url)
	{
		window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=850, height=600")
	}
    
	function validateForm(form) 
	{
	 if (trim(form.compound_id.value) == "" || trim(form.compound_id.value) == "null" || trim(form.compound_id.value) == "0") 
	 {
	  alert("Please select a compound for this batch");
	  form.compound.focus();
	  return false;
	 }	
	return true;
	}
	</script>    
  </head>
  <%
  String user = request.getRemoteUser();
  String com_name = request.getParameter("chemical_name");
  String com_id = request.getParameter("compound_id");
  int batch_id = 0;
  Vector list = new Vector();
  
  if(com_name != null && com_id != null && Integer.parseInt(com_id) > 0)
  {
	com_name = URLDecoder.decode(com_name, "UTF-8");
	
	//create a list of samples available to attach to this batch
	list = sample.samplesOnCompoundList(com_id);
  }
  else
  {
	com_name = "Search For Compound!";
	com_id = "0";
  }
 
  if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("errorcode1") == null)
  {%>
  <body onload="document.resource_form.compound.focus();">
  <%
  }
  else
  {
  %>
  <body>
  <%
  }
  %>
  <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
  
  <span class="posAdm1">
 	<img src="<%=Attributes.IMAGE_FOLDER%>/bar_batch_start.png" height="55" width="820" usemap="#nav_bar" border="0">
  </span> 
   <span class="posAdm2">
	   | <a class="adm" href="<%=Attributes.BATCH_BASE%>?action=new_batch">Create New Batch</a> |
	   <a class="adm" href="<%=Attributes.BATCH_BASE%>?action=modify_batch">Modify Batch</a> |
	   <a class="adm" href="<%=Attributes.D_BATCH_BASE%>?action=view_batch">Display Batch</a> |
   	   <a class="adm" href="<%=Attributes.BATCH_BASE%>?action=lock_batch">Lock Batch</a> |
   	   <a class="adm" href="<%=Attributes.BATCH_BASE%>?action=unlock_batch">Un-Lock Batch</a> |
  </span>
  <span class="textboxadm">
	
   <%
    if (request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("errorcode1") == null)
 	{%>
    <h2>Create Batch</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
 	  <center>
		<form method="post" action="<%=Attributes.BATCH_BASE%>?action=new_batch&code1=yes" name="resource_form" onsubmit="return validateForm(this)">
			<table class="box" width="650px"><!-- the box sourrounding the form-->
				<tr>
					<th class="blue" align="center" colspan="3">Create Batch</th>
				</tr>
				<tr>
					<th align="left" style="width: 160px">Compound</th>
					<td>
						<input type="text" name="compound" value="<%=com_name%>" style="width: 400px">
						<input type="hidden" name="compound_id" value="<%=com_id%>">
						<input type="hidden" name="user" value="<%=user%>">
					</td>
					<td>
						<a href="#" onclick="openWindow('<%=Attributes.JSP_BASE%>?action=s_search&return_to=new_batch')">
							<img src="<%=Attributes.IMAGE_FOLDER%>/plus_mark.png" height="15" width="15" border="0">
						</a>
					</td>
				</tr>
				<tr>
					<th align="left" style="width: 160px">Production Location</th>
					<td>
						<input type="text" name="production_location" style="width: 400px">
					</td>
				</tr>
				<tr>
					<th align="left" style="width: 160px">Include Samples</th>
					<td>
						<%
							if(!list.isEmpty() && list != null)
							{%>
							<select name="samples" multiple="multiple" style="width: 400px" size="2">
							<%
								for(int i = 0; i < list.size(); i++)
								{
									String sample_id = (String) list.get(i);
									%>
									<option value="<%=sample_id%>"><%=sample_id%></option>
									<%
								}
								%>
							</select>
							<%
							}
							else
							{
								%>
								<p>No samples available....</p>
								<%
							}
						%>
					</td>
				</tr>
				<tr>
					<th align="left" style="width: 160px">Notebook Reference</th>
					<td><input type="text" name="notebook_reference" style="width: 400px"></td>
				</tr>
				<tr>
					<th align="left" style="width: 160px">Purity</th>
					<td><input type="text" name="purity" style="width: 400px"></td>
				</tr>						
				<tr>
					<th align="left" style="width: 160px">Description</th>
					<td>
						<textarea name="description" cols="10" rows="5" style="width: 400px"></textarea>
					</td>
				</tr>									
			</table><br/>
		    <input class="submit" type="submit" value="Submit">&nbsp;&nbsp;&nbsp;
    		<input class="submit" type="reset" value="Reset">
		</form>
	</center>
	<%
	}
	if(request.getParameter("code1") != null)
	{
		//register the values
		boolean check = batch.create_batch();
		batch_id = batch.getBatch_id();
			
		/*new batch successfully created*/
		if(check)
		{
			response.sendRedirect(Attributes.BATCH_BASE+"?action=new_batch&code2=yes&batch_id="+batch_id);
		}
		else
		{
			response.sendRedirect(Attributes.BATCH_BASE+"?action=new_batch&errorcode1=yes");
		}
	}
	
	if(request.getParameter("code2") != null)
	{
			//get information about the newly creted batch
			batch_id = batch.getBatch_id();
			
			System.out.println("batch id fra jsp code 2 = "+batch.getBatch_id());
			batch.getBatchInfo(batch_id);
		%>
			<h2>Create Batch - status</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<h3>The new batch has been succesfully created.</h3>
			<br/>
			<center>
				<table class="box" width="650px">
					<tr>
						<th class="blue" align="center" colspan="2">Batch Details</th>
					</tr>
					<tr>
						<th align="left" style="width: 160px">Batch</th>
						<td><%=batch_id%></td>
				    </tr>
					<tr>
						<th align="left" style="width: 160px">Compound</th>
						<td><%=URLDecoder.decode(batch.getChemical_name(), "UTF-8")%></td>
					</tr>
					<tr>
						<th align="left" style="width: 160px">Production Location</th>
						<td>
							<%=batch.getProduction_location()%>
						</td>
					</tr>
					<tr>
						<th align="left" style="width: 160px">Samples Included</th>
						<td><%=batch.getSample_list()%></td>
					</tr>
					<tr>
						<th align="left" style="width: 160px">Notebook Reference</th>
						<td><%=batch.getNotebook_reference()%></td>
					</tr>
					<tr>
						<th align="left" style="width: 160px">Purity</th>
						<td><%=batch.getPurity()%></td>
					</tr>						
					<tr>
						<th align="left" style="width: 160px">Description</th>
						<td><%=batch.getDescription()%></td>
					</tr>									
				</table><br/>
			</center>
		<%
	}
	if(request.getParameter("errorcode1") != null)
	{
		%>
		<h2>Create Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>The new batch could not be created, an error orcurred.</h3>
		<%
	}
	%>
	</span>
		<map name="nav_bar">
		  <AREA SHAPE="rect" COORDS="3,2,150,23" href="<%=Attributes.JSP_BASE%>?action=batch">
		</map>
  </body>
</html>