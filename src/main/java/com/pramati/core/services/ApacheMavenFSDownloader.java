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
 *         This impl is responsible to download the url content to a file
 *         system.
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
	public void download(List<String> urlsList) throws IOException {
		LOG.info("Download is initiated. Dowloading emails to {}",
				downloadsDirectory);
		downloadToFileSystem(urlsList, downloadsDirectory);
		LOG.info("Download completed");
	}

	private void downloadToFileSystem(List<String> urlsList,
			String directoryPath) throws IOException {
		int emailNumber = 1;
		for (String url : urlsList) {
			File directory = new File(directoryPath + url.substring(53, 59));
			if (!directory.exists())
				directory.mkdir();

			File file = new File(directory, "Email #" + (emailNumber++)
					+ ".txt");
			file.createNewFile();
			saveToFile(url, file);
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
