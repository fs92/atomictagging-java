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
package org.atomictagging.ui.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.img.metadataextractor.ExifUtil;
import org.atomictagging.img.metadataextractor.KeyValue;
import org.atomictagging.utils.FileUtils;

/**
 * 
 * @author strange
 */
public class ImageMolecule implements IMolecule {

	private IMolecule	molecule;

	private File		fileImage;
	private File		fileThumb;

	private byte[]		byteImage;
	private byte[]		byteThumb;

	private boolean		dirty	= false;


	// TODO: config
	// TODO: save

	/**
	 * @param file
	 */
	public ImageMolecule( final File file ) {
		fileImage = file;
		setDirty( true );

		molecule = new Molecule();
		molecule.getTags().add( "mediatype-image" );
		final IAtom atom = new Atom();
		atom.setData( file.getAbsolutePath() );
		atom.getTags().add( CoreTags.FILETYPE_IMAGE );
		molecule.getAtoms().add( atom );

		processExif( file, molecule );

		getByteThumb();
	}


	/**
	 * @param molecule
	 */
	public ImageMolecule( final IMolecule molecule ) {
		setMolecule( molecule );

		// TODO: get files for image and thumb
	}


	@Override
	public long getId() {
		return molecule.getId();
	}


	@Override
	public List<IAtom> getAtoms() {
		return molecule.getAtoms();
	}


	@Override
	public void setAtoms( final List<IAtom> atoms ) {
		molecule.setAtoms( atoms );
	}


	@Override
	public List<String> getTags() {
		return molecule.getTags();
	}


	@Override
	public void setTags( final List<String> tags ) {
		molecule.setTags( tags );
	}


	@Override
	public List<String> getAtomTags() {
		return molecule.getAtomTags();
	}


	@Override
	public MoleculeBuilder modify() {
		return null;
	}


	/**
	 * @param molecule
	 *            the molecule to set
	 */
	public void setMolecule( final IMolecule molecule ) {
		this.molecule = molecule;
	}


	/**
	 * @return the molecule
	 */
	public IMolecule getMolecule() {
		return molecule;
	}


	public static byte[] transform( final File originalFile, int thumbWidth, int thumbHeight ) throws Exception {
		final BufferedImage image = javax.imageio.ImageIO.read( originalFile );

		final double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		final int imageWidth = image.getWidth( null );
		final int imageHeight = image.getHeight( null );
		final double imageRatio = (double) imageWidth / (double) imageHeight;
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

		final BufferedImage thumbImage = new BufferedImage( thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB );
		final Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setBackground( Color.WHITE );
		graphics2D.setPaint( Color.WHITE );
		graphics2D.fillRect( 0, 0, thumbWidth, thumbHeight );
		graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		graphics2D.drawImage( image, 0, 0, thumbWidth, thumbHeight, null );

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		javax.imageio.ImageIO.write( thumbImage, "JPG", baos );

		return baos.toByteArray();
	}


	// public static byte[] loadImage( final File file ) throws IOException {
	// byte[] bytes = null;
	//
	// final FileInputStream fileInputStream = new FileInputStream( file );
	// bytes = new byte[(int) file.length()];
	// fileInputStream.read( bytes );
	// fileInputStream.close();
	//
	// return bytes;
	// }

	/**
	 * Reads the image file and transforms it into a thumb of a configured size. <br>
	 * The thumb is cached for the subsequent calls.
	 * 
	 * @return the byteThumb
	 */
	public byte[] getByteThumb() {
		if ( byteThumb == null ) {

			File file = fileThumb;
			if ( fileThumb == null ) {
				file = fileImage;
			}

			try {
				byteThumb = transform( file, 200, 200 );
			} catch ( final Exception e ) {
				e.printStackTrace();
			}
		}
		return byteThumb;
	}


	/**
	 * Reads the image file into a byte array. <br>
	 * The image is cached for the subsequent calls.
	 * 
	 * @return the byteImage
	 */
	public byte[] getByteImage() {
		if ( byteImage == null ) {
			try {
				byteImage = FileUtils.loadImage( fileImage );
			} catch ( final IOException e ) {
				e.printStackTrace();
			}
		}
		return byteImage;
	}


	/**
	 * @return the fileImage
	 */
	public File getFileImage() {
		return fileImage;
	}


	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty( final boolean dirty ) {
		this.dirty = dirty;
	}


	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}


	public void processExif( final File file, final IMolecule molecule ) {
		List<KeyValue> list;
		try {
			list = ExifUtil.getExif( file );
		} catch ( final Exception e ) {
			e.printStackTrace();
			return;
		}

		for ( final KeyValue kv : list ) {
			final IAtom atom = new Atom();
			atom.getTags().add( kv.key );
			atom.setData( kv.value );

			molecule.getAtoms().add( atom );
		}
	}


	@Override
	public List<IAtom> findAtomsWithTag( final String searchTag ) {
		return molecule.findAtomsWithTag( searchTag );
	}


	/**
	 * @param fileThumb
	 *            the fileThumb to set
	 */
	public void setFileThumb( final File fileThumb ) {
		this.fileThumb = fileThumb;
	}
}
