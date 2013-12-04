package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the fin_ar_funds_in_journal_transaction database table.
 * 
 */
@Embeddable
public class FinArFundsInJournalTransactionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="reconciliation_record_id")
	private Long reconciliationRecordId;

	@Column(name="transaction_id")
	private Long transactionId;

    public FinArFundsInJournalTransactionPK() {
    }
	public Long getReconciliationRecordId() {
		return this.reconciliationRecordId;
	}
	public void setReconciliationRecordId(Long reconciliationRecordId) {
		this.reconciliationRecordId = reconciliationRecordId;
	}
	public Long getTransactionId() {
		return this.transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FinArFundsInJournalTransactionPK)) {
			return false;
		}
		FinArFundsInJournalTransactionPK castOther = (FinArFundsInJournalTransactionPK)other;
		return 
			this.reconciliationRecordId.equals(castOther.reconciliationRecordId)
			&& this.transactionId.equals(castOther.transactionId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.reconciliationRecordId.hashCode();
		hash = hash * prime + this.transactionId.hashCode();
		
		return hash;
    }
}