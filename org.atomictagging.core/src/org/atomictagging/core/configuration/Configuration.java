/**
 * 
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
	public static final String	BASE_DIR	= System.getProperty("user.home") + "/.atomictagging/";
}
