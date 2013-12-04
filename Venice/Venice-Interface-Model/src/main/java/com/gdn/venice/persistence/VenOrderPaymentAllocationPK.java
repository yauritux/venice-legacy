package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_order_payment_allocation database table.
 * 
 */
@Embeddable
public class VenOrderPaymentAllocationPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="order_payment_id", unique=true, nullable=false)
	private Long orderPaymentId;

	@Column(name="order_id", unique=true, nullable=false)
	private Long orderId;

    public VenOrderPaymentAllocationPK() {
    }
	public Long getOrderPaymentId() {
		return this.orderPaymentId;
	}
	public void setOrderPaymentId(Long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}
	public Long getOrderId() {
		return this.orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VenOrderPaymentAllocationPK)) {
			return false;
		}
		VenOrderPaymentAllocationPK castOther = (VenOrderPaymentAllocationPK)other;
		return 
			this.orderPaymentId.equals(castOther.orderPaymentId)
			&& this.orderId.equals(castOther.orderId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orderPaymentId.hashCode();
		hash = hash * prime + this.orderId.hashCode();
		
		return hash;
    }
}