package com.pramati.core.util;

import org.junit.Test;

import junit.framework.TestCase;

public class StringUtilitiesTest extends TestCase 
{
	@Test
	public void testRemoveSpecialChars() 
	{
		assertEquals("Re ABC", StringUtilities.removeSpecialChars("[/\\*:?\"<>|]", "Re: ABC"));
	}
}
