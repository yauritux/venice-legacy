package com.djarum.raf.utilities;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

/**
 * Locator.java - a class for locating EJB objects
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 * @param <T>
 */
public class Locator<T> {

	/**
	 * Basic constructor establishes an initial context
	 * 
	 * @throws Exception
	 */
	public Locator() throws Exception {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.djarum.raf.utilities.Locator");
		this._ic = this.getInitialContext();
	}

	/**
	 * Constructor with parameters for the JNDI connection
	 * 
	 * @param hostName
	 *            the host to connect to
	 * @param port
	 *            the port to connect on
	 * @param props
	 *            the connection properties
	 * @throws Exception
	 *             whenever any exceptiojn occurs
	 */
	public Locator(String hostName, String port, Properties props)
			throws Exception {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.util.Locator");
		this.hostName = hostName;
		this.port = port;
		this.props = props;
		this._ic = this.getInitialContext();
	}

	private String hostName = "127.0.0.1";
	private String port = "4211";

	private Properties props = new Properties();

	private Context _ic = null;

	protected static Logger _log = null;

	/**
	 * getInitialContext - returns a context to use for lookups
	 * 
	 * @return a context to use for lookups
	 * @throws Exception
	 *             whenever any exception occurs
	 */
	private Context getInitialContext() throws Exception {
		
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.openejb.client.RemoteInitialContextFactory");
		props.setProperty(Context.PROVIDER_URL, "ejbd://" + hostName + ":" + port);
		System.setProperty("openejb.client.connection.pool.size", "100");
		System.setProperty("openejb.client.connection.pool.timeout", "2000");
		_log.debug("getInitialContext() properties:" + props.toString());
		Context ic = null;
		try {
			// Get the initial context based on the props
			ic = new InitialContext(props);
		} catch (Throwable e) {
			_log.error("An exception has occured while getting the initial context:"
					+ e.getMessage());
			throw new Exception(e);
		}
		return ic;
	}

	/**
	 * lookup - a generic method for remote object lookup
	 * 
	 * @param c
	 *            the class/type of object to lookup (class)
	 * @param remoteName
	 *            the JNDI name of the object
	 * @return an object of the type specified by T
	 * @throws Exception
	 */
	public Object lookup(Class<?> c, String remoteName) throws Exception {
		_log.debug("Looking up remote object of class:" + c.getSimpleName());
		try {

			@SuppressWarnings("rawtypes")
			NamingEnumeration ne = _ic.list("");

			while (ne.hasMoreElements()) {
				_log.debug("Next:" + ne.next().toString());
			}

			@SuppressWarnings("unchecked")
			T objectHome = (T) PortableRemoteObject.narrow(
					_ic.lookup(remoteName), c);
			return objectHome;
		} catch (Exception e) {
			_log.error("An exception occured when looking up a remote EJB home:"
					+ e.getMessage());
			throw new Exception(e);
		}
	}

	/**
	 * lookupLocal - a generic method for looking up a local EJB
	 * 
	 * @param c
	 *            the type of object to lookup (class)
	 * @param beanName
	 *            the JNDI name of the object
	 * @return an object of the type specified by T
	 * @throws Exception
	 */
	public Object lookupLocal(Class<?> c, String beanName) throws Exception {
		_log.debug("Looking up local object of class:" + c.getSimpleName());
		try {
			Properties props = new Properties();
			props.setProperty("java.naming.factory.initial",
					"org.openejb.client.LocalInitialContextFactory");
			System.setProperty("openejb.client.connection.pool.size", "100");
			System.setProperty("openejb.client.connection.pool.timeout", "2000");
			Context ic = new InitialContext(props);

			@SuppressWarnings("rawtypes")
			NamingEnumeration ne = ic.list("");

			while (ne.hasMoreElements()) {
				_log.debug("Next:" + ne.next().toString());
			}

			@SuppressWarnings("unchecked")
			T objectHome = (T) ic.lookup(beanName);
			return objectHome;
		} catch (Exception e) {
			_log.error("An exception occured when looking up a local EJB home:"
					+ e.getMessage());
			throw new Exception(e);
		}
	}

	/**
	 * close - closes the locators connections
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		if (_ic != null) {
			try {
				_ic.close();
			} catch (NamingException e) {
				_log.error("An exception occured when closing a locator context"
						+ e.getMessage());
				throw new Exception(e);
			}
		}
	}
}
