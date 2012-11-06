<%@ page language="java" import="chemaxon.util.*, java.util.*, java.net.*"%>
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
<title>
The Chemical search page
</title>
</head>
<%
if(request.getParameter("code1")==null)
{%>
<body onload="f=setInterval('document.search.chemicalName.focus()',1000);" onkeydown="clearInterval(f);" onkeypress="clearInterval(f);" onClick="clearInterval(f);">
<%
}
else
{%>
<body>
<%
}%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%
  String stat = null;
  String history = request.getParameter("history");
  String value = request.getParameter("value");
  if(history != null)
  {
    HttpSession statementSession = request.getSession();
    stat = (String) statementSession.getAttribute("sqlStatement");
    search.setStatement(stat);
  }
%>

<h2>Search for chemical/compound</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
<center>
<form method="post" action="<%=Attributes.JSP_BASE%>?action=Search&code1=yes" name="search">
	<TABLE class="box" cellpadding="0" cellspacing="1" width="795" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Search</TH></TR>
		<TR>
		<!-- Side for text search input -->
			<TD>
				<TABLE width="375" cellpadding="0" cellspacing="0" border="1" rules="rows" bordercolor="white">
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Name:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('Enter the search criteria; searching on the chemical name of the compound. It is not nesessary to use sql notations in this field. Search for multiple criterias by using \'\+\', as delimiter character.', LEFT, BORDER, 2, CAPTION, 'CHEMICAL NAME');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input class="w200" type="text" name="chemicalName" tabindex="1"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Formula:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('Search on the chemical formula. To get all chemicals containing C5 enter %C5% as search criteria. Only compounds with a structure will have a chemical formula registered.', LEFT, BORDER, 2, CAPTION, 'CHEMICAL FORMULA');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input class="w200" type="text" name="cd_formula" tabindex="2"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('If you know the CAS number of the compound in question, search for it in this field. Here you can again use sql notations and search for CAS number containing 345 with %345%.', LEFT, BORDER, 2, CAPTION, 'CAS NUMBER');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input type="text" class="w200" name="casNumber" tabindex="3"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Mol Weight:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('This field is a numbers only field. Here you can search for exact numbers or use < and > signs. Only compounds where a chemical structure is registered will have a mol weigth registrered.', LEFT, BORDER, 2, CAPTION, 'MOL WEIGHT');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input type="text" class="w200" name="cd_molweight" tabindex="4"></TD>
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Density:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('This field is a numbers only field. Here you can search for exact numbers or use < and > signs.', LEFT, BORDER, 2, CAPTION, 'DENSITY');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input type="text" class="w200" name="density" tabindex="5"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Register Date:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('Search for the date the compound was registrered. The data format is YYYY-MM-DD ex. 2003-12-24. You can here use < and > to find compound registrered before or after a specific date. Entering one date will only show the compounds registrered on that specific date.', ABOVE, BORDER, 2, CAPTION, 'REGISTRATION DATE');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input type="text" class="w200" name="register_date" tabindex="6"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Registered By:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('Select all chemicals registered by a specific person. The initials of the person registring the chemical is registrered here.', ABOVE, BORDER, 2, CAPTION, 'REGISTRERED BY');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD><input type="text" class="w200" name="register_by" tabindex="7"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Search method:</TH>
						<TD class="standard18"><a href="javascript:void(0);" onmouseover="return overlib('In the simplest case two molecular structures are compared. An exact structure search determines whether they have the same topology or not. Chemists are more often interested in substructure search, that is, whether one molecular structure contains the other one as a substructure or not. Similarity searching finds molecules that are similar to the query structure. For further detailed information see the JChem documentation.', ABOVE, BORDER, 2, CAPTION, 'SEARCH METHOD');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a></TD>
						<TD>
							<select name="search_method" style="width: 200;">
								<option value="SUBSTRUCTURE" selected>SUBSTRUCTURE</option>
								<option value="EXACT">EXACT</option>
								<option value="SIMILARITY">SIMILARITY</option>
							</select> 
							<input type="hidden" name="molfile">
						</TD>	
					</TR>
				</TABLE>
			</TD>
		<!-- Side for structure search input -->
			<TD>
				<TABLE width="430" cellpadding="0" cellspacing="1" border="0">
					<TR>
						<TD>
						   <CENTER>
						    <script LANGUAGE="JavaScript1.1" SRC="<%=Attributes.MARVIN_JS_FILE%>"></script>
						    <script LANGUAGE="JavaScript1.1">
						    <!--
						    // marvin_jvm = "builtin"; // "builtin" or "plugin"
						    // marvin_gui = "awt"; // "awt" or "swing"
						    // marvin_signed = "true"; // "signed applet
						    msketch_name = "MSketch";   
						    msketch_begin("<%=Attributes.MARVIN_FOLDER%>", 430, 384); // arguments: CODEBASE, WIDTH, HEIGHT
						    msketch_param("mol", "<%= HTMLTools.convertForJavaScript(search.getMolfile())%>");
						    msketch_param("background", "#FFFFFF");
						    msketch_param("molbg", "#FFFFFF");
						    msketch_param("colorScheme", "cpk");
						    msketch_end();
						    function exportMol(format) 
						    {
						     if(document.MSketch != null) 
						       {
						        var molfile = "";
						        document.forms[0].molfile.value = document.MSketch.getMol(format);
						        molfile = unix2local(molfile);
						       } else {
						        alert("Cannot import molecule:\n"+
						            "no JavaScript to Java communication in your browser.\n");
						       }
						    }
						    -->
						    </script>
						    </CENTER>
						</TD>												
					</TR>				
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<input class="submit" type="submit" name="Search" value="Search" onClick="exportMol('mol')" tabindex="7">&nbsp;&nbsp;&nbsp;
	<input class="submit" type="submit" value="Reset" tabindex="8"
		onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=Search'">
</form>
</center>
<%
if(request.getParameter("code1")!=null)
{ 
  %><hr><h3>The result of your search:</h3><%

  boolean check = false;
  int size = 0;
  
  String show_all = request.getParameter("showall");
  if(show_all == null)
    show_all = "no";
  
  if (value != null)//the search has been performed from the top frame!!
  {
  	check = search.isQuickSearchOK();
  }
  else
  {
	  /* Decide to perform either a new search or repeat a search from the session*/
	  if(stat != null && history != null)
	  { 
	    check = search.searchStatementOK();
	  }
	  else
	  {
	    check = search.searchOK();
	  }
	}
  
  //create a session element containing the latest search result.
  HttpSession statementSession = request.getSession(true);
  statementSession.setAttribute("sqlStatement", search.getStatement());
  
  if(search.getCountHit() > 10 && (!show_all.equalsIgnoreCase("yes") || show_all == null))
  {
    String order_by = request.getParameter("ord_by");
    size = 10;
    %> <img src="<%=Attributes.IMAGE_FOLDER%>/warning-bar.png"><br>
       This search result containes more than 10 hits. (Total number of hits: <%=search.getCountHit()%>)
      <a href="<%=Attributes.JSP_BASE%>?action=Search&history=true&code1=yes&showall=yes&ord_by=<%=order_by%>">SHOW ALL</a><br><br>
    <%
  }  
  else
  {
    size = search.result.size();
  }
  
  if(search.getExceedFrq()==true)
  {
      %><hr><h4>This search result may not be correct! You can only perform the amount of searches
      			pr. minute allowed by your licence 	when including structure search, 
                and this frequency has been exceded. Therefore any
                structures included in this search has been omitted.</h4><hr><br><%
  }

  if(check==true)
  { %>
    <table class="box" cellspacing="1" cellpadding="1" width="810px">
    <thead><tr> <th class="blue">No.</th>
                <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=Search&history=true&code1=yes&ord_by=c.chemical_name&showall=<%=show_all%>">Chemical Name</a></th>
                <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=Search&history=true&code1=yes&ord_by=c.cas_number&showall=<%=show_all%>">CAS number</a></th>
                <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=Search&history=true&code1=yes&ord_by=s.cd_formula&showall=<%=show_all%>">Formula</a></th>
                <th class="blue"><a class="res" href="<%= Attributes.JSP_BASE %>?action=Search&history=true&code1=yes&ord_by=s.cd_molweight&showall=<%=show_all%>">Molweight</a></th>
                <th class="blue"></th></tr> </thead>
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
          <form method="post" action="<%=Attributes.JSP_BASE%>?action=ResultPage">
          	<td><input class="submit_nowidth" type="submit" name="Select" value="Show Details">
          		<input type="hidden" name="id" value="<%= id %>"></td>
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
    <thead><tr> <th class="blue">No.</th>
                <th class="blue">Chemical Name</th>
                <th class="blue">CAS number</th>
                <th class="blue">Formula</th>
                <th class="blue">Molweigth</th></tr> </thead>
    <tbody>
       <tr align="center">
       <% if(search.getNoValues()==true)
          {%>
            <td colspan="7">No search criteria has been entered.<br>
                            Please enter a value in one of the above fields and try again.</td>
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
</body>
</html
>