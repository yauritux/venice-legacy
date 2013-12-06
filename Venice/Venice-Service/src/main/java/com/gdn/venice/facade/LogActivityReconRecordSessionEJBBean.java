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
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class LogActivityReconRecordSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "LogActivityReconRecordSessionEJBBean")
public class LogActivityReconRecordSessionEJBBean implements LogActivityReconRecordSessionEJBRemote,
		LogActivityReconRecordSessionEJBLocal {

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
	public LogActivityReconRecordSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.LogActivityReconRecordSessionEJBBean");
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
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<LogActivityReconRecord> queryByRange(String jpqlStmt, int firstResult,
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
		List<LogActivityReconRecord> returnList = (List<LogActivityReconRecord>)query.getResultList();
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
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#persistLogActivityReconRecord(com
	 * .gdn.venice.persistence.LogActivityReconRecord)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogActivityReconRecord persistLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistLogActivityReconRecord()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(logActivityReconRecord)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogActivityReconRecord existingLogActivityReconRecord = null;

		if (logActivityReconRecord != null && logActivityReconRecord.getActivityReconRecordId() != null) {
			_log.debug("persistLogActivityReconRecord:em.find()");
			try {
				existingLogActivityReconRecord = em.find(LogActivityReconRecord.class,
						logActivityReconRecord.getActivityReconRecordId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingLogActivityReconRecord == null) {
			_log.debug("persistLogActivityReconRecord:em.persist()");
			try {
				em.persist(logActivityReconRecord);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistLogActivityReconRecord:em.flush()");
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
				if (!this._callback.onPostPersist(logActivityReconRecord)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistLogActivityReconRecord() duration:" + duration + "ms");
			
			return logActivityReconRecord;
		} else {
			throw new EJBException("LogActivityReconRecord exists!. LogActivityReconRecord = "
					+ logActivityReconRecord.getActivityReconRecordId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#persistLogActivityReconRecordList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogActivityReconRecord> persistLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList) {
		_log.debug("persistLogActivityReconRecordList()");
		Iterator i = logActivityReconRecordList.iterator();
		while (i.hasNext()) {
			this.persistLogActivityReconRecord((LogActivityReconRecord) i.next());
		}
		return (ArrayList<LogActivityReconRecord>)logActivityReconRecordList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#mergeLogActivityReconRecord(com.
	 * gdn.venice.persistence.LogActivityReconRecord)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogActivityReconRecord mergeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeLogActivityReconRecord()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(logActivityReconRecord)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogActivityReconRecord existing = null;
		if (logActivityReconRecord.getActivityReconRecordId() != null){
			_log.debug("mergeLogActivityReconRecord:em.find()");
			existing = em.find(LogActivityReconRecord.class, logActivityReconRecord.getActivityReconRecordId());
		}
		
		if (existing == null) {
			return this.persistLogActivityReconRecord(logActivityReconRecord);
		} else {
			if(logActivityReconRecord.getComment() == null)
				logActivityReconRecord.setComment(existing.getComment());
			if(logActivityReconRecord.getLogActionApplied() == null)
				logActivityReconRecord.setLogActionApplied(existing.getLogActionApplied());
			if(logActivityReconRecord.getLogActivityReconCommentHistories() == null)
				logActivityReconRecord.setLogActivityReconCommentHistories(existing.getLogActivityReconCommentHistories());
			if(logActivityReconRecord.getLogActivityReportUpload() == null)
				logActivityReconRecord.setLogActivityReportUpload(existing.getLogActivityReportUpload());
			if(logActivityReconRecord.getLogAirwayBill() == null)
				logActivityReconRecord.setLogAirwayBill(existing.getLogAirwayBill());
			if(logActivityReconRecord.getLogReconActivityRecordResult() == null)
				logActivityReconRecord.setLogReconActivityRecordResult(existing.getLogReconActivityRecordResult());
			if(logActivityReconRecord.getLogReconRecordStatus() == null)
				logActivityReconRecord.setLogReconRecordStatus(existing.getLogReconRecordStatus());
			if(logActivityReconRecord.getManuallyEnteredData() == null)
				logActivityReconRecord.setManuallyEnteredData(existing.getManuallyEnteredData());
			if(logActivityReconRecord.getProviderData() == null)
				logActivityReconRecord.setProviderData(existing.getProviderData());
			if(logActivityReconRecord.getUserLogonName() == null)
				logActivityReconRecord.setUserLogonName(existing.getUserLogonName());
			if(logActivityReconRecord.getVeniceData() == null)
				logActivityReconRecord.setVeniceData(existing.getVeniceData());
			
			_log.debug("mergeLogActivityReconRecord:em.merge()");
			try {
				em.merge(logActivityReconRecord);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeLogActivityReconRecord:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			LogActivityReconRecord newobject = em.find(LogActivityReconRecord.class,
					logActivityReconRecord.getActivityReconRecordId());
			_log.debug("mergeLogActivityReconRecord():em.refresh");
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
			_log.debug("mergeLogActivityReconRecord() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#mergeLogActivityReconRecordList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogActivityReconRecord> mergeLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList) {
		_log.debug("mergeLogActivityReconRecordList()");
		Iterator i = logActivityReconRecordList.iterator();
		while (i.hasNext()) {
			this.mergeLogActivityReconRecord((LogActivityReconRecord) i.next());
		}
		return (ArrayList<LogActivityReconRecord>)logActivityReconRecordList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#removeLogActivityReconRecord(com.
	 * gdn.venice.persistence.LogActivityReconRecord)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeLogActivityReconRecord()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(logActivityReconRecord)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeLogActivityReconRecord:em.find()");
		logActivityReconRecord = em.find(LogActivityReconRecord.class, logActivityReconRecord.getActivityReconRecordId());
		
		try {
			_log.debug("removeLogActivityReconRecord:em.remove()");
			em.remove(logActivityReconRecord);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(logActivityReconRecord)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeLogActivityReconRecord:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeLogActivityReconRecord() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#removeLogActivityReconRecordList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogActivityReconRecordList(List<LogActivityReconRecord> logActivityReconRecordList) {
		_log.debug("removeLogActivityReconRecordList()");
		Iterator i = logActivityReconRecordList.iterator();
		while (i.hasNext()) {
			this.removeLogActivityReconRecord((LogActivityReconRecord) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#findByLogActivityReconRecordLike(
	 * com.gdn.venice.persistence.LogActivityReconRecord, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<LogActivityReconRecord> findByLogActivityReconRecordLike(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogActivityReconRecordLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logActivityReconRecord);
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
			List<LogActivityReconRecord> logActivityReconRecordList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByLogActivityReconRecordLike() duration:" + duration + "ms");
			return logActivityReconRecordList;			
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
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#findByLogActivityReconRecordLikeFR(
	 * com.gdn.venice.persistence.LogActivityReconRecord, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByLogActivityReconRecordLikeFR(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogActivityReconRecordLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logActivityReconRecord);
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
			_log.debug("findByLogActivityReconRecordLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
