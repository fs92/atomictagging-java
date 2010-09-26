package org.atomictagging.ui.dto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;

public class ImageMoleculeDto {

	private IMolecule molecule;
	private File imgFile;
	private byte[] imgBytes;
	
	public ImageMoleculeDto(File imgFile, IMolecule molecule) {
		this.imgFile = imgFile;
		this.molecule = molecule;
		try {
			imgBytes = transform(imgFile, 150, 150);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IMolecule getMolecule() {
		return molecule;
	}

	public void setMolecule(IMolecule molecule) {
		this.molecule = molecule;
	}

	public File getImgFile() {
		return imgFile;
	}
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public byte[] getImgBytes() {
		return imgBytes;
	}
	public void setImgBytes(byte[] imgBytes) {
		this.imgBytes = imgBytes;
	}

	public static byte[] transform( File originalFile, int thumbWidth, int thumbHeight ) throws Exception {
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
			} else if ( imageWidth < thumbWidth )
				thumbWidth = imageWidth;
			else if ( imageHeight < thumbHeight )
				thumbHeight = imageHeight;
	
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
