package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinJournalConstants {
	FIN_JOURNAL_CASH_RECEIVE(0),
	FIN_JOURNAL_SALES(1),
	FIN_JOURNAL_LOGISTICS_DEBT_ACKNOWLEDGEMENT(2),
	FIN_JOURNAL_REFUND_OTHERS(3),
	FIN_JOURNAL_PAYMENT(4),
	FIN_JOURNAL_MANUAL(5),
	FIN_JOURNAL_ALLOCATION(6);
	
	private long id;
	
	public long id() {
		return id;
	}
	
	private FinJournalConstants(long id) {
		this.id = id;
	}
}
