package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.gdn.venice.facade.callback.SessionCallback;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.LogProviderPickupOrderReport;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class LogProviderPickupOrderReportSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "LogProviderPickupOrderReportSessionEJBBean")
public class LogProviderPickupOrderReportSessionEJBBean implements LogProviderPickupOrderReportSessionEJBRemote,
		LogProviderPickupOrderReportSessionEJBLocal {

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
	public LogProviderPickupOrderReportSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBBean");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<LogProviderPickupOrderReport> queryByRange(String jpqlStmt, int firstResult,
			int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRange()");

		Query query = null;
		try {
			query = em.createQuery(jpqlStmt);
			if(this.bindingArray != null){
				for(int i = 0; i < bindingArray.length; ++i){
				    if(bindingArray[i] != null){
						query.setParameter(i+1, bindingArray[i]);
					}
				}
			}
		} catch (Exception e) {
			_log.error("An exception occured when calling em.createQuery():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		try {
			if (firstResult > 0) {
				query = query.setFirstResult(firstResult);
			}
			if (maxResults > 0) {
				query = query.setMaxResults(maxResults);
			}
		} catch (Exception e) {
			_log.error("An exception occured when accessing the result set of a query:"
					+ e.getMessage());
			throw new EJBException(e);
		}		
		List<LogProviderPickupOrderReport> returnList = (List<LogProviderPickupOrderReport>)query.getResultList();
		this.bindingArray = null;
		
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRange() duration:" + duration + "ms");
		return returnList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#persistLogProviderPickupOrderReport(com
	 * .gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogProviderPickupOrderReport persistLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistLogProviderPickupOrderReport()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(logProviderPickupOrderReport)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogProviderPickupOrderReport existingLogProviderPickupOrderReport = null;

		if (logProviderPickupOrderReport != null && logProviderPickupOrderReport.getProviderInstructionsReportId() != null) {
			_log.debug("persistLogProviderPickupOrderReport:em.find()");
			try {
				existingLogProviderPickupOrderReport = em.find(LogProviderPickupOrderReport.class,
						logProviderPickupOrderReport.getProviderInstructionsReportId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingLogProviderPickupOrderReport == null) {
			_log.debug("persistLogProviderPickupOrderReport:em.persist()");
			try {
				em.persist(logProviderPickupOrderReport);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistLogProviderPickupOrderReport:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			
			// Call the onPostPersist() callback and throw an exception if it fails
			if (this._callback != null) {
				if (!this._callback.onPostPersist(logProviderPickupOrderReport)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistLogProviderPickupOrderReport() duration:" + duration + "ms");
			
			return logProviderPickupOrderReport;
		} else {
			throw new EJBException("LogProviderPickupOrderReport exists!. LogProviderPickupOrderReport = "
					+ logProviderPickupOrderReport.getProviderInstructionsReportId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#persistLogProviderPickupOrderReportList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogProviderPickupOrderReport> persistLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList) {
		_log.debug("persistLogProviderPickupOrderReportList()");
		Iterator i = logProviderPickupOrderReportList.iterator();
		while (i.hasNext()) {
			this.persistLogProviderPickupOrderReport((LogProviderPickupOrderReport) i.next());
		}
		return (ArrayList<LogProviderPickupOrderReport>)logProviderPickupOrderReportList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#mergeLogProviderPickupOrderReport(com.
	 * gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogProviderPickupOrderReport mergeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeLogProviderPickupOrderReport()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(logProviderPickupOrderReport)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogProviderPickupOrderReport existing = null;
		if (logProviderPickupOrderReport.getProviderInstructionsReportId() != null){
			_log.debug("mergeLogProviderPickupOrderReport:em.find()");		
			existing = em.find(LogProviderPickupOrderReport.class, logProviderPickupOrderReport.getProviderInstructionsReportId());
		}
		
		if (existing == null) {
			return this.persistLogProviderPickupOrderReport(logProviderPickupOrderReport);
		} else {
			_log.debug("mergeLogProviderPickupOrderReport:em.merge()");
			try {
				em.merge(logProviderPickupOrderReport);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeLogProviderPickupOrderReport:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			LogProviderPickupOrderReport newobject = em.find(LogProviderPickupOrderReport.class,
					logProviderPickupOrderReport.getProviderInstructionsReportId());
			_log.debug("mergeLogProviderPickupOrderReport():em.refresh");
			try {
				em.refresh(newobject);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.refresh():"
						+ e.getMessage());
				throw new EJBException(e);
			}

			// Call the onPostMerge() callback and throw an exception if it fails
			if (this._callback != null) {
				if (!this._callback.onPostMerge(newobject)) {
					_log.error("An onPostMerge callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostMerge callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}	
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("mergeLogProviderPickupOrderReport() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#mergeLogProviderPickupOrderReportList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogProviderPickupOrderReport> mergeLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList) {
		_log.debug("mergeLogProviderPickupOrderReportList()");
		Iterator i = logProviderPickupOrderReportList.iterator();
		while (i.hasNext()) {
			this.mergeLogProviderPickupOrderReport((LogProviderPickupOrderReport) i.next());
		}
		return (ArrayList<LogProviderPickupOrderReport>)logProviderPickupOrderReportList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#removeLogProviderPickupOrderReport(com.
	 * gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeLogProviderPickupOrderReport()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(logProviderPickupOrderReport)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeLogProviderPickupOrderReport:em.find()");
		logProviderPickupOrderReport = em.find(LogProviderPickupOrderReport.class, logProviderPickupOrderReport.getProviderInstructionsReportId());
		
		try {
			_log.debug("removeLogProviderPickupOrderReport:em.remove()");
			em.remove(logProviderPickupOrderReport);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(logProviderPickupOrderReport)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeLogProviderPickupOrderReport:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeLogProviderPickupOrderReport() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#removeLogProviderPickupOrderReportList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogProviderPickupOrderReportList(List<LogProviderPickupOrderReport> logProviderPickupOrderReportList) {
		_log.debug("removeLogProviderPickupOrderReportList()");
		Iterator i = logProviderPickupOrderReportList.iterator();
		while (i.hasNext()) {
			this.removeLogProviderPickupOrderReport((LogProviderPickupOrderReport) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#findByLogProviderPickupOrderReportLike(
	 * com.gdn.venice.persistence.LogProviderPickupOrderReport, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<LogProviderPickupOrderReport> findByLogProviderPickupOrderReportLike(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogProviderPickupOrderReportLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logProviderPickupOrderReport);
		HashMap complexTypeBindings = new HashMap();
		String stmt = qb.buildQueryString(complexTypeBindings, criteria);
		if(criteria != null){
			/*
			 * Get the binding array from the query builder and make
			 * it available to the queryByRange method
			 */
			this.bindingArray = qb.getBindingArray();
			for(int i = 0; i < qb.getBindingArray().length; i++){
				System.out.println("Bindings:" + i + ":" + qb.getBindingArray()[i]);
			}
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByLogProviderPickupOrderReportLike() duration:" + duration + "ms");
			return logProviderPickupOrderReportList;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#findByLogProviderPickupOrderReportLikeFR(
	 * com.gdn.venice.persistence.LogProviderPickupOrderReport, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByLogProviderPickupOrderReportLikeFR(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogProviderPickupOrderReportLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logProviderPickupOrderReport);
		HashMap complexTypeBindings = new HashMap();
		String stmt = qb.buildQueryString(complexTypeBindings, criteria);
		if(criteria != null){
			/*
			 * Get the binding array from the query builder and make
			 * it available to the queryByRange method
			 */
			this.bindingArray = qb.getBindingArray();
			for(int i = 0; i < qb.getBindingArray().length; i++){
				System.out.println("Bindings:" + i + ":" + qb.getBindingArray()[i]);
			}
			
			//Set the finder return object with the count of the total query rows
			FinderReturn fr = new FinderReturn();
			String countStmt = "select count(o) " + stmt.substring(stmt.indexOf("from"));
			Query query = null;
			try {
				query = em.createQuery(countStmt);
				if(this.bindingArray != null){
					for(int i = 0; i < bindingArray.length; ++i){
					    if(bindingArray[i] != null){
							query.setParameter(i+1, bindingArray[i]);
						}
					}
				}
				Long totalRows = (Long)query.getSingleResult();
				fr.setNumQueryRows(totalRows);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.createQuery():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			
			//Set the finder return object with the query list
			fr.setResultList(this.queryByRange(stmt, firstResult, maxResults));			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByLogProviderPickupOrderReportLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
