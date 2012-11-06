<%@ page language="java"%>
<%@ page import="chemicalinventory.context.Attributes" %>

<map NAME="nav_bar">
	<AREA SHAPE="rect" COORDS="4,1,144,22" href="<%=Attributes.JSP_BASE%>?action=sample">
	<AREA SHAPE="rect" COORDS="149,2,285,22"
		href="<%=Attributes.RESULT_BASE%>?action=result_entry">
	<AREA SHAPE="rect" COORDS="290,2,435,22"
		href="<%=Attributes.JSP_BASE%>?action=display_single_sample">
</map>
