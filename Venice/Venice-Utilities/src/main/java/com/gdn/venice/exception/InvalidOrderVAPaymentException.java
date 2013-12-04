package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderVAPaymentException extends InvalidOrderException {

	public InvalidOrderVAPaymentException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	private static final long serialVersionUID = 1437265996967440938L;

}
