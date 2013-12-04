package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum VenCSPaymentStatusIDConstants {

	 VEN_CS_PAYMENT_STATUS_ID_APPROVING(0),
	 VEN_CS_PAYMENT_STATUS_ID_APPROVED(1);
	 
	 private long code;
	 
	 private VenCSPaymentStatusIDConstants(long code) {
		 this.code = code;
	 }
	 
	 public long code() {
		 return code;
	 }	
}
