package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_retur_status_history database table.
 * 
 */
@Embeddable
public class VenReturStatusHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="retur_id")
	private Long returId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp")
	private java.util.Date historyTimestamp;

    public VenReturStatusHistoryPK() {
    }
	public Long getReturId() {
		return this.returId;
	}
	public void setReturId(Long returId) {
		this.returId = returId;
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
		if (!(other instanceof VenReturStatusHistoryPK)) {
			return false;
		}
		VenReturStatusHistoryPK castOther = (VenReturStatusHistoryPK)other;
		return 
			this.returId.equals(castOther.returId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.returId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}