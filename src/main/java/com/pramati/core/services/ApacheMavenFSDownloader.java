/**
 * 
 */
package com.pramati.core.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author PAmbure
 * 
 *         This impl is responsible to download the url content to a given file
 *         directory location.
 * 
 */
public class ApacheMavenFSDownloader implements Downloader {
	private String downloadsDirectory;
	private static final Logger LOG = LoggerFactory
			.getLogger(ApacheMavenFSDownloader.class);

	public ApacheMavenFSDownloader(String downloadsDirectory) {
		this.downloadsDirectory = downloadsDirectory;
	}

	@Override
	public Boolean download(List<String> urlsList) {
		LOG.info("Download is initiated. Dowloading emails to {}",
				downloadsDirectory);
		try {
			int emailNumber = 1;
			for (String url : urlsList) {
				File directory = new File(downloadsDirectory
						+ url.substring(53, 59));
				if (!directory.exists())
					directory.mkdir();

				File file = new File(directory, "Email #" + (emailNumber++)
						+ ".txt");
				saveToFile(url, file);
				LOG.info("Download completed");
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			LOG.error("Exception occurred while processing download action: ",
					e);
			return Boolean.FALSE;
		}
	}

	private void saveToFile(String url, File file) throws IOException {
		URL emailUrl;
		URLConnection conn;
		OutputStream outputStream = new FileOutputStream(file);
		try {
			emailUrl = new URL(url);
			conn = emailUrl.openConnection();
			IOUtils.copy(conn.getInputStream(), outputStream);
		} catch (MalformedURLException e) {
			LOG.error("MalformedURLException occurred: {}", e);
		} finally {
			outputStream.close();
		}
	}
}
