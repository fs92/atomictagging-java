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

import java.sql.SQLException;
import java.util.List;

import org.atomictagging.core.types.IAtom;

/**
 * This is the entity based service for atoms. In provides CRUD as well as advanced search operations for atoms.
 * 
 * @author Stephan Mann
 */
public interface IAtomService {

	/**
	 * Decide whether a given filter should be inclusive or exclusive.
	 */
	public enum Filter {
		/**
		 * The given filter is inclusive. The given arguments should be part of the result.
		 */
		INCLUDE,
		/**
		 * The given filter is exclusive. The given arguments should <b>not</b> be part of the result.
		 */
		EXCLUDE
	}


	/**
	 * Returns the atom with the given ID from the database.
	 * 
	 * @param atomId
	 * @return A consistent atom or null, if no atom was found with the given ID or a database error occurred
	 */
	IAtom find( long atomId );


	/**
	 * Returns a list of atoms which are tagged with at least one of the given tags.
	 * 
	 * @param inclusiveTags
	 * @return A list of atoms or an empty list if no atoms where found or a database error occurred. Never null.
	 */
	List<IAtom> find( List<String> inclusiveTags );


	/**
	 * Returns a list of atoms which are either tagged with one of the given tags or which are <b>not</b> tagged with
	 * the given tags, depending on the given filter option.
	 * 
	 * @param tags
	 * @param filter
	 * @return A list of atoms or an empty list if no atoms where found or a database error occurred. Never null.
	 */
	List<IAtom> find( List<String> tags, Filter filter );


	/**
	 * 
	 * @return
	 */
	List<IAtom> findUserAtoms();


	void save( IAtom atom );


	void delete( IAtom atom );


	/**
	 * @param atoms
	 * @return
	 * @throws SQLException
	 */
	List<Long> save( List<IAtom> atoms ) throws SQLException;

}
