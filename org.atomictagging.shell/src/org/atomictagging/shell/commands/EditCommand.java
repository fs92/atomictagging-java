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

import org.atomictagging.core.accessors.DbModifier;
import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.shell.IShell;

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

		File temp = null;
		BufferedWriter writer = null;
		try {
			temp = File.createTempFile( "atomictagging", ".tmp" );
			writer = new BufferedWriter( new FileWriter( temp ) );
			writer.write( atom.getData() );
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

		StringBuilder builder = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader( new FileReader( temp ) );
			String line = null;
			while ( ( line = reader.readLine() ) != null ) {
				builder.append( line );
			}
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AtomBuilder aBuilder = atom.modify();
		aBuilder.withData( builder.toString() );
		DbModifier.modify( aBuilder.buildWithDataAndTag() );

		temp.delete();
		return 0;
	}


	@Override
	protected int handleMolecule( long id, PrintStream stdout ) {
		stdout.println( "Not yet implemented" );
		return 1;
	}

}
