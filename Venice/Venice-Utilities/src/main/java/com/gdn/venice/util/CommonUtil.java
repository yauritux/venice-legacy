package com.gdn.venice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.gdn.venice.constants.LoggerLevel;
import com.gdn.venice.exception.VeniceInternalException;

/**
 * 
 * @author yauritux
 * 
 */
public class CommonUtil {

	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c) {
			r.add(clazz.cast(o));
		}
		return r;
	}

	public static void logException(VeniceInternalException exception
			, Logger logger, LoggerLevel level) {
		switch (level) {
		case TRACE:
			logger.trace(exception.getMessage(), exception);
			break;
		case DEBUG:
			logger.debug(exception.getMessage(), exception);
			break;
		case INFO:
			logger.info(exception.getMessage(), exception);
			break;
		case WARN:
			logger.warn(exception.getMessage(), exception);
			break;
		default:
			logger.error(exception.getMessage(), exception);
		}
	}
	
	public static VeniceInternalException logAndReturnException(VeniceInternalException exception
			, Logger logger, LoggerLevel level) {
		logException(exception, logger, level);
		return exception;
	}
	
	public static void logTrace(Logger logger, String message) {
		if (logger.isTraceEnabled()) logger.trace(message);
	}
	
	public static void logDebug(Logger logger, String message) {
		if (logger.isDebugEnabled()) logger.debug(message);
	}
	
	public static void logInfo(Logger logger, String message) {
		if (logger.isInfoEnabled()) logger.info(message);
	}
}
