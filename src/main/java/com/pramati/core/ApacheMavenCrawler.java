/**
 * 
 */
package com.pramati.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pramati.core.config.AppSettings;
import com.pramati.core.services.ApacheMavenFSDownloader;
import com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl;
import com.pramati.core.services.Downloader;
import com.pramati.core.services.WebCrawlerService;

/**
 * @author PAmbure
 * 
 *         A simple crawler to crawl and download all mails for a given year
 *         from http://mail-archives.apache.org/mod_mbox/maven-users/.
 */
public class ApacheMavenCrawler {
	private static final Logger LOG = LoggerFactory
			.getLogger(ApacheMavenCrawler.class);

	public static void main(String[] args) {
		LOG.info("Crawling has started...");
		long startTime = System.currentTimeMillis();
		try {
			AppSettings settings = new AppSettings("crawler.properties");
			WebCrawlerService webCrawlerService = new ApacheMavenWebCrawlerServiceImpl();
			Downloader downloader = new ApacheMavenFSDownloader(
					settings.getPropValue("DOWNLOADS_DIRECTORY"));
			List<String> urlsList = webCrawlerService.getUrls(
					settings.getPropValue("URL"), "2014");
			LOG.info("Apache Maven Email URLS loaded. About to start downloading...");
			downloader.download(urlsList);
			long endTime = System.currentTimeMillis();
			LOG.info("Crawling finished successfully. Time taken: {}",
					(endTime - startTime) / 1000);
		} catch (Exception e) {
			LOG.error("Exception occurred while crawling: ", e);
		}
	}
}
