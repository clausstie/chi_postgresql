<%@ page language="java" import="java.text.*" import="java.util.*" import="java.io.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="container" class="chemicalinventory.beans.ContainerRegBean" scope="page"/>
<jsp:setProperty name="container" property="*"/>
<jsp:useBean id="reg" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="group" class="chemicalinventory.groups.User_group"/>
<jsp:useBean id="supplier" class="chemicalinventory.beans.supplierBean" scope="page"/>
<jsp:useBean id="location" class="chemicalinventory.beans.locationBean" scope="page"/>
<%@page import="chemicalinventory.utility.Return_codes"%>
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

<script language="JavaScript" src="<%=Attributes.APPLICATION%>/script/overlib.js"></script>
<script language="JavaScript" src="<%=Attributes.APPLICATION%>/script/inventoryScript.js"></script>
<script language="JavaScript" src="<%=Attributes.APPLICATION%>/script/calendar1.js"></script>

<title>
This page is for adding a new container.
</title> 

<SCRIPT LANGUAGE="JavaScript">
// Begin
<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null &&
	request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null && request.getParameter("errorcode4") == null &&
	request.getParameter("labelPrint") == null)
{%>
var arrItems1 = new Array();
var arrItemsGrp1 = new Array();
var arrItemsLocID1 = new Array();

<%  location.showAllLocations();
    for(int a=0; a<location.l1_id.size(); a++)
    {
       String id = (String) location.l1_id.elementAt(a);
       String loc_id = (String) location.l1_loc_id.elementAt(a);
       String location_name = (String) location.l1_name.elementAt(a);
       %>
        arrItemsLocID1[<%= a %>] = "<%= loc_id %>";
        arrItems1[<%= a %>] = "<%=Util.encodeTag(location_name) %>";
        arrItemsGrp1[<%= a %>] = "<%= id %>";
       <%
    }  
%>

var arrItems2 = new Array();
var arrItemsGrp2 = new Array();
var arrItemsLocID2 = new Array();

<%  
    for(int j=0; j<location.l2_id.size(); j++)
    {
       String id1 = (String) location.l2_id.elementAt(j);
       String loc_id1 = (String) location.l2_loc_id.elementAt(j);
       String location_name1 = (String) location.l2_name.elementAt(j);
       %>
        arrItemsLocID2[<%= j %>] = "<%= loc_id1 %>";
        arrItems2[<%= j %>] = "<%= Util.encodeTag(location_name1) %>";
        arrItemsGrp2[<%= j %>] = "<%= id1 %>";
       <%
    }
%>

function selectChange(control, controlToPopulate, ItemArray, GroupArray)
{
  var myEle ;
  var x ;
  // Empty the second drop down box of any choices
  for (var q=controlToPopulate.options.length;q>=0;q--) controlToPopulate.options[q]=null;
  if (control.name == "firstChoice") 
  {
      // Empty the third drop down box of any choices
      for (var q=control.form.thirdChoice.options.length;q>=0;q--) control.form.thirdChoice.options[q] = null;
  }
  // ADD Default Choice - in case there are no values
  myEle = document.createElement("option") ;
  myEle.value = 0 ;
  myEle.text = "[SELECT]" ;
    try {
    /* this will work for IE, Gecko will throw an Exception... */
      controlToPopulate.add(myEle);
    }
    catch (ex) {
    /* ... catch it and do it the right way */
        controlToPopulate.add(myEle, null);
    }
  // Now loop through the array of individual items
  // Any containing the same child id are added to
  // the second dropdown box
  for ( x = 0; x < ItemArray.length; x++ )
    {
      if(control.name == "firstChoice")
      {
        if (arrItemsLocID1[x] == control.value )
          {
            myEle = document.createElement("option") ;
            myEle.value = GroupArray[x];
            myEle.text = ItemArray[x] ;
            try {
              /* this will work for IE, Gecko will throw an Exception... */
              controlToPopulate.add(myEle);
            }
            catch (ex) {
              /* ... catch it and do it the right way */
              controlToPopulate.add(myEle, null);
            }
          }
      }

      if(control.name == "secondChoice")
      {
        if (arrItemsLocID2[x] == control.value )
          {
            myEle = document.createElement("option") ;
            myEle.value = GroupArray[x];
            myEle.text = ItemArray[x] ;
            try {
              /* this will work for IE, Gecko will throw an Exception... */
              controlToPopulate.add(myEle);
            }
            catch (ex) {
              /* ... catch it and do it the right way */
              controlToPopulate.add(myEle, null);
            }
          }
      }
    }
}
//  End
function validateForm(form) 
{
 if (trim(form.quantity.value) == "") 
 {
  alert("Please fill in a valid quantity!");
  form.quantity.focus();
  return false;
 }
 else
 {
   if(isPositiveInteger(form.quantity.value)==false)
   {
    alert("The quantity you entered is not valid. please enter a number only and use '.' as decimal seperator");
    form.quantity.focus();
    return false;
   }
 }
 if (trim(form.tara_Weight.value) == "") 
 {
  alert("Please fill in a valid tara weight!");
  form.tara_Weight.focus();
  return false;
 }
 else
 {
   if(isPositiveInteger(form.tara_Weight.value)==false)
   {
    alert("The tara weight you entered is not valid. please enter a number only and use '.' as decimal seperator");
    form.tara_Weight.focus();
    return false;
   }
 }
 if(isPositiveInteger(form.no_containers.value)==false)
 {
   alert("Please select a valid number of containers to create (Minimum: 1. Maximum: 100)");
   form.no_containers.value = 1;
   form.no_containers.focus();
   return false;
 }

return true;
}
<%
}
%>
</script>
</head>
<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null &&
	request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null && request.getParameter("errorcode4") == null &&
	request.getParameter("labelPrint") == null && request.getParameter("errorcode5") == null)
{%>
<body>
<%
}
else
{
%>
<body>
<%
}
%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:100;"></div>

