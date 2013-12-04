package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the fin_ar_funds_in_refund database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_refund")
public class FinArFundsInRefund implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_refund")  
	@TableGenerator(name="fin_ar_funds_in_refund", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="refund_record_id", unique=true, nullable=false)
	private Long refundRecordId;

	@Column(name="ap_amount", nullable=false, precision=20, scale=2)
	private BigDecimal apAmount;

	@Column(name="ar_amount", precision=20, scale=2)
	private BigDecimal arAmount;
	
	@Column(name="refund_type", length=50)
	private String refundType;

	//bi-directional many-to-one association to FinApPayment
    @ManyToOne
	@JoinColumn(name="ap_payment_id")
	private FinApPayment finApPayment;

	//bi-directional many-to-one association to FinArFundsInReconRecord
    @ManyToOne
	@JoinColumn(name="reconciliation_record_id", nullable=false)
	private FinArFundsInReconRecord finArFundsInReconRecord;
    
    @Column(name="refund_timestamp", nullable=true)
	private Timestamp refundTimestamp;    
   
	@Column(name="ap_payment_timestamp", nullable=true)
	private Timestamp apPaymentTimestamp;

    public FinArFundsInRefund() {
    }

	public Long getRefundRecordId() {
		return this.refundRecordId;
	}

	public void setRefundRecordId(Long refundRecordId) {
		this.refundRecordId = refundRecordId;
	}

	public BigDecimal getApAmount() {
		return this.apAmount;
	}

	public void setApAmount(BigDecimal apAmount) {
		this.apAmount = apAmount;
	}

	public BigDecimal getArAmount() {
		return this.arAmount;
	}

	public void setArAmount(BigDecimal arAmount) {
		this.arAmount = arAmount;
	}

	public FinApPayment getFinApPayment() {
		return this.finApPayment;
	}

	public void setFinApPayment(FinApPayment finApPayment) {
		this.finApPayment = finApPayment;
	}
	
	public FinArFundsInReconRecord getFinArFundsInReconRecord() {
		return this.finArFundsInReconRecord;
	}

	public void setFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord) {
		this.finArFundsInReconRecord = finArFundsInReconRecord;
	}
	
	public Timestamp getRefundTimestamp() {
		return refundTimestamp;
	}

	public void setRefundTimestamp(Timestamp refundTimestamp) {
		this.refundTimestamp = refundTimestamp;
	}

	public Timestamp getApPaymentTimestamp() {
		return apPaymentTimestamp;
	}

	public void setApPaymentTimestamp(Timestamp apPaymentTimestamp) {
		this.apPaymentTimestamp = apPaymentTimestamp;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
}