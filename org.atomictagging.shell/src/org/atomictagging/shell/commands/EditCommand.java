/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.accessors.DbModifier;
import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.shell.IShell;
import org.atomictagging.utils.StringUtils;

/**
 * A command to edit atoms and molecules.
 * 
 * @author Stephan Mann
 */
public class EditCommand extends AbstractModifyCommand {

	/**
	 * @param shell
	 */
	public EditCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "edit";
	}


	@Override
	public String getHelpMessage() {
		return "edit <a/m> <ID>\t-Edit an atom or a molecule";
	}


	@Override
	protected int handleAtom( long id, PrintStream stdout ) {
		IAtom atom = null;
		try {
			atom = DbReader.readAtom( id );
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (atom == null) {
			stdout.println( "No atom found with the given ID " + id );
			return 1;
		}

		File temp = writeAtomTempFile( atom );

		// This can't happen, can it?
		if (temp == null) {
			stdout.println( "Failed to create temporarry file." );
			return 1;
		}

		openEditorAndWait( temp );

		AtomBuilder aBuilder = getAtomFromFile( atom.modify(), temp );
		DbModifier.modify( aBuilder.buildWithDataAndTag() );

		temp.delete();
		return 0;
	}


	/**
	 * @param aBuilder
	 * @param temp
	 * @return
	 */
	static AtomBuilder getAtomFromFile( AtomBuilder aBuilder, File temp ) {
		StringBuilder builder = new StringBuilder();
		List<String> tags = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader( new FileReader( temp ) );
			String line = null;
			boolean startData = false;

			while ( ( line = reader.readLine() ) != null ) {
				if (line.startsWith( "Tags:" )) {
					String[] newTags = line.substring( 5 ).split( "," );
					for ( String tag : newTags ) {
						tags.add( tag.trim() );
					}
					continue;
				}

				if (line.startsWith( "Data:" )) {
					startData = true;
					continue;
				}

				if (startData) {
					builder.append( line );
				}
			}
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		aBuilder.withData( builder.toString() );
		aBuilder.replaceTags( tags );
		return aBuilder;
	}


	/**
	 * @param temp
	 */
	static void openEditorAndWait( File temp ) {
		ProcessBuilder pb = new ProcessBuilder( "gedit", temp.getAbsolutePath() );
		try {
			Process p = pb.start();
			p.waitFor();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private File writeAtomTempFile( IAtom atom ) {
		return writeAtomTempFile( atom.getTags(), atom.getData() );
	}


	/**
	 * @param atom
	 * @param temp
	 * @return
	 */
	static File writeAtomTempFile( List<String> tags, String data ) {
		File temp = null;
		BufferedWriter writer = null;
		try {
			temp = File.createTempFile( "atomictagging", ".tmp" );
			writer = new BufferedWriter( new FileWriter( temp ) );
			writer.write( "Tags: " + StringUtils.join( tags, ", " ) );
			writer.write( "\nData:\n" + data );
		} catch ( IOException x ) {
			System.err.println( x );
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch ( IOException ignore ) {
				}
			}

		}
		return temp;
	}


	@Override
	protected int handleMolecule( long id, PrintStream stdout ) {
		IMolecule molecule = null;
		molecule = DbReader.read( id );

		if (molecule == null) {
			stdout.println( "No molecule found with the given ID " + id );
			return 1;
		}

		File temp = null;
		BufferedWriter writer = null;
		try {
			temp = File.createTempFile( "atomictagging", ".tmp" );
			writer = new BufferedWriter( new FileWriter( temp ) );
			writer.write( "Tags: " + StringUtils.join( molecule.getTags(), ", " ) );
			writer.write( "\nAtoms: " );

			List<String> atomIds = new ArrayList<String>();
			for ( IAtom atom : molecule.getAtoms() ) {
				atomIds.add( String.valueOf( atom.getId() ) );
			}

			writer.write( StringUtils.join( atomIds, ", " ) );
		} catch ( IOException x ) {
			System.err.println( x );
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch ( IOException ignore ) {
				}
			}

		}

		// This can't happen, can it?
		if (temp == null) {
			stdout.println( "Failed to create temporarry file." );
			return 1;
		}

		openEditorAndWait( temp );

		List<String> tags = new ArrayList<String>();
		List<String> atoms = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader( new FileReader( temp ) );
			String line = null;

			while ( ( line = reader.readLine() ) != null ) {
				if (line.startsWith( "Tags:" )) {
					String[] newTags = line.substring( 5 ).split( "," );
					for ( String tag : newTags ) {
						tags.add( tag.trim() );
					}
					continue;
				}

				if (line.startsWith( "Atoms:" )) {
					String[] newAtoms = line.substring( 6 ).split( "," );
					for ( String atom : newAtoms ) {
						atoms.add( atom.trim() );
					}
					continue;
				}
			}
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MoleculeBuilder mBuilder = molecule.modify();
		mBuilder.replaceTags( tags );
		mBuilder.deleteAtoms();

		for ( String atomId : atoms ) {
			try {
				IAtom atom = DbReader.readAtom( Long.parseLong( atomId ) );

				if (atom == null) {
					stdout.println( "Unknown atom with ID: " + atomId );
					return 1;
				}

				mBuilder.withAtom( atom );
			} catch ( NumberFormatException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch ( SQLException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				temp.delete();
			}
		}

		DbModifier.modify( mBuilder.buildWithAtomsAndTags() );

		return 0;
	}

}
