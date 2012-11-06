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
<script language="JavaScript" src="../script/overlib.js"></script>
<title>Create a of compounds - The search result from the SampleSearch page</title>
</head>
<body>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<%
//initial values to be used on this page
Vector result = (Vector) session.getAttribute("compoundList");
boolean check = false;

if(result == null || result.isEmpty())
{
	check = false;
}
else
{
	check = true;
}
	

//now based on the infomation in the vector display an list of compounds in the search result
%>
<center>

<%
if(check == true)
{%>
 <table cellspacing="1" cellpadding="1" border="0" width="100%" rules="rows">
    <thead><tr> <th class="blue">No.</th>
                <th class="blue">Chemical Name</th>
                <th class="blue">CAS number</th>
                <th class="blue">Formula</th>
                <th class="blue">Molweight</th>
            </tr>
    </thead>
    <tbody>
    <%
       for(int i=0; i<result.size(); i++)
       {
        String color = "normal";
          if(i % 2 != 0)
          {
            color = "blue";
          }
     %>
       <tr align="center" class="<%= color %>">
       <td> <% out.print(i+1); %></td><%
          String data = (String) result.get(i);
          StringTokenizer tokens = new StringTokenizer(data, "|", false);
          while(tokens.hasMoreElements())
          {
            String token = tokens.nextToken();
            token.trim();
            %><td><%              
                  out.println(URLDecoder.decode(token, "UTF-8"));
                  %>
              </td>
        <%}%>
        </tr>
     <%}%>
    </tbody>
  </table>
  <%
  } 
  if(check==false)
  {
    %>
    <table  cellspacing="1" cellpadding="1" border="0" width="100%" rules="rows">
	    <thead>
	    	<tr> 
	    		<th colspan="7" class="blue">Information</th>
	        </tr>
	    </thead>
	    <tbody>
	       <tr>
	            <td colspan="7" align="left"><i>Search result is empty.</i></td>
	       </tr>
	    </tbody>
    </table>
    <%
    }%>
</center>
</body>
</html>  