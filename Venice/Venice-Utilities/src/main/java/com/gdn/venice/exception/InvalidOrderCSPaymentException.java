package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderCSPaymentException extends InvalidOrderException {

	public InvalidOrderCSPaymentException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	private static final long serialVersionUID = -2464449256556704904L;

}
