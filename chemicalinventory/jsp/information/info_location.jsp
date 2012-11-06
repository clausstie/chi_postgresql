<%@ page language="java" import="java.util.*, java.net.*"%>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="Util" class="chemicalinventory.utility.Util" scope="page"/>
<jsp:useBean id="location" class="chemicalinventory.beans.userInfoBean" scope="page"/>
<jsp:setProperty name="location" property="*"/>
<jsp:useBean id="ui" class="chemicalinventory.user.UserInfo" scope="page"/>
<jsp:useBean id="grBean" class="chemicalinventory.groups.Container_group" scope="page"/>
<jsp:useBean id="loc" class="chemicalinventory.beans.locationBean" scope="page"/>
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
Information about the containers in a specific location.
</title>
<SCRIPT LANGUAGE="JavaScript">
// Begin
var arrItems1 = new Array();
var arrItemsGrp1 = new Array();
var arrItemsLocID1 = new Array();

<%  loc.showAllLocations();
    for(int i=0; i<loc.l1_id.size(); i++)
    {
       String id = (String) loc.l1_id.elementAt(i);
       String loc_id = (String) loc.l1_loc_id.elementAt(i);
       String location_name = (String) loc.l1_name.elementAt(i);
       %>
        arrItemsLocID1[<%= i %>] = "<%= loc_id %>";
        arrItems1[<%= i %>] = "<%= chemicalinventory.utility.Util.encodeTag(location_name) %>";
        arrItemsGrp1[<%= i %>] = "<%= id %>";
       <%
    }  
%>

var arrItems2 = new Array();
var arrItemsGrp2 = new Array();
var arrItemsLocID2 = new Array();

<%  
    for(int i=0; i<loc.l2_id.size(); i++)
    {
       String id = (String) loc.l2_id.elementAt(i);
       String loc_id = (String) loc.l2_loc_id.elementAt(i);
       String location_name = (String) loc.l2_name.elementAt(i);
       %>
        arrItemsLocID2[<%= i %>] = "<%= loc_id %>";
        arrItems2[<%= i %>] = "<%= chemicalinventory.utility.Util.encodeTag(location_name) %>";
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
</script>
</head>
<body>
<%
location.setBase(Attributes.JSP_BASE);
String user = request.getRemoteUser();
String role = "adm";
boolean inRole = request.isUserInRole(role);
%>

<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<span class="posAdm1">
 <img src="<%=Attributes.IMAGE_FOLDER%>/bar_location_info.png" height="55" width="820" usemap="#nav_bar" border="0">
</span> 
<span class="textboxadm">
<!-- ------------------ Show selection boxes with locations options ------------------------ -->
<h2>Information about the containers at a specific location</h2>
<img src="<%=Attributes.IMAGE_FOLDER%>/Divider1a800.png">
<center>
<p>This page is used to get information about all containers stored at specific locations. 
You can choose to perform a wide search, by only selecting one of the selection boxes. 
Or you can narrow it by selecting two or three selection boxes.</p>

<form action="<%=Attributes.JSP_BASE%>?action=location&code1=yes" method="post" name="locationChoices"> 
	<TABLE class="box" cellpadding="0" cellspacing="1" bgcolor="#ffffff">
		<TR><TH colspan="2" class="blue">Location Information</TH></TR>
        <tr>
            <td colspan="2">
               <select style="width: 200;" tabindex="1" id=firstChoice name=firstChoice onchange="selectChange(this, locationChoices.secondChoice, arrItems1, arrItemsGrp1);">
                <option value="X" selected>[SELECT]</option>
              <%  
                 for(int i=0; i<loc.l0_id.size(); i++)
                    {
                       String loc_id = (String) loc.l0_id.elementAt(i);
                       String location_name = (String) loc.l0_name.elementAt(i);
                       %>
                        <option value="<%= loc_id %>"><%= chemicalinventory.utility.Util.encodeTag(location_name) %></option>
                       <%
                    }
              %>
              </select>&nbsp;&nbsp;&nbsp;
              <select style="width: 200;" tabindex="2" id=secondChoice name=secondChoice onchange="selectChange(this, locationChoices.thirdChoice, arrItems2, arrItemsGrp2);">
                <option>[SELECT]</option>
              </select>&nbsp;&nbsp;&nbsp;
             <select style="width: 200;" tabindex="3" id=thirdChoice name=thirdChoice></select>&nbsp;&nbsp;&nbsp;
            </td>
        </tr>
     </table>
     <BR>
     <input class="submit" tabindex="4" type="submit" name="Submit" value="Submit">
	 <input class="submit" tabindex="5" type="reset" name="Reset" value="Reset">
</form>

<!--------------- Show Search Result ----------------->

<%
  if (request.getParameter("code1")!=null) 
  {
    //Search for containers..
        location.container_at_location_Info(1);

%>
    <hr>
    <h3>Search result</h3>
<%
    if(location.location.isEmpty())
    {%>
    <table class="box" cellspacing="1" cellpadding="1" width="65%" align="center">
    <thead><tr> <th class="blue">Chemical Name:</th>
                <th class="blue">Container Id::</th>
                <th class="blue">Quantity:</th>
                <th class="blue">Home location:</th>
                <th class="blue">Current location:</th>
                <th class="blue">Owned by:</th>
                <th class="blue">Remark:</th>
                </tr> </thead>
    <tbody>
       <tr align="center">
           <td colspan="9">No containers registered for the location selected, please try again.</td>
       </tr>
    </tbody>
    </table>
  <%
  }
  else//There is containers to show
  {
       /*Only show this information to adm user, because potetially not all elements are shown
        *to the normal user*/
        if(inRole == true)
        {%>
           <p>This search result contains <%= location.location.size() %> containers.</p>
      <%}%>  
   <table class="box" cellspacing="1" cellpadding="1" width="100%" align="center">
   <thead><tr> <th class="blue">Chemical Name:</th>
                <th class="blue">Container Id::</th>
                <th class="blue">Quantity:</th>
                <th class="blue">Home location:</th>
                <th class="blue">Current location:</th>
                <th class="blue">Owned by:</th>
                <th class="blue">Remark:</th>
                </tr> </thead>
    <tbody>
    <%
       /*find the user id, information used if the user is not a adm user.*/
       ui.retrieveNameId(user);
       int user_id = ui.getUser_id();
       int con_id = 0;
       int n = 0;
       
       String color = "normal";
       for(int i=0; i<location.location.size(); i++)
       {
          //If the user is not in role limit the result based on group relations.
          if(inRole == false)
          {
            con_id = Integer.parseInt((String) location.container_ids.elementAt(i));
           /*
            *make sure that only the containers which is either a
            *part of the user group the user is a part of, or the 
            *container is not, in any group, is displayed. This is only
            *applicable for not administrators
            */
            if (grBean.group_relations(user_id, con_id))
            {        
              if(n % 2 != 0)
              {
                color = "blue";
              }
              else
                color = "normal";
           %><tr class="<%= color %>"><%
              String data = (String) location.location.elementAt(i);
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
          <%n++;
           }
          }
          //if the user is adm, show all containers NOT limited by group relations
          else
          {        
            if(i % 2 != 0)
            {
              color = "blue";
            }
            else
              color = "normal";
            %><tr class="<%= color %>"><%
              String data = (String) location.location.elementAt(i);
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
      <%   }
       }%>
   </tbody>
   </table>
<%
  }
}
%>
</center>
</span>
<MAP NAME="nav_bar">
  <AREA SHAPE="rect" COORDS="9,1,118,21" href="<%=Attributes.JSP_BASE%>?action=User">
  <AREA SHAPE="rect" COORDS="134,1,252,21" href="<%=Attributes.JSP_BASE%>?action=location">
  <AREA SHAPE="rect" COORDS="262,1,381,21" href="<%=Attributes.JSP_BASE%>?action=Container">
  <AREA SHAPE="rect" COORDS="388,1,514,21" href="<%=Attributes.PRINT_BASE%>?action=printindex">
  <AREA SHAPE="rect" COORDS="518,1,644,21" href="<%=Attributes.HISTORY_BASE%>?action=analysis_history">
</map>
</body>
</html>