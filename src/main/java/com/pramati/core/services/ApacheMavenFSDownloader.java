/**
 * 
 */
package com.pramati.core.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.pramati.core.util.PropsUtilities;

/**
 * @author PAmbure
 * 
 *         This impl is responsible to download the url content to a file
 *         system.
 * 
 */
public class ApacheMavenFSDownloader implements Downloader {
	PropsUtilities props;
	private static final Logger LOG = Logger
			.getLogger(ApacheMavenFSDownloader.class);

	public ApacheMavenFSDownloader() {
		props = new PropsUtilities();
	}

	@Override
	public void download(List<String> urlsList) throws IOException {
		downloadToFileSystem(urlsList,
				props.fetchPropValue("DOWNLOADS_DIRECTORY"));
	}

	private void downloadToFileSystem(List<String> urlsList,
			String directoryPath) throws IOException {
		String folderName = StringUtils.EMPTY;
		String fileName = "Email #";
		int emailNumber = 1;
		for (String url : urlsList) {
			folderName = url.substring(53, 59);
			File directory = new File(directoryPath + folderName);
			if (!directory.exists())
				directory.mkdir();

			File file = new File(directoryPath + folderName + "//" + fileName
					+ (emailNumber++) + ".txt");
			file.createNewFile();
			saveToFile(url, file);
		}
	}

	private void saveToFile(String url, File file) throws IOException {
		URL oracle;
		try {
			oracle = new URL(url);
			URLConnection conn = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuffer buffer = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
				buffer.append('\n');
			}
			FileUtils.writeStringToFile(file, buffer.toString());
			in.close();
		} catch (MalformedURLException e) {
			LOG.error(e);
		}
	}
}
