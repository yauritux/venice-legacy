package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class CannotPersistOrderPaymentException extends VeniceInternalException {

	private static final long serialVersionUID = 1L;

	public CannotPersistOrderPaymentException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
