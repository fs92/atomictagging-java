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

import java.util.List;

import org.atomictagging.core.types.Atom.AtomBuilder;

/**
 * Representation of an atom
 * 
 * @author Stephan Mann
 */
public interface IAtom {

	/**
	 * Get the ID of this atom.
	 * 
	 * @return The ID of this atom, will be 0 if atom was not loaded from the database
	 */
	public long getId();


	/**
	 * Get the data contained in this atom.
	 * 
	 * @return The atoms data
	 */
	public String getData();


	/**
	 * Get all tags of this atom.
	 * 
	 * @return The atoms tags
	 */
	public List<String> getTags();


	/**
	 * Modify an atom.
	 * 
	 * @return An AtomBuilder object containing the data of the atom.
	 */
	public AtomBuilder modify();
}
