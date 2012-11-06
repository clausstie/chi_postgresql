<%@ page language="java" %>
<%@ page import="chemicalinventory.context.Attributes" %>
<jsp:useBean id="container" class="chemicalinventory.beans.ContainerRegBean" scope="page"/>
<html>
<head>
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
<link rel="stylesheet" type="text/css" href="<%=Attributes.STYLE_SHEET_FOLDER%>/Style.css">
<title>
print label for a container
</title>
<SCRIPT LANGUAGE="VBScript">
<!-- http://www.p-touchsolutions.com/europe/uk/bpacsdk_step2.cfm

' Data Folder
	Const sDataFolder = "<%=Attributes.LABEL_TEMPLATE_PATH%>"
	'*******************************************************************
	'	Replaces Image, Prints or Preview Label
	'*******************************************************************
	Sub DoPrint(strExport)
		Dim TheForm
		Set TheForm = Document.ValidForm
		Set ObjDoc = CreateObject("BrssCom.Document")
		bRet = ObjDoc.Open(sDataFolder & "<%=Attributes.LABEL_TEMPLATE%>")
		If (bRet <> False) Then

	         nIndex = ObjDoc.GetTextIndex("container_id")
	     ObjDoc.SetText nIndex, TheForm.container_id.Value
	         nIndex2 = ObjDoc.GetTextIndex("location")
	     ObjDoc.SetText nIndex2, TheForm.location.Value
	         nIndex3 = ObjDoc.GetTextIndex("compound_name")
	     ObjDoc.SetText nIndex3, TheForm.compound_name.Value
	         nIndex4 = ObjDoc.GetTextIndex("tara_w")
	     ObjDoc.SetText nIndex4, TheForm.tara_w.Value
		 ObjDoc.SetBarcodeData 0, TheForm.barcode.Value
			If (strExport = "") Then
				ObjDoc.DoPrint 0, "0"			'Print
			Else
				strExport = sDataFolder & strExport
				ObjDoc.Export 2, strExport, 180	'Export
				window.navigate strExport
			End If
		End If
	    Set ObjDoc = Nothing
           window.close
	End Sub
-->
</SCRIPT>
</head>
<body onload='DoPrint ""'>
<%
String sid = request.getParameter("container_id");
int id = 0;
boolean ok = true;
try{
    id = Integer.parseInt(sid); }
catch (NumberFormatException  n) {
 ok = false; }
%>
<center>
<%=container.labelData(id, 0)%>
<h2>IF THIS PAGE DOES NOT CLOSE AUTOMATICALLY YOU DO NOT HAVE ACCESS TO THE LABEL PRINT FEATURE!!!</h2>
</center>
</body>
</html>