/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PAmbure
 * 
 */
public class ApacheMavenWebCrawlerServiceImpl implements WebCrawlerService {

	private final static Logger LOG = LoggerFactory
			.getLogger(ApacheMavenWebCrawlerServiceImpl.class);

	public List<String> getUrls(String seedUrl, String searchText)
			throws IOException {
		return getUrls(Jsoup.connect(seedUrl).get(),searchText);
	}
	
	public List<String> getUrls(Document docHTML, String searchText) throws IOException{
		Elements anchorElements = getAnchorElements(docHTML, "a");
		CollectionUtils.filter(anchorElements,
				getAnchorFilterPredicate(getRegexYearPattern(searchText)));
		LOG.info("Anchor elements filtered to have {} list only ",
				searchText);
		return getApacheMailUrls(anchorElements, searchText);
	}

	/**
	 * @param year
	 * @return a regular expression pattern which will match URLS like
	 *         http://mail
	 *         -archives.apache.org/mod_mbox/maven-users/201411.mbox/thread
	 */
	public String getRegexYearPattern(String year) {
		return "(http://)?[a-zA-Z-._/]+" + year + "[0-9]{2}[.a-z/]+thread";
	}

	/**
	 * @param year
	 * @return a regular expression pattern which will match URLS like
	 *         http://mail
	 *         -archives.apache.org/mod_mbox/maven-users/201412.mbox/%
	 *         3c547C1A5F.7070709@uni-jena.de%3e
	 */
	public String getRegexMailUrlPattern(String year) {
		return "(http://)?[a-zA-Z-._/]+" + year
				+ "[0-9]{2}[.a-z]+/%[a-zA-Z0-9-._@%\\s]+";
	}

	private Elements getAnchorElements(Document docHTML, String cssSelector)
			throws IOException {
		return docHTML.select(cssSelector);
	}

	private Predicate getAnchorFilterPredicate(final String hrefPattern) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Pattern REGEX_PATTERN = Pattern.compile(hrefPattern);
				Element anchorElement = (Element) arg0;
				String absUrl = anchorElement.attr("abs:href");
				Matcher matcher = REGEX_PATTERN.matcher(absUrl);
				if (matcher.find())
					return true;
				return false;
			}
		};
	}

	private List<String> getApacheMailUrls(Elements anchorElementsList,
			String searchText) throws IOException {
		final List<String> mavenMailAbsUrls = new ArrayList<String>();
		List<Element> mavenMailRelativeUrls = new ArrayList<Element>();
		for (Element anchorElement : anchorElementsList) {
			String absUrl = anchorElement.attr("abs:href");
			Elements anchorElements = getAnchorElements(Jsoup.connect(absUrl).get(), "a");
			CollectionUtils
					.select(anchorElements,
							getAnchorFilterPredicate(getRegexMailUrlPattern(searchText)),
							mavenMailRelativeUrls);
		}

		// Convert all the Relative URL's to Absolute URL's.
		for (Element element : mavenMailRelativeUrls) {
			mavenMailAbsUrls.add(element.attr("abs:href"));
		}
		LOG.info("Anchor elements converted to absolute URLS and ready for download.");
		return mavenMailAbsUrls;
	}
}
