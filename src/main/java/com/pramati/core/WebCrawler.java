/**
 * 
 */
package com.pramati.core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pramati.core.services.ApacheEmailsDownloader;
import com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl;
import com.pramati.core.services.EmailsDownloader;
import com.pramati.core.services.WebCrawlerService;
import com.pramati.core.util.PropsUtilities;

/**
 * @author PAmbure
 * A simple crawler to crawl and download all mails for a given year from http://mail-archives.apache.org/mod_mbox/maven-users/.
 */
public class WebCrawler 
{
	private static final Logger log = Logger.getLogger(WebCrawler.class);
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		log.info("Crawling has started...");
		long startTime = System.currentTimeMillis();
		PropsUtilities props = new PropsUtilities();
		WebCrawlerService webCrawlerService = new ApacheMavenWebCrawlerServiceImpl();
		Map<String, List<String>> urlsListPerMonth = Collections.EMPTY_MAP;
		try 
		{
			urlsListPerMonth = webCrawlerService.getURLSFromHTMLTable(props.fetchPropValue("URL"));
		} 
		catch (ValidationException e) 
		{
			log.error(e);
		}
		if(urlsListPerMonth.size() > 0)
		{
			EmailsDownloader emailsDownloader = new ApacheEmailsDownloader();
			emailsDownloader.downloadEmails(urlsListPerMonth);
		}
		long endTime = System.currentTimeMillis();
		log.info("Crawling finished successfully. Time taken: "+(startTime-endTime)/1000);
	}
}
