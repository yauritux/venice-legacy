package com.gdn.venice.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.callback.LogAirwayBillSessionEJBCallback;
import com.gdn.venice.facade.callback.SessionCallback;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.LogAirwayBill;

/**
 * Session Bean implementation class LogAirwayBillSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "LogAirwayBillSessionEJBBean")
public class LogAirwayBillSessionEJBBean implements LogAirwayBillSessionEJBRemote,
		LogAirwayBillSessionEJBLocal {

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
	
	@PersistenceUnit
	private EntityManagerFactory emf;

	private EntityManager emForJDBC;
	
	private static final String AIRWAY_BILL_COUNT_SQL = "select count(*) as totalAirwayBill " +
																								"from log_airway_bill " +
																								"where airway_bill_id = ?";
	
	private static final String SERVICE_SQL = "select service from log_airway_bill where airway_bill_id=?";
	
	private static final String AIRWAY_BILL_SELECT_RELATION_SQL = "select invoice_approval_status_id, " +
																														    "activity_approval_status_id, " +
																														    "logistics_provider_id, " +
																														    "distribution_cart_id, " +
																														    "order_item_id " +
																												  "from log_airway_bill " +
																												  "where airway_bill_id = ?"; 
	
	private static final String AIRWAY_BILL_MERGE_SQL = "update log_airway_bill set " +
																		   "activity_approved_by_user_id = ?," +
																		   "activity_file_name_and_loc = ?," +
																		   "activity_result_status = ?," +
																		   "actual_pickup_date = ?," +
																		   "address = ?," +
																		   "airway_bill_number = ?," +
																		   "airway_bill_pickup_date_time = ?," +
																		   "airway_bill_timestamp = ?," +
																		   "consignee = ?," +
																		   "contact_person = ?," +
																		   "content = ?," +
																		   "date_of_return = ?," +
																		   "delivery_order = ?," +
																		   "dest_code = ?," +
																		   "destination = ?," +
																		   "gdn_reference = ?," +
																		   "gift_wrap_charge = ?," +
																		   "insurance_charge = ?," +
																		   "insured_amount = ?," +
																		   "invoice_approved_by_user_id = ?," +
																		   "invoice_file_name_and_loc = ?," +
																		   "invoice_result_status = ?," +
																		   "mta_data = ?," +
																		   "note_return = ?," +
																		   "note_undelivered = ?," +
																		   "num_packages = ?," +
																		   "origin = ?," +
																		   "other_charge = ?," +
																		   "package_weight = ?," +
																		   "price_per_kg = ?," +
																		   "provider_total_charge = ?," +
																		   "received = ?," +
																		   "recipient = ?," +
																		   "relation = ?," +
																		   "return = ?," +
																		   "service = ?," +
																		   "shipper = ?," +
																		   "status = ?," +
																		   "tariff = ?," +
																		   "total_charge = ?," +
																		   "tracking_number = ?," +
																		   "type = ?," +
																		   "undelivered = ?," +
																		   "zip = ?," +
																		   "kpi_pickup_perf_clocked = ?," +
																		   "kpi_delivery_perf_clocked = ?," +
																		   "kpi_invoice_accuracy_clocked = ?," +
																		   "invoice_approval_status_id = ?," +
																		   "activity_approval_status_id = ?," +
																		   "logistics_provider_id = ?," +																		  
																		   "order_item_id = ?," +
																		   "distribution_cart_id = ?, " +
																		   "invoice_airwaybill_record_id = ? " +
																		"where airway_bill_id = ?";
	
	private static final String AIRWAY_BILL_MERGE_SQL_BP = "update log_airway_bill set " +
	   "activity_approved_by_user_id = ?," +
	   "activity_file_name_and_loc = ?," +
	   "activity_result_status = ?," +
	   "actual_pickup_date = ?," +
	   "address = ?," +
	   "airway_bill_number = ?," +
	   "airway_bill_pickup_date_time = ?," +
	   "airway_bill_timestamp = ?," +
	   "consignee = ?," +
	   "contact_person = ?," +
	   "content = ?," +
	   "date_of_return = ?," +
	   "delivery_order = ?," +
	   "dest_code = ?," +
	   "destination = ?," +
	   "gdn_reference = ?," +
	   "gift_wrap_charge = ?," +
	   "insurance_charge = ?," +
	   "insured_amount = ?," +
	   "invoice_approved_by_user_id = ?," +
	   "invoice_file_name_and_loc = ?," +
	   "invoice_result_status = ?," +
	   "mta_data = ?," +
	   "note_return = ?," +
	   "note_undelivered = ?," +
	   "num_packages = ?," +
	   "origin = ?," +
	   "other_charge = ?," +
	   "package_weight = ?," +
	   "price_per_kg = ?," +
	   "provider_total_charge = ?," +
	   "received = ?," +
	   "recipient = ?," +
	   "relation = ?," +
	   "return = ?," +
	   "service = ?," +
	   "shipper = ?," +
	   "status = ?," +
	   "tariff = ?," +
	   "total_charge = ?," +
	   "tracking_number = ?," +
	   "type = ?," +
	   "undelivered = ?," +
	   "zip = ?," +
	   "kpi_pickup_perf_clocked = ?," +
	   "kpi_delivery_perf_clocked = ?," +
	   "kpi_invoice_accuracy_clocked = ?," +
	   "invoice_approval_status_id = ?," +
	   "activity_approval_status_id = ?," +
	   "logistics_provider_id = ?," +																		  
	   "order_item_id = ? "+
	"where airway_bill_id = ?";
	
	/**
	 * Default constructor.
	 */
	public LogAirwayBillSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.facade.LogAirwayBillSessionEJBBean");
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
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<LogAirwayBill> queryByRange(String jpqlStmt, int firstResult,
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
		List<LogAirwayBill> returnList = (List<LogAirwayBill>)query.getResultList();
		this.bindingArray = null;
		
		for (LogAirwayBill logAirwayBill2 :returnList) {
			if(logAirwayBill2.getGdnReference() != null && 
				!logAirwayBill2.getGdnReference().equals("") && 
					(!logAirwayBill2.getVenOrderItem().getVenOrderStatus().getOrderStatusCode().equals("C") ||
					 !logAirwayBill2.getVenOrderItem().getVenOrderStatus().getOrderStatusCode().equals("FP"))){
				
				Hibernate.initialize(logAirwayBill2.getVenDistributionCart());
				
			}
		}
		
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRange() duration:" + duration + "ms");
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public  List<LogAirwayBill> queryByRangeWithNativeQuery(String jpqlStmt, int firstResult,
			int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("queryByRangeWithNativeQuery()");
		Query query = null;
		try {
			query = em.createNativeQuery(jpqlStmt,com.gdn.venice.persistence.LogAirwayBill.class);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.createNativeQuery():"
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
		
		 List<LogAirwayBill> returnList = (List<LogAirwayBill>)query.getResultList();

		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("queryByRangeWithNativeQuery() duration:" + duration + "ms");
		return returnList;
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#persistLogAirwayBill(com
	 * .gdn.venice.persistence.LogAirwayBill)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogAirwayBill persistLogAirwayBill(LogAirwayBill logAirwayBill) {
		Long startTime = System.currentTimeMillis();
		_log.debug("persistLogAirwayBill()");

		// Call the onPrePersist() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPrePersist(logAirwayBill)) {
				_log.error("An onPrePersist callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPrePersist callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		LogAirwayBill existingLogAirwayBill = null;

		if (logAirwayBill != null && logAirwayBill.getAirwayBillId() != null) {
			_log.debug("persistLogAirwayBill:em.find()");
			try {
				existingLogAirwayBill = em.find(LogAirwayBill.class,
						logAirwayBill.getAirwayBillId());
			} catch (Exception e) {
				_log.error("An exception occured when calling em.find():"
						+ e.getMessage());
				throw new EJBException(e);
			}
		}
		
		if (existingLogAirwayBill == null) {
			_log.debug("persistLogAirwayBill:em.persist()");
			try {
				em.persist(logAirwayBill);
			} catch (Exception e) {
				_log.error("An exception occured when calling em.persist():"
						+ e.getMessage());
				throw new EJBException(e);
			}
			_log.debug("persistLogAirwayBill:em.flush()");
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
				if (!this._callback.onPostPersist(logAirwayBill)) {
					_log.error("An onPostPersist callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostPersist callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("persistLogAirwayBill() duration:" + duration + "ms");
			
			return logAirwayBill;
		} else {
			throw new EJBException("LogAirwayBill exists!. LogAirwayBill = "
					+ logAirwayBill.getAirwayBillId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#persistLogAirwayBillList
	 * (java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogAirwayBill> persistLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList) {
		_log.debug("persistLogAirwayBillList()");
		Iterator i = logAirwayBillList.iterator();
		while (i.hasNext()) {
			this.persistLogAirwayBill((LogAirwayBill) i.next());
		}
		return (ArrayList<LogAirwayBill>)logAirwayBillList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#mergeLogAirwayBill(com.
	 * gdn.venice.persistence.LogAirwayBill)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LogAirwayBill mergeLogAirwayBill(LogAirwayBill logAirwayBill) {
		Long startTime = System.currentTimeMillis();
		_log.debug("mergeLogAirwayBill()");

		if(emForJDBC == null)
			emForJDBC = emf.createEntityManager();
	
		LogAirwayBill existingAirwayBill = new LogAirwayBill();
		
		// Call the onPreMerge() callback and throw an exception if it fails
		if (this._callback != null) {
			LogAirwayBillSessionEJBCallback callback = new LogAirwayBillSessionEJBCallback();
			if (!callback.onPreMerge(logAirwayBill, emf.createEntityManager(), existingAirwayBill)) {
				_log.error("An onPreMerge callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreMerge callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
		
		int totalAirwayBill = 0;
		if (logAirwayBill.getAirwayBillId() != null)
			_log.debug("mergeLogAirwayBill:em.find()");

		Connection conn =  (Connection) ((EntityManagerImpl)emForJDBC).getSession().connection();
		
		try {
			PreparedStatement psAirwayBill = conn.prepareStatement(AIRWAY_BILL_COUNT_SQL);
			psAirwayBill.setLong(1, logAirwayBill.getAirwayBillId());
			ResultSet rsAirwayBill = psAirwayBill.executeQuery();
			
			rsAirwayBill.next();
			
			totalAirwayBill = rsAirwayBill.getInt("totalAirwayBill");
			
		} catch (SQLException exSQL) {
			_log.error(exSQL.getMessage(), exSQL);
		}
		
		
		if (totalAirwayBill == 0) {
			return this.persistLogAirwayBill(logAirwayBill);
		} else {
			_log.debug("mergeLogAirwayBill:em.merge()");
			
			try {
				
				PreparedStatement psAirwayBillRelationship = conn.prepareStatement(AIRWAY_BILL_SELECT_RELATION_SQL);
				psAirwayBillRelationship.setLong(1, logAirwayBill.getAirwayBillId());
				
				ResultSet rsAirwayBillRelationship = psAirwayBillRelationship.executeQuery();
				rsAirwayBillRelationship.next();
				
				//get order item status for BP
				PreparedStatement psService = conn.prepareStatement(SERVICE_SQL);
				psService.setLong(1, logAirwayBill.getAirwayBillId());
				
				ResultSet rsService = psService.executeQuery();
				rsService.next();
								
				String service = rsService.getString("service");
				_log.debug("service: "+service);
				
				psService.close();
				rsService.close();
				
				PreparedStatement psAirwayBill=null;
				
				//if order item status BP don't update distribution cart, because it is null.
				if(service!=null && (service.equals("Big Product") || service.equals("BOPIS"))){
					_log.debug("service is BP or BOPIS, use AIRWAY_BILL_MERGE_SQL_BP query.");
					psAirwayBill = conn.prepareStatement(AIRWAY_BILL_MERGE_SQL_BP);
				}else{
					psAirwayBill = conn.prepareStatement(AIRWAY_BILL_MERGE_SQL);
				}
																				
				psAirwayBill.setString(1, logAirwayBill.getActivityApprovedByUserId());
				psAirwayBill.setString(2, logAirwayBill.getActivityFileNameAndLoc());
				psAirwayBill.setString(3, logAirwayBill.getActivityResultStatus());
				psAirwayBill.setDate(4, logAirwayBill.getActualPickupDate() != null ? new java.sql.Date(logAirwayBill.getActualPickupDate().getTime()):null);
				psAirwayBill.setString(5, logAirwayBill.getAddress());
				psAirwayBill.setString(6, logAirwayBill.getAirwayBillNumber());
				psAirwayBill.setTimestamp(7, logAirwayBill.getAirwayBillPickupDateTime());
				psAirwayBill.setTimestamp(8, logAirwayBill.getAirwayBillTimestamp());
				psAirwayBill.setString(9, logAirwayBill.getConsignee());
				psAirwayBill.setString(10, logAirwayBill.getContactPerson());
				psAirwayBill.setString(11, logAirwayBill.getContent());
				psAirwayBill.setDate(12, logAirwayBill.getDateOfReturn() != null ? new java.sql.Date(logAirwayBill.getDateOfReturn().getTime()):null);
				psAirwayBill.setString(13, logAirwayBill.getDeliveryOrder());
				psAirwayBill.setString(14, logAirwayBill.getDestCode());
				psAirwayBill.setString(15, logAirwayBill.getDestination());
				psAirwayBill.setString(16, logAirwayBill.getGdnReference());
				psAirwayBill.setBigDecimal(17, logAirwayBill.getGiftWrapCharge());
				psAirwayBill.setBigDecimal(18, logAirwayBill.getInsuranceCharge());
				psAirwayBill.setBigDecimal(19, logAirwayBill.getInsuredAmount());
				psAirwayBill.setString(20, logAirwayBill.getInvoiceApprovedByUserId());
				psAirwayBill.setString(21, logAirwayBill.getInvoiceFileNameAndLoc());
				psAirwayBill.setString(22, logAirwayBill.getInvoiceResultStatus());
				if(logAirwayBill.getMtaData()!=null)
					psAirwayBill.setBoolean(23, logAirwayBill.getMtaData());
				else
					psAirwayBill.setBoolean(23, new Boolean(false));
				psAirwayBill.setString(24, logAirwayBill.getNoteReturn());
				psAirwayBill.setString(25, logAirwayBill.getNoteUndelivered());
				if(logAirwayBill.getNumPackages() != null)
					psAirwayBill.setInt(26, logAirwayBill.getNumPackages());
				else
					psAirwayBill.setNull(26, java.sql.Types.INTEGER);
				psAirwayBill.setString(27, logAirwayBill.getOrigin());
				psAirwayBill.setBigDecimal(28, logAirwayBill.getOtherCharge());
				psAirwayBill.setBigDecimal(29, logAirwayBill.getPackageWeight());
				psAirwayBill.setBigDecimal(30, logAirwayBill.getPricePerKg());
				psAirwayBill.setBigDecimal(31, logAirwayBill.getProviderTotalCharge());
				psAirwayBill.setDate(32, logAirwayBill.getReceived()!=null?new java.sql.Date(logAirwayBill.getReceived().getTime()):null);
				psAirwayBill.setString(33, logAirwayBill.getRecipient());
				psAirwayBill.setString(34, logAirwayBill.getRelation());
				psAirwayBill.setString(35, logAirwayBill.getReturn_());
				psAirwayBill.setString(36, logAirwayBill.getService());
				psAirwayBill.setString(37, logAirwayBill.getShipper());
				psAirwayBill.setString(38, logAirwayBill.getStatus());
				psAirwayBill.setString(39, logAirwayBill.getTariff());
				psAirwayBill.setBigDecimal(40, logAirwayBill.getTotalCharge());
				psAirwayBill.setString(41, logAirwayBill.getTrackingNumber());
				psAirwayBill.setString(42, logAirwayBill.getType());
				psAirwayBill.setDate(43, logAirwayBill.getUndelivered()!=null?new java.sql.Date(logAirwayBill.getUndelivered().getTime()):null);
				psAirwayBill.setString(44, logAirwayBill.getZip());
				if(logAirwayBill.getKpiPickupPerfClocked()!=null)
					psAirwayBill.setBoolean(45, logAirwayBill.getKpiPickupPerfClocked());
				else
					psAirwayBill.setBoolean(45, new Boolean(false));
				if(logAirwayBill.getKpiDeliveryPerfClocked()!=null)
					psAirwayBill.setBoolean(46, logAirwayBill.getKpiDeliveryPerfClocked());
				else
					psAirwayBill.setBoolean(46, new Boolean(false));
				if(logAirwayBill.getKpiInvoiceAccuracyClocked()!=null)
					psAirwayBill.setBoolean(47, logAirwayBill.getKpiInvoiceAccuracyClocked());
				else
					psAirwayBill.setBoolean(47, new Boolean(false));				
				if(logAirwayBill.getLogApprovalStatus1()!=null && logAirwayBill.getLogApprovalStatus1().getApprovalStatusId() != null)
					psAirwayBill.setLong(48, logAirwayBill.getLogApprovalStatus1().getApprovalStatusId());
				else
					psAirwayBill.setLong(48, rsAirwayBillRelationship.getLong("invoice_approval_status_id"));
				
				if(logAirwayBill.getLogApprovalStatus2()!=null && logAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != null)
					psAirwayBill.setLong(49, logAirwayBill.getLogApprovalStatus2().getApprovalStatusId());
				else
					psAirwayBill.setLong(49, rsAirwayBillRelationship.getLong("activity_approval_status_id"));
				
				psAirwayBill.setLong(50, logAirwayBill.getLogLogisticsProvider().getLogisticsProviderId());			
				
				if(logAirwayBill.getVenOrderItem()!=null && logAirwayBill.getVenOrderItem().getOrderItemId() != null)
					psAirwayBill.setLong(51, logAirwayBill.getVenOrderItem().getOrderItemId());
				else
					psAirwayBill.setLong(51, rsAirwayBillRelationship.getLong("order_item_id"));
				
				if(service.equals("Big Product") || service.equals("BOPIS")){
					_log.debug("service is BP or BOPIS, don't update distribution cart.");
					psAirwayBill.setLong(52, logAirwayBill.getAirwayBillId());
				}else{
						psAirwayBill.setLong(52, rsAirwayBillRelationship.getLong("distribution_cart_id"));
					
						if(logAirwayBill.getLogInvoiceAirwaybillRecord() != null && logAirwayBill.getLogInvoiceAirwaybillRecord().getInvoiceAirwaybillRecordId() != null)
							psAirwayBill.setLong(53, logAirwayBill.getLogInvoiceAirwaybillRecord().getInvoiceAirwaybillRecordId());
						else
							psAirwayBill.setNull(53, java.sql.Types.BIGINT);
						psAirwayBill.setLong(54, logAirwayBill.getAirwayBillId());
				}
												
				psAirwayBill.executeUpdate();
				psAirwayBill.close();
				
				rsAirwayBillRelationship.close();
				psAirwayBillRelationship.close();
				
			} catch (Exception e) {
				_log.error("An exception occured when calling em.merge():"
						+ e.getMessage());
				throw new EJBException(e);
			}

//			LogAirwayBill newobject = em.find(LogAirwayBill.class,
//					logAirwayBill.getAirwayBillId());
//			_log.debug("mergeLogAirwayBill():em.refresh");
//			try {
//				em.refresh(newobject);
//			} catch (Exception e) {
//				_log.error("An exception occured when calling em.refresh():"
//						+ e.getMessage());
//				throw new EJBException(e);
//			}

			// Call the onPostMerge() callback and throw an exception if it fails
			if (this._callback != null) {
				LogAirwayBillSessionEJBCallback callback = new LogAirwayBillSessionEJBCallback();
//				if (!this._callback.onPostMerge(newobject, em)) {
				if (!callback.onPostMerge(logAirwayBill, emf.createEntityManager(), existingAirwayBill)) {
					_log.error("An onPostMerge callback operation failed for:"
							+ this._sessionCallbackClassName);
					throw new EJBException(
							"An onPostMerge callback operation failed for:"
									+ this._sessionCallbackClassName);
				}
			}	
			
			try {
				conn.close();
			} catch (SQLException e) {
				_log.error(e.getMessage(), e);
			}
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("mergeLogAirwayBill() duration:" + duration + "ms");
						
			return logAirwayBill;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#mergeLogAirwayBillList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ArrayList<LogAirwayBill> mergeLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList) {
		_log.debug("mergeLogAirwayBillList()");
		Iterator i = logAirwayBillList.iterator();
		while (i.hasNext()) {
			this.mergeLogAirwayBill((LogAirwayBill) i.next());
		}
		return (ArrayList<LogAirwayBill>)logAirwayBillList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#removeLogAirwayBill(com.
	 * gdn.venice.persistence.LogAirwayBill)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogAirwayBill(LogAirwayBill logAirwayBill) {
		Long startTime = System.currentTimeMillis();
		_log.debug("removeLogAirwayBill()");

		// Call the onPreRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPreRemove(logAirwayBill)) {
				_log.error("An onPreRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPreRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}
	
		_log.debug("removeLogAirwayBill:em.find()");
		logAirwayBill = em.find(LogAirwayBill.class, logAirwayBill.getAirwayBillId());
		
		try {
			_log.debug("removeLogAirwayBill:em.remove()");
			em.remove(logAirwayBill);
		} catch (Exception e) {
			_log.error("An exception occured when calling em.remove():"
					+ e.getMessage());
			throw new EJBException(e);
		}
		
		// Call the onPostRemove() callback and throw an exception if it fails
		if (this._callback != null) {
			if (!this._callback.onPostRemove(logAirwayBill)) {
				_log.error("An onPostRemove callback operation failed for:"
						+ this._sessionCallbackClassName);
				throw new EJBException(
						"An onPostRemove callback operation failed for:"
								+ this._sessionCallbackClassName);
			}
		}			

		_log.debug("removeLogAirwayBill:em.flush()");
		em.flush();
		em.clear();
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("removeLogAirwayBill() duration:" + duration + "ms");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#removeLogAirwayBillList(
	 * java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLogAirwayBillList(List<LogAirwayBill> logAirwayBillList) {
		_log.debug("removeLogAirwayBillList()");
		Iterator i = logAirwayBillList.iterator();
		while (i.hasNext()) {
			this.removeLogAirwayBill((LogAirwayBill) i.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#findByLogAirwayBillLike(
	 * com.gdn.venice.persistence.LogAirwayBill, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<LogAirwayBill> findByLogAirwayBillLike(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogAirwayBillLike()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logAirwayBill);
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
			List<LogAirwayBill> logAirwayBillList = this.queryByRange(stmt, firstResult, maxResults);		
			
			for (LogAirwayBill logAirwayBill2 :logAirwayBillList) {
				if(logAirwayBill2.getGdnReference() != null && 
					!logAirwayBill2.getGdnReference().equals("") && 
						(!logAirwayBill2.getVenOrderItem().getVenOrderStatus().getOrderStatusCode().equals("C") ||
						 !logAirwayBill2.getVenOrderItem().getVenOrderStatus().getOrderStatusCode().equals("FP"))){
					
					Hibernate.initialize(logAirwayBill2.getVenDistributionCart());
					
				}
			}
			
			Long endTime = System.currentTimeMillis();
			Long duration = startTime - endTime;
			_log.debug("findByLogAirwayBillLike() duration:" + duration + "ms");
			return logAirwayBillList;			
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
	 * com.gdn.venice.facade.LogAirwayBillSessionEJBRemote#findByLogAirwayBillLikeFR(
	 * com.gdn.venice.persistence.LogAirwayBill, int, int)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FinderReturn findByLogAirwayBillLikeFR(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("findByLogAirwayBillLikeFR()");
		JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(logAirwayBill);
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
			_log.debug("findByLogAirwayBillLike() duration:" + duration + "ms");
			return fr;			
		}else{
			String errMsg = "A query has been initiated with null criteria.";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}		
	}

	@Override
	public String countQueryByRange(String jpqlStmt, int firstResult,
			int maxResults) {
		Long startTime = System.currentTimeMillis();
		_log.debug("countQueryByRange()");

		Query query = null;
		try {
			query = em.createNativeQuery(jpqlStmt);
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
		String returnList = query.getSingleResult().toString();
		this.bindingArray = null;
		
		Long endTime = System.currentTimeMillis();
		Long duration = startTime - endTime;
		_log.debug("countQueryByRange() duration:" + duration + "ms");
		return returnList;
	}

}
