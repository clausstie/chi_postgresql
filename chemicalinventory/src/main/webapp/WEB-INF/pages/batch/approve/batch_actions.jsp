<%@ page language="java" import="java.util.*" import="java.net.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
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
    <title>Lock a batch</title>
        
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
    <script language="JavaScript" src="../script/overlib.js"></script>
    <script language="JavaScript" src="../script/inventoryScript.js"></script>
    <script LANGUAGE="JavaScript">   
    <%
	if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode1") == null && request.getParameter("errorcode3") == null)
	  {
	   %>
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
	   <%
	  }
	  %>
	</script>    
  </head>
  <%
  String user = request.getRemoteUser();
  int batch_id = 0;
  boolean isBatchLocked = false;
  
   if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode1") == null && request.getParameter("errorcode3") == null)  
   {
   %>
   <body onload="document.resource_form.batch_id.focus()">
   <%
  }
  else
  {
  	%> 
  <body>
  <%
  }%>
  
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
   if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null && 
   request.getParameter("errorcode1") == null && request.getParameter("errorcode1") == null && request.getParameter("errorcode3") == null)
   {
   %>
   <h2>Lock Batch</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
   <center>
   	<form method="post" action="<%=Attributes.BATCH_BASE%>?action=lock_batch&code1=yes" name="resource_form" onsubmit="return validateForm(this)">
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
 	%>
		    <h2>Lock Batch</h2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		 	  <center>
				<form method="post" action="<%=Attributes.BATCH_BASE%>?action=lock_batch&code2=yes">
					<table class="box" width="650px" border="0">
						<tr>
							<th class="blue" align="center" colspan="2">Batch Details</th>
						</tr>
						<tr>
							<th align="left" style="width: 160px">Batch</th>
							<td><%=batch_id%>
								<input type="hidden" name="batch_id" value="<%=batch_id%>"/>
							</td>
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
							<th align="left" style="width: 160px">Locked</th>
							<td><%=batch.getLocked()%></td>
						</tr>					
						<tr>
							<th align="left" style="width: 160px">Description</th>
							<td><%=batch.getDescription()%></td>
						</tr>									
						<tr>
							<th class="blue" align="center" colspan="2">Sample Overview</th>
						</tr>
						<tr>
							<td colspan="2">
							<table width="100%" border="0">
								<tr>
									<th class="blue" align="center" width="200px">Analysis Name</th>
									<th class="blue" align="center" width="50px">Sample Id</th>
									<th class="blue" align="center" width="200px">Text Id</th>
									<th class="blue" align="center" width="100px">Value</th>
									<th class="blue" align="center" width="50px">Unit</th>
									<th class="blue" align="center" width="50px">Status</th>						
								</tr>
								<%
								if(Util.isValueEmpty(batch.getSample_list()) && !batch.getSample_list().equals("--"))
								{
									batch.listSamplesOnBatch(batch.getSample_list(), 1);
			
									Vector data = batch.getAnalysis_lines();
									
									if(!data.isEmpty() && data != null)
									{
										for(int t = 0; t<data.size(); t++)
										{
											String batch_data = (String) data.get(t);
											%>
												<%=batch_data%>
											<%
										}
									}
									else
									{
										%>
										<tr>
											<td colspan="5"><b><i>...No samples for this batch..</i></b></td>
										</tr>
										<%
									}
								}
								else
								{
									%>
									<tr>
										<td colspan="5"><b><i>...No samples for this batch..</i></b></td>
									</tr>
									<%
								}
							%>	
								</table>			
							</td>
						  </tr>
					</table><br/>
					<%
					if(isBatchLocked == false)
					{%>
				    <input class="submit" type="submit" value="Lock Batch" onclick="this.form.action='<%= Attributes.BATCH_BASE %>?action=lock_batch&code2=yes'">&nbsp;&nbsp;
				    <%
				    }
				    else
				    {%>
				    <p>This batch is locked, and you here have the ability to unlock the batch</p>
				    <br/>
		   	   	    <input class="submit" type="submit" value="Un-Lock Batch" onclick="this.form.action='<%= Attributes.BATCH_BASE %>?action=unlock_batch&code2=yes'">
		   	   	    <%
		   	   	    }%>
			   	    <input class="submit" type="submit" value="Cancel" onclick="this.form.action='<%= Attributes.BATCH_BASE %>?action=lock_batch'">
				</form>
			</center>
		<%
		}
		else
		{%>
		 <h2>Lock Batch - Error Status</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br/>
		<h3>The batch number <%=batch_id%>, is not valid, please enter a valid batch id.</h3>
		<%
		}
	}
	//lock the batch
	if (request.getParameter("code2") != null)
 	{
 		batch_id = batch.getBatch_id();
 		batch.setUser(user);
 		boolean lock = batch.lockBatch(batch_id);
 		
 		if(lock)
 		{
 			response.sendRedirect(Attributes.BATCH_BASE+"?action=lock_batch&code3=yes&batch_id="+batch_id);
 		}
 		else
 		{
 			String unlocked_list = batch.getUnlocked_list();
 			if(unlocked_list.equals(""))
 			{
	 			response.sendRedirect(Attributes.BATCH_BASE+"?action=lock_batch&errorcode2=yes&batch_id="+batch_id);
 			}
 			else
 			{
 				response.sendRedirect(Attributes.BATCH_BASE+"?action=lock_batch&errorcode3=yes&batch_id="+batch_id+"&unlocked_list="+unlocked_list);
 			}
 		}
 	}
 	//status of the lock operation.
 	if (request.getParameter("code3") != null)
 	{
 		batch_id = batch.getBatch_id();
 		
 		%>
 		 <h2>Lock Batch - Status</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br/>
		<h3>The batch number <%=batch_id%> has been locked.</h3>
 		<%
 		
 	}		
 	if (request.getParameter("errorcode2") != null)
 	{
 		batch_id = batch.getBatch_id();
 		
 		%>
 		 <h2>Lock Batch - Error Status</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br/>
		<h3>The batch number <%=batch_id%> could not be locked.</h3>
 		<%
 	}
 	if (request.getParameter("errorcode3") != null)
 	{
 		batch_id = batch.getBatch_id();
 		String unlocked_list = request.getParameter("unlocked_list");
 		
 		%>
 		 <h2>Lock Batch - Error Status</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br/>
		<h3>The batch number <%=batch_id%>, could not be locked because the following samples has not been locked: (<%=unlocked_list%>)</h3>
<%
 	}				
	if(request.getParameter("errorcode1") != null)
	{
		%>
		<h2>Batch - Error</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png"><br/>
		<h3>The batch could not be displayed.</h3>
		<%
	}
	%>
	</span>
		<map name="nav_bar">
		  <AREA SHAPE="rect" COORDS="3,2,150,23" href="<%=Attributes.JSP_BASE%>?action=batch">
		</map>
  </body>
</html>