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
    String base = "/jsp/";
    String container = "container/";
	String information = "information/";
	String mail = "mail/";
	String print = "print/";
	String sample = "sample/";
	String batch = "batch/";
	String s_search = "sample/sample_search/";
	String display = "sample/display/";
	String analysis = "analysis/";
	String compound = "compound/";
    String url = "/welcome.jsp";
    String action = request.getParameter("action");
    
    if (action!=null) 
    {
// search chemicals compound
      if (action.equals("Search"))
        url = base + "Search.jsp";
// in use?
      else if (action.equals("RegisterUser"))
        url = base + "UserRegistration.jsp";
 // check in containers       
      else if (action.equals("check_in_direct"))
        url = base + container + "check_in_direct.jsp";
      else if (action.equals("check_in"))
        url = base + container + "check_in.jsp";
//check out container
      else if (action.equals("check_out_direct"))
        url = base + container + "check_out_direct.jsp";
      else if (action.equals("check_out"))
        url = base + container + "check_out.jsp";
//change password
      else if (action.equals("ChangePwd"))
        url = base + "CngPwddisplay.jsp";
//result page for search
      else if (action.equals("ResultPage"))
        url = base + "ResultPage.jsp";
//information about a container
      else if (action.equals("Container"))
        url = base + information + "info_container.jsp";
//information - location
      else if (action.equals("location"))
        url = base + information + "info_location.jsp";
//information - user
      else if (action.equals("User"))
        url = base + information + "info_user.jsp";
//the misc button
      else if (action.equals("misc"))
        url = base + information +"Miscellaneous.jsp";
//mail webmaster
      else if (action.equals("mail_webmaster"))
        url = base + mail + "Mail_webmaster.jsp";
//mail news
      else if (action.equals("mail_news"))
        url = base + mail +  "Mail_news.jsp";
//mail support
      else if (action.equals("support"))
        url = base + mail +  "Mail_support.jsp";
//mail form
      else if (action.equals("mail"))  
        url = base + mail +  "MailForm.jsp";
//secondary mail form
      else if (action.equals("mail2"))  
        url = base + mail +  "MailForm_2.jsp";
//print an extra label for a container.
      else if (action.equals("label_print"))  
        url = base + print + "Extra_labelPrint.jsp";
      else if (action.equals("label_print2"))  
        url = base + print + "Extra_labelPrint_2.jsp";
//transport a container from one user to another
      else if (action.equals("trans"))  
        url = base + container + "Transfer_container.jsp";
//Search for compounds - the simple search
      else if (action.equals("s_search"))  
        url = base + s_search + "Simple_search.jsp";
//Simpel details page for a compound
      else if (action.equals("details"))
        url = base + compound + "chemical_details.jsp";
//list of compounds        
      else if (action.equals("compound_list"))
        url = base + compound + "compound_list.jsp";
//display a single sample.
      else if (action.equals("display_single_sample"))
        url = base + display + "display_sample_info.jsp";       
//display a list of samples        
      else if (action.equals("display_sample_list"))
        url = base + display + "display_sample_overview.jsp";    
//display samples connected to a compound        
      else if (action.equals("display_sample_compound"))
        url = base + display + "display_sample_compound.jsp";        
//display sample.
      else if (action.equals("display_sample"))
        url = base + display + "display_sample.jsp";    
//search page for samples and analysis results.
      else if (action.equals("sample_search"))
        url = base + s_search + "Search_sample.jsp";
//display the selected sample from a list.        
      else if (action.equals("dis_sample"))
        url = base + display + "select_display_sample.jsp";
//display list of samples        
      else if (action.equals("dis_sample_list"))
        url = base + display + "sample_list.jsp";
      else if (action.equals("dis_sample_list2"))
        url = base + display + "sample_list2.jsp";
//display list of analysis
      else if (action.equals("analysis_list"))
        url = base + analysis + "analysis_list.jsp"; 
//Details for an analysis
      else if (action.equals("analysis_detail"))
        url = base + analysis + "analysis_details.jsp";  
//display an analysis
      else if (action.equals("display_analysis"))
        url = base + analysis + "display_analysis.jsp";        
//list samples... ??
      else if (action.equals("analysis_list_sample"))
        url = base + analysis + "analysis_list_sample.jsp";   
      else if (action.equals("analysis_list_search"))
        url = base + analysis + "analysis_list_search.jsp";            
//front page for the sample module.
      else if (action.equals("sample"))
        url = base + sample + "sample_intro.jsp";
//front page for the batch module.
      else if (action.equals("batch"))
        url = base + batch + "batch_intro.jsp";    
//logout and end the user session.
      else if (action.equals("Logout"))
        url = "/Logout.jsp";
   }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>