package com.gdn.venice.exception;

/**
 * 
 * @author yauritux
 *
 */
public class NoParameterSuppliedException extends VeniceInternalException {

	private static final long serialVersionUID = -1832220203703348110L;
	
	public NoParameterSuppliedException(String message) {
		super(message);
	}
	
	public NoParameterSuppliedException(String message, Throwable cause) {
		super(message, cause);
	}		
}
