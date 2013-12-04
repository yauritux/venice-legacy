package com.gdn.venice.util;

import java.math.BigDecimal;

import com.djarum.raf.utilities.ModuleConfigUtility;

public interface VeniceConstants {
	public static final long VEN_ADDRESS_TYPE_DEFAULT = 0;
	public static final long VEN_ADDRESS_TYPE_SHIPPING = 1;
	public static final long VEN_ADDRESS_TYPE_BILLING = 2;
	public static final long VEN_ADDRESS_TYPE_MAILING = 3;
	public static final long VEN_ADDRESS_TYPE_OTHER = 4;
	
	public static final long VEN_PARTY_TYPE_BANK = 0;
	public static final long VEN_PARTY_TYPE_MERCHANT = 1;
	public static final long VEN_PARTY_TYPE_LOGISTICS = 2;
	public static final long VEN_PARTY_TYPE_RECIPIENT = 3;
	public static final long VEN_PARTY_TYPE_CUSTOMER = 4;
	public static final long VEN_PARTY_TYPE_USER = 5;
	
	public static final String VEN_PAYMENT_TYPE_CC = "CC";
	public static final String VEN_PAYMENT_TYPE_IB = "IB";
	public static final String VEN_PAYMENT_TYPE_VA = "VA";
	public static final String VEN_PAYMENT_TYPE_CS = "CS";

	public static final long VEN_PAYMENT_TYPE_ID_CC = 0;
	public static final long VEN_PAYMENT_TYPE_ID_IB = 1;
	public static final long VEN_PAYMENT_TYPE_ID_VA = 2;
	public static final long VEN_PAYMENT_TYPE_ID_CS = 3;
	
	public static final long VEN_VA_PAYMENT_STATUS_ID_APPROVING = 0;
	public static final long VEN_VA_PAYMENT_STATUS_ID_APPROVED = 1;	
	
	public static final long VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED = 0;
	public static final long VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED = 1;
	public static final long VENORDERPAYMENT_PAYMENTSTATUSID_REJECTED = 2;
	public static final long VENORDERPAYMENT_PAYMENTSTATUSID_PENDING = 3;
	
	public static final String VEN_WCS_PAYMENT_TYPE_KlikBCA = "KlikBCA";
	public static final String VEN_WCS_PAYMENT_TYPE_DebitMandiri = "DebitMandiri";
	public static final String VEN_WCS_PAYMENT_TYPE_MandiriKlikpay = "MandiriKlikpay";
	public static final String VEN_WCS_PAYMENT_TYPE_VirtualAccount = "VirtualAccount";
	public static final String VEN_WCS_PAYMENT_TYPE_MIGSCreditCard = "MIGSCreditCard";
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment = "KlikPAYFullPayment";
	// 2 constants below are replacing KlikPAYFullPayment
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit = "KlikPAYKartuKredit";
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA = "KlikPAYKlikBCA";
	
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYZeroPercentInstallment = "KlikPAYZeroPercentInstallment";
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYXPercentInstallment = "KlikPAYXPercentInstallment";
	public static final String VEN_WCS_PAYMENT_TYPE_MIGSBCAInstallment = "MIGSBCAInst";
	public static final String VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment = "KlikPayInst";
	public static final String VEN_WCS_PAYMENT_TYPE_CIMBClicks = "CIMBClicks";
	public static final String VEN_WCS_PAYMENT_TYPE_XLTunai = "XLTunai";
	public static final String VEN_WCS_PAYMENT_TYPE_MandiriInstallment = "MandiriInstallment";
	public static final String VEN_WCS_PAYMENT_TYPE_BIIngkisan = "BIIngkisan";
	public static final String VEN_WCS_PAYMENT_TYPE_BRI = "BRI";
	public static final String VEN_WCS_PAYMENT_TYPE_MandiriDebit = "MandiriDebit";
	public static final String VEN_WCS_PAYMENT_TYPE_CSPayment = "CSPayment";
	
	//this is only for wcs
	public static final String VEN_WCS_PAYMENT_TYPE_PartialFulfillment = "PartialFulfillment";
	
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikBCA = 0;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_MandiriKlikpay = 1;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_VirtualAccount = 2;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_MIGSCreditCard = 3;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYFullPayment = 4;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYZeroPercentInstallment = 5;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYXPercentInstallment = 6;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_MIGSBCAInstallment = 7;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYInstallment = 8;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_CIMBClicks = 9;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_XLTunai = 10;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_MandiriInstallment= 11;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_BIIngkisan = 12;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_BRI = 13;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_MandiriDebit = 14;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_CSPayment = 15;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYKartuKredit = 16;
	public static final long VEN_WCS_PAYMENT_TYPE_ID_KlikPAYKlikBCA = 17;
	
	public static final Integer VEN_FULFILLMENT_STATUS_ZERO = 0;
	public static final Integer VEN_FULFILLMENT_STATUS_ONE = 1;
	
	public static final long VEN_ORDER_STATUS_VA = 0;
	public static final long VEN_ORDER_STATUS_C = 1;
	public static final long VEN_ORDER_STATUS_SF = 2;
	public static final long VEN_ORDER_STATUS_FC = 3;
	public static final long VEN_ORDER_STATUS_FP = 4;
	public static final long VEN_ORDER_STATUS_D = 5;
	public static final long VEN_ORDER_STATUS_X = 6;
	public static final long VEN_ORDER_STATUS_RV = 7;
	public static final long VEN_ORDER_STATUS_PU = 8;
	public static final long VEN_ORDER_STATUS_BP = 9;
	public static final long VEN_ORDER_STATUS_ES = 10;
	public static final long VEN_ORDER_STATUS_RM = 11;
	public static final long VEN_ORDER_STATUS_RL = 12;
	public static final long VEN_ORDER_STATUS_PP = 13;
	public static final long VEN_ORDER_STATUS_PF = 14;
	public static final long VEN_ORDER_STATUS_RT = 15;
	public static final long VEN_ORDER_STATUS_CX = 16;
	public static final long VEN_ORDER_STATUS_OS = 17;
	public static final long VEN_ORDER_STATUS_CS = 18;
	public static final long VEN_ORDER_STATUS_B = 19;
	public static final long VEN_ORDER_STATUS_R = 20;
	public static final long VEN_ORDER_STATUS_RF = 21;
	public static final long VEN_ORDER_STATUS_TS = 22;
	
	public static final long VEN_ACTIVITY_RECON_RESULT_0 = 0;
	public static final long VEN_ACTIVITY_RECON_RESULT_1 = 1;
	public static final long VEN_ACTIVITY_RECON_RESULT_2 = 2;
	public static final long VEN_ACTIVITY_RECON_RESULT_3 = 3;
	public static final long VEN_ACTIVITY_RECON_RESULT_4 = 4;
	public static final long VEN_ACTIVITY_RECON_RESULT_5 = 5;
	public static final long VEN_ACTIVITY_RECON_RESULT_6 = 6;
	
	public static final long VEN_INVOICE_RECON_RESULT_0 = 0;
	public static final long VEN_INVOICE_RECON_RESULT_1 = 1;
	public static final long VEN_INVOICE_RECON_RESULT_2 = 2;
	public static final long VEN_INVOICE_RECON_RESULT_3 = 3;
	public static final long VEN_INVOICE_RECON_RESULT_4 = 4;
	public static final long VEN_INVOICE_RECON_RESULT_5 = 5;
	public static final long VEN_INVOICE_RECON_RESULT_6 = 6;
	public static final long VEN_INVOICE_RECON_RESULT_7 = 7;
	public static final long VEN_INVOICE_RECON_RESULT_8 = 8;
	
	public static final long VEN_CONTACT_TYPE_PHONE = 0;
	public static final long VEN_CONTACT_TYPE_MOBILE = 1;
	public static final long VEN_CONTACT_TYPE_FAX = 2;
	public static final long VEN_CONTACT_TYPE_EMAIL = 3;
	public static final long VEN_CONTACT_TYPE_ONLINE = 4;
	
	public static final long VEN_LOGISTICS_PROVIDER_RPX = 0;
	public static final long VEN_LOGISTICS_PROVIDER_NCS = 1;
	public static final long VEN_LOGISTICS_PROVIDER_JNE = 2;
	public static final long VEN_LOGISTICS_PROVIDER_GOJEK = 3;
	public static final long VEN_LOGISTICS_PROVIDER_MSG = 4;
	public static final long VEN_LOGISTICS_PROVIDER_BOPIS = 10;
	public static final long VEN_LOGISTICS_PROVIDER_BIGPRODUCT = 11;
	
	public static final long VEN_LOGISTICS_APPROVAL_STATUS_NEW = 0;
	public static final long VEN_LOGISTICS_APPROVAL_STATUS_SUBMITTED = 1;
	public static final long VEN_LOGISTICS_APPROVAL_STATUS_APPROVED = 2;
	public static final long VEN_LOGISTICS_APPROVAL_STATUS_REJECTED = 3;
	
	public static final String LOG_LOGISTICS_SERVICE_CODE_NCS_REG = "NCS_REG";
	public static final String LOG_LOGISTICS_SERVICE_CODE_NCS_EXP = "NCS_EXP";
	public static final String LOG_LOGISTICS_SERVICE_CODE_RPX_REG = "RPX_REG";
	public static final String LOG_LOGISTICS_SERVICE_CODE_RPX_EXP = "RPX_EXP";

	
	public static final String LOG_LOGISTICS_STATUS_CODE_POD = "POD";
	public static final String LOG_LOGISTICS_STATUS_CODE_VAN = "VAN";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_01 = "DEX 01";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_03 = "DEX 03";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_07 = "DEX 07";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_08 = "DEX 08";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_14 = "DEX 14";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_17 = "DEX 17";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_29 = "DEX 29";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_31 = "DEX 31";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_33 = "DEX 33";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_42 = "DEX 42";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_84 = "DEX 84";
	public static final String LOG_LOGISTICS_STATUS_CODE_PDEX_59 = "DEX 59";
	public static final String LOG_LOGISTICS_STATUS_CODE_DEX_78 = "DEX 78";
	public static final String LOG_LOGISTICS_STATUS_CODE_HLD = "HLD";
	public static final String LOG_LOGISTICS_STATUS_CODE_MIS = "MIS";
	public static final String LOG_LOGISTICS_STATUS_CODE_SIP = "SIP";
	public static final String LOG_LOGISTICS_STATUS_CODE_TRI = "TRI";
	public static final String LOG_LOGISTICS_STATUS_CODE_SOP = "SOP";
	public static final String LOG_LOGISTICS_STATUS_CODE_TRO = "TRO";
	public static final String LOG_LOGISTICS_STATUS_CODE_WHS = "WHS";
	public static final String LOG_LOGISTICS_STATUS_CODE_COM = "COM";
	public static final String LOG_LOGISTICS_STATUS_CODE_MDE = "MDE";
	
	public static final long LOG_APPROVAL_STATUS_NEW = 0;
	public static final long LOG_APPROVAL_STATUS_SUBMITTED = 1;
	public static final long LOG_APPROVAL_STATUS_APPROVED = 2;
	public static final long LOG_APPROVAL_STATUS_REJECTED = 3;
	
	public static final long LOG_ACTION_APPLIED_VENICE = 0;
	public static final long LOG_ACTION_APPLIED_PROVIDER = 1;
	public static final long LOG_ACTION_APPLIED_MANUAL = 2;
	public static final long LOG_ACTION_APPLIED_IGNORE = 3;
	
	public static final int LOG_INVOICE_RECON_RESULT_GDN_REF_MISMATCH = 0;
	public static final int LOG_INVOICE_RECON_RESULT_ORDER_ITEM_MISMATCH = 1;
	public static final int LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH = 2;
	public static final int LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH = 3;
	public static final int LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH = 4;
	public static final int LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH = 5;
	public static final int LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH = 6;
	public static final int LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH = 7;
	public static final int LOG_INVOICE_RECON_RESULT_AWB_NO_MISMATCH = 8;
	
	public static final int LOG_ACTIVITY_RECON_RESULT_GDN_REF_MISMATCH = 0;
	public static final int LOG_ACTIVITY_RECON_RESULT_PICKUP_DATE_MISMATCH = 1;
	public static final int LOG_ACTIVITY_RECON_RESULT_SETTLEMENT_CODE_MISMATCH = 2;
	public static final int LOG_ACTIVITY_RECON_RESULT_SERVICE_MISMATCH = 3;
	public static final int LOG_ACTIVITY_RECON_RESULT_RECIPIENT_MISMATCH = 4;
	public static final int LOG_ACTIVITY_RECON_RESULT_WEIGHT_MISMATCH = 5;
	
	
	public static final long LOG_REPORT_STATUS_NEW = 0;
	public static final long LOG_REPORT_STATUS_PROCESSING = 1;
	public static final long LOG_REPORT_STATUS_RECONCILED = 2;
	
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA = 0;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB = 1;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC = 2;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA = 3;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB = 4;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB = 5;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB = 6;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC = 7;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC = 8;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB = 9;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC = 10;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_BIINGKISAN = 11;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB = 12;
	public static final long FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_CC = 13;
	
	public static final long FIN_AR_RECON_RESULT_ALL = 0;
	public static final long FIN_AR_RECON_RESULT_PARTIAL = 1;
	public static final long FIN_AR_RECON_RESULT_OVERPAID = 2;
	public static final long FIN_AR_RECON_RESULT_TIMEOUT = 3;
	public static final long FIN_AR_RECON_RESULT_NONE = 4;
	public static final long FIN_AR_RECON_RESULT_REFUNDED = 5;
	public static final long FIN_AR_RECON_RESULT_NOT_RECOGNIZED = 6;
	public static final long FIN_AR_RECON_RESULT_ALLOCATE_TO_ORDER = 7;
	
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE = 0;
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER = 1;
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED = 2;
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED = 3;
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK = 4;
	public static final long FIN_AR_FUNDS_IN_ACTION_APPLIED_RECEIVE = 5;
	
	public static final String FIN_SALES_RECORD_PAYMENT_STATUS_PAID = "Paid";
	public static final String FIN_SALES_RECORD_PAYMENT_STATUS_READY_TO_PAY = "Ready to Pay";
	
	public static final String FIN_SALES_BATCH_STATUS_READY = "Ready";
	public static final String FIN_SALES_BATCH_STATUS_IN_PROCESS = "In process";
	public static final String FIN_SALES_BATCH_STATUS_DONE = "Done";
		
	public static final long FIN_APPROVAL_STATUS_NEW = 0;
	public static final long FIN_APPROVAL_STATUS_SUBMITTED = 1;
	public static final long FIN_APPROVAL_STATUS_APPROVED = 2;
	public static final long FIN_APPROVAL_STATUS_REJECTED = 3;

	public static final long FIN_ACCOUNT_1120101 = 1120101;
	public static final long FIN_ACCOUNT_1120102 = 1120102;
	public static final long FIN_ACCOUNT_1120103 = 1120103;
	public static final long FIN_ACCOUNT_1120104 = 1120104;
	public static final long FIN_ACCOUNT_1120105 = 1120105;
	public static final long FIN_ACCOUNT_1120301 = 1120301;
	public static final long FIN_ACCOUNT_1120302 = 1120302;
	public static final long FIN_ACCOUNT_1120401 = 1120401;
	public static final long FIN_ACCOUNT_1120402 = 1120402;
	public static final long FIN_ACCOUNT_1120888 = 1120888;
	public static final long FIN_ACCOUNT_1121001 = 1121001;
	public static final long FIN_ACCOUNT_1140001 = 1140001;
	public static final long FIN_ACCOUNT_1140002 = 1140002;
	public static final long FIN_ACCOUNT_1180001 = 1180001;
	public static final long FIN_ACCOUNT_1190001 = 1190001;
	public static final long FIN_ACCOUNT_2130001 = 2130001;
	public static final long FIN_ACCOUNT_2140001 = 2140001;
	public static final long FIN_ACCOUNT_2150099 = 2150099;
	public static final long FIN_ACCOUNT_2170001 = 2170001;
	public static final long FIN_ACCOUNT_2170002 = 2170002;
	public static final long FIN_ACCOUNT_2180005 = 2180005;
	public static final long FIN_ACCOUNT_2180008 = 2180008;
	public static final long FIN_ACCOUNT_2190001 = 2190001;
	public static final long FIN_ACCOUNT_2210002 = 2210002;

	public static final long FIN_ACCOUNT_2220001 = 2220001;
	public static final long FIN_ACCOUNT_2230001 = 2230001;
	public static final long FIN_ACCOUNT_2230002 = 2230002;

	public static final long FIN_ACCOUNT_4110001 = 4110001;
	public static final long FIN_ACCOUNT_4110003 = 4110003;
	public static final long FIN_ACCOUNT_4110004 = 4110004;
	public static final long FIN_ACCOUNT_4110005 = 4110005;
	public static final long FIN_ACCOUNT_4110007 = 4110007;
	public static final long FIN_ACCOUNT_4900000 = 4900000;
	public static final long FIN_ACCOUNT_5110001 = 5110001;
	public static final long FIN_ACCOUNT_5110003 = 5110003;
	public static final long FIN_ACCOUNT_5110004 = 5110004;
	public static final long FIN_ACCOUNT_6133010 = 6133010;
	public static final long FIN_ACCOUNT_6133018 = 6133018;
	public static final long FIN_ACCOUNT_6133020 = 6133020;
	public static final long FIN_ACCOUNT_1160001 = 1160001;
	//public static final long FIN_ACCOUNT_6133001 = 6133001;
	public static final long FIN_ACCOUNT_7140001 = 7140001;
	public static final long FIN_ACCOUNT_7170001 = 7170001;
	public static final long FIN_ACCOUNT_1120999 = 1120999;
	
	public static final long FIN_JOURNAL_CASH_RECEIVE = 0;
	public static final long FIN_JOURNAL_SALES = 1;
	public static final long FIN_JOURNAL_LOGISTICS_DEBT_ACKNOWLEDGEMENT = 2;
	public static final long FIN_JOURNAL_REFUND_OTHERS = 3;
	public static final long FIN_JOURNAL_PAYMENT = 4;
	public static final long FIN_JOURNAL_MANUAL = 5;
	public static final long FIN_JOURNAL_ALLOCATION = 6;
	
	
	public static final long FIN_TRANSACTION_STATUS_NEW = 0;
	public static final long FIN_TRANSACTION_STATUS_RECONCILED = 1;
	public static final long FIN_TRANSACTION_STATUS_POSTED = 2;
	public static final long FIN_TRANSACTION_STATUS_EXPORTED = 3;
	
	public static final long FIN_TRANSACTION_TYPE_UANG_MUKA_PELANGGAN = 0;
	public static final long FIN_TRANSACTION_TYPE_REFUND_PELANGGAN = 1;
	public static final long FIN_TRANSACTION_TYPE_COMMISSION = 2;
	public static final long FIN_TRANSACTION_TYPE_REBATE = 3;
	public static final long FIN_TRANSACTION_TYPE_PROCESS_FEE = 4;
	public static final long FIN_TRANSACTION_TYPE_LOGISTICS_REVENUE = 5;
	public static final long FIN_TRANSACTION_TYPE_LOGISTICS_PAYMENT = 6;
	public static final long FIN_TRANSACTION_TYPE_LOGISTICS_PENALTY = 7;
	public static final long FIN_TRANSACTION_TYPE_OVER_LESS_PAYMENT = 8;
	public static final long FIN_TRANSACTION_TYPE_MERCHANT_PAYMENT = 9;
	public static final long FIN_TRANSACTION_TYPE_MERCHANT_PENALTY = 10;
	public static final long FIN_TRANSACTION_TYPE_PROMO_SPONSORSHIP = 11;
	public static final long FIN_TRANSACTION_TYPE_PPN = 12;
	public static final long FIN_TRANSACTION_TYPE_MANUAL_ADJUSTMENT_JOURNAL = 13;
	public static final long FIN_TRANSACTION_TYPE_UANG_JAMINAN_TRANSAKSI = 14;
	public static final long FIN_TRANSACTION_TYPE_SM_PROMOSI_KONSUMEN = 15;
	public static final long FIN_TRANSACTION_TYPE_HPP_BIAYA_BARANG = 16;
	public static final long FIN_TRANSACTION_TYPE_POT_PENJUALAN = 17;
	public static final long FIN_TRANSACTION_TYPE_VOUCHER_YANG_BELUM_DIREALISASI = 18;
	public static final long FIN_TRANSACTION_TYPE_SM_VOUCHER_CUSTOMER_SERVICE = 19;
	public static final long FIN_TRANSACTION_TYPE_HUTANG_MERCHANT = 20;
	public static final long FIN_TRANSACTION_TYPE_PENJUALAN = 21;
	public static final long FIN_TRANSACTION_TYPE_PENDAPATAN_KOMISI = 22;
	public static final long FIN_TRANSACTION_TYPE_PENDAPATAN_LOGISTIK = 23;
	public static final long FIN_TRANSACTION_TYPE_PENDAPATAN_JASA_TRANSAKSI = 24;
	public static final long FIN_TRANSACTION_TYPE_PENDAPATAN_JASA_HANDLING = 25;
	public static final long FIN_TRANSACTION_TYPE_INVENTORY_OVER_VOUCHER = 26;
	public static final long FIN_TRANSACTION_TYPE_SM_BEBAN_PROMOSI_BEBAS_BIAYA_KIRIM = 27;
	public static final long FIN_TRANSACTION_TYPE_BIAYA_HARUS_DIBAYAR_LOGISTIK = 28;
	public static final long FIN_TRANSACTION_TYPE_INVENTORY = 29;
	public static final long FIN_TRANSACTION_TYPE_BARANG_DITERIMA_DIMUKA = 30;
	public static final long FIN_TRANSACTION_TYPE_REFUND_BANK = 31;
	public static final long FIN_TRANSACTION_TYPE_HPP_LOGISTIK = 32;
	public static final long FIN_TRANSACTION_TYPE_PIUTANG_MERCHANT= 33;
	public static final long FIN_TRANSACTION_TYPE_PIUTANG_LAIN_LAIN= 34;
	public static final long FIN_TRANSACTION_TYPE_PERSEDIAAN_BARANG_DAGANG= 35;
	public static final long FIN_TRANSACTION_TYPE_UANG_JAMINAN_BELUM_TERIDENTIFIKASI=36;
	public static final long FIN_TRANSACTION_TYPE_AYAT_AYAT_SILANG_PADA_BANK=37;
	public static final long FIN_TRANSACTION_TYPE_LEBIH_ATAU_KURANG_BAYAR=38;
	
	public static final long FIN_ROLLED_UP_JOURNAL_STATUS_OPEN = 0;
	public static final long FIN_ROLLED_UP_JOURNAL_STATUS_PROCESSED = 1;
	public static final long FIN_ROLLED_UP_JOURNAL_STATUS_APPROVED = 2;
	public static final long FIN_ROLLED_UP_JOURNAL_STATUS_REJECTED = 3;
	public static final long FIN_ROLLED_UP_JOURNAL_STATUS_EXPORTED = 4;
	
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_CASH_RECEIVE = 0;
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_SALES = 1;
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_LDA = 2;
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_REFUND = 3;
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_PAYMENT = 4;
	public static final long FIN_ROLLED_UP_JOURNAL_TYPE_MANUAL = 5;

	
	public static final long FIN_AP_PAYMENT_TYPE_LOGISTICS = 0;
	public static final long FIN_AP_PAYMENT_TYPE_MERCHANT = 1;
	public static final long FIN_AP_PAYMENT_TYPE_REFUND = 2;
	public static final long FIN_AP_PAYMENT_TYPE_OTHER = 3;
		
	public static final long VEN_BANK_ID_BCA = 0;
	public static final long VEN_BANK_ID_MANDIRI = 1;
	public static final long VEN_BANK_ID_NIAGA = 2;
	public static final long VEN_BANK_ID_XLAXIATA = 3;
	public static final long VEN_BANK_ID_BII = 4;
	public static final long VEN_BANK_ID_BRI = 5;
	
	public static final double VEN_GDN_PPN_RATE = 10; //This is the global tax rate 
	
	public static final long VEN_PROMOTION_SHARE_CALC_METHOD_PERCENTAGE = 0;
	public static final long VEN_PROMOTION_SHARE_CALC_METHOD_FLAT = 1;
	
	public static final long VEN_PROMOTION_TYPE_VOUCHER = 1;
	public static final long  VEN_PROMOTION_TYPE_VOUCHERCS = 2;
	public static final long  VEN_PROMOTION_TYPE_NONVOUCHER = 3;
	public static final long  VEN_PROMOTION_TYPE_FREESHIPPING = 4;
	
	//These are the refund types that can be passed to the refund journal
	public static final int FIN_REFUND_TYPE_CUSTOMER = 0;
	public static final int FIN_REFUND_TYPE_BANK = 1;
	
	public static final long KPI_LOGISTICS_PICKUP_PERFORMANCE = 1;
	public static final long KPI_LOGISTICS_DELIVERY_PERFORMANCE = 2;
	public static final long KPI_LOGISTICS_INVOICE_ACCURACY = 3;
	public static final long KPI_MERCHANT_PARTIAL_FULFILLMENT_FREQUENCY = 4;
	public static final long KPI_MERCHANT_FULFILLMENT_RESPONSE = 5;
	
	public static final long KPI_TARGET_BASELINE_WORST = 1;
	public static final long KPI_TARGET_BASELINE_AVERAGE = 2;
	public static final long KPI_TARGET_BASELINE_BEST = 3;
	
	public static final long MILLISECONDS_IN_A_DAY = 1000 * 24 * 60 * 60;
	
	public static final String VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_COMMISSION = "CM";
	public static final String VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_REBATE = "RB";
	public static final String VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_TRADING = "TD";
	public static final String VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_CONSIGNMENT = "KS";
	public static final String VEN_SETTLEMENT_RECORD_COMMISSIONTYPE_MERCHANTPARTNER = "MP";
	
	public static final int VEN_ROLE_ADMIN=1;
	public static final int VEN_ROLE_GENERAL=2;
	public static final int VEN_ROLE_LOGISTIC=3;
	public static final int VEN_ROLE_FRAUD=4;
	public static final int VEN_ROLE_FINANCE=5;
	public static final int VEN_ROLE_KPI=6;
	
	public static final String VEN_ROLE_NAME_ADMIN = "admin";
	public static final String VEN_ROLE_NAME_GENERAL = "general";
	public static final String VEN_ROLE_NAME_LOGISTIC = "logistic";
	public static final String VEN_ROLE_NAME_FRAUD = "fraud";
	public static final String VEN_ROLE_NAME_FINANCE = "finance";
	public static final String VEN_ROLE_NAME_KPI = "kpi";
	
	public static final String MONDAY = "Monday";
	public static final String FRIDAY = "Friday";
	
	public static final int PAYMENT_CANCELED = 1;
	
	public static final BigDecimal  TRACEHOLD_RECEIVED = new BigDecimal(5000);
	public static final String VEN_ORDER_STATUS_CODE = "X";
	
	
	public static final String E_COMMERCE_INDICATOR_5 = "05";
	public static final String E_COMMERCE_INDICATOR_7 = "07";
	
	public static final String BLIBLI_PB3 = "49002900002";
	public static final String BLIBLI_PB6 = "49002900003";
	public static final String BLIBLI_PB12 = "49002900004";
	
	public static final String FRD_ORDER_HISTORY_CUSTOMER_FULLNAME_FILTER = "1";
	public static final String FRD_ORDER_HISTORY_CUSTOMER_PHONE_FILTER = "2";
	public static final String FRD_ORDER_HISTORY_CUSTOMER_MOBILE_FILTER = "3";
	public static final String FRD_ORDER_HISTORY_CUSTOMER_EMAIL_FILTER = "4";
	public static final String FRD_ORDER_HISTORY_CUSTOMER_ADDRESS_FILTER = "5";
	public static final String FRD_ORDER_HISTORY_SHIPPING_ADDRESS_FILTER = "6";
	public static final String FRD_ORDER_HISTORY_BILLING_ADDRESS_FILTER = "7";
	public static final String FRD_ORDER_HISTORY_IP_ADDRESS_FILTER = "8";
	public static final String FRD_ORDER_HISTORY_CC_NUMBER_FILTER = "9";
	
	public static final long FIN_AR_FUNDS_IN_REPORT_TIME_REAL_TIME= 1;
	public static final long FIN_AR_FUNDS_IN_REPORT_TIME_H1 = 2;
}