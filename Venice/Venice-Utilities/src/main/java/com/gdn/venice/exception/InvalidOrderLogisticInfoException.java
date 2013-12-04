package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderLogisticInfoException extends InvalidOrderException {
	
	public InvalidOrderLogisticInfoException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	private static final long serialVersionUID = -3899088116224657826L;
	
}
