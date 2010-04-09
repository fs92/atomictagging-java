/**
 * 
 */
package org.atomictagging.shell.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.shell.IShell;

/**
 * The implementation of a command that opens binary files
 * 
 * @author Stephan Mann
 */
public class ShowCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public ShowCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "show";
	}


	@Override
	public String getHelpMessage() {
		return "show <MID>\t- Shows a x-fileref atom from the molecule in the desktops default application";
	}


	@Override
	public String getVerboseHelpMessage() {
		return getHelpMessage() + "\n"
				+ "\t\tThis command looks for a atom with the tag \"x-fileref\" in the specified molecule\n"
				+ "\t\tand opens the file this atom points to in the desktops default application.";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		if (input.trim().isEmpty()) {
			stdout.println( "Please specifiy a molecule ID." );
			return 1;
		}

		long moleculeId = Long.parseLong( input );

		if (moleculeId == 0) {
			stdout.println( "No valid molecule ID given." );
			return 1;
		}

		IMolecule molecule = DbReader.read( moleculeId );

		if (molecule == null) {
			stdout.println( "No molecule found with given ID " + moleculeId );
			return 1;
		}

		boolean atomFound = false;

		for ( IAtom atom : molecule.getAtoms() ) {
			if (atom.getTags().contains( "x-fileref" )) {
				try {
					Desktop dt = Desktop.getDesktop();
					dt.open( new File( Configuration.BASE_DIR + atom.getData() ) );
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				atomFound = true;
			}
		}

		if (!atomFound) {
			stdout.println( "The given molecule contains no atom with a file reference (x-fileref)." );
			return 1;
		}

		return 0;
	}

}
