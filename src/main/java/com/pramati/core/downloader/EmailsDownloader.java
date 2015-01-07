/**
 * 
 */
package com.pramati.core.downloader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author PAmbure
 *
 */
public class EmailsDownloader 
{
	public void downloadEmailsToFolder(String path, String fileName, String emailMsgText) throws IOException
	{
		File f = new File(path+"//"+fileName+".txt");
		f.createNewFile();
		FileUtils.writeStringToFile(f, emailMsgText);
	}
}
