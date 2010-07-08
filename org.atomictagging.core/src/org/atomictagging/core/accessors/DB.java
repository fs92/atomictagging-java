/**
 * This file is part of Atomic Tagging.
 * 
 * Atomic Tagging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Atomic Tagging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Atomic Tagging. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.atomictagging.core.accessors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.configuration.CombinedConfiguration;
import org.atomictagging.core.configuration.Configuration;

/**
 * A temporary class holding the connection to the MySQL server
 * 
 * @author Stephan Mann
 */
public class DB {
	/**
	 * Connection to the MySQL server.
	 */
	static public Connection	CONN;


	private DB() {
		// Utility class
	}

	static {
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
		} catch ( ClassNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CombinedConfiguration conf = Configuration.get();

		String type = conf.getString( "database.type" );
		String host = conf.getString( "database.host" );
		String db = conf.getString( "database.db" );
		String user = conf.getString( "database.user" );
		String pass = conf.getString( "database.pass" );

		String connectString = "jdbc:" + type + "://" + host + "/" + db;

		try {
			CONN = DriverManager.getConnection( connectString, user, pass );
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
