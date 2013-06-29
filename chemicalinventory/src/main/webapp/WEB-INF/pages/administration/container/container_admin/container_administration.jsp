<%@ page language="java" import="java.util.*" import="java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="modify" class="chemicalinventory.beans.modifyContainerBean" scope="page"/>
<jsp:setProperty name="modify" property="*"/>
<jsp:useBean id="reg" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="group" class="chemicalinventory.groups.Container_group" scope="page"/>
<jsp:useBean id="Locationgroup" class="chemicalinventory.groups.Location_group" scope="page"/>
<jsp:useBean id="supplier" class="chemicalinventory.beans.supplierBean" scope="page"/>
<jsp:useBean id="location" class="chemicalinventory.beans.locationBean" scope="page"/>
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
<script language="JavaScript" src="<%=Attributes.APPLICATION%>/script/calendar1.js"></script>
<script language="JavaScript" src="<%=Attributes.APPLICATION%>/script/inventoryScript.js"></script>
<SCRIPT LANGUAGE="JavaScript">
// Begin
<%
if(request.getParameter("code1")!=null)
{%>
var arrItems1 = new Array();
var arrItemsGrp1 = new Array();
var arrItemsLocID1 = new Array();

<%  location.showAllLocations();
    for(int i=0; i<location.l1_id.size(); i++)
    {
       String id = (String) location.l1_id.elementAt(i);
       String loc_id = (String) location.l1_loc_id.elementAt(i);
       String location_name = (String) location.l1_name.elementAt(i);
       %>
        arrItemsLocID1[<%= i %>] = "<%= loc_id %>";
        arrItems1[<%= i %>] = "<%= Util.encodeTag(location_name) %>";
        arrItemsGrp1[<%= i %>] = "<%= id %>";
       <%
    }  
%>

var arrItems2 = new Array();
var arrItemsGrp2 = new Array();
var arrItemsLocID2 = new Array();

<%  
    for(int i=0; i<location.l2_id.size(); i++)
    {
       String id = (String) location.l2_id.elementAt(i);
       String loc_id = (String) location.l2_loc_id.elementAt(i);
       String location_name = (String) location.l2_name.elementAt(i);
       %>
        arrItemsLocID2[<%= i %>] = "<%= loc_id %>";
        arrItems2[<%= i %>] = "<%= Util.encodeTag(location_name) %>";
        arrItemsGrp2[<%= i %>] = "<%= id %>";
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
        if ( arrItemsLocID1[x] == control.value )
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
        if ( arrItemsLocID2[x] == control.value )
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

function enableField()
{
    if(document.locationChoices.rem_owner.checked)
    {
       document.locationChoices.owner_1.disabled=true;
       document.locationChoices.owner_2.disabled=true;
    }
    else
    {
       document.locationChoices.owner_1.disabled=false;
       document.locationChoices.owner_2.disabled=false;
    }
}

function validateForm(form) 
{

 if (trim(form.quantity.value) == "") 
 {
  alert("Please fill in a valid quantity!");
  form.quantity.focus();
  return false;
 }
 if (trim(form.tara_weight.value) == "") 
 {
  alert("Please fill in a valid tara weight!");
  form.tara_Weight.focus();
  return false;
 }
return true;
}
<%
}
%>
</script>
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
<title>
This page is for modifying information stored about a container.
</title>
</head>
<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("code3")==null && request.getParameter("code4")==null)
{
%>
<body onload="document.con.container_id.focus()">
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
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_administration_container.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="posAdm2">
     | <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=newContainer&code1=yes">Register a new container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=Container_adm">Modify an existing container</a> |
     <a class="adm" href="<%=Attributes.CONTAINER_ADMINISTRATOR_BASE%>?action=emptyContainer">Register a container as empty</a> |
</span>
<span class="textboxadm">
<h2>Modify the information stored about a container</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">

<%
if(request.getParameter("code1")==null && request.getParameter("code2")==null && request.getParameter("code3")==null && request.getParameter("code4")==null)
{
%>
<center>
	<form method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=Container_adm&code1=yes" name="con">
 	<TABLE class="box" cellpadding="1" cellspacing="2" width="370">
		<TR><TH colspan="2" class="blue">Container</TH></TR>
        <tr>
            <th align="left" class="standard">Container id:</th>
            <TD><input type="text" name="container_id" class="w200"></TD>
        </tr>
	    </table><br>
	    <input class="submit" type="Submit" name="Submit" value="Submit">
	</form>
</center>
<%
}

if(request.getParameter("code1")!=null)
{
  modify.setContainerInfo(modify.getContainer_id());//get the initial information about the container to alter.

  String own_display1 = modify.getOwner();

  if (own_display1 == null)
        own_display1 = "-";


  if(modify.getSearchOk() == true)//a valid container - display the info.
  {
    %>
  <center>
    <form name="locationChoices" method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=Container_adm&code2=yes" onSubmit="return validateForm(this)">
	<TABLE class="box" cellpadding="1" cellspacing="2" width="560">
		<TR><TH colspan="2" class="blue">Container</TH></TR>
            <tr>
                <th align="left" class="standard">Chemical name:</th>
                <td><%= modify.getChemical_name() %>
                	<input type="hidden" value="<%= modify.getChemical_name() %>" name="chemical_name" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Container id:</th>
                <td><%= modify.getContainer_id() %>
                	<input type="hidden" value="<%= modify.getContainer_id() %>" name="container_id" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Supplier:</th>
                <td>
                    <select name="supplierID" style="width: 200px">
                        <%  
                            String id_2 = ""+modify.getSupplierID();
                            boolean selected = false;
                            supplier.getSuppliers();
                            for(int i=0; i<supplier.supplier_id.size(); i++)
                            {
                               String id = (String) supplier.supplier_id.elementAt(i);
                               String supplier_name = (String) supplier.supplier.elementAt(i);
                               if(id.equals(id_2))
                               {  %>
                                    <option value="<%= id %>" selected>Current: <%= Util.encodeTag(URLDecoder.decode(supplier_name, "UTF-8")) %></option> <%
                                    selected = true;
                               }
                               else
                               {
                               %>   <option value="<%= id %>"><%= Util.encodeTag(URLDecoder.decode(supplier_name, "UTF-8")) %></option><%
                               }
                            }
                            
                            if (selected == false)
                            {
                                  %><option value="0" selected>[SELECT]</option> <%
                            } 
%>
                    </select>
                    <input type="hidden" name="o_supplier" value="<%=modify.getSupplierID()%>"/>
                </td>
            <tr>
                <th align="left" class="standard">Home Location (Current):</th>
                <td><%= modify.getLocation()%>
                	<input type="hidden" value="<%=modify.getLocationID()%>" name="locationID">
	                <input type="hidden" name="o_location" value="<%=modify.getLocationID()%>"/>	
                </td>
            </tr>
            <tr>
                <th align="left" class="standard">Home Location (Modify):</th>
                <td>
                <select id="firstChoice" name="firstChoice" onchange="selectChange(this, locationChoices.secondChoice, arrItems1, arrItemsGrp1);">
                <option value="X" selected>[SELECT]</option>
                <%  
        
                    for(int i=0; i<location.l0_id.size(); i++)
                    {
                       String loc_id = (String) location.l0_id.elementAt(i);
                       String location_name = (String) location.l0_name.elementAt(i);
                       %>
                        <option value="<%= loc_id %>"><%= Util.encodeTag(location_name) %></option>
                       <%
                    }
                %>
                </select>
                <select id="secondChoice" name="secondChoice" onchange="selectChange(this, locationChoices.thirdChoice, arrItems2, arrItemsGrp2);">
                <option>[SELECT]</option>
                </select>
                <select id="thirdChoice" name="thirdChoice"></select>
                </td>
            </tr>
            <tr>
                <th align="left" class="standard">Quantity:</th>
                <td>
                	<input type="text" value="<%= modify.getQuantity()%>" name="quantity" class="w200">
   	                <input type="hidden" name="o_quantity" value="<%=modify.getQuantity()%>"/>
                </td>

            </tr>
            <tr>
                <th align="left" class="standard">Unit:</th>
                <td>
                    <select name="unit" size="1" style="width: 200px">
                    <%
                    String g = "<option value='g'>Gram (g)</option>";
                    String mg = "<option value='mg'>Milli Gram (mg)</option>";
                    String l = "<option value='l'>Liter (l)</option>";
                    String ml = "<option value='ml'>Milli liter (ml)</option>";
 
                        if(modify.getUnit().equalsIgnoreCase("g"))
                        {
                            %><option value="g" selected="selected">Current: Gram (g)</option><%
                            out.print(mg);
                            out.print(l);
                            out.print(ml);
                        }
                        else if(modify.getUnit().equalsIgnoreCase("mg"))
                        {
                            %><option value='mg' selected="selected">Current: Milli Gram (mg)</option><%
                            out.print(g);
                            out.print(l);
                            out.print(ml);
                        }
                        else if(modify.getUnit().equalsIgnoreCase("l"))
                        {
                            %><option value='l' selected="selected">Current: Liter (l)</option><%
                            out.print(g);
                            out.print(mg);
                            out.print(ml);
                        }
                        else if(modify.getUnit().equalsIgnoreCase("ml"))
                        {
                            %><option value='ml' selected="selected">Current: Milli liter (ml)</option><%
                            out.print(g);
                            out.print(mg);
                            out.print(l);
                        }%>
                    </select>
                    
                    <input type="hidden" name="o_unit" value="<%=modify.getUnit()%>"/>
                </td>
            </tr>
            <tr>
                <th align="left" class="standard">Tara Weight:</th>
                <td>
                	<input type="text" value="<%= modify.getTara_weight() %>" class="w200" name="tara_weight">gram
   	                <input type="hidden" name="o_tara" value="<%=modify.getTara_weight()%>"/>
                </td>
            </tr>
            <tr>
                <th align="left" class="standard">Owned by(Current):</th>
                <td><%= Util.encodeNullValue(own_display1) %></td>
            </tr>
            <tr>
                <th align="left" class="standard">Owned by(Modify):</th>
                <td>
                <select name="owner_1" style="width: 104px">
                      <option value="X">[SELECT]</option>
                <%  
                    reg.userName_used();
                    Vector username = reg.getUn_taken();
                    for(int i=0; i<username.size(); i++)
                    {
                       String user_name = (String) username.elementAt(i);
                       %>
                        <option value="<%=user_name%>"><%=user_name%></option>
                       <%
                    }
                %>
                </select>
                <select name="owner_2" style="width: 104px">
                      <option value="X">[SELECT]</option>
                 <%  
                    for(int i=0; i<username.size(); i++)
                    {
                       String user_name = (String) username.elementAt(i);
                       %>
                        <option value="<%=user_name%>"><%=user_name%></option>
                       <%
                    }
                 %>
                 </select>
                    <input type="hidden" name="o_owner" value="<%=modify.getOwner()%>"/>
                 </td>
            </tr>
            <tr>
                <th align="left" class="standard">Remove owners:</th>
                <td><input type="checkbox" name="rem_owner" onclick="javascript:enableField()">
                </td>
            </tr>
            <tr>
		  		<th align="left" class="standard">Procurement Date:</th>
	  			<td>
	  				<input type="text" class="w200" name="procurement_date" value="<%=modify.getProcurement_date()%>" readonly="readonly">
	  				<a href="javascript:cal1.popup();"><img src="<%=Attributes.IMAGE_FOLDER%>/cal.gif" border="0"></a>
	  				<a onclick="locationChoices.procurement_date.value = '';" title="Remove the procurement date."><img src="<%=Attributes.IMAGE_FOLDER%>/nook-mark.gif" border="0"></a><br>
		  			<input type="hidden" name="o_procurement_date" value="<%=modify.getProcurement_date()%>"/>
	  			</td>
		  	</tr>
		 	<tr>
		   		<th align="left" class="standard">Expiry Date:</th>
	  			<td>
	  				<input type="text" class="w200" name="expiry_date" value="<%=modify.getExpiry_date()%>" readonly="readonly">
	  				<a href="javascript:cal2.popup();"><img src="<%=Attributes.IMAGE_FOLDER%>/cal.gif" border="0"></a>
	  				<a onclick="locationChoices.expiry_date.value = '';" title="Remove the expiry date."><img src="<%=Attributes.IMAGE_FOLDER%>/nook-mark.gif" border="0"></a><br>
		  			<input type="hidden" name="o_expiry_date" value="<%=modify.getExpiry_date()%>"/>
	  			</td>
			</tr>
            <tr>
                <th align="left" class="standard">Register date:</th>
                <td><%= modify.getRegister_date() %>
                	<input type="hidden" value="<%= modify.getRegister_date() %>" name="register_date" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="left" class="standard">Register by:</th>
                <td><%= modify.getRegister_by()%>
                	<input type="hidden" value="<%= modify.getRegister_by()%>" name="register_by" readonly="readonly"></td>
            </tr>
            <tr>    
                <th align="left" class="standard">User groups</th>
                <td>
                <%
                     int id = Integer.parseInt(modify.getContainer_id());
                     /*
                     * Show checkbox as readonly, if the container is at a location
                     * that is in a user group.
                     */
                     boolean readonly_checkobox = Locationgroup.isLocationInGroup(Util.getIntValue(modify.getLocationID()));
                 
                     group.find_container_groups_from_id(id, readonly_checkobox);
                     Vector groups = group.getAll_groups();
                     String tag = null;
                     for (int i=0; i<groups.size(); ++i)
                     {
                         tag = (String) groups.elementAt(i);
                         out.println(tag);%><br><%
                     }
                 %>
	                <input type="hidden" name="o_group" value="<%=group.getGroup_list()%>"/>
                </td>
            </tr>
            <tr>
                <th align="left" class="standard">Remark:</th>
                <td>
                	<textarea name="remark" style="width=400;" rows="4"><%if(modify.getRemark()!= null){%><%=modify.getRemark()%><%}%></textarea>
   	                <input type="hidden" name="o_remark" value="<%=modify.getRemark()%>"/>
                </td>
            </tr>            
            <tr>
            	<td colspan="2">
					<table border="0" width="100%" cellpadding="1" cellspacing="0">
						<tr>
							<th align="center" class="blue">Reason For Change</th>
						</tr>
						<tr>
							<td>
								<TEXTAREA name="reason_for_change" style="width=560;" rows="4"></TEXTAREA>	
							</td>
						</tr>
					</table>
				</td>
            </tr>
        </table><br>
        <input class="submit" type="submit" name="Submit" value="Submit">&nbsp;&nbsp;&nbsp;
        <input class="submit" type="Reset" value="Reset">
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
<%}
  else
  {
    %> <h3>An error orcurred. Please try again, and make sure the entered container
           id is valid, and it is not an empty container.</h3>

     <center>       
        <form method="post" action="<%= Attributes.CONTAINER_ADMINISTRATOR_BASE %>?action=Container_adm&code1=yes">
            <table class="special" cellspacing="1" cellpadding="1" border="1">
                <tr>
                    <th>Container Id:</th>
                    <td><input type="text" name="container_id" tabindex="1"></td>
            </table><br>
            <input class="submit" type="Submit" name="Submit" value="Submit" tabindex="2">
        </form>
      </center>
<%
  }
}

if(request.getParameter("code2")!=null)
{
 /*Create a string holding all the user groups selected
  * the string shall be made up of 1, 2, 3.... etc*/
  String[] groups = request.getParameterValues("groups");
  
  if (groups!=null)
  {
      String gr = null;

      for(int i = 0; i < groups.length; i++)
      {
        if(i == 0)
         gr = groups[i];
        else
         gr = gr + "," + groups[i]; 
      }
      
      modify.setGroup(gr);
  }

    /*Perform the modification of the container*/
    modify.setUser(user);
    modify.modifyContainer();

	//perform the modification, and show status message to the user.
	if(modify.isUpdate())
	{
		if(modify.isEmpty())
			response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=Container_adm&code4=yes&container_id="+modify.getContainer_id());
		else
			response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=Container_adm&code3=yes&container_id="+modify.getContainer_id());
	}
	else
	{
		response.sendRedirect(Attributes.CONTAINER_ADMINISTRATOR_BASE+"?action=Container_adm&errorcode1=yes&container_id="+modify.getContainer_id());
	}
}

if(request.getParameter("code3") != null)
{
	modify.setContainerInfo(modify.getContainer_id());
	
    String own_display2 = modify.getOwner();
    String useragent = request.getHeader("User-Agent");
%>
<h3>The result of your update:</h3>
  <center>
	  	<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center" colspan="2">General Container Data</th>
	  		</tr>
		  	<tr>
	  			<th align="left" class="standard">Container Id</th>
	  			<td><%=modify.getContainer_id()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Compound</th>
	  			<td><%=modify.getChemical_name()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Supplier</th>
	  			<td><%=modify.getSupplier()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Location</th>
	  			<td><%=modify.getLocation()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Quantity</th>
	  			<td><%=modify.getQuantity()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Unit</th>
	  			<td><%=modify.getUnit()%></td>
	  		</tr>
	   		<tr>
	  			<th align="left" class="standard">Tara Weight</th>
	  			<td><%=modify.getTara_weight()%>&nbsp;g</td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Owner(s)</th>
	  			<td><%=Util.encodeNullValue(own_display2) %></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Group(s)</th>
	  			<td><%=Util.encodeNullValue(modify.getGroup())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Procurement Date</th>
	  			<td><%=Util.encodeNullValue(modify.getProcurement_date())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Expiry Date</th>
	  			<td><%=Util.encodeNullValue(modify.getExpiry_date())%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Registered By</th>
	  			<td><%=modify.getRegister_by()%></td>
	  		</tr>
	  		<tr>
	  			<th align="left" class="standard">Registered Date</th>
	  			<td><%=modify.getRegister_date()%></td>
	  		</tr>
	        <tr>
	           <th align="left" class="standard">Remark:</th>
	           <td>
	           	<%= Util.encodeTagAndNull(modify.getRemark())%>
	           </td>
	        </tr>	  		
	  	</table><br>
	<%
	   if(useragent.indexOf("MSIE") != -1)
    	{
    %>
	  	<form method="post" action="<%=Attributes.ADMINISTRATOR_BASE%>?action=printLabel&label=<%=modify.getContainer_id()%>&single=yes" target="blank">
        	<input class="submit_width125" type="submit" value="Print label" name="print">
   		</form>
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
 </center>      
<%
}

//display message that the container has been deleted.
if(request.getParameter("code4") != null)
{
%>
<h3>The result of your update:</h3>
  <center>
	  	<table class="box" width="600px">
	  		<tr>
	  			<th class="blue" align="center">Update Status</th>
	  		</tr>
		  	<tr>
	  			<td><I>Container id <%=modify.getContainer_id()%> has been deleted.</I></td>
	  		</tr>
	  	</table>
	 </center>
<%
}

if(request.getParameter("errorcode1") != null)
{
    %><h3>Modification of container <%=modify.getContainer_id()%> failed. 
       An unexpected error orcurred!</h3><%
	
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
</center>
</body>
</html>