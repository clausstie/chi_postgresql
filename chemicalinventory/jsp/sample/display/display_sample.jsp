<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:useBean id="sample" class="chemicalinventory.sample.SampleBean" scope="page"/>
<jsp:setProperty name="sample" property="*"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
    <title>Display Sample</title>
    
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
   </head>
  
  <body>
  <%
String sample_id = sample.getSample_id();
boolean showResult = sample.getSampleInfo2();

if(showResult)
{	
//display the entered results...
%>
<h2>Display sample information</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>	
<center>
	<table class="box" cellspacing="1" cellpadding="1" border="0" width="700px">
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
						<td colspan="4"><%=Util.encodeTag(sample.getRemark())%></td>
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
				analysis.getAnalysisDataAll(analysis_id, sample_id);
				%>
				<table border="0" width="100%">
					<tr>
						<th colspan="5" class="blue"><%=analysis.getAnalysis_name()%></th>
					</tr>
					<tr>
						<th width="250px" class="blue">Text id</th>
						<th width="250px" class="blue">Result</th>
						<th width="75px" class="blue">Unit</th>
						<th width="75px" class="blue">Status</th>
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
			}
			//end for list of analysis%>
			 </td>
		</tr>
	</table><br>
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
	}%>
  </body>
</html>