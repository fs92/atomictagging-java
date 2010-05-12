/**
 * 
 */
package org.atomictagging.utils;

import java.util.List;

/**
 * Some helper methods for handling strings
 * 
 * TODO Check whether this is still appropriate or a switch to Apache commons would be in order.
 * 
 * @author Stephan Mann
 */
public class StringUtils {

	/**
	 * Joins a list of strings to one string
	 * 
	 * @param strings
	 * @param delimiter
	 * @return Joined string
	 */
	public static String join( final List<String> strings, final String delimiter ) {
		assert strings != null;

		if (strings.isEmpty()) {
			return "";
		}

		boolean first = true;
		StringBuilder builder = new StringBuilder();

		for ( String string : strings ) {
			if (!first) {
				builder.append( delimiter );
			}
			first = false;
			builder.append( string );
		}

		return builder.toString();
	}


	/**
	 * Cut a string that is longer than the given length an append three dots "..."
	 * 
	 * @param string
	 * @param length
	 * @return The given string or a cut version, if the string was longer than the given length
	 */
	public static String cut( final String string, final int length ) {
		return ( string.length() > length ) ? string.substring( 0, length - 3 ) + "..." : string;
	}
}
