/**
 * 
 */
package com.pramati.core.util;

/**
 * @author PAmbure
 *
 */
public class StringUtilities 
{
	/**
	 * For a given regular expression if found any such characters in input argument, they would be replaced with blank.
	 * @param regex
	 * @param input
	 * @return a string which has all the characters matching regex with blank
	 */
	public static String removeSpecialChars(String regex, String input)
	{
		return input.replaceAll(regex, "");
	}
}
