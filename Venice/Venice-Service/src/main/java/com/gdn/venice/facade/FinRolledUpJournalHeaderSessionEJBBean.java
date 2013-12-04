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
import com.gdn.venice.persistence.FinRolledUpJournalHeader;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class FinRolledUpJournalHeaderSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "FinRolledUpJournalHeaderSessionEJBBean")
public class FinRolledUpJournalHeaderSessionEJBBean implements FinRolledUpJournalHeaderSessionEJBRemote,
		FinRolledUpJournalHeaderSessionEJBLocal {

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
	public FinRolledUpJournalHeaderSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBBean");
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
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<FinRolledUpJournalHeader> queryByRange(String jpqlStmt, int firstResult,
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
		List<FinRolledUpJournalHeader> returnList = (List<FinRolledUpJournalHeader>)query.getResultList();
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
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#persistFinRolledUpJournalHeader(com
	 * .gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FinRolledUpJournalHeader persistFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistFinRolledUpJournalHeader()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(finRolledUpJournalHeader)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		FinRolledUpJournalHeader existingFinRolledUpJournalHeader = null;

		if (finRolledUpJournalHeader != null && finRolledUpJournalHeader.getRuJournalHeaderId() != null) {
			_log.debug("persistFinRolledUpJournalHeader:em.find()");
			try {
				existingFinRolledUpJournalHeader = em.find(FinRolledUpJournalHeader.class,
						finRolledUpJournalHeader.getRuJournalHeaderId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingFinRolledUpJournalHeader == null) {
			_log.debug("persistFinRolledUpJournalHeader:em.persist()");
			try {
				em.persist(finRolledUpJournalHeader);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistFinRolledUpJournalHeader:em.flush()");
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
				if (!this._callback.onPostPersist(finRolledUpJournalHeader)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistFinRolledUpJournalHeader() duration:" + duration + "ms");
			
			return finRolledUpJournalHeader;
		} else {
			throw new EJBException("FinRolledUpJournalHeader exists!. FinRolledUpJournalHeader = "
					+ finRolledUpJournalHeader.getRuJournalHeaderId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#persistFinRolledUpJournalHeaderList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<FinRolledUpJournalHeader> persistFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList) {
		_log.debug("persistFinRolledUpJournalHeaderList()");
		Iterator i = finRolledUpJournalHeaderList.iterator();
		while (i.hasNext()) {
			this.persistFinRolledUpJournalHeader((FinRolledUpJournalHeader) i.next());
		}
		return (ArrayList<FinRolledUpJournalHeader>)finRolledUpJournalHeaderList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#mergeFinRolledUpJournalHeader(com.
	 * gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FinRolledUpJournalHeader mergeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeFinRolledUpJournalHeader()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(finRolledUpJournalHeader)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		FinRolledUpJournalHeader existing = null;
		if (finRolledUpJournalHeader.getRuJournalHeaderId() != null){
			_log.debug("mergeFinRolledUpJournalHeader:em.find()");
			existing = em.find(FinRolledUpJournalHeader.class, finRolledUpJournalHeader.getRuJournalHeaderId());
		}
		
		if (existing == null) {
			return this.persistFinRolledUpJournalHeader(finRolledUpJournalHeader);
		} else {
			_log.debug("mergeFinRolledUpJournalHeader:em.merge()");
			try {
				em.merge(finRolledUpJournalHeader);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeFinRolledUpJournalHeader:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			FinRolledUpJournalHeader newobject = em.find(FinRolledUpJournalHeader.class,
					finRolledUpJournalHeader.getRuJournalHeaderId());
			_log.debug("mergeFinRolledUpJournalHeader():em.refresh");
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
			_log.debug("mergeFinRolledUpJournalHeader() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#mergeFinRolledUpJournalHeaderList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<FinRolledUpJournalHeader> mergeFinRolledUpJournalHeaderList(
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList) {
		_log.debug("mergeFinRolledUpJournalHeaderList()");
		Iterator i = finRolledUpJournalHeaderList.iterator();
		while (i.hasNext()) {
			this.mergeFinRolledUpJournalHeader((FinRolledUpJournalHeader) i.next());
		}
		return (ArrayList<FinRolledUpJournalHeader>)finRolledUpJournalHeaderList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#removeFinRolledUpJournalHeader(com.
	 * gdn.venice.persistence.FinRolledUpJournalHeader)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeFinRolledUpJournalHeader()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(finRolledUpJournalHeader)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeFinRolledUpJournalHeader:em.find()");
		finRolledUpJournalHeader = em.find(FinRolledUpJournalHeader.class, finRolledUpJournalHeader.getRuJournalHeaderId());
		
		try {
			_log.debug("removeFinRolledUpJournalHeader:em.remove()");
			em.remove(finRolledUpJournalHeader);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(finRolledUpJournalHeader)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeFinRolledUpJournalHeader:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeFinRolledUpJournalHeader() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#removeFinRolledUpJournalHeaderList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeFinRolledUpJournalHeaderList(List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList) {
		_log.debug("removeFinRolledUpJournalHeaderList()");
		Iterator i = finRolledUpJournalHeaderList.iterator();
		while (i.hasNext()) {
			this.removeFinRolledUpJournalHeader((FinRolledUpJournalHeader) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#findByFinRolledUpJournalHeaderLike(
	 * com.gdn.venice.persistence.FinRolledUpJournalHeader, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<FinRolledUpJournalHeader> findByFinRolledUpJournalHeaderLike(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByFinRolledUpJournalHeaderLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(finRolledUpJournalHeader);
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
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByFinRolledUpJournalHeaderLike() duration:" + duration + "ms");
			return finRolledUpJournalHeaderList;			
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
	 * com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote#findByFinRolledUpJournalHeaderLikeFR(
	 * com.gdn.venice.persistence.FinRolledUpJournalHeader, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByFinRolledUpJournalHeaderLikeFR(FinRolledUpJournalHeader finRolledUpJournalHeader,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByFinRolledUpJournalHeaderLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(finRolledUpJournalHeader);
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
			_log.debug("findByFinRolledUpJournalHeaderLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
