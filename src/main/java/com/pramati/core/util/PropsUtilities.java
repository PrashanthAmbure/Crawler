/**
 * 
 */
package com.pramati.core.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author PAmbure
 *
 */
public class PropsUtilities 
{
	public String fetchPropValue(String propName) throws IOException
	{
		Properties props = new Properties();
		String propFileName = "crawler.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		 
		if (inputStream != null) 
		{
			props.load(inputStream);
		} 
		else 
		{
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
 
 
		// get the property value and print it out
		String propValue = props.getProperty(propName);
		return propValue;
	}
}
