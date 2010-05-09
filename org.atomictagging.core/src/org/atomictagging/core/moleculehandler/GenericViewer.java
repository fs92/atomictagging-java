/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;

/**
 * Views any given molecule.<br>
 * <br>
 * This is the generic "catch all" viewer for Atomic Tagging. It therefore will always say yes if asked whether it can
 * view a molecule (see {@link #canHandle(IMolecule)} and it will put itself at the end of the
 * {@link MoleculeHandlerFactory} viewer chain (see {@link #getOrdinal()}). It is expected that there will be a number
 * of better viewers for any given file but if all else fails, this viewer will try to give an informative
 * representation of the molecule.
 * 
 * @author Stephan Mann
 */
public class GenericViewer implements IMoleculeViewer {

	@Override
	public boolean canHandle( IMolecule molecule ) {
		return true;
	}


	@Override
	public int getOrdinal() {
		return Integer.MAX_VALUE;
	}


	@Override
	public String getUniqueId() {
		return "atomictagging-genericviewer";
	}

	private static final List<String>	DISPLAY_TAGS	= new ArrayList<String>();
	static {
		DISPLAY_TAGS.add( "title" );
		DISPLAY_TAGS.add( "filename" );
		DISPLAY_TAGS.add( "name" );
	}


	@Override
	public String getTextRepresentation( IMolecule molecule ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void showMolecule( IMolecule molecule ) {
		for ( IAtom atom : molecule.getAtoms() ) {
			if (atom.getTags().contains( CoreTags.FILEREF_TAG )) {
				try {
					Desktop dt = Desktop.getDesktop();
					dt.open( new File( Configuration.BASE_DIR + atom.getData() ) );
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
