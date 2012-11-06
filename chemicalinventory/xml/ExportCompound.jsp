<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="chemicalinventory.compound.export.*" %>
<jsp:directive.page import="chemicalinventory.compound.export.Compound_export"/>
<jsp:useBean id="exporter" scope="page" class="chemicalinventory.compound.export.Compound_export"/>
<%
	System.out.println("Starter xml generering **************");

	exporter.createEmptyExportXML();
	
	System.out.println("xml generering slut **************");
%>
