package com.gdn.venice.facade.callback;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.integration.outbound.Publisher;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.persistence.VenMerchant;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenRecipient;
import com.gdn.venice.util.VeniceConstants;

/**
 * VenOrderItemSessionEJBCallback.java
 *
 * This callback object is used to implement the publishing of order item status
 * changes
 *
 * <p> <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p> <b>version:</b> 1.0 <p> <b>since:</b> 2011
 */
public class VenOrderItemSessionEJBCallback implements SessionCallback {

    protected static Logger _log = null;

    /**
     * Default constructor.
     */
    public VenOrderItemSessionEJBCallback() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.VenOrderItemSessionEJBCallback");
    }

    @Override
    public Boolean onPrePersist(Object businessObject) {
        _log.debug("onPrePersist");
        return Boolean.TRUE;
    }

    @Override
    public Boolean onPostPersist(Object businessObject) {
        _log.debug("onPostPersist");
        return Boolean.TRUE;
    }

    @Override
    public Boolean onPreMerge(Object businessObject) {
        _log.debug("onPreMerge");
        return Boolean.TRUE;
    }

    public Boolean onPreMerge(Object businessObject, EntityManager em) {
        _log.debug("onPreMerge");
        long start = System.currentTimeMillis();
        VenOrderItem venOrderItem = (VenOrderItem) businessObject;

        /*
         * If the settlement record is present then the update has come from MTA
         * message and Venice should not publish another message or it may end
         * up in a message fest
         */
//		if (venOrderItem.getVenSettlementRecords() != null && !venOrderItem.getVenSettlementRecords().isEmpty()) {
//			return Boolean.TRUE;
//		}

        Locator<Object> locator = null;
        try {
            _log.debug("start order item callback");
            locator = new Locator<Object>();

            @SuppressWarnings("deprecation")
            Connection conn = ((EntityManagerImpl) em).getSession().connection();

            String orderItemSQL = "select * from ven_order_item where order_item_id = ?";
            PreparedStatement psOrderItem = conn.prepareStatement(orderItemSQL);
            psOrderItem.setLong(1, venOrderItem.getOrderItemId());

            ResultSet rsOrderItem = psOrderItem.executeQuery();
            rsOrderItem.next();
            VenOrderItem existingVenOrderItem = new VenOrderItem();

            existingVenOrderItem.setEtd(rsOrderItem.getInt("etd"));
            existingVenOrderItem.setGiftCardFlag(rsOrderItem.getBoolean("gift_card_flag"));
            existingVenOrderItem.setGiftCardNote(rsOrderItem.getString("gift_card_note"));
            existingVenOrderItem.setGiftWrapPrice(rsOrderItem.getBigDecimal("gift_wrap_price"));
            existingVenOrderItem.setGiftWrapFlag(rsOrderItem.getBoolean("gift_wrap_flag"));
            existingVenOrderItem.setInsuranceCost(rsOrderItem.getBigDecimal("insurance_cost"));
            existingVenOrderItem.setMaxEstDate(rsOrderItem.getTimestamp("max_est_date"));
            existingVenOrderItem.setMerchantSettlementFlag(rsOrderItem.getBoolean("merchant_settlement_flag"));
            existingVenOrderItem.setMinEstDate(rsOrderItem.getTimestamp("min_est_date"));
            existingVenOrderItem.setOrderItemId(rsOrderItem.getLong("order_item_id"));
            existingVenOrderItem.setPackageCount(rsOrderItem.getInt("package_count"));
            existingVenOrderItem.setPrice(rsOrderItem.getBigDecimal("price"));
            existingVenOrderItem.setQuantity(rsOrderItem.getInt("quantity"));
            existingVenOrderItem.setSaltCode(rsOrderItem.getString("salt_code"));
            existingVenOrderItem.setShippingCost(rsOrderItem.getBigDecimal("shipping_cost"));
            existingVenOrderItem.setShippingWeight(rsOrderItem.getBigDecimal("shipping_weight"));
            existingVenOrderItem.setSpecialHandlingInstructions(rsOrderItem.getString("special_handling_instructions"));
            existingVenOrderItem.setTotal(rsOrderItem.getBigDecimal("total"));
            existingVenOrderItem.setWcsOrderItemId(rsOrderItem.getString("wcs_order_item_id"));
            existingVenOrderItem.setLogisticsPricePerKg(rsOrderItem.getBigDecimal("logistics_price_per_kg"));

            VenAddress shippingAddress = new VenAddress();
            shippingAddress.setAddressId(rsOrderItem.getLong("shipping_address_id"));
            existingVenOrderItem.setVenAddress(shippingAddress);

            VenMerchantProduct product = new VenMerchantProduct();
            product.setProductId(rsOrderItem.getLong("product_id"));
            existingVenOrderItem.setVenMerchantProduct(product);

            VenOrder order = new VenOrder();
            order.setOrderId(rsOrderItem.getLong("order_id"));
            existingVenOrderItem.setVenOrder(order);

            VenOrderStatus orderStatus = new VenOrderStatus();
            orderStatus.setOrderStatusId(rsOrderItem.getLong("order_status_id"));
            existingVenOrderItem.setVenOrderStatus(orderStatus);

            VenRecipient venRecipient = new VenRecipient();
            venRecipient.setRecipientId(rsOrderItem.getLong("recipient_id"));
            existingVenOrderItem.setVenRecipient(venRecipient);

            LogLogisticService logisticService = new LogLogisticService();
            logisticService.setLogisticsServiceId(rsOrderItem.getLong("logistics_service_id"));
            existingVenOrderItem.setLogLogisticService(logisticService);

            rsOrderItem.close();
            psOrderItem.close();

            Long existingStatus = existingVenOrderItem.getVenOrderStatus().getOrderStatusId();
            Long newStatus = venOrderItem.getVenOrderStatus().getOrderStatusId();
            _log.debug("existingStatus: " + existingVenOrderItem.getVenOrderStatus().getOrderStatusId());
            _log.debug("newStatus: " + venOrderItem.getVenOrderStatus().getOrderStatusId());

            /*
             * If there is a status change then publish it: 
             * o PU > ES 
             * o ES > PP
             * o ES > CX 
             * o PP > CX  
             * o ES > RT 
             * o ES > D 
             * o CX > D
             * o PF > FP
             *  CX must include the logistics info with distribution cart containing airway bill
             * also write it to the status history
             */
            if (!existingVenOrderItem.getVenOrderStatus().getOrderStatusId().equals(venOrderItem.getVenOrderStatus().getOrderStatusId())) {
                _log.debug("there is status change");
                /*
                 * For the state transitions below get all the south information and then publish
                 * the status change.
                 */
                if ((existingStatus == VeniceConstants.VEN_ORDER_STATUS_PU && newStatus == VeniceConstants.VEN_ORDER_STATUS_ES)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newStatus == VeniceConstants.VEN_ORDER_STATUS_PP)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newStatus == VeniceConstants.VEN_ORDER_STATUS_CX)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_PP && newStatus == VeniceConstants.VEN_ORDER_STATUS_CX)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newStatus == VeniceConstants.VEN_ORDER_STATUS_RT)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newStatus == VeniceConstants.VEN_ORDER_STATUS_D)
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_CX && newStatus == VeniceConstants.VEN_ORDER_STATUS_D)
                        //ketika PF dan status order item diupdate jadi FP, maka publish FP untuk order item tersebut
                        || (existingStatus == VeniceConstants.VEN_ORDER_STATUS_PF && newStatus == VeniceConstants.VEN_ORDER_STATUS_FP)) {

                    _log.debug("condition for publish order item status is true");

                    // Fish out all of the South information for the venOrderItem
                    String airwayBillSQL = "select * from log_airway_bill where order_item_id = ?";
                    PreparedStatement psAirwayBill = conn.prepareStatement(airwayBillSQL);

                    psAirwayBill.setLong(1, venOrderItem.getOrderItemId());
                    ResultSet rsAirwayBill = psAirwayBill.executeQuery();

                    ArrayList<LogAirwayBill> airwayBillList = new ArrayList<LogAirwayBill>();

                    while (rsAirwayBill.next()) {

                        LogAirwayBill airwayBill = new LogAirwayBill();

                        airwayBill.setAirwayBillId(rsAirwayBill.getLong("airway_bill_id"));
                        airwayBill.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
                        airwayBill.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
                        airwayBill.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
                        airwayBill.setActualPickupDate(rsAirwayBill.getDate("actual_pickup_date"));
                        airwayBill.setAddress(rsAirwayBill.getString("address"));
                        airwayBill.setAirwayBillNumber(rsAirwayBill.getString("airway_bill_number"));
                        airwayBill.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
                        airwayBill.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
                        airwayBill.setConsignee(rsAirwayBill.getString("consignee"));
                        airwayBill.setContactPerson(rsAirwayBill.getString("contact_person"));
                        airwayBill.setContent(rsAirwayBill.getString("content"));
                        airwayBill.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
                        airwayBill.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
                        airwayBill.setDestCode(rsAirwayBill.getString("dest_code"));
                        airwayBill.setDestination(rsAirwayBill.getString("destination"));
                        airwayBill.setGdnReference(rsAirwayBill.getString("gdn_reference"));
                        airwayBill.setGiftWrapCharge(rsAirwayBill.getBigDecimal("gift_wrap_charge"));
                        airwayBill.setInsuranceCharge(rsAirwayBill.getBigDecimal("insurance_charge"));
                        airwayBill.setInsuredAmount(rsAirwayBill.getBigDecimal("insured_amount"));
                        airwayBill.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
                        airwayBill.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
                        airwayBill.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
                        airwayBill.setMtaData(rsAirwayBill.getBoolean("mta_data"));
                        airwayBill.setNoteReturn(rsAirwayBill.getString("note_return"));
                        airwayBill.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
                        airwayBill.setNumPackages(rsAirwayBill.getInt("num_packages"));
                        airwayBill.setOrigin(rsAirwayBill.getString("origin"));
                        airwayBill.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
                        airwayBill.setPackageWeight(rsAirwayBill.getBigDecimal("package_weight"));
                        airwayBill.setPricePerKg(rsAirwayBill.getBigDecimal("price_per_kg"));
                        airwayBill.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
                        airwayBill.setReceived(rsAirwayBill.getDate("received"));
                        airwayBill.setRecipient(rsAirwayBill.getString("recipient"));
                        airwayBill.setRelation(rsAirwayBill.getString("relation"));
                        airwayBill.setReturn_(rsAirwayBill.getString("return"));
                        airwayBill.setService(rsAirwayBill.getString("service"));
                        airwayBill.setShipper(rsAirwayBill.getString("shipper"));
                        airwayBill.setStatus(rsAirwayBill.getString("status"));
                        airwayBill.setTariff(rsAirwayBill.getString("tariff"));
                        airwayBill.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
                        airwayBill.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
                        airwayBill.setType(rsAirwayBill.getString("type"));
                        airwayBill.setUndelivered(rsAirwayBill.getDate("undelivered"));
                        airwayBill.setZip(rsAirwayBill.getString("zip"));
                        airwayBill.setKpiPickupPerfClocked(rsAirwayBill.getBoolean("kpi_pickup_perf_clocked"));
                        airwayBill.setKpiDeliveryPerfClocked(rsAirwayBill.getBoolean("kpi_delivery_perf_clocked"));
                        airwayBill.setKpiInvoiceAccuracyClocked(rsAirwayBill.getBoolean("kpi_invoice_accuracy_clocked"));

                        LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
                        logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
                        airwayBill.setLogApprovalStatus1(logApprovalStatus1);

                        LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
                        logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
                        airwayBill.setLogApprovalStatus2(logApprovalStatus2);

                        LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
                        logLogisticsProvider.setLogisticsProviderId(rsAirwayBill.getLong("logistics_provider_id"));
                        airwayBill.setLogLogisticsProvider(logLogisticsProvider);

                        airwayBill.setVenOrderItem(venOrderItem);
                        // add this airway bill to the list
                        airwayBillList.add(airwayBill);
                    }

                    venOrderItem.setLogAirwayBills(airwayBillList);

                    if (rsAirwayBill != null) {
                        rsAirwayBill.close();
                    }
                    if (psAirwayBill != null) {
                        psAirwayBill.close();
                    }

                    _log.debug("start publish order item status");
                    Publisher publisher = new Publisher();
                    publisher.publishUpdateOrderItemStatus(venOrderItem, conn);
                    _log.debug("done publish order item status");
                }

                if (newStatus == VeniceConstants.VEN_ORDER_STATUS_PU
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_ES
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_PP
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_CX
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_RT
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_D
                        || newStatus == VeniceConstants.VEN_ORDER_STATUS_X) {

                    //add order item status history 
                    _log.debug("add order item status history");
                    _log.debug("\n wcs order item id: " + venOrderItem.getWcsOrderItemId());
                    _log.debug("\n order item status: " + venOrderItem.getVenOrderStatus().getOrderStatusId());
                    VenOrderItemStatusHistorySessionEJBRemote orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
                            .lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");

                    VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
                    venOrderItemStatusHistoryPK.setOrderItemId(venOrderItem.getOrderItemId());
                    venOrderItemStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));

                    VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
                    orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
                    orderItemStatusHistory.setStatusChangeReason("Updated by System");
                    orderItemStatusHistory.setVenOrderStatus(venOrderItem.getVenOrderStatus());

                    orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);
                    _log.debug("done add order item status history");
                }
            }

            //add dummy airway bill when status become FP
