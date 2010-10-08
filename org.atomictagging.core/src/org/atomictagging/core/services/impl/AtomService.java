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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.utils.StringUtils;
import org.eclipse.core.runtime.Assert;

/**
 * @author tokei
 * 
 */
public class AtomService implements IAtomService {

	private final static String			ID				= "atomid";
	private final static String			DATA			= "data";
	private final static String			TAG				= "tag";

	private static PreparedStatement	readAtom;

	private final static String			SELECT_ALL		= "SELECT " + ID + ", " + DATA + ", " + TAG + " ";
	private final static String			FROM_JOIN_WHERE	= " FROM atoms JOIN atom_has_tags JOIN tags "
																+ "WHERE atomid = atoms_atomid AND tags_tagid = tagid ";

	static {
		try {
			readAtom = DB.CONN.prepareStatement( SELECT_ALL + FROM_JOIN_WHERE
					+ " AND tags_tagid = tagid AND atomid = ?" );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}
	}


	@Override
	public IAtom find( final long atomId ) {
		try {
			readAtom.setLong( 1, atomId );
			final ResultSet atomResult = readAtom.executeQuery();

			final List<IAtom> atoms = readFromResultSet( atomResult );
			Assert.isTrue( atoms.size() <= 1 );

			if ( atoms.size() == 1 ) {
				return atoms.get( 0 );
			}
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return null;
	}


	@Override
	public List<IAtom> find( final List<String> inclusiveTags ) {
		return find( inclusiveTags, Filter.INCLUDE );
	}


	@Override
	public List<IAtom> find( final List<String> tags, final Filter filter ) {
		List<IAtom> atoms = new ArrayList<IAtom>();

		try {
			String is = "";
			if ( Filter.EXCLUDE == filter ) {
				is = " NOT ";
			}

			// FIXME Tags need to be escaped
			final String subQuery = "SELECT " + ID + FROM_JOIN_WHERE + " AND tag " + is + in( tags );
			final String query = SELECT_ALL + FROM_JOIN_WHERE + " AND " + ID + " IN (" + subQuery + " ) ORDER BY " + ID;

			final Statement stmt = DB.CONN.createStatement();
			final ResultSet atomsResult = stmt.executeQuery( query );
			atoms = readFromResultSet( atomsResult );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return atoms;
	}


	@Override
	public List<IAtom> findUserAtoms() {
		return find( CoreTags.asList(), Filter.EXCLUDE );
	}


	@Override
	public void save( final IAtom atom ) {
		// TODO Auto-generated method stub

	}


	@Override
	public void delete( final IAtom atom ) {
		// TODO Auto-generated method stub

	}


	private List<IAtom> readFromResultSet( final ResultSet atomsResult ) throws SQLException {
		final List<IAtom> atoms = new ArrayList<IAtom>();

		try {
			atomsResult.next();

			while ( !atomsResult.isAfterLast() ) {
				final long atomId = atomsResult.getLong( ID );

				final AtomBuilder builder = Atom.build().withId( atomId );
				builder.withData( atomsResult.getString( DATA ) );
				builder.withTag( atomsResult.getString( TAG ) );

				while ( atomsResult.next() && atomsResult.getLong( ID ) == atomId ) {
					builder.withTag( atomsResult.getString( TAG ) );
				}

				atoms.add( builder.buildWithDataAndTag() );
			}
		} finally {
			atomsResult.close();
		}

		return atoms;
	}


	private static String in( final List<String> tags ) {
		return " IN ('" + StringUtils.join( tags, "', '" ) + "') ";
	}

}
