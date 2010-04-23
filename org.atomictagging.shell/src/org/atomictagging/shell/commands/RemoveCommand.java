package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.core.accessors.DbRemover;
import org.atomictagging.shell.IShell;

/**
 * 
 */

/**
 * Command to delete atoms or molecules from the database.
 * 
 * @author Stephan Mann
 */
public class RemoveCommand extends AbstractModifyCommand {

	/**
	 * @param shell
	 */
	public RemoveCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "rm";
	}


	@Override
	public String getHelpMessage() {
		return "rm <a/m> <ID>\t- Remove an atom or a molecule";
	}


	@Override
	protected int handleAtom( long id, PrintStream stdout ) {
		DbRemover.removeAtom( id );
		return 0;
	}


	@Override
	protected int handleMolecule( long id, PrintStream stdout ) {
		DbRemover.removeMolecule( id );
		return 0;
	}

}
