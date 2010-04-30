/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import java.io.File;
import java.util.Collection;

import org.atomictagging.core.types.IMolecule;

/**
 * Interface for classes that can import a file or directory into the Atomic Tagging environment.<br>
 * <br>
 * Note: Any implementation that is to be used in an environment must be registered with the
 * {@link MoleculeHandlerFactory}.
 * 
 * @author Stephan Mann
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


	/**
	 * Read the given file or directory from the file system and create molecules representing the file(s). After return
	 * of this method, the file(s) has been copied into the Atomic Tagging environment and the molecule has been written
	 * to the database.
	 * 
	 * @param molecules
	 *            The given collection will be filed with whatever molecules where created
	 * @param file
	 *            Source to read from
	 */
	public void importFile( Collection<IMolecule> molecules, File file );
}
