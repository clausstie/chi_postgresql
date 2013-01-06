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
package chemicalinventory.resource;

import java.io.Serializable;
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
public class Resources_modify implements Serializable {
	
	public Resources_modify()
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
	
	private String chemical_name = "";
	private String compound_id = "";
	private String cas_number = "";
	
	private String resource_display = "";
	private String visibility_display = "";
	
	
	//the original values returned from the client.
	private String mouse_text_original = "";
	private String sticky_text_original = "";
	private String icon_original = "";
	private int position_original = 0;
	private String destination = ""; //returner for the original value if is an internal or ext. resourse.
	private String visibility_original = "";
	private String disabled_icon_original = "";
	private String alternative_mouse_text_o = "";
	private String resource_original = "";
	private String chemical_name_original = "";
	private String compound_id_original = "";
	private String cas_number_original = "";
	
	private String url_original = "";
	private String url_input_1_original = "";
	private String url_input_2_original = "";
	private String url_input_3_original = "";
	private String url_input_4_original = "";
	private String id_1_original = "";
	private String id_2_original = "";
	private String id_3_original = "";
	private String id_4_original = "";
	private String cas_required_original = "";
	private String icon_disabled_url_original = "";
	private String alternative_text_url_original = "";
	
	private int status = 0;
	
	
	/**
	 * Here the entered value is converted to a user friendly display value.
	 * @param value
	 * @return
	 */
	private String setDisplayResource(String value)
	{
		if(value.equals(Attributes.REGISTER_CONTAINER))
			value = "Register Container";
		
		if(value.equals(Attributes.MODIFY_COMPOUND))
			value = "Modify Compound";
		
		if(value.equals(Attributes.COMPOUND_HISTORY))
			value = "View Compound History";
		
		if(value.equals(Attributes.CREATE_COMPOUND_SAMPLE))
			value = "Register New Sample For Compound";

		if(value.equals(Attributes.COMPOUND_SAMPLE_LIST))
			value = "Find Samples For Compound";

		return value;
	}

	/**
	 * The value is formatted to a more userfriendly display value.
	 * @param value
	 * @return
	 */
	private String setDisplayVisibility(String value)
	{
		if(value.equalsIgnoreCase("adm"))
			value = "Administrators Only";
		
		if(value.equals("normal"))
			value = "Visible To All";

		return value;
	}
	
	public String internal_include_modify(String c_name, String c_id, String c_cas)
	{
		String name_check = "";
		String cas_check = "";
		String id_check = "";
		
		if(c_name.equals(Attributes.COMPOUND_NAME))
			name_check = "checked=\"checked\"";

		if(c_id.equals(Attributes.COMPOUND_ID))
			id_check = "checked=\"checked\"";

		if(c_cas.equals(Attributes.COMPOUND_CAS))
			cas_check = "checked=\"checked\"";		
		
		String includes = "Chemical Name&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_NAME+"\" "+name_check+" /><br/>"+
						  "Chemical Id&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_ID+"\" "+id_check+"/><br/>"+
						  "Cas Number&nbsp;<input type=\"checkbox\" name=\""+Attributes.COMPOUND_CAS+"\" "+cas_check+"/><br/>";
		
		return includes;
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
	public boolean getResourceInfo(String text_id)
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
		               
		               //Validate that the name is actually unique
		               String sql = "SELECT text_id, use_text, mouse_text, alternative_text, icon, icon_disabled, visibility," +
		               		" internal, position, include_1, include_2, include_3, include_4, id_1, id_2, id_3, id_4, cas_required," +
		               		" url, resource, sticky_text" +
		               		" FROM resources" +
		               		" WHERE text_id = '"+text_id+"';";
		               
		               ResultSet set = stmt.executeQuery(sql);
		               
		               if(set.next())
		               {
		               		//set the values that is common for both internal and external
		               		if(!set.getString("use_text").equals(""))
		               		{
		               			this.mouse_text = set.getString("mouse_text").trim();
		               		}
		               		
		               		this.name = set.getString("text_id");
		               		this.icon = set.getString("icon");
		               		this.position = set.getInt("position");
		               				               	
		               		//decide if it is an external or external resource
		               		if(set.getString("internal").equalsIgnoreCase("internal"))
		               		{
		               			this.select_dest = "internal";
		               			this.visibility = set.getString("visibility");
		               			this.visibility_display = setDisplayVisibility(set.getString("visibility"));
		               			this.disabled = set.getString("icon_disabled");
		               			this.alternative_mouse_text = set.getString("alternative_text");
		               			this.resource = set.getString("resource");
		               			this.resource_display = setDisplayResource(set.getString("resource"));
		               			this.chemical_name = set.getString("include_1");
								this.compound_id = set.getString("include_2");						
		               			this.cas_number = set.getString("include_3");								
		               			
		               			//set all the external resources to blank
								this.cas_required = "";
								this.url = "";
								this.url_input_1 = "";
								this.url_input_2 = "";
								this.url_input_3 = "";
								this.url_input_4 = "";
								this.id_1 = "";
								this.id_2 = "";
								this.id_3 = "";
								this.id_4 = "";
							}
		               		else
		               		{
		               			this.select_dest = "external";
		               			
		               			this.url = set.getString("url");
								this.url_input_1 = set.getString("include_1");
								this.url_input_2 = set.getString("include_2");
								this.url_input_3 = set.getString("include_3");
								this.url_input_4 = set.getString("include_4");
								this.id_1 = set.getString("id_1");
								this.id_2 = set.getString("id_2");
								this.id_3 = set.getString("id_3");
								this.id_4 = set.getString("id_4");
								this.cas_required = set.getString("cas_required");
								this.disabled_url = set.getString("icon_disabled");
								this.alternative_mouse_text_url = set.getString("alternative_text");

		               			//set all the internal resource fields to blank
		               			this.visibility = "";
		               			this.disabled = "";
		               			this.alternative_mouse_text = "";
		               			this.resource = "";
		               			this.chemical_name = "";
								this.compound_id = "";						
		               			this.cas_number = "";								
		               		}
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
	 * Perform an update in the database, the sql entered must be an sql update statement
	 * update, delete, insert
	 * @param sql must be a complete sql update statment ready to be executed,
	 * @return true if and only if the update was performed ok 
	 */
	public boolean performUpdate(String sql)
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
		               
		               stmt.executeUpdate(sql);
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
	 * Perform the modification of an existing resource.
	 * @param text_id
	 * @return
	 */
	public boolean modifyResource(String text_id)
	{
		String sql = "UPDATE resources SET text_id = '"+text_id+"'";
		
		/*
		 * Make sure that the minimum requirements to the input is ok
		 */
		if(validateInput())
		{
			//has the on mouse over text been altered??
			if(!this.mouse_text.equals(this.mouse_text_original))
			{
//				create the on mouse text
				if(!Util.isValueEmpty(this.mouse_text))
				{
					this.mouse_text = this.mouse_text.trim();
					this.mouse_text = Util.double_q(this.mouse_text);
				}
				
				sql = sql + ", mouse_text = '"+this.mouse_text+"'";
				
				if(this.mouse_text.length() > 1)
				{
					sql = sql + ", use_text = 'Y'";
				}
				else
				{
					sql = sql + ", use_text = 'N'";
				}
			}
			
			//register the sticky text value:
            if(this.sticky_text.equalsIgnoreCase("on"))
            	sql = sql + ", sticky_text = 'Y'";
            else
            	sql = sql + ", sticky_text = 'Y'";
            
            //set the icon for normal display of this resource.
            if(!this.icon.equals(this.icon_original) && !this.icon.equalsIgnoreCase("X"))
            {
            	sql = sql + ", icon = '"+this.icon+"'";
            }
            
            //set the position for the resource
            if(this.position != this.position_original && this.position != 0)
			{
            	sql = sql + ", position = "+this.position;
			}
            
            //setup internal elements
            if(this.select_dest.equalsIgnoreCase("internal"))
            {
            	//set the resource to be internal
            	sql = sql +", internal = 'internal'";
            	            	
            	//set the disabled icon
            	if(!this.disabled.equals(this.disabled_icon_original) && !this.disabled.equalsIgnoreCase("X"))
            	{
            		sql = sql + ", icon_disabled = '"+this.disabled+"'";
            	}
            	
            	//set the alternative text
            	if(!this.alternative_mouse_text.equals(this.alternative_mouse_text_o))
            	{
            		sql = sql + ", alternative_text = '"+this.alternative_mouse_text+"'";
            	}
            	
            	//define the internal resource.
            	if(!this.resource.equals(this.resource_original) && !this.resource.equalsIgnoreCase("X"))
            	{
            		sql = sql + ", resource = '"+this.resource+"'";
            	}
            	
            	if(this.chemical_name.equalsIgnoreCase("on"))
                	sql = sql + ", include_1 = '"+Attributes.COMPOUND_NAME+"'";
				else
					sql = sql + ", include_1 = ''";
            	
            	if(this.compound_id.equalsIgnoreCase("on"))
            		sql = sql + ", include_2 = '"+Attributes.COMPOUND_ID+"'";
            	else
            		sql = sql + ", include_2 = ''";

            	if(this.cas_number.equalsIgnoreCase("on"))
            		sql = sql + ", include_3 = '"+Attributes.COMPOUND_CAS+"'";
            	else
            		sql = sql + ", include_3 = ''";   
            	
            	/* *********** Reset the external attributes... **************** */
            	
            	//cas internal only for external resource
            	sql = sql + ", cas_required = 'N', url = '', id_1 = '', id_2 = '', id_3 = '', id_4 = ''";
            }
            else //setup the external attributes.
            {
//            	set the resource to be external
            	sql = sql +", internal = 'external'";
            	
            	//set the url
            	if(!this.url.equals(this.url_original))
            	{
            		sql = sql +", url = '"+Util.double_q(this.url)+"'";
            	}
            	
            	//set the include_1 attributes
            	if(!this.url_input_1.equals(this.url_input_1_original))
            	{
            		sql = sql +", include_1 = '"+Util.double_q(this.url_input_1)+"'";
            	}
            	else if(!Util.isValueEmpty(this.url_input_1))
            	{
            		sql = sql +", include_1 = ''";
            	}
            	
            	if(!this.id_1.equals(this.id_1_original))
            	{
            		sql = sql +", id_1 = '"+Util.double_q(this.id_1)+"'";
            	}
            	            	
//            	set the include_2 attributes
            	if(!this.url_input_2.equals(this.url_input_2_original))
            	{
            		sql = sql +", include_2 = '"+Util.double_q(this.url_input_2)+"'";
            	}
            	else if(!Util.isValueEmpty(this.url_input_2))
            	{
            		sql = sql +", include_2 = ''";
            	}

            	
            	if(!this.id_2.equals(this.id_2_original))
            	{
            		sql = sql +", id_2 = '"+Util.double_q(this.id_2)+"'";
            	}
            	
//            	set the include_3 attributes
            	
            	if(!this.url_input_3.equals(this.url_input_3_original))
            	{
            		sql = sql +", include_3 = '"+Util.double_q(this.url_input_3)+"'";
            	}	
            	else if(!Util.isValueEmpty(this.url_input_3))
            	{
            		sql = sql +", include_3 = ''";
            	}

            	
            	if(!this.id_3.equals(this.id_3_original))
            	{
            		sql = sql +", id_3 = '"+Util.double_q(this.id_3)+"'";
            	}

//            	set the include_4 attributes
            	
            	if(!this.url_input_4.equals(this.url_input_4_original))
            	{
            		sql = sql +", include_4 = '"+Util.double_q(this.url_input_4)+"'";
            	}	
            	else if(!Util.isValueEmpty(this.url_input_4))
            	{
            		sql = sql +", include_4 = ''";
            	}
            	
            	if(!this.id_4.equals(this.id_4_original))
            	{
            		sql = sql +", id_4 = '"+Util.double_q(this.id_4)+"'";
            	}

            	
            	//set the required cas attribute
            	if(this.cas_required.equalsIgnoreCase("on"))
               	{
            		sql = sql +", cas_required = 'Y'";
               	}
            	else
            	{
            		sql = sql +", cas_required = 'N'";
            	}
            	
            	//set the disabled icon
            	if(!this.disabled_url.equals(this.icon_disabled_url_original) && !this.disabled_url.equalsIgnoreCase("X"))
            	{
            		sql = sql + ", icon_disabled = '"+this.disabled_url+"'";
            	}
            	
            	//set the alternative text
            	if(!this.alternative_mouse_text_url.equals(this.alternative_text_url_original))
            	{
            		sql = sql + ", alternative_text = '"+this.alternative_mouse_text_url+"'";
            	}
            	
            	//set the internal attributes
            	sql = sql + ", resource = '', visibility = 'normal'";
               }
            
            //Perform the update on the database
            sql = sql + "WHERE text_id = '"+text_id+"';";
            
            if(performUpdate(sql))
            {
            	this.status = 0;
            	return true;
            }
            else
            {
            	this.status = 2;
            	return false;
            }
		}
		else
		{
			this.status = 1;
			return false;
		}
	}
	
	/**
	 * Delete a resource from the database.
	 * @param text_id
	 * @return true only if the action was a success.
	 */
	public boolean deleteResource(String text_id)
	{
		String sql = "DELETE FROM resources WHERE text_id = '"+text_id+"';";
		
	    if(performUpdate(sql))
        {
        	return true;
        }
        else
        {
        	return false;
        }
	}
	
	
	/**
	 * Validate that the minimum requirements to create a new resource has
	 * been entered
	 * @return boolean 
	 */
	public boolean validateInput()
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
	 * @param alternative_mouse_text_o The alternative_mouse_text_o to set.
	 */
	public void setAlternative_mouse_text_o(String alternative_mouse_text_o) {
		this.alternative_mouse_text_o = alternative_mouse_text_o;
	}

	/**
	 * @param alternative_text_url_original The alternative_text_url_original to set.
	 */
	public void setAlternative_text_url_original(
			String alternative_text_url_original) {
		this.alternative_text_url_original = alternative_text_url_original;
	}

	/**
	 * @param cas_number_original The cas_number_original to set.
	 */
	public void setCas_number_original(String cas_number_original) {
		this.cas_number_original = cas_number_original;
	}

	/**
	 * @param cas_required_original The cas_required_original to set.
	 */
	public void setCas_required_original(String cas_required_original) {
		this.cas_required_original = cas_required_original;
	}

	/**
	 * @param chemical_name_original The chemical_name_original to set.
	 */
	public void setChemical_name_original(String chemical_name_original) {
		this.chemical_name_original = chemical_name_original;
	}

	/**
	 * @param compound_id_original The compound_id_original to set.
	 */
	public void setCompound_id_original(String compound_id_original) {
		this.compound_id_original = compound_id_original;
	}
	/**
	 * @return Returns the destination.
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @param destination The destination to set.
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @param disabled_icon_original The disabled_icon_original to set.
	 */
	public void setDisabled_icon_original(String disabled_icon_original) {
		this.disabled_icon_original = disabled_icon_original;
	}

	/**
	 * @param icon_disabled_url_original The icon_disabled_url_original to set.
	 */
	public void setIcon_disabled_url_original(String icon_disabled_url_original) {
		this.icon_disabled_url_original = icon_disabled_url_original;
	}

	/**
	 * @param icon_original The icon_original to set.
	 */
	public void setIcon_original(String icon_original) {
		this.icon_original = icon_original;
	}

	/**
	 * @param id_1_original The id_1_original to set.
	 */
	public void setId_1_original(String id_1_original) {
		this.id_1_original = id_1_original;
	}

	/**
	 * @param id_2_original The id_2_original to set.
	 */
	public void setId_2_original(String id_2_original) {
		this.id_2_original = id_2_original;
	}

	/**
	 * @param id_3_original The id_3_original to set.
	 */
	public void setId_3_original(String id_3_original) {
		this.id_3_original = id_3_original;
	}

	/**
	 * @param mouse_text_original The mouse_text_original to set.
	 */
	public void setMouse_text_original(String mouse_text_original) {
		this.mouse_text_original = mouse_text_original;
	}

	/**
	 * @param position_original The position_original to set.
	 */
	public void setPosition_original(int position_original) {
		this.position_original = position_original;
	}

	/**
	 * @param resource_original The resource_original to set.
	 */
	public void setResource_original(String resource_original) {
		this.resource_original = resource_original;
	}

	/**
	 * @param sticky_text_original The sticky_text_original to set.
	 */
	public void setSticky_text_original(String sticky_text_original) {
		this.sticky_text_original = sticky_text_original;
	}
	/**
	 * @param url_input_1_original The url_input_1_original to set.
	 */
	public void setUrl_input_1_original(String url_input_1_original) {
		this.url_input_1_original = url_input_1_original;
	}

	/**
	 * @param url_input_2_original The url_input_2_original to set.
	 */
	public void setUrl_input_2_original(String url_input_2_original) {
		this.url_input_2_original = url_input_2_original;
	}
	
	/**
	 * @param url_input_3_original The url_input_3_original to set.
	 */
	public void setUrl_input_3_original(String url_input_3_original) {
		this.url_input_3_original = url_input_3_original;
	}


	/**
	 * @param url_original The url_original to set.
	 */
	public void setUrl_original(String url_original) {
		this.url_original = url_original;
	}
	/**
	 * @return Returns the use_text.
	 */
	public String getUse_text() {
		return use_text;
	}
	/**
	 * @param use_text The use_text to set.
	 */
	public void setUse_text(String use_text) {
		this.use_text = use_text;
	}
	/**
	 * @param visibility_original The visibility_original to set.
	 */
	public void setVisibility_original(String visibility_original) {
		this.visibility_original = visibility_original;
	}
	/**
	 * @return Returns the resource_display.
	 */
	public String getResource_display() {
		return resource_display;
	}
	/**
	 * @return Returns the visibility_display.
	 */
	public String getVisibility_display() {
		return visibility_display;
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
	 * @return the id_4_original
	 */
	public String getId_4_original() {
		return id_4_original;
	}

	/**
	 * @param id_4_original the id_4_original to set
	 */
	public void setId_4_original(String id_4_original) {
		this.id_4_original = id_4_original;
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

	/**
	 * @return the url_input_4_original
	 */
	public String getUrl_input_4_original() {
		return url_input_4_original;
	}

	/**
	 * @param url_input_4_original the url_input_4_original to set
	 */
	public void setUrl_input_4_original(String url_input_4_original) {
		this.url_input_4_original = url_input_4_original;
	}
}