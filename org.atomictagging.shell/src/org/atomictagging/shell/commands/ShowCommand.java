/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.core.moleculehandler.IMoleculeViewer;
import org.atomictagging.core.moleculehandler.MoleculeHandlerFactory;
import org.atomictagging.core.moleculehandler.IMoleculeViewer.VERBOSITY;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.shell.IShell;

/**
 * @author tokei
 * 
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
		return "show <MID>\t- Shows a molecule with all its atoms";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		long moleculeId = validateAndParseId( input, stdout );
		IMolecule molecule = validateAndLoad( moleculeId, stdout );

		if (molecule == null) {
			return 1;
		}

		IMoleculeViewer viewer = MoleculeHandlerFactory.getInstance().getViewer( molecule );
		stdout.println( viewer.getTextRepresentation( molecule, MAX_LENGTH, VERBOSITY.VERBOSE ) );
		return 0;
	}

}
