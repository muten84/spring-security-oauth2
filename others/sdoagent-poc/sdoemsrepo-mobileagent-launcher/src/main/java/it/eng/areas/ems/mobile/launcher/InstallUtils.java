/**
 * 
 */
package it.eng.areas.ems.mobile.launcher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

/**
 * @author Bifulco Luigi
 *
 */
public class InstallUtils {

	private static final int BUFFER_SIZE = 4096;

	public static boolean extractAndCopy(String archive, String targetPath) throws IOException {
		unzip(archive, targetPath);
		return true;
	}

	public static boolean installationDone(String zipFilePath) {
		File f = new File(zipFilePath);
		if (f.exists()) {
			return f.delete();
		}
		return false;
	}

	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = destDirectory + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}

	}

	public static boolean clearAppCache(String app) {
		String home = System.getProperty("user.home");
		home = home.replace("\\", "/");
		home += "/AppData/Roaming/" + app;
		File f = new File(home);
		return FileUtils.deleteQuietly(f);
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static void backupFolder(String backupStore, String folderToBackup) throws IOException {
		File bs = new File(backupStore);
		if (!bs.exists()) {
			bs.mkdir();
		} else {
			bs.delete();
		}
		File ftb = new File(folderToBackup);
		FileUtils.copyDirectory(ftb, bs, true);
	}

	public static void revert(String backupStore, String artifactId) {

	}

	public static void main(String[] args) {
		// InstallUtils.clearAppCache();
	}

}