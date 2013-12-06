package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_fraud_related_order_info database table.
 * 
 */
@Entity
@Table(name="frd_fraud_related_order_info")
public class FrdFraudRelatedOrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FrdFraudRelatedOrderInfoPK id;

	//bi-directional one-to-one association to FrdFraudSuspicionCase
	@ManyToOne
	@JoinColumn(name="fraud_suspicion_case_id", nullable=false, insertable=false, updatable=false)
	private FrdFraudSuspicionCase frdFraudSuspicionCase;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false, insertable=false, updatable=false)
	private VenOrder venOrder;

    public FrdFraudRelatedOrderInfo() {
    }

	public FrdFraudRelatedOrderInfoPK getId() {
		return this.id;
	}

	public void setId(FrdFraudRelatedOrderInfoPK id) {
		this.id = id;
	}
	
	public FrdFraudSuspicionCase getFrdFraudSuspicionCase() {
		return this.frdFraudSuspicionCase;
	}

	public void setFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase) {
		this.frdFraudSuspicionCase = frdFraudSuspicionCase;
	}
	
	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
}