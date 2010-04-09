/**
 * 
 */
package org.atomictagging.core.accessors;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.utils.FileUtils;
import org.atomictagging.utils.StringUtils;

/**
 * Imports a generic file into a molecule
 * 
 * Creates a molecule with an atom for the file name and an atom for the files content.
 * 
 * @author Stephan Mann
 */
public class Importer {
	/**
	 * @param file
	 *            File to import
	 */
	public static void importFile( final File file ) {
		String hash = getHashedPath( file );
		List<String> pathArray = new ArrayList<String>( 3 );
		pathArray.add( hash.substring( 0, 2 ) );
		pathArray.add( hash.substring( 2, 4 ) );

		File targetDir = new File( Configuration.BASE_DIR + StringUtils.join( pathArray, "/" ) );

		pathArray.add( hash.substring( 4 ) );

		File target = new File( Configuration.BASE_DIR + StringUtils.join( pathArray, "/" ) );
		try {
			targetDir.mkdirs();
			target.createNewFile();
		} catch ( IOException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			FileUtils.copyFile( file, target );
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println( "Created file: " + target.getAbsolutePath() );

		IAtom filename = Atom.build().withData( file.getName() ).withTag( "filename" ).buildWithDataAndTag();
		IAtom binRef = Atom.build().withData( "/" + StringUtils.join( pathArray, "/" ) ).withTag( CoreTags.FILEREF_TAG )
				.withTag( "x-filetype-unknown" ).buildWithDataAndTag();
		IMolecule molecule = Molecule.build().withAtom( filename ).withAtom( binRef ).withTag( "generic-file" )
				.buildWithAtomsAndTags();
		DbWriter.write( molecule );
	}


	// TODO We need to hash the files content of course, not the path.
	private static String getHashedPath( final File file ) {

		String hash = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance( "MD5" );
			byte[] thedigest = md.digest( file.getAbsolutePath().getBytes( "UTF-8" ) );

			BigInteger bigInt = new BigInteger( 1, thedigest );
			hash = bigInt.toString( 16 );
			// Now we need to zero pad it if you actually want the full 32 chars.
			while ( hash.length() < 32 ) {
				hash = "0" + hash;
			}

		} catch ( NoSuchAlgorithmException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( UnsupportedEncodingException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hash;
	}

}
