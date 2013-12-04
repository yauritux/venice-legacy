package com.gdn.venice.exception;

/**
 * 
 * @author yauritux
 *
 */
public class UnsupportedJournalTransactionException extends VeniceInternalException {

	private static final long serialVersionUID = 2485781474549613610L;
	
	public UnsupportedJournalTransactionException(String message) {
		super(message);
	}
	
	public UnsupportedJournalTransactionException(String message, Throwable cause) {
		super(message, cause);
	}
}
