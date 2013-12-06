package com.gdn.venice.finance.batch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJBException;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.gdn.venice.persistence.VenSettlementRecord;
import com.gdn.venice.util.VeniceConstants;

public class SalesRecordCleanupGeneratorBatchJob {

    protected static Logger _log = null;
    protected static final String AIRWAYBILL_ENGINE_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/airwaybill-engine.properties";
    
    BigDecimal marginFee = new BigDecimal(0);
    BigDecimal transFee = new BigDecimal(0);

    private SalesRecordCleanupGeneratorBatchJob() {
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.batch.SalesRecordGeneratorBatchJob");
    }
    
    private  Properties getAirwayBillEngineProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(AIRWAYBILL_ENGINE_PROPERTIES_FILE));
		} catch (Exception e) {
			_log.error("Error getting airwaybill-engine.properties", e);
			e.printStackTrace();
			return null;
		}
		return properties;
	}

    public Timestamp getCxMtaDate(Locator<Object> locator, long orderItemId) {
        //get cx from mta date (none, then return null)
        _log.info("check cx mta date exist");
        VenOrderItemStatusHistorySessionEJBRemote venOrderItemStatusHistorySessionHome;
        try {
            venOrderItemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
                    .lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");

            String query = "Select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId=" + orderItemId
                    + " and o.venOrderStatus.orderStatusId=" + VeniceConstants.VEN_ORDER_STATUS_CX + " and o.statusChangeReason like '%CX MTA%'";

            List<VenOrderItemStatusHistory> cxMtaHistoryList = venOrderItemStatusHistorySessionHome.queryByRange(query, 0, 1);
            if (cxMtaHistoryList.size() > 0) {
                _log.debug("cx mta date found, add CX MTA date in sales record");
                return new Timestamp(cxMtaHistoryList.get(0).getId().getHistoryTimestamp().getTime());
            }
            return null;
        } catch (Exception e) {
            String errMsg = "An exception occured when calculating values for the sales record:" + e.getMessage();
            _log.error(errMsg);
            e.printStackTrace();
            throw new EJBException(errMsg);
        }
    }

    public Timestamp getCxFinanceDate(Locator<Object> locator, long orderItemId) {
        //get cx from mta date (none, then return null)
        _log.info("check cx mta date exist");
        VenOrderItemStatusHistorySessionEJBRemote venOrderItemStatusHistorySessionHome;
        try {
            venOrderItemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
                    .lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");

            String query = "Select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId=" + orderItemId
                    + " and o.venOrderStatus.orderStatusId=" + VeniceConstants.VEN_ORDER_STATUS_CX + " and o.statusChangeReason like '%CX Finance%'";

            List<VenOrderItemStatusHistory> cxFinHistoryList = venOrderItemStatusHistorySessionHome.queryByRange(query, 0, 1);
            if (cxFinHistoryList.size() > 0) {
                _log.debug("cx mta date found, add CX Finance date in sales record");
                return new Timestamp(cxFinHistoryList.get(0).getId().getHistoryTimestamp().getTime());
            }
            return null;
        } catch (Exception e) {
            String errMsg = "An exception occured when calculating values for the sales record:" + e.getMessage();
            _log.error(errMsg);
            e.printStackTrace();
            throw new EJBException(errMsg);
        }
    }

    private FinSalesRecord createOrUpdateSalesRecord(Locator<Object> locator, VenOrderItem venOrderItem) throws Exception {

        FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator
                .lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");

        FinSalesRecord finSalesRecord = new FinSalesRecord();

        try {
            finSalesRecord.setVenOrderItem(venOrderItem);

            // Calculate the sales record
            _log.info("calculate sales record for order item id: " + venOrderItem.getWcsOrderItemId());
            finSalesRecord = this.calculateSalesRecord(locator, finSalesRecord, venOrderItem);
            
            if(finSalesRecord==null){
            	_log.error("calculate sales record skipped, because can not get margin fee or transfee from MTA service");
            	return null;
            }
            
            FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
            finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
            finSalesRecord.setFinApprovalStatus(finApprovalStatus);

            /*
             * If the sales record for the order item on the AWB does not
             * exist then create it. If the sales record exists already then update it
             */
            List<FinSalesRecord> finSalesRecordList = salesRecordHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
            if (finSalesRecordList.isEmpty()) {
                _log.debug("create new sales record");
                finSalesRecord.setSalesTimestamp(new Timestamp(System.currentTimeMillis()));

                finSalesRecord.setCxMtaDate(getCxMtaDate(locator, venOrderItem.getOrderItemId()));

                finSalesRecord.setCxFinanceDate(getCxFinanceDate(locator, venOrderItem.getOrderItemId()));

                finSalesRecord = salesRecordHome.persistFinSalesRecord(finSalesRecord);
                _log.debug("finSalesRecord id => " + finSalesRecord.getSalesRecordId());
            } else {
                FinSalesRecord existingFinSalesRecord = finSalesRecordList.get(0);
                /*
                 * To ensure that the sales record is only updated to approved status once
                 */
                if (existingFinSalesRecord.getFinApprovalStatus().getApprovalStatusId() == VeniceConstants.FIN_APPROVAL_STATUS_APPROVED) {
                    _log.info("existing sales record already approved");
                    return null;
                }
                _log.debug("update existing sales record");
                existingFinSalesRecord.setCustomerDownpayment(finSalesRecord.getCustomerDownpayment());
                existingFinSalesRecord.setFinApprovalStatus(finApprovalStatus);
                existingFinSalesRecord.setGdnCommissionAmount(finSalesRecord.getGdnCommissionAmount());
                existingFinSalesRecord.setGdnGiftWrapChargeAmount(finSalesRecord.getGdnGiftWrapChargeAmount());
                existingFinSalesRecord.setGdnHandlingFeeAmount(finSalesRecord.getGdnHandlingFeeAmount());
                existingFinSalesRecord.setGdnPromotionAmount(finSalesRecord.getGdnPromotionAmount());
                existingFinSalesRecord.setGdnTransactionFeeAmount(finSalesRecord.getGdnTransactionFeeAmount());
                existingFinSalesRecord.setMerchantPaymentAmount(finSalesRecord.getMerchantPaymentAmount());
                existingFinSalesRecord.setMerchantPromotionAmount(finSalesRecord.getMerchantPromotionAmount());
                existingFinSalesRecord.setThirdPartyPromotionAmount(finSalesRecord.getThirdPartyPromotionAmount());
                existingFinSalesRecord.setTotalLogisticsRelatedAmount(finSalesRecord.getTotalLogisticsRelatedAmount());
                existingFinSalesRecord.setVatAmount(finSalesRecord.getVatAmount());
                finSalesRecord = salesRecordHome.mergeFinSalesRecord(existingFinSalesRecord);
                _log.debug("finSalesRecord id => " + finSalesRecord.getSalesRecordId());
            }

            /*
             * Set the sales record immediately to approved and merge it
             * This will trigger the sales journal creation
             */
            _log.info("Set status to approved and merge to trigger the sales journal creation");
            finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
            finSalesRecord.setFinApprovalStatus(finApprovalStatus);
            return salesRecordHome.mergeFinSalesRecord(finSalesRecord);
        } catch(Exception e){
            e.printStackTrace();
            String errMsg = "An exception occured when create Or Update SalesRecord:" + e.getMessage();
            _log.error(errMsg);
            return null;
        }
    }

    /**
     * calculateSalesRecord - Calculates and journals the sales record for the
     * order item on the airway bill
     *
     * @param finSalesRecord is the existing sales record
     * @param logAirwayBill is the airway bill to use for the calculations
     * @return the completed calculated sales record
     */
    private FinSalesRecord calculateSalesRecord(Locator<Object> locator,
            FinSalesRecord finSalesRecord, VenOrderItem orderItem) {
        _log.info("Start calculate sales record");
        try {
            VenOrderItemSessionEJBRemote orderItemHome = (VenOrderItemSessionEJBRemote) locator
                    .lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");

            VenOrderItemAdjustmentSessionEJBRemote orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBRemote) locator
                    .lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");

            VenSettlementRecordSessionEJBRemote settlementRecordHome = (VenSettlementRecordSessionEJBRemote) locator
                    .lookup(VenSettlementRecordSessionEJBRemote.class, "VenSettlementRecordSessionEJBBean");

            /*
             * Calculate the downpayment amount (payment must have been made
             * already or the item would not have gone to logistics)
             * 
             * Calculate as the catalog price of the item multiplied by the 
             * quantity plus the shipping (shipping cost plus insurance) plus the
             * handling fee for the payment (only for the first order item to go
             * to CX status) minus any adjustment.
             */
            List<VenOrderItem> itemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.orderItemId = " + orderItem.getOrderItemId(), 0, 0);
            VenOrderItem venOrderItem = itemList.get(0);
            _log.debug("Set customer downpayment");
            BigDecimal customerDownpaymentAmount = venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()));
            customerDownpaymentAmount.setScale(2, RoundingMode.HALF_UP);
            customerDownpaymentAmount = customerDownpaymentAmount.add(venOrderItem.getShippingCost()).add(venOrderItem.getInsuranceCost().add(venOrderItem.getGiftWrapPrice()));

            /*
             * If it is the first order item to go CX then add 
             * the handling fees to the downpayment amount
             * JASA HANDLING
             */
            if (this.isHandlingFeeExist(locator, venOrderItem) == false) {
                _log.debug("handling fee not exist, set handling fee");
                customerDownpaymentAmount = customerDownpaymentAmount.add(this.getHandlingFeesFromOrderPayments(locator, venOrderItem));
            }

            List<VenOrderItemAdjustment> adjustmentList = orderItemAdjustmentHome.queryByRange("select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);

            /*
             *  Get any marginPromo and add them to the
             *  customer downpayment amount (note
             *  that they are -ve numbers)
             */
            _log.debug("Get marginPromo");
            BigDecimal marginPromo = new BigDecimal(0);
            marginPromo.setScale(2, RoundingMode.HALF_UP);

            for (VenOrderItemAdjustment adjustment : adjustmentList) {
                marginPromo = marginPromo.add(adjustment.getAmount());
            }

            customerDownpaymentAmount = customerDownpaymentAmount.add(marginPromo);

            /*
             * Set the downpayment amount in the sales record
             */
            finSalesRecord.setCustomerDownpayment(customerDownpaymentAmount);

            /*
             * Get the commission amount from the settlement record
             *  - to be subtracted from merchant payment after tax taken out
             */
            _log.debug("Check settlement record");
            List<VenSettlementRecord> settlementRecordList = settlementRecordHome
                    .queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);

            if (settlementRecordList == null || settlementRecordList.isEmpty()) {
                throw new EJBException("No settlement record for order item " + venOrderItem.getOrderItemId() + " was found. Sales record cannot be calculated!. Ops ignore this message");
            }

            VenSettlementRecord venSettlementRecord = settlementRecordList.get(0);

            /*
             * This is the divisor to use when calulating the PPN amount
             */
            _log.debug("Set ppn divisor");
            BigDecimal gdnPPN_Divisor = new BigDecimal(VeniceConstants.VEN_GDN_PPN_RATE).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).add(new BigDecimal(1));

            /*
             * This is the commission amount before the PPN is taken out
             */
            _log.debug("Set commission");
            BigDecimal gdnCommissionAmountBeforeTax = venSettlementRecord.getCommissionValue();
            gdnCommissionAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
            
            if(venSettlementRecord.getCommissionValue()==null || venOrderItem.getTransactionFeeAmount() == null){	
            	Boolean result = getMarginFeeAndTransactionFee(venOrderItem.getWcsOrderItemId());
            	if(result==false){
            		_log.error("can not get margin fee or trans fee from MTA service");
            		return null;
            	}
            }
            
            /*
             * cek commission value, jika null maka request ke MTA
             */
            if(venSettlementRecord.getCommissionValue()==null){
            	 _log.debug("Commission value null, set value from MTA");
            	 gdnCommissionAmountBeforeTax = marginFee;
            }else{            
            	_log.debug("Commission value not null, set from settlement record");
            	gdnCommissionAmountBeforeTax = venSettlementRecord.getCommissionValue();            
            }

            /*
             * This is the commission amount after the PPN is taken out
             */
            BigDecimal gdnCommissionAmountAfterTax = new BigDecimal(0);
            gdnCommissionAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
            if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
                gdnCommissionAmountAfterTax = gdnCommissionAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
            }

            /*
             * This is the amount of PPN incurred by the commission
             */
            BigDecimal gdnCommissionAmountPPN = gdnCommissionAmountBeforeTax.subtract(gdnCommissionAmountAfterTax);

            finSalesRecord.setGdnCommissionAmount(gdnCommissionAmountBeforeTax);

            /*
             * This is the handling fee for the payment from the order
             * before PPN is taken out.
             * 
             * Note that handling fees are only included for the 
             * first order item to go to CX status (i.e. have a sales record).
             */
            _log.debug("Set handling fee");
            BigDecimal gdnHandlingFeeAmountBeforeTax = new BigDecimal(0);
            gdnHandlingFeeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);

            /*
             * This is the handling fee after PPN is taken out
             */
            BigDecimal gdnHandlingFeeAmountAfterTax = new BigDecimal(0);
            gdnHandlingFeeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);

            /*
             * This is the amount of PPN incurred by the handling fees
             */
            BigDecimal gdnHandlingFeePPN = new BigDecimal(0);
            gdnHandlingFeePPN.setScale(2, RoundingMode.HALF_UP);

            Boolean flagHandlingFeeExist = isHandlingFeeExist(locator, venOrderItem);
            _log.debug("Check handling fee exist");
            if (flagHandlingFeeExist == false) {
                _log.debug("handling fee not exist, set handling fee");
                gdnHandlingFeeAmountBeforeTax = getHandlingFeesFromOrderPayments(locator, venOrderItem);

                if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
                    gdnHandlingFeeAmountAfterTax = gdnHandlingFeeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
                }

                gdnHandlingFeePPN = gdnHandlingFeeAmountBeforeTax.subtract(gdnHandlingFeeAmountAfterTax);
            }

            _log.debug("gdnHandlingFeeAmountBeforeTax: " + gdnHandlingFeeAmountBeforeTax);
            finSalesRecord.setGdnHandlingFeeAmount(gdnHandlingFeeAmountBeforeTax);

            BigDecimal gdnTransactionFeeAmountBeforeTax = new BigDecimal(0);
            gdnTransactionFeeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);

            BigDecimal gdnTransactionFeeAmountPPN = new BigDecimal(0);

            /*
             * cek merchant type
             */
            _log.debug("[sales record generator] cek merchant type");
            if (venSettlementRecord.getCommissionType().equals("CM") || venSettlementRecord.getCommissionType().equals("RB")) {
                _log.debug("[sales record generator] merchant type is CM or RB: " + venSettlementRecord.getCommissionType());

                /*
                 * These are the transaction fees which are per order and per merchant (not per order item). 
                 */
                _log.debug("[sales record generator] Set transaction fee");
                
                /*
                 * Cek trans fee, jika null maka request ke MTA
                 */
                if (venOrderItem.getTransactionFeeAmount() == null) {
                    _log.debug("[sales record generator] trans fee is null, set value from MTA");
                    gdnTransactionFeeAmountBeforeTax = transFee;        
                } else {
                    _log.debug("[sales record generator] trans fee not null , set from order item");
                    gdnTransactionFeeAmountBeforeTax = venOrderItem.getTransactionFeeAmount();
                }
                
                BigDecimal gdnTransactionFeeAmountAfterTax = new BigDecimal(0);
                gdnTransactionFeeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);

                if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
                    gdnTransactionFeeAmountAfterTax = gdnTransactionFeeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
                }
                gdnTransactionFeeAmountPPN = gdnTransactionFeeAmountBeforeTax.subtract(gdnTransactionFeeAmountAfterTax);

                finSalesRecord.setGdnTransactionFeeAmount(gdnTransactionFeeAmountBeforeTax);
            } else {
                _log.debug("[sales record generator] merchant type is Not CM or RB: " + venSettlementRecord.getCommissionType());
                _log.debug("[sales record generator] set transaction fee to 0");
                finSalesRecord.setGdnTransactionFeeAmount(new BigDecimal(0));
            }

            /*
             * This is the amount of the promotion that is to be borne by the
             * merchant
             */
            _log.debug("Set merchant promotion");
            BigDecimal merchantPromotionAmount = getMerchantPromotionAmount(locator, venOrderItem, adjustmentList);

            merchantPromotionAmount.setScale(2, RoundingMode.HALF_UP);
            finSalesRecord.setMerchantPromotionAmount(merchantPromotionAmount);

            /*
             * This is the promotion amount that the 3rd party is accountable
             * for.
             */
            _log.debug("Set 3rd party promotion");
            BigDecimal thirdPartyPromotionAmount = getThirdPartyPromotionAmount(locator, venOrderItem, adjustmentList);

            thirdPartyPromotionAmount.setScale(2, RoundingMode.HALF_UP);
            finSalesRecord.setThirdPartyPromotionAmount(thirdPartyPromotionAmount);

            /*
             * Calculate the GDN amount for the promotion
             * by subtracting the 3rd party promotion amounts
             * from the marginPromo
             */
            _log.debug("Set gdn promotion amount");
            BigDecimal gdnPromotionAmount = new BigDecimal(0);
            if (marginPromo.compareTo(new BigDecimal(0)) > 0) {
                gdnPromotionAmount = marginPromo.subtract(merchantPromotionAmount).subtract(thirdPartyPromotionAmount);
            }

            gdnPromotionAmount.setScale(2, RoundingMode.HALF_UP);
            finSalesRecord.setGdnPromotionAmount(gdnPromotionAmount);

            /*
             * Calculate the total payment amount that is 
             * due to the merchant for the order item.
             */
            _log.debug("Set merchant payment amount");
            BigDecimal merchantPaymentAmount = new BigDecimal(0);
            merchantPaymentAmount.setScale(2, RoundingMode.HALF_UP);

            //if rebate, amount=(item_price * qty)
            //if commission, amount=(item_price * qty) - merchantPromo - gdnCommission (with tax taken out) - transactionFee (with tax taken out)
            if (venSettlementRecord.getCommissionType().equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_REBATE)) {
                _log.debug("commission type rebate");
                merchantPaymentAmount = venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()));
            } else if (venSettlementRecord.getCommissionType().equals(VeniceConstants.VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_COMMISSION)) {
                _log.debug("commission type commission");
                merchantPaymentAmount = (venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()))).subtract(gdnCommissionAmountBeforeTax).subtract(gdnTransactionFeeAmountBeforeTax);
            }

            //if promo merchant, subtract from total amount
            if (merchantPaymentAmount.compareTo(new BigDecimal(0)) > 0 && merchantPromotionAmount.compareTo(new BigDecimal(0)) > 0) {
                _log.debug("promo merchant, subtract from promo amount total amount");
                merchantPaymentAmount = merchantPaymentAmount.subtract(merchantPromotionAmount);
            }

            if (merchantPaymentAmount.compareTo(new BigDecimal(0)) > 0 && (gdnPromotionAmount.compareTo(new BigDecimal(0)) > 0 || thirdPartyPromotionAmount.compareTo(new BigDecimal(0)) > 0)) {
                _log.debug("promo gdn or third party, set amount like commission");
                merchantPaymentAmount = (venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()))).subtract(gdnCommissionAmountBeforeTax).subtract(gdnTransactionFeeAmountBeforeTax);
            }

            finSalesRecord.setMerchantPaymentAmount(merchantPaymentAmount);

            /*
             * Calculate the total of the logistics charges and PPN for the order item
             */
            _log.debug("Set logistic charge");
            BigDecimal totalLogisticsRelatedAmountBeforeTax = venOrderItem.getShippingCost().add(venOrderItem.getInsuranceCost());
            totalLogisticsRelatedAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);

            BigDecimal totalLogisticsRelatedAmountAfterTax = new BigDecimal(0);

            if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
                totalLogisticsRelatedAmountAfterTax = totalLogisticsRelatedAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
            }

            totalLogisticsRelatedAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalLogisticsRelatedAmountPPN = totalLogisticsRelatedAmountBeforeTax.subtract(totalLogisticsRelatedAmountAfterTax);

            finSalesRecord.setTotalLogisticsRelatedAmount(totalLogisticsRelatedAmountAfterTax);

            /*
             * Calculate the gift wrap PPN and assign the amounts
             */
            _log.debug("Set gift wrap amount");
            BigDecimal gdnGiftWrapChargeAmountBeforeTax = venOrderItem.getGiftWrapPrice();
            gdnGiftWrapChargeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);

            BigDecimal gdnGiftWrapChargeAmountAfterTax = new BigDecimal(0);

            if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
                gdnGiftWrapChargeAmountAfterTax = gdnGiftWrapChargeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
            }

            gdnGiftWrapChargeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
            BigDecimal gdnGiftWrapChargeAmountPPN = gdnGiftWrapChargeAmountBeforeTax.subtract(gdnGiftWrapChargeAmountAfterTax);

            finSalesRecord.setGdnGiftWrapChargeAmount(gdnGiftWrapChargeAmountBeforeTax);

            /*
             * Calculates the PPN amount for the sale
             * by adding the various PPN amounts
             * 
             */
            _log.debug("Set ppn amount");
            BigDecimal vatAmount = new BigDecimal(0);

            vatAmount = totalLogisticsRelatedAmountPPN.add(gdnCommissionAmountPPN).add(gdnTransactionFeeAmountPPN).add(gdnHandlingFeePPN).add(gdnGiftWrapChargeAmountPPN);

            vatAmount.setScale(2, RoundingMode.HALF_UP);
            finSalesRecord.setVatAmount(vatAmount);

            /*
             * Calculates the PPH 23		
             * (Commission + Trans Fee)*2%	 
             */
            _log.debug("Set pph 23 amount");
            BigDecimal pph23Amount = new BigDecimal(0);

            boolean isPPh23 = (venOrderItem.getVenSettlementRecords() != null && venOrderItem.getVenSettlementRecords().get(0).getPph23() != null) ? venOrderItem.getVenSettlementRecords().get(0).getPph23() : false;
            if (isPPh23) {
                pph23Amount = (finSalesRecord.getGdnCommissionAmount().add(finSalesRecord.getGdnTransactionFeeAmount())).multiply(new BigDecimal(0.02));
            }
            _log.debug("pph 23 amount: " + pph23Amount);
            finSalesRecord.setPph23Amount(pph23Amount);

            _log.info("Done calculate sales record");
        } catch (Exception e) {
            String errMsg = "An exception occured when calculating values for the sales record:" + e.getMessage();
            _log.error(errMsg);
            e.printStackTrace();
            return null;
        }
        return finSalesRecord;
    }

    /**
     * Calculates the third party promotion amount for the order item
     *
     * @param locator is a locator to use for EJB lookup
     * @param venOrderItem is the order item in question
     * @param adjustmentList is a list of the marginPromo
     * @return the third party promotion amount
     */
    private BigDecimal getThirdPartyPromotionAmount(Locator<Object> locator, VenOrderItem venOrderItem, List<VenOrderItemAdjustment> adjustmentList) {
        BigDecimal thirdPartyPromotionAmount = new BigDecimal(0);

        try {
            VenPartyPromotionShareSessionEJBRemote partyPromotionShareHome = (VenPartyPromotionShareSessionEJBRemote) locator
                    .lookup(VenPartyPromotionShareSessionEJBRemote.class, "VenPartyPromotionShareSessionEJBBean");

            for (VenOrderItemAdjustment adjustment : adjustmentList) {
                List<VenPartyPromotionShare> thirdPartyPromotionShareList = partyPromotionShareHome
                        .queryByRange("select o from VenPartyPromotionShare o where o.venPromotion.promotionId = " + adjustment.getVenPromotion().getPromotionId(), 0, 1);

                /*
                 * If flat then calculate flat amount else calculate
                 * percentage based
                 */
                if (thirdPartyPromotionShareList.size() > 0) {
                    //cari yang party nya beda dengan party di order item
                    if (!thirdPartyPromotionShareList.get(0).getVenParty().getFullOrLegalName().equalsIgnoreCase(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName())) {
                        if (thirdPartyPromotionShareList.get(0).getVenPromotionShareCalcMethod().getPromotionCalcMethodId().equals(VeniceConstants.VEN_PROMOTION_SHARE_CALC_METHOD_FLAT)) {
                            thirdPartyPromotionAmount = thirdPartyPromotionAmount.add(new BigDecimal(thirdPartyPromotionShareList.get(0).getPromotionCalcValue()));
                        } else {
                            BigDecimal percentageFraction = new BigDecimal(thirdPartyPromotionShareList.get(0).getPromotionCalcValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            thirdPartyPromotionAmount = thirdPartyPromotionAmount.add(adjustment.getAmount().multiply(percentageFraction));
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errorText = e.getMessage();
            _log.error(errorText);
            e.printStackTrace();
            throw new EJBException(errorText);
        }
        return thirdPartyPromotionAmount;
    }

    /**
     * Returns the merchant promotion amount for the order item
     *
     * @param locator is a locator to use for EJB lookup
     * @param venOrderItem is the order item in question
     * @param adjustmentList is the list of marginPromo
     * @return the merchant promotion amount
     */
    private BigDecimal getMerchantPromotionAmount(Locator<Object> locator, VenOrderItem venOrderItem, List<VenOrderItemAdjustment> adjustmentList) {
        BigDecimal merchantPromotionAmount = new BigDecimal(0);

        try {
            VenPartyPromotionShareSessionEJBRemote partyPromotionShareHome = (VenPartyPromotionShareSessionEJBRemote) locator
                    .lookup(VenPartyPromotionShareSessionEJBRemote.class, "VenPartyPromotionShareSessionEJBBean");

            for (VenOrderItemAdjustment adjustment : adjustmentList) {
                List<VenPartyPromotionShare> merchantPartyPromotionShareList = partyPromotionShareHome
                        .queryByRange("select o from VenPartyPromotionShare o where o.venPromotion.promotionId = " + adjustment.getVenPromotion().getPromotionId(), 0, 1);

                /*
                 * If flat then calculate flat amount else calculate
                 * percentage based
                 */
                if (merchantPartyPromotionShareList.size() > 0) {
                    //cari yang party nya sama dengan party di order item
                    if (merchantPartyPromotionShareList.get(0).getVenParty().getFullOrLegalName().equalsIgnoreCase(venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName())) {
                        if (merchantPartyPromotionShareList.get(0).getVenPromotionShareCalcMethod().getPromotionCalcMethodId().equals(VeniceConstants.VEN_PROMOTION_SHARE_CALC_METHOD_FLAT)) {
                            merchantPromotionAmount = merchantPromotionAmount.add(new BigDecimal(merchantPartyPromotionShareList.get(0).getPromotionCalcValue()));
                        } else {
                            BigDecimal percentageFraction = new BigDecimal(merchantPartyPromotionShareList.get(0).getPromotionCalcValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            merchantPromotionAmount = merchantPromotionAmount.add(adjustment.getAmount().multiply(percentageFraction));
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errorText = e.getMessage();
            _log.error(errorText);
            e.printStackTrace();
            throw new EJBException(errorText);
        }
        merchantPromotionAmount.setScale(2, RoundingMode.HALF_UP);
        return merchantPromotionAmount;
    }

    /**
     * Returns the sum of all the handling fees for payments attached to the
     * order
     *
     * @param locator a locator for the EJBs
     * @param venOrderItem the order item in question
     * @return the handling fees
     */
    private BigDecimal getHandlingFeesFromOrderPayments(Locator<Object> locator, VenOrderItem venOrderItem) {
        BigDecimal handlingFee = new BigDecimal(0);
        handlingFee.setScale(2, RoundingMode.HALF_UP);

        try {
            VenOrderPaymentAllocationSessionEJBRemote venOrderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
                    .lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

            List<VenOrderPaymentAllocation> venOrderPaymentAllocationList =
                    venOrderPaymentAllocationHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + venOrderItem.getVenOrder().getOrderId(), 0, 0);

            if (!venOrderPaymentAllocationList.isEmpty()) {
                _log.debug("handling fee found from payment: " + venOrderPaymentAllocationList.get(0).getVenOrderPayment().getHandlingFee());
                handlingFee = handlingFee.add(venOrderPaymentAllocationList.get(0).getVenOrderPayment().getHandlingFee());
            }
        } catch (Exception e) {
            String errorText = e.getMessage();
            _log.error(errorText);
            e.printStackTrace();
            throw new EJBException(errorText);
        }

        return handlingFee;
    }

    /**
     * Returns true if the order item is the first order item for the order
     * created in the Sales record
     *
     * @param locator is a Locator object to locate the EJB
     * @param venOrderItem is the order item in question
     * @return true if the item is the first else false
     */
    private boolean isHandlingFeeExist(Locator<Object> locator, VenOrderItem venOrderItem) {
        boolean flagHandlingFeeExist = false;
        try {
            FinSalesRecordSessionEJBRemote finSalesRecordHome = (FinSalesRecordSessionEJBRemote) locator
                    .lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");

            List<FinSalesRecord> finSalesRecordList = finSalesRecordHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.venOrder.orderId = " + venOrderItem.getVenOrder().getOrderId(), 0, 0);

            for (FinSalesRecord finSalesRecord : finSalesRecordList) {
                if (finSalesRecord.getGdnHandlingFeeAmount().compareTo(new BigDecimal(0)) == 1) {
                    flagHandlingFeeExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            String errorText = e.getMessage();
            _log.error(errorText);
            e.printStackTrace();
            throw new EJBException(errorText);
        }

        return flagHandlingFeeExist;
    }
    
    private Boolean getMarginFeeAndTransactionFee(String wcsOrderItemId) {
        _log.debug("start getMarginFeeAndTransactionFee from MTA");        
  	  
        marginFee = new BigDecimal(0);
		marginFee.setScale(2, RoundingMode.HALF_UP);
		
        transFee = new BigDecimal(0);
		transFee.setScale(2, RoundingMode.HALF_UP);
        
        try {       
    		URL obj = new URL(getAirwayBillEngineProperties().getProperty("mtaAddress")+"MtaHttpServices?serviceType=MarginTransactionFeeRequest&orderItemId="+wcsOrderItemId);
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		con.setRequestMethod("GET");
        	
    		con.setRequestProperty("User-Agent",  "GdnWS/1.0");
        	
    		if (con.getResponseCode() == HttpStatus.SC_OK) {
    		
        		_log.info("service call to MTA service success, response status: "+ con.getResponseCode());
        		
        		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        		String inputLine;
        		StringBuffer response = new StringBuffer();
         
        		while ((inputLine = in.readLine()) != null) {
        			response.append(inputLine);
        		}
        		in.close();

        		if(response!=null){	            	
	            	_log.info("result from MTA: "+response.toString());
	            	
	            	JSONObject jsonObject = new JSONObject(response.toString());
	            	
	            	if(jsonObject.getBoolean("success")){	       
	            		_log.debug("data is available from MTA");
	            			            		
	            		marginFee=new BigDecimal(jsonObject.getDouble("marginFee"));

	            		_log.debug("marginFee from MTA: "+marginFee);

		            	transFee=new BigDecimal(jsonObject.getDouble("transxFee"));
	            		_log.debug("transFee from MTA: "+transFee);
	            	}else{
	            		_log.debug("data is not available from MTA");
	            		return false;
	            	}
	            }	            
			}else{
				_log.error("service call to MTA service failed, response status: "+ con.getResponseCode());
				return false;
			}
        } catch (IOException e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
          
          return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SalesRecordCleanupGeneratorBatchJob salesRecordJob = new SalesRecordCleanupGeneratorBatchJob();
        _log.info("Prepare to create sales record");

        Locator<Object> locator = null;
        FinSalesRecordSessionEJBRemote finSalesRecordHome;
        VenOrderItemSessionEJBRemote itemHome;
        VenOrderItemStatusHistorySessionEJBRemote itemHistoryHome;
        FinSalesRecord salesRecord = null;

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {
            Long startTime = System.currentTimeMillis();
            int count = 0, errorCount = 0;
            locator = new Locator<Object>();
            finSalesRecordHome = (FinSalesRecordSessionEJBRemote) locator
                    .lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
            itemHome = (VenOrderItemSessionEJBRemote) locator
                    .lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
            itemHistoryHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
                    .lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
            String query = "select o from VenOrderItemStatusHistory o "
                    + " where o.venOrderItem.salesBatchStatus in ( '" + VeniceConstants.FIN_SALES_BATCH_STATUS_READY + "', '"
                    + "Failed" + "')"
                    + " and o.venOrderStatus.orderStatusCode = 'CX'"
                    + " and o.statusChangeReason = 'Updated by System' ";
            _log.info("Query: " + query);
            List<VenOrderItemStatusHistory> itemsHistory = itemHistoryHome.queryByRange(query, 0, 0);
            
            _log.info("Query returns: " + itemsHistory.size() + " row(s)");
            
            VenOrderItem item;
            for (VenOrderItemStatusHistory venOrderItemStatusHistory : itemsHistory) {
                item = venOrderItemStatusHistory.getVenOrderItem();
                item.setSalesBatchStatus(VeniceConstants.FIN_SALES_BATCH_STATUS_IN_PROCESS);
                venOrderItemStatusHistory.setVenOrderItem(itemHome.mergeVenOrderItem(item));
            }

            for (VenOrderItemStatusHistory venOrderItemStatusHistory : itemsHistory) {
                item = venOrderItemStatusHistory.getVenOrderItem();

                salesRecord = salesRecordJob.createOrUpdateSalesRecord(locator, item);

                if (salesRecord != null) {
                    _log.debug("set reconcile date");
                    salesRecord.setReconcileDate(new Timestamp(venOrderItemStatusHistory.getId().getHistoryTimestamp().getTime()));

                    finSalesRecordHome.mergeFinSalesRecord(salesRecord);
                    item.setSalesBatchStatus(VeniceConstants.FIN_SALES_BATCH_STATUS_DONE);
                    itemHome.mergeVenOrderItem(item);
                    count++;
                } else {
                    errorCount++;
//                    item.setSalesBatchStatus(VeniceConstants.FIN_SALES_BATCH_STATUS_FAILED);
                    item.setSalesBatchStatus("Failed");
                    itemHome.mergeVenOrderItem(item);
                    _log.debug("Sales record/journal creation failed for order item: " + item.getWcsOrderItemId());
                }
            }
            Long endTime = System.currentTimeMillis();
            _log.info(count + " sales record(s) generated, with duration:" + (endTime - startTime) + "ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
