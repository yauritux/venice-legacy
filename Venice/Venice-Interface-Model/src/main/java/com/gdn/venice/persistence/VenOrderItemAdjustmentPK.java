package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_order_item_adjustment database table.
 * 
 */
@Embeddable
public class VenOrderItemAdjustmentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="promotion_id", unique=true, nullable=false)
	private Long promotionId;

	@Column(name="order_item_id", unique=true, nullable=false)
	private Long orderItemId;

    public VenOrderItemAdjustmentPK() {
    }
	public Long getPromotionId() {
		return this.promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public Long getOrderItemId() {
		return this.orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VenOrderItemAdjustmentPK)) {
			return false;
		}
		VenOrderItemAdjustmentPK castOther = (VenOrderItemAdjustmentPK)other;
		return 
			this.promotionId.equals(castOther.promotionId)
			&& this.orderItemId.equals(castOther.orderItemId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.promotionId.hashCode();
		hash = hash * prime + this.orderItemId.hashCode();
		
		return hash;
    }
}