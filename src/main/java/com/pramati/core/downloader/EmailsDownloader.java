/**
 * 
 */
package com.pramati.core.downloader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	
	public Boolean verifyDownload(Element tr, String downloadFolderName, Map<String,List<Element>> visitsMap)
	{
		Elements currentTRmailDateTD = tr.select("td.date");
		String currentTRmailDate = StringUtils.EMPTY;
		for(Element mailDate:currentTRmailDateTD)
		{
			currentTRmailDate = mailDate.text();
		}
		
		List<Element> vistedTRsList = visitsMap.get(downloadFolderName);
		if(vistedTRsList != null)
		{
			for(Element visitedTR:vistedTRsList)
			{
				Elements visitedTRmailDateTD = visitedTR.select("td.date");
				String visitedTRmailDate = StringUtils.EMPTY;
				for(Element mailDate:visitedTRmailDateTD)
				{
					visitedTRmailDate = mailDate.text();
				}
				
				if(visitedTRmailDate.equals(currentTRmailDate))
					return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
