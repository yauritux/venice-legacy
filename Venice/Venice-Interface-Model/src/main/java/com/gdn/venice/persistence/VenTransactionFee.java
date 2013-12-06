package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_transaction_fees database table.
 * 
 */
@Entity
@Table(name="ven_transaction_fees")
public class VenTransactionFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenTransactionFeePK id;

	@Column(name="fee_amount", nullable=false, precision=20, scale=2)
	private BigDecimal feeAmount;

	//bi-directional many-to-one association to VenMerchant
    @ManyToOne
	@JoinColumn(name="merchant_id", nullable=false, insertable=false, updatable=false)
	private VenMerchant venMerchant;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false, insertable=false, updatable=false)
	private VenOrder venOrder;

    public VenTransactionFee() {
    }

	public VenTransactionFeePK getId() {
		return this.id;
	}

	public void setId(VenTransactionFeePK id) {
		this.id = id;
	}
	
	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public VenMerchant getVenMerchant() {
		return this.venMerchant;
	}

	public void setVenMerchant(VenMerchant venMerchant) {
		this.venMerchant = venMerchant;
	}
	
	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
}