<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="chemicalinventory.utility.Util" %>
<%@ page import="java.net.URLDecoder" %>
<jsp:useBean id="check" class="chemicalinventory.beans.BorrowBean" scope="page"/>
<jsp:setProperty name="check" property="*"/>
<jsp:useBean id="Util" class="chemicalinventory.utility.Util" scope="page"/>
<jsp:useBean id="info" class="chemicalinventory.user.UserInfo" scope="page"/>
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
<script language="JavaScript" src="../script/inventoryScript.js"></script>
<script LANGUAGE="JavaScript">
function validate(used_quantity) 
{
  if (!document.getElementById("empty_container").checked) 
  {
    if(trim(used_quantity) == "")
    {
        alert('Quantity Used must have a value');
        document.con.used_quantity.focus();
        return false;
    }
    else
    {
       if(isPositiveInteger(used_quantity)==false)
       {
        alert("The quantity you entered is not valid. please enter a number only and use '.' as decimal seperator");
        document.con.used_quantity.focus();        
        return false;
       }
    }
    return true;
  }
  else
    return true;
}
</script>
<title>
Check in a container.
</title>
</head>
<body>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<center>
<%
String user = request.getRemoteUser();
String queryuser = request.getParameter("queryuser");
if (queryuser == null || queryuser.equals(""))
{
	queryuser = "";
}

  if (request.getParameter("code1")!=null) 
  {
    String id = request.getParameter("id");
    String compound_id = request.getParameter("compound_id");
    String chemical_name = request.getParameter("chemical_name");
    check.setIntermediate_quantity();

    if(check.getIntermediate_quantity() >= 0)
    {
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
	    <tr>
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
	            %><td><%=Util.getLocation(check.getLocation_id()) %></td><%
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
	  {%>
		   </center>
		   <h2>Error in check in of the container!</h2>
		   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
		   <p>The container was not checked in, please try again</p>
		   <br/>
	   <%
	  }
   
	   /*
	   * We have checked in from the info page,
	   * Display a button to return to the user info page.
	   */
	  if (queryuser != null && !queryuser.equals(""))
	  {%>
	 	<form method="post">
	  	  <input class="submit_nowidth" type="submit" value="Return to user info" onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=User&code1=yes&user_name=<%=user%>'">
		</form>
	  <%
	  }%>				 
	 				 <!--Display action icons-->
		<FIELDSET>
		 <LEGEND>ACTION</LEGEND>
			<table class="action_noheader" width="790px">
				<tr>
					<td>
						<a href="<%= Attributes.JSP_BASE %>?action=ResultPage&id=<%= compound_id %>" onmouseover="return overlib('Detailed information for <%= chemical_name %>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
						<a href="<%= Attributes.JSP_BASE %>?action=check_in_direct" onmouseover="return overlib('Check-in', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
					    <a href="<%= Attributes.JSP_BASE %>?action=Search&code1=yes&history=true" onmouseover="return overlib('Show the last search result.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_search_30.png" border="0"></a>		     
					    <a href="<%= Attributes.JSP_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>
					</td>
				</tr>
			</table>
		</FIELDSET> 
	    <%
  	}// end if
    else
    {%>
     <h3>You cannot register a value below 0 as the new quantity.</h3>
           The values you are trying to register are:<br>
           <ul>
             <li>Current quantity: <%=check.getCurrent_quantity()%><%=check.getUnit()%></li>
             <li>Quantity used: <%=check.getUsed_quantity()%><%=check.getUnit()%></li>
             <li>New quantity: <%=check.getIntermediate_quantity()%><%=check.getUnit()%></li>
           </ul>
           
		<FIELDSET>
		 <LEGEND>ACTION</LEGEND>
		  <table class="action_noheader" width="790px">
			<tr>
				<td>
		       <a href="<%= Attributes.JSP_BASE %>?action=Search&code1=yes&history=true" onmouseover="return overlib('Last search result.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_search_30.png" border="0"></a>
		       <a href="<%= Attributes.JSP_BASE %>?action=ResultPage&id=<%= compound_id %>" onmouseover="return overlib('Return to detailed information on <%= chemical_name %>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_doub_arrow_left.png" border="0"></a>
		       <a href="<%= Attributes.JSP_BASE %>?action=check_in&id=<%= id %>&queryuser=<%=queryuser%>" onmouseover="return overlib('Check in container no.: <%= id%> of <%=chemical_name%>.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
		       <a href="<%= Attributes.JSP_BASE %>" onmouseover="return overlib('HOME', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_home_30.png" border="0"></a>    		
				</td>
			</tr>
		  </table>
		</FIELDSET>
       <%
     }
}//end code 1 = yes

if (request.getParameter("code2")==null && request.getParameter("code1")==null) 
   {
      check.find();
      %>
      </center>
      <h2>Check in a container</h2>
      <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
      <center>
      The following data has been transferred from the search result.<br>
      
      <form method="post" action="<%= Attributes.JSP_BASE %>?action=check_in&code1=yes&id=<%=check.getId()%>&compound_id=<%= check.getCompound_id() %>&location_id=<%= check.getLocation_id()%>&queryuser=<%=queryuser%>"
            name="con" OnSubmit="return validate(this.used_quantity.value);">
		<TABLE class="box" cellpadding="1" cellspacing="1" width="550">
		<TR><TH colspan="3" class="blue">Check In:</TH></TR>
	      <tr>
	          <th align="left" class="standard">Chemical name:</th>
			  <td><%=Util.encodeTag(URLDecoder.decode(check.getChemical_name(), "UTF-8"))%>
	          		<input type="hidden" name="chemical_name" value="<%=URLDecoder.decode(check.getChemical_name(), "UTF-8")%>"></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Container id:</th>
	          <td><%=check.getId()%></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Home location:</th>
	          <td><%=check.getHome_location()%><td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Current location:</th>
	          <td><%=check.getCurrent_location()%></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Owner:</th>
	          <td><%=Util.encodeNullValue(check.getOwner())%></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Tara Weight</th>
	          <td><%=check.getTara_weight()%>&nbsp;gram</td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Initial Quantitiy</th>
	          <td><%=check.getInitial_quantity()%>&nbsp;<%=check.getUnit()%></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Current Quantity</th>
	          <td><%check.setCurrent_quantity();%><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%></td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">Qantity Used</th>
	          <td><input class="w200" type="text" name="used_quantity" tabindex="1" value="0.0"> or</td>
	      </tr>
	      <tr>
	          <th align="left" class="standard">The contianer is empty</th>
	          <td><input type="checkbox" name="empty_container" 
	                     onclick="if(this.checked){this.form.used_quantity.disabled = true}
	                              else{this.form.used_quantity.disabled = false}">
	          </td>
	      </tr>
	   </table><br>
            <input class="submit" type="submit" name="Submit" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=check_in&code1=yes&id=<%=check.getId()%>&compound_id=<%= check.getCompound_id() %>&location_id=<%= check.getLocation_id()%>&queryuser=<%=queryuser%>'" value="Check In">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            
			 <%if (queryuser != null && !queryuser.equals(""))
 				 {
 				 	//if you arrived here from the user_info page, when cancelling return to there other wise return to resultpage
 				 %>
	 				 <input class="submit" type="submit" name="cancel" value="Cancel" onclick="this.form.action='<%=Attributes.JSP_BASE%>?action=User&code1=yes&user_name=<%=user%>'">
 				 <%
 				 }
 				 else
 				 {%>
 				 	<input class="submit" type="submit" name="cancel" value="Cancel" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=ResultPage&id=<%=check.getCompound_id()%>'"> 
       <%}%>
      </form>
  <%}%>
</center>
</body>
</html>