package com.gdn.venice.facade.fraud;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.callback.SessionCallback;
import com.gdn.venice.persistence.FrdParameterRule21;
import com.gdn.venice.persistence.FrdParameterRule26272829;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenParty;

/**
 * Session Bean implementation class FraudRuleWithNativeQuery
 */
@Stateless(mappedName = "FraudRuleWithNativeQuerySessionEJBBean")
public class FraudRuleWithNativeQuerySessionEJBBean implements FraudRuleWithNativeQueryRemote {

	/*
	 * Implements an IOC model for pre/post callbacks to persist, merge, and
	 * remove operations. The onPrePersist, onPostPersist, onPreMerge,
	 * onPostMerge, onPreRemove and OnPostRemove operations must be implemented
	 * by the callback class.
	 */
	private String _sessionCallbackClassName = null;

	// A reference to the callback object that has been instantiated
	private SessionCallback _callback = null;

	protected static Logger _log = null;

	// The configuration file to use
	private String _configFile = System.getenv("VENICE_HOME")
			+ "/conf/module-config.xml";

	//The binding array used when binding variables into a JPQL query
	private Object[] bindingArray = null;

	@PersistenceContext(unitName = "GDN-Venice-Persistence", type = PersistenceContextType.TRANSACTION)
	protected EntityManager em;

	/**
	 * Default constructor.
	 */
    public FraudRuleWithNativeQuerySessionEJBBean() {
    	super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.FraudRuleWithNativeQuerySessionEJBBean");
		// If the configuration is successful then instantiate the callback
		if (this.configure())
			this.instantiateTriggerCallback();
    }
    /**
	 * Reads the venice configuration file and configures the EJB's
	 * triggerCallbackClassName
	 */
	private Boolean configure() {
		_log.debug("Venice Configuration File:" + _configFile);
		try {
			XMLConfiguration config = new XMLConfiguration(_configFile);

			/*
			 * Get the index entry for the adapter configuration from the
			 * configuration file - there will be multiple adapter
			 * configurations
			 */
			@SuppressWarnings({ "rawtypes" })
			List callbacks = config
					.getList("sessionBeanConfig.callback.[@name]");
			Integer beanConfigIndex = new Integer(Integer.MAX_VALUE);
			@SuppressWarnings("rawtypes")
			Iterator i = callbacks.iterator();
			while (i.hasNext()) {
				String beanName = (String) i.next();
				if (this.getClass().getSimpleName().equals(beanName)) {
					beanConfigIndex = callbacks.indexOf(beanName);
					_log.debug("Bean configuration for " + beanName
							+ " found at " + beanConfigIndex);
				}
			}
			this._sessionCallbackClassName = config
					.getString("sessionBeanConfig.callback(" + beanConfigIndex + ").[@class]");

			_log.debug("Loaded configuration for _sessionCallbackClassName:"
					+ _sessionCallbackClassName);
		} catch (ConfigurationException e) {
			_log.error("A ConfigurationException occured when processing the configuration file"
					+ e.getMessage());
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Instantiates the trigger callback handler class
	 * 
	 * @return
	 */
	Boolean instantiateTriggerCallback() {
		if (_sessionCallbackClassName != null
				&& !_sessionCallbackClassName.isEmpty())
			try {
				Class<?> c = Class.forName(_sessionCallbackClassName);
				_callback = (SessionCallback) c.newInstance();
			} catch (ClassNotFoundException e) {
				_log.error("A ClassNotFoundException occured when trying to instantiate:"
						+ this._sessionCallbackClassName);
				e.printStackTrace();
				return Boolean.FALSE;
			} catch (InstantiationException e) {
				_log.error("A InstantiationException occured when trying to instantiate:"
						+ this._sessionCallbackClassName);
				e.printStackTrace();
				return Boolean.FALSE;
			} catch (IllegalAccessException e) {
				_log.error("A IllegalAccessException occured when trying to instantiate:"
						+ this._sessionCallbackClassName);
				e.printStackTrace();
				return Boolean.FALSE;
			}
		return Boolean.TRUE;

	}

	
	@Override
	public List<VenOrderPaymentAllocation> queryByRangeRuleVenOrderPaymentAllocation(
			String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeRuleVenOrderPaymentAllocation()");
		
		 List<VenOrderPaymentAllocation> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.VenOrderPaymentAllocation.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeRuleVenOrderPaymentAllocation() duration:" + duration + "ms");
		return returnList;
	}

	@Override
	public List<VenOrder> queryByRangeVenOrder(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeVenOrder()");
		
		 List<VenOrder> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.VenOrder.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeVenOrder() duration:" + duration + "ms");
		return returnList;
	}
	
	@Override
	public List<VenCustomer> queryByRangeVenCustomer(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeVenCustomer()");
		
		 List<VenCustomer> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.VenCustomer.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeVenCustomer() duration:" + duration + "ms");
		return returnList;
	}
	
	@Override
	public Long queryByRangeGetId(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeGetId()");
		
		BigInteger returnList = (BigInteger) em.createNativeQuery(jpqlStmt).getSingleResult();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeGetId() duration:" + duration + "ms");
		return returnList.longValue();
	}
	
	@Override
	public List<VenParty> queryByRangeVenParty(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeVenParty()");
		
		 List<VenParty> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.VenParty.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeVenParty() duration:" + duration + "ms");
		return returnList;
	}

	@Override
	public List<FrdParameterRule26272829> queryByRangeRule26272829(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeRule26272829()");
		
		 List<FrdParameterRule26272829> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.FrdParameterRule26272829.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeRule26272829() duration:" + duration + "ms");
		return returnList;
	}
	@Override
	public Double queryByRangeValue(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeValue()");
		BigDecimal returnlist = (BigDecimal) em.createNativeQuery(jpqlStmt).getSingleResult();
		 Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("queryByRangeValue() duration:" + duration + "ms");
		return returnlist.doubleValue();
	}
	@Override
	public List<BigDecimal> queryByRangeValueList(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeValueList()");
		List<BigDecimal> returnlist = (List<BigDecimal>) em.createNativeQuery(jpqlStmt).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeValueList() duration:" + duration + "ms");
		return returnlist;
	}
	@Override
	public List<FrdParameterRule21> queryByRangeNativeRule21(String jpqlStmt) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeNativeRule21()");
		
		 List<FrdParameterRule21> returnList = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.FrdParameterRule21.class).getResultList();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeNativeRule21() duration:" + duration + "ms");
		return returnList;
	}

}
