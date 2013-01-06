/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2003-2006.
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
 *   along with chemicalinventory; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

package chemicalinventory.utility;

import java.net.URLEncoder;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import javax.naming.*;
import javax.sql.*;

import chemicalinventory.context.Attributes;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/** 
 * Utillity class for doing odd tasks.
 **/

public class Util implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6182663190620064458L;
	//number patterns
	public static final String PATTERN = "##0.00";
	public static final String PATTERN_mg = "##0.00###";
	public static final String PATTERN_endless = "#####0.00########";

	//ok not ok images
	public static final String CHECK_MARK = "<img src=\"../images/ok-mark.gif\"/>";
	public static final String X_MARK = "<img src=\"../images/nook-mark.gif\"/>";

	//OK not ok text
	public static final String OK_MARK = "OK";
	public static final String NOTOK_MARK = "NOT OK";

	//operator definitions
	public static final String EQUAL = "EQ"; // =
	public static final String LESS_THAN = "LES"; // <
	public static final String GRATER_THAN = "GRE"; // >
	public static final String LESS_EQUAL = "LESEQ"; //<=
	public static final String GREATER_EQUAL = "GREEQ";// >=

	public Util()
	{
	}

	/**
	 * Takes a status message and replaces it with the appropiate 
	 * image from the image folder
	 * if status = T image = check mark
	 * if status = F image = x mark
	 * else the status is returned as entered.
	 * 
	 * if the text or grafic parameter = 1 use grafic
	 * otherwise text replacement is used.
	 * 
	 * @param status
	 * @param text_or_grafic
	 * @return
	 */
	public static String replaceCheckMark(String status, int text_or_grafic)
	{
		if(status.equalsIgnoreCase("T"))
		{
			if(text_or_grafic == 1)
				return CHECK_MARK;
			else
				return OK_MARK;
		}
		else if(status.equalsIgnoreCase("F"))
		{
			if(text_or_grafic == 1)
				return X_MARK;
			else
				return NOTOK_MARK;
		}
		else
		{
			return status;
		}
	}

	/**
	 * encode an operator takes an predefined operator and returns
	 * the math. character(s) corresponding.
	 * @param operator
	 * @return character
	 */
	public static String decodeOperator(String operator)
	{
		if(operator.equalsIgnoreCase(Util.EQUAL))
		{
			return "=";
		}
		else if(operator.equalsIgnoreCase(Util.LESS_THAN))
		{
			return "<";
		}
		else if(operator.equalsIgnoreCase(Util.GRATER_THAN))
		{
			return ">";
		}
		else if(operator.equalsIgnoreCase(Util.LESS_EQUAL))
		{
			return "<=";
		}
		else if(operator.equalsIgnoreCase(Util.GREATER_EQUAL))
		{
			return ">=";
		}
		else
			return "=";
	}

	/** takes a string and returns the encoded string, ready to display
	 * on an html page.
	 * @param str String.
	 * @return String.
	 */
	public static String encodeTag(String str)
	{
		if(str.indexOf("&") != -1)
		{
			str = str.replaceAll("&", "&#38;");
		}
		if(str.indexOf("\\+") != -1)
		{
			str = str.replaceAll("\\+", "&#43;");
		}
		if(str.indexOf("<") != -1)
		{
			str = str.replaceAll("<", "&#60;");
		}
		if(str.indexOf(">") != -1)
		{
			str = str.replaceAll(">", "&#62;");
		}
		if(str.indexOf("\"") != -1)
		{
			str = str.replaceAll("\"", "&#34;");
		}
		if(str.indexOf("%") != -1)
		{
			str = str.replaceAll("%", "&#37;");
		}
		if(str.indexOf("\\") != -1)
		{
			str = str.replaceAll("\\", "&#92;");
		}
		if(str.indexOf("/") != -1)
		{
			str = str.replaceAll("/", "&#47;");
		}
		return str;
	}


	/**
	 * This method encodes a value for display on html page.
	 * If the value is null or the value is an empty string
	 * the method returns: --
	 * 
	 * Else the method takes any html critical characters and encodes those.
	 * @param str
	 * @return
	 */
	public static String encodeTagAndNull(String str)
	{
		if(str == null || str.equals("") || str.equalsIgnoreCase("null"))
			return "--";

		str = Util.encodeTag(str);

		return str;
	}

	/** takes a string and encodes singel ping (') to double ping ('').
	 * for use when putiing data into the db.
	 * @param val String.
	 * @return String.
	 */
	public static String double_q(String val)
	{
		if (val==null)
			return null;

		if(val.indexOf("'") != -1)
		{
			val = val.replaceAll("'", "''");
		}
		return val;
	}

	/**
	 * Method used to get the entire location as a string. This is done by using
	 * a self join.
	 * @param id String.
	 * @return String.
	 */
	public static String getLocation(String id)
	{
		String location = "";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
					String sql_location = "SELECT l1.location_name, l2.location_name, l3.location_name"+
					" FROM location l1, location l2, location l3"+
					" WHERE l3.id = l2.location_id"+
					" AND l2.id = l1.location_id"+
					" AND l1.id ="+id+"";

					ResultSet result = stmt.executeQuery(sql_location);

					if(result.next())
					{
						location = "&#124; "+encodeTag(result.getString("l3.location_name"))+" &#124;<br/>&#124; "+encodeTag(result.getString("l2.location_name"))+" &#124;<br/>&#124; "+encodeTag(result.getString("l1.location_name"))+" &#124; ";
					}
				}
				con.close();
			}
		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
		}    
		return location;
	}

	/**
	 * Create the location string not in html format, but plain text.
	 * @param id
	 * @return
	 */
	public static String getLocation_notHtml(String id)
	{
		String location = "--";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
					String sql_location = "SELECT l1.location_name, l2.location_name, l3.location_name"+
					" FROM location l1, location l2, location l3"+
					" WHERE l3.id = l2.location_id"+
					" AND l2.id = l1.location_id"+
					" AND l1.id ="+id+"";

					ResultSet result = stmt.executeQuery(sql_location);

					if(result.next())
					{
						location = "/"+result.getString("l3.location_name")+" / "+result.getString("l2.location_name")+" / "+result.getString("l1.location_name")+" /";
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}    

		return location;
	}

	/**
	 * Method used to get the entire location as a string, this version there
	 * is no break <br> in the returned string.
	 * @param id String.
	 * @return String.
	 */
	public static String getLocation_no_br(String id)
	{
		String location_no_br = null;
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();
					String sql_location = "SELECT l1.location_name, l2.location_name, l3.location_name"+
					" FROM location l1, location l2, location l3"+
					" WHERE l3.id = l2.location_id"+
					" AND l2.id = l1.location_id"+
					" AND l1.id ="+id+"";

					ResultSet result = stmt.executeQuery(sql_location);

					if(result.next())
					{
						location_no_br = "&#124; "+encodeTag(result.getString("l3.location_name"))+" &#124; "+encodeTag(result.getString("l2.location_name"))+" &#124; "+encodeTag(result.getString("l1.location_name"))+" &#124; ";
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		return location_no_br;
	}

	/**
	 * Generates a random password of 8 characters
	 * @return The randomized password as a String
	 */
	public static String generatePassword()
	{
		String password = "";
		String values[] = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", 
				"l", "m", "n", "p", "q", "r", "s", "t", "u", "v", "z", 
				"x", "y", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
				"&", "%", "?", "!", "A", "B", "C", "D", "E", "F", "G",
				"H", "J", "K", "L", "M", "N", "P", "Q", "R",
				"S", "T", "U", "V", "Z", "X", "Y"};

		Random ran = new Random();
		int range = values.length;
		int number;

		while (password.length()!=8)
		{
			number = ran.nextInt(range);
			password = password + values[number];
		}
		System.out.println("password random value: "+password);

		return password;
	} 

	/**
	 * Get the chemical name by giving the compound id as a parameter.
	 * The chemical name is html encoded...
	 * @param id
	 * @return
	 */
	public static String getChemicalName(String id)
	{
		String name = "";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT chemical_name FROM compound"+
					" WHERE id = "+id+";";

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())
					{
						name = encodeTag(result.getString("chemical_name"));
					}
				}
				con.close();
				return name;
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}    
		return name;
	}

	/**
	 * Get the chemical name (not html encoded..) by giving the compound id as a parameter.
	 * @param id the id int.
	 * @return
	 */
	public static String getChemicalName3(int id)
	{
		String name = "";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT chemical_name FROM compound"+
					" WHERE id = "+id+";";

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())
					{
						name = result.getString("chemical_name");
					}
				}
				con.close();
				return name;
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}    
		return name;
	}

	/**
	 *Get the chemical name by giving the CONTAINER id as parameter 
	 * @param id
	 * @return
	 */
	public static String getChemicalName2(String id)
	{
		/*
		 * Validate input
		 */
		if(!Util.isValueEmpty(id) || Util.getIntValue(id) == 0)
			return "--";

		String name = "";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT c.chemical_name FROM compound c, container con"+
					" WHERE con.compound_id = c.id"+
					" AND con.id = "+id;

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())
					{
						name = result.getString("c.chemical_name");
						name = encodeTag(name);
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}    
		return name;
	}

	/**
	 * Get the chemicalname using the container id and a connection and statement object
	 * @param container_id
	 * @param con
	 * @param stmt
	 * @return
	 */
	public static String getChemicalName4(String container_id, Connection con, Statement stmt)
	{
		String name = "";
		try{
			if(con != null)  
			{               
				String sql = "SELECT c.chemical_name FROM compound c, container con"+
				" WHERE con.compound_id = c.id"+
				" AND con.id = "+container_id;

				ResultSet result = stmt.executeQuery(sql);

				if(result.next())
				{
					name = result.getString("c.chemical_name");
				}
			}

		}//end of try

		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		return name;
	}

	/**
	 * Get the id of a compound by entering the id of the container
	 * @param id - contaienr id
	 * @return compound id
	 */
	public static String getChemicalId(String id)
	{
		String id_compound = "";
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT c.id FROM compound c, container con"+
					" WHERE con.compound_id = c.id"+
					" AND con.id = "+id;

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())
					{
						id_compound = result.getString("c.id");
					}
				}
				con.close();
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}    
		return id_compound;
	}

	/**
	 * Check if a contaier id is existing in the database
	 * @param id
	 * @return true = valid container id, false = not valid container id.
	 */
	public static boolean isContainerId(String id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id FROM container"+
					" WHERE container.id = "+id;

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())
					{
						con.close();
						return true;
					}
					else
					{
						con.close();
						return false;
					}
				}
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}    
		return false;
	}

	/**
	 * Does a compound has containers connected...??
	 * @param compound_id
	 * @return
	 */
	public static boolean hasCompoundContainers(String compound_id)
	{
		try{              
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql1 = "SELECT compound_id FROM container c, compound cc where c.compound_id = cc.id and c.compound_id= "+compound_id+" AND EMPTY = 'F' AND current_quantity > 0;";

					ResultSet rs1 = stmt.executeQuery(sql1);

					if (rs1.next()) {

						rs1.close();
						con.close();

						return true;
					}
					else {
						rs1.close();
						con.close();

						return true;
					}
				}

				con.close();
			}
			
			return false;
		}//end of try

		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Is the container id existing in the contaner table or history table...
	 * @param id
	 * @return
	 */
	public static boolean isContainerId_history(String id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id FROM container"+
					" WHERE container.id = "+id;

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())//yes an existing and active container
					{
						con.close();
						return true;
					}
					else//check if the container is in the history table
					{
						sql = "SELECT history.id" +
						" FROM history" +
						" WHERE history.table = 'CONTAINER'" +
						" AND history.table_id = "+id+";";

						ResultSet result2 = stmt.executeQuery(sql);

						if(result2.next())//yes an existing container but only in the history table.
						{
							con.close();
							return true;
						}
						else//NO SUCH CONTAINER
						{
							con.close();
							return false;
						}
					}
				}
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}    
		return false;
	}

	/**
	 * Check if the entered batch id is infact a valid batch,
	 * that is or has been registered in the system.
	 * @param id
	 * @return
	 */
	public static boolean isBatchId_history(String id)
	{
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{
					Statement stmt = con.createStatement();

					String sql = "SELECT id FROM batch"+
					" WHERE batch.id = "+id;

					ResultSet result = stmt.executeQuery(sql);

					if(result.next())//yes an existing and active batch
					{
						con.close();
						return true;
					}
					else//check if the batch is in the history table
					{
						sql = "SELECT history.id" +
						" FROM history" +
						" WHERE history.table = 'BATCH'" +
						" AND history.table_id = "+id+";";

						ResultSet result2 = stmt.executeQuery(sql);

						if(result2.next())//yes an existing batch but only in the history table.
						{
							con.close();
							return true;
						}
						else//NO SUCH BATCH
						{
							con.close();
							return false;
						}
					}
				}
			}
		}//end of try

		catch (ClassNotFoundException e) 
		{
			System.out.println(e);
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}    
		return false;
	}

	/**
	 * Get a date in the format yyyy-MM-dd.
	 * @return The date represented as a string.
	 */	
	public static String getDate()
	{
		/*
		 * Get time 
		 */
		Calendar date = Calendar.getInstance(); // Time in future

		/*
		 * Format to specific locale
		 */
		Locale UKlocale = new Locale("en", "GB");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", UKlocale); 

		return (String) formatter.format(date.getTime());
	}	

	/**
	 * Get the date time string in the example format:<br><br>
	 * 
	 * 2006-12-24 23:59:59<br>
	 * yyyy-mm-dd hh:mm:ss
	 * @return String representation of the current system date-time.
	 */
	public static String getDateTime()
	{
		/*
		 * Get time 
		 */
		Calendar datetime = Calendar.getInstance(); // Time in future

		/*
		 * Format to specific locale
		 */
		Locale UKlocale = new Locale("en", "GB");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", UKlocale); 

		return (String) formatter.format(datetime.getTime());
	}

	/**
	 * Get the current time in the format HHMMSS
	 * @return String representation of the current server time;
	 */
	public static String getTime()
	{
		/*
		 * Get time 
		 */
		Calendar time = Calendar.getInstance();

		/*
		 * Format to specific locale
		 */
		Locale UKlocale = new Locale("en", "GB");
		SimpleDateFormat formatter = new SimpleDateFormat("kk:mm:ss", UKlocale); 

		return (String) formatter.format(time.getTime());
	}

	/**
	 * check if the number is a valid double
	 * @param number
	 * @return
	 */
	public static boolean isValidNumber(String number)
	{
		try
		{
			Double.parseDouble(number);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if the number is an integer
	 * @param number
	 * @return
	 */
	public static boolean isValidInt(String number)
	{
		try
		{
			Integer.parseInt(number);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}


	/**
	 * encode a null or empty value to "--".
	 * @param value
	 * @return
	 */
	public static String encodeNullValue(String value)
	{
		if (value == null || value.equals("") || value.equalsIgnoreCase("null"))
			return "--";
		else
			return value;
	}


	/**
	 * Validate the input value.
	 * if the value is empty or null the return value of this method is false
	 * if the value is not empty the return value i TRUE
	 * @param value
	 * @return boolean true = empty value
	 */
	public static boolean isValueEmpty(String value)
	{
		if (value != null && !value.equals("") && !value.equalsIgnoreCase("null"))
			return true;
		else
			return false;
	}


	/**
	 * Format the input value to a text field...
	 * @param value
	 * @return
	 */
	public static String formatNumericResult(String value)
	{
		try
		{
			value = value.replaceAll(",", ".");

			double d = 0.0;

			d = Double.parseDouble(value);

			value = String.valueOf(d);

			return value;
		}
		catch (NumberFormatException e) {
			return "0.0";
		}
	}

	/**
	 * Parse an string value to the integer value
	 * if not a valid int 0 (zero) is returned.
	 * @param number
	 * @return the int value of the string.
	 */
	public static int getIntValue(String number)
	{
		try
		{
			int no = Integer.parseInt(number);
			return no;
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}
}