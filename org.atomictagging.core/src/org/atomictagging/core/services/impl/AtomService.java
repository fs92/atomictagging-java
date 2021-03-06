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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.eclipse.core.runtime.Assert;

/**
 * @author Stephan Mann
 */
public class AtomService extends AbstractService implements IAtomService {

	private final static String			ID				= "atomid";
	private final static String			DATA			= "data";
	private final static String			HASHCODE		= "hashcode";
	private final static String			TAG				= "tag";

	private final static String			SELECT_ALL		= "SELECT " + ID + ", " + DATA + ", " + HASHCODE + ", " + TAG
																+ " ";
	private final static String			FROM_JOIN_WHERE	= " FROM atoms JOIN atom_has_tags JOIN tags "
																+ "WHERE atomid = atoms_atomid AND tags_tagid = tagid ";

	private static PreparedStatement	checkAtom;
	private static PreparedStatement	readAtom;
	private static PreparedStatement	insertAtom;
	private static PreparedStatement	checkAtomTags;
	private static PreparedStatement	insertAtomTags;

	static {
		try {
			checkAtom = DB.CONN.prepareStatement( "SELECT atomid FROM atoms WHERE data = ?" );
			readAtom = DB.CONN.prepareStatement( SELECT_ALL + FROM_JOIN_WHERE
					+ " AND tags_tagid = tagid AND atomid = ?" );
			insertAtom = DB.CONN.prepareStatement( "INSERT INTO atoms (data, hashcode) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS );
			checkAtomTags = DB.CONN
					.prepareStatement( "SELECT atoms_atomid FROM atom_has_tags WHERE atoms_atomid = ? AND tags_tagid = ?" );
			insertAtomTags = DB.CONN
					.prepareStatement( "INSERT INTO atom_has_tags (atoms_atomid, tags_tagid) VALUES (?, ?)" );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}
	}


	@Override
	public IAtom create( final String tag, final String data, final String hashCode ) {
		return create( Arrays.asList( tag ), data, hashCode );
	}


	@Override
	public IAtom create( final List<String> tags, final String data, final String hashCode ) {
		final Atom atom = new Atom();
		atom.setTags( tags );
		atom.setData( data );
		atom.setHashCode( hashCode );
		return atom;
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
	public List<String> getDomain() {
		final List<String> domain = new ArrayList<String>();

		final String sql = "SELECT distinct a.data FROM atoms a JOIN atom_has_tags at "
				+ "ON a.atomid = at.atoms_atomid JOIN tags t ON at.tags_tagid = t.tagid " + "WHERE t.tag NOT "
				+ in( CoreTags.asList() ) + " ORDER BY data";

		try {
			final PreparedStatement readMolecules = DB.CONN.prepareStatement( sql );

			final ResultSet resultSet = readMolecules.executeQuery();

			while ( resultSet.next() ) {
				domain.add( resultSet.getString( 1 ) );
			}

		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return domain;
	}


	@Override
	public String[] getDomainAsArray() {
		final List<String> domain = getDomain();
		return domain.toArray( new String[domain.size()] );
	}


	@Override
	public IAtom findByData( final String data ) {
		IAtom atom = null;

		try {
			final String query = SELECT_ALL + FROM_JOIN_WHERE + " AND tags_tagid = tagid AND data = '" + data + "'";
			final Statement stmt = DB.CONN.createStatement();
			final ResultSet atomsResult = stmt.executeQuery( query );
			final List<IAtom> resultSet = readFromResultSet( atomsResult );
			if ( !resultSet.isEmpty() ) {
				atom = resultSet.get( 0 );
			}
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return atom;
	}


	@Override
	public long save( final IAtom atom ) {
		try {
			final List<Long> ids = save( Arrays.asList( atom ) );
			Assert.isTrue( ids.size() == 1 );
			return ids.get( 0 );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}
		return -1;
	}


	@Override
	public List<Long> save( final List<IAtom> atoms ) throws SQLException {
		final List<Long> atomIds = new ArrayList<Long>();

		for ( final IAtom atom : atoms ) {
			checkAtom.setString( 1, atom.getData() );
			checkAtom.execute();
			long atomId = getIdOfExistingEntity( checkAtom, "atomid" );

			if ( atomId == -1 ) {
				insertAtom.setString( 1, atom.getData() );
				insertAtom.setString( 2, atom.getHashCode() );
				insertAtom.execute();
				atomId = getAutoIncrementId( insertAtom );
			}

			for ( final String tag : atom.getTags() ) {
				final long tagId = ATService.getTagService().save( tag );

				checkAtomTags.setLong( 1, atomId );
				checkAtomTags.setLong( 2, tagId );
				checkAtomTags.execute();
				final long tagCheck = getIdOfExistingEntity( checkAtomTags, "atoms_atomid" );

				if ( tagCheck == -1 ) {
					insertAtomTags.setLong( 1, atomId );
					insertAtomTags.setLong( 2, tagId );
					insertAtomTags.execute();
				}
			}

			atomIds.add( atomId );
		}

		return atomIds;
	}


	@Override
	public void delete( final IAtom atom ) {
		throw new NotImplementedException( "Not yet implemented. Sorry." );
	}


	private List<IAtom> readFromResultSet( final ResultSet atomsResult ) throws SQLException {
		final List<IAtom> atoms = new ArrayList<IAtom>();

		try {
			// The result set contains atoms multiple times, as often as they have tags.
			// That's why the next() call is around the tag retrieval. If it was in the
			// while loop, we would loose atoms or at least tags.
			atomsResult.next();

			while ( !atomsResult.isAfterLast() ) {
				final long atomId = atomsResult.getLong( ID );
				final String data = atomsResult.getString( DATA );
				final String tag = atomsResult.getString( TAG );
				final String hashCode = atomsResult.getString( HASHCODE );

				final ArrayList<String> tags = new ArrayList<String>();
				tags.add( tag );

				final Atom atom = (Atom) create( tags, data, hashCode );
				atom.setId( atomId );

				while ( atomsResult.next() && atomsResult.getLong( ID ) == atomId ) {
					atom.addTag( atomsResult.getString( TAG ) );
				}

				atoms.add( atom );
			}
		} finally {
			atomsResult.close();
		}

		return atoms;
	}

}
