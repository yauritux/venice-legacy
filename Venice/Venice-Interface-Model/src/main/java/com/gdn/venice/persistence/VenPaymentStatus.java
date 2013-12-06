package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_payment_status database table.
 * 
 */
@Entity
@Table(name="ven_payment_status")
public class VenPaymentStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_payment_status")  
	@TableGenerator(name="ven_payment_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_status_id", unique=true, nullable=false)
	private Long paymentStatusId;

	@Column(name="payment_status_code", nullable=false, length=100)
	private String paymentStatusCode;

	@Column(name="payment_status_desc", nullable=false, length=100)
	private String paymentStatusDesc;

	//bi-directional many-to-one association to VenOrderPayment
	@OneToMany(mappedBy="venPaymentStatus")
	private List<VenOrderPayment> venOrderPayments;

    public VenPaymentStatus() {
    }

	public Long getPaymentStatusId() {
		return this.paymentStatusId;
	}

	public void setPaymentStatusId(Long paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public String getPaymentStatusCode() {
		return this.paymentStatusCode;
	}

	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}

	public String getPaymentStatusDesc() {
		return this.paymentStatusDesc;
	}

	public void setPaymentStatusDesc(String paymentStatusDesc) {
		this.paymentStatusDesc = paymentStatusDesc;
	}

	public List<VenOrderPayment> getVenOrderPayments() {
		return this.venOrderPayments;
	}

	public void setVenOrderPayments(List<VenOrderPayment> venOrderPayments) {
		this.venOrderPayments = venOrderPayments;
	}
	
}