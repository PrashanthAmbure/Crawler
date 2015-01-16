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
public class PropsUtilities {
	private final Properties props;
	public PropsUtilities(String propertyFile) throws FileNotFoundException, IOException {
		props = new Properties();
		 InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propertyFile);
		if (inputStream != null) {
			props.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propertyFile
					+ "' not found in the classpath");
		}
	}

	public String fetchPropValue(String propName) throws IOException {
		return props.getProperty(propName);
	}
}
