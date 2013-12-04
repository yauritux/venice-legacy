package com.gdn.venice.fraud.batch;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdBlacklistReason;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.persistence.FrdParameterRule31;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;
import com.gdn.venice.util.VeniceConstants;

/**
 * UpdateOrderFPBatchJob.java
 *
 * This batch job identifies the order that has non credit card payment and
 * update the order and order item status to FP automatically
 *
 * <p> <b>author: roland <p> <b>version:</b> 1.0 <p> <b>since:</b> 2012
 */
public class UpdateOrderFPBatchJob {

    private static final String VEN_CONTACT_DETAIL_ID_PHONE = "0";
    private static final String VEN_CONTACT_DETAIL_ID_MOBILE = "1";
    private static final String VEN_CONTACT_DETAIL_ID_EMAIL = "3";
    protected static Logger _log = null;

    /**
     * Default constructor.
     */
    public UpdateOrderFPBatchJob() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.fraud.batch.UpdateOrderFPBatchJob");
    }

    /**
     * Venice 116 Finds the order that has order status still C and check the
     * payment type. If the payment type is not credit card, then update the
     * order status to FP.
     *
     * @return true if the method succeeds else false
     */
    private Boolean findAndUpdateOrderStatusToFP() {
        _log.info("start updateOrderToFP");
        Long startTime = System.currentTimeMillis();
        Locator<Object> locator = null;
        List<VenOrderPaymentAllocation> orderPaymentAllocationList;
        List<VenOrderItem> originalOrderItemList;
        ArrayList<String> wcsOrderIdList = new ArrayList<String>();
        Locator<Object> historyLocator = null;

        VenOrderStatus venOrderStatusFP = new VenOrderStatus();
        venOrderStatusFP.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_FP);

        //get today's date and convert to timestamp so it can be compared to order_timestamp in ven_order
        String dateParam = null;
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateParam = dateTimestamp.format(calendar.getTime());
            Date date = (Date) dateFormat.parse(dateParam);
            Date toWeek = DateUtils.addDays(date, -10);
            dateParam = dateTimestamp.format(toWeek.getTime());
            _log.debug("date start " + dateParam);
        } catch (ParseException e) {
            e.printStackTrace();
            _log.error("error when getting date to check orders from and convert it to timestamp!");
        }

        try {
            locator = new Locator<Object>();

            VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
            VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
            VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
            VenBinCreditLimitEstimateSessionEJBRemote binCreditLimitSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");

            //Ambil order yang:
            //	- Status C (Confirmed)
            //	- Sampai order date H-10
            //  - Hanya untuk order yang ada itemnya
            String selectOrderPaymentAllocation = "select o from VenOrderPaymentAllocation o join fetch o.venOrder join fetch o.venOrder.venOrderItems"
                    + " where o.venOrder.venOrderStatus.orderStatusId = " + VeniceConstants.VEN_ORDER_STATUS_C
                    + " and o.venOrder.orderDate >= '" + dateParam + "' order by o.venOrder.wcsOrderId";

//			untuk testing
//			selectOrderPaymentAllocation = "select o from VenOrderPaymentAllocation o where o.venOrder.wcsOrderId='942288'";
            orderPaymentAllocationList = orderPaymentAllocationSessionHome.queryByRange(selectOrderPaymentAllocation, 0, 0);
            _log.debug("orderPaymentAllocationList size: " + orderPaymentAllocationList.size());

            if (orderPaymentAllocationList.size() > 0) {
                //for multiple payment, get the order id, then check the payment for each order id
                Boolean hasCCPaymentType;

                //from orderPaymentAllocationList, separate the list based on order id
                wcsOrderIdList.add(orderPaymentAllocationList.get(0).getVenOrder().getWcsOrderId());
                for (int i = 1; i < orderPaymentAllocationList.size(); i++) {
                    if (!orderPaymentAllocationList.get(i).getVenOrder().getWcsOrderId().equalsIgnoreCase(orderPaymentAllocationList.get(i - 1).getVenOrder().getWcsOrderId())) {
                        if(orderPaymentAllocationList.get(i).getVenOrder().getVenOrderItems() != null && !orderPaymentAllocationList.get(i).getVenOrder().getVenOrderItems().isEmpty()){
                            wcsOrderIdList.add(orderPaymentAllocationList.get(i).getVenOrder().getWcsOrderId());
                        } else {
                            _log.info("WcsOrderId: " + orderPaymentAllocationList.get(i).getVenOrder().getWcsOrderId() + " has no item");
                        }
                    }
                }

                //for each order id, loop the payment to check the payment type
                int tempCounter;
                String binNumber = "", eciCode;
                List<VenBinCreditLimitEstimate> binCreditLimitList;
                for (int j = 0; j < wcsOrderIdList.size(); j++) {
                    _log.info("Processing WcsOrderId: " + wcsOrderIdList.get(j));
                    hasCCPaymentType = false;
                    tempCounter = 0;
                    String creditCardNumber = "";
                    for (int k = 0; k < orderPaymentAllocationList.size(); k++) {
                        //check the payment per order id
                        if (orderPaymentAllocationList.get(k).getVenOrder().getWcsOrderId().equals(wcsOrderIdList.get(j))) {
                            tempCounter = k;
                            eciCode = orderPaymentAllocationList.get(k).getVenOrderPayment().getThreeDsSecurityLevelAuth() != null ? orderPaymentAllocationList.get(k).getVenOrderPayment().getThreeDsSecurityLevelAuth() : "";
                            _log.debug("payment type code: " + orderPaymentAllocationList.get(k).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode() + ", ECI code: " + eciCode);
                            if (orderPaymentAllocationList.get(k).getVenOrderPayment().getVenPaymentType().getPaymentTypeCode().equals(VeniceConstants.VEN_PAYMENT_TYPE_CC)) {
                                //cek eci, jika eci 5, dan bin number terdaftar, maka updata FP otomatis.								
                                if ((eciCode != null || !eciCode.equals("")) && (eciCode.equals("05") || (eciCode.equals("02")
                                        && (orderPaymentAllocationList.get(k).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriInstallment)
                                        || orderPaymentAllocationList.get(k).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriDebit))))) {

                                    //cek blacklist
                                    if (isIPAddressBlacklist(orderPaymentAllocationList.get(k).getVenOrder().getIpAddress())) {
                                        _log.info("IP blacklisted, don't update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                        hasCCPaymentType = true;
                                        break;
                                    } else if (isCustomerBlacklist(orderPaymentAllocationList.get(k).getVenOrder())) {
                                        _log.info("Customer blacklisted, don't update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                        hasCCPaymentType = true;
                                        break;
                                    } else {
                                        //ip dan customer tidak ter blacklist
                                        //cek bin number
                                        _log.info("order is not blacklisted, cek bin number, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                        creditCardNumber = orderPaymentAllocationList.get(k).getVenOrderPayment().getMaskedCreditCardNumber() != null ? orderPaymentAllocationList.get(k).getVenOrderPayment().getMaskedCreditCardNumber() : "";
                                        if (!creditCardNumber.equals("") || creditCardNumber != null) {
                                            //check CC limit from BIN number
                                            if (creditCardNumber.length() > 6) {
                                                binNumber = creditCardNumber.substring(0, 6);
                                            }

                                            binCreditLimitList = binCreditLimitSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '" + binNumber + "'", 0, 1);
                                            if (binCreditLimitList.size() > 0) {
                                                _log.info("BIN number registered, update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                            } else {
                                                hasCCPaymentType = true;
                                                _log.info("BIN number not registered, don't update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                                break;
                                            }
                                        } else {
                                            hasCCPaymentType = true;
                                            _log.info("No CC number, don't update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                            break;
                                        }
                                    }
                                } else {
                                    hasCCPaymentType = true;
                                    _log.info("ECI not 05 for MIGS or not 02 for Infinitium, don't update FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                                    break;
                                }
                            }
                        }
                    }
                    if (!hasCCPaymentType) {
                        //update order id to FP
                        _log.info("Update to FP automatically, wcsOrderId: " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getWcsOrderId());
                        VenOrder order;
                        try {
                            order = orderSessionHome.queryByRange("select o from VenOrder o where o.orderId = " + orderPaymentAllocationList.get(tempCounter).getVenOrder().getOrderId(), 0, 1).get(0);
                        } catch (IndexOutOfBoundsException e) {
                            order = new VenOrder();
                            order.setOrderId(orderPaymentAllocationList.get(tempCounter).getVenOrder().getOrderId());
                        }
                        order.setVenOrderStatus(venOrderStatusFP);
                        orderSessionHome.mergeVenOrder(order);

                        //add order status history
                        _log.debug("add order status history");
                        String username = "System";
                        historyLocator = new Locator<Object>();
                        VenOrderStatusHistorySessionEJBRemote orderHistorySessionHome = (VenOrderStatusHistorySessionEJBRemote) historyLocator
                                .lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");

                        VenOrderItemStatusHistorySessionEJBRemote orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) historyLocator
                                .lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");

                        VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
                        venOrderStatusHistoryPK.setOrderId(orderPaymentAllocationList.get(tempCounter).getVenOrder().getOrderId());
                        venOrderStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));

                        VenOrderStatusHistory orderStatusHistory = new VenOrderStatusHistory();
                        orderStatusHistory.setId(venOrderStatusHistoryPK);
                        orderStatusHistory.setStatusChangeReason("Updated by " + username);
                        orderStatusHistory.setVenOrderStatus(venOrderStatusFP);

                        orderHistorySessionHome.persistVenOrderStatusHistory(orderStatusHistory);

                        ArrayList<VenOrderItem> orderItemList = new ArrayList<VenOrderItem>();
                        String selectOrderItem = "select o from VenOrderItem o where o.venOrder.orderId=" + order.getOrderId();
                        originalOrderItemList = orderItemSessionHome.queryByRange(selectOrderItem, 0, 0);

                        //update order item to FP
                        if (originalOrderItemList.size() > 0) {
                            for (int m = 0; m < originalOrderItemList.size(); m++) {
                                VenOrderItem orderItem = originalOrderItemList.get(m);
                                orderItem.setVenOrderStatus(venOrderStatusFP);
                                orderItemList.add(orderItem);
                            }
                            orderItemSessionHome.mergeVenOrderItemList(orderItemList);

                            //add order item status history
                            _log.debug("add order item status history");
                            for (int m = 0; m < originalOrderItemList.size(); m++) {
                                VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
                                venOrderItemStatusHistoryPK.setOrderItemId(originalOrderItemList.get(m).getOrderItemId());
                                venOrderItemStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));

                                VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
                                orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
                                orderItemStatusHistory.setStatusChangeReason("Updated by " + username);
                                orderItemStatusHistory.setVenOrderStatus(venOrderStatusFP);

                                orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);
                            }
                        }

                        //check genuine list
                        FrdParameterRule31SessionEJBRemote genuineListSessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
                        List<FrdParameterRule31> genuineListList;
                        genuineListList = genuineListSessionHome.queryByRange("select o from FrdParameterRule31 o where o.email='" + order.getVenCustomer().getCustomerUserName() + "' and o.noCc='" + creditCardNumber + "'", 0, 0);

                        if (genuineListList.isEmpty()) {
                            //add to genuine list		
                            _log.info("add to genuine list");
                            FrdParameterRule31 genuineList = new FrdParameterRule31();
                            genuineList.setEmail(order.getVenCustomer().getCustomerUserName());
                            genuineList.setNoCc(creditCardNumber);

                            genuineListSessionHome.persistFrdParameterRule31(genuineList);
                        } else {
                            _log.info("genuine list already exist");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            _log.error("error when set status order automatically to FP for non CC payment!");
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
                if (historyLocator != null) {
                    historyLocator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Long endTime = System.currentTimeMillis();
        Long duration = endTime - startTime;
        _log.info("updateOrderToFP completed in:" + duration + "ms");
        return Boolean.TRUE;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UpdateOrderFPBatchJob updateOrderFPBatchJob = new UpdateOrderFPBatchJob();
        updateOrderFPBatchJob.findAndUpdateOrderStatusToFP();
    }

    public boolean isIPAddressBlacklist(String ipAddress) {
        Boolean result = false;
        Locator<Object> locator = null;
        try {
            locator = new Locator<Object>();
            FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
            List<FrdEntityBlacklist> ipBlacklistList = sessionHome.queryByRange("select o from FrdEntityBlacklist o where o.blackOrWhiteList = upper('BLACKLIST') and o.blacklistString = '" + ipAddress + "'", 0, 0);
            if (ipBlacklistList.size() > 0) {
                _log.info("ip blacklist found");
                result = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean isCustomerBlacklist(VenOrder venOrder) {
        Boolean result = false;
        Locator<Object> locator = null;
        ArrayList<String> blacklistReason = new ArrayList<String>();
        try {
            locator = new Locator<Object>();
            FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
            FrdBlacklistReasonSessionEJBRemote blacklistReasonSessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");
            VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
            VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
            VenOrderItemContactDetailSessionEJBRemote orderItemContactDetailSessionHome = (VenOrderItemContactDetailSessionEJBRemote) locator.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
            VenAddressSessionEJBRemote shippingAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
            VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

            List<FrdCustomerWhitelistBlacklist> customerNameBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.customerFullName)<>'ANONYMOUS' and upper(o.customerFullName) = '" + venOrder.getVenCustomer().getVenParty().getFullOrLegalName().toUpperCase() + "'", 0, 0);
            if (customerNameBlacklistList.size() > 0) {
                _log.info("customer name blacklist found");
                result = true;

                blacklistReason.add("Customer name blacklist");
            }

            List<FrdCustomerWhitelistBlacklist> customerAddressBlacklistList;
            List<VenOrderAddress> orderAddressBlacklistList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId =" + venOrder.getOrderId(), 0, 1);

            if (orderAddressBlacklistList.size() > 0) {
                if (orderAddressBlacklistList.get(0).getVenAddress().getStreetAddress1() != null) {
                    customerAddressBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.address) like '%" + orderAddressBlacklistList.get(0).getVenAddress().getStreetAddress1().toUpperCase() + "%'", 0, 0);
                    if (customerAddressBlacklistList.size() > 0) {
                        _log.info("customer address blacklist found");
                        result = true;

                        blacklistReason.add("Customer address blacklist");
                    }
                }
            }

            List<FrdCustomerWhitelistBlacklist> emailBlacklistList;
            List<VenOrderContactDetail> contactDetailEmailBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = " + venOrder.getOrderId() + " and o.venContactDetail.venContactDetailType.contactDetailTypeId =" + VEN_CONTACT_DETAIL_ID_EMAIL, 0, 1);
            if (contactDetailEmailBlacklistList.size() > 0) {
                if (contactDetailEmailBlacklistList.get(0).getVenContactDetail().getContactDetail() != null) {
                    emailBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where upper(o.email) like '" + contactDetailEmailBlacklistList.get(0).getVenContactDetail().getContactDetail().toUpperCase() + "'", 0, 0);
                    if (emailBlacklistList.size() > 0) {
                        _log.info("customer email blacklist found");
                        result = true;

                        blacklistReason.add("Customer email blacklist");
                    }
                }
            }

            List<FrdCustomerWhitelistBlacklist> PhoneBlacklistList;
            List<VenOrderContactDetail> contactDetailPhoneBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = " + venOrder.getOrderId() + " and (o.venContactDetail.venContactDetailType.contactDetailTypeId =" + VEN_CONTACT_DETAIL_ID_PHONE + " or o.venContactDetail.venContactDetailType.contactDetailTypeId =" + VEN_CONTACT_DETAIL_ID_MOBILE + ")", 0, 0);
            if (contactDetailPhoneBlacklistList.size() > 0) {
                for (int i = 0; i < contactDetailPhoneBlacklistList.size(); i++) {
                    PhoneBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.phoneNumber like '" + contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail() + "' or o.handphoneNumber like '" + contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail() + "'", 0, 0);
                    if (PhoneBlacklistList.size() > 0) {
                        _log.info("customer phone blacklist found");
                        result = true;

                        blacklistReason.add("Customer phone blacklist");
                    }
                }
            }

            /**
             * cek blacklist shipping phone and hancphone
             */
            List<FrdCustomerWhitelistBlacklist> shippingPhoneBlacklistList;
            List<VenOrderItemContactDetail> shippingContactDetailPhoneBlacklistList = orderItemContactDetailSessionHome.queryByRange("select o from VenOrderItemContactDetail o where o.venOrderItem.venOrder.orderId = " + venOrder.getOrderId() + " and (o.venContactDetail.venContactDetailType.contactDetailTypeId =" + VEN_CONTACT_DETAIL_ID_PHONE + " or o.venContactDetail.venContactDetailType.contactDetailTypeId =" + VEN_CONTACT_DETAIL_ID_MOBILE + ")", 0, 0);
            if (shippingContactDetailPhoneBlacklistList.size() > 0) {
                for (int i = 0; i < shippingContactDetailPhoneBlacklistList.size(); i++) {
                    shippingPhoneBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.shippingPhoneNumber like '" + shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail() + "' or o.shippingHandphoneNumber like '" + shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail() + "'", 0, 0);
                    if (shippingPhoneBlacklistList.size() > 0) {
                        _log.info("Shipping phone blacklist found");
                        result = true;

                        blacklistReason.add("Shipping phone blacklist");
                    }
                }
            }

            /**
             * cek blacklist shipping address
             */
            List<FrdCustomerWhitelistBlacklist> shippingAddress;
            List<VenAddress> shippingAddressBlacklistList = shippingAddressSessionHome.queryByRange("select o from VenAddress o where o.addressId in  (select u.venAddress.addressId from VenOrderItem u where u.venOrder.orderId=" + venOrder.getOrderId() + ")", 0, 1);
            if (shippingAddressBlacklistList.size() > 0) {
                for (int i = 0; i < shippingAddressBlacklistList.size(); i++) {
                    shippingAddress = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.shippingAddress like '" + (shippingAddressBlacklistList.get(i).getStreetAddress1() == null ? "" : shippingAddressBlacklistList.get(i).getStreetAddress1()) + "'", 0, 0);
                    if (shippingAddress.size() > 0) {
                        _log.info("Shipping Address blacklist found");
                        result = true;

                        blacklistReason.add("Shipping Address blacklist");
                    }
                }
            }

            /**
             * cek blacklist credit card
             */
            List<FrdCustomerWhitelistBlacklist> allocationBlacklistList;
            List<VenOrderPaymentAllocation> ccBlacklistList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + venOrder.getOrderId(), 0, 0);
            if (ccBlacklistList.size() > 0) {
                for (int i = 0; i < ccBlacklistList.size(); i++) {
                    if (ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber() != null) {
                        allocationBlacklistList = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.ccNumber like '" + ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber() + "' ", 0, 0);
                        if (allocationBlacklistList.size() > 0) {
                            _log.info("Credit Card blacklist found");
                            result = true;

                            blacklistReason.add("Credit Card blacklist");
                        }
                    }
                }
            }

            //insert blacklist reason if it is blacklisted
            if (result == true) {
                FrdBlacklistReason reason = new FrdBlacklistReason();
                reason.setOrderId(venOrder.getOrderId());
                reason.setWcsOrderId(venOrder.getWcsOrderId());
                for (int i = 0; i < blacklistReason.size(); i++) {
                    reason.setBlacklistReason(blacklistReason.get(i));
                    blacklistReasonSessionHome.persistFrdBlacklistReason(reason);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
