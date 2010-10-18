package org.atomictagging.img.metadataextractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;

public class TestMDE {
	
	@Test
	public void testExifUtil() {
		List<KeyValue> list = ExifUtil.getExif("c:\\1\\bestofmexico\\IMG_1991.JPG");
		
		System.out.println("TestMDE.testExifUtil()");
	}

	@Test
	public void test() throws JpegProcessingException, MetadataException {
//		File jpegFile = new File("c:\\1\\images\\PICT0060.jpg");
		File jpegFile = new File("c:\\1\\bestofmexico\\IMG_1991.JPG");
		Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
		
		// iterate through metadata directories
		Iterator directories = metadata.getDirectoryIterator();
		while (directories.hasNext()) {
		    Directory directory = (Directory)directories.next();
		    // iterate through tags and print to System.out
		    Iterator tags = directory.getTagIterator();
		    while (tags.hasNext()) {
		        Tag tag = (Tag)tags.next();
		        // use Tag.toString()
		        System.out.println(tag);
		        
//		        if(tag.getTagName().equals("Thumbnail Data")) {
//		        	System.out.println("found");
//		        	byte[] byteArray = directory.getByteArray(tag.getTagType());
//		        	
//		        	try {
//						FileOutputStream fos = new FileOutputStream("c:\\1\\test.jpg");
//						fos.write(byteArray);
//						fos.flush();
//						fos.close();
//					} catch (FileNotFoundException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        }
		    }
		}
		
	}
	
}
