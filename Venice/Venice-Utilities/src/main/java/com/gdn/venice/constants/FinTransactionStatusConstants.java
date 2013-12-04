package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinTransactionStatusConstants {
	
	 FIN_TRANSACTION_STATUS_NEW(0),
	 FIN_TRANSACTION_STATUS_RECONCILED(1),
	 FIN_TRANSACTION_STATUS_POSTED(2),
	 FIN_TRANSACTION_STATUS_EXPORTED(3);
	 
	 private long id;
	 
	 public long id() {
		 return id;
	 }
	 
	 private FinTransactionStatusConstants(long id) {
		 this.id = id;
	 }
}
