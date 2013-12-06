package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_transaction_type database table.
 * 
 */
@Entity
@Table(name="fin_transaction_type")
public class FinTransactionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_transaction_type")  
	@TableGenerator(name="fin_transaction_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="transaction_type_id", unique=true, nullable=false)
	private Long transactionTypeId;

	@Column(name="transaction_type_desc", nullable=false, length=100)
	private String transactionTypeDesc;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finTransactionType")
	private List<FinJournalTransaction> finJournalTransactions;

    public FinTransactionType() {
    }

	public Long getTransactionTypeId() {
		return this.transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getTransactionTypeDesc() {
		return this.transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc) {
		this.transactionTypeDesc = transactionTypeDesc;
	}

	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}

}