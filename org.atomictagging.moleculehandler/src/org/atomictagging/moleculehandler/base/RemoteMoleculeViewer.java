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
package org.atomictagging.moleculehandler.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.moleculehandler.GenericViewer;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;

/**
 * @author Stephan Mann
 */
public class RemoteMoleculeViewer extends GenericViewer {

	@Override
	public boolean canHandle( IMolecule molecule ) {
		if ( molecule.getAtomTags().contains( CoreTags.FILEREF_REMOTE_TAG ) ) {
			return true;
		}
		return false;
	}


	@Override
	public void showMolecule( IMolecule molecule ) {
		String confKey = null;

		for ( IAtom atom : molecule.getAtoms() ) {
			if ( atom.getTags().contains( CoreTags.FILEREF_REMOTE_LOCATION ) ) {
				confKey = atom.getData();
				break;
			}
		}

		if ( confKey == null ) {
			System.out.println( "Remote molecule has no location atom. Invalid molecule state." );
			return;
		}

		String remoteLoc = Configuration.getRepository( confKey );

		if ( remoteLoc == null ) {
			System.out.println( "Failed to retrieve remote location for identifier '" + confKey
					+ "' from configuration." );
			return;
		}

		for ( IAtom atom : molecule.getAtoms() ) {
			if ( atom.getTags().contains( CoreTags.FILEREF_TAG )
					&& atom.getTags().contains( CoreTags.FILEREF_REMOTE_TAG ) ) {

				String fileName = remoteLoc + "/" + atom.getData();
				File file = new File( fileName );
				if ( file.canRead() ) {
					// IMoleculeViewer viewer = MoleculeHandlerFactory.getInstance().getNextViewer( molecule, this );
					// viewer.showMolecule( molecule );
					try {
						Desktop dt = Desktop.getDesktop();
						dt.open( file );
					} catch ( IOException e ) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println( "File is not readable at the moment. Try providing the remote source "
							+ remoteLoc + "." );
				}
			}
		}
	}


	@Override
	public int getOrdinal() {
		return Integer.MAX_VALUE - 100;
	}


	@Override
	public String getUniqueId() {
		return "atomictagging-remoteviewer";
	}

}
