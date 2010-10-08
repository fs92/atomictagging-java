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
package org.atomictagging.core.accessors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.types.IMolecule;

/**
 * API class to write molecules to the DB.
 * 
 * @author Stephan Mann
 */
public class DbWriter {

	/**
	 * Writes a molecule with all its atoms, tags, and the atoms tags to the database.
	 * 
	 * @param molecule
	 * @return The ID of the molecule as generated by the DB
	 */
	public static long write( final IMolecule molecule ) {
		long moleculeId = 0;

		try {
			DB.CONN.setAutoCommit( false );

			// Write atoms
			final List<Long> atomIds = ATService.getAtomService().save( molecule.getAtoms() );

			// Write molecule
			final PreparedStatement insertMolecule = DB.CONN.prepareStatement( "INSERT INTO molecules VALUES ()",
					Statement.RETURN_GENERATED_KEYS );
			insertMolecule.execute();
			moleculeId = getAutoIncrementId( insertMolecule );

			// Write molecule tags
			final PreparedStatement insertTags = DB.CONN
					.prepareStatement( "INSERT INTO molecule_has_tags (molecules_moleculeid, tags_tagid) VALUES (?, ?)" );
			insertTags.setLong( 1, moleculeId );

			for ( final String tag : molecule.getTags() ) {
				final long tagId = ATService.getTagService().save( tag );
				insertTags.setLong( 2, tagId );
				insertTags.execute();
			}

			// Write links between atoms and molecules
			final PreparedStatement insertLinks = DB.CONN
					.prepareStatement( "INSERT INTO molecule_has_atoms (molecules_moleculeid, atoms_atomid) VALUES (?, ?)" );
			insertLinks.setLong( 1, moleculeId );

			for ( final long atomId : atomIds ) {
				insertLinks.setLong( 2, atomId );
				insertLinks.execute();
			}

			DB.CONN.commit();
			DB.CONN.setAutoCommit( true );
		} catch ( final SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return moleculeId;
	}


	@Deprecated
	protected static long getAutoIncrementId( final PreparedStatement statement ) throws SQLException {
		final ResultSet resultSet = statement.getGeneratedKeys();

		if ( resultSet.next() ) {
			return resultSet.getLong( "GENERATED_KEY" );
		}

		return -1;
	}

}