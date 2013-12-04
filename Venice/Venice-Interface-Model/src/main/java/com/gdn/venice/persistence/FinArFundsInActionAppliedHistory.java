package com.gdn.venice.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the fin_ar_funds_in_action_applied_history database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_action_applied_history")
public class FinArFundsInActionAppliedHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_action_applied_history")  
	@TableGenerator(name="fin_ar_funds_in_action_applied_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="action_taken_id")
	private Long actionTakenId;

	//bi-directional many-to-one association to 
	@ManyToOne
	@JoinColumn(name="action_applied_id", nullable=false)
	private FinArFundsInActionApplied finArFundsInActionApplied;
	
	//bi-directional many-to-one association to 
	@ManyToOne
	@JoinColumn(name="reconciliation_record_id", nullable=false)
	private FinArFundsInReconRecord finArFundsInReconRecords;
	
	 @Temporal( TemporalType.TIMESTAMP)
	@Column(name="action_taken_timestamp",nullable=true)
	private Date actionTakenTimestamp;

	@Column(name="amount", precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="reference_id")
	private String referenceId;

    public FinArFundsInActionAppliedHistory() {
    }

	public Long getActionTakenId() {
		return this.actionTakenId;
	}

	public void setActionTakenId(Long actionTakenId) {
		this.actionTakenId = actionTakenId;
	}

	public Date getActionTakenTimestamp() {
		return this.actionTakenTimestamp;
	}

	public void setActionTakenTimestamp(Date actionTakenTimestamp) {
		this.actionTakenTimestamp = actionTakenTimestamp;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	public FinArFundsInActionApplied getFinArFundsInActionApplied() {
		return finArFundsInActionApplied;
	}

	public void setFinArFundsInActionApplied(
			FinArFundsInActionApplied finArFundsInActionApplied) {
		this.finArFundsInActionApplied = finArFundsInActionApplied;
	}

	public FinArFundsInReconRecord getFinArFundsInReconRecords() {
		return finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(
			FinArFundsInReconRecord finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}

}