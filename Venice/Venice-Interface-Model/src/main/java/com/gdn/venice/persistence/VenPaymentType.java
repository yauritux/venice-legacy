package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_payment_type database table.
 * 
 */
@Entity
@Table(name="ven_payment_type")
public class VenPaymentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_payment_type")  
	@TableGenerator(name="ven_payment_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_type_id", unique=true, nullable=false)
	private Long paymentTypeId;

	@Column(name="payment_type_code", nullable=false, length=100)
	private String paymentTypeCode;

	@Column(name="payment_type_desc", nullable=false, length=100)
	private String paymentTypeDesc;

	//bi-directional many-to-one association to VenOrderPayment
	@OneToMany(mappedBy="venPaymentType")
	private List<VenOrderPayment> venOrderPayments;

    public VenPaymentType() {
    }

	public Long getPaymentTypeId() {
		return this.paymentTypeId;
	}

	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getPaymentTypeCode() {
		return this.paymentTypeCode;
	}

	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}

	public String getPaymentTypeDesc() {
		return this.paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public List<VenOrderPayment> getVenOrderPayments() {
		return this.venOrderPayments;
	}

	public void setVenOrderPayments(List<VenOrderPayment> venOrderPayments) {
		this.venOrderPayments = venOrderPayments;
	}
	
}