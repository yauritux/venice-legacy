package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_order_payment_allocation database table.
 * 
 */
@Entity
@Table(name="ven_order_payment_allocation")
public class VenOrderPaymentAllocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenOrderPaymentAllocationPK id;

	@Column(name="allocation_amount", nullable=false, precision=20, scale=2)
	private BigDecimal allocationAmount;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false, insertable=false, updatable=false)
	private VenOrder venOrder;

	//bi-directional many-to-one association to VenOrderPayment
    @ManyToOne
	@JoinColumn(name="order_payment_id", nullable=false, insertable=false, updatable=false)
	private VenOrderPayment venOrderPayment;

    public VenOrderPaymentAllocation() {
    }

	public VenOrderPaymentAllocationPK getId() {
		return this.id;
	}

	public void setId(VenOrderPaymentAllocationPK id) {
		this.id = id;
	}
	
	public BigDecimal getAllocationAmount() {
		return this.allocationAmount;
	}

	public void setAllocationAmount(BigDecimal allocationAmount) {
		this.allocationAmount = allocationAmount;
	}

	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
	public VenOrderPayment getVenOrderPayment() {
		return this.venOrderPayment;
	}

	public void setVenOrderPayment(VenOrderPayment venOrderPayment) {
		this.venOrderPayment = venOrderPayment;
	}
	
}