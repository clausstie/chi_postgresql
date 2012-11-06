<%@ page language="java" import="java.util.*, java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="check" class="chemicalinventory.beans.BorrowBean" scope="page"/>
<jsp:setProperty name="check" property="*"/>
<jsp:useBean id="info" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="group" class="chemicalinventory.groups.Container_group" scope="page"/>
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
<script language="JavaScript" src="../script/overlib.js"></script>
<script LANGUAGE="JavaScript">
function validateForm(form) 
{
 if (trim(form.id.value) == "") 
 {
  alert("Please fill in a valid container id, that is checked out by you!");
  form.id.focus();
  return false;
 }

 if (trim(form.used_quantity.value) == "") 
 {
  alert("Please fill in a valid quantity used, using '.' as decimal seperator!");
  form.used_quantity.focus();
  return false;
 }
 else
 {
   if(isPositiveInteger(form.used_quantity.value)==false)
   {
    alert("The quantity you entered is not valid. please enter a number only and use '.' as decimal seperator");
    form.used_quantity.focus();
    return false;
   }
 }

return true;
}
</script>
<meta name="author" content="Dann Vestergaard">
<title>
Transfer the container to another user.
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
<%}

String user = request.getRemoteUser();
String id = request.getParameter("id");
%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<center>
<%

  if (request.getParameter("code2")!=null) 
  {
    String chemical_name = request.getParameter("chemical_name");
    check.setUser_name(user);
    
    //Perform the transfer of the container.
    boolean status = check.transfer(chemical_name);
    
    //Check the status of the operation and show the result.
    if(status)
    {
		    String user_name = info.getUserNameFromID(check.getTransfer_to());
	        chemical_name = Util.encodeTag(chemical_name);
		    %>
		    
		    </center>
		    <h2>The container has been transferred</h2>
		    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
		    <center>
			<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
			<TR><TH colspan="3" class="blue">Transfer - Receipt:</TH></TR>    
		    <tr>
		        <th align="left" width="160">Chemical name:</th>
		        <td><%=chemical_name%></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Container id:</th>
		        <td> <%= check.getId()%></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">New Location:</th>
		        <td><%= info.display_owner_data_base(user_name, Attributes.JSP_BASE) %></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Home location:</th>
					<td><%= Util.getLocation(check.getLocation_id()) %></td>
		    </tr>
		</table><br>
	<%
	}
	else
	{
	%>
	    </center>
	    <h2>Error in transfer of the container!</h2>
	    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	    <p>The container could not be transferred, please try again</p>
	    <br/>
	    <%
    }
    %>
<FIELDSET>
	<LEGEND>ACTION</LEGEND>
		<table class="action_noheader" width="790px">
			<tr>
				<td>
					<a href="<%=Attributes.JSP_BASE%>?action=check_in_direct" onmouseover="return overlib('Check-In a container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
					<a href="<%=Attributes.JSP_BASE%>?action=trans" onmouseover="return overlib('Transfer another container', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
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
   <h2>Transfer a container</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center>
      <form name="checkin" id="checkin" method="post" action="<%= Attributes.JSP_BASE %>?action=trans&code1=yes" 
          OnSubmit="return validateForm(this);">
    	<TABLE class="box" cellpadding="1" cellspacing="1" width="360">
		  <TR><TH colspan="3" class="blue">Transfer Container:</TH></TR>    
	      <tr>
	          <th align="left" width="160">Container Id:</th>
	          <td><input type="text" name="id" class="w200"></td>
	      </tr>
	      <tr>
	          <th align="left" width="160">Quantity Used:</th>
	          <td><input type="text" name="used_quantity" class="w200" value="0.0"></td>
	      </tr>
	      <tr>
	          <th align="left" width="160">Transfer To:</th>
	          <td>
		          <SELECT name="transfer_to">
		          	<%info.getUserNameAndIDList();
	          		Vector names = info.getName_list();
	          		Vector ids = info.getId_list();
	          		
	          		for (int i = 0; i<ids.size(); i++)
	          		{%>
	          			<option value="<%=ids.get(i)%>"><%=names.get(i)%></option><%	          			
	          		}%>
		          </SELECT>
	          </td>
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
   	<H3>The entered value is not a valid container id, please try again.</H3>
   	<%  
  }
  
  if(request.getParameter("rcode2")!=null)
  {
   	%>
   	<BR>
   	<HR>
   	<H3>You have to be in the same group as the container to perform transfer.</H3>
   	<%  
  }

  if(request.getParameter("rcode3")!=null)
  {
   	%>
   	<BR>
   	<HR>
   	<H3>The receipient of the container is not in the same group as the container.</H3>
   	<%  
  }
 }
  


   if (request.getParameter("code1")!=null) 
   {
   String transfer_to_user = request.getParameter("transfer_to");
   
   	/* Make sure a valid id has been endted*/
	if(!Util.isValidInt(id))
	{
		/* Not a valid container id	*/
		response.sendRedirect(Attributes.JSP_BASE+"?action=trans&rcode2=yes");
	}
	else
	{
		//Make sure the user trying to perform the transfer is in the same group
        info.retrieveNameId(user);
        int user_id = info.getUser_id();

		info.retrieveNameId(info.getUserNameFromID(transfer_to_user));
        int user_id_to = info.getUser_id();

		if(!group.group_relations(user_id, Util.getIntValue(id)))
		{
			/* Is the container checked out by the user..??	*/
			response.sendRedirect(Attributes.JSP_BASE+"?action=trans&rcode2=yes&id="+id);
		}
		
		//Make sure the user who shall receive the container is in the same group	
		else if(!group.group_relations(user_id_to, Util.getIntValue(id)))
		{
			/* Is the container checked out by the user..??	*/
			response.sendRedirect(Attributes.JSP_BASE+"?action=trans&rcode3=yes&id="+id+"&user="+check.getTransfer_to());
		}
	   }  
   
   		//get information about the container.
	    check.find();
	   	
	    if(!check.getUser_id().equals("0") && check.getIntermediate_quantity() > 0 && check.getCheck()==true && check.empty_flag==false)
	    {%>
		                        
	   </center>
	   <h2>Confirm that you want to transfer this container</h2>
	   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	   <center>
	    <form method="post" action="<%= Attributes.JSP_BASE %>?action=trans&code2=yes&id=<%=check.getId()%>&location_id=<%= check.getLocation_id() %>">
	   	<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
		  <TR><TH colspan="3" class="blue">Transfer Container - Confirmation:</TH></TR>    
		    <tr>
		        <th align="left" width="160">Chemical name:</th>
		        <td><%=Util.encodeTag(URLDecoder.decode(check.getChemical_name(), "UTF-8"))%>
	       		<input type="hidden" name="chemical_name" value="<%=URLDecoder.decode(check.getChemical_name(), "UTF-8")%>"></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Container id:</th>
		        <td><%=check.getId()%>
		        	<input type="hidden" name="id" value="<%=check.getId()%>" readonly="readonly">
		        </td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Transfer From:</th>
		        <td> <%=check.containerAtUser(Util.getIntValue(check.getId()))%>
		        	<input type="hidden" name="transfer_name" value="<%=check.containerAtUser(Util.getIntValue(check.getId()))%>" readonly="readonly">
		        </td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Transfer To:</th>
		        <td> <%=info.getUserNameFromID(check.getTransfer_to())%>
		        	<input type="hidden" name="transfer_name" value="<%=info.getUserNameFromID(check.getTransfer_to())%>">
		        	 <input type="hidden" name="transfer_to" value="<%=check.getTransfer_to()%>">
		        </td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Owner:</th>
		        <%String owner = check.getOwner();
		        System.out.println("owner: "+owner);
				if(owner.equals("null") || owner.equals(""))
		       	  	owner = "-";%>
		        <td><%=owner%>
		        	<input type="hidden" name="owner" value="<%=owner%>">
		        </td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Initial Quantity:</th>
		        <%check.setCurrent_quantity();%>
		        <td><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%>
			        <input type="hidden" name="current_quantity" value="<%=check.getCurrent_quantity()%>">
			    </td>
		    </tr>
	        <tr>
	          <th align="left" width="160">Quantity Used:</th>
	          <td><%=check.getUsed_quantity()%>&nbsp;<%=check.getUnit()%>
	          	<input type="hidden" name="used_quantity" value="<%=check.getUsed_quantity()%>">
	          </td>
	        </tr>
	        <tr>
	          <th align="left" width="160">Current Quantity:</th>
	          <td><%=check.getIntermediate_quantity()%>&nbsp;<%=check.getUnit()%>
	          	<input type="hidden" name="intermediate_quantity" value="<%=check.getIntermediate_quantity()%>">
	          </td>
	       </tr>
		    <tr>
	    </table><br>
	    <input class="submit" type="submit" name="ok" value="OK" tabindex="1">&nbsp;&nbsp;&nbsp;
	    <input class="submit" type="submit" name="cancel" value="Cancel" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=trans'">
	  </form>
	<% }
	    else
	    {%>
	     </center>
	     <h2>Transfer container - Error</h2>
	     <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	     <center>
	     <%
	      if(check.getUser_id().equals("0"))
	      {%>
	     	 <br><h3>This container can not be transferred, as it has not been checked out!</h3>        
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
	              else 
	              {
	                 if(check.getIntermediate_quantity() <= 0)
	                 {
	                 %><br><h3>You cannot register a value below or equal to 0 as the new quantity.</h3>
	                         The values you are trying to register are:<br>
	                         <ul>
		                         <li>Current quantity: <%=check.getCurrent_quantity()%><%=check.getUnit()%></li>
		                         <li>Quantity used: <%=check.getUsed_quantity()%><%=check.getUnit()%></li>
		                         <li>New quantity: <%=check.getIntermediate_quantity()%><%=check.getUnit()%></li>
	                         </ul>
	                 <%
	                 }//end if intermediate < 0
	              }//end else
	           }//end else
	       }//end else
	       //<!--Action icons-->%>           
		 <FIELDSET>
			<LEGEND>ACTION</LEGEND>
				<table class="action_noheader" width="790px">
					<tr>
						<td>
						 <a href="<%=Attributes.JSP_BASE%>?action=check_in_direct" onmouseover="return overlib('Check-In a container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
	 					 <a href="<%=Attributes.JSP_BASE%>?action=trans" onmouseover="return overlib('Transfer another container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
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