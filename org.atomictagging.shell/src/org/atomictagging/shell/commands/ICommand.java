/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.shell.Shell;

/**
 * The interface for commands that can be executed in a Shell
 * 
 * @see Shell
 * @author Stephan Mann
 */
public interface ICommand {
	/**
	 * Gets the string that will call the command.
	 * 
	 * @return The string identifying the command
	 */
	String getCommandString();


	/**
	 * Executes the command.
	 * 
	 * @param input
	 * @param stdout
	 * @return 0 in case of success, an error code != 0 in case of failure
	 */
	int handleInput( String input, PrintStream stdout );


	/**
	 * Gets the help message for this command.
	 * 
	 * @return Help message
	 */
	String getHelpMessage();


	/**
	 * Gets a more verbose help message for this command.
	 * 
	 * @return Verbose help message
	 */
	String getVerboseHelpMessage();
}
