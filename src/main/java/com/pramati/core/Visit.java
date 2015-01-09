/**
 * 
 */
package com.pramati.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

/**
 * @author PAmbure
 *
 */
public class Visit 
{
	Map<String,List<Element>> visitsMap;
	public Visit()
	{
		visitsMap = new HashMap<String, List<Element>>();
	}
	
	protected Map<String,List<Element>> getVisitMap()
	{
		return visitsMap;
	}
}
