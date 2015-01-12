/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.core.ValidationException;
import com.pramati.core.Visit;
import com.pramati.core.downloader.EmailsDownloader;
import com.pramati.core.util.PropsUtilities;


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
	
	@Override
	public Map<String,List<String>> getURLSFromHTMLTable(String URL) throws IOException, ValidationException
	{
		Document docHTML = Jsoup.connect(URL).get();
		log.info("Connected to "+URL);
		Element tableElement = getTableFromHTMLDocument(docHTML, "grid", "table.year", "2014");
		return fetchDownloadURLSList(tableElement);
	}
		
	private Element getTableFromHTMLDocument(Document docHTML, String tableID, String tableClass, String searchTHeadText) throws ValidationException
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
	
	private Map<String,List<String>> fetchDownloadURLSList(Element tableElement) throws IOException
	{
		Map<String,List<String>> emailsListPerMonthFolder = new LinkedHashMap<String,List<String>>();
		Elements tableYearBody = tableElement.select("tbody");
		Elements tableYearEachMonthTRList = tableYearBody.select("tr");
		for(Element tableYearTR:tableYearEachMonthTRList)
		{
			String downloadFolderName = getFolderNameToDownloadEmails(tableYearTR);
			List<String> emailURLList = new ArrayList<String>();
			emailsListPerMonthFolder.put(downloadFolderName, emailURLList);
			Elements emailURLElements = tableYearTR.select("a[href]");
			emailURLList = emailsListPerMonthFolder.get(downloadFolderName);
			for(Element link:emailURLElements)
			{
				String hrefLink = link.attr("href");
				if(hrefLink.contains("thread"))
				{
					emailURLList.add(props.fetchPropValue("URL")+hrefLink);
				}
			}
		}
		return emailsListPerMonthFolder;
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
}
