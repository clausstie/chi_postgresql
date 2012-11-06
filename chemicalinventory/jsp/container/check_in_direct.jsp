<%@ page language="java" import="java.util.*, java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Return_codes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="check" class="chemicalinventory.beans.BorrowBean" scope="page"/>
<jsp:setProperty name="check" property="*"/>
<jsp:useBean id="info" class="chemicalinventory.user.UserInfo" scope="page"/>
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
<script language="JavaScript" src="../script/overlib.js"></script>
<script LANGUAGE="JavaScript">
function validate1(id) 
{
   if (trim(id) == "") 
    {
      alert('Container Id must have a value');    
      document.forms.checkin.id.focus();
      return false;
    }
    else
    {
       if(isPositiveInteger(id)==false)
       {
        alert("Please enter a valid container id.");
        document.forms.checkin.id.focus();
        return false;
       }
    }
}
function validate2(used_quantity) 
{
  if (!document.forms.checkin2.empty_container.checked) 
  {
	if(trim(used_quantity) == "")
    {
        alert('Quantity Used must have a value');
        document.forms.checkin2.used_quantity.focus();
        return false;
    }
    else
    {
       if(isPositiveInteger(used_quantity)==false)
       {
        alert("The quantity you entered is not valid. please enter a number only and use '.' as decimal seperator");
        document.forms.checkin2.used_quantity.focus();
        return false;
       }
    }
    return true;
  } 
}
</script>
<title>
Check in a container using the fast lane..
</title>
</head>
<%
if (request.getParameter("code1")!=null || request.getParameter("code2")!=null)
{%>
<body>
<%
}
else
{%>
<body onload="document.checkin.id.focus()">
<%
}%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<center>
<%
String user = request.getRemoteUser();
String id = request.getParameter("id");

  if (request.getParameter("code2")!=null) 
  {
    String chemical_name = request.getParameter("chemical_name");
    
    /*
    * check in the container
    */
	boolean status = check.check_in(id, user, chemical_name);
	
	//encode the chemical name
	chemical_name = chemicalinventory.utility.Util.encodeTag(chemical_name);
	
	if(status)
	{
	%>
	    </center>
	    <h2>The container has been checked in</h2>
	    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	    <center>
	
		<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
		<TR><TH colspan="3" class="blue">Check In - Receipt:</TH></TR>    
	        <th align="left" class="standard">Chemical name:</th>
	        <td><%=chemical_name%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Container id:</th>
	        <td> <%= check.getId()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User Name:</th>
	        <td><%= info.display_owner_data_base(user, Attributes.JSP_BASE) %></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Home location:</th>
	        <%if(check.getEmpty_container().equals("on"))
	          {
	              %><td>EMPTY CONTAINER</td><%
	          }
	         else
	         {
	            %><td><%= chemicalinventory.utility.Util.getLocation(check.getLocation_id()) %></td><%
	         }%>
	    </tr>
        <%if(!check.getEmpty_container().equals("on"))
          {
              %>
        <tr>
	        <th align="left" class="standard">Current Quantity:</th>
	        <td><%check.setCurrent_quantity();%><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%></td>
	    </tr><%
          }%>
	</table><br>
<%
    }
    else//an error orcurred during check in..
    {
    	if(check.getStatus() == Return_codes.VALUE_TOO_HIGH)
    	{
    		response.sendRedirect(Attributes.JSP_BASE+"?action=check_in_direct&code1=yes&rcode1=yes&id="+id);
    	}
    %>
	    </center>
	    <h2>Error in check in of the container!</h2>
	    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	    <p>The container was not checked in, please try again</p>
	    <br/>
	    <%
    }
    %>
<FIELDSET>
	<LEGEND>ACTION</LEGEND>
		<table class="action_noheader" width="790px">
			<tr>
				<td>
				<a href="<%=Attributes.JSP_BASE%>?action=check_in_direct" onmouseover="return overlib('Check-In one more container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
				<a href="<%= Attributes.JSP_BASE %>?action=ResultPage&id=<%= Util.getChemicalId(check.getId()) %>" onmouseover="return overlib('Detailed information for <%=chemical_name %>', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_right.png" border="0"></a>
				<a href="<%= Attributes.JSP_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>
				</td>
			</tr>
		</table>
</FIELDSET>
<%
  }%>

<%
if (request.getParameter("code2")==null && request.getParameter("code1")==null) 
   {  
   %> 
   </center>
   <h2>Check in a container</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center>
   <p>On this page you have the option to check in a container, for which you have the 
   Container ID and the quantity used. If you do not have the Container ID proceed
   to the <a href="<%= Attributes.JSP_BASE %>?action=Search">Search page</a>.</p>    
    
      <form name="checkin" id="checkin" method="post" action="<%= Attributes.JSP_BASE %>?action=check_in_direct&code1=yes" 
          OnSubmit="return validate1(this.id.value);">
		<TABLE class="box" cellpadding="1" cellspacing="1" width="360">
  		  <TR><TH colspan="3" class="blue">Check In:</TH></TR>    
	      <tr>
	          <th align="left" class="standard">Container Id:</th>
	          <td><input type="text" name="id" class="w200"></td>
	      </tr>
      </table><br>
         <input class="submit" type="submit" name="Submit" value="Submit">
      </form>
  <%

  if(request.getParameter("rcode1")!=null)
  {
   	%>
   	<BR>
   	<HR>
   	<H3>You cannot check-in this container(<%=id%>). You are not the user that performed checkout.</H3>
   	<%  
  }
  
  if(request.getParameter("rcode2")!=null)
  {
   	%>
   	<BR>
   	<HR>
   	<H3>Please enter a valid container id.</H3>
   	<%  
  }
  
  }//end start checkin.
  %>

<% if (request.getParameter("code1")!=null) 
   {
	/* Make sure a valid id has been endted*/
	if(!Util.isValidInt(id))
	{
		/* Not a valid container id	*/
		response.sendRedirect(Attributes.JSP_BASE+"?action=check_in_direct&rcode2=yes");
	}
	else
	{
		/*
	    * If the user is adm, ignore the following check
	    */
		String role = "adm";
  		boolean inRole = request.isUserInRole(role);
	    
	    if(!inRole)
	    {
	        /*
	    	* Make sure the container is checked out by the user that is trying
    		* to check it in, or it is an adm user
    		*/
    		
		 	//If not adm user make sure the container is 
		 	// checked out by the user trying to perform checkin. 
			if(!check.isCheckedOutByUser(user, id))
			{
				/* Is the container checked out by the user..??	*/
				response.sendRedirect(Attributes.JSP_BASE+"?action=check_in_direct&rcode1=yes&id="+id);
			}
	    }	
	    
	    /* Get the data for the container */
    	check.find();
	}


    if(!check.getUser_id().equals("0") && check.getCheck()==true && check.empty_flag==false)
    {%>
   </center>
   <h2>Confirm that you want to check in a container with the following details</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center>
    <form method="post" name="checkin2" action="<%= Attributes.JSP_BASE %>?action=check_in_direct&code2=yes&id=<%=check.getId()%>&location_id=<%= check.getLocation_id() %>"
    	OnSubmit="return validate2(this.used_quantity.value);">
	   <TABLE class="box" cellpadding="1" cellspacing="1" width="550">
  		<TR><TH colspan="3" class="blue">Check In - Confirmation:</TH></TR>    
	    <tr>
	        <th align="left" class="standard">Chemical Name:</th>
	        <td><%=chemicalinventory.utility.Util.encodeTag(URLDecoder.decode(check.getChemical_name(), "UTF-8"))%>
       		<input type="hidden" name="chemical_name" value="<%=URLDecoder.decode(check.getChemical_name(), "UTF-8")%>"></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Container Id:</th>
	        <td><%=check.getId()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">User Name:</th>
	        <td><%=user.toUpperCase()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Owner:</th>
	        <td><%=check.getOwner()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">New Location:</th>
	        <td><%=check.getHome_location()%></td>
	    </tr>
	    <tr>
	        <th align="left" class="standard">Current Quantity:</th>
	        <td><%check.setCurrent_quantity();%><%=check.getCurrent_quantity()%><input type="hidden" name="current_quantity" value="<%=check.getCurrent_quantity()%>">&nbsp;<%=check.getUnit()%></td>
	    </tr>
        <tr>
          <th align="left" class="standard">Quantity Used:</th>
          <td><input type="text" name="used_quantity" value="0.0">&nbsp;<%=check.getUnit()%></td>
        </tr>
	    <tr>
      <tr>
	      <th align="left" class="standard">Container Is Empty:</th>
          <td>
          <input type="checkbox" name="empty_container" 
                 onclick="if(this.checked){this.form.used_quantity.disabled = true; this.form.used_quantity.value = '0.0';}
                          else{this.form.used_quantity.disabled = false}">The container is empty
          </td>
      </tr>	    
    </table><br>
    <input class="submit" type="submit" name="ok" value="OK" tabindex="1">&nbsp;&nbsp;&nbsp;
    <input class="submit" type="submit" name="cancel" value="Cancel" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=check_in_direct'">
  </form>
  <%
  //show error message
  if (request.getParameter("rcode1")!=null) 
   {
   	%>
   	<BR>
   	<HR>
   	<H3>Please make sure that used quantity is equal or less than current quantity!</H3>
   	<%
   }
   
    }
    else
    {%>
    </center>
   <h2>Check in - Error</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center><%

      if(check.getUser_id().equals("0"))
      {
        %><br><h3>This container can not be checked in, as it has not been checked out!</h3>        
        <%
      }
      else
      {
          if(check.empty_flag==true)
          {
             %><br><h3>The container ID entered is no longer active, as the container is empty.</h3>
             <%
           }
          else
          {
             if(check.getCheck()==false)
             {
              %><br><h3> An error orcurred. The container ID entered is not valid! </h3><%
             }
           }//end else
       }//end else
       //<!--Action icons-->%>           
	 <FIELDSET>
		<LEGEND>ACTION</LEGEND>
			<table class="action_noheader" width="790px">
				<tr>
					<td>
					 <a href="<%=Attributes.JSP_BASE%>?action=check_in_direct" onmouseover="return overlib('Check-In one more container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
		   	         <a href="<%= Attributes.JSP_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>
					</td>
				</tr>
			</table>
	</FIELDSET><%       
    }//end else check in failed
  }//end code 1
%>
</center>
</body>
</html>