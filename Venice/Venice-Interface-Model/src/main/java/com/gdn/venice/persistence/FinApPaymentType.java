package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_ap_payment_type database table.
 * 
 */
@Entity
@Table(name="fin_ap_payment_type")
public class FinApPaymentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ap_payment_type")  
	@TableGenerator(name="fin_ap_payment_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_type_id", unique=true, nullable=false)
	private Long paymentTypeId;

	@Column(name="payment_type_desc", nullable=false, length=100)
	private String paymentTypeDesc;

	//bi-directional many-to-one association to FinApPayment
	@OneToMany(mappedBy="finApPaymentType")
	private List<FinApPayment> finApPayments;

    public FinApPaymentType() {
    }

	public Long getPaymentTypeId() {
		return this.paymentTypeId;
	}

	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getPaymentTypeDesc() {
		return this.paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
}