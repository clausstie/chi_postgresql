<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="chemicalinventory.utility.Util" %>
<jsp:useBean id="check" class="chemicalinventory.beans.BorrowBean" scope="page"/>
<jsp:setProperty name="check" property="*"/>
<jsp:useBean id="userInf" class="chemicalinventory.beans.userInfoBean" scope="page"/>
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
function validate(id) {

   if (trim(id) == "") 
    {
      alert('Container Id must have a value');    
      document.forms.check.id.focus();
      return false;
    }
 //   else
 //   {
 //      if(isPositiveInteger(id)==false)
 //      {
 //       alert("Please enter a valid container id.");
 //       document.forms.check.used_quantity.focus();
 //       return false;
 //      }
 //   }
  return true;
}
</script>
<title>
Check out a container using the fast lane..
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
<body onload="document.check.id.focus()">
<%}%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<center>
<%
userInf.setBase(Attributes.JSP_BASE);
String user = request.getRemoteUser();

  if (request.getParameter("code2")!=null)
  {
    String id = request.getParameter("id");
    String chemical_name = Util.getChemicalName2(id);
    
    /*
    * check out the container
    */
    boolean status = check.check_out(id, user, chemical_name);
    
    if(status)//the container is checked out ok!!
    {
	    userInf.shortUserInfo(user);
	   %>
	    </center>
	    <h2>The following container has been checked out!</h2>
	    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	    <center>
        <TABLE class="box" cellpadding="1" cellspacing="1" width="550">
			<TR><TH colspan="3" class="blue">Check Out - Receipt:</TH></TR>    
		    <tr>
		        <th align="left" width="160">Chemical name:</th>
		        <td><%=chemical_name%></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Container id:</th>
		        <td> <%= check.getId()%></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">User Name:</th>
		        <td><%= userInf.getUser_name() %></td>
		    </tr>
		   <tr>
		        <th align="left" width="160">New Location:</th>
		        <td><%= userInf.getUser_name() %></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">Current Quantity:</th>
		        <td><%check.setCurrent_quantity();%><%=check.getCurrent_quantity()%>&nbsp;<%=check.getUnit()%></td>
		    </tr>
	    </table><br>    
    <%
    }
    else//an error orcurred during check out..
    {%>
	    </center>
	    <h2>Error in check out of the container!</h2>
	    <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
	    <p>The container was not checked out, please try again</p>
	    <br/>
	    <%
    }
    %>
    <FIELDSET>
 <LEGEND>ACTION</LEGEND>
   <table class="action_noheader" width="790px">
	<tr>
		<td>
		 <a href="<%=Attributes.JSP_BASE%>?action=check_out_direct" onmouseover="return overlib('Check-Out one more container.', BORDER, 2);" onmouseout="return nd();"><img src="<%=Attributes.IMAGE_FOLDER%>/blue_sing_arrow_left.png" border="0"></a>
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
   <h2>Check out a container.</h2>
   <img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
   <center>
   <p>
   Check out a container for which you have the container id at hand.<br>
   If you do not have the contaiener id, goto the <a href="<%= Attributes.JSP_BASE %>?action=Search">Search page</a>.</p>
    
    <form method="post" action="<%= Attributes.JSP_BASE %>?action=check_out_direct&code1=yes" 
        OnSubmit="return validate(this.id.value);" name="check">
        <TABLE class="box" cellpadding="1" cellspacing="1" width="360">
			<TR><TH colspan="3" class="blue">Check Out:</TH></TR>    
		    <tr>
		        <th align="left" width="160">Container Id:</th>
		        <td><input type="text" name="id" class="w200"></td>
		    </tr>
		    <tr>
		        <th align="left" width="160">User/New Location:</th>
		        <td><input class="w200" type="text" name="user_name" value="<%= user.toUpperCase() %>" readonly="readonly"></td>
		    </tr>
    	</table><br>
      <input class="submit" type="submit" name="Submit" value="Submit">
    </form>
    <BR>
    
    <%
    if(request.getParameter("errorcode1")!=null)
	{
	%>
	<P><B>Please enter a valid container id!</B></P>
	<%
	}    
    %>
    
  <%}%>

<% if (request.getParameter("code1")!=null) 
   {
      /*A user can check out a container that is not a part of a group, 
       *or a he/she can check-out the container, if they are both part
       *of the same group*/
      info.retrieveNameId(user);
      int u_id = Util.getIntValue(check.getId());
      if(u_id > 0 && group.group_relations(info.getUser_id(), u_id))
      {
          check.find();
          userInf.shortUserInfo(user);

          if(check.getUser_id().equals("0") && check.empty == true && check.empty_flag==false)
          {%>
			</center>
			<h2>Confirm that you want to check out the following container</h2>
    		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
    		<center>
       
            <form method="post" action="<%= Attributes.JSP_BASE %>?action=check_out_direct&code2=yes&id=<%=check.getId()%>&chemical_name=<%=check.getChemical_name()%>">
                   <TABLE class="box" cellpadding="1" cellspacing="1" width="550">
					<TR><TH colspan="3" class="blue">Check Out - Confirmation:</TH></TR>    
   		            <tr>
		                <th align="left" width="160">Chemical name:</th>
		                <td><%=Util.encodeTag(URLDecoder.decode(check.getChemical_name(), "UTF-8"))%></td>
		            </tr>
		            <tr>
		                <th align="left" width="160">Container id:</th>
		                <td> <%=check.getId()%></td>
		            </tr>
		            <tr>
		                <th align="left" width="160">New Location:</th>
		                <td><%= userInf.getUser_name() %></td>
		            </tr>
		            <tr>
		           </table><br>
        	    <input class="submit" type="submit" name="ok" value="OK" tabindex="1">&nbsp;&nbsp;&nbsp;
            	<input class="submit" type="submit" name="cancel" value="Cancel" onclick="this.form.action='<%= Attributes.JSP_BASE %>?action=check_out_direct'">
         </form>
    <%    }
            else
            {%>
            </center>
			<h2>Check out - Error</h2>
    		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
    		<center><br><%
              if(check.empty_flag==true)
              {
                %><h3>The Container you are trying to check out is empty!!</h3><%
              }
              else
              {
                if(!check.getUser_id().equals("0"))
                {
                    %><h3>The container you are trying to check-out has already been checked out,<br>
                                or the container id is not valid.</h3><%
                }
                else
                {
                    %><h3>An error orcurred, please try again, and if the error
                          persists please contact your administrator.</h3><%
                }
              }
            }
    }
    else
    {
        %>	
        	</center>
			<h2>Check out - Error</h2>
    		<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a.png">
    		<center>
    	<%
    	if(u_id == 0)
    	{
			response.sendRedirect(Attributes.JSP_BASE +"?action=check_out_direct&errorcode1=yes");
    	}
    	else
    	{
       	%>    	
       	 		<h3>An error orcurred. You do not have sufficient privileges to check out 
                    the container.</h3>
    	<%	
    	}
     }
   }
%>
</center>
</body>
</html>