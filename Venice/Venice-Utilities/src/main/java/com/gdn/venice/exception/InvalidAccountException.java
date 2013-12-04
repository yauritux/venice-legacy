package com.gdn.venice.exception;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidAccountException extends VeniceInternalException {

	private static final long serialVersionUID = 682411538253867690L;
	
	public InvalidAccountException(String message) {
		super(message);
	}
	
	public InvalidAccountException(String message, Throwable cause) {
		super(message, cause);
	}			
}
