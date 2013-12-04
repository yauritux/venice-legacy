package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ven_fraud_check_status database table.
 * 
 */
@Entity
@Table(name="ven_fraud_check_status")
public class VenFraudCheckStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_fraud_check_status")  
	@TableGenerator(name="ven_fraud_check_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_check_status_id", unique=true, nullable=false)
	private Long fraudCheckStatusId;

	@Column(name="fraud_check_status_desc", nullable=false, length=100)
	private String fraudCheckStatusDesc;

	//bi-directional many-to-one association to VenOrder
	@OneToMany(mappedBy="venFraudCheckStatus")
	private List<VenOrder> venOrders;

    public VenFraudCheckStatus() {
    }

	public Long getFraudCheckStatusId() {
		return this.fraudCheckStatusId;
	}

	public void setFraudCheckStatusId(Long fraudCheckStatusId) {
		this.fraudCheckStatusId = fraudCheckStatusId;
	}

	public String getFraudCheckStatusDesc() {
		return this.fraudCheckStatusDesc;
	}

	public void setFraudCheckStatusDesc(String fraudCheckStatusDesc) {
		this.fraudCheckStatusDesc = fraudCheckStatusDesc;
	}

	public List<VenOrder> getVenOrders() {
		return this.venOrders;
	}

	public void setVenOrders(List<VenOrder> venOrders) {
		this.venOrders = venOrders;
	}
	
}