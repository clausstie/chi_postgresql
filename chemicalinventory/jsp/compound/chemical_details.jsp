<%@ page language="java" import="chemaxon.util.*" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="result" class="chemicalinventory.beans.ResultBean" scope="page"/>
<jsp:setProperty name="result" property="*"/>
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
<title>
Details for chemical substance.
</title>
</head>
<body class="mol" onload="window.resizeTo(900, 500)">
<%
  result.result();
  String diverse = request.getParameter("id");
%>

<h2>Selected Compound: <%= URLDecoder.decode(result.getChemical_name(), "UTF-8") %></h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<TABLE class="box" cellpadding="0" cellspacing="0" width="805" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Result</TH></TR>
		<TR>
		<!-- Side for structure search input -->
			<TD>
				<TABLE width="440" cellpadding="0" cellspacing="1" border="0">
					<TR>
					  <TD>
						<%if(result.getStructure()==true)
						  {%>

							<CENTER>
								<script LANGUAGE="JavaScript1.1" SRC="<%=Attributes.MARVIN_JS_FILE%>"></script>
								<script LANGUAGE="JavaScript1.1">
							    <!--
							    marvin_jvm = "builtin"; // "builtin" or "plugin"
							    marvin_gui = "swing"; // "awt" or "swing"
							    mview_begin("<%=Attributes.MARVIN_FOLDER%>", 440, 294);
							    mview_param("mol", "<%= HTMLTools.convertForJavaScript(result.getDbMolfile())%>");
							    mview_param("background", "#FFFFFF");
							    mview_param("animate", "none");
							    mview_param("navmode", "rot3d");
							    mview_param("rendering", "wireframe");
							    mview_param("colorScheme", "mono");
							    mview_param("animFPS", "20");
							    mview_end();
							    //-->
							    </script>
						    </CENTER>
								<%}
						else
						{%>
								<h3>&nbsp;&nbsp;&nbsp;...No structure available.</h3>
								<%
						}%>						
						</TD>
					</TR>
				</TABLE>
			</TD>
					<!-- Side for text search input -->
			<TD>
				<TABLE width="365" cellpadding="0" cellspacing="0" border="1" rules="rows" bordercolor="white">
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Formula:</TH>
						<TD><%= result.getChemicalFormula()%></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD><%= result.getCas_number() %></TD>												
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Mol Weight:</TH>
						<TD><%= result.getMolWeight()%></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Density:</TH>
						<TD><%= result.getDensity() %></TD>
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Register Date:</TH>
						<TD><%= result.getRegister_date() %></TD>		
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Registered By:</TH>
						<TD><%= result.getRegister_by() %></TD>												
					</TR>
					<TR class="h42">
						<TH align="left" class="standard150">Remarks:</TH>
						<TD><%if(result.getRemark()!= null){%><%=result.getRemark()%><%}else{%>No comments.<% }%>
						</TD>												
					</TR>
				</TABLE><!-- Table for text input end -->
			</TD>															
		</TR>				
	</TABLE><!-- Main table end. -->
</body>
</html>