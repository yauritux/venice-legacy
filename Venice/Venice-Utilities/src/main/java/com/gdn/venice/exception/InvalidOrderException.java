package com.gdn.venice.exception;

import static com.gdn.venice.constants.VeniceExceptionConstants.VEN_EX_000001;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderException extends VeniceInternalException {

	private static final long serialVersionUID = 2437996569564419677L;

	public InvalidOrderException(String message) {
		super(message);
		errorCode = VEN_EX_000001; // default exception to 'Invalid Order'
	}
	
	public InvalidOrderException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public InvalidOrderException(String message, Throwable cause) {
		super(message, cause);
		errorCode = VEN_EX_000001; // default exception to 'Invalid Order'
	}				
	
	public InvalidOrderException(String message, Throwable cause, VeniceExceptionConstants errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}	
}
