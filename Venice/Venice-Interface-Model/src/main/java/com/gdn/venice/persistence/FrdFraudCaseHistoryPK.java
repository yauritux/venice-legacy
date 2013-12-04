package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the frd_fraud_case_history database table.
 * 
 */
@Embeddable
public class FrdFraudCaseHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="fraud_suspicion_case_id", unique=true, nullable=false)
	private Long fraudSuspicionCaseId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="fraud_case_history_date", unique=true, nullable=false)
	private java.util.Date fraudCaseHistoryDate;

    public FrdFraudCaseHistoryPK() {
    }
	public Long getFraudSuspicionCaseId() {
		return this.fraudSuspicionCaseId;
	}
	public void setFraudSuspicionCaseId(Long fraudSuspicionCaseId) {
		this.fraudSuspicionCaseId = fraudSuspicionCaseId;
	}
	public java.util.Date getFraudCaseHistoryDate() {
		return this.fraudCaseHistoryDate;
	}
	public void setFraudCaseHistoryDate(java.util.Date fraudCaseHistoryDate) {
		this.fraudCaseHistoryDate = fraudCaseHistoryDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FrdFraudCaseHistoryPK)) {
			return false;
		}
		FrdFraudCaseHistoryPK castOther = (FrdFraudCaseHistoryPK)other;
		return 
			this.fraudSuspicionCaseId.equals(castOther.fraudSuspicionCaseId)
			&& this.fraudCaseHistoryDate.equals(castOther.fraudCaseHistoryDate);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fraudSuspicionCaseId.hashCode();
		hash = hash * prime + this.fraudCaseHistoryDate.hashCode();
		
		return hash;
    }
}