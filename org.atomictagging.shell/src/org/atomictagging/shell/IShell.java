/**
 * 
 */
package org.atomictagging.shell;

import java.util.Collection;

import org.atomictagging.shell.commands.ICommand;

/**
 * Interface for a CLI running on an Atomic Tagging environment
 * 
 * @author Stephan Mann
 */
public interface IShell {
	/**
	 * Registers a command in the shell. Only registered commands can be executed.
	 * 
	 * @param command
	 */
	void register( ICommand command );


	/**
	 * Gets the current value of the specified environment variable.
	 * 
	 * @param key
	 * @return The value or null if no value was set for this key
	 */
	String getEnvironment( String key );


	/**
	 * Write a value to the shells environment. Existing keys will be overwritten.
	 * 
	 * @param key
	 * @param value
	 */
	void setEnvironment( String key, String value );


	/**
	 * Gets the command that was registered using the given key.
	 * 
	 * @param commandString
	 * @return The command or null if no command was registered under this key
	 */
	ICommand getCommand( String commandString );


	/**
	 * Gets all registered commands.
	 * 
	 * @return An unmodifiable list of all registered commands. Will be a new list every time it is called.
	 */
	Collection<ICommand> getCommands();
}
