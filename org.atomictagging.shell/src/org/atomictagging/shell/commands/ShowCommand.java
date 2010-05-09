/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.moleculehandler.IMoleculeViewer;
import org.atomictagging.core.moleculehandler.MoleculeHandlerFactory;
import org.atomictagging.core.types.CoreTags;
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
		return "show <MID>\t- Shows a " + CoreTags.FILEREF_TAG
				+ " atom from the molecule in the desktops default application";
	}


	@Override
	public String getVerboseHelpMessage() {
		return getHelpMessage() + "\n" + "\t\tThis command looks for a atom with the tag \"" + CoreTags.FILEREF_TAG
				+ "\" in the specified molecule\n"
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

		IMoleculeViewer viewer = MoleculeHandlerFactory.getInstance().getViewer( molecule );
		viewer.showMolecule( molecule );
		return 0;
	}

}
