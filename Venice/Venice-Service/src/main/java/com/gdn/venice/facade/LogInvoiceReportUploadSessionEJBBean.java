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
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class LogInvoiceReportUploadSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "LogInvoiceReportUploadSessionEJBBean")
public class LogInvoiceReportUploadSessionEJBBean implements LogInvoiceReportUploadSessionEJBRemote,
		LogInvoiceReportUploadSessionEJBLocal {

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
	public LogInvoiceReportUploadSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBBean");
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
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<LogInvoiceReportUpload> queryByRange(String jpqlStmt, int firstResult,
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
		List<LogInvoiceReportUpload> returnList = (List<LogInvoiceReportUpload>)query.getResultList();
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
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#persistLogInvoiceReportUpload(com
	 * .gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogInvoiceReportUpload persistLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistLogInvoiceReportUpload()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(logInvoiceReportUpload)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogInvoiceReportUpload existingLogInvoiceReportUpload = null;

		if (logInvoiceReportUpload != null && logInvoiceReportUpload.getInvoiceReportUploadId() != null) {
			_log.debug("persistLogInvoiceReportUpload:em.find()");
			try {
				existingLogInvoiceReportUpload = em.find(LogInvoiceReportUpload.class,
						logInvoiceReportUpload.getInvoiceReportUploadId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingLogInvoiceReportUpload == null) {
			_log.debug("persistLogInvoiceReportUpload:em.persist()");
			try {
				em.persist(logInvoiceReportUpload);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistLogInvoiceReportUpload:em.flush()");
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
				if (!this._callback.onPostPersist(logInvoiceReportUpload)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistLogInvoiceReportUpload() duration:" + duration + "ms");
			
			return logInvoiceReportUpload;
		} else {
			throw new EJBException("LogInvoiceReportUpload exists!. LogInvoiceReportUpload = "
					+ logInvoiceReportUpload.getInvoiceReportUploadId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#persistLogInvoiceReportUploadList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogInvoiceReportUpload> persistLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList) {
		_log.debug("persistLogInvoiceReportUploadList()");
		Iterator i = logInvoiceReportUploadList.iterator();
		while (i.hasNext()) {
			this.persistLogInvoiceReportUpload((LogInvoiceReportUpload) i.next());
		}
		return (ArrayList<LogInvoiceReportUpload>)logInvoiceReportUploadList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#mergeLogInvoiceReportUpload(com.
	 * gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogInvoiceReportUpload mergeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeLogInvoiceReportUpload()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(logInvoiceReportUpload)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogInvoiceReportUpload existing = null;
		if (logInvoiceReportUpload.getInvoiceReportUploadId() != null){
			_log.debug("mergeLogInvoiceReportUpload:em.find()");		
			existing = em.find(LogInvoiceReportUpload.class, logInvoiceReportUpload.getInvoiceReportUploadId());
		}
		
		if (existing == null) {
			return this.persistLogInvoiceReportUpload(logInvoiceReportUpload);
		} else {
			_log.debug("mergeLogInvoiceReportUpload:em.merge()");
			try {
				em.merge(logInvoiceReportUpload);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeLogInvoiceReportUpload:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			LogInvoiceReportUpload newobject = em.find(LogInvoiceReportUpload.class,
					logInvoiceReportUpload.getInvoiceReportUploadId());
			_log.debug("mergeLogInvoiceReportUpload():em.refresh");
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
			_log.debug("mergeLogInvoiceReportUpload() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#mergeLogInvoiceReportUploadList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogInvoiceReportUpload> mergeLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList) {
		_log.debug("mergeLogInvoiceReportUploadList()");
		Iterator i = logInvoiceReportUploadList.iterator();
		while (i.hasNext()) {
			this.mergeLogInvoiceReportUpload((LogInvoiceReportUpload) i.next());
		}
		return (ArrayList<LogInvoiceReportUpload>)logInvoiceReportUploadList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#removeLogInvoiceReportUpload(com.
	 * gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeLogInvoiceReportUpload()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(logInvoiceReportUpload)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeLogInvoiceReportUpload:em.find()");
		logInvoiceReportUpload = em.find(LogInvoiceReportUpload.class, logInvoiceReportUpload.getInvoiceReportUploadId());
		
		try {
			_log.debug("removeLogInvoiceReportUpload:em.remove()");
			em.remove(logInvoiceReportUpload);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(logInvoiceReportUpload)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeLogInvoiceReportUpload:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeLogInvoiceReportUpload() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#removeLogInvoiceReportUploadList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogInvoiceReportUploadList(List<LogInvoiceReportUpload> logInvoiceReportUploadList) {
		_log.debug("removeLogInvoiceReportUploadList()");
		Iterator i = logInvoiceReportUploadList.iterator();
		while (i.hasNext()) {
			this.removeLogInvoiceReportUpload((LogInvoiceReportUpload) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#findByLogInvoiceReportUploadLike(
	 * com.gdn.venice.persistence.LogInvoiceReportUpload, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<LogInvoiceReportUpload> findByLogInvoiceReportUploadLike(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogInvoiceReportUploadLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logInvoiceReportUpload);
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
			List<LogInvoiceReportUpload> logInvoiceReportUploadList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByLogInvoiceReportUploadLike() duration:" + duration + "ms");
			return logInvoiceReportUploadList;			
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
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#findByLogInvoiceReportUploadLikeFR(
	 * com.gdn.venice.persistence.LogInvoiceReportUpload, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByLogInvoiceReportUploadLikeFR(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogInvoiceReportUploadLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logInvoiceReportUpload);
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
			_log.debug("findByLogInvoiceReportUploadLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
