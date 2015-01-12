/**
 * 
 */
package com.pramati.core.services;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.core.util.PropsUtilities;
import com.pramati.core.util.StringUtilities;

/**
 * @author PAmbure
 *
 */
public class ApacheEmailsDownloader implements EmailsDownloader 
{
	PropsUtilities props;
	public ApacheEmailsDownloader()
	{
		props = new PropsUtilities();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Boolean downloadEmails(Map<String,List<String>> emailsListPerMonthFolder) throws IOException
	{
		Iterator mapIterator = emailsListPerMonthFolder.entrySet().iterator();
		while(mapIterator.hasNext())
		{
			Map.Entry kvPairs = (Map.Entry)mapIterator.next();
			String folderName = (String)kvPairs.getKey();
			List<String> urlsList = (List<String>)kvPairs.getValue();
			
			new File(props.fetchPropValue("DOWNLOADS_DIRECTORY")+folderName).mkdir();
			for(String emailURL:urlsList)
			{
				processEmailURLAndDownloadToFolder(emailURL, folderName);
			}
		}
		return null;
	}
	
	private void processEmailURLAndDownloadToFolder(String emailURL, String downloadFolderName) throws IOException
	{
		Document mailDocHTML = Jsoup.connect(emailURL).get();
		Element msgList = mailDocHTML.getElementById("msglist");
		Elements tBody = msgList.select("tbody");
		Elements tableRowList = tBody.select("tr");
		for(Element tr:tableRowList)
		{
			String fileName = "";
			Elements mailSubjectTD = tr.select("td.subject");
			for(Element mailSubject:mailSubjectTD)
			{
				fileName = mailSubject.text();
				fileName = StringUtilities.removeSpecialChars("[/\\*:?\"<>|]", fileName);
			}
			
			Elements mailDateTD = tr.select("td.date");
			for(Element mailDate:mailDateTD)
			{
				fileName += "-"+mailDate.text();
				fileName = StringUtilities.removeSpecialChars("[/\\*:?\"<>|]", fileName);
			}
			Elements subjectLinks = tr.select("a[href]");
			for(Element subLinks:subjectLinks)
			{
				String subHrefLink = subLinks.attr("href");
				Document message = Jsoup.connect(emailURL.substring(0, 65)+subHrefLink).get();
				Element msgView = message.getElementById("msgview");
				Elements msgViewTableBody = msgView.select("tbody");
				Elements emailMsgContent = msgViewTableBody.select("tr.contents");
				File f = new File(props.fetchPropValue("DOWNLOADS_DIRECTORY")+downloadFolderName+"//"+fileName+".txt");
				f.createNewFile();
				FileUtils.writeStringToFile(f, emailMsgContent.text());
			}
		}
	}

}
