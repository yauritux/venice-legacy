package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_order_status_history database table.
 * 
 */
@Embeddable
public class VenOrderStatusHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="order_id", unique=true, nullable=false)
	private Long orderId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp", unique=true, nullable=false)
	private java.util.Date historyTimestamp;

    public VenOrderStatusHistoryPK() {
    }
	public Long getOrderId() {
		return this.orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
		if (!(other instanceof VenOrderStatusHistoryPK)) {
			return false;
		}
		VenOrderStatusHistoryPK castOther = (VenOrderStatusHistoryPK)other;
		return 
			this.orderId.equals(castOther.orderId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orderId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}