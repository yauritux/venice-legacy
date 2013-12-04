package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum VenPartyTypeConstants {

	 VEN_PARTY_TYPE_BANK(0),
	 VEN_PARTY_TYPE_MERCHANT(1),
	 VEN_PARTY_TYPE_LOGISTICS(2),
	 VEN_PARTY_TYPE_RECIPIENT(3),
	 VEN_PARTY_TYPE_CUSTOMER(4),
	 VEN_PARTY_TYPE_USER(5);
	 
	 private long code;
	 
	 private VenPartyTypeConstants(long code) {
		 this.code = code;
	 }
	 
	 public long code() {
		 return code;
	 }
}
