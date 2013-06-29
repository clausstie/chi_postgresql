<%@ page language="java" import="chemaxon.util.*" import="java.util.*" import="java.net.*" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="search" class="chemicalinventory.beans.SearchBean" scope="page"/>
<jsp:setProperty name="search" property="*"/>
<jsp:useBean id="result" class="chemicalinventory.beans.ResultBean" scope="page"/>
<jsp:setProperty name="result" property="*"/>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyCompoundBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<title>
The main page for chemical administration
</title>
<SCRIPT LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.chemical_name.value) == "") 
 {
  alert("Please fill in a valid name for the compound!");
  form.chemical_name.focus();
  return false;
 }
 if(trim(form.density.value) != "")
 {
 		if(isPositiveInteger(form.density.value)==false)
    {
        alert("Please fill a valid density, this is a numbers only field using '.' as decimal seperator");
        form.density.focus();
        return false;
    }
 }
 if(trim(form.cas_number.value) != "")
 {
     if(isValidCas(form.cas_number.value)==false)
     {
          alert("Please fill a valid CAS number using only numbers and '-' as value");    
          form.cas_number.focus();
          return false;
     }
 }
return true;
}
</SCRIPT>

</head>
<%
if (request.getParameter("code2")!=null)
{%>
<body>
<%
}
else
{%>
<body onload="document.modify.chemicalName.focus()">
<%}%>
<%
  String history = request.getParameter("history");
  String stat = request.getParameter("statement");
  String user = request.getRemoteUser();
%>
<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_compound.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">Register a new chemical</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical">Modify a exsisting chemical</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=create_com_resource">Create resource</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify">Modify resource</a> |

</span>
<span class="textboxadm">
<center>
<%if(request.getParameter("code2")==null)
{%>
</center>
    <h2>Enter the chemical name/CAS for the compund to edit</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>

<form action="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&code1=yes" method="post" name="modify">
		<TABLE class="box" cellpadding="1" cellspacing="2" width="720">
			<TR><TH colspan="4" class="blue">Compound</TH></TR>
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

if(request.getParameter("code1")!=null)
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
      <a href="<%= Attributes.ADMINISTRATOR_BASE %>?action=Chemical&history=true&code1=yes&showall=yes&statement=<%= statement%>&ord_by=<%=order_by%>">SHOW ALL</a><br><br>
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
                 <th class="blue"><a class="res" href="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&history=true&code1=yes&ord_by=c.chemical_name&statement=<%= statement%>&showall=<%=show_all%>">Chemical Name</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&history=true&code1=yes&ord_by=c.cas_number&statement=<%= statement%>&showall=<%=show_all%>">CAS number</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&history=true&code1=yes&ord_by=s.cd_formula&statement=<%= statement%>&showall=<%=show_all%>">Formula</a></th>
                 <th class="blue"><a class="res" href="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&history=true&code1=yes&ord_by=s.cd_molweight&statement=<%= statement%>&showall=<%=show_all%>">Molweight</a></th>
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
	        <form method="post" action="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical&code2=yes">
	          <td>
	          	<input class="submit_nowidth" type="submit" name="Select" value="Show Details">
	          	<input type="hidden" name="id" value="<%= id %>">
	          	<input type="hidden" name="statement" value="<%=statement%>">
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
}%>
<%
if(request.getParameter("code2")!=null)
{ 
    result.result();
  %>
  </center>
<h2>Compound to modify: <br/><%= URLDecoder.decode(result.getChemical_name().toUpperCase(), "UTF-8") %></h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<form action="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=Chemical&code3=yes" method="post" onSubmit="return validateForm(this)">
<TABLE class="box" cellpadding="0" cellspacing="1" width="795" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Compound</TH></TR>
		<TR>
		<!-- Side for text input -->
			<TD>
				<TABLE width="375" cellpadding="0" cellspacing="0" border="1" rules="rows" bordercolor="white">
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Name:</TH>
						<TD><input type="text" class="w200" name="chemical_name" value="<%= URLDecoder.decode(result.getChemical_name(), "UTF-8")%>"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD><input type="text" class="w200" name="cas_number" value="<%= result.getCas_number() %>"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Density:</TH>
						<TD><input type="text" class="w200" name="density" value="<%= result.getDensity() %>"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Mol Weight:</TH>
						<TD><%= result.getMolWeight()%>
							<input type="hidden" name="mol_weight" value="<%= result.getMolWeight()%>"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Chemical Formula:</TH>
						<TD><%= result.getChemicalFormula()%>
							<input type="hidden" name="chemical_formula" value="<%= result.getChemicalFormula()%>"></TD>		
					</TR>
					
					<TR class="h48">
						<TH align="left" class="standard150">Register Date:</TH>
						<TD><%=Util.getDate()%>
							<input type="hidden" name="register_date" value="<%=Util.getDate()%>"></TD>
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Registered By:</TH>
						<TD><%=user.toUpperCase()%>
							<input type="hidden" name="register_user" value="<%=user%>"></TD>
					</TR>
					<TR class="h54">
						<TH align="left" class="standard150">Remark:</TH>
						<TD>
				        	<textarea name="remark" style="width: 200;" rows="4" type="_moz"><%if(result.getRemark()!= null){%><%=result.getRemark()%><%}else{%>No comments.<% }%></textarea>
				            <input type="hidden" name="id" value="<%=result.getId()%>">
				            <input type="hidden" name="user" value="<%=user%>">
	                        <input type="hidden" name="molfile">

				            <!--original values for the various fields. -->
					        <input type="hidden" name="o_name" value="<%= URLDecoder.decode(result.getChemical_name(), "UTF-8")%>"\>
					        <input type="hidden" name="o_density" value="<%= result.getDensity() %>"\> 
					        <input type="hidden" name="o_cas" value="<%= result.getCas_number() %>"\>
					        <input type="hidden" name="o_formula" value="<%= result.getChemicalFormula()%>"\>
					        <input type="hidden" name="o_mw" value="<%= result.getMolWeight()%>">
				   	        <input type="hidden" name="o_remark" value="<%=result.getRemark()%>">				            
						</TD>	
					</TR>
				</TABLE>
			</TD>
		<!-- Side for structure input -->
			<TD>
				<TABLE width="420" cellpadding="0" cellspacing="1" border="0">
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
					        msketch_begin("<%=Attributes.MARVIN_FOLDER%>", 420, 384); // arguments: CODEBASE, WIDTH, HEIGHT
					        <%if(!result.getDbMolfile().equals("0"))
					        {%>
					        msketch_param("mol", "<%= HTMLTools.convertForJavaScript(result.getDbMolfile())%>");
					        <%
					        }%>
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
		<tr>
			<td colspan="5">
				<table border="0" width="100%">
					<tr>
						<th align="center" class="blue">Reason For Change</th>
					</tr>
					<tr>
						<td align="center">
							<TEXTAREA name="reason_for_change" style="width: 795;" rows="4"></TEXTAREA>	
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</TABLE>
   <br>
   <input class="submit" type="submit" name="Update" value="Update" onClick="exportMol('mol')" tabindex="7">&nbsp;&nbsp;&nbsp;
   <input class="submit" type="Reset" tabindex="8">
</form>
<%
}
%>
<%
if(request.getParameter("code3")!=null)
{ 
    modify.modifyCompound();
    if(modify.checkOK == false)
    {
%>
    <h3>An error orcurred, update not performed....!! </h3>
    <%if(modify.exist == true)
      {
        %><h5>The structure drawn allready exists for a different compound.</h5><%
      }%>
    <p>Go to the compound an try again <a href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical&code2=yes&id=<%= modify.getId()%>">Click Here.</a><%
    }
    else
    {
%>
     <h3>Update performed succesfully. <a href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical&code2=yes&id=<%= modify.getId()%>">View Result.</a> </h3><%
    }
}
%>
<center>
</span>
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