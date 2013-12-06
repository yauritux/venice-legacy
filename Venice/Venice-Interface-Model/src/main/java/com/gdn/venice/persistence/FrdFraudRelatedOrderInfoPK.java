package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the frd_fraud_related_order_info database table.
 * 
 */
@Embeddable
public class FrdFraudRelatedOrderInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="fraud_suspicion_case_id", unique=true, nullable=false)
	private Long fraudSuspicionCaseId;

	@Column(name="order_id", unique=true, nullable=false)
	private Long orderId;

    public FrdFraudRelatedOrderInfoPK() {
    }
	public Long getFraudSuspicionCaseId() {
		return this.fraudSuspicionCaseId;
	}
	public void setFraudSuspicionCaseId(Long fraudSuspicionCaseId) {
		this.fraudSuspicionCaseId = fraudSuspicionCaseId;
	}
	public Long getOrderId() {
		return this.orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FrdFraudRelatedOrderInfoPK)) {
			return false;
		}
		FrdFraudRelatedOrderInfoPK castOther = (FrdFraudRelatedOrderInfoPK)other;
		return 
			this.fraudSuspicionCaseId.equals(castOther.fraudSuspicionCaseId)
			&& this.orderId.equals(castOther.orderId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fraudSuspicionCaseId.hashCode();
		hash = hash * prime + this.orderId.hashCode();
		
		return hash;
    }
}