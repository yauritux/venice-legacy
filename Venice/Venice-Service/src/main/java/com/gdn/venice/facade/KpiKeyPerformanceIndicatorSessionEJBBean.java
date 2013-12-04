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
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class KpiKeyPerformanceIndicatorSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "KpiKeyPerformanceIndicatorSessionEJBBean")
public class KpiKeyPerformanceIndicatorSessionEJBBean implements KpiKeyPerformanceIndicatorSessionEJBRemote,
		KpiKeyPerformanceIndicatorSessionEJBLocal {

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
	public KpiKeyPerformanceIndicatorSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBBean");
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
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<KpiKeyPerformanceIndicator> queryByRange(String jpqlStmt, int firstResult,
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
		List<KpiKeyPerformanceIndicator> returnList = (List<KpiKeyPerformanceIndicator>)query.getResultList();
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
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#persistKpiKeyPerformanceIndicator(com
	 * .gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KpiKeyPerformanceIndicator persistKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistKpiKeyPerformanceIndicator()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(kpiKeyPerformanceIndicator)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		KpiKeyPerformanceIndicator existingKpiKeyPerformanceIndicator = null;

		if (kpiKeyPerformanceIndicator != null && kpiKeyPerformanceIndicator.getKpiId() != null) {
			_log.debug("persistKpiKeyPerformanceIndicator:em.find()");
			try {
				existingKpiKeyPerformanceIndicator = em.find(KpiKeyPerformanceIndicator.class,
						kpiKeyPerformanceIndicator.getKpiId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingKpiKeyPerformanceIndicator == null) {
			_log.debug("persistKpiKeyPerformanceIndicator:em.persist()");
			try {
				em.persist(kpiKeyPerformanceIndicator);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistKpiKeyPerformanceIndicator:em.flush()");
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
				if (!this._callback.onPostPersist(kpiKeyPerformanceIndicator)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistKpiKeyPerformanceIndicator() duration:" + duration + "ms");
			
			return kpiKeyPerformanceIndicator;
		} else {
			throw new EJBException("KpiKeyPerformanceIndicator exists!. KpiKeyPerformanceIndicator = "
					+ kpiKeyPerformanceIndicator.getKpiId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#persistKpiKeyPerformanceIndicatorList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<KpiKeyPerformanceIndicator> persistKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList) {
		_log.debug("persistKpiKeyPerformanceIndicatorList()");
		Iterator i = kpiKeyPerformanceIndicatorList.iterator();
		while (i.hasNext()) {
			this.persistKpiKeyPerformanceIndicator((KpiKeyPerformanceIndicator) i.next());
		}
		return (ArrayList<KpiKeyPerformanceIndicator>)kpiKeyPerformanceIndicatorList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#mergeKpiKeyPerformanceIndicator(com.
	 * gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KpiKeyPerformanceIndicator mergeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeKpiKeyPerformanceIndicator()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(kpiKeyPerformanceIndicator)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		KpiKeyPerformanceIndicator existing = null;
		if (kpiKeyPerformanceIndicator.getKpiId() != null){
			_log.debug("mergeKpiKeyPerformanceIndicator:em.find()");
			existing = em.find(KpiKeyPerformanceIndicator.class, kpiKeyPerformanceIndicator.getKpiId());
		}
		
		if (existing == null) {
			return this.persistKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
		} else {
			_log.debug("mergeKpiKeyPerformanceIndicator:em.merge()");
			try {
				em.merge(kpiKeyPerformanceIndicator);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeKpiKeyPerformanceIndicator:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			KpiKeyPerformanceIndicator newobject = em.find(KpiKeyPerformanceIndicator.class,
					kpiKeyPerformanceIndicator.getKpiId());
			_log.debug("mergeKpiKeyPerformanceIndicator():em.refresh");
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
			_log.debug("mergeKpiKeyPerformanceIndicator() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#mergeKpiKeyPerformanceIndicatorList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<KpiKeyPerformanceIndicator> mergeKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList) {
		_log.debug("mergeKpiKeyPerformanceIndicatorList()");
		Iterator i = kpiKeyPerformanceIndicatorList.iterator();
		while (i.hasNext()) {
			this.mergeKpiKeyPerformanceIndicator((KpiKeyPerformanceIndicator) i.next());
		}
		return (ArrayList<KpiKeyPerformanceIndicator>)kpiKeyPerformanceIndicatorList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#removeKpiKeyPerformanceIndicator(com.
	 * gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeKpiKeyPerformanceIndicator()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(kpiKeyPerformanceIndicator)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeKpiKeyPerformanceIndicator:em.find()");
		kpiKeyPerformanceIndicator = em.find(KpiKeyPerformanceIndicator.class, kpiKeyPerformanceIndicator.getKpiId());
		
		try {
			_log.debug("removeKpiKeyPerformanceIndicator:em.remove()");
			em.remove(kpiKeyPerformanceIndicator);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(kpiKeyPerformanceIndicator)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeKpiKeyPerformanceIndicator:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeKpiKeyPerformanceIndicator() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#removeKpiKeyPerformanceIndicatorList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeKpiKeyPerformanceIndicatorList(List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList) {
		_log.debug("removeKpiKeyPerformanceIndicatorList()");
		Iterator i = kpiKeyPerformanceIndicatorList.iterator();
		while (i.hasNext()) {
			this.removeKpiKeyPerformanceIndicator((KpiKeyPerformanceIndicator) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#findByKpiKeyPerformanceIndicatorLike(
	 * com.gdn.venice.persistence.KpiKeyPerformanceIndicator, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<KpiKeyPerformanceIndicator> findByKpiKeyPerformanceIndicatorLike(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByKpiKeyPerformanceIndicatorLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(kpiKeyPerformanceIndicator);
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
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByKpiKeyPerformanceIndicatorLike() duration:" + duration + "ms");
			return kpiKeyPerformanceIndicatorList;			
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
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#findByKpiKeyPerformanceIndicatorLikeFR(
	 * com.gdn.venice.persistence.KpiKeyPerformanceIndicator, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByKpiKeyPerformanceIndicatorLikeFR(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByKpiKeyPerformanceIndicatorLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(kpiKeyPerformanceIndicator);
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
			_log.debug("findByKpiKeyPerformanceIndicatorLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
