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
package org.atomictagging.core.services;

import java.util.List;

import org.atomictagging.core.types.IMolecule;

/**
 * @author Stephan Mann
 */
public interface IMoleculeService {

	/**
	 * @param moleculeId
	 * @return The molecule with the given ID
	 */
	IMolecule find( long moleculeId );


	/**
	 * Read all molecules from the DB that are either tagged with the given tags or contain an atom that is tagged with
	 * those tags.
	 * 
	 * @param tags
	 * @return List of molecules as read from the DB.
	 */
	List<IMolecule> find( List<String> tags );


	/**
	 * @param molecule
	 * @return The ID of the saved molecule as returned from the DB.
	 */
	long save( IMolecule molecule );

}
