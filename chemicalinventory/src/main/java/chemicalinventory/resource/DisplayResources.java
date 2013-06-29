/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallese 2003-2006.
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
package chemicalinventory.resource;

import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class DisplayResources implements Serializable {
	
	public DisplayResources()
	{
	}
	
	private String compound_name = "";
	private String compound_id = "";
	private String cas_number = "";
	private ResultSet resource_set;
	private String java_script = "";
	private String form_elements = "";
	private String link_element = "";

	/**
	 * Create all the elements for the html page to display the resources.
	 * @param role true = adm user, false = normal user
	 * @param compound_id
	 * @return 
	 */
	public boolean display(boolean role, String compound_id)
	{
		this.compound_id = compound_id;
		
		this.java_script = "";
		this.form_elements = "";
		this.link_element = "";
		
		//first get information about the compound
		if (!getChemicalNameAndCas(compound_id))
			return false;
				
		/*
		 * Create the different parts needed to display the resources on the html page.
		 * running through the result set received.
		 */
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
					
					String sql = "SELECT text_id, use_text, mouse_text, alternative_text, icon," +
					" icon_disabled, visibility, internal, position, include_1, include_2," +
					" include_3, include_4, id_1, id_2, id_3, id_4, cas_required, url, resource, sticky_text" +
					" FROM resources" +
					" ORDER BY position;";
					
					resource_set = stmt.executeQuery(sql);
					
					while(resource_set.next())
					{
						//	              internal or external resource
						if(resource_set.getString("internal").equalsIgnoreCase("internal"))
						{
							//is the resource for admnistrators only
							if(resource_set.getString("visibility").equalsIgnoreCase("adm"))//administrators only
							{
								if(role)
								{
									//create the resource as internal, with full visibility
									createElements_internal(resource_set.getString("text_id"), resource_set.getString("use_text"), 
											resource_set.getString("mouse_text"), resource_set.getString("icon"), 
											resource_set.getString("include_1"), resource_set.getString("include_2"), 
											resource_set.getString("include_3"), resource_set.getString("resource"), 
											resource_set.getString("sticky_text"));
								}
								else
								{
									//create the resource as internal disabled.
									createElements_disabled(resource_set.getString("icon_disabled"), resource_set.getString("alternative_text"));
								}
							}
							else//the resource should be visibel to all user.
							{
								//Create the resource as internal, with full visibility
								createElements_internal(resource_set.getString("text_id"), resource_set.getString("use_text"), 
										resource_set.getString("mouse_text"), resource_set.getString("icon"), 
										resource_set.getString("include_1"), resource_set.getString("include_2"), 
										resource_set.getString("include_3"), resource_set.getString("resource"), 
										resource_set.getString("sticky_text"));
							}
						}
						
						else//the resource is external
						{
							//is the resource required to have cas number
							if(resource_set.getString("cas_required").equalsIgnoreCase("Y"))//cas is required
							{
								if(this.cas_number != null && 
								   !this.cas_number.equalsIgnoreCase("null") && 
								   !this.cas_number.equalsIgnoreCase("") &&
								   !this.cas_number.equals("--"))
								{
									//create the resource as external, with full visibility
									createElements_external(resource_set.getString("text_id"), 
											resource_set.getString("use_text"), resource_set.getString("mouse_text"), 
											resource_set.getString("icon"), resource_set.getString("include_1"), 
											resource_set.getString("include_2"), resource_set.getString("include_3"), resource_set.getString("include_4"), 
											resource_set.getString("id_1"), resource_set.getString("id_2"), 
											resource_set.getString("id_3"), resource_set.getString("id_4"), resource_set.getString("url"),  
											resource_set.getString("sticky_text")); 
								}
								else
								{
									//create the resource as external disabled.
									createElements_disabled(resource_set.getString("icon_disabled"), resource_set.getString("alternative_text"));
								}
							}
							else//the resource should be visibel not dependent on cas is present
							{
								//Create the resource as external, with full visibility
								createElements_external(resource_set.getString("text_id"), 
										resource_set.getString("use_text"), resource_set.getString("mouse_text"), 
										resource_set.getString("icon"), resource_set.getString("include_1"), 
										resource_set.getString("include_2"), resource_set.getString("include_3"), resource_set.getString("include_4"), 
										resource_set.getString("id_1"), resource_set.getString("id_2"), 
										resource_set.getString("id_3"), resource_set.getString("id_4"), resource_set.getString("url"),  
										resource_set.getString("sticky_text"));
							}
						}
						
					}
				}
				con.close();
				return true;
			}
		}//end of try
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
	return false;
	}
	
	/**
	 * Crate javascript, form element and icon with on mouse over effect for
	 * an internal resource. 
	 * 
	 * @param form_name The name of the form = text_id
	 * @param use_text use on mouse over text?
	 * @param mouse_text the text to use
	 * @param icon the icon to use
	 * @param include_1 include element
	 * @param include_2 include element
	 * @param include_3 include element
	 * @param resource The resource in the application (eg. create new container, compound report etc)
	 * @param sticky_text Is the text sticky??
	 * 
	 * @return
	 */
	public boolean createElements_internal(String form_name, String use_text, String mouse_text,
										  String icon, String include_1, String include_2, String include_3,
										  String resource, String sticky_text)
	{
		String javascript_function = "submit"+form_name+"()";
		String onmouseover = "";
		String sticky = "";
		String base = Attributes.getBaseForResource(resource);
		String action = "";
		String action1 = "";
		String action2 = "";
		String action3 = "";
				
		if(include_1.equalsIgnoreCase(Attributes.COMPOUND_NAME))
		{
			try {
				action1 = "&chemical_name="+URLEncoder.encode(this.compound_name, "UTF-8");
			} catch (Exception e) {
				action1 = "";
			}
		}
		
		if(include_2.equalsIgnoreCase(Attributes.COMPOUND_ID))
		{
			action2 = "&compound_id="+this.compound_id;
		}
		
		if(include_3.equalsIgnoreCase(Attributes.COMPOUND_CAS))
		{
			action3 = "&cas_number="+this.cas_number;
		}
		
		
		action = base+"?action="+resource+action1+action2+action3;
		
		this.form_elements = this.form_elements + "</form>\n";
		
		//create the icon
		if(use_text.equals("Y") && Util.isValueEmpty(mouse_text))
		{
			if(sticky_text.equals("Y"))
				sticky = "STICKY,";
				
			//decode mouse text
			mouse_text = replaceWithValues(mouse_text);
			
			onmouseover = "onmouseover=\"return overlib('"+mouse_text+"', "+sticky+" BORDER, 2);\" onmouseout=\"return nd();\"><img src=\"../images/icons/"+icon+"\" border=\"0\">";
		}
		else
			onmouseover = "><img src=\"../images/icons/"+icon+"\" border=\"0\">";
		
		this.link_element = this.link_element + "<a href=\""+action+"\" "+onmouseover+"</a>&nbsp;\n";
		
		return true;
	}
	
	
	/**
	 * 
	 * Create javascript, form elements, and icons for an external element.
	 * 
	 * @param form_name
	 * @param use_text
	 * @param mouse_text
	 * @param icon
	 * @param include_1
	 * @param include_2
	 * @param include_3
	 * @param include_4
	 * @param id_1
	 * @param id_2
	 * @param id_3
	 * @param id_4
	 * @param url
	 * @param sticky_text
	 * @return
	 */
	public boolean createElements_external(String form_name, String use_text, String mouse_text,
			String icon, String include_1, String include_2, String include_3, String include_4, String id_1,
			String id_2, String id_3, String id_4, String url, String sticky_text)
	{
		String javascript_function = "submit"+form_name+"()";
		String onmouseover = "";
		String sticky = "";		
		String method = "post";
		
		//create the javascript
		this.java_script = this.java_script + "\n function "+javascript_function+"\n"+
		"{\n"+
		" document."+form_name+".submit();\n"+
		"}";
		
		//create the form elements
		
		//decode the url
		url = replaceWithValues(url);
		
		if(!Util.isValueEmpty(include_1) && !Util.isValueEmpty(include_2) && !Util.isValueEmpty(include_3))
			method = "get";
		
		this.form_elements = this.form_elements + "\n <form action=\""+url+"\" method=\""+method+"\" name=\""+form_name+"\" target=\"blank\">\n";
		
		if(Util.isValueEmpty(include_1))
		{
			//decode the include_1
			include_1 = replaceWithValues(include_1);			
			
			this.form_elements = this.form_elements + "<input type=\"hidden\" id=\""+id_1+"\" name=\""+id_1+"\" value=\""+include_1+"\">\n";
		}
		
		if(Util.isValueEmpty(include_2))
		{
			//decode the include_2
			include_2 = replaceWithValues(include_2);
			
			this.form_elements = this.form_elements + "<input type=\"hidden\" id=\""+id_2+"\" name=\""+id_2+"\" value=\""+include_2+"\">\n";
		}
		
		if(Util.isValueEmpty(include_3))
		{
			//decode the include_3
			include_3 = replaceWithValues(include_3);
			
			this.form_elements = this.form_elements +  "<input type=\"hidden\" id=\""+id_3+"\" name=\""+id_3+"\" value=\""+include_3+"\">\n";
		}
		
		if(Util.isValueEmpty(include_4))
		{
			//decode the include_4
			include_4 = replaceWithValues(include_4);
			
			this.form_elements = this.form_elements +  "<input type=\"hidden\" id=\""+id_4+"\" name=\""+id_4+"\" value=\""+include_4+"\">\n";
		}
		
		this.form_elements = this.form_elements + "</form>\n";
		
		//create the icon
		if(use_text.equals("Y") && Util.isValueEmpty(mouse_text))
		{
			if(sticky_text.equals("Y"))
				sticky = "STICKY,";

			//decode mouse text
			mouse_text = replaceWithValues(mouse_text);
			
			onmouseover = "onmouseover=\"return overlib('"+mouse_text+"', "+sticky+" BORDER, 2);\" onmouseout=\"return nd();\"><img src=\"../images/icons/"+icon+"\" border=\"0\">";
		}
		else
			onmouseover = "><img src=\"../images/icons/"+icon+"\" border=\"0\">";
		
		this.link_element = this.link_element + "<a href=\"Javascript: "+javascript_function+"\" "+onmouseover+"</a>&nbsp;\n";
		
		return true;
	}
	
	/**
	 *Create the elements for a disabled icon, there has to be an icon in the displayed icon parameter 
	 *else this is skipped. 
	 * @param icon_disabled
	 * @param alternative_text
	 */
	public void createElements_disabled(String icon_disabled, String alternative_text)
	{
		String onmouseover = "";
		
		if(icon_disabled != null && !icon_disabled.equals("") && !icon_disabled.equalsIgnoreCase("null"))
		{
			//create the icon
			if(Util.isValueEmpty(alternative_text))
			{
				onmouseover = "onmouseover=\"return overlib('"+alternative_text+"', BORDER, 2);\" onmouseout=\"return nd();\"><img src=\"../images/icons/disabled/"+icon_disabled+"\" border=\"0\">";
				
				this.link_element = this.link_element + "<a "+onmouseover+"</a>&nbsp;\n";
			}
			else
			{
				this.link_element = this.link_element + "<a><img src=\"../images/icons/disabled/"+icon_disabled+"\" border=\"0\"></a>&nbsp;\n";
			}
		}	
	}
	
	/**
	 * Get the cas number and chemical name for the compound.
	 * @param id
	 * @return
	 */
	public boolean getChemicalNameAndCas(String id)
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
	               
	               String sql = "SELECT c.chemical_name, c.cas_number FROM compound c"+
				   				" WHERE c.id = "+id+";";
	                                     
	               ResultSet result = stmt.executeQuery(sql);

	               if(result.next())
	               {
	               	 this.compound_name = result.getString("c.chemical_name");
	                 this.cas_number = result.getString("c.cas_number");
	               }
	            }
	            con.close();
	            return true;
	         }
	   }//end of try
	    
	    catch (SQLException e)
	    {
	     e.printStackTrace();
	     return false;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
		   return false;
	    }    
	  return false;
	  }

	
	/**
	 * Get all the resource information.
	 * @return
	 * 
	 * Not in use - placed under the Display() methodn... dann 01feb2005
	 */
	public ResultSet getResources()
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
	               
	               String sql = "SELECT text_id, use_text, mouse_text, alternative_text, icon, icon_disabled," +
	               		" visibility, internal, position, include_1, include_2, include_3, include_4, id_1, id_2, id_3, id_4," +
	               		" cas_required, url, resource, sticky_text" +
	               		" FROM resources" +
	               		" ORDER BY position;";
	        
	               ResultSet result = stmt.executeQuery(sql);

	               while(result.next())
	               {
//	               	con.close();
//	               	return result;
	               }
	               return result;
	            }
	            con.close();
	            return null;
	         }
	   }//end of try
	    
	    catch (SQLException e)
	    {
	     e.printStackTrace();
	     return null;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
		   return null;
	    }    
	  return null;
	  }
	
	/**
	 * theres is an option to include compound_id, cas_name or chemical name in a string.
	 * use the folowing syntax:
	 * 
	 * !1! = will be replaced with compound id
	 * !2! = will be replaced with compound/chemcal name
	 * !3! = will be replaced with cas number
	 * 
	 * @param url_or_text
	 * @return
	 */
	private String replaceWithValues(String url_or_text)
	{	
		while(url_or_text.indexOf("!1!") != -1)
		{			
			int replacement = url_or_text.indexOf("!1!");
			String temp1 = "";
			
			temp1 = url_or_text.substring(0, replacement);
			temp1 = temp1 + this.compound_name;
			temp1 = temp1 + url_or_text.substring(replacement+3, url_or_text.length());
			url_or_text = temp1;
		}
		
		while(url_or_text.indexOf("!2!") != -1)
		{
			int replacement = url_or_text.indexOf("!2!");
			String temp1 = "";
			
			temp1 = url_or_text.substring(0, replacement);
			temp1 = temp1 + this.compound_id;
			temp1 = temp1 + url_or_text.substring(replacement+3, url_or_text.length());
			url_or_text = temp1;
		}
		
		while(url_or_text.indexOf("!3!") != -1)
		{
			int replacement = url_or_text.indexOf("!3!");
			String temp1 = "";
			
			temp1 = url_or_text.substring(0, replacement);
			temp1 = temp1 + this.cas_number;
			temp1 = temp1 + url_or_text.substring(replacement+3, url_or_text.length());
			url_or_text = temp1;
		}
				
		return url_or_text;
	}
	
	/**
	 * @return Returns the cas_number.
	 */
	public String getCas_number() {
		return cas_number;
	}
	/**
	 * @param cas_number The cas_number to set.
	 */
	public void setCas_number(String cas_number) {
		this.cas_number = cas_number;
	}
	/**
	 * @return Returns the compound_name.
	 */
	public String getCompound_name() {
		return compound_name;
	}
	/**
	 * @param compound_name The compound_name to set.
	 */
	public void setCompound_name(String compound_name) {
		this.compound_name = compound_name;
	}
	/**
	 * @return Returns the form_elements.
	 */
	public String getForm_elements() {
		return form_elements;
	}
	/**
	 * @param form_elements The form_elements to set.
	 */
	public void setForm_elements(String form_elements) {
		this.form_elements = form_elements;
	}
	/**
	 * @return Returns the java_script.
	 */
	public String getJava_script() {
		return java_script;
	}
	/**
	 * @param java_script The java_script to set.
	 */
	public void setJava_script(String java_script) {
		this.java_script = java_script;
	}
	/**
	 * @return Returns the link_element.
	 */
	public String getLink_element() {
		return link_element;
	}
	/**
	 * @param link_element The link_element to set.
	 */
	public void setLink_element(String link_element) {
		this.link_element = link_element;
	}
}
