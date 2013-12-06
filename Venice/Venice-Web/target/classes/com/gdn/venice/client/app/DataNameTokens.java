package com.gdn.venice.client.app;

import java.util.HashMap;
import java.util.Map;

/**
 * DataNameTokens.java
 * 
 * A Singleton container for a map of the various data names from the Entity classes
 * the map contains the dotted form of the JPA entity field (for example
 * VenOrder.VenOrderStatus.orderStatusId) and the associated wrapper class
 * in Java that the field is defined as (for example "java.lang.String")
 * 
 * Comments added by David Forden
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">Henry Chandra</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 *
 */
public class DataNameTokens {
	private static DataNameTokens dataNameTokens;
	
	/*
	 * The map that contains all of the tokens. The first String in the Map is the 
	 * name token of the field in the JPA Entity class. The second String
	 * in the Map is the Java wrapper type of the field.
	 * 
	 * Really reflection should have been used here instead of this mechanism - David
	 */
	private Map<String, String> fieldClassMap;
	
	//set to true when deploy to activate authentication
	public static boolean GLOBAL_SECURITY_ENABLED = true;
	
	public static String FRDENTITYBLACKLIST_ENTITYBLACKLISTID="FrdEntityBlacklist.entityBlacklistId";
	public static String FRDENTITYBLACKLIST_BLACKLISTSTRING="FrdEntityBlacklist.blacklistString";
	public static String FRDENTITYBLACKLIST_DESCRIPTION="FrdEntityBlacklist.description";
	public static String FRDENTITYBLACKLIST_CREATEDBY="FrdEntityBlacklist.createdBy";
	public static String FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP="FrdEntityBlacklist.blacklistTimestamp";
	public static String FRDENTITYBLACKLIST_BLACKORWHITELIST="FrdEntityBlacklist.blackOrWhiteList";
	
	public static String FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID="FrdCustomerWhitelistBlacklist.id";
	public static String FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME="FrdCustomerWhitelistBlacklist.customerFullName";
	public static String FRDCUSTOMERBLACKLIST_ADDRESS = "FrdCustomerWhitelistBlacklist.address";
	public static String FRDCUSTOMERBLACKLIST_PHONENUMBER="FrdCustomerWhitelistBlacklist.phoneNumber";
	public static String FRDCUSTOMERBLACKLIST_EMAIL="FrdCustomerWhitelistBlacklist.email";
	public static String FRDCUSTOMERBLACKLIST_DESCRIPTION="FrdCustomerWhitelistBlacklist.description";
	public static String FRDCUSTOMERBLACKLIST_CREATEDBY="FrdCustomerWhitelistBlacklist.createdBy";
	public static String FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP="FrdCustomerWhitelistBlacklist.timestamp";	
	public static String FRDCUSTOMERBLACKLIST_HANDPHONENUMBER="FrdCustomerWhitelistBlacklist.handphoneNumber";
	public static String FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER="FrdCustomerWhitelistBlacklist.shippingPhoneNumber";
	public static String FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER="FrdCustomerWhitelistBlacklist.shippingHandphoneNumber";
	public static String FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS="FrdCustomerWhitelistBlacklist.shippingAddress";
	public static String FRDCUSTOMERBLACKLIST_CCNUMBER="FrdCustomerWhitelistBlacklist.ccNumber";
	public static String FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP="FrdCustomerWhitelistBlacklist.orderTimestamp";
	
	public static String FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID="FrdCustomerWhitelist.id";
	public static String FRDCUSTOMERWHITELIST_ORDERID="FrdCustomerWhitelist.orderid";
	public static String FRDCUSTOMERWHITELIST_ORDERTIMESTAMP="FrdCustomerWhitelist.ordertimestamp";
	public static String FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME="FrdCustomerWhitelist.customername";
	public static String FRDCUSTOMERWHITELIST_SHIPPINGADDRESS = "FrdCustomerWhitelist.shippingaddress";
	public static String FRDCUSTOMERWHITELIST_EMAIL="FrdCustomerWhitelist.email";
	public static String FRDCUSTOMERWHITELIST_CREDITCARDNUMBER="FrdCustomerWhitelist.creditcardnumber";
	public static String FRDCUSTOMERWHITELIST_ISSUERBANK="FrdCustomerWhitelist.issuerbank";
	public static String FRDCUSTOMERWHITELIST_ECI="FrdCustomerWhitelist.eci";
	public static String FRDCUSTOMERWHITELIST_EXPIREDDATE="FrdCustomerWhitelist.expireddate";
	public static String FRDCUSTOMERWHITELIST_GENUINEDATE="FrdCustomerWhitelist.genuinedate";	
	public static String FRDCUSTOMERWHITELIST_REMARK="FrdCustomerWhitelist.remark";
	public static String FRDCUSTOMERWHITELIST_CREATEDBY="FrdCustomerWhitelist.createdby";
	public static String FRDCUSTOMERWHITELIST_CREATED="FrdCustomerWhitelist.created";
	
	public static String FRDFRAUDPOINT_FRAUDPOINTSREFERENCEID="FrdFraudPointsReference.fraudPointsReferenceId";
	public static String FRDFRAUDPOINT_FRAUDREFERENCEDESCRIPTION="FrdFraudPointsReference.fraudReferenceDescription";
	public static String FRDFRAUDPOINT_FRAUDPOINTSVALUE="FrdFraudPointsReference.fraudPointsValue";
	
	public static String FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID="FrdFraudSuspicionCase.fraudSuspicionCaseId";
	public static String FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME="FrdFraudSuspicionCase.fraudCaseDateTime";
	public static String FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC="FrdFraudSuspicionCase.fraudCaseDesc";
	public static String FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES="FrdFraudSuspicionCase.fraudSuspicionNotes";
	public static String FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID="FrdFraudSuspicionCase.frdFraudCaseStatus.fraudCaseStatusId";
	public static String FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC="FrdFraudSuspicionCase.frdFraudCaseStatus.fraudCaseStatusDesc";
	public static String FRDFRAUDSUSPICIONCASE_VENORDER_ORDERID="FrdFraudSuspicionCase.venOrder.orderId";
	public static String FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID="FrdFraudSuspicionCase.venOrder.wcsOrderId";
	public static String FRDFRAUDSUSPICIONCASE_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE="FrdFraudSuspicionCase.venOrder.venOrderStatus.orderStatusCode";	
	public static String FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON="FrdFraudSuspicionCase.suspicionReason";
	public static String FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS="FrdFraudSuspicionCase.fraudTotalPoints";
	public static String FRDFRAUDSUSPICIONCASE_ENABLEMODIFYAFTERCOMPLETED="FrdFraudSuspicionCase.enableModifyAfterCompleted";
	public static String FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE="FrdFraudSuspicionCase.venOrder.orderDate";
	public static String FRDFRAUDSUSPICIONCASE_LASTACTION="FrdFraudSuspicionCase.lastAction";
	public static String FRDFRAUDSUSPICIONCASE_TYPE_FC="description";
	public static String FRDFRAUDSUSPICIONCASE_GENUINE="genuine";
	
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERNAME="FrdFraudSuspicionCase.summary.customerName";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE="FrdFraudSuspicionCase.summary.customerMobilePhone";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL="FrdFraudSuspicionCase.summary.customerEmail";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERHOMEPHONE="FrdFraudSuspicionCase.summary.customerHomePhone";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_PAYMENTADDRESSSIMILARITY="FrdFraudSuspicionCase.summary.paymentAddressSimilarity";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_MULTIPLESHIPMENT="FrdFraudSuspicionCase.summary.multipleShipment";
	
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID="FrdFraudSuspicionPoint.frdFraudSuspicionCase.fraudSuspicionCaseId";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID="FrdFraudSuspicionPoint.fraudSuspicionPointsId";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDRULENAME="FrdFraudSuspicionPoint.fraudRuleName";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS="FrdFraudSuspicionPoint.riskPoints";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE="FrdFraudSuspicionCase.summary.totalRiskScore";
	public static String FRDFRAUDSUSPICIONCASE_SUMMARY_ILOGRECOMENDATION="FrdFraudSuspicionCase.ilogFraudStatus";
	
	public static String FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID="FrdFraudActionLog.frdFraudSuspicionCase.fraudSuspicionCaseId";
	public static String FRDFRAUDCASEACTIONLOG_FRAUDACTIONID="FrdFraudActionLog.fraudActionId";
	public static String FRDFRAUDCASEACTIONLOG_DATETIME="FrdFraudActionLog.fraudActionLogDate";
	public static String FRDFRAUDCASEACTIONLOG_ACTIONTYPE="FrdFraudActionLog.fraudActionLogType";
	public static String FRDFRAUDCASEACTIONLOG_PARTYID="FrdFraudActionLog.venParty.partyId";
	public static String FRDFRAUDCASEACTIONLOG_PARTYNAME="FrdFraudActionLog.venParty.fullOrLegalName";
	public static String FRDFRAUDCASEACTIONLOG_NOTES="FrdFraudActionLog.fraudActionLogNotes";
	public static String FRDFRAUDCASEACTIONLOG_CREATEDBY="FrdFraudActionLog.createdBy";
	public static String FRDFRAUDCASEACTIONLOG_CREATEDDATE="FrdFraudActionLog.createdDate";
	public static String FRDFRAUDCASEACTIONLOG_MODIFIEDBY="FrdFraudActionLog.modifiedBy";
	public static String FRDFRAUDCASEACTIONLOG_MODIFIEDDATE="FrdFraudActionLog.modifiedDate";
	public static String FRDFRAUDCASEACTIONLOG_ISACTIVE="FrdFraudActionLog.isActive";
	
	public static String FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID="FrdFraudCaseHistory.id.fraudSuspicionCaseId";
	public static String FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE="FrdFraudCaseHistory.id.fraudCaseHistoryDate";
	public static String FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYNOTES="FrdFraudCaseHistory.fraudCaseHistoryNotes";
	public static String FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSID="FrdFraudCaseHistory.frdFraudCaseStatus.fraudCaseStatusId";
	public static String FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSDESC="FrdFraudCaseHistory.frdFraudCaseStatus.fraudCaseStatusDesc";
	
	public static String FRDFRAUDCASEATTACHMENT_ATTACHMENTID="FrdFraudFileAttachment.fraudFileAttachmentId";
	public static String FRDFRAUDCASEATTACHMENT_CREATEDBY="FrdFraudFileAttachment.createdBy";
	public static String FRDFRAUDCASEATTACHMENT_FILELOCATION="FrdFraudFileAttachment.fileLocation";
	public static String FRDFRAUDCASEATTACHMENT_FILENAME="FrdFraudFileAttachment.fileName";
	public static String FRDFRAUDCASEATTACHMENT_DESCRIPTION="FrdFraudFileAttachment.fraudFileAttachmentDescription";
	public static String FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID="FrdFraudFileAttachment.frdFraudSuspicionCase.fraudSuspicionCaseId";
	
	//for fraud parameter
	public static String FRDPARAMETERRULE31_ID="FrdParameterRule31.id";
	public static String FRDPARAMETERRULE31_EMAIL="FrdParameterRule31.email";
	public static String FRDPARAMETERRULE31_CCNUMBER="FrdParameterRule31.noCc";

	public static String MIGSUPLOAD_MIGSID = "VenMigsUploadTemporary.migsId";
	public static String MIGSUPLOAD_TRANSACTIONID = "VenMigsUploadTemporary.transactionId";
	public static String MIGSUPLOAD_TRANSACTIONDATE = "VenMigsUploadTemporary.transactionDate";
	public static String MIGSUPLOAD_MERCHANTID = "VenMigsUploadTemporary.merchantId";
	public static String MIGSUPLOAD_ORDERREFERENCE = "VenMigsUploadTemporary.orderReference";
	public static String MIGSUPLOAD_ORDERID = "VenMigsUploadTemporary.orderId";
	public static String MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE = "VenMigsUploadTemporary.merchantTransactionReference";
	public static String MIGSUPLOAD_TRANSACTIONTYPE = "VenMigsUploadTemporary.transactionType";
	public static String MIGSUPLOAD_ACQUIRERID = "VenMigsUploadTemporary.acquirerId";
	public static String MIGSUPLOAD_BATCHNUMBER = "VenMigsUploadTemporary.batchNumber";
	public static String MIGSUPLOAD_CURRENCY = "VenMigsUploadTemporary.currency";
	public static String MIGSUPLOAD_AMOUNT = "VenMigsUploadTemporary.amount";
	public static String MIGSUPLOAD_RRN = "VenMigsUploadTemporary.rrn";
	public static String MIGSUPLOAD_RESPONSECODE = "VenMigsUploadTemporary.responseCode";
	public static String MIGSUPLOAD_ACQUIRERRESPONSECODE = "VenMigsUploadTemporary.acquirerResponseCode";
	public static String MIGSUPLOAD_AUTHORISATIONCODE = "VenMigsUploadTemporary.authorisationCode";
	public static String MIGSUPLOAD_OPERATOR = "VenMigsUploadTemporary.operator";
	public static String MIGSUPLOAD_MERCHANTTRANSACTIONSOURCE = "VenMigsUploadTemporary.merchantTransactionSource";
	public static String MIGSUPLOAD_ORDERDATE = "VenMigsUploadTemporary.orderDate";
	public static String MIGSUPLOAD_CARDTYPE = "VenMigsUploadTemporary.cardType";
	public static String MIGSUPLOAD_CARDNUMBER = "VenMigsUploadTemporary.cardNumber";
	public static String MIGSUPLOAD_CARDEXPIRYMONTH = "VenMigsUploadTemporary.cardExpiryMonth";
	public static String MIGSUPLOAD_CARDEXPIRYYEAR = "VenMigsUploadTemporary.cardExpiryYear";
	public static String MIGSUPLOAD_DIALECTCSCRESULTCODE = "VenMigsUploadTemporary.dialectCscResultCode";
	public static String MIGSUPLOAD_ACTION = "VenMigsUploadTemporary.action";
	public static String MIGSUPLOAD_PROBLEMDESCRIPTION = "VenMigsUploadTemporary.problemDescription";
	public static String MIGSUPLOAD_COMMENT = "VenMigsUploadTemporary.comment";
	public static String MIGSUPLOAD_ECOMMERCEINDICATOR = "VenMigsUploadTemporary.ecommerceIndicator";
	public static String MIGSUPLOAD_TYPE = "VenMigsUploadTemporary.type";
	
	public static String MIGSMASTER_MIGSID = "VenMigsUploadMaster.migsId";
	public static String MIGSMASTER_TRANSACTIONID = "VenMigsUploadMaster.transactionId";
	public static String MIGSMASTER_TRANSACTIONDATE = "VenMigsUploadMaster.transactionDate";
	public static String MIGSMASTER_MERCHANTID = "VenMigsUploadMaster.merchantId";
	public static String MIGSMASTER_ORDERREFERENCE = "VenMigsUploadMaster.orderReference";
	public static String MIGSMASTER_ORDERID = "VenMigsUploadMaster.orderId";
	public static String MIGSMASTER_MERCHANTTRANSACTIONREFERENCE = "VenMigsUploadMaster.merchantTransactionReference";
	public static String MIGSMASTER_TRANSACTIONTYPE = "VenMigsUploadMaster.transactionType";
	public static String MIGSMASTER_ACQUIRERID = "VenMigsUploadMaster.acquirerId";
	public static String MIGSMASTER_BATCHNUMBER = "VenMigsUploadMaster.batchNumber";
	public static String MIGSMASTER_CURRENCY = "VenMigsUploadMaster.currency";
	public static String MIGSMASTER_AMOUNT = "VenMigsUploadMaster.amount";
	public static String MIGSMASTER_RRN = "VenMigsUploadMaster.rrn";
	public static String MIGSMASTER_RESPONSECODE = "VenMigsUploadMaster.responseCode";
	public static String MIGSMASTER_ACQUIRERRESPONSECODE = "VenMigsUploadMaster.acquirerResponseCode";
	public static String MIGSMASTER_AUTHORISATIONCODE = "VenMigsUploadMaster.authorisationCode";
	public static String MIGSMASTER_OPERATOR = "VenMigsUploadMaster.operator";
	public static String MIGSMASTER_MERCHANTTRANSACTIONSOURCE = "VenMigsUploadMaster.merchantTransactionSource";
	public static String MIGSMASTER_ORDERDATE = "VenMigsUploadMaster.orderDate";
	public static String MIGSMASTER_CARDTYPE = "VenMigsUploadMaster.cardType";
	public static String MIGSMASTER_CARDNUMBER = "VenMigsUploadMaster.cardNumber";
	public static String MIGSMASTER_CARDEXPIRYMONTH = "VenMigsUploadMaster.cardExpiryMonth";
	public static String MIGSMASTER_CARDEXPIRYYEAR = "VenMigsUploadMaster.cardExpiryYear";
	public static String MIGSMASTER_DIALECTCSCRESULTCODE = "VenMigsUploadMaster.dialectCscResultCode";
	public static String MIGSMASTER_ACTION = "VenMigsUploadMaster.action";
	public static String MIGSMASTER_PROBLEMDESCRIPTION = "VenMigsUploadMaster.problemDescrioption";
	public static String MIGSMASTER_COMMENT = "VenMigsUploadMaster.comment";
	public static String MIGSMASTER_ECOMMERCEINDICATOR = "VenMigsUploadMaster.ecommerceIndicator";
	public static String MIGSMASTER_TYPE = "VenMigsUploadMaster.type";
	
/*
 * Commented after merge because defined twice 2011-10-20 DF
 */
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC = "VenOrderPaymentAllocation.venOrderPayment.venPaymentType.paymentTypeDesc";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER = "VenOrderPaymentAllocation.venOrderPayment.vaorbiorccNumber";
//	public static String VENORDERPAYMENTALLOCATION_VENADDRESS = "VenOrderPaymentAllocation.venAddress";
	
	public static String VENMERCHANT_MERCHANTID = "VenMerchant.merchantId";
	public static String VENMERCHANT_WCSMERCHANTID = "VenMerchant.wcsMerchantId";
	public static String VENMERCHANT_VENPARTY_FULLORLEGALNAME = "VenMerchant.venParty.fullOrLegalName";

