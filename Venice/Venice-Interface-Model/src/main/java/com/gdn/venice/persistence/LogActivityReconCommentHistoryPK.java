package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the log_activity_recon_comment_history database table.
 * 
 */
@Embeddable
public class LogActivityReconCommentHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="activity_recon_record_id", unique=true, nullable=false)
	private Long activityReconRecordId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="history_timestamp", unique=true, nullable=false)
	private java.util.Date historyTimestamp;

    public LogActivityReconCommentHistoryPK() {
    }
	public Long getActivityReconRecordId() {
		return this.activityReconRecordId;
	}
	public void setActivityReconRecordId(Long activityReconRecordId) {
		this.activityReconRecordId = activityReconRecordId;
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
		if (!(other instanceof LogActivityReconCommentHistoryPK)) {
			return false;
		}
		LogActivityReconCommentHistoryPK castOther = (LogActivityReconCommentHistoryPK)other;
		return 
			this.activityReconRecordId.equals(castOther.activityReconRecordId)
			&& this.historyTimestamp.equals(castOther.historyTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.activityReconRecordId.hashCode();
		hash = hash * prime + this.historyTimestamp.hashCode();
		
		return hash;
    }
}