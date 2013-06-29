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
    <title>Batch intro page</title>
        
        <link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
  </head>
  
  <body>
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
  <h2>Batch module</h2>
  <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
  <br>
  <h3>This is the batch module, allowing you to handle batches of compounds.</h3>
	<p>The batch module, gives you the posibility to register created batches
	of compounds. To these batches you can add the samples analysed on this
	particular batch of a compound, creating an easy approch to the results.</p>
  </span>
		<MAP NAME="nav_bar">
		  <AREA SHAPE="rect" COORDS="3,2,150,23" href="<%=Attributes.JSP_BASE%>?action=batch">
		</map>
  </body>
</html>