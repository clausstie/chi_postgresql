package chemicalinventory.table;

import java.util.Iterator;
import java.util.Vector;
import chemicalinventory.utility.Return_codes;
import chemicalinventory.utility.Util;

public class HTMLTable extends CITable {

	public HTMLTable() {
		/*
		 * The first element in the array is a list of action parameters for the
		 * link/button in the following syntax ["&id=37&compound=3837....etc"]
		 * ["9348"] ["23 mg] ["SYS"] .... more more more... (int linesPerPage,
		 * Vector lineList, // all lines for the result, each line is an array!.
		 * int width, //table width String cssTableClass, //css class for the
		 * entire table String hrAlign, //align in the header String cssHrClass, //
		 * css for the header String cssTrEven, //css for even rows String
		 * cssTrOdd, //css for odd rows String headerColour,//colour of the
		 * header int noOfColoums, //no of coloumns in the table. String[]
		 * headers, // list of headers int[] headerwidths, // headerwidths int
		 * actionType, //use button or link in the table String table_action,
		 * //action when button or link pressed in the table. String
		 * page_action, //action when go-to page link pressed, and also to the
		 * order by, for header coloums. int offset //page to display.
		 */
	}

	/**
	 * 
	 * @param linesPerPage
	 * @param lineList
	 * @param width
	 * @param cssTableClass
	 * @param hrAlign
	 * @param tdAlign
	 * @param cssHrClass
	 * @param cssTrEven
	 * @param cssTrOdd
	 * @param headerColour
	 * @param noOfColoums
	 * @param headers
	 * @param headerwidths
	 * @param actionType
	 * @param buttonText
	 * @param table_action
	 * @param page_action
	 * @param orderByAction
	 * @return Status of the opration.
	 */
	public int createTable(int linesPerPage, Vector lineList, int width,
			String cssTableClass, String hrAlign, String tdAlign,
			String cssHrClass, String cssTrEven, String cssTrOdd,
			String headerColour, int noOfColoums, String[] headers,
			int[] headerwidths, int actionType, String buttonText,
			String table_action, String page_action, String orderByAction) {
		
		if (cssTableClass == null)
			cssTableClass = "";

		//handle the lines per page value.
		if(linesPerPage <= 0)
			linesPerPage = 10; 
		else if(linesPerPage > 100)
		{
			if(lineList != null && lineList.size() > 0)
				linesPerPage = lineList.size();
			else
				linesPerPage = 1;
		}
				
		// alter the width of the table
		int cellpadding = 0;
		
		if (actionType == CITable.SINGLE_BUTTON_MODE) {
			width = width + 225;// The desired width plus button and counter row.
			cellpadding = 0;
		} else {
			width = width + 75;//the desired width plus counter row.
			cellpadding = 2;
		}
		
		/*
		 * Create a table element to hold a dropdown box for
		 * selecting no. to show per pages.
		 */
		StringBuffer no_per_page_table = new StringBuffer();
		no_per_page_table.append("<FORM name=\"search_result\">\n");
		
		no_per_page_table.append("<TABLE border=\"0\"");
		no_per_page_table.append(" cellspacing=\"0\" cellpadding=\"3\" width=\""+ width + "px>\"\n");
		no_per_page_table.append("<TR><TD align=\"left\">No. of results pr. page:&nbsp;\n");
		no_per_page_table.append("<SELECT name=\""+HTMLTable.NO_PER_PAGE+"\" ");				
		no_per_page_table.append("onChange=\"location.href=this.options[this.selectedIndex].value\">\n");
		
		no_per_page_table.append("<OPTION value=\"\">[-- SELECT --]");
		no_per_page_table.append("</OPTION>\n");
	
		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=5"+"\">5");
		no_per_page_table.append("</OPTION>\n");
		
		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=10"+"\">10");
		no_per_page_table.append("</OPTION>\n");
		
		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=25"+"\">25");
		no_per_page_table.append("</OPTION>\n");

		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=50"+"\">50");
		no_per_page_table.append("</OPTION>\n");
		
		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=100"+"\">100");
		no_per_page_table.append("</OPTION>\n");
		
		no_per_page_table.append("<OPTION value=\""+page_action + "&" + HTMLTable.NO_PER_PAGE +"=101"+"\">Show All");
		no_per_page_table.append("</OPTION>\n");
		
		no_per_page_table.append("</SELECT>\n");
		no_per_page_table.append("<BR><BR>\n");
		no_per_page_table.append("</TR></TD>\n");
		no_per_page_table.append("</TABLE>\n");

		/* Start the table holding results.*/
		StringBuffer startTable = new StringBuffer();
		startTable.append("<TABLE class=\"");
		startTable.append(cssTableClass
				+ "\" cellspacing=\"0\" cellpadding=\"3\" width=\"" + width
				+ "px>\"\n");
		startTable.append("<TR><TD>\n");

		startTable.append("<TABLE border=\"0\" class=\"");
		startTable.append("\" cellspacing=\"0\" cellpadding=\"" + cellpadding
				+ "\" width=\"100%>\"\n");

		/* Create the header row */
		StringBuffer headerRows = new StringBuffer();
		headerRows.append("<TR class=\"special\">\n");

		// first add the header for the row counter.
		headerRows.append("<TH class=\"" + cssHrClass + "\" align=\"" + hrAlign
				+ "\" width=\"75\">#</TH>\n");

		// add the user defined rows.
		for (int i = 0; i < noOfColoums; i++) {
			String header_value = headers[i];

			// make the headers clickable, to sort/order by the header value.
			if (Util.isValueEmpty(orderByAction)) {
				String orderAddOn = "&" + HTMLTable.ORDER_BY + "=" + (i + 1);
				header_value = "<A class=\"black\" href=\"" + orderByAction
						+ orderAddOn + "\">" + header_value + "</A>";
			}

			String header_width = "";
			if (headerwidths != null && headerwidths.length == headers.length) {
				int n = headerwidths[i];
				header_width = "width=\"" + String.valueOf(n) + "\"";
			}

			headerRows.append("<TH class=\"" + cssHrClass + "\" align=\""
					+ hrAlign + "\" " + header_width + ">");
			headerRows.append(header_value + "</TH>\n");
		}

		// If button mode, add extra header row for the button.
		if (actionType == CITable.SINGLE_BUTTON_MODE)
			headerRows.append("<TH class=\"" + cssHrClass
					+ "\" align=\" width=\"150\"" + hrAlign
					+ "\">&nbsp;</TH>\n");

		// end header row.
		headerRows.append("</TR>\n");

		/*
		 * Create the individual lines of information in the table.
		 */
		Vector result_buffer = new Vector();
		int ind_counter = 0;

		if (lineList != null && lineList.size() > 0) {
			// run through the entire list
			for (int i = 0; i < lineList.size(); i++) {

				String[] a_line = (String[]) lineList.get(i);

				// Define the coloring for the individual lines in the table.
				String tr_class = cssTrOdd;
				if (ind_counter % 2 == 0)
					tr_class = cssTrEven;

				/*
				 * Create the href value for the line.
				 */
				String href = table_action + a_line[0];

				/*
				 * A single line in the table.
				 */
				StringBuffer resultline = new StringBuffer();

				resultline.append("<TR class=\"" + tr_class + "\">\n");

				// loop through the single line.
				for (int j = 1; j <= noOfColoums; j++) {

					// Add a row counter to the table.
					if (j == 1) {
						resultline.append("<TD align=\"" + tdAlign + "\">\n");

						if (actionType == CITable.SINGLE_BUTTON_MODE)
							resultline.append((i + 1));
						else if (actionType == CITable.LINK_MODE)
							resultline.append("<A class=\"table_link\" href=\""
									+ href + "\">" + (i + 1) + "</A>\n");

						resultline.append("</TD>\n");
					}

					resultline.append("<TD align=\"" + tdAlign + "\">\n");

					if (actionType == CITable.SINGLE_BUTTON_MODE)
						resultline.append(a_line[j]);
					else if (actionType == CITable.LINK_MODE)
						resultline.append("<A class=\"table_link\" href=\""
								+ href + "\">" + a_line[j] + "</A>\n");

					resultline.append("</TD>\n");
				}

				// add a button to the end of the line.
				if (actionType == CITable.SINGLE_BUTTON_MODE) {
					if (!Util.isValueEmpty(buttonText))
						buttonText = "Submit";

					resultline.append("<FORM method=\"post\" action=\"" + href
							+ "\">\n");
					resultline.append("<TD align=\"center\">\n");
					resultline
							.append("<INPUT class=\"submit\" type=\"submit\" name=\"Submit\" value=\""
									+ buttonText + "\">\n");
					resultline.append("</TD>\n");
					resultline.append("</FORM>\n");
				}

				// end the row.
				resultline.append("</TR>\n");

				// Count up the line
				ind_counter++;

				// IF the resultbuffer is empty add the header line.
				if (result_buffer.isEmpty()) {
					result_buffer.add(no_per_page_table.toString());
					result_buffer.add(startTable.toString());
					result_buffer.add(headerRows.toString());
				}

				// add the row to the buffer of results.
				result_buffer.add(resultline.toString());

				if ((i > 0 && linesPerPage == 1) || i > 0)// avoid deviding by
															// zero
				{
					if ((i + 1) % linesPerPage == 0 || i == lineList.size() - 1
							|| linesPerPage == 1) {
						// end the table
						result_buffer.add("</TABLE>\n");
						startTable.append("</TR></TD>\n");
						result_buffer.add("</TABLE>\n");

						// add the elements for one page of results, and clear
						// the result_buffer list.
						this.list.add(new Vector(result_buffer));
						result_buffer.clear();
						ind_counter = 0;
					}
				} else if ((i == 0 && i == lineList.size() - 1)
						|| linesPerPage == 1) {
					// end the table
					result_buffer.add("</TABLE>\n");
					startTable.append("</TR></TD>\n");
					result_buffer.add("</TABLE>\n");

					// only one result add this, and clear the result_buffer
					// list.
					this.list.add(new Vector(result_buffer));
					result_buffer.clear();
					ind_counter = 0;
				}
			}// end loop through the individual list of lines for the table.

			/*
			 * Create the list of available pages as links beneath the table.
			 * But only if there is more than one page.
			 */
			StringBuffer pagelist = new StringBuffer();

			pagelist.append("<TABLE border=\"0\"");
			pagelist.append(" cellspacing=\"0\" cellpadding=\"3\" width=\""
					+ width + "px\"\n");
			pagelist.append("<TR><TD>\n");
			pagelist.append("<BR>\n");
			pagelist.append("<P>Page(s):&nbsp;");

			// create a href list of pages and add to this object.
			for (int i = 0; i < this.list.size(); i++) {
				pagelist.append("<A class=\"black_u\" href=\"" + page_action + "&" + HTMLTable.NO_PER_PAGE +"="+ linesPerPage
						+ "&" + HTMLTable.PAGE_NO + "=" + (i + 1) + "\">"
						+ (i + 1) + "</A>");
				pagelist.append("&nbsp;\n");
			}

			pagelist.append("</TR></TD>\n");
			pagelist.append("</TABLE>\n");
			pagelist.append("</FORM>\n");

			/*
			 * Now add the page list to the end of each page in the list of
			 * pages..
			 */
			for (Iterator iter = this.list.iterator(); iter.hasNext();) {
				Vector element = (Vector) iter.next();
				element.add(pagelist.toString());
			}
		} else {
			// handle empty lists.

			result_buffer.add(startTable.toString());
			result_buffer.add(headerRows.toString());

			StringBuffer resultline = new StringBuffer();
			resultline.append("<TR class=\"" + cssTrEven + "\">\n");
			resultline.append("<TD align=\"left\" colspan=\"10\">\n");
			resultline.append(".......No data in the result set.\n");
			resultline.append("</TD>\n");
			resultline.append("</TR>\n");

			// end the table
			resultline.append("</TABLE>\b");
			startTable.append("</TR></TD>\n");
			result_buffer.add("</TABLE>\n");
			result_buffer.add("</FORM>\n");
			
			result_buffer.add(resultline.toString());

			// add the elements for one page of results, and clear the
			// result_buffer list.
			this.list.add(new Vector(result_buffer));
			result_buffer.clear();
		}

		return Return_codes.SUCCESS;
	}

	/**
	 * 
	 * @param page
	 * @return
	 */
	public String writeTable(int page) {
		page = page - 1;

		// make sure that the page to display is a valid page value.
		if (page <= 0)
			page = 0;
		else if (page > this.list.size()-1)
			page = (this.list.size() - 1);

		Vector table_list = (Vector) this.list.get(page);

		StringBuffer table = new StringBuffer();

		for (Iterator iter = table_list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();

			table.append(element);
		}

		return table.toString();
	}
}