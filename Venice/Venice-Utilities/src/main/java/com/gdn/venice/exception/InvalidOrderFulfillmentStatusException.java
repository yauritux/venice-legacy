package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderFulfillmentStatusException extends InvalidOrderException {
	
	public InvalidOrderFulfillmentStatusException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	private static final long serialVersionUID = -8680942311997198902L;

}
