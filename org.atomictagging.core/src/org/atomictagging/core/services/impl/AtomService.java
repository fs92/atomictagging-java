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
package org.atomictagging.core.services.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.IAtom;

/**
 * @author tokei
 * 
 */
public class AtomService implements IAtomService {

	private static PreparedStatement	readAtom;
	static {
		try {
			readAtom = DB.CONN.prepareStatement( "SELECT atomid, data, tag "
					+ "FROM atoms JOIN atom_has_tags JOIN tags "
					+ "WHERE atomid = atoms_atomid AND tags_tagid = tagid AND atomid = ?" );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}
	}


	@Override
	public IAtom find( final long atomId ) {
		try {
			readAtom.setLong( 1, atomId );
			final ResultSet atomResult = readAtom.executeQuery();

			if ( atomResult.next() ) {
				final AtomBuilder builder = Atom.build().withId( atomResult.getLong( "atomid" ) )
						.withData( atomResult.getString( "data" ) ).withTag( atomResult.getString( "tag" ) );

				while ( atomResult.next() && atomResult.getLong( "atomid" ) == atomId ) {
					builder.withTag( atomResult.getString( "tag" ) );
				}

				return builder.buildWithDataAndTag();
			}
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return null;
	}


	@Override
	public List<IAtom> find( final List<String> tags ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<IAtom> find( final List<String> tags, final Filter filter ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<IAtom> findUserAtoms( final List<String> tags ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void save( final IAtom atom ) {
		// TODO Auto-generated method stub

	}


	@Override
	public void delete( final IAtom atom ) {
		// TODO Auto-generated method stub

	}

}
