package chemicalinventory.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemicalinventory.context.*;
import chemicalinventory.utility.*;

public class Database {

	/**
	 * Performs an update of a database tuple.
	 * 
	 * @param sql
	 * @return
	 */
	public static int performUpdate(String sql) {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					stmt.executeUpdate(sql);
				}
				con.close();
				return Return_codes.SUCCESS;
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return Return_codes.GENERAL_ERROR;
		}
		return Return_codes.GENERAL_ERROR;
	}

	/**
	 * Perform an insert using the sql received.
	 * 
	 * The return value (int) can be:
	 * 
	 * 0: the operation did NOT succed X: the key of the inserted tuple.
	 * 
	 * @param sql
	 * @return
	 */
	public static int performInsert_WKey(String sql) {
		try {
			/*
			 * The key to return.
			 */
			int incKey = -1;

			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

					ResultSet key1 = stmt.getGeneratedKeys();

					if (key1.next()) {
						incKey = key1.getInt(1);
						con.close();
						return incKey;
					} else {
						con.close();
						return 0;
					}
				}
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	/**
	 * Perform an insert to the database based on the sql received as a
	 * parameter.
	 * 
	 * @param sql
	 * @return Status of the operation succes or creation_failed.
	 */
	public static int performInsert(String sql) {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {
					Statement stmt = con.createStatement();

					stmt.executeUpdate(sql);
					con.close();
					return Return_codes.SUCCESS;
				}
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return Return_codes.CREATION_FAILED;
		}
		return Return_codes.CREATION_FAILED;
	}

	/**
	 * Perform the query received as an sql string...
	 * 
	 * @param sql
	 * @return The ResultSet from the query. The resultset is null, if nothing
	 *         found, or error.
	 */
	public static ResultSet performQuery(Statement stmt, 
			Connection con,
			String sql) {
		
	try {
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				return rs;
			} else {
				return null;
			}

		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Perform a query based on the received sql string. This method returns the
	 * amount of hits the query got from the db.
	 * 
	 * @param sql
	 * @return size of the resultset.
	 */
	public static int performQuery_getSize(String sql) {
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con2 = ds.getConnection();
				if (con2 != null) {
					Statement stmt = con2.createStatement();

					ResultSet rs = stmt.executeQuery(sql);
					int i = 0;

					while (rs.next()) {
						i++;
					}

					con2.close();
					return i;
				}
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	/**
	 * Get a connection from the database.
	 * @return
	 */
	public static Connection getDBConnection()
	{
		try {
			// Connection from the pool
			Context init = new InitialContext();
			if (init == null)
				throw new Exception("No Context");

			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if (ds != null) {
				Connection con = ds.getConnection();
				if (con != null) {				
					return con;
				}
				else
					return null;
			}
		}// end of try

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
}