/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.shell.IShell;

/**
 * The implementation of a list command that show molecules according to the given tags
 * 
 * @author Stephan Mann
 */
public class ListCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public ListCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "ls";
	}


	@Override
	public String getHelpMessage() {
		return "ls [tag1/tag2]\t- List all molecules (with the given tags)";
	}


	@Override
	public String getVerboseHelpMessage() {
		return getHelpMessage();
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		List<String> tags = new ArrayList<String>();

		String scope = shell.getEnvironment( "scope" );
		scope = ( scope == null ) ? "" : scope;

		if ( input != null && !input.isEmpty() ) {
			scope += "/" + input;
		}

		if ( !scope.isEmpty() ) {
			for ( String tag : scope.split( "/" ) ) {
				if ( !tag.isEmpty() ) {
					tags.add( tag );
				}
			}
		}

		List<IMolecule> molecules = DbReader.read( tags );

		for ( IMolecule molecule : molecules ) {
			stdout.println( molecule.getId() + "\t" + molecule.getTags() );

			for ( IAtom atom : molecule.getAtoms() ) {
				String data = ( atom.getData().length() > 20 ) ? atom.getData().substring( 0, 20 ) + "..." : atom
						.getData();
				stdout.printf( "\t%d\t%-25s\t%s\n", atom.getId(), data, atom.getTags() );
			}

			stdout.println();
		}
		return 0;
	}

}