<%
  Locale UKlocale = new Locale("en", "GB");
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", UKlocale);
  Date today = new Date();
  String chemical_name = "";
  String dato = formatter.format(today);
  String user = request.getRemoteUser();
  String new_compound = request.getParameter("new");
  String useragent = request.getHeader("User-Agent");
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

<%
if(request.getParameter("code1") == null && request.getParameter("code2") == null && request.getParameter("code3") == null &&
	request.getParameter("errorcode2") == null && request.getParameter("errorcode3") == null && request.getParameter("errorcode4") == null &&
	request.getParameter("labelPrint") == null && request.getParameter("errorcode5") == null)
{
    String compound_id = String.valueOf(container.getCompound_id());
	chemical_name = Util.getChemicalName(compound_id);
	container.setChemical_name(chemical_name);
  %>
  <H2>Register a new container of <br/><%=container.getChemical_name()%></H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<center>

  <form name="locationChoices" method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=newContainer_reg&code1=yes" onSubmit="return validateForm(this)">
		<TABLE class="box" cellpadding="1" cellspacing="2" width="560">
	      <TR><TH colspan="3" class="blue">Container</TH></TR>
	      <%if(Attributes.USE_CUSTOM_ID) { %>
	      <tr>
	  			<th align="left" style="width: 200px;">Custom Id:</th>
	  			<td><input type="text" name="custom_id" class="w200"></td>
	  	  </tr>
	  		<%
	  	  }
	  	  %>
		  <tr>
		      <th align="left" class="standard">Chemical Name</th>
		      <td><%=container.getChemical_name().toUpperCase()%>
		      	<input type="hidden" name="name" value="<%=container.getChemical_name().toUpperCase()%>">
		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Home Location:</th>
		      <td>
			      <select id=firstChoice name=firstChoice onchange="selectChange(this, this.form.secondChoice, arrItems1, arrItemsGrp1);">
				      <option value="X" selected>[SELECT]</option>
		      <%  
		          location.showAllLocations();
		          for(i=0; i<location.l0_id.size(); i++)
		          {
		             String l_id = (String) location.l0_id.elementAt(i);
		             String l_name = (String) location.l0_name.elementAt(i);
		             %>
		              <option value="<%= l_id %>"><%= Util.encodeTag(URLDecoder.decode(l_name, "UTF-8")) %></option>
		             <%
		          }
		      %>
			      </select>
			      <select id=secondChoice name=secondChoice onchange="selectChange(this, this.form.thirdChoice, arrItems2, arrItemsGrp2);">
				      <option>[SELECT]</option>
		      	  </select>
			      <select id=thirdChoice name=thirdChoice></select>
		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Supplier:</th>
		      <td><select name="supplierID" style="width: 200px">
		      <%  
		          supplier.getSuppliers();
		          for(i=0; i<supplier.supplier_id.size(); i++)
		          {
		             String supplierID = (String) supplier.supplier_id.elementAt(i);
		             String supplier_name = (String) supplier.supplier.elementAt(i);
		             %>
		              <option value="<%= supplierID %>"><%= Util.encodeTag(URLDecoder.decode(supplier_name, "UTF-8")) %></option>
		             <%
		          }
		      %>
		     	 </select>
		      </td>
		  </tr>		  
		  <tr>
		      <th align="left" class="standard">Quantity:</th>
		      <td><input type="text" name="quantity" class="w200"></td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Unit:</th>
		      <td colspan="2">
		      	<select name="unit" size="1"style="width: 200px">
		          <option value="g" selected>Gram (g)</option>
		          <option value="mg">Milli Gram (mg)</option>
		          <option value="l">Liter (l)</option>
		          <option value="ml">Milli liter (ml)</option>
		        </select>
		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Tare Weight:</th>
		      <td><input type="text" name="tara_Weight" class="w200"></td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">No of Containers:</th>
		      <td>
			      <select name="no_containers" style="width: 200px">
   			      		<option value="1" selected="selected">1</option>
			      	<%
			      	for (int n = 2; n<=100; n++)
			      	{
			      		%>
			      		<option value="<%=n%>"><%=n%></option>
			      		<%
			      	}
			      	%>
			      </select>
		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Owner(s):</th>
		      <td>
		      <select name="owner_1" style="width: 197px">
		            <option value="X">[SELECT]</option>
		      <%  
		          reg.userName_used();
		          Vector userVec = reg.getUn_taken();
		          for(i=0; i<userVec.size(); i++)
		          {
		             String user_name = (String) userVec.elementAt(i);
		             %>
		              <option value="<%=user_name%>"><%=user_name%></option>
		             <%
		          }
		      %>
		      </select>
		      <select name="owner_2" style="width: 197px">
		            <option value="X">[SELECT]</option>
		      <%  
		          reg.userName_used();
		          for(i=0; i<userVec.size(); i++)
		          {
		             String user_name = (String) userVec.elementAt(i);
		             %>
		              <option value="<%=user_name%>"><%=user_name%></option>
		             <%
		          }
		      %>
		      </select>
		      </td>
		  </tr>
		  <tr valign="middle">
		      <th align="left" class="standard">Procurement Date:</th>
		      <td>
		     	 <input type="text" name="procurement_date" class="w200" readonly="readonly">
		     	 <a href="javascript:cal1.popup();"><img src="<%=Attributes.IMAGE_FOLDER%>/cal.gif" border="0"></a>
		     	 <a href="#" onclick="locationChoices.procurement_date.value = '';" title="Remove the procurement date."><img src="<%=Attributes.IMAGE_FOLDER%>/nook-mark.gif" border="0"></a><br>
		      </td>
		  </tr>
		  <tr valign="middle">
		      <th align="left" class="standard">Expiry Date:</th>
		      <td>
		     	 <input type="text" name="expiry_date" class="w200" readonly="readonly">
		     	 <a href="javascript:cal2.popup();"><img src="<%=Attributes.IMAGE_FOLDER%>/cal.gif" border="0"></a>
		     	 <a href="#" onclick="locationChoices.expiry_date.value = '';" title="Remove the expiry date."><img src="<%=Attributes.IMAGE_FOLDER%>/nook-mark.gif" border="0"></a>
		     	 <br>
		      </td>
		  </tr>		  
		  <tr>
		      <th align="left" class="standard">User groups</th>
		      <td>
		     <%
		          group.find_groups(13);
		          Vector groups = group.getAll_groups();
		          String tag = null;
		          for (i=0; i<groups.size(); ++i)
		          {
		              tag = (String) groups.elementAt(i);
		              out.println(tag);%><br><%
		          }
		        %>
   		          <input type="hidden" name="compound_id"  value="<%= container.getCompound_id()%>">
		          <input type="hidden" name="chemical_name"  value="<%= container.getChemical_name()%>">
   		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Register Date:</th>
		      <td><%= dato %>
		      	<input type="hidden" name="registerDate" value="<%= dato %>"></td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Registered By:</th>
		      <td><%= user.toUpperCase() %>
		      	<input type="hidden" name="registerUser"value="<%= user.toUpperCase() %>">
		      </td>
		  </tr>
		  <tr>
		      <th align="left" class="standard">Remarks:</th>
		      <td><textarea name="remark" style="width=400" rows="4"></textarea></td>
		  </tr>
	 </table><br>
	<input class="submit" type="submit" name="Submit" value="Submit">&nbsp;&nbsp;&nbsp;
	<input class="submit" type="Reset">&nbsp;&nbsp;&nbsp;
</form>

<script language="JavaScript">
<!-- // create calendar object(s) just after form tag closed
	 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
	 // note: you can have as many calendar objects as you need for your application
	var cal1 = new calendar1(document.forms['locationChoices'].elements['procurement_date']);
	cal1.year_scroll = true;
	cal1.time_comp = false;
	
	var cal2 = new calendar1(document.forms['locationChoices'].elements['expiry_date']);
	cal2.year_scroll = true;
	cal2.time_comp = false;
//-->
</script>

</center>
<span id="displayArea"></span>
<%}

