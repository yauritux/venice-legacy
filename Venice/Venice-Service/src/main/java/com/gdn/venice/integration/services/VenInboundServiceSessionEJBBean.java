package com.gdn.venice.integration.services;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.djarum.raf.utilities.JPQLStringEscapeUtility;
import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.MathUtil;
import com.djarum.raf.utilities.XMLGregorianCalendarConverter;
import com.gdn.integration.jaxb.MerchantProduct;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.integration.jaxb.Payment;
import com.gdn.integration.jaxb.TransactionFee;
import com.gdn.venice.bpmenablement.BPMAdapter;
import com.gdn.venice.dao.VenOrderBlockingSourceDAO;
import com.gdn.venice.dao.VenOrderDAO;
import com.gdn.venice.dao.VenOrderItemDAO;
import com.gdn.venice.dao.VenOrderStatusDAO;
import com.gdn.venice.dao.VenOrderStatusHistoryDAO;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBLocal;
import com.gdn.venice.facade.FinSalesRecordSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBLocal;
import com.gdn.venice.facade.KpiPartySlaSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBLocal;
import com.gdn.venice.facade.LogActivityReportUploadSessionEJBLocal;
import com.gdn.venice.facade.LogAirwayBillReturSessionEJBLocal;
import com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBLocal;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBLocal;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBLocal;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBLocal;
import com.gdn.venice.facade.VenAddressSessionEJBLocal;
import com.gdn.venice.facade.VenAddressTypeSessionEJBLocal;
import com.gdn.venice.facade.VenBankSessionEJBLocal;
import com.gdn.venice.facade.VenCitySessionEJBLocal;
import com.gdn.venice.facade.VenContactDetailSessionEJBLocal;
import com.gdn.venice.facade.VenContactDetailTypeSessionEJBLocal;
import com.gdn.venice.facade.VenCountrySessionEJBLocal;
import com.gdn.venice.facade.VenCustomerSessionEJBLocal;
import com.gdn.venice.facade.VenFraudCheckStatusSessionEJBLocal;
import com.gdn.venice.facade.VenMerchantProductSessionEJBLocal;
import com.gdn.venice.facade.VenMerchantSessionEJBLocal;
import com.gdn.venice.facade.VenOrderAddressSessionEJBLocal;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBLocal;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBLocal;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBLocal;
import com.gdn.venice.facade.VenOrderSessionEJBLocal;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBLocal;
import com.gdn.venice.facade.VenOrderStatusSessionEJBLocal;
import com.gdn.venice.facade.VenPartyAddressSessionEJBLocal;
import com.gdn.venice.facade.VenPartySessionEJBLocal;
import com.gdn.venice.facade.VenPartyTypeSessionEJBLocal;
import com.gdn.venice.facade.VenPaymentStatusSessionEJBLocal;
import com.gdn.venice.facade.VenPaymentTypeSessionEJBLocal;
import com.gdn.venice.facade.VenProductCategorySessionEJBLocal;
import com.gdn.venice.facade.VenProductTypeSessionEJBLocal;
import com.gdn.venice.facade.VenPromotionSessionEJBLocal;
import com.gdn.venice.facade.VenRecipientSessionEJBLocal;
import com.gdn.venice.facade.VenReturAddressSessionEJBLocal;
import com.gdn.venice.facade.VenReturContactDetailSessionEJBLocal;
import com.gdn.venice.facade.VenReturItemAddressSessionEJBLocal;
import com.gdn.venice.facade.VenReturItemContactDetailSessionEJBLocal;
import com.gdn.venice.facade.VenReturItemSessionEJBLocal;
import com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBLocal;
import com.gdn.venice.facade.VenReturSessionEJBLocal;
import com.gdn.venice.facade.VenReturStatusHistorySessionEJBLocal;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBLocal;
import com.gdn.venice.facade.VenStateSessionEJBLocal;
import com.gdn.venice.facade.VenTransactionFeeSessionEJBLocal;
import com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBLocal;
import com.gdn.venice.facade.kpi.KPI_TransactionPosterSessionEJBLocal;
import com.gdn.venice.facade.logistics.activity.SalesRecordGenerator;
import com.gdn.venice.facade.spring.VenOrderItemService;
import com.gdn.venice.facade.spring.VenOrderItemStatusHistoryService;
import com.gdn.venice.facade.util.AWBReconciliation;
import com.gdn.venice.facade.util.HolidayUtil;
import com.gdn.venice.facade.util.KpiPeriodUtil;
import com.gdn.venice.factory.VenOrderStatusFP;
import com.gdn.venice.inbound.services.OrderService;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriodPK;
import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.gdn.venice.persistence.KpiPartyPeriodActualPK;
import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.persistence.KpiTargetBaseline;
import com.gdn.venice.persistence.LogActivityReportUpload;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogAirwayBillRetur;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenAddressType;
import com.gdn.venice.persistence.VenBank;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenContactDetailType;
import com.gdn.venice.persistence.VenCountry;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenFraudCheckStatus;
import com.gdn.venice.persistence.VenMerchant;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderBlockingSource;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderItemAdjustmentPK;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenOrderPaymentAllocationPK;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.persistence.VenPartyAddressPK;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.persistence.VenPaymentStatus;
import com.gdn.venice.persistence.VenPaymentType;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.persistence.VenProductType;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.persistence.VenPromotionType;
import com.gdn.venice.persistence.VenRecipient;
import com.gdn.venice.persistence.VenRetur;
import com.gdn.venice.persistence.VenReturAddress;
import com.gdn.venice.persistence.VenReturContactDetail;
import com.gdn.venice.persistence.VenReturItem;
import com.gdn.venice.persistence.VenReturItemAddress;
import com.gdn.venice.persistence.VenReturItemContactDetail;
import com.gdn.venice.persistence.VenReturItemStatusHistory;
import com.gdn.venice.persistence.VenReturItemStatusHistoryPK;
import com.gdn.venice.persistence.VenReturStatusHistory;
import com.gdn.venice.persistence.VenReturStatusHistoryPK;
import com.gdn.venice.persistence.VenSettlementRecord;
import com.gdn.venice.persistence.VenState;
import com.gdn.venice.persistence.VenTransactionFee;
import com.gdn.venice.persistence.VenTransactionFeePK;
import com.gdn.venice.persistence.VenWcsPaymentType;
import com.gdn.venice.util.VeniceConstants;

