package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class VeniceInternalException extends Exception {

	private static final long serialVersionUID = -5502201424187852986L;
	
	protected VeniceExceptionConstants errorCode;

	public VeniceInternalException(String message) {
		super(message);
	}
	
	public VeniceInternalException(String message, Throwable cause) {
		super(message, cause);
	}				
	
	public VeniceExceptionConstants getErrorCode() {
		return errorCode;
	}
}
