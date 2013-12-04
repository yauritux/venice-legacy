package com.gdn.venice.constants;

public enum VenWCSPaymentTypeIDConstants {
	 VEN_WCS_PAYMENT_TYPE_ID_KlikBCA(0),
	 VEN_WCS_PAYMENT_TYPE_ID_MandiriKlikpay(1),
	 VEN_WCS_PAYMENT_TYPE_ID_VirtualAccount(2),
	 VEN_WCS_PAYMENT_TYPE_ID_MIGSCreditCard(3),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYFullPayment(4),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYZeroPercentInstallment(5),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYXPercentInstallment(6),
	 VEN_WCS_PAYMENT_TYPE_ID_MIGSBCAInstallment(7),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYInstallment(8),
	 VEN_WCS_PAYMENT_TYPE_ID_CIMBClicks(9),
	 VEN_WCS_PAYMENT_TYPE_ID_XLTunai(10),
	 VEN_WCS_PAYMENT_TYPE_ID_MandiriInstallment(11),
	 VEN_WCS_PAYMENT_TYPE_ID_BIIngkisan(12),
	 VEN_WCS_PAYMENT_TYPE_ID_BRI(13),
	 VEN_WCS_PAYMENT_TYPE_ID_MandiriDebit(14),
	 VEN_WCS_PAYMENT_TYPE_ID_CSPayment(15),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYKartuKredit(16),
	 VEN_WCS_PAYMENT_TYPE_ID_KlikPAYKlikBCA(17);
	 
	 private long id;
	 
	 private VenWCSPaymentTypeIDConstants(long id) {
		 this.id = id;
	 }
	 
	 public long id() {
		 return id;
	 }	 	 
}
