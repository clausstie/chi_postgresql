<%@ page language="java" %> 
	
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
    <%
    String base = "/sampleApproval/";
    String sampleAccess = "sampleAccess/";
    String url = "/welcome.jsp";
    String action = request.getParameter("action");
    if (action!=null) 
    {
	  if (action.equals("lock_sample"))
        url = base + sampleAccess + "lock_sample.jsp";              
      else if (action.equals("unlock_sample"))
        url = base + sampleAccess + "unlock_sample.jsp";                  
      else if (action.equals("lock_result"))
        url = base + sampleAccess + "lock_unlock_result.jsp";
      else if (action.equals("modify_sample"))
        url = base + sampleAccess + "modify_sample_add_analysis.jsp";                 
     
      else if (action.equals("Logout"))
        url = "/Logout.jsp";
   }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>