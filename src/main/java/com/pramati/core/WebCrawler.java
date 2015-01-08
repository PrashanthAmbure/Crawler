/**
 * 
 */
package com.pramati.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl;
import com.pramati.core.services.WebCrawlerService;
import com.pramati.core.util.PropsUtilities;

/**
 * @author PAmbure
 * A simple crawler to crawl and download all mails for a given year from http://mail-archives.apache.org/mod_mbox/maven-users/.
 */
public class WebCrawler 
{
	private static final Logger log = Logger.getLogger(WebCrawler.class);
	public static void main(String[] args) throws IOException
	{
		log.info("Crawling has started...");
		try
		{
			WebCrawlerService webCrawlerService = new ApacheMavenWebCrawlerServiceImpl();
			PropsUtilities props = new PropsUtilities();
			Document docHTML = webCrawlerService.loadHTMLDocument(props.fetchPropValue("URL"));
			Element tableElement;
			try 
			{
				tableElement = webCrawlerService.getTableFromHTMLDocument(docHTML, "grid", "2014");
				webCrawlerService.processHTMLTableAndDownloadEmails(tableElement);
				log.info("Crawling finished successfully...");
			} 
			catch (ValidationException e) 
			{
				log.error("Crawling finished with errors..."+e);
			}
		}
		catch(UnknownHostException e)
		{
			log.info("No Internet Connection !!");
			log.error("Crawling finished with errors..."+e);
		}
		catch(SocketTimeoutException e)
		{
			log.info("No Internet Connection !!");
			log.error("Crawling finished with errors..."+e);
		}
		catch(Exception e)
		{
			log.error("Crawling finished with errors..."+e);
		}
	}
}
