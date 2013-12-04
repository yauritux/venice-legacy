package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_order_item_status_history database table.
 * 
 */
@Embeddable
public class VenOrderItemStatusHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="order_item_id", unique=true, nullable=false)
	private Long orderItemId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp", unique=true, nullable=false)
	private java.util.Date historyTimestamp;

    public VenOrderItemStatusHistoryPK() {
    }
	public Long getOrderItemId() {
		return this.orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public java.util.Date getHistoryTimestamp() {
		return this.historyTimestamp;
	}
	public void setHistoryTimestamp(java.util.Date historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VenOrderItemStatusHistoryPK)) {
			return false;
		}
		VenOrderItemStatusHistoryPK castOther = (VenOrderItemStatusHistoryPK)other;
		return 
			this.orderItemId.equals(castOther.orderItemId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orderItemId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}