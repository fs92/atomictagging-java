/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import org.atomictagging.core.types.IMolecule;

/**
 * Interface for all classes that are able to view a molecule.<br>
 * <br>
 * Note: Any implementation that is to be used in an environment must be registered with the
 * {@link MoleculeHandlerFactory}.
 * 
 * @author Stephan Mann
 */
public interface IMoleculeViewer extends IMoleculeHandler {

	/**
	 * Called by the {@link MoleculeHandlerFactory} to ask whether the given molecule can be handled by this viewer.
	 * 
	 * @param molecule
	 * @return true if the molecule can be handled, false otherwise
	 */
	public boolean canHandle( IMolecule molecule );


	/**
	 * Get a text representation of the given molecule.
	 * 
	 * @param molecule
	 * @return A text representation
	 */
	public String getTextRepresentation( IMolecule molecule );


	/**
	 * Show the molecule in whatever way the handler deems appropriate.
	 * 
	 * @param molecule
	 */
	public void showMolecule( IMolecule molecule );

}
