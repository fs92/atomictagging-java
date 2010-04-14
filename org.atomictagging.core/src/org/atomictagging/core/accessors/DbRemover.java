/**
 * 
 */
package org.atomictagging.core.accessors;

/**
 * A class that handles removing atoms or molecules.
 * 
 * @author Stephan Mann
 */
public class DbRemover {

	/**
	 * Remove the molecule specified by the given ID and all atoms that are not part of another molecule.
	 * 
	 * @param id
	 */
	public static void removeMolecule( long id ) {
		// Find all atoms in this molecule
		// Delete atoms that are referenced only by this molecule
		// Delete tags that are only attached to these atoms
		// Delete tags that are only attached to this molecule
		// Delete molecule
	}


	/**
	 * Remove the atom specified by the given ID and all molecules that only consist this one atom.
	 * 
	 * @param id
	 */
	public static void removeAtom( long id ) {

	}

}
