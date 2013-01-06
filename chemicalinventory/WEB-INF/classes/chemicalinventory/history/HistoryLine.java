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
package chemicalinventory.history;


import java.io.Serializable;
import java.util.StringTokenizer;

import chemicalinventory.context.Attributes;
import chemicalinventory.utility.Util;

/**
 * @author Dann Vestergaard
 */
public class HistoryLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779194877351202781L;
	private String id = null;
	private String table = null;
	private String table_id = null;
	private String text_id = null;
	private String change_details = null;
	private String text = null;
	private String changed_by = null;
	private String timestamp = null;
	private String unit = null;
	private String old_value = null;
	private String new_value = null;
	private String structure = null;

	private String html_line = "";

	/**
	 * 
	 * @param id
	 * @param table
	 * @param table_id
	 * @param text_id
	 * @param change_details
	 * @param text
	 * @param changed_by
	 * @param timestamp
	 * @param unit
	 * @param old_value
	 * @param new_value
	 */
	public HistoryLine(String id, String table, String table_id, String text_id, String change_details,
			String text, String changed_by, String timestamp, String unit, String old_value, String new_value, String structure)
	{
		this.id = id;
		this.table = table;
		this.table_id = table_id;
		this.text_id = text_id;
		this.change_details = change_details;
		this.text = text;
		this.changed_by = changed_by;
		this.timestamp = timestamp;
		this.unit = unit;
		this.old_value = old_value;
		this.new_value = new_value;

		//Handle search history.
		if(this.table.equals(History.SEARCH_COMPOUND))
		{
			html_line = "	<td style=\"vertical-align: top\">" +
			"		" +Util.encodeTagAndNull(this.table)+//Table
			"	</td>"+
			"	<td style=\"vertical-align: top\">" +
			"		" +Util.encodeTagAndNull(this.text_id)+//Text id = Search performed, quick search or repeated.
			"	</td>"+
			"	<td style=\"vertical-align: top\">";

			StringTokenizer st = new StringTokenizer(this.change_details, "#", true);
			int i = 1;

			while (st.hasMoreElements()) {
				String str = (String) st.nextElement();

				if(i == 1) {
					if(!str.equals("#"))
						html_line += "Chemical Name: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 2) {
					if(!str.equals("#"))
						html_line += "Formula: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 3) {
					if(!str.equals("#"))
						html_line += "CAS: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 4) {
					if(!str.equals("#"))
						html_line += "Density: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 5) {
					if(!str.equals("#"))
						html_line += "Reg. date: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 6) {
					if(!str.equals("#"))
						html_line += "Reg. by: "+Util.encodeTagAndNull(str) + "<br/>";
				}
				else if(i == 7) {
					if(!str.equals("#"))
						html_line += "Method: "+Util.encodeTagAndNull(str) + "<br/>";
				}

				i++;			
			}

			html_line += "	</td>"+
			"	<td style=\"vertical-align: top\"><a title=\""+Util.encodeTagAndNull(this.text)+"\" href=\""+Attributes.HISTORY_BASE+"?action=search_history&repeatSearch=true&repeatSearchId="+id+"\">"+
			"		RUN QUERY"+//text = the resulting sql
			"	</a></td>"+
			"	<td style=\"vertical-align: top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//Search by
			"	</td>"+
			"	<td style=\"vertical-align: top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//timestamp
			"	</td>";
			if(structure == null || structure.equals("") || structure.trim().length() < 15) {
				html_line += "	<td style=\"vertical-align: top\">"+
				"No structure"+
				"</td>";
			} 
			else {
				html_line += "	<td style=\"vertical-align: top\"><a href=\"#\" title=\"Display Structure data in new window.\" onclick=\"openWindow(\'"+Attributes.HISTORY_BASE+"?action=search_structure&search_Id="+id+"\')\">"+
				"Show Structure"+//Show the strucure data
				"	</a></td>";
			}
		}

		//Handle container created line
		if(this.text.equals(History.CREATE_CONTAINER))
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td>" +
			"		--"+//change details is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		--" +//old value is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		" +Util.encodeTagAndNull(this.new_value)+//new value = the registered value
			"	</td>"+
			"	<td>" +
			"		" +Util.encodeTagAndNull(this.unit)+//unit
			"	</td>";
		}
		//handle container check in
		if(this.text.equals(History.CHECK_IN))
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td>" +
			"		--"+//change details is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		" + Util.encodeTag(this.old_value)+
			"	</td>"+
			"	<td>" +
			"		" +Util.encodeTagAndNull(this.new_value)+//new value = the registered value
			"	</td>"+
			"	<td>" +
			"		" +Util.encodeTagAndNull(this.unit)+//unit
			"	</td>";
		}