if (request.getParameter("errorcode1")!=null) 
  {
    String c_id = request.getParameter("compound_id");
    %>
    Creation of new cotainer failed. Please try again.
    <BR>
    <%        
  }

if(request.getParameter("code1")!=null) 
  {
    /*Create a string holding all the user groups selected
     * if all is selected this will be the only value sendt to
     * the bean. otherwise the string shall be made up of
     * 1, 2, 3.... etc*/
    String[] groups = request.getParameterValues("groups");
    if (groups != null)
    {
       String gr = null;
       
         for(i = 0; i < groups.length; i++)
        {
         if(i == 0)
          gr = groups[i];
         else
          gr = gr + "," + groups[i]; 
        }
        container.setGroup(gr);
    }

    /*insert all the values in appropriate tables...*/
    boolean containerCheck = container.registerMultipleContainers();
	
    String compound_id = String.valueOf(container.getCompound_id());
    String name = container.getChemical_name();
    int status = container.getStatus();

    if(containerCheck)
    {
	  int no_created = container.getI();
	  Vector list_of_containers = container.getList_of_new();
	  String the_list = "";
	  
	  //Create comma separeted list 
	  for(int t = 0; t<list_of_containers.size(); t++)
	  {
	  	if(the_list.equals(""))
	  		the_list = (String) list_of_containers.get(t);
	  	else
	  	 	the_list = the_list + "," + (String) list_of_containers.get(t);
	  }
    
//      String id = container.getCainer_id();
      String tc = container.getThirdChoice();
      
      response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=newContainer_reg&code2=yes&thirdChoice="+tc+"&compound_id="+compound_id+"&new="+new_compound+"&list="+the_list);
    }
    else
    { 
    	if(status == 1)//Error in selected locations
    	{
    		response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=newContainer_reg&errorcode2=yes"+"&container_id="+container.getContainer_id()+"&compound_id="+compound_id+"&new="+new_compound);
    	}
    	else if(status == 2)//error in number of containers to create.
    	{
    		response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=newContainer_reg&errorcode3=yes"+"&container_id="+container.getContainer_id()+"&compound_id="+compound_id+"&new="+new_compound);
    	}
    	else if(status == Return_codes.CUSTOM_ID_NOT_UNIQUE)//error in number of containers to create.
    	{
    		response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=newContainer_reg&errorcode5=yes"+"&container_id="+container.getContainer_id()+"&compound_id="+compound_id+"&new="+new_compound);
    	}
    	else //another error orcurred.
    	{
    		response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=newContainer_reg&errorcode4=yes"+"&container_id="+container.getContainer_id()+"&compound_id="+compound_id+"&new="+new_compound);
    	}
    }
  }
 	 
