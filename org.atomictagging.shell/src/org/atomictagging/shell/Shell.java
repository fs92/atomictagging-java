/**
 * This file is part of Atomic Tagging.
 * 
 * Atomic Tagging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Atomic Tagging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Atomic Tagging. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.atomictagging.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.atomictagging.shell.commands.EditCommand;
import org.atomictagging.shell.commands.HelpCommand;
import org.atomictagging.shell.commands.ICommand;
import org.atomictagging.shell.commands.ImportCommand;
import org.atomictagging.shell.commands.ListCommand;
import org.atomictagging.shell.commands.NewCommand;
import org.atomictagging.shell.commands.OpenCommand;
import org.atomictagging.shell.commands.RemoveCommand;
import org.atomictagging.shell.commands.SetScopeCommand;
import org.atomictagging.shell.commands.ShowCommand;
import org.atomictagging.shell.commands.TestDataCommand;

/**
 * A CLI for Atomic Tagging
 * 
 * @author Stephan Mann
 */
public class Shell implements IShell {
	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		Shell shell = new Shell();
		shell.register( new HelpCommand( shell ) );
		shell.register( new ListCommand( shell ) );
		shell.register( new TestDataCommand( shell ) );
		shell.register( new ShowCommand( shell ) );
		shell.register( new OpenCommand( shell ) );
		shell.register( new ImportCommand( shell ) );
		shell.register( new SetScopeCommand( shell ) );
		shell.register( new EditCommand( shell ) );
		shell.register( new NewCommand( shell ) );
		shell.register( new RemoveCommand( shell ) );
		shell.run();
	}

	Map<String, ICommand>	commands	= new HashMap<String, ICommand>();
	Map<String, String>		environment	= new HashMap<String, String>();


	@Override
	public String getEnvironment( final String key ) {
		return environment.get( key );
	}


	@Override
	public void setEnvironment( final String key, final String value ) {
		if ( key == null || value == null ) {
			return;
		}

		environment.put( key, value );
	}


	@Override
	public void register( final ICommand command ) {
		commands.put( command.getCommandString(), command );
	}


	@Override
	public ICommand getCommand( final String commandString ) {
		if ( commands.containsKey( commandString ) ) {
			return commands.get( commandString );
		}
		return null;
	}


	@Override
	public Collection<ICommand> getCommands() {
		return Collections.unmodifiableCollection( commands.values() );
	}


	private void run() {
		printWelcomeMessage();
		boolean run = true;

		while ( run ) {
			printPrompt();
			String input = readInput();

			if ( input == null || input.isEmpty() ) {
				continue;

			} else if ( input.trim().equals( "quit" ) || input.trim().equals( "exit" ) ) {
				run = false;

			} else {
				delegate( input );
			}
		}

		printGoodByeMessage();
	}


	private void delegate( String input ) {
		String[] parts = input.trim().split( " ", 2 );
		String command = parts[0];

		String params = "";
		if ( parts.length == 2 ) {
			params = parts[1];
		}

		if ( command.isEmpty() ) {
			return;
		}

		if ( commands.containsKey( command ) ) {
			commands.get( command ).handleInput( params, System.out );
		} else {
			System.out.println( "Command \"" + command + "\" not found." );
		}
	}


	private static String readInput() {
		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
		String input = null;

		try {
			input = br.readLine();
		} catch ( IOException ioe ) {
			System.err.println( "IO error trying to read user input." );
			System.exit( 1 );
		}

		return input;
	}


	private void printPrompt() {
		String scope = getEnvironment( "scope" );
		scope = ( scope == null ) ? "" : scope;
		System.out.print( scope + "> " );
	}


	private static void printWelcomeMessage() {
		System.out.println( "Welcome to AtomicTagging Shell version 0.0.3" );
		System.out.println( "Type 'help' to get startet." );
	}


	private static void printGoodByeMessage() {
		System.out.println( "Bye." );
	}

}
