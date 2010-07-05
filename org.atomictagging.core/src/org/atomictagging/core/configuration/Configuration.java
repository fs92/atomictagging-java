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

import java.io.File;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

/**
 * Representation of the users configuration<br>
 * <br>
 * Core is depending on meaningful values being returned, so it is the applications job to initialize this
 * configuration.
 * 
 * @author Stephan Mann
 */
public class Configuration {

	private static CombinedConfiguration	conf	= new CombinedConfiguration();


	/**
	 * Adds a configuration file to the global configuration.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void addFile( File file ) throws Exception {
		conf.addConfiguration( new HierarchicalINIConfiguration( file ) );
	}


	/**
	 * Returns a copy of the current configuration state.
	 * 
	 * @return Current global configuration
	 */
	public static CombinedConfiguration get() {
		return (CombinedConfiguration) conf.clone();
	}

}
