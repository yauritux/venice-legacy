package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class InvalidOrderStatusException extends InvalidOrderException {
	
	public InvalidOrderStatusException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	private static final long serialVersionUID = 8380585073486282503L;

}
