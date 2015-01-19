/**
 * 
 */
package com.pramati.core.services;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author PAmbure
 * 
 */
public class ApacheMavenFSDownloaderTest {
	File testDirectory;
	File file;
	ApacheMavenFSDownloader downloader;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		downloader = new ApacheMavenFSDownloader(System.getProperty("java.io.tmpdir")+"crawler-mails-test-folder/");
		testDirectory = new File(System.getProperty("java.io.tmpdir")+"crawler-mails-test-folder/");
		if (!testDirectory.exists())
			testDirectory.mkdir();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(testDirectory);
	}

	/**
	 * Test method for
	 * {@link com.pramati.core.services.ApacheMavenFSDownloader#download(java.util.List)}
	 * .
	 */
	@Test
	public void testDownload() {
		List<String> mailURLList = new ArrayList<String>();
		mailURLList
				.add("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/%3c547C1A5F.7070709@uni-jena.de%3e");
		downloader.download(mailURLList);
		file = new File(testDirectory + "/201412/Email #1.txt");
		assertTrue(file.exists());
	}

}
