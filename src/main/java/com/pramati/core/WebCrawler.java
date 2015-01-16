/**
 * 
 */
package com.pramati.core;

import java.util.List;

import org.apache.log4j.Logger;

import com.pramati.core.services.ApacheMavenFSDownloader;
import com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl;
import com.pramati.core.services.Downloader;
import com.pramati.core.services.WebCrawlerService;
import com.pramati.core.util.PropsUtilities;

/**
 * @author PAmbure
 * 
 *         A simple crawler to crawl and download all mails for a given year
 *         from http://mail-archives.apache.org/mod_mbox/maven-users/.
 */
public class WebCrawler {
	private static final Logger LOG = Logger.getLogger(WebCrawler.class);

	public static void main(String[] args) {
		LOG.info("Crawling has started...");
		long startTime = System.currentTimeMillis();
		PropsUtilities props = new PropsUtilities();
		WebCrawlerService webCrawlerService = new ApacheMavenWebCrawlerServiceImpl();
		Downloader downloader = new ApacheMavenFSDownloader();
		try {
			List<String> urlsList = webCrawlerService.getUrls(props
					.fetchPropValue("URL"));
			downloader.download(urlsList);
			long endTime = System.currentTimeMillis();
			LOG.info("Crawling finished successfully. Time taken: "
					+ (startTime - endTime) / 1000);
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
