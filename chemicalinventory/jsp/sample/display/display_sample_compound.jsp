<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
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
<script language="JavaScript" src="../script/overlib.js"></script>

<title>Display list of samples for a compound</title>
</head>
<body>

<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%
//initial values to be used on this page
sample.setBase(Attributes.JSP_BASE);
String compound_id = "";
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_display.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.JSP_BASE %>?action=display_single_sample">Display sample information</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=display_sample_list">List samples for compound</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a> |
</span>
<span class="textboxadm">
<%
compound_id = sample.getCompound_id();
boolean showResult = sample.hasCompoundSamples(compound_id, true);
	
if(showResult)
{
	
	//display the search result
%>
<h2>Display list of samples for <%=sample.getChemical_name()%></h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>	
<center>
	<table class="box" cellspacing="1" cellpadding="1" border="0" width="725px">
		<tr>
			<td>
				<table border="0" width="100%">
					<thead>
						<tr>
							<th class="blue">Sample id</th>
							<th class="blue">Container id</th>
							<th class="blue">Created date</th>
							<th class="blue">Created by</th>
							<th class="blue">Batch</th>
							<th class="blue">Locked</th>
							<th class="blue">Remark</th>
							<th class="blue">&nbsp;</th>
						</tr>
					</thead>
					<tbody><%
						Vector elements = sample.getElements();
						
						for (int i = 0; i<elements.size(); i++)
						{
						  String color = "normal";
				          if(i % 2 != 0)
				          {
				            color = "smoke";
				          }
				          %>
				          <tr class="<%=color%>"><%
						  
							String data = (String) elements.get(i);
							String s_id = "";
							int counter = 0;
							StringTokenizer tokens = new StringTokenizer(data, "|");
							
							while (tokens.hasMoreTokens())
							{
								String token = tokens.nextToken().trim();
								
								if(counter == 0)
								{
									s_id = token;
									counter++;
								}	

								%>
								<td align="center">
									<%out.print(token);%>
								</td>
								<%
							}%>
								<form method="post" action="<%= Attributes.JSP_BASE %>?action=display_single_sample&code1=yes">
									<td>
										<input class="submit_width85" type="submit" value="Details"/>
										<input type="hidden" name="sample_id" value="<%=s_id%>"/>
									</td>
								</form>
						 </tr><%
						}%>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
</center>
  <%}
	else
	{
		int errorCode = sample.getErrorCode();
		%>
		<h2>Display list of samples</h2>
		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
		<br>
	<%
			if(errorCode == 1)// no sample for the id
			{%>
			<center>
				<h3>No samples registered for the compound.</h3>
			</center><%
			}
			else if(errorCode == 2)
			{%>
			<center>
				<h3>An error orcurred please try again...</h3>
			</center><%
			}
	}%>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>