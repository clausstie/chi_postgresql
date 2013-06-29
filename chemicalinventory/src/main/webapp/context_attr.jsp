<%@ page language="java"%>
<%!
public void jspInit()
    {
      System.out.println("INITIALIZING THE CONTROLLER");
      
      ServletContext context = getServletContext();

      context.setAttribute("base", chemicalinventory.context.Attributes.JSP_BASE);
      context.setAttribute("adminbase", chemicalinventory.context.Attributes.ADMINISTRATOR_BASE);
      context.setAttribute("appbase", chemicalinventory.context.Attributes.SAMPLEAPPROVER_BASE);
      context.setAttribute("samplebase", chemicalinventory.context.Attributes.SAMPLE_BASE);
      context.setAttribute("historybase", chemicalinventory.context.Attributes.HISTORY_BASE);
      context.setAttribute("resultbase", chemicalinventory.context.Attributes.RESULT_BASE);
      context.setAttribute("batchbase", chemicalinventory.context.Attributes.BATCH_BASE);
      context.setAttribute("d_batchbase", chemicalinventory.context.Attributes.D_BATCH_BASE);
      context.setAttribute("printbase", chemicalinventory.context.Attributes.PRINT_BASE);
      context.setAttribute("adm_printbase", chemicalinventory.context.Attributes.PRINT_ADMINISTRATION);
    }%>