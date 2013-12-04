package com.gdn.venice.constants;

public enum VenWCSPaymentTypeConstants {

	 VEN_WCS_PAYMENT_TYPE_KlikBCA("KlikBCA"),
	 VEN_WCS_PAYMENT_TYPE_DebitMandiri("DebitMandiri"),
	 VEN_WCS_PAYMENT_TYPE_MandiriKlikpay("MandiriKlikpay"),
	 VEN_WCS_PAYMENT_TYPE_VirtualAccount("VirtualAccount"),
	 VEN_WCS_PAYMENT_TYPE_MIGSCreditCard("MIGSCreditCard"),
	 VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment("KlikPAYFullPayment"),

	 // 2 constants below are replacing KlikPAYFullPayment
	 VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit("KlikPAYKartuKredit"),
	 VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA("KlikPAYKlikBCA"),
	
	 VEN_WCS_PAYMENT_TYPE_KlikPAYZeroPercentInstallment("KlikPAYZeroPercentInstallment"),
	 VEN_WCS_PAYMENT_TYPE_KlikPAYXPercentInstallment("KlikPAYXPercentInstallment"),
	 VEN_WCS_PAYMENT_TYPE_MIGSBCAInstallment("MIGSBCAInst"),
	 VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment("KlikPayInst"),
	 VEN_WCS_PAYMENT_TYPE_CIMBClicks("CIMBClicks"),
	 VEN_WCS_PAYMENT_TYPE_XLTunai("XLTunai"),
	 VEN_WCS_PAYMENT_TYPE_MandiriInstallment("MandiriInstallment"),
	 VEN_WCS_PAYMENT_TYPE_BIIngkisan("BIIngkisan"),
	 VEN_WCS_PAYMENT_TYPE_BRI("BRI"),
	 VEN_WCS_PAYMENT_TYPE_MandiriDebit("MandiriDebit"),
	 VEN_WCS_PAYMENT_TYPE_CSPayment("CSPayment"),
	
	//this is only for wcs
	 VEN_WCS_PAYMENT_TYPE_PartialFulfillment("PartialFulfillment");
	 
	 private String desc;
	 
	 private VenWCSPaymentTypeConstants(String desc) {
		 this.desc = desc;
	 }
	 
	 public String desc() {
		 return desc;
	 }
	
}
