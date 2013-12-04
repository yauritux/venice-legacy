package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum VenVAPaymentStatusIDConstants {
	
	 VEN_VA_PAYMENT_STATUS_ID_APPROVING(0),
	 VEN_VA_PAYMENT_STATUS_ID_APPROVED(1);
	 
	 private long code;
	 
	 private VenVAPaymentStatusIDConstants(long code) {
		 this.code = code;
	 }
	 
	 public long code() {
		 return code;
	 }
}