//		handle container check out
		if(this.text.equals(History.CHECK_OUT))
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td>" +
			"		--"+//change details is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		--" +//old value is empty for check out
			"	</td>"+
			"	<td>" +
			"		--"+//new value is empty for check out
			"	</td>"+
			"	<td>" +
			"		--" +//unit is empty for checkout
			"	</td>";
		}
		//handle delete of container
		if(this.text.equals(History.DELETE) && this.table.equals(History.CONTAINER_TABLE))
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td>" +
			"		--"+//change details is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		" +this.old_value+//old value
			"	</td>"+
			"	<td>" +
			"		"+this.new_value+//new value
			"	</td>"+
			"	<td>" +
			"		" +this.unit+//unit
			"	</td>";
		}
		//Handle transfer of a container
		if(this.text.indexOf(History.TRANSFER_BY) != -1)
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td>" +
			"		--"+//change details is empty for creation of a container
			"	</td>"+
			"	<td>" +
			"		" +this.old_value+//old value
			"	</td>"+
			"	<td>" +
			"		"+this.new_value+//new value
			"	</td>"+
			"	<td>" +
			"		" +this.unit+//unit
			"	</td>";
		}

		//handle moving a container.
		if(this.text.indexOf(History.MOVE_CONTAINER) != -1 && this.table.equals(History.CONTAINER_TABLE))
		{
			/*
			 * Create the details
			 */
			String the_details = changeDetails();		

			//encode the text remark of the change line
			this.text = Util.encodeTagAndNull(this.text);
			this.text = this.text.replaceAll("\n", "<br//>");

			String old_location = "";
			String new_location = "";
			int u = 0;

			StringTokenizer strtok = new StringTokenizer(change_details, "|");

			while (strtok.hasMoreElements()) {
				String tmpstr = (String) strtok.nextElement();

				if(u == 0)
					new_location = tmpstr;
				else
					old_location = tmpstr;

				u++;
			}

			html_line = "	<td valign=\"top\">" +
			"		" +this.text+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td align=\"center\"  valign=\"top\">" +
			"		<table class=\"history\" border=\"1\">" +
			"			<tr>" +
			"				<th width=\"35%\">" +
			"					Old Loc." +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					New Loc." +
			"				</th>" +
			"			</tr>" +
			"			<tr>" +
			"				<td align=\"center\">" + old_location+				
			"				</td>" +
			"				<td align=\"center\">" + new_location+
			"				</td>" +
			"			</tr>" +
			"		</table>"+//change details is empty for creation of a container
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--"+//old value user change details
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--"+//new value use change details
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--" +//unit use change details
			"	</td>";
		}

		//handle modify of a container.
		if(this.text.indexOf(History.MODIFY) != -1 && this.table.equals(History.CONTAINER_TABLE))
		{
			/*
			 * Create the details
			 */
			String the_details = changeDetails();		

			//encode the text remark of the change line
			this.text = Util.encodeTagAndNull(this.text);
			this.text = this.text.replaceAll("\n", "<br//>");

			html_line = "	<td valign=\"top\">" +
			"		" +this.text+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td align=\"center\"  valign=\"top\">" +
			"		<table class=\"history\" border=\"1\">" +
			"			<tr>" +
			"				<th width=\"30%\">" +
			"					Field" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					Old" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					New" +
			"				</th>" +
			"			</tr>" +
			"			" +the_details+
			"		</table>"+//change details is empty for creation of a container
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--"+//old value user change details
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--"+//new value use change details
			"	</td>"+
			"	<td valign=\"top\">" +
			"		--" +//unit use change details
			"	</td>";
		}

		/*Create the history line, for a creation of a compound or modification of a compound*/
		if(this.text.equals(History.CREATE_COMPOUND) || (this.text.indexOf(History.MODIFY) != -1 && this.table.equals(History.COMPOUND_TABLE)))
		{
			/*
			 * Create the details
			 */
			String the_details = changeDetails();		

			//encode the text remark of the change line
			this.text = Util.encodeTagAndNull(this.text);
			this.text = this.text.replaceAll("\n", "<br//>");

			html_line = "	<td valign=\"top\">" +
			"		" +this.text+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td align=\"center\"  valign=\"top\">" +
			"		<table class=\"history\" border=\"1\">" +
			"			<tr>" +
			"				<th width=\"30%\">" +
			"					Field" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					Old" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					New" +
			"				</th>" +
			"			</tr>" +
			"			" +the_details+
			"		</table>"+
			"	</td>";
		}
		//History line for delete compound
		if(this.text.indexOf(History.DELETE_COMPOUND) != -1)
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td valign=\"top\">" +Util.encodeTagAndNull(this.text)+"</td>";
		}

		//History line for delete compound
		if(this.text.indexOf(History.DISPLAY_COMPOUND) != -1 && this.table.indexOf(History.COMPOUND_TABLE) != -1)
		{
			html_line = "	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.text)+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td valign=\"top\">" +Util.encodeTagAndNull(this.change_details)+"</td>";
		}

		/*history line for the creation and modifycation of a batch*/
		if(this.text.equals(History.CREATE_BATCH) || (this.text.indexOf(History.MODIFY_BATCH) != -1 && this.table.equals(History.BATCH_TABLE)))
		{
			/*
			 * Create the details
			 */
			String the_details = changeDetails();		

			//encode the text remark of the change line
			this.text = Util.encodeTagAndNull(this.text);
			this.text = this.text.replaceAll("\n", "<br//>");

			html_line = "	<td valign=\"top\">" +
			"		" +this.text+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td align=\"center\"  valign=\"top\">" +
			"		<table class=\"history\" border=\"1\">" +
			"			<tr>" +
			"				<th width=\"30%\">" +
			"					Field" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					Old" +
			"				</th>" +
			"				<th width=\"35%\">" +
			"					New" +
			"				</th>" +
			"			</tr>" +
			"			" +the_details+
			"		</table>"+
			"	</td>";
		}
		/*history line for lock and unlock of a batch*/
		if(this.text.equals(History.LOCK_BATCH) || (this.text.indexOf(History.UNLOCK_BATCH) != -1 && this.table.equals(History.BATCH_TABLE)))
		{	
			//encode the text remark of the change line
			this.text = Util.encodeTagAndNull(this.text);
			this.text = this.text.replaceAll("\n", "<br//>");

			html_line = "	<td valign=\"top\">" +
			"		" +this.text+//type of change
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.changed_by)+//changed by
			"	</td>"+
			"	<td valign=\"top\">" +
			"		" +Util.encodeTagAndNull(this.timestamp)+//time stamp
			"	</td>"+
			"	<td align=\"center\"  valign=\"top\">" +
			"		--"+
			"	</td>";
		}

	}


	/**
	 * Create and return the details for a history line.
	 * @param details
	 * @return
	 */
	public String changeDetails()
	{
		String the_details = "";

		StringTokenizer tokens = new StringTokenizer(this.change_details, "|");
		while(tokens.hasMoreTokens())
		{
			String token = tokens.nextToken().trim();
			int i = 0;

			StringTokenizer tokens2 = new StringTokenizer(token, ";");
			while(tokens2.hasMoreTokens())
			{
				String token2 = tokens2.nextToken().trim();
				token2 = Util.encodeTagAndNull(token2);
				i++;

				if(i==1)
				{
					the_details = the_details + "<tr>" +
					"	<td>" +
					"		<b>"+token2+":</b>"+
					"	</td>";
				}
				if(i==2)
				{
					the_details = the_details + 
					"	<td>" +
					"		"+token2+
					"	</td>";
				}
				if(i==3)
				{
					the_details = the_details + 
					"	<td>" +
					"		"+token2+
					"	</td>" +
					"</tr>";
				}
			}//end inner tokenizer for deteils on eg. location

		}//end outher tokenizer on the list of changes for a container.

		return the_details;
	}

	/**
	 * @return Returns the html_line.
	 */
	public String getHtml_line() {
		return html_line;
	}


	/**
	 * @return the structure
	 */
	public String getStructure() {
		return structure;
	}


	/**
	 * @param structure the structure to set
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}
}