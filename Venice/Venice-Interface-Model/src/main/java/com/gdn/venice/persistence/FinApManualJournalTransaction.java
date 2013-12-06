package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fin_ap_manual_journal_transaction database table.
 * 
 */
@Entity
@Table(name="fin_ap_manual_journal_transaction")
public class FinApManualJournalTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ap_manual_journal_transaction")  
	@TableGenerator(name="fin_ap_manual_journal_transaction", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="manual_journal_transaction_id", unique=true, nullable=false)
	private Long manualJournalTransactionId;

	//bi-directional many-to-one association to FinApPayment
    @ManyToOne
	@JoinColumn(name="ap_payment_id")
	private FinApPayment finApPayment;

	//bi-directional many-to-one association to FinJournalTransaction
    @ManyToOne
	@JoinColumn(name="transaction_id", nullable=false)
	private FinJournalTransaction finJournalTransaction;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id")
	private VenOrder venOrder;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id")
	private VenParty venParty;

    public FinApManualJournalTransaction() {
    }

	public Long getManualJournalTransactionId() {
		return this.manualJournalTransactionId;
	}

	public void setManualJournalTransactionId(Long manualJournalTransactionId) {
		this.manualJournalTransactionId = manualJournalTransactionId;
	}

	public FinApPayment getFinApPayment() {
		return this.finApPayment;
	}

	public void setFinApPayment(FinApPayment finApPayment) {
		this.finApPayment = finApPayment;
	}
	
	public FinJournalTransaction getFinJournalTransaction() {
		return this.finJournalTransaction;
	}

	public void setFinJournalTransaction(FinJournalTransaction finJournalTransaction) {
		this.finJournalTransaction = finJournalTransaction;
	}
	
	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}