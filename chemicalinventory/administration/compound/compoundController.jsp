<%@ page language="java" %> 
	
	<!--
 * 	 Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              needed to manage a chemical storage, based on the java technology.
 *				In addition it includes a sample module. This module, is used
 *				to create samples, store results etc.
 *
 * 	 Copyright: 	2004-2007 Dann Vestergaard and Claus Stie Kalles�e
 *
 * 	 overLIB:     overLIB 3.51 -- Copyright Erik Bosrup 1998-2002. All rights reserved.
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
    <%
    String adm_base = "/administration/";
    String adm = "/administration/compound/";
	String compound = "compound_admin/";
    String user = "user/";
    String url = "/welcome.jsp";
    String resource = "/administration/resources/";
    String action = request.getParameter("action");
    
    if (action!=null) 
    {
      if (action.equals("Chemical"))
        url = adm + compound + "chemical_administration.jsp";
      else if (action.equals("new_Chemical"))
        url = adm + compound + "chemical_administration_new.jsp";
        
      else if (action.equals("create_com_resource"))
        url = resource + "compound_res_create.jsp";
      else if (action.equals("resource_modify"))
        url = resource + "compound_res_modify.jsp";
      else if (action.equals("showIcons"))
        url = resource + "showIcons.jsp";         
   }
   else
   {
     url = adm_base + user + "user_administration.jsp";
   }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>