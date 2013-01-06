package chemicalinventory.table;

import java.util.Vector;

public class CITable {
	
	protected int status = 0;
	protected int noOfPagesCounter = 0;
	protected Vector list = new Vector();
	
	/** The table will not contain links or buttons **/
	public static final int READ_ONLY_MODE = 1;
	/**To each line will added one button to the end, with a single action. **/
	public static final int SINGLE_BUTTON_MODE = 2;
	/**The text on the line will point to a <a href=xxx> link. no buttons. **/
	public static final int LINK_MODE = 3;	
	
	public static final String PAGE_NO = "page_no";
	public static final String ORDER_BY = "order_by";
	public static final String NO_PER_PAGE = "linesPerPage";
	
	/**The value to register the generated table in the session object.*/
	public static final String SESSION_TABLE = "session_table";
	
	
	public String createTable()
	{
		return "";
	}
	
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return Returns the list.
	 */
	public Vector getList() {
		return list;
	}

}
