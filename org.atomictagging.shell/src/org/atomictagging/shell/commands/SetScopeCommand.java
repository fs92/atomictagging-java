/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.shell.IShell;

/**
 * Command to set the tag scope. Same as "cd" on file systems.
 * 
 * @author Stephan Mann
 */
public class SetScopeCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public SetScopeCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "cd";
	}


	@Override
	public String getHelpMessage() {
		return "cd <tag>\t- Set the scope of all following ls commands";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		if ( input.isEmpty() ) {
			return 1;
		}

		if ( input.equals( "/" ) ) {
			shell.setEnvironment( "scope", "" );
			return 0;
		}

		if ( input.equals( ".." ) ) {
			stdout.println( "Sorry, not yet implemented. Try \"cd /\"." );
			return 1;
		}

		shell.setEnvironment( "scope", input );
		return 0;
	}

}