	public static String VENMERCHANTPRODUCT_PRODUCTID = "VenMerchantProduct.productId";
	public static String VENMERCHANTPRODUCT_WCSPRODUCTSKU = "VenMerchantProduct.wcsProductSku";
	public static String VENMERCHANTPRODUCT_WCSPRODUCTNAME = "VenMerchantProduct.wcsProductName";
	public static String VENMERCHANTPRODUCT_COSTOFGOODSSOLD = "VenMerchantProduct.costOfGoodsSold";
	public static String VENMERCHANTPRODUCT_MERCHANTID = "VenMerchantProduct.venMerchant.merchantId";
	
	//General Name Tokens
	public static String VENORDER_ORDERID = "VenOrder.orderId";
	public static String VENORDER_WCSORDERID = "VenOrder.wcsOrderId";
	public static String VENORDER_ORDERDATE = "VenOrder.orderDate";
	public static String VENORDER_VENCUSTOMER_CUSTOMERUSERNAME = "VenOrder.venCustomer.customerUserName";
	public static String VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG = "VenOrder.venCustomer.firstTimeTransactionFlag";
	public static String VENORDER_VENORDERSTATUS_ORDERSTATUSID = "VenOrder.venOrderStatus.orderStatusId";
	public static String VENORDER_VENORDERSTATUS_ORDERSTATUSCODE = "VenOrder.venOrderStatus.orderStatusCode";
	public static String VENORDER_FPDATE = "VenOrder.venOrderStatusHistories.id.historyTimestamp";
	public static String VENORDER_RMAFLAG = "VenOrder.rmaFlag";
	public static String VENORDER_VENFRAUDCHECKSTATUS_FRAUDCHECKSTATUSDESC = "VenOrder.venFraudCheckStatus.fraudCheckStatusDesc";
	public static String VENORDER_FINANCERECONCILEFLAG = "VenOrder.financeReconcileFlag";
	public static String VENORDER_BLOCKEDFLAG = "VenOrder.blockedFlag";
	public static String VENORDER_FULFILLMENTSTATUS = "VenOrder.fulfillmentStatus";
	public static String VENORDER_RMAACTION = "VenOrder.rmaAction";
//	public static String VENORDER_AMOUNT_OPERATOR = "VenOrder.amount.operator";
	public static String VENORDER_AMOUNT = "VenOrder.amount";
	public static String VENORDER_DELIVEREDDATETIME = "VenOrder.deliveredDateTime";
	public static String VENORDER_IPADDRESS = "VenOrder.ipAddress";
//	public static String VENORDER_BLOCKEDSOURCE = "VenOrder.blockedSource"; //NOT IMPLEMENTED IN VENORDER 
	public static String VENORDER_BLOCKEDTIMESTAMP = "VenOrder.blockedTimestamp";
	public static String VENORDER_BLOCKEDREASON = "VenOrder.blockedReason";
	
	public static String VENORDERITEM_ORDERITEMID = "VenOrderItem.orderItemId";
	public static String VENORDERITEM_WCSORDERITEMID = "VenOrderItem.wcsOrderItemId";
	public static String VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE = "VenOrderItem.venOrderStatus.orderStatusCode";
	public static String VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE = "VenOrderItem.venSettlementRecords.commissionType";
	public static String VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME = "VenOrderItem.venMerchantProduct.wcsProductName";
	public static String VENORDERITEM_QUANTITY = "VenOrderItem.quantity";
	public static String VENORDERITEM_TOTAL = "VenOrderItem.total";
	public static String VENORDERITEM_SHIPPINGCOST = "VenOrderItem.shippingCost";
	public static String VENORDERITEM_INSURANCECOST = "VenOrderItem.insuranceCost";
	public static String VENORDERITEM_VENORDER_ORDERID = "VenOrderItem.venOrder.orderId";
	public static String VENORDERITEM_VENMERCHANTPRODUCT_SUMMARY = "VenOrderItem.venMerchantProduct.summary";
	public static String VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC = "VenOrderItem.venMerchantProduct.venProductType.productTypeDesc";
	public static String VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU = "VenOrderItem.venMerchantProduct.wcsProductSku";
	public static String VENORDERITEM_VENMERCHANTPRODUCT_PRODUCTID = "VenOrderItem.venMerchantProduct.productId";
	public static String VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC = "VenOrderItem.logLogisticService.logLogisticServiceType.logLogisticServiceTypeDesc";
//	public static String VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTCATEGORIES_PRODUCTCATEGORY = "VenOrderItem.venMerchantProduct.venProductCategories.productCategory";
//	public static String VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTCATEGORIES_LEVEL = "VenOrderItem.venMerchantProduct.venProductCategories.venProductCategory.level";
	public static String VENORDERITEM_PRICE = "VenOrderItem.price";
	public static String VENORDERITEM_PRODUCTPRICE = "VenOrderItem.productPrice";
	public static String VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME = "VenOrderItem.venRecipient.venParty.fullOrLegalName";
	public static String VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP = "VenOrderItem.venRecipient.recipientHp";
	public static String VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL = "VenOrderItem.venRecipient.recipientEmail";
	public static String VENORDERITEM_VENADDRESS = "VenOrderItem.venAddress";
	public static String VENORDERITEM_VENADDRESS_ADDRESSID = "VenOrderItem.venAddress.address_id";
	public static String VENORDERITEM_VENADDRESS_STREETADDRESS1 = "VenOrderItem.venAddress.streetAddress1";
	public static String VENORDERITEM_VENADDRESS_STREETADDRESS2 = "VenOrderItem.venAddress.streetAddress2";
	public static String VENORDERITEM_VENADDRESS_KECAMATAN = "VenOrderItem.venAddress.kecamatan";
	public static String VENORDERITEM_VENADDRESS_KELURAHAN = "VenOrderItem.venAddress.kelurahan";
	public static String VENORDERITEM_VENADDRESS_POSTALCODE = "VenOrderItem.venAddress.postalCode";
	public static String VENORDERITEM_VENADDRESS_VENCITY_CITYNAME = "VenOrderItem.venAddress.venCity.cityName";
	public static String VENORDERITEM_VENADDRESS_VENCOUNTRY_COUNTRYNAME = "VenOrderItem.venAddress.venCountry.countryName";
	public static String VENORDERITEM_VENADDRESS_VENCOUNTRY_STATE = "VenOrderItem.venAddress.venState.stateName";
	
	public static String VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE = "VenOrderItemAdjustment.venPromotion.promoCode";
	public static String VENORDERITEMADJUSTMENT_VENORDERITEM_ORDERITEMID = "VenOrderItemAdjustment.venOrderItem.orderItemId";
	public static String VENORDERITEMADJUSTMENT_AMOUNT = "VenOrderItemAdjustment.amount";
	public static String VENPRODUCTCATEGORY_PRODUCTCATEGORYID = "VenProductCategory.productCategoryId";
	public static String VENPRODUCTCATEGORY_PRODUCTCATEGORY = "VenProductCategory.productCategory";
	public static String VENPRODUCTCATEGORY_LEVEL = "VenProductCategory.level";
	
	public static String VENORDERITEMADJUSTMENT_PROMOTIONVOUCHERCODE="VenOrderItemAdjustment.promotionVoucherCode";
	public static String VENORDERITEMADJUSTMENT_ADMINDESC="VenOrderItemAdjustment.adminDesc";
	
	//Retur Name Tokens
	public static String VENRETUR_RETURID = "VenRetur.returId";
	public static String VENRETUR_WCSRETURID = "VenRetur.wcsReturId";
	public static String VENRETUR_RETURDATE = "VenRetur.returDate";
	public static String VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME = "VenRetur.venCustomer.customerUserName";
	public static String VENRETUR_VENRETURSTATUS_ORDERSTATUSID = "VenRetur.venOrderStatus.orderStatusId";
	public static String VENRETUR_VENRETURSTATUS_ORDERSTATUSCODE = "VenRetur.venReturStatus.orderStatusCode";
	public static String VENRETUR_RMAACTION = "VenRetur.rmaAction";
	public static String VENRETUR_AMOUNT = "VenRetur.amount";
	public static String VENRETUR_DELIVEREDDATETIME = "VenRetur.deliveredDateTime";
	
	public static String VENRETURITEM_RETURITEMID = "VenReturItem.returItemId";
	public static String VENRETURITEM_WCSRETURITEMID = "VenReturItem.wcsReturItemId";
	public static String VENRETURITEM_VENRETURSTATUS_ORDERSTATUSCODE = "VenReturItem.venReturStatus.orderStatusCode";
	public static String VENRETURITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME = "VenReturItem.venMerchantProduct.wcsProductName";
	public static String VENRETURITEM_QUANTITY = "VenOrderItem.quantity";
	public static String VENRETURITEM_TOTAL = "VenOrderItem.total";

	public static String LOGAIRWAYBILLRETUR_VENRETURITEM_RETURITEMID = "LogAirwayBillRetur.venReturItem.returItemId";
	public static String LOGAIRWAYBILLRETUR_VENRETURITEM_VENRETUR_WCSRETURID = "LogAirwayBillRetur.venReturItem.venRetur.wcsReturId";
	public static String LOGAIRWAYBILLRETUR_VENRETURITEM_WCSRETURITEMID = "LogAirwayBillRetur.venReturItem.wcsReturItemId";
	public static String LOGAIRWAYBILLRETUR_GDNREFERENCE = "LogAirwayBillRetur.gdnReference";
	public static String LOGAIRWAYBILLRETUR_AIRWAYBILLPICKUPDATETIME = "LogAirwayBillRetur.airwayBillPickupDateTime";
	public static String LOGAIRWAYBILLRETUR_AIRWAYBILLID = "LogAirwayBillRetur.airwayBillId";
	public static String LOGAIRWAYBILLRETUR_TRACKINGNUMBER = "LogAirwayBillRetur.trackingNumber";
	public static String LOGAIRWAYBILLRETUR_RECEIVED = "LogAirwayBillRetur.received";
	
	//Retur status history
	public static String VENRETURSTATUSHISTORY_RETURID="VenReturStatusHistory.id.returId";
	public static String VENRETURSTATUSHISTORY_WCSRETURID="wcsReturId";
	public static String VENRETURSTATUSHISTORY_TIMESTAMP="VenReturStatusHistory.id.historyTimestamp";
	public static String VENRETURSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE="VenReturStatusHistory.venReturStatus.orderStatusCode";
	public static String VENRETURSTATUSHISTORY_STATUSCHANGEREASON="VenReturStatusHistory.statusChangeReason";
	
	//Retur item status history
	public static String VENRETURITEMSTATUSHISTORY_RETURITEMID="VenReturItemStatusHistory.id.returItemId";
	public static String VENRETURITEMSTATUSHISTORY_WCSRETURITEMID="wcsReturItemId";
	public static String VENRETURITEMSTATUSHISTORY_TIMESTAMP="VenReturItemStatusHistory.id.historyTimestamp";
	public static String VENRETURITEMSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE="VenReturItemStatusHistory.venReturStatus.orderStatusCode";
	public static String VENRETURITEMSTATUSHISTORY_STATUSCHANGEREASON="VenReturItemStatusHistory.statusChangeReason";
	
	//Fields for Sales Record Screen
	public static String FINSALESRECORD_SALESRECORDID = "FinSalesRecord.salesRecordId";
	public static String FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE = "FinSalesRecord.venOrderItem.venOrder.orderDate";
	public static String FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID = "FinSalesRecord.venOrderItem.venOrder.wcsOrderId";
	public static String FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID = "FinSalesRecord.venOrderItem.venMerchantProduct.venMerchant.wcsMerchantId";
	public static String FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME = "FinSalesRecord.venOrderItem.venMerchantProduct.venMerchant.venParty.fullOrLegalName";
	public static String FINSALESRECORD_VENORDERITEM_WCSORDERITEMID = "FinSalesRecord.venOrderItem.wcsOrderItemId";	
	public static String FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME = "FinSalesRecord.venOrderItem.venMerchantProduct.wcsProductName";
	public static String FINSALESRECORD_VENORDERITEM_QUANTITY = "FinSalesRecord.venOrderItem.quantity";
	public static String FINSALESRECORD_VENORDERITEM_PRICE = "FinSalesRecord.venOrderItem.price";
	public static String FINSALESRECORD_VENORDERITEM_TOTAL = "FinSalesRecord.venOrderItem.total";
	public static String FINSALESRECORD_GDNCOMMISIONAMOUNT = "FinSalesRecord.gdnCommisionAmount";
	public static String FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT = "FinSalesRecord.gdnTransactionFeeAmount";
	public static String FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE = "FinSalesRecord.venOrderItem.venSettlementRecords.commissionType";
	public static String FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE = "FinSalesRecord.venOrderItem.venOrder.venOrderPaymentAllocations.venOrderPayment.venWcsPaymentType.paymentTypeCode";
	public static String FINSALESRECORD_PAYMENT_STATUS = "FinSalesRecords.paymentStatus";
	public static String FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG = "FinSalesRecords.venOrderItem.venSettlementRecords.pph23";
	public static String FINSALESRECORD_PPH23_AMOUNT = "FinSalesRecords.pph23Amount";
	
//	public static String FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME = "FinSalesRecord.venOrderItem.logLogisticService.logLogisticsProvider.venParty.fullOrLegalName";
	public static String FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_PROVIDER_CODE = "FinSalesRecord.venOrderItem.logLogisticService.logLogisticsProvider.logisticsProviderCode";
	public static String FINSALESRECORD_VENORDERITEM_SHIPPINGCOST = "FinSalesRecord.venOrderItem.shippingCost";
	public static String FINSALESRECORD_VENORDERITEM_INSURANCECOST = "FinSalesRecord.venOrderItem.insuranceCost";
	public static String FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST = "FinSalesRecord.venOrderItem.shippingCostInsuranceCost";
	public static String FINSALESRECORD_GDNHANDLINGFEEAMOUNT = "FinSalesRecord.gdnHandlingFeeAmount";
	// Need to handle Adjustment in Server Presenter later 
	public static String FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT = "FinSalesRecord.venOrderItem.venOrderItemAdjustment.amount";
	public static String FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL = "FinSalesRecord.venOrderItem.venOrderItemAdjustment.code.amount";
	public static String FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT = "FinSalesRecord.gdnGiftWrapCharge";
	public static String FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC = "FinSalesRecord.finApprovalStatus.approvalStatusDesc";
	public static String FINSALESRECORD_CUSTOMERDOWNPAYMENT = "FinSalesRecord.customerDownpayment";
	public static String FINSALESRECORD_FINAPPAYMENT = "FinSalesRecord.finApPayment";
	public static String FINSALESRECORD_RECONCILEDATE = "FinSalesRecord.reconcileDate";
	public static String FINSALESRECORD_SALESTIMESTAMP = "FinSalesRecord.salesTimestamp";
	//Fields for Export Screen - Journal Voucher
	public static String FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID = "FinRolledUpJournalHeader.ruJournalHeaderId";
	public static String FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH = "FinRolledUpJournalHeader.ruJournalFilenameAndPath";
	public static String FINROLLEDUPJOURNALHEADER_RUTIMESTAMP = "FinRolledUpJournalHeader.ruTimestamp";
	public static String FINROLLEDUPJOURNALHEADER_RUJOURNALDESC = "FinRolledUpJournalHeader.ruJournalHeaderDesc";
	public static String FINROLLEDUPJOURNALHEADER_FINPERIOD_PERIODID = "FinRolledUpJournalHeader.finPeriod.periodId";
	public static String FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID = "FinRolledUpJournalHeader.finRolledUpJournalType.financeJournalTypeId";
	public static String FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC = "FinRolledUpJournalHeader.finRolledUpJournalType.financeJournalTypeDesc";
	
	//Fields for Export Screen - Journal Voucher - Journal type
	public static String FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID = "FinRolledUpJournalType.financeJournalTypeId";
	public static String FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC = "FinRolledUpJournalType.financeJournalTypeDesc";

	//Fields for Period Setup Screen
	public static String FINPERIOD_PERIODID = "FinPeriod.periodId";
	public static String FINPERIOD_PERIODDESC = "FinPeriod.periodDesc";
	public static String FINPERIOD_FROMDATETIME = "FinPeriod.fromDatetime";
	public static String FINPERIOD_TODATETIME = "FinPeriod.toDatetime";
	
	//Field for Export screen - Account Lines
	public static String FINROLLEDUPJOURNALENTRY_FINANCEJOURNALENTRYID = "FinRolledUpJournalEntry.financeJournalEntryId";
	public static String FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID = "FinRolledUpJournalEntry.finRolledUpJournalHeader.ruJournalHeaderId";
	public static String FINROLLEDUPJOURNALENTRY_RUVALUE = "FinRolledUpJournalEntry.ruValue";
	public static String FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET = "FinRolledUpJournalEntry.ruValueDebet";
	public static String FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT = "FinRolledUpJournalEntry.ruValueCredit";
	public static String FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP = "FinRolledUpJournalEntry.ruJournalEntryTimestamp";
	public static String FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID = "FinRolledUpJournalEntry.finAccount.accountId";
	public static String FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC = "FinRolledUpJournalEntry.finAccount.accountDesc";
	public static String FINROLLEDUPJOURNALENTRY_CREDITDEBITFLAG = "FinRolledUpJournalEntry.creditDebitFlag";
	public static String FINROLLEDUPJOURNALENTRY_GROUPID = "FinRolledUpJournalEntry.groupId";
	public static String FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSID = "FinRolledUpJournalEntry.finRolledUpJournalStatus.journalEntryStatusId";
	public static String FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC = "FinRolledUpJournalEntry.finRolledUpJournalStatus.journalEntryStatusDesc";
	
