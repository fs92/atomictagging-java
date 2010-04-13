/**
 * 
 */
package org.atomictagging.core.accessors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.utils.StringUtils;

/**
 * API class to read from the DB.
 * 
 * @author Stephan Mann
 */
public class DbReader {

	/**
	 * Read all molecules from the DB that are either tagged with the given tags or contain an atom that is tagged with
	 * those tags.
	 * 
	 * @param tags
	 * @return List of molecules as read from the DB.
	 */
	public static List<IMolecule> read( List<String> tags ) {
		List<IMolecule> result = new ArrayList<IMolecule>();

		try {
			String tagFilter = StringUtils.join( tags, "' OR tag = '" );
			String moleculeSQL;

			// FIXME SQL injection! Fix this!!
			if (!tagFilter.isEmpty()) {
				// TODO stephan@2010-04-07 My SQL got rusty. There must be a better solution than this!
				moleculeSQL =
				// Select molecules with all their tags
				"SELECT molecules_moleculeid AS moleculeid, tag "
						+ "FROM molecule_has_tags JOIN tags ON (tags_tagid = tagid) "
						+ "WHERE molecules_moleculeid IN ("
						// Molecules that contain atoms that are tagged with this/these tag(s)
						+ "SELECT ma.molecules_moleculeid AS moleculeid "
						+ "FROM tags JOIN atom_has_tags AS at ON (tagid = at.tags_tagid) "
						+ "JOIN molecule_has_atoms AS ma ON (at.atoms_atomid = ma.atoms_atomid) " + "WHERE tag = '"
						+ tagFilter
						+ "' GROUP BY molecules_moleculeid HAVING COUNT(molecules_moleculeid) = "
						+ tags.size()
						+ " UNION "
						// Molecules that are tagged with this/these tag(s)
						+ "SELECT mt.molecules_moleculeid AS moleculeid "
						+ "FROM tags JOIN molecule_has_tags AS mt ON (tagid = mt.tags_tagid) " + "WHERE tag = '"
						+ tagFilter + "' GROUP BY molecules_moleculeid HAVING COUNT(molecules_moleculeid) = "
						+ tags.size() + ") ORDER BY moleculeid;";
			} else {
				moleculeSQL = "SELECT moleculeid, tag FROM molecules JOIN molecule_has_tags JOIN tags "
						+ "WHERE moleculeid = molecules_moleculeid AND tags_tagid = tagid";
			}

			PreparedStatement readMolecules = DB.CONN.prepareStatement( moleculeSQL );

			String atomSQL = "SELECT ma.atoms_atomid AS atomid "
					+ "FROM molecule_has_atoms AS ma JOIN atom_has_tags AS at JOIN tags "
					+ "WHERE at.atoms_atomid = at.tags_tagid AND tags_tagid = tagid AND molecules_moleculeid = ? "
					+ "GROUP BY ma.atoms_atomid";
			PreparedStatement readAtoms = DB.CONN.prepareStatement( atomSQL );

			ResultSet moleculeResult = readMolecules.executeQuery();
			MoleculeBuilder builder = Molecule.build();
			long moleculeId = 0;

			// Build molecules with all their atoms
			while ( moleculeResult.next() ) {
				if (moleculeId != moleculeResult.getLong( "moleculeid" )) {

					// Whenever the molecule ID changes, but not in first iteration
					if (moleculeId != 0) {
						result.add( builder.buildWithAtomsAndTags() );
						builder = Molecule.build();
					}

					moleculeId = moleculeResult.getLong( "moleculeid" );
					builder.withId( moleculeId );

					readAtoms.setLong( 1, moleculeId );
					ResultSet atomResult = readAtoms.executeQuery();

					while ( atomResult.next() ) {
						long atomId = atomResult.getLong( "atomid" );
						builder.withAtom( readAtom( atomId ) );
					}
				}

				builder.withTag( moleculeResult.getString( "tag" ) );
			}

			// Only if there was at least one molecule
			if (moleculeId != 0) {
				result.add( builder.buildWithAtomsAndTags() );
			}

		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static PreparedStatement	readMolecule;
	private static PreparedStatement	readMoleculeTags;
	static {
		try {
			readMolecule = DB.CONN
					.prepareStatement( "SELECT molecules_moleculeid, atoms_atomid FROM molecule_has_atoms WHERE molecules_moleculeid = ?" );
			readMoleculeTags = DB.CONN
					.prepareStatement( "SELECT tagid, tag FROM tags JOIN molecule_has_tags WHERE tagid = tags_tagid AND molecules_moleculeid = ?" );
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param moleculeId
	 * @return The molecule with the given ID
	 */
	public static IMolecule read( long moleculeId ) {
		if (moleculeId <= 0) {
			throw new IllegalArgumentException( "Invalid molecule ID." );
		}

		try {
			readMolecule.setLong( 1, moleculeId );
			ResultSet moleculeResult = readMolecule.executeQuery();
			boolean first = true;
			MoleculeBuilder builder = Molecule.build();

			while ( moleculeResult.next() ) {

				if (first) {
					builder.withId( moleculeResult.getLong( "molecules_moleculeid" ) );
					first = false;
				}

				builder.withAtom( readAtom( moleculeResult.getLong( "atoms_atomid" ) ) );
			}

			readMoleculeTags.setLong( 1, moleculeId );
			ResultSet moleculeTagResult = readMoleculeTags.executeQuery();
			while ( moleculeTagResult.next() ) {
				builder.withTag( moleculeTagResult.getString( "tag" ) );
			}

			// No molecule found
			if (first) {
				return null;
			}

			return builder.buildWithAtomsAndTags();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static PreparedStatement	readAtom;
	static {
		try {
			readAtom = DB.CONN.prepareStatement( "SELECT atomid, data, tag "
					+ "FROM atoms JOIN atom_has_tags JOIN tags "
					+ "WHERE atomid = atoms_atomid AND tags_tagid = tagid AND atomid = ?" );
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Get the atom with the specified ID from the database.
	 * 
	 * @param atomId
	 * @return A consistent atom or null if no atom was found with the given ID
	 * @throws SQLException
	 */
	public static IAtom readAtom( long atomId ) throws SQLException {
		readAtom.setLong( 1, atomId );
		ResultSet atomResult = readAtom.executeQuery();

		if (atomResult.next()) {
			AtomBuilder builder = Atom.build().withId( atomResult.getLong( "atomid" ) ).withData(
					atomResult.getString( "data" ) ).withTag( atomResult.getString( "tag" ) );

			while ( atomResult.next() && atomResult.getLong( "atomid" ) == atomId ) {
				builder.withTag( atomResult.getString( "tag" ) );
			}

			return builder.buildWithDataAndTag();
		}

		return null;
	}

}