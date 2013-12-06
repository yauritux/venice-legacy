package com.gdn.venice.client.app;

import java.util.LinkedHashMap;

public class DataConstantNameTokens {
	public static Long  LOGAPPROVALSTATUS_APPROVALSTATUSID_NEW = new Long(0);
	public static String  LOGAPPROVALSTATUS_APPROVALSTATUSDESC_NEW = "New";
	public static Long  LOGAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED = new Long(1);
	public static String  LOGAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED = "Submitted";
	public static Long  LOGAPPROVALSTATUS_APPROVALSTATUSID_APPROVED = new Long(2);
	public static String  LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED = "Approved";
	public static Long  LOGAPPROVALSTATUS_APPROVALSTATUSID_REJECTED = new Long(3);
	public static String  LOGAPPROVALSTATUS_APPROVALSTATUSDESC_REJECTED = "Rejected";
	
	
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_TRACKINGNUMBERDOESNOTEXIST = "0";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_PICKUPDATELATE = "1";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SETTLEMENTCODEMISMATCH= "2";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SERVICEMISMATCH = "3";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_RECIPIENTMISMATCH = "4";
	public static String LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH = "5";
	
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_AIRWAYBILLDOESNOTEXIST = "0";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_ORDERITEMDOESNOTEXIST = "1";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH = "2";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_PRICEPERKGMISMATCH = "3";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_INSURANCECOSTMISMATCH = "4";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_OTHERCHARGEMISMATCH="5";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_GIFTWRAPMISMATCH = "6";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_TOTALCHARGEMISMATCH = "7";
	public static String LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_AIRWAYBILLMISMATCH = "8";
	
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED = "0";
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED_DESC = "MTA Data Applied";
	public static String INVOICE_LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED_DESC = "Venice Data Applied";
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED = "1";
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED_DESC = "Logistic Data Applied";
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_MANUALDATAPPLIED = "2";
	public static String LOGACTIONAPPLIED_ACTIONAPPLIEDID_IGNORED = "3";
	
	public static Long  VENORDERSTATUS_ORDERSTATUSID_C = new Long(1);
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_C = "C";
	public static Long  VENORDERSTATUS_ORDERSTATUSID_SF = new Long(2);
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_SF = "SF";
	public static Long  VENORDERSTATUS_ORDERSTATUSID_FC = new Long(3);
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_FC = "FC";
	public static Long  VENORDERSTATUS_ORDERSTATUSID_FP = new Long(4);
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_FP = "FP";
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_VA = "VA";
	public static String  VENORDERSTATUS_ORDERSTATUSCODE_X = "X";
	
	public static String VENORDER_BLOCKINGSOURCEDESC_FRD = "FRD";
	public static Long VENORDER_BLOCKINGSOURCEID_FRD = new Long(0);
	public static String VENORDER_BLOCKINGSOURCEDESC_FIN = "FIN";
	public static Long VENORDER_BLOCKINGSOURCEID_FIN = new Long(1);
	
	public static Long VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED = new Long(0);
	public static Long VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED = new Long(1);
	public static Long VENORDERPAYMENT_PAYMENTSTATUSID_REJECTED = new Long(2);
	public static Long VENORDERPAYMENT_PAYMENTSTATUSID_PENDING = new Long(3);
	
	public static String LOGAIRWAYBILL_RESULTSTATUS_OK = "OK";
	public static String LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS = "Problem Exists";
	public static String LOGAIRWAYBILL_RESULTSTATUS_NODATAFROMMTA = "No Data from MTA";
	public static String LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF = "Invalid GDN Ref";
	
	public static String LOGAIRWAYBILL_LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED = "Approved";
	
	
	public static Long  FINAPPROVALSTATUS_APPROVALSTATUSID_NEW = new Long(0);
	public static String  FINAPPROVALSTATUS_APPROVALSTATUSDESC_NEW = "New";
	public static Long  FINAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED = new Long(1);
	public static String  FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED = "Submitted";
	public static Long  FINAPPROVALSTATUS_APPROVALSTATUSID_APPROVED = new Long(2);
	public static String  FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED = "Approved";
	public static Long  FINAPPROVALSTATUS_APPROVALSTATUSID_REJECTED = new Long(3);
	public static String  FINAPPROVALSTATUS_APPROVALSTATUSDESC_REJECTED = "Rejected";
	public static Long  FINAPPROVALSTATUS_APPROVALSTATUSID_DONE = new Long(4);
	public static String  FINAPPROVALSTATUS_APPROVALSTATUSDESC_DONE = "Done";
	
	public static Long  FINARRECONRESULT_RECONRESULTID_ALLFUNDSRECEIVED = new Long(0);
	public static String  FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED = "All Funds Received";
	public static Long  FINARRECONRESULT_RECONRESULTID_PARTIALFUNDSRECEIVED = new Long(1);
	public static String  FINARRECONRESULT_RECONRESULTDESC_PARTIALFUNDSRECEIVED = "Partial Funds Received";
	public static Long  FINARRECONRESULT_RECONRESULTID_OVERPAYMENTFUNDSRECEIVED = new Long(2);
	public static String  FINARRECONRESULT_RECONRESULTDESC_OVERPAIDFUNDSRECEIVED = "Overpaid Funds Received";
	public static Long  FINARRECONRESULT_RECONRESULTID_PAYMENTTIMEOUT = new Long(3);
	public static String  FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT = "Payment Timeout";
	public static Long  FINARRECONRESULT_RECONRESULTID_PAYMENTNOTRECOGNIZED = new Long(6);
	public static String  FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED = "Payment Not Recognized";
	public static String  FINARRECONRESULT_RECONRESULTDESC_REFUNDED = "Refunded";
	public static String  FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT = "No Payment";

	
	public static String  FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_RECONCILED = "Reconciled";
	public static String  FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_UNRECONCILED = "Unreconciled";
	
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_NONE = "None";
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER= "Refund to Customer";
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK= "Refund to Bank";
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_ALLOCATED= "Allocated";
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REMOVED= "Removed";
	
	
	/*
	 * Permission types
	 */
	public static final long RAF_PERMISSION_TYPE_READ = 1;
	public static final long RAF_PERMISSION_TYPE_WRITE = 2;
	public static final long RAF_PERMISSION_TYPE_EXECUTE = 4;
	
	/*
	 * Reports selections
	 */	
	public static final int VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_MERCHANT = 0;
	public static final int VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_LOGISTICS = 1;
	public static final int VEN_REPORT_SELECTION_FUNDS_IN_RECONCILEMENT = 2;
	public static final int VEN_REPORT_SELECTION_SALES = 3;
	public static final int VEN_REPORT_SELECTION_REFUND = 4;
	
	public static String  LOGISTICPROVIDER_JNE = "JNE";
	public static String  LOGISTICPROVIDER_NCS = "NCS";
	public static String  LOGISTICPROVIDER_RPX = "RPX";
	public static String LOGISTICPROVIDER_MSG = "MSG";
	public static String  LOGISTICPROVIDER_BOPIS = "BOPIS";
	
	public static String  VEN_PROMOTION_TYPE_VOUCHER = "Voucher";
	public static String  VEN_PROMOTION_TYPE_VOUCHERCS = "Voucher Customer Service";
	public static String  VEN_PROMOTION_TYPE_NONVOUCHER = "Non Voucher";
	public static String  VEN_PROMOTION_TYPE_FREESHIPPING = "Free Shipping";
	
	public static String ANONYMOUS_CUSTOMER="Anonymous";
	
	public static String VEN_CONTACT_DETAIL_ID_PHONE="0";
	public static String VEN_CONTACT_DETAIL_ID_MOBILE="1";
	public static String VEN_CONTACT_DETAIL_ID_EMAIL="3";
	public static String VEN_CONTACT_DETAIL_ID_CUSTOMER_ADDRESS="3";
	public static String VEN_PARTY_ADDRESS_TYPE_DEFAULT="0";
	public static String FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_30="FRD_PARAMETER_RULE_30";
	public static String FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_35="FRD_PARAMETER_RULE_35";
	public static String FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_18="FRD_PARAMETER_RULE_18";
	public static String VEN_CONTACT_DETAIL_TYPE_PHONE="0";

	
	public static String FRAUD_RULE_1="Rule 01 - First time shopper";
	public static String FRAUD_RULE_2="Rule 02 - Larger than normal order";
	public static String FRAUD_RULE_3="Rule 03 - Order that include several of the same time";
	public static String FRAUD_RULE_4="Rule 04 - Order made up of big ticket items";
	public static String FRAUD_RULE_5="Rule 05 - Rush or overnight shipping";
	public static String FRAUD_RULE_6="Rule 06 - Shipping to an international address";
	public static String FRAUD_RULE_7="Rule 07 - Transactions with similar account number";
	public static String FRAUD_RULE_8="Rule 08 - Payment Type";
	public static String FRAUD_RULE_9="Rule 09 - Shipping to a single address, but transactions placed on multiple cards";
	public static String FRAUD_RULE_10="Rule 10 - Multiple transactions on one card over a very short period of time";
	public static String FRAUD_RULE_11="Rule 11 - Multiple transactions on one card or a similar card with a single billing address, but multiple shipping addresses";
	public static String FRAUD_RULE_12="Rule 12 - Multiple cards used from a single IP address";
	public static String FRAUD_RULE_13="Rule 13 - IP address company";
	public static String FRAUD_RULE_14="Rule 14 - Order from internet addresses that make use of free email services";
	public static String FRAUD_RULE_15="Rule 15 - Bin number not registered";
	public static String FRAUD_RULE_16="Rule 16 - Same customer with different credit card";
	public static String FRAUD_RULE_17="Rule 17 - City blacklist";
		
	public static String FRAUD_RULE_18="Rule 18 - Validity of wording customer, shipping, billing address";
	public static String FRAUD_RULE_19="Rule 19 - Validity of address";
	public static String FRAUD_RULE_20="Rule 20 - UMR by 33 province";
	public static String FRAUD_RULE_21="Rule 21 - Order timestamp blacklist";
	public static String FRAUD_RULE_22="Rule 22 - Customer shopping limit per month";
	public static String FRAUD_RULE_23="Rule 23 - Customer name vs customer email";
	public static String FRAUD_RULE_24="Rule 24 - Total order amount ";
	public static String FRAUD_RULE_25="Rule 25 - Company shipping address";
	public static String FRAUD_RULE_26="Rule 26 - Same customer email in one week";
	public static String FRAUD_RULE_27="Rule 27 - Same product name & customer email in one week";
	public static String FRAUD_RULE_28="Rule 28 - Same product category & customer email in one week";
	public static String FRAUD_RULE_29="Rule 29 - Same customer address in one week";
	public static String FRAUD_RULE_30="Rule 30 - Phone area code customer";
	public static String FRAUD_RULE_31="Rule 31 - List genuine transaction by BCA";
	public static String FRAUD_RULE_32="Rule 32 - Collection blacklist";
	public static String FRAUD_RULE_33="Rule 33 - E-Commerce Indicator (ECI)";
	public static String FRAUD_RULE_34="Rule 34 - IP geolocation information";
	public static String FRAUD_RULE_35="Rule 35 - Grey list";
	public static String FRAUD_RULE_36="Rule 36 - Black List";
	public static String FRAUD_RULE_37="Rule 37 - MIGS History with same credit card";
	public static String FRAUD_RULE_38="Rule 38 - MIGS History with different credit card";	
	public static String FRAUD_RULE_39="Rule 39 - Pasca bayar";
	public static String FRAUD_RULE_40="Rule 40 - Handphone area vs customer location";
	public static String FRAUD_RULE_26_ID="26";
	public static String FRAUD_RULE_27_ID="27";
	public static String FRAUD_RULE_28_ID="28";
	public static String FRAUD_RULE_29_ID="29";
	
	public static LinkedHashMap<String, String> promoTypes(){
		LinkedHashMap<String, String> promoTypes = new LinkedHashMap<String, String>();
		//TODO consider get value from database
		promoTypes.put("1", DataConstantNameTokens.VEN_PROMOTION_TYPE_VOUCHER);
		promoTypes.put("2", DataConstantNameTokens.VEN_PROMOTION_TYPE_VOUCHERCS);
		promoTypes.put("3", DataConstantNameTokens.VEN_PROMOTION_TYPE_NONVOUCHER);
		promoTypes.put("4", DataConstantNameTokens.VEN_PROMOTION_TYPE_FREESHIPPING);
		
		return promoTypes;
	}
	
	public static Long  FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID_REALTIME = new Long(1);
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_REALTIME = "Real Time";
	public static Long  FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID_H1 = new Long(2);
	public static String  FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_H1 = "H+1";
	
}


