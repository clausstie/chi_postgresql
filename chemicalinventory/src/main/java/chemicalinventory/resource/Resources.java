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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.FileOperations;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class Resources implements Serializable {
	
	public Resources()
	{
	}
	
	private String name = "";
	private String use_text = "";
	private String mouse_text = "";
	private String alternative_mouse_text = "";
	private String alternative_mouse_text_url = "";
	private String sticky_text = "";
	private String icon = "";
	private String disabled = "";
	private String disabled_url = "";	
	private int position = 0;
	private String select_dest = "";
	private String visibility = "";
	private String resource = "";
	private String chemical_name = "";
	private String compound_id = "";
	private String cas_number = "";
	private String cas_required = "";
	private String url = "";
	private String url_input_1 = "";
	private String url_input_2 = "";
	private String url_input_3 = "";
	private String url_input_4 = "";
	private String id_1 = "";
	private String id_2 = "";
	private String id_3 = "";
	private String id_4 = "";
	private int status = 0;

	
	/**
	 * List all the pages that a user can choose amongst
	 * @return
	 */
	public String listAvailablePages()
	{
		String html_option_list = "";
		
		html_option_list = "<option value=\"X\">[SELECT]</option>" +
				"<option value=\""+Attributes.REGISTER_CONTAINER+"\">Register Container</option>" +
				"<option value=\""+Attributes.MODIFY_COMPOUND+"\">Modify Compound</option>" +
				"<option value=\""+Attributes.COMPOUND_HISTORY+"\">View Compound History</option>" +
				"<option value=\""+Attributes.CREATE_COMPOUND_SAMPLE+"\">Register New Sample For Compound</option>" +
				"<option value=\""+Attributes.COMPOUND_SAMPLE_LIST+"\">Find Samples For Compound</option>";
		
		return html_option_list;
	}
	
	/**
	 * Create a list of available icons
	 * 
	 * syntax:
	 * 
	 * <code><option value="zzz"></code>xxx.gif/png/jpg/jpeg<code></option></code>
	 * @return 
	 * 	return to the jsp page a list of icons from the icon directory
	 */
	public String listIcons()
	{
		String html_option_list = "<option value=\"X\">[SELECT]</option>";
		
		FileOperations imageFiles = new FileOperations();
		Vector imgList = imageFiles.listImages();
		
		for (int i =0; i<imgList.size(); i++)
		{
			html_option_list = html_option_list + "<option value=\""+(String) imgList.get(i)+"\">"+(String) imgList.get(i)+"</option>\n";
		}
				
		return html_option_list;
	}
	
	/**
	 * Create a list of icons returned as a list
	 * @return Vector the list of icons
	 */
	public Vector listIcons_vector()
	{		
		FileOperations imageFiles = new FileOperations();
		Vector imgList = imageFiles.listImages();

		return imgList;
	}

	/**
	 * Create a list of disabled icons...
	 * @return Vector holding the list.
	 */
	public Vector listIcons_vector_disabled()
	{		
		FileOperations imageFiles = new FileOperations();
		Vector imgList = imageFiles.listImages_disabled();

		return imgList;
	}

	
	/**
	 * Create the list of visibity options and return the option list 
	 * for the html page.
	 * @return
	 */
	public String listVisibilityOptions()
	{
		String html_option_list = "<option value=\"adm\">[SELECT]</option>";
		
		html_option_list = html_option_list + "<option value=\"adm\">Administrators Only</option><br/>" +
				"<option value=\"normal\">Visible to All</option>";
		
		return html_option_list;
	}
	
	/**
	 * Create a list of available icons for disabled display
	 * 
	 * syntax:
	 * 
	 * <code><option value="zzz"></code>xxx.gif/png/jpg/jpeg<code></option></code>
	 * @return 
	 * 	return to the jsp page a list of icons from the icon directory For display as disabled.
	 */
	public String listIcons_disabled()
	{
		String html_option_list = "<option value=\"\">[SELECT]</option>";
		
		FileOperations imageFiles = new FileOperations();
		Vector imgList = imageFiles.listImages_disabled();
		
		for (int i =0; i<imgList.size(); i++)
		{
			html_option_list = html_option_list + "<option value=\""+(String) imgList.get(i)+"\">"+(String) imgList.get(i)+"</option>\n";
		}
				
		return html_option_list;
	}
	
	
	/**
	 * Create a list of empty positions for the new resouce to be placed.
	 * @return return the html options list.
	 */
	public String listPositions()
	{
		Vector positions = getPositions();
		String position_list = "<option value=\"0\">[SELECT]</option>";
		
		for (int i = 1; i<=10; i++)
		{
			if(!positions.contains(String.valueOf(i)))
			{
				position_list = position_list + "<option value=\""+i+"\">"+i+"</option>\n";
			}
		}
				
		return position_list;
	}
	
	/**
	 * Create a vector with all the taken positions.
	 * @return
	 */
	public Vector getPositions()
	  {
	   Vector pos = new Vector();
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
	               
	               String sql = "SELECT position FROM resources"+
				   						 " ORDER BY position;";
	               
	               ResultSet result = stmt.executeQuery(sql);
	               
	               while(result.next())
	               {
	                 pos.add(result.getString("position"));
	               }
	            }
	            con.close();
	            return pos;
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
	  return pos;
	  }
	
	/**
	 * Create a list of all current taken names in the resource table.
	 * @return
	 */
	public Vector getResourceNameList()
	  {
	   Vector pos = new Vector();
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
	               
	               String sql = "SELECT text_id FROM resources"+
				   						 " ORDER BY position;";
	               
	               ResultSet result = stmt.executeQuery(sql);
	               
	               while(result.next())
	               {
	                 pos.add(result.getString("text_id"));
	               }
	            }
	            con.close();
	            return pos;
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
	  return pos;
	  }
	
	/**
	 * Create the check boxes for the include of internal elements, compound id, cas etc..
	 * @return the string to put on the jsp page
	 */
	public String internal_include()
	{
		String includes = "Chemical Name&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_NAME+"\"/><br/>"+
						  "Chemical Id&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_ID+"\"/><br/>"+
						  "Cas Number&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_CAS+"\"/><br/>";
		
		return includes;
	}
	
	
	/**
	 * Check if there is room for one more link to 
	 * an internal or external ressource on the page...
	 * 
	 * @return true/false
	 */
	public boolean isRoomForMoreIcons()
	{
		return false;
	}
	
	/**
	 * Create a new internal or external resource.
	 * 
	 * The int status is during the method set to a value indicating the status of the return method:
	 * 
	 * 0: resource created ok
	 * 1: input could not be validated.
	 * 2: The name is not unique.
	 * 3: unexpected error orcurred.
	 * 
	 * @return true = the resource was succesfully created
	 * 	false = the new resource failed to be created.
	 */
	public boolean createResource()
	{
		/*
		 * Make sure that the minimum requirements to the input is ok
		 */
		if(validateInput())
		{
			/*
			 * Create all the elements for this resource,
			 * dependent of the informaiton entered on the page.
			 */
			
				//the name cannot include blanks.. thats the convention..
				this.name = this.name.replaceAll(" ", "_");
			
				//create the on mouse overlib box...
				if(!Util.isValueEmpty(this.mouse_text))
				{
					this.mouse_text = this.mouse_text.trim();
					this.mouse_text = Util.double_q(this.mouse_text);
				}
				
//				create the on mouse overlib box for disabled view...
				if(!Util.isValueEmpty(this.alternative_mouse_text))
				{
					this.alternative_mouse_text = this.alternative_mouse_text.trim();
					this.alternative_mouse_text = Util.double_q(this.alternative_mouse_text);
				}
				
               //Use on mouse over text??
               if(this.mouse_text.length() > 1)
               	this.use_text = "Y";
               else
               	this.use_text = "N";
               
               //sticky text
               if(this.sticky_text.equalsIgnoreCase("on"))
               	this.sticky_text = "Y";
               else
               	this.sticky_text = "N";
					
               //setup include elements
               if(this.select_dest.equalsIgnoreCase("internal"))
               {
               	if(this.chemical_name.equalsIgnoreCase("on"))
               		this.url_input_1 = Attributes.COMPOUND_NAME;
				else
					this.url_input_1 = "";
               	
               	if(this.compound_id.equalsIgnoreCase("on"))
               		this.url_input_2 = Attributes.COMPOUND_ID;
               	else
               		this.url_input_2 = "";

               	if(this.cas_number.equalsIgnoreCase("on"))
               		this.url_input_3 = Attributes.COMPOUND_CAS;
               	else
               		this.url_input_3 = "";
               	
               	//cas internal only for external resource
               	this.cas_required = "";
               	
               	//url only for external
               	this.url = "";
               	
               	//alternative text
               	this.alternative_mouse_text_url = this.alternative_mouse_text;

               	//disabled icon
               	this.disabled_url = this.disabled;
               	
               	id_1 = "";
               	id_2 = "";
               	id_3 = "";
               	id_4 = "";
               }
               
               if(this.select_dest.equalsIgnoreCase("external"))
               {
               	if(this.cas_required.equalsIgnoreCase("on"))
               	{
               		this.cas_required = "Y";
               	}
               	else
               		this.cas_required = "N";
               	
               	//external visibil to all users
               	this.visibility = "normal";
               	
               	//encode single ping in the url and other fields.
               	this.url = Util.double_q(this.url);
               	this.url_input_1 = Util.double_q(this.url_input_1);
               	this.url_input_2 = Util.double_q(this.url_input_2);
               	this.url_input_3 = Util.double_q(this.url_input_3);
               	this.url_input_4 = Util.double_q(this.url_input_4);
               }
               
			
				/*
				 * now register the information in the db.
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
		               
		               //Validate that the name is actually unique
		               String uniq_name = "SELECT resources.text_id FROM resources WHERE resources.text_id = '"+this.name+"';";
		               		               
		               ResultSet uniqSet = stmt.executeQuery(uniq_name);
		               
		               if(uniqSet.next())
		               {
		               	con.close();
		               	this.status = 2;
		               	return false;
		               }
		               		              		               
		               //insert the values in the database
		               String insert = "INSERT INTO resources" +
		               		" (text_id, use_text, mouse_text, icon, icon_disabled, visibility, position, include_1, include_2, include_3, include_4, cas_required, url, resource, sticky_text, alternative_text, internal, id_1, id_2, id_3, id_4)" +
		               		" VALUES('"+this.name.toUpperCase()+"', '"+this.use_text+"', '"+this.mouse_text+"', '"+this.icon+"', '"+this.disabled_url+"', '"+this.visibility+"', "+this.position+", " +
		               		"'"+this.url_input_1+"', '"+this.url_input_2+"', '"+this.url_input_3+"', '"+this.url_input_4+"', '"+this.cas_required+"',  '"+this.url+"', '"+this.resource+"', '"+this.sticky_text+"', '"+this.alternative_mouse_text_url+"', " +
		               		"'"+this.select_dest+"', '"+id_1+"', '"+id_2+"', '"+id_3+"', '"+id_4+"')";
		               
		               stmt.executeUpdate(insert);
		            }
		            con.close();
		            this.status = 0;
		            return true;
		         }
		   }//end of try
		    
		    catch (SQLException e)
		    {
	             e.printStackTrace();
			     status = 3;
			     return false;
		    }
		    catch (Exception e)
		    {
	             e.printStackTrace();
			     status = 3;
			     return false;
		    }
		}
		else//validation of fields failed.
		{
			this.status = 1;
			return false;
		}
		
		return false;
	}
	

	/**
	 * Validate that the minimum requirements to create a new resource has
	 * been entered
	 * @return boolean 
	 */
	public boolean validateInput()
	{	
		if(Util.isValueEmpty(this.name))//a valid name
		{
				if(!this.icon.equals("X") && this.position >= 1) //icon seletcted and position selected
				{
					//FOR INTERNAL INPUT
					if(this.select_dest.equalsIgnoreCase("internal"))
					{
						if(!this.resource.equals("X"))//the resource is seleted ok.
						{
							return true;
						}
					}
					else if(this.select_dest.equalsIgnoreCase("external"))//EXTERNAL INPUT
						{
							if(Util.isValueEmpty(this.url))//an url is entered.
							{
								return true;
							}
						}
				}
		}
		
		return false;
	}
	
	/**
	 * Modify an existing resource
	 * @return
	 */
	public boolean modifyResource()
	{
		return false;
	}
	
	/**
	 * @return Returns the icon.
	 */
	public String getIcon() 
	{
		return icon;
	}
	/**
	 * @param icon The icon to set.
	 */
	public void setIcon(String icon) {
		this.icon = icon;
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
	public void setCas_number(String include_cas) {
		this.cas_number = include_cas;
	}
	/**
	 * @return Returns the compound_id.
	 */
	public String getCompound_id() {
		return compound_id;
	}
	/**
	 * @param compound_id The compound_id to set.
	 */
	public void setCompound_id(String include_compoundid) {
		this.compound_id = include_compoundid;
	}
	/**
	 * @return Returns the chemical_name.
	 */
	public String getChemical_name() {
		return chemical_name;
	}
	/**
	 * @param chemical_name The chemical_name to set.
	 */
	public void setChemical_name(String include_compoundname) {
		this.chemical_name = include_compoundname;
	}
	/**
	 * @return Returns the mouse_text.
	 */
	public String getMouse_text() {
		return mouse_text;
	}
	/**
	 * @param mouse_text The mouse_text to set.
	 */
	public void setMouse_text(String mouse_text) {
		this.mouse_text = mouse_text;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	/**
	 * @return Returns the resource.
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @param resource The resource to set.
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	/**
	 * @return Returns the select_dest.
	 */
	public String getSelect_dest() {
		return select_dest;
	}
	/**
	 * @param select_dest The select_dest to set.
	 */
	public void setSelect_dest(String select_dest) {
		this.select_dest = select_dest;
	}
	/**
	 * @return Returns the sticky_text.
	 */
	public String getSticky_text() {
		return sticky_text;
	}
	/**
	 * @param sticky_text The sticky_text to set.
	 */
	public void setSticky_text(String sticky_text) {
		this.sticky_text = sticky_text;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return Returns the url_input_1.
	 */
	public String getUrl_input_1() {
		return url_input_1;
	}
	/**
	 * @param url_input_1 The url_input_1 to set.
	 */
	public void setUrl_input_1(String url_input_1) {
		this.url_input_1 = url_input_1;
	}
	/**
	 * @return Returns the url_input_2.
	 */
	public String getUrl_input_2() {
		return url_input_2;
	}
	/**
	 * @param url_input_2 The url_input_2 to set.
	 */
	public void setUrl_input_2(String url_input_2) {
		this.url_input_2 = url_input_2;
	}
	/**
	 * @return Returns the url_input_3.
	 */
	public String getUrl_input_3() {
		return url_input_3;
	}
	/**
	 * @param url_input_3 The url_input_3 to set.
	 */
	public void setUrl_input_3(String url_input_3) {
		this.url_input_3 = url_input_3;
	}
	/**
	 * @return Returns the visibility.
	 */
	public String getVisibility() {
		return visibility;
	}
	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	/**
	 * @return Returns the disabled.
	 */
	public String getDisabled() {
		return disabled;
	}
	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(String icon_disabled) {
		this.disabled = icon_disabled;
	}
	/**
	 * @return Returns the alternative_mouse_text.
	 */
	public String getAlternative_mouse_text() {
		return alternative_mouse_text;
	}
	/**
	 * @param alternative_mouse_text The alternative_mouse_text to set.
	 */
	public void setAlternative_mouse_text(String alternative_mouse_text) {
		this.alternative_mouse_text = alternative_mouse_text;
	}
	/**
	 * @return Returns the cas_required.
	 */
	public String getCas_required() {
		return cas_required;
	}
	/**
	 * @param cas_required The cas_required to set.
	 */
	public void setCas_required(String cas_required) {
		this.cas_required = cas_required;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return Returns the alternative_mouse_text_url.
	 */
	public String getAlternative_mouse_text_url() {
		return alternative_mouse_text_url;
	}
	/**
	 * @param alternative_mouse_text_url The alternative_mouse_text_url to set.
	 */
	public void setAlternative_mouse_text_url(String alternative_mouse_text_url) {
		this.alternative_mouse_text_url = alternative_mouse_text_url;
	}
	/**
	 * @return Returns the disabled_url.
	 */
	public String getDisabled_url() {
		return disabled_url;
	}
	/**
	 * @param disabled_url The disabled_url to set.
	 */
	public void setDisabled_url(String icon_disabled_url) {		
		this.disabled_url = icon_disabled_url;
	}
	/**
	 * @return Returns the id_1.
	 */
	public String getId_1() {
		return id_1;
	}
	/**
	 * @param id_1 The id_1 to set.
	 */
	public void setId_1(String id_1) {
		this.id_1 = id_1;
	}
	/**
	 * @return Returns the id_2.
	 */
	public String getId_2() {
		return id_2;
	}
	/**
	 * @param id_2 The id_2 to set.
	 */
	public void setId_2(String id_2) {
		this.id_2 = id_2;
	}
	/**
	 * @return Returns the id_3.
	 */
	public String getId_3() {
		return id_3;
	}
	/**
	 * @param id_3 The id_3 to set.
	 */
	public void setId_3(String id_3) {
		this.id_3 = id_3;
	}

	/**
	 * @return the id_4
	 */
	public String getId_4() {
		return id_4;
	}

	/**
	 * @param id_4 the id_4 to set
	 */
	public void setId_4(String id_4) {
		this.id_4 = id_4;
	}

	/**
	 * @return the url_input_4
	 */
	public String getUrl_input_4() {
		return url_input_4;
	}

	/**
	 * @param url_input_4 the url_input_4 to set
	 */
	public void setUrl_input_4(String url_input_4) {
		this.url_input_4 = url_input_4;
	}
}