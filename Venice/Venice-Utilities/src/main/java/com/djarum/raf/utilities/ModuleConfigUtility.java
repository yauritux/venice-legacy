package com.djarum.raf.utilities;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * ModuleConfigUtility.java - a class for accessing module-config.xml
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2012
 * 
 */
public class ModuleConfigUtility {
    
        /**
	 * Logger instance
	 */
	protected static Logger _log = null;
	
	private String _moduleConfigFilePath;
	
	public ModuleConfigUtility() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.djarum.raf.utilities.ModuleConfigUtility");
        _moduleConfigFilePath = System.getenv("VENICE_HOME") + "/conf/module-config.xml";
	}

	/**
	 * Returns a string value from the configuration path
	 * @param key is the configuration key
	 * @return a String value
	 */
	public String getStringValue(String key){
		String value = "";
		try {
			XMLConfiguration config = new XMLConfiguration(_moduleConfigFilePath);
			value = config.getString(key);
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when reading:" + _moduleConfigFilePath);
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * Returns a long value from the configuration path
	 * @param key is the configuration path
	 * @return a Long value or zero as default
	 */
	public Long getLongValue(String key){
		Long value = new Long(0);
		try {
			XMLConfiguration config = new XMLConfiguration(_moduleConfigFilePath);
			value = config.getLong(key);
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when reading:" + _moduleConfigFilePath);
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * Returns a Double value from the configuration
	 * @param key is the configuration key
	 * @return a Double value or zero as default
	 */
	public Double getDoubleValue(String key){
		Double value = new Double(0);
		try {
			XMLConfiguration config = new XMLConfiguration(_moduleConfigFilePath);
			value = config.getDouble(key);
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when reading:" + _moduleConfigFilePath);
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * Returns a Boolean value from the configuration
	 * @param key is the configuration key
	 * @return a Boolean value or false as default
	 */
	public Boolean getBooleanValue(String key){
		Boolean value = new Boolean(false);
		try {
			XMLConfiguration config = new XMLConfiguration(_moduleConfigFilePath);
			value = config.getBoolean(key);
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when reading:" + _moduleConfigFilePath);
			e.printStackTrace();
		}
		return value;
	}
}
