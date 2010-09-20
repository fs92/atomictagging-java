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
package org.atomictagging.moleculehandler.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collection;

import org.atomictagging.core.accessors.DbWriter;
import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.moleculehandler.GenericImporter;
import org.atomictagging.core.moleculehandler.IMoleculeImporter;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;

/**
 * @author Alexander Oros
 */
public class ImageMoleculeImporter implements IMoleculeImporter {

	@Override
	public String getUniqueId() {
		return "atomictagging-imageimporter";
	}


	@Override
	public int getOrdinal() {
		return 1;
	}


	@Override
	public boolean canHandle( File file ) {
		String fileName = file.getName();
		if ( fileName.endsWith( ".jpg" ) || fileName.endsWith( ".gif" ) ) {
			return true;
		}
		return false;
	}


	@Override
	public void importFile( Collection<IMolecule> molecules, File file ) {
		importFile( molecules, file, null );
	}


	@Override
	public void importFile( Collection<IMolecule> molecules, File file, String repository ) {
		boolean isRemote = true;
		String targetDirName = Configuration.getRepository( repository );

		if ( repository != null && targetDirName == null ) {
			System.out.println( "Unkown remote location \"" + repository + "\". Check your config." );
			return;
		}

		if ( targetDirName == null ) {
			targetDirName = Configuration.get().getString( "base.dir" );
			isRemote = false;
		}

		// targetDirName = repository
		// fileName = 79/8b/498c975f328ec67ec3f76d7d423b
		String fileNameIamge = GenericImporter.copyFile( file, targetDirName );
		if ( fileNameIamge == null ) {
			System.out.println( "Error. No file imported." );
			return;
		}

		String fileNameImageThumb = "";
		try {
			byte[] thumb = transform( file, 200, 200 );
			fileNameImageThumb = GenericImporter.saveFile( thumb, targetDirName );
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IAtom atomImage = Atom.build().withData( "/" + fileNameIamge ).withTag( CoreTags.FILETYPE_IMAGE )
				.buildWithDataAndTag();
		IAtom atomImageThumb = Atom.build().withData( "/" + fileNameImageThumb ).withTag( CoreTags.FILETYPE_IMAGE )
				.withTag( "thumb" ).buildWithDataAndTag();

		MoleculeBuilder mBuilder = Molecule.build().withAtom( atomImage ).withAtom( atomImageThumb );
		mBuilder.withTag( "generic-file" );

		IMolecule molecule = mBuilder.buildWithAtomsAndTags();
		DbWriter.write( molecule );
		molecules.add( molecule );
	}


	private static byte[] transform( File originalFile, int thumbWidth, int thumbHeight ) throws Exception {
		BufferedImage image = javax.imageio.ImageIO.read( originalFile );

		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth( null );
		int imageHeight = image.getHeight( null );
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if ( thumbRatio < imageRatio ) {
			thumbHeight = (int) ( thumbWidth / imageRatio );
		} else {
			thumbWidth = (int) ( thumbHeight * imageRatio );
		}

		if ( imageWidth < thumbWidth && imageHeight < thumbHeight ) {
			thumbWidth = imageWidth;
			thumbHeight = imageHeight;
		} else if ( imageWidth < thumbWidth ) {
			thumbWidth = imageWidth;
		} else if ( imageHeight < thumbHeight ) {
			thumbHeight = imageHeight;
		}

		BufferedImage thumbImage = new BufferedImage( thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setBackground( Color.WHITE );
		graphics2D.setPaint( Color.WHITE );
		graphics2D.fillRect( 0, 0, thumbWidth, thumbHeight );
		graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		graphics2D.drawImage( image, 0, 0, thumbWidth, thumbHeight, null );

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		javax.imageio.ImageIO.write( thumbImage, "JPG", baos );

		return baos.toByteArray();
	}
}
