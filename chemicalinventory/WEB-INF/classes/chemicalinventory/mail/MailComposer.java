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
package chemicalinventory.mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

import chemicalinventory.beans.UserRegBean;
import chemicalinventory.beans.userInfoBean;
import chemicalinventory.context.Attributes;

/**
 * @author Dann Vestergaard
 */
public class MailComposer {
	
	private String to = "";
	private String from = "";
	private String cc = "";
	private String bcc = "";
	private String subject = "";
	private String message = "";
	private String username = "";
	private String from_fullname = "";
	private String to_fullname = "";
	private String filepath = "";
	private boolean status;
	private String succes_send = "";
	private	String error_send = "";
	
	userInfoBean userinfobean = new userInfoBean();
	MailBean mailworker = new MailBean();
	
	public MailComposer()
	{
	}
	
	/**
	 * @return Returns the status.
	 */
	public boolean isStatus() {
		return status;
	}
	
	/**
	 * @return Returns the filepath.
	 */
	public String getFilepath() {
		return filepath;
	}
	/**
	 * @param filepath The filepath to set.
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * @return Returns the fullname.
	 */
	public String getTo_fullname() {
		return to_fullname;
	}
	/**
	 * @param fullname The fullname to set.
	 */
	public void setTo_fullname(String fullname) {
		this.to_fullname = fullname;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return Returns the from_fullname.
	 */
	public String getFrom_fullname() {
		return from_fullname;
	}
	/**
	 * @param from_fullname The from_fullname to set.
	 */
	public void setFrom_fullname(String from_fullname) {
		this.from_fullname = from_fullname;
	}
	/**
	 * @return Returns the bcc.
	 */
	public String getBcc() {
		return bcc;
	}
	/**
	 * @param bcc The bcc to set.
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return Returns the cc.
	 */
	public String getCc() {
		return cc;
	}
	/**
	 * @param cc The cc to set.
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	/**
	 * @return Returns the error_send.
	 */
	public String getError_send() {
		return error_send;
	}
	/**
	 * @param error_send The error_send to set.
	 */
	public void setError_send(String error_send) {
		this.error_send = error_send;
	}
	/**
	 * @return Returns the succes_send.
	 */
	public String getSucces_send() {
		return succes_send;
	}
	/**
	 * @param succes_send The succes_send to set.
	 */
	public void setSucces_send(String succes_send) {
		this.succes_send = succes_send;
	}

	/**
	 * Get the end of the html document
	 * @return String representation of the html start of doc.
	 */
	public String startHTML()
	{
		return "<html>" +
		"<head>" +
		getCSS() +
		"</head>" +
		"<body>"+
		"<center>";				
	}
	
	/**
	 * Start the html document
	 * @return String representation of the html end of doc.
	 */
	public String endHTML()
	{
		return "</center>"+
		"</body>" +
		"</html>";
	}
	
	/**
	 * Get the css file.
	 * @return the style tag with the complete contents of the css file.
	 */
	public String getCSS()
	{
		try {
			BufferedReader in = new BufferedReader(new FileReader(filepath));
			String str;
			String css = "";
			while ((str = in.readLine()) != null) {
				css = css + str;
			}
			in.close();
			css = "<style type=\"text/css\">"+ css + " </style>";
			return css;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error getting css file.");
		}
		return null;
	}
	
	/**
	 * Send the mail using different parameters
	 * @param type: 	'info_mail' = mail about containers checked out by a specific user.
	 * 				'new_user'  = mail to the new user just added to the system.
	 * @return
	 */
	public String sendMail(String type)
	{
		String the_complete_message = "";//the message body
		int mail_type = 1;//send eiher html or text message
		String ret = ""; //result to return....
		
		if(type.equals("info_mail"))
		{
			the_complete_message = compose_infomail(username);
			subject = "Containers checked out in chemicalInventory";
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("new_user"))
		{
			the_complete_message = message;
			subject = "New user registration in chemicalInventory";
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("update_user"))
		{
			the_complete_message = message;
			subject = "Update of user profile in chemicalInventory";
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("password"))
		{
			the_complete_message = message;
			subject = "chemicalInventory - new password value";
			mail_type = 2;
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("webmaster"))
		{
			the_complete_message = composeMailToWebmaster();
			mail_type = 2;
			to = Attributes.CI_MAIL_WEBMASTER;
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("single_user"))
		{
			the_complete_message = composeMailToSUser();
			mail_type = 2;
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("news"))
		{
			the_complete_message = composeNewsMail();
			mail_type = 2;
			this.from = Attributes.CI_MAIL_BOX;
		}
		else if (type.equals("support"))
		{
			the_complete_message = composeSupportMail();
			mail_type = 2;
			to = Attributes.CI_MAIL_SUPPORT;
		}
		
		/*
		 * using this to send to a number of different users.
		 * Here we send the message to each user and then validate the
		 * status of this action.
		 */
		if (type.equals("news"))
		{
			StringTokenizer tokens = new StringTokenizer(to, ",");
			
			while(tokens.hasMoreTokens())
			{
				String single_rec = tokens.nextToken().trim();
				
				//send the message to one user at a time
				if(mailworker.send_single(from, single_rec, subject, the_complete_message,mail_type))//send succesfully
				{					
					succes_send = succes_send + single_rec+"<br>";
				}
				else //error in sending to this user
				{
					error_send = error_send + single_rec+"<br>";
				}
			}
		}
		else
		{
			//then send the mail using the mailbean
			System.out.println("the complete message: \n" +the_complete_message);
			
			ret = mailworker.send(from, to, cc, bcc, subject, the_complete_message, mail_type);
			
			//set the status boolean to be used in pages where status message is not used directly
			status = mailworker.isStatus();
		}
		
		return ret;
	}
	
	/**
	 * URL decode the string.
	 * @param data
	 * @return return the url decoded string.
	 */
	public String urlDecode(String data)
	{
		try {
			data = URLDecoder.decode(data, "UTF-8");
			return data;
			
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}
	
	/**
	 * Compose the new user mail and return the content.
	 * 
	 * @param regBean
	 * @param password
	 * @return The mail text
	 */
	public String composeNewUserMail(UserRegBean regBean, String password)
	{
//		header part of the html mail
		String body_part_header = "";
		body_part_header = startHTML();
		
		String data = "" +
		  "<table class=\"box\" cellpadding=\"1\" cellspacing=\"2\" border=\"0\" width=\"450\">"+
		  	"<TR><TH colspan=\"2\" class=\"blue\">User Creation Status</TH></TR>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">First Name</th>"+
		        "<td>"+regBean.getFirstName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">Last Name</th>"+
		        "<td>"+regBean.getLastName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		         "<th align=\"left\" class=\"standard\">Room Number</th>"+
		         "<td>"+regBean.getRoom_number()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">Telephone Number</th>"+
		        "<td>"+regBean.getTelephone()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">E-mail</th>"+
		        "<td>"+regBean.getEmail()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User Name</th>"+
		        "<td>"+regBean.getUserName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">Password</th>"+
	   	        "<td>"+password+"</td>"+
   	        "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User Type</th>"+
	   	        "<td>"+regBean.getUserType()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User groups</th>"+
		        "<td>";
		
		        String groups[] = regBean.getGroups();
		        if(groups != null) {
	        		for (int i = 0; i < groups.length; i++) {
	        			String output = (String) groups[i];
	        			data = data + output;        									
					}
		        }	        
		        
		        data = data + "</td>"+
		    "</tr>"+
	    "</table>";
				
		String data_message = "<table>"+
		"<tr>"+
		"<td align=left><h4>Your user profile has been added to the chemicalInventory"+
		" system. below you see the details registered:</h4></td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+ data +"<br></td>"+
		"</tr>"+
		"<tr>"+
		"<td align=left><hr><p>Password: "+password+"</p>"+
		"<p>Remember that all passwords are strictly personal an confidential, and this"+
		" password should be changed, the first time you logon."+
		" Changing the password can be done by clicking \"CHANGE PASSWORD\"" +
		" In the top of the application.</p>"+
		"<hr></td>"+
		"</tr>"+
		"</table>";
		
//		end part of the mail
		String end_part = endHTML();
		
		//complete html part of the mail:
		String mail = body_part_header + data_message + end_part;
		
		return mail; 	
	}
	
	/**
	 * Compose new Update user mail, and return the content.
	 * 
	 * @param regBean
	 * @return the message body.
	 */
	public String composeUpdateUserMail(UserRegBean regBean)
	{
//		header part of the html mail
		String body_part_header = "";
		body_part_header = startHTML();
		
		/*
		 * Find the information on the user
		 */
		
		String data = "" +
		  "<table class=\"box\" cellpadding=\"1\" cellspacing=\"2\" border=\"0\" width=\"450\">"+
		  	"<TR><TH colspan=\"2\" class=\"blue\">Modified User Data</TH></TR>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">First Name</th>"+
		        "<td>"+regBean.getFirstName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">Last Name</th>"+
		        "<td>"+regBean.getLastName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		         "<th align=\"left\" class=\"standard\">Room Number</th>"+
		         "<td>"+regBean.getRoom_number()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">Telephone Number</th>"+
		        "<td>"+regBean.getTelephone()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">E-mail</th>"+
		        "<td>"+regBean.getEmail()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User Name</th>"+
		        "<td>"+regBean.getUserName()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User Type</th>"+
	   	        "<td>"+regBean.getUserType()+"</td>"+
		    "</tr>"+
		    "<tr>"+
		        "<th align=\"left\" class=\"standard\">User groups</th>"+
		        "<td>";
	
	        String groups[] = regBean.getGroups();
	        if(groups != null) {
        		for (int i = 0; i < groups.length; i++) {
        			String output = (String) groups[i];
        			data = data + output;        									
				}
	        }	        
	        
	        data = data + "</td>"+
	    "</tr>"+
    "</table>";
				
		String data_message = "<table>"+
		"<tr>"+
		"<td align=left><h4>Your user profile has been updated in the chemicalInventory"+
		" system. below you see the details registered:</h4></td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+ data +"<br></td>"+
		"</tr>"+
		"<tr>"+
		"<td align=left><hr><p>Password: Has not been changed!</p>"+									
		"<hr></td>"+
		"</tr>"+
		"</table>";
		
//		end part of the mail
		String end_part = endHTML();
		
		//complete html part of the mail:
		String mail = body_part_header + data_message + end_part;
		
		return mail; 	
	}
	
	/**
	 * Create the mail content when updating a users password.
	 * 
	 * @param password
	 * @return
	 */
	public String composeNewPasswordMail(String password)
	{
		String mail = to_fullname+", your password to the chemicalInventory system has been reset.\n\n"+
		"New password value: "+password +"\n\n"+
		"Remember that all passwords are strictly personal an confidential, and this"+
		" password should be changed, the first time you logon."+
		" Changing the password can be done by clicking \"CHANGE PASSWORD\"+" +
		" In the top of the application.";	 	
		return mail;
	}
	
	/**
	 * Compose mail to the webmaster.
	 * 
	 * @return
	 */
	public String composeMailToWebmaster()
	{
		String mail = from_fullname+" - "+from+", has sent you the following message in the chemicalInventory system:\n\n"+message;
		System.out.println("mail: "+mail);
		return mail;
	}
	
	/**
	 * Compose mail to singele user.
	 * 
	 * @return
	 */
	public String composeMailToSUser()
	{
		String mail = from_fullname+" - "+from+", has sent you the following message in the chemicalInventory system:\n\n"+message;
		
		return mail;
	}
	
	/**
	 * Compose news mail.
	 * 
	 * 
	 * @return
	 */
	public String composeNewsMail()
	{
		String mail = from_fullname+" - "+from+", has sent you the following message in the chemicalInventory system:\n\n"+message;
		
		return mail;
	}
	
	/**
	 * Compose support mail
	 * 
	 * @return
	 */
	public String composeSupportMail()
	{
		String mail = from_fullname+" - "+from+", has sent you the following message in the chemicalInventory system:\n\n"+message;
		
		return mail;
	}
	
	/**
	 * Compose mail with informaion to the user about containers checked out.
	 * @param username
	 * @return
	 */
	public String compose_infomail(String username)
	{
		//header part of the html mail
		String body_part_header = "";
		body_part_header = startHTML();
		
		//body part of the html header...
		String body_part = userinfobean.mailInfo(username);
		
		String data = "<table>"+
		"<tr>"+
		"<td align=left><h4> "+to_fullname+", below you see a list of containers checked out"+
		" by you, in the chemicalInventory system.</h4></td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+ body_part +"</td>"+
		"</tr>"+
		"<tr>"+
		"<td align=left><br><h5> "+ from_fullname+"- "+from+" added the following message to you: </h5>"+
		"<hr><p> "+ this.message +"</p><hr></td>"+
		"</tr>"+
		"</table>";
		
		//end part of the mail
		String end_part = endHTML();
		
		//complete html part of the mail:
		String mail = body_part_header + data + end_part;
		
		return mail;
	}
}