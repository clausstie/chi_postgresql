<%@ page language="java" import="java.util.*" import="java.io.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="search" class="chemicalinventory.beans.SearchBean" scope="page"/>
<jsp:setProperty name="search" property="*"/>
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<title>
This page is for adding a new container.
</title>
</head>
<%
if(request.getParameter("code1")!=null)
{
%>
<body onload="document.container.chemicalName.focus()">
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
<%
  String history = request.getParameter("history");
  String stat = search.getStatement();
  int i = 0;
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_container.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer&code1=yes">Register a new container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">Modify an existing container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=emptyContainer">Register a container as empty</a> |
</span>
<span class="textboxadm">
<%if(request.getParameter("code1")!=null)
{%>

    <h2>Enter the chemical name.</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<p>Search for the compound for which you wish to register a new container. Search by either the
    chemical name or the CAS number, or a combination of both.</p>

<form action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&code2=yes&code1=yes" method="post" name=container>
		<TABLE class="box" cellpadding="1" cellspacing="2" width="720">
			<TR><TH colspan="4" class="blue">Container</TH></TR>
        <tr>
            <th align="left" class="standard">Chemical name:</th>
            <td><input type="text" name="chemicalName" class="w200"></td>
            <th align="left" class="standard">CAS No.:</th>
            <td><input type="text" name="casNumber" class="w200"></td>
        </tr>
    </table><br>
    <input class="submit" type="submit" value="Submit">&nbsp;&nbsp;&nbsp;
		<input class="submit" type="reset" value="Reset">
</form>
<%
}
%>
<%
if(request.getParameter("code2")!=null)
{ 
  %><hr><h3>The result of your search:</h3><%

  boolean check;
  int size = 0;
  
  String show_all = request.getParameter("showall");
  if(show_all == null)
    show_all = "no";
  
  if(stat != null && history != null)
  { 
    check = search.searchStatementOK();
  }
  else
  {
    check = search.searchOK();
  }

   String statement = URLEncoder.encode(search.getStatement(), "UTF-8");
  
  if(search.getCountHit() > 10 && (!show_all.equalsIgnoreCase("yes") || show_all == null))
  {
    String order_by = request.getParameter("ord_by");
    size = 10;
    %> 
      </center>
      <img src="<%=Attributes.IMAGE_FOLDER%>/warning-bar.png"><br>
       This search result containes more than 10 hits. (Total number of hits: <%=search.getCountHit()%>)
      <a href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&history=true&code2=yes&code1=yes&showall=yes&ord_by=<%=order_by%>&statement=<%=statement%>">SHOW ALL</a><br><br>
      <center>
    <%
  }  
  else
  {
    size = search.result.size();
  }
  
  if(check==true)
  {
  %>
    </center>
    Click on the coloumn of your choice, to see details for the compound, or a heading to sort.
    <center>
    <table class="box" cellspacing="1" cellpadding="1" width="100%">
    <thead>
            <tr> <th class="blue">No.</th>
                 <th class="blue"><a class="res" href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&history=true&code2=yes&code1=yes&ord_by=c.chemical_name&statement=<%=statement%>&showall=<%=show_all%>">Chemical Name</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&history=true&code2=yes&code1=yes&ord_by=c.cas_number&statement=<%=statement%>&showall=<%=show_all%>">CAS number<a/></th>
                 <th class="blue"><a class="res" href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&history=true&code2=yes&code1=yes&ord_by=s.cd_formula&statement=<%=statement%>&showall=<%=show_all%>">Formula</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer&history=true&code2=yes&code1=yes&ord_by=s.cd_molweight&statement=<%=statement%>&showall=<%=show_all%>">Molweight</a></th>
                 <th class="blue"></th>
            </tr>
    </thead>

    <tbody>
    <%
       for(i=0; i<size; i++)
       {  
        String color = "normal";
          if(i % 2 != 0)
          {
            color = "blue";
          }
          String id = (String) search.result_id.elementAt(i);
          String name = (String) search.result_name.elementAt(i);
     %>
       <tr align="center" class="<%= color %>">
	       <td>
	       	<a class="res" href="<%=Attributes.JSP_BASE%>?action=details&id=<%=id%>" target="blank"><% out.print(i+1); %></a>
	       </td><%
          String data = (String) search.result.elementAt(i);
          StringTokenizer tokens = new StringTokenizer(data, "|", false);
          while(tokens.hasMoreElements())
          {
            String token = tokens.nextToken();
            token.trim();
            %><td>
              	<a class="res" href="<%=Attributes.JSP_BASE%>?action=details&id=<%=id%>" target="blank"><% out.println(URLDecoder.decode(token, "UTF-8"));%></a>
              </td>
        <%}%>
          <form method="post" action="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer_reg">
          <td>
	          <input class="submit_nowidth" type="submit" name="Select" value="Registration">
	          <input type="hidden" name="compound_id" value="<%=id%>">
	          <input type="hidden" name="chemical_name" value="<%=name%>">
         </td>
        </tr>
        </form>
     <%}%>
    </tbody>
    </table>
  <%
  } 
  if(check==false)
  {
    %>
    <table class="box" cellspacing="1" cellpadding="1" width="100%">
    <thead>
        <tr> <th class="blue">No.</th>
             <th class="blue">Chemical Name</th>
             <th class="blue">CAS number</th>
             <th class="blue">Formula</th>
             <th class="blue">Molweigth</th>
        </tr>
    </thead>
    <tbody>
       <tr align="center">
       <% if(search.getNoValues()==true)
          {%>
            <td colspan="7">No search criteria has been entered.<br>
                            Please enter a value and try again.</td>
        <%}
          else
          {%>
            <td colspan="7">Search result is empty.</td>
        <%}%>
       </tr>
    </tbody>
    </table>
  <%
  } 
}%>
</span>
</center>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="3,2,90,23" href="<%=Attributes.ADMINISTRATOR_BASE%>?action=Adm">
  <AREA SHAPE="rect" COORDS="92,2,179,23" href="<%=Attributes.SUPPLIER_ADMINISTRATOR_BASE%>?action=Supplier">
  <AREA SHAPE="rect" COORDS="181,2,268,23" href="<%=Attributes.LOCATION_ADMINISTRATOR_BASE%>?action=Location">
  <AREA SHAPE="rect" COORDS="270,2,362,23" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">
  <AREA SHAPE="rect" COORDS="364,2,451,23" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">
  <AREA SHAPE="rect" COORDS="453,2,543,23" href="<%=Attributes.GROUP_ADMINISTRATOR_BASE%>?action=new_group">
  <AREA SHAPE="rect" COORDS="544,3,634,23" href="<%=Attributes.ANALYSIS_ADMINISTRATOR_BASE %>?action=new_analysis">
  <AREA SHAPE="rect" COORDS="637,3,727,23" href="<%=Attributes.UNIT_ADMINISTRATOR_BASE %>?action=new_unit">
</map>
</body>
</html>