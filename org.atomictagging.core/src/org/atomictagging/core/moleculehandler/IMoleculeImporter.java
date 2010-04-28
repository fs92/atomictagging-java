/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import java.io.File;

import org.atomictagging.core.types.IMolecule;

/**
 * @author Stephan Mann
 * 
 */
public interface IMoleculeImporter extends IMoleculeHandler {

	/**
	 * Called by the {@link MoleculeHandlerFactory} to ask whether the given file or directory can be handled by this
	 * importer. The {@link MoleculeHandlerFactory} will assure that the file or directory exists and is readable before
	 * asking.
	 * 
	 * @param file
	 *            A file or directory
	 * @return true if this file can be handled, false otherwise
	 */
	public boolean canHandle( File file );


	public IMolecule importFile( File file );
}