/**
 * VenInboundServiceSessionEJBBean.java
 * 
 * Session Bean implementation for inbound integration transactions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Stateless(mappedName = "VenInboundServiceSessionEJBBean")
@WebService(serviceName = "VenInboundServiceSessionEJBBean", portName = "VenInboundServiceSessionEJBPort", endpointInterface = "com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote", targetNamespace = "http://integration.venice.gdn.com/services")
public class VenInboundServiceSessionEJBBean implements VenInboundServiceSessionEJBRemote, VenInboundServiceSessionEJBLocal, VeniceConstants {

	@PersistenceUnit
	private EntityManagerFactory emf;

	@Autowired
	VenOrderDAO venOrderDAO;
	@Autowired
	VenOrderStatusDAO venOrderStatusDAO;
	@Autowired
	VenOrderBlockingSourceDAO venOrderBlockingSourceDAO;
	@Autowired
	VenOrderItemDAO venOrderItemDAO;
	@Autowired
	VenOrderItemService venOrderItemService;
	@Autowired
	VenOrderStatusHistoryDAO venOrderStatusHistoryDAO;
	@Autowired
	VenOrderItemStatusHistoryService venOrderItemStatusHistoryService;
	@Autowired
	OrderService orderService;
	
	private EntityManager emForJDBC;
	
	private static Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
	private static Logger _log = loggerFactory.getLog4JLogger("com.gdn.venice.integration.services.VenInboundServiceSessionEJBBean");
	
	protected Locator<Object> _genericLocator = null;
	protected Mapper _mapper = DozerBeanMapperSingletonWrapper.getInstance();
	
	//Constants for BPM adapter call
//	private static final String AIRWAYBILLID = "airwayBillId";
//	private static final String LOGISTICSMTADATAACTIVITYRECONCILIATION = "Logistics MTA Data Activity Reconciliation";
//	private static final String LOGISTICSMTADATAINVOICERECONCILIATION = "Logistics MTA Data Invoice Reconciliation";
	
	private static final String ORDERID = "orderId";
	private static final String ORDERITEMID = "orderItemId";
	private static final String FINANCEORDERITEMCANCELLEDNOTIFICATION = "Finance Order Item Cancelled Notification";

	/**
	 * Default constructor.
	 */
	public VenInboundServiceSessionEJBBean() {
		super();

		try {
			// Establish a JNDI connection when the bean is started
			this._genericLocator = new Locator<Object>();
		} catch (Exception e) {
			_log.error("An exception occured when looking instantiating the generic locator" + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * createOrder (com.gdn.integration.jaxb.Order)
	 */
	public Boolean createOrder(Order order) {
		_log.debug("\n start createOrder()");
		Long startTime = System.currentTimeMillis();
		
		/*
		 * We must try to validate as much of the order information as possible
		 */
		if(order.isRmaFlag() != null && order.isRmaFlag()){
			createRetur(order);
			
			return Boolean.TRUE;
		}
		
		try {
			orderService.createOrder(order);
		} catch (InvalidOrderException e) {
			_log.error(e);
			throw new EJBException(e.getMessage());
		}
		
		/*

		// Amount
		if (order.getAmount() == null && (order.isRmaFlag() == null || !order.isRmaFlag())) {
			String errMsg = "\n createOrder: message received with no amount";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Customer
		if (order.getCustomer() == null) {
			String errMsg = "\n createOrder: message received with no customer information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Identifier
		if (order.getOrderId() == null) {
			String errMsg = "\n createOrder: message received with no identifier information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Order items
		if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
			String errMsg = "\n createOrder: message received with no order item information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Payments
		if ((order.isRmaFlag() == null || !order.isRmaFlag()) && (order.getPayments() == null || order.getPayments().isEmpty())) {
			String errMsg = "\n createOrder: message received with no payment information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Timestamp
		if (order.getTimestamp() == null) {
			String errMsg = "\n createOrder: message received with no timestamp information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
		
		// Check the fulfillment status
		if (order.getFullfillmentStatus() != null) {
			if (order.getFullfillmentStatus() == VEN_FULFILLMENT_STATUS_ONE) {
				String errMsg = "\n createOrder: message received fulfillment status = VEN_FULFILLMENT_STATUS_ONE(1). Orders cannot be created with fulfillment status = 1";
				_log.error(errMsg);
				throw new EJBException(errMsg);

			}
		}

		// Status
		if (order.getStatus() == null) {
			String errMsg = "\n createOrder: message received with no status information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		//Shipping
		for(OrderItem item : order.getOrderItems()){
			if(item.getLogisticsInfo().getLogisticsProvider().getParty().getFullOrLegalName().equalsIgnoreCase("Select Shipping")){
				String errMsg = "\n createOrder: message received with wrong Logistic Info ("+item.getLogisticsInfo().getLogisticsProvider().getParty().getFullOrLegalName()+")";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}
		}
		*/

		/*
		 * Check that none of the order items already exist and remove any party
		 * record from merchant to prevent data problems from WCS
		 */
		/*
		List <String> merchantProduct =  new ArrayList<String>();
		
		for (OrderItem item : order.getOrderItems()) {
			
			if (this.orderItemExists(item.getItemId().getCode())) {
				String errMsg = "\n createOrder: message received with an order item that already exists in the database:" + item.getItemId().getCode();
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}
			// Remove party from merchant
			if (item.getProduct().getMerchant().getParty() != null) {
				String merchantSKU = item.getProduct().getMerchant().getMerchantId().getCode()+"&"+(item.getProduct().getMerchant().getParty().getFullOrLegalName()!=null?item.getProduct().getMerchant().getParty().getFullOrLegalName():"");
				merchantProduct.add(merchantSKU);
				item.getProduct().getMerchant().setParty(null);
			}
		}
		*/

		// Do the main processing inside this try/catch block
		//try {

			//_genericLocator = new Locator<Object>();
			/*
			 * If there has been a VA payment then we need to merge the order
			 * because it will exist along with all of the payment(s). There may
			 * also be IB and CC payments All of the payments MUST be approved
			 * before we receive them.
			 */
			/*
			_log.debug("\n check va payment");
			Boolean vaPaymentExists = false;
			Boolean csPaymentExists = false;
			Iterator<Payment> i = order.getPayments().iterator();
			while (i.hasNext()) {
				Payment next = i.next();
				if (next.getPaymentType().equals(VEN_WCS_PAYMENT_TYPE_VirtualAccount)) {
					VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class,	"VenOrderPaymentSessionEJBBeanLocal");
					List<VenOrderPayment> venOrderPaymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='"+ next.getPaymentId().getCode()+ "'", 0, 1);
					// Check that the VA payment exists else throw exception
					if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
						String errMsg = "\n createOrder: An order with a VA payment that does not exist has been received";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}

					
					//Check that the VA payment is approved... if not then
					//throw an exception
					
					_log.debug("\n check va payment approval");
					VenOrderPayment venOrderPayment = venOrderPaymentList.get(0);
					if (!venOrderPayment.getVenPaymentStatus().getPaymentStatusId().equals(VEN_VA_PAYMENT_STATUS_ID_APPROVED)) {
						String errMsg = "\n createOrder: An order with an unapproved VA payment has been received";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					vaPaymentExists = true;
				}
				
				if (next.getPaymentType().equals(VEN_WCS_PAYMENT_TYPE_CSPayment)) {
					VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class,	"VenOrderPaymentSessionEJBBeanLocal");
					List<VenOrderPayment> venOrderPaymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='"+ next.getPaymentId().getCode()+ "'", 0, 1);
					// Check that the CS payment exists else throw exception
					if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
						String errMsg = "\n createOrder: An order with a CS payment that does not exist has been received";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}

					
					 // Check that the CS payment is approved... if not then
					 // throw an exception
					 
					_log.debug("\n check CS payment approval");
					VenOrderPayment venOrderPayment = venOrderPaymentList.get(0);
					if (!venOrderPayment.getVenPaymentStatus().getPaymentStatusId().equals(VEN_VA_PAYMENT_STATUS_ID_APPROVED)) {
						String errMsg = "\n createOrder: An order with an unapproved CS payment has been received";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					csPaymentExists = true;
				}
			}

			// Validate if order id already exist for non VA and non CS payments
			_log.debug("\n check wcs order id exist 1");
			if ((order.getPayments() != null && !order.getPayments().isEmpty())) {
				if (!vaPaymentExists && !csPaymentExists) {
					if (this.orderExists(order.getOrderId().getCode())) {
						String errMsg = "\n createOrder: An order with this WCS orderId already exists: "+ order.getOrderId().getCode();
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
				}
			}

			
			 // This is really important. We need to get the order from the DB if
			 // it is VA because it will exist and we must update it with all of
			 // the details.
			  
			 // There MUST be NO changes to the VA payment data because it MUST
			 // be what was sent by Venice to WCS originally. Therefore if the
			 // payment information is included then we must not update it.
			 
			VenOrder venOrder = new VenOrder();
			// If there is a VA payment then get the order from the cache
			if (vaPaymentExists || csPaymentExists) {
				_log.debug("\n va payment exist, retrieve existing order");
				venOrder = this.retreiveExistingOrder(order.getOrderId().getCode());
				// If there is no existing VA status order then throw an exception
				if (venOrder == null) {
					String errMsg = "\n createOrder: message received for an order with VA payments where there is no existing VA status order:" + order.getOrderId().getCode();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}

				
				 // If the status of the existing order is not VA then it is a duplicate.
				 
				_log.debug("\n cek wcs order id exist 2");
				if (!venOrder.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_VA) && !venOrder.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_CS)) {
					String errMsg = "\n createOrder: message received with  the status of the existing order is not VA/CS (duplicate wcs order id):" + venOrder.getWcsOrderId();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
			} else {
				_log.debug("\n cek wcs order id exist 3");
				if (this.orderExists(order.getOrderId().getCode())) {
					String errMsg = "\n createOrder: message received where an order with WCS orderId already exists:" + venOrder.getWcsOrderId();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
			}
			_log.debug("\n set status to C");
			//Set the status of the order explicitly to C (workaround for OpenJPA 2.0 problem)
			VenOrderStatus venOrderStatusC = new VenOrderStatus();
			venOrderStatusC.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_C);
			venOrderStatusC.setOrderStatusCode("C");
			venOrder.setVenOrderStatus(venOrderStatusC);

			
			 // Map the jaxb Order object to a JPA VenOrder object. This will be
			 // ok for both persist and merge because the PK is not touched and
			 // everything must be added anyway (VA payment will only have an
			 // orderId and timestamp).
			 
			_log.debug("Mapping the order object to the venOrder object...");
			_mapper.map(order, venOrder);
						
			
			 // Party for merchant
			 
			
			List<VenOrderItem> orderItems = venOrder.getVenOrderItems();
			VenPartySessionEJBLocal venPartyHome = (VenPartySessionEJBLocal) this._genericLocator.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");
			VenMerchantSessionEJBLocal venMerchantHome = (VenMerchantSessionEJBLocal) this._genericLocator.lookupLocal(VenMerchantSessionEJBLocal.class, "VenMerchantSessionEJBBeanLocal");
		
			// Set the defaults for all of the boolean values in venOrder
			if (venOrder.getBlockedFlag() == null) {
				venOrder.setBlockedFlag(false);
			}
			if (venOrder.getRmaFlag() == null) {
				venOrder.setRmaFlag(false);
			}
			// Default the finance reconcile flag to false.
			venOrder.setFinanceReconcileFlag(false);

			// If the order amount is missing then set it to default to 0
			if (venOrder.getAmount() == null) {
				venOrder.setAmount(new BigDecimal(0));
			}
			
			
			 // This method call will persist the order if there has been no VA payment else it will merge
			 
			_log.debug("\n persist order");
			venOrder = this.persistOrder(vaPaymentExists, csPaymentExists, venOrder);
			_log.debug("\n done persist order");
			
				for(String party : merchantProduct){				
					String[] temp = party.split("&");
					_log.debug("show venParty in orderItem :  "+party);
					_log.debug("string merchant :  "+temp[0]+" and "+temp[1]);
					if(!temp[1].equals("")){
					for(int h =0; h< orderItems.size();h++){
						if(orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId().equals(temp[0])){
							List<VenMerchant> venMechantList = venMerchantHome.queryByRange("select o from VenMerchant o where o.wcsMerchantId ='" +temp[0]+ "'", 0, 1);
							if( venMechantList.size()>0 || venMechantList!=null ){
								if(venMechantList.get(0).getVenParty()==null){
									List<VenParty> vePartyList = venPartyHome.queryByRange("select o from VenParty o where o.fullOrLegalName ='" +temp[1]+ "'", 0, 1);
										if(vePartyList==null || vePartyList.isEmpty()){
											VenParty venPartyitem = new VenParty();
											VenPartyType venPartyType = new VenPartyType();
											// set party type id = 1 adalah merchant
											venPartyType.setPartyTypeId(new Long(1));
											venPartyitem.setVenPartyType(venPartyType);
											venPartyitem.setFullOrLegalName(temp[1]);	
											_log.debug("persist venParty :  "+venPartyitem.getFullOrLegalName());
											venPartyitem = venPartyHome.persistVenParty(venPartyitem);	
											venMechantList.get(0).setVenParty(venPartyitem);
											venMerchantHome.mergeVenMerchant(venMechantList.get(0));
											_log.debug(" add  new party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");			
										}else{
											venMechantList.get(0).setVenParty(vePartyList.get(0));
											venMerchantHome.mergeVenMerchant(venMechantList.get(0));
											_log.debug(" add  party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");
							
										}
									}
								}
						}
						
					}
					}else 	_log.debug(" party is null for venmerchant (Merchant Id :"+ temp[0] +" )");
						
				}
		
			// If the order is RMA do nothing with payments because there are none
			if (!venOrder.getRmaFlag()) {
				_log.debug("\n masuk rma flag false, remove payment existing");
				// Remove any existing order payment allocations that were allocated at VA stage
				this.removeOrderPaymentAllocationList(venOrder);
				_log.debug("\n done remove payment existing");
				// An array list of order payment allocations
				List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = new ArrayList<VenOrderPaymentAllocation>();
				List<VenOrderPayment> venOrderPaymentList = new ArrayList<VenOrderPayment>();

				
				 // Allocate the payments to the order.
				 
				_log.debug("\n Allocate the payments to the order");				
				if (order.getPayments() != null && !order.getPayments().isEmpty()) {
					Iterator<?> paymentIterator = order.getPayments().iterator();
					while (paymentIterator.hasNext()) {
						Payment next = (Payment) paymentIterator.next();
						
						//  Ignore partial fulfillment payments ... looks like a work around in WCS ... no need for this in Venice
						 
						if (!next.getPaymentType().equals(VEN_WCS_PAYMENT_TYPE_PartialFulfillment)) {
							VenOrderPayment venOrderPayment = new VenOrderPayment();
							
							 // If the payment already exists then just fish it
							 // from the DB. This is the case for VA payments as
							 // they are received before the confirmed order.
							 
							VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
							List<VenOrderPayment> venOrderPaymentList2 = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='" + next.getPaymentId().getCode() + "'", 0, 1);
							
							if (venOrderPaymentList2 != null && !venOrderPaymentList2.isEmpty()) {
								venOrderPayment = venOrderPaymentList2.get(0);
							}
							// Map the payment with dozer
							_log.debug("Mapping the VenOrderPayment object...");
							_mapper.map(next, venOrderPayment);

							// Set the payment type based on the WCS payment type
							VenPaymentType venPaymentType = new VenPaymentType();
							_log.debug("\n mapping payment type");
							if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_DebitMandiri)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikBCA)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYXPercentInstallment)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_KlikPAYZeroPercentInstallment)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_MIGSCreditCard)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CC);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_CC);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_VirtualAccount)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_VA);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_VA);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_CSPayment)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CS);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_CS);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_MIGSBCAInstallment)){
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CC);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_CC);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_CIMBClicks)) {
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							}  else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_XLTunai)) {
								_log.debug("payment type XLTunai, reference id: "+next.getReferenceId());
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
								venOrderPayment.setReferenceId(next.getReferenceId());
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_MandiriInstallment)){
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CC);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_CC);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_BIIngkisan)){
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_BRI)){
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_IB);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_IB);
								venOrderPayment.setVenPaymentType(venPaymentType);
							} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VEN_WCS_PAYMENT_TYPE_MandiriDebit)){
								venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CC);
								venPaymentType.setPaymentTypeId(VEN_PAYMENT_TYPE_ID_CC);
								venOrderPayment.setVenPaymentType(venPaymentType);
							}
							venOrderPaymentList.add(venOrderPayment);
						}
					}					
					
					_log.debug("\n persist payment");
					venOrderPaymentList = this.persistOrderPaymentList(venOrderPaymentList);

					paymentIterator = venOrderPaymentList.iterator();
					BigDecimal paymentBalance = venOrder.getAmount();
					int p=0;
					while (paymentIterator.hasNext()) {
						VenOrderPayment next = (VenOrderPayment) paymentIterator.next();

						
						 // Only include the allocations for non-VA payments
						 // because VA payments are already in the DB
						 
						_log.debug("\n allocate payment");
						
						 // semua Payment di allocate, untuk payment VA dan non-VA.
						 
						//if (!next.getVenPaymentType().getPaymentTypeCode().equals(VEN_PAYMENT_TYPE_VA)) {
							// Build the allocation list manually based on the payment
							VenOrderPaymentAllocation allocation = new VenOrderPaymentAllocation();
							allocation.setVenOrder(venOrder);
							BigDecimal paymentAmount = next.getAmount();
							
							_log.debug("Order Amount = "+paymentBalance);
							_log.debug("paymentBalance.compareTo(new BigDecimal(0)):  "+paymentBalance.compareTo(new BigDecimal(0)) );
							
							// If the balance is greater than zero
							if (paymentBalance.compareTo(new BigDecimal(0)) >= 0) {
								
								 // If the payment amount is greater than the
								 // balance then allocate the balance amount else
								 // allocate the payment amount.
								 
								if (paymentBalance.compareTo(paymentAmount) < 0) {
									allocation.setAllocationAmount(paymentBalance);
									_log.debug("Order Allocation Amount is paymentBalance = "+paymentBalance);
								} else {
									allocation.setAllocationAmount(paymentAmount);
									_log.debug("Order Allocation Amount is paymentAmount = "+paymentAmount);
								}
								
								paymentBalance = paymentBalance.subtract(paymentAmount);
								allocation.setVenOrderPayment(next);

								// Need a primary key object...
								VenOrderPaymentAllocationPK venOrderPaymentAllocationPK = new VenOrderPaymentAllocationPK();
								venOrderPaymentAllocationPK.setOrderPaymentId(next.getOrderPaymentId());
								venOrderPaymentAllocationPK.setOrderId(venOrder.getOrderId());
								allocation.setId(venOrderPaymentAllocationPK);

								venOrderPaymentAllocationList.add(allocation);
								_log.debug("venOrder Payment Allocation List size from looping ke-"+p+" = "+venOrderPaymentAllocationList.size());
								p++;
							}
						//}
					}
					_log.debug("\n persist payment allocation");
					venOrderPaymentAllocationList = this.persistOrderPaymentAllocationList(venOrderPaymentAllocationList);
					venOrder.setVenOrderPaymentAllocations(venOrderPaymentAllocationList);
					
					
					 // Here we need to create a dummy reconciliation records
					 // for the non-VA payments so that they appear in the 
					 // reconciliation screen as unreconciled.
					 // Later these records will be updated when the funds in
					 // reports are processed 
					 					
					FinArFundsInReconRecordSessionEJBLocal reconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) this._genericLocator
					.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");
					_log.debug("\n create reconciliation record");
					for (VenOrderPayment payment : venOrderPaymentList) {
						
						 // Only insert reconciliation records for non-VA payments here
						 // because the VA records will have been inserted when a VA payment is received.
						 
						if (payment.getVenPaymentType().getPaymentTypeId() != VeniceConstants.VEN_PAYMENT_TYPE_ID_VA 
								&& payment.getVenPaymentType().getPaymentTypeId() != VeniceConstants.VEN_PAYMENT_TYPE_ID_CS) {
							FinArFundsInReconRecord reconRecord = new FinArFundsInReconRecord();

							FinArReconResult result = new FinArReconResult();
							result.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NONE);
							reconRecord.setFinArReconResult(result);
							
							FinArFundsInActionApplied actionApplied = new FinArFundsInActionApplied();
							actionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
							reconRecord.setFinArFundsInActionApplied(actionApplied);

							FinApprovalStatus approvalStatus = new FinApprovalStatus();
							approvalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
							reconRecord.setFinApprovalStatus(approvalStatus);
							
							reconRecord.setVenOrderPayment(payment);
							reconRecord.setWcsOrderId(venOrder.getWcsOrderId());
							reconRecord.setOrderDate(venOrder.getOrderDate());
							reconRecord.setPaymentAmount(payment.getAmount());
							reconRecord.setNomorReff(payment.getReferenceId()!=null?payment.getReferenceId():"");

							// balance per payment amount - handling fee = payment amount, jadi bukan amount order total keseluruhan
							_log.debug("\n payment Amount  = "+payment.getAmount());
							_log.debug("\n HandlingFee = "+payment.getHandlingFee());
							_log.debug("\n setRemainingBalanceAmount = "+payment.getAmount().subtract(payment.getHandlingFee()));
							
							reconRecord.setRemainingBalanceAmount(payment.getAmount());
							reconRecord.setUserLogonName("System");
							reconRecordHome.persistFinArFundsInReconRecord(reconRecord);
						}
					}
				}			
			}

			_log.debug("\n done create order!");
			Long endTime = System.currentTimeMillis();
			Long duration = endTime - startTime;
			_log.debug("createOrder: persisted new venOrder.orderId:"
					+ venOrder.getOrderId() + " status:"
					+ venOrder.getVenOrderStatus().getOrderStatusCode()
					+ " in:" + duration + "ms");
			*/

		/*
		} catch (Exception e) {
			String errMsg = "An Exception occured when persisting the order:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(_genericLocator!=null){
					_genericLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		*/
		
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		_log.debug("createOrder() completed in:" + duration + "ms");
		return Boolean.TRUE;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * createOrderVAPayment (com.gdn.integration.jaxb.Order)
	 */
	public Boolean createOrderVAPayment(Order order) {
		_log.debug("createOrderVAPayment() - wcsOrderId:" + order.getOrderId().getCode());
		Long startTime = System.currentTimeMillis();

		// Order id
		if (order.getOrderId() == null) {
			String errMsg = "createOrderVAPayment: message received with no WCS orderId information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Timestamp
		if (order.getTimestamp() == null) {
			String errMsg = "createOrderVAPayment: message received with no timestamp information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// If there is no payment record at all then throw an exception
		if (order.getPayments() == null || order.getPayments().isEmpty()) {
			String errMsg = "createOrderVAPayment: message has been received with no payment information";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Check the payments records
		Boolean vaPaymentExists = false;
		Boolean csPaymentExists = false;
		Iterator<Payment> i = order.getPayments().iterator();
		while (i.hasNext()) {
			_log.debug("createOrderVAPayment: payment record received...");
			
			Payment payment = i.next();
			if (payment.getPaymentType().equals(VEN_WCS_PAYMENT_TYPE_VirtualAccount)) {
				_log.debug("VA payment type...");
				vaPaymentExists = true;
			}
			
			if (payment.getPaymentType().equals(VEN_WCS_PAYMENT_TYPE_CSPayment)) {
				_log.debug("CS payment type...");
				csPaymentExists = true;
				
				if (order.getOldOrderId() == null || order.getOldOrderId().equals("")) {
					String errMsg = "createOrderVAPayment: message has been received with no old order id";
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
				
			}
/*
 * Commented because there may be a mix of payments that need to be stored.
 */
// else {
//				String errMsg = "createOrderVAPayment: payment information received has a payment that is not VA payment type";
//				_log.error(errMsg);
//				throw new EJBException(errMsg);
//			}
		}

		// If there is no VA/CS payment throw an exception
		if (!vaPaymentExists && !csPaymentExists) {
			String errMsg = "createOrderVAPayment: message has been received with no VA/CS payment information";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		} else {
			// Check that the payments in the message do not already exist in the cache
			Iterator<Payment> paymentIterator = order.getPayments().iterator();
			while (paymentIterator.hasNext()) {
				if (this.orderPaymentExists(paymentIterator.next().getPaymentId().getCode())) {
					String errMsg = "createOrderVAPayment: message has been received with duplicate VA/CS payment information";
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
			}
		}

		// Remove the customer and the order items because they need to be ignored at this stage
		order.setCustomer(null);
		order.getOrderItems().clear();

		// Map the jaxb Order object to a JPA VenOrder object.
		_log.debug("createOrderVAPayment: mapping the order object to the venOrder object...");
		VenOrder venOrder = new VenOrder();
		VenOrder existingOrder = new VenOrder();
		try {
			_mapper.map(order, venOrder);
		} catch (MappingException e) {
			String errMsg = "createOrderVAPayment: An Exception occured when mapping the order object:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}

		
		VenOrderStatus venOrderStatus = new VenOrderStatus();
		// Set the order status to VA
		if(vaPaymentExists){
			venOrderStatus.setOrderStatusCode("VA");
			venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_VA);
		}
		// Set the order status to CS
		if(csPaymentExists){
			venOrderStatus.setOrderStatusCode("CS");
			venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CS);
		}
		
		venOrder.setVenOrderStatus(venOrderStatus);

		// Synchronize the reference data
		venOrder = this.synchronizeVenOrderReferenceData(venOrder);

		// Persist the order
		try {
			_genericLocator = new Locator<Object>();
			VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator .lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
			Boolean orderExists = false;
			if (this.orderExists(order.getOrderId().getCode())) {
				orderExists = true;
				existingOrder  = this.retreiveExistingOrder(order.getOrderId() .getCode());
				// Check that the status of the order is still VA
				if (!existingOrder.getVenOrderStatus().getOrderStatusCode().equals("VA") && vaPaymentExists) {
					String errMsg = "createOrderVAPayment: A message was received with an orderId that currently exists and is not VA status:" + order.getOrderId();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
				
				// Check that the status of the order is still CS
				if (!existingOrder.getVenOrderStatus().getOrderStatusCode().equals("CS") && csPaymentExists) {
					String errMsg = "createOrderVAPayment: A message was received with an orderId that currently exists and is not CS status:" + order.getOrderId();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
			}
			if (!orderExists) {
				venOrder = (VenOrder) orderHome.persistVenOrder(venOrder);
			} else {
				//venice 117, remove existing order, then persist new order, so the payment not duplicate
				orderHome.removeVenOrder(existingOrder);				
				venOrder = (VenOrder) orderHome.persistVenOrder(venOrder);
			}

		} catch (Exception e) {
			String errMsg = "createOrderVAPayment: An exception occured when persisting VenOrder for payment type VA/CS:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		} 

		/*
		 * There may be more than one VA payment so iterate the list These
		 * payments may also have different billing addresses
		 */
		Iterator<Payment> paymentIterator = order.getPayments().iterator();
		while (paymentIterator.hasNext()) {
			VenOrderPayment venOrderPayment = new VenOrderPayment();

			Payment next = paymentIterator.next();

			// Ignore payments that are not VA
			if (!next.getPaymentType().equalsIgnoreCase(VEN_WCS_PAYMENT_TYPE_VirtualAccount) && !next.getPaymentType().equalsIgnoreCase(VEN_WCS_PAYMENT_TYPE_CSPayment)) {
				continue;
			}

			// Map the payment with dozer
			_log.debug("createOrderVAPayment: mapping the VenOrderPayment object...");
			_mapper.map(next, venOrderPayment);

			// Set the payment type to VA/CS based on the WCS payment type being VA/CS
			VenPaymentType venPaymentType = new VenPaymentType();
			if(vaPaymentExists){
				venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_VA);
			}else if(csPaymentExists){
				venPaymentType.setPaymentTypeCode(VEN_PAYMENT_TYPE_CS);
				
				VenOrder oldOrder = new VenOrder();
				oldOrder.setWcsOrderId(order.getOldOrderId());
				
				venOrderPayment.setOldVenOrder(oldOrder);
				
				if(venOrderPayment.getVenBank().getBankCode() == null){
					VenBank bank = new VenBank();
					bank.setBankCode("CENAIDJA");
					
					venOrderPayment.setVenBank(bank);
				}
				
			}
			
			venOrderPayment.setVenPaymentType(venPaymentType);
			// Synchronize the reference data
			venOrderPayment = this.synchronizeVenOrderPaymentReferenceData(venOrderPayment);

			// Persist the billing address
			venOrderPayment.setVenAddress(this.persistAddress(venOrderPayment.getVenAddress()));

			// Persist the VA/CS payment
			try {
				VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
				venOrderPayment = (VenOrderPayment) paymentHome.persistVenOrderPayment(venOrderPayment);
				
				/*
				 * Create a reconciliation record for the payment here.
				 */
				FinArFundsInReconRecordSessionEJBLocal reconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) this._genericLocator.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");
				
				FinArFundsInReconRecord reconRecord = new FinArFundsInReconRecord();
				
				FinArReconResult result = new FinArReconResult();
				result.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NONE);
				reconRecord.setFinArReconResult(result);
				
				FinArFundsInActionApplied actionApplied = new FinArFundsInActionApplied();
				actionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
				reconRecord.setFinArFundsInActionApplied(actionApplied);
				
				FinApprovalStatus approvalStatus = new FinApprovalStatus();
				approvalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
				reconRecord.setFinApprovalStatus(approvalStatus);
				
				reconRecord.setVenOrderPayment(venOrderPayment);
				reconRecord.setWcsOrderId(venOrder.getWcsOrderId());
				reconRecord.setOrderDate(venOrder.getOrderDate());
				reconRecord.setPaymentAmount(venOrderPayment.getAmount());
				reconRecord.setNomorReff(venOrderPayment.getVirtualAccountNumber());
				//reconRecord.setRemainingBalanceAmount(venOrder.getAmount());
				//payment balance diganti dengan payment Amount dikurangi dengan handling fee
				_log.debug("\n payment Amount VA/CS = "+venOrderPayment.getAmount());
				_log.debug("\n HandlingFee VA/CS = "+venOrderPayment.getHandlingFee());
				_log.debug("\n setRemainingBalanceAmount VA/CS = "+venOrderPayment.getAmount());
				
				reconRecord.setRemainingBalanceAmount(venOrderPayment.getAmount());
				
				reconRecord.setUserLogonName("System");
				reconRecordHome.persistFinArFundsInReconRecord(reconRecord);

			} catch (Exception e) {
				String errMsg = "createOrderVAPayment: An exception occured when peristing VenOrderPayment for payment type VA/CS:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();		
				throw new EJBException(errMsg);
			} 

			// Persist the payment allocation
			VenOrderPaymentAllocation venOrderPaymentAllocation = new VenOrderPaymentAllocation();
			venOrderPaymentAllocation.setVenOrder(venOrder);
			venOrderPaymentAllocation.setVenOrderPayment(venOrderPayment);
			VenOrderPaymentAllocationPK id = new VenOrderPaymentAllocationPK();
			id.setOrderId(venOrder.getOrderId());
			id.setOrderPaymentId(venOrderPayment.getOrderPaymentId());
			venOrderPaymentAllocation.setId(id);
			/*
			 * At VA stage the order amount may be null. 
			 * Allocate the order amount if it is <= to the payment amount
			 * otherwise allocate the payment amount to the order. 
			 */
			if(venOrder.getAmount() != null && venOrder.getAmount().compareTo(venOrderPayment.getAmount()) < 0){
				venOrderPaymentAllocation.setAllocationAmount(venOrder.getAmount());				
			}else{
				venOrderPaymentAllocation.setAllocationAmount(venOrderPayment.getAmount());
			}
			try {
				VenOrderPaymentAllocationSessionEJBLocal allocationHome = (VenOrderPaymentAllocationSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class, "VenOrderPaymentAllocationSessionEJBBeanLocal");
				if(venOrderPayment.getVenOrderPaymentAllocations() == null){
					venOrderPayment.setVenOrderPaymentAllocations(new ArrayList<VenOrderPaymentAllocation>());
				}
				venOrderPayment.getVenOrderPaymentAllocations().add(allocationHome.persistVenOrderPaymentAllocation(venOrderPaymentAllocation));
			} catch (Exception e) {
				String errMsg = "createOrderVAPayment: An exception occured when peristing VenOrderPaymentAllocation for payment type VA/CS:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		
		//ad order status history
		this.createOrderStatusHistory(venOrder, venOrder.getVenOrderStatus());
		try{
			if(_genericLocator!=null){
				_genericLocator.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		_log.debug("createOrderVAPayment() completed in:" + duration + "ms");
		return Boolean.TRUE;
	}

	private void validateOrderIdNotNull(Order order){
		if (order.getOrderId() == null) {
			String errMsg = "message received with no identifier information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
	}
	
	private void validateOrderTimestampNotNull(Order order){
		if (order.getTimestamp() == null) {
			String errMsg = "message received with no timestamp information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
	}
	
	private void validateOrderStatusNotNull(Order order){
		if (order.getStatus() == null) {
			String errMsg = "message received with no status information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
	}
	
	private void validateOrderFullfillmentStatusNotOne(Order order){
		if (order.getFullfillmentStatus() != VEN_FULFILLMENT_STATUS_ONE) {
			String errMsg = "message received with fulfillment status not 1. Orders can only be updated where the fulfillment status is 1";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
	}
	
	private void validateUpdateOrderParameters(Order order){
		validateOrderIdNotNull(order);
		validateOrderTimestampNotNull(order);
		validateOrderStatusNotNull(order);
		validateOrderFullfillmentStatusNotOne(order);
	}
	
	private Boolean updateOrderIgnoredWhenStatusSFOrFC(Long orderStatusId){
		if(orderStatusId == VeniceConstants.VEN_ORDER_STATUS_SF || 
		   orderStatusId == VeniceConstants.VEN_ORDER_STATUS_FC){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	private void validateContainsOrderItemWithStatusPFOrOS(List<VenOrderItem> venOrderItemList){
		Boolean pfFound = false;
		Boolean osFound = false;
		for(int i=0;i<venOrderItemList.size();i++){
			if (venOrderItemList.get(i).getVenOrderStatus().getOrderStatusId() == VEN_ORDER_STATUS_PF){		
				pfFound = true;
				_log.info("order item with status PF found, order item id: "+venOrderItemList.get(i).getWcsOrderItemId());
			}
			if (venOrderItemList.get(i).getVenOrderStatus().getOrderStatusId() == VEN_ORDER_STATUS_OS){		
				osFound = true;
				_log.info("order item with status OS found, order item id: "+venOrderItemList.get(i).getWcsOrderItemId());
			}
		}

		if (!pfFound && !osFound) {
			String errMsg = "updateOrder: message received for order with no order items that are in PF or OS status:" + venOrderItemList.get(0).getVenOrder().getWcsOrderId();
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
	}
	
	private void mapOldOrderItemWithNewOrderItem(VenOrderItem oldOrderItem, OrderItem newOrderItem){
		
		oldOrderItem.setGiftWrapPrice(new BigDecimal(newOrderItem.getLogisticsInfo().getGiftWrapPrice()));
		oldOrderItem.setInsuranceCost(new BigDecimal(newOrderItem.getFinanceInfo().getLogisticsInsuranceCost()));
		oldOrderItem.setLogisticsPricePerKg(new BigDecimal(newOrderItem.getLogisticsInfo().getLogisticsPricePerKg()));
		oldOrderItem.setPackageCount(newOrderItem.getLogisticsInfo().getPackageCount());
		oldOrderItem.setPrice(new BigDecimal(newOrderItem.getPrice()));
		
		oldOrderItem.setQuantity(newOrderItem.getQuantity());
		oldOrderItem.setShippingCost(new BigDecimal(newOrderItem.getFinanceInfo().getShippingCost()));
		oldOrderItem.setShippingWeight(new BigDecimal(newOrderItem.getProduct().getShippingWeight() * oldOrderItem.getQuantity()));
		oldOrderItem.setTotal(new BigDecimal(newOrderItem.getTotal()));
		
		_log.debug("new quantity: "+newOrderItem.getQuantity());
	}
	
	private void updateExistingOrderItemWithNewOrderItem(VenOrderItem venOrderItem, OrderItem orderItem){
		//adjust the quantity and the related price fields for the PF item.
		_log.info("the order item in update order is found, check if it is in PF status in venice");
		if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VEN_ORDER_STATUS_PF) {
			_log.info("the order item found, is in PF status, so update it");
			// Set the status of the adjusted order to FP
			venOrderItem.setVenOrderStatus(VenOrderStatusFP.createVenOrderStatus());
			
			mapOldOrderItemWithNewOrderItem(venOrderItem, orderItem);
			
			//update shipping address
			_log.debug("update shipping address");
			VenAddress oldShippingAddress = new VenAddress();
			
			oldShippingAddress = venOrderItem.getVenAddress();
			
			_log.debug("old address id: "+oldShippingAddress.getAddressId());
			_log.debug("old address streetAddress1: "+oldShippingAddress.getStreetAddress1());
			_log.debug("old address kecamatan: "+oldShippingAddress.getKecamatan());
			_log.debug("old address kelurahan: "+oldShippingAddress.getKelurahan());
			_log.debug("old address postalCode: "+oldShippingAddress.getPostalCode());
			_log.debug("old address cityName: "+oldShippingAddress.getVenCity().getCityName());
			_log.debug("old address stateName: "+oldShippingAddress.getVenState().getStateName());
			_log.debug("old address countryName: "+oldShippingAddress.getVenCountry().getCountryName());
			
			_mapper.map(orderItem.getLogisticsInfo().getShippingAddress(), oldShippingAddress);

			_log.debug("new address id: "+oldShippingAddress.getAddressId());
			_log.debug("new address streetAddress1: "+oldShippingAddress.getStreetAddress1());
			_log.debug("new address kecamatan: "+oldShippingAddress.getKecamatan());
			_log.debug("new address kelurahan: "+oldShippingAddress.getKelurahan());
			_log.debug("new address postalCode: "+oldShippingAddress.getPostalCode());
			_log.debug("new address cityName: "+oldShippingAddress.getVenCity().getCityName());
			_log.debug("new address stateName: "+oldShippingAddress.getVenState().getStateName());
			_log.debug("new address countryName: "+oldShippingAddress.getVenCountry().getCountryName());
			
			_log.debug("merge new shipping address");
			venOrderItem.setVenAddress(this.persistAddress(oldShippingAddress));																						
			
			venOrderItemService.mergeVenOrderItem(venOrderItem);
			_log.debug("done update order item");
			
			// Synchronize the reference data
			venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
			
			_log.debug("start add order item status history");	
			_log.debug("order item id: "+venOrderItem.getOrderItemId());
			venOrderItemStatusHistoryService.savePartialPartialFulfillmentVenOrderItemStatusHistory(venOrderItem);
			_log.debug("done add order item status history");
		} else {
			String errMsg = "updateOrder: message received for order with order items that are not in PF status. Only PF status items can be updated:" + venOrderItem.getVenOrder().getWcsOrderId();
			_log.error(errMsg);
		}							
		
	}
	
	private boolean isExistingOrderItemInNewOrderItem(VenOrderItem venOrderItem, Iterator<OrderItem> orderItemIterator){
		boolean matched = false;
		
		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = orderItemIterator.next();
			
			if (orderItem.getItemId().getCode().equals(venOrderItem.getWcsOrderItemId())) {
				matched = true;
			}
		}
		
		return matched;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * updateOrder (com.gdn.integration.jaxb.Order)
	 */
	/*
	 * This operation only checks the fulfillmentStatus of the order and if there are changes due to a partial fulfillment situation then these
	 * changes are applied to the order (for example the quantities and order items may change). The following fields may change: 
	 * o timestamp
	 * o fulfillmentStatus 
	 * o orderItem.quantity may be reduced 
	 * o orderAmount may be reduced 
	 * o Order status will be kept in FP
	 * o Order item status changed from PF to FP
	 * o remove thetransaction fees 
	 * o orderItem - may be removed. - compare the existing order items with the new ones - adjust quantity related fields
	 * o shipping address
	 * o price, shipping weight, shipping cost, insurance cost, price per kg, giftwrap price, total
	 * 
	 * Note that the addresses and all other facets of the order will never
	 * change. Order items will never be added.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean updateOrder(Order order) {
		_log.debug("start updateOrder() for order id: "+order.getOrderId());
				
		Long startTime = System.currentTimeMillis();

		_log.debug("start validation on updateOrder()");
		
		validateUpdateOrderParameters(order);

		// Fetch the order and check that it exists
		List<VenOrder> venOrderList = venOrderDAO.findByWcsOrderId(order.getOrderId().getCode()); 
		VenOrder venOrder = ((venOrderList != null) && (!(venOrderList.isEmpty())) ? venOrderList.get(0) : null);
		if (venOrder == null) {
			String errMsg = "updateOrder: message received for order that does not exist:" + order.getOrderId().getCode();
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		_log.debug("done validation on updateOrder()");
		
		//jika status order adalah SF atau FC, maka abaikan update order
		if(updateOrderIgnoredWhenStatusSFOrFC(venOrder.getVenOrderStatus().getOrderStatusId())){
			_log.info("order status is SF or FC, so ignore the update order");
			return Boolean.FALSE;
		}else{
			// Set the fields in the order body
			venOrder.setAmount(new BigDecimal(order.getAmount()));
			venOrder.setFulfillmentStatus(new Long(order.getFullfillmentStatus()));		
			
			VenOrderStatus statusFP = VenOrderStatusFP.createVenOrderStatus();
			venOrder.setVenOrderStatus(statusFP);			
			
			// Synchronize the reference data
			venOrder = this.synchronizeVenOrderReferenceData(venOrder);
	
			// process the order items
			_log.debug("process the order items");
			List<VenOrderItem> venOrderItemList = null;
			List<VenTransactionFee> transFee = null;
	
			try {
				_genericLocator = new Locator<Object>();
				VenTransactionFeeSessionEJBLocal transactionFeesHome = (VenTransactionFeeSessionEJBLocal) this._genericLocator.lookupLocal(VenTransactionFeeSessionEJBLocal.class, "VenTransactionFeeSessionEJBBeanLocal");
				VenOrderItemAdjustmentSessionEJBLocal orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderItemAdjustmentSessionEJBLocal.class, "VenOrderItemAdjustmentSessionEJBBeanLocal");
				
				venOrderItemList = venOrderItemDAO.findWithVenOrderStatusByVenOrder(venOrder);
				_log.debug("venOrderItemList size: "+venOrderItemList.size());
				
				//Check that the existing status of any order item in the order is PF or OS
				_log.info("check that the existing status of any order item in the order is PF or OS");
				validateContainsOrderItemWithStatusPFOrOS(venOrderItemList);
	
				// Make the changes
				_log.debug("looping order item list");
				for (int i=0;i<venOrderItemList.size();i++) {
					VenOrderItem venOrderItem = venOrderItemList.get(i);
					_log.info("order item id to check: "+venOrderItem.getWcsOrderItemId());
					
					Iterator<OrderItem> orderItemIterator = order.getOrderItems().iterator();
					
					Boolean matched = isExistingOrderItemInNewOrderItem(venOrderItem, orderItemIterator);
					/*
					 * If the item was not found, then set to cancel.
					 */
					if (!matched) {
						_log.info("the order item in update order is not found, check if it is in PF or OS status in venice");
						if (venOrderItem.getVenOrderStatus().getOrderStatusId() == VEN_ORDER_STATUS_PF || (venOrderItem.getVenOrderStatus().getOrderStatusId() == VEN_ORDER_STATUS_OS)) {
							_log.info("the order item not found in new order, is in PF or OS status in venice, so set to cancel");
							_log.info("order item id to remove: "+venOrderItem.getWcsOrderItemId());
							
							/*
							 * Recalculate promo
							 */
							List<VenOrderItem> venOrderItems = venOrderItemList;
							List<VenOrderItemAdjustment> venOrderItemAdjustmentNewSpreadList = new ArrayList<VenOrderItemAdjustment>();
							_log.debug("Total Order Item : " + venOrderItems.size());
							
							BigDecimal totalPromo = new BigDecimal(0);
							
							for(VenOrderItem venOrderItem2 : venOrderItems){
								BigDecimal currentItemShippingAndInsuranceCost = venOrderItem2.getShippingCost().add(venOrderItem2.getInsuranceCost());
								
								String query = "select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + venOrderItem2.getOrderItemId();
								
								List<VenOrderItemAdjustment> venOrderItemAdjustmentList = orderItemAdjustmentHome.queryByRange(query, 0, 0);
								
								_log.debug("Total Free Shipping " + currentItemShippingAndInsuranceCost);
								_log.debug("Total Adjustment for Order Item " + venOrderItem2.getOrderItemId() + " : " + venOrderItemAdjustmentList.size());
								
								for(VenOrderItemAdjustment venOrderItemAdjustment:venOrderItemAdjustmentList){
									_log.debug("Adjustment " + venOrderItemAdjustment.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustment.getVenOrderItem().getOrderItemId() + " : " + venOrderItemAdjustment.getAmount());
									
									// sum a not free shipping adjustment
									if(currentItemShippingAndInsuranceCost.compareTo(venOrderItemAdjustment.getAmount().abs()) != 0){
										totalPromo = totalPromo.add(venOrderItemAdjustment.getAmount());
									}
									
									// add non free shipping promo for item not cancelled to list
									if(currentItemShippingAndInsuranceCost.compareTo(venOrderItemAdjustment.getAmount().abs()) != 0 &&
										venOrderItemAdjustment.getVenOrderItem().getOrderItemId() != venOrderItem.getOrderItemId()){
										
										venOrderItemAdjustmentNewSpreadList.add(venOrderItemAdjustment);	
									}
									
									// set adjustment amount to Rp 0 for cancelled item
									if(venOrderItemAdjustment.getVenOrderItem().getOrderItemId() == venOrderItem.getOrderItemId()){
										_log.debug("Merging Adjustment " + venOrderItemAdjustment.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustment.getVenOrderItem().getOrderItemId());
										
										venOrderItemAdjustment.setAmount(new BigDecimal(0));
										orderItemAdjustmentHome.mergeVenOrderItemAdjustment(venOrderItemAdjustment);
									}									
								}
							}
							
							int totalOrderItemAfterPF =  venOrderItems.size() - 1;
							BigDecimal adjustmentSpread = new BigDecimal(0);
							
							if(totalOrderItemAfterPF > 0){
								adjustmentSpread = totalPromo.divide(new BigDecimal(totalOrderItemAfterPF), 2, RoundingMode.HALF_UP);
							}
							
							_log.debug("Total Promo : " + totalPromo);
							_log.debug("Total Order Item : " + totalOrderItemAfterPF);
							_log.debug("Adjustment Spread : " + adjustmentSpread);
							
							//merge all non free shipping adjustment with the new adjustment spread
							for(VenOrderItemAdjustment venOrderItemAdjustmentNewSpread : venOrderItemAdjustmentNewSpreadList){
								venOrderItemAdjustmentNewSpread.setAmount(adjustmentSpread);
								
								_log.debug("Merging Adjustment " + venOrderItemAdjustmentNewSpread.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustmentNewSpread.getVenOrderItem().getOrderItemId());

								orderItemAdjustmentHome.mergeVenOrderItemAdjustment(venOrderItemAdjustmentNewSpread);
							}
							
							// update order
							VenOrderStatus canceled = new VenOrderStatus();
							canceled.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_X);
							canceled.setOrderStatusCode("X");
							venOrderItem.setVenOrderStatus(canceled);
							
							venOrderItemService.mergeVenOrderItem(venOrderItem);
							
							// send cancelled item to Lombardi
							Properties properties = new Properties();
							properties.load(new FileInputStream(BPMAdapter.WEBAPI_PROPERTIES_FILE));
							String userName = properties.getProperty("javax.xml.rpc.security.auth.username");		
							String password = BPMAdapter.getUserPasswordFromLDAP(userName);
							BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, password);
							bpmAdapter.synchronize();
							
							HashMap<String, String> taskData = new HashMap<String, String>();
							taskData.put(ORDERITEMID, venOrderItem.getOrderItemId().toString());
							taskData.put(ORDERID, venOrder.getOrderId().toString());							
	
							try {
								_log.debug("Starting bpm " + FINANCEORDERITEMCANCELLEDNOTIFICATION);
								bpmAdapter.startBusinessProcess(FINANCEORDERITEMCANCELLEDNOTIFICATION, taskData);
								_log.debug("Done starting bpm " + taskData.toString());
							} catch (Exception e) {
								_log.error("Error when starting bpm");
								e.printStackTrace();
							}
							
							_log.debug("the order item is canceled");
							/*
							 * Remove the transaction fees because they will be
							 * applied again when the order goes to PU. Note that
							 * these cannot be additive but must be applied for the
							 * whole order.
							 */
							_log.debug("remove the transaction fees");
							transFee = transactionFeesHome.queryByRange("select o from VenTransactionFee o where o.venOrder.orderId ="+ venOrder.getOrderId(), 0, 0);
							if(transFee.size()>0){
								_log.debug("removing the transaction fees");
								venOrder.setVenTransactionFees(transFee);
								transactionFeesHome.removeVenTransactionFeeList(venOrder.getVenTransactionFees());
								_log.debug("done removing the transaction fees");
							} else{
								_log.debug("the transaction fees is not found");
							}						
						} else {
							String errMsg = "updateOrder: message received for order with missing order items that are in PF or OS status. Only PF or OS status items can be removed:" + order.getOrderId().getCode();
							_log.error(errMsg);
						}
					}
				}
			} catch (Exception e) {
				String errMsg = "updateOrder: an exception occured when looking up VenOrderItemSessionEJBBean: " + e.getMessage();
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}
			
			// Merge the order
			try {
				VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
				_log.debug("merge order");
				venOrder = (VenOrder) orderHome.mergeVenOrder(venOrder);
				_log.debug("done merge order");
				
				_log.debug("start add order history");
				VenOrderStatusHistorySessionEJBLocal orderHistorySessionHome = (VenOrderStatusHistorySessionEJBLocal) this._genericLocator
				.lookupLocal(VenOrderStatusHistorySessionEJBLocal.class, "VenOrderStatusHistorySessionEJBBeanLocal");
							
				VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
				venOrderStatusHistoryPK.setOrderId(new Long(venOrder.getOrderId()));
				venOrderStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
				
				VenOrderStatusHistory orderStatusHistory = new VenOrderStatusHistory();
				orderStatusHistory.setId(venOrderStatusHistoryPK);
				orderStatusHistory.setStatusChangeReason("Updated by System (Partial Fulfillment)");
				orderStatusHistory.setVenOrderStatus(statusFP);
				
				orderHistorySessionHome.persistVenOrderStatusHistory(orderStatusHistory);	
				_log.debug("done add order status history");
			} catch (Exception e) {
				String errMsg = "updateOrder: An Exception occured when merging VenOrder:" + e.getMessage();
				_log.error(errMsg);
				e.printStackTrace();
				throw new EJBException(errMsg);
			} finally{
				try{
					if(_genericLocator!=null){
						_genericLocator.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		_log.debug("updateOrder() completed in:" + duration + "ms");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * updateOrderStatus (com.gdn.integration.jaxb.Order)
	 */
	public Boolean updateOrderStatus(Order order) {
		_log.debug("start updateOrderStatus():" + order.getOrderId().getCode() + " new status:" + order.getStatus());
		Long startTime = System.currentTimeMillis();

		// Identifier
		if (order.getOrderId() == null) {
			String errMsg = "updateOrderStatus: message received with no identifier information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Timestamp
		if (order.getTimestamp() == null) {
			String errMsg = "updateOrderStatus: message received with no timestamp information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Status
		if (order.getStatus() == null) {
			String errMsg = "updateOrderStatus: message received with no status information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
				
		//retur
		if(order.getStatus().equals("R") || order.getStatus().equals("RF") || order.getStatus().equals("TS")){			
			_log.debug("updateReturStatus: "+ order.getOrderId().getCode());
		
			try {
				_genericLocator = new Locator<Object>();
				
				VenOrderStatusSessionEJBLocal orderStatusSessionHome= (VenOrderStatusSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderStatusSessionEJBLocal.class, "VenOrderStatusSessionEJBBeanLocal");
				VenReturSessionEJBLocal returHome = (VenReturSessionEJBLocal) this._genericLocator.lookupLocal(VenReturSessionEJBLocal.class, "VenReturSessionEJBBeanLocal");
				
				VenRetur venRetur = this.retrieveExistingRetur(order.getOrderId().getCode());
				
				if (venRetur == null) {
					String errMsg = "updateReturStatus: message received for retur that does not exist:" + order.getOrderId().getCode();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
				
				if(!venRetur.getVenReturStatus().getOrderStatusCode().equals("B") && !venRetur.getVenReturStatus().getOrderStatusCode().equals("TS")){
					String errMsg = "updateReturStatus: message received for retur that is not in B or TS status:" + order.getOrderId().getCode();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
				
				// Set the new timestamp value
				_log.debug("existing retur id: "+venRetur.getWcsReturId());
				venRetur.setReturTimestamp(com.djarum.raf.utilities.SQLDateUtility.utilDateToSqlTimestamp(XMLGregorianCalendarConverter.asDate(order.getTimestamp())));
				
				VenOrderStatus venReturStatusNew = new VenOrderStatus();
				
				List<VenOrderStatus> venOrderStatusList = orderStatusSessionHome.queryByRange("select o from VenOrderStatus o where o.orderStatusCode = '" + order.getStatus()+"'", 0, 0);
				venReturStatusNew.setOrderStatusId(venOrderStatusList.get(0).getOrderStatusId());
				venReturStatusNew.setOrderStatusCode(venOrderStatusList.get(0).getOrderStatusCode());
				venRetur.setVenReturStatus(venReturStatusNew);
				
				// If the RMA status is not null then set the RMA status and the RMA action				
				if (order.getRmaAction() != null && !order.getRmaAction().isEmpty()) {
					venRetur.setRmaAction(order.getRmaAction());
				}
				
				// Synchronize the reference data
				venRetur = this.synchronizeVenReturReferenceData(venRetur);
		
				_log.debug("merge retur status");				
				returHome.mergeVenRetur(venRetur);
				_log.debug("done merge retur status");		
				
				//add retur status history
				this.createReturStatusHistory(venRetur, venRetur.getVenReturStatus());
							
				// If the status of the retur changes then the retur item status must follow
				VenReturItemSessionEJBLocal returItemHome = (VenReturItemSessionEJBLocal) this._genericLocator.lookupLocal(VenReturItemSessionEJBLocal.class, "VenReturItemSessionEJBBeanLocal");
				List<VenReturItem> venReturItemList = returItemHome.queryByRange("select o from VenReturItem o where o.venRetur.returId = " + venRetur.getReturId(), 0, 0);
				_log.debug("venReturItemList size: "+venReturItemList.size());
				
				for (VenReturItem item : venReturItemList) {				
					_log.debug("set new order item: "+item.getWcsReturItemId()+", status: "+venRetur.getVenReturStatus().getOrderStatusCode());
					item.setVenReturStatus(venReturStatusNew);
					
					_log.debug("merge order item status");
					returItemHome.mergeVenReturItem(item);
					_log.debug("done merge order item status");
					
					//add retur item status history
					this.createReturItemStatusHistory(item, item.getVenReturStatus());
					
					if(order.getStatus().equals("R")){
						createDummyAirwaybillRetur(this._genericLocator, item);
					}
				}				
			} catch (Exception e) {
				String errMsg = "updateReturStatus: An Exception occured when accessing the VenReturSessionEJBBean:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg + e.getMessage());
			}			
			
			Long endTime = System.currentTimeMillis();
			Long duration = endTime - startTime;
			_log.debug("updateReturStatus() completed in:" + duration + "ms");
		}else{
			try {
				_genericLocator = new Locator<Object>();
				VenOrderStatusSessionEJBLocal orderStatusSessionHome = (VenOrderStatusSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderStatusSessionEJBLocal.class, "VenOrderStatusSessionEJBBeanLocal");
				VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
				
				//order
				_log.info("updateOrderStatus: "+ order.getOrderId().getCode());
				VenOrder venOrder = this.retreiveExistingOrder(order.getOrderId().getCode());
				if (venOrder == null) {
					String errMsg = "updateOrderStatus: message received for order that does not exist:" + order.getOrderId().getCode();
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}

				// Set the new timestamp value
				venOrder.setOrderTimestamp(com.djarum.raf.utilities.SQLDateUtility.utilDateToSqlTimestamp(XMLGregorianCalendarConverter.asDate(order.getTimestamp())));
						
				List<VenOrderStatus> venOrderStatusList = orderStatusSessionHome.queryByRange("select o from VenOrderStatus o where o.orderStatusCode = '" + order.getStatus()+"'", 0, 0);
				
				// Set the new status code
				VenOrderStatus venOrderStatusNew = new VenOrderStatus();
				venOrderStatusNew.setOrderStatusCode(venOrderStatusList.get(0).getOrderStatusCode());
				venOrderStatusNew.setOrderStatusId(venOrderStatusList.get(0).getOrderStatusId());
				venOrder.setVenOrderStatus(venOrderStatusNew);
				
				// If the RMA status is not null then set the RMA status and the RMA action
				if (order.isRmaFlag() != null) {
					venOrder.setRmaFlag(order.isRmaFlag());
				}
				if (order.getRmaAction() != null && !order.getRmaAction().isEmpty()) {
					venOrder.setRmaAction(order.getRmaAction());
				}
		
				// Synchronize the reference data
				venOrder = this.synchronizeVenOrderReferenceData(venOrder);
		
				_log.debug("merge order status");				
				orderHome.mergeVenOrder(venOrder);
				_log.debug("done merge order status");		
							
				// If the status of the order changes then the order item status must follow
				VenOrderItemSessionEJBLocal orderItemHome = (VenOrderItemSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
				List<VenOrderItem> venOrderItemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + venOrder.getOrderId(), 0, 0);
				_log.debug("venOrderItemList size: "+venOrderItemList.size());
							
				/*
				 * prepare Lombardi call for cancelled item
				 */
				Properties properties = new Properties();
				properties.load(new FileInputStream(BPMAdapter.WEBAPI_PROPERTIES_FILE));
				String userName = properties.getProperty("javax.xml.rpc.security.auth.username");		
				String password = BPMAdapter.getUserPasswordFromLDAP(userName);
				BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, password);
				bpmAdapter.synchronize();
				
				
				for (VenOrderItem item : venOrderItemList) {				
					_log.debug("set new order item: "+item.getWcsOrderItemId()+", status: "+venOrder.getVenOrderStatus().getOrderStatusCode());
					item.setVenOrderStatus(venOrderStatusNew);
					
					_log.debug("merge order item status");
					orderItemHome.mergeVenOrderItem(item);
					_log.debug("done merge order item status");
					
					if(venOrder.getVenOrderStatus().getOrderStatusCode().equals("X")){
						HashMap<String, String> taskData = new HashMap<String, String>();
						taskData.put(ORDERITEMID, item.getOrderItemId().toString());
						taskData.put(ORDERID, venOrder.getOrderId().toString());							

						try {
							_log.debug("\n Starting bpm " + FINANCEORDERITEMCANCELLEDNOTIFICATION);
							bpmAdapter.startBusinessProcess(FINANCEORDERITEMCANCELLEDNOTIFICATION, taskData);
							_log.debug("\n Done starting bpm " + taskData.toString());
						} catch (Exception e) {
							_log.error("\n Error when starting bpm");
							e.printStackTrace();
						}
					}		
				}
				/*
				 * tidak jadi diminta/permintaaan dibatalkan
				 */
				/*								
				if (venOrder.getVenOrderStatus().getOrderStatusId().equals(VeniceConstants.VEN_ORDER_STATUS_X)) {
							//read data fin ar fund in recon record by arifin
						_log.debug("Start update Order Payment cancel");
						_log.debug("read data fin ar fund in recon record");
						FinArFundsInReconRecordSessionEJBLocal fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) this._genericLocator.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");
						List<FinArFundsInReconRecord> reconRecordList = null;
						reconRecordList = fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where  o.wcsOrderId ='"+venOrder.getWcsOrderId()+"'", 0, 0);
						if (!reconRecordList.isEmpty() && reconRecordList!=null) {
							_log.debug("change Payment status");
							List<FinArFundsInReconRecord> newFinArFundsInReconRecordList = new ArrayList<FinArFundsInReconRecord>();
							for (FinArFundsInReconRecord item : reconRecordList) {		
								
								FinArFundsInActionApplied tempFinArFundsInActionApplied = new FinArFundsInActionApplied();
								tempFinArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED);
								item.setFinArFundsInActionApplied(tempFinArFundsInActionApplied);						
								newFinArFundsInReconRecordList.add(item);							
							}
							_log.debug("merge Payment status");
							fundsInReconRecordHome.mergeFinArFundsInReconRecordList(newFinArFundsInReconRecordList);			
							_log.debug("done merge Payment status");
						}else
							_log.debug("Payment is not found");					
				}*/
				
				Long endTime = System.currentTimeMillis();
				Long duration = endTime - startTime;
				_log.debug("updateOrderStatus() completed in:" + duration + "ms");
			} catch (Exception e) {
				String errMsg = "updateOrderStatus: An Exception occured when accessing the VenOrderSessionEJBBean:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg + e.getMessage());
			}
		}
		
		if(_genericLocator!=null){
			try {
				_genericLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * updateOrderItemStatus (com.gdn.integration.jaxb.Order)
	 */
	public Boolean updateOrderItemStatus(Order order) {
		_log.debug("updateOrderItemStatus()");
		Long startTime = System.currentTimeMillis();

		// Order Identifier
		if (order.getOrderId() == null) {
			String errMsg = "updateOrderItemStatus: message received with no identifier information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Timestamp
		if (order.getTimestamp() == null) {
			String errMsg = "updateOrderItemStatus: message received with no timestamp information record";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Order Items list null or empty
		if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
			String errMsg = "updateOrderItemStatus: message received with no order item information records";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Order Items list too big
		if (order.getOrderItems().size() > 1) {
			String errMsg = "updateOrderItemStatus: message received with too many (more than 1) order item information records";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// Order Item Identifier
		if (order.getOrderItems().get(0).getItemId().getCode() == null) {
			String errMsg = "updateOrderItemStatus: message received with no order item identifier (MasterData.code)";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		// The order must contain only one item for the PU status update
		if (order.getOrderItems().size() != 1) {
			String errMsg = "updateOrderItemStatus: message received an invalid number of order item information records - must be 1 and only 1";
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}
		
		// If retur item exist, update retur item, else update order item
		if (this.returItemExists(order.getOrderItems().get(0).getItemId().getCode())) {
			_log.info("update retur item status");
			// Check that there is distribution cart information for PU and CX status change messages
			if (order.getOrderItems().get(0).getStatus().equals("CX") || order.getOrderItems().get(0).getStatus().equals("PU")) {
				// Checks for PU status change messages
				if (order.getOrderItems().get(0).getStatus().equals("PU")) {
					// If the order item is PU then there must be settlement information. required for creating sales journal
					if (order.getOrderItems().get(0).getSettlementRecord() == null || order.getOrderItems().get(0).getSettlementRecord().getCommissionType() == null
							|| order.getOrderItems().get(0).getSettlementRecord().getCommissionType().isEmpty()) {
						String errMsg = "updateReturItemStatus: message received having an retur item status PU with no settlement information record";
						_log.error(errMsg);
						throw new EJBException(errMsg);
		
					}
					// Validate that the message contains the pickup instructions for the logistics provider
					if (order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction() == null 
							|| order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint() == null
							|| order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getPickupDateTime() == null) {
						String errMsg = "updateReturItemStatus: message received PU status change request for retur item that has incomplete merchant pickup instructions";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
		
					// Validate that the party details are provided for merchants
					if (order.getOrderItems().get(0).getProduct().getMerchant().getParty() == null
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getFullOrLegalName() == null
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getAddresses().isEmpty()
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getContacts().isEmpty()) {
						String errMsg = "updateReturItemStatus: message received PU status change request for retur item having incomplete merchant party/address/contact details";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
				}
			}
			
			// Fetch the retur from the cache and check that it exists
			VenRetur venRetur = this.retrieveExistingRetur(order.getOrderId().getCode());
			if (venRetur == null) {
				String errMsg = "updateReturItemStatus: message received for retur that does not exist:" + order.getOrderId().getCode();
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}
		
			try {
				_genericLocator = new Locator<Object>();
				VenReturItemSessionEJBLocal returItemHome = (VenReturItemSessionEJBLocal) this._genericLocator
						.lookupLocal(VenReturItemSessionEJBLocal.class, "VenReturItemSessionEJBBeanLocal");
				
				LogAirwayBillReturSessionEJBLocal airwayBillHome = (LogAirwayBillReturSessionEJBLocal) this._genericLocator
						.lookupLocal(LogAirwayBillReturSessionEJBLocal.class, "LogAirwayBillReturSessionEJBBeanLocal");
		
				LogMerchantPickupInstructionSessionEJBLocal pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBLocal) this._genericLocator
						.lookupLocal(LogMerchantPickupInstructionSessionEJBLocal.class, "LogMerchantPickupInstructionSessionEJBBeanLocal");
		
				// Get the Retur item to update using wcsReturItemId
				VenReturItem venReturItem = null;
		
				/*
				 * Fetch the order lines because we are using default loading for JPA relationships
				 */
				List<VenReturItem> venReturItemList = returItemHome.queryByRange("select o from VenReturItem o where o.venRetur.returId = " + venRetur.getReturId(), 0, 0);
		
				//Iterate and find the right retur item to operate on
				Iterator<VenReturItem> venReturItemIterator = venReturItemList.iterator();
				while (venReturItemIterator.hasNext()) {
					VenReturItem next = venReturItemIterator.next();
					if (next.getWcsReturItemId().equals(order.getOrderItems().get(0).getItemId().getCode())) {
						venReturItem = next;
					}
				}
		
				// ***** Case PU
				if (order.getOrderItems().get(0).getStatus().equals("PU")) {
					// Enforce the state transition rules
					Long orderItemStatus = venReturItem.getVenReturStatus().getOrderStatusId();
					if (!orderItemStatus.equals(VEN_ORDER_STATUS_R)) {
						String errMsg = "updateReturItemStatus: message received PU status change request for retur item that is not status R: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
		
					/*
					 * This is to prevent side effects because most of the
					 * merchant data is null at this point. The record will
					 * be mapped to the input message anyway and we only
					 * need the
					 */
					venReturItem.getVenMerchantProduct().getVenMerchant().setVenParty(null);
		
					VenOrderStatus venReturStatus = new VenOrderStatus();
					venReturStatus.setOrderStatusCode("PU");
					venReturStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_PU);
					venReturItem.setVenReturStatus(venReturStatus);
					
					// Synchronize the reference data
					venReturItem = this.synchronizeVenReturItemReferenceData(venReturItem);
		
					// Delete any existing merchant pickup instructions for the order item
					List<LogMerchantPickupInstruction> logMerchantPickupInstructionList = pickupInstructionHome
							.queryByRange("select o from LogMerchantPickupInstruction o where o.venReturItem.returItemId =" + venReturItem.getReturItemId(), 0, 0);
		
					if (!logMerchantPickupInstructionList.isEmpty()) {
						pickupInstructionHome.removeLogMerchantPickupInstructionList(logMerchantPickupInstructionList);
					}
		
					// Persist the merchant pickup instructions
					LogMerchantPickupInstruction logMerchantPickupInstruction = new LogMerchantPickupInstruction();
					logMerchantPickupInstruction.setVenReturItem(venReturItem);
					logMerchantPickupInstruction.setMerchantPic(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getMerchantPIC());
					logMerchantPickupInstruction.setMerchantPicPhone(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getMerchantPICPhone());
					logMerchantPickupInstruction.setPickupDateTime(com.djarum.raf.utilities.SQLDateUtility.utilDateToSqlTimestamp((com.djarum.raf.utilities.XMLGregorianCalendarConverter
						.asDate(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getPickupDateTime()))));
					logMerchantPickupInstruction.setSpecialHandlingInstructions(order.getOrderItems().get(0).getLogisticsInfo().getSpecialHandlingInstructions());
					
					VenAddress venAddress = new VenAddress();
		
					_mapper.map(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getPickupAddress(), venAddress);
		
					venAddress = this.persistAddress(venAddress);
					logMerchantPickupInstruction.setVenAddress(venAddress);
		
					VenMerchant venMerchant = venReturItem.getVenMerchantProduct().getVenMerchant();
					
					_mapper.map(order.getOrderItems().get(0).getProduct().getMerchant(), venMerchant);
		
					// Persist the merchant to create the merchant party and address records
					venMerchant = this.persistMerchant(venMerchant);
					logMerchantPickupInstruction.setVenMerchant(venMerchant);
		
					logMerchantPickupInstruction = pickupInstructionHome.persistLogMerchantPickupInstruction(logMerchantPickupInstruction);
		
					logMerchantPickupInstructionList = new ArrayList<LogMerchantPickupInstruction>();
					logMerchantPickupInstructionList.add(logMerchantPickupInstruction);
		
					/*
					 * add a dummy airway bill 
					 * 
					 * o Note that must do this here after merchant is mapped
					 * o Note that this happens only the first time it is PU
					 *   (because subsequent PU message - no need for dummy AWB)
					 */
					List<LogAirwayBillRetur> airwayBillReturList = new ArrayList<LogAirwayBillRetur>();
					List<LogAirwayBillRetur> airwayBillReturListTemp = new ArrayList<LogAirwayBillRetur>();
					
					//delete dummy airway bill from FP status first
					_log.debug("update/insert dummy airway bill from FP status first");
					
					LogAirwayBillRetur airwayBillRetur = new LogAirwayBillRetur();
					
					airwayBillReturListTemp=airwayBillHome.queryByRange("select o from LogAirwayBillRetur o where o.venReturItem.returItemId = " + venReturItem.getReturItemId(), 0, 0);
					if(airwayBillReturListTemp.size()>0){
						_log.debug("number of dummy airway bill to be updated: "+airwayBillReturListTemp.size());
						airwayBillRetur = airwayBillReturListTemp.get(0);						
					}else{
						_log.debug("dummy airway bill not found");
					}
					
					_log.debug("start insert new dummy airway bills with distribution cart info");
															
					airwayBillRetur.setVenReturItem(venReturItem);
					airwayBillRetur.setGdnReference("");
					airwayBillRetur.setSequence(1);
					airwayBillRetur.setShipper(venReturItem.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName());
					airwayBillRetur.setDestination(venReturItem.getVenAddress().getVenCity().getCityName());
					airwayBillRetur.setZip(venReturItem.getVenAddress().getPostalCode());
					airwayBillRetur.setLogLogisticsProvider(venReturItem.getLogLogisticService().getLogLogisticsProvider());
					airwayBillRetur.setService(venReturItem.getLogLogisticService().getServiceCode());
					airwayBillRetur.setInsuranceCharge(venReturItem.getInsuranceCost());
					airwayBillRetur.setPricePerKg(venReturItem.getLogisticsPricePerKg());
					airwayBillRetur.setGiftWrapCharge(venReturItem.getGiftWrapPrice());
					airwayBillRetur.setOrigin(venReturItem.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity().getCityName());
					
					BigDecimal correctShippingCostPerItem = new BigDecimal("0");
					correctShippingCostPerItem = correctShippingCostPerItem.setScale(0, RoundingMode.UP);
					correctShippingCostPerItem = airwayBillRetur.getVenReturItem().getShippingCost().subtract(airwayBillRetur.getVenReturItem().getShippingWeight().multiply(airwayBillRetur.getVenReturItem().getLogisticsPricePerKg()));
					
					BigDecimal totalCharge=new BigDecimal(0);
					if(airwayBillRetur.getVenReturItem()!=null && airwayBillRetur.getVenReturItem().getShippingCost()!=null){
						totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getShippingCost());
					}
					if(airwayBillRetur.getOtherCharge()!=null){
						totalCharge=totalCharge.add(airwayBillRetur.getOtherCharge());
					}
					if(airwayBillRetur.getInsuranceCharge()!=null){
						totalCharge=totalCharge.add(airwayBillRetur.getInsuranceCharge());
					}else{
						totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getInsuranceCost());
					}
					if(airwayBillRetur.getGiftWrapCharge()!=null){
						totalCharge=totalCharge.add(airwayBillRetur.getGiftWrapCharge());
					}else{
						totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getGiftWrapPrice());
					}
					airwayBillRetur.setTotalCharge(totalCharge);
					
					// Set the default approval status' for the AWB
					LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
					logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
					airwayBillRetur.setLogApprovalStatus1(logApprovalStatus);
					airwayBillRetur.setLogApprovalStatus2(logApprovalStatus);
					airwayBillRetur.setMtaData(false);
					
					// Calculate the sum insured if the item is insured
					if (venReturItem.getInsuranceCost().compareTo(new BigDecimal(0)) > 0) {
						_log.debug("The item is insured, so calculate insured amount");
						Double totalSumInsured = venReturItem.getTotal().doubleValue() + venReturItem.getGiftWrapPrice().doubleValue() + venReturItem.getShippingCost().doubleValue();
						airwayBillRetur.setInsuredAmount(new BigDecimal(totalSumInsured));
						_log.debug("insured amount is: "+totalSumInsured);
					} else {
						_log.debug("The item is not insured, so set insured amount to 0");
						airwayBillRetur.setInsuredAmount(new BigDecimal(0));
					}				
					airwayBillReturList.add(airwayBillRetur);					
		
					airwayBillHome. mergeLogAirwayBillReturList(airwayBillReturList);
		
					// Set the lists in the order item
					venReturItem.setLogMerchantPickupInstructions(logMerchantPickupInstructionList);
					venReturItem.setLogAirwayBillReturs(airwayBillReturList);
		
					// Apply the finance info delta if one exists
					if (order.getOrderItems().get(0).getFinanceInfoDelta() != null && (order.getOrderItems().get(0).getFinanceInfoDelta().getLogisticsInsuranceCost() > new Double(0))) {
						venReturItem.setInsuranceCost(venReturItem.getInsuranceCost().add(new BigDecimal(order.getOrderItems().get(0).getFinanceInfoDelta().getLogisticsInsuranceCost())));
					}
					if (order.getOrderItems().get(0).getFinanceInfoDelta() != null && (order.getOrderItems().get(0).getFinanceInfoDelta().getShippingCost() > new Double(0))) {
						venReturItem.setShippingCost(venReturItem.getShippingCost().add(new BigDecimal(order.getOrderItems().get(0).getFinanceInfoDelta().getShippingCost())));
					}
					returItemHome.mergeVenReturItem(venReturItem);
					this.createReturItemStatusHistory(venReturItem, venReturItem.getVenReturStatus());
				}
		
				// ***** Case CX
				if (order.getOrderItems().get(0).getStatus().equals("CX")) {
					/*
					 * Enforce the state transition rules 
					 * o note that must accept CX and D because report may come before and set status
					 */
					if (!venReturItem.getVenReturStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_ES)
							&& !venReturItem.getVenReturStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_CX)
							&& !venReturItem.getVenReturStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_D)) {
						String errMsg = "updateReturItemStatus: message received CX status change request for order item that is not status ES, CX, D: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}		
	
					List<LogAirwayBillRetur> logAirwayBillReturList = new ArrayList<LogAirwayBillRetur>();
					List<LogAirwayBillRetur> logAirwayBillReturTempList = airwayBillHome.queryByRange("select o from LogAirwayBillRetur o join fetch o.venReturItem where o.venReturItem.returItemId = " + venReturItem.getReturItemId(), 0, 1);
	
					LogAirwayBillRetur logAirwayBillRetur = logAirwayBillReturTempList.get(0);
	
					/*
					 * Receiving a CX status ALWAYS means that there is new logistics data from MTA so set the flag
					 */
					logAirwayBillRetur.setMtaData(true);
					
					// Make the pickup date/time the same as the timestamp
					logAirwayBillRetur.setAirwayBillPickupDateTime(logAirwayBillRetur.getAirwayBillTimestamp());
					logAirwayBillRetur.setVenReturItem(venReturItem);
	
					// The tracking number is actually resi-pickup...
					// alias delivery order or DO
					logAirwayBillRetur.setDeliveryOrder("");
					logAirwayBillRetur.setTrackingNumber("");
	
					/*
					 * Also set the AWB number here because it needs to be used for RPX reconciliation (only if it is null or empty).
					 *  
					 * o Note that this will never be written for NCS until the activity report comes
					 * o also note that it will never be overwritten for RPX when the activity report comes
					 */
					if (logAirwayBillRetur.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX") 
							|| logAirwayBillRetur.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
						if (logAirwayBillRetur.getAirwayBillNumber() == null || logAirwayBillRetur.getAirwayBillNumber().isEmpty()) {
							logAirwayBillRetur.setAirwayBillNumber("");
						}
					}
	
					logAirwayBillReturList.add(logAirwayBillRetur);
	
					// Persist the airway bill list
					venReturItem.setLogAirwayBillReturs(airwayBillHome.mergeLogAirwayBillReturList(logAirwayBillReturList));	
					
					venReturItem = this.synchronizeVenReturItemReferenceData(venReturItem);
																	
					// Merge the order item
					returItemHome.mergeVenReturItem(venReturItem);
					
					_log.debug("status after merge: "+venReturItem.getVenReturStatus().getOrderStatusCode());		
					_log.debug("add retur item status history CX MTA");
					_log.debug("\n wcs retur item id: "+venReturItem.getWcsReturItemId());
					VenReturItemStatusHistorySessionEJBLocal returItemHistorySessionHome = (VenReturItemStatusHistorySessionEJBLocal) this._genericLocator
						.lookupLocal(VenReturItemStatusHistorySessionEJBLocal.class, "VenReturItemStatusHistorySessionEJBBeanLocal");
					
					VenReturItemStatusHistoryPK venReturItemStatusHistoryPK = new VenReturItemStatusHistoryPK();
					venReturItemStatusHistoryPK.setReturItemId(venReturItem.getReturItemId());	
					
					Timestamp cxMtaDate = new Timestamp(System.currentTimeMillis());
					venReturItemStatusHistoryPK.setHistoryTimestamp(cxMtaDate);
					
					VenOrderStatus returStatusCXMTA = new VenOrderStatus();
					returStatusCXMTA.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
					
					VenReturItemStatusHistory returtemStatusHistory = new VenReturItemStatusHistory();
					returtemStatusHistory.setId(venReturItemStatusHistoryPK);
					returtemStatusHistory.setStatusChangeReason("Updated by System (CX MTA)");
					returtemStatusHistory.setVenReturStatus(returStatusCXMTA);
					
					returItemHistorySessionHome.persistVenReturItemStatusHistory(returtemStatusHistory);	
					_log.debug("done add retur item status history");				
				}
				
				venReturItem = returItemHome.mergeVenReturItem(venReturItem);
			} catch (Exception e) {
				String errMsg = "An Exception occured when accessing the updating the retur items:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg + e.getMessage());
			} finally{
				try{
					if(_genericLocator!=null){
						_genericLocator.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	
				Long endTime = System.currentTimeMillis();
				Long duration = endTime - startTime;
				_log.debug("updateReturItemStatus() completed in:" + duration + "ms");
		}else if(this.orderItemExists(order.getOrderItems().get(0).getItemId().getCode())){
			_log.info("update order item status");
			// Check that there is distribution cart information for PU and CX status change messages
			if (order.getOrderItems().get(0).getStatus().equals("CX") || order.getOrderItems().get(0).getStatus().equals("PU")) {
				// Checks for CX status change messages
				if (order.getOrderItems().get(0).getStatus().equals("CX")) {
					// If the order Item is CX then the order must contain transaction fees
					if (order.getTransactionFees() == null || order.getTransactionFees().isEmpty()) {
						String errMsg = "updateOrderItemStatus: message received having an order item status CX with no transaction fee information records";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					// Merchants for transaction fees must exist
					Iterator<TransactionFee> transactionFeeIterator = order.getTransactionFees().iterator();
					while (transactionFeeIterator.hasNext()) {
						TransactionFee next = transactionFeeIterator.next();
						if (!this.merchantExists(next.getMerchant().getMerchantId().getCode())) {
							String errMsg = "updateOrderItemStatus: message received having an order item status CX with transaction fees for a merchant that does not exist";
							_log.error(errMsg);
							throw new EJBException(errMsg);
						}
					}
					
					// If the order item is CX then there must be settlement information. required for creating sales journal
					if (order.getOrderItems().get(0).getSettlementRecord() == null || order.getOrderItems().get(0).getSettlementRecord().getCommissionType() == null
							|| order.getOrderItems().get(0).getSettlementRecord().getCommissionType().isEmpty()) {
						String errMsg = "updateOrderItemStatus: message received having an order item status CX with no settlement information record";
						_log.error(errMsg);
						throw new EJBException(errMsg);

					}
				}
				// Checks for PU status change messages
				if (order.getOrderItems().get(0).getStatus().equals("PU")) {
					// If the order item is PU then there must be settlement information. required for creating sales journal
					if (order.getOrderItems().get(0).getSettlementRecord() == null || order.getOrderItems().get(0).getSettlementRecord().getCommissionType() == null
							|| order.getOrderItems().get(0).getSettlementRecord().getCommissionType().isEmpty()) {
						String errMsg = "updateOrderItemStatus: message received having an order item status PU with no settlement information record";
						_log.error(errMsg);
						throw new EJBException(errMsg);

					}
					// Validate that the message contains the pickup instructions for the logistics provider
					if (order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction() == null 
							|| order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint() == null
							|| order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getPickupDateTime() == null) {
						String errMsg = "updateOrderItemStatus: message received PU status change request for order item that has incomplete merchant pickup instructions";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}

					// Validate that the party details are provided for merchants
					if (order.getOrderItems().get(0).getProduct().getMerchant().getParty() == null
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getFullOrLegalName() == null
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getAddresses().isEmpty()
							|| order.getOrderItems().get(0).getProduct().getMerchant().getParty().getContacts().isEmpty()) {
						String errMsg = "updateOrderItemStatus: message received PU status change request for order item having incomplete merchant party/address/contact details";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
				}
			}
			
			// Fetch the order from the cache and check that it exists
			VenOrder venOrder = this.retreiveExistingOrder(order.getOrderId().getCode());
			if (venOrder == null) {
				String errMsg = "updateOrderItemStatus: message received for order that does not exist:" + order.getOrderId().getCode();
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			// Check that the order status is FP (this is the only status that can e updated)
			if (venOrder.getVenOrderStatus().getOrderStatusId() != VEN_ORDER_STATUS_FP) {
				String errMsg = "updateOrderItemStatus: message received non FP order status. Order status must be FP for updateOrderItemStatus";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			/*
			 * Status Change actions for ORDER ITEM
			 * -------------------------------------------------------------------
			 * Case PU o Change the status so it gets picked up by the batch job o
			 * Insert the transaction fees for the order o Insert the distribution
			 * carts for the order item
			 * 
			 * Case CX o Insert the airway bill with the tracking number only o
			 * Insert the settlement records against the order items If there is
			 * existing AWB data from a provider report then reconcile the data.
			 * 
			 * Case D o Ignore because logistics needs to verify
			 * 
			 * Case Big Product / BOPIS o Just change the status so logistics will skip it
			 * 
			 * Case PP o Just change the status so that Venice will wait
			 * 
			 * Case RM o Just change the status so that Venice will wait o KPI
			 * clocking needs to be done using the postPersist callback handler
			 * 
			 * Case RL o Just change the status so that Venice will wait o KPI
			 * clocking needs to be done using the postPersist callback handler
			 * 
			 * Case PF o Just change the status so that Venice will wait o KPI
			 * clocking needs to be done using the postPersist callback handler
			 * 
			 * Case OS o Just change the status so that Venice will wait updateOrder 
			 * from WCS and delete the item 
			 */
			try {
				_genericLocator = new Locator<Object>();
				VenOrderItemSessionEJBLocal orderItemHome = (VenOrderItemSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
				
				VenOrderItemAdjustmentSessionEJBLocal orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderItemAdjustmentSessionEJBLocal.class, "VenOrderItemAdjustmentSessionEJBBeanLocal");

				LogAirwayBillSessionEJBLocal airwayBillHome = (LogAirwayBillSessionEJBLocal) this._genericLocator
						.lookupLocal(LogAirwayBillSessionEJBLocal.class, "LogAirwayBillSessionEJBBeanLocal");

				VenSettlementRecordSessionEJBLocal settlementRecordHome = (VenSettlementRecordSessionEJBLocal) this._genericLocator
						.lookupLocal(VenSettlementRecordSessionEJBLocal.class, "VenSettlementRecordSessionEJBBeanLocal");

				LogMerchantPickupInstructionSessionEJBLocal pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBLocal) this._genericLocator
						.lookupLocal(LogMerchantPickupInstructionSessionEJBLocal.class, "LogMerchantPickupInstructionSessionEJBBeanLocal");

				LogActivityReportUploadSessionEJBLocal activityReportHome = (LogActivityReportUploadSessionEJBLocal) this._genericLocator
						.lookupLocal(LogActivityReportUploadSessionEJBLocal.class, "LogActivityReportUploadSessionEJBBeanLocal");

				// Get the order item to update using wcsOrderItemId
				VenOrderItem venOrderItem = null;

				/*
				 * Fetch the order lines because we are using default loading for JPA relationships
				 */
				List<VenOrderItem> venOrderItemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + venOrder.getOrderId(), 0, 0);

				//Iterate and find the right order item to operate on
				Iterator<VenOrderItem> venOrderItemIterator = venOrderItemList.iterator();
				while (venOrderItemIterator.hasNext()) {
					VenOrderItem next = venOrderItemIterator.next();
					if (next.getWcsOrderItemId().equals(order.getOrderItems().get(0).getItemId().getCode())) {
						venOrderItem = next;
					}
				}

				// ***** Case PU
				if (order.getOrderItems().get(0).getStatus().equals("PU")) {
					// Enforce the state transition rules
					Long orderItemStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
					if (!orderItemStatus.equals(VEN_ORDER_STATUS_FP)
							&& !orderItemStatus.equals(VEN_ORDER_STATUS_RL)
							&& !orderItemStatus.equals(VEN_ORDER_STATUS_PP)
							&& !orderItemStatus.equals(VEN_ORDER_STATUS_RM)
							&& !orderItemStatus.equals(VEN_ORDER_STATUS_RV)) {
						String errMsg = "updateOrderItemStatus: message received PU status change request for order item that is not status PP, FP, RL, RM, RV: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}

					/*
					 * This is to prevent side effects because most of the
					 * merchant data is null at this point. The record will
					 * be mapped to the input message anyway and we only
					 * need the
					 */
					venOrderItem.getVenMerchantProduct().getVenMerchant().setVenParty(null);

					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("PU");
					venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_PU);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);

					/*
					 * Any existing distribution carts for the order item must be
					 * deleted because the item may have been PU then PP then RM/RL
					 * etc. and PU again.
					 * 
					 * Note that the delete cascade relationships will handle the
					 * children but if an airway bill has been reconciled then it
					 * cannot be deleted.
					 */

					// Delete any existing merchant pickup instructions for the order item
					List<LogMerchantPickupInstruction> logMerchantPickupInstructionList = pickupInstructionHome
							.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId =" + venOrderItem.getOrderItemId(), 0, 0);

					if (!logMerchantPickupInstructionList.isEmpty()) {
						pickupInstructionHome.removeLogMerchantPickupInstructionList(logMerchantPickupInstructionList);
					}

					// Persist the merchant pickup instructions
					LogMerchantPickupInstruction logMerchantPickupInstruction = new LogMerchantPickupInstruction();
					logMerchantPickupInstruction.setVenOrderItem(venOrderItem);
					logMerchantPickupInstruction.setMerchantPic(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getMerchantPIC());
					logMerchantPickupInstruction.setMerchantPicPhone(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getMerchantPICPhone());
					logMerchantPickupInstruction.setPickupDateTime(com.djarum.raf.utilities.SQLDateUtility.utilDateToSqlTimestamp((com.djarum.raf.utilities.XMLGregorianCalendarConverter
						.asDate(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getPickupDateTime()))));
					logMerchantPickupInstruction.setSpecialHandlingInstructions(order.getOrderItems().get(0).getLogisticsInfo().getSpecialHandlingInstructions());
					VenAddress venAddress = new VenAddress();

					_mapper.map(order.getOrderItems().get(0).getLogisticsInfo().getMerchantPickupInstruction().getMerchantPickupPoint().getPickupAddress(), venAddress);

					venAddress = this.persistAddress(venAddress);
					logMerchantPickupInstruction.setVenAddress(venAddress);

					VenMerchant venMerchant = venOrderItem.getVenMerchantProduct().getVenMerchant();
					
					_mapper.map(order.getOrderItems().get(0).getProduct().getMerchant(), venMerchant);

					// Persist the merchant to create the merchant party and address records
					venMerchant = this.persistMerchant(venMerchant);
					logMerchantPickupInstruction.setVenMerchant(venMerchant);

					logMerchantPickupInstruction = pickupInstructionHome.persistLogMerchantPickupInstruction(logMerchantPickupInstruction);

					logMerchantPickupInstructionList = new ArrayList<LogMerchantPickupInstruction>();
					logMerchantPickupInstructionList.add(logMerchantPickupInstruction);

					/*
					 * For each distribution cart in the list add a dummy airway bill 
					 * 
					 * o Note that must do this here after merchant is mapped
					 * o Note that this happens only the first time it is PU
					 *   (because subsequent PU message - no need for dummy AWB)
					 */
					List<LogAirwayBill> airwayBillList = new ArrayList<LogAirwayBill>();
					List<LogAirwayBill> airwayBillListTemp = new ArrayList<LogAirwayBill>();
					
					//delete dummy airway bill from FP status first
					_log.debug("update/insert dummy airway bill from FP status first");
					
					LogAirwayBill airwayBill = new LogAirwayBill();
					
					airwayBillListTemp=airwayBillHome.queryByRange("select o from LogAirwayBill o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
					if(airwayBillListTemp.size()>0){
						_log.debug("number of dummy airway bill to be updated: "+airwayBillListTemp.size());
						airwayBill = airwayBillListTemp.get(0);
					}else{
						_log.debug("dummy airway bill not found");
					}
					
					_log.debug("start insert new dummy airway bills with distribution cart info");
															
					airwayBill.setVenOrderItem(venOrderItem);
					airwayBill.setGdnReference("");
					airwayBill.setSequence(1);
					airwayBill.setShipper(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName());
					airwayBill.setDestination(venOrderItem.getVenAddress().getVenCity().getCityName());
					airwayBill.setZip(venOrderItem.getVenAddress().getPostalCode());
					airwayBill.setLogLogisticsProvider(venOrderItem.getLogLogisticService().getLogLogisticsProvider());
					airwayBill.setService(venOrderItem.getLogLogisticService().getServiceCode());
					airwayBill.setInsuranceCharge(venOrderItem.getInsuranceCost());
					airwayBill.setPricePerKg(venOrderItem.getLogisticsPricePerKg());
					airwayBill.setGiftWrapCharge(venOrderItem.getGiftWrapPrice());
					airwayBill.setOrigin(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity().getCityName());
					
					BigDecimal correctShippingCostPerItem = new BigDecimal("0");
					correctShippingCostPerItem = correctShippingCostPerItem.setScale(0, RoundingMode.UP);
					correctShippingCostPerItem = airwayBill.getVenOrderItem().getShippingCost().subtract(airwayBill.getVenOrderItem().getShippingWeight().multiply(airwayBill.getVenOrderItem().getLogisticsPricePerKg()));
					
					BigDecimal totalCharge=new BigDecimal(0);
					if(airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getShippingCost()!=null){
						totalCharge=totalCharge.add(airwayBill.getVenOrderItem().getShippingCost());
					}
					if(airwayBill.getOtherCharge()!=null){
						totalCharge=totalCharge.add(airwayBill.getOtherCharge());
					}
					if(airwayBill.getInsuranceCharge()!=null){
						totalCharge=totalCharge.add(airwayBill.getInsuranceCharge());
					}else{
						totalCharge=totalCharge.add(airwayBill.getVenOrderItem().getInsuranceCost());
					}
					if(airwayBill.getGiftWrapCharge()!=null){
						totalCharge=totalCharge.add(airwayBill.getGiftWrapCharge());
					}else{
						totalCharge=totalCharge.add(airwayBill.getVenOrderItem().getGiftWrapPrice());
					}
					airwayBill.setTotalCharge(totalCharge);
					
					// Set the default approval status' for the AWB
					LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
					logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
					airwayBill.setLogApprovalStatus1(logApprovalStatus);
					airwayBill.setLogApprovalStatus2(logApprovalStatus);
					airwayBill.setMtaData(false);
					
					// Calculate the sum insured if the item is insured
					if (venOrderItem.getInsuranceCost().compareTo(new BigDecimal(0)) > 0) {
						_log.debug("The item is insured, so calculate insured amount");
						Double totalSumInsured = venOrderItem.getTotal().doubleValue() + venOrderItem.getGiftWrapPrice().doubleValue() + venOrderItem.getShippingCost().doubleValue();
						airwayBill.setInsuredAmount(new BigDecimal(totalSumInsured));
						_log.debug("insured amount is: "+totalSumInsured);
					} else {
						_log.debug("The item is not insured, so set insured amount to 0");
						airwayBill.setInsuredAmount(new BigDecimal(0));
					}				
					airwayBillList.add(airwayBill);					

					// Merge the dummy airway bills from PU status
					_log.debug("Merge the dummy airway bills from PU status");
					airwayBillHome. mergeLogAirwayBillList(airwayBillList);

					// Set the lists in the order item
					venOrderItem.setLogMerchantPickupInstructions(logMerchantPickupInstructionList);
					venOrderItem.setLogAirwayBills(airwayBillList);

					// Apply the finance info delta if one exists
					if (order.getOrderItems().get(0).getFinanceInfoDelta() != null && (order.getOrderItems().get(0).getFinanceInfoDelta().getLogisticsInsuranceCost() > new Double(0))) {
						venOrderItem.setInsuranceCost(venOrderItem.getInsuranceCost().add(new BigDecimal(order.getOrderItems().get(0).getFinanceInfoDelta().getLogisticsInsuranceCost())));
					}
					if (order.getOrderItems().get(0).getFinanceInfoDelta() != null && (order.getOrderItems().get(0).getFinanceInfoDelta().getShippingCost() > new Double(0))) {
						venOrderItem.setShippingCost(venOrderItem.getShippingCost().add(new BigDecimal(order.getOrderItems().get(0).getFinanceInfoDelta().getShippingCost())));
					}
					orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
					
					// Persist the settlement record
					VenSettlementRecord venSettlementRecord = new VenSettlementRecord();
					_mapper.map(order.getOrderItems().get(0).getSettlementRecord(), venSettlementRecord);

					// Link it back to the order item
					venSettlementRecord.setVenOrderItem(venOrderItem);

					// Persist the settlement record
					venSettlementRecord = settlementRecordHome.persistVenSettlementRecord(venSettlementRecord);

					// Set the settlement record back with the order item
					List<VenSettlementRecord> venSettlementRecordList = new ArrayList<VenSettlementRecord>();
					venSettlementRecordList.add(venSettlementRecord);
					venOrderItem.setVenSettlementRecords(venSettlementRecordList);
					
					/*
					 * Clock the KPI for merchant fullfillment time
					 */
					//this.clockMerchantFulfillmentResponseKpi(_genericLocator, venOrderItem, logMerchantPickupInstruction);
				}

				// ***** Case CX
				if (order.getOrderItems().get(0).getStatus().equals("CX")) {
					/*
					 * Enforce the state transition rules 
					 * o note that must accept CX and D because report may come before and set status
					 */
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_ES)
							&& !venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_BP)
							&& !venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_CX)
							&& !venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PP)
							&& !venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_D)) {
						String errMsg = "updateOrderItemStatus: message received CX status change request for order item that is not status ES, CX, PP, D or BP: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}

					/*
					 * Check if the existing order item status is Big Product / BOPIS 
					 * If it is Big Product / BOPIS then there will be no logistics information
					 */
					boolean isBigProductOrBp = false;
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_BP)) {
						_log.info("Received CX for order item not Big product / BP: "+venOrderItem.getWcsOrderItemId());

						List<LogAirwayBill> logAirwayBillList = new ArrayList<LogAirwayBill>();

						LogAirwayBill logAirwayBill = new LogAirwayBill();
						List<LogAirwayBill> logAirwayBillTempList = airwayBillHome.queryByRange("select o from LogAirwayBill o join fetch o.venOrderItem where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 1);

						logAirwayBill = logAirwayBillTempList.get(0);

						/*
						 * If there is existing data in the AWB that has been
						 * saved after a 3PL report has been uploaded then it
						 * must be reconciled against the new AWB data from MTA
						 * in this message. 
						 * 
						 * o We can tell by looking at the mtaData flag (false) and the status 
						 * o We need to find the last report that was processed for each (NASTY) and link to it
						 * 
						 * Note that it could be an activity report, an invoice report or both
						 */
						if ((logAirwayBill.getMtaData() != null && !logAirwayBill.getMtaData()) && ((logAirwayBill.getActivityFileNameAndLoc() != null && !logAirwayBill.getActivityFileNameAndLoc().isEmpty())
								|| (logAirwayBill.getLogInvoiceAirwaybillRecord() != null && logAirwayBill.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getFileNameAndLocation() != null && !logAirwayBill.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getFileNameAndLocation().isEmpty()))) {

							// Create a new AWB to hold the MTA data for reconciliation and stuff it with the data
							LogAirwayBill mtaAirwayBill = new LogAirwayBill();

							mtaAirwayBill.setLogLogisticsProvider(venOrderItem.getLogLogisticService().getLogLogisticsProvider());
							LogApprovalStatus activityApprovalStatus = new LogApprovalStatus();
							activityApprovalStatus.setApprovalStatusId(new Long(0));
							LogApprovalStatus invoiceApprovalStatus = new LogApprovalStatus();
							invoiceApprovalStatus.setApprovalStatusId(new Long(0));
							mtaAirwayBill.setLogApprovalStatus1(invoiceApprovalStatus);
							mtaAirwayBill.setLogApprovalStatus2(activityApprovalStatus);
							mtaAirwayBill.setAirwayBillTimestamp(null);							
							mtaAirwayBill.setAirwayBillPickupDateTime(logAirwayBill.getAirwayBillPickupDateTime());
							mtaAirwayBill.setActualPickupDate(null);
							mtaAirwayBill.setVenOrderItem(venOrderItem);
							mtaAirwayBill.setDeliveryOrder("");	
							mtaAirwayBill.setAirwayBillNumber(logAirwayBill.getAirwayBillNumber()!=null?logAirwayBill.getAirwayBillNumber():"");
							mtaAirwayBill.setTrackingNumber("");
							
							/*
							 * If the activity report requires reconciliation then
							 * reconcile the report with the MTA data
							 */
							if(logAirwayBill.getActivityResultStatus() != null && ! logAirwayBill.getActivityResultStatus().isEmpty()){
								// Last report must be an activity report
								List<LogActivityReportUpload> activityReportList = activityReportHome.queryByRange("select o from LogActivityReportUpload o where o.fileNameAndLocation = '" + logAirwayBill.getActivityFileNameAndLoc() + "'", 0, 0);
								LogActivityReportUpload activityReport = activityReportList.get(0);

								
								if(emForJDBC == null)
									emForJDBC = emf.createEntityManager();
								
								@SuppressWarnings("deprecation")
								Connection conn =  ((EntityManagerImpl) emForJDBC).getSession().connection();
								
								/* 
								 * Perform the reconciliation with existing
								 * activity report data
								 */

								AWBReconciliation awbReconciliation = new AWBReconciliation();
								mtaAirwayBill = awbReconciliation.performActivityReconciliation(conn,activityReport, logAirwayBill, mtaAirwayBill, venOrderItem.getWcsOrderItemId());
								logAirwayBill.setActivityResultStatus(mtaAirwayBill.getActivityResultStatus());
															
								//if activityResultStatus OK, then set to approved, this will trigger CX finance.
								_log.debug("activity result status: "+mtaAirwayBill.getActivityResultStatus());
								if(mtaAirwayBill.getActivityResultStatus().equals("OK")){
									_log.debug("activity result status OK, set approval status to approved to trigger CX finance");
									LogApprovalStatus activityStatusApproved = new LogApprovalStatus();
									activityStatusApproved.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
									logAirwayBill.setLogApprovalStatus2(activityStatusApproved);
									logAirwayBill.setActivityApprovedByUserId("System");
								}
								
								conn.close();
								conn = null;
								
//								// BPM notification - call Lombardi
//								try {
//									Properties properties = new Properties();
//									properties.load(new FileInputStream(BPMAdapter.WEBAPI_PROPERTIES_FILE));
	//
//									String userName = properties.getProperty("javax.xml.rpc.security.auth.username");
//									BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
//									bpmAdapter.synchronize();
	//
//									HashMap<String, String> taskData = new HashMap<String, String>();
//									taskData.put(AIRWAYBILLID, logAirwayBill.getAirwayBillId().toString());
//									
//									_log.debug("Task data:" + taskData);
	//
//									bpmAdapter.startBusinessProcess(LOGISTICSMTADATAACTIVITYRECONCILIATION, taskData);
//									_log.info("A BPM process has been started to notify AWB activity reconciliation");
//								} catch (Exception e) {
//									_log.error("An exception occured when starting a BPM process to notify AWB activity reconciliation on CX");
//									e.printStackTrace();
//								}
							}
							
							logAirwayBill.setMtaData(true);
						} else {
							/*
							 * The else case is for data from MTA with no prior activity or invoice report data (only a dummy AWB from PU status)
							 */

//							logAirwayBill.setLogLogisticsProvider(venOrderItem.getLogLogisticService().getLogLogisticsProvider());

							LogApprovalStatus activityApprovalStatus = new LogApprovalStatus();
							activityApprovalStatus.setApprovalStatusId(new Long(0));
							LogApprovalStatus invoiceApprovalStatus = new LogApprovalStatus();
							invoiceApprovalStatus.setApprovalStatusId(new Long(0));
							logAirwayBill.setLogApprovalStatus1(invoiceApprovalStatus);
							logAirwayBill.setLogApprovalStatus2(activityApprovalStatus);

							// Make the pickup date/time the same as the timestamp
							logAirwayBill.setAirwayBillPickupDateTime(logAirwayBill.getAirwayBillTimestamp());

							logAirwayBill.setVenOrderItem(venOrderItem);

							// The tracking number is actually resi-pickup...
							// alias delivery order or DO
							logAirwayBill.setDeliveryOrder("");
							logAirwayBill.setTrackingNumber("");

							/*
							 * Also set the AWB number here because it needs to be used for RPX reconciliation (only if it is null or empty).
							 *  
							 * o Note that this will never be written for NCS until the activity report comes
							 *  
							 * o also note that it will never be overwritten for RPX when the activity report comes
							 */
							if (logAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX") 
									|| logAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
								if (logAirwayBill.getAirwayBillNumber() == null || logAirwayBill.getAirwayBillNumber().isEmpty()) {
									logAirwayBill.setAirwayBillNumber("");
								}
							}
							/*
							 * Receiving a CX status ALWAYS means that there is new logistics data from MTA so set the flag
							 */
							logAirwayBill.setMtaData(true);
						}

						logAirwayBillList.add(logAirwayBill);

						// Persist the airway bill list
						venOrderItem.setLogAirwayBills(airwayBillHome.mergeLogAirwayBillList(logAirwayBillList));								
					}else{
						//for order item status BP, update the status to CX before updating it to D

						_log.info("Received CX for order item Big Product / BP: "+venOrderItem.getWcsOrderItemId());
						
						isBigProductOrBp = true;
						
						_log.debug("persist the settlement record");
						List<VenSettlementRecord> venSettlementRecordList = new ArrayList<VenSettlementRecord>();
						VenSettlementRecord venSettlementRecord = new VenSettlementRecord();
						
						_mapper.map(order.getOrderItems().get(0).getSettlementRecord(), venSettlementRecord);

						venSettlementRecord.setVenOrderItem(venOrderItem);

						venSettlementRecord = settlementRecordHome.persistVenSettlementRecord(venSettlementRecord);
						_log.debug("settlement code: " + venSettlementRecord.getSettlementCode());
						_log.debug("settlement type: " + venSettlementRecord.getSettlementRecordType());
						_log.debug("commision type: " + venSettlementRecord.getCommissionType());
						
						venSettlementRecordList.add(venSettlementRecord);
						venOrderItem.setVenSettlementRecords(venSettlementRecordList);
						
						VenOrderStatus statusCX = new VenOrderStatus();
						statusCX.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
						statusCX.setOrderStatusCode("CX");
						venOrderItem.setVenOrderStatus(statusCX);				
					}								
					
					//jika status PP, dan terima settlement dari mta, maka status kembali ke ES.
					if(venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PP)){
						_log.info("set order status back to ES (PP to CX from MTA)");
						VenOrderStatus venOrderStatus = new VenOrderStatus();
						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_ES);
						venOrderStatus.setOrderStatusCode("ES");
						venOrderItem.setVenOrderStatus(venOrderStatus);
					}

					/*
					 * Synchronize the reference data (merge the status later after
					 * the ) other child objects etc. have been merged to ensure
					 * that there are no outbound synchronization issues.
					 */
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);	
					
					// Merge the order item
					orderItemHome.mergeVenOrderItem(venOrderItem);
					
					_log.debug("status after merge: "+venOrderItem.getVenOrderStatus().getOrderStatusCode());
					
					/*
					 * Sales record creation has been moved to separated batch 
					 */
					//if order item status BP, create sales record
//					if(isBigProductOrBp){
//						_log.info("Prepare to create sales record for order item Big product / BP: "+venOrderItem.getWcsOrderItemId());
//						SalesRecordGenerator salesRecordGenerator = new SalesRecordGenerator();
//						boolean result = salesRecordGenerator.createOrUpdateSalesRecord(venOrderItem);
//						
//						if(result==true){
//							_log.info("sales record created for order item Big product / BP: "+venOrderItem.getWcsOrderItemId());
//						}else{
//							_log.info("sales record creation failed for order item Big product / BP: "+venOrderItem.getWcsOrderItemId());
//						}
//					}
					
					//set transaction fee
					setTransactionFee(venOrder, venOrderItem, order);
					
					_log.debug("add order item status history CX MTA");
					_log.debug("\n wcs order item id: "+venOrderItem.getWcsOrderItemId());
					VenOrderItemStatusHistorySessionEJBLocal orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderItemStatusHistorySessionEJBLocal.class, "VenOrderItemStatusHistorySessionEJBBeanLocal");
					
					VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
					venOrderItemStatusHistoryPK.setOrderItemId(venOrderItem.getOrderItemId());	
					
					Timestamp cxMtaDate = new Timestamp(System.currentTimeMillis());
					venOrderItemStatusHistoryPK.setHistoryTimestamp(cxMtaDate);
					
					VenOrderStatus orderStatusCXMTA = new VenOrderStatus();
					orderStatusCXMTA.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
					
					VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
					orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
					orderItemStatusHistory.setStatusChangeReason("Updated by System (CX MTA)");
					orderItemStatusHistory.setVenOrderStatus(orderStatusCXMTA);
					
					orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);	
					_log.debug("done add order item status history");
					
					//add cx mta date to sales record
					_log.debug("check sales record exist");
					FinSalesRecordSessionEJBLocal salesRecordSessionHome = (FinSalesRecordSessionEJBLocal) this._genericLocator
					.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");
					
					List<FinSalesRecord> finSalesRecords = salesRecordSessionHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 1);
					
					if(finSalesRecords.size()>0){
						_log.debug("sales record found, add CX MTA date in sales record");
						FinSalesRecord finSalesRecord=finSalesRecords.get(0);
						finSalesRecord.setCxMtaDate(cxMtaDate);
						
						salesRecordSessionHome.mergeFinSalesRecord(finSalesRecord);
					}					
				}

				// ***** Case Big Product / BOPIS
				if (order.getOrderItems().get(0).getStatus().equals("BP")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_FP)) {
						String errMsg = "updateOrderItemStatus: message received Big Product / BOPIS status change request for order item that is not status FP: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					
					VenOrderItem orderItemInbound = new VenOrderItem();
					_mapper.map(order.getOrderItems().get(0), orderItemInbound);
					venOrderItem.setMerchantCourier(orderItemInbound.getMerchantCourier());	
					if(orderItemInbound.getMerchantDeliveredDateStart()!=null) venOrderItem.setMerchantDeliveredDateStart(orderItemInbound.getMerchantDeliveredDateStart());
					if(orderItemInbound.getMerchantDeliveredDateStart()!=null) venOrderItem.setMerchantDeliveredDateEnd(orderItemInbound.getMerchantDeliveredDateEnd());
					if(orderItemInbound.getMerchantInstallationDateStart()!=null) venOrderItem.setMerchantInstallationDateStart(orderItemInbound.getMerchantInstallationDateStart());
					if(orderItemInbound.getMerchantDeliveredDateEnd()!=null) venOrderItem.setMerchantInstallationDateEnd(orderItemInbound.getMerchantInstallationDateEnd());
					venOrderItem.setMerchantInstallOfficer(orderItemInbound.getMerchantInstallOfficer());
					venOrderItem.setMerchantInstallMobile(orderItemInbound.getMerchantInstallMobile());
					venOrderItem.setMerchantInstallNote(orderItemInbound.getMerchantInstallNote());
					
					_log.debug("merchantCourier: "+venOrderItem.getMerchantCourier());
					_log.debug("merchantDeliveredDateStart: "+venOrderItem.getMerchantDeliveredDateStart());
					_log.debug("merchantDeliveredDateEnd: "+venOrderItem.getMerchantDeliveredDateEnd());
					_log.debug("merchantInstallationDateStart: "+venOrderItem.getMerchantInstallationDateStart());
					_log.debug("merchantInstallationDateEnd: "+venOrderItem.getMerchantInstallationDateEnd());
					_log.debug("merchantInstallOfficer: "+venOrderItem.getMerchantInstallOfficer());
					_log.debug("merchantInstallMobile: "+venOrderItem.getMerchantInstallMobile());
					_log.debug("merchantInstallNote: "+venOrderItem.getMerchantInstallNote());
					
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("BP");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_BP);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
				}

				// ***** Case PP
				if (order.getOrderItems().get(0).getStatus().equals("PP")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PU)
							&& !venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_ES)) {
						String errMsg = "updateOrderItemStatus: message received PP status change request for order item that is not status PU or ES: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("PP");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_PP);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					
					//for PP add order item history from callback, because it can be from mta and venice.
//					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
				}

				// ***** Case RM
				if (order.getOrderItems().get(0).getStatus().equals("RM")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PP)) {
						String errMsg = "updateOrderItemStatus: message received RM status change request for order item that is not status PP: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("RM");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_RM);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
				}

				// ***** Case RL
				if (order.getOrderItems().get(0).getStatus().equals("RL")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PP)) {
						String errMsg = "updateOrderItemStatus: message received RL status change request for order item that is not status PP: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("RL");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_RL);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
				}

				// ***** Case PF
				if (order.getOrderItems().get(0).getStatus().equals("PF")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_FP)) {
						String errMsg = "updateOrderItemStatus: message received PF status change request for order item that is not status FP: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("PF");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_PF);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					
					List<VenOrderItem> venOrderItems = venOrder.getVenOrderItems();
					List<VenOrderItemAdjustment> venOrderItemAdjustmentNewSpreadList = new ArrayList<VenOrderItemAdjustment>();
					_log.debug("Total Order Item : " + venOrderItems.size());
					
					BigDecimal totalPromo = new BigDecimal(0);
					
					for(VenOrderItem venOrderItem2 : venOrderItems){
						BigDecimal currentItemShippingAndInsuranceCost = venOrderItem2.getShippingCost().add(venOrderItem2.getInsuranceCost());
						
						String query = "select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + venOrderItem2.getOrderItemId();
						
						List<VenOrderItemAdjustment> venOrderItemAdjustmentList = orderItemAdjustmentHome.queryByRange(query, 0, 0);
						
						_log.debug("Total Free Shipping " + currentItemShippingAndInsuranceCost);
						_log.debug("Total Adjustment for Order Item " + venOrderItem2.getOrderItemId() + " : " + venOrderItemAdjustmentList.size());
						
						for(VenOrderItemAdjustment venOrderItemAdjustment:venOrderItemAdjustmentList){
							_log.debug("Adjustment " + venOrderItemAdjustment.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustment.getVenOrderItem().getOrderItemId() + " : " + venOrderItemAdjustment.getAmount());
							
							// sum a not free shipping adjustment
							if(currentItemShippingAndInsuranceCost.compareTo(venOrderItemAdjustment.getAmount().abs()) != 0){
								totalPromo = totalPromo.add(venOrderItemAdjustment.getAmount());
							}
							
							// add non free shipping promo for item not cancelled to list
							if(currentItemShippingAndInsuranceCost.compareTo(venOrderItemAdjustment.getAmount().abs()) != 0 &&
								venOrderItemAdjustment.getVenOrderItem().getOrderItemId() != venOrderItem.getOrderItemId()){
								
								venOrderItemAdjustmentNewSpreadList.add(venOrderItemAdjustment);	
							}
							
							// set adjustment amount to Rp 0 for cancelled item
							if(venOrderItemAdjustment.getVenOrderItem().getOrderItemId() == venOrderItem.getOrderItemId()){
								_log.debug("Merging Adjustment " + venOrderItemAdjustment.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustment.getVenOrderItem().getOrderItemId());
								
								venOrderItemAdjustment.setAmount(new BigDecimal(0));
								orderItemAdjustmentHome.mergeVenOrderItemAdjustment(venOrderItemAdjustment);
							}						
						}
					}
					
					int totalOrderItemAfterPF =  venOrderItems.size() - 1;
					BigDecimal adjustmentSpread = new BigDecimal(0);
					
					if(totalOrderItemAfterPF > 0){
						adjustmentSpread = totalPromo.divide(new BigDecimal(totalOrderItemAfterPF), 2, RoundingMode.HALF_UP);
					}
					
					_log.debug("Total Promo : " + totalPromo);
					_log.debug("Total Order Item : " + totalOrderItemAfterPF);
					_log.debug("Adjustment Spread : " + adjustmentSpread);
					
					//merge all non free shipping adjustment with the new adjustment spread
					for(VenOrderItemAdjustment venOrderItemAdjustmentNewSpread : venOrderItemAdjustmentNewSpreadList){
						venOrderItemAdjustmentNewSpread.setAmount(adjustmentSpread);
						
						_log.debug("Merging Adjustment " + venOrderItemAdjustmentNewSpread.getVenPromotion().getPromotionId() + "," + venOrderItemAdjustmentNewSpread.getVenOrderItem().getOrderItemId());
						
						orderItemAdjustmentHome.mergeVenOrderItemAdjustment(venOrderItemAdjustmentNewSpread);
					}
									
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
//					this.clockMerchantPartialFulfillmentKpi(this._genericLocator, venOrderItem);
				}
				
				// ***** Case OS
				if (order.getOrderItems().get(0).getStatus().equals("OS")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_PU)) {
						String errMsg = "updateOrderItemStatus: message received OS status change request for order item that is not status PU: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusCode("OS");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_OS);
					venOrderItem.setVenOrderStatus(venOrderStatus);

					// Synchronize the reference data then merge the status
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					
					venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
					this.createOrderItemStatusHistory(venOrderItem, venOrderItem.getVenOrderStatus());
				}

				// ***** Case D
				if (order.getOrderItems().get(0).getStatus().equals("D")) {
					// Enforce the state transition rules
					if (!venOrderItem.getVenOrderStatus().getOrderStatusId().equals(VEN_ORDER_STATUS_CX)) {
						String errMsg = "updateOrderItemStatus: message received D status change request for order item that is not status CX: illegal state transition";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_D);
					venOrderStatus.setOrderStatusCode("D");
					venOrderStatus.setOrderStatusId(VEN_ORDER_STATUS_D);
					venOrderItem.setVenOrderStatus(venOrderStatus);
					
					//set receiver, received date, dan status untuk BP
					VenOrderItem orderItemInbound = new VenOrderItem();
					_mapper.map(order.getOrderItems().get(0), orderItemInbound);

					_log.debug("logistic provider: "+venOrderItem.getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode());
					if(venOrderItem.getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderId().equals(VeniceConstants.VEN_LOGISTICS_PROVIDER_BIGPRODUCT)){
						_log.debug("set receiver, received date, relation for big product");
						if(orderItemInbound.getDeliveryReceivedDate()!=null){							
							venOrderItem.setDeliveryReceivedDate(orderItemInbound.getDeliveryReceivedDate());
							_log.debug("received date: "+venOrderItem.getDeliveryReceivedDate());
						}
						
						venOrderItem.setDeliveryRecipientName(orderItemInbound.getDeliveryRecipientName());
						_log.debug("recipient: "+venOrderItem.getDeliveryRecipientName());
							
						venOrderItem.setDeliveryRecipientStatus(orderItemInbound.getDeliveryRecipientStatus());
						_log.debug("relation: "+venOrderItem.getDeliveryRecipientStatus());
											
						venOrderItem.setTrackingNumber(orderItemInbound.getTrackingNumber());
						_log.debug("no tanda terima: "+venOrderItem.getTrackingNumber());	
					}		
				}
				
				// Synchronize the reference data then merge the status
				venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
				venOrderItem = orderItemHome.mergeVenOrderItem(venOrderItem);
			} catch (Exception e) {
				String errMsg = "An Exception occured when accessing the updating the order items:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg + e.getMessage());
			} finally{
				try{
					if(_genericLocator!=null){
						_genericLocator.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			Long endTime = System.currentTimeMillis();
			Long duration = endTime - startTime;
			_log.debug("updateOrderItemStatus() completed in:" + duration + "ms");
		}else{
			String errMsg = "updateOrderItemStatus: message received for an order/retur item that does not exist:" + order.getOrderItems().get(0).getItemId();
			_log.error(errMsg);
			throw new EJBException(errMsg);
		}

		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#
	 * notifyProductStockLevel (com.gdn.integration.jaxb.MerchantProduct)
	 */
	public Boolean notifyProductStockLevel(MerchantProduct merchantProduct) {
		_log.debug("notifyProductStockLevel()");
		Long startTime = System.currentTimeMillis();
		try {
			// Map the jaxb MerchantProduct object to a JPA VenMerchantProduct
			// object.
			_log.debug("Mapping the merchantProduct object to the venMerchantProduct object...");
			VenMerchantProduct venMerchantProduct = new VenMerchantProduct();
			_mapper.map(merchantProduct, venMerchantProduct);

			// Persist the merchant product
			this.persistMerchantProduct(venMerchantProduct);

		} catch (Exception e) {
			String errMsg = "An Exception occured when persisting data for notifyProductStockLevel:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);

		}
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		_log.debug("notifyProductStockLevel() completed in:" + duration + "ms");
		return Boolean.TRUE;
	}

	/**
	 * Returns true if the order already exists in the cache else false
	 * 
	 * @param wcsOrderId
	 * @return true if the order already exists in the cache else false
	 */
	private Boolean orderExists(String wcsOrderId) {
		if (this.retreiveExistingOrder(wcsOrderId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Retreives an existing order from the cache
	 * 
	 * @param wcsOrderId
	 * @return an existing order or null if it doesn't exist
	 */
	private VenOrder retreiveExistingOrder(String wcsOrderId) {
		try {
			VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
			List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.wcsOrderId ='" + wcsOrderId + "'", 0, 1);
			if (venOrderList != null && !venOrderList.isEmpty()) {
				return venOrderList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenOrderSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	/**
	 * Retreives an existing order from the cache
	 * 
	 * @param wcsOrderId
	 * @return an existing order or null if it doesn't exist
	 */
	private VenRetur retrieveExistingRetur(String wcsReturId) {
		try {
			VenReturSessionEJBLocal returHome = (VenReturSessionEJBLocal) this._genericLocator
					.lookupLocal(VenReturSessionEJBLocal.class, "VenReturSessionEJBBeanLocal");
			List<VenRetur> venReturList = returHome.queryByRange("select o from VenRetur o where o.wcsReturId ='" + wcsReturId + "'", 0, 1);
			if (venReturList != null && !venReturList.isEmpty()) {
				return venReturList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenReturSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Returns true if the order item already exists in the cache else false
	 * 
	 * @param wcsOrderId
	 * @return true if the order already exists in the cache else false
	 */
	private Boolean orderItemExists(String wcsOrderItemId) {
		if (this.retrieveExistingOrderItem(wcsOrderItemId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	/**
	 * Returns true if the retur item already exists in the cache else false
	 * 
	 * @param wcsReturId
	 * @return true if the retur already exists in the cache else false
	 */
	private Boolean returItemExists(String wcsReturItemId) {
		if (this.retreiveExistingReturItem(wcsReturItemId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Retreives an existing order item from the cache
	 * 
	 * @param wcsOrderItemId
	 * @return an existing order item or null if it doesn't exist
	 */
	private VenOrderItem retrieveExistingOrderItem(String wcsOrderItemId) {
		/*
		try {
			VenOrderItemSessionEJBLocal orderItemHome = (VenOrderItemSessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
			List<VenOrderItem> venOrderItemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.wcsOrderItemId ='" + wcsOrderItemId + "'", 0, 1);
			if (venOrderItemList != null && !venOrderItemList.isEmpty()) {
				return venOrderItemList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenOrderItemSessionEJBBean to retrieve an order item:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		*/
		List<VenOrderItem> venOrderItemList = venOrderItemDAO.findByWcsOrderItemId(wcsOrderItemId);
		return (((venOrderItemList != null) && (!(venOrderItemList.isEmpty()))) ? venOrderItemList.get(0) : null);
	}
	
	/**
	 * Retreives an existing retur item from the cache
	 * 
	 * @param wcsReturItemId
	 * @return an existing retur item or null if it doesn't exist
	 */
	private VenReturItem retreiveExistingReturItem(String wcsReturItemId) {
		try {
			VenReturItemSessionEJBLocal returItemHome = (VenReturItemSessionEJBLocal) this._genericLocator
					.lookupLocal(VenReturItemSessionEJBLocal.class, "VenReturItemSessionEJBBeanLocal");
			List<VenReturItem> venReturItemList = returItemHome.queryByRange("select o from VenReturItem o where o.wcsReturItemId ='" + wcsReturItemId + "'", 0, 1);
			if (venReturItemList != null && !venReturItemList.isEmpty()) {
				return venReturItemList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenReturItemSessionEJBBean to retrieve an retur item:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Retrives a transaction fee record if one exists
	 * 
	 * @param venOrder
	 * @param venMerchant
	 * @return returns the existing transaction fee record otherwise null
	 */
	VenTransactionFee retrieveExistingTransactionFee(VenOrder venOrder, VenMerchant venMerchant) {
		_log.debug("retrieve existing transaction fee");
		try {
			VenTransactionFeeSessionEJBLocal transactionFeeHome = (VenTransactionFeeSessionEJBLocal) this._genericLocator
					.lookupLocal(VenTransactionFeeSessionEJBLocal.class, "VenTransactionFeeSessionEJBBeanLocal");
			List<VenTransactionFee> venTransactionFeeList = transactionFeeHome
					.queryByRange("select o from VenTransactionFee o where o.venOrder.orderId ="+ venOrder.getOrderId() + " and o.venMerchant.merchantId ="+ venMerchant.getMerchantId(), 0, 1);
			if (venTransactionFeeList == null || venTransactionFeeList.isEmpty()) {
				_log.debug("no existing transaction fee");
				return null;
			} else {
				_log.debug("there is existing transaction fee");
				return venTransactionFeeList.get(0);
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenTransactionFeeSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	List<FinSalesRecord> retrieveExistingSalesRecord(VenOrderItem venOrderItem) {
		try {
			FinSalesRecordSessionEJBLocal salesHome = (FinSalesRecordSessionEJBLocal) this._genericLocator
					.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");
			List<FinSalesRecord> salesRecordList = salesHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId ="+ venOrderItem.getOrderItemId(), 0, 1);
			if (salesRecordList == null || salesRecordList.isEmpty()) {
				return null;
			} else {
				return salesRecordList;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the FinSalesRecordSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Returns true if the payment already exists in the cache else false
	 * 
	 * @param wcsPaymentId
	 * @return true if the payment already exists in the cache else false
	 */
	private Boolean orderPaymentExists(String wcsPaymentId) {
		if (this.retreiveExistingOrderPayment(wcsPaymentId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Retreives an existing payment from the cache
	 * 
	 * @param wcsPaymentId
	 * @return an existing payment or null if it doesn't exist
	 */
	private VenOrderPayment retreiveExistingOrderPayment(String wcsPaymentId) {
		try {
			VenOrderPaymentSessionEJBLocal orderPaymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
			List<VenOrderPayment> venOrderPaymentList = orderPaymentHome
					.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='" + wcsPaymentId + "'", 0, 1);
			if (venOrderPaymentList != null && !venOrderPaymentList.isEmpty()) {
				return venOrderPaymentList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenOrderPaymentSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Returns true if the merchant already exists in the cache else false
	 * 
	 * @param wcsPaymentId
	 * @return true if the merchant already exists in the cache else false
	 */
	private Boolean merchantExists(String wcsMerchantId) {
		if (this.retrieveExistingMerchant(wcsMerchantId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Retreives an existing merchant from the cache
	 * 
	 * @param wcsMerchantId
	 * @return
	 */
	private VenMerchant retrieveExistingMerchant(String wcsMerchantId) {
		try {
			VenMerchantSessionEJBLocal merchantHome = (VenMerchantSessionEJBLocal) this._genericLocator
					.lookupLocal(VenMerchantSessionEJBLocal.class, "VenMerchantSessionEJBBeanLocal");
			List<VenMerchant> merchantList = merchantHome
					.queryByRange("select o from VenMerchant o where o.wcsMerchantId ='" + wcsMerchantId + "'", 0, 1);
			if (merchantList != null && !merchantList.isEmpty()) {
				return merchantList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenMerchantSessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Persists the order using the session tier.
	 * 
	 * @return the persisted object
	 */
	private VenOrder persistOrder(Boolean vaPaymentExists, Boolean csPaymentExists, VenOrder venOrder) {
		_log.debug("vaPaymentExists: "+vaPaymentExists);
		_log.debug("csPaymentExists: "+csPaymentExists);
		if (venOrder != null) {
			try {
				_log.debug("Persisting VenOrder... :" + venOrder.getWcsOrderId());				
				
				// Save the order items before persisting as it will be detached
				List<VenOrderItem> venOrderItemList = venOrder.getVenOrderItems();

				// Detach the order items prior to persisting the order.
				venOrder.setVenOrderItems(null);
				
				// Detach the order payment allocations
				// Note that these will be allocated at 100% of the order price later when processing payments
				venOrder.setVenOrderPaymentAllocations(null);

				// Detach the transaction fees list
				venOrder.setVenTransactionFees(null);
				// Detach the customer first then persist and re-attach
				VenCustomer customer = venOrder.getVenCustomer();
				venOrder.setVenCustomer(null);

				if(venOrder.getVenOrderBlockingSource().getBlockingSourceId()== null && venOrder.getVenOrderBlockingSource().getBlockingSourceDesc()==null)
					venOrder.setVenOrderBlockingSource(null);
									
				// Persist the customer
				venOrder.setVenCustomer(this.persistCustomer(customer));
				
				VenAddress orderAddress = new VenAddress();
				orderAddress = this.persistAddress(venOrder.getVenCustomer().getVenParty().getVenPartyAddresses().get(0).getVenAddress());
				
				// Synchronize the reference data
				venOrder = this.synchronizeVenOrderReferenceData(venOrder);
				
				VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
				
				VenOrderAddressSessionEJBLocal orderAddressHome = (VenOrderAddressSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderAddressSessionEJBLocal.class, "VenOrderAddressSessionEJBBeanLocal");
				
				VenOrderContactDetailSessionEJBLocal orderContactDetailHome = (VenOrderContactDetailSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderContactDetailSessionEJBLocal.class, "VenOrderContactDetailSessionEJBBeanLocal");

				// If a VA payment exists then merge else persist the order
				if (vaPaymentExists || csPaymentExists) {
					_log.debug("masuk merge di persist");
					venOrder = (VenOrder) orderHome.mergeVenOrder(venOrder);
				} else {
					_log.debug("masuk persist di persist");
					venOrder = (VenOrder) orderHome.persistVenOrder(venOrder);
				}
				//add order status history
				this.createOrderStatusHistory(venOrder, venOrder.getVenOrderStatus());
				/*
				 * Persist the order items regardless of if it is VA or not
				 * because if there has been a VA payment then the items will
				 * not be in the cache anyway.
				 */
				_log.debug("\n venOrder.wcsOrderId: "+venOrder.getWcsOrderId());
				venOrder.setVenOrderItems(this.persistOrderItemList(venOrder, venOrderItemList));
				
				/*
				 * Tally Order with customer address and contact details
				 * defined in the ref tables VenOrderAddress and VenOrderContactDetail
				 */				
				if(orderAddress!=null){
					VenOrderAddress venOrderAddress = new VenOrderAddress();
					venOrderAddress.setVenOrder(venOrder);
					venOrderAddress.setVenAddress(orderAddress);
						
					_log.debug("Persist VenOrderAddress");
					// persist VenOrderAddress
					orderAddressHome.persistVenOrderAddress(venOrderAddress);
				}else{
					_log.debug("customer address is null");
				}
				
				List<VenOrderContactDetail> venOrderContactDetailList = new ArrayList<VenOrderContactDetail>();
				
				List<VenContactDetail> venContactDetailList = venOrder.getVenCustomer().getVenParty().getVenContactDetails();
				if(venContactDetailList != null){				
					for (VenContactDetail venContactDetail:venContactDetailList){
						VenOrderContactDetail venOrderContactDetail = new VenOrderContactDetail();
						venOrderContactDetail.setVenOrder(venOrder);
						venOrderContactDetail.setVenContactDetail(venContactDetail);
						
						venOrderContactDetailList.add(venOrderContactDetail);
					}
					
					_log.debug("Total VenOrderContactDetail to be persisted => " + venOrderContactDetailList.size());
					// persist VenOrderContactDetail
					orderContactDetailHome.persistVenOrderContactDetailList(venOrderContactDetailList);
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrder:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return venOrder;
	}

	/**
	 * Synchronizes the reference data for the direct VenOrder references
	 * 
	 * @param venOrder
	 * @return the synchronized data object
	 */
	private VenOrder synchronizeVenOrderReferenceData(VenOrder venOrder) {
		List<Object> references = new ArrayList<Object>();
		references.add(venOrder.getVenOrderBlockingSource());
		references.add(venOrder.getVenOrderStatus());

		// Synchronize the data references
		List<Object> synchronizedReferences = synchronizeReferenceData(references);

		// Push the keys back into the order record
		Iterator<Object> referencesIterator = synchronizedReferences.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenOrderBlockingSource) {
				venOrder.setVenOrderBlockingSource((VenOrderBlockingSource) next);
			} else if (next instanceof VenOrderStatus) {
				venOrder.setVenOrderStatus((VenOrderStatus) next);
			}
		}
		return venOrder;
	}

	/**
	 * Synchronizes the reference data for the direct VenRetur references
	 * 
	 * @param venOrder
	 * @return the synchronized data object
	 */
	private VenRetur synchronizeVenReturReferenceData(VenRetur venRetur) {
		List<Object> references = new ArrayList<Object>();
		references.add(venRetur.getVenReturStatus());

		// Synchronize the data references
		List<Object> synchronizedReferences = synchronizeReferenceData(references);

		// Push the keys back into the order record
		Iterator<Object> referencesIterator = synchronizedReferences.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenOrderStatus) {
				venRetur.setVenReturStatus((VenOrderStatus) next);
			}
		}
		return venRetur;
	}
	
	private VenCustomer persistCustomer(VenCustomer venCustomer) {
		if (venCustomer != null) {
			try {
				_log.debug("Persisting VenCustomer... :" + venCustomer.getCustomerUserName());
				VenCustomerSessionEJBLocal customerHome = (VenCustomerSessionEJBLocal) this._genericLocator
						.lookupLocal(VenCustomerSessionEJBLocal.class, "VenCustomerSessionEJBBeanLocal");
				// If the customer already exists then return it, else persist everything
				List<VenCustomer> venCustomerList = customerHome.queryByRange( "select o from VenCustomer o where o.wcsCustomerId = '" + venCustomer.getWcsCustomerId() + "'", 0, 0);
				if (venCustomerList != null && !venCustomerList.isEmpty()) {
					venCustomer.setCustomerId(venCustomerList.get(0).getCustomerId());
				}

				VenPartyType venPartyType = new VenPartyType();
				// Set the party type to Customer
				venPartyType.setPartyTypeId(new Long(4));
				venPartyType.setPartyTypeDesc("Customer");
				venCustomer.getVenParty().setVenPartyType(venPartyType);
				
				VenParty party = venCustomer.getVenParty();
				party.setVenCustomers(new ArrayList<VenCustomer>());
				party.getVenCustomers().add(0, venCustomer);
				venCustomer.setVenParty(this.persistParty(party, "Customer"));
				// Synchronize the reference data
				venCustomer = this.synchronizeVenCustomerReferenceData(venCustomer);

				// Persist the object
				VenCustomer customer = venCustomer;
				venCustomer = customerHome.mergeVenCustomer(venCustomer);
				venCustomer.setVenParty(customer.getVenParty());
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenCustomer:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return venCustomer;
	}

	/**
	 * Synchronizes the data for the direct VenCustomer references
	 * 
	 * @param venCustomer
	 * @return the synchronized data object
	 */
	private VenCustomer synchronizeVenCustomerReferenceData(
			VenCustomer venCustomer) {

		List<Object> references = new ArrayList<Object>();
		references.add(venCustomer.getVenParty());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenParty) {
				venCustomer.setVenParty((VenParty) next);
			}
		}
		return venCustomer;
	}

	/**
	 * Persists a list of order payments using the session tier.
	 * 
	 * @param orderPayments
	 * @return the persisted object
	 */
	private List<VenOrderPayment> persistOrderPaymentList(List<VenOrderPayment> venOrderPaymentList) {
		List<VenOrderPayment> newVenOrderPaymentList = new ArrayList<VenOrderPayment>();
		if (venOrderPaymentList != null && !venOrderPaymentList.isEmpty()) {
			try {
				_log.debug("Persisting VenOrderPayment list...:"+ venOrderPaymentList.size());
				Iterator<VenOrderPayment> i = venOrderPaymentList.iterator();
				while (i.hasNext()) {
					VenOrderPayment next = i.next();
					
					// Detach the allocations before persisting
					List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = (List<VenOrderPaymentAllocation>)next.getVenOrderPaymentAllocations();
					
					next.setVenOrderPaymentAllocations(null);

					// Synchronize the references
					next = this.synchronizeVenOrderPaymentReferenceData(next);

					// Persist the billing address
					next.setVenAddress(this.persistAddress(next.getVenAddress()));

					VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
					
					/*
					 * Check to see if the payment is already in the cache and
					 * if it is then assume it is a VA payment and should not be
					 * changed because it was APPROVED by Venice
					 */
					List<VenOrderPayment> paymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId = '" + next.getWcsPaymentId() + "'", 0, 1);

					if (paymentList.isEmpty()) {
						_log.debug("Payment not found so persisting it...");
						// Persist the object
						newVenOrderPaymentList.add((VenOrderPayment) paymentHome.persistVenOrderPayment(next));
						// Persist the allocations
						next.setVenOrderPaymentAllocations(this.persistOrderPaymentAllocationList(venOrderPaymentAllocationList));
					} else {
						// Persist the allocations
						next.setVenOrderPaymentAllocations(this.persistOrderPaymentAllocationList(venOrderPaymentAllocationList));
						// Just put it back into the new list
						newVenOrderPaymentList.add(next);
					}
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderItem:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
			return newVenOrderPaymentList;
		}
		return venOrderPaymentList;
	}

	/**
	 * Persists the payment allocation list to the cache
	 * 
	 * @param venOrderPaymentAllocationList
	 * @return
	 */
	private List<VenOrderPaymentAllocation> persistOrderPaymentAllocationList(List<VenOrderPaymentAllocation> venOrderPaymentAllocationList) {
		List<VenOrderPaymentAllocation> newVenOrderPaymentAllocationList = new ArrayList<VenOrderPaymentAllocation>();
		if (venOrderPaymentAllocationList != null	&& !venOrderPaymentAllocationList.isEmpty()) {
			try {
				_log.debug("Persisting VenOrderPaymentAllocation list...:"+ venOrderPaymentAllocationList.size());
				Iterator<VenOrderPaymentAllocation> i = venOrderPaymentAllocationList.iterator();
				while (i.hasNext()) {
					VenOrderPaymentAllocation next = i.next();
					_log.debug("value of paymentAllocation ......: order_id = "+ next.getVenOrder().getOrderId() +" and wcs_code_payment = "+next.getVenOrderPayment().getWcsPaymentId());
					// Persist the object
					VenOrderPaymentAllocationSessionEJBLocal paymentAllocationHome = (VenOrderPaymentAllocationSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class,"VenOrderPaymentAllocationSessionEJBBeanLocal");
					newVenOrderPaymentAllocationList.add((VenOrderPaymentAllocation) paymentAllocationHome.mergeVenOrderPaymentAllocation(next));
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderPaymentAllocation:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}else{
			_log.debug("Persisting VenOrderPaymentAllocation list is null");
		}
		return newVenOrderPaymentAllocationList;
	}

	/**
	 * Removes all of the payment allocations for an order
	 * 
	 * @param venOrder
	 * @return true if the operation succeeds else false
	 */
	private Boolean removeOrderPaymentAllocationList(VenOrder venOrder) {
		try {
			_log.debug("Remove Order Payment Allocation List ...:order id= "+venOrder.getOrderId()+" and wcs Order Id= "+venOrder.getWcsOrderId());
			VenOrderPaymentAllocationSessionEJBLocal paymentAllocationHome = (VenOrderPaymentAllocationSessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class, "VenOrderPaymentAllocationSessionEJBBeanLocal");
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = paymentAllocationHome
					.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId =" + venOrder.getOrderId(), 0, 1000);
			paymentAllocationHome
					.removeVenOrderPaymentAllocationList(venOrderPaymentAllocationList);
		} catch (Exception e) {
			String errMsg = "An exception occured when persisting VenOrderPaymentAllocation:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		}
		return Boolean.TRUE;
	}

	/**
	 * Synchronizes the data for the direct VenOrderPayment references
	 * 
	 * @param venOrderPayment
	 * @return the synchronized data object
	 */
	private VenOrderPayment synchronizeVenOrderPaymentReferenceData(
			VenOrderPayment venOrderPayment) {

		List<Object> references = new ArrayList<Object>();
		references.add(venOrderPayment.getVenBank());
		references.add(venOrderPayment.getVenPaymentStatus());
		references.add(venOrderPayment.getVenPaymentType());
		references.add(venOrderPayment.getVenAddress());
		references.add(venOrderPayment.getVenWcsPaymentType());
		references.add(venOrderPayment.getOldVenOrder());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenBank) {
				venOrderPayment.setVenBank((VenBank) next);
			} else if (next instanceof VenPaymentStatus) {
				venOrderPayment.setVenPaymentStatus((VenPaymentStatus) next);
			} else if (next instanceof VenPaymentType) {
				venOrderPayment.setVenPaymentType((VenPaymentType) next);
			} else if (next instanceof VenAddress) {
				venOrderPayment.setVenAddress((VenAddress) next);
			} else if (next instanceof VenWcsPaymentType) {
				venOrderPayment.setVenWcsPaymentType((VenWcsPaymentType) next);
			} else if (next instanceof VenOrder) {
				venOrderPayment.setOldVenOrder((VenOrder) next);
			}
		}
		return venOrderPayment;
	}

	/**
	 * Persists a list of order items using the session tier.
	 * 
	 * @param venOrderLineList
	 * @return the persisted object
	 */
	List<VenOrderItem> persistOrderItemList(VenOrder venOrder, List<VenOrderItem> venOrderItemList) {
		List<VenOrderItem> newVenOrderItemList = new ArrayList<VenOrderItem>();
		if (venOrderItemList != null && !venOrderItemList.isEmpty()) {
			try {
				_log.debug("Persisting VenOrderItem list...:" + venOrderItemList.size());
				Iterator<VenOrderItem> i = venOrderItemList.iterator();
				
				// Synchronize the references before persisting anything
				while(i.hasNext()){
					VenOrderItem venOrderItem = i.next();
					venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
				}
				
				//Main processing loop
				i = venOrderItemList.iterator();
				while (i.hasNext()) {
					VenOrderItem orderItem = i.next();

					// Attach the order
					orderItem.setVenOrder(venOrder);

					// Detach the marginPromo before persisting
					List<VenOrderItemAdjustment> venOrderItemAdjustmentList = (List<VenOrderItemAdjustment>)orderItem.getVenOrderItemAdjustments();
					orderItem.setVenOrderItemAdjustments(null);

					// Detach the pickup instructions before persisting
					orderItem.setLogMerchantPickupInstructions(null);

					// Persist the shipping address
					orderItem.setVenAddress(this.persistAddress(orderItem.getVenAddress()));

					// Persist the recipient
					orderItem.setVenRecipient(this.persistRecipient(orderItem.getVenRecipient()));
					
					// Adjust the shipping weight because it comes across as the
					// product shipping weight
					orderItem.setShippingWeight(new BigDecimal(orderItem.getShippingWeight().doubleValue() * orderItem.getQuantity()));

					// Persist the object
					VenOrderItemSessionEJBLocal itemHome = (VenOrderItemSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
					orderItem = itemHome.persistVenOrderItem(orderItem);					

					/*
					 * Tally Order Item with recipient address and contact details
					 * defined in the ref tables VenOrderItemAddress and VenOrderItemContactDetail
					 */
					
					VenOrderItemAddressSessionEJBLocal itemAddressHome = (VenOrderItemAddressSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemAddressSessionEJBLocal.class, "VenOrderItemAddressSessionEJBBeanLocal");
					
					VenOrderItemContactDetailSessionEJBLocal itemContactDetailHome = (VenOrderItemContactDetailSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemContactDetailSessionEJBLocal.class, "VenOrderItemContactDetailSessionEJBBeanLocal");
					
					List<VenOrderItemContactDetail> venOrderItemContactDetailList = new ArrayList<VenOrderItemContactDetail>();
					
					VenOrderItemAddress venOrderItemAddress = new VenOrderItemAddress();
					
					venOrderItemAddress.setVenOrderItem(orderItem);
					venOrderItemAddress.setVenAddress(orderItem.getVenAddress());

					_log.debug("persisting  VenOrderItemAddress" );
					// persist VenOrderItemAddress
					itemAddressHome.persistVenOrderItemAddress(venOrderItemAddress);
					
					List<VenContactDetail> venContactDetailList = orderItem.getVenRecipient().getVenParty().getVenContactDetails();
					for (VenContactDetail venContactDetail:venContactDetailList){
						VenOrderItemContactDetail venOrderItemContactDetail = new VenOrderItemContactDetail();
						venOrderItemContactDetail.setVenOrderItem(orderItem);
						venOrderItemContactDetail.setVenContactDetail(venContactDetail);
						
						venOrderItemContactDetailList.add(venOrderItemContactDetail);
					}
					
					_log.debug("Total VenOrderItemContactDetail to be persisted => " + venOrderItemContactDetailList.size());
					// persist VenOrderContactDetail
					itemContactDetailHome.persistVenOrderItemContactDetailList(venOrderItemContactDetailList);
					
					//add order item history
					this.createOrderItemStatusHistory(orderItem, venOrder.getVenOrderStatus());

					// Persist the marginPromo
					orderItem.setVenOrderItemAdjustments(this.persistOrderItemAdjustmentList(orderItem, venOrderItemAdjustmentList));
					
					newVenOrderItemList.add(orderItem);
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderItem:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();				
				throw new EJBException(errMsg);
			}
		}
		return newVenOrderItemList;
	}

	/**
	 * Synchronizes the reference data for the direct VenOrderItem references
	 * 
	 * @param venOrderItem
	 * @return the synchronized data object
	 */
	private VenOrderItem synchronizeVenOrderItemReferenceData(
			VenOrderItem venOrderItem) {

		List<Object> references = new ArrayList<Object>();
		references.add(venOrderItem.getLogLogisticService());
		references.add(venOrderItem.getVenMerchantProduct());
		references.add(venOrderItem.getVenOrderStatus());

		// Synchronize the data references
		List<Object> synchronizedReferences = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = synchronizedReferences.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof LogLogisticService) {
				venOrderItem.setLogLogisticService((LogLogisticService) next);
			} else if (next instanceof VenMerchantProduct) {
				venOrderItem.setVenMerchantProduct((VenMerchantProduct) next);
			} else if (next instanceof VenOrderStatus) {
				venOrderItem.setVenOrderStatus((VenOrderStatus) next);
			}
		}
		return venOrderItem;
	}
	
	/**
	 * Synchronizes the reference data for the direct VenReturItem references
	 * 
	 * @param venReturItem
	 * @return the synchronized data object
	 */
	private VenReturItem synchronizeVenReturItemReferenceData(
			VenReturItem venReturItem) {

		List<Object> references = new ArrayList<Object>();
		references.add(venReturItem.getLogLogisticService());
		references.add(venReturItem.getVenMerchantProduct());
		references.add(venReturItem.getVenReturStatus());

		// Synchronize the data references
		List<Object> synchronizedReferences = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = synchronizedReferences.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof LogLogisticService) {
				venReturItem.setLogLogisticService((LogLogisticService) next);
			} else if (next instanceof VenMerchantProduct) {
				venReturItem.setVenMerchantProduct((VenMerchantProduct) next);
			} else if (next instanceof VenOrderStatus) {
				venReturItem.setVenReturStatus((VenOrderStatus) next);
			}
		}
		return venReturItem;
	}

	/**
	 * Persists a list of order item marginPromo
	 * 
	 * @param venOrderItemAdjustment
	 * @return the persisted object
	 */
	List<VenOrderItemAdjustment> persistOrderItemAdjustmentList(VenOrderItem venOrderItem, List<VenOrderItemAdjustment> venOrderItemAdjustmentList) {
		List<VenOrderItemAdjustment> newVenOrderItemAdjustmentList = new ArrayList<VenOrderItemAdjustment>();
		if (venOrderItemAdjustmentList != null && !venOrderItemAdjustmentList.isEmpty()) {
			try {
				_log.debug("Persisting VenOrderItemAdjustment list...:" + venOrderItemAdjustmentList.size());
				Iterator<VenOrderItemAdjustment> i = venOrderItemAdjustmentList.iterator();
				while (i.hasNext()) {
					VenOrderItemAdjustment next = i.next();
					// Synchronize the references
					next = this.synchronizeVenOrderItemAdjustmentReferenceData(next);

					// Attach the order item
					next.setVenOrderItem(venOrderItem);
					// Attach a primary key
					VenOrderItemAdjustmentPK id = new VenOrderItemAdjustmentPK();
					id.setOrderItemId(venOrderItem.getOrderItemId());					
					id.setPromotionId(next.getVenPromotion().getPromotionId());
					_log.debug("id.getOrderItemId: "+id.getOrderItemId());
					_log.debug("id.getPromotionId: "+id.getPromotionId());
					next.setId(id);
					// Persist the object
					VenOrderItemAdjustmentSessionEJBLocal adjustmentHome = (VenOrderItemAdjustmentSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemAdjustmentSessionEJBLocal.class, "VenOrderItemAdjustmentSessionEJBBeanLocal");
					
					List<VenOrderItemAdjustment> existingVenOrderItemAdjustments = adjustmentHome.queryByRange("select o from VenOrderItemAdjustment o where o.venPromotion.promotionId = " + id.getPromotionId() + " and o.venOrderItem.orderItemId = " + id.getOrderItemId(), 0, 0);
					_log.debug("existingVenOrderItemAdjustments.size: "+existingVenOrderItemAdjustments.size());
					if(existingVenOrderItemAdjustments.size() == 0){
						if(next.getAdminDesc() == null || next.getAdminDesc().equals("")){
							next.setAdminDesc("-");
							_log.info("Set adminDesc to (-) if null or empty");
						}
						newVenOrderItemAdjustmentList.add((VenOrderItemAdjustment) adjustmentHome.persistVenOrderItemAdjustment(next));
					}else{
						newVenOrderItemAdjustmentList.add((VenOrderItemAdjustment) next);
					}					
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderItemAdjustment:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return newVenOrderItemAdjustmentList;
	}

	/**
	 * Synchronizes the reference data for the direct VenOrderItemAdjustment
	 * references
	 * 
	 * @param venOrderItemAdjustment
	 * @return the synchronized data object
	 */
	VenOrderItemAdjustment synchronizeVenOrderItemAdjustmentReferenceData(VenOrderItemAdjustment venOrderItemAdjustment) {
		List<Object> references = new ArrayList<Object>();	
		VenPromotion promotion = venOrderItemAdjustment.getVenPromotion();
		if(venOrderItemAdjustment.getVenPromotion().getPromotionName()==null || venOrderItemAdjustment.getVenPromotion().getPromotionName().equals("") )			
			promotion.setPromotionName("-");
		if(venOrderItemAdjustment.getVenPromotion().getPromotionCode()==null || venOrderItemAdjustment.getVenPromotion().getPromotionCode().equals("") )
			promotion.setPromotionCode("-");
		references.add(promotion);
		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenPromotion) {
				venOrderItemAdjustment.setVenPromotion((VenPromotion) next);
				// Set the adjustment side of the primary key
				VenOrderItemAdjustmentPK pk = new VenOrderItemAdjustmentPK();
				pk.setPromotionId(venOrderItemAdjustment.getVenPromotion().getPromotionId());
				venOrderItemAdjustment.setId(pk);
			}
		}
		return venOrderItemAdjustment;
	}

	/**
	 * Persists a merchant using the session tier.
	 * 
	 * @param venMerchant
	 * @return the persisted object
	 */
	VenMerchant persistMerchant(VenMerchant venMerchant) {
		if (venMerchant != null) {
			try {
				_log.debug("Synchronizing VenMerchant... :" + venMerchant.getWcsMerchantId());
				if (venMerchant.getVenParty() != null && venMerchant.getVenParty().getFullOrLegalName() != null && !venMerchant.getVenParty().getFullOrLegalName().equals("")) {
					VenPartyType venPartyType = new VenPartyType();
					// Set the party type to Merchant
					venPartyType.setPartyTypeId(new Long(VEN_PARTY_TYPE_MERCHANT));
					venMerchant.getVenParty().setVenPartyType(venPartyType);

					// Persist the party
					venMerchant.setVenParty(this.persistParty(venMerchant.getVenParty(), "Merchant"));
				}
				
				// If the merchant exists then merge else persist
				VenMerchantSessionEJBLocal merchantHome = (VenMerchantSessionEJBLocal) this._genericLocator.lookupLocal(VenMerchantSessionEJBLocal.class, "VenMerchantSessionEJBBeanLocal");
				List<VenMerchant> merchantList = merchantHome.queryByRange("select o from VenMerchant o where o.wcsMerchantId ='" + venMerchant.getWcsMerchantId() + "'", 0, 1);
				if (merchantList == null || merchantList.isEmpty()) {
					venMerchant = merchantHome.persistVenMerchant(venMerchant);
				} else {
					VenMerchant existingVenMerchant = merchantList.get(0);
					existingVenMerchant.setVenParty(venMerchant.getVenParty());
					venMerchant = merchantHome.mergeVenMerchant(existingVenMerchant);
				}
				
//				/*
//				 * If the KPI targets for the merchant do not exist
//				 * then create defaults for them
//				 */
//				if(venMerchant.getVenParty() != null && venMerchant.getVenParty().getFullOrLegalName() != null && !venMerchant.getVenParty().getFullOrLegalName().equals("")){
//					this.createDefaultMerchantKPIPartyTargetRecords(this._genericLocator, venMerchant);
//				}
//				
//				/*
//				 * If the KPI actuals for merchants for the current period 
//				 * don't already exist then create them.
//				 */
//				if(venMerchant.getVenParty() != null && venMerchant.getVenParty().getFullOrLegalName() != null && !venMerchant.getVenParty().getFullOrLegalName().equals("") ){
//					this.createDefaultMerchantCurrentKPIPeriodActualRecords(this._genericLocator, venMerchant);
//				}

			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenMerchant:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();	
				throw new EJBException(errMsg);
			}
		}
		return venMerchant;
	}

	/**
	 * Persists a merchant product using the session tier.
	 * 
	 * @param venMerchantProduct
	 * @return the persisted object
	 */
	VenMerchantProduct persistMerchantProduct(VenMerchantProduct venMerchantProduct) {
		if (venMerchantProduct != null) {
			try {
				_log.debug("Persisting VenMerchantProduct... :" + venMerchantProduct.getWcsProductSku());

				// If the merchant exists then set it else persist it
				if (this.merchantExists(venMerchantProduct.getVenMerchant().getWcsMerchantId())) {
					venMerchantProduct.setVenMerchant(this.retrieveExistingMerchant(venMerchantProduct.getVenMerchant().getWcsMerchantId()));
				} else {
					venMerchantProduct.setVenMerchant(this.persistMerchant(venMerchantProduct.getVenMerchant()));
				}

				// Synchronize the reference data
				venMerchantProduct = this.synchronizeVenMerchantProductReferenceData(venMerchantProduct);

				// Detach the product categories prior to persisting
				List<VenProductCategory> venProductCategoryList = (List<VenProductCategory>)venMerchantProduct.getVenProductCategories();
				venMerchantProduct.setVenProductCategories(null);

				// Persist the object
				VenMerchantProductSessionEJBLocal productHome = (VenMerchantProductSessionEJBLocal) this._genericLocator
						.lookupLocal(VenMerchantProductSessionEJBLocal.class, "VenMerchantProductSessionEJBBeanLocal");
				VenProductCategorySessionEJBLocal categoryHome = (VenProductCategorySessionEJBLocal) this._genericLocator
						.lookupLocal(VenProductCategorySessionEJBLocal.class, "VenProductCategorySessionEJBBeanLocal");
				List<VenMerchantProduct> venMerchantProductList = productHome.queryByRange("select o from VenMerchantProduct o where o.wcsProductSku = '" + venMerchantProduct.getWcsProductSku() + "'", 0, 1);
				if (venMerchantProductList == null || venMerchantProductList.isEmpty()) {
					venMerchantProduct = (VenMerchantProduct) productHome.persistVenMerchantProduct(venMerchantProduct);
				} else {
					venMerchantProduct = venMerchantProductList.get(0);
				}

				// Persist the list of product categories using merge
				venMerchantProduct.setVenProductCategories(this.synchronizeVenProductCategories(venProductCategoryList));
				/*
				 * We need to make sure that both sides of the relationship are populated
				 * prior to performing the merge for VenMerchantProduct. We also need to
				 * merge each VenProductCategory because it is the dominant side of the relationship.
				 * 
				 * This is not pretty but it is necessary - DF
				 */
				for(VenProductCategory category:venProductCategoryList){
					ArrayList<VenMerchantProduct> venMerchantProductList2 = new ArrayList<VenMerchantProduct>();
					if(category.getVenMerchantProducts() == null){
						category.setVenMerchantProducts(venMerchantProductList2);						
					}
					category.getVenMerchantProducts().add(venMerchantProduct);
				}
				for(VenProductCategory category:venMerchantProduct.getVenProductCategories()){
					category = categoryHome.mergeVenProductCategory(category);
				}
				venMerchantProduct = (VenMerchantProduct) productHome.mergeVenMerchantProduct(venMerchantProduct);

			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenMerchantProduct:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();	
				throw new EJBException(errMsg);
			}
		}
		return venMerchantProduct;
	}

	/**
	 * Synchronizes the data for the direct VenMerchantProduct references
	 * 
	 * @param venMerchantProduct
	 * @return the synchronized data object
	 */
	private VenMerchantProduct synchronizeVenMerchantProductReferenceData(
			VenMerchantProduct venMerchantProduct) {

		List<Object> references = new ArrayList<Object>();
		references.add(venMerchantProduct.getVenProductType());
		references.add(venMerchantProduct.getVenMerchant());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenProductType) {
				venMerchantProduct.setVenProductType((VenProductType) next);
			}else if(next instanceof VenMerchant){
				venMerchantProduct.setVenMerchant((VenMerchant) next);
			}
		}
		return venMerchantProduct;
	}

	/**
	 * Synchronizes a list of product categories with the cache
	 * 
	 * @param venProductCategoryList
	 * @return
	 */
	private List<VenProductCategory> synchronizeVenProductCategories(
			List<VenProductCategory> venProductCategoryList) {

		List<Object> references = new ArrayList<Object>();
		Iterator<VenProductCategory> i = venProductCategoryList.iterator();
		while (i.hasNext()) {
			references.add(i.next());
		}

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the list
		Iterator<Object> referencesIterator = references.iterator();
		int index = 0;
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenProductCategory) {
				venProductCategoryList.remove(index);
				venProductCategoryList.add((VenProductCategory) next);
			}
		}
		return venProductCategoryList;
	}

	/**
	 * Persists a recipient using the session tier
	 * 
	 * @param venRecipient
	 * @return the persisted object
	 */
	VenRecipient persistRecipient(VenRecipient venRecipient) {
		if (venRecipient != null) {
			try {
				_log.debug("Persisting VenRecipient... :" + venRecipient.getVenParty().getFullOrLegalName());

				// Persist the party
				VenPartyType venPartyType = new VenPartyType();

				// Set the party type to Recipient
				venPartyType.setPartyTypeId(new Long(VEN_PARTY_TYPE_RECIPIENT));
				venRecipient.getVenParty().setVenPartyType(venPartyType);
				
				venRecipient.setVenParty(this.persistParty(venRecipient.getVenParty(), "Recipient"));
				// Synchronize the reference data
				venRecipient = this.synchronizeVenRecipientReferenceData(venRecipient);
				// Persist the object
				VenRecipientSessionEJBLocal recipientHome = (VenRecipientSessionEJBLocal) this._genericLocator
						.lookupLocal(VenRecipientSessionEJBLocal.class, "VenRecipientSessionEJBBeanLocal");
				venRecipient = (VenRecipient) recipientHome.persistVenRecipient(venRecipient);
				
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenRecipient:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return venRecipient;
	}

	/**
	 * Synchronizes the data for the direct VenRecipient references
	 * 
	 * @param venRecipient
	 * @return the synchronized data object
	 */
	private VenRecipient synchronizeVenRecipientReferenceData(VenRecipient venRecipient) {

		List<Object> references = new ArrayList<Object>();
		references.add(venRecipient.getVenParty());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenParty) {
				venRecipient.setVenParty((VenParty) next);
			}
		}
		return venRecipient;
	}

	/**
	 * Persists a party using the session tier.
	 * 
	 * @param venParty
	 * @return the persisted object
	 */
	VenParty persistParty(VenParty venParty, String type) {
		if (venParty != null) {
			try {
				VenParty existingParty;
				if(type.equals("Customer")){
					_log.debug("Persisting VenParty... :" + venParty.getVenCustomers().get(0).getCustomerUserName());
	
					// Get any existing party based on customer username
					existingParty = this.retrieveExistingParty(venParty.getVenCustomers().get(0).getCustomerUserName());
				} else {
					_log.debug("Persisting VenParty... :" + venParty.getFullOrLegalName());

					// Get any existing party based on full or legal name 
					existingParty = this.retrieveExistingParty(venParty.getFullOrLegalName());
				}
			
				/*
				 * If the party already exists then the existing party 
				 * addresses and contacts may have changed so we
				 * need to synchronize them
				 */								
				if (existingParty != null) {
					_log.debug("existing Party not null" );
					
					//Get the existing addresses
					List<VenAddress> existingAddressList = new ArrayList<VenAddress>();
					for(VenPartyAddress venPartyAddress:existingParty.getVenPartyAddresses()){
						_log.debug("ven Party Address... :"+venPartyAddress.getVenAddress().getAddressId());
						existingAddressList.add(venPartyAddress.getVenAddress());
					}
					
					//Get the new addresses
					List<VenAddress> newAddressList = new ArrayList<VenAddress>();
					if(venParty.getVenPartyAddresses()!=null){
						for(VenPartyAddress venPartyAddress:venParty.getVenPartyAddresses()){
							_log.debug("New ven Party Address... :"+venPartyAddress.getVenAddress().getStreetAddress1());
							newAddressList.add(venPartyAddress.getVenAddress());
						}
					}

					/*
					 * If any new addresses are provided then check that 
					 * the existing addresses match the new addresses
					 * else add the new addresses for the party 
					 */
					
					
					if(!newAddressList.isEmpty()){
						_log.debug("New ven Party Address is not empty and Update Address List");
						List<VenAddress> updatedAddressList = this.updateAddressList(existingAddressList, newAddressList);
						_log.debug("updatedAddressList size => " + updatedAddressList.size());
						List<VenAddress> tempAddressList = new ArrayList<VenAddress>();
						
						tempAddressList.addAll(updatedAddressList);
						
						_log.debug("Remove old VenAddress");
						//Remove all the existing addresses
						updatedAddressList.removeAll(existingAddressList);
						
						//Setup the new VenPartyAddress records
						List<VenPartyAddress> venPartyAddressList = new ArrayList<VenPartyAddress>();
						for(VenAddress updatedAddress:updatedAddressList){
							VenPartyAddress venPartyAddress = new VenPartyAddress();							
							VenAddressType venAddressType = new VenAddressType();
							venAddressType.setAddressTypeId(VeniceConstants.VEN_ADDRESS_TYPE_DEFAULT);
														
							venPartyAddress.setVenAddress(updatedAddress);
							venPartyAddress.setVenAddressType(venAddressType);
							venPartyAddress.setVenParty(existingParty);
							existingParty.getVenPartyAddresses().add(venPartyAddress);
							
							venPartyAddressList.add(venPartyAddress);							
						}
						
						_log.debug("persist Party Addresses ");
						//Persist the new VenPartyAddress records
						venPartyAddressList = this.persistPartyAddresses(venPartyAddressList);						
						
						if(updatedAddressList.size() == 0){
							for(VenAddress updatedAddress:tempAddressList){
								venPartyAddressList.addAll(updatedAddress.getVenPartyAddresses());	
								for(VenPartyAddress venPartyAddress:venPartyAddressList){
									_log.debug("VenPartyAddress => " + venPartyAddress.getVenAddress().getAddressId());
								}
							}
						}
						
						//copy existing address list to new list so it can be added new address list
						_log.debug("copy address list to new list");
//						List<VenPartyAddress> venPartyAddressList2=new ArrayList<VenPartyAddress>(existingParty.getVenPartyAddresses()).subList(0,venPartyAddressList.size());
						
						//Add all the new party address records	
						_log.debug("add All VenParty Addresses");
						existingParty.setVenPartyAddresses(venPartyAddressList);
						for(VenPartyAddress venPartyAddress:venPartyAddressList){
							_log.debug("VenPartyAddress => " + venPartyAddress.getVenAddress().getAddressId());
						}
						_log.debug("done add All VenParty Addresses");
					}
					
					/*
					 * If any new contact details are provided then check
					 * that the new contact details match the existing 
					 * contact details else add the new contact details
					 * to the party and then merge.
					 */
					
					_log.debug("Get old and new party ven contact Detail  ");
					//Get the existing contact details
					List<VenContactDetail> existingContactDetailList = existingParty.getVenContactDetails();
					
					if(venParty.getVenContactDetails()!=null){
							//Get the new addresses
							List<VenContactDetail> newContactDetailList = venParty.getVenContactDetails();
							
							if(!newContactDetailList.isEmpty()){
								_log.debug("updatedContact Detail List from existingContactDetailList to newContactDetailList");
								_log.debug("start updating contact detail");
								//if the contact detail of existing party is null we can not get the party id using existingContactDetailList, so send the existing party
								List<VenContactDetail> updatedContactDetailList = this.updateContactDetailList(existingParty, existingContactDetailList, newContactDetailList);
								_log.debug("done updating contact detail!!!");
		
								existingParty.setVenContactDetails(updatedContactDetailList);
							}
					}
					return existingParty;
				}

				// Persist addresses
				List<VenAddress> addressList = new ArrayList<VenAddress>();
				Iterator<VenPartyAddress> i = venParty.getVenPartyAddresses().iterator();
				while (i.hasNext()) {
					addressList.add(i.next().getVenAddress());
				}
				_log.debug("persist Address List");
				addressList = this.persistAddressList(addressList);

				// Assign the address keys back to the n-n object
				i = venParty.getVenPartyAddresses().iterator();
				int index = 0;
				while (i.hasNext()) {
					VenPartyAddress next = i.next();
					next.setVenAddress(addressList.get(index));
					VenAddressType addressType = new VenAddressType();
					addressType.setAddressTypeId(VEN_ADDRESS_TYPE_DEFAULT);
					List<Object> references = new ArrayList<Object>();
					references.add(addressType);
					references = this.synchronizeReferenceData(references);
					index++;
				}

				// Detach the party addresses object before persisting party
				List<VenPartyAddress> venPartyAddressList = venParty.getVenPartyAddresses();
				venParty.setVenPartyAddresses(null);

				// Detach the list of contact details before persisting party
				List<VenContactDetail> venContactDetailList = venParty.getVenContactDetails();
				venParty.setVenContactDetails(null);
				
				_log.debug("synchronize VenParty Reference Data");
				// Synchronize the reference data
				venParty = this.synchronizeVenPartyReferenceData(venParty);
				
				// Persist the object
				VenPartySessionEJBLocal partyHome = (VenPartySessionEJBLocal) this._genericLocator.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");

				_log.debug("persist VenParty ");
				// Merge the party object
				venParty = (VenParty) partyHome.persistVenParty(venParty);

				VenAddressType venAddressType = new VenAddressType();
				venAddressType.setAddressTypeId(VEN_ADDRESS_TYPE_DEFAULT);

				// Set the party relationship for each VenPartyAddress
				i = venPartyAddressList.iterator();
				while (i.hasNext()) {
					VenPartyAddress next = i.next();
					next.setVenParty(venParty);
					next.setVenAddressType(venAddressType);
				}

				_log.debug("persist Party Addresses ");
				// Persist the party addresses
				venParty.setVenPartyAddresses(this.persistPartyAddresses(venPartyAddressList));
				_log.debug("Venpartyaddress size = >" + venParty.getVenPartyAddresses().size());
				// Set the party relationship for each contact detail
				Iterator<VenContactDetail> contactsIterator = venContactDetailList.iterator();
				while (contactsIterator.hasNext()) {
					contactsIterator.next().setVenParty(venParty);
				}

				_log.debug("persist Contact Details");
				// Persist the contact details
				venParty.setVenContactDetails(this.persistContactDetails(venContactDetailList));
				_log.debug("VenContactDetails size = >" + venParty.getVenContactDetails());
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenParty:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return venParty;
	}
	
	/**
	 * updateAddressList - compares the existing address list with the new address list,
	 * writes any new addresses to the database and returns the updated address list.
	 * 
	 * @param existingVenAddressList
	 * @param newVenAddressList
	 * @return the updated address list
	 */
	List<VenAddress> updateAddressList(List<VenAddress> existingVenAddressList, List<VenAddress> newVenAddressList){
		List<VenAddress> updatedVenAddressList = new ArrayList<VenAddress>();
		List<VenAddress> persistVenAddressList = new ArrayList<VenAddress>();
		VenAddress tempAddress = new VenAddress();
		
		Boolean isAddressEqual=false;
		for(VenAddress newVenAddress:newVenAddressList){
			for(VenAddress existingVenAddress:existingVenAddressList){		
				if(((existingVenAddress.getKecamatan() == null && newVenAddress.getKecamatan() == null) || (existingVenAddress.getKecamatan()==null?"":existingVenAddress.getKecamatan().trim()).equalsIgnoreCase(newVenAddress.getKecamatan()==null?"":newVenAddress.getKecamatan().trim()))
						&& ((existingVenAddress.getKelurahan() == null && newVenAddress.getKelurahan() == null) || (existingVenAddress.getKelurahan()==null?"":existingVenAddress.getKelurahan().trim()).equalsIgnoreCase(newVenAddress.getKelurahan()==null?"":newVenAddress.getKelurahan().trim()))
						&& ((existingVenAddress.getPostalCode() == null && newVenAddress.getPostalCode() == null) || (existingVenAddress.getPostalCode()==null?"":existingVenAddress.getPostalCode().trim()).equalsIgnoreCase(newVenAddress.getPostalCode()==null?"":newVenAddress.getPostalCode().trim()))
						&& ((existingVenAddress.getStreetAddress1() == null && newVenAddress.getStreetAddress1() == null) || (existingVenAddress.getStreetAddress1()==null?"":existingVenAddress.getStreetAddress1().trim()).equalsIgnoreCase(newVenAddress.getStreetAddress1()==null?"":newVenAddress.getStreetAddress1().trim()))
						&& ((existingVenAddress.getStreetAddress2() == null && newVenAddress.getStreetAddress2() == null) || (existingVenAddress.getStreetAddress2()==null?"":existingVenAddress.getStreetAddress2().trim()).equalsIgnoreCase(newVenAddress.getStreetAddress2()==null?"":newVenAddress.getStreetAddress2().trim()))
						&& ((existingVenAddress.getVenCity() == null && newVenAddress.getVenCity() == null) || ((existingVenAddress.getVenCity()!=null?existingVenAddress.getVenCity().getCityCode():null)==null?"":existingVenAddress.getVenCity().getCityCode().trim()).equalsIgnoreCase((newVenAddress.getVenCity()!=null?newVenAddress.getVenCity().getCityCode():null)==null?"":newVenAddress.getVenCity().getCityCode().trim()))
						&& ((existingVenAddress.getVenCountry() == null && newVenAddress.getVenCountry() == null) || (existingVenAddress.getVenCountry().getCountryCode()==null?"":existingVenAddress.getVenCountry().getCountryCode().trim()).equalsIgnoreCase(newVenAddress.getVenCountry().getCountryCode()==null?"":newVenAddress.getVenCountry().getCountryCode().trim()))
						&& ((existingVenAddress.getVenState() == null && newVenAddress.getVenState() == null) || ((existingVenAddress.getVenState()!=null?existingVenAddress.getVenState().getStateCode():null)==null?"":existingVenAddress.getVenState().getStateCode().trim()).equalsIgnoreCase((newVenAddress.getVenState()!=null?newVenAddress.getVenState().getStateCode():null)==null?"":newVenAddress.getVenState().getStateCode().trim()))){
					/*
					 * The address is assumed to be equal, not that the equals() 
					 * operation can't be used because it is implemented by 
					 * JPA on the primary key. Add it to the list
					 */
					isAddressEqual=true;
					_log.debug("\n party address equal with existing.");
					updatedVenAddressList.add(existingVenAddress);	
					break;
				}else{
					_log.debug("\n party address NOT equal with existing.");
					isAddressEqual=false;
					tempAddress=existingVenAddress;
				}
			}
			if(isAddressEqual==false){
				/*
				 * The address is a new address so it needs to be persisted
				 */
				_log.debug("\n party address is new address.");
				newVenAddress.setVenPartyAddresses(tempAddress.getVenPartyAddresses());
				persistVenAddressList.add(newVenAddress);
			}
		}

		
		/*
		 * Persist any addresses that are new
		 */
		if(!persistVenAddressList.isEmpty()){
			persistVenAddressList = this.persistAddressList(persistVenAddressList);
			
			//Add the persisted addresses to the new list
			updatedVenAddressList.addAll(persistVenAddressList);
		}
		return updatedVenAddressList;
	}

	/**
	 * updateContactDetailList - compares the existing contact detail list with the 
	 * new contact detail list, writes any new contact details to the database 
	 * and returns the updated contact detail list.
	 * @param existingVenContactDetailList
	 * @param newVenContactDetailList
	 * @return the updated contact detail list
	 */
	List<VenContactDetail> updateContactDetailList(VenParty existingParty, List<VenContactDetail> existingVenContactDetailList, List<VenContactDetail> newVenContactDetailList){
		List<VenContactDetail> updatedVenContactDetailList = new ArrayList<VenContactDetail>();
		List<VenContactDetail> persistVenContactDetailList = new ArrayList<VenContactDetail>();
		_log.debug("\n masuk method update contact detail");
		/*
		 * Iterate the list of existing contacts to determine if 
		 * the new contacts exist already
		 */
		for(VenContactDetail newVenContactDetail:newVenContactDetailList){
			Boolean bFound = false;
			if(!existingVenContactDetailList.isEmpty()){
				_log.debug("\n existingVenContactDetailList not empty");
				for(VenContactDetail existingVenContactDetail:existingVenContactDetailList){
					/*
					 * If the contact detail and type are not null and they are equal to each other (new and existing) 
					 * then the contact is existing and is added to the return list only.
					 * 
					 * If it is a new contact it is added to the persist list
					 */
					if((existingVenContactDetail.getContactDetail() != null && newVenContactDetail.getContactDetail() != null) 
							&& existingVenContactDetail.getContactDetail().trim().equalsIgnoreCase(newVenContactDetail.getContactDetail().trim())
							&& ((existingVenContactDetail.getVenContactDetailType() != null	&& newVenContactDetail.getVenContactDetailType() != null)) 
							&& existingVenContactDetail.getVenContactDetailType().getContactDetailTypeDesc().equals(newVenContactDetail.getVenContactDetailType().getContactDetailTypeDesc())){
						/*
						 * The contact detail is assumed to be equal (note that the equals() 
						 * operation can't be used because it is implemented by 
						 * JPA on the primary key. Add it to the list
						 */
						_log.debug("\n contact detail equal with existing, added to updated list");
						updatedVenContactDetailList.add(existingVenContactDetail);
						
						bFound = true;
						//Break from the inner loop as the contact is found
						break;
					}
				}
			}else{
				_log.debug("\n existingVenContactDetailList is empty");
			}
			/*
			 * The contact detail is not found in the existing
			 * contact list therefore it is a new contact detail 
			 * and it needs to be persisted. The existing party
			 * record also needs to be set otherwise it
			 * will fail as the new contact record has a
			 * detached party
			 */
			if(!bFound){
				_log.debug("\n contact detail not equal with existing, persist it");
				newVenContactDetail.setVenParty(existingParty);
				if(!persistVenContactDetailList.contains(newVenContactDetail)){
					_log.debug("\n added the new contact detail to list");
					persistVenContactDetailList.add(newVenContactDetail);
				}
			}
		}
		
		/*
		 * Persist any contact details that are new
		 */
		if(!persistVenContactDetailList.isEmpty()){
			_log.debug("\n new contact detail list not empty, start persist new contact detail");
			persistVenContactDetailList = this.persistContactDetails(persistVenContactDetailList);
			_log.debug("\n done persist contact detail");
			//Add the persisted contact details to the new list
			updatedVenContactDetailList.addAll(persistVenContactDetailList);
		}
		_log.debug("\n return updated contact detail list");
		return updatedVenContactDetailList;
	}

	/**
	 * Synchronizes the data for the direct VenParty references
	 * 
	 * @param venParty
	 * @return the synchronized data object
	 */
	private VenParty synchronizeVenPartyReferenceData(VenParty venParty) {
		List<Object> references = new ArrayList<Object>();
		if (venParty.getVenParty() != null) {
			references.add(venParty.getVenParty());
		}
		references.add(venParty.getVenPartyType());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenParty) {
				venParty.setVenParty((VenParty) next);
			} else if (next instanceof VenPartyType) {
				venParty.setVenPartyType((VenPartyType) next);
			}
		}
		return venParty;
	}
	
	/**
	 * Retreives an existing party from the cache along with 
	 * contact and address details
	 * 
	 * @param fullOrLegalName
	 * @return the party if it exists else null
	 */
	private VenParty retrieveExistingParty(String custUserName) {
		try {
			String escapeChar = "";
			
			VenCustomerSessionEJBLocal customerHome = (VenCustomerSessionEJBLocal) this._genericLocator
			.lookupLocal(VenCustomerSessionEJBLocal.class, "VenCustomerSessionEJBBeanLocal");
			
			VenContactDetailSessionEJBLocal contactDetailHome = (VenContactDetailSessionEJBLocal) this._genericLocator
			.lookupLocal(VenContactDetailSessionEJBLocal.class, "VenContactDetailSessionEJBBeanLocal");

			VenPartyAddressSessionEJBLocal partyAddressHome = (VenPartyAddressSessionEJBLocal) this._genericLocator
			.lookupLocal(VenPartyAddressSessionEJBLocal.class, "VenPartyAddressSessionEJBBeanLocal");
			
			List<VenCustomer> customerList = customerHome.queryByRange("select o from VenCustomer o where o.customerUserName ='" + JPQLStringEscapeUtility.escapeJPQLStringData(custUserName, escapeChar) + "' order by o.customerId desc", 0, 0);
			if (customerList != null && !customerList.isEmpty()) {
				VenParty party = customerList.get(0).getVenParty();
				
				/*
				 * Fetch the list of contact details for the party
				 */
				List<VenContactDetail> venContactDetailList = contactDetailHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId = " + party.getPartyId(), 0, 0);
				_log.debug("Total existing vencontactdetail => " + venContactDetailList.size());
				party.setVenContactDetails(venContactDetailList);
				
				/*
				 * Fetch the list of party addresses for the party
				 */
				List<VenPartyAddress> venPartyAddressList = partyAddressHome.queryByRange("select o from VenPartyAddress o where o.venParty.partyId = " + party.getPartyId(), 0, 0);
				_log.debug("Total existing VenPartyAddress => " + venPartyAddressList.size());
				party.setVenPartyAddresses(venPartyAddressList);
				
				return party;
			} else {
				return null;
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when accessing the VenPartySessionEJBBean:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();	
			throw new EJBException(errMsg + e.getMessage());
		}
	}

	/**
	 * Persists a list of party addresses using the session tier.
	 * 
	 * @param venPartyAddressList
	 * @return the persisted object
	 */
	List<VenPartyAddress> persistPartyAddresses(List<VenPartyAddress> venPartyAddressList) {
		List<VenPartyAddress> newVenPartyAddressList = new ArrayList<VenPartyAddress>();
		if (venPartyAddressList != null && !venPartyAddressList.isEmpty()) {
			try {
				_log.debug("Persisting VenPartyAddress list...:" + venPartyAddressList.size());
				Iterator<VenPartyAddress> i = venPartyAddressList.iterator();
				while (i.hasNext()) {
					VenPartyAddress next = i.next();

					// Set up the primary key object
					VenPartyAddressPK id = new VenPartyAddressPK();
					id.setAddressId(next.getVenAddress().getAddressId());
					id.setPartyId(next.getVenParty().getPartyId());
					id.setAddressTypeId(next.getVenAddressType().getAddressTypeId());
					next.setId(id);
					// Persist the object
					VenPartyAddressSessionEJBLocal addressHome = (VenPartyAddressSessionEJBLocal) this._genericLocator
							.lookupLocal(VenPartyAddressSessionEJBLocal.class, "VenPartyAddressSessionEJBBeanLocal");
					newVenPartyAddressList.add((VenPartyAddress) addressHome.persistVenPartyAddress(next));
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenPartyAddress:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();	
				throw new EJBException(errMsg);
			}
		}
		return newVenPartyAddressList;
	}

	/**
	 * Synchronizes the data for the direct VenPartyAddress references
	 * 
	 * @param venPartyAddress
	 * @return
	 */
	private VenPartyAddress synchronizeVenPartyAddressReferenceData(VenPartyAddress venPartyAddress) {
		List<Object> references = new ArrayList<Object>();
		references.add(venPartyAddress.getVenAddress());
		references.add(venPartyAddress.getVenAddressType());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenAddress) {
				venPartyAddress.setVenAddress((VenAddress) next);
				VenPartyAddressPK pk = new VenPartyAddressPK();
				pk.setAddressId(((VenAddress) next).getAddressId());
				venPartyAddress.setId(pk);
			} else if (next instanceof VenPartyType) {
				venPartyAddress.setVenAddressType((VenAddressType) next);
			}
		}
		return venPartyAddress;
	}

	/**
	 * Persists a list of contact details using the session tier.
	 * 
	 * @param venContactDetails
	 * @return the persisted object
	 */
	List<VenContactDetail> persistContactDetails(List<VenContactDetail> venContactDetailList) {
		_log.debug("\n start method persist contact detail");
		List<VenContactDetail> newVenContactDetailList = new ArrayList<VenContactDetail>();
		if (venContactDetailList != null && !venContactDetailList.isEmpty()) {
			try {
				_log.debug("Persisting VenContactDetail list...:" + venContactDetailList.size());
				Iterator<VenContactDetail> i = venContactDetailList.iterator();
				while (i.hasNext()) {
					VenContactDetail next = i.next();
					// Synchronize the references
					this.synchronizeVenContactDetailReferenceData(next);
					// Persist the object
					VenContactDetailSessionEJBLocal detailHome = (VenContactDetailSessionEJBLocal) this._genericLocator
							.lookupLocal(VenContactDetailSessionEJBLocal.class, "VenContactDetailSessionEJBBeanLocal");
					_log.debug("\n start persisting contact detail");
					newVenContactDetailList.add((VenContactDetail) detailHome.persistVenContactDetail(next));
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenContactDetail:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();	
				throw new EJBException(errMsg);
			}
		}
		return newVenContactDetailList;
	}

	/**
	 * Synchronizes the data for the direct VenContactDetail references
	 * 
	 * @param venContactDetail
	 * @return the synchronized data object
	 */
	private VenContactDetail synchronizeVenContactDetailReferenceData(VenContactDetail venContactDetail) {
		List<Object> references = new ArrayList<Object>();
		references.add(venContactDetail.getVenContactDetailType());
		_log.debug("\n start sync contact detail method");
		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenContactDetailType) {
				venContactDetail.setVenContactDetailType((VenContactDetailType) next);
			}
		}
		return venContactDetail;
	}

	/**
	 * Persists a list of addresses.
	 * 
	 * @param venAddressList
	 * @return
	 */
	List<VenAddress> persistAddressList(List<VenAddress> venAddressList) {
		List<VenAddress> newVenAddressList = new ArrayList<VenAddress>();
		Iterator<VenAddress> i = venAddressList.iterator();
		while (i.hasNext()) {
			VenAddress newAddress = this.persistAddress(i.next());
			newVenAddressList.add(newAddress);
		}
		return newVenAddressList;
	}

	/**
	 * Persists an address using the session tier
	 * 
	 * @param venAddress
	 * @return the persisted object
	 */
	VenAddress persistAddress(VenAddress venAddress) {
		if (venAddress != null) {
			try {
				_log.debug("Persisting VenAddress... :" + venAddress.getStreetAddress1());
				// Synchronize the reference data
				venAddress = this.synchronizeVenAddressReferenceData(venAddress);
				// Persist the object
				VenAddressSessionEJBLocal addressHome = (VenAddressSessionEJBLocal) this._genericLocator
						.lookupLocal(VenAddressSessionEJBLocal.class, "VenAddressSessionEJBBeanLocal");

				if (venAddress.getAddressId() == null) {
					if(venAddress.getStreetAddress1()==null && venAddress.getKecamatan()==null && venAddress.getKelurahan()==null && venAddress.getVenCity()==null &&
							venAddress.getVenState()==null && venAddress.getPostalCode()==null && venAddress.getVenCountry()==null){
						_log.info("Address is null, no need to persist address");
					}else{
						//detach city, state, dan country karena bisa null dari WCS	
						_log.info("Address is not null, detach city, state, and country");
						VenCity city = null;
						VenState state = null;
						VenCountry country = null;
						
						if(venAddress.getVenCity()!=null){
							if(venAddress.getVenCity().getCityCode()!=null){
								city = venAddress.getVenCity();
							}							
							venAddress.setVenCity(null);
						}
						
						if(venAddress.getVenState()!=null){
							if(venAddress.getVenState().getStateCode()!=null){
								state = venAddress.getVenState();
							}							
							venAddress.setVenState(null);
						}
						
						if(venAddress.getVenCountry()!=null){
							if(venAddress.getVenCountry().getCountryCode()!=null){
								country = venAddress.getVenCountry();
							}							
							venAddress.setVenCountry(null);
						}			
						
						venAddress = (VenAddress) addressHome.persistVenAddress(venAddress);
						
						//attach lagi setelah persist
						venAddress.setVenCity(city);
						venAddress.setVenState(state);
						venAddress.setVenCountry(country);
						
						_log.debug("persist address");
						venAddress = (VenAddress) addressHome.mergeVenAddress(venAddress);
					}
				} else {
					_log.debug("merge address");
					venAddress = (VenAddress) addressHome.mergeVenAddress(venAddress);
				}

			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenAddress:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
		}
		return venAddress;
	}

	/**
	 * Synchronizes the data for the direct VenAddress references
	 * 
	 * @param venAddress
	 * @return the synchronized data object
	 */
	private VenAddress synchronizeVenAddressReferenceData(VenAddress venAddress) {
		List<Object> references = new ArrayList<Object>();
		references.add(venAddress.getVenCity());
		references.add(venAddress.getVenCountry());
		references.add(venAddress.getVenState());

		// Synchronize the data references
		references = this.synchronizeReferenceData(references);

		// Push the keys back into the record
		Iterator<Object> referencesIterator = references.iterator();
		while (referencesIterator.hasNext()) {
			Object next = referencesIterator.next();
			if (next instanceof VenCity) {
				venAddress.setVenCity((VenCity) next);
			} else if (next instanceof VenCountry) {
				venAddress.setVenCountry((VenCountry) next);
			} else if (next instanceof VenState) {
				venAddress.setVenState((VenState) next);
			}
		}
		return venAddress;
	}

	/**
	 * This is a private method specifically to handle the synchronization
	 * problems with reference data using the master data management strategy
	 * implemented in the interfaces. Whenever any inbound message is received
	 * this method is called to check the cache for the dependent data.
	 * 
	 * SLAVE: If the reference does not exist and one of the other applications
	 * is the master then the data will be inserted.
	 * 
	 * MASTER: if the reference data does not exist and Venice is the master
	 * then an exception shall be thrown.
	 * 
	 * If the data already exists (either as MASTER or SLAVE) as a part of this
	 * process then the keys used will be handed back to the caller to use in
	 * persisting the master record.
	 * 
	 * Note: that this method hits most of the order related database structure
	 * recursively upon any of the modifying operations (see above).
	 * 
	 * @return a list of synchronized objects
	 */
	private List<Object> synchronizeReferenceData(List<Object> references) {
		List<Object> retVal = new ArrayList<Object>();
		Iterator<Object> i = references.iterator();
		while (i.hasNext()) {
			Object next = i.next();
			if (next != null) {
				if (next instanceof VenAddress) {
					this.synchronizeVenAddressReferenceData((VenAddress) next);
				}
				
				// Banks need to be restricted to Venice values
				if (next instanceof VenOrder) {
					if (((VenOrder) next).getWcsOrderId() != null) {
						try {
							_log.debug("Restricting VenOrder... :" + ((VenOrder) next).getWcsOrderId());
							VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator .lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
							List<VenOrder> orderList = orderHome.queryByRange("select o from VenOrder o where o.wcsOrderId ='" + ((VenOrder) next).getWcsOrderId() + "'", 0, 1);
							if (orderList == null || orderList.isEmpty()) {
								throw new EJBException("Order does not exist");
							} else {
								retVal.add(orderList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenOrderSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenOrderSessionEJBBean:");
						}
					}
				}
				
				// Order payments need to be synchronized SPECIAL CASE
				// MANY-MANY)
				if (next instanceof VenOrderPayment) {
					if (((VenOrderPayment) next).getWcsPaymentId() != null) {
						_log.debug("Synchronizing VenOrderPayment... :" + ((VenOrderPayment) next).getWcsPaymentId());
						retVal.add(this.synchronizeVenOrderPaymentReferenceData((VenOrderPayment) next));
					}
				}

				// Parties need to be synchronized
				if (next instanceof VenParty) {
					if (((VenParty) next).getFullOrLegalName() != null) {
						try {
							_log.debug("Synchronizing VenParty reference data... ");
							next = this.synchronizeVenPartyReferenceData((VenParty) next);
							retVal.add(next);

						} catch (Exception e) {
							String errMsg = "An exception occured synchronizing VenParty reference data:";
							_log.error(errMsg + e.getMessage());
							e.printStackTrace();
							throw new EJBException(errMsg);

						}
					}
				}
				// Banks need to be restricted to Venice values
				if (next instanceof VenBank) {
					if (((VenBank) next).getBankCode() != null) {
						try {
							_log.debug("Restricting VenBank... :" + ((VenBank) next).getBankCode());
							VenBankSessionEJBLocal bankHome = (VenBankSessionEJBLocal) this._genericLocator .lookupLocal(VenBankSessionEJBLocal.class, "VenBankSessionEJBBeanLocal");
							List<VenBank> bankList = bankHome.queryByRange("select o from VenBank o where o.bankCode ='" + ((VenBank) next).getBankCode() + "'", 0, 1);
							if (bankList == null || bankList.isEmpty()) {
								throw new EJBException("Bank does not exist");
							} else {
								retVal.add(bankList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenBankSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenBankSessionEJBBean:");
						}
					}
				}
				// Customers need to be synchronized
				if (next instanceof VenCustomer) {
					if (((VenCustomer) next).getWcsCustomerId() != null) {
						try {
							_log.debug("Synchronizing VenCustomer... :" + ((VenCustomer) next).getWcsCustomerId());
							// Synchronize the reference data
							next = this.synchronizeVenCustomerReferenceData((VenCustomer) next);
							// Synchronize the object
							VenCustomerSessionEJBLocal customerHome = (VenCustomerSessionEJBLocal) this._genericLocator.lookupLocal(VenCustomerSessionEJBLocal.class, "VenCustomerSessionEJBBeanLocal");
							VenCustomer customer = (VenCustomer) customerHome.persistVenCustomer((VenCustomer) next);
							retVal.add(customer);

						} catch (Exception e) {
							_log.error("An exception occured when looking up VenCustomerSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenCustomerSessionEJBBean:");
						}
					}
				}
				// Logistics provider must be restricted to Venice values
				if (next instanceof LogLogisticsProvider) {
					if (((LogLogisticsProvider) next).getLogisticsProviderCode() != null) {
						try {
							_log.debug("Restricting LogLogisticsProvider... :" + ((LogLogisticsProvider) next).getLogisticsProviderCode());
							LogLogisticsProviderSessionEJBLocal logisticsProviderHome = (LogLogisticsProviderSessionEJBLocal) this._genericLocator
									.lookupLocal(LogLogisticsProviderSessionEJBLocal.class, "LogLogisticsProviderSessionEJBBeanLocal");
							List<LogLogisticsProvider> logisticsProviderList = logisticsProviderHome
									.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderCode ='" + ((LogLogisticsProvider) next).getLogisticsProviderCode() + "'", 0, 1);
							if (logisticsProviderList == null || logisticsProviderList.isEmpty()) {
								throw new EJBException("Logistics provider does not exist");
							} else {
								retVal.add(logisticsProviderList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up LogLogisticsProviderSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up LogLogisticsProviderSessionEJBBean:");
						}
					}
				}
				// Merchant products need to be synchronized
				if (next instanceof VenMerchantProduct) {
					if (((VenMerchantProduct) next).getWcsProductSku() != null) {
						try {
							_log.debug("Synchronizing VenMerchantProduct... :" + ((VenMerchantProduct) next)	.getWcsProductSku());
							next = this.synchronizeVenMerchantProductReferenceData((VenMerchantProduct) next);
							VenMerchantProductSessionEJBLocal merchantProductHome = (VenMerchantProductSessionEJBLocal) this._genericLocator
									.lookupLocal(VenMerchantProductSessionEJBLocal.class, "VenMerchantProductSessionEJBBeanLocal");
							List<VenMerchantProduct> merchantProductList = merchantProductHome
									.queryByRange("select o from VenMerchantProduct o where o.wcsProductSku ='" + ((VenMerchantProduct) next).getWcsProductSku() + "'", 0, 1);
							if (merchantProductList == null || merchantProductList.isEmpty()) {
								VenMerchantProduct merchantProduct = this.persistMerchantProduct((VenMerchantProduct) next);
								retVal.add(merchantProduct);

							} else {
								retVal.add(merchantProductList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenMerchantProductSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenMerchantProductSessionEJBBean:");
						}
					}
				}
				// Logistics service must be restricted to Venice values
				if (next instanceof LogLogisticService) {
					if (((LogLogisticService) next).getServiceCode() != null) {
						try {
							_log.debug("Restricting LogLogisticService... :" + ((LogLogisticService) next).getServiceCode());
							LogLogisticServiceSessionEJBLocal logisticServiceHome = (LogLogisticServiceSessionEJBLocal) this._genericLocator
									.lookupLocal(LogLogisticServiceSessionEJBLocal.class, "LogLogisticServiceSessionEJBBeanLocal");
							List<LogLogisticService> logisticServiceList = logisticServiceHome
									.queryByRange("select o from LogLogisticService o where o.serviceCode ='" + ((LogLogisticService) next).getServiceCode() + "'", 0, 1);
							if (logisticServiceList == null || logisticServiceList.isEmpty()) {
								throw new EJBException("Logistics service does not exist");
							} else {
								retVal.add(logisticServiceList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up LogLogisticServiceSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up LogLogisticServiceSessionEJBBean:");
						}
					}
				}
				// Party addresses need to be synchronized
				if (next instanceof VenPartyAddress) {
					if (((VenPartyAddress) next).getVenAddress() != null) {
						try {
							_log.debug("Synchronizing VenPartyAddress... :" + ((VenPartyAddress) next).getVenAddress().getStreetAddress1());
							// Synchronize the reference data
							next = this.synchronizeVenPartyAddressReferenceData((VenPartyAddress) next);
							// Synchronize the object
							VenPartyAddressSessionEJBLocal partyAddressHome = (VenPartyAddressSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPartyAddressSessionEJBLocal.class, "VenPartyAddressSessionEJBBeanLocal");
							VenPartyAddress partyAddress = (VenPartyAddress) partyAddressHome.persistVenPartyAddress((VenPartyAddress) next);
							retVal.add(partyAddress);

						} catch (Exception e) {
							_log.error("An exception occured when looking up VenPartyAddressSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPartyAddressSessionEJBBean:");
						}
					}
				}
				// Order status values need to be restricted to Venice values
				if (next instanceof VenOrderStatus) {
					if (((VenOrderStatus) next).getOrderStatusCode() != null) {
						_log.debug("Restricting VenOrderStatus... :" + ((VenOrderStatus) next).getOrderStatusCode());
						
						VenOrderStatus venOrderStatus = venOrderStatusDAO.findByOrderStatusCode(((VenOrderStatus) next).getOrderStatusCode());
						
						if (venOrderStatus == null) {
							throw new EJBException("Order status does not exist");
						} else {
							retVal.add(venOrderStatus);
						}
					}
				}
				// Fraud check status needs to be restricted to Venice values
				if (next instanceof VenFraudCheckStatus) {
					if (((VenFraudCheckStatus) next).getFraudCheckStatusDesc() != null) {
						_log.debug("Restricting VenFraudCheckStatus... :" + ((VenFraudCheckStatus) next).getFraudCheckStatusDesc());
						try {
							VenFraudCheckStatusSessionEJBLocal fraudCheckStatusHome = (VenFraudCheckStatusSessionEJBLocal) this._genericLocator
									.lookupLocal(VenFraudCheckStatusSessionEJBLocal.class, "VenFraudCheckStatusSessionEJBBeanLocal");
							List<VenFraudCheckStatus> fraudCheckStatusList = fraudCheckStatusHome
									.queryByRange("select o from VenFraudCheckStatus o where o.fraudCheckStatusDesc ='" + ((VenFraudCheckStatus) next).getFraudCheckStatusDesc() + "'", 0, 1);
							if (fraudCheckStatusList == null || fraudCheckStatusList.isEmpty()) {
								throw new EJBException("Fraud check status value does not exist");
							} else {
								retVal.add(fraudCheckStatusList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenFraudCheckStatusSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenFraudCheckStatusSessionEJBBean:");
						}
					}
				}
				// Contact details need to be synchronized
				if (next instanceof VenContactDetail) {
					if (((VenContactDetail) next).getVenContactDetailType() != null) {
						try {
							_log.debug("Synchronizing VenContactDetail... :" + ((VenContactDetail) next).getVenContactDetailType());
							// Synchronize the reference data
							next = this.synchronizeVenContactDetailReferenceData((VenContactDetail) next);
							// Synchronize the object
							VenContactDetailSessionEJBLocal contactDetailHome = (VenContactDetailSessionEJBLocal) this._genericLocator
									.lookupLocal(VenContactDetailSessionEJBLocal.class, "VenContactDetailSessionEJBBeanLocal");
							VenContactDetail contactDetail = (VenContactDetail) contactDetailHome.persistVenContactDetail((VenContactDetail) next);
							retVal.add(contactDetail);

						} catch (Exception e) {
							_log.error("An exception occured when looking up VenContactDetailSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenContactDetailSessionEJBBean:");
						}
					}
				}
				// Contact detail types need to be restricted to Venice values
				if (next instanceof VenContactDetailType) {
					if (((VenContactDetailType) next).getContactDetailTypeDesc() != null) {
						_log.debug("Restricting VenContactDetailType... :" + ((VenContactDetailType) next).getContactDetailTypeDesc());
						try {
							VenContactDetailTypeSessionEJBLocal contactDetailTypeHome = (VenContactDetailTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenContactDetailTypeSessionEJBLocal.class, "VenContactDetailTypeSessionEJBBeanLocal");
							List<VenContactDetailType> contactDetailTypeList = contactDetailTypeHome
									.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeDesc ='" + ((VenContactDetailType) next).getContactDetailTypeDesc() + "'", 0, 1);
							if (contactDetailTypeList == null || contactDetailTypeList.isEmpty()) {
								throw new EJBException("Contact detail type does not exist");
							} else {
								retVal.add(contactDetailTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenContactDetailTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenContactDetailTypeSessionEJBBean:");
						}
					}
				}
				// Blocking sources need to be restricted to Venice values
				if (next instanceof VenOrderBlockingSource) {
					if (((VenOrderBlockingSource) next).getBlockingSourceDesc() != null) {
						_log.debug("Restricting VenOrderBlockingSource... :" + ((VenOrderBlockingSource) next).getBlockingSourceDesc());
							
						VenOrderBlockingSource venOrderBlockingSource = venOrderBlockingSourceDAO.findByBlockingSourceDesc(((VenOrderBlockingSource) next).getBlockingSourceDesc());
						
						if (venOrderBlockingSource == null) {
							throw new EJBException("Order blocking source does not exist");
						} else {
							retVal.add(venOrderBlockingSource);
						}
					}
				}
				// Party types need to be restricted to Venice values
				if (next instanceof VenPartyType) {
					if (((VenPartyType) next).getPartyTypeDesc() != null) {
						try {
							_log.debug("Restricting VenPartyType... :" + ((VenPartyType) next).getPartyTypeId());
							VenPartyTypeSessionEJBLocal partyTypeHome = (VenPartyTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPartyTypeSessionEJBLocal.class, "VenPartyTypeSessionEJBBeanLocal");

							List<VenPartyType> partyTypeList = partyTypeHome
									.queryByRange("select o from VenPartyType o where o.partyTypeId =" + ((VenPartyType) next).getPartyTypeId(), 0, 1);
							if (partyTypeList == null || partyTypeList.isEmpty()) {
								throw new EJBException("Party type does not exist");
							} else {
								retVal.add(partyTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenPartyTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenPartyTypeSessionEJBBean:");
						}
					}
				}
				// Payment status must be restricted to Venice values
				if (next instanceof VenPaymentStatus) {
					if (((VenPaymentStatus) next).getPaymentStatusCode() != null) {
						try {
							_log.debug("Restricting VenPaymentStatus... :" + ((VenPaymentStatus) next).getPaymentStatusCode());
							VenPaymentStatusSessionEJBLocal paymentStatusHome = (VenPaymentStatusSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPaymentStatusSessionEJBLocal.class, "VenPaymentStatusSessionEJBBeanLocal");
							List<VenPaymentStatus> paymentStatusList = paymentStatusHome
									.queryByRange("select o from VenPaymentStatus o where o.paymentStatusCode ='" + ((VenPaymentStatus) next).getPaymentStatusCode() + "'", 0, 1);
							if (paymentStatusList == null || paymentStatusList.isEmpty()) {
								throw new EJBException("Payment status does not exist");
							} else {
								retVal.add(paymentStatusList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenPaymentStatusSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPaymentStatusSessionEJBBean:");
						}
					}
				}
				// Payment type must be restricted to Venice values
				if (next instanceof VenPaymentType) {
					if (((VenPaymentType) next).getPaymentTypeCode() != null) {
						try {
							_log.debug("Restricting VenPaymentType... :" + ((VenPaymentType) next).getPaymentTypeCode());
							VenPaymentTypeSessionEJBLocal paymentTypeHome = (VenPaymentTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPaymentTypeSessionEJBLocal.class, "VenPaymentTypeSessionEJBBeanLocal");
							List<VenPaymentType> paymentTypeList = paymentTypeHome
									.queryByRange("select o from VenPaymentType o where o.paymentTypeCode ='" + ((VenPaymentType) next).getPaymentTypeCode() + "'", 0, 1);
							if (paymentTypeList == null || paymentTypeList.isEmpty()) {
								throw new EJBException("Payment type does not exist");
							} else {
								retVal.add(paymentTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenPaymentTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPaymentTypeSessionEJBBean:");
						}
					}
				}
				// WCS Payment type must be restricted
				if (next instanceof VenWcsPaymentType) {
					if (((VenWcsPaymentType) next).getWcsPaymentTypeCode() != null) {
						try {
							_log.debug("Restricting VenWcsPaymentType... :" + ((VenWcsPaymentType) next).getWcsPaymentTypeCode());
							VenWcsPaymentTypeSessionEJBLocal wcsPaymentTypeHome = (VenWcsPaymentTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenWcsPaymentTypeSessionEJBLocal.class, "VenWcsPaymentTypeSessionEJBBeanLocal");
							List<VenWcsPaymentType> wcsPaymentTypeList = wcsPaymentTypeHome
									.queryByRange("select o from VenWcsPaymentType o where o.wcsPaymentTypeCode ='" + ((VenWcsPaymentType) next).getWcsPaymentTypeCode() + "'", 0, 1);
							if (wcsPaymentTypeList == null || wcsPaymentTypeList.isEmpty()) {
								throw new EJBException("WCS Payment type does not exist");
							} else {
								retVal.add(wcsPaymentTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenWcsPaymentTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenWcsPaymentTypeSessionEJBBean:");
						}
					}
				}

				// Product categories need to be synchronized
				if (next instanceof VenProductCategory) {
					if (((VenProductCategory) next).getProductCategory() != null) {
						try {
							_log.debug("Synchronizing VenProductCategory... :" + ((VenProductCategory) next).getProductCategory());
							VenProductCategorySessionEJBLocal productCategoryHome = (VenProductCategorySessionEJBLocal) this._genericLocator
									.lookupLocal(VenProductCategorySessionEJBLocal.class, "VenProductCategorySessionEJBBeanLocal");
							List<VenProductCategory> productCategoryList = productCategoryHome
									.queryByRange("select o from VenProductCategory o where o.productCategory ='" + ((VenProductCategory) next).getProductCategory() + "'", 0, 1);
							if (productCategoryList == null || productCategoryList.isEmpty()) {
								VenProductCategory productCategory = (VenProductCategory) productCategoryHome.persistVenProductCategory((VenProductCategory) next);
								retVal.add(productCategory);
							} else {
								retVal.add(productCategoryList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenProductCategorySessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenProductCategorySessionEJBBean:");
						}
					}
				}
				// Product types need to be synchronized
				if (next instanceof VenProductType) {
					if (((VenProductType) next).getProductTypeCode() != null) {
						try {
							_log.debug("Synchronizing VenProductType... :" + ((VenProductType) next).getProductTypeCode());
							VenProductTypeSessionEJBLocal productTypeHome = (VenProductTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenProductTypeSessionEJBLocal.class, "VenProductTypeSessionEJBBeanLocal");
							List<VenProductType> productTypeList = productTypeHome
									.queryByRange("select o from VenProductType o where o.productTypeCode ='" + ((VenProductType) next).getProductTypeCode() + "'", 0, 1);
							if (productTypeList == null || productTypeList.isEmpty()) {
								VenProductType productType = (VenProductType) productTypeHome.persistVenProductType((VenProductType) next);
								retVal.add(productType);

							} else {
								retVal.add(productTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenProductTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenProductTypeSessionEJBBean:");
						}
					}
				}
				// Promotions need to be synchronized
				if (next instanceof VenPromotion) {
					if (((VenPromotion) next).getPromotionCode() != null) {
						try {
							_log.debug("Synchronizing VenPromotion... :" + ((VenPromotion) next).getPromotionCode());
							VenPromotionSessionEJBLocal promotionHome = (VenPromotionSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPromotionSessionEJBLocal.class, "VenPromotionSessionEJBBeanLocal");
							
							VenPromotion promotion = new VenPromotion();
							
							List<VenPromotion> promotionExactList = promotionHome.queryByRange("select o from VenPromotion o where o.promotionCode = '"+((VenPromotion) next).getPromotionCode()+"' " +
									"and o.promotionName = '"+((VenPromotion) next).getPromotionName()+"' " +
									"and o.gdnMargin =" + ((VenPromotion) next).getGdnMargin() + " " +
									"and o.merchantMargin = " + ((VenPromotion) next).getMerchantMargin() + " " +
									"and o.othersMargin = " +((VenPromotion) next).getOthersMargin(), 0, 1);
							
							_log.debug("promotionExactList size: "+promotionExactList.size());
														
							if(promotionExactList.size()>0) {
								_log.debug("exact promo found");
								promotion=promotionExactList.get(0);								
								if(promotion.getVenPromotionType()==null || promotion.getVenPromotionType().getPromotionType()==null){	
									if(promotion.getPromotionName().toLowerCase().contains("free shipping")){
										VenPromotionType type = new VenPromotionType();
										type.setPromotionType(VeniceConstants.VEN_PROMOTION_TYPE_FREESHIPPING);
										promotion.setVenPromotionType(type);
										promotion=promotionHome.mergeVenPromotion(promotion);
									}																				
								}								
							}else {								
								_log.debug("exact promo not found, cek uploaded promo");
								List<VenPromotion> promotionUploadedList = promotionHome.queryByRange("select o from VenPromotion o where o.promotionCode = '"+((VenPromotion) next).getPromotionCode()+"' " +
										"and o.promotionName is null and o.gdnMargin is null and o.merchantMargin is null and o.othersMargin is null", 0, 1);
								if(promotionUploadedList.size()>0){
									_log.debug("uploaded promo found, set the promo name and margins and then merge");
									promotion=promotionUploadedList.get(0);
									promotion.setPromotionName(((VenPromotion) next).getPromotionName());
									promotion.setGdnMargin(((VenPromotion) next).getGdnMargin());
									promotion.setMerchantMargin(((VenPromotion) next).getMerchantMargin());
									promotion.setOthersMargin(((VenPromotion) next).getOthersMargin());	
									promotion=promotionHome.mergeVenPromotion(promotion);
								}else{
									_log.debug("no exact matching promo code, no uploaded promo, persist promo from inbound");
									promotion=promotionHome.persistVenPromotion((VenPromotion) next);
									
									//check the promo code for free shipping
									if(promotion.getVenPromotionType()==null || promotion.getVenPromotionType().getPromotionType()==null){											
										if(promotion.getPromotionName().toLowerCase().contains("free shipping")){
											VenPromotionType type = new VenPromotionType();
											type.setPromotionType(VeniceConstants.VEN_PROMOTION_TYPE_FREESHIPPING);
											promotion.setVenPromotionType(type);
											promotion=promotionHome.mergeVenPromotion(promotion);
										}
									}
								}															
							}
							retVal.add(promotion);							
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenPromotionSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPromotionSessionEJBBean:");
						}
					}
				}
				// Cities need to be synchronized
				if (next instanceof VenCity) {
					if (((VenCity) next).getCityCode() != null) {
						_log.debug("Synchronizing VenCity... :" + ((VenCity) next).getCityCode());
						try {
							VenCitySessionEJBLocal cityHome = (VenCitySessionEJBLocal) this._genericLocator.lookupLocal(VenCitySessionEJBLocal.class, "VenCitySessionEJBBeanLocal");
							List<VenCity> cityList = cityHome.queryByRange("select o from VenCity o where o.cityCode ='" + ((VenCity) next).getCityCode() + "'", 0, 1);
							if (cityList == null || cityList.isEmpty()) {
								VenCity city = (VenCity) cityHome.persistVenCity((VenCity) next);
								retVal.add(city);

							} else {
								retVal.add(cityList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenCitySessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenCitySessionEJBBean:");
						}
					}
				}
				// States need to be synchronized
				if (next instanceof VenState) {
					if (((VenState) next).getStateCode() != null) {
						try {
							_log.debug("Synchronizing VenState... :" + ((VenState) next).getStateCode());
							VenStateSessionEJBLocal stateHome = (VenStateSessionEJBLocal) this._genericLocator
									.lookupLocal(VenStateSessionEJBLocal.class, "VenStateSessionEJBBeanLocal");
							List<VenState> stateList = stateHome.queryByRange("select o from VenState o where o.stateCode ='" + ((VenState) next).getStateCode() + "'", 0, 1);
							if (stateList == null || stateList.isEmpty()) {
								VenState state = (VenState) stateHome.persistVenState((VenState) next);
								retVal.add(state);

							} else {
								retVal.add(stateList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenStateSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenStateSessionEJBBean:");
						}
					}
				}
				// Countries need to be synchronized
				if (next instanceof VenCountry) {
					if (((VenCountry) next).getCountryCode() != null) {
						try {
							_log.debug("Synchronizing VenCountry... :" + ((VenCountry) next).getCountryCode());
							VenCountrySessionEJBLocal countryHome = (VenCountrySessionEJBLocal) this._genericLocator
									.lookupLocal(VenCountrySessionEJBLocal.class, "VenCountrySessionEJBBeanLocal");
							List<VenCountry> countryList = countryHome
									.queryByRange("select o from VenCountry o where o.countryCode ='" + ((VenCountry) next).getCountryCode() + "'", 0, 1);
							if (countryList == null || countryList.isEmpty()) {
								VenCountry country = (VenCountry) countryHome.persistVenCountry((VenCountry) next);
								retVal.add(country);
							} else {
								retVal.add(countryList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenCountrySessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenCountrySessionEJBBean:");
						}
					}
				}
				// Address types need to be restricted
				if (next instanceof VenAddressType) {
					if (((VenAddressType) next).getAddressTypeId() != null) {
						try {
							_log.debug("Restricting VenAddressType... :" + ((VenAddressType) next).getAddressTypeId());
							VenAddressTypeSessionEJBLocal addressTypeHome = (VenAddressTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenAddressTypeSessionEJBLocal.class, "VenAddressTypeSessionEJBBeanLocal");
							List<VenAddressType> addressTypeList = addressTypeHome
									.queryByRange("select o from VenAddressType o where o.addressTypeId =" + ((VenAddressType) next).getAddressTypeId(), 0, 1);
							if (addressTypeList == null || addressTypeList.isEmpty()) {
								VenAddressType addressType = (VenAddressType) addressTypeHome.persistVenAddressType((VenAddressType) next);
								retVal.add(addressType);

							} else {
								retVal.add(addressTypeList.get(0));
							}
						} catch (Exception e) {
							_log.error("An exception occured when looking up VenAddressTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenAddressTypeSessionEJBBean:");
						}
					}
				}							
			}
		}
		return retVal;
	}
	
	/**
	 * Used to check the status history for an order item (for example if it has ever been PU.
	 * @param orderItemId
	 * @param orderStatusId
	 * @return true if the hostory record exists else false
	 */
	@SuppressWarnings("unused")
	private Boolean checkOrderItemStatusHistory(Long orderItemId, Long orderStatusId) {
		try {
			VenOrderItemStatusHistorySessionEJBLocal orderItemStatusHistoryHome = (VenOrderItemStatusHistorySessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderItemStatusHistorySessionEJBLocal.class, "VenOrderItemStatusHistorySessionEJBBeanLocal");

			List<VenOrderItemStatusHistory> orderItemStatusHistory = orderItemStatusHistoryHome
					.queryByRange("select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId =" + orderItemId + " and o.venOrderStatus.orderStatusId =" + orderStatusId, 0, 0);
			
			if(!orderItemStatusHistory.isEmpty()){
				return true;
			}

		} catch (Exception e) {
			String errMsg = "An exception occured when checking order item status history:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Create a history record for the new status for an order item 
	 * received as an updateOrderItemStatus message
	 * @param venOrderItem
	 * @param orderStatusId
	 * @return
	 */
	private Boolean createOrderItemStatusHistory(VenOrderItem venOrderItem, VenOrderStatus venOrderStatus){
		try {
			_log.debug("add order item status history");
			_log.debug("\n wcs order item id: "+venOrderItem.getWcsOrderItemId());
			_log.debug("\n order item status: "+venOrderItem.getVenOrderStatus().getOrderStatusCode());
			VenOrderItemStatusHistorySessionEJBLocal orderItemStatusHistoryHome = (VenOrderItemStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderItemStatusHistorySessionEJBLocal.class, "VenOrderItemStatusHistorySessionEJBBeanLocal");
			
			VenOrderItemStatusHistory venOrderItemStatusHistory = new VenOrderItemStatusHistory();
			
			VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
			venOrderItemStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venOrderItemStatusHistoryPK.setOrderItemId(venOrderItem.getOrderItemId());
			
			venOrderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
			venOrderItemStatusHistory.setVenOrderItem(venOrderItem);
			venOrderItemStatusHistory.setStatusChangeReason("Updated by System");
			venOrderItemStatusHistory.setVenOrderStatus(venOrderStatus);
			
			venOrderItemStatusHistory = orderItemStatusHistoryHome.persistVenOrderItemStatusHistory(venOrderItemStatusHistory);
			_log.debug("done add order item status history");
			if(venOrderItemStatusHistory != null){
				return true;
			}
			return false;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating order item status history:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	private Boolean createOrderStatusHistory(VenOrder venOrder, VenOrderStatus venOrderStatus){
		try {
			_log.debug("add order status history");
			_log.debug("\n wcs order id: "+venOrder.getWcsOrderId());
			_log.debug("\n order status: "+venOrder.getVenOrderStatus().getOrderStatusCode());
			VenOrderStatusHistorySessionEJBLocal orderStatusHistoryHome = (VenOrderStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderStatusHistorySessionEJBLocal.class, "VenOrderStatusHistorySessionEJBBeanLocal");
			
			VenOrderStatusHistory venOrderStatusHistory = new VenOrderStatusHistory();
			
			VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
			venOrderStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venOrderStatusHistoryPK.setOrderId(venOrder.getOrderId());
			
			venOrderStatusHistory.setId(venOrderStatusHistoryPK);
			venOrderStatusHistory.setVenOrder(venOrder);
			venOrderStatusHistory.setStatusChangeReason("Updated by System");
			venOrderStatusHistory.setVenOrderStatus(venOrderStatus);
			
			venOrderStatusHistory = orderStatusHistoryHome.persistVenOrderStatusHistory(venOrderStatusHistory);
			_log.debug("done add order status history");
			if(venOrderStatusHistory != null){
				return true;
			}
			return false;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating order status history:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	private Boolean createReturStatusHistory(VenRetur venRetur, VenOrderStatus venReturStatus){
		try {
			_log.debug("add retur status history");
			_log.debug("\n wcs retur id: "+venRetur.getWcsReturId());
			_log.debug("\n retur status: "+venRetur.getVenReturStatus().getOrderStatusCode());
			VenReturStatusHistorySessionEJBLocal returStatusHistoryHome = (VenReturStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenReturStatusHistorySessionEJBLocal.class, "VenReturStatusHistorySessionEJBBeanLocal");
			
			VenReturStatusHistory venReturStatusHistory = new VenReturStatusHistory();
			
			VenReturStatusHistoryPK venReturStatusHistoryPK = new VenReturStatusHistoryPK();
			venReturStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venReturStatusHistoryPK.setReturId(venRetur.getReturId());
			
			venReturStatusHistory.setId(venReturStatusHistoryPK);
			venReturStatusHistory.setVenRetur(venRetur);
			venReturStatusHistory.setStatusChangeReason("Updated by System");
			venReturStatusHistory.setVenReturStatus(venReturStatus);
			
			venReturStatusHistory = returStatusHistoryHome.persistVenReturStatusHistory(venReturStatusHistory);
			_log.debug("done add retur status history");
			if(venReturStatusHistory != null){
				return true;
			}
			return false;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating retur status history:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	private Boolean createReturItemStatusHistory(VenReturItem venReturItem, VenOrderStatus venReturStatus){
		try {
			_log.debug("add retur item status history");
			_log.debug("\n wcs retur item id: "+venReturItem.getWcsReturItemId());
			_log.debug("\n retur item status: "+venReturItem.getVenReturStatus().getOrderStatusCode());
			VenReturItemStatusHistorySessionEJBLocal returItemStatusHistoryHome = (VenReturItemStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenReturItemStatusHistorySessionEJBLocal.class, "VenReturItemStatusHistorySessionEJBBeanLocal");
			
			VenReturItemStatusHistory venReturItemStatusHistory = new VenReturItemStatusHistory();
			
			VenReturItemStatusHistoryPK venReturItemStatusHistoryPK = new VenReturItemStatusHistoryPK();
			venReturItemStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venReturItemStatusHistoryPK.setReturItemId(venReturItem.getReturItemId());
			
			venReturItemStatusHistory.setId(venReturItemStatusHistoryPK);
			venReturItemStatusHistory.setVenReturItem(venReturItem);
			venReturItemStatusHistory.setStatusChangeReason("Updated by System");
			venReturItemStatusHistory.setVenReturStatus(venReturStatus);
			
			venReturItemStatusHistory = returItemStatusHistoryHome.persistVenReturItemStatusHistory(venReturItemStatusHistory);
			_log.debug("done add retur item status history");
			if(venReturItemStatusHistory != null){
				return true;
			}
			return false;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating retur item status history:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
	}
	
	/**
	 * Clocks the merchant fulfillment response KPI and updates the period actual
	 * @param locator
	 * @param venOrderItem
	 * @param logMerchantPickupInstruction
	 * @return
	 */
	@SuppressWarnings("unused")
	private Boolean clockMerchantFulfillmentResponseKpi(Locator<Object> locator, VenOrderItem venOrderItem, LogMerchantPickupInstruction logMerchantPickupInstruction){
		try{
			KPI_TransactionPosterSessionEJBLocal kpiTransactionPosterHome = (KPI_TransactionPosterSessionEJBLocal) locator
			.lookupLocal(KPI_TransactionPosterSessionEJBLocal.class, "KPI_TransactionPosterSessionEJBBeanLocal");
			
			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");

			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");
			
			Calendar orderDateCal = Calendar.getInstance();
			orderDateCal.setTime(new Date(venOrderItem.getVenOrder().getOrderDate().getTime()));
			
			Calendar pickupDateCal = Calendar.getInstance();
			pickupDateCal.setTime(new Date(logMerchantPickupInstruction.getPickupDateTime().getTime()));

			Integer fulfillmentResponseKPIValue = HolidayUtil.getWorkingDaysBetweenDates(orderDateCal.getTime(), pickupDateCal.getTime());
			KpiMeasurementPeriod period = KpiPeriodUtil.getCurrentPeriod();
			Long partyId = venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId();

			kpiTransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE, period.getKpiPeriodId(), partyId, fulfillmentResponseKPIValue, "Fulfillment response for Order Item:" + venOrderItem.getWcsOrderItemId());

			KpiPartyPeriodActual kpiPartyPeriodActual = null;
			Boolean bActualRecordExists = false;
			
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.id.partyId = " + partyId + " and o.id.kpiPeriodId = " +  period.getKpiPeriodId() + " and o.id.kpiId = " + VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE, 0, 0);
			
			/*
			 * If the period actual doesn't exist then create it
			 * otherwise update it
			 */
			if(kpiPartyPeriodActualList == null || kpiPartyPeriodActualList.isEmpty()){
				kpiPartyPeriodActual = new KpiPartyPeriodActual();
				KpiPartyPeriodActualPK id = new KpiPartyPeriodActualPK();
				id.setKpiId(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE);
				id.setKpiPeriodId(period.getKpiPeriodId());
				id.setPartyId(partyId);
				kpiPartyPeriodActual.setId(id);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
			}else{
				kpiPartyPeriodActual = kpiPartyPeriodActualList.get(0);
				bActualRecordExists = true;
			}
			
			/*
			 * Get the  KPI transactions for the KPI within the period
			 * and average the value to get the result
			 */
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList = 
				kpiPartyPeriodTransactionHome.queryByRange("select o from KpiPartyPeriodTransaction o where o.kpiPartyMeasurementPeriod.kpiMeasurementPeriod.kpiPeriodId = " + period.getKpiPeriodId() + 
						" and o.kpiPartyMeasurementPeriod.venParty.partyId = " + partyId + 
						" and o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE, 0, 0);
			
			ArrayList<Integer> kpiTransactionsInPeriod = new ArrayList<Integer>();
			for(KpiPartyPeriodTransaction transaction:kpiPartyPeriodTransactionList){
				kpiTransactionsInPeriod.add(transaction.getKpiTransactionValue());
			}
			
			Integer kpiAverage = 0;
			if(kpiTransactionsInPeriod.size() > 0){
				kpiAverage = MathUtil.avg(kpiTransactionsInPeriod);
			}
			
			/*
			 * Update the actual record with the appropriate value 
			 */
			if(kpiAverage <= 1){
				kpiPartyPeriodActual.setKpiCalculatedValue(10);
			}else if(kpiAverage <= 2){
				kpiPartyPeriodActual.setKpiCalculatedValue(8);
			}else if(kpiAverage <= 3){
				kpiPartyPeriodActual.setKpiCalculatedValue(6);
			}else if(kpiAverage <= 4){
				kpiPartyPeriodActual.setKpiCalculatedValue(4);
			}else{
				kpiPartyPeriodActual.setKpiCalculatedValue(2);
			}
			
			/*
			 * If the record existed then merge else persist 
			 */
			if(bActualRecordExists){
				kpiPartyPeriodActualHome.mergeKpiPartyPeriodActual(kpiPartyPeriodActual);
			}else{
				kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
		} catch (Exception e) {
			String errMsg = "An exception occured when clocking the merchant fulfillment response KPI:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		return true;
	}
	
	/**
	 * Clocks the KPI for merchant partial fulfillment and updates the period actual
	 * @param locator a locator for EJB lookup
	 * @param venOrderItem the order item in question
	 * @return true if the operation succeeds else false
	 */
	private Boolean clockMerchantPartialFulfillmentKpi(Locator<Object> locator, VenOrderItem venOrderItem){
		try{
			KPI_TransactionPosterSessionEJBLocal kpiTransactionPosterHome = (KPI_TransactionPosterSessionEJBLocal) locator
			.lookupLocal(KPI_TransactionPosterSessionEJBLocal.class, "KPI_TransactionPosterSessionEJBBeanLocal");
			
			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");

			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");

			/*
			 * Post 1 for the transaction and then update the 
			 * party period actual score for the period as
			 *  o Zero times = 10
			 *  o One time = 8
			 *  o Two times = 6
			 *  o Three times = 4
			 *  o Four times or more = 2
			 */
			KpiMeasurementPeriod period = KpiPeriodUtil.getCurrentPeriod();
			Long partyId = venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId();
			
			kpiTransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY, period.getKpiPeriodId(), partyId, 1, "Partial fulfillment for Order Item:" + venOrderItem.getWcsOrderItemId());
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.id.partyId = " + partyId + " and o.id.kpiPeriodId = " +  period.getKpiPeriodId() + " and o.id.kpiId = " + VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY, 0, 0);
			
			KpiPartyPeriodActual kpiPartyPeriodActual = null;
			Boolean bActualRecordExists = false;
			
			/*
			 * If the period actual doesn't exist then create it
			 * otherwise update it
			 */
			if(kpiPartyPeriodActualList == null || kpiPartyPeriodActualList.isEmpty()){
				kpiPartyPeriodActual = new KpiPartyPeriodActual();
				KpiPartyPeriodActualPK id = new KpiPartyPeriodActualPK();
				id.setKpiId(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY);
				id.setKpiPeriodId(period.getKpiPeriodId());
				id.setPartyId(partyId);
				kpiPartyPeriodActual.setId(id);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
			}else{
				kpiPartyPeriodActual = kpiPartyPeriodActualList.get(0);
				bActualRecordExists = true;
			}
			
			/*
			 * Get the number of KPI transactions for the KPI within the period
			 */
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList = 
				kpiPartyPeriodTransactionHome.queryByRange("select o from KpiPartyPeriodTransaction o where o.kpiPartyMeasurementPeriod.kpiMeasurementPeriod.kpiPeriodId = " + period.getKpiPeriodId() + 
						" and o.kpiPartyMeasurementPeriod.venParty.partyId = " + partyId + 
						" and o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY, 0, 0);
			
			Integer kpiTransactionsInPeriod = 0;
			for(KpiPartyPeriodTransaction transaction:kpiPartyPeriodTransactionList){
				kpiTransactionsInPeriod = kpiTransactionsInPeriod + transaction.getKpiTransactionValue();
			}
			
			/*
			 * Update the actual record with the appropriate value 
			 */
			if(kpiTransactionsInPeriod == 0){
				kpiPartyPeriodActual.setKpiCalculatedValue(10);
			}else if(kpiTransactionsInPeriod == 1){
				kpiPartyPeriodActual.setKpiCalculatedValue(8);
			}else if(kpiTransactionsInPeriod == 2){
				kpiPartyPeriodActual.setKpiCalculatedValue(6);
			}else if(kpiTransactionsInPeriod == 3){
				kpiPartyPeriodActual.setKpiCalculatedValue(4);
			}else{
				kpiPartyPeriodActual.setKpiCalculatedValue(2);
			}
			
			/*
			 * If the record existed then merge else persist 
			 */
			if(bActualRecordExists){
				kpiPartyPeriodActualHome.mergeKpiPartyPeriodActual(kpiPartyPeriodActual);
			}else{
				kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
		} catch (Exception e) {
			String errMsg = "An exception occured when clocking the merchant partial fulfillment KPI:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		return true;
	}
	
	/**
	 * Creates a default party SLA and targets for the merchant
	 * @param locator the locator for EJBs
	 * @param venMerchant the merchant in question
	 * @return true if the operation succeeds else false.
	 */
	private Boolean createDefaultMerchantKPIPartyTargetRecords(Locator<Object> locator, VenMerchant venMerchant){
		try{
			KpiPartyTargetSessionEJBLocal kpiPartyTargetHome = (KpiPartyTargetSessionEJBLocal) locator
			.lookupLocal(KpiPartyTargetSessionEJBLocal.class, "KpiPartyTargetSessionEJBBeanLocal");
			
			KpiPartySlaSessionEJBLocal kpiPartySlaHome = (KpiPartySlaSessionEJBLocal) locator
			.lookupLocal(KpiPartySlaSessionEJBLocal.class, "KpiPartySlaSessionEJBBeanLocal");
			
			Long partyId = venMerchant.getVenParty().getPartyId();
			
			/*
			 * If there is no party SLA then create one.
			 */
			List<KpiPartySla> kpiPartySlaList = kpiPartySlaHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = " + partyId, 0, 0);
			
			KpiPartySla kpiPartySla = null;
			if(kpiPartySlaList.isEmpty()){
				kpiPartySla = new KpiPartySla();
				kpiPartySla.setVenParty(venMerchant.getVenParty());
				kpiPartySla = kpiPartySlaHome.persistKpiPartySla(kpiPartySla);
			}else{
				kpiPartySla = kpiPartySlaList.get(0);
			}
			
			List<KpiPartyTarget> kpiPartyTargetList = kpiPartyTargetHome.queryByRange("select o from KpiPartyTarget o where o.kpiPartySla.partySlaId = " + kpiPartySla.getPartySlaId(), 0, 0);
			
			/*
			 * If there are no party targets then create the defaults
			 * for the merchant targets of fulfillment response
			 * and partial fulfillment frequency
			 */
			if(kpiPartyTargetList.isEmpty()){				
				KpiTargetBaseline kpiTargetBaselineWorst = new KpiTargetBaseline();
				kpiTargetBaselineWorst.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_WORST);

				KpiTargetBaseline kpiTargetBaselineAverage = new KpiTargetBaseline();
				kpiTargetBaselineAverage.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_AVERAGE);

				KpiTargetBaseline kpiTargetBaselineBest = new KpiTargetBaseline();
				kpiTargetBaselineBest.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_BEST);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicatorPF_Frequency = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicatorPF_Frequency.setKpiId(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicatorFulfillmentResponse = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicatorFulfillmentResponse.setKpiId(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE);

				List<KpiPartyTarget> newKpiPartyTargetList = new ArrayList<KpiPartyTarget>();
				
				/*
				 * For partial fulfillment frequency 
				 */
				KpiPartyTarget worstTargetPF_FrequencyDefault = new KpiPartyTarget();
				worstTargetPF_FrequencyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPF_Frequency);
				worstTargetPF_FrequencyDefault.setKpiPartySla(kpiPartySla);
				worstTargetPF_FrequencyDefault.setKpiTargetBaseline(kpiTargetBaselineWorst);
				worstTargetPF_FrequencyDefault.setKpiTargetValue(6);
				newKpiPartyTargetList.add(worstTargetPF_FrequencyDefault);
				
				KpiPartyTarget avgTargetPF_FrequencyDefault = new KpiPartyTarget();
				avgTargetPF_FrequencyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPF_Frequency);
				avgTargetPF_FrequencyDefault.setKpiPartySla(kpiPartySla);
				avgTargetPF_FrequencyDefault.setKpiTargetBaseline(kpiTargetBaselineAverage);
				avgTargetPF_FrequencyDefault.setKpiTargetValue(8);
				newKpiPartyTargetList.add(avgTargetPF_FrequencyDefault);
				
				KpiPartyTarget bestTargetPF_FrequencyDefault = new KpiPartyTarget();
				bestTargetPF_FrequencyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPF_Frequency);
				bestTargetPF_FrequencyDefault.setKpiPartySla(kpiPartySla);
				bestTargetPF_FrequencyDefault.setKpiTargetBaseline(kpiTargetBaselineBest);
				bestTargetPF_FrequencyDefault.setKpiTargetValue(10);
				newKpiPartyTargetList.add(bestTargetPF_FrequencyDefault);
				
				/*
				 * For fulfillment response
				 */
				KpiPartyTarget worstTargetFulfillmentResponseDefault = new KpiPartyTarget();
				worstTargetFulfillmentResponseDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorFulfillmentResponse);
				worstTargetFulfillmentResponseDefault.setKpiPartySla(kpiPartySla);
				worstTargetFulfillmentResponseDefault.setKpiTargetBaseline(kpiTargetBaselineWorst);
				worstTargetFulfillmentResponseDefault.setKpiTargetValue(6);
				newKpiPartyTargetList.add(worstTargetFulfillmentResponseDefault);

				KpiPartyTarget avgTargetFulfillmentResponseDefault = new KpiPartyTarget();
				avgTargetFulfillmentResponseDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorFulfillmentResponse);
				avgTargetFulfillmentResponseDefault.setKpiPartySla(kpiPartySla);
				avgTargetFulfillmentResponseDefault.setKpiTargetBaseline(kpiTargetBaselineAverage);
				avgTargetFulfillmentResponseDefault.setKpiTargetValue(8);
				newKpiPartyTargetList.add(avgTargetFulfillmentResponseDefault);

				KpiPartyTarget bestTargetFulfillmentResponseDefault = new KpiPartyTarget();
				bestTargetFulfillmentResponseDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorFulfillmentResponse);
				bestTargetFulfillmentResponseDefault.setKpiPartySla(kpiPartySla);
				bestTargetFulfillmentResponseDefault.setKpiTargetBaseline(kpiTargetBaselineBest);
				bestTargetFulfillmentResponseDefault.setKpiTargetValue(10);
				newKpiPartyTargetList.add(bestTargetFulfillmentResponseDefault);

				/*
				 * Persist the new values
				 */
				newKpiPartyTargetList = kpiPartyTargetHome.persistKpiPartyTargetList(newKpiPartyTargetList);
			}
		}catch (Exception e) {
			String errMsg = "An exception occured when creating default merchant KPI party target records:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		return true;	
	}
	
	/**
	 * Creates the merchant KPI actual records for partial fulfillment and fulfillement response
	 * for the current period using a default of 10 as the value if the records dont already exist.
	 * @param locator
	 * @param venMerchant
	 * @return
	 */
	private Boolean createDefaultMerchantCurrentKPIPeriodActualRecords(Locator<Object> locator, VenMerchant venMerchant){
		try{
			KpiPartyMeasurementPeriodSessionEJBLocal kpiPartyMeasurementPeriodHome = (KpiPartyMeasurementPeriodSessionEJBLocal) locator
			.lookupLocal(KpiPartyMeasurementPeriodSessionEJBLocal.class, "KpiPartyMeasurementPeriodSessionEJBBeanLocal");

			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");
			
			Long partyId = venMerchant.getVenParty().getPartyId();
			KpiMeasurementPeriod period = KpiPeriodUtil.getCurrentPeriod();

			/*
			 * If the KPI party measurement period doesn't exist then create it
			 */
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList = kpiPartyMeasurementPeriodHome.queryByRange("select o from KpiPartyMeasurementPeriod o where o.kpiMeasurementPeriod.kpiPeriodId = " + period.getKpiPeriodId() + " and o.venParty.partyId = " + partyId, 0, 0);
			
			if(kpiPartyMeasurementPeriodList.isEmpty()){
				KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod = new KpiPartyMeasurementPeriod();
				KpiPartyMeasurementPeriodPK id = new KpiPartyMeasurementPeriodPK();
				id.setKpiPeriodId(period.getKpiPeriodId());
				id.setPartyId(partyId);
				kpiPartyMeasurementPeriod.setId(id);
				kpiPartyMeasurementPeriod.setKpiMeasurementPeriod(period);
				kpiPartyMeasurementPeriod.setVenParty(venMerchant.getVenParty());
				
				kpiPartyMeasurementPeriodHome.persistKpiPartyMeasurementPeriod(kpiPartyMeasurementPeriod);
			}

			/*
			 * If the partial fulfillment KPI actual 
			 * doesn't exist for the period then create it
			 */
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.id.partyId = " + partyId + " and o.id.kpiPeriodId = " +  period.getKpiPeriodId() + " and o.id.kpiId = " + VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY, 0, 0);

			if(kpiPartyPeriodActualList.isEmpty()){
				KpiPartyPeriodActual kpiPartyPeriodActual = new KpiPartyPeriodActual();
				KpiPartyPeriodActualPK id = new KpiPartyPeriodActualPK();
				id.setKpiId(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY);
				id.setKpiPeriodId(period.getKpiPeriodId());
				id.setPartyId(partyId);
				kpiPartyPeriodActual.setId(id);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
				
				kpiPartyPeriodActual.setKpiCalculatedValue(10);
				kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
			
			/*
			 * If the fulfillment response KPI actual 
			 * doesn't exist for the period then create it
			 */
			kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.id.partyId = " + partyId + " and o.id.kpiPeriodId = " +  period.getKpiPeriodId() + " and o.id.kpiId = " + VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE, 0, 0);
			if(kpiPartyPeriodActualList.isEmpty()){				
				KpiPartyPeriodActual kpiPartyPeriodActual = new KpiPartyPeriodActual();
				KpiPartyPeriodActualPK id = new KpiPartyPeriodActualPK();
				id.setKpiId(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE);
				id.setKpiPeriodId(period.getKpiPeriodId());
				id.setPartyId(partyId);
				kpiPartyPeriodActual.setId(id);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_MERCHANT_FULFILLMENT_RESPONSE);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
				
				kpiPartyPeriodActual.setKpiCalculatedValue(10);
				kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
		}catch (Exception e) {
			String errMsg = "An exception occured when creating default merchant KPI actual records:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();		
			throw new EJBException(errMsg + e.getMessage());
		}
		return true;
	}
	
	private void createDummyAirwaybillRetur(Locator<Object> locator, VenReturItem item) throws Exception{
		//add dummy airway bill when status become R	
		_log.debug("Check if status R then add dummy airway bill");
		LogAirwayBillReturSessionEJBLocal airwayBillReturHome = (LogAirwayBillReturSessionEJBLocal) locator.lookupLocal(LogAirwayBillReturSessionEJBRemote.class, "LogAirwayBillReturSessionEJBBeanLocal");		
		
		//delete dummy airway bill if it is already exist.
		_log.debug("check if dummy airwaybill already exist");
		
		EntityManager em = emf.createEntityManager();
		@SuppressWarnings("deprecation")
		Connection conn = ((EntityManagerImpl)em).getSession().connection();
		String deleteAirwayBillReturSQL = "delete from log_airway_bill_retur where retur_item_id = ?";
		PreparedStatement psAirwayBillRetur = conn.prepareStatement(deleteAirwayBillReturSQL);
		psAirwayBillRetur.setLong(1, item.getReturItemId());
		int totalAirwayBillDeleted = psAirwayBillRetur.executeUpdate();
		
		psAirwayBillRetur.close();
		
		_log.debug(totalAirwayBillDeleted + " dummy airway bill retur deleted");

		String returItemSQL = "select oi.*, " +
										"p.*, " +
										"a.*, " +
										"c.*, " +
										"ls.*, " +
										"lp.*, " +
										"m.* " +
					                "from ven_retur_item oi " +
					                "inner join ven_merchant_product mp on mp.product_id = oi.product_id " +
					                "inner join ven_merchant m on m.merchant_id = mp.merchant_id " +
					                "inner join ven_party p on m.party_id = p.party_id " +
					                "inner join ven_address a on a.address_id = oi.shipping_address_id " +	
					                "inner join ven_city c on a.city_id = c.city_id " +
					                "inner join log_logistic_service ls on ls.logistics_service_id = oi.logistics_service_id " +
					                "inner join log_logistics_provider lp on lp.logistics_provider_id = ls.logistics_provider_id " +
					                "where wcs_retur_item_id = ?";
		
		PreparedStatement psReturItem = conn.prepareStatement(returItemSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		psReturItem.setString(1, item.getWcsReturItemId());
		ResultSet rsReturItem = psReturItem.executeQuery();
		
		rsReturItem.last();
		int totalReturItem = rsReturItem.getRow();
		rsReturItem.beforeFirst();
		
		VenReturItem returItemResult = new VenReturItem();
		if(totalReturItem > 0){
			rsReturItem.next();
						
			returItemResult.setEtd(rsReturItem.getInt("etd"));
			returItemResult.setGiftCardFlag(rsReturItem.getBoolean("gift_card_flag"));
			returItemResult.setGiftCardNote(rsReturItem.getString("gift_card_note"));
			returItemResult.setGiftWrapPrice(rsReturItem.getBigDecimal("gift_wrap_price"));
			returItemResult.setGiftWrapFlag(rsReturItem.getBoolean("gift_wrap_flag"));
			returItemResult.setInsuranceCost(rsReturItem.getBigDecimal("insurance_cost"));
			returItemResult.setMaxEstDate(rsReturItem.getTimestamp("max_est_date"));
			returItemResult.setMerchantSettlementFlag(rsReturItem.getBoolean("merchant_settlement_flag"));
			returItemResult.setMinEstDate(rsReturItem.getTimestamp("min_est_date"));
			returItemResult.setReturItemId(rsReturItem.getLong("retur_item_id"));
			returItemResult.setPackageCount(rsReturItem.getInt("package_count"));
			returItemResult.setPrice(rsReturItem.getBigDecimal("price"));
			returItemResult.setQuantity(rsReturItem.getInt("quantity"));
			returItemResult.setSaltCode(rsReturItem.getString("salt_code"));
			returItemResult.setShippingCost(rsReturItem.getBigDecimal("shipping_cost"));
			returItemResult.setShippingWeight(rsReturItem.getBigDecimal("shipping_weight"));
			returItemResult.setSpecialHandlingInstructions(rsReturItem.getString("special_handling_instructions"));
			returItemResult.setTotal(rsReturItem.getBigDecimal("total"));
			returItemResult.setWcsReturItemId(rsReturItem.getString("wcs_retur_item_id"));
			returItemResult.setLogisticsPricePerKg(rsReturItem.getBigDecimal("logistics_price_per_kg"));
			
			VenCity cityResult = new VenCity();
			cityResult.setCityName(rsReturItem.getString("city_name"));
			
			VenAddress shippingAddressResult = new VenAddress();
			shippingAddressResult.setAddressId(rsReturItem.getLong("shipping_address_id"));
			shippingAddressResult.setPostalCode(rsReturItem.getString("postal_code"));
			shippingAddressResult.setVenCity(cityResult);
			returItemResult.setVenAddress(shippingAddressResult);
			
			VenParty partyMerchantResult = new  VenParty();
			partyMerchantResult.setPartyId(rsReturItem.getLong("party_id"));
			partyMerchantResult.setFullOrLegalName(rsReturItem.getString("full_or_legal_name"));
			
			VenMerchant merchantResult = new VenMerchant();
			merchantResult.setMerchantId(rsReturItem.getLong("merchant_id"));
			merchantResult.setVenParty(partyMerchantResult);
			
			VenMerchantProduct productResult = new VenMerchantProduct();
			productResult.setProductId(rsReturItem.getLong("product_id"));
			productResult.setVenMerchant(merchantResult);
			returItemResult.setVenMerchantProduct(productResult);
			
			VenRetur returResult = new VenRetur();
			returResult.setReturId(rsReturItem.getLong("retur_id"));
			returItemResult.setVenRetur(returResult);
			
			VenOrderStatus venReturStatusResult = new VenOrderStatus();
			venReturStatusResult.setOrderStatusId(rsReturItem.getLong("retur_status_id"));
			returItemResult.setVenReturStatus(venReturStatusResult);
			
			VenRecipient venRecipientResult = new VenRecipient();
			venRecipientResult.setRecipientId(rsReturItem.getLong("recipient_id"));
			returItemResult.setVenRecipient(venRecipientResult);
			
			LogLogisticsProvider logisticsProviderResult = new LogLogisticsProvider();
			logisticsProviderResult.setLogisticsProviderCode(rsReturItem.getString("logistics_provider_code"));
			logisticsProviderResult.setLogisticsProviderId(rsReturItem.getLong("logistics_provider_id"));
			
			LogLogisticService logisticServiceResult = new LogLogisticService();
			logisticServiceResult.setLogisticsServiceId(rsReturItem.getLong("logistics_service_id"));
			logisticServiceResult.setServiceCode(rsReturItem.getString("service_code"));
			logisticServiceResult.setLogLogisticsProvider(logisticsProviderResult);
			returItemResult.setLogLogisticService(logisticServiceResult);
		}
		
		rsReturItem.close();
		psReturItem.close();
		
		_log.debug("add dummy airway bill retur for wcs retur item id: "+returItemResult.getWcsReturItemId());
		LogAirwayBillRetur airwayBillRetur = new LogAirwayBillRetur();
		airwayBillRetur.setVenReturItem(returItemResult);
		
		_log.debug("venReturItem.getVenMerchantProduct() => " + returItemResult.getVenMerchantProduct().getProductId());
		_log.debug("venReturItem.getVenMerchantProduct().getVenMerchant() => " + returItemResult.getVenMerchantProduct().getVenMerchant().getMerchantId());
		_log.debug("venReturItem.getVenMerchantProduct().getVenMerchant().getVenParty() => " + returItemResult.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId());
		airwayBillRetur.setShipper(returItemResult.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName());
		airwayBillRetur.setDestination(returItemResult.getVenAddress().getVenCity()!=null?returItemResult.getVenAddress().getVenCity().getCityName():"");
		airwayBillRetur.setZip(returItemResult.getVenAddress().getPostalCode());
		airwayBillRetur.setLogLogisticsProvider(returItemResult.getLogLogisticService().getLogLogisticsProvider());
		airwayBillRetur.setService(returItemResult.getLogLogisticService().getServiceCode());
		airwayBillRetur.setInsuranceCharge(returItemResult.getInsuranceCost());
		airwayBillRetur.setPricePerKg(returItemResult.getLogisticsPricePerKg());
		airwayBillRetur.setGiftWrapCharge(returItemResult.getGiftWrapPrice());
		if(totalReturItem==0){
			airwayBillRetur.setOrigin(returItemResult.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity()!=null?returItemResult.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity().getCityName():"");					
		}else{
			airwayBillRetur.setOrigin(returItemResult.getVenAddress().getVenCity()!=null?returItemResult.getVenAddress().getVenCity().getCityName():"");
		}
		
		BigDecimal totalCharge=new BigDecimal(0);
		if(airwayBillRetur.getVenReturItem()!=null && airwayBillRetur.getVenReturItem().getShippingCost()!=null){
			totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getShippingCost());
		}
		if(airwayBillRetur.getOtherCharge()!=null){
			totalCharge=totalCharge.add(airwayBillRetur.getOtherCharge());
		}
		if(airwayBillRetur.getInsuranceCharge()!=null){
			totalCharge=totalCharge.add(airwayBillRetur.getInsuranceCharge());
		}else{
			totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getInsuranceCost());
		}
		if(airwayBillRetur.getGiftWrapCharge()!=null){
			totalCharge=totalCharge.add(airwayBillRetur.getGiftWrapCharge());
		}else{
			totalCharge=totalCharge.add(airwayBillRetur.getVenReturItem().getGiftWrapPrice());
		}
		airwayBillRetur.setTotalCharge(totalCharge);
		
		// Set the default approval status' for the AWB
		LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
		logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
		airwayBillRetur.setLogApprovalStatus1(logApprovalStatus);
		airwayBillRetur.setLogApprovalStatus2(logApprovalStatus);
		airwayBillRetur.setMtaData(false);
												
		// Persist the dummy airway bills
		_log.debug("persist the dummy airway bills");
		airwayBillReturHome.persistLogAirwayBillRetur(airwayBillRetur);
		_log.debug("done persist the dummy airway bills");
		
		rsReturItem.close();
		psReturItem.close();
		conn.close();
	}
	

	public Boolean createRetur(Order order){
			_log.debug("\n start createRetur()");
			Long startTime = System.currentTimeMillis();
			
			/*
			 * We must try to validate as much of the order information as possible
			 */
		
			// Amount
			if (order.getAmount() == null ) {
				String errMsg = "\n createRetur: message received with no amount";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			// Customer
			if (order.getCustomer() == null) {
				String errMsg = "\n createRetur: message received with no customer information record";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			// Identifier
			if (order.getOrderId() == null) {
				String errMsg = "\n createRetur: message received with no identifier information record";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			// Order items
			if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
				String errMsg = "\n createRetur: message received with no order item information record";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			
			// Timestamp
			if (order.getTimestamp() == null) {
				String errMsg = "\n createRetur: message received with no timestamp information record";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}
			
			// Check the fulfillment status
			if (order.getFullfillmentStatus() != null) {
				if (order.getFullfillmentStatus() == VEN_FULFILLMENT_STATUS_ONE) {
					String errMsg = "\n createRetur: message received fulfillment status = VEN_FULFILLMENT_STATUS_ONE(1). Retur cannot be created with fulfillment status = 1";
					_log.error(errMsg);
					throw new EJBException(errMsg);

				}
			}

			// Status
			if (order.getStatus() == null) {
				String errMsg = "\n createRetur: message received with no status information record";
				_log.error(errMsg);
				throw new EJBException(errMsg);
			}

			//Shipping
			for(OrderItem item : order.getOrderItems()){
				if(item.getLogisticsInfo().getLogisticsProvider().getParty().getFullOrLegalName().equalsIgnoreCase("Select Shipping")){
					String errMsg = "\n createRetur: message received with wrong Logistic Info ("+item.getLogisticsInfo().getLogisticsProvider().getParty().getFullOrLegalName()+")";
					_log.error(errMsg);
					throw new EJBException(errMsg);
				}
			}

			/*
			 * Check that none of the order items already exist and remove any party
			 * record from merchant to prevent data problems from WCS
			 */
			List <String> merchantProduct =  new ArrayList<String>();
			
			for (OrderItem item : order.getOrderItems()) {
				
				// Remove party from merchant
				if (item.getProduct().getMerchant().getParty() != null) {
					String merchantSKU = item.getProduct().getMerchant().getMerchantId().getCode()+"&"+(item.getProduct().getMerchant().getParty().getFullOrLegalName()!=null?item.getProduct().getMerchant().getParty().getFullOrLegalName():"");
					merchantProduct.add(merchantSKU);
					item.getProduct().getMerchant().setParty(null);
				}
			}

			// Do the main processing inside this try/catch block
			try {

				_genericLocator = new Locator<Object>();
		
				VenOrder venOrder = new VenOrder();
				_log.debug("Mapping the order object to the venOrder object...");
				_mapper.map(order, venOrder);
							
				/**
				 * Party for merchant
				 */
				
				List<VenOrderItem> orderItems = venOrder.getVenOrderItems();
				VenPartySessionEJBLocal venPartyHome = (VenPartySessionEJBLocal) this._genericLocator.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");
				VenMerchantSessionEJBLocal venMerchantHome = (VenMerchantSessionEJBLocal) this._genericLocator.lookupLocal(VenMerchantSessionEJBLocal.class, "VenMerchantSessionEJBBeanLocal");
			
				// Set the defaults for all of the boolean values in venOrder
				if (venOrder.getBlockedFlag() == null) {
					venOrder.setBlockedFlag(false);
				}
				if (venOrder.getRmaFlag() == null) {
					venOrder.setRmaFlag(false);
				}
				// Default the finance reconcile flag to false.
				venOrder.setFinanceReconcileFlag(false);

				// If the order amount is missing then set it to default to 0
				if (venOrder.getAmount() == null) {
					venOrder.setAmount(new BigDecimal(0));
				}			
				
				_log.debug("\n persist Retur");
				VenRetur venRetur = new VenRetur();
				venRetur = this.persistRetur(venOrder);
				_log.debug("\n done persist Retur");
				
					for(String party : merchantProduct){				
						String[] temp = party.split("&");
						_log.debug("show venParty in orderItem :  "+party);
						_log.debug("string merchant :  "+temp[0]+" and "+temp[1]);
						if(!temp[1].equals("")){
						for(int h =0; h< orderItems.size();h++){
							if(orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId().equals(temp[0])){
								List<VenMerchant> venMechantList = venMerchantHome.queryByRange("select o from VenMerchant o where o.wcsMerchantId ='" +temp[0]+ "'", 0, 1);
								if( venMechantList.size()>0 || venMechantList!=null ){
									if(venMechantList.get(0).getVenParty()==null){
										List<VenParty> vePartyList = venPartyHome.queryByRange("select o from VenParty o where o.fullOrLegalName ='" +temp[1]+ "'", 0, 1);
											if(vePartyList==null || vePartyList.isEmpty()){
												VenParty venPartyitem = new VenParty();
												VenPartyType venPartyType = new VenPartyType();
												// set party type id = 1 adalah merchant
												venPartyType.setPartyTypeId(new Long(1));
												venPartyitem.setVenPartyType(venPartyType);
												venPartyitem.setFullOrLegalName(temp[1]);	
												_log.debug("persist venParty :  "+venPartyitem.getFullOrLegalName());
												venPartyitem = venPartyHome.persistVenParty(venPartyitem);	
												venMechantList.get(0).setVenParty(venPartyitem);
												venMerchantHome.mergeVenMerchant(venMechantList.get(0));
												_log.debug(" add  new party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");			
											}else{
												venMechantList.get(0).setVenParty(vePartyList.get(0));
												venMerchantHome.mergeVenMerchant(venMechantList.get(0));
												_log.debug(" add  party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");
								
											}
										}
									}
							}
							
						}
						}else 	_log.debug(" party is null for venmerchant (Merchant Id :"+ temp[0] +" )");
							
					}
			
				

				_log.debug("\n done create Retur!");
				Long endTime = System.currentTimeMillis();
				Long duration = endTime - startTime;
				_log.debug("createRetur: persisted new venRetur.returId:"
						+ venRetur.getReturId() + " status:"
						+ venRetur.getVenReturStatus().getOrderStatusCode()
						+ " in:" + duration + "ms");

			} catch (Exception e) {
				String errMsg = "An Exception occured when persisting the Retur:";
				_log.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			} finally{
				try{
					if(_genericLocator!=null){
						_genericLocator.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			Long endTime = System.currentTimeMillis();
			Long duration = endTime - startTime;
			_log.debug("createRetur() completed in:" + duration + "ms");
			return Boolean.TRUE;
		}
		
		/**
		 * Persists the order using the session tier.
		 * 
		 * @return the persisted object
		 */
		private VenRetur persistRetur(VenOrder venOrder) {
			VenRetur venRetur =null;
			if (venOrder != null) {			
				try {
					VenReturSessionEJBLocal returHome = (VenReturSessionEJBLocal) this._genericLocator
					.lookupLocal(VenReturSessionEJBLocal.class, "VenReturSessionEJBBeanLocal");
					
					_log.debug("Persisting VenRetur... :" + venOrder.getWcsOrderId());				
					
					// Save the order items before persisting as it will be detached
					List<VenOrderItem> venOrderItemList = venOrder.getVenOrderItems();

					// Detach the order items prior to persisting the order.
					venOrder.setVenOrderItems(null);
					
					// Detach the order payment allocations
					// Note that these will be allocated at 100% of the order price later when processing payments
					venOrder.setVenOrderPaymentAllocations(null);

					// Detach the transaction fees list
					venOrder.setVenTransactionFees(null);
					// Detach the customer first then persist and re-attach
					VenCustomer customer = venOrder.getVenCustomer();
					venOrder.setVenCustomer(null);

					if(venOrder.getVenOrderBlockingSource().getBlockingSourceId()== null && venOrder.getVenOrderBlockingSource().getBlockingSourceDesc()==null)
						venOrder.setVenOrderBlockingSource(null);
					if(customer.getUserType()==null) customer.setUserType("G");								
										
					// Persist the customer
					venOrder.setVenCustomer(this.persistCustomer(customer));
					
					VenOrderStatus venReturStatus = new VenOrderStatus();
					venReturStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_B);
										
					List<VenRetur> venReturList = returHome.queryByRange("select o from VenRetur o where o.wcsReturId ='"+venOrder.getWcsOrderId()+"'", 0, 0);
					if(venReturList.isEmpty()){						
						
						VenReturAddressSessionEJBLocal returAddressHome = (VenReturAddressSessionEJBLocal) this._genericLocator
								.lookupLocal(VenReturAddressSessionEJBLocal.class, "VenReturAddressSessionEJBBeanLocal");
						
						VenReturContactDetailSessionEJBLocal returContactDetailHome = (VenReturContactDetailSessionEJBLocal) this._genericLocator
								.lookupLocal(VenReturContactDetailSessionEJBLocal.class, "VenReturContactDetailSessionEJBBeanLocal");
						
						VenAddress orderAddress = new VenAddress();
						orderAddress = this.persistAddress(venOrder.getVenCustomer().getVenParty().getVenPartyAddresses().get(0).getVenAddress());
						
								_log.debug("set retur from order object");
								venRetur = new VenRetur();
								venRetur.setAmount(venOrder.getAmount());
								venRetur.setBlockedFlag(venOrder.getBlockedFlag());
								venRetur.setWcsReturId(venOrder.getWcsOrderId());
								venRetur.setReturTimestamp(venOrder.getOrderTimestamp());
								venRetur.setVenReturStatus(venReturStatus);
								venRetur.setRmaFlag(venOrder.getRmaFlag());
								venRetur.setRmaAction(venOrder.getRmaAction());
								venRetur.setVenCustomer(venOrder.getVenCustomer());
								venRetur.setDeliveredDateTime(venOrder.getDeliveredDateTime());
								venRetur.setIpAddress(venOrder.getIpAddress());
								venRetur.setFulfillmentStatus(venOrder.getFulfillmentStatus());
								venRetur.setFinanceReconcileFlag(venOrder.getFinanceReconcileFlag());
								venRetur.setFraudCheckStatusId(null);
								venRetur.setBlockingSourceId(null);
								venRetur.setBlockedTimestamp(venOrder.getBlockedTimestamp());
								venRetur.setBlockedReason(venOrder.getBlockedReason());
								venRetur.setReturDate(venOrder.getOrderDate());					
								
								venRetur=returHome.persistVenRetur(venRetur);
												
							//add order status history
							this.createReturStatusHistory(venRetur, venRetur.getVenReturStatus());				
							_log.debug("\n venRetur.wcsOrderId: "+venRetur.getWcsReturId());
							
							/*
							 * Tally Retur with customer address and contact details
							 * defined in the ref tables VenReturAddress and VenReturContactDetail
							 */				
							if(orderAddress!=null){
								VenReturAddress venReturAddress = new VenReturAddress();
								venReturAddress.setVenRetur(venRetur);
								venReturAddress.setVenAddress(orderAddress);
									
								_log.debug("Persist VenReturAddress");
								// persist VenOrderAddress
								returAddressHome.persistVenReturAddress(venReturAddress);
							}else{
								_log.debug("customer address is null");
							}
							
							List<VenReturContactDetail> venReturContactDetailList = new ArrayList<VenReturContactDetail>();
							
							List<VenContactDetail> venContactDetailList = venOrder.getVenCustomer().getVenParty().getVenContactDetails();
							if(venContactDetailList != null){				
								for (VenContactDetail venContactDetail:venContactDetailList){
									VenReturContactDetail venReturContactDetail = new VenReturContactDetail();
									venReturContactDetail.setVenRetur(venRetur);
									venReturContactDetail.setVenContactDetail(venContactDetail);
									
									venReturContactDetailList.add(venReturContactDetail);
								}
								
								_log.debug("Total VenReturContactDetail to be persisted => " + venReturContactDetailList.size());
								// persist VenReturContactDetail
								returContactDetailHome.persistVenReturContactDetailList(venReturContactDetailList);
							}
					}else{
						venRetur=venReturList.get(0);
					}

					venRetur.setVenReturItems(this.persistReturItemList(venRetur, venOrderItemList));
					
				} catch (Exception e) {
					String errMsg = "An exception occured when persisting VenOrder:";
					_log.error(errMsg + e.getMessage());
					e.printStackTrace();
					throw new EJBException(errMsg);
				}
			}
			return venRetur;
		}	
		
		
		List<VenReturItem> persistReturItemList(VenRetur venRetur, List<VenOrderItem> venOrderItemList) {
			List<VenReturItem> newVenReturItemList = new ArrayList<VenReturItem>();
			if (venOrderItemList != null && !venOrderItemList.isEmpty()) {
				try {
					_log.debug("Persisting VenReturItem list...:" + venOrderItemList.size());
					Iterator<VenOrderItem> i = venOrderItemList.iterator();
					
					// Persist the object
					VenReturItemSessionEJBLocal itemHome = (VenReturItemSessionEJBLocal) this._genericLocator
							.lookupLocal(VenReturItemSessionEJBLocal.class, "VenReturItemSessionEJBBeanLocal");
					
					// Synchronize the references before persisting anything
					while(i.hasNext()){
						VenOrderItem venOrderItem = i.next();
						venOrderItem = this.synchronizeVenOrderItemReferenceData(venOrderItem);
					}
				
					//Main processing loop
					i = venOrderItemList.iterator();
					while (i.hasNext()) {
						VenOrderItem orderItem = i.next();
						//retur item bisa duplicate
//						List<VenReturItem> venReturItemList = itemHome.queryByRange("select o from VenReturItem o where o.wcsReturItemId='"+orderItem.getWcsOrderItemId()+"'", 0, 0);
//						if(venReturItemList.isEmpty()){
								VenReturItem returItem = new VenReturItem();
								returItem.setVenRetur(venRetur);
								returItem.setWcsReturItemId(orderItem.getWcsOrderItemId());
								
								VenOrderStatus venReturStatus = new VenOrderStatus();
								venReturStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_B);
								returItem.setVenReturStatus(venReturStatus);
								returItem.setVenMerchantProduct(orderItem.getVenMerchantProduct());
								returItem.setQuantity(orderItem.getQuantity());
								returItem.setPrice(orderItem.getPrice());
								returItem.setTotal(orderItem.getTotal());
								returItem.setShippingCost(orderItem.getShippingCost());
								returItem.setInsuranceCost(orderItem.getInsuranceCost());
								returItem.setPackageCount(orderItem.getPackageCount());
								returItem.setShippingWeight(orderItem.getShippingWeight());
								returItem.setLogisticsPricePerKg(orderItem.getLogisticsPricePerKg());
								returItem.setVenAddress(orderItem.getVenAddress());
								returItem.setGiftCardFlag(orderItem.getGiftCardFlag());
								returItem.setGiftWrapFlag(orderItem.getGiftWrapFlag());
								returItem.setGiftCardNote(orderItem.getGiftCardNote());
								returItem.setGiftWrapPrice(orderItem.getGiftWrapPrice());
								returItem.setVenRecipient(orderItem.getVenRecipient());
								returItem.setLogLogisticService(orderItem.getLogLogisticService());
								returItem.setSaltCode(orderItem.getSaltCode());
								returItem.setMerchantSettlementFlag(orderItem.getMerchantSettlementFlag());
								returItem.setSpecialHandlingInstructions(orderItem.getSpecialHandlingInstructions());
								returItem.setMinEstDate(orderItem.getMinEstDate());
								returItem.setMaxEstDate(orderItem.getMaxEstDate());
								returItem.setEtd(orderItem.getEtd());
								returItem.setDeliveryRecipientStatus(orderItem.getDeliveryRecipientStatus());
								returItem.setMerchantCourier(orderItem.getMerchantCourier());
								returItem.setMerchantInstallationDateStart(orderItem.getMerchantInstallationDateStart());
								returItem.setMerchantInstallationDateEnd(orderItem.getMerchantInstallationDateEnd());
								returItem.setMerchantDeliveredDateStart(orderItem.getMerchantDeliveredDateStart());
								returItem.setMerchantDeliveredDateEnd(orderItem.getMerchantDeliveredDateEnd());
								returItem.setMerchantInstallOfficer(orderItem.getMerchantInstallOfficer());
								returItem.setMerchantInstallMobile(orderItem.getMerchantInstallMobile());
								returItem.setMerchantInstallNote(orderItem.getMerchantInstallNote());
								returItem.setDeliveryRecipientName(orderItem.getDeliveryRecipientName());
								returItem.setDeliveryReceivedDate(orderItem.getDeliveryReceivedDate());
								returItem.setLogisticDiscountPercentage(orderItem.getLogisticDiscountPercentage());
								returItem.setLogisticDiscountAmount(orderItem.getLogisticDiscountAmount());
								returItem.setTrackingNumber(orderItem.getTrackingNumber());
								/**
								 * set VenRetur
								 */
								returItem.setVenRetur(venRetur);
		
								// Persist the shipping address
								returItem.setVenAddress(this.persistAddress(orderItem.getVenAddress()));
		
								// Persist the recipient
								returItem.setVenRecipient(this.persistRecipient(orderItem.getVenRecipient()));
								
								// Adjust the shipping weight because it comes across as the
								// product shipping weight
								returItem.setShippingWeight(new BigDecimal(orderItem.getShippingWeight().doubleValue() * orderItem.getQuantity()));
		
								
								returItem = itemHome.persistVenReturItem(returItem);			
								
								/*
								 * Tally Order Item with recipient address and contact details
								 * defined in the ref tables VenOrderItemAddress and VenOrderItemContactDetail
								 */
								
								VenReturItemAddressSessionEJBLocal itemAddressHome = (VenReturItemAddressSessionEJBLocal) this._genericLocator
										.lookupLocal(VenReturItemAddressSessionEJBLocal.class, "VenReturItemAddressSessionEJBBeanLocal");
								
								VenReturItemContactDetailSessionEJBLocal itemContactDetailHome = (VenReturItemContactDetailSessionEJBLocal) this._genericLocator
										.lookupLocal(VenReturItemContactDetailSessionEJBLocal.class, "VenReturItemContactDetailSessionEJBBeanLocal");
								
								List<VenReturItemContactDetail> venReturItemContactDetailList = new ArrayList<VenReturItemContactDetail>();
								
								VenReturItemAddress venReturItemAddress = new VenReturItemAddress();
								
								venReturItemAddress.setVenReturItem(returItem);
								venReturItemAddress.setVenAddress(returItem.getVenAddress());

								_log.debug("persisting  VenReturItemAddress" );
								// persist VenReturItemAddress
								itemAddressHome.persistVenReturItemAddress(venReturItemAddress);
								
								List<VenContactDetail> venContactDetailList = returItem.getVenRecipient().getVenParty().getVenContactDetails();
								for (VenContactDetail venContactDetail:venContactDetailList){
									VenReturItemContactDetail venReturItemContactDetail = new VenReturItemContactDetail();
									venReturItemContactDetail.setVenReturItem(returItem);
									venReturItemContactDetail.setVenContactDetail(venContactDetail);
									
									venReturItemContactDetailList.add(venReturItemContactDetail);
								}
								
								_log.debug("Total VenReturItemContactDetail to be persisted => " + venReturItemContactDetailList.size());
								// persist VenReturContactDetail
								itemContactDetailHome.persistVenReturItemContactDetailList(venReturItemContactDetailList);		
												
								//add Retur item history
								this.createReturItemStatusHistory(returItem, returItem.getVenReturStatus());
								
								newVenReturItemList.add(returItem);
//						}else{
//							_log.debug("Retur Item Already Exist...:" + orderItem.getWcsOrderItemId());
//						}
					}
				} catch (Exception e) {
					String errMsg = "An exception occured when persisting VenReturItem:";
					_log.error(errMsg + e.getMessage());
					e.printStackTrace();				
					throw new EJBException(errMsg);
				}
			}
			return newVenReturItemList;
		}						
		
		private void setTransactionFee(VenOrder venOrder, VenOrderItem venOrderItem, Order order) throws Exception{
			_log.debug("[venInbound] set transaction fee");
			VenTransactionFeeSessionEJBLocal transactionFeeHome = (VenTransactionFeeSessionEJBLocal) this._genericLocator
			.lookupLocal(VenTransactionFeeSessionEJBLocal.class, "VenTransactionFeeSessionEJBBeanLocal");
			
			VenSettlementRecordSessionEJBLocal settlementRecordHome = (VenSettlementRecordSessionEJBLocal) this._genericLocator
			.lookupLocal(VenSettlementRecordSessionEJBLocal.class, "VenSettlementRecordSessionEJBBeanLocal");
						
			FinSalesRecordSessionEJBLocal salesRecordHome = (FinSalesRecordSessionEJBLocal) this._genericLocator
			.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");
			
			// Synchronize the reference data for each record
			Iterator<TransactionFee> transactionFeesIterator = order.getTransactionFees().iterator();
			while (transactionFeesIterator.hasNext()) {
				TransactionFee next = transactionFeesIterator.next();
				VenMerchant venMerchant = new VenMerchant();
				venMerchant.setWcsMerchantId(next.getMerchant().getMerchantId().getCode());
				venMerchant = this.retrieveExistingMerchant(venMerchant.getWcsMerchantId());

				VenTransactionFee venTransactionFee = this.retrieveExistingTransactionFee(venOrder, venMerchant);

				if (venTransactionFee != null) {
					/*
					 * If the transaction fee if a record for that merchant already exists then just update it with the new value. 
					 * This cannot be additive because of partial fulfillment. 
					 * When that happens there is no way to match the transaction fees on an order item basis.
					 * The fees must be deleted by the updateOrder message when the partial fulfillment is remedied
					 */
					_log.debug("[venInbound] transaction fee exist, cek transfee amount: "+next.getAmount());
					BigDecimal newTransFee = new BigDecimal(next.getAmount());
					
					if(newTransFee.compareTo(new BigDecimal(0))!=0){
						_log.debug("[venInbound] newTransFee is > 0, set and merge");						
						venTransactionFee.setFeeAmount(new BigDecimal(next.getAmount()));
						
						// merge the transaction fees
						transactionFeeHome.mergeVenTransactionFee(venTransactionFee);
					}
				} else {
					_log.debug("[venInbound] transaction fee not exist, set new transaction fee, amount: "+next.getAmount());
					venTransactionFee = new VenTransactionFee();
					VenTransactionFeePK id = new VenTransactionFeePK();
					id.setMerchantId(venMerchant.getMerchantId());
					id.setOrderId(venOrder.getOrderId());
					venTransactionFee.setId(id);

					venTransactionFee.setVenMerchant(venMerchant);
					venTransactionFee.setVenOrder(venOrder);
					venTransactionFee.setFeeAmount(new BigDecimal(next.getAmount()));
					
					// Persist the transaction fees
					transactionFeeHome.persistVenTransactionFee(venTransactionFee);
				}
			}
			
			_log.debug("check settlement record");
			List<VenSettlementRecord> settlementRecordList = settlementRecordHome
					.queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
			
			if (settlementRecordList != null) {
				VenSettlementRecord venSettlementRecord = settlementRecordList.get(0);
				
				/*
				 * Cek sales record exist, if yes update the transaction fee in fin_sales_record
				 */
				_log.debug("[venInbound] check sales record exist");
				List<FinSalesRecord> salesRecordList = retrieveExistingSalesRecord(venOrderItem);
				
				FinSalesRecord sales = new FinSalesRecord();
				sales = salesRecordList.get(0);
				
				if(salesRecordList!=null && salesRecordList.size()>0){		
					_log.debug("[venInbound] sales record exist, update transaction fee");
					if(venSettlementRecord.getCommissionType().equals("CM") || venSettlementRecord.getCommissionType().equals("RB")){
						_log.debug("[venInbound] merchant type is CM or RB: "+venSettlementRecord.getCommissionType());									
						
						_log.debug("[venInbound] check trans fee exist in sales record");						
						SalesRecordGenerator salesRecordGenerator = new SalesRecordGenerator();
						Boolean flagTransFeeExist = salesRecordGenerator.isTransFeeExist(this._genericLocator, venOrderItem);
						if(flagTransFeeExist==false){
							_log.debug("[venInbound] trans fee not exist in sales record, set transaction fee");
							BigDecimal gdnTransactionFeeAmountBeforeTax = new BigDecimal(0);
							gdnTransactionFeeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
							
							gdnTransactionFeeAmountBeforeTax = salesRecordGenerator.getMerchantTransactionFees(this._genericLocator, venOrderItem);
							sales.setGdnTransactionFeeAmount(gdnTransactionFeeAmountBeforeTax);
							
							BigDecimal gdnTransactionFeeAmountAfterTax = new BigDecimal(0);
							gdnTransactionFeeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
							
							if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
								BigDecimal gdnPPN_Divisor = new BigDecimal(VeniceConstants.VEN_GDN_PPN_RATE).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).add(new BigDecimal(1));
								gdnTransactionFeeAmountAfterTax = gdnTransactionFeeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
							}
							
							BigDecimal gdnTransactionFeeAmountPPN = gdnTransactionFeeAmountBeforeTax.subtract(gdnTransactionFeeAmountAfterTax);
							BigDecimal vatAmount = sales.getVatAmount().add(gdnTransactionFeeAmountPPN);
							
							_log.debug("[venInbound] update ppn amount in sales record");
							vatAmount.setScale(2, RoundingMode.HALF_UP);
							sales.setVatAmount(vatAmount);	
							
							salesRecordHome.mergeFinSalesRecord(sales);
						}else{
							_log.debug("[venInbound] trans fee exist, don't set transaction fee");
						}
					}else{
						_log.debug("[venInbound] merchant type is Not CM or RB: "+venSettlementRecord.getCommissionType());
					}
				}else{
					_log.debug("[venInbound] sales record not exist yet");
				}
			}else{
				_log.debug("[venInbound] settlement is null");
			}
		}
}
