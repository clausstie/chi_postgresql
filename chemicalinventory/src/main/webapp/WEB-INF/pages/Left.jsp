<%@ page language="java"%>

<%
	String ip_address = request.getServerName();
	String app = request.getContextPath();
	String port = String.valueOf(request.getServerPort());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<!--
 * Description: Application used for managing a chemical storage solution.
 *              This application handles users, compounds, containers,
 *              suppliers, locations, labelprinting and everything else
 *              neded to manage a chemical storage, based on the java technology.
 *				In addition it includes a sample module. This module, is used
 *				to create samples, store results etc.
 *
 * Copyright: 	2004, 2005 Dann Vestergaard and Claus Stie Kallesøe
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
<meta name="author"
	content="Dann Vestergaard &amp; Claus Stie Kalles&oslash;e">
<title>Left pane</title>
<script type="text/javascript" language="JavaScript">
 
    <!--
    // HENTER IMAGES
    if (document.images) 
    {
     img1_on =new Image(); img1_on.src ="images/Button1b.png"; 
     img1_off=new Image(); img1_off.src="images/Button1.png"; 

     img2_on =new Image(); img2_on.src ="images/Button2b.png"; 
     img2_off=new Image(); img2_off.src="images/Button2.png"; 

     img3_on =new Image(); img3_on.src ="images/Button11b.png"; 
     img3_off=new Image(); img3_off.src="images/Button11.png"; 

     img4_on =new Image(); img4_on.src ="images/Button9b.png"; 
     img4_off=new Image(); img4_off.src="images/Button9.png"; 

     img5_on =new Image(); img5_on.src ="images/Button5b.png"; 
     img5_off=new Image(); img5_off.src="images/Button5.png"; 

     img6_on =new Image(); img6_on.src ="images/Button6b.png"; 
     img6_off=new Image(); img6_off.src="images/Button6.png"; 

     img7_on =new Image(); img7_on.src ="images/Button10b.png"; 
     img7_off=new Image(); img7_off.src="images/Button10.png"; 
     
     img8_on =new Image(); img8_on.src ="images/Button12b.png"; 
     img8_off=new Image(); img8_off.src="images/Button12.png";
     
     img9_on =new Image(); img9_on.src ="images/Button13b.png"; 
     img9_off=new Image(); img9_off.src="images/Button13.png"; 
     
     img10_on =new Image(); img10_on.src ="images/Button14b.png"; 
     img10_off=new Image(); img10_off.src="images/Button14.png"; 
     
     img11_on =new Image(); img11_on.src ="images/Button15b.png"; 
     img11_off=new Image(); img11_off.src="images/Button15.png"; 
    }

    function movr(k) {
     if (document.images) 
      eval('document.img'+k+'.src=img'+k+'_on.src');
    }

    function mout(k) {
     if (document.images) 
      eval('document.img'+k+'.src=img'+k+'_off.src');
    }
    -->
     
</script>
</head>
<body>
<table cellspacing="0" cellpadding="0" border="0" width="100%">
	<tr>
		<td><img src="images/empty.png" width="146" height="70"
			alt="Left Menu"></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp"
			onmouseover="movr(1);return true;" onmouseout="mout(1);return true;"
			accesskey="H"><img align="Top" name="img1" border="0"
			src="images/Button1.png" width="146" height="23" alt="Home"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=Search"
			onmouseover="movr(2);return true;" onmouseout="mout(2);return true;"
			accesskey="S"><img align="Top" name="img2" border="0"
			src="images/Button2.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=User"
			onmouseover="movr(3);return true;" onmouseout="mout(3);return true;"
			accesskey="N"><img align="Top" name="img3" border="0"
			src="images/Button11.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=sample"
			onmouseover="movr(10);return true;"
			onmouseout="mout(10);return true;" accesskey="R"><img align="Top"
			name="img10" border="0" src="images/Button14.png" width="146"
			height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=batch"
			onmouseover="movr(11);return true;"
			onmouseout="mout(11);return true;" accesskey="B"><img align="Top"
			name="img11" border="0" src="images/Button15.png" width="146"
			height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/administration/adminController.jsp"
			onmouseover="movr(4);return true;" onmouseout="mout(4);return true;"
			accesskey="A"><img align="Top" name="img4" border="0"
			src="images/Button9.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=check_in_direct"
			onmouseover="movr(5);return true;" onmouseout="mout(5);return true;"
			accesskey="I"><img align="Top" name="img5" border="0"
			src="images/Button5.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=check_out_direct"
			onmouseover="movr(6);return true;" onmouseout="mout(6);return true;"
			accesskey="O"><img align="Top" name="img6" border="0"
			src="images/Button6.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=trans"
			onmouseover="movr(9);return true;" onmouseout="mout(9);return true;"
			accesskey="T"><img align="Top" name="img9" border="0"
			src="images/Button13.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=mail_webmaster"
			onmouseover="movr(8);return true;" onmouseout="mout(8);return true;"
			accesskey="M"><img align="Top" name="img8" border="0"
			src="images/Button12.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><a target="Main"
			href="//<%=ip_address%>:<%=port%><%=app%>/jsp/Controller.jsp?action=misc"
			onmouseover="movr(7);return true;" onmouseout="mout(7);return true;"
			accesskey="L"><img align="Top" name="img7" border="0"
			src="images/Button10.png" width="146" height="23"></a></td>
	</tr>

	<tr>
		<td><img src="images/empty.png" width="146" height="70"
			alt="Left menu"></td>
	</tr>

	<tr>
		<td><img src="images/Silver.png" width="147" height="210"
			alt="Left menu"></td>
	</tr>

	<tr>
		<td><img src="images/Silver2.png" width="147" height="425"
			alt="Left menu"></td>
	</tr>
</table>
</body>
</html>