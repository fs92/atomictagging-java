/**
 * 
 */
package org.atomictagging.core.accessors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.types.IAtom;

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
}
