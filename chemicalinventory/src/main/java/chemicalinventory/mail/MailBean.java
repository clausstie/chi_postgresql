/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2003-2005.
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

import javax.mail.*;          //JavaMail packages
import javax.mail.internet.*; //JavaMail Internet packages

import chemicalinventory.context.Attributes;

import java.util.*;           //Java Util packages
/**
 *
 * @author  Dann Vestergaard
 */
public class MailBean {
	
	private boolean status;
    
	/**
	 * @return Returns the status.
	 */
	public boolean isStatus() {
		return status;
	}
		
    /** Creates a new instance of MailBean */
    public MailBean() {
    }
       
    /**
     * Send a mail giving the parameters of the receivers and senders email, subject,
     * message body, type, and cc's and bcc's..
     * 
     * @param p_from String - email address of the sender
     * @param p_to String - email address of the receiver
     * @param p_cc String - email address of cc
     * @param p_bcc String - email address of bcc
     * @param p_subject String - subject of this mail
     * @param p_message String - the message body
     * @param c_type int - 1 = html message, 2 = text message
     * 
     * @return String - the status of the operation.  
     */
    public String send(String p_from, String p_to, String p_cc, String p_bcc,
                                    String p_subject, String p_message, int c_type) {
    	    		
    //result of the mail transaction.
    String state = "";
    
    /*** TODO change the server name..***/
    // Name of the Host machine where the SMTP server is running
    String l_host = Attributes.CI_SMTP_SERVER;//insert the site smtp server

    // Gets the System properties
    Properties l_props = System.getProperties();

    // Puts the SMTP server name to properties object
    l_props.put("mail.smtp.host", l_host);
    //l_props.put("mail.smtp.auth", "true");

    //create authentication objeck
 //   Authenticator auth = new SMTPAuthenticator();
    
    // Get the default Session using Properties Object
    Session l_session = Session.getDefaultInstance(l_props, null); // not using auth use this else use:
  //  Session l_session = Session.getDefaultInstance(l_props, auth);

    l_session.setDebug(false); // Enable/disable the debug mode

    try {
      MimeMessage l_msg = new MimeMessage(l_session); // Create a New message

      l_msg.setFrom(new InternetAddress(p_from)); // Set the From address

      // Setting the "To recipients" addresses
      l_msg.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse(p_to, false));

      // Setting the "Cc recipients" addresses
      l_msg.setRecipients(Message.RecipientType.CC,
                                  InternetAddress.parse(p_cc, false));

      // Setting the "BCc recipients" addresses

      l_msg.setRecipients(Message.RecipientType.BCC,
                                  InternetAddress.parse(p_bcc, false));

      l_msg.setSubject(p_subject, "ISO-8859-1"); // Sets the Subject

      // Create and fill the first message part
      MimeBodyPart l_mbp = new MimeBodyPart();

      //Send the message as html message.
      if(c_type == 1)
          l_mbp.setContent(p_message, "text/html");
      
      //send the message as plain text
      if(c_type == 2)
      {
      	  l_mbp.setText(p_message, "ISO-8859-1");
      }
     
      // Create the Multipart and its parts to it
      Multipart l_mp = new MimeMultipart();
      l_mp.addBodyPart(l_mbp);

      // Add the Multipart to the message
      l_msg.setContent(l_mp);

      // Set the Date: header
      l_msg.setSentDate(new Date());

      // Send the message
      Transport.send(l_msg);
      // If here, then message is successfully sent.
      // Display Success message
      state = "<FONT SIZE=4 COLOR=\"blue\"><hr><B>Message successfully sent to "+p_to+"</B><br>";

      //if CCed then, add html for displaying info
      if (!p_cc.equals(""))
      	state = state +"<FONT color=green><B>CCed To </B></FONT>: "+p_cc+"<BR>";
      //if BCCed then, add html for displaying info
      if (!p_bcc.equals(""))
      	state = state +"<FONT color=green><B>BCCed To </B></FONT>: "+p_bcc ;

      state = state+"<BR><HR>";
      status = true;
    } catch (MessagingException mex) { // Trap the MessagingException Error
        // If here, then error in sending Mail. Display Error message.
    	state = state + "<FONT SIZE=4 COLOR=\"red\"> <B>Error : </B><BR><HR> "+
                   "<FONT SIZE=3 COLOR=\"black\">"+mex.toString()+"<BR><HR>";
        status = false;
        return state;
    } catch (Exception e) {

        // If here, then error in sending Mail. Display Error message.
    	state = state + "<FONT SIZE=4 COLOR=\"red\"> <B>Error : </B><BR><HR> "+
                   "<FONT SIZE=3 COLOR=\"black\">"+e.toString()+"<BR><HR>";

        e.printStackTrace();
        status = false;
        return state;
    }//end catch block
    status = true;
    return state;
  } // end of method send   */
    
    /**
     * This method takes a single recepient and sends a message to this person.
     * 
     * @param p_from
     * @param p_to
     * @param p_subject
     * @param p_message
     * @param c_type
     * 
     * @return true/false boolean
     */
   public boolean send_single(String p_from, String p_to, String p_subject, String p_message, int c_type)
   {    
    // Name of the Host machine where the SMTP server is running
    String l_host = Attributes.CI_SMTP_SERVER;//Site smtp server
   
    // Gets the System properties
    Properties l_props = System.getProperties();

//  Puts the SMTP server name to properties object
    l_props.put("mail.smtp.host", l_host);
   // l_props.put("mail.smtp.auth", "true");

    //create authentication objeck
  //  Authenticator auth = new SMTPAuthenticator();
    
    // Get the default Session using Properties Object
    Session l_session = Session.getDefaultInstance(l_props, null); // not using auth use this else use:
  //  Session l_session = Session.getDefaultInstance(l_props, auth);
    
    // Puts the SMTP server name to properties object
    l_session.setDebug(false); // Enable the debug mode
   
    try {
        MimeMessage l_msg = new MimeMessage(l_session); // Create a New message

        l_msg.setFrom(new InternetAddress(p_from)); // Set the From address

        // Setting the "To recipients" addresses
        l_msg.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(p_to, false));

        l_msg.setSubject(p_subject, "ISO-8859-1"); // Sets the Subject

        // Create and fill the first message part
        MimeBodyPart l_mbp = new MimeBodyPart();

        //Send the message as html message.
        if(c_type == 1)
            l_mbp.setContent(p_message, "text/html");
        
        //send the message as plain text
        if(c_type == 2)
        {
        	  l_mbp.setText(p_message, "ISO-8859-1");
        }
       
        // Create the Multipart and its parts to it
        Multipart l_mp = new MimeMultipart();
        l_mp.addBodyPart(l_mbp);

        // Add the Multipart to the message
        l_msg.setContent(l_mp);

        // Set the Date: header
        l_msg.setSentDate(new Date());

        // Send the message
        Transport.send(l_msg);
        // If here, then message is successfully sent.
        return true;
        
      } catch (MessagingException mex) { // Trap the MessagingException Error
          // If here, then error in sending Mail. Display Error message.
      	  mex.printStackTrace();
          return false;
          
      } catch (Exception e) {
          // If here, then error in sending Mail. Display Error message.
          e.printStackTrace();
          return false;
      }//end catch block
   }//end of send single message.
   
   /**
   * SimpleAuthenticator is used to do simple authentication
   * when the SMTP server requires it.
   */
   private class SMTPAuthenticator extends javax.mail.Authenticator
   {

       public PasswordAuthentication getPasswordAuthentication()
       {
           String username = "";
           String password = "";
           return new PasswordAuthentication(username, password);
       }
   }
}