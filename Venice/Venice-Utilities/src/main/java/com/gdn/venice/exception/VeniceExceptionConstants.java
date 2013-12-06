package com.gdn.venice.exception;

public enum VeniceExceptionConstants {
	
	VEN_EX_000001("Invalid Order"),
	VEN_EX_000002("No Order Received"),
	VEN_EX_000003("No Order Amount"),
	VEN_EX_000004("No Customer Record"),
	VEN_EX_000005("No Order ID"),
	VEN_EX_000006("No Order Items"),
	VEN_EX_000007("No Payment Information"),
	VEN_EX_000008("No Timestamp Information"),
	VEN_EX_000009("No Status Information"),
	VEN_EX_000010("Fullfilment status not 1"),
	VEN_EX_999999("Unknown Exception");
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	private VeniceExceptionConstants(String message) {
		this.message = message;
	}
}
