/**
 * 
 */
package org.atomictagging.core.accessors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;

/**
 * A class that handles modifications of atoms and molecules.
 * 
 * @author Stephan Mann
 */
public class DbModifier {

	/**
	 * Update a previously loaded atom.
	 * 
	 * @param atom
	 */
	public static void modify( IAtom atom ) {
		if (atom.getId() == 0) {
			throw new IllegalArgumentException( "Can't modify atom that has no ID." );
		}

		try {
			PreparedStatement statement = DB.CONN.prepareStatement( "UPDATE atoms SET data = ? WHERE atomid = ?" );
			statement.setString( 1, atom.getData() );
			statement.setLong( 2, atom.getId() );
			statement.execute();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			IAtom reference = DbReader.readAtom( atom.getId() );

			List<String> curTags = new ArrayList<String>( atom.getTags() );
			List<String> refTags = new ArrayList<String>( reference.getTags() );

			curTags.removeAll( reference.getTags() );
			refTags.removeAll( atom.getTags() );

			// curTags now contains tags that need to be added to the atom.
			// refTags now contains tags that need to be removed from the atom.

			// For all tags in curTags, check whether tag exists and create link to atom.
			for ( String tag : curTags ) {
				// FIXME Gaaah!
				long tagId = DbWriter.writeTag( tag );
				DbWriter.insertAtomTags.setLong( 1, atom.getId() );
				DbWriter.insertAtomTags.setLong( 2, tagId );
				DbWriter.insertAtomTags.execute();
			}

			// For all tags in refTags, remove link from atom and check whether the tag can be removed.
			PreparedStatement removeTag = DB.CONN
					.prepareStatement( "DELETE FROM atom_has_tags WHERE atoms_atomid = ? AND tags_tagid = ?" );

			for ( String tag : refTags ) {
				long tagId = DbWriter.writeTag( tag );
				removeTag.setLong( 1, atom.getId() );
				removeTag.setLong( 2, tagId );
				removeTag.execute();
			}

			// TODO check for obsolete tags that need to be removed.
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Update a previously loaded molecule.
	 * 
	 * @param molecule
	 */
	public static void modify( IMolecule molecule ) {
		if (molecule.getId() == 0) {
			throw new IllegalArgumentException( "Can't modify molecule that has no ID." );
		}

		try {
			IMolecule reference = DbReader.read( molecule.getId() );

			List<String> curTags = new ArrayList<String>( molecule.getTags() );
			List<String> refTags = new ArrayList<String>( reference.getTags() );

			curTags.removeAll( reference.getTags() );
			refTags.removeAll( molecule.getTags() );

			// curTags now contains tags that need to be added to the molecule.
			// refTags now contains tags that need to be removed from the molecule.

			// For all tags in curTags, check whether tag exists and create link to atom.
			PreparedStatement insertTags = DB.CONN
					.prepareStatement( "INSERT INTO molecule_has_tags (molecules_moleculeid, tags_tagid) VALUES (?, ?)" );
			insertTags.setLong( 1, molecule.getId() );

			for ( String tag : curTags ) {
				long tagId = DbWriter.writeTag( tag );
				insertTags.setLong( 2, tagId );
				insertTags.execute();
			}

			// For all tags in refTags, remove link from atom and check whether the tag can be removed.
			PreparedStatement removeTag = DB.CONN
					.prepareStatement( "DELETE FROM molecule_has_tags WHERE molecules_moleculeid = ? AND tags_tagid = ?" );

			for ( String tag : refTags ) {
				long tagId = DbWriter.writeTag( tag );
				removeTag.setLong( 1, molecule.getId() );
				removeTag.setLong( 2, tagId );
				removeTag.execute();
			}

			// Update the atoms
			List<IAtom> curAtoms = new ArrayList<IAtom>( molecule.getAtoms() );
			List<IAtom> refAtoms = new ArrayList<IAtom>( reference.getAtoms() );

			curAtoms.removeAll( reference.getAtoms() );
			refAtoms.removeAll( molecule.getAtoms() );

			// curAtoms now contains atoms that need to be added to the molecule.
			// refAtoms now contains atoms that need to be removed from the molecule.

			PreparedStatement addAtom = DB.CONN
					.prepareStatement( "INSERT INTO molecule_has_atoms (molecules_moleculeid, atoms_atomid) VALUES (?, ?)" );
			addAtom.setLong( 1, molecule.getId() );

			for ( IAtom atom : curAtoms ) {
				addAtom.setLong( 2, atom.getId() );
				addAtom.execute();
			}

			PreparedStatement removeAtom = DB.CONN
					.prepareStatement( "DELETE FROM molecule_has_atoms WHERE molecules_moleculeid = ? AND atoms_atomid = ?" );
			removeAtom.setLong( 1, molecule.getId() );

			for ( IAtom atom : refAtoms ) {
				removeAtom.setLong( 2, atom.getId() );
				removeAtom.execute();
			}

			// TODO check for obsolete tags that need to be removed.
			// TODO check for orphan atoms that need to be removed
			// TODO all this needs to be a transaction and of course we remove orphan atoms only at the users request
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
