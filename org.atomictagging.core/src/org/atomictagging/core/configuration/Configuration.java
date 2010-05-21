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
package org.atomictagging.core.configuration;

/**
 * The users configuration
 * 
 * TODO stephan@2010-04-02 Of course that must be externalized to a file or to system properties
 * 
 * @author Stephan Mann
 */
public class Configuration {
	/**
	 * JDBC connection string to connect to the database. (jdbc:[dbtype]://[host]/[database])
	 */
	public static final String	JDBC_URL	= "jdbc:mysql://localhost/atomictagging";

	/**
	 * Database user
	 */
	public static final String	DB_USER		= "atomictagging";

	/**
	 * Database password
	 */
	public static final String	DB_PASSWORD	= "";

	/**
	 * Directory in with all imported files will be stored.
	 */
	public static final String	BASE_DIR	= System.getProperty( "user.home" ) + "/.atomictagging/";
}
