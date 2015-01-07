/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.core.downloader.EmailsDownloader;
import com.pramati.core.util.FileUtilities;
import com.pramati.core.util.PropsUtilities;
import com.pramati.core.util.StringUtilities;


/**
 * @author PAmbure
 *
 */
public class WebCrawlerServiceImpl implements WebCrawlerService 
{
	EmailsDownloader emailsDownloader;
	public WebCrawlerServiceImpl()
	{
		emailsDownloader = new EmailsDownloader();
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

	/**
	 * 
	 */
	@Override
	public void navigateHTMLDocument(Document docHTML) throws IOException
	{
		PropsUtilities props = new PropsUtilities();
		Element yearsGrid = docHTML.getElementById("grid");
		for(Element tableYear: yearsGrid.select("table.year"))
		{
			Elements tableYearHead = tableYear.select("thead");
			Elements th = tableYearHead.select("th");
			if(th.text().contains("2014"))
			{
				Elements tableYearBody = tableYear.select("tbody");
				Elements tableYearEachMonthTRList = tableYearBody.select("tr");
				for(Element tableYearTR:tableYearEachMonthTRList)
				{
					String folderName = "";
					Elements folderNameTD = tableYearTR.select("td.date");
					for(Element folder:folderNameTD)
					{
						folderName = folder.text();
						FileUtilities.createDirectory(props.fetchPropValue("DOWNLOADS_DIRECTORY"), folderName);
					}
					
					Elements linksList = tableYearTR.select("a[href]");
					for(Element link:linksList)
					{
						String hrefLink = link.attr("href");
						if(hrefLink.contains("thread"))
						{
							
							Document mailDoc = loadHTMLDocument(props.fetchPropValue("URL")+hrefLink);
							Element msgList = mailDoc.getElementById("msglist");
							Elements tBody = msgList.select("tbody");
							Elements tableRowList = tBody.select("tr");
							for(Element tr:tableRowList)
							{
								String fileName = "";
								Elements mailSubjectTD = tr.select("td.subject");
								for(Element mailSubject:mailSubjectTD)
								{
									fileName = mailSubject.text();
									fileName = StringUtilities.replaceBlank("[/\\*:?\"<>|]", fileName);
								}
								Elements subjectLinks = tr.select("a[href]");
								for(Element subLinks:subjectLinks)
								{
									String subHrefLink = subLinks.attr("href");
									Document message = loadHTMLDocument(props.fetchPropValue("URL")+hrefLink.substring(0, 12)+subHrefLink);
									Element msgView = message.getElementById("msgview");
									Elements msgViewTableBody = msgView.select("tbody");
									Elements emailMsgContent = msgViewTableBody.select("tr.contents");
									emailsDownloader.downloadEmailsToFolder(props.fetchPropValue("DOWNLOADS_DIRECTORY")+folderName+"//", fileName, emailMsgContent.text());
								}
							}
						}
					}
				}
			}
		}
	}
}
