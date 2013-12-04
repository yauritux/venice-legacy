package com.gdn.venice.inbound.receivers.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.djarum.raf.utilities.JPQLStringEscapeUtility;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.integration.jaxb.Payment;
import com.gdn.venice.constants.VenCSPaymentStatusIDConstants;
import com.gdn.venice.constants.VenOrderStatusConstants;
import com.gdn.venice.constants.VenPartyTypeConstants;
import com.gdn.venice.constants.VenVAPaymentStatusIDConstants;
import com.gdn.venice.constants.VenWCSPaymentTypeConstants;
import com.gdn.venice.constants.VeniceExceptionConstants;
import com.gdn.venice.dao.FinArFundsInReconRecordDAO;
import com.gdn.venice.dao.LogLogisticServiceDAO;
import com.gdn.venice.dao.LogLogisticsProviderDAO;
import com.gdn.venice.dao.VenAddressDAO;
import com.gdn.venice.dao.VenAddressTypeDAO;
import com.gdn.venice.dao.VenBankDAO;
import com.gdn.venice.dao.VenCityDAO;
import com.gdn.venice.dao.VenContactDetailDAO;
import com.gdn.venice.dao.VenContactDetailTypeDAO;
import com.gdn.venice.dao.VenCountryDAO;
import com.gdn.venice.dao.VenCustomerDAO;
import com.gdn.venice.dao.VenFraudCheckStatusDAO;
import com.gdn.venice.dao.VenMerchantDAO;
import com.gdn.venice.dao.VenMerchantProductDAO;
import com.gdn.venice.dao.VenOrderAddressDAO;
import com.gdn.venice.dao.VenOrderBlockingSourceDAO;
import com.gdn.venice.dao.VenOrderContactDetailDAO;
import com.gdn.venice.dao.VenOrderDAO;
import com.gdn.venice.dao.VenOrderItemAddressDAO;
import com.gdn.venice.dao.VenOrderItemAdjustmentDAO;
import com.gdn.venice.dao.VenOrderItemContactDetailDAO;
import com.gdn.venice.dao.VenOrderItemDAO;
import com.gdn.venice.dao.VenOrderItemStatusHistoryDAO;
import com.gdn.venice.dao.VenOrderPaymentAllocationDAO;
import com.gdn.venice.dao.VenOrderPaymentDAO;
import com.gdn.venice.dao.VenOrderStatusDAO;
import com.gdn.venice.dao.VenOrderStatusHistoryDAO;
import com.gdn.venice.dao.VenPartyAddressDAO;
import com.gdn.venice.dao.VenPartyDAO;
import com.gdn.venice.dao.VenPartyTypeDAO;
import com.gdn.venice.dao.VenPaymentStatusDAO;
import com.gdn.venice.dao.VenPaymentTypeDAO;
import com.gdn.venice.dao.VenProductCategoryDAO;
import com.gdn.venice.dao.VenProductTypeDAO;
import com.gdn.venice.dao.VenPromotionDAO;
import com.gdn.venice.dao.VenRecipientDAO;
import com.gdn.venice.dao.VenStateDAO;
import com.gdn.venice.dao.VenWcsPaymentTypeDAO;
import com.gdn.venice.exception.DuplicateWCSOrderIDException;
import com.gdn.venice.exception.InvalidOrderCSPaymentException;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.exception.InvalidOrderItemException;
import com.gdn.venice.exception.InvalidOrderLogisticInfoException;
import com.gdn.venice.exception.InvalidOrderVAPaymentException;
import com.gdn.venice.inbound.receivers.OrderReceiver;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
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
import com.gdn.venice.persistence.VenState;
import com.gdn.venice.persistence.VenWcsPaymentType;
import com.gdn.venice.util.VeniceConstants;

/**
 * 
 * @author yauritux
 *
 */
public class OrderReceiverImpl implements OrderReceiver {

	@Autowired
	VenAddressDAO venAddressDAO;
	@Autowired
	VenCountryDAO venCountryDAO;
	@Autowired
	VenPaymentStatusDAO venPaymentStatusDAO;
	@Autowired
	VenStateDAO venStateDAO;
	@Autowired	
	VenPaymentTypeDAO venPaymentTypeDAO;
	@Autowired
	VenPartyTypeDAO venPartyTypeDAO;
	@Autowired
	VenContactDetailTypeDAO venContactDetailTypeDAO;
	@Autowired
	VenOrderStatusDAO venOrderStatusDAO;
	@Autowired
	VenFraudCheckStatusDAO venFraudCheckStatusDAO;
	@Autowired
	LogLogisticServiceDAO logLogisticServiceDAO;
	@Autowired
	LogLogisticsProviderDAO logLogisticProviderDAO;
	@Autowired
	VenWcsPaymentTypeDAO venWcsPaymentTypeDAO;
	@Autowired
	VenProductCategoryDAO venProductCategoryDAO;
	@Autowired
	VenProductTypeDAO venProductTypeDAO;
	@Autowired
	VenBankDAO venBankDAO;
	@Autowired
	FinArFundsInReconRecordDAO finArFundsInReconRecordDAO;
	@Autowired
	VenOrderAddressDAO venOrderAddressDAO;
	@Autowired
	VenMerchantDAO venMerchantDAO;
	@Autowired
	VenOrderBlockingSourceDAO venOrderBlockingSourceDAO;
	@Autowired
	VenOrderPaymentAllocationDAO venOrderPaymentAllocationDAO;
	@Autowired
	VenPartyDAO venPartyDAO;
	@Autowired
	VenPromotionDAO venPromotionDAO;
	@Autowired
	VenOrderContactDetailDAO venOrderContactDetailDAO;
	@Autowired
	VenContactDetailDAO venContactDetailDAO;	
	@Autowired
	VenCustomerDAO venCustomerDAO;
	@Autowired
	VenAddressTypeDAO venAddressTypeDAO;
	@Autowired
	VenOrderDAO venOrderDAO;
	@Autowired	
	VenOrderItemDAO venOrderItemDAO;
	@Autowired
	VenMerchantProductDAO venMerchantProductDAO;
	@Autowired
	VenOrderItemAddressDAO venOrderItemAddressDAO;
	@Autowired
	VenOrderItemAdjustmentDAO venOrderItemAdjustmentDAO;
	@Autowired
	VenOrderItemContactDetailDAO venOrderItemContactDetailDAO;
	@Autowired
	VenOrderPaymentDAO venOrderPaymentDAO;
	@Autowired
	VenOrderStatusHistoryDAO venOrderStatusHistoryDAO;
	@Autowired
	VenOrderItemStatusHistoryDAO venOrderItemStatusHistoryDAO;
	@Autowired
	VenPartyAddressDAO venPartyAddressDAO;
	@Autowired
	VenRecipientDAO venRecipientDAO;
	@Autowired
	VenCityDAO venCityDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(OrderReceiverImpl.class);
	
	private Order order;
	
	private Mapper mapper;
	
	public OrderReceiverImpl(Order order) {
		super();
		this.order = order;
	}
	
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean createOrder() throws InvalidOrderException {	
		/*
		 * Check that none of the order items already exist and remove any party
		 * record from merchant to prevent data problems from WCS
		 */
		List <String> merchantProduct =  new ArrayList<String>();
		
		for (OrderItem item : order.getOrderItems()) {
			
			if (isItemWCSExistInDB(item.getItemId().getCode())) {
				String errMsg = "\n createOrder: message received with an order item that already exists in the database:" 
			                       + item.getItemId().getCode();
				throw new InvalidOrderItemException(errMsg, VeniceExceptionConstants.VEN_EX_000012);
			}
			// Remove party from merchant
			if (item.getProduct().getMerchant().getParty() != null) {
				String merchantSKU = item.getProduct().getMerchant().getMerchantId().getCode()+"&"+(item.getProduct().getMerchant().getParty().getFullOrLegalName()!=null?item.getProduct().getMerchant().getParty().getFullOrLegalName():"");
				merchantProduct.add(merchantSKU);
				item.getProduct().getMerchant().setParty(null);
			}
		}				
		
		LOG.debug("\n check va payment");
		Boolean vaPaymentExists = false;
		Boolean csPaymentExists = false;
		Iterator<Payment> i = order.getPayments().iterator();
		while (i.hasNext()) {
			Payment next = i.next();
			if (next.getPaymentType().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount.desc())) {
				//VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class,	"VenOrderPaymentSessionEJBBeanLocal");				
				//List<VenOrderPayment> venOrderPaymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='"+ next.getPaymentId().getCode()+ "'", 0, 1);
				List<VenOrderPayment> venOrderPaymentList = venOrderPaymentDAO.findByWcsPaymentId(next.getPaymentId().getCode());
				// Check that the VA payment exists else throw exception
				if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
					String errMsg = "\n createOrder: An order with a VA payment that does not exist has been received";
					LOG.error(errMsg);
					throw new InvalidOrderVAPaymentException(errMsg, VeniceExceptionConstants.VEN_EX_000013);
				}

				/*
				 * Check that the VA payment is approved... if not then
				 * throw an exception
				 */
				LOG.debug("\n check va payment approval");
				VenOrderPayment venOrderPayment = venOrderPaymentList.get(0);
				if (venOrderPayment.getVenPaymentStatus().getPaymentStatusId() != VenVAPaymentStatusIDConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVED.code()) {
					String errMsg = "\n createOrder: An order with an unapproved VA payment has been received";
					LOG.error(errMsg);
					throw new InvalidOrderVAPaymentException(errMsg, VeniceExceptionConstants.VEN_EX_000014);
				}
				vaPaymentExists = true;
			}
			
			if (next.getPaymentType().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_CSPayment.desc())) {
				//VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class,	"VenOrderPaymentSessionEJBBeanLocal");
				//List<VenOrderPayment> venOrderPaymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='"+ next.getPaymentId().getCode()+ "'", 0, 1);
				List<VenOrderPayment> venOrderPaymentList = venOrderPaymentDAO.findByWcsPaymentId(next.getPaymentId().getCode());				
				// Check that the CS payment exists else throw exception
				if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
					String errMsg = "\n createOrder: An order with a CS payment that does not exist has been received";
					LOG.error(errMsg);
					throw new InvalidOrderCSPaymentException(errMsg, VeniceExceptionConstants.VEN_EX_000015);
				}

				/*
				 * Check that the CS payment is approved... if not then
				 * throw an exception
				 */
				LOG.debug("\n check CS payment approval");
				VenOrderPayment venOrderPayment = venOrderPaymentList.get(0);
				if (!venOrderPayment.getVenPaymentStatus().getPaymentStatusId().equals(VenCSPaymentStatusIDConstants.VEN_CS_PAYMENT_STATUS_ID_APPROVED.code())) {
					String errMsg = "\n createOrder: An order with an unapproved CS payment has been received";
					LOG.error(errMsg);
					throw new InvalidOrderCSPaymentException(errMsg, VeniceExceptionConstants.VEN_EX_000016);
				}
				csPaymentExists = true;
			}
		}

		// Validate if order id already exist for non VA and non CS payments
		LOG.debug("\n check wcs order id exist 1");
		if ((order.getPayments() != null && !order.getPayments().isEmpty())) {
			if (!vaPaymentExists && !csPaymentExists) {
				if (this.isOrderExist(order.getOrderId().getCode())) {
					String errMsg = "\n createOrder: An order with this WCS orderId already exists: "+ order.getOrderId().getCode();
					LOG.error(errMsg);
					throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_000017);
				}
			}
		}

		/*
		 * This is really important. We need to get the order from the DB if
		 * it is VA because it will exist and we must update it with all of
		 * the details.
		 * 
		 * There MUST be NO changes to the VA payment data because it MUST
		 * be what was sent by Venice to WCS originally. Therefore if the
		 * payment information is included then we must not update it.
		 */
		VenOrder venOrder = new VenOrder();
		// If there is a VA payment then get the order from the cache
		if (vaPaymentExists || csPaymentExists) {
			LOG.debug("\n va payment exist, retrieve existing order");
			venOrder = retrieveExistingOrder(order.getOrderId().getCode());
			// If there is no existing VA status order then throw an exception
			if (venOrder == null) {
				String errMsg = "\n createOrder: message received for an order with VA payments where there is no existing VA status order:" + order.getOrderId().getCode();
				LOG.error(errMsg);
				throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_000013);
			}

			/*
			 * If the status of the existing order is not VA then it is a duplicate.
			 */
			LOG.debug("\n cek wcs order id exist 2");
			if (venOrder.getVenOrderStatus().getOrderStatusId() != VenOrderStatusConstants.VEN_ORDER_STATUS_VA.code() 
					&& venOrder.getVenOrderStatus().getOrderStatusId() != VenOrderStatusConstants.VEN_ORDER_STATUS_CS.code()) {
				String errMsg = "\n createOrder: message received with  the status of the existing order is not VA/CS (duplicate wcs order id):" + venOrder.getWcsOrderId();
				LOG.error(errMsg);
				throw new DuplicateWCSOrderIDException(errMsg, VeniceExceptionConstants.VEN_EX_000019);
			}
		} else {
			LOG.debug("\n cek wcs order id exist 3");
			if (this.isOrderExist(order.getOrderId().getCode())) {
				String errMsg = "\n createOrder: message received where an order with WCS orderId already exists:" + venOrder.getWcsOrderId();
				LOG.error(errMsg);
				throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_000017);
			}
		}
		LOG.debug("\n set status to C");
		//Set the status of the order explicitly to C (workaround for OpenJPA 2.0 problem)
		VenOrderStatus venOrderStatusC = new VenOrderStatus();
		venOrderStatusC.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_C);
		venOrderStatusC.setOrderStatusCode("C");
		venOrder.setVenOrderStatus(venOrderStatusC);

		/*
		 * Map the jaxb Order object to a JPA VenOrder object. This will be
		 * ok for both persist and merge because the PK is not touched and
		 * everything must be added anyway (VA payment will only have an
		 * orderId and timestamp).
		 */
		LOG.debug("Mapping the order object to the venOrder object...");
		mapper.map(order, venOrder);
					
		/**
		 * Party for merchant
		 */
		
		List<VenOrderItem> orderItems = venOrder.getVenOrderItems();
		/*
		VenPartySessionEJBLocal venPartyHome = (VenPartySessionEJBLocal) this._genericLocator.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");
		VenMerchantSessionEJBLocal venMerchantHome = (VenMerchantSessionEJBLocal) this._genericLocator.lookupLocal(VenMerchantSessionEJBLocal.class, "VenMerchantSessionEJBBeanLocal");
		*/
	
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
		
		/*
		 * This method call will persist the order if there has been no VA payment else it will merge
		 */
		LOG.debug("\n persist order");
		venOrder = this.persistOrder(vaPaymentExists, csPaymentExists, venOrder);
		LOG.debug("\n done persist order");
		
			for(String party : merchantProduct){				
				String[] temp = party.split("&");
				LOG.debug("show venParty in orderItem :  "+party);
				LOG.debug("string merchant :  "+temp[0]+" and "+temp[1]);
				if(!temp[1].equals("")){
				for(int h =0; h< orderItems.size();h++){
					if(orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId().equals(temp[0])){
						//List<VenMerchant> venMechantList = venMerchantHome.queryByRange("select o from VenMerchant o where o.wcsMerchantId ='" +temp[0]+ "'", 0, 1);
						List<VenMerchant> venMerchantList = venMerchantDAO.findByWcsMerchantId(temp[0]);
						if( venMerchantList.size()>0 || venMerchantList!=null ){
							if(venMerchantList.get(0).getVenParty()==null){
								//List<VenParty> vePartyList = venPartyHome.queryByRange("select o from VenParty o where o.fullOrLegalName ='" +temp[1]+ "'", 0, 1);
								List<VenParty> venPartyList = venPartyDAO.findByFullOrLegalName(temp[1]);
									if(venPartyList==null || venPartyList.isEmpty()){
										VenParty venPartyitem = new VenParty();
										VenPartyType venPartyType = new VenPartyType();
										// set party type id = 1 adalah merchant
										venPartyType.setPartyTypeId(new Long(1));
										venPartyitem.setVenPartyType(venPartyType);
										venPartyitem.setFullOrLegalName(temp[1]);	
										LOG.debug("persist venParty :  "+venPartyitem.getFullOrLegalName());
										//venPartyitem = venPartyHome.persistVenParty(venPartyitem);
										venPartyitem = venPartyDAO.saveAndFlush(venPartyitem);
										venMerchantList.get(0).setVenParty(venPartyitem);
										//venMerchantHome.mergeVenMerchant(venMechantList.get(0));
										venMerchantDAO.saveAndFlush(venMerchantList.get(0));
										LOG.debug(" add  new party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");			
									}else{
										venMerchantList.get(0).setVenParty(venPartyList.get(0));
										//venMerchantHome.mergeVenMerchant(venMerchantList.get(0));
										venMerchantDAO.saveAndFlush(venMerchantList.get(0));
										LOG.debug(" add  party for venmerchant (Merchant Id :"+ orderItems.get(h).getVenMerchantProduct().getVenMerchant().getWcsMerchantId() +" )");
						
									}
								}
							}
					}
					
				}
				}else 	LOG.debug(" party is null for venmerchant (Merchant Id :"+ temp[0] +" )");
					
			}
	
		// If the order is RMA do nothing with payments because there are none
		if (!venOrder.getRmaFlag()) {
			LOG.debug("\n masuk rma flag false, remove payment existing");
			// Remove any existing order payment allocations that were allocated at VA stage
			this.removeOrderPaymentAllocationList(venOrder);
			LOG.debug("\n done remove payment existing");
			// An array list of order payment allocations
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = new ArrayList<VenOrderPaymentAllocation>();
			List<VenOrderPayment> venOrderPaymentList = new ArrayList<VenOrderPayment>();

			/*
			 * Allocate the payments to the order.
			 */
			LOG.debug("\n Allocate the payments to the order");				
			if (order.getPayments() != null && !order.getPayments().isEmpty()) {
				Iterator<?> paymentIterator = order.getPayments().iterator();
				while (paymentIterator.hasNext()) {
					Payment next = (Payment) paymentIterator.next();
					/*
					 * Ignore partial fulfillment payments ... looks like a work around in WCS ... no need for this in Venice
					 */
					if (!next.getPaymentType().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_PartialFulfillment.desc())) {
						VenOrderPayment venOrderPayment = new VenOrderPayment();
						/*
						 * If the payment already exists then just fish it
						 * from the DB. This is the case for VA payments as
						 * they are received before the confirmed order.
						 */
						/*
						VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
						List<VenOrderPayment> venOrderPaymentList2 = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId ='" + next.getPaymentId().getCode() + "'", 0, 1);
						*/
						List<VenOrderPayment> venOrderPaymentList2 = venOrderPaymentDAO.findByWcsPaymentId(next.getPaymentId().getCode());
						
						if (venOrderPaymentList2 != null && !venOrderPaymentList2.isEmpty()) {
							venOrderPayment = venOrderPaymentList2.get(0);
						}
						// Map the payment with dozer
						LOG.debug("Mapping the VenOrderPayment object...");
						mapper.map(next, venOrderPayment);

						// Set the payment type based on the WCS payment type
						VenPaymentType venPaymentType = new VenPaymentType();
						LOG.debug("\n mapping payment type");
						if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_DebitMandiri.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						}else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYXPercentInstallment.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYZeroPercentInstallment.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MIGSCreditCard.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_CC);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_CC);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_VA);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_VA);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_CSPayment.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_CS);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_CS);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MIGSBCAInstallment.desc())){
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_CC);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_CC);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_CIMBClicks.desc())) {
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						}  else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_XLTunai.desc())) {
							LOG.debug("payment type XLTunai, reference id: "+next.getReferenceId());
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
							venOrderPayment.setReferenceId(next.getReferenceId());
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MandiriInstallment.desc())){
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_CC);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_CC);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_BIIngkisan.desc())){
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_BRI.desc())){
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_IB);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
							venOrderPayment.setVenPaymentType(venPaymentType);
						} else if (venOrderPayment.getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VenWCSPaymentTypeConstants.VEN_WCS_PAYMENT_TYPE_MandiriDebit.desc())){
							venPaymentType.setPaymentTypeCode(VeniceConstants.VEN_PAYMENT_TYPE_CC);
							venPaymentType.setPaymentTypeId(VeniceConstants.VEN_PAYMENT_TYPE_ID_CC);
							venOrderPayment.setVenPaymentType(venPaymentType);
						}
						venOrderPaymentList.add(venOrderPayment);
					}
				}					
				
				LOG.debug("\n persist payment");
				venOrderPaymentList = this.persistOrderPaymentList(venOrderPaymentList);

				paymentIterator = venOrderPaymentList.iterator();
				BigDecimal paymentBalance = venOrder.getAmount();
				int p=0;
				while (paymentIterator.hasNext()) {
					VenOrderPayment next = (VenOrderPayment) paymentIterator.next();

					/*
					 * Only include the allocations for non-VA payments
					 * because VA payments are already in the DB
					 */
					LOG.debug("\n allocate payment");
					/*
					 * semua Payment di allocate, untuk payment VA dan non-VA.
					 */
					//if (!next.getVenPaymentType().getPaymentTypeCode().equals(VEN_PAYMENT_TYPE_VA)) {
						// Build the allocation list manually based on the payment
						VenOrderPaymentAllocation allocation = new VenOrderPaymentAllocation();
						allocation.setVenOrder(venOrder);
						BigDecimal paymentAmount = next.getAmount();
						
						LOG.debug("Order Amount = "+paymentBalance);
						LOG.debug("paymentBalance.compareTo(new BigDecimal(0)):  "+paymentBalance.compareTo(new BigDecimal(0)) );
						
						// If the balance is greater than zero
						if (paymentBalance.compareTo(new BigDecimal(0)) >= 0) {
							/*
							 * If the payment amount is greater than the
							 * balance then allocate the balance amount else
							 * allocate the payment amount.
							 */
							if (paymentBalance.compareTo(paymentAmount) < 0) {
								allocation.setAllocationAmount(paymentBalance);
								LOG.debug("Order Allocation Amount is paymentBalance = "+paymentBalance);
							} else {
								allocation.setAllocationAmount(paymentAmount);
								LOG.debug("Order Allocation Amount is paymentAmount = "+paymentAmount);
							}
							
							paymentBalance = paymentBalance.subtract(paymentAmount);
							allocation.setVenOrderPayment(next);

							// Need a primary key object...
							VenOrderPaymentAllocationPK venOrderPaymentAllocationPK = new VenOrderPaymentAllocationPK();
							venOrderPaymentAllocationPK.setOrderPaymentId(next.getOrderPaymentId());
							venOrderPaymentAllocationPK.setOrderId(venOrder.getOrderId());
							allocation.setId(venOrderPaymentAllocationPK);

							venOrderPaymentAllocationList.add(allocation);
							LOG.debug("venOrder Payment Allocation List size from looping ke-"+p+" = "+venOrderPaymentAllocationList.size());
							p++;
						}
					//}
				}
				LOG.debug("\n persist payment allocation");
				venOrderPaymentAllocationList = this.persistOrderPaymentAllocationList(venOrderPaymentAllocationList);
				venOrder.setVenOrderPaymentAllocations(venOrderPaymentAllocationList);
				
				/*
				 * Here we need to create a dummy reconciliation records
				 * for the non-VA payments so that they appear in the 
				 * reconciliation screen as unreconciled.
				 * Later these records will be updated when the funds in
				 * reports are processed 
				 */					
				/*
				FinArFundsInReconRecordSessionEJBLocal reconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) this._genericLocator
				.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");
				*/
				LOG.debug("\n create reconciliation record");
				for (VenOrderPayment payment : venOrderPaymentList) {
					/*
					 * Only insert reconciliation records for non-VA payments here
					 * because the VA records will have been inserted when a VA payment is received.
					 */
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
						LOG.debug("\n payment Amount  = "+payment.getAmount());
						LOG.debug("\n HandlingFee = "+payment.getHandlingFee());
						LOG.debug("\n setRemainingBalanceAmount = "+payment.getAmount().subtract(payment.getHandlingFee()));
						
						reconRecord.setRemainingBalanceAmount(payment.getAmount());
						reconRecord.setUserLogonName("System");
						finArFundsInReconRecordDAO.saveAndFlush(reconRecord);
						//reconRecordHome.persistFinArFundsInReconRecord(reconRecord);
					}
				}
			}			
		}

		/*
		LOG.debug("\n done create order!");
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		LOG.debug("createOrder: persisted new venOrder.orderId:"
				+ venOrder.getOrderId() + " status:"
				+ venOrder.getVenOrderStatus().getOrderStatusCode()
				+ " in:" + duration + "ms");
		*/		
		
		
		return true;
	}

	@Override
	public boolean updateOrder() {
		return true;
	}
	
	/**
	 * Synchronizes the data for the direct VenOrderPayment references
	 * 
	 * @param venOrderPayment
	 * @return the synchronized data object
	 */
	private VenOrderPayment synchronizeVenOrderPaymentReferenceData(
			VenOrderPayment venOrderPayment) throws InvalidOrderException {

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
	 * Persists a list of order payments using the session tier.
	 * 
	 * @param orderPayments
	 * @return the persisted object
	 */
	private List<VenOrderPayment> persistOrderPaymentList(List<VenOrderPayment> venOrderPaymentList) {
		List<VenOrderPayment> newVenOrderPaymentList = new ArrayList<VenOrderPayment>();
		if (venOrderPaymentList != null && !venOrderPaymentList.isEmpty()) {
			try {
				LOG.debug("Persisting VenOrderPayment list...:"+ venOrderPaymentList.size());
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

					/*
					VenOrderPaymentSessionEJBLocal paymentHome = (VenOrderPaymentSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderPaymentSessionEJBLocal.class, "VenOrderPaymentSessionEJBBeanLocal");
					*/
					
					/*
					 * Check to see if the payment is already in the cache and
					 * if it is then assume it is a VA payment and should not be
					 * changed because it was APPROVED by Venice
					 */
					//List<VenOrderPayment> paymentList = paymentHome.queryByRange("select o from VenOrderPayment o where o.wcsPaymentId = '" + next.getWcsPaymentId() + "'", 0, 1);
					List<VenOrderPayment> paymentList = venOrderPaymentDAO.findByWcsPaymentId(next.getWcsPaymentId());

					if (paymentList.isEmpty()) {
						LOG.debug("Payment not found so persisting it...");
						// Persist the object
						//newVenOrderPaymentList.add((VenOrderPayment) paymentHome.persistVenOrderPayment(next));
						newVenOrderPaymentList.add(venOrderPaymentDAO.saveAndFlush(next));
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
				LOG.error(errMsg + e.getMessage());
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
				LOG.debug("Persisting VenOrderPaymentAllocation list...:"+ venOrderPaymentAllocationList.size());
				Iterator<VenOrderPaymentAllocation> i = venOrderPaymentAllocationList.iterator();
				while (i.hasNext()) {
					VenOrderPaymentAllocation next = i.next();
					LOG.debug("value of paymentAllocation ......: order_id = "+ next.getVenOrder().getOrderId() +" and wcs_code_payment = "+next.getVenOrderPayment().getWcsPaymentId());
					// Persist the object
					/*
					VenOrderPaymentAllocationSessionEJBLocal paymentAllocationHome = (VenOrderPaymentAllocationSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class,"VenOrderPaymentAllocationSessionEJBBeanLocal");
					newVenOrderPaymentAllocationList.add((VenOrderPaymentAllocation) paymentAllocationHome.mergeVenOrderPaymentAllocation(next));
					*/
					newVenOrderPaymentAllocationList.add(venOrderPaymentAllocationDAO.saveAndFlush(next));
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderPaymentAllocation:";
				LOG.error(errMsg + e.getMessage());
				throw new EJBException(errMsg);
			}
		}else{
			LOG.debug("Persisting VenOrderPaymentAllocation list is null");
		}
		return newVenOrderPaymentAllocationList;
	}	
	
	/**
	 * Persists a list of order items using the session tier.
	 * 
	 * @param venOrderLineList
	 * @return the persisted object
	 */
	List<VenOrderItem> persistOrderItemList(VenOrder venOrder, List<VenOrderItem> venOrderItemList) throws InvalidOrderException {
		List<VenOrderItem> newVenOrderItemList = new ArrayList<VenOrderItem>();
		if (venOrderItemList != null && !venOrderItemList.isEmpty()) {
			try {
				LOG.debug("Persisting VenOrderItem list...:" + venOrderItemList.size());
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
					/*
					VenOrderItemSessionEJBLocal itemHome = (VenOrderItemSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
					*/
					//orderItem = itemHome.persistVenOrderItem(orderItem);
					orderItem = venOrderItemDAO.saveAndFlush(orderItem);

					/*
					 * Tally Order Item with recipient address and contact details
					 * defined in the ref tables VenOrderItemAddress and VenOrderItemContactDetail
					 */
					
					/*
					VenOrderItemAddressSessionEJBLocal itemAddressHome = (VenOrderItemAddressSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemAddressSessionEJBLocal.class, "VenOrderItemAddressSessionEJBBeanLocal");
					
					VenOrderItemContactDetailSessionEJBLocal itemContactDetailHome = (VenOrderItemContactDetailSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemContactDetailSessionEJBLocal.class, "VenOrderItemContactDetailSessionEJBBeanLocal");
					*/
					
					List<VenOrderItemContactDetail> venOrderItemContactDetailList = new ArrayList<VenOrderItemContactDetail>();
					
					VenOrderItemAddress venOrderItemAddress = new VenOrderItemAddress();
					
					venOrderItemAddress.setVenOrderItem(orderItem);
					venOrderItemAddress.setVenAddress(orderItem.getVenAddress());

					LOG.debug("persisting  VenOrderItemAddress" );
					// persist VenOrderItemAddress
					//itemAddressHome.persistVenOrderItemAddress(venOrderItemAddress);
					venOrderItemAddressDAO.saveAndFlush(venOrderItemAddress);
					
					List<VenContactDetail> venContactDetailList = orderItem.getVenRecipient().getVenParty().getVenContactDetails();
					for (VenContactDetail venContactDetail:venContactDetailList){
						VenOrderItemContactDetail venOrderItemContactDetail = new VenOrderItemContactDetail();
						venOrderItemContactDetail.setVenOrderItem(orderItem);
						venOrderItemContactDetail.setVenContactDetail(venContactDetail);
						
						venOrderItemContactDetailList.add(venOrderItemContactDetail);
					}
					
					LOG.debug("Total VenOrderItemContactDetail to be persisted => " + venOrderItemContactDetailList.size());
					// persist VenOrderContactDetail
					//itemContactDetailHome.persistVenOrderItemContactDetailList(venOrderItemContactDetailList);
					venOrderItemContactDetailDAO.save(venOrderItemContactDetailList);
					
					//add order item history
					createOrderItemStatusHistory(orderItem, venOrder.getVenOrderStatus());

					// Persist the marginPromo
					orderItem.setVenOrderItemAdjustments(this.persistOrderItemAdjustmentList(orderItem, venOrderItemAdjustmentList));
					
					newVenOrderItemList.add(orderItem);
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderItem:";
				LOG.error(errMsg + e.getMessage());
				throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_000021);
			}
		}
		return newVenOrderItemList;
	}	
	
	/**
	 * Removes all of the payment allocations for an order
	 * 
	 * @param venOrder
	 * @return true if the operation succeeds else false
	 */
	private Boolean removeOrderPaymentAllocationList(VenOrder venOrder) {
		//try {
			LOG.debug("Remove Order Payment Allocation List ...:order id= "+venOrder.getOrderId()+" and wcs Order Id= "+venOrder.getWcsOrderId());
			/*
			VenOrderPaymentAllocationSessionEJBLocal paymentAllocationHome = (VenOrderPaymentAllocationSessionEJBLocal) this._genericLocator
					.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class, "VenOrderPaymentAllocationSessionEJBBeanLocal");
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = paymentAllocationHome
					.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId =" + venOrder.getOrderId(), 0, 1000);					
			paymentAllocationHome
					.removeVenOrderPaymentAllocationList(venOrderPaymentAllocationList);
					*/
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = venOrderPaymentAllocationDAO.findByVenOrder(venOrder);
			venOrderPaymentAllocationDAO.deleteInBatch(venOrderPaymentAllocationList);
		/*
		} catch (Exception e) {
			String errMsg = "An exception occured when persisting VenOrderPaymentAllocation:";
			LOG.error(errMsg + e.getMessage());

			throw new EJBException(errMsg);
		}
		*/
		return Boolean.TRUE;
	}	
	
	/**
	 * Synchronizes the data for the direct VenRecipient references
	 * 
	 * @param venRecipient
	 * @return the synchronized data object
	 */
	private VenRecipient synchronizeVenRecipientReferenceData(VenRecipient venRecipient) throws InvalidOrderException {

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
	 * Persists a recipient using the session tier
	 * 
	 * @param venRecipient
	 * @return the persisted object
	 */
	VenRecipient persistRecipient(VenRecipient venRecipient) throws InvalidOrderException {
		if (venRecipient != null) {
			LOG.debug("Persisting VenRecipient... :" + venRecipient.getVenParty().getFullOrLegalName());

			// Persist the party
			VenPartyType venPartyType = new VenPartyType();

			// Set the party type to Recipient
			venPartyType.setPartyTypeId(new Long(VenPartyTypeConstants.VEN_PARTY_TYPE_RECIPIENT.code()));
			venRecipient.getVenParty().setVenPartyType(venPartyType);

			venRecipient.setVenParty(this.persistParty(venRecipient.getVenParty(), "Recipient"));
			// Synchronize the reference data
			venRecipient = this.synchronizeVenRecipientReferenceData(venRecipient);
			// Persist the object
			/*
				VenRecipientSessionEJBLocal recipientHome = (VenRecipientSessionEJBLocal) this._genericLocator
						.lookupLocal(VenRecipientSessionEJBLocal.class, "VenRecipientSessionEJBBeanLocal");
				venRecipient = (VenRecipient) recipientHome.persistVenRecipient(venRecipient);
			 */
			venRecipient = venRecipientDAO.saveAndFlush(venRecipient);
		}
		return venRecipient;
	}	
	
	/**
	 * Synchronizes the reference data for the direct VenOrderItemAdjustment
	 * references
	 * 
	 * @param venOrderItemAdjustment
	 * @return the synchronized data object
	 */
	VenOrderItemAdjustment synchronizeVenOrderItemAdjustmentReferenceData(VenOrderItemAdjustment venOrderItemAdjustment) throws InvalidOrderException {
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
	 * Persists a list of order item marginPromo
	 * 
	 * @param venOrderItemAdjustment
	 * @return the persisted object
	 */
	List<VenOrderItemAdjustment> persistOrderItemAdjustmentList(VenOrderItem venOrderItem, List<VenOrderItemAdjustment> venOrderItemAdjustmentList) 
	   throws InvalidOrderException {
		List<VenOrderItemAdjustment> newVenOrderItemAdjustmentList = new ArrayList<VenOrderItemAdjustment>();
		if (venOrderItemAdjustmentList != null && !venOrderItemAdjustmentList.isEmpty()) {
			try {
				LOG.debug("Persisting VenOrderItemAdjustment list...:" + venOrderItemAdjustmentList.size());
				Iterator<VenOrderItemAdjustment> i = venOrderItemAdjustmentList.iterator();
				while (i.hasNext()) {
					VenOrderItemAdjustment next = i.next();
					// Synchronize the references
					next = this.synchronizeVenOrderItemAdjustmentReferenceData(next);

					VenPromotion venPromotion = next.getVenPromotion();
					// Attach the order item
					next.setVenOrderItem(venOrderItem);
					// Attach a primary key
					VenOrderItemAdjustmentPK id = new VenOrderItemAdjustmentPK();
					id.setOrderItemId(venOrderItem.getOrderItemId());					
					//id.setPromotionId(next.getVenPromotion().getPromotionId());
					id.setPromotionId(venPromotion.getPromotionId());
					LOG.debug("id.getOrderItemId: "+id.getOrderItemId());
					LOG.debug("id.getPromotionId: "+id.getPromotionId());
					next.setId(id);
					// Persist the object
					/*
					VenOrderItemAdjustmentSessionEJBLocal adjustmentHome = (VenOrderItemAdjustmentSessionEJBLocal) this._genericLocator
							.lookupLocal(VenOrderItemAdjustmentSessionEJBLocal.class, "VenOrderItemAdjustmentSessionEJBBeanLocal");
					*/
					
					//List<VenOrderItemAdjustment> existingVenOrderItemAdjustments = adjustmentHome.queryByRange("select o from VenOrderItemAdjustment o where o.venPromotion.promotionId = " + id.getPromotionId() + " and o.venOrderItem.orderItemId = " + id.getOrderItemId(), 0, 0);
					List<VenOrderItemAdjustment> existingVenOrderItemAdjustments = venOrderItemAdjustmentDAO.findByOrderItemAndPromotion(venOrderItem, venPromotion);
					LOG.debug("existingVenOrderItemAdjustments.size: "+existingVenOrderItemAdjustments.size());
					if(existingVenOrderItemAdjustments.size() == 0){
						if(next.getAdminDesc() == null || next.getAdminDesc().equals("")){
							next.setAdminDesc("-");
							LOG.info("Set adminDesc to (-) if null or empty");
						}
						//newVenOrderItemAdjustmentList.add((VenOrderItemAdjustment) adjustmentHome.persistVenOrderItemAdjustment(next));
						newVenOrderItemAdjustmentList.add((VenOrderItemAdjustment) venOrderItemAdjustmentDAO.saveAndFlush(next));
					}else{
						newVenOrderItemAdjustmentList.add((VenOrderItemAdjustment) next);
					}					
				}
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrderItemAdjustment:";
				LOG.error(errMsg + e.getMessage());
				throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_000022);
			}
		}
		return newVenOrderItemAdjustmentList;
	}	
	
	private VenOrder persistOrder(Boolean vaPaymentExists, Boolean csPaymentExists, VenOrder venOrder) throws InvalidOrderException {
		LOG.debug("vaPaymentExists: "+vaPaymentExists);
		LOG.debug("csPaymentExists: "+csPaymentExists);
		if (venOrder != null) {
			//try {
				LOG.debug("Persisting VenOrder... :" + venOrder.getWcsOrderId());				
				
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
				
				/*
				VenOrderSessionEJBLocal orderHome = (VenOrderSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
				
				VenOrderAddressSessionEJBLocal orderAddressHome = (VenOrderAddressSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderAddressSessionEJBLocal.class, "VenOrderAddressSessionEJBBeanLocal");
				
				VenOrderContactDetailSessionEJBLocal orderContactDetailHome = (VenOrderContactDetailSessionEJBLocal) this._genericLocator
						.lookupLocal(VenOrderContactDetailSessionEJBLocal.class, "VenOrderContactDetailSessionEJBBeanLocal");
				*/

				// If a VA payment exists then merge else persist the order
				if (vaPaymentExists || csPaymentExists) {
					LOG.debug("masuk merge di persist");
					//venOrder = (VenOrder) orderHome.mergeVenOrder(venOrder);
					venOrder = venOrderDAO.saveAndFlush(venOrder);
				} else {
					LOG.debug("masuk persist di persist");
					//venOrder = (VenOrder) orderHome.persistVenOrder(venOrder);
					venOrder = venOrderDAO.saveAndFlush(venOrder);
				}
				//add order status history
				this.createOrderStatusHistory(venOrder, venOrder.getVenOrderStatus());
				/*
				 * Persist the order items regardless of if it is VA or not
				 * because if there has been a VA payment then the items will
				 * not be in the cache anyway.
				 */
				LOG.debug("\n venOrder.wcsOrderId: "+venOrder.getWcsOrderId());
				venOrder.setVenOrderItems(this.persistOrderItemList(venOrder, venOrderItemList));
				
				/*
				 * Tally Order with customer address and contact details
				 * defined in the ref tables VenOrderAddress and VenOrderContactDetail
				 */				
				if(orderAddress!=null){
					VenOrderAddress venOrderAddress = new VenOrderAddress();
					venOrderAddress.setVenOrder(venOrder);
					venOrderAddress.setVenAddress(orderAddress);
						
					LOG.debug("Persist VenOrderAddress");
					// persist VenOrderAddress
					//orderAddressHome.persistVenOrderAddress(venOrderAddress);
					venOrderAddressDAO.saveAndFlush(venOrderAddress);
				}else{
					LOG.debug("customer address is null");
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
					
					LOG.debug("Total VenOrderContactDetail to be persisted => " + venOrderContactDetailList.size());
					// persist VenOrderContactDetail
					//orderContactDetailHome.persistVenOrderContactDetailList(venOrderContactDetailList);
					venOrderContactDetailDAO.save(venOrderContactDetailList);
				}
			/*
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenOrder:";
				LOG.error(errMsg + e.getMessage());
				throw new EJBException(errMsg);
			}
			*/
		}
		return venOrder;
	}	
	
	private Boolean createOrderStatusHistory(VenOrder venOrder, VenOrderStatus venOrderStatus){
		try {
			LOG.debug("add order status history");
			LOG.debug("\n wcs order id: "+venOrder.getWcsOrderId());
			LOG.debug("\n order status: "+venOrder.getVenOrderStatus().getOrderStatusCode());
			/*VenOrderStatusHistorySessionEJBLocal orderStatusHistoryHome = (VenOrderStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderStatusHistorySessionEJBLocal.class, "VenOrderStatusHistorySessionEJBBeanLocal");*/
			
			VenOrderStatusHistory venOrderStatusHistory = new VenOrderStatusHistory();
			
			VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
			venOrderStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venOrderStatusHistoryPK.setOrderId(venOrder.getOrderId());
			
			venOrderStatusHistory.setId(venOrderStatusHistoryPK);
			venOrderStatusHistory.setVenOrder(venOrder);
			venOrderStatusHistory.setStatusChangeReason("Updated by System");
			venOrderStatusHistory.setVenOrderStatus(venOrderStatus);
			
			//venOrderStatusHistory = orderStatusHistoryHome.persistVenOrderStatusHistory(venOrderStatusHistory);
			venOrderStatusHistory = venOrderStatusHistoryDAO.saveAndFlush(venOrderStatusHistory);
			LOG.debug("done add order status history");
			if(venOrderStatusHistory != null){
				return true;
			}
			return false;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating order status history:";
			LOG.error(errMsg + e.getMessage());
			throw new EJBException(errMsg + e.getMessage());
		}
	}	
	
	/**
	 * Synchronizes the reference data for the direct VenOrder references
	 * 
	 * @param venOrder
	 * @return the synchronized data object
	 */
	private VenOrder synchronizeVenOrderReferenceData(VenOrder venOrder) throws InvalidOrderException {
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
	
	private VenCustomer persistCustomer(VenCustomer venCustomer) throws InvalidOrderException {
		if (venCustomer != null) {
			try {
				LOG.debug("Persisting VenCustomer... :" + venCustomer.getCustomerUserName());
				/*
				VenCustomerSessionEJBLocal customerHome = (VenCustomerSessionEJBLocal) this._genericLocator
						.lookupLocal(VenCustomerSessionEJBLocal.class, "VenCustomerSessionEJBBeanLocal");
				*/
				// If the customer already exists then return it, else persist everything
				//List<VenCustomer> venCustomerList = customerHome.queryByRange( "select o from VenCustomer o where o.wcsCustomerId = '" + venCustomer.getWcsCustomerId() + "'", 0, 0);
				List<VenCustomer> venCustomerList = venCustomerDAO.findByWcsCustomerId(venCustomer.getWcsCustomerId());
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
				//venCustomer = customerHome.mergeVenCustomer(venCustomer);
				venCustomer = venCustomerDAO.saveAndFlush(venCustomer);
				venCustomer.setVenParty(customer.getVenParty());
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenCustomer:";
				LOG.error(errMsg + e.getMessage());
				throw new InvalidOrderException(errMsg, VeniceExceptionConstants.VEN_EX_100001);
			}
		}
		return venCustomer;
	}	
	
	/**
	 * Create a history record for the new status for an order item 
	 * received as an updateOrderItemStatus message
	 * @param venOrderItem
	 * @param orderStatusId
	 * @return
	 */
	private Boolean createOrderItemStatusHistory(VenOrderItem venOrderItem, VenOrderStatus venOrderStatus){
		//try {
			LOG.debug("add order item status history");
			LOG.debug("\n wcs order item id: "+venOrderItem.getWcsOrderItemId());
			LOG.debug("\n order item status: "+venOrderItem.getVenOrderStatus().getOrderStatusCode());
			/*VenOrderItemStatusHistorySessionEJBLocal orderItemStatusHistoryHome = (VenOrderItemStatusHistorySessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderItemStatusHistorySessionEJBLocal.class, "VenOrderItemStatusHistorySessionEJBBeanLocal");*/
			
			VenOrderItemStatusHistory venOrderItemStatusHistory = new VenOrderItemStatusHistory();
			
			VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
			venOrderItemStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
			venOrderItemStatusHistoryPK.setOrderItemId(venOrderItem.getOrderItemId());
			
			venOrderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
			venOrderItemStatusHistory.setVenOrderItem(venOrderItem);
			venOrderItemStatusHistory.setStatusChangeReason("Updated by System");
			venOrderItemStatusHistory.setVenOrderStatus(venOrderStatus);
			
			//venOrderItemStatusHistory = orderItemStatusHistoryHome.persistVenOrderItemStatusHistory(venOrderItemStatusHistory);
			venOrderItemStatusHistory = venOrderItemStatusHistoryDAO.saveAndFlush(venOrderItemStatusHistory);
			LOG.debug("done add order item status history");
			if(venOrderItemStatusHistory != null){
				return true;
			}
			return false;
		/*
		} catch (Exception e) {
			String errMsg = "An exception occured when creating order item status history:";
			LOG.error(errMsg + e.getMessage());
			throw new EJBException(errMsg + e.getMessage());
		}
		*/
	}	
	
	/**
	 * Synchronizes the reference data for the direct VenOrderItem references
	 * 
	 * @param venOrderItem
	 * @return the synchronized data object
	 */
	private VenOrderItem synchronizeVenOrderItemReferenceData(
			VenOrderItem venOrderItem) throws InvalidOrderException {

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
	 * Synchronizes the data for the direct VenCustomer references
	 * 
	 * @param venCustomer
	 * @return the synchronized data object
	 */
	private VenCustomer synchronizeVenCustomerReferenceData(
			VenCustomer venCustomer) throws InvalidOrderException {

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
	 * Persists a list of party addresses using the session tier.
	 * 
	 * @param venPartyAddressList
	 * @return the persisted object
	 */
	List<VenPartyAddress> persistPartyAddresses(List<VenPartyAddress> venPartyAddressList) {
		List<VenPartyAddress> newVenPartyAddressList = new ArrayList<VenPartyAddress>();
		if (venPartyAddressList != null && !venPartyAddressList.isEmpty()) {
			//try {
				LOG.debug("Persisting VenPartyAddress list...:" + venPartyAddressList.size());
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
					/*VenPartyAddressSessionEJBLocal addressHome = (VenPartyAddressSessionEJBLocal) this._genericLocator
							.lookupLocal(VenPartyAddressSessionEJBLocal.class, "VenPartyAddressSessionEJBBeanLocal"); */
					//newVenPartyAddressList.add((VenPartyAddress) addressHome.persistVenPartyAddress(next));
					newVenPartyAddressList.add(venPartyAddressDAO.saveAndFlush(next));
				}
				/*
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenPartyAddress:";
				LOG.error(errMsg + e.getMessage());
				throw new EJBException(errMsg);
			}
			*/
		}
		return newVenPartyAddressList;
	}	
	
	VenParty persistParty(VenParty venParty, String type) throws InvalidOrderException {
		if (venParty != null) {
			//try {
				VenParty existingParty;
				if(type.equals("Customer")){
					LOG.debug("Persisting VenParty... :" + venParty.getVenCustomers().get(0).getCustomerUserName());
	
					// Get any existing party based on customer username
					existingParty = this.retrieveExistingParty(venParty.getVenCustomers().get(0).getCustomerUserName());
				} else {
					LOG.debug("Persisting VenParty... :" + venParty.getFullOrLegalName());

					// Get any existing party based on full or legal name 
					existingParty = this.retrieveExistingParty(venParty.getFullOrLegalName());
				}
			
				/*
				 * If the party already exists then the existing party 
				 * addresses and contacts may have changed so we
				 * need to synchronize them
				 */								
				if (existingParty != null) {
					LOG.debug("existing Party not null" );
					
					//Get the existing addresses
					List<VenAddress> existingAddressList = new ArrayList<VenAddress>();
					for(VenPartyAddress venPartyAddress:existingParty.getVenPartyAddresses()){
						LOG.debug("ven Party Address... :"+venPartyAddress.getVenAddress().getAddressId());
						existingAddressList.add(venPartyAddress.getVenAddress());
					}
					
					//Get the new addresses
					List<VenAddress> newAddressList = new ArrayList<VenAddress>();
					if(venParty.getVenPartyAddresses()!=null){
						for(VenPartyAddress venPartyAddress:venParty.getVenPartyAddresses()){
							LOG.debug("New ven Party Address... :"+venPartyAddress.getVenAddress().getStreetAddress1());
							newAddressList.add(venPartyAddress.getVenAddress());
						}
					}

					/*
					 * If any new addresses are provided then check that 
					 * the existing addresses match the new addresses
					 * else add the new addresses for the party 
					 */
					
					
					if(!newAddressList.isEmpty()){
						LOG.debug("New ven Party Address is not empty and Update Address List");
						List<VenAddress> updatedAddressList = this.updateAddressList(existingAddressList, newAddressList);
						LOG.debug("updatedAddressList size => " + updatedAddressList.size());
						List<VenAddress> tempAddressList = new ArrayList<VenAddress>();
						
						tempAddressList.addAll(updatedAddressList);
						
						LOG.debug("Remove old VenAddress");
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
						
						LOG.debug("persist Party Addresses ");
						//Persist the new VenPartyAddress records
						venPartyAddressList = this.persistPartyAddresses(venPartyAddressList);						
						
						if(updatedAddressList.size() == 0){
							for(VenAddress updatedAddress:tempAddressList){
								venPartyAddressList.addAll(updatedAddress.getVenPartyAddresses());	
								for(VenPartyAddress venPartyAddress:venPartyAddressList){
									LOG.debug("VenPartyAddress => " + venPartyAddress.getVenAddress().getAddressId());
								}
							}
						}
						
						//copy existing address list to new list so it can be added new address list
						LOG.debug("copy address list to new list");
//						List<VenPartyAddress> venPartyAddressList2=new ArrayList<VenPartyAddress>(existingParty.getVenPartyAddresses()).subList(0,venPartyAddressList.size());
						
						//Add all the new party address records	
						LOG.debug("add All VenParty Addresses");
						existingParty.setVenPartyAddresses(venPartyAddressList);
						for(VenPartyAddress venPartyAddress:venPartyAddressList){
							LOG.debug("VenPartyAddress => " + venPartyAddress.getVenAddress().getAddressId());
						}
						LOG.debug("done add All VenParty Addresses");
					}
					
					/*
					 * If any new contact details are provided then check
					 * that the new contact details match the existing 
					 * contact details else add the new contact details
					 * to the party and then merge.
					 */
					
					LOG.debug("Get old and new party ven contact Detail  ");
					//Get the existing contact details
					List<VenContactDetail> existingContactDetailList = existingParty.getVenContactDetails();
					
					if(venParty.getVenContactDetails()!=null){
							//Get the new addresses
							List<VenContactDetail> newContactDetailList = venParty.getVenContactDetails();
							
							if(!newContactDetailList.isEmpty()){
								LOG.debug("updatedContact Detail List from existingContactDetailList to newContactDetailList");
								LOG.debug("start updating contact detail");
								//if the contact detail of existing party is null we can not get the party id using existingContactDetailList, so send the existing party
								List<VenContactDetail> updatedContactDetailList = this.updateContactDetailList(existingParty, existingContactDetailList, newContactDetailList);
								LOG.debug("done updating contact detail!!!");
		
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
				LOG.debug("persist Address List");
				addressList = this.persistAddressList(addressList);

				// Assign the address keys back to the n-n object
				i = venParty.getVenPartyAddresses().iterator();
				int index = 0;
				while (i.hasNext()) {
					VenPartyAddress next = i.next();
					next.setVenAddress(addressList.get(index));
					VenAddressType addressType = new VenAddressType();
					addressType.setAddressTypeId(VeniceConstants.VEN_ADDRESS_TYPE_DEFAULT);
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
				
				LOG.debug("synchronize VenParty Reference Data");
				// Synchronize the reference data
				venParty = this.synchronizeVenPartyReferenceData(venParty);
				
				// Persist the object
				//VenPartySessionEJBLocal partyHome = (VenPartySessionEJBLocal) this._genericLocator.lookupLocal(VenPartySessionEJBLocal.class, "VenPartySessionEJBBeanLocal");

				LOG.debug("persist VenParty ");
				// Merge the party object
				//venParty = (VenParty) partyHome.persistVenParty(venParty);
				venParty = venPartyDAO.saveAndFlush(venParty);

				VenAddressType venAddressType = new VenAddressType();
				venAddressType.setAddressTypeId(VeniceConstants.VEN_ADDRESS_TYPE_DEFAULT);

				// Set the party relationship for each VenPartyAddress
				i = venPartyAddressList.iterator();
				while (i.hasNext()) {
					VenPartyAddress next = i.next();
					next.setVenParty(venParty);
					next.setVenAddressType(venAddressType);
				}

				LOG.debug("persist Party Addresses ");
				// Persist the party addresses
				venParty.setVenPartyAddresses(this.persistPartyAddresses(venPartyAddressList));
				LOG.debug("Venpartyaddress size = >" + venParty.getVenPartyAddresses().size());
				// Set the party relationship for each contact detail
				Iterator<VenContactDetail> contactsIterator = venContactDetailList.iterator();
				while (contactsIterator.hasNext()) {
					contactsIterator.next().setVenParty(venParty);
				}

				LOG.debug("persist Contact Details");
				// Persist the contact details
				venParty.setVenContactDetails(this.persistContactDetails(venContactDetailList));
				LOG.debug("VenContactDetails size = >" + venParty.getVenContactDetails());
			/*
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenParty:";
				LOG.error(errMsg + e.getMessage());
				throw new EJBException(errMsg);
			}
			*/
		}
		return venParty;
	}	
	
	/**
	 * Synchronizes the data for the direct VenParty references
	 * 
	 * @param venParty
	 * @return the synchronized data object
	 */
	private VenParty synchronizeVenPartyReferenceData(VenParty venParty) throws InvalidOrderException {
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
	 * updateContactDetailList - compares the existing contact detail list with the 
	 * new contact detail list, writes any new contact details to the database 
	 * and returns the updated contact detail list.
	 * @param existingVenContactDetailList
	 * @param newVenContactDetailList
	 * @return the updated contact detail list
	 */
	List<VenContactDetail> updateContactDetailList(VenParty existingParty
			, List<VenContactDetail> existingVenContactDetailList, List<VenContactDetail> newVenContactDetailList)
			throws InvalidOrderException {
		List<VenContactDetail> updatedVenContactDetailList = new ArrayList<VenContactDetail>();
		List<VenContactDetail> persistVenContactDetailList = new ArrayList<VenContactDetail>();
		LOG.debug("\n masuk method update contact detail");
		/*
		 * Iterate the list of existing contacts to determine if 
		 * the new contacts exist already
		 */
		for(VenContactDetail newVenContactDetail:newVenContactDetailList){
			Boolean bFound = false;
			if(!existingVenContactDetailList.isEmpty()){
				LOG.debug("\n existingVenContactDetailList not empty");
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
						LOG.debug("\n contact detail equal with existing, added to updated list");
						updatedVenContactDetailList.add(existingVenContactDetail);
						
						bFound = true;
						//Break from the inner loop as the contact is found
						break;
					}
				}
			}else{
				LOG.debug("\n existingVenContactDetailList is empty");
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
				LOG.debug("\n contact detail not equal with existing, persist it");
				newVenContactDetail.setVenParty(existingParty);
				if(!persistVenContactDetailList.contains(newVenContactDetail)){
					LOG.debug("\n added the new contact detail to list");
					persistVenContactDetailList.add(newVenContactDetail);
				}
			}
		}	
		
		/*
		 * Persist any contact details that are new
		 */
		if(!persistVenContactDetailList.isEmpty()){
			LOG.debug("\n new contact detail list not empty, start persist new contact detail");
			persistVenContactDetailList = this.persistContactDetails(persistVenContactDetailList);
			LOG.debug("\n done persist contact detail");
			//Add the persisted contact details to the new list
			updatedVenContactDetailList.addAll(persistVenContactDetailList);
		}
		LOG.debug("\n return updated contact detail list");
		return updatedVenContactDetailList;
	}	
	
	/**
	 * Synchronizes the data for the direct VenContactDetail references
	 * 
	 * @param venContactDetail
	 * @return the synchronized data object
	 */
	private VenContactDetail synchronizeVenContactDetailReferenceData(VenContactDetail venContactDetail) throws InvalidOrderException {
		List<Object> references = new ArrayList<Object>();
		references.add(venContactDetail.getVenContactDetailType());
		LOG.debug("\n start sync contact detail method");
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
	 * Persists a list of contact details using the session tier.
	 * 
	 * @param venContactDetails
	 * @return the persisted object
	 */
	List<VenContactDetail> persistContactDetails(List<VenContactDetail> venContactDetailList) throws InvalidOrderException {
		LOG.debug("\n start method persist contact detail");
		List<VenContactDetail> newVenContactDetailList = new ArrayList<VenContactDetail>();
		if (venContactDetailList != null && !venContactDetailList.isEmpty()) {
			//try {
				LOG.debug("Persisting VenContactDetail list...:" + venContactDetailList.size());
				Iterator<VenContactDetail> i = venContactDetailList.iterator();
				while (i.hasNext()) {
					VenContactDetail next = i.next();
					// Synchronize the references
					this.synchronizeVenContactDetailReferenceData(next);
					// Persist the object
					/*
					VenContactDetailSessionEJBLocal detailHome = (VenContactDetailSessionEJBLocal) this._genericLocator
							.lookupLocal(VenContactDetailSessionEJBLocal.class, "VenContactDetailSessionEJBBeanLocal");
					*/							
					LOG.debug("\n start persisting contact detail");
					//newVenContactDetailList.add((VenContactDetail) detailHome.persistVenContactDetail(next));
					newVenContactDetailList.add(venContactDetailDAO.saveAndFlush(next));
				}
			/*
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenContactDetail:";
				LOG.error(errMsg + e.getMessage());
				throw new EJBException(errMsg);
			}
			*/
		}
		return newVenContactDetailList;
	}	
	
	/**
	 * Persists an address using the session tier
	 * 
	 * @param venAddress
	 * @return the persisted object
	 * @throws InvalidOrderException 
	 */
	VenAddress persistAddress(VenAddress venAddress) throws InvalidOrderException {
		if (venAddress != null) {
			//try {
				LOG.debug("Persisting VenAddress... :" + venAddress.getStreetAddress1());
				// Synchronize the reference data
				venAddress = this.synchronizeVenAddressReferenceData(venAddress);
				// Persist the object
				/*
				VenAddressSessionEJBLocal addressHome = (VenAddressSessionEJBLocal) this._genericLocator
						.lookupLocal(VenAddressSessionEJBLocal.class, "VenAddressSessionEJBBeanLocal");
				*/

				if (venAddress.getAddressId() == null) {
					if(venAddress.getStreetAddress1()==null && venAddress.getKecamatan()==null && venAddress.getKelurahan()==null && venAddress.getVenCity()==null &&
							venAddress.getVenState()==null && venAddress.getPostalCode()==null && venAddress.getVenCountry()==null){
						LOG.info("Address is null, no need to persist address");
					}else{
						//detach city, state, dan country karena bisa null dari WCS	
						LOG.info("Address is not null, detach city, state, and country");
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
						
						//venAddress = (VenAddress) addressHome.persistVenAddress(venAddress);
						venAddress = venAddressDAO.saveAndFlush(venAddress);
						
						//attach lagi setelah persist
						venAddress.setVenCity(city);
						venAddress.setVenState(state);
						venAddress.setVenCountry(country);
						
						LOG.debug("persist address");
						//venAddress = (VenAddress) addressHome.mergeVenAddress(venAddress);
						venAddress = venAddressDAO.saveAndFlush(venAddress);
					}
				} else {
					LOG.debug("merge address");
					//venAddress = (VenAddress) addressHome.mergeVenAddress(venAddress);
					venAddress = venAddressDAO.saveAndFlush(venAddress);
				}

				/*
			} catch (Exception e) {
				String errMsg = "An exception occured when persisting VenAddress:";
				LOG.error(errMsg + e.getMessage());
				e.printStackTrace();
				throw new EJBException(errMsg);
			}
			*/
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
	private List<Object> synchronizeReferenceData(List<Object> references) throws InvalidOrderException {
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
						LOG.debug("Restricting VenOrder... :" + ((VenOrder) next).getWcsOrderId());
						List<VenOrder> orderList = venOrderDAO.findByWcsOrderId(((VenOrder) next).getWcsOrderId());
						if (orderList == null || orderList.isEmpty()) {
							throw new InvalidOrderException("Order does not exist", VeniceExceptionConstants.VEN_EX_000020);
						} else {
							retVal.add(orderList.get(0));
						}
					}
				}
				
				// Order payments need to be synchronized SPECIAL CASE
				// MANY-MANY)
				if (next instanceof VenOrderPayment) {
					if (((VenOrderPayment) next).getWcsPaymentId() != null) {
						LOG.debug("Synchronizing VenOrderPayment... :" + ((VenOrderPayment) next).getWcsPaymentId());
						retVal.add(this.synchronizeVenOrderPaymentReferenceData((VenOrderPayment) next));
					}
				}

				// Parties need to be synchronized
				if (next instanceof VenParty) {
					if (((VenParty) next).getFullOrLegalName() != null) {
						try {
							LOG.debug("Synchronizing VenParty reference data... ");
							next = this.synchronizeVenPartyReferenceData((VenParty) next);
							retVal.add(next);

						} catch (Exception e) {
							String errMsg = "An exception occured synchronizing VenParty reference data:";
							LOG.error(errMsg + e.getMessage());
							e.printStackTrace();
							throw new EJBException(errMsg);

						}
					}
				}
				// Banks need to be restricted to Venice values
				if (next instanceof VenBank) {
					if (((VenBank) next).getBankCode() != null) {
						//try {
							LOG.debug("Restricting VenBank... :" + ((VenBank) next).getBankCode());
							//VenBankSessionEJBLocal bankHome = (VenBankSessionEJBLocal) this._genericLocator .lookupLocal(VenBankSessionEJBLocal.class, "VenBankSessionEJBBeanLocal");
							//List<VenBank> bankList = bankHome.queryByRange("select o from VenBank o where o.bankCode ='" + ((VenBank) next).getBankCode() + "'", 0, 1);
							List<VenBank> bankList = venBankDAO.findByBankCode(((VenBank) next).getBankCode());
							if (bankList == null || bankList.isEmpty()) {
								throw new InvalidOrderException("Bank does not exist", VeniceExceptionConstants.VEN_EX_200001);
							} else {
								retVal.add(bankList.get(0));
							}
						/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenBankSessionEJBBean:" + e.getMessage());
							//e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenBankSessionEJBBean:");
						}
						*/
					}
				}
				// Customers need to be synchronized
				if (next instanceof VenCustomer) {
					if (((VenCustomer) next).getWcsCustomerId() != null) {
						//try {
							LOG.debug("Synchronizing VenCustomer... :" + ((VenCustomer) next).getWcsCustomerId());
							// Synchronize the reference data
							next = this.synchronizeVenCustomerReferenceData((VenCustomer) next);
							// Synchronize the object
							//VenCustomerSessionEJBLocal customerHome = (VenCustomerSessionEJBLocal) this._genericLocator.lookupLocal(VenCustomerSessionEJBLocal.class, "VenCustomerSessionEJBBeanLocal");
							//VenCustomer customer = (VenCustomer) customerHome.persistVenCustomer((VenCustomer) next);
							VenCustomer customer = venCustomerDAO.saveAndFlush((VenCustomer) next);
							retVal.add(customer);

							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenCustomerSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenCustomerSessionEJBBean:");
						}
						*/
					}
				}
				// Logistics provider must be restricted to Venice values
				if (next instanceof LogLogisticsProvider) {
					if (((LogLogisticsProvider) next).getLogisticsProviderCode() != null) {
						//try {
							LOG.debug("Restricting LogLogisticsProvider... :" + ((LogLogisticsProvider) next).getLogisticsProviderCode());
							/*LogLogisticsProviderSessionEJBLocal logisticsProviderHome = (LogLogisticsProviderSessionEJBLocal) this._genericLocator
									.lookupLocal(LogLogisticsProviderSessionEJBLocal.class, "LogLogisticsProviderSessionEJBBeanLocal");*/
							/*
							List<LogLogisticsProvider> logisticsProviderList = logisticsProviderHome
									.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderCode ='" + ((LogLogisticsProvider) next).getLogisticsProviderCode() + "'", 0, 1);
							*/
							List<LogLogisticsProvider> logisticsProviderList = logLogisticProviderDAO.findByLogisticsProviderCode(((LogLogisticsProvider) next).getLogisticsProviderCode());
							if (logisticsProviderList == null || logisticsProviderList.isEmpty()) {
								throw new InvalidOrderLogisticInfoException("Logistics provider does not exist", VeniceExceptionConstants.VEN_EX_000011);
							} else {
								retVal.add(logisticsProviderList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up LogLogisticsProviderSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up LogLogisticsProviderSessionEJBBean:");
						}
						*/
					}
				}
				// Merchant products need to be synchronized
				if (next instanceof VenMerchantProduct) {
					if (((VenMerchantProduct) next).getWcsProductSku() != null) {
						//try {
							LOG.debug("Synchronizing VenMerchantProduct... :" + ((VenMerchantProduct) next)	.getWcsProductSku());
							next = this.synchronizeVenMerchantProductReferenceData((VenMerchantProduct) next);
							/*VenMerchantProductSessionEJBLocal merchantProductHome = (VenMerchantProductSessionEJBLocal) this._genericLocator
									.lookupLocal(VenMerchantProductSessionEJBLocal.class, "VenMerchantProductSessionEJBBeanLocal");
							List<VenMerchantProduct> merchantProductList = merchantProductHome
									.queryByRange("select o from VenMerchantProduct o where o.wcsProductSku ='" + ((VenMerchantProduct) next).getWcsProductSku() + "'", 0, 1);*/
							List<VenMerchantProduct> merchantProductList = venMerchantProductDAO.findByWcsProductSku(((VenMerchantProduct) next).getWcsProductSku());
							if (merchantProductList == null || merchantProductList.isEmpty()) {
								//VenMerchantProduct merchantProduct = this.persistMerchantProduct((VenMerchantProduct) next);
								VenMerchantProduct merchantProduct = venMerchantProductDAO.saveAndFlush((VenMerchantProduct) next);
								retVal.add(merchantProduct);

							} else {
								retVal.add(merchantProductList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenMerchantProductSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenMerchantProductSessionEJBBean:");
						}
						*/
					}
				}
				// Logistics service must be restricted to Venice values
				if (next instanceof LogLogisticService) {
					if (((LogLogisticService) next).getServiceCode() != null) {
						//try {
							LOG.debug("Restricting LogLogisticService... :" + ((LogLogisticService) next).getServiceCode());
							/*LogLogisticServiceSessionEJBLocal logisticServiceHome = (LogLogisticServiceSessionEJBLocal) this._genericLocator
									.lookupLocal(LogLogisticServiceSessionEJBLocal.class, "LogLogisticServiceSessionEJBBeanLocal");
							List<LogLogisticService> logisticServiceList = logisticServiceHome
									.queryByRange("select o from LogLogisticService o where o.serviceCode ='" + ((LogLogisticService) next).getServiceCode() + "'", 0, 1);*/
							List<LogLogisticService> logisticServiceList = logLogisticServiceDAO.findByServiceCode(((LogLogisticService) next).getServiceCode());
							if (logisticServiceList == null || logisticServiceList.isEmpty()) {
								throw new InvalidOrderException("Logistics service does not exist", VeniceExceptionConstants.VEN_EX_000011);
							} else {
								retVal.add(logisticServiceList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up LogLogisticServiceSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up LogLogisticServiceSessionEJBBean:");
						}
						*/
					}
				}
				// Party addresses need to be synchronized
				if (next instanceof VenPartyAddress) {
					if (((VenPartyAddress) next).getVenAddress() != null) {
						//try {
							LOG.debug("Synchronizing VenPartyAddress... :" + ((VenPartyAddress) next).getVenAddress().getStreetAddress1());
							// Synchronize the reference data
							next = this.synchronizeVenPartyAddressReferenceData((VenPartyAddress) next);
							// Synchronize the object
							/*
							VenPartyAddressSessionEJBLocal partyAddressHome = (VenPartyAddressSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPartyAddressSessionEJBLocal.class, "VenPartyAddressSessionEJBBeanLocal");
							VenPartyAddress partyAddress = (VenPartyAddress) partyAddressHome.persistVenPartyAddress((VenPartyAddress) next);
							*/
							VenPartyAddress partyAddress = (VenPartyAddress) venPartyAddressDAO.saveAndFlush((VenPartyAddress) next);
							retVal.add(partyAddress);

							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenPartyAddressSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPartyAddressSessionEJBBean:");
						}
						*/
					}
				}
				// Order status values need to be restricted to Venice values
				if (next instanceof VenOrderStatus) {
					if (((VenOrderStatus) next).getOrderStatusCode() != null) {
						LOG.debug("Restricting VenOrderStatus... :" + ((VenOrderStatus) next).getOrderStatusCode());
						
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
						LOG.debug("Restricting VenFraudCheckStatus... :" + ((VenFraudCheckStatus) next).getFraudCheckStatusDesc());
						//try {
							/*
							VenFraudCheckStatusSessionEJBLocal fraudCheckStatusHome = (VenFraudCheckStatusSessionEJBLocal) this._genericLocator
									.lookupLocal(VenFraudCheckStatusSessionEJBLocal.class, "VenFraudCheckStatusSessionEJBBeanLocal");
							List<VenFraudCheckStatus> fraudCheckStatusList = fraudCheckStatusHome
									.queryByRange("select o from VenFraudCheckStatus o where o.fraudCheckStatusDesc ='" + ((VenFraudCheckStatus) next).getFraudCheckStatusDesc() + "'", 0, 1);
							*/
							List<VenFraudCheckStatus> fraudCheckStatusList = venFraudCheckStatusDAO.findByFraudCheckStatusDesc(((VenFraudCheckStatus) next).getFraudCheckStatusDesc());
							if (fraudCheckStatusList == null || fraudCheckStatusList.isEmpty()) {
								throw new InvalidOrderException("Fraud check status value does not exist", VeniceExceptionConstants.VEN_EX_300001);
							} else {
								retVal.add(fraudCheckStatusList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenFraudCheckStatusSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenFraudCheckStatusSessionEJBBean:");
						}
						*/
					}
				}
				// Contact details need to be synchronized
				if (next instanceof VenContactDetail) {
					if (((VenContactDetail) next).getVenContactDetailType() != null) {
						//try {
							LOG.debug("Synchronizing VenContactDetail... :" + ((VenContactDetail) next).getVenContactDetailType());
							// Synchronize the reference data
							next = this.synchronizeVenContactDetailReferenceData((VenContactDetail) next);
							// Synchronize the object
							/*
							VenContactDetailSessionEJBLocal contactDetailHome = (VenContactDetailSessionEJBLocal) this._genericLocator
									.lookupLocal(VenContactDetailSessionEJBLocal.class, "VenContactDetailSessionEJBBeanLocal");
							VenContactDetail contactDetail = (VenContactDetail) contactDetailHome.persistVenContactDetail((VenContactDetail) next);
							*/
							VenContactDetail contactDetail = venContactDetailDAO.saveAndFlush((VenContactDetail) next);
							retVal.add(contactDetail);

							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenContactDetailSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenContactDetailSessionEJBBean:");
						}
						*/
					}
				}
				// Contact detail types need to be restricted to Venice values
				if (next instanceof VenContactDetailType) {
					if (((VenContactDetailType) next).getContactDetailTypeDesc() != null) {
						LOG.debug("Restricting VenContactDetailType... :" + ((VenContactDetailType) next).getContactDetailTypeDesc());
						//try {
							/*
							VenContactDetailTypeSessionEJBLocal contactDetailTypeHome = (VenContactDetailTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenContactDetailTypeSessionEJBLocal.class, "VenContactDetailTypeSessionEJBBeanLocal");
							List<VenContactDetailType> contactDetailTypeList = contactDetailTypeHome
									.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeDesc ='" + ((VenContactDetailType) next).getContactDetailTypeDesc() + "'", 0, 1);
									*/
							List<VenContactDetailType> contactDetailTypeList = venContactDetailTypeDAO.findByContactDetailTypeDesc(((VenContactDetailType) next).getContactDetailTypeDesc());
							if (contactDetailTypeList == null || contactDetailTypeList.isEmpty()) {
								throw new InvalidOrderException("Contact detail type does not exist", VeniceExceptionConstants.VEN_EX_999999);
							} else {
								retVal.add(contactDetailTypeList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenContactDetailTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenContactDetailTypeSessionEJBBean:");
						}
						*/
					}
				}
				// Blocking sources need to be restricted to Venice values
				if (next instanceof VenOrderBlockingSource) {
					if (((VenOrderBlockingSource) next).getBlockingSourceDesc() != null) {
						LOG.debug("Restricting VenOrderBlockingSource... :" + ((VenOrderBlockingSource) next).getBlockingSourceDesc());
							
						VenOrderBlockingSource venOrderBlockingSource = venOrderBlockingSourceDAO.findByBlockingSourceDesc(((VenOrderBlockingSource) next).getBlockingSourceDesc());
						
						if (venOrderBlockingSource == null) {
							throw new InvalidOrderException("Order blocking source does not exist", VeniceExceptionConstants.VEN_EX_999999);
						} else {
							retVal.add(venOrderBlockingSource);
						}
					}
				}
				// Party types need to be restricted to Venice values
				if (next instanceof VenPartyType) {
					if (((VenPartyType) next).getPartyTypeDesc() != null) {
						//try {
							LOG.debug("Restricting VenPartyType... :" + ((VenPartyType) next).getPartyTypeId());
							/*
							VenPartyTypeSessionEJBLocal partyTypeHome = (VenPartyTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPartyTypeSessionEJBLocal.class, "VenPartyTypeSessionEJBBeanLocal");

							List<VenPartyType> partyTypeList = partyTypeHome
									.queryByRange("select o from VenPartyType o where o.partyTypeId =" + ((VenPartyType) next).getPartyTypeId(), 0, 1);
							*/
							VenPartyType partyType = venPartyTypeDAO.findOne(((VenPartyType) next).getPartyTypeId());
							if (partyType == null) {
								throw new InvalidOrderException("Party type does not exist", VeniceExceptionConstants.VEN_EX_999999);
							} else {
								//retVal.add(partyTypeList.get(0));
								retVal.add(partyType);
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenPartyTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenPartyTypeSessionEJBBean:");
						}
						*/
					}
				}
				// Payment status must be restricted to Venice values
				if (next instanceof VenPaymentStatus) {
					if (((VenPaymentStatus) next).getPaymentStatusCode() != null) {
						//try {
							LOG.debug("Restricting VenPaymentStatus... :" + ((VenPaymentStatus) next).getPaymentStatusCode());
							/*
							VenPaymentStatusSessionEJBLocal paymentStatusHome = (VenPaymentStatusSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPaymentStatusSessionEJBLocal.class, "VenPaymentStatusSessionEJBBeanLocal");
							List<VenPaymentStatus> paymentStatusList = paymentStatusHome
									.queryByRange("select o from VenPaymentStatus o where o.paymentStatusCode ='" + ((VenPaymentStatus) next).getPaymentStatusCode() + "'", 0, 1);
							*/
							List<VenPaymentStatus> paymentStatusList = venPaymentStatusDAO.findByPaymentStatusCode(((VenPaymentStatus) next).getPaymentStatusCode());
							if (paymentStatusList == null || paymentStatusList.isEmpty()) {
								throw new InvalidOrderException("Payment status does not exist", VeniceExceptionConstants.VEN_EX_999999);
							} else {
								retVal.add(paymentStatusList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenPaymentStatusSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPaymentStatusSessionEJBBean:");
						}
						*/
					}
				}
				// Payment type must be restricted to Venice values
				if (next instanceof VenPaymentType) {
					if (((VenPaymentType) next).getPaymentTypeCode() != null) {
						//try {
							LOG.debug("Restricting VenPaymentType... :" + ((VenPaymentType) next).getPaymentTypeCode());
							/*
							VenPaymentTypeSessionEJBLocal paymentTypeHome = (VenPaymentTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPaymentTypeSessionEJBLocal.class, "VenPaymentTypeSessionEJBBeanLocal");
							List<VenPaymentType> paymentTypeList = paymentTypeHome
									.queryByRange("select o from VenPaymentType o where o.paymentTypeCode ='" + ((VenPaymentType) next).getPaymentTypeCode() + "'", 0, 1);
							*/
							List<VenPaymentType> paymentTypeList = venPaymentTypeDAO.findByPaymentTypeCode(((VenPaymentType) next).getPaymentTypeCode());
							if (paymentTypeList == null || paymentTypeList.isEmpty()) {
								throw new InvalidOrderException("Payment type does not exist", VeniceExceptionConstants.VEN_EX_999999);
							} else {
								retVal.add(paymentTypeList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenPaymentTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPaymentTypeSessionEJBBean:");
						}
						*/
					}
				}
				// WCS Payment type must be restricted
				if (next instanceof VenWcsPaymentType) {
					if (((VenWcsPaymentType) next).getWcsPaymentTypeCode() != null) {
						//try {
							LOG.debug("Restricting VenWcsPaymentType... :" + ((VenWcsPaymentType) next).getWcsPaymentTypeCode());
							/*
							VenWcsPaymentTypeSessionEJBLocal wcsPaymentTypeHome = (VenWcsPaymentTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenWcsPaymentTypeSessionEJBLocal.class, "VenWcsPaymentTypeSessionEJBBeanLocal");
							List<VenWcsPaymentType> wcsPaymentTypeList = wcsPaymentTypeHome
									.queryByRange("select o from VenWcsPaymentType o where o.wcsPaymentTypeCode ='" + ((VenWcsPaymentType) next).getWcsPaymentTypeCode() + "'", 0, 1);
							*/
							List<VenWcsPaymentType> wcsPaymentTypeList = venWcsPaymentTypeDAO.findByWcsPaymentTypeCode(((VenWcsPaymentType) next).getWcsPaymentTypeCode()); 
							if (wcsPaymentTypeList == null || wcsPaymentTypeList.isEmpty()) {
								throw new InvalidOrderException("WCS Payment type does not exist", VeniceExceptionConstants.VEN_EX_999999);
							} else {
								retVal.add(wcsPaymentTypeList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenWcsPaymentTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenWcsPaymentTypeSessionEJBBean:");
						}
						*/
					}
				}

				// Product categories need to be synchronized
				if (next instanceof VenProductCategory) {
					if (((VenProductCategory) next).getProductCategory() != null) {
						//try {
							LOG.debug("Synchronizing VenProductCategory... :" + ((VenProductCategory) next).getProductCategory());
							/*
							VenProductCategorySessionEJBLocal productCategoryHome = (VenProductCategorySessionEJBLocal) this._genericLocator
									.lookupLocal(VenProductCategorySessionEJBLocal.class, "VenProductCategorySessionEJBBeanLocal");
							List<VenProductCategory> productCategoryList = productCategoryHome
									.queryByRange("select o from VenProductCategory o where o.productCategory ='" + ((VenProductCategory) next).getProductCategory() + "'", 0, 1);
							*/
							List<VenProductCategory> productCategoryList = venProductCategoryDAO.findByProductCategory(((VenProductCategory) next).getProductCategory());
							if (productCategoryList == null || productCategoryList.isEmpty()) {
								//VenProductCategory productCategory = (VenProductCategory) productCategoryHome.persistVenProductCategory((VenProductCategory) next);
								VenProductCategory productCategory = venProductCategoryDAO.saveAndFlush((VenProductCategory) next);
								retVal.add(productCategory);
							} else {
								retVal.add(productCategoryList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenProductCategorySessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenProductCategorySessionEJBBean:");
						}
						*/
					}
				}
				// Product types need to be synchronized
				if (next instanceof VenProductType) {
					if (((VenProductType) next).getProductTypeCode() != null) {
						//try {
							LOG.debug("Synchronizing VenProductType... :" + ((VenProductType) next).getProductTypeCode());
							/*
							VenProductTypeSessionEJBLocal productTypeHome = (VenProductTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenProductTypeSessionEJBLocal.class, "VenProductTypeSessionEJBBeanLocal");
							List<VenProductType> productTypeList = productTypeHome
									.queryByRange("select o from VenProductType o where o.productTypeCode ='" + ((VenProductType) next).getProductTypeCode() + "'", 0, 1);
							*/
							List<VenProductType> productTypeList = venProductTypeDAO.findByProductTypeCode(((VenProductType) next).getProductTypeCode());
							if (productTypeList == null || productTypeList.isEmpty()) {
								//VenProductType productType = (VenProductType) productTypeHome.persistVenProductType((VenProductType) next);
								VenProductType productType = venProductTypeDAO.saveAndFlush((VenProductType) next);
								retVal.add(productType);

							} else {
								retVal.add(productTypeList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenProductTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenProductTypeSessionEJBBean:");
						}
						*/
					}
				}
				// Promotions need to be synchronized
				if (next instanceof VenPromotion) {
					if (((VenPromotion) next).getPromotionCode() != null) {
						try {
							LOG.debug("Synchronizing VenPromotion... :" + ((VenPromotion) next).getPromotionCode());
							/*
							VenPromotionSessionEJBLocal promotionHome = (VenPromotionSessionEJBLocal) this._genericLocator
									.lookupLocal(VenPromotionSessionEJBLocal.class, "VenPromotionSessionEJBBeanLocal");
								*/
							
							VenPromotion promotion = new VenPromotion();
							
							/*
							List<VenPromotion> promotionExactList = promotionHome.queryByRange("select o from VenPromotion o where o.promotionCode = '"+((VenPromotion) next).getPromotionCode()+"' " +
									"and o.promotionName = '"+((VenPromotion) next).getPromotionName()+"' " +
									"and o.gdnMargin =" + ((VenPromotion) next).getGdnMargin() + " " +
									"and o.merchantMargin = " + ((VenPromotion) next).getMerchantMargin() + " " +
									"and o.othersMargin = " +((VenPromotion) next).getOthersMargin(), 0, 1);
							*/
							List<VenPromotion> promotionExactList = venPromotionDAO.findByPromotionAndMargin(((VenPromotion) next).getPromotionCode()
									, ((VenPromotion) next).getPromotionName(), ((VenPromotion) next).getGdnMargin()
									, ((VenPromotion) next).getMerchantMargin(), ((VenPromotion) next).getOthersMargin());
							
							LOG.debug("promotionExactList size: "+promotionExactList.size());
														
							if(promotionExactList.size()>0) {
								LOG.debug("exact promo found");
								promotion=promotionExactList.get(0);								
								if(promotion.getVenPromotionType()==null || promotion.getVenPromotionType().getPromotionType()==null){	
									if(promotion.getPromotionName().toLowerCase().contains("free shipping")){
										VenPromotionType type = new VenPromotionType();
										type.setPromotionType(VeniceConstants.VEN_PROMOTION_TYPE_FREESHIPPING);
										promotion.setVenPromotionType(type);
										//promotion=promotionHome.mergeVenPromotion(promotion);
										promotion = venPromotionDAO.saveAndFlush(promotion);
									}																				
								}								
							}else {								
								LOG.debug("exact promo not found, cek uploaded promo");
								/*
								List<VenPromotion> promotionUploadedList = promotionHome.queryByRange("select o from VenPromotion o where o.promotionCode = '"+((VenPromotion) next).getPromotionCode()+"' " +
										"and o.promotionName is null and o.gdnMargin is null and o.merchantMargin is null and o.othersMargin is null", 0, 1);
								*/
								List<VenPromotion> promotionUploadedList = venPromotionDAO.findByPromotionAndMargin(
										((VenPromotion) next).getPromotionCode(), null, null, null, null);
								if(promotionUploadedList.size()>0){
									LOG.debug("uploaded promo found, set the promo name and margins and then merge");
									promotion=promotionUploadedList.get(0);
									promotion.setPromotionName(((VenPromotion) next).getPromotionName());
									promotion.setGdnMargin(((VenPromotion) next).getGdnMargin());
									promotion.setMerchantMargin(((VenPromotion) next).getMerchantMargin());
									promotion.setOthersMargin(((VenPromotion) next).getOthersMargin());	
									//promotion=promotionHome.mergeVenPromotion(promotion);
									promotion = venPromotionDAO.saveAndFlush(promotion);
								}else{
									LOG.debug("no exact matching promo code, no uploaded promo, persist promo from inbound");
									//promotion=promotionHome.persistVenPromotion((VenPromotion) next);
									promotion = venPromotionDAO.saveAndFlush((VenPromotion) next);
									
									//check the promo code for free shipping
									if(promotion.getVenPromotionType()==null || promotion.getVenPromotionType().getPromotionType()==null){											
										if(promotion.getPromotionName().toLowerCase().contains("free shipping")){
											VenPromotionType type = new VenPromotionType();
											type.setPromotionType(VeniceConstants.VEN_PROMOTION_TYPE_FREESHIPPING);
											promotion.setVenPromotionType(type);
											//promotion=promotionHome.mergeVenPromotion(promotion);
											promotion = venPromotionDAO.saveAndFlush(promotion);
										}
									}
								}															
							}
							retVal.add(promotion);							
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenPromotionSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenPromotionSessionEJBBean:");
						}
					}
				}
				// Cities need to be synchronized
				if (next instanceof VenCity) {
					if (((VenCity) next).getCityCode() != null) {
						LOG.debug("Synchronizing VenCity... :" + ((VenCity) next).getCityCode());
						//try {
							//VenCitySessionEJBLocal cityHome = (VenCitySessionEJBLocal) this._genericLocator.lookupLocal(VenCitySessionEJBLocal.class, "VenCitySessionEJBBeanLocal");
							//List<VenCity> cityList = cityHome.queryByRange("select o from VenCity o where o.cityCode ='" + ((VenCity) next).getCityCode() + "'", 0, 1);
							List<VenCity> cityList = venCityDAO.findByCityCode(((VenCity) next).getCityCode());
							if (cityList == null || cityList.isEmpty()) {
								//VenCity city = (VenCity) cityHome.persistVenCity((VenCity) next);
								VenCity city = (VenCity) venCityDAO.saveAndFlush((VenCity) next);
								retVal.add(city);

							} else {
								retVal.add(cityList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenCitySessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenCitySessionEJBBean:");
						}
						*/
					}
				}
				// States need to be synchronized
				if (next instanceof VenState) {
					if (((VenState) next).getStateCode() != null) {
						//try {
							LOG.debug("Synchronizing VenState... :" + ((VenState) next).getStateCode());
							/*
							VenStateSessionEJBLocal stateHome = (VenStateSessionEJBLocal) this._genericLocator
									.lookupLocal(VenStateSessionEJBLocal.class, "VenStateSessionEJBBeanLocal");
							List<VenState> stateList = stateHome.queryByRange("select o from VenState o where o.stateCode ='" + ((VenState) next).getStateCode() + "'", 0, 1);
							*/
							List<VenState> stateList = venStateDAO.findByStateCode(((VenState) next).getStateCode());
							if (stateList == null || stateList.isEmpty()) {
								//VenState state = (VenState) stateHome.persistVenState((VenState) next);
								VenState state = venStateDAO.saveAndFlush((VenState) next);
								retVal.add(state);

							} else {
								retVal.add(stateList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenStateSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenStateSessionEJBBean:");
						}
						*/
					}
				}
				// Countries need to be synchronized
				if (next instanceof VenCountry) {
					if (((VenCountry) next).getCountryCode() != null) {
						//try {
							LOG.debug("Synchronizing VenCountry... :" + ((VenCountry) next).getCountryCode());
							/*
							VenCountrySessionEJBLocal countryHome = (VenCountrySessionEJBLocal) this._genericLocator
									.lookupLocal(VenCountrySessionEJBLocal.class, "VenCountrySessionEJBBeanLocal");
							List<VenCountry> countryList = countryHome
									.queryByRange("select o from VenCountry o where o.countryCode ='" + ((VenCountry) next).getCountryCode() + "'", 0, 1);
							*/
							List<VenCountry> countryList = venCountryDAO.findByCountryCode(((VenCountry) next).getCountryCode());
							if (countryList == null || countryList.isEmpty()) {
								//VenCountry country = (VenCountry) countryHome.persistVenCountry((VenCountry) next);
								VenCountry country = venCountryDAO.saveAndFlush((VenCountry) next);
								retVal.add(country);
							} else {
								retVal.add(countryList.get(0));
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenCountrySessionEJBBean:" + e.getMessage());
							e.printStackTrace();	
							throw new EJBException("An exception occured when looking up VenCountrySessionEJBBean:");
						}
						*/
					}
				}
				// Address types need to be restricted
				if (next instanceof VenAddressType) {
					if (((VenAddressType) next).getAddressTypeId() != null) {
						//try {
							LOG.debug("Restricting VenAddressType... :" + ((VenAddressType) next).getAddressTypeId());
							/*
							VenAddressTypeSessionEJBLocal addressTypeHome = (VenAddressTypeSessionEJBLocal) this._genericLocator
									.lookupLocal(VenAddressTypeSessionEJBLocal.class, "VenAddressTypeSessionEJBBeanLocal");
							List<VenAddressType> addressTypeList = addressTypeHome
									.queryByRange("select o from VenAddressType o where o.addressTypeId =" + ((VenAddressType) next).getAddressTypeId(), 0, 1);
							*/
							VenAddressType venAddressType = venAddressTypeDAO.findOne(((VenAddressType) next).getAddressTypeId());
							//if (addressTypeList == null || addressTypeList.isEmpty()) {
							if (venAddressType == null) {
								//VenAddressType addressType = (VenAddressType) addressTypeHome.persistVenAddressType((VenAddressType) next);
								VenAddressType addressType = venAddressTypeDAO.saveAndFlush((VenAddressType) next); 
								retVal.add(addressType);

							} else {
								//retVal.add(addressTypeList.get(0));
								retVal.add(venAddressType);
							}
							/*
						} catch (Exception e) {
							LOG.error("An exception occured when looking up VenAddressTypeSessionEJBBean:" + e.getMessage());
							e.printStackTrace();
							throw new EJBException("An exception occured when looking up VenAddressTypeSessionEJBBean:");
						}
						*/
					}
				}							
			}
		}
		return retVal;
	}	
	
	/**
	 * Synchronizes the data for the direct VenAddress references
	 * 
	 * @param venAddress
	 * @return the synchronized data object
	 */
	private VenAddress synchronizeVenAddressReferenceData(VenAddress venAddress) throws InvalidOrderException {
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
	 * Synchronizes the data for the direct VenPartyAddress references
	 * 
	 * @param venPartyAddress
	 * @return
	 */
	private VenPartyAddress synchronizeVenPartyAddressReferenceData(VenPartyAddress venPartyAddress)
	 throws InvalidOrderException {
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
	 * Synchronizes the data for the direct VenMerchantProduct references
	 * 
	 * @param venMerchantProduct
	 * @return the synchronized data object
	 */
	private VenMerchantProduct synchronizeVenMerchantProductReferenceData(
			VenMerchantProduct venMerchantProduct) throws InvalidOrderException {

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
	 * Persists a list of addresses.
	 * 
	 * @param venAddressList
	 * @return
	 * @throws InvalidOrderException 
	 */
	List<VenAddress> persistAddressList(List<VenAddress> venAddressList) throws InvalidOrderException {
		List<VenAddress> newVenAddressList = new ArrayList<VenAddress>();
		Iterator<VenAddress> i = venAddressList.iterator();
		while (i.hasNext()) {
			VenAddress newAddress = this.persistAddress(i.next());
			newVenAddressList.add(newAddress);
		}
		return newVenAddressList;
	}	
	
	/**
	 * updateAddressList - compares the existing address list with the new address list,
	 * writes any new addresses to the database and returns the updated address list.
	 * 
	 * @param existingVenAddressList
	 * @param newVenAddressList
	 * @return the updated address list
	 * @throws InvalidOrderException 
	 */
	List<VenAddress> updateAddressList(List<VenAddress> existingVenAddressList, List<VenAddress> newVenAddressList) throws InvalidOrderException{
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
					LOG.debug("\n party address equal with existing.");
					updatedVenAddressList.add(existingVenAddress);	
					break;
				}else{
					LOG.debug("\n party address NOT equal with existing.");
					isAddressEqual=false;
					tempAddress=existingVenAddress;
				}
			}
			if(isAddressEqual==false){
				/*
				 * The address is a new address so it needs to be persisted
				 */
				LOG.debug("\n party address is new address.");
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
	
	private VenOrder retrieveExistingOrder(String wcsOrderId) {
		List<VenOrder> venOrderList = venOrderDAO.findByWcsOrderId(wcsOrderId);
		if (venOrderList != null && !venOrderList.isEmpty()) {
			return venOrderList.get(0);
		} 
		
		return null;
	}	
	
	/**
	 * Retreives an existing party from the cache along with 
	 * contact and address details
	 * 
	 * @param fullOrLegalName
	 * @return the party if it exists else null
	 */
	private VenParty retrieveExistingParty(String custUserName) {
		String escapeChar = "";

		List<VenCustomer> customerList = venCustomerDAO.findByCustomerName(JPQLStringEscapeUtility.escapeJPQLStringData(custUserName, escapeChar));
		if (customerList != null && !customerList.isEmpty()) {
			VenParty party = customerList.get(0).getVenParty();

			/*
			 * Fetch the list of contact details for the party
			 */
			List<VenContactDetail> venContactDetailList = venContactDetailDAO.findByParty(party);
			LOG.debug("Total existing vencontactdetail => " + venContactDetailList.size());
			party.setVenContactDetails(venContactDetailList);

			/*
			 * Fetch the list of party addresses for the party
			 */
			//List<VenPartyAddress> venPartyAddressList = partyAddressHome.queryByRange("select o from VenPartyAddress o where o.venParty.partyId = " + party.getPartyId(), 0, 0);
			List<VenPartyAddress> venPartyAddressList = venPartyAddressDAO.findByVenParty(party);
			LOG.debug("Total existing VenPartyAddress => " + venPartyAddressList.size());
			party.setVenPartyAddresses(venPartyAddressList);

			return party;
		} else {
			return null;
		}
	}	
	
	private boolean isOrderExist(String wcsOrderId) {
		if (retrieveExistingOrder(wcsOrderId) != null) return true;
		
		return false;
	}
	
	private boolean isItemWCSExistInDB(String wcsOrderItemId) {
		List<VenOrderItem> venOrderItemList = venOrderItemDAO.findByWcsOrderItemId(wcsOrderItemId);
		if ((venOrderItemList != null) && (!(venOrderItemList.isEmpty()))) return true;
		return false;
	}

}
