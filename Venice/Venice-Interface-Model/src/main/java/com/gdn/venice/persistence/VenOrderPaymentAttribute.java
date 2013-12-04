package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_payment_attributes database table.
 * 
 */
@Entity
@Table(name="ven_order_payment_attributes")
public class VenOrderPaymentAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_payment_attributes")  
	@TableGenerator(name="ven_order_payment_attributes", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_attribute_id", unique=true, nullable=false)
	private Long paymentAttributeId;

	@Column(name="attribute_name", nullable=false, length=100)
	private String attributeName;

	@Column(name="attribute_value", nullable=false, length=200)
	private String attributeValue;

	//bi-directional many-to-one association to VenOrderPayment
    @ManyToOne
	@JoinColumn(name="order_payment_id", nullable=false)
	private VenOrderPayment venOrderPayment;

    public VenOrderPaymentAttribute() {
    }

	public Long getPaymentAttributeId() {
		return this.paymentAttributeId;
	}

	public void setPaymentAttributeId(Long paymentAttributeId) {
		this.paymentAttributeId = paymentAttributeId;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public VenOrderPayment getVenOrderPayment() {
		return this.venOrderPayment;
	}

	public void setVenOrderPayment(VenOrderPayment venOrderPayment) {
		this.venOrderPayment = venOrderPayment;
	}
	
}