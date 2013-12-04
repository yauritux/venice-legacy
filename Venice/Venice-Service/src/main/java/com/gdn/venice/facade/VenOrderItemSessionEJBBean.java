package com.gdn.venice.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import com.gdn.venice.facade.callback.SessionCallback;
import com.gdn.venice.facade.callback.VenOrderItemSessionEJBCallback;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.VenOrderItem;

/**
 * Session Bean implementation class VenOrderItemSessionEJBBean
 *
 * <p> <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p> <b>version:</b> 1.0 <p> <b>since:</b> 2011
 *
 */
@Stateless(mappedName = "VenOrderItemSessionEJBBean")
public class VenOrderItemSessionEJBBean implements VenOrderItemSessionEJBRemote,
        VenOrderItemSessionEJBLocal {

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
    private static final String COUNT_ORDER_ITEM_SQL = "select count(*) as totalOrderItem from ven_order_item where order_item_id = ?";
    private static final String MERGE_ORDER_ITEM_SQL = "update ven_order_item set etd = ?,  gift_card_flag = ?,  gift_card_note = ?,  gift_wrap_price = ?,  gift_wrap_flag = ?,  insurance_cost = ?,  max_est_date = ?,  merchant_settlement_flag = ?,  min_est_date = ?,  package_count = ?,  price = ?,  quantity = ?,  salt_code = ?, 	 shipping_cost = ?,  shipping_weight = ?,  special_handling_instructions = ?,	 total = ?,  logistics_price_per_kg = ?,  shipping_address_id = ?,  product_id = ?, order_status_id = ?,  recipient_id = ?,  logistics_service_id = ?, sales_batch_status = ? where order_item_id = ? ";

    /**
     * Default constructor.
     */
    public VenOrderItemSessionEJBBean() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory
                .getLog4JLogger("com.gdn.venice.facade.VenOrderItemSessionEJBBean");
        // If the configuration is successful then instantiate the callback
        if (this.configure()) {
            this.instantiateTriggerCallback();
        }

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
            @SuppressWarnings({"rawtypes"})
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
                && !_sessionCallbackClassName.isEmpty()) {
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
        }
        return Boolean.TRUE;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#queryByRange(java.lang
     * .String, int, int)
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public List<VenOrderItem> queryByRange(String jpqlStmt, int firstResult,
            int maxResults) {
        Long startTime = System.currentTimeMillis();
        _log.debug("queryByRange()");

        Query query = null;
        try {
            query = em.createQuery(jpqlStmt);
            if (this.bindingArray != null) {
                for (int i = 0; i < bindingArray.length; ++i) {
                    if (bindingArray[i] != null) {
                        query.setParameter(i + 1, bindingArray[i]);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("An exception occured when calling em.createQuery():"
                    + e.getMessage(), e);
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
                    + e.getMessage(), e);
            throw new EJBException(e);
        }
        List<VenOrderItem> returnList = (List<VenOrderItem>) query.getResultList();
        this.bindingArray = null;

        if (returnList.size() > 0) {
            Hibernate.initialize(returnList.get(0).getVenSettlementRecords());
            for (VenOrderItem venOrderItem : returnList) {
                Hibernate.initialize(venOrderItem.getVenMerchantProduct().getVenProductCategories());
                Hibernate.initialize(venOrderItem.getVenOrder().getVenCustomer().getVenParty().getVenPartyAddresses());
                Hibernate.initialize(venOrderItem.getVenOrder().getVenCustomer().getVenParty().getVenContactDetails());
            }
        }
        Long endTime = System.currentTimeMillis();
        Long duration = startTime - endTime;
        _log.debug("queryByRange() duration:" + duration + "ms");
        return returnList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#persistVenOrderItem(com
     * .gdn.venice.persistence.VenOrderItem)
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public VenOrderItem persistVenOrderItem(VenOrderItem venOrderItem) {
        Long startTime = System.currentTimeMillis();
        _log.debug("persistVenOrderItem()");

        // Call the onPrePersist() callback and throw an exception if it fails
        if (this._callback != null) {
            if (!this._callback.onPrePersist(venOrderItem)) {
                _log.error("An onPrePersist callback operation failed for:"
                        + this._sessionCallbackClassName);
                throw new EJBException(
                        "An onPrePersist callback operation failed for:"
                        + this._sessionCallbackClassName);
            }
        }

        VenOrderItem existingVenOrderItem = null;

        if (venOrderItem != null && venOrderItem.getOrderItemId() != null) {
            _log.debug("persistVenOrderItem:em.find()");
            try {
                existingVenOrderItem = em.find(VenOrderItem.class,
                        venOrderItem.getOrderItemId());
            } catch (Exception e) {
                _log.error("An exception occured when calling em.find():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            }
        }

        if (existingVenOrderItem == null) {
            _log.debug("persistVenOrderItem:em.persist()");
            try {
                em.persist(venOrderItem);
            } catch (Exception e) {
                _log.error("An exception occured when calling em.persist():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            }
            _log.debug("persistVenOrderItem:em.flush()");
            try {
                em.flush();
                em.clear();
            } catch (Exception e) {
                _log.error("An exception occured when calling em.flush():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            }

            // Call the onPostPersist() callback and throw an exception if it fails
            if (this._callback != null) {
                if (!this._callback.onPostPersist(venOrderItem)) {
                    _log.error("An onPostPersist callback operation failed for:"
                            + this._sessionCallbackClassName);
                    throw new EJBException(
                            "An onPostPersist callback operation failed for:"
                            + this._sessionCallbackClassName);
                }
            }

            Long endTime = System.currentTimeMillis();
            Long duration = startTime - endTime;
            _log.debug("persistVenOrderItem() duration:" + duration + "ms");

            return venOrderItem;
        } else {
            throw new EJBException("VenOrderItem exists!. VenOrderItem = "
                    + venOrderItem.getOrderItemId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#persistVenOrderItemList
     * (java.util.List)
     */
    @Override
    @SuppressWarnings("rawtypes")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ArrayList<VenOrderItem> persistVenOrderItemList(
            List<VenOrderItem> venOrderItemList) {
        _log.debug("persistVenOrderItemList()");
        Iterator i = venOrderItemList.iterator();
        while (i.hasNext()) {
            this.persistVenOrderItem((VenOrderItem) i.next());
        }
        return (ArrayList<VenOrderItem>) venOrderItemList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#mergeVenOrderItem(com.
     * gdn.venice.persistence.VenOrderItem)
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public VenOrderItem mergeVenOrderItem(VenOrderItem venOrderItem) {
        Long startTime = System.currentTimeMillis();
        _log.debug("mergeVenOrderItem()");

        if (emForJDBC == null) {
            emForJDBC = emf.createEntityManager();
        }

        // Call the onPreMerge() callback and throw an exception if it fails
        if (this._callback != null) {
            VenOrderItemSessionEJBCallback callback = new VenOrderItemSessionEJBCallback();
            if (!callback.onPreMerge(venOrderItem, emForJDBC)) {
                _log.error("An onPreMerge callback operation failed for:"
                        + this._sessionCallbackClassName);
                throw new EJBException(
                        "An onPreMerge callback operation failed for:"
                        + this._sessionCallbackClassName);
            }
        }

        int totalOrderItem = 0;
        if (venOrderItem.getOrderItemId() != null) {
            _log.debug("mergeVenOrderItem:em.find()");
        }

//		long start = System.currentTimeMillis();
        PreparedStatement psOrderItem = null;
        ResultSet rsOrderItem = null;
        Connection conn = ((EntityManagerImpl) emForJDBC).getSession().connection();
        try {
            psOrderItem = conn.prepareStatement(COUNT_ORDER_ITEM_SQL);
            psOrderItem.setLong(1, venOrderItem.getOrderItemId());

            rsOrderItem = psOrderItem.executeQuery();
            rsOrderItem.next();

            totalOrderItem = rsOrderItem.getInt("totalOrderItem");

        } catch (SQLException exSQL) {
            _log.error(exSQL.getMessage(), exSQL);
        } finally {
            try {
                if (psOrderItem != null) {
                    psOrderItem.close();
                }
                if (rsOrderItem != null) {
                    rsOrderItem.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        PreparedStatement psMergeOrderItem = null;

        if (totalOrderItem == 0) {
            return this.persistVenOrderItem(venOrderItem);
        } else {
            _log.debug("mergeVenOrderItem:em.merge()");
            try {

                psMergeOrderItem = conn.prepareStatement(MERGE_ORDER_ITEM_SQL);

                if (venOrderItem.getEtd() != null) {
                    psMergeOrderItem.setInt(1, venOrderItem.getEtd());
                } else {
                    psMergeOrderItem.setNull(1, Types.INTEGER);
                }

                psMergeOrderItem.setBoolean(2, venOrderItem.getGiftCardFlag());
                if (venOrderItem.getGiftCardNote() != null) {
                    psMergeOrderItem.setString(3, venOrderItem.getGiftCardNote());
                } else {
                    psMergeOrderItem.setNull(3, Types.VARCHAR);
                }
                psMergeOrderItem.setBigDecimal(4, venOrderItem.getGiftWrapPrice());
                psMergeOrderItem.setBoolean(5, venOrderItem.getGiftWrapFlag());
                psMergeOrderItem.setBigDecimal(6, venOrderItem.getInsuranceCost());

                if (venOrderItem.getMaxEstDate() != null) {
                    psMergeOrderItem.setTimestamp(7, venOrderItem.getMaxEstDate());
                } else {
                    psMergeOrderItem.setNull(7, Types.TIMESTAMP);
                }

                if (venOrderItem.getMerchantSettlementFlag() != null) {
                    psMergeOrderItem.setBoolean(8, venOrderItem.getMerchantSettlementFlag());
                } else {
                    psMergeOrderItem.setNull(8, Types.BOOLEAN);
                }

                if (venOrderItem.getMinEstDate() != null) {
                    psMergeOrderItem.setTimestamp(9, venOrderItem.getMinEstDate());
                } else {
                    psMergeOrderItem.setNull(9, Types.TIMESTAMP);
                }

                if (venOrderItem.getPackageCount() != null) {
                    psMergeOrderItem.setInt(10, venOrderItem.getPackageCount());
                } else {
                    psMergeOrderItem.setNull(10, Types.INTEGER);
                }
                psMergeOrderItem.setBigDecimal(11, venOrderItem.getPrice());
                psMergeOrderItem.setInt(12, venOrderItem.getQuantity());
                if (venOrderItem.getSaltCode() != null) {
                    psMergeOrderItem.setString(13, venOrderItem.getSaltCode());
                } else {
                    psMergeOrderItem.setNull(13, Types.VARCHAR);
                }
                psMergeOrderItem.setBigDecimal(14, venOrderItem.getShippingCost());
                psMergeOrderItem.setBigDecimal(15, venOrderItem.getShippingWeight());
                if (venOrderItem.getSpecialHandlingInstructions() != null) {
                    psMergeOrderItem.setString(16, venOrderItem.getSpecialHandlingInstructions());
                } else {
                    psMergeOrderItem.setNull(16, Types.VARCHAR);
                }
                psMergeOrderItem.setBigDecimal(17, venOrderItem.getTotal());
                psMergeOrderItem.setBigDecimal(18, venOrderItem.getLogisticsPricePerKg());
                psMergeOrderItem.setLong(19, venOrderItem.getVenAddress().getAddressId());
                psMergeOrderItem.setLong(20, venOrderItem.getVenMerchantProduct().getProductId());
                psMergeOrderItem.setLong(21, venOrderItem.getVenOrderStatus().getOrderStatusId());
                psMergeOrderItem.setLong(22, venOrderItem.getVenRecipient().getRecipientId());
                psMergeOrderItem.setLong(23, venOrderItem.getLogLogisticService().getLogisticsServiceId());
                if (venOrderItem.getSalesBatchStatus() != null) {
                    psMergeOrderItem.setString(24, venOrderItem.getSalesBatchStatus());
                } else {
                    psMergeOrderItem.setNull(24, Types.VARCHAR);
                }
                psMergeOrderItem.setLong(25, venOrderItem.getOrderItemId());

                psMergeOrderItem.executeUpdate();

            } catch (Exception e) {
                _log.error("An exception occured when calling em.merge():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            } finally {
                try {
                    psMergeOrderItem.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            _log.debug("mergeVenOrderItem:em.flush()");
            try {
//				start = System.currentTimeMillis();
//				em.flush();
//				em.clear();
//				_log.debug("em.flush(); em.clear(); : " + (System.currentTimeMillis() - start) + " ms");
            } catch (Exception e) {
                _log.error("An exception occured when calling em.flush():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            }

//			VenOrderItem newobject = em.find(VenOrderItem.class,
//					venOrderItem.getOrderItemId());
//			_log.debug("mergeVenOrderItem():em.refresh");
//			try {
//				em.refresh(newobject);
//			} catch (Exception e) {
//				_log.error("An exception occured when calling em.refresh():"
//						+ e.getMessage());
//				throw new EJBException(e);
//			}
//			
//			// Call the onPostMerge() callback and throw an exception if it fails
//			if (this._callback != null) {
//				if (!this._callback.onPostMerge(newobject)) {
//					_log.error("An onPostMerge callback operation failed for:"
//							+ this._sessionCallbackClassName);
//					throw new EJBException(
//							"An onPostMerge callback operation failed for:"
//									+ this._sessionCallbackClassName);
//				}
//			}	

            try {
                conn.close();
            } catch (SQLException e) {
                _log.error(e.getMessage(), e);
                throw new EJBException(e);
            }
            Long endTime = System.currentTimeMillis();
            Long duration = startTime - endTime;
            _log.debug("mergeVenOrderItem() duration:" + duration + "ms");

            return venOrderItem;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#mergeVenOrderItemList(
     * java.util.List)
     */
    @Override
    @SuppressWarnings("rawtypes")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ArrayList<VenOrderItem> mergeVenOrderItemList(
            List<VenOrderItem> venOrderItemList) {
        _log.debug("mergeVenOrderItemList()");
        Iterator i = venOrderItemList.iterator();
        while (i.hasNext()) {
            this.mergeVenOrderItem((VenOrderItem) i.next());
        }
        return (ArrayList<VenOrderItem>) venOrderItemList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#removeVenOrderItem(com.
     * gdn.venice.persistence.VenOrderItem)
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeVenOrderItem(VenOrderItem venOrderItem) {
        Long startTime = System.currentTimeMillis();
        _log.debug("removeVenOrderItem()");

        // Call the onPreRemove() callback and throw an exception if it fails
        if (this._callback != null) {
            if (!this._callback.onPreRemove(venOrderItem)) {
                _log.error("An onPreRemove callback operation failed for:"
                        + this._sessionCallbackClassName);
                throw new EJBException(
                        "An onPreRemove callback operation failed for:"
                        + this._sessionCallbackClassName);
            }
        }

        _log.debug("removeVenOrderItem:em.find()");
        venOrderItem = em.find(VenOrderItem.class, venOrderItem.getOrderItemId());

        try {
            _log.debug("removeVenOrderItem:em.remove()");
            em.remove(venOrderItem);
        } catch (Exception e) {
            _log.error("An exception occured when calling em.remove():"
                    + e.getMessage(), e);
            throw new EJBException(e);
        }

        // Call the onPostRemove() callback and throw an exception if it fails
        if (this._callback != null) {
            if (!this._callback.onPostRemove(venOrderItem)) {
                _log.error("An onPostRemove callback operation failed for:"
                        + this._sessionCallbackClassName);
                throw new EJBException(
                        "An onPostRemove callback operation failed for:"
                        + this._sessionCallbackClassName);
            }
        }

        _log.debug("removeVenOrderItem:em.flush()");
        em.flush();
        em.clear();
        Long endTime = System.currentTimeMillis();
        Long duration = startTime - endTime;
        _log.debug("removeVenOrderItem() duration:" + duration + "ms");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#removeVenOrderItemList(
     * java.util.List)
     */
    @Override
    @SuppressWarnings("rawtypes")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeVenOrderItemList(List<VenOrderItem> venOrderItemList) {
        _log.debug("removeVenOrderItemList()");
        Iterator i = venOrderItemList.iterator();
        while (i.hasNext()) {
            this.removeVenOrderItem((VenOrderItem) i.next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#findByVenOrderItemLike(
     * com.gdn.venice.persistence.VenOrderItem, int, int)
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<VenOrderItem> findByVenOrderItemLike(VenOrderItem venOrderItem,
            JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
        Long startTime = System.currentTimeMillis();
        _log.debug("findByVenOrderItemLike()");
        JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(venOrderItem);
        HashMap complexTypeBindings = new HashMap();
        String stmt = qb.buildQueryString(complexTypeBindings, criteria);
        if (criteria != null) {
            /*
             * Get the binding array from the query builder and make
             * it available to the queryByRange method
             */
            this.bindingArray = qb.getBindingArray();
            for (int i = 0; i < qb.getBindingArray().length; i++) {
                System.out.println("Bindings:" + i + ":" + qb.getBindingArray()[i]);
            }
            List<VenOrderItem> venOrderItemList = this.queryByRange(stmt, firstResult, maxResults);
            Long endTime = System.currentTimeMillis();
            Long duration = startTime - endTime;
            _log.debug("findByVenOrderItemLike() duration:" + duration + "ms");
            return venOrderItemList;
        } else {
            String errMsg = "A query has been initiated with null criteria.";
            _log.error(errMsg);
            throw new EJBException(errMsg);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gdn.venice.facade.VenOrderItemSessionEJBRemote#findByVenOrderItemLikeFR(
     * com.gdn.venice.persistence.VenOrderItem, int, int)
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public FinderReturn findByVenOrderItemLikeFR(VenOrderItem venOrderItem,
            JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults) {
        Long startTime = System.currentTimeMillis();
        _log.debug("findByVenOrderItemLikeFR()");
        JPQLQueryStringBuilder qb = new JPQLQueryStringBuilder(venOrderItem);
        HashMap complexTypeBindings = new HashMap();
        String stmt = qb.buildQueryString(complexTypeBindings, criteria);
        if (criteria != null) {
            /*
             * Get the binding array from the query builder and make
             * it available to the queryByRange method
             */
            this.bindingArray = qb.getBindingArray();
            for (int i = 0; i < qb.getBindingArray().length; i++) {
                System.out.println("Bindings:" + i + ":" + qb.getBindingArray()[i]);
            }

            //Set the finder return object with the count of the total query rows
            FinderReturn fr = new FinderReturn();
            String countStmt = "select count(o) " + stmt.substring(stmt.indexOf("from"));
            Query query = null;
            try {
                query = em.createQuery(countStmt);
                if (this.bindingArray != null) {
                    for (int i = 0; i < bindingArray.length; ++i) {
                        if (bindingArray[i] != null) {
                            query.setParameter(i + 1, bindingArray[i]);
                        }
                    }
                }
                Long totalRows = (Long) query.getSingleResult();
                fr.setNumQueryRows(totalRows);
            } catch (Exception e) {
                _log.error("An exception occured when calling em.createQuery():"
                        + e.getMessage(), e);
                throw new EJBException(e);
            }

            //Set the finder return object with the query list
            fr.setResultList(this.queryByRange(stmt, firstResult, maxResults));
            Long endTime = System.currentTimeMillis();
            Long duration = startTime - endTime;
            _log.debug("findByVenOrderItemLike() duration:" + duration + "ms");
            return fr;
        } else {
            String errMsg = "A query has been initiated with null criteria.";
            _log.error(errMsg);
            throw new EJBException(errMsg);
        }
    }
}
