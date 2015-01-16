/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author PAmbure
 * 
 */
public class ApacheMavenWebCrawlerServiceImpl implements WebCrawlerService {

	private final static String YEAR_PATTERN = "(http://)?[a-zA-Z-._/]+2014[0-9]{2}[.a-z/]+thread";
	private final static String MAILS_URL_PATTERN = "(http://)?[a-zA-Z-._/]+2014[0-9]{2}[.a-z]+/%[a-zA-Z0-9-._@%\\s]+";

	public List<String> getUrls(String seedUrl) throws IOException {
		Elements anchorElements = getAnchorElements(seedUrl,"a");
		CollectionUtils.filter(anchorElements,
				getAnchorFilterPredicate(YEAR_PATTERN));
		return getApacheMailUrls(anchorElements);
	}

	private Elements getAnchorElements(String url, String cssSelector) throws IOException {
		Document docHTML = Jsoup.connect(url).get();
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

	private List<String> getApacheMailUrls(Elements anchorElementsList)
			throws IOException {
		final List<String> mavenMailAbsUrls = new ArrayList<String>();
		List<Element> mavenMailRelativeUrls = new ArrayList<Element>();
		for (Element anchorElement : anchorElementsList) {
			String absUrl = anchorElement.attr("abs:href");
			Elements anchorElements = getAnchorElements(absUrl,"a");
			CollectionUtils.select(anchorElements,
					getAnchorFilterPredicate(MAILS_URL_PATTERN),
					mavenMailRelativeUrls);
		}

//		Convert all the Relative URL's to Absolute URL's.
		CollectionUtils.forAllDo(mavenMailRelativeUrls, new Closure() {
			@Override
			public void execute(Object arg0) {
				Element element = (Element) arg0;
				mavenMailAbsUrls.add(element.attr("abs:href"));
			}
		});
		return mavenMailAbsUrls;
	}
}
