/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;

import org.atomictagging.shell.IShell;

/**
 * Base for all commands that need to differentiate atoms and molecules by the command arguments.
 * 
 * @author Stephan Mann
 */
public abstract class AbstractModifyCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public AbstractModifyCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		String[] parts = input.trim().split( " ", 2 );
		if (parts.length != 2) {
			stdout.println( "Please specify type (atom, molecule) and ID." );
			return 1;
		}

		String type = parts[0];
		if (!type.equals( "atom" ) && !type.equals( "molecule" )) {
			stdout.println( "Please specify valid type: atom or molecule." );
			return 1;
		}

		long id;
		try {
			id = Long.parseLong( parts[1].trim() );
		} catch ( NumberFormatException e ) {
			stdout.println( "Invalid ID: " + parts[1] );
			return 1;
		}

		if (type.equals( "atom" )) {
			return handleAtom( id, stdout );
		}
		return handleMolecule( id, stdout );
	}


	/**
	 * @param id
	 * @param stdout
	 * @return
	 */
	protected abstract int handleAtom( long id, PrintStream stdout );


	/**
	 * @param id
	 * @param stdout
	 * @return
	 */
	protected abstract int handleMolecule( long id, PrintStream stdout );

}
