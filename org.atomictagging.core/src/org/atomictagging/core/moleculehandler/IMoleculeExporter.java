/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import java.io.File;

import org.atomictagging.core.types.IMolecule;

/**
 * Interface for classes that can export a molecule to the file system.<br>
 * <br>
 * Note: Any implementation that is to be used in an environment must be registered with the
 * {@link MoleculeHandlerFactory}.
 * 
 * @author Stephan Mann
 */
public interface IMoleculeExporter extends IMoleculeHandler {

	/**
	 * Called by the {@link MoleculeHandlerFactory} to ask whether the given molecule can be handled by this exporter.
	 * 
	 * @param molecule
	 * @return true if the molecule can be handled, false otherwise
	 */
	public boolean canHandle( IMolecule molecule );


	/**
	 * Export the given molecule to the given file.
	 * 
	 * @param molecule
	 * @param file
	 */
	public void exportToFile( IMolecule molecule, File file );
}
