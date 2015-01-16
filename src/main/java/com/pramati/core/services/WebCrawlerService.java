/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.List;

/**
 * @author PAmbure
 * 
 */
public interface WebCrawlerService {
	List<String> getUrls(String seedURL, String searchText) throws IOException;
}
