package com.gdn.venice.facade.callback;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.persistence.VenCountry;

/**
 * VenCountrySessionEJBCallback.java
 * 
 * This is a trivial example of how to intercept the events in the session
 * and perform additional business logic processing. Returning false
 * from any of these method calls will fail and rollback the entire
 * container managed transaction by throwing an EJBException.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class VenCountrySessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public VenCountrySessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.VenCountrySessionEJBCallback");
	}


	@Override
	public Boolean onPrePersist(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPrePersist()" + country.getCountryName());
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostPersist(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPostPersist()" + country.getCountryName());
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPreMerge(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPreMerge()" + country.getCountryName());
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostMerge(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPostMerge()" + country.getCountryName());
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPreRemove(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPreRemove()" + country.getCountryName());
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostRemove(Object businessObject) {
		VenCountry country = (VenCountry)businessObject;
		_log.debug("onPostRemove()" + country.getCountryName());
		return Boolean.TRUE;
	}

}
