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
		registerImporter( new GenericImporter() );
	}


	public static MoleculeHandlerFactory getInstance() {
		if (instance == null) {
			instance = new MoleculeHandlerFactory();
		}
		return instance;
	}


	public void registerViewer( IMoleculeViewer viewer ) {
		viewers.put( viewer.getOrdinal(), viewer );
	}


	public void registerViewer( IMoleculeViewer viewer, int ordinal ) {
		viewers.put( ordinal, viewer );
	}


	public void registerExporter( IMoleculeExporter exporter ) {
		exporters.put( exporter.getOrdinal(), exporter );
	}


	public void registerExporter( IMoleculeExporter exporter, int ordinal ) {
		exporters.put( ordinal, exporter );
	}


	public void registerImporter( IMoleculeImporter importer ) {
		importers.put( importer.getOrdinal(), importer );
	}


	public void registerImporter( IMoleculeImporter importer, int ordinal ) {
		importers.put( ordinal, importer );
	}


	public IMoleculeViewer getViewer( IMolecule molecule ) {
		for ( IMoleculeViewer viewer : viewers.values() ) {
			if (viewer.canHandle( molecule )) {
				return viewer;
			}
		}

		return null;
	}


	public IMoleculeExporter getExporter( IMolecule molecule ) {
		return null;
	}


	/**
	 * Get an importer that is capable of handling the given file or directory.
	 * 
	 * The factory will not decide this on its own but ask all registered importers in order of the ordinal they were
	 * registered under whether they can handle the given file. The factory will however assure that the given file or
	 * directory exists and is readable before asking the importers.
	 * 
	 * @param file
	 * @return An importer which can handle the given file
	 */
	public IMoleculeImporter getImporter( File file ) {
		for ( IMoleculeImporter importer : importers.values() ) {
			if (importer.canHandle( file )) {
				return importer;
			}
		}

		// TODO This seems redundant, because we already registered this importer with the highest possible ordinal.
		return new GenericImporter();
	}


	// TODO We should check whether the given ordinal is still available.
	private boolean checkOrdinal( Map<Integer, IMoleculeHandler> map, int ordinal ) {
		return false;
	}
}