	//Field for COA Setup screen
	public static String FINACCOUNT_ACCOUNTID = "FinAccount.accountId";
	public static String FINACCOUNT_ACCOUNTDESC = "FinAccount.accountDesc";
	public static String FINACCOUNT_ACCOUNTNUMBER = "FinAccount.accountNumber";
	public static String FINACCOUNT_SUMMARYACCOUNT = "FinAccount.summaryAccount";
	public static String FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID = "FinAccount.finAccountType.accountTypeId";
	public static String FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC = "FinAccount.finAccountType.accountTypeDesc";
	public static String FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID = "FinAccount.finAccountCategory.accountCategoryId";
	public static String FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC = "FinAccount.finAccountCategory.accountCategoryDesc";
	
	public static String FINACCOUNTTYPE_ACCOUNTTYPEID = "FinAccountType.accountTypeId";
	public static String FINACCOUNTTYPE_ACCOUNTTYPEDESC = "FinAccountType.accountTypeDesc";

	public static String FINACCOUNTCATEGORY_ACCOUNTCATEGORYID = "FinAccountCategory.accountCategoryId";
	public static String FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC = "FinAccountCategory.accountCategoryDesc";

	//Field for Export screen - Account Lines - status
	public static String FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSID = "FinRolledUpJournalStatus.journalEntryStatusId";
	public static String FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC = "FinRolledUpJournalStatus.journalEntryStatusDesc";
	
	/*
	 * Commented after merge because defined twice 2011-10-20 DF
	 */
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID = "VenOrderPaymentAllocation.venOrderPayment.orderPaymentId";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID = "VenOrderPaymentAllocation.venOrderPayment.wcsPaymentId";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME = "VenOrderPaymentAllocation.venOrderPayment.venBank.bankShortName";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE = "VenOrderPaymentAllocation.venOrderPayment.venPaymentType.paymentTypeCode";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER = "VenOrderPaymentAllocation.venOrderPayment.virtualAccountNumber";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT = "VenOrderPaymentAllocation.venOrderPayment.amount";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID = "VenOrderPaymentAllocation.venOrderPayment.venPaymentStatus.paymentStatusId";
//	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC = "VenOrderPaymentAllocation.venOrderPayment.venPaymentStatus.paymentStatusDesc";
//	public static String VENORDERPAYMENTALLOCATION_VENORDER_ORDERID = "VenOrderPaymentAllocation.venOrder.orderId";
	
	//Logistics Module Name Tokens	
	public static String LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICKUPDETAILSID = "LogMerchantPickupInstruction.merchantPickupDetailsId";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENMERCHANT_VENPARTY_FULLORLEGALNAME = "LogMerchantPickupInstruction.venMerchant.venParty.fullOrLegalName";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS1 = "LogMerchantPickupInstruction.venAddress.streetAddress1";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS2 = "LogMerchantPickupInstruction.venAddress.streetAddress2";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KECAMATAN = "LogMerchantPickupInstruction.venAddress.kecamatan";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KELURAHAN = "LogMerchantPickupInstruction.venAddress.kelurahan";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCITY_CITYNAME = "LogMerchantPickupInstruction.venAddress.venCity.cityName";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENSTATE_STATENAME= "LogMerchantPickupInstruction.venAddress.venState.stateName";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_POSTALCODE= "LogMerchantPickupInstruction.venAddress.postalCode";
	public static String LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCOUNTRY_COUNTRYNAME= "LogMerchantPickupInstruction.venAddress.venCountry.countryName";
	public static String LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPIC= "LogMerchantPickupInstruction.merchantPic";
	public static String LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICPHONE= "LogMerchantPickupInstruction.merchantPicPhone";
	
	public static String VENCUSTOMER_CUSTOMERID = "VenCustomer.customerId";
	public static String VENCUSTOMER_WCSCUSTOMERID = "VenCustomer.wcsCustomerId";
	public static String VENCUSTOMER_CUSTOMERUSERNAME = "VenCustomer.customerUserName";
	public static String VENCUSTOMER_USERTYPE= "VenCustomer.userType";
	public static String VENCUSTOMER_DATEOFBIRTH = "VenCustomer.dateOfBirth";
	public static String VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG = "VenCustomer.firstTimeTransactionFlag";
	public static String VENCUSTOMER_VENPARTY_PARTYFIRSTNAME = "VenCustomer.venParty.partyFirstName";
	public static String VENCUSTOMER_VENPARTY_PARTYLASTNAME = "VenCustomer.venParty.partyLastName";
	public static String VENCUSTOMER_VENPARTY_FULLORLEGALNAME = "VenCustomer.venParty.fullOrLegalName";
	

	//General - Party Maintenance Name Tokens
	public static String VENPARTY_PARTYID = "VenParty.partyId";	
	public static String VENPARTY_FIRSTNAME = "VenParty.partyFirstName";
	public static String VENPARTY_MIDDLENAME = "VenParty.partyMiddleName";
	public static String VENPARTY_LASTNAME = "VenParty.partyLastName";
	public static String VENPARTY_FULLORLEGALNAME = "VenParty.fullOrLegalName";
	public static String VENPARTY_POSITION = "VenParty.partyPosition";	
	public static String VENPARTY_VENPARTYTYPE_PARTYTYPEID = "VenParty.venPartyType.partyTypeId";
	public static String VENPARTY_VENPARTYTYPE_PARTYTYPEDESC = "VenParty.venPartyType.partyTypeDesc";
	
	//Party Maintenance - Party Type Id and its description
	public static String VENPARTYTYPE_PARTYTYPEID = "VenPartyType.partyTypeId";
	public static String VENPARTYTYPE_PARTYTYPEDESC = "VenPartyType.partyTypeDesc";
	public static String VENPARTY_VENPARTYTYPE_VENPARTYTYPEID = "VenParty.venPartyType.partyTypeId";
	public static String VENPARTY_PARTYTYPEDESC = "VenParty.partyTypeDesc";
	public static String VENPARTYADDRESS_VENADDRESS_ADDRESSID = "VenPartyAddress.venAddress.addressId";
	public static String VENPARTYADDRESS_VENADDRESS_STREETADDRESS1 = "VenPartyAddress.venAddress.streetAddress1";
	public static String VENPARTYADDRESS_VENADDRESS_STREETADDRESS2 = "VenPartyAddress.venAddress.streetAddress2";
	public static String VENPARTYADDRESS_VENADDRESS_KECAMATAN = "VenPartyAddress.venAddress.kecamatan";
	public static String VENPARTYADDRESS_VENADDRESS_KELURAHAN = "VenPartyAddress.venAddress.kelurahan";
	public static String VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID = "VenPartyAddress.venAddress.venCity.cityId";
	public static String VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME = "VenPartyAddress.venAddress.venCity.cityName";
	public static String VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID= "VenPartyAddress.venAddress.venState.stateId";
	public static String VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME = "VenPartyAddress.venAddress.venState.stateName";
	public static String VENPARTYADDRESS_VENADDRESS_POSTALCODE = "VenPartyAddress.venAddress.postalCode";
	public static String VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID = "VenPartyAddress.venAddress.venCountry.countryId";
	public static String VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME = "VenPartyAddress.venAddress.venCountry.countryName";
	public static String VENPARTYADDRESS_VENADDRESS = "VenPartyAddress.venAddress";
	public static String VENPARTYADDRESS_VENPARTY_PARTYID = "VenPartyAddress.venParty.partyId";
	public static String VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID = "VenPartyAddress.venAddressType.addressTypeId";
	public static String VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC = "VenPartyAddress.venAddressType.addressTypeDesc";

	
	//Party Maintenance - Contact detail for Partys
	public static String VENCONTACTDETAIL_CONTACTDETAILID = "VenContactDetail.contactDetailId";
	public static String VENCONTACTDETAIL_CONTACTDETAIL = "VenContactDetail.contactDetail";
	public static String VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC = "VenContactDetail.venContactDetailType.contactDetailTypeDesc";
	public static String VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID = "VenContactDetail.venContactDetailType.contactDetailTypeId";
	public static String VENCONTACTDETAIL_VENPARTY_PARTYID = "VenContactDetail.venParty.partyId";
	public static String VENCONTACTDETAIL_VENPARTY_FULLORLEGALNAME = "VenContactDetail.venParty.fullOrLegalName";

	
	public static String VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID = "VenContactDetailType.contactDetailTypeId";
	public static String VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC = "VenContactDetailType.contactDetailTypeDesc";

	public static String VENDISTRIBUTIONCART_DISTRIBUTIONCARTID = "VenDistributionCart.distributionCartId";
	public static String VENDISTRIBUTIONCART_VENORDERITEM_WCSORDERITEMID = "VenDistributionCart.venOrderItem.wcsOrderItemId";
	public static String VENDISTRIBUTIONCART_DCSEQUENCE = "VenDistributionCart.dcSequence";
	public static String VENDISTRIBUTIONCART_PACKAGEWEIGHT = "VenDistributionCart.packageWeight";
	public static String VENDISTRIBUTIONCART_QUANTITY = "VenDistributionCart.quantity";
	public static String VENDISTRIBUTIONCART_VENORDERITEM_ORDERITEMID = "VenDistributionCart.venOrderItem.orderItemId";

	public static String VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID = "VenBinCreditLimitEstimate.binCreditLimitEstimateId";
	public static String VENBINCREDITLIMITESTIMATE_BINNUMBER = "VenBinCreditLimitEstimate.binNumber";
	public static String VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE = "VenBinCreditLimitEstimate.creditLimitEstimate";
	public static String VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME = "VenBinCreditLimitEstimate.bankName";
	public static String VENBINCREDITLIMITESTIMATE_SEVERITY = "VenBinCreditLimitEstimate.severity";
	public static String VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID = "VenBinCreditLimitEstimate.venCardType.cardTypeId";
	public static String VENBINCREDITLIMITESTIMATE_ISACTIVE = "VenBinCreditLimitEstimate.isActive";
	public static String VENBINCREDITLIMITESTIMATE_DESCRIPTION = "VenBinCreditLimitEstimate.description";
	
//	public static String VENDISTRIBUTIONCART_ITEMDESCRIPTION = "VenDistributionCart.itemDescription"; //NOT IMPLEMENTED IN VENDISTRIBUTIONCART
	
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID = "VenOrderPaymentAllocation.venOrderPayment.orderPaymentId";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID = "VenOrderPaymentAllocation.venOrderPayment.wcsPaymentId";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER = "VenOrderPaymentAllocation.venOrderPayment.paymentConfirmationNumber";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME = "VenOrderPaymentAllocation.venOrderPayment.venBank.bankShortName";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK = "VenBinCreditLimitEstimate.bankName";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION = "VenBinCreditLimitEstimate.description";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT= "VenBinCreditLimitEstimate.creditLimitEstimate";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE = "VenOrderPaymentAllocation.venOrderPayment.venPaymentType.paymentTypeCode";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC = "VenOrderPaymentAllocation.venOrderPayment.venPaymentType.paymentTypeDesc";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC = "VenOrderPaymentAllocation.venOrderPayment.venWcsPaymentType.wcsPaymentTypeDesc";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_MASKEDCREDITCARDNUMBER = "VenOrderPaymentAllocation.venOrderPayment.maskedCreditCardNumber";	
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER = "VenOrderPaymentAllocation.venOrderPayment.virtualAccountNumber";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER = "VenOrderPaymentAllocation.venOrderPayment.vaorbiorccNumber";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT = "VenOrderPaymentAllocation.venOrderPayment.amount";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE = "VenOrderPaymentAllocation.venOrderPayment.handlingFee";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO = "VenOrderPaymentAllocation.venOrderPayment.otherInfo";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID = "VenOrderPaymentAllocation.venOrderPayment.referenceId";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_WCSORDERID = "VenOrderPaymentAllocation.venOrderPayment.oldVenOrder.wcsOrderId";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC = "VenOrderPaymentAllocation.venOrderPayment.oldVenOrder.venOrderPaymentAllocation.venOrderPayment.venWcsPaymentType.wcsPaymentTypeDesc";
	
	
	public static String VENORDERPAYMENT_TOTALTRANSACTION = "VenOrderPayment.totalTransaction";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID = "VenOrderPaymentAllocation.venOrderPayment.venPaymentStatus.paymentStatusId";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSCODE = "VenOrderPaymentAllocation.venOrderPayment.venPaymentStatus.paymentStatusCode";
	public static String VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC = "VenOrderPaymentAllocation.venOrderPayment.venPaymentStatus.paymentStatusDesc";
	public static String VENORDERPAYMENTALLOCATION_VENORDER_ORDERID = "VenOrderPaymentAllocation.venOrder.orderId";
	public static String VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID = "VenOrderPaymentAllocation.venOrder.wcsOrderId";
	public static String VENORDERPAYMENTALLOCATION_VENADDRESS = "VenOrderPaymentAllocation.venAddress";
	public static String VENORDERPAYMENTALLOCATION_VENADDRESS_ADDRESSID = "VenOrderPaymentAllocation.venOrderPayment.venAddress.addressId";
	
	public static String VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE = "VenOrderPaymentAllocation.venOrder.orderDate";	
	public static String VENORDERPAYMENTALLOCATION_VENORDER_AMOUNT = "VenOrderPaymentAllocation.venOrder.amount";
	public static String VENORDERPAYMENTALLOCATION_VENORDER_VENCUSTOMER_CUSTOMERUSERNAME = "VenOrderPaymentAllocation.venOrder.venCustomer.customerUserName";
	public static String VENORDERPAYMENTALLOCATION_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE = "VenOrderPaymentAllocation.venOrder.venOrderStatus.orderStatusCode";

	
	/*
	 * Commented after merge because defined twice 2011-10-20 DF
	 */

//	public static String FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID = "FinArFundsInReconRecord.reconciliationRecordId";
//	public static String FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION = "FinArFundsInReconRecord.finArFundsInReport.fileNameAndLocation";
//	public static String FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC = "FinArFundsInReconRecord.finArReconResult.reconResultDesc";
	
	public static String LOGAIRWAYBILL_DETAIL = "LogAirwayBill.detail";
	public static String LOGAIRWAYBILL_AIRWAYBILLID = "LogAirwayBill.airwayBillId";
	public static String LOGAIRWAYBILL_ACTUALPICKUPDATE = "LogAirwayBill.actualPickupDate";
	public static String LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID = "LogAirwayBill.venOrderItem.orderItemId";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID = "LogAirwayBill.venOrderItem.venOrder.wcsOrderId";
	public static String LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID = "LogAirwayBill.venOrderItem.wcsOrderItemId";
	public static String LOGAIRWAYBILL_GDNREFERENCE = "LogAirwayBill.gdnReference";
	public static String LOGAIRWAYBILL_ACTIVITYRESULTSTATUS = "LogAirwayBill.activityResultStatus";
	public static String LOGAIRWAYBILL_INVOICERESULTSTATUS = "LogAirwayBill.invoiceResultStatus";
	public static String LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP = "LogAirwayBill.airwayBillTimestamp";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME = "LogAirwayBill.venOrderItem.venMerchantProduct.venMerchant.venParty.fullOrLegalName";
	public static String LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME = "LogAirwayBill.venOrderItem.logMerchantPickupInstructions.venAddress.venCity.cityName";
	public static String LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME = "LogAirwayBill.airwayBillPickupDateTime";
	public static String LOGAIRWAYBILL_AIRWAYBILLNUMBER = "LogAirwayBill.airwayBillNumber";
	public static String LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE = "LogAirwayBill.logLogisticsProvider.logisticsProviderCode";
	public static String LOGAIRWAYBILL_LOGAPPROVALSTATUS1_APPROVALSTATUSID = "LogAirwayBill.logApprovalStatus1.approvalStatusId"; //FOR INVOICE RECONCILIATION
	public static String LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID = "LogAirwayBill.logApprovalStatus2.approvalStatusId"; //FOR ACTIVITY REPORT RECONCILIATION
	public static String LOGAIRWAYBILL_LOGAPPROVALSTATUS1_APPROVALSTATUSDESC = "LogAirwayBill.logApprovalStatus1.approvalStatusDesc"; //FOR INVOICE RECONCILIATION
	public static String LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC = "LogAirwayBill.logApprovalStatus2.approvalStatusDesc"; //FOR ACTIVITY REPORT RECONCILIATION
	public static String LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID = "LogAirwayBill.activityApprovedByUserId";
	public static String LOGAIRWAYBILL_INVOICEAPPROVEDBYUSERID = "LogAirwayBill.invoiceApprovedByUserId";
	public static String LOGAIRWAYBILL_INVOICEFILENAMEANDLOC = "LogAirwayBill.invoiceFileNameAndLoc";
	public static String LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC = "LogAirwayBill.activityFileNameAndLoc";
	public static String LOGAIRWAYBILL_INSUREDAMOUNT = "LogAirwayBill.insuredAmount";
	
	public static String LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE = "LogAirwayBill.venOrderItem.venOrder.orderDate";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID = "LogAirwayBill.venOrderItem.venOrderStatus.orderStatusId";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE = "LogAirwayBill.venOrderItem.venOrderStatus.orderStatusCode";
	public static String LOGAIRWAYBILL_DESTINATION = "LogAirwayBill.destination";
	public static String LOGAIRWAYBILL_ZIP = "LogAirwayBill.zip";
	public static String LOGAIRWAYBILL_SERVICE = "LogAirwayBill.venOrderItem.logLogisticService.logisticsServiceId";
	public static String LOGAIRWAYBILL_SERVICEDESC = "LogAirwayBill.venOrderItem.logLogisticsProvider.logisticProviderDesc";
	public static String LOGAIRWAYBILL_STATUS = "LogAirwayBill.status";
	public static String LOGAIRWAYBILL_RECEIVED = "LogAirwayBill.received";
	public static String LOGAIRWAYBILL_RECIPIENT = "LogAirwayBill.recipient";
	public static String LOGAIRWAYBILL_RELATION = "LogAirwayBill.relation";
	public static String LOGAIRWAYBILL_WEIGHT = "LogAirwayBill.packageWeight";	
	public static String LOGAIRWAYBILL_VENDISTRIBUTIONCART_DISTRIBUTIONCARTID = "LogAirwayBill.venDistributionCart.distributionCartId";
	public static String LOGAIRWAYBILL_TRACKINGNUMBER = "LogAirwayBill.trackingNumber";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU = "LogAirwayBill.venOrderItem.venMerchantProduct.wcsProductSku";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME = "LogAirwayBill.venOrderItem.venMerchantProduct.wcsProductName";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENPARTYADDRESS_VENADDRESS = "LogAirwayBill.venOrderItem.venMerchantProduct.venMerchant.venParty.venPartyAddress";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENCONTACTDETAIL_PHONE = "LogAirwayBill.venOrderItem.venMerchantProduct.venMerchant.venParty.venContactDetail.phone";
	public static String LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS = "LogMerchantPickupInstructions.venAddress";
	public static String LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPIC = "LogMerchantPickupInstructions.merchantPic";
	public static String LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPICPHONE = "LogMerchantPickupInstructions.merchantPicPhone";
	public static String LOGAIRWAYBILL_VENORDERITEM_QUANTITY = "LogAirwayBill.venOrderItem.quantity";
	public static String LOGAIRWAYBILL_VENDISTRIBUTIONCART_QUANTITY = "LogAirwayBill.venDistributionCart.quantity";
	public static String LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT = "LogAirwayBill.venDistributionCart.packageWeight";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME = "LogAirwayBill.venOrderItem.venRecipient.venParty.fullOrLegalName";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENPARTYADDRESS_VENADDRESS = "LogAirwayBill.venOrderItem.venRecipient.venParty.venPartyAddress";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_PHONE = "LogAirwayBill.venOrderItem.venRecipient.venParty.venContactDetail.phone";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_MOBILE = "LogAirwayBill.venOrderItem.venRecipient.venParty.venContactDetail.mobile";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_FULLORLEGALNAME = "LogAirwayBill.venOrderItem.venCustomer.venParty.fullOrLegalName";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENPARTYADDRESS_VENADDRESS = "LogAirwayBill.venOrderItem.venCustomer.venParty.venPartyAddress";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_PHONE = "LogAirwayBill.venOrderItem.venCustomer.venParty.venContactDetail.phone";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_MOBILE = "LogAirwayBill.venOrderItem.venCustomer.venParty.venContactDetail.mobile";
	public static String LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_EMAIL = "LogAirwayBill.venOrderItem.venCustomer.venParty.venContactDetail.email";
	public static String LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE = "LogAirwayBill.venOrderItem.logMerchantPickupInstructions.pickupDateTime";
	public static String LOGAIRWAYBILL_VENORDERITEM_GIFTWRAP = "LogAirwayBill.venOrderItem.giftWrapFlag";
	public static String LOGAIRWAYBILL_VENORDERITEM_GIFTCARD = "LogAirwayBill.venOrderItem.giftCardFlag";
	public static String LOGAIRWAYBILL_VENORDERITEM_GIFTNOTE = "LogAirwayBill.venOrderItem.giftCardNote";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTCOURIER = "LogAirwayBill.venOrderItem.merchantCourier";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTDELIVEREDDATESTARTEND = "LogAirwayBill.venOrderItem.merchantDeliveredDateStart-merchantDeliveredDateEnd";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALATIONDATESTARTEND = "LogAirwayBill.venOrderItem.merchantInstallationDateStart-merchantInstallationDateEnd";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLOFFICER = "LogAirwayBill.venOrderItem.merchantInstallOfficer";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLMOBILE = "LogAirwayBill.venOrderItem.merchantInstallMobile";
	public static String LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLNOTE = "LogAirwayBill.venOrderItem.merchantInstallNote";
	public static String LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE = "LogAirwayBill.venOrderItem.minEstDate";
	public static String LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE = "LogAirwayBill.venOrderItem.maxEstDate";
		
	public static String LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_SPECIALHANDLING = "LogAirwayBill.venOrderItem.logMerchantPickupInstructions.specialHandlingInstructions";
	public static String LOGAIRWAYBILL_TOTALCHARGE="LogAirwayBill.totalCharge";
	public static String LOGAIRWAYBILL_SHIPPINGCHARGE="LogAirwayBill.venOrderItemshippingCost";
	public static String LOGAIRWAYBILL_INSURANCECHARGE="LogAirwayBill.insuranceCharge";
	public static String LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU="LogAirwayBill.otherCharge";
	public static String LOGAIRWAYBILL_GIFTWRAPCHARGE="LogAirwayBill.giftWrapCharge";
	
	public static String LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID = "LogInvoiceAirwaybillRecord.invoiceAirwaybillRecordId";
	public static String LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER = "LogInvoiceAirwaybillRecord.airwayBillNumber";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE= "LogInvoiceAirwaybillRecord.veniceOtherCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE= "LogInvoiceAirwaybillRecord.providerOtherCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDOTHERCHARGE= "LogInvoiceAirwaybillRecord.approvedOtherCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT = "LogInvoiceAirwaybillRecord.venicePackageWeight";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDERPACKAGEWEIGHT = "LogInvoiceAirwaybillRecord.providerPackageWeight";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDPACKAGEWEIGHT = "LogInvoiceAirwaybillRecord.approvedPackageWeight";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG = "LogInvoiceAirwaybillRecord.venicePricePerKg";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG = "LogInvoiceAirwaybillRecord.providerPricePerKg";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDPRICEPERKG = "LogInvoiceAirwaybillRecord.approvedPricePerKg";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE = "LogInvoiceAirwaybillRecord.veniceGiftWrapCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE = "LogInvoiceAirwaybillRecord.providerGiftWrapCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDGIFTWRAPCHARGE = "LogInvoiceAirwaybillRecord.approvedGiftWrapCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE = "LogInvoiceAirwaybillRecord.veniceInsuranceCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE = "LogInvoiceAirwaybillRecord.providerInsuranceCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDINSURANCECHARGE = "LogInvoiceAirwaybillRecord.approvedInsuranceCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE = "LogInvoiceAirwaybillRecord.veniceTotalCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE = "LogInvoiceAirwaybillRecord.providerTotalCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_APPROVEDTOTALCHARGE = "LogInvoiceAirwaybillRecord.approvedTotalCharge";
	public static String LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS = "LogInvoiceAirwaybillRecord.invoiceResultStatus";
	public static String LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID = "LogInvoiceAirwaybillRecord.logInvoiceReportUpload.logApprovalStatus.approvalStatusId";
	public static String LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC = "LogInvoiceAirwaybillRecord.logInvoiceReportUpload.logApprovalStatus.approvalStatusDesc";
	public static String LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID = "LogInvoiceAirwaybillRecord.logInvoiceReportUpload.invoiceReportUploadId";
	
	public static String LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID = "LogInvoiceReportUpload.invoiceReportUploadId";
	public static String LOGINVOICEREPORTUPLOAD_INVOICENUMBER = "LogInvoiceReportUpload.invoiceNumber";
	public static String LOGINVOICEREPORTUPLOAD_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE = "LogInvoiceReportUpload.logLogisticsProvider.logisticsProviderCode";
	public static String LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION = "LogInvoiceReportUpload.fileNameAndLocation";
	public static String LOGINVOICEREPORTUPLOAD_REPORTRECONCILIATIONTIMESTAMP = "LogInvoiceReportUpload.reportReconciliationTimestamp";
	public static String LOGINVOICEREPORTUPLOAD_USERLOGONNAME = "LogInvoiceReportUpload.userLogonName";
	public static String LOGINVOICEREPORTUPLOAD_INVOICERECONTOLERANCE = "LogInvoiceReportUpload.invoiceReconTolerance";
	public static String LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID = "LogInvoiceReportUpload.logApprovalStatus.approvalStatusId";
	public static String LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC = "LogInvoiceReportUpload.logApprovalStatus.approvalStatusDesc";
	public static String LOGINVOICEREPORTUPLOAD_APPROVEDBY = "LogInvoiceReportUpload.approvedBy";	
	
	public static String LOGFILEUPLOADLOG_FILEUPLOADLOGID = "LogFileUploadLog.fileUploadLogId";
	public static String LOGFILEUPLOADLOG_FILEUPLOADNAME = "LogFileUploadLog.fileUploadName";
	public static String LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC = "LogFileUploadLog.fileUploadNameAndLoc";
	public static String LOGFILEUPLOADLOG_ACTUALFILEUPLOADNAME = "LogFileUploadLog.actualFileUploadName";
	public static String LOGFILEUPLOADLOG_FILEUPLOADFORMAT = "LogFileUploadLog.fileUploadFormat";
	public static String LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAME = "LogFileUploadLog.failedFileUploadName";
	public static String LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC = "LogFileUploadLog.failedFileUploadNameAndLoc";
	public static String LOGFILEUPLOADLOG_UPLOADSTATUS = "LogFileUploadLog.uploadStatus";
	public static String LOGFILEUPLOADLOG_TIMESTAMP = "LogFileUploadLog.uploadTimestamp";
	public static String LOGFILEUPLOADLOG_USERNAME = "LogFileUploadLog.username";
	
	public static String LOGINVOICEUPLOADLOG_INVOICEUPLOADLOGID = "LogInvoiceUploadLog.invoiceUploadLogId";
	public static String LOGINVOICEUPLOADLOG_INVOICENUMBER = "LogInvoiceUploadLog.invoiceNumber";
	public static String LOGINVOICEUPLOADLOG_FILEUPLOADNAME = "LogInvoiceUploadLog.fileUploadName";
	public static String LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC = "LogInvoiceUploadLog.fileUploadNameAndLoc";
	public static String LOGINVOICEUPLOADLOG_ACTUALFILEUPLOADNAME = "LogInvoiceUploadLog.actualFileUploadName";
	public static String LOGINVOICEUPLOADLOG_FILEUPLOADFORMAT = "LogInvoiceUploadLog.fileUploadFormat";
	public static String LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAME = "LogInvoiceUploadLog.failedFileUploadName";
	public static String LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC = "LogInvoiceUploadLog.failedFileUploadNameAndLoc";
	public static String LOGINVOICEUPLOADLOG_UPLOADSTATUS = "LogInvoiceUploadLog.uploadStatus";
	public static String LOGINVOICEUPLOADLOG_TIMESTAMP = "LogInvoiceUploadLog.uploadTimestamp";
	public static String LOGINVOICEUPLOADLOG_UPLOADEDBY = "LogInvoiceUploadLog.uploadedBy";
	
	public static String VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSDESC="VenOrderItemStatusHistory.venOrderStatus.orderStatusShortDesc";
	public static String VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP="VenOrderItemStatusHistory.historyTimestamp";
	
	public static String LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID = "LogActivityReconRecord.activityReconRecordId";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC = "LogActivityReconRecord.logReconActivityRecordResult.reconRecordResultDesc";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID = "LogActivityReconRecord.logReconActivityRecordResult.reconRecordResultId";
	public static String LOGACTIVITYRECONRECORD_VENICEDATA = "LogActivityReconRecord.veniceData";
	public static String LOGACTIVITYRECONRECORD_PROVIDERDATA = "LogActivityReconRecord.providerData";
	public static String LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA = "LogActivityReconRecord.manuallyEnteredData";
	public static String LOGACTIVITYRECONRECORD_USERLOGONNAME = "LogActivityReconRecord.userLogonName";
	public static String LOGACTIVITYRECONRECORD_COMMENT = "LogActivityReconRecord.comment";
	public static String LOGACTIVITYRECONRECORD_COMMENTHISTORY = "LogActivityReconRecord.commenthistory";
	public static String LOGACTIVITYRECONRECORD_LOGAIRWAYBILL_AIRWAYBILLID = "LogActivityReconRecord.logAirwayBill.airwayBillId";
	public static String LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID = "LogActivityReconRecord.logActionApplied.actionAppliedId";
	public static String LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC = "LogActivityReconRecord.logActionApplied.actionAppliedDesc";
	public static String LOGACTIVITYRECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP = "LogActivityReconCommentHistory.id.historyTimestamp";
	public static String LOGACTIVITYRECONCOMMENTHISTORY_COMMENT = "LogActivityReconCommentHistory.comment";
	public static String LOGACTIVITYRECONCOMMENTHISTORY_USERLOGONNAME = "LogActivityReconCommentHistory.userLogonName";
		
	public static String LOGINVOICERECONRECORD_INVOICERECONRECORDID = "LogInvoiceReconRecord.invoiceReconRecordId";
	public static String LOGINVOICERECONRECORD_VENICEDATA = "LogInvoiceReconRecord.veniceData";
	public static String LOGINVOICERECONRECORD_PROVIDERDATA = "LogInvoiceReconRecord.providerData";
	public static String LOGINVOICERECONRECORD_MANUALLYENTEREDDATA = "LogInvoiceReconRecord.manuallyEnteredData";
	public static String LOGINVOICERECONRECORD_USERLOGONNAME = "LogInvoiceReconRecord.userLogonName";
	public static String LOGINVOICERECONRECORD_COMMENT = "LogInvoiceReconRecord.comment";
	public static String LOGINVOICERECONRECORD_COMMENTHISTORY = "LogInvoiceReconRecord.commenthistory";
	public static String LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID = "LogInvoiceReconRecord.logActionApplied.actionAppliedId";
	public static String LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC = "LogInvoiceReconRecord.logActionApplied.actionAppliedDesc";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID = "LogInvoiceReconRecord.logReconInvoiceRecordResult.reconRecordResultId";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC = "LogInvoiceReconRecord.logReconInvoiceRecordResult.reconRecordResultDesc";
	public static String LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER = "LogInvoiceReconRecord.logInvoiceAirwaybillRecord.airwayBillNumber";
	public static String LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID = "LogInvoiceReconRecord.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId";
	
	public static String LOGINVOICERECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP = "LogInvoiceReconCommentHistory.id.historyTimestamp";
	public static String LOGINVOICERECONCOMMENTHISTORY_COMMENT = "LogInvoiceReconCommentHistory.comment";
	public static String LOGINVOICERECONCOMMENTHISTORY_USERLOGONNAME = "LogInvoiceReconCommentHistory.userLogonName";
	
	//Logistics Module - Provider Management Name Tokens
	public static String LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID = "LogLogisticsProvider.logisticsProviderId";
	public static String LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE = "LogLogisticsProvider.logisticsProviderCode";
	public static String LOGLOGISTICSPROVIDER_VENPARTY_PARTYID = "LogLogisticsProvider.venParty.partyId";
	public static String LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME = "LogLogisticsProvider.venParty.fullOrLegalName";
	public static String LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID = "LogLogisticsProvider.venParty.venPartyType.partyTypeId";
	public static String LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC = "LogLogisticsProvider.venParty.venPartyType.partyTypeDesc";
	public static String LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID = "LogLogisticsProvider.logReportTemplate2.templateId";
	public static String LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC = "LogLogisticsProvider.logReportTemplate2.templateDesc";
	public static String LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID = "LogLogisticsProvider.logReportTemplate1.templateId";
	public static String LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC = "LogLogisticsProvider.logReportTemplate1.templateDesc";

	public static String LOGREPORTTEMPLATE_TEMPLATEID = "LogReportTemplate.templateId";
	public static String LOGREPORTTEMPLATE_TEMPLATEDESC = "LogReportTemplate.templateDesc";
	
	//Logistics Module - Provider Management - Service Tab Name Tokens
	public static String LOGLOGISTICSERVICE_LOGISTICSSERVICEID = "LogLogisticService.logisticsServiceId";	
	public static String LOGLOGISTICSERVICE_SERVICECODE = "LogLogisticService.serviceCode";
	public static String LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC = "LogLogisticService.logisticsServiceDesc";
	public static String LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID = "LogLogisticService.logLogisticsProvider.logisticsProviderId";
	public static String LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID = "LogLogisticService.logLogisticsServiceType.logisticsServiceTypeId";	
	public static String LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC = "LogLogisticService.logLogisticsServiceType.logisticsServiceTypeDesc";	

	public static String LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID = "LogLogisticsServiceType.logisticsServiceTypeId";	
	public static String LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC = "LogLogisticsServiceType.logisticsServiceTypeDesc";	
	public static String LOGLOGISTICSSERVICETYPE_EXPRESSFLAG = "LogLogisticsServiceType.expressFlag";	
	
	public static String LOGPICKUPSCHEDULE_PICKUPSCHEDULESID = "LogPickupSchedule.pickupSchedulesId";
	public static String LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID = "LogPickupSchedule.logLogisticsProvider.logisticsProviderId";
	public static String LOGPICKUPSCHEDULE_FROMTIME = "LogPickupSchedule.fromTime";
	public static String LOGPICKUPSCHEDULE_TOTIME = "LogPickupSchedule.toTime";
	public static String LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC = "LogPickupSchedule.pickupScheduleDesc";
	public static String LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS = "LogPickupSchedule.includePublicHolidays";	
	public static String LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID = "LogPickupSchedule.logScheduleDayOfWeeks.scheduleDayId";
	public static String LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC = "LogPickupSchedule.logScheduleDayOfWeeks.scheduleDayDesc";	

	public static String LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID = "LogScheduleDayOfWeeks.scheduleDayId";
	public static String LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC = "LogScheduleDayOfWeeks.scheduleDayDesc";	
	
	public static String LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID = "LogProviderAgreement.providerAgreementId";	
	public static String LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID = "LogProviderAgreement.logLogisticsProvider.logisticsProviderId";	
	public static String LOGPROVIDERAGREEMENT_AGREEMENTDESC= "LogProviderAgreement.agreementDesc";
	public static String LOGPROVIDERAGREEMENT_AGREEMENTDATE = "LogProviderAgreement.agreementDate";
	public static String LOGPROVIDERAGREEMENT_EXPIRYDATE = "LogProviderAgreement.expiryDate";
	public static String LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT = "LogProviderAgreement.discountLevelPct";	
	public static String LOGPROVIDERAGREEMENT_PPNPERCENTAGE = "LogProviderAgreement.ppnPercentage";	
	public static String LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT = "LogProviderAgreement.pickupTimeCommitment";	
	public static String LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT = "LogProviderAgreement.deliveryTimeCommitment";	
	
	public static String VENCITY_CITYID = "VenCity.cityId";
	public static String VENCITY_CITYCODE = "VenCity.cityCode";	
	public static String VENCITY_CITYNAME = "VenCity.cityName";	
	
	public static String VENSTATE_STATEID = "VenState.stateId";
	public static String VENSTATE_STATECODE = "VenState.stateCode";	
	public static String VENSTATE_STATENAME = "VenState.stateName";	
	
	public static String VENCOUNTRY_COUNTRYID = "VenCountry.countryId";
	public static String VENCOUNTRY_COUNTRYCODE = "VenCountry.countryCode";	
	public static String VENCOUNTRY_COUNTRYNAME = "VenCountry.countryName";	
	
	public static String VENADDRESSTYPE_ADDRESSTYPEID = "VenAddressType.addressTypeId";
	public static String VENADDRESSTYPE_ADDRESSTYPEDESC = "VenAddressType.addressTypeDesc";	

	public static String VENPARTY_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID = "VenParty.venContactDetailType.contactDetailTypeId";
	public static String VENPARTY_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC = "VenParty.venContactDetailType.contactDetailTypeDesc";
	

	//Finance Module Name Tokens
	public static String FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID = "FinArFundsInReconRecord.reconciliationRecordId";
	public static String FINARFUNDSINRECONRECORD_WCSORDERID = "FinArFundsInReconRecord.wcsOrderId";
	public static String FINARFUNDSINRECONRECORD_ORDERDATE = "FinArFundsInReconRecord.orderDate";
	public static String FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE = "FinArFundsInReconRecord.reconcilliationRecordTimestamp";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID = "FinArFundsInReconRecord.venOrderPayment.venBank.bankId";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME = "FinArFundsInReconRecord.venOrderPayment.venBank.bankShortName";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION = "FinArFundsInReconRecord.finArFundsInReport.fileNameAndLocation";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID = "FinArFundsInReconRecord.venOrderPayment.orderPaymentId";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID = "FinArFundsInReconRecord.venOrderPayment.wcsPaymentId";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID = "FinArFundsInReconRecord.venOrderPayment.venWcsPaymentType.wcsPaymentTypeId";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC = "FinArFundsInReconRecord.venOrderPayment.venWcsPaymentType.wcsPaymentTypeDesc";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID = "FinArFundsInReconRecord.finArFundsInReport.finArFundsInReportType.paymentReportTypeId";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID= "FinArFundsInReconRecord.finArFundsInReport.paymentReportId";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID= "FinArFundsInReconRecord.finArFundsIdReportTime.reportTimeId";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC= "FinArFundsInReconRecord.finArFundsIdReportTime.reportTimeDesc";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC = "FinArFundsInReconRecord.finArFundsInReport.finArFundsInReportType.paymentReportTypeDesc";
	public static String FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT = "FinArFundsInReconRecord.venOrderPayment.amount";
	public static String FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT = "FinArFundsInReconRecord.paymentAmount";
	public static String FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT = "FinArFundsInReconRecord.providerReportPaidAmount";
	public static String FINARFUNDSINRECONRECORD_REFUNDAMOUNT = "FinArFundsInReconRecord.refundAmount";
	public static String FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT = "FinArFundsInReconRecord.remainingBalanceAmount";
	public static String FINARFUNDSINRECONRECORD_NOMOR_REFF = "FinArFundsInReconRecord.nomorReff";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID ="FinArFundsInReconRecord.finArFundsInActionApplied.actionAppliedId";
	public static String FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC ="FinArFundsInReconRecord.finArFundsInActionApplied.actionAppliedDesc";
	public static String FINARFUNDSINRECONRECORD_STATUSORDER ="FinArFundsInReconRecord.statusorder";
	public static String FINARFUNDSINRECONRECORD_FETCH ="fetch";
	//Aging shall be calculated by client
	public static String FINARFUNDSINRECONRECORD_AGING = "FinArFundsInReconRecord.finArReconResult.aging";
	public static String FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC = "FinArFundsInReconRecord.finArReconResult.reconResultDesc";
	public static String FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC = "FinArFundsInReconRecord.finApprovalStatus.approvalStatusDesc";
	public static String FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID = "FinArFundsInReconRecord.finApprovalStatus.approvalStatusId";
	//Reconciliation Status shall be managed by client
	public static String FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS = "FinArFundsInReconRecord.reconciliationStatus";
	public static String FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT = "FinArFundsInReconRecord.providerReportFeeAmount";
	public static String FINARFUNDSINRECONRECORD_COMMENT = "FinArFundsInReconRecord.comment";
	public static String FINARFUNDSINRECONRECORD_COMMENTHISTORY = "FinArFundsInReconRecord.commenthistory";
	
	//Refund Party is for Refund Window
	public static String FINARFUNDSINRECONRECORD_REFUNDPARTY = "FinArFundsInReconRecord.refundParty";
	
	public static String VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID = "VenOrderPayment.venOrderPaymentAllocation.venOrder.orderId";
	
	public static String VENORDERPAYMENT_ORDERPAYMENTID = "VenOrderPayment.orderPaymentId";
	public static String VENORDERPAYMENT_WCSPAYMENTID = "VenOrderPayment.wcsPaymentId";
	public static String VENORDERPAYMENT_AMOUNT = "VenOrderPayment.amount";
	public static String VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC = "VenOrderPayment.venPaymentType.paymentTypeDesc";
	public static String VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC = "VenOrderPayment.venPaymentType.paymentStatusDesc";
	
	public static String FINARFUNDSINRECONCOMMENT_ID_RECONCILIATIONRECORDID = "FinArFundsInReconComment.id.reconciliationRecordId";
	public static String FINARFUNDSINRECONCOMMENT_ID_COMMENTTIMESTAMP = "FinArFundsInReconComment.id.commentTimestamp";
	public static String FINARFUNDSINRECONCOMMENT_COMMENT = "FinArFundsInReconComment.comment";
	public static String FINARFUNDSINRECONCOMMENT_USERLOGONNAME = "FinArFundsInReconComment.userLogonName";
	
	
	//Merchant Payment
	public static String FINSALESRECORD_MCX_DATE = "FinSalesRecord.cxMtaDate";
	public static String FINSALESRECORD_CXF_DATE = "FinSalesRecord.cxFinanceDate";
	public static String FINSALESRECORD_MERCHANTPAYMENTAMOUNT = "FinSalesRecords.merchantPaymentAmount";
	public static String FINAPPAYMENT_APPAYMENTID = "FinApPayment.apPaymentId";
	public static String FINAPPAYMENT_VENPARTY_FULLORLEGALNAME = "FinApPayment.venParty.fullOrLegalName";
	public static String FINAPPAYMENT_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC= "FinApPayment.venParty.venPartyType.partyTypeDesc";
	public static String FINAPPAYMENT_AMOUNT = "FinApPayment.amount";
	public static String FINAPPAYMENT_PENALTYAMOUNT = "FinApPayment.penaltyAmount";
	public static String FINAPPAYMENT_PPH23_AMOUNT = "FinApPayment.pph23Amount";
	
	//This is handled in the client/server side by code
	public static String FINAPPAYMENT_BALANCE = "FinApPayment.balance";
	public static String FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS = "FinApPayment.finApManualJournalTransactions";
	public static String FINAPPAYMENT_FINSALESRECORDS = "FinApPayment.finSalesRecords";

	public static String FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC = "FinApPayment.finAccount.accountDesc";
	public static String FINAPPAYMENT_FINACCOUNT_ACCOUNTID = "FinApPayment.finAccount.accountId";
	
	//Logistics Payment
	public static String FINAPINVOICE_APINVOICEID = "FinApInvoice.apInvoiceId";
	public static String FINAPINVOICE_LOGINVOICEREPORTUPLOADS_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE = "FinApInvoice.logInvoiceReportUploads.logLogisticsProvider.logisticsProviderCode";
	public static String FINAPINVOICE_VENPARTY_FULLORLEGALNAME = "FinApInvoice.venParty.fullOrLegalName";
	public static String FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER = "FinApInvoice.logInvoiceReportUploads.invoiceNumber";
	public static String FINAPINVOICE_INVOICEDATE = "FinApInvoices.invoiceDate";
	public static String FINAPINVOICE_INVOICEAMOUNT = "FinApInvoices.invoiceAmount";
	public static String FINAPINVOICE_INVOICEARAMOUNT = "FinApInvoices.arAmount";
	
	//This is handled in the client/server side by code
	public static String FINAPPAYMENT_FINAPINVOICES = "FinApPayment.finApInvoices";
	
	//Refund Payment
	public static String FINARFUNDSINREFUND_REFUNDRECORDID = "FinArFundsInRefund.refundRecordId";
	//Handled in query in server
	public static String FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_WCSCUSTOMERID = "FinArFundsInRefund.venOrder.venCustomer.wcsCustomerId";
	public static String FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_WCSORDERID = "FinArFundsInRefund.finArFundsInReconRecord.wcsOrderId";
	public static String FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE = "FinArFundsInRefund.finArFundsInReconRecord.orderDate";
	public static String FINARFUNDSINREFUND_APAMOUNT = "FinArFundsInRefund.apAmount";
	public static String FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID = "FinArFundsInRefund.finArFundsInReconRecord.reconciliationRecordId";
	public static String FINARFUNDSINREFUND_REFUNDTIMESTAMP = "FinArFundsInRefund.refundTimestamp";	
	public static String FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME = "FinArFundsInRefund.venOrder.venCustomer.venParty.fullOrLegalName";
	public static String FINARFUNDSINREFUND_ACTION_TAKEN = "FinArFundsInRefund.refundType";
	public static String FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE = "FinArFundsInRefund.finArFundsInReconRecord.providerReportFeeAmount";
	public static String FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_REASON = "FinArFundsInRefund.finArFundsInReconRecord.comment";
	
	//This is handled in the client/server side by code
	public static String FINAPPAYMENT_FINARFUNDSINREFUNDS = "FinApPayment.finArFundsInRefunds";
	
	//Fin Ap Manual Journal Transaction for pop up dialog in Payment Processing Screen
	public static String FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID = "finApManualJournalTransaction.manualJournalTransactionId";
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT = "finApManualJournalTransaction.finJournalTransaction.transactionAmount";
	public static String FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME = "finApManualJournalTransaction.venParty.fullOrLegalName";
	public static String FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID = "finApManualJournalTransaction.venOrder.wcsOrderId";
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS = "finApManualJournalTransaction.finJournalTransaction.comments";
	
	//Payment Screen
	public static String FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC = "FinApPayment.finApprovalStatus.approvalStatusDesc";	
	
	//Journal Screen
	public static String FINJOURNALAPPROVALGROUP_JOURNALGROUPID = "FinJournalApprovalGroup.journalGroupId";
	public static String FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID = "FinJournalApprovalGroup.finJournal.journalId";
	public static String FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC = "FinJournalApprovalGroup.finJournal.journalDesc";
	public static String FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP = "FinJournalApprovalGroup.journalGroupTimestamp";
	public static String FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC = "FinJournalApprovalGroup.journalGroupDesc";
	public static String FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC = "FinJournalApprovalGroup.finApprovalStatus.approvalStatusDesc";
	
	//Journal Screen - Journal Detail
	public static String FINJOURNALTRANSACTION_TRANSACTIONID = "FinJournalTransaction.transactionId";
	public static String FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC = "FinJournalTransaction.finJournalApprovalGroup.journalGroupDesc";
	public static String FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTDESC = "FinJournalTransaction.finAccount.accountDesc";
	public static String FINJOURNALTRANSACTION_TRANSACTIONTIMESTAMP = "FinJournalTransaction.transactionTimestamp";
	
	public static String FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPID= "FinJournalTransaction.finJournalApprovalGroup.journalGroupId";
	
	//Processed in the server and client side
	public static String FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT = "FinJournalTransaction.debitTransactionAmount";
	public static String FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT = "FinJournalTransaction.creditTransactionAmount";
	
	public static String FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC = "FinJournalTransaction.finTransactionStatus.transactionStatusDesc";
	public static String FINJOURNALTRANSACTION_COMMENTS = "FinJournalTransaction.comments";
	public static String FINJOURNALTRANSACTION_REFF = "FinJournalTransaction.wcsOrderID";
	public static String FINJOURNALTRANSACTION_PAYMENT_TYPE = "FinJournalTransaction.paymenttype";
	public static String FINJOURNALTRANSACTION_GROUP_JOURNAL = "FinJournalTransaction.groupJournal";
	
	//Manual Journal Screen - Manual Journal Detail
	public static String FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID = "FinApManualJournalTransaction.venOrder.orderId";
	public static String FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID = "FinApManualJournalTransaction.venParty.partyId";
	
	//Processed in the server and client side
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC = "FinApManualJournalTransaction.FinJournalTransaction.finAccount.accountNumberDesc";
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT = "FinApManualJournalTransaction.FinJournalTransaction.debitTransactionAmount";
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT = "FinApManualJournalTransaction.FinJournalTransaction.creditTransactionAmount";
	
	public static String FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC = "FinApManualJournalTransaction.FinJournalTransaction.finTransactionStatus.transactionStatusDesc";

	public static String FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD = "FinArFundsInAllocatePayment.idReconRecord";
	public static String FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE = "FinArFundsInAllocatePayment.idReconRecordSource";
	public static String FINARFUNDSINALLOCATEPAYMENT_AMOUNT = "FinArFundsInAllocatePayment.amount";
	public static String FINARFUNDSINALLOCATEPAYMENT_DEST_ORDER = "Dest.order";
		
	//To Do List Name Tokens
	public static String TASKID = "taskId";
	public static String TASKTYPE = "taskType";
	//Added in to support passing of task type to enable differentiation between tasks
	public static final String TASKTYPEID = "taskTypeId";
	public static String TASKDESCRIPTION = "taskDescription";
	public static String TASKASSIGNEE = "taskAssignee";
	public static String TASKCREATEDDATE = "taskCreatedDate";
	public static String TASKDUEDATE = "taskDueDate";
	public static String TASKSTATUS = "taskStatus";
	public static String TASKABLETOESCALATE = "taskAbleToEscalate";
	
	//Finance - Promotion Name Tokens 
	public static String VENPROMOTION_PROMOTIONID = "VenPromotion.promotionId";
	public static String VENPROMOTION_PROMOTIONCODE = "VenPromotion.promotionCode";
	public static String VENPROMOTION_PROMOTIONNAME = "VenPromotion.promotionName";
	public static String VENPROMOTION_VENPROMOTIONTYPE_PROMOTIONTYPEDESC = "VenPromotion.venPromotionType.promotionTypeDesc";
	public static String VENPROMOTION_GDNMARGIN = "VenPromotion.gdnMargin";
	public static String VENPROMOTION_MERCHANTMARGIN = "VenPromotion.merchantMargin";
	public static String VENPROMOTION_OTHERSMARGIN = "VenPromotion.othersMargin";
	
	//Finance - Promotion - Promotion Share Calc Method Combobox Name Tokens
	public static String VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID = "VenPromotionShareCalcMethodId.promotionCalcMethodId";
	public static String VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC = "VenPromotionShareCalcMethodId.promotionCalcMethodDesc";
	
	//Finance - Promotion - Party Promotion Share Detail Name Tokens
	public static String VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID = "VenPartyPromotionShare.venParty.partyId";
	public static String VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID = "VenPartyPromotionShare.venPromotion.promotionId";
	public static String VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME = "VenPartyPromotionShare.venParty.fullOrLegalName";
	public static String VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID = "VenPartyPromotionShare.venPromotionShareCalcMethod.promotionCalcMethodId";
	public static String VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC = "VenPartyPromotionShare.venPromotionShareCalcMethod.promotionCalcMethodDesc";
	public static String VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE = "VenPartyPromotionShare.promotionCalcValue";
	public static String VENPARTYPROMOTIONSHARE_PROMOTIONTYPEID = "VenPartyPromotionShare.promotionTypeID";
	public static String VENPARTYPROMOTIONSHARE_PROMOTIONTYPE = "VenPartyPromotionShare.promotionType";
	
	//Administration - Role
	public static String RAFROLE_ROLEID = "RafRole.roleId";
	public static String RAFROLE_ROLENAME = "RafRole.roleName";
	public static String RAFROLE_ROLEDESC = "RafRole.roleDesc";
	public static String RAFROLE_PARENTROLE = "RafRole.rafRole.roleId";
	
	//Administration - Role User Detail
	public static String RAFROLE_RAFUSERROLES_USERID = "RafRole.rafUserRoles.userId";
	public static String RAFROLE_RAFUSERROLES_ROLEID = "RafRole.rafUserRoles.roleId";
	
	//Administration - Role Profile Detail
	public static String RAFROLEPROFILE_RAFROLEPROFILEID = "RafRoleProfile.rafRoleProfileId";
	public static String RAFROLE_RAFROLEPROFILES_PROFILEID = "RafRole.rafRoleProfiles.profileId";
	public static String RAFROLE_RAFROLEPROFILES_ROLEID = "RafRole.rafRoleProfiles.roleId";
	
	//Administration - User
	public static String RAFUSER_USERID = "RafUser.userId";
	public static String RAFUSER_LOGINNAME = "RafUser.loginName";
	
	//Administration - User Role Detail
	public static String RAFUSERROLE_RAFUSERROLEID = "RafUserRole.rafUserRoleId";
	public static String RAFUSER_RAFUSERROLES_USERID = "RafUser.rafUserRoles.userId";
	public static String RAFUSER_RAFUSERROLES_ROLEID = "RafUser.rafUserRoles.roleId";
	
	//Administration - User Group Detail
	public static String RAFUSERGROUP_RAFUSERGROUPID = "RafUserGroup.rafUserGroupId";
	public static String RAFUSER_RAFUSERGROUP_USERID = "RafUser.rafUserGroupMemberships.userId";
	public static String RAFUSER_RAFUSERGROUP_GROUPID = "RafUser.rafUserMembership.groupId";
	
	//Administration - Profile
	public static String RAFPROFILE_PROFILEID = "RafProfile.profileId";
	public static String RAFPROFILE_PROFILENAME = "RafProfile.profileName";
	public static String RAFPROFILE_PROFILEDESC = "RafProfile.profileDesc";
	
	//Administration - Profile Detail
	public static String RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID = "RafProfilePermission.rafProfilePermissionId";
	public static String RAFPROFILEPERMISSION_PROFILEID = "RafProfilePermission.rafProfile.profileId";
	public static String RAFPROFILEPERMISSION_APPLICATIONOBJECTID = "RafProfilePermission.rafApplicationObject.applicationObjectId";
	public static String RAFPROFILEPERMISSION_PERMISSIONTYPEID = "RafProfilePermission.rafPermissionType.permissionTypeId";
	
	//Administration - Group
	public static String RAFGROUP_GROUPID = "RafGroup.groupId";
	public static String RAFGROUP_GROUPNAME = "RafGroup.groupName";
	public static String RAFGROUP_GROUPDESC = "RafGroup.groupDesc";
	
	//Administration - Group Detail
	public static String RAFGROUPROLE_RAFGROUPROLEID = "RafGroupRole.rafGroupRoleId";
	public static String RAFGROUP_RAFGROUPROLES_GROUPID = "RafGroup.rafGroupRoles.groupId";
	public static String RAFGROUP_RAFGROUPROLES_ROLEID = "RafGroup.rafGroupRoles.roleId";
	
	//Administration - Module Configuration
	public static String RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID = "RafApplicationObject.applicationObjectId";
	public static String RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID = "RafApplicationObject.applicationObjectUuid";
	public static String RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME = "RafApplicationObject.applicationObjectCanonicalName";
	public static String RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID = "RafApplicationObject.rafApplicationObjectType.applicationObjectTypeId";
	public static String RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID = "RafApplicationObject.rafApplicationObject.applicationObjectId";
	
	//KPI - Kpi_measurement_period screen by arifin
	public static String KPIMEASUREMENTPERIOD_PERIODID = "KpiMeasurementPeriod.kpiPeriodId";
	public static String KPIMEASUREMENTPERIOD_FROMDATETIME = "KpiMeasurementPeriod.fromDateTime";
	public static String KPIMEASUREMENTPERIOD_TODATETIME = "KpiMeasurementPeriod.toDateTime";
	public static String KPIMEASUREMENTPERIOD_DESCRIPTION = "KpiMeasurementPeriod.description";
	
	//KPI - Kpi party sla by Arifin
	public static String KPIPARTYTARGET_KPIPARTYTARGETID = "KpiPartyTarget.kpiPartyTargetId";
	public static String KPIPARTYTARGET_KPIPARTYSLAID = "KpiPartyTarget.kpiPartySla.partySlaId";
	public static String KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIDESC = "KpiPartyTarget.kpiKeyPerformanceIndicator.kpiDesc";	
	public static String KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID = "KpiPartyTarget.kpiKeyPerformanceIndicator.kpiId";
	public static String KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME= "KpiPartyTarget.kpiPartySla.venParty.fullOrLegalName";
	public static String KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID= "KpiPartyTarget.kpiPartySla.venParty.partyId";
	public static String KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEDESC= "KpiPartyTarget.kpiTargetBaseline.targetBaselineDesc";
	public static String KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID= "KpiPartyTarget.kpiTargetBaseline.targetBaselineId";
	public static String KPIPARTYTARGET_KPITARGETVALUE= "KpiPartyTarget.kpiTargetValue";
	
	//KPI - KPI PARTY PERIOD ACTUAL by Arifin
	public static String KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID= "KpiPartyPeriodActual.kpiPartyMeasurementPeriod.id.kpiPeriodId";
	public static String KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC= "KpiPartyPeriodActual.kpiPartyMeasurementPeriod.kpiMeasurementPeriod.description";
	public static String KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID= "KpiPartyPeriodActual.kpiPartyMeasurementPeriod.id.partyId";
	public static String KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME= "KpiPartyPeriodActual.kpiPartyMeasurementPeriod.venParty.fullOrLegalName";
	public static String KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID= "KpiPartyPeriodActual.kpiKeyPerformanceIndicator.kpiId";
	public static String KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC= "KpiPartyPeriodActual.kpiKeyPerformanceIndicator.kpiDesc";
	public static String KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE= "KpiPartyPeriodActual.kpiCalculatedValue";	
		
	//KPI - KPI PARTY PERIOD TRANSACTION by Arifin
	public static String KPIPARTYPERIODTRANSACTION_ID="KpiPartyPeriodTransaction.kpiPartyPeriodTransactionId";
	public static String KPIPARTYPERIODTRANSACTION_TIMESTAMP="KpiPartyPeriodTransaction.transactionTimestamp";
	public static String KPIPARTYPERIODTRANSACTION_KPIKEYPERMORMANCEINDICATOR_KPIID="KpiPartyPeriodTransaction.kpiKeyPerformanceIndicator.kpiId";
	public static String KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_KPIPERIODID="KpiPartyPeriodTransaction.kpiPartyMeasurementPeriod.id.kpiPeriodId";
	public static String KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_PARTYID="KpiPartyPeriodTransaction.kpiPartyMeasurementPeriod.id.partyId";
	public static String KPIPARTYPERIODTRANSACTION_TRANSACTIONVALUE="KpiPartyPeriodTransaction.kpiTransactionValue";
	public static String KPIPARTYPERIODTRANSACTION_TRANSACTIONREASON="KpiPartyPeriodTransaction.kpiTransactionReason";
	
	//Order status history
	public static String VENORDERSTATUSHISTORY_ORDERID="VenOrderStatusHistory.id.orderId";
	public static String VENORDERSTATUSHISTORY_WCSORDERID="wcsOrderId";
	public static String VENORDERSTATUSHISTORY_TIMESTAMP="VenOrderStatusHistory.id.historyTimestamp";
	public static String VENORDERSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE="VenOrderStatusHistory.venOrderStatus.orderStatusCode";
	public static String VENORDERSTATUSHISTORY_STATUSCHANGEREASON="VenOrderStatusHistory.statusChangeReason";
	
	//Order item status history
	public static String VENORDERITEMSTATUSHISTORY_ORDERITEMID="VenOrderItemStatusHistory.id.orderItemId";
	public static String VENORDERITEMSTATUSHISTORY_WCSORDERITEMID="wcsOrderItemId";
	public static String VENORDERITEMSTATUSHISTORY_TIMESTAMP="VenOrderItemStatusHistory.id.historyTimestamp";
	public static String VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE="VenOrderItemStatusHistory.venOrderStatus.orderStatusCode";
	public static String VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON="VenOrderItemStatusHistory.statusChangeReason";
	
	//Order history filter for fraud
	public static String ORDERHISTORY_ID="id";
	public static String ORDERHISTORY_STRINGFILTER="Filter";
	public static String ORDERHISTORY_DESCRIPTION="description";
	public static String ORDERHISTORY_TOTALORDERHISTORY="totalorder";
	
	//ORDER HISTORY
	public static String ORDERHISTORY_PHONE_NUMBER = "CustomerPhoneNumber";
	public static String ORDERHISTORY_MOBILE_NUMBER= "CustomerMobileNumber";
	public static String ORDERHISTORY_EMAIL= "CustomerEmail";
	public static String ORDERHISTORY_PRODUCT_NAME= "ProductName";
	public static String ORDERHISTORY_QTY= "OrderQty";
	public static String ORDERHISTORY_SHPIPPING_ADDRESS= "ShippingAddress";
	public static String ORDERHISTORY_BILLING_METHOD= "BillingMethod";
	public static String ORDERHISTORY_STATUS= "PaymentStatus";
	public static String ORDERHISTORY_CC= "CreditCardNumber";
	public static String ORDERHISTORY_ISSUER= "IssuerBank";
	public static String ORDERHISTORY_ECI= "ECI";
	
	//why frd blscklist
	public static String FRDBLACKLIST_ID="FrdBlacklistReason.id";
	public static String FRDBLACKLIST_ORDERID="FrdBlacklistReason.orderId";
	public static String FRDBLACKLIST_WCSORDERID="FrdBlacklistReason.wcsOrderId";
	public static String FRDBLACKLIST_BLACKLIST_REASON="FrdBlacklistReason.blacklistReason";
	
	//reservation management order 
	
	public static String RSV_ORDERMANAGEMENT_ORDERDATE="orderDate";
	public static String RSV_ORDERMANAGEMENT_RESERVATIONID="reservationId";
	public static String RSV_ORDERMANAGEMENT_WCSORDERID="wcsOrderId";
	public static String RSV_ORDERMANAGEMENT_EMAILADDRESS="emailAddress";
	public static String RSV_ORDERMANAGEMENT_FIRSTNAME="firstName";
	public static String RSV_ORDERMANAGEMENT_LASTNAME="lastName";
	public static String RSV_ORDERMANAGEMENT_PAYMENTSTATUS="paymentStatus";
	public static String RSV_ORDERMANAGEMENT_MOBILEPHONE="mobilePhone";
	
	
	//Action Taken History
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_ID="FinArFundsInActionAppliedHistory.actionTakenId";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_ID="FinArFundsInActionAppliedHistory.finArFundsInReconRecords.actionAppliedId";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_DESC="FinArFundsInActionAppliedHistory.finArFundsInReconRecords.actionAppliedDesc";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID="FinArFundsInActionAppliedHistory.finArFundsInReconRecords.reconciliationRecordId";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_NOMORREFF="FinArFundsInActionAppliedHistory.finArFundsInReconRecords.nomorReff";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_DATE="FinArFundsInActionAppliedHistory.actionTakenTimestamp";
	public static String FINARFUNDSINACTIONAPPLIEDHISTORY_AMOUNT="FinArFundsInActionAppliedHistory.amount";
	
	/**
	 * This constructor adds all of the name token and the wrapper type
	 * definitions into the Map. This should really be removed so that the 
	 * class can't be instantiated and the code within moved to the Singleton
	 * instance manager method - David
	 */
	public DataNameTokens() {
		fieldClassMap = new HashMap<String, String>();

		//Fraud Module Class Mapping
		fieldClassMap.put(FRDENTITYBLACKLIST_ENTITYBLACKLISTID, "java.lang.Long");
		fieldClassMap.put(FRDENTITYBLACKLIST_BLACKLISTSTRING, "java.lang.String");
		fieldClassMap.put(FRDENTITYBLACKLIST_DESCRIPTION, "java.lang.String");
		fieldClassMap.put(FRDENTITYBLACKLIST_CREATEDBY, "java.lang.String");
		fieldClassMap.put(FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FRDENTITYBLACKLIST_BLACKORWHITELIST, "java.lang.String");
		
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID, "java.lang.Long");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_ADDRESS, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_PHONENUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_EMAIL, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_DESCRIPTION, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_CREATEDBY, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_HANDPHONENUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_CCNUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP, "java.sql.Timestamp");	
		
		
		fieldClassMap.put(FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID, "java.lang.Long");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_ORDERID, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_ORDERTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_SHIPPINGADDRESS , "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_EMAIL, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_CREDITCARDNUMBER, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_ISSUERBANK, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_ECI, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_EXPIREDDATE, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_GENUINEDATE, "java.sql.Timestamp");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_REMARK, "java.lang.String");
		fieldClassMap.put(FRDCUSTOMERWHITELIST_CREATED, "java.sql.Timestamp");
		
		fieldClassMap.put(FRDFRAUDPOINT_FRAUDPOINTSREFERENCEID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDPOINT_FRAUDREFERENCEDESCRIPTION, "java.lang.String");
		fieldClassMap.put(FRDFRAUDPOINT_FRAUDPOINTSVALUE, "java.lang.Long");
		
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "java.lang.String");		
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, "java.lang.Integer");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_ENABLEMODIFYAFTERCOMPLETED, "java.lang.Boolean");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_TYPE_FC, "java.lang.String");	
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_GENUINE, "java.lang.Boolean");		

		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERNAME, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERHOMEPHONE, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_PAYMENTADDRESSSIMILARITY, "java.lang.Boolean");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_MULTIPLESHIPMENT, "java.lang.Boolean");
		
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDRULENAME, "java.lang.String");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDSUSPICIONCASE_SUMMARY_ILOGRECOMENDATION, "java.lang.String");		
		
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_DATETIME, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_ACTIONTYPE, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_PARTYID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_PARTYNAME, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_NOTES, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_CREATEDBY, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_CREATEDDATE, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_MODIFIEDBY, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_MODIFIEDDATE, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDCASEACTIONLOG_ISACTIVE, "java.lang.Boolean");
		
		fieldClassMap.put(FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE, "java.sql.Timestamp");
		fieldClassMap.put(FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYNOTES, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSDESC, "java.lang.String");
		
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_ATTACHMENTID, "java.lang.Long");
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_CREATEDBY, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_FILELOCATION, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_FILENAME, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_DESCRIPTION, "java.lang.String");
		fieldClassMap.put(FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID, "java.lang.Long");
		
		fieldClassMap.put(FRDPARAMETERRULE31_ID, "java.lang.String");
		fieldClassMap.put(FRDPARAMETERRULE31_EMAIL, "java.lang.String");
		fieldClassMap.put(FRDPARAMETERRULE31_CCNUMBER, "java.lang.String");

		fieldClassMap.put(MIGSUPLOAD_TRANSACTIONID, "java.lang.Long");
		fieldClassMap.put(MIGSUPLOAD_MIGSID, "java.lang.Long");
		fieldClassMap.put(MIGSUPLOAD_TRANSACTIONDATE, "java.sql.Timestamp");
		fieldClassMap.put(MIGSUPLOAD_MERCHANTID, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ORDERREFERENCE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ORDERID, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_TRANSACTIONTYPE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ACQUIRERID, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_BATCHNUMBER, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_CURRENCY, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(MIGSUPLOAD_RRN, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_RESPONSECODE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ACQUIRERRESPONSECODE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_AUTHORISATIONCODE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_OPERATOR, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_MERCHANTTRANSACTIONSOURCE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(MIGSUPLOAD_CARDTYPE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_CARDNUMBER, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_CARDEXPIRYMONTH, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_CARDEXPIRYYEAR, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_DIALECTCSCRESULTCODE, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ACTION, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_PROBLEMDESCRIPTION, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_COMMENT, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_ECOMMERCEINDICATOR, "java.lang.String");
		fieldClassMap.put(MIGSUPLOAD_TYPE, "java.lang.String");
		
		fieldClassMap.put(MIGSMASTER_TRANSACTIONID, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_MIGSID, "java.lang.Long");
		fieldClassMap.put(MIGSMASTER_TRANSACTIONDATE, "java.sql.Timestamp");
		fieldClassMap.put(MIGSMASTER_MERCHANTID, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ORDERREFERENCE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ORDERID, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_MERCHANTTRANSACTIONREFERENCE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_TRANSACTIONTYPE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ACQUIRERID, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_BATCHNUMBER, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_CURRENCY, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(MIGSMASTER_RRN, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_RESPONSECODE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ACQUIRERRESPONSECODE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_AUTHORISATIONCODE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_OPERATOR, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_MERCHANTTRANSACTIONSOURCE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(MIGSMASTER_CARDTYPE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_CARDNUMBER, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_CARDEXPIRYMONTH, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_CARDEXPIRYYEAR, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_DIALECTCSCRESULTCODE, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ACTION, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_PROBLEMDESCRIPTION, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_COMMENT, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_ECOMMERCEINDICATOR, "java.lang.String");
		fieldClassMap.put(MIGSMASTER_TYPE, "java.lang.String");
		
		
		fieldClassMap.put(VENMERCHANT_MERCHANTID, "java.lang.Long");
		fieldClassMap.put(VENMERCHANT_WCSMERCHANTID, "java.lang.String");
		fieldClassMap.put(VENMERCHANT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		
		//Added by merging Djarum's team code: 2011-08-14 [UNVERIFIED]
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS, "java.lang.String");
		fieldClassMap.put(VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_SUMMARY, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, "java.lang.String");
		
	
		//General Field Class Mapping
		fieldClassMap.put(VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(VENORDER_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, "java.lang.String");
		fieldClassMap.put(VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENORDER_VENORDERSTATUS_ORDERSTATUSID, "java.lang.Long");
		fieldClassMap.put(VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENORDER_FPDATE, "java.sql.Timestamp");
		fieldClassMap.put(VENORDER_RMAFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENORDER_VENFRAUDCHECKSTATUS_FRAUDCHECKSTATUSDESC, "java.lang.String");
		fieldClassMap.put(VENORDER_FINANCERECONCILEFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENORDER_BLOCKEDFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENORDER_FULFILLMENTSTATUS, "java.lang.Long");
		fieldClassMap.put(VENORDER_RMAACTION, "java.lang.String");
		fieldClassMap.put(VENORDER_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENORDER_DELIVEREDDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(VENORDER_IPADDRESS, "java.lang.String");
		fieldClassMap.put(VENORDER_BLOCKEDTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(VENORDER_BLOCKEDREASON, "java.lang.String");
		
		fieldClassMap.put(VENORDERITEM_ORDERITEMID, "java.lang.Long");
		fieldClassMap.put(VENORDERITEM_WCSORDERITEMID, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_ADDRESSID, "java.lang.Long");
		fieldClassMap.put(VENORDERITEM_QUANTITY, "java.lang.Integer");
		fieldClassMap.put(VENORDERITEM_TOTAL, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEM_SHIPPINGCOST, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEM_INSURANCECOST, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEM_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENMERCHANTPRODUCT_PRODUCTID, "java.lang.Long");
		fieldClassMap.put(VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC, "java.lang.String");

		fieldClassMap.put(VENPRODUCTCATEGORY_PRODUCTCATEGORYID, "java.lang.Long");
		fieldClassMap.put(VENPRODUCTCATEGORY_PRODUCTCATEGORY, "java.lang.String");
		fieldClassMap.put(VENPRODUCTCATEGORY_LEVEL, "java.lang.Integer");
		
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_MASKEDCREDITCARDNUMBER, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "java.lang.String");
		
		//Logistics Module Field Class Mapping		
		fieldClassMap.put(VENORDERITEM_PRICE, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEM_PRODUCTPRICE, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_STREETADDRESS1, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_STREETADDRESS2, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_KECAMATAN, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_KELURAHAN, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_POSTALCODE, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_VENCITY_CITYNAME, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "java.lang.String");
		fieldClassMap.put(VENORDERITEM_VENADDRESS_VENCOUNTRY_STATE, "java.lang.String");

		fieldClassMap.put(VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE, "java.lang.String");
		fieldClassMap.put(VENORDERITEMADJUSTMENT_VENORDERITEM_ORDERITEMID, "java.lang.Long");
		fieldClassMap.put(VENORDERITEMADJUSTMENT_AMOUNT, "java.math.BigDecimal");
		
		fieldClassMap.put(VENORDERITEMADJUSTMENT_PROMOTIONVOUCHERCODE, "java.lang.String");
		fieldClassMap.put(VENORDERITEMADJUSTMENT_ADMINDESC, "java.lang.String");
		
		//Retur Field Class Mapping
		fieldClassMap.put(VENRETUR_RETURID, "java.lang.Long");
		fieldClassMap.put(VENRETUR_WCSRETURID, "java.lang.String");
		fieldClassMap.put(VENRETUR_RETURDATE, "java.sql.Timestamp");
		fieldClassMap.put(VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME, "java.lang.String");		
		fieldClassMap.put(VENRETUR_VENRETURSTATUS_ORDERSTATUSID, "java.lang.Long");
		fieldClassMap.put(VENRETUR_VENRETURSTATUS_ORDERSTATUSCODE, "java.lang.String");	
		fieldClassMap.put(VENRETUR_RMAACTION, "java.lang.String");		
		fieldClassMap.put(VENRETUR_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENRETUR_DELIVEREDDATETIME, "java.sql.Timestamp");
				
		fieldClassMap.put(VENRETURITEM_RETURITEMID, "java.lang.Long");
		fieldClassMap.put(VENRETURITEM_WCSRETURITEMID, "java.lang.String");
		fieldClassMap.put(VENRETURITEM_VENRETURSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENRETURITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "java.lang.String");
		fieldClassMap.put(VENRETURITEM_QUANTITY, "java.lang.Integer");
		fieldClassMap.put(VENRETURITEM_TOTAL, "java.math.BigDecimal");
		
		fieldClassMap.put(LOGAIRWAYBILLRETUR_VENRETURITEM_RETURITEMID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_VENRETURITEM_VENRETUR_WCSRETURID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_VENRETURITEM_WCSRETURITEMID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_GDNREFERENCE, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_AIRWAYBILLPICKUPDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_AIRWAYBILLID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_TRACKINGNUMBER, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILLRETUR_RECEIVED, "java.util.Date");
		
		fieldClassMap.put(VENRETURSTATUSHISTORY_RETURID, "java.lang.Long");
		fieldClassMap.put(VENRETURSTATUSHISTORY_WCSRETURID, "java.lang.String");
		fieldClassMap.put(VENRETURSTATUSHISTORY_TIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(VENRETURSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENRETURSTATUSHISTORY_STATUSCHANGEREASON, "java.lang.String");
		
		fieldClassMap.put(VENRETURITEMSTATUSHISTORY_RETURITEMID, "java.lang.Long");
		fieldClassMap.put(VENRETURITEMSTATUSHISTORY_WCSRETURITEMID, "java.lang.String");
		fieldClassMap.put(VENRETURITEMSTATUSHISTORY_TIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(VENRETURITEMSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(VENRETURITEMSTATUSHISTORY_STATUSCHANGEREASON, "java.lang.String");
		
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICKUPDETAILSID, "java.lang.Long");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS1, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS2, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KECAMATAN, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KELURAHAN, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCITY_CITYNAME, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENSTATE_STATENAME, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_POSTALCODE, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPIC, "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICPHONE, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_ZIP, "java.lang.String");
	
		fieldClassMap.put(VENCUSTOMER_CUSTOMERID, "java.lang.Long");
		fieldClassMap.put(VENCUSTOMER_WCSCUSTOMERID, "java.lang.String");
		fieldClassMap.put(VENCUSTOMER_CUSTOMERUSERNAME, "java.lang.String");
		fieldClassMap.put(VENCUSTOMER_DATEOFBIRTH, "java.util.Date");
		fieldClassMap.put(VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, "java.lang.Boolean");
		fieldClassMap.put(VENCUSTOMER_VENPARTY_PARTYFIRSTNAME, "java.lang.String");
		fieldClassMap.put(VENCUSTOMER_VENPARTY_PARTYLASTNAME, "java.lang.String");
		fieldClassMap.put(VENCUSTOMER_VENPARTY_PARTYLASTNAME, "java.lang.String");
		
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_ADDRESSID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_KECAMATAN, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_KELURAHAN, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_POSTALCODE, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "java.lang.String");
		fieldClassMap.put(VENPARTYADDRESS_VENPARTY_PARTYID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID, "java.lang.Long");
		fieldClassMap.put(VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC, "java.lang.String");
		
		fieldClassMap.put(VENMERCHANTPRODUCT_PRODUCTID, "java.lang.Long");
		fieldClassMap.put(VENMERCHANTPRODUCT_WCSPRODUCTSKU, "java.lang.String");
		fieldClassMap.put(VENMERCHANTPRODUCT_MERCHANTID, "java.lang.Long");
		fieldClassMap.put(VENMERCHANTPRODUCT_COSTOFGOODSSOLD, "java.math.BigDecimal");
		
		fieldClassMap.put(VENCITY_CITYID, "java.lang.Long");
		fieldClassMap.put(VENCITY_CITYNAME, "java.lang.String");
		fieldClassMap.put(VENCITY_CITYCODE, "java.lang.String");
		
		fieldClassMap.put(VENSTATE_STATEID, "java.lang.Long");
		fieldClassMap.put(VENSTATE_STATECODE, "java.lang.String");
		fieldClassMap.put(VENSTATE_STATENAME, "java.lang.String");
		
		fieldClassMap.put(VENCOUNTRY_COUNTRYID, "java.lang.Long");
		fieldClassMap.put(VENCOUNTRY_COUNTRYCODE, "java.lang.String");
		fieldClassMap.put(VENCOUNTRY_COUNTRYNAME, "java.lang.String");
		
		fieldClassMap.put(VENADDRESSTYPE_ADDRESSTYPEID, "java.lang.Long");
		fieldClassMap.put(VENADDRESSTYPE_ADDRESSTYPEDESC, "java.lang.String");
		
		fieldClassMap.put(VENCONTACTDETAIL_CONTACTDETAILID, "java.lang.Long");
		fieldClassMap.put(VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENCONTACTDETAIL_CONTACTDETAIL, "java.lang.String");
		fieldClassMap.put(VENCONTACTDETAIL_VENPARTY_PARTYID, "java.lang.Long");
		fieldClassMap.put(VENCONTACTDETAIL_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		
		fieldClassMap.put(VENDISTRIBUTIONCART_DISTRIBUTIONCARTID, "java.lang.Long");
		fieldClassMap.put(VENDISTRIBUTIONCART_VENORDERITEM_WCSORDERITEMID, "java.lang.String");
		fieldClassMap.put(VENDISTRIBUTIONCART_DCSEQUENCE, "java.lang.Integer");
		fieldClassMap.put(VENDISTRIBUTIONCART_PACKAGEWEIGHT, "java.math.BigDecimal");
		fieldClassMap.put(VENDISTRIBUTIONCART_QUANTITY, "java.lang.Integer");
		fieldClassMap.put(VENDISTRIBUTIONCART_VENORDERITEM_ORDERITEMID, "java.lang.Long");
//		fieldClassMap.put(VENDISTRIBUTIONCART_ITEMDESCRIPTION, "");
		
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID, "java.lang.Long");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_BINNUMBER, "java.lang.String");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE, "java.math.BigDecimal");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME, "java.lang.String");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_SEVERITY, "java.lang.String");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID, "java.lang.Long");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_ISACTIVE, "java.lang.Boolean");
		fieldClassMap.put(VENBINCREDITLIMITESTIMATE_DESCRIPTION, "java.lang.String");
		
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK, "java.lang.String");		
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION, "java.lang.String");	
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT, "java.math.BigDecimal");			
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENT_TOTALTRANSACTION, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENADDRESS, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENADDRESS_ADDRESSID, "java.lang.Long");
		
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENTALLOCATION_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "java.lang.String");
		
//		fieldClassMap.put(FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "java.lang.Long");
//		fieldClassMap.put(FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, "java.lang.String");
		
		fieldClassMap.put(LOGAIRWAYBILL_AIRWAYBILLID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_ACTUALPICKUPDATE, "java.sql.Timestamp");		
		fieldClassMap.put(LOGAIRWAYBILL_DETAIL, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_GDNREFERENCE, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_ACTIVITYRESULTSTATUS, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_INVOICERESULTSTATUS, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME,"java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, "java.util.Date");
		fieldClassMap.put(LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(LOGAIRWAYBILL_AIRWAYBILLNUMBER, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE,"java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_LOGAPPROVALSTATUS1_APPROVALSTATUSID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_LOGAPPROVALSTATUS1_APPROVALSTATUSDESC, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_INVOICEAPPROVEDBYUSERID, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_INVOICEFILENAMEANDLOC, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_INSUREDAMOUNT, "java.math.BigDecimal");
		
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, "java.sql.Timestamp");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_DESTINATION, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_SERVICE, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_SERVICEDESC, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_STATUS, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_RECEIVED, "java.util.Date");
		fieldClassMap.put(LOGAIRWAYBILL_RECIPIENT, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_RELATION, "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_WEIGHT, "java.math.BigDecimal");
		
		fieldClassMap.put(LOGAIRWAYBILL_VENDISTRIBUTIONCART_DISTRIBUTIONCARTID, "java.lang.Long");
		fieldClassMap.put(LOGAIRWAYBILL_TRACKINGNUMBER,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENPARTYADDRESS_VENADDRESS,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENCONTACTDETAIL_PHONE,  "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS,  "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPIC,  "java.lang.String");
		fieldClassMap.put(LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPICPHONE,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_QUANTITY,  "java.lang.Integer");	
		fieldClassMap.put(LOGAIRWAYBILL_VENDISTRIBUTIONCART_QUANTITY,  "java.lang.Integer");	
		fieldClassMap.put(LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT,  "java.math.BigDecimal");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENPARTYADDRESS_VENADDRESS,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_PHONE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_MOBILE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_FULLORLEGALNAME,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENPARTYADDRESS_VENADDRESS,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_PHONE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_MOBILE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_EMAIL,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE,  "java.sql.Timestamp");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_GIFTWRAP,  "java.lang.Boolean");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_GIFTCARD,  "java.lang.Boolean");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_GIFTNOTE,  "java.lang.String");		
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTCOURIER,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTDELIVEREDDATESTARTEND,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALATIONDATESTARTEND,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLOFFICER,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLMOBILE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLNOTE,  "java.lang.String");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE,  "java.sql.Timestamp");	
		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE,  "java.sql.Timestamp");	 

		fieldClassMap.put(LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_SPECIALHANDLING,  "java.lang.String");
		fieldClassMap.put(LOGAIRWAYBILL_TOTALCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGAIRWAYBILL_SHIPPINGCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGAIRWAYBILL_INSURANCECHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU,  "java.math.BigDecimal");
		fieldClassMap.put(LOGAIRWAYBILL_GIFTWRAPCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSDESC,  "java.lang.String");	
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP,  "java.sql.Timestamp");	
		
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDOTHERCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDERPACKAGEWEIGHT,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDPACKAGEWEIGHT,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDPRICEPERKG,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDGIFTWRAPCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDINSURANCECHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_APPROVEDTOTALCHARGE,  "java.math.BigDecimal");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID, "java.lang.Long");
		
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_INVOICENUMBER,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_USERLOGONNAME,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_INVOICERECONTOLERANCE,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_REPORTRECONCILIATIONTIMESTAMP,  "java.sql.Timestamp");	
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_APPROVEDBY,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC,  "java.lang.String");
		
		fieldClassMap.put(LOGFILEUPLOADLOG_FILEUPLOADLOGID,  "java.lang.Long");	
		fieldClassMap.put(LOGFILEUPLOADLOG_FILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC,  "java.lang.String");
		fieldClassMap.put(LOGFILEUPLOADLOG_ACTUALFILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGFILEUPLOADLOG_FILEUPLOADFORMAT,  "java.lang.String");
		fieldClassMap.put(LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC,  "java.lang.String");	
		fieldClassMap.put(LOGFILEUPLOADLOG_UPLOADSTATUS,  "java.lang.String");
		fieldClassMap.put(LOGFILEUPLOADLOG_TIMESTAMP,  "java.sql.Timestamp");	
		fieldClassMap.put(LOGFILEUPLOADLOG_USERNAME,  "java.lang.String");
				
		fieldClassMap.put(LOGINVOICEUPLOADLOG_INVOICEUPLOADLOGID,  "java.lang.Long");
		fieldClassMap.put(LOGINVOICEUPLOADLOG_INVOICENUMBER,  "java.lang.String");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_FILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEUPLOADLOG_ACTUALFILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_FILEUPLOADFORMAT,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAME,  "java.lang.String");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC,  "java.lang.String");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_UPLOADSTATUS,  "java.lang.String");
		fieldClassMap.put(LOGINVOICEUPLOADLOG_TIMESTAMP,  "java.sql.Timestamp");	
		fieldClassMap.put(LOGINVOICEUPLOADLOG_UPLOADEDBY,  "java.lang.String");
		
		fieldClassMap.put(LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, "java.lang.Long");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID, "java.lang.Long");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_VENICEDATA, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_PROVIDERDATA, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_USERLOGONNAME, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_COMMENT, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_LOGAIRWAYBILL_AIRWAYBILLID, "java.lang.Long");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, "java.lang.Long");
		fieldClassMap.put(LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, "java.util.Date");
		fieldClassMap.put(LOGACTIVITYRECONCOMMENTHISTORY_COMMENT, "java.lang.String");
		fieldClassMap.put(LOGACTIVITYRECONCOMMENTHISTORY_USERLOGONNAME, "java.lang.String");
		
		fieldClassMap.put(LOGINVOICERECONRECORD_INVOICERECONRECORDID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_VENICEDATA, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_PROVIDERDATA, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_MANUALLYENTEREDDATA, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_USERLOGONNAME, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_COMMENT, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, "java.lang.Long");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, "java.util.Date");
		fieldClassMap.put(LOGINVOICERECONCOMMENTHISTORY_COMMENT, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONCOMMENTHISTORY_USERLOGONNAME, "java.lang.String");
		fieldClassMap.put(LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, "java.lang.Long");
		
		//Logistics Module - Provider Management field class mapping
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_VENPARTY_PARTYID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC , "java.lang.String");
		fieldClassMap.put( LOGREPORTTEMPLATE_TEMPLATEID , "java.lang.Long");
		fieldClassMap.put( LOGREPORTTEMPLATE_TEMPLATEDESC , "java.lang.String");
		
		//Logistics Module - Provider Management - Service Tab class mapping
		fieldClassMap.put( LOGLOGISTICSERVICE_LOGISTICSSERVICEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSERVICE_SERVICECODE , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID , "java.lang.Long");
		fieldClassMap.put( LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC , "java.lang.String");
		fieldClassMap.put( LOGLOGISTICSSERVICETYPE_EXPRESSFLAG , "java.lang.Boolean");
		
		
		//Logistics Module - Provider Management - Schedule Tab class mapping
		fieldClassMap.put( LOGPICKUPSCHEDULE_PICKUPSCHEDULESID , "java.lang.Long");
		fieldClassMap.put( LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , "java.lang.Long");
		fieldClassMap.put( LOGPICKUPSCHEDULE_FROMTIME , "java.sql.Time");
		fieldClassMap.put( LOGPICKUPSCHEDULE_TOTIME , "java.sql.Time");
		fieldClassMap.put( LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC , "java.lang.String");
		fieldClassMap.put( LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS , "java.lang.Boolean");
		fieldClassMap.put( LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID , "java.lang.Long");
		fieldClassMap.put( LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC , "java.lang.String");
		
		fieldClassMap.put( LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID , "java.lang.Long");
		fieldClassMap.put( LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC , "java.lang.String");
		
		//Logistics Module - Provider Management - Agreement Tab class mapping
		fieldClassMap.put( LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID , "java.lang.Long");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , "java.lang.Long");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_AGREEMENTDESC , "java.lang.String");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_AGREEMENTDATE , "java.util.Date");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_EXPIRYDATE , "java.util.Date");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT , "java.lang.Integer");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT , "java.lang.Integer");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT , "java.math.BigDecimal");
		fieldClassMap.put( LOGPROVIDERAGREEMENT_PPNPERCENTAGE , "java.math.BigDecimal");
		
		fieldClassMap.put( VENPARTY_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID , "java.lang.Long");
		fieldClassMap.put( VENPARTY_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC , "java.lang.String");

		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_WCSORDERID, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_ORDERDATE, "java.util.Date");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE, "java.sql.Timestamp");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID, "java.lang.Long");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID, "java.lang.Long");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC, "java.lang.String");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC , "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT, "java.math.BigDecimal");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_REFUNDAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_NOMOR_REFF, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_STATUSORDER, "java.lang.String");		
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FETCH, "java.lang.String");			
		//only put fieldClass for aging here to satisfy "equals" method in JPSQLSimpleQueryCriteria to enable removing this criteria later
		fieldClassMap.put(FINARFUNDSINRECONRECORD_AGING, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID, "java.lang.Long");
		//only put fieldClass for reconciliation status here to satisfy "equals" method in JPSQLSimpleQueryCriteria to enable removing this criteria later
		fieldClassMap.put(FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINRECONRECORD_COMMENT, "java.lang.String");

		fieldClassMap.put(VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENT_ORDERPAYMENTID, "java.lang.Long");
		fieldClassMap.put(VENORDERPAYMENT_WCSPAYMENTID, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, "java.lang.String");
		
		fieldClassMap.put(FINSALESRECORD_SALESRECORDID, "java.lang.Long");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE, "java.sql.Timestamp");
		
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_QUANTITY, "java.lang.Integer");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_PRICE, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_TOTAL, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_GDNCOMMISIONAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT, "java.math.BigDecimal");
//		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_PROVIDER_CODE, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_SHIPPINGCOST, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_INSURANCECOST, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_GDNHANDLINGFEEAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_CUSTOMERDOWNPAYMENT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_RECONCILEDATE,"java.sql.Timestamp");
		fieldClassMap.put(FINSALESRECORD_PAYMENT_STATUS, "java.lang.String");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG, "java.lang.Boolean");
		fieldClassMap.put(FINSALESRECORD_PPH23_AMOUNT, "java.math.BigDecimal");
		
		fieldClassMap.put(FINSALESRECORD_MCX_DATE, "java.sql.Timestamp");
		fieldClassMap.put(FINSALESRECORD_CXF_DATE, "java.sql.Timestamp");
		fieldClassMap.put(FINSALESRECORD_MERCHANTPAYMENTAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_SALESTIMESTAMP, "java.sql.Timestamp");
		
		fieldClassMap.put(FINAPPAYMENT_APPAYMENTID, "java.lang.Long");
		fieldClassMap.put(FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINAPPAYMENT_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "java.lang.String");
		fieldClassMap.put(FINAPPAYMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINAPPAYMENT_PENALTYAMOUNT, "java.math.BigDecimal");	
		fieldClassMap.put(FINAPPAYMENT_PPH23_AMOUNT, "java.math.BigDecimal");
		
		fieldClassMap.put(FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC,"java.lang.String");
		fieldClassMap.put(FINAPPAYMENT_FINACCOUNT_ACCOUNTID,"java.lang.Long");
		
		fieldClassMap.put(FINAPINVOICE_APINVOICEID, "java.lang.Long");
		fieldClassMap.put(FINAPINVOICE_LOGINVOICEREPORTUPLOADS_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, "java.lang.String");
		fieldClassMap.put(FINAPINVOICE_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER, "java.lang.String");
		fieldClassMap.put(FINAPINVOICE_INVOICEDATE, "java.util.Date");
		fieldClassMap.put(FINAPINVOICE_INVOICEAMOUNT, "java.math.BigDecimal");
		
		fieldClassMap.put(FINARFUNDSINREFUND_REFUNDRECORDID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_WCSCUSTOMERID, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_WCSORDERID, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE, "java.util.Date");
		fieldClassMap.put(FINARFUNDSINREFUND_APAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "java.lang.Long");
		fieldClassMap.put(FINARFUNDSINREFUND_REFUNDTIMESTAMP, "java.sql.Timestamp");		
		fieldClassMap.put(FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINREFUND_ACTION_TAKEN, "java.lang.String");
		fieldClassMap.put(FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE, "java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_REASON, "java.lang.String");
		
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, "java.lang.Long");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID, "java.lang.String");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS, "java.lang.String");
		fieldClassMap.put(FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC,"java.lang.String");
		
		
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_JOURNALGROUPID, "java.lang.Long");
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID, "java.lang.Long");
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC, "java.lang.String");
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, "java.lang.String");
		fieldClassMap.put(FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "java.lang.String");
		
		fieldClassMap.put(FINJOURNALTRANSACTION_TRANSACTIONID, "java.lang.Long");
		fieldClassMap.put(FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, "java.lang.String");
		fieldClassMap.put(FINJOURNALTRANSACTION_TRANSACTIONTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTDESC, "java.lang.String");
		fieldClassMap.put(FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, "java.lang.String");
		fieldClassMap.put(FINJOURNALTRANSACTION_COMMENTS, "java.lang.String");
		fieldClassMap.put(FINJOURNALTRANSACTION_REFF, "java.lang.String");
		fieldClassMap.put(FINJOURNALTRANSACTION_GROUP_JOURNAL, "java.lang.String");		
		fieldClassMap.put(FINJOURNALTRANSACTION_PAYMENT_TYPE, "java.lang.String");			
		
		fieldClassMap.put(FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPID, "java.lang.Long");				
		
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC, "java.lang.String");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID, "java.lang.Long");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID, "java.lang.Long");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "java.math.BigDecimal");
		fieldClassMap.put(FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, "java.lang.String");
		
		//Finance - Export & Period Setup Field class mapping
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH, "java.lang.String");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_RUTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_RUJOURNALDESC, "java.lang.String");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_FINPERIOD_PERIODID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC, "java.lang.String");
		fieldClassMap.put(FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC, "java.lang.String");
		fieldClassMap.put(FINPERIOD_PERIODID, "java.lang.Long");
		fieldClassMap.put(FINPERIOD_PERIODDESC, "java.lang.String");
		fieldClassMap.put(FINPERIOD_FROMDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(FINPERIOD_TODATETIME, "java.sql.Timestamp");
		fieldClassMap.put(FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC, "java.lang.String");	
		
		//Finance - Export screen - Account Lines field class mapping
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINANCEJOURNALENTRYID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_RUVALUE, "java.math.BigDecimal");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET , "java.math.BigDecimal");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT , "java.math.BigDecimal");
		
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP, "java.sql.Timestamp");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC, "java.lang.String");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_CREDITDEBITFLAG, "java.lang.String");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_GROUPID, "java.lang.Long");		
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC, "java.lang.String");	
		
		//Finance - COA Setup screen  field class mapping
		fieldClassMap.put(FINACCOUNT_ACCOUNTID, "java.lang.Long");
		fieldClassMap.put(FINACCOUNT_ACCOUNTDESC, "java.lang.String");
		fieldClassMap.put(FINACCOUNT_ACCOUNTNUMBER, "java.lang.String");
		fieldClassMap.put(FINACCOUNT_SUMMARYACCOUNT, "java.lang.Boolean");
		fieldClassMap.put(FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID, "java.lang.Long");
		fieldClassMap.put(FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC, "java.lang.String");
		fieldClassMap.put(FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID, "java.lang.Long");
		fieldClassMap.put(FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC, "java.lang.String");
		
		fieldClassMap.put(FINACCOUNTTYPE_ACCOUNTTYPEID, "java.lang.Long");
		fieldClassMap.put(FINACCOUNTTYPE_ACCOUNTTYPEDESC, "java.lang.String");
		
		fieldClassMap.put(FINACCOUNTCATEGORY_ACCOUNTCATEGORYID, "java.lang.Long");
		fieldClassMap.put(FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC, "java.lang.String");

		//Finance - Export screen - Account Lines - status field class mapping
		fieldClassMap.put(FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSID, "java.lang.Long");
		fieldClassMap.put(FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC, "java.lang.String");
			
		// General - Party Maintenance Field Class mapping
		fieldClassMap.put(VENPARTY_PARTYID, "java.lang.Long");
		fieldClassMap.put(VENPARTY_VENPARTYTYPE_PARTYTYPEID, "java.lang.Long");
		fieldClassMap.put(VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENPARTY_FIRSTNAME, "java.lang.String");
		fieldClassMap.put(VENPARTY_MIDDLENAME, "java.lang.String");
		fieldClassMap.put(VENPARTY_LASTNAME, "java.lang.String");
		fieldClassMap.put(VENPARTY_FULLORLEGALNAME, "java.lang.String");

		fieldClassMap.put(VENPARTY_POSITION, "java.lang.String");	
		fieldClassMap.put(VENCONTACTDETAIL_CONTACTDETAILID, "java.lang.Long");
		fieldClassMap.put(VENCONTACTDETAIL_CONTACTDETAIL, "java.lang.String");
		fieldClassMap.put(VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "java.lang.String");
		
		fieldClassMap.put(VENPARTYTYPE_PARTYTYPEID, "java.lang.Long");
		fieldClassMap.put(VENPARTYTYPE_PARTYTYPEDESC, "java.lang.String");
		
		fieldClassMap.put(VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "java.lang.String");
		fieldClassMap.put(VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, "java.lang.Long");
		fieldClassMap.put(VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, "java.lang.Long");
		fieldClassMap.put(TASKID, "java.lang.String");
		
		//Finance - Promotion Field Class Mapping
		fieldClassMap.put(VENPROMOTION_PROMOTIONID, "java.lang.Long");
		fieldClassMap.put(VENPROMOTION_PROMOTIONCODE, "java.lang.String");
		fieldClassMap.put(VENPROMOTION_PROMOTIONNAME, "java.lang.String");
		fieldClassMap.put(VENPROMOTION_VENPROMOTIONTYPE_PROMOTIONTYPEDESC,"java.lang.String");
		fieldClassMap.put(VENPROMOTION_GDNMARGIN, "java.lang.Integer");
		fieldClassMap.put(VENPROMOTION_MERCHANTMARGIN, "java.lang.Integer");
		fieldClassMap.put(VENPROMOTION_OTHERSMARGIN, "java.lang.Integer");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID, "java.lang.Long");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID , "java.lang.Long");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID, "java.lang.Long");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC, "java.lang.String");
		fieldClassMap.put(VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE, "java.lang.String");
		fieldClassMap.put(VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID, "java.lang.Long");
		fieldClassMap.put(VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC, "java.lang.String");
		
		//Administration - Role Field Class Mapping
		fieldClassMap.put(RAFROLE_ROLEID, "java.lang.Long");
		fieldClassMap.put(RAFROLE_ROLENAME, "java.lang.String");
		fieldClassMap.put(RAFROLE_ROLEDESC, "java.lang.String");
		fieldClassMap.put(RAFROLE_PARENTROLE, "java.lang.Long");
		
		fieldClassMap.put(RAFROLE_RAFUSERROLES_USERID, "java.lang.Long");
		fieldClassMap.put(RAFROLE_RAFUSERROLES_ROLEID, "java.lang.Long");
		
		fieldClassMap.put(RAFROLEPROFILE_RAFROLEPROFILEID, "java.lang.Long");
		fieldClassMap.put(RAFROLE_RAFROLEPROFILES_PROFILEID, "java.lang.Long");
		fieldClassMap.put(RAFROLE_RAFROLEPROFILES_ROLEID, "java.lang.Long");
		
		//Administration - Profile Field Class Mapping
		fieldClassMap.put(RAFPROFILE_PROFILEID, "java.lang.Long");
		fieldClassMap.put(RAFPROFILE_PROFILENAME, "java.lang.String");
		fieldClassMap.put(RAFPROFILE_PROFILEDESC, "java.lang.String");
		
		//Administration - Profile Detail Class Mapping
		fieldClassMap.put(RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID, "java.lang.Long");
		fieldClassMap.put(RAFPROFILEPERMISSION_PROFILEID, "java.lang.Long");
		fieldClassMap.put(RAFPROFILEPERMISSION_APPLICATIONOBJECTID, "java.lang.Long");
		fieldClassMap.put(RAFPROFILEPERMISSION_PERMISSIONTYPEID, "java.lang.Long");
		
		//Administration - User Field Class Mapping
		fieldClassMap.put(RAFUSER_USERID, "java.lang.Long");
		fieldClassMap.put(RAFUSER_LOGINNAME, "java.lang.String");
		
		//Administration - User Role Detail Field Class Mapping
		fieldClassMap.put(RAFUSERROLE_RAFUSERROLEID, "java.lang.Long");
		fieldClassMap.put(RAFUSER_RAFUSERROLES_USERID, "java.lang.Long");
		fieldClassMap.put(RAFUSER_RAFUSERROLES_ROLEID, "java.lang.Long");
		
		//Administration - User Group Detail Field Class Mapping
		fieldClassMap.put(RAFUSERGROUP_RAFUSERGROUPID, "java.lang.Long");
		fieldClassMap.put(RAFUSER_RAFUSERGROUP_USERID, "java.lang.Long");
		fieldClassMap.put(RAFUSER_RAFUSERGROUP_GROUPID, "java.lang.Long");
		
		//Administration - Group Field Class Mapping
		fieldClassMap.put(RAFGROUP_GROUPID, "java.lang.Long");
		fieldClassMap.put(RAFGROUP_GROUPNAME, "java.lang.String");
		fieldClassMap.put(RAFGROUP_GROUPDESC, "java.lang.String");
		
		//Administration - Group Role Detail Field Class Mapping
		fieldClassMap.put(RAFGROUPROLE_RAFGROUPROLEID, "java.lang.Long");
		fieldClassMap.put(RAFGROUP_RAFGROUPROLES_GROUPID, "java.lang.Long");
		fieldClassMap.put(RAFGROUP_RAFGROUPROLES_ROLEID, "java.lang.Long");
		
		//Administration - Module Configuration Mapping
		fieldClassMap.put(RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID, "java.lang.Long");
		fieldClassMap.put(RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID, "java.lang.String");
		fieldClassMap.put(RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME, "java.lang.String");
		fieldClassMap.put(RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID, "java.lang.Long");
		fieldClassMap.put(RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID, "java.lang.Long");
		
		//KPI - Kpi_measurement_period screen by arifin
		fieldClassMap.put(KPIMEASUREMENTPERIOD_PERIODID, "java.lang.Long");
		fieldClassMap.put(KPIMEASUREMENTPERIOD_FROMDATETIME, "java.sql.Timestamp");
		fieldClassMap.put(KPIMEASUREMENTPERIOD_TODATETIME, "java.sql.Timestamp");
		fieldClassMap.put(KPIMEASUREMENTPERIOD_DESCRIPTION, "java.lang.String");
				
		//KPI - Kpi party sla by Arifin
		fieldClassMap.put(KPIPARTYTARGET_KPIPARTYTARGETID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYTARGET_KPIPARTYSLAID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIDESC, "java.lang.String");		 
		fieldClassMap.put(KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEDESC, "java.lang.String");
		fieldClassMap.put(KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYTARGET_KPITARGETVALUE, "java.lang.Integer");
		
		//KPI - KPI PARTY PERIOD ACTUAL by Arifin
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC, "java.lang.String");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME, "java.lang.String");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, "java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC, "java.lang.String");
		fieldClassMap.put(KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE, "java.lang.Integer");	
			
		//KPI - KPI PARTY PERIOD TRANSACTION by Arifin
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_ID,"java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_TIMESTAMP,"java.sql.Timestamp");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_KPIKEYPERMORMANCEINDICATOR_KPIID,"java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_KPIPERIODID,"java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_PARTYID,"java.lang.Long");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_TRANSACTIONVALUE,"java.lang.Integer");
		fieldClassMap.put(KPIPARTYPERIODTRANSACTION_TRANSACTIONREASON,"java.lang.String");
	
		//Order status history
		fieldClassMap.put(VENORDERSTATUSHISTORY_ORDERID,"java.lang.Long");
		fieldClassMap.put(VENORDERSTATUSHISTORY_WCSORDERID,"java.lang.String");
		fieldClassMap.put(VENORDERSTATUSHISTORY_TIMESTAMP,"java.sql.Timestamp");
		fieldClassMap.put(VENORDERSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE,"java.lang.String");
		fieldClassMap.put(VENORDERSTATUSHISTORY_STATUSCHANGEREASON,"java.lang.String");
		
		//Order item status history
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_ORDERITEMID,"java.lang.Long");
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_WCSORDERITEMID,"java.lang.String");
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_TIMESTAMP,"java.sql.Timestamp");
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE,"java.lang.String");
		fieldClassMap.put(VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON,"java.lang.String");
		
		//Order item status history
		fieldClassMap.put(ORDERHISTORY_ID,"java.lang.Long");
		fieldClassMap.put(ORDERHISTORY_STRINGFILTER,"java.lang.String");
		fieldClassMap.put(ORDERHISTORY_DESCRIPTION,"java.lang.String");
		fieldClassMap.put(ORDERHISTORY_TOTALORDERHISTORY,"java.lang.String");	
		
		//ORDER HISTORY
		fieldClassMap.put( ORDERHISTORY_PHONE_NUMBER ,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_MOBILE_NUMBER,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_EMAIL,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_PRODUCT_NAME,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_QTY,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_SHPIPPING_ADDRESS,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_BILLING_METHOD,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_STATUS,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_CC,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_ISSUER,"java.lang.String");
		fieldClassMap.put( ORDERHISTORY_ECI,"java.lang.String");
		
		//why frd blscklist
		fieldClassMap.put(FRDBLACKLIST_ID,"java.lang.Long");
		fieldClassMap.put(FRDBLACKLIST_ORDERID,"java.lang.Long");
		fieldClassMap.put(FRDBLACKLIST_WCSORDERID,"java.lang.String");;
		fieldClassMap.put(FRDBLACKLIST_BLACKLIST_REASON,"java.lang.String");
		
		
		//tabel fin_ar_funds_in_allocation_payment
		fieldClassMap.put(FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD,"java.lang.Long");
		fieldClassMap.put(FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE,"java.lang.Long");
		fieldClassMap.put(FINARFUNDSINALLOCATEPAYMENT_AMOUNT,"java.math.BigDecimal");
		fieldClassMap.put(FINARFUNDSINALLOCATEPAYMENT_DEST_ORDER,"java.lang.String");
		
		//TABEL FIN_AR_FUNDS_IN_ACTION_APPLIED_HISTORY
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_ID,"java.lang.Long");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_ID,"java.lang.Long");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_DESC,"java.lang.String");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID,"java.lang.Long");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_NOMORREFF,"java.lang.String");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_DATE,"java.sql.Timestamp");
		fieldClassMap.put(FINARFUNDSINACTIONAPPLIEDHISTORY_AMOUNT,"java.math.BigDecimal");
							
	}
	
	/**
	 * This returns a singleton instance of the dataNameTokens class
	 * @return the singleton instance
	 */
	public static DataNameTokens getDataNameToken() {
		if (dataNameTokens == null) {
			return new DataNameTokens();
		} else {
			return dataNameTokens;
		}
	}
	
	/**
	 * Returns the field wrapper class of the data name token
	 * @param dataName is the data name token
	 * @return the field wrapper class name (for example java.lang.String etc.)
	 */
	public String getFieldClass(String dataName){
		return fieldClassMap.get(dataName);
	}
}