//show a receipt for the creation of the container.
if (request.getParameter("code2") != null) 
{
  String id = request.getParameter("container_id");
  String tc = request.getParameter("thirdChoice");
  String c_id = request.getParameter("compound_id");
  String list_of_new = container.getList();
    
  boolean check = container.containerReceipt_multiple(list_of_new);
  
  if(check)
  {
  	//container(s) registred ok...
  	Vector list = container.getList_of_new();
  	check = true;
  	%>
  	<H2>Register Container Receipt</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<center>
  	<form method="post" action="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=printLabel" target="blank">
	  	<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center" colspan="2">General Container Data</th>
	  		</tr>
	  	
	  		<tr>
	  			<th align="left" style="width: 200px;">Compound</th>
	  			<td><%=URLDecoder.decode(container.getChemical_name(), "UTF-8")%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" style="width: 200px;">Location</th>
	  			<td><%=container.getLocation()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" style="width: 200px;">Quantity</th>
	  			<td><%=container.getQuantity()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" style="width: 200px;">Unit</th>
	  			<td><%=container.getUnit()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" style="width: 200px;">Tara Weight</th>
	  			<td><%=container.getTara_Weight()%>&nbsp;gram</td>
	  		</tr>
	  		<tr>
	  			<th align="left" style="width: 200px;">Owner(s)</th>
	  			<td><%=container.getOwner_1()%></td>
	  		</tr>
	  		<tr>
		  		<th align="left" style="width: 200px;">Procurement Date</th>
	  			<td><%=container.getProcurement_date()%></td>
		  	</tr>
		 	<tr>
		   		<th align="left" style="width: 200px;">Expiry Date</th>
	  			<td><%=container.getExpiry_date()%></td>
			</tr>		  	  		
	  		<tr>
	  			<th align="left" style="width: 200px;">Group(s)</th>
	  			<td><%=container.getGroup()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" style="width: 200px;">Registered By</th>
	  			<td><%=container.getRegisterUser()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" style="width: 200px;">Registered Date</th>
	  			<td><%=container.getRegisterDate()%></td>
	  		</tr>
	  		<tr>
	           <th align="left">Remark:</th>
	           <td>
	           	<%= Util.encodeTagAndNull(container.getRemark())%>
	           </td>
	        </tr>	  
	  		<tr>
	  			<th align="left" style="width: 200px;"># Registered:</th>
	  			<td><%=list.size()%></td>
	  		</tr>
	  		<tr>
	  			<th class="blue" align="center" colspan="2">Container Id's Registered</th>
	  		</tr>
	   		<tr>
	  			<th class="blue">Id</th>
	  			<th class="blue">Print</th>
	  		</tr>
	  		<%
	  		for (int t=0; t<list.size(); t++)
	  		{
	  			String con_id = (String) list.get(t);
	%>
	  		<tr>
	  			<td align="center"><%=con_id%></td>
	  			<td><input type="checkbox" checked="checked" value="<%=con_id%>" name="label"/></td>
	  		</tr>
	<%  			
	  		}
	  		%>  	
	  	</table><br>
	<%
		  if(useragent.indexOf("MSIE") != -1)
	      {
	    %>
		  	<input class="submit_width125" type="submit" value="Print label(s)" name="print">
		  	<%
		  }
		  else
		  {
		  	%>
		  	<H2>Register Container Receipt</H2>
			<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
			<br/>
		  	<h4>..Sorry, only label print from Internet Explorer.</h4>
		  	<%
		  }
	  %>
	  </form>
	 </center>
  	<%
  }
  else
  {
  	//no containers registeret...
  	%>
  	<h3>Container could not be created, please try again.</h3>
  	<%
  }
  %>
  <br/>
<FIELDSET>
 <LEGEND>ACTION</LEGEND>
   <table class="action_noheader" width="650px">
	<tr>
		<td>
	  <%if(!check)
  		{     
  			String name = Util.getChemicalName(c_id);
  			%>
    	 	<a href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer_reg&compound_id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Register a new container of the compound <%=name%>', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
     	 	<a href="<%=Attributes.JSP_BASE%>?action=ResultPage&id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Goto detailed information on <%=name%>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
     	 <%
     	}
     	else
     	{%>
	   		<a href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer_reg&compound_id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Register a new container of the compound <%=URLDecoder.decode(container.getChemical_name(), "UTF-8")%>', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
        	<a href="<%=Attributes.JSP_BASE%>?action=ResultPage&id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Goto detailed information on <%=URLDecoder.decode(container.getChemical_name(), "UTF-8")%>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
        <%
        }%>
        	<a href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>
		</td>
	</tr>
  </table>
</FIELDSET>
<br><br>
  <%
}

if(request.getParameter("errorcode2") != null)
{
%>
	<H2>Register Container - Error</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<h3>Error in selected locations, please try again</h3>
<%
}
if(request.getParameter("errorcode3") != null)
{
%>
	<H2>Register Container - Error</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<h3>Error in numbers of containers, please try again.</h3>
<%
}
if(request.getParameter("errorcode4") != null)
{
%>
	<H2>Register Container - Error</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<h3>Error in container registration, please try again.</h3>
<%
}
if(request.getParameter("errorcode5") != null)
{
%>
	<H2>Register Container - Error</H2>
	<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
	<br/>
	<h3>Error in container registration. The entered custom container id is allready registered.</h3>
<%
}
if(request.getParameter("errorcode2") != null || request.getParameter("errorcode3") != null || request.getParameter("errorcode4") != null)
{
  	String c_id = request.getParameter("compound_id");
  	String name = Util.getChemicalName(c_id); 	
%>
<FIELDSET>
 <LEGEND>ACTION</LEGEND>
   <table class="action_noheader" width="650px">
	<tr>
		<td>
    	 	<a href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer_reg&compound_id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Register a new container of the compound <%=name%>', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
     	 	<a href="<%=Attributes.JSP_BASE%>?action=ResultPage&id=<%=c_id%>&new=<%= new_compound %>" onmouseover="return overlib('Goto detailed information on <%=name%>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
           	<a href="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>
		</td>
	</tr>
  </table>
</FIELDSET>
<br><br>
<%
}
%>
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