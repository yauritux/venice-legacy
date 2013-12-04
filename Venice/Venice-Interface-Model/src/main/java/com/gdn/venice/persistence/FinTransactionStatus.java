package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_transaction_status database table.
 * 
 */
@Entity
@Table(name="fin_transaction_status")
public class FinTransactionStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_transaction_status")  
	@TableGenerator(name="fin_transaction_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="transaction_status_id", unique=true, nullable=false)
	private Long transactionStatusId;

	@Column(name="transaction_status_desc", nullable=false, length=100)
	private String transactionStatusDesc;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finTransactionStatus")
	private List<FinJournalTransaction> finJournalTransactions;

    public FinTransactionStatus() {
    }

	public Long getTransactionStatusId() {
		return this.transactionStatusId;
	}

	public void setTransactionStatusId(Long transactionStatusId) {
		this.transactionStatusId = transactionStatusId;
	}

	public String getTransactionStatusDesc() {
		return this.transactionStatusDesc;
	}

	public void setTransactionStatusDesc(String transactionStatusDesc) {
		this.transactionStatusDesc = transactionStatusDesc;
	}

	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
}