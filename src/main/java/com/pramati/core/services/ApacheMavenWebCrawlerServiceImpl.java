/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.core.ValidationException;
import com.pramati.core.Visit;
import com.pramati.core.downloader.EmailsDownloader;
import com.pramati.core.util.FileUtilities;
import com.pramati.core.util.PropsUtilities;
import com.pramati.core.util.StringUtilities;


/**
 * @author PAmbure
 *
 */
public class ApacheMavenWebCrawlerServiceImpl extends Visit implements WebCrawlerService 
{
	private static final Logger log = Logger.getLogger(ApacheMavenWebCrawlerServiceImpl.class);
	EmailsDownloader emailsDownloader;
	PropsUtilities props;
	public ApacheMavenWebCrawlerServiceImpl()
	{
		emailsDownloader = new EmailsDownloader();
		props = new PropsUtilities();
	}
	/**
	 * @param url
	 * @return A HTML Document.
	 */
	@Override
	public Document loadHTMLDocument(String url) throws IOException
	{
		return Jsoup.connect(url).get();
	}
	
	public Element getTableFromHTMLDocument(Document docHTML, String tableID, String searchTHeadText) throws ValidationException
	{
		Element yearsGrid = docHTML.getElementById(tableID);
		for(Element tableYear: yearsGrid.select("table.year"))
		{
			Elements tableYearHead = tableYear.select("thead");
			Elements th = tableYearHead.select("th");
			if(th.text().contains(searchTHeadText))
			{
				return tableYear;
			}
		}
		throw new ValidationException("No table found in the given HTML document.");
	}
	
	public void processHTMLTableAndDownloadEmails(Element tableElement) throws IOException
	{
		Elements tableYearBody = tableElement.select("tbody");
		Elements tableYearEachMonthTRList = tableYearBody.select("tr");
		for(Element tableYearTR:tableYearEachMonthTRList)
		{
			String downloadFolderName = getFolderNameToDownloadEmails(tableYearTR);
			
//			Create a folder if doesn't exists to download the mails.
			if( !StringUtils.isEmpty(downloadFolderName) )
			{
				FileUtilities.createDirectory(props.fetchPropValue("DOWNLOADS_DIRECTORY"), downloadFolderName);
			}			
			
			Elements emailURLElements = tableYearTR.select("a[href]");
			for(;;)
			{
				try
				{
					log.info(downloadFolderName+" emails starting to download");
					processApacheMavenEmailsURLAndDownload(emailURLElements, downloadFolderName);
					log.info(downloadFolderName+" emails download finished successfully");
					break;
				}
				catch(UnknownHostException e)
				{
					log.info("Internet Connection is lost, trying to reconnect !!");
					continue;
				}
				catch(SocketTimeoutException e)
				{
					log.info("Internet Connection is lost, trying to reconnect !!");
					continue;
				}
			}
		}
	}
	
	private String getFolderNameToDownloadEmails(Element tableRow) throws IOException
	{
		String folderName = "";
		Elements folderNameTD = tableRow.select("td.date");
		for(Element folder:folderNameTD)
		{
			folderName = folder.text();
		}
		return folderName;
	}
	
	private void processApacheMavenEmailsURLAndDownload(Elements emailURLElements, String downloadFolderName) throws IOException
	{
		for(Element link:emailURLElements)
		{
			String hrefLink = link.attr("href");
			if(hrefLink.contains("thread"))
			{
				Document mailDoc = loadHTMLDocument(props.fetchPropValue("URL")+hrefLink);
				Element msgList = mailDoc.getElementById("msglist");
				Elements tBody = msgList.select("tbody");
				Elements tableRowList = tBody.select("tr");
				List<Element> visitedTRList = new ArrayList<Element>();
				
				for(Element tr:tableRowList)
				{
					if(emailsDownloader.verifyDownload(tr, downloadFolderName, getVisitMap()))
					{
						continue;
					}
					else
					{
						visitedTRList.add(tr);
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
							Document message = loadHTMLDocument(props.fetchPropValue("URL")+hrefLink.substring(0, 12)+subHrefLink);
							Element msgView = message.getElementById("msgview");
							Elements msgViewTableBody = msgView.select("tbody");
							Elements emailMsgContent = msgViewTableBody.select("tr.contents");
							emailsDownloader.downloadEmailsToFolder(props.fetchPropValue("DOWNLOADS_DIRECTORY")+downloadFolderName+"//", fileName, emailMsgContent.text());
						}
						getVisitMap().put(downloadFolderName, visitedTRList);
					}
				}
			}
		}
	}
}
