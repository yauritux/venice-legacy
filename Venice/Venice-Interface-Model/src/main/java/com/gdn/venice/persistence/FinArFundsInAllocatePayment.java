package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the fin_ar_funds_in_allocate_payment database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_allocate_payment")
public class FinArFundsInAllocatePayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_allocate_payment")  
	@TableGenerator(name="fin_ar_funds_in_allocate_payment", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="id_recon_record")
	private Long idReconRecord;

	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="paidAmount")
	private BigDecimal paidamount;
	
	@Column(name="bankFee")
	private BigDecimal bankfee;	

	@Column(name="id_recon_record_dest")
	private Long idReconRecordDest;

	@Column(name="id_recon_record_source")
	private Long idReconRecordSource;

	@Column(name="isActive")
	private Boolean isactive;
	
	@Column(name="allocation_timestamp", nullable=true)
	private Timestamp allocationTimestamp;   

	public FinArFundsInAllocatePayment() {
    }

	public Long getIdReconRecord() {
		return this.idReconRecord;
	}

	public void setIdReconRecord(Long idReconRecord) {
		this.idReconRecord = idReconRecord;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getIdReconRecordDest() {
		return this.idReconRecordDest;
	}

	public void setIdReconRecordDest(Long idReconRecordDest) {
		this.idReconRecordDest = idReconRecordDest;
	}

	public Long getIdReconRecordSource() {
		return this.idReconRecordSource;
	}

	public void setIdReconRecordSource(Long idReconRecordSource) {
		this.idReconRecordSource = idReconRecordSource;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}
	
	public BigDecimal getBankfee() {
		return bankfee;
	}

	public void setBankfee(BigDecimal bankfee) {
		this.bankfee = bankfee;
	}
	
	 public Timestamp getAllocationTimestamp() {
			return allocationTimestamp;
	}

	public void setAllocationTimestamp(Timestamp allocationTimestamp) {
		this.allocationTimestamp = allocationTimestamp;
	}
}