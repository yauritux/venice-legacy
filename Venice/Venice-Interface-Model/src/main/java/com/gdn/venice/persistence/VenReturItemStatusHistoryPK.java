package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_retur_item_status_history database table.
 * 
 */
@Embeddable
public class VenReturItemStatusHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="retur_item_id")
	private Long returItemId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp")
	private java.util.Date historyTimestamp;

    public VenReturItemStatusHistoryPK() {
    }
	public Long getReturItemId() {
		return this.returItemId;
	}
	public void setReturItemId(Long returItemId) {
		this.returItemId = returItemId;
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
		if (!(other instanceof VenReturItemStatusHistoryPK)) {
			return false;
		}
		VenReturItemStatusHistoryPK castOther = (VenReturItemStatusHistoryPK)other;
		return 
			this.returItemId.equals(castOther.returItemId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.returItemId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}