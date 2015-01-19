/**
 * 
 */
package com.pramati.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author PAmbure
 * 
 */
public class ApacheMavenWebCrawlerServiceImplTest {
	ApacheMavenWebCrawlerServiceImpl service;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		service = new ApacheMavenWebCrawlerServiceImpl();
		URL url;
		try {
			url = new URL("http://www.google.com");
			url.openConnection();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		service = null;
	}

	/**
	 * Test method for
	 * {@link com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl#getUrls(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testGetUrls() throws IOException, URISyntaxException {
		URL url = getClass().getResource("/apache-maven.html");
		Document docHTML = Jsoup.parse(new File(url.toURI()), "UTF-8");
		List<String> actualOutput = service.getUrls(docHTML, "2014");
		List<String> expectedOutput = new ArrayList<String>();
		expectedOutput
				.add("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/%3c547C1A5F.7070709@uni-jena.de%3e");
		assertEquals(expectedOutput.get(0), actualOutput.get(0));
	}

	/**
	 * Test method for
	 * {@link com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl#getRegexYearPattern(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetRegexYearPattern() {
		String hrefPattern = service.getRegexYearPattern("2014");
		Pattern REGEX_PATTERN = Pattern.compile(hrefPattern);
		Matcher matcher = REGEX_PATTERN
				.matcher("http://maven-users/201412.mbox/thread");
		assertTrue(matcher.find());
	}

	/**
	 * Test method for
	 * {@link com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl#getRegexMailUrlPattern(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetRegexMailUrlPattern() {
		String hrefPattern = service.getRegexMailUrlPattern("2014");
		Pattern REGEX_PATTERN = Pattern.compile(hrefPattern);
		Matcher matcher = REGEX_PATTERN
				.matcher("http://maven-users/201412.mbox/%thread");
		assertTrue(matcher.find());
	}

}
