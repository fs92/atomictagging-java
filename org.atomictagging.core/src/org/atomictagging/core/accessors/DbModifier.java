/**
 * 
 */
package org.atomictagging.core.accessors;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
			System.out.println( statement );
			statement.execute();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
