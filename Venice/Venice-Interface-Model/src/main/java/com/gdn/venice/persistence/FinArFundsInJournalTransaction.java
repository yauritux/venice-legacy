package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fin_ar_funds_in_journal_transaction database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_journal_transaction")
public class FinArFundsInJournalTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FinArFundsInJournalTransactionPK id;
	
	//bi-directional many-to-one association to FinJournalTransaction
    @ManyToOne
	@JoinColumn(name="transaction_id", nullable=false, insertable=false, updatable=false)
	private FinJournalTransaction finJournalTransactions;

	//bi-directional many-to-one association to FinArFundsInReconRecord
    @ManyToOne
	@JoinColumn(name="reconciliation_record_id", nullable=false, insertable=false, updatable=false)
	private FinArFundsInReconRecord finArFundsInReconRecords;

    public FinArFundsInJournalTransaction() {
    }

	public FinArFundsInJournalTransactionPK getId() {
		return this.id;
	}

	public void setId(FinArFundsInJournalTransactionPK id) {
		this.id = id;
	}
	
	public FinJournalTransaction getFinJournalTransactions() {
		return finJournalTransactions;
	}

	public void setFinJournalTransactions(
			FinJournalTransaction finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}

	public FinArFundsInReconRecord getFinArFundsInReconRecord() {
		return finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecord(
			FinArFundsInReconRecord finArFundsInReconRecord) {
		this.finArFundsInReconRecords = finArFundsInReconRecord;
	}
	
}