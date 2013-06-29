//\//////////////////////////////////////////////////////////////////////////////////
//\ 
 //\   Description: Application used for managing a chemical storage solution.
 //\              This application handles users, compounds, containers,
 //\              suppliers, locations, labelprinting and everything else
 //\        	  neded to manage a chemical storage, based on the java technology.
 //\	    	  In addition it includes a sample module. This module, is used
 //\      	      to create samples, store results etc.
 //\
 //\   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesøe 2004-20075.
 //\				All rights reserved.
 //\
 //\   overLIB:     overLIB 3.51  -- Copyright Erik Bosrup 1998-2002. All rights reserved.
 //\
 //\   This file is part of chemicalinventory.
 //\
 //\   chemicalinventory is free software; you can redistribute it and/or modify
 //\   it under the terms of the GNU General Public License as published by
 //\   the Free Software Foundation; either version 2 of the License, or
 //\   any later version.
 //\
 //\   chemicalinventory is distributed in the hope that it will be useful,
 //\   but WITHOUT ANY WARRANTY; without even the implied warranty of
 //\   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //\   GNU General Public License for more details.
 //\
 //\   You should have received a copy of the GNU General Public License
 //\   along with Foobar; if not, write to the Free Software
 //\   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 //\
//\//////////////////////////////////////////////////////////////////////////////////

//Function to remove unvanted spaces from the beginnig and end of a string
function trim(str)
{
  while (str.charAt(str.length - 1) == " ")
  {
    str = str.substring(0, str.length -1);
  }
  while (str.charAt(0) == " ")
  {
    str = str.substring(1, str.length);
  }
 return str;
}

//Function to remove carrige return etc. from a text string.
function trimAll(str) {
 if (str!=null)
 {
    while (str.length > 0 && "\n\r\t ".indexOf(str.charAt(str.length - 1)) != -1)
    {
      str = str.substring(0, str.length - 1);
    }
    while (str.length > 0 && "\n\r\t ".indexOf(str.charAt(0)) != -1)
    {
      str = str.substring(1, str.length);
    }
  }
  return str;
}

//Validate a number field.
function isPositiveInteger(str) 
{
  var pattern = "0123456789."
  var i = 0;
  do {
    var pos = 0;
    for (var j=0; j<pattern.length; j++)
      if (str.charAt(i)==pattern.charAt(j)) {
        pos = 1;
        break;
      }
    i++;
  } while (pos==1 && i<str.length)  
  if (pos==0) 
    return false;
  return true;
}

//valdate if the field contains a valid positive or negative number
function isNegPosNumber(str) 
{
  var pattern = "0123456789.-"
  var i = 0;
  do {
    var pos = 0;
    for (var j=0; j<pattern.length; j++)
      if (str.charAt(i)==pattern.charAt(j)) {
        pos = 1;
        break;
      }
    i++;
  } while (pos==1 && i<str.length)  
  if (pos==0) 
    return false;
  return true;
}

function isNumber(str) 
{
  var pattern = "0123456789"
  var i = 0;
  do {
    var pos = 0;
    for (var j=0; j<pattern.length; j++)
      if (str.charAt(i)==pattern.charAt(j)) {
        pos = 1;
        break;
      }
    i++;
  } while (pos==1 && i<str.length)  
  if (pos==0) 
    return false;
  return true;
}

//Validate a cas number field.
function isValidCas(str) 
{
  var pattern = "0123456789-"
  var i = 0;
  do {
    var pos = 0;
    for (var j=0; j<pattern.length; j++)
      if (str.charAt(i)==pattern.charAt(j)) {
        pos = 1;
        break;
      }
    i++;
  } while (pos==1 && i<str.length)  
  if (pos==0) 
    return false;
  return true;
} 

//Validate a phone number field.
function isValidPhoneNumber(str) {
  var pattern = "0123456789()-+"
  var i = 0;
  do {
    var pos = 0;
    for (var j=0; j<pattern.length; j++)
      if (str.charAt(i)==pattern.charAt(j)) {
        pos = 1;
        break;
      }
    i++;
  } while (pos==1 && i<str.length)  
  if (pos==0) 
    return false;
  return true;
} 

//Validate Email adress.
function isValidEmail(str)
{
 var i = 0;
 var regexp = /^[\w\.\-_]+@[\w\-_]+\.[\w\.\-_]{2,}$/i;
  tekst = str;
  if (!regexp.test(tekst))
  {
      i=1;
  }
return i;
}

//function to either select or deselect all options in a select box.
function selectAllOptions(select, status) 
{
    var aOptions = select.options;
    for (var i=0; i<aOptions.length; i++)
    {
        if(status == 1)
        {
            aOptions[i].selected = true;
        }
        if(status == 2)
        {
            aOptions[i].selected = false;
        }
    }
}

//VALIDATE FORM FIELDS WHEN CREATING A NEW ANALYSIS
function validateForm_fields(form) 
{
 //check row 1
 if(trim(form.result_min1.value) != "" || trim(form.result_max1.value) != "")
 {
 	if(form.result_type1.value == "numeric")
 	{
	 	if(trim(form.result_min1.value) != "" && trim(form.result_max1.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min1.value)==false)
		    {
		        alert("Result field for min result in row 1 is not a valid number.");
		        form.result_min1.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max1.value)==false)
		    {
		        alert("Result field for max result in row 1 is not a valid number.");
		        form.result_max1.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min1 = parseFloat(form.result_min1.value);
			var max1 = parseFloat(form.result_max1.value);
			
			if(min1 > max1)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min1.focus();
				return false;
			}
		}
    	else if(trim(form.result_min1.value) != "" && trim(form.result_max1.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min1.value)==false)
		    {
		        alert("Result field for min result in row 1 is not a valid number.");
		        form.result_min1.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min1.value) == "" && trim(form.result_max1.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max1.value)==false)
		    {
		        alert("Result field for max result in row 1 is not a valid number.");
		        form.result_max1.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 2
 if(trim(form.result_min2.value) != "" || trim(form.result_max2.value) != "")
 {
 	if(form.result_type2.value == "numeric")
 	{
	 	if(trim(form.result_min2.value) != "" && trim(form.result_max2.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min2.value)==false)
		    {
		        alert("Result field for min result in row 2 is not a valid number.");
		        form.result_min2.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max2.value)==false)
		    {
		        alert("Result field for max result in row 2 is not a valid number.");
		        form.result_max2.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.result_min2.value);
			var max = parseFloat(form.result_max2.value);
						
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min2.focus();
				return false;
			}
		}
    	else if(trim(form.result_min2.value) != "" && trim(form.result_max2.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min2.value)==false)
		    {
		        alert("Result field for min result in row 2 is not a valid number.");
		        form.result_min2.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min2.value) == "" && trim(form.result_max2.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max2.value)==false)
		    {
		        alert("Result field for max result in row 2 is not a valid number.");
		        form.result_max2.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 3
 if(trim(form.result_min3.value) != "" || trim(form.result_max3.value) != "")
 {
 	if(form.result_type3.value == "numeric")
 	{
	 	if(trim(form.result_min3.value) != "" && trim(form.result_max3.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min3.value)==false)
		    {
		        alert("Result field for min result in row 3 is not a valid number.");
		        form.result_min3.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max3.value)==false)
		    {
		        alert("Result field for max result in row 3 is not a valid number.");
		        form.result_max3.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min3.value);
			var max = parseFloat(form.result_max3.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min3.focus();
				return false;
			}
		}
    	else if(trim(form.result_min3.value) != "" && trim(form.result_max3.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min3.value)==false)
		    {
		        alert("Result field for min result in row 3 is not a valid number.");
		        form.result_min3.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min3.value) == "" && trim(form.result_max3.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max3.value)==false)
		    {
		        alert("Result field for max result in row 3 is not a valid number.");
		        form.result_max3.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 4
 if(trim(form.result_min4.value) != "" || trim(form.result_max4.value) != "")
 {
 	if(form.result_type4.value == "numeric")
 	{
	 	if(trim(form.result_min4.value) != "" && trim(form.result_max4.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min4.value)==false)
		    {
		        alert("Result field for min result in row 4 is not a valid number.");
		        form.result_min4.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max4.value)==false)
		    {
		        alert("Result field for max result in row 4 is not a valid number.");
		        form.result_max4.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min4.value);
			var max = parseFloat(form.result_max4.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min4.focus();
				return false;
			}
		}
    	else if(trim(form.result_min4.value) != "" && trim(form.result_max4.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min4.value)==false)
		    {
		        alert("Result field for min result in row 4 is not a valid number.");
		        form.result_min4.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min4.value) == "" && trim(form.result_max4.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max4.value)==false)
		    {
		        alert("Result field for max result in row 4 is not a valid number.");
		        form.result_max4.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 5
 if(trim(form.result_min5.value) != "" || trim(form.result_max5.value) != "")
 {
 	if(form.result_type5.value == "numeric")
 	{
	 	if(trim(form.result_min5.value) != "" && trim(form.result_max5.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min5.value)==false)
		    {
		        alert("Result field for min result in row 5 is not a valid number.");
		        form.result_min5.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max5.value)==false)
		    {
		        alert("Result field for max result in row 5 is not a valid number.");
		        form.result_max5.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min5.value);
			var max = parseFloat(form.result_max5.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min5.focus();
				return false;
			}
		}
    	else if(trim(form.result_min5.value) != "" && trim(form.result_max5.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min5.value)==false)
		    {
		        alert("Result field for min result in row 5 is not a valid number.");
		        form.result_min5.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min5.value) == "" && trim(form.result_max5.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max5.value)==false)
		    {
		        alert("Result field for max result in row 5 is not a valid number.");
		        form.result_max5.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 6
 if(trim(form.result_min6.value) != "" || trim(form.result_max6.value) != "")
 {
 	if(form.result_type6.value == "numeric")
 	{
	 	if(trim(form.result_min6.value) != "" && trim(form.result_max6.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min6.value)==false)
		    {
		        alert("Result field for min result in row 6 is not a valid number.");
		        form.result_min6.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max6.value)==false)
		    {
		        alert("Result field for max result in row 6 is not a valid number.");
		        form.result_max6.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min6.value);
			var max = parseFloat(form.result_max6.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min6.focus();
				return false;
			}
		}
    	else if(trim(form.result_min6.value) != "" && trim(form.result_max6.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min6.value)==false)
		    {
		        alert("Result field for min result in row 6 is not a valid number.");
		        form.result_min6.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min6.value) == "" && trim(form.result_max6.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max6.value)==false)
		    {
		        alert("Result field for max result in row 6 is not a valid number.");
		        form.result_max6.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 7
 if(trim(form.result_min7.value) != "" || trim(form.result_max7.value) != "")
 {
 	if(form.result_type7.value == "numeric")
 	{
	 	if(trim(form.result_min7.value) != "" && trim(form.result_max7.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min7.value)==false)
		    {
		        alert("Result field for min result in row 7 is not a valid number.");
		        form.result_min7.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max7.value)==false)
		    {
		        alert("Result field for max result in row 7 is not a valid number.");
		        form.result_max7.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min7.value);
			var max = parseFloat(form.result_max7.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min7.focus();
				return false;
			}
		}
    	else if(trim(form.result_min7.value) != "" && trim(form.result_max7.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min7.value)==false)
		    {
		        alert("Result field for min result in row 7 is not a valid number.");
		        form.result_min7.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min7.value) == "" && trim(form.result_max7.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max7.value)==false)
		    {
		        alert("Result field for max result in row 7 is not a valid number.");
		        form.result_max7.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 8
 if(trim(form.result_min8.value) != "" || trim(form.result_max8.value) != "")
 {
 	if(form.result_type8.value == "numeric")
 	{
	 	if(trim(form.result_min8.value) != "" && trim(form.result_max8.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min8.value)==false)
		    {
		        alert("Result field for min result in row 8 is not a valid number.");
		        form.result_min8.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max8.value)==false)
		    {
		        alert("Result field for max result in row 8 is not a valid number.");
		        form.result_max8.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min8.value);
			var max = parseFloat(form.result_max8.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min8.focus();
				return false;
			}
		}
    	else if(trim(form.result_min8.value) != "" && trim(form.result_max8.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min8.value)==false)
		    {
		        alert("Result field for min result in row 8 is not a valid number.");
		        form.result_min8.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min8.value) == "" && trim(form.result_max8.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max8.value)==false)
		    {
		        alert("Result field for max result in row 8 is not a valid number.");
		        form.result_max8.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 9
 if(trim(form.result_min9.value) != "" || trim(form.result_max9.value) != "")
 {
 	if(form.result_type9.value == "numeric")
 	{
	 	if(trim(form.result_min9.value) != "" && trim(form.result_max9.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min9.value)==false)
		    {
		        alert("Result field for min result in row 9 is not a valid number.");
		        form.result_min9.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max9.value)==false)
		    {
		        alert("Result field for max result in row 9 is not a valid number.");
		        form.result_max9.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min9.value);
			var max = parseFloat(form.result_max9.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min9.focus();
				return false;
			}
		}
    	else if(trim(form.result_min9.value) != "" && trim(form.result_max9.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min9.value)==false)
		    {
		        alert("Result field for min result in row 9 is not a valid number.");
		        form.result_min9.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min9.value) == "" && trim(form.result_max9.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max9.value)==false)
		    {
		        alert("Result field for max result in row 9 is not a valid number.");
		        form.result_max9.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 10
 if(trim(form.result_min10.value) != "" || trim(form.result_max10.value) != "")
 {
 	if(form.result_type10.value == "numeric")
 	{
	 	if(trim(form.result_min10.value) != "" && trim(form.result_max10.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.result_min10.value)==false)
		    {
		        alert("Result field for min result in row 10 is not a valid number.");
		        form.result_min10.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.result_max10.value)==false)
		    {
		        alert("Result field for max result in row 10 is not a valid number.");
		        form.result_max10.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.result_min10.value);
			var max = parseFloat(form.result_max10.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.result_min10.focus();
				return false;
			}
		}
    	else if(trim(form.result_min10.value) != "" && trim(form.result_max10.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.result_min10.value)==false)
		    {
		        alert("Result field for min result in row 10 is not a valid number.");
		        form.result_min10.focus();
		        return false;
		    }
    	}
    	else if(trim(form.result_min10.value) == "" && trim(form.result_max10.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max10.value)==false)
		    {
		        alert("Result field for max result in row 10 is not a valid number.");
		        form.result_max10.focus();
		        return false;
		    }
    	}
    }
 }
 //everything ok return ok!
 return true;
}


//Validate the fields when adding new field to an analysis..
//VALIDATE FORM FIELDS WHEN MODIFYING AN ANALYSIS
function validateForm_Newfields(form) 
{
 //check row 1
 if(trim(form.new_result_min1.value) != "" || trim(form.new_result_max1.value) != "")
 {
 	if(form.new_result_type1.value == "numeric")
 	{
	 	if(trim(form.new_result_min1.value) != "" && trim(form.new_result_max1.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min1.value)==false)
		    {
		        alert("result field for min result in row 1 is not a valid number.");
		        form.new_result_min1.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max1.value)==false)
		    {
		        alert("result field for max result in row 1 is not a valid number.");
		        form.new_result_max1.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min1 = parseFloat(form.new_result_min1.value);
			var max1 = parseFloat(form.new_result_max1.value);
			
			if(min1 > max1)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min1.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min1.value) != "" && trim(form.new_result_max1.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min1.value)==false)
		    {
		        alert("result field for min result in row 1 is not a valid number.");
		        form.new_result_min1.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min1.value) == "" && trim(form.new_result_max1.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max1.value)==false)
		    {
		        alert("result field for max _result in row 1 is not a valid number.");
		        form.new_result_max1.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 2
 if(trim(form.new_result_min2.value) != "" || trim(form.new_result_max2.value) != "")
 {
 	if(form.new_result_type2.value == "numeric")
 	{
	 	if(trim(form.new_result_min2.value) != "" && trim(form.new_result_max2.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min2.value)==false)
		    {
		        alert("result field for min result in row 2 is not a valid number.");
		        form.new_result_min2.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max2.value)==false)
		    {
		        alert("result field for max result in row 2 is not a valid number.");
		        form.new_result_max2.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min2.value);
			var max = parseFloat(form.new_result_max2.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min2.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min2.value) != "" && trim(form.new_result_max2.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min2.value)==false)
		    {
		        alert("result field for min result in row 2 is not a valid number.");
		        form.new_result_min2.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min2.value) == "" && trim(form.new_result_max2.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max2.value)==false)
		    {
		        alert("result field for max result in row 2 is not a valid number.");
		        form.new_result_max2.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 3
 if(trim(form.new_result_min3.value) != "" || trim(form.new_result_max3.value) != "")
 {
 	if(form.new_result_type3.value == "numeric")
 	{
	 	if(trim(form.new_result_min3.value) != "" && trim(form.new_result_max3.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min3.value)==false)
		    {
		        alert("result field for min result in row 3 is not a valid number.");
		        form.new_result_min3.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max3.value)==false)
		    {
		        alert("result field for max result in row 3 is not a valid number.");
		        form.new_result_max3.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min3.value);
			var max = parseFloat(form.new_result_max3.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min3.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min3.value) != "" && trim(form.new_result_max3.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min3.value)==false)
		    {
		        alert("result field for min result in row 3 is not a valid number.");
		        form.new_result_min3.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min3.value) == "" && trim(form.new_result_max3.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max3.value)==false)
		    {
		        alert("result field for max result in row 3 is not a valid number.");
		        form.new_result_max3.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 4
 if(trim(form.new_result_min4.value) != "" || trim(form.new_result_max4.value) != "")
 {
 	if(form.new_result_type4.value == "numeric")
 	{
	 	if(trim(form.new_result_min4.value) != "" && trim(form.new_result_max4.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min4.value)==false)
		    {
		        alert("result field for min result in row 4 is not a valid number.");
		        form.new_result_min4.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max4.value)==false)
		    {
		        alert("result field for max result in row 4 is not a valid number.");
		        form.new_result_max4.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min4.value);
			var max = parseFloat(form.new_result_max4.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min4.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min4.value) != "" && trim(form.new_result_max4.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min4.value)==false)
		    {
		        alert("result field for min result in row 4 is not a valid number.");
		        form.new_result_min4.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min4.value) == "" && trim(form.new_result_max4.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max4.value)==false)
		    {
		        alert("result field for max result in row 4 is not a valid number.");
		        form.new_result_max4.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 5
 if(trim(form.new_result_min5.value) != "" || trim(form.new_result_max5.value) != "")
 {
 	if(form.new_result_type5.value == "numeric")
 	{
	 	if(trim(form.new_result_min5.value) != "" && trim(form.new_result_max5.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min5.value)==false)
		    {
		        alert("result field for min result in row 5 is not a valid number.");
		        form.new_result_min5.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max5.value)==false)
		    {
		        alert("result field for max result in row 5 is not a valid number.");
		        form.new_result_max5.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min5.value);
			var max = parseFloat(form.new_result_max5.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min5.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min5.value) != "" && trim(form.new_result_max5.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min5.value)==false)
		    {
		        alert("result field for min result in row 5 is not a valid number.");
		        form.new_result_min5.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min5.value) == "" && trim(form.new_result_max5.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.result_max5.value)==false)
		    {
		        alert("result field for max result in row 5 is not a valid number.");
		        form.new_result_max5.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 6
 if(trim(form.new_result_min6.value) != "" || trim(form.new_result_max6.value) != "")
 {
 	if(form.new_result_type6.value == "numeric")
 	{
	 	if(trim(form.new_result_min6.value) != "" && trim(form.new_result_max6.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min6.value)==false)
		    {
		        alert("result field for min result in row 6 is not a valid number.");
		        form.new_result_min6.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max6.value)==false)
		    {
		        alert("result field for max result in row 6 is not a valid number.");
		        form.new_result_max6.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min6.value);
			var max = parseFloat(form.new_result_max6.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min6.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min6.value) != "" && trim(form.new_result_max6.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min6.value)==false)
		    {
		        alert("result field for min result in row 6 is not a valid number.");
		        form.new_result_min6.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min6.value) == "" && trim(form.new_result_max6.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max6.value)==false)
		    {
		        alert("result field for max result in row 6 is not a valid number.");
		        form.new_result_max6.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 7
 if(trim(form.new_result_min7.value) != "" || trim(form.new_result_max7.value) != "")
 {
 	if(form.new_result_type7.value == "numeric")
 	{
	 	if(trim(form.new_result_min7.value) != "" && trim(form.new_result_max7.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min7.value)==false)
		    {
		        alert("result field for min result in row 7 is not a valid number.");
		        form.new_result_min7.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max7.value)==false)
		    {
		        alert("result field for max result in row 7 is not a valid number.");
		        form.new_result_max7.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var min = parseFloat(form.new_result_min7.value);
			var max = parseFloat(form.new_result_max7.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min7.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min7.value) != "" && trim(form.new_result_max7.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min7.value)==false)
		    {
		        alert("result field for min result in row 7 is not a valid number.");
		        form.new_result_min7.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min7.value) == "" && trim(form.new_result_max7.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max7.value)==false)
		    {
		        alert("new_result field for max new_result in row 7 is not a valid number.");
		        form.new_result_max7.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 8
 if(trim(form.new_result_min8.value) != "" || trim(form.new_result_max8.value) != "")
 {
 	if(form.new_result_type8.value == "numeric")
 	{
	 	if(trim(form.new_result_min8.value) != "" && trim(form.new_result_max8.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min8.value)==false)
		    {
		        alert("result field for min result in row 8 is not a valid number.");
		        form.new_result_min8.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max8.value)==false)
		    {
		        alert("result field for max result in row 8 is not a valid number.");
		        form.new_result_max8.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.new_result_min8.value);
			var max = parseFloat(form.new_result_max8.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min8.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min8.value) != "" && trim(form.new_result_max8.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min8.value)==false)
		    {
		        alert("Result field for min result in row 8 is not a valid number.");
		        form.new_result_min8.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min8.value) == "" && trim(form.new_result_max8.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max8.value)==false)
		    {
		        alert("Result field for max result in row 8 is not a valid number.");
		        form.new_result_max8.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 9
 if(trim(form.new_result_min9.value) != "" || trim(form.new_result_max9.value) != "")
 {
 	if(form.new_result_type9.value == "numeric")
 	{
	 	if(trim(form.new_result_min9.value) != "" && trim(form.new_result_max9.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min9.value)==false)
		    {
		        alert("result field for min result in row 9 is not a valid number.");
		        form.new_result_min9.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max9.value)==false)
		    {
		        alert("result field for max result in row 9 is not a valid number.");
		        form.new_result_max9.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.new_result_min9.value);
			var max = parseFloat(form.new_result_max9.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min9.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min9.value) != "" && trim(form.new_result_max9.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min9.value)==false)
		    {
		        alert("result field for min result in row 9 is not a valid number.");
		        form.new_result_min9.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min9.value) == "" && trim(form.new_result_max9.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max9.value)==false)
		    {
		        alert("result field for max result in row 9 is not a valid number.");
		        form.new_result_max9.focus();
		        return false;
		    }
    	}
    }
 }
 
 //check row 10
 if(trim(form.new_result_min10.value) != "" || trim(form.new_result_max10.value) != "")
 {
 	if(form.new_result_type10.value == "numeric")
 	{
	 	if(trim(form.new_result_min10.value) != "" && trim(form.new_result_max10.value) != "")
	 	{
			//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min10.value)==false)
		    {
		        alert("result field for min result in row 10 is not a valid number.");
		        form.new_result_min10.focus();
		        return false;
		    }
		    
		    //check the max field
		    if(isNegPosNumber(form.new_result_max10.value)==false)
		    {
		        alert("result field for max result in row 10 is not a valid number.");
		        form.new_result_max10.focus();
		        return false;
		    }

			//check if the min number is actually lower than the max number
			var  min = parseFloat(form.new_result_min10.value);
			var max = parseFloat(form.new_result_max10.value);
			
			if(min > max)
			{
				alert("Minumum value has to be lower than the maximum value!");
				form.new_result_min10.focus();
				return false;
			}
		}
    	else if(trim(form.new_result_min10.value) != "" && trim(form.new_result_max10.value) == "")
    	{
    		//first check the min field	 	
		 	if(isNegPosNumber(form.new_result_min10.value)==false)
		    {
		        alert("result field for min result in row 10 is not a valid number.");
		        form.new_result_min10.focus();
		        return false;
		    }
    	}
    	else if(trim(form.new_result_min10.value) == "" && trim(form.new_result_max10.value) != "")
    	{	
    		//check the max field
		    if(isNegPosNumber(form.new_result_max10.value)==false)
		    {
		        alert("result field for max result in row 10 is not a valid number.");
		        form.new_result_max10.focus();
		        return false;
		    }
    	}
    }
 }
 //everything ok return ok!
 return true;
}