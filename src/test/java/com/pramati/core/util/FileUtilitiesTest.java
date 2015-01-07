package com.pramati.core.util;

import org.junit.Test;

import junit.framework.TestCase;

public class FileUtilitiesTest extends TestCase 
{
	@Test
	public void testCreateDirectory() 
	{
		assertTrue(FileUtilities.createDirectory("D://", "TestFolder"));
	}
}
