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

/**
 * API class to read from the DB.
 * 
 * @author Stephan Mann
 */
public class DbReader {

	/**
	 * Read all molecules from the DB that apply to the given filters.
	 * 
	 * @param moleculeTags
	 * @param atomTags
	 * @param atomContent
	 * @return List of molecules as read from the DB.
	 */
	public static List<IMolecule> read(List<String> moleculeTags, List<String> atomTags, List<String> atomContent) {
		List<IMolecule> result = new ArrayList<IMolecule>();

		try {
			// String atomTagFilter = "";
			// if (atomTags.size() > 0) {
			// List<String> filter = new ArrayList<String>();
			//
			// for (String tag : atomTags) {
			// filter.add("tag = '" + tag + "'");
			// }
			//
			// atomTagFilter = " AND (" + join(filter, " OR ") + ")";
			// }
			//
			// String foo =
			// "SELECT atomid FROM atoms JOIN atom_has_tags JOIN tags WHERE atomid = atoms_atomid AND tags_tagid = tagid"
			// + atomTagFilter;

			String moleculeTagFilter = "";
			if (moleculeTags.size() > 0) {
				for (String tag : moleculeTags) {
					moleculeTagFilter += " AND tag = '" + tag + "'";
				}
			}

			String mSQL = "SELECT moleculeid, tag " + "FROM molecules JOIN molecule_has_tags JOIN tags "
					+ "WHERE moleculeid = molecules_moleculeid AND tags_tagid = tagid" + moleculeTagFilter;

			// System.err.println("molecules: " + mSQL);

			// FIXME SQL injection! Fix this!!
			PreparedStatement readMolecules = DB.CONN.prepareStatement(mSQL);

			String atomContentFilter = "";
			if (atomContent.size() > 0) {
				for (String content : atomContent) {
					atomContentFilter += " AND data = '" + content + "'";
				}
			}

			// FIXME SQL injection! Fix this!!
			String sql = "SELECT ma.atoms_atomid AS atomid "
					+ "FROM molecule_has_atoms AS ma JOIN atom_has_tags AS at JOIN tags "
					+ "WHERE at.atoms_atomid = at.tags_tagid AND tags_tagid = tagid AND molecules_moleculeid = ? "
					+ /* atomTagFilter + atomContentFilter + */" GROUP BY ma.atoms_atomid";

			// System.err.println("atoms: " + sql);

			PreparedStatement readAtoms = DB.CONN.prepareStatement(sql);

			ResultSet moleculeResult = readMolecules.executeQuery();
			MoleculeBuilder builder = Molecule.build();
			long moleculeId = 0;

			while (moleculeResult.next()) {
				if (moleculeId != moleculeResult.getLong("moleculeid")) {

					// Whenever the molecule ID changes, but not in first iteration
					if (moleculeId != 0) {
						try {
							result.add(builder.buildWithAtomsAndTags());
						} catch (Exception e) {
						}
						builder = Molecule.build();
					}

					moleculeId = moleculeResult.getLong("moleculeid");
					builder.withId(moleculeId);

					readAtoms.setLong(1, moleculeId);
					ResultSet atomResult = readAtoms.executeQuery();

					while (atomResult.next()) {
						long atomId = atomResult.getLong("atomid");
						builder.withAtom(readAtom(atomId));
					}
				}

				builder.withTag(moleculeResult.getString("tag"));
			}

			// Only if there was at least one molecule
			if (moleculeId != 0) {
				// result.add(builder.buildWithAtomsAndTags());
				try {
					result.add(builder.buildWithAtomsAndTags());
				} catch (Exception e) {
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static PreparedStatement	readMolecule;
	static {
		try {
			readMolecule = DB.CONN
					.prepareStatement("SELECT molecules_moleculeid, atoms_atomid FROM molecule_has_atoms WHERE molecules_moleculeid = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param moleculeId
	 * @return The molecule with the given ID
	 */
	public static IMolecule read(long moleculeId) {
		if (moleculeId <= 0) {
			throw new IllegalArgumentException("Invalid molecule ID.");
		}

		try {
			readMolecule.setLong(1, moleculeId);
			ResultSet moleculeResult = readMolecule.executeQuery();
			boolean first = true;
			MoleculeBuilder builder = Molecule.build();

			while (moleculeResult.next()) {

				if (first) {
					builder.withId(moleculeResult.getLong("molecules_moleculeid"));
					// TODO Read the tags from the database.
					builder.withTag("x-notag");
					first = false;
				}

				builder.withAtom(readAtom(moleculeResult.getLong("atoms_atomid")));
			}

			return builder.buildWithAtomsAndTags();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static PreparedStatement	readAtom;
	static {
		try {
			readAtom = DB.CONN.prepareStatement("SELECT atomid, data, tag "
					+ "FROM atoms JOIN atom_has_tags JOIN tags "
					+ "WHERE atomid = atoms_atomid AND tags_tagid = tagid AND atomid = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static IAtom readAtom(long atomId) throws SQLException {
		readAtom.setLong(1, atomId);
		ResultSet atomResult = readAtom.executeQuery();

		if (atomResult.next()) {
			AtomBuilder builder = Atom.build().withId(atomResult.getLong("atomid")).withData(
					atomResult.getString("data")).withTag(atomResult.getString("tag"));

			while (atomResult.next() && atomResult.getLong("atomid") == atomId) {
				builder.withTag(atomResult.getString("tag"));
			}

			return builder.buildWithDataAndTag();
		}

		return null;
	}

}