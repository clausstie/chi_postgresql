<%@ page language="java" %> 

<%
    String base = "/print/";
    String url = "/welcome.jsp";
    String action = request.getParameter("action");

    if (action!=null) 
    {
    //Printing of reports 
      if (action.equals("printindex"))  
        url = base + "Printindex.jsp";
    //Printchooser
      if (action.equals("choosePrint"))  
        url = base + "PrintChooser.jsp";             
    }
   
  RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
  requestDispatcher.forward(request, response);
%>