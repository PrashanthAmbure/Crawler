/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * @author PAmbure
 *
 */
public interface WebCrawlerService 
{
	public Document loadHTMLDocument(String url) throws IOException;
	public void navigateHTMLDocument(Document docHTML) throws IOException;
}
