package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.shell.IShell;

/**
 * 
 */

/**
 * Command to delete atoms or molecules from the database.
 * 
 * @author Stephan Mann
 */
public class RemoveCommand extends AbstractCommand {

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
		return "rm <ID>\t- Remove an atom or a molecule";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {

		return 0;
	}

}
