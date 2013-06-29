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
    <title>Modify a batch</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
    <script language="JavaScript" src="../script/inventoryScript.js"></script>
	<%
   if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null &&
   request.getParameter("errorcode3") == null)
   {
   %>
    <script LANGUAGE="JavaScript">  
	function validateForm(form) 
		{
			if(trim(form.batch_id.value)== "")
		 	{
		    	alert("Please fill in a valid batch id!");
		    	form.batch_id.focus();
		    	return false;
		 	}
		 	 else
			 {
			 	if(isPositiveInteger(form.batch_id.value)==false)
			    {
			        alert("Please fill in a valid batch id");
			        form.batch_id.focus();
			        return false;
			    }
			 }
		return true;
		}
	</script>    
	<%
	}
	%>
  </head>
  <%
  String user = request.getRemoteUser();
  int batch_id = 0;
  boolean isBatchLocked = false;
  Vector list = new Vector();
  Vector list_current = new Vector();
  
   if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null &&
   request.getParameter("errorcode3") == null)
   {%>
     <body onload="document.batch_form.batch_id.focus();">
    <%
   }
   else
   {
    %>
    <body>
    <%
   }%>
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
   if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null &&
   request.getParameter("errorcode3") == null)
   {
   %>
   <h2>Modify Batch</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
   <center>
   	<form method="post" action="<%=Attributes.BATCH_BASE%>?action=modify_batch&code1=yes" name="batch_form" onsubmit="return validateForm(this)">
    <table class="box" cellpadding="1" cellspacing="2" border="0" width="370">
  		<TR><TH colspan="4" class="blue">Batch</TH></TR>
   			<tr>
   				<th align="left" class="standard">Batch Id:</th>
   				<td><input type="text" name="batch_id" class="w200"/></td>
   			</tr>
   		</table><br/>
	    <input class="submit" type="submit" value="Submit">&nbsp;&nbsp;&nbsp;
  		<input class="submit" type="reset" value="Reset">
   	</form>
   </center>
   <%
   }
    if (request.getParameter("code1") != null)
 	{
	 	batch_id = batch.getBatch_id();
 		boolean check = batch.getBatchInfo(batch_id);
 		isBatchLocked = batch.isBatchLocked(batch_id);
 		
 		if(check)
 		{
	 		//if the batch is locked no modification is allowed.
	 		if(isBatchLocked)
	 		{
	 			response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&errorcode1=yes&batch_id="+batch_id);
	 		}
	 		else
	 		{
	 	%>
			    <h2>Modify Batch</h2>
				<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			 	  <center>
					<form method="post" action="<%=Attributes.BATCH_BASE%>?action=modify_batch&code2=yes" name="resource_form">
						<table class="box" width="650px"><!-- the box sourrounding the form-->
							<tr>
								<th class="blue" align="center" colspan="3">Modify Batch</th>
							</tr>
							<tr>
								<th align="left">Batch Id</th>
								<td><%=batch_id%></td>
							</tr>				
							<tr>
								<th align="left" style="width: 250px">Compound</th>
								<td>
									<%=URLDecoder.decode(batch.getChemical_name(),"UTF-8")%>
									<input type="hidden" name="user" value="<%=user%>"/>
									<input type="hidden" name="o_production_location" value="<%=batch.getProduction_location()%>"/>
									<input type="hidden" name="o_notebook_reference" value="<%=batch.getNotebook_reference()%>"/>					
									<input type="hidden" name="o_purity" value="<%=batch.getPurity()%>"/>
									<input type="hidden" name="o_description" value="<%=batch.getDescription()%>"/>		
									<input type="hidden" name="o_samples" value="<%=batch.getSample_list()%>"/>	
									<input type="hidden" name="batch_id" value="<%=batch_id%>"/>		
								</td>
							</tr>
							<tr>
								<th align="left" style="width: 250px">Production Location</th>
								<td>
									<input type="text" name="production_location" style="width: 400px" value="<%=batch.getProduction_location()%>">
								</td>
							</tr>
	    					<tr>
								<th align="left" style="width: 250px">Currently Connected Samples</th>
								<td>
									<%=batch.getSample_list()%>
								</td>
							</tr>
							<tr>
								<th align="left" style="width: 250px">Include Samples</th>
								<td>
									<%
										//list of all posible samples...
										String com_id = String.valueOf(batch.getCompound_id());
										
										list = sample.samplesOnCompoundList_mod(com_id, batch_id);
										
										if(!list.isEmpty() && list != null)
										{
											String selected = "";
											list_current = batch.listSamplesOnBatch_vector(batch_id);
										%>
										<select name="samples" multiple="multiple" style="width: 400px" size="2">
										<%
											for(int i = 0; i < list.size(); i++)
											{
												String sample_id = (String) list.get(i);
												
												if(list_current.contains(sample_id))
													selected = 	"selected=\"selected\"";
												else
													selected = "";				
												%>
												<option <%=selected%> value="<%=sample_id%>"><%=sample_id%></option>
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
							<%
							if(!list.isEmpty() && list != null)
							{
							%>
							<tr>
								<th align="left" style="width: 250px">Remove All Samples</th>
								<td><input type="checkbox" name="remove_samples"/></td>
							</tr>
							
							<%
							}
							%>
							<tr>
								<th align="left" style="width: 250px">Notebook Reference</th>
								<td><input type="text" name="notebook_reference" style="width: 400px" value="<%=batch.getNotebook_reference()%>"></td>
							</tr>
							<tr>
								<th align="left" style="width: 250px">Purity</th>
								<td><input type="text" name="purity" style="width: 400px" value="<%=batch.getPurity()%>"></td>
							</tr>						
							<tr>
								<th align="left" style="width: 250px">Description</th>
								<td>
									<textarea name="description" cols="10" rows="5" style="width: 400px"><%=batch.getDescription()%></textarea>
								</td>
							</tr>						
							 <tr>
						    	<td colspan="3">
									<table border="0" width="100%">
										<tr>
											<th class="blue" align="center">Reason For Change</th>
										</tr>
										<tr>
											<td align="center">
												<TEXTAREA name="reason_for_change" cols="20" rows="4" style="width: 650px"></TEXTAREA>	
											</td>
										</tr>
									</table>
								</td>
						    </tr>			
						</table><br/>
					    <input class="submit" type="submit" value="Submit">&nbsp;&nbsp;&nbsp;
			    		<input class="submit" type="reset" value="Reset">
					</form>
				</center>
<%
			}
		}
		else
		{%>
		 <h2>Modify Batch - Error Status</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br/>
		<h3>The batch number <%=batch_id%>, is not valid, please enter a valid batch id.</h3>
		<%
		}
   }
	if(request.getParameter("code2") != null)
	{
		//register the values
		batch.setUser(user);
		batch_id = batch.getBatch_id();
		boolean check = batch.modifyBatch(batch_id);
			
		/* batch successfully modified.*/
		if(check)
		{
			response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&code3=yes&batch_id="+batch_id);
		}
		else
		{
			int status = batch.getStatus();
			
			if(status == 1)
			{
				//samples on batch could not be modified.
				response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&errorcode2=yes&batch_id="+batch_id);
			}
			else if(status == 2)
			{	
				//unexpected error..
				response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&errorcode3=yes&batch_id="+batch_id);
			}
			else if(status == 3)
			{
				//input validation failed.
				response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&errorcode4=yes&batch_id="+batch_id);
			}
			else
			{
				//another bizarre error
				response.sendRedirect(Attributes.BATCH_BASE+"?action=modify_batch&errorcode3=yes&batch_id="+batch_id);
			}
		}
	}
	
	if(request.getParameter("code3") != null)
	{
			//get information about the newly creted batch
			batch_id = batch.getBatch_id();
		
			batch.getBatchInfo(batch_id);
		%>
			<h2>Modify Batch - status</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
			<h3>The batch has been succesfully modified.</h3>
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
		batch_id = batch.getBatch_id();
		%>
		<h2>Modify Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>Batch number <%=batch_id%> cannot be modified because it is locked.</h3>
		<%
	}
	if(request.getParameter("errorcode2") != null)
	{
		batch_id = batch.getBatch_id();
		%>
		<h2>Modify Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>Batch number <%=batch_id%> cannot be modified because samples connected to the batch
		could not be modified, please try again.</h3>
		<%
	}
	if(request.getParameter("errorcode3") != null)
	{
		batch_id = batch.getBatch_id();
		%>
		<h2>Modify Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>Batch number <%=batch_id%> cannot be modified, please try again.</h3>
		<%
	}
	if(request.getParameter("errorcode4") != null)
	{
		batch_id = batch.getBatch_id();
		%>
		<h2>Modify Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>Batch number <%=batch_id%> cannot be modified - error in input fields.</h3>
		<%
	}
	%>
	</span>
		<map name="nav_bar">
		  <AREA SHAPE="rect" COORDS="3,2,150,23" href="<%=Attributes.JSP_BASE%>?action=batch">
		</map>
  </body>
</html>