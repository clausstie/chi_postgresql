<%@ page language="java" %>
<jsp:directive.page import="chemicalinventory.context.Attributes"/>
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
 *   along with Foobar; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
-->
<title>Miscellaneous information about chemicalinventory</title>
<link rel="stylesheet" type="text/css" href="/chemicalinventory/Style.css">
</head>
<body class="mol">
<center>
<h2>On this page you will find information about the chemical inventory.</h2>
<br><br>

<table style="border-style: dotted; border-width: 2px;" width="750">
    <tr>
        <td>
            <h3>Shortcut keys:</h3>
            <p>As this application matures, the developors will try to make it
            as easy to use as possible. Where usefull we will make menus, links and
            other ressources available trough shortcut keys. The list of shortcut keys shown 
            here is very limited, but will be extended through future development.</p>

            <p>In Internet Explorer a shortcut key is activated by pressing 'alt'+key and enter</p>

            <h5>The main menus in the top and left of the application is activated by the following shortcut keys:</h5>
            <ul>
                <LI>HOME: 'H'</LI>
                <LI>SEARCH: 'S'</LI>
                <LI>INFORMATION: 'Q'</LI>
                <LI>SAMPLE: 'R'</LI>
                <LI>BATCH: 'B'</LI>
                <LI>ADMINISTRATION: 'A'</LI>
                <LI>CHECK-IN: 'I'</LI>
                <LI>CHECK-OUT: 'O'</LI>
                <LI>TRANSFER: 'T'</LI>
                <LI>MESSAGES: 'M'</LI>
                <LI>CHANGE PASSWORD: 'C'</LI>
                <LI>MISCELLANEOUS: 'L'</LI>
                <LI>LOGOUT: 'X'</LI>
            </ul><BR>
        </td>
     </tr>
</table><br>
<table style="border-style: dotted; border-width: 2px;" width="750">
    <tr>
        <td>
            <h3>CI information</h3>
            <p>Further information for the system is available <a href="http://www.chemicalinventory.org" target="blank">here!</a>
            </p>
            <p>At this address you can try an online <a href="http://www.ci.dfuni.dk/chemicalinventory" target="blank">demo system</a>
            Make a <a href="http://www.chemicalinventory.org/donate/StartDonate.html" target="blank">donation</a> 
            if you feel this system adds value to your organisation.
            </p>
        </td>
     </tr>
</table>
<br>
<table style="border-style: dotted; border-width: 2px;" width="750">
    <tr>
        <td>
            <h3>The future...</h3>
            <p>The system is constantly developing... Take part in the next generation of online
            laboratory information management software. Read the proposal for creating the next generation of 
            chemicalinventory, <a href="<%=Attributes.APPLICATION%>/text/Proposal.html">HERE.</a>
            </p>
        </td>
     </tr>
</table>
<br>
<table style="border-style: dotted; border-width: 2px;" width="750">
    <tr>
        <td>
            <h3>Questions...??</h3>
            <p>For any questions, ideas etc. contact the chemicalinventory developers <a target="blank" href="http://www.chemicalinventory.org/contact/StartContact.html">
            by mail or at sourceforge.</a>
            </p>
        </td>
     </tr>
</table>
</center>
</body>
</html>
