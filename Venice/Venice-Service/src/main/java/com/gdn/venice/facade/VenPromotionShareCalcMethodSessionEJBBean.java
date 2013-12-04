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
import com.gdn.venice.persistence.VenPromotionShareCalcMethod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Session Bean implementation class VenPromotionShareCalcMethodSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "VenPromotionShareCalcMethodSessionEJBBean")
public class VenPromotionShareCalcMethodSessionEJBBean implements VenPromotionShareCalcMethodSessionEJBRemote,
		VenPromotionShareCalcMethodSessionEJBLocal {

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
	public VenPromotionShareCalcMethodSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBBean");
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
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<VenPromotionShareCalcMethod> queryByRange(String jpqlStmt, int firstResult,
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
		List<VenPromotionShareCalcMethod> returnList = (List<VenPromotionShareCalcMethod>)query.getResultList();
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
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#persistVenPromotionShareCalcMethod(com
	 * .gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VenPromotionShareCalcMethod persistVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistVenPromotionShareCalcMethod()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(venPromotionShareCalcMethod)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		VenPromotionShareCalcMethod existingVenPromotionShareCalcMethod = null;

		if (venPromotionShareCalcMethod != null && venPromotionShareCalcMethod.getPromotionCalcMethodId() != null) {
			_log.debug("persistVenPromotionShareCalcMethod:em.find()");
			try {
				existingVenPromotionShareCalcMethod = em.find(VenPromotionShareCalcMethod.class,
						venPromotionShareCalcMethod.getPromotionCalcMethodId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingVenPromotionShareCalcMethod == null) {
			_log.debug("persistVenPromotionShareCalcMethod:em.persist()");
			try {
				em.persist(venPromotionShareCalcMethod);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistVenPromotionShareCalcMethod:em.flush()");
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
				if (!this._callback.onPostPersist(venPromotionShareCalcMethod)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistVenPromotionShareCalcMethod() duration:" + duration + "ms");
			
			return venPromotionShareCalcMethod;
		} else {
			throw new EJBException("VenPromotionShareCalcMethod exists!. VenPromotionShareCalcMethod = "
					+ venPromotionShareCalcMethod.getPromotionCalcMethodId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#persistVenPromotionShareCalcMethodList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<VenPromotionShareCalcMethod> persistVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList) {
		_log.debug("persistVenPromotionShareCalcMethodList()");
		Iterator i = venPromotionShareCalcMethodList.iterator();
		while (i.hasNext()) {
			this.persistVenPromotionShareCalcMethod((VenPromotionShareCalcMethod) i.next());
		}
		return (ArrayList<VenPromotionShareCalcMethod>)venPromotionShareCalcMethodList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#mergeVenPromotionShareCalcMethod(com.
	 * gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VenPromotionShareCalcMethod mergeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeVenPromotionShareCalcMethod()");

		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreMerge(venPromotionShareCalcMethod)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		VenPromotionShareCalcMethod existing = null;
		if (venPromotionShareCalcMethod.getPromotionCalcMethodId() != null){
			_log.debug("mergeVenPromotionShareCalcMethod:em.find()");
		
		existing = em.find(VenPromotionShareCalcMethod.class, venPromotionShareCalcMethod.getPromotionCalcMethodId());
		}
		if (existing == null) {
			return this.persistVenPromotionShareCalcMethod(venPromotionShareCalcMethod);
		} else {
			_log.debug("mergeVenPromotionShareCalcMethod:em.merge()");
			try {
				em.merge(venPromotionShareCalcMethod);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("mergeVenPromotionShareCalcMethod:em.flush()");
			try {
				em.flush();
				em.clear();
			} catch (Exception e) {
				_log.error("An exception occured when calling em.flush():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			VenPromotionShareCalcMethod newobject = em.find(VenPromotionShareCalcMethod.class,
					venPromotionShareCalcMethod.getPromotionCalcMethodId());
			_log.debug("mergeVenPromotionShareCalcMethod():em.refresh");
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
			_log.debug("mergeVenPromotionShareCalcMethod() duration:" + duration + "ms");
						
			return newobject;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#mergeVenPromotionShareCalcMethodList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<VenPromotionShareCalcMethod> mergeVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList) {
		_log.debug("mergeVenPromotionShareCalcMethodList()");
		Iterator i = venPromotionShareCalcMethodList.iterator();
		while (i.hasNext()) {
			this.mergeVenPromotionShareCalcMethod((VenPromotionShareCalcMethod) i.next());
		}
		return (ArrayList<VenPromotionShareCalcMethod>)venPromotionShareCalcMethodList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#removeVenPromotionShareCalcMethod(com.
	 * gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeVenPromotionShareCalcMethod()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(venPromotionShareCalcMethod)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeVenPromotionShareCalcMethod:em.find()");
		venPromotionShareCalcMethod = em.find(VenPromotionShareCalcMethod.class, venPromotionShareCalcMethod.getPromotionCalcMethodId());
		
		try {
			_log.debug("removeVenPromotionShareCalcMethod:em.remove()");
			em.remove(venPromotionShareCalcMethod);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(venPromotionShareCalcMethod)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeVenPromotionShareCalcMethod:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeVenPromotionShareCalcMethod() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#removeVenPromotionShareCalcMethodList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeVenPromotionShareCalcMethodList(List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList) {
		_log.debug("removeVenPromotionShareCalcMethodList()");
		Iterator i = venPromotionShareCalcMethodList.iterator();
		while (i.hasNext()) {
			this.removeVenPromotionShareCalcMethod((VenPromotionShareCalcMethod) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#findByVenPromotionShareCalcMethodLike(
	 * com.gdn.venice.persistence.VenPromotionShareCalcMethod, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<VenPromotionShareCalcMethod> findByVenPromotionShareCalcMethodLike(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByVenPromotionShareCalcMethodLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(venPromotionShareCalcMethod);
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
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList = this.queryByRange(stmt, firstResult, maxResults);			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByVenPromotionShareCalcMethodLike() duration:" + duration + "ms");
			return venPromotionShareCalcMethodList;			
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
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#findByVenPromotionShareCalcMethodLikeFR(
	 * com.gdn.venice.persistence.VenPromotionShareCalcMethod, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByVenPromotionShareCalcMethodLikeFR(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByVenPromotionShareCalcMethodLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(venPromotionShareCalcMethod);
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
			_log.debug("findByVenPromotionShareCalcMethodLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

}
