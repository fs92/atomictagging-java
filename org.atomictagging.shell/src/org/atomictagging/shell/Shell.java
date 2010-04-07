/**
 * 
 */
package org.atomictagging.shell;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atomictagging.core.accessors.DbReader;
import org.atomictagging.core.accessors.DbWriter;
import org.atomictagging.core.accessors.Importer;
import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Molecule;

/**
 * A CLI for AtomicTagging
 * 
 * @author Stephan Mann
 */
public class Shell {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		printWelcomeMessage();
		boolean run = true;

		while ( run ) {
			printPrompt();
			String input = readInput();

			if (input == null || input.isEmpty()) {
				continue;

			} else if (input.equals( "quit" ) || input.equals( "exit" )) {
				run = false;

			} else {
				handleInput( input );
			}
		}

		printGoodByeMessage();
	}


	/**
	 * @param input
	 */
	private static void handleInput( String input ) {
		assert input != null;

		if (input.equals( "help" )) {
			printHelpMessage();
		} else if (input.startsWith( "ls" )) {
			showList( input );
		} else if (input.equals( "testdata" )) {
			writeTestData();
		} else if (input.startsWith( "import" )) {
			importFile( input );
		} else if (input.startsWith( "show" )) {
			showFile( input );
		}
	}


	/**
	 * @param input
	 */
	private static void showList( String input ) {
		String filter = "";
		if (input.startsWith( "ls " )) {
			filter = input.substring( 3 );
		}

		List<String> tags = new ArrayList<String>();

		if (!filter.isEmpty()) {
			for ( String tag : filter.split( "/" ) ) {
				tags.add( tag );
			}
		}

		List<IMolecule> molecules = DbReader.read( tags );

		for ( IMolecule molecule : molecules ) {
			System.out.println( molecule.getId() + "\t" + molecule.getTags() );

			for ( IAtom atom : molecule.getAtoms() ) {
				// System.out.println("\t" + atom.getData() + "\t\t" + atom.getTags());
				String data = ( atom.getData().length() > 20 ) ? atom.getData().substring( 0, 20 ) + "..." : atom
						.getData();
				System.out.printf( "\t%d\t%-25s\t%s\n", atom.getId(), data, atom.getTags() );
			}

			System.out.println();
		}
	}


	private static void writeTestData() {
		IAtom artist = Atom.build().withData( "A Perfect Circle" ).withTag( "artist" ).buildWithDataAndTag();
		IAtom title = Atom.build().withData( "The Noose" ).withTag( "title" ).buildWithDataAndTag();
		IAtom album = Atom.build().withData( "13th Step" ).withTag( "album" ).buildWithDataAndTag();
		IAtom rock = Atom.build().withData( "Rock" ).withTag( "genre" ).buildWithDataAndTag();
		IAtom metal = Atom.build().withData( "Metal" ).withTag( "genre" ).buildWithDataAndTag();

		DbWriter.write( Molecule.build().withAtoms( Arrays.asList( artist, title, album, rock, metal ) ).withTag(
				"audio" ).withTag( "favorite" ).buildWithAtomsAndTags() );

		title = Atom.build().withData( "The Outsider" ).withTag( "title" ).buildWithDataAndTag();
		DbWriter.write( Molecule.build().withAtoms( Arrays.asList( artist, title, album, rock, metal ) ).withTag(
				"audio" ).buildWithAtomsAndTags() );

		artist = Atom.build().withData( "Led Zeppelin" ).withTag( "artist" ).buildWithDataAndTag();
		title = Atom.build().withData( "The Battle of Evermore" ).withTag( "title" ).buildWithDataAndTag();
		album = Atom.build().withData( "Led Zeppelin IV" ).withTag( "album" ).buildWithDataAndTag();
		DbWriter.write( Molecule.build().withAtoms( Arrays.asList( artist, title, album, rock ) ).withTag( "audio" )
				.withTag( "favorite" ).buildWithAtomsAndTags() );

		title = Atom.build().withData( "Stairway to Heaven" ).withTag( "title" ).buildWithDataAndTag();
		DbWriter.write( Molecule.build().withAtoms( Arrays.asList( artist, title, album, rock ) ).withTag( "audio" )
				.buildWithAtomsAndTags() );

		IAtom author1 = Atom.build().withData( "Erich Gamma" ).withTag( "author" ).buildWithDataAndTag();
		IAtom author2 = Atom.build().withData( "Richard Helm" ).withTag( "author" ).buildWithDataAndTag();
		IAtom author3 = Atom.build().withData( "Ralph Johnson" ).withTag( "author" ).buildWithDataAndTag();
		IAtom author4 = Atom.build().withData( "John Vlissides" ).withTag( "author" ).buildWithDataAndTag();
		IAtom date = Atom.build().withData( "1995" ).withTag( "release-year" ).buildWithDataAndTag();
		IAtom bookTitle = Atom.build().withData( "Design Patterns: Elements of Reusable Object-Oriented Software" )
				.withTag( "title" ).buildWithDataAndTag();
		DbWriter.write( Molecule.build()
				.withAtoms( Arrays.asList( author1, author2, author3, author4, bookTitle, date ) ).withTags(
						Arrays.asList( "document", "book", "software", "favorite" ) ).buildWithAtomsAndTags() );

		author1 = Atom.build().withData( "Mark Pilgrim" ).withTag( "author" ).buildWithDataAndTag();
		date = Atom.build().withData( "2004" ).withTag( "release-year" ).buildWithDataAndTag();
		bookTitle = Atom.build().withData( "Dive Into Python - Python from novice to pro" ).withTag( "title" )
				.buildWithDataAndTag();
		DbWriter.write( Molecule.build().withAtoms( Arrays.asList( author1, date, bookTitle ) ).withTags(
				Arrays.asList( "document", "book", "python" ) ).buildWithAtomsAndTags() );

	}


	/**
	 * @param input
	 */
	private static void importFile( String input ) {
		File file = new File( input.substring( 7 ) );
		if (!file.exists() || !file.canRead()) {
			System.out.println( "Can't read from given file: " + file.getAbsolutePath() );
			return;
		}

		if (file.isDirectory()) {
			System.out.println( "Given path points to a directory. Importing directories is not yet supported." );
			return;
		}

		Importer.importFile( file );
	}


	/**
	 * @param input
	 */
	private static void showFile( String input ) {
		long moleculeId = Long.parseLong( input.substring( 5 ) );
		IMolecule molecule = DbReader.read( moleculeId );
		boolean atomFound = false;

		for ( IAtom atom : molecule.getAtoms() ) {
			if (atom.getTags().contains( "x-fileref" )) {
				try {
					Desktop dt = Desktop.getDesktop();
					dt.open( new File( Configuration.BASE_DIR + atom.getData() ) );
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				atomFound = true;
			}
		}

		if (!atomFound) {
			System.out.println( "The given molecule contains no atom with a file reference (x-fileref)." );
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


	private static void printPrompt() {
		System.out.print( "> " );
	}


	private static void printWelcomeMessage() {
		System.out.println( "Welcome to AtomicTagging Shell version 0.0.2-pre-alpha-not-quite-useable-yet" );
		System.out.println( "Type 'help' to get startet." );
	}


	private static void printGoodByeMessage() {
		System.out.println( "Bye." );
	}


	private static void printHelpMessage() {
		System.out.println( "Implemented commands so far:" );
		System.out.println( "  ls - list molecules and atoms" );
		System.out.println( "  import <File> - import a file from your file system" );
		System.out.println( "  show <MoleculeID> - display a fileref atom with the systems default application" );
		System.out.println( "  help - show this help message" );
		System.out.println( "  quit/exit - exit this shell" );
	}

}
