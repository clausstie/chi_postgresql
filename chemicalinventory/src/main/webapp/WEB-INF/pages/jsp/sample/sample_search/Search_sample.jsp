<%@ page language="java" import="chemaxon.util.*, java.util.*, java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="search" class="chemicalinventory.search.SampleSearch" scope="page"/>
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
<script LANGUAGE="JavaScript">
function openWindow(url)
{
	window.open(url,"c","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width=800, height=600");

}

function getValue1(f){

  var val = "";
  ifrChecks = window.frames['list_frame1'].document.analysis.idArray;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)val += ", " + ifrChecks[i].value;
	  }  
	  
	  f.id_field1.value = (val)?val.substring(2):"";
  
	  //get the value of the text field
	  var text = window.frames['list_frame1'].document.analysis.criteria.value;
	  f.search_field1.value = text;
	  
   	  //get the value of the dropdown box
	  var opt = window.frames['list_frame1'].document.analysis.option.value;  
	  f.option1.value = opt;
  }
}

function getValue1a(f){

  var val = "";
  ifrChecks = window.frames['list_frame1'].document.analysis.type;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)
	    	val = ifrChecks[i].value;
	  }  
	  
	  f.type1.value = val;
  }
}

function getValue2(f){

  var val = "";
  ifrChecks = window.frames['list_frame2'].document.analysis.idArray;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)val += ", " + ifrChecks[i].value;
	  }  
	  
	  f.id_field2.value = (val)?val.substring(2):"";
	  
	  //get the value of the text field
	  var text = window.frames['list_frame2'].document.analysis.criteria.value;  
	  f.search_field2.value = text;
	  
  	  //get the value of the dropdown box
	  var opt = window.frames['list_frame2'].document.analysis.option.value;  
	  f.option2.value = opt;
  }
 }
  
  function getValue2a(f){

  var val = "";
  ifrChecks = window.frames['list_frame2'].document.analysis.type;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)
	    	val = ifrChecks[i].value;
	  }  
	  
	  f.type2.value = val;
  }
}

function getValue3(f){

  /*get the values of the checkboxes*/
  var val = "";
  ifrChecks = window.frames['list_frame3'].document.analysis.idArray;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)val += ", " + ifrChecks[i].value;
	  }  
	  
	  f.id_field3.value = (val)?val.substring(2):"";
	  
	  //get the value of the text field  
	  var text = window.frames['list_frame3'].document.analysis.criteria.value;
	  f.search_field3.value = text;
	  
   	  //get the value of the dropdown box
	  var opt = window.frames['list_frame3'].document.analysis.option.value;  
	  f.option3.value = opt;
 }
}

function getValue3a(f){

  var val = "";
  ifrChecks = window.frames['list_frame3'].document.analysis.type;
  
  if(ifrChecks != null)
  {
	  for(i=0;ifrChecks.length>i;i++){
	    if(ifrChecks[i].checked)
	    	val = ifrChecks[i].value;
	  }  
	  
	  f.type3.value = val;
  }
}

function sendForm(form)
{
	getValue1(form);
	getValue1a(form);
	getValue2(form);
	getValue2a(form);
	getValue3(form);
	getValue3a(form);
}

var ie4 = false; if(document.all) { ie4 = true; }
function getObject(id) { if (ie4) { return document.all[id]; } else { return document.getElementById(id); } }
function toggle(link, divId) { var lText = link.innerHTML; var d = getObject(divId);
 if (lText == '+') { link.innerHTML = '&#45;'; d.style.display = 'block'; }
 else { link.innerHTML = '+'; d.style.display = 'none'; } }

</script>
<title>
Search for samples and results.
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
  //Set the base to be used in the bean
  search.setBase(Attributes.JSP_BASE);
  String stat = null;
  String history = request.getParameter("history");
  String value = request.getParameter("value");
  if(history != null)
  {
    HttpSession statementSession = request.getSession();
    stat = (String) statementSession.getAttribute("sqlSample");
    search.setStatement(stat);
  }
%>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_sample_login.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.SAMPLE_BASE %>?action=create_sample">Compound Dependent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLE_BASE%>?action=create_sample_ind">Independent Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_sample">Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=unlock_sample">Un-Lock Sample</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=lock_result">Lock/Un-Lock Result</a> |
     <a class="adm" href="<%=Attributes.SAMPLEAPPROVER_BASE%>?action=modify_sample">Add/Remove Analysis</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=sample_search">Search</a> |
     <a class="adm" href="<%=Attributes.JSP_BASE%>?action=dis_sample">List samples</a> |
