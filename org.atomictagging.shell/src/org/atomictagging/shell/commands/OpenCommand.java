/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

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
public class OpenCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public OpenCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "open";
	}


	@Override
	public String getHelpMessage() {
		return "open <MID>\t- Opens a " + CoreTags.FILEREF_TAG
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
		long moleculeId = validateAndParseId( input, stdout );
		IMolecule molecule = validateAndLoad( moleculeId, stdout );

		if (molecule == null) {
			return 1;
		}

		IMoleculeViewer viewer = MoleculeHandlerFactory.getInstance().getViewer( molecule );
		viewer.showMolecule( molecule );
		return 0;
	}

}
