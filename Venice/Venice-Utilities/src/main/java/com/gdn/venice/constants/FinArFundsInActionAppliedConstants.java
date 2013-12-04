package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinArFundsInActionAppliedConstants {
	
	FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE(0),
	FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER(1),
	FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED(2),
	FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED(3),
	FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK(4);
	
	private long id;
	
	public long id() {
		return id;
	}
	
	private FinArFundsInActionAppliedConstants(long id) {
		this.id = id;
	}
}
