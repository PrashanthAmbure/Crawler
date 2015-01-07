/**
 * 
 */
package com.pramati.core;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.pramati.core.services.WebCrawlerService;
import com.pramati.core.services.WebCrawlerServiceImpl;
import com.pramati.core.util.PropsUtilities;

/**
 * @author PAmbure
 * A simple crawler to crawl and download all mails for a given year from http://mail-archives.apache.org/mod_mbox/maven-users/.
 */
public class WebCrawler 
{
	public static Logger log = Logger.getLogger(WebCrawler.class);
	public static void main(String[] args) throws IOException
	{
		WebCrawlerService webCrawlerService = new WebCrawlerServiceImpl();
		PropsUtilities props = new PropsUtilities();
		Document docHTML = webCrawlerService.loadHTMLDocument(props.fetchPropValue("URL"));
		webCrawlerService.navigateHTMLDocument(docHTML);
	}
}
