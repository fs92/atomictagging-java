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
package org.atomictagging.core.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Tags that are added and interpreted by the system.
 * 
 * @author Stephan Mann
 */
public final class CoreTags {

	/**
	 * The tag that gets attached to all atoms pointing to a file on the file system.
	 */
	public final static String	FILEREF_TAG				= "x-fileref";

	/**
	 * The tag that gets attached to all atoms pointing to a file not on the local file system. These files might not be
	 * available at all times and therefore need special treatment.
	 */
	public final static String	FILEREF_REMOTE_TAG		= "x-remotefile";

	/**
	 * The tag that gets attached to atoms that point to a remote location specified by the configuration.
	 */
	public final static String	FILEREF_REMOTE_LOCATION	= "x-remotelocation";

	/**
	 * Generic tag for files which type is unknown.
	 */
	public final static String	FILETYPE_UNKNOWN		= "x-filetype-unknown";

	/**
	 * Tag for video files.
	 */
	public final static String	FILETYPE_VIDEO			= "x-filetype-video";

	/**
	 * Tag for image files.
	 */
	public final static String	FILETYPE_IMAGE			= "x-filetype-image";


	public static List<String> asList() {
		final List<String> coreTags = new ArrayList<String>();

		try {
			for ( final Field field : CoreTags.class.getDeclaredFields() ) {
				coreTags.add( (String) field.get( CoreTags.class ) );
			}
		} catch ( final IllegalArgumentException e ) {
			e.printStackTrace();
		} catch ( final IllegalAccessException e ) {
			e.printStackTrace();
		}

		return coreTags;
	}
}
