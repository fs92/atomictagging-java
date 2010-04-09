/**
 * 
 */
package org.atomictagging.shell.commands;

import org.atomictagging.shell.IShell;

/**
 * Default implementation of ICommand
 * 
 * @author Stephan Mann
 */
public abstract class AbstractCommand implements ICommand {

	/**
	 * A reference to the shell the command is registered to.
	 */
	protected IShell	shell;


	/**
	 * Default constructor
	 * 
	 * @param shell
	 */
	public AbstractCommand( IShell shell ) {
		this.shell = shell;
	}


	@Override
	public String getVerboseHelpMessage() {
		return getHelpMessage();
	}

}
