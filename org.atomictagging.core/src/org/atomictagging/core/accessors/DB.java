/**
 * 
 */
package org.atomictagging.core.accessors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.atomictagging.core.configuration.Configuration;

/**
 * A temporary class holding the connection to the MySQL server and the data required to connect to the right database.
 * 
 * @author Stephan Mann
 */
public class DB {
	/**
	 * Connection to the MySQL server.
	 */
	static public Connection	CONN;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			CONN = DriverManager.getConnection(Configuration.JDBC_CONNECT_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
