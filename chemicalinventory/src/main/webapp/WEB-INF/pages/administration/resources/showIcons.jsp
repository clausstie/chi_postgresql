<%@ page language="java" import="java.util.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="resource" class="chemicalinventory.resource.Resources" scope="page"/>
<%
String path = "";
    if(request.getParameter("disabled") != null)
		path = Attributes.IMAGE_DISABLED;
	else
		path = Attributes.IMAGE;
%>

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
    
    <title>Show available icons...</title>
    
    <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
  </head>
  
  <body>
    <table>
    	<%
    	Vector imgList;
	    if(request.getParameter("disabled") != null)
			imgList = resource.listIcons_vector_disabled();
		else
			imgList = resource.listIcons_vector();
    	
    	if(imgList.isEmpty())
    	{
    	%>
    		<h3>...No images to show</h3>
    	<%
    	}
    	else
    	{  	
    	
	    	for(int i=0; i<imgList.size(); i++)
	    	{
	    		String the_image = (String) imgList.get(i);
	    	%>
	    	  <tr>
	    		<td><p><%=the_image%>:</p></td>
	    		<td>&nbsp;<img src="<%=path+the_image%>" border="0"/><br/></td>
	    	  </tr>
	    	<%
	    	}
    	} 
    	%>
    </table>
  </body>
</html>