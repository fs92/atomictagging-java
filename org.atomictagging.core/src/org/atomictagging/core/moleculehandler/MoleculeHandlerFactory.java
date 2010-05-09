/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.atomictagging.core.types.IMolecule;

/**
 * @author Stephan Mann
 * 
 */
public class MoleculeHandlerFactory {

	private final Map<Integer, IMoleculeViewer>		viewers		= new TreeMap<Integer, IMoleculeViewer>();
	private final Map<Integer, IMoleculeExporter>	exporters	= new TreeMap<Integer, IMoleculeExporter>();
	private final Map<Integer, IMoleculeImporter>	importers	= new TreeMap<Integer, IMoleculeImporter>();

	private static MoleculeHandlerFactory			instance	= null;


	private MoleculeHandlerFactory() {
		// Register default molecule handler so the factories getters will always have something to return.
		importers.put( Integer.MAX_VALUE, new GenericImporter() );
		viewers.put( Integer.MAX_VALUE, new GenericViewer() );
	}


	/**
	 * Singleton
	 * 
	 * @return The instance of the handler factory
	 */
	public static MoleculeHandlerFactory getInstance() {
		if (instance == null) {
			instance = new MoleculeHandlerFactory();
		}
		return instance;
	}


	/**
	 * Get a viewer that is capable of handling the given molecule.<br>
	 * <br>
	 * The factory will not decide this on its own but ask all registered viewers in order of the ordinal they were
	 * registered under whether they can handle the given molecule.
	 * 
	 * @param molecule
	 * @return A viewer which can handle the given molecule
	 */
	public IMoleculeViewer getViewer( IMolecule molecule ) {
		IMoleculeViewer result = null;

		for ( IMoleculeViewer viewer : viewers.values() ) {
			if (viewer.canHandle( molecule )) {
				result = viewer;
			}
		}

		return result;
	}


	public IMoleculeExporter getExporter( IMolecule molecule ) {
		return null;
	}


	/**
	 * Get an importer that is capable of handling the given file.<br>
	 * <br>
	 * The factory will not decide this on its own but ask all registered importers in order of the ordinal they were
	 * registered under whether they can handle the given file. The factory will however assure that the given file
	 * exists and is readable before asking the importers.
	 * 
	 * @param file
	 * @return An importer which can handle the given file
	 */
	public IMoleculeImporter getImporter( File file ) {
		if (!file.exists() || !file.canRead()) {
			throw new IllegalArgumentException( "Given file <" + file.getAbsolutePath()
					+ "> doesn't exist or is not readable." );
		}

		IMoleculeImporter result = null;

		for ( IMoleculeImporter importer : importers.values() ) {
			if (importer.canHandle( file )) {
				result = importer;
				break;
			}
		}

		return result;
	}


	/**
	 * Register a molecule viewer with the factory. The viewers ordinal will be used.
	 * 
	 * @param viewer
	 */
	public void registerViewer( IMoleculeViewer viewer ) {
		validateOrdinal( viewer.getOrdinal() );
		viewers.put( viewer.getOrdinal(), viewer );
	}


	/**
	 * Register a molecule viewer with the factory. The provided ordinal will be used in preference of the viewers own
	 * ordinal.
	 * 
	 * @param viewer
	 * @param ordinal
	 */
	public void registerViewer( IMoleculeViewer viewer, int ordinal ) {
		validateOrdinal( ordinal );
		viewers.put( ordinal, viewer );
	}


	/**
	 * Register a molecule exporter with the factory. The viewers ordinal will be used.
	 * 
	 * @param exporter
	 */
	public void registerExporter( IMoleculeExporter exporter ) {
		validateOrdinal( exporter.getOrdinal() );
		exporters.put( exporter.getOrdinal(), exporter );
	}


	/**
	 * Register a molecule exporter with the factory. The provided ordinal will be used in preference of the exporters
	 * own ordinal.
	 * 
	 * @param exporter
	 * @param ordinal
	 */
	public void registerExporter( IMoleculeExporter exporter, int ordinal ) {
		validateOrdinal( ordinal );
		exporters.put( ordinal, exporter );
	}


	/**
	 * Register a molecule importer with the factory. The viewers ordinal will be used.
	 * 
	 * @param importer
	 */
	public void registerImporter( IMoleculeImporter importer ) {
		validateOrdinal( importer.getOrdinal() );
		importers.put( importer.getOrdinal(), importer );
	}


	/**
	 * Register a molecule importer with the factory. The provided ordinal will be used in preference of the importers
	 * own ordinal.
	 * 
	 * @param importer
	 * @param ordinal
	 */
	public void registerImporter( IMoleculeImporter importer, int ordinal ) {
		validateOrdinal( ordinal );
		importers.put( ordinal, importer );
	}


	private void validateOrdinal( int ordinal ) {
		if (ordinal == Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"INTEGER.MAX_VALUE is reserved for the default handlers and must not be used." );
		}
	}
}
