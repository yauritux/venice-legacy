package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum VenPaymentTypeConstants {
	
	 VEN_PAYMENT_TYPE_CC(0, "CC"),
	 VEN_PAYMENT_TYPE_IB(1, "IB"),
	 VEN_PAYMENT_TYPE_VA(2, "VA"),
	 VEN_PAYMENT_TYPE_CS(3, "CS");
	 
	 private long id;
	 private String desc;
	 
	 private VenPaymentTypeConstants(long id, String desc) {
		 this.id = id;
		 this.desc = desc;
	 }
	 
	 public long id() {
		 return id;
	 }
	 
	 public String desc() {
		 return desc;
	 }
}