//			List<LogAirwayBill> airwayBillListTemp = new ArrayList<LogAirwayBill>();		
            _log.debug("Check if status FP then add dummy airway bill");
            LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

            if ((existingStatus == VeniceConstants.VEN_ORDER_STATUS_C || existingStatus == VeniceConstants.VEN_ORDER_STATUS_SF) && newStatus == VeniceConstants.VEN_ORDER_STATUS_FP) {
                //delete dummy airway bill if it is already exist.
                //this could happen if FP status sent multiple times, for example if the FP status not updated in MTA, then it is sent manually from venice again.
                _log.debug("new status is FP, check if dummy airwaybill already exist");

                String deleteAirwayBillSQL = "delete from log_airway_bill where order_item_id = ?";
                PreparedStatement psAirwayBill = conn.prepareStatement(deleteAirwayBillSQL);
                psAirwayBill.setLong(1, existingVenOrderItem.getOrderItemId());
                int totalAirwayBillDeleted = psAirwayBill.executeUpdate();

                psAirwayBill.close();

                _log.debug(totalAirwayBillDeleted + " dummy airway bill deleted");

//				_log.debug("query order item list");				
//				List<VenOrderItem> venOrderItemList2 = orderItemHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + existingVenOrderItem.getVenOrder().getOrderId(), 0, 0);
//				_log.debug("looping order item list");
                orderItemSQL = "select oi.*, "
                        + "p.*, "
                        + "a.*, "
                        + "c.*, "
                        + "ls.*, "
                        + "lp.*, "
                        + "m.* "
                        + "from ven_order_item oi "
                        + "inner join ven_merchant_product mp on mp.product_id = oi.product_id "
                        + "inner join ven_merchant m on m.merchant_id = mp.merchant_id "
                        + "inner join ven_party p on m.party_id = p.party_id "
                        + "inner join ven_address a on a.address_id = oi.shipping_address_id "
                        + "left join ven_city c on a.city_id = c.city_id "
                        + "inner join log_logistic_service ls on ls.logistics_service_id = oi.logistics_service_id "
                        + "inner join log_logistics_provider lp on lp.logistics_provider_id = ls.logistics_provider_id "
                        + "where wcs_order_item_id = ?";

                psOrderItem = conn.prepareStatement(orderItemSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                psOrderItem.setString(1, existingVenOrderItem.getWcsOrderItemId());
                rsOrderItem = psOrderItem.executeQuery();

                rsOrderItem.last();
                int totalOrderItem = rsOrderItem.getRow();
                rsOrderItem.beforeFirst();

                if (totalOrderItem > 0) {
                    rsOrderItem.next();

                    VenOrderItem orderItemResult = new VenOrderItem();

                    orderItemResult.setEtd(rsOrderItem.getInt("etd"));
                    orderItemResult.setGiftCardFlag(rsOrderItem.getBoolean("gift_card_flag"));
                    orderItemResult.setGiftCardNote(rsOrderItem.getString("gift_card_note"));
                    orderItemResult.setGiftWrapPrice(rsOrderItem.getBigDecimal("gift_wrap_price"));
                    orderItemResult.setGiftWrapFlag(rsOrderItem.getBoolean("gift_wrap_flag"));
                    orderItemResult.setInsuranceCost(rsOrderItem.getBigDecimal("insurance_cost"));
                    orderItemResult.setMaxEstDate(rsOrderItem.getTimestamp("max_est_date"));
                    orderItemResult.setMerchantSettlementFlag(rsOrderItem.getBoolean("merchant_settlement_flag"));
                    orderItemResult.setMinEstDate(rsOrderItem.getTimestamp("min_est_date"));
                    orderItemResult.setOrderItemId(rsOrderItem.getLong("order_item_id"));
                    orderItemResult.setPackageCount(rsOrderItem.getInt("package_count"));
                    orderItemResult.setPrice(rsOrderItem.getBigDecimal("price"));
                    orderItemResult.setQuantity(rsOrderItem.getInt("quantity"));
                    orderItemResult.setSaltCode(rsOrderItem.getString("salt_code"));
                    orderItemResult.setShippingCost(rsOrderItem.getBigDecimal("shipping_cost"));
                    orderItemResult.setShippingWeight(rsOrderItem.getBigDecimal("shipping_weight"));
                    orderItemResult.setSpecialHandlingInstructions(rsOrderItem.getString("special_handling_instructions"));
                    orderItemResult.setTotal(rsOrderItem.getBigDecimal("total"));
                    orderItemResult.setWcsOrderItemId(rsOrderItem.getString("wcs_order_item_id"));
                    orderItemResult.setLogisticsPricePerKg(rsOrderItem.getBigDecimal("logistics_price_per_kg"));

                    VenCity cityResult = new VenCity();
                    cityResult.setCityName(rsOrderItem.getString("city_name"));

                    VenAddress shippingAddressResult = new VenAddress();
                    shippingAddressResult.setAddressId(rsOrderItem.getLong("shipping_address_id"));
                    shippingAddressResult.setPostalCode(rsOrderItem.getString("postal_code"));
                    shippingAddressResult.setVenCity(cityResult);
                    orderItemResult.setVenAddress(shippingAddressResult);

                    VenParty partyMerchantResult = new VenParty();
                    partyMerchantResult.setPartyId(rsOrderItem.getLong("party_id"));
                    partyMerchantResult.setFullOrLegalName(rsOrderItem.getString("full_or_legal_name"));

                    VenMerchant merchantResult = new VenMerchant();
                    merchantResult.setMerchantId(rsOrderItem.getLong("merchant_id"));
                    merchantResult.setVenParty(partyMerchantResult);

                    VenMerchantProduct productResult = new VenMerchantProduct();
                    productResult.setProductId(rsOrderItem.getLong("product_id"));
                    productResult.setVenMerchant(merchantResult);
                    orderItemResult.setVenMerchantProduct(productResult);

                    VenOrder orderResult = new VenOrder();
                    orderResult.setOrderId(rsOrderItem.getLong("order_id"));
                    orderItemResult.setVenOrder(orderResult);

                    VenOrderStatus venOrderStatusResult = new VenOrderStatus();
                    venOrderStatusResult.setOrderStatusId(rsOrderItem.getLong("order_status_id"));
                    orderItemResult.setVenOrderStatus(venOrderStatusResult);

                    VenRecipient venRecipientResult = new VenRecipient();
                    venRecipientResult.setRecipientId(rsOrderItem.getLong("recipient_id"));
                    orderItemResult.setVenRecipient(venRecipientResult);

                    LogLogisticsProvider logisticsProviderResult = new LogLogisticsProvider();
                    logisticsProviderResult.setLogisticsProviderCode(rsOrderItem.getString("logistics_provider_code"));
                    logisticsProviderResult.setLogisticsProviderId(rsOrderItem.getLong("logistics_provider_id"));

                    LogLogisticService logisticServiceResult = new LogLogisticService();
                    logisticServiceResult.setLogisticsServiceId(rsOrderItem.getLong("logistics_service_id"));
                    logisticServiceResult.setServiceCode(rsOrderItem.getString("service_code"));
                    logisticServiceResult.setLogLogisticsProvider(logisticsProviderResult);
                    orderItemResult.setLogLogisticService(logisticServiceResult);

                    venOrderItem = orderItemResult;
                }

                rsOrderItem.close();
                psOrderItem.close();

                _log.debug("add dummy airway bill for wcs order item id: " + venOrderItem.getWcsOrderItemId());
                LogAirwayBill airwayBill = new LogAirwayBill();
                airwayBill.setVenOrderItem(venOrderItem);

                //don't set gdn reference when add dummy airway bill from status FP, because we don't have distribution cart info at the moment.
                //String gdnReference = venOrder.getRmaFlag() ? "R-" : "O-" + venOrder.getWcsOrderId() + "-" + venOrderItem.getWcsOrderItemId() + "-1";
                //airwayBill.setGdnReference(gdnReference);
                _log.debug("venOrderItem.getVenMerchantProduct() => " + venOrderItem.getVenMerchantProduct().getProductId());
                _log.debug("venOrderItem.getVenMerchantProduct().getVenMerchant() => " + venOrderItem.getVenMerchantProduct().getVenMerchant().getMerchantId());
                _log.debug("venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty() => " + venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId());
                airwayBill.setShipper(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName());
                airwayBill.setDestination(venOrderItem.getVenAddress().getVenCity() != null ? venOrderItem.getVenAddress().getVenCity().getCityName() : "");
                airwayBill.setZip(venOrderItem.getVenAddress().getPostalCode());
                airwayBill.setLogLogisticsProvider(venOrderItem.getLogLogisticService().getLogLogisticsProvider());
                airwayBill.setService(venOrderItem.getLogLogisticService().getServiceCode());
                airwayBill.setInsuranceCharge(venOrderItem.getInsuranceCost());
                airwayBill.setPricePerKg(venOrderItem.getLogisticsPricePerKg());
                airwayBill.setGiftWrapCharge(venOrderItem.getGiftWrapPrice());
                if (totalOrderItem == 0) {
                    airwayBill.setOrigin(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity() != null ? venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getVenPartyAddresses().get(0).getVenAddress().getVenCity().getCityName() : "");
                } else {
                    airwayBill.setOrigin(venOrderItem.getVenAddress().getVenCity() != null ? venOrderItem.getVenAddress().getVenCity().getCityName() : "");
                }

                BigDecimal totalCharge = new BigDecimal(0);
                if (airwayBill.getVenOrderItem() != null && airwayBill.getVenOrderItem().getShippingCost() != null) {
                    totalCharge = totalCharge.add(airwayBill.getVenOrderItem().getShippingCost());
                }
                if (airwayBill.getOtherCharge() != null) {
                    totalCharge = totalCharge.add(airwayBill.getOtherCharge());
                }
                if (airwayBill.getInsuranceCharge() != null) {
                    totalCharge = totalCharge.add(airwayBill.getInsuranceCharge());
                } else {
                    totalCharge = totalCharge.add(airwayBill.getVenOrderItem().getInsuranceCost());
                }
                if (airwayBill.getGiftWrapCharge() != null) {
                    totalCharge = totalCharge.add(airwayBill.getGiftWrapCharge());
                } else {
                    totalCharge = totalCharge.add(airwayBill.getVenOrderItem().getGiftWrapPrice());
                }
                airwayBill.setTotalCharge(totalCharge);

                // Set the default approval status' for the AWB
                LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
                logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
                airwayBill.setLogApprovalStatus1(logApprovalStatus);
                airwayBill.setLogApprovalStatus2(logApprovalStatus);
                airwayBill.setMtaData(false);

                // Persist the dummy airway bills
                _log.debug("persist the dummy airway bills");
                airwayBillHome.persistLogAirwayBill(airwayBill);
                _log.debug("done persist the dummy airway bills");
            }

            rsOrderItem.close();
            psOrderItem.close();
            conn.close();

        } catch (Exception e) {
            String errMsg = "An exception occured when processing a preMerge callback for VenOrderItemSessionEJBCallback:" + e.getMessage();
            _log.error(errMsg, e);
            e.printStackTrace();
            throw new EJBException(errMsg);
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        _log.debug("Closing connections...");
        long stop = System.currentTimeMillis();
        _log.debug("venOrderItem onPreMerge() duration " + (stop - start) + " ms");
        return Boolean.TRUE;
    }

    @Override
    public Boolean onPostMerge(Object businessObject) {
        _log.debug("onPostMerge");

        return Boolean.TRUE;
    }

    @Override
    public Boolean onPreRemove(Object businessObject) {
        _log.debug("onPreRemove");
        return Boolean.TRUE;
    }

    @Override
    public Boolean onPostRemove(Object businessObject) {
        _log.debug("onPostRemove");
        return Boolean.TRUE;
    }
}
