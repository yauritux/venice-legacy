package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderTimestampException extends InvalidOrderException {

	public InvalidOrderTimestampException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	private static final long serialVersionUID = 8355613737002063599L;
	
}
