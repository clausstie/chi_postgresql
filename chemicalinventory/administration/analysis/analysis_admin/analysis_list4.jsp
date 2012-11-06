<%@ page language="java" import="java.net.*" import="java.util.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="analysis" class="chemicalinventory.analysis.AnalysisBean" scope="page"/>
<jsp:setProperty name="analysis" property="*"/>
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
<title>Create a list of analysis - selected analysis is to be modified</title>
</head>
<body>
<center>
<table border="0" cellpadding="2" cellspacing="1" width="100%">

	<%
		analysis.setBase(Attributes.ANALYSIS_ADMINISTRATOR_BASE);
		analysis.setNormalbase(Attributes.JSP_BASE);
		analysis.listAnalysis4();
		Vector elements = analysis.getElements();
		String color = "normal";
		
		for(int i=0; i<elements.size(); i++)
		{
	      color = "normal";
          if(i % 2 != 0)
          {
            color = "blue";
          }
		%>
		<tr class="<%=color%>"><%
			String data = (String) elements.get(i);
			
			StringTokenizer tokens = new StringTokenizer(data, "|");
			
			while(tokens.hasMoreTokens())
			{
				String token = tokens.nextToken().trim();

				out.print(token);
			}
      %></tr><%
		}
	%>
</table>
</center>
</body>
</html>  