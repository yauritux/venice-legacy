package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the fin_ar_funds_in_recon_comments database table.
 * 
 */
@Embeddable
public class FinArFundsInReconCommentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="reconciliation_record_id", unique=true, nullable=false)
	private Long reconciliationRecordId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="comment_timestamp", unique=true, nullable=false)
	private java.util.Date commentTimestamp;

    public FinArFundsInReconCommentPK() {
    }
	public Long getReconciliationRecordId() {
		return this.reconciliationRecordId;
	}
	public void setReconciliationRecordId(Long reconciliationRecordId) {
		this.reconciliationRecordId = reconciliationRecordId;
	}
	public java.util.Date getCommentTimestamp() {
		return this.commentTimestamp;
	}
	public void setCommentTimestamp(java.util.Date commentTimestamp) {
		this.commentTimestamp = commentTimestamp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FinArFundsInReconCommentPK)) {
			return false;
		}
		FinArFundsInReconCommentPK castOther = (FinArFundsInReconCommentPK)other;
		return 
			this.reconciliationRecordId.equals(castOther.reconciliationRecordId)
			&& this.commentTimestamp.equals(castOther.commentTimestamp);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.reconciliationRecordId.hashCode();
		hash = hash * prime + this.commentTimestamp.hashCode();
		
		return hash;
    }
}