package com.djarum.raf.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Log4JLoggerFactory - A utility class that implements a wrapper for Log4J
 * logger factory
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2010
 * 
 */
public class Log4jLoggerFactory {

	protected static final String LOG_PROPERTIES_FILE = System
			.getenv("VENICE_HOME") + "/lib/Log4J.properties";

	private static Properties logProperties = null;
	
	/**
	 * getLog4JLogger - gets a log4j logger based on the category and logs the
	 * logger initialization
	 * 
	 * @param logCategory
	 *            is the category to use when getting the logger instance
	 * @return returns a log4j logger instance
	 */
	public Logger getLog4JLogger(String logCategory) {

		Logger log = null;
		try {
			// load our log4j properties / configuration file
			if(logProperties == null){
				logProperties = new Properties();
				logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
			}
			
			PropertyConfigurator.configure(logProperties);
			log = Logger.getLogger(logCategory);
			log.debug("Logging for " + logCategory + " initialized.");
		} catch (IOException e) {
			throw new RuntimeException("Unable to load logging property "
					+ LOG_PROPERTIES_FILE);
		}

		return log;

	}

}
