<%@ page language="java" pageEncoding="UTF-8"%>

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
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 *
-->
<meta name="author" content="Dann Vestergaard">
<title>ChemicalInventory</title>
  </head>
  
<frameset cols="*,1005,*" frameborder="no">
<frame name="right.html" noresize="noresize" name="Right"
scrolling="No">
<frameset rows="95, *">
<frame src="Top.jsp" noresize="noresize" name="Top" scrolling="No"
frameborder="0" marginheight="0" marginwidth="0">
<frameset cols="155, 850">
<frame src="Left.jsp" noresize="noresize" name="Left" scrolling="No" frameborder="0" marginheight="0" marginwidth=
"3">
<frame src="welcome.jsp" noresize="noresize" name="Main" scrolling=
"Auto" frameborder="0">
</frameset>
</frameset>
<frame name="right.html" noresize="noresize" name="Right"
scrolling="No">
<noframes>
<body>
<strong>If you see this text you browser does not support frames
and you cannot, see the chemical inventory, until you get yourself
a new and improved version.</strong>
</body>
</noframes>
</frameset></html>