package org.atomictagging.img.metadataextractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class ExifUtil {

	
	public static List<KeyValue> getExif(File file) {
		List<KeyValue> result = new ArrayList<KeyValue>();
		
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(file);
			
			Directory directory = metadata.getDirectory(ExifDirectory.class);
			result.add(getKeyValue(directory, ExifDirectory.TAG_MODEL));
			result.add(getKeyValue(directory, ExifDirectory.TAG_DATETIME));
			result.add(getKeyValue(directory, ExifDirectory.TAG_EXIF_IMAGE_WIDTH));
			result.add(getKeyValue(directory, ExifDirectory.TAG_EXIF_IMAGE_HEIGHT));
			
		} catch (JpegProcessingException e) {
			e.printStackTrace();
		} catch (MetadataException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public static List<KeyValue> getExif(String file) {
		return getExif(new File(file));
	}
	
	public static KeyValue getKeyValue(Directory directory, int tag) throws MetadataException {
		if(directory.containsTag(tag)) {
			return new KeyValue(directory.getTagName(tag), directory.getDescription(tag));
		}
		return null;
	}
}
