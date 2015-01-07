package com.pramati.core.util;

import org.junit.Test;

import junit.framework.TestCase;

public class StringUtilitiesTest extends TestCase 
{
	@Test
	public void testReplaceBlank() 
	{
		assertEquals("Re  ABC", StringUtilities.replaceBlank("[/\\*:?\"<>|]", "Re: ABC"));
	}
}
