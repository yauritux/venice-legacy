package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the frd_related_fraud_case database table.
 * 
 */
@Embeddable
public class FrdRelatedFraudCasePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="fraud_suspicion_case_id", unique=true, nullable=false)
	private Long fraudSuspicionCaseId;

	@Column(name="related_fraud_suspicion_case_id", unique=true, nullable=false)
	private Long relatedFraudSuspicionCaseId;

    public FrdRelatedFraudCasePK() {
    }
	public Long getFraudSuspicionCaseId() {
		return this.fraudSuspicionCaseId;
	}
	public void setFraudSuspicionCaseId(Long fraudSuspicionCaseId) {
		this.fraudSuspicionCaseId = fraudSuspicionCaseId;
	}
	public Long getRelatedFraudSuspicionCaseId() {
		return this.relatedFraudSuspicionCaseId;
	}
	public void setRelatedFraudSuspicionCaseId(Long relatedFraudSuspicionCaseId) {
		this.relatedFraudSuspicionCaseId = relatedFraudSuspicionCaseId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FrdRelatedFraudCasePK)) {
			return false;
		}
		FrdRelatedFraudCasePK castOther = (FrdRelatedFraudCasePK)other;
		return 
			this.fraudSuspicionCaseId.equals(castOther.fraudSuspicionCaseId)
			&& this.relatedFraudSuspicionCaseId.equals(castOther.relatedFraudSuspicionCaseId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fraudSuspicionCaseId.hashCode();
		hash = hash * prime + this.relatedFraudSuspicionCaseId.hashCode();
		
		return hash;
    }
}