/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bifulco Luigi
 *
 */
public class FileListUtil {

	/**
	 * List all the files and folders from a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public List<String> listFilesAndFolders(String directoryName) {
		List<String> toRet = new ArrayList<>();

		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			toRet.add(file.getName());
		}
		return toRet;
	}

	/**
	 * List all the folder under a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public List<String> listFolders(String directoryName) {

		List<String> toRet = new ArrayList<>();
		File directory = new File(directoryName);
		if (!directory.exists()) {
			return new ArrayList<>();
		}
		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isDirectory()) {
				toRet.add(file.getName());
			}
		}
		return toRet;
	}

	/**
	 * List all the files under a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public List<String> listFiles(String directoryName) {
		List<String> toRet = new ArrayList<>();

		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		if (fList == null || fList.length == 0) {
			return new ArrayList<>();
		}
		for (File file : fList) {
			if (file.isFile()) {
				toRet.add(file.getName());
			}
		}
		return toRet;
	}

	/**
	 * List all files from a directory and its subdirectories
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public void listFilesAndFilesSubDirectories(String directoryName) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				System.out.println(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

}
