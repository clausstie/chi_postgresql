<%@ page language="java" import="java.text.*" import="java.util.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="chemical" class="chemicalinventory.beans.ChemicalRegBean" scope="page"/>
<jsp:setProperty name="chemical" property="*"/>
<html>
<head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              neded to manage a chemical storage, based on the java technology.
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
Register Chemical / Compound
</title>
<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.chemicalName.value) == "") 
 {
  alert("Please fill in a valid chemical name!");
  form.chemicalName.focus();
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
 if(trim(form.casNumber.value) != "")
 {
     if(isValidCas(form.casNumber.value)==false)
     {
          alert("Please fill a valid CAS number, using only numbers and '-' as value");    
          form.casNumber.focus();
          return false;
     }
 }
 return true;
}
</script>
</head>
<%
if (request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("errorcode1")==null) 
{%>
<body onload="f=setInterval('document.registration.chemicalName.focus()',1000);" onkeydown="clearInterval(f);" onkeypress="clearInterval(f);" onClick="clearInterval(f);">
<%
}
else
{%>
<body>
<%
}

String user = request.getRemoteUser();
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_compound.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
   | <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=new_Chemical">Register a new chemical</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=Chemical">Modify an exsisting chemical</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=create_com_resource">Create resource</a> |
     <a class="adm" href="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=resource_modify">Modify resource</a> |     
</span>
<span class="textboxadm">
<center>
<%
Locale UKlocale = new Locale("en", "GB");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", UKlocale);
Date today = new Date();
String result = formatter.format(today);

if (request.getParameter("code1")!=null) 
{
  chemical.setUser(user);
  boolean check = chemical.regOK();
  String id = chemical.getId();
  if(check)
  {
    response.sendRedirect(Attributes.COMPOUND_ADMINISTRATOR_BASE+"?action=new_Chemical&code2=yes&id="+id);
  }
  else
  {
    response.sendRedirect(Attributes.COMPOUND_ADMINISTRATOR_BASE+"?action=new_Chemical&errorcode1=yes");
  }
}

if (request.getParameter("code2")!=null) 
{
  String id = chemical.getId();
  chemical.compoundReceipt(id);

%>
	</center>
	<H2>Register Chemical/Compound - Status</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<center>
<%
  if(chemical.result.isEmpty())
  {
  %>
    <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
    <thead><tr> <th class="blue">Chemical Name:</th>
                <th class="blue">CAS number:</th>
                <th class="blue">Remark:</th>
                <th class="blue">Density:</th>
                <th class="blue">Date:</th>
                <th class="blue">Register By:</th>
                </tr> </thead>
    <tbody>
       <tr align="center">
       <td colspan="9">ERROR no new compound added...</td>
       </tr>
    </tbody>
    </table>
  <%
  }
  else
  {
  %><h3>One new compound has been added to the system</h3><%
  %>
   <table class="box" cellspacing="1" cellpadding="1" width="100%" align="center">
       <thead><tr> <th class="blue">Chemical Name:</th>
                   <th class="blue">CAS number:</th>
                   <th class="blue">Remark:</th>
                   <th class="blue">Density:</th>
                   <th class="blue">Date:</th>
                   <th class="blue">Register By:</th>
                   </tr> </thead>
    <tbody>
    <%
       for(int i=0; i<chemical.result.size(); i++)
       {
       %><tr><%
          String data = (String) chemical.result.elementAt(i);
          StringTokenizer tokens = new StringTokenizer(data, "|", false);
          while(tokens.hasMoreElements())
          {
            String token = tokens.nextToken();
            token.trim();
            %><td align="center">
                  <%              
                    out.println(URLDecoder.decode(token, "UTF-8"));
                  %>
              </td>
        <%}%>
        </tr>
     <%}%>
   </tbody>
   </table>
    <form method="post" action="<%=Attributes.COMPOUND_ADMINISTRATOR_BASE%>?action=newContainer_reg&new=yes">
        <input type="hidden" name="compound_id" value="<%= chemical.getId() %>">
        <input type="hidden" name="chemical_name" value="<%= chemical.getChemicalName() %>">
        <input class="submit_nowidth" type="submit" name="RegisterContainer" value="Register a new container of this compound">
    </form>
   <br>
<%
  }
}  

if (request.getParameter("errorcode1")!=null) 
{
  %>
  Creation of new compound failed. Please try again.
  <BR><HR>
  <%        
}%>
<%
if (request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("errorcode1")==null) 
{%>
</center>
<H2>Register Chemical / Compound </H2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<form method="post" action="<%= Attributes.COMPOUND_ADMINISTRATOR_BASE %>?action=new_Chemical&code1=yes" name="registration" onsubmit="return validateForm(this)">
	<TABLE class="box" cellpadding="0" cellspacing="1" width="785" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Search</TH></TR>
		<TR>
		<!-- Side for text input -->
			<TD>
				<TABLE width="355" cellpadding="0" cellspacing="0" border="1" rules="rows" bordercolor="white">
					<TR class="h54">
						<TH align="left" class="standard150">Chemical Name:</TH>
						<TD><input class="w200" type="text" name="chemicalName"></TD>		
					</TR>
					<TR class="h54">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD><input type="text" class="w200" name="casNumber"></TD>												
					</TR>
					<TR class="h54">
						<TH align="left" class="standard150">Density:</TH>
						<TD><input type="text" class="w200" name="density"></TD>		
					</TR>
					<TR class="h54">
						<TH align="left" class="standard150">Register Date:</TH>
						<TD><%=Util.getDate()%>
							<input type="hidden" name="registerDate" value="<%=chemicalinventory.utility.Util.getDate()%>"></TD>
					</TR>
					<TR class="h54">
						<TH align="left" class="standard150">Registered By:</TH>
						<TD><%=user.toUpperCase()%>
							<input type="hidden" name="registerUser" value="<%=user%>"></TD>
					</TR>
					<TR class="h108">
						<TH align="left" class="standard150">Remark:</TH>
						<TD>
			            	<textarea name="remark" style="width: 200;" rows="6"></textarea>
							<input type="hidden" name="molfile">
						</TD>	
					</TR>
				</TABLE>
			</TD>
		<!-- Side for structure input -->
			<TD>
				<TABLE width="440" cellpadding="0" cellspacing="1" border="0">
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
						    msketch_begin("<%=Attributes.MARVIN_FOLDER%>", 440, 384); // arguments: CODEBASE, WIDTH, HEIGHT
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
	<br>
    <input class="submit" type="submit" name="Submit" value="Submit" tabindex="5" onClick="exportMol('mol')">&nbsp;&nbsp;&nbsp;
    <input class="submit" type="Reset" tabindex="6">
  </form>
<%
 }%>
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
