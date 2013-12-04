package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_related_fraud_case database table.
 * 
 */
@Entity
@Table(name="frd_related_fraud_case")
public class FrdRelatedFraudCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FrdRelatedFraudCasePK id;

    public FrdRelatedFraudCase() {
    }

	public FrdRelatedFraudCasePK getId() {
		return this.id;
	}

	public void setId(FrdRelatedFraudCasePK id) {
		this.id = id;
	}
	
}