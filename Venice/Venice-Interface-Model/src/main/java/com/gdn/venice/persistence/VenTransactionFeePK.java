package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_transaction_fees database table.
 * 
 */
@Embeddable
public class VenTransactionFeePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="merchant_id", unique=true, nullable=false)
	private Long merchantId;

	@Column(name="order_id", unique=true, nullable=false)
	private Long orderId;

    public VenTransactionFeePK() {
    }
	public Long getMerchantId() {
		return this.merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
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
		if (!(other instanceof VenTransactionFeePK)) {
			return false;
		}
		VenTransactionFeePK castOther = (VenTransactionFeePK)other;
		return 
			this.merchantId.equals(castOther.merchantId)
			&& this.orderId.equals(castOther.orderId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.merchantId.hashCode();
		hash = hash * prime + this.orderId.hashCode();
		
		return hash;
    }
}