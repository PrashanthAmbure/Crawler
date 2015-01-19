/**
 * 
 */
package com.pramati.core.services;

import java.util.List;

/**
 * @author PAmbure
 * 
 *         Interface which can be implemented to download the information to a
 *         file system/database etc.
 * 
 * @input List of URLS whose information to download.
 * 
 */
public interface Downloader {
	Boolean download(List<String> urlsList) throws Exception;
}
