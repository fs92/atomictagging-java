package org.atomictagging.core.types;

import java.util.List;

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
	 * Get all tags of this molecule.
	 * 
	 * @return The molecules tags
	 */
	List<String> getTags();

}
