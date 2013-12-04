package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum OrderConstants {
	
	VEN_FULFILLMENT_STATUS_ONE(1);
	
	private int code;
	
	private OrderConstants(int code) {
		this.code = code;
	}
	
	public int code() {
		return code;
	}
}
