package com.gdn.venice.exception;

/**
 * 
 * @author yauritux
 *
 */
public class NoRecordFoundException extends VeniceInternalException {

	private static final long serialVersionUID = -6293554267795181286L;

	public NoRecordFoundException(String message) {
		super(message);
	}
	
	public NoRecordFoundException(String message, Throwable cause) {
		super(message, cause);
	}	
}
