/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.shell.IShell;

/**
 * The implementation of the help command printing help messages for any command that implements ICommand
 * 
 * @see ICommand
 * @author Stephan Mann
 */
public class HelpCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public HelpCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "help";
	}


	@Override
	public String getHelpMessage() {
		return "help [command]\t- Print a help text to all available commands or a verbose text for some specific command";
	}


	@Override
	public String getVerboseHelpMessage() {
		return getHelpMessage() + "\n"
				+ "\t\tSpecify a command if you are interessted in a more verbose help on this command.";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		if (input.trim().isEmpty()) {
			for ( ICommand command : shell.getCommands() ) {
				stdout.println( command.getHelpMessage() );
			}
			return 0;
		}

		ICommand command = shell.getCommand( input.trim() );
		if (command == null) {
			stdout.println( "Command not found." );
			return 1;
		}

		stdout.println( command.getVerboseHelpMessage() );
		return 0;
	}

}
