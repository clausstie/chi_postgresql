<%@ page language="java" %> 

<%
    String base = "/print/print_administration/";
    String url = "/welcome.jsp";
    String action = request.getParameter("action");

    if (action!=null) 
    {
    //Creation of report defenition on the db
      if (action.equals("printCreation"))
        url = base + "Print_create.jsp";
    //Modification of a report definition
      if (action.equals("PrintModification"))  
        url = base + "Print_Modify.jsp";
    //Display a report definition.
      if (action.equals("PrintDefinition"))  
        url = base + "Display_Print_Def.jsp";      
    }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>