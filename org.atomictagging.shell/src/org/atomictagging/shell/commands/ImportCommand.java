/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.File;
import java.io.PrintStream;

import org.atomictagging.core.accessors.DbWriter;
import org.atomictagging.core.moleculehandler.IMoleculeImporter;
import org.atomictagging.core.moleculehandler.MoleculeHandlerFactory;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.shell.IShell;

/**
 * Command to import files from the file system into Atomic Tagging
 * 
 * @author Stephan Mann
 */
public class ImportCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public ImportCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "import";
	}


	@Override
	public String getHelpMessage() {
		return "import <FILE>\t- imports a file from the file system";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		File file = new File( input );
		if (!file.exists() || !file.canRead()) {
			stdout.println( "Can't read from given file: " + file.getAbsolutePath() );
			return 1;
		}

		if (file.isDirectory()) {
			stdout.println( "Given path points to a directory. Importing directories is not yet supported." );
			return 2;
		}

		IMoleculeImporter importer = MoleculeHandlerFactory.getInstance().getImporter( file );
		IMolecule molecule = importer.importFile( file );
		DbWriter.write( molecule );

		return 0;
	}

}
