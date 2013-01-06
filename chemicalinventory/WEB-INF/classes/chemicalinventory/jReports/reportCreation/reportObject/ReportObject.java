package chemicalinventory.jReports.reportCreation.reportObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import chemicalinventory.context.Attributes;
import chemicalinventory.db.Database;
import chemicalinventory.utility.Util;

/**
 * Creating the actual html for one report object, ready
 * to be displayed on the page.
 * 
 * @author Dann Vestergaard.
 *
 */
public class ReportObject {
	
	private StringBuffer sb = null;
	
	/**
	 * Create the elements for an report object..
	 * 
	 * @param id
	 * @return String representation of a report definition
	 * null if none for the id registered.
	 */
	public String createReportObject(String id)
	{
		try {
			Connection con = Database.getDBConnection();
			Statement stmt = con.createStatement();
			
			ResultSet set = Database.performQuery(stmt, con, "SELECT r.report_id, r.report_name, r.display_name, r.description" +
					" FROM reports r" +
					" WHERE r.report_name = '"+id+"';");
			
			if(set != null)
			{
				set.beforeFirst();
				this.sb = new StringBuffer();
				
				if(set.next())
				{
					String report_name = set.getString("r.report_name");
					
					this.sb.append("<FORM method=\"post\" action=\"\">");
					this.sb.append("\n");
					this.sb.append("<TABLE class=\"box\" width=\"500\">");
					this.sb.append("\n");
					this.sb.append("<TR>");
					this.sb.append("\n");
					this.sb.append("<TH colspan=\"2\" class=\"blue\">");
					this.sb.append(Util.encodeTagAndNull(set.getString("r.display_name")));
					this.sb.append("</TH>");
					this.sb.append("\n");
					this.sb.append("</TR>");
					this.sb.append("\n");
					this.sb.append("<TR>");
					this.sb.append("\n");
					this.sb.append("<TH class=\"blue\" width=\"100\">Description:</TH>");
					this.sb.append("\n");
					this.sb.append("<TD>");
					this.sb.append(Util.encodeTagAndNull(set.getString("r.description")));
					this.sb.append("</TD>");
					this.sb.append("\n");
					this.sb.append("</TR>");
					this.sb.append("\n");
					/*
					 * insert the parameters to be filled in by the user
					 */
					createParamters(set.getString("r.report_id"), con, stmt);
					
					/*
					 * Create the button to click to create the
					 * pdf report, and the url to user for this.
					 */
					this.sb.append("<TR align=\"right\">");
					this.sb.append("\n");
					this.sb.append("<TD colspan=\"2\">");
					this.sb.append("\n");
					this.sb.append("<HR>");
					this.sb.append("\n");
					this.sb.append("<INPUT class=\"submit\" type=\"submit\" value=\"Create Report\"");
					this.sb.append("onclick=\"this.form.action=\'"+Attributes.PRINT_BASE+"?action=printindex&print=yes&report_type=");
					this.sb.append(report_name);
					this.sb.append("\'\"/>");
					this.sb.append("\n");
					this.sb.append("</TD>");
					this.sb.append("\n");
					this.sb.append("</TR>");
					this.sb.append("\n");
					this.sb.append("</TABLE>");
					this.sb.append("\n");
					this.sb.append("</FORM>");
					this.sb.append("\n");
					this.sb.append("<BR>");
				}
				
				con.close();
				
				if(this.sb != null)
				{
					return sb.toString();	
				}
				else
				{
					return null;
				}				
			}
			else
			{
				con.close();
				return null;
			}
			
		} catch (SQLException e) {
			
			return null;
		}
	}
	
	/**
	 * Get the parameter values and create data for them..
	 * 
	 * @param id
	 * @param con1
	 * @param stmt1
	 */
	private void createParamters(String id, Connection con1, Statement stmt1)
	{
		try {
			ResultSet set = Database.performQuery(stmt1, con1, "SELECT parameter_name" +
					" FROM report_parameters r" +
					" WHERE report_id = "+id+";");
			
			if(set != null)
			{
				set.beforeFirst();
				
				while(set.next())
				{
					String parameter_name = set.getString("parameter_name");
					
					this.sb.append("<TR>");
					this.sb.append("\n");
					this.sb.append("<TH class=\"blue\" width=\"100\">");
					this.sb.append(Util.encodeTagAndNull(parameter_name)+":");
					this.sb.append("</TH>");
					this.sb.append("\n");
					this.sb.append("<TD>");
					this.sb.append("\n");
					this.sb.append("<INPUT type=\"text\" name=\""+Attributes.REPORT_PARAM+parameter_name+"\"/>");
					this.sb.append("</TD>");
					this.sb.append("\n");
					this.sb.append("</TR>");
					this.sb.append("\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
