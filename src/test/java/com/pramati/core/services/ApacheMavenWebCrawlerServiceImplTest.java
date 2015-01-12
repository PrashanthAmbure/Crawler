/**
 * 
 */
package com.pramati.core.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pramati.core.ValidationException;

/**
 * @author PAmbure
 *
 */
public class ApacheMavenWebCrawlerServiceImplTest 
{
	String url;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		url = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	}


	/**
	 * Test method for {@link com.pramati.core.services.ApacheMavenWebCrawlerServiceImpl#getURLSFromHTMLTable(java.lang.String)}.
	 */
	@Test
	public void testGetURLSFromHTMLTable() 
	{
		ApacheMavenWebCrawlerServiceImpl service = new ApacheMavenWebCrawlerServiceImpl();
		try 
		{
			Map<String,List<String>> urlsListPerMonth = service.getURLSFromHTMLTable(url);
			assertNotNull(urlsListPerMonth);
			if(urlsListPerMonth.size() <= 0)
				throw new ValidationException("No data found.");
		} 
		catch (IOException | ValidationException e) 
		{
			fail("Exception occurred."+e.getLocalizedMessage());
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		url="";
	}
}
