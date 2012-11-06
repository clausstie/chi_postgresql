<%@ page language="java"%>
<%
	String url = (String) session.getAttribute("PrintURL");
	System.out.println("Url fra JSP: "+url);
%>
<!-- Put the html to open a preview window for the report -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>FragtStyring Print Visning</title>
    <SCRIPT language="JavaScript1.2">
		function poponload()
		{
		 	window= window.open ("<%=url%>", "Printing",
    		"width=800,height=450,scrollbars,menubar=yes,resizable=yes");
		}
	</SCRIPT>
  </head>  
  <body onload="javascript: poponload()">
  </body>
</html>