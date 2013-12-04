package com.gdn.venice.exception;

import com.gdn.venice.constants.VeniceExceptionConstants;

/**
 * 
 * @author yauritux
 *
 */
public class DuplicateWCSOrderIDException extends InvalidOrderException {

	
	public DuplicateWCSOrderIDException(String message, VeniceExceptionConstants errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	private static final long serialVersionUID = 2921006742496189781L;
		
}
