/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.pramati.core.ValidationException;

/**
 * @author PAmbure
 *
 */
public interface WebCrawlerService 
{
	public Document loadHTMLDocument(String url) throws IOException;
	public Element getTableFromHTMLDocument(Document docHTML, String tableID, String searchTHeadText)  throws ValidationException;
	public void processHTMLTableAndDownloadEmails(Element tableElement) throws IOException;
}
