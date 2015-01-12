/**
 * 
 */
package com.pramati.core.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author PAmbure
 *
 */
public interface EmailsDownloader 
{
	public Boolean downloadEmails(Map<String,List<String>> emailsListPerMonthFolder)  throws IOException;
}
