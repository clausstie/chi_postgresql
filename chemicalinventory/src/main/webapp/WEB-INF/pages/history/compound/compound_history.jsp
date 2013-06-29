<%@ page language="java" import="java.net.*" import="java.util.*" import="chemicalinventory.history.HistoryLine"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="history" class="chemicalinventory.history.History" scope="page"/>
<jsp:setProperty name="history" property="*"/>
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

  <title>Compound History</title>
</head>
<%
if(request.getParameter("code1") == null)
{%>
<body onload="document.compound.chemicalName.focus();">
<%
}
else
{%>
<body>
<%
}

//initial values to be used on this page
String history_st = request.getParameter("history");
String stat = request.getParameter("statement");
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_history_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">Analysis History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=sample_history">Sample History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=container_history">Container History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=compound_history">Compound History</a> |
   <a class="adm" href="<%=Attributes.HISTORY_BASE%>?action=batch_history">Batch History</a> |   
</span>
<span class="textboxadm">
<%

//select the container..
if(request.getParameter("code1") == null)
{%>
<h2>Compound History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<p>Search for compound using either the chemical name or cas number.</p>
<center>
	<form action="<%= Attributes.HISTORY_BASE %>?action=compound_history&code1a=yes" method="post" name="compound">
    <table class="box" cellpadding="1" cellspacing="2" border="0" width="720">
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
</center>
<%
}
//display error message if invalid container id.
if(request.getParameter("errorCode1") != null)
{%>
	<h3>Not a valid compound entered, please try again.</h3>
<%
}

//show the search result for compounds
if(request.getParameter("code1a")!=null)
{ 
  %>
  <h2>Compound History</h2>
  <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
  <br/>
  <h3>The result of your search:</h3><%

  boolean check;
  int size = 0;

  String show_all = request.getParameter("showall");
  if(show_all == null)
    show_all = "no";
  
  if(stat != null && history_st != null)
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
      <a href="<%= Attributes.HISTORY_BASE %>?action=compound_history&history=true&code1a=yes&showall=yes&statement=<%= statement%>&ord_by=<%=order_by%>">SHOW ALL</a><br><br>
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
    <table class="box" cellspacing="1" cellpadding="1" width="100%">
    <thead>
            <tr> <th class="blue">No.</th>
                 <th class="blue"><a class="res" href="<%= Attributes.HISTORY_BASE %>?action=compound_history&history=true&code1a=yes&ord_by=c.chemical_name&statement=<%= statement%>&showall=<%=show_all%>">Chemical Name</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.HISTORY_BASE %>?action=compound_history&history=true&code1a=yes&ord_by=c.cas_number&statement=<%= statement%>&showall=<%=show_all%>">CAS number</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.HISTORY_BASE %>?action=compound_history&history=true&code1a=yes&ord_by=s.cd_formula&statement=<%= statement%>&showall=<%=show_all%>">Formula</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.HISTORY_BASE %>?action=compound_history&history=true&code1a=yes&ord_by=s.cd_molweight&statement=<%= statement%>&showall=<%=show_all%>">Molweight</a></th>
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
     %>
       <tr align="center" class="<%= color %>">
       <td> <% out.print(i+1); %></td><%
          String data = (String) search.result.elementAt(i);
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
	        <form method="post" action="<%=Attributes.HISTORY_BASE%>?action=compound_history&code1=yes">
	          <td>
	          	<input class="submit_nowidth" type="submit" name="Select" value="Show Details"/>
	          	<input type="hidden" name="id" value="<%= id %>"/>
	          	<input type="hidden" name="statement" value="<%=statement%>"/>
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
                 <th class="blue"></th>
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
}

//second display information about the analysis..
if (request.getParameter("code1") != null)
{
	String id = history.getId();
	boolean check = history.getCompoundHistory(id);

	if(check)
	{
%>
<h2>Compound History</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<br>
<CENTER>
	<table class="box" cellpadding="1" cellspacing="1" width="99%" >
		<tr>
			<th colspan="2" align="left">Chemical Name</th>
			<td colspan="6"><b><%=history.getChemical_name()%></b></td>
		</tr>
		<tr>
			<!--spacer cell-->
			<td colspan="8">&nbsp;</td>
		</tr>
		<tr>
			<th class="blue" align="center" colspan="9">History Lines</th>
		</tr>
		<tr>
			<th align="center" class="blue" width="5%">#</th>
			<th align="center" class="blue" width="15%">Change</th>
			<th align="center" class="blue" width="7%">User</th>
			<th align="center" class="blue" width="14%">Timestamp</th>
			<th align="center" class="blue" width="59%">Change Remark</th>
		</tr>
		
		<!--show the lines in the history result-->
		<%
			String color = "";
			Vector elements = history.getTable_list();
			
			if(!elements.isEmpty())
			{
				for(int i=0; i<elements.size(); i++)
				{
			      color = "normal";
		          if(i % 2 != 0)
		          {
		            color = "blue";
		          }
				%>
				<tr class="<%=color%>">
					<td align="center">
						<%=i+1%>
					</td><%
					HistoryLine line = (HistoryLine) elements.get(i);
					String data = line.getHtml_line();
					
					%><%=data%><%
		      %></tr><%
				}
			}
			else
			{
				%>
				<tr>
					<td colspan="8"><i>No history lines for the compound</i></td>
				</tr>
				<%
			}%>
	</table>
</CENTER>

<%}//check was ok
  else//check not ok
  {
   int status = history.getStatus();
   
   if(status == 1)
   {
   	response.sendRedirect(Attributes.HISTORY_BASE+"?action=compound_history&errorCode1=yes");
   }
   else
   {  
  %>
  <h2>Compound History - Error</h2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br>
	<CENTER>
	<h3>Error in display of history for the compound, please try again.</h3>
	</CENTER> 
	<%
	}
  }
}//end code 1. Show history
%>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
</map>
</body>
</html>  