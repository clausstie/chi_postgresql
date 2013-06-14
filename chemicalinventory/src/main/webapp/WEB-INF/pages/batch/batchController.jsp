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
    String base = "/batch/";
    String create = "create/";
    String modify = "modify/";
    String approve = "approve/";
    String url = "/welcome.jsp";
    String action = request.getParameter("action");

    if (action!=null) 
    {
    //Create the new batch
      if (action.equals("new_batch"))
        url = base + create + "create_batch.jsp"; 
        
     //Modify batch
	else if (action.equals("modify_batch"))
        url = base + modify + "modify_batch.jsp"; 
        
    //lock a batch
    else if (action.equals("lock_batch"))
        url = base + approve + "batch_actions.jsp"; 
        
    //lock a batch
    else if (action.equals("unlock_batch"))
        url = base + approve + "unlock_batch.jsp"; 
    
   }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>