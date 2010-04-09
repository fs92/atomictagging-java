/**
 * 
 */
package org.atomictagging.shell.commands;

import java.io.PrintStream;
import java.util.Arrays;

import org.atomictagging.core.accessors.DbWriter;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.shell.IShell;

/**
 * A command that inserts some test data to play with into the database.
 * 
 * @author Stephan Mann
 */
public class TestDataCommand extends AbstractCommand {

	/**
	 * @param shell
	 */
	public TestDataCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "testdata";
	}


	@Override
	public String getHelpMessage() {
		return "testdata\t- Write a set of test data to the database";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
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

		return 0;
	}

}
