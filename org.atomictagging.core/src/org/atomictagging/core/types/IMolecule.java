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

import org.atomictagging.core.types.Molecule.MoleculeBuilder;

/**
 * Representation of a molecule.
 * 
 * @author Stephan Mann
 */
public interface IMolecule {

	/**
	 * Get the ID of this molecule.
	 * 
	 * @return The ID of this molecule, will be 0 if molecule was not loaded from the database
	 */
	long getId();


	/**
	 * Get all atoms of this molecule.
	 * 
	 * @return The molecules atoms
	 */
	List<IAtom> getAtoms();


	/**
	 * Override atoms of this molecule.
	 * 
	 * @param atoms
	 */
	void setAtoms( List<IAtom> atoms );


	/**
	 * Get all tags of this molecule.
	 * 
	 * @return The molecules tags
	 */
	List<String> getTags();


	/**
	 * Override tags of this molecule.
	 * 
	 * @param tags
	 */
	void setTags( List<String> tags );


	/**
	 * Returns all tags of all atoms.
	 * 
	 * @return All the tags of all atoms
	 */
	List<String> getAtomTags();


	/**
	 * Modify a molecule.
	 * 
	 * @return A MoleculeBuilder object containing the data of the molecule.
	 */
	MoleculeBuilder modify();
}
