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
		long startTime = System.currentTimeMillis();
		PropsUtilities props = new PropsUtilities();
		int count = 0;
		for(;;)
		{
			try
			{
				WebCrawlerService webCrawlerService = new ApacheMavenWebCrawlerServiceImpl();
				Document docHTML = webCrawlerService.loadHTMLDocument(props.fetchPropValue("URL"));
				if(count !=0)
					log.info("Connected to internet now ...");
				Element tableElement;
				try 
				{
					tableElement = webCrawlerService.getTableFromHTMLDocument(docHTML, "grid", "2014");
					webCrawlerService.processHTMLTableAndDownloadEmails(tableElement);
					long endTime = System.currentTimeMillis();
					log.info("Crawling finished successfully. Time taken: "+(startTime-endTime)/1000);
					break;
				} 
				catch (ValidationException e) 
				{
					log.error("Crawling finished with errors..."+e);
				}
			}
			catch(UnknownHostException e)
			{
				log.info("Internet Connection was lost while connecting to Apache Maven URL: "+props.fetchPropValue("URL")+", trying to reconnect !!");
				count++;
				continue;
			}
			catch(SocketTimeoutException e)
			{
				log.info("Internet Connection was lost while connecting to Apache Maven URL: "+props.fetchPropValue("URL")+", trying to reconnect !!");
				count++;
				continue;
			}
		}
	}
}
