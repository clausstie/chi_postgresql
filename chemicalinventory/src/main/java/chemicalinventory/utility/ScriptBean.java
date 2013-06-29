/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2004-2009.
 *				  All rights reserved.
 *
 *   overLIB:     overLIB 3.51  -- Copyright Erik Bosrup 1998-2002. All rights reserved.
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
 */
package chemicalinventory.utility;

/**
 * @author Dann Vestergaard
 */
public class ScriptBean {
	
	
	/**
	 * Create a javaScript code block that checks min and max values
	 * in creation/modification of analysis fields.
	 * @param number analysis field number
	 * @return the javascript code.
	 */
	public String ModifyAnalysisScript(String number)
	{
		String script = "";
		
		script = "\n" +
				"if(trim(form.result_min_"+number+".value) != \"\" || trim(form.result_max_"+number+".value) != \"\")\n" +
				"{\n" +
				" if(form.result_type_"+number+".value == \"numeric\")\n" +
				" {\n" +
				"	 if(trim(form.result_min_"+number+".value) != \"\" && trim(form.result_max_"+number+".value) != \"\")\n" +
				" 	 {\n" +
				"  		if(isNegPosNumber(form.result_min_"+number+".value)==false)\n" +
				"   	{\n" +
				"   		alert(\"Result field ("+number+") for min result is not a valid number.\");\n" +
				"		    form.result_min_"+number+".focus();\n" +
				"   		return false;\n" +
				"   	}\n" +
				"   	if(isNegPosNumber(form.result_max_"+number+".value)==false)\n" +
				"   	{\n" +
				"    		alert(\"Result field ("+number+") for max result is not a valid number.\");\n" +
				"    		form.result_max_"+number+".focus();\n" +
				"    		return false;\n" +
				"  		}\n" +
				" 		var min1 = parseFloat(form.result_min_"+number+".value);\n" +
				"   	var max1 = parseFloat(form.result_max_"+number+".value);\n" +
				"	\n" +
				"		if(min1 > max1)\n" +
				"		{\n" +
				" 			alert(\"Minumum value has to be lower than the maximum value!\");\n" +
				"			form.result_min_"+number+".focus();\n" +
				"			return false;\n" +
				" 		}\n" +
				" 	}\n" +
				" 	else if(trim(form.result_min_"+number+".value) != \"\" && trim(form.result_max_"+number+".value) == \"\")\n" +
				" 	{\n" +
				" 		if(isNegPosNumber(form.result_min_"+number+".value)==false)\n" +
				" 		{" +
				"  		 alert(\"Result field ("+number+") for min result is not a valid number.\");\n" +
				"  		 form.result_min_"+number+".focus();\n" +
				"  		 return false;\n" +
				" 		}\n" +
				" 	}\n" +
				" 	else if(trim(form.result_min_"+number+".value) == \"\" && trim(form.result_max_"+number+".value) != \"\")\n" +
				" 	{\n	" +
				"			if(isNegPosNumber(form.result_max_"+number+".value)==false)\n" +
				"			{\n" +
				"   			alert(\"Result field ("+number+") for max result is not a valid number.\");\n" +
				"   			form.result_max_"+number+".focus();\n" +
				"   			return false;\n" +
				"			}\n" +
				" 	}\n" +
				" }\n" +
				"}\n";
		
		return script;	
	}
}
