/**
 * 
 */
package org.atomictagging.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Some helper methods for handling files
 * 
 * TODO Check whether this is still appropriate or a switch to Apache commons would be in order.
 * 
 * @author Stephan Mann
 */
public class FileUtils {

	/**
	 * Copy a file
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyFile(File source, File target) {
		if (!source.exists() || !source.canRead()) {
			throw new IllegalArgumentException("Can't read from given source file: " + source.getAbsolutePath());
		}
		if (!target.exists()) {
			try {
				if (!target.createNewFile()) {
					throw new IllegalArgumentException("Can't write to given target file: " + target.getAbsolutePath());
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Can't write to given target file: " + target.getAbsolutePath(), e);
			}
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(source);
			fos = new FileOutputStream(target);
		} catch (FileNotFoundException ignore) {
			// Was checked previously.
		}

		if (fis == null || fos == null) {
			throw new RuntimeException("Failed to create file streams.");
		}

		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (IOException e) {
			// FIXME
			throw new RuntimeException("Failed to copy file.", e);
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// Nothing we can do, or is there?
				e.printStackTrace();
			}
		}
	}

}
