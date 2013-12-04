package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_fraud_suspicion_points database table.
 * 
 */
@Entity
@Table(name="frd_fraud_suspicion_points")
public class FrdFraudSuspicionPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_fraud_suspicion_points")  
	@TableGenerator(name="frd_fraud_suspicion_points", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_suspicion_points_id", unique=true, nullable=false)
	private Long fraudSuspicionPointsId;

	@Column(name="fraud_rule_name", nullable=false, length=1000)
	private String fraudRuleName;

	@Column(name="risk_points", nullable=false)
	private Integer riskPoints;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
    @ManyToOne
	@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
	private FrdFraudSuspicionCase frdFraudSuspicionCase;

    public FrdFraudSuspicionPoint() {
    }

	public Long getFraudSuspicionPointsId() {
		return this.fraudSuspicionPointsId;
	}

	public void setFraudSuspicionPointsId(Long fraudSuspicionPointsId) {
		this.fraudSuspicionPointsId = fraudSuspicionPointsId;
	}

	public String getFraudRuleName() {
		return this.fraudRuleName;
	}

	public void setFraudRuleName(String fraudRuleName) {
		this.fraudRuleName = fraudRuleName;
	}

	public Integer getRiskPoints() {
		return this.riskPoints;
	}

	public void setRiskPoints(Integer riskPoints) {
		this.riskPoints = riskPoints;
	}

	public FrdFraudSuspicionCase getFrdFraudSuspicionCase() {
		return this.frdFraudSuspicionCase;
	}

	public void setFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase) {
		this.frdFraudSuspicionCase = frdFraudSuspicionCase;
	}
	
}