/**
 * 
 */
package com.pramati.core.util;

import java.io.File;

/**
 * @author Pambure
 *
 */
public class FileUtilities 
{
	/**
	 * Creates a new folder with the given folder name in the specified directory name.
	 * @param directoryName
	 * @param folderName
	 * @return true if it was successful in creation of a directory
	 */
	public static Boolean createDirectory(String directoryName, String folderName)
	{
		return new File(directoryName+folderName).mkdir();
	}
}