</span>
<span class="textboxadm">

<h2>Search for samples</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<form method="post" action="<%= Attributes.JSP_BASE %>?action=sample_search&code1=yes" name="search"  onSubmit="sendForm(this)">
<table border="0" width="100%">
	<tr>
		<td>
		<!-- Start table for structure and text search for compound part of the search. -->
		<TABLE class="box" cellpadding="0" cellspacing="1" width="785" bgcolor="#ffffff">
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
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('Search on the chemical formula. To get all chemicals containing C5 enter %C5% as search criteria. Only compounds with a structure will have a chemical formula registered.', LEFT, BORDER, 2, CAPTION, 'CHEMICAL FORMULA');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input class="w200" type="text" name="cd_formula" tabindex="2"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">CAS Number:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('If you know the CAS number of the compound in question, search for it in this field. Here you can again use sql notations and search for CAS number containing 345 with %345%.', LEFT, BORDER, 2, CAPTION, 'CAS NUMBER');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input type="text" class="w200" name="casNumber" tabindex="3"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Mol Weight:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('This field is a numbers only field. Here you can search for exact numbers or use < and > signs. Only compounds where a chemical structure is registered will have a mol weigth registrered.', LEFT, BORDER, 2, CAPTION, 'MOL WEIGHT');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input type="text" class="w200" name="cd_molweight" tabindex="4"></TD>
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Density:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('This field is a numbers only field. Here you can search for exact numbers or use < and > signs.', LEFT, BORDER, 2, CAPTION, 'DENSITY');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input type="text" class="w200" name="density" tabindex="5"></TD>		
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Sample Reg. Date:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('Search for the date the sample was created. The data format is YYYY-MM-DD ex. 2004-12-24. You can here use < and > to find compound registrered before or after a specific date. Entering a single date will only show the compounds registrered on that specific date.', ABOVE, BORDER, 2, CAPTION, 'REGISTRATION DATE');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
						<TD><input type="text" class="w200" name="register_date" tabindex="6"></TD>												
					</TR>
					<TR class="h48">
						<TH align="left" class="standard150">Sample Reg. By:</TH>
						<TD class="standard18">
							<a href="javascript:void(0);" onmouseover="return overlib('Select samples limited by a specific user who has created the sample. The initials of the person registring the sample is used here.', ABOVE, BORDER, 2, CAPTION, 'REGISTRERED BY');" onmouseout="return nd();"><img align="top" src="<%=Attributes.IMAGE_FOLDER%>/q_mark2.png" height="15" width="15" border="0"></a>
						</TD>
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
		
		<!--  Start analysis data for the search -->
	  </td>
	 </tr>
 	 <tr>
 		<td align="left">
 				<!-- Expandable Content box start -->
				<div style="border: 1px none #000000; padding: 1px; background: #FFFFFF; ">
					<table border="0" cellspacing="1" cellpadding="1" width="785" class="special" style="background: #E8E6EC; color: #000000; ">
						<tr>
							<th align="left">Select Analysis</th>
							<th align="right">[
								<a title="show/hide" id="exp1098695543_link" href="javascript: void(0);" onclick="toggle(this, 'exp1098695543');"  class="class=\\\\" style="text-decoration: none; color: #000000; ">&#45;</a>
								]</th>
						</tr>
					</table>
				<div id="exp1098695543" style="padding: 3px;">
					<table class="special" cellspacing="1" cellpadding="1" border="0" rules="rows" width="785" align="center">
					<tr>
						<th class="blue">Analysis Search 1</th>
					</tr>		
					<tr>
						<td align="center">
							<iframe id="list_frame1" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list_search" width="780" height="150" marginwidth="0" name="list_frame">
							</iframe>
							
							<input type="hidden" name="search_field1" value="">
							<input type="hidden" name="id_field1" value="">
							<input type="hidden" name="type1" value="">
							<input type="hidden" name="option1" value="">
						</td>
					</tr>
					<tr>
						<th class="blue">Analysis Search 2</th>
					</tr>		
					<tr>
						<td align="center">
							<iframe id="list_frame2" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list_search" width="780" height="150" marginwidth="0" name="list_frame">
							</iframe>
							
							<input type="hidden" name="search_field2" value="">
							<input type="hidden" name="id_field2" value="">
							<input type="hidden" name="type2" value="">						
							<input type="hidden" name="option2" value="">
						</td>
					</tr>
					<tr>
						<th class="blue">Analysis Search 3</th>
					</tr>		
					<tr>
						<td align="center">
							<iframe id="list_frame3" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=analysis_list_search" width="780" height="150" marginwidth="0" name="list_frame">
							</iframe>
							
							<input type="hidden" name="search_field3" value="">
							<input type="hidden" name="id_field3" value="">
							<input type="hidden" name="type3" value="">						
							<input type="hidden" name="option3" value="">
						</td>
					</tr>
				</table>
			</div>
	</div>
	<noscript>IF YOU SEE THIS TEXT IT IS STRONGLY ADVISED THAT YOU GET YOURSELF A NEW BROWSER.
				IMMIDIATELY STOP WORKING IN CI AND CONTACT YOUR ADMINISTRATOR</noscript>
	<script language="javascript">toggle(getObject('exp1098695543_link'), 'exp1098695543');</script>
							<!-- Expandable Content box end  -->

 		</td>
 	</tr>
  </table>
	<br/>
	<input class="submit" type="submit" name="Search" value="Search" onClick="exportMol('mol')" tabindex="7"/>&nbsp;&nbsp;&nbsp;
	<input class="submit" type="Reset" tabindex="8"/>
</form>

</center>


<%
//here the search is performed and result is processed into html display
if(request.getParameter("code1")!=null)
{     
	//perform the search
	search.performTheSearch();	
	
	//put the vectors into the session object to make them accessible from the iframe
	if(search.getCountHit() > 0)
	{
		session.setAttribute("compoundList", search.getResult());
	}
	else
	{
		session.removeAttribute("compoundList")	;
	}
	
	//display the search result
	Vector full_result = search.getSample_result();
	%>
	<hr>
	<h3>Result of the search for samples/result</h3>
	<%
	//check to see if the search frequency for structures has been exeeded, then show warning.
	if(search.isExceedFrq() == true)
  	{
      %><hr><h4>This search result may not be correct! You only have a license to perform
                3 searches pr. minute when including structure search, 
                and this frequency has been exceded. Therefore any
                structures included in this search has been omitted.</h4><hr><br><%
  	}
	%>
	<center>
	<%
	if(!full_result.isEmpty())
	{
		
		for ( int i = 0; i<full_result.size(); i++)
		{
			String entry = (String) full_result.get(i);
			%>	
				<table width="700" class="box">
					<%=entry%>
				</table><br><%		
		}
	}
	else
	{%>	
		<table width="700" class="box">
			<tr>
				<th class="blue">Information</th>
			<tr>
			<tr>
				<td><i>No samples/results found for the entered criteria(s).</i></td>
			</tr>
		</table><br><%			
	}
	%>
	</center>
	<br/>
	
	<hr>
	<h3>Below you see the compounds included in your search</h3>
	<%
	//Display an expandable box with all the compounds in the search result
	%>				<!-- Expandable Content box start -->
		<center>
			<div style="border: 1px none #000000; padding: 1px; background: #FFFFFF; ">
				<table border="0" cellspacing="1" cellpadding="1" width="700" class="class=special" style="background: #E8E6EC; color: #000000; ">
					<tr>
						<th align="left">Display list of compounds in search result</th>
						<th align="right">[
							<a title="show/hide" id="exp2098695543_link" href="javascript: void(0);" onclick="toggle(this, 'exp2098695543');"  class="class=\\\\" style="text-decoration: none; color: #000000; ">&#45;</a>
							]</th>
					</tr>
				</table>
			<div id="exp2098695543" style="padding: 3px;">
				<table class="special" cellspacing="1" cellpadding="1" border="0" rules="rows" width="700" align="center">
					<tr>
						<td align="center">
							<iframe id="compound_frame" frameborder="0" scrolling="yes" src="<%= Attributes.JSP_BASE %>?action=compound_list" width="700" height="150" marginwidth="0" name="list_frame">
							</iframe>
						</td> 
					</tr>
				</table>
			</div>
		</center>
	<noscript>IF YOU SEE THIS TEXT IT IS STRONGLY ADVISED THAT YOU GET YOURSELF A NEW BROWSER.
				IMMIDIATELY STOP WORKING IN CI AND CONTACT YOUR ADMINISTRATOR</noscript>
	<script language="javascript">toggle(getObject('exp2098695543_link'), 'exp2098695543');</script>
							<!-- Expandable Content box end  -->
	
<%
}%>
</span>
<jsp:include page="/text/sample_nav_bar.jsp"/>
</body>
</html>