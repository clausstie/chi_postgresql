<%@ page language="java" import="java.util.StringTokenizer" import="java.net.*"%>
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
<title>perform a search for the compound...</title>
</head>
<%
if(request.getParameter("code1") == null)
{
%>
<body onload="document.searcher.chemicalName.focus()">
<%
}
else
{%>
<body>
<%
}

//initial values to be used on this page
String history = request.getParameter("history");
String stat = search.getStatement();

/*Define where to return to when a compound has been selected*/
String return_to = request.getParameter("return_to");
String return_base = ""; 

if(return_to.indexOf("display_sample_list") != -1)
{
	return_base = Attributes.JSP_BASE;
}
else if(return_to.indexOf("create_sample") != -1)
{
	return_base = Attributes.SAMPLE_BASE;
}
else if(return_to.indexOf("new_batch") != -1)
{
	return_base = Attributes.BATCH_BASE;
}


%>

<h2>Search for the compound</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<br>
<center>
<!-- First show the initial fields to perform the search -->

<form action="<%= Attributes.JSP_BASE %>?action=s_search&code1=yes&return_to=<%=return_to%>" method="post" name="searcher">
    <table class="box" cellpadding="1" cellspacing="2" border="0" width="700">
  		<TR><TH colspan="4" class="blue">Search</TH></TR>
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
if(request.getParameter("code1")!=null)
{ 
  %>
  <br><hr><br>
  <h3>The result of your search:</h3><%

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
      <a href="<%= Attributes.JSP_BASE %>?action=s_search&history=true&code1=yes&showall=yes&ord_by=<%=order_by%>&statement=<%=statement%>&return_to=<%=return_to%>">SHOW ALL</a><br><br>
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
                 <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=s_search&history=true&code2=yes&code1=yes&ord_by=c.chemical_name&statement=<%=statement%>&showall=<%=show_all%>&return_to=<%=return_to%>">Chemical Name</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=s_search&history=true&code2=yes&code1=yes&ord_by=c.cas_number&statement=<%=statement%>&showall=<%=show_all%>&return_to=<%=return_to%>">CAS number<a/></th>
                 <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=s_search&history=true&code2=yes&code1=yes&ord_by=s.cd_formula&statement=<%=statement%>&showall=<%=show_all%>&return_to=<%=return_to%>">Formula</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=s_search&history=true&code2=yes&code1=yes&ord_by=s.cd_molweight&statement=<%=statement%>&showall=<%=show_all%>&return_to=<%=return_to%>">Molweight</a></th>
                 <th class="blue"></th>
            </tr>
    </thead>

    <tbody>
    <%
       for(int i=0; i<size; i++)
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
          <form method="post" action="<%=return_base%>?action=<%=return_to%>" target="Main">
	          <td>
		          <input class="submit_nowidth" type="submit" name="Select" value="Transfer">
		          <input type="hidden" name="compound_id" value="<%= id %>">
		          <input type="hidden" name="chemical_name" value="<%=name%>">
		          <input type="hidden" name="statement" value="<%=statement%>">
	          </td>
          </form>
        </tr>
     <%}%>
    </tbody>
    </table>
  <%
  } 
  if(check==false)
  {
    %>
    <table class="box"  cellspacing="1" cellpadding="1" width="100%">
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
</center>
</body>
</html>