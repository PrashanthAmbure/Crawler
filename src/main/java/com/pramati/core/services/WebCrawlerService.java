/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pramati.core.ValidationException;

/**
 * @author PAmbure
 *
 */
public interface WebCrawlerService 
{
	public Map<String,List<String>> getURLSFromHTMLTable(String URL) throws IOException, ValidationException;
}
