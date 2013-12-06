package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the frd_fraud_case_status database table.
 * 
 */
@Entity
@Table(name="frd_fraud_case_status")
public class FrdFraudCaseStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_fraud_case_status")  
	@TableGenerator(name="frd_fraud_case_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_case_status_id", unique=true, nullable=false)
	private Long fraudCaseStatusId;

	@Column(name="fraud_case_status_desc", nullable=false, length=100)
	private String fraudCaseStatusDesc;

	//bi-directional many-to-one association to FrdFraudCaseHistory
	@OneToMany(mappedBy="frdFraudCaseStatus")
	private List<FrdFraudCaseHistory> frdFraudCaseHistories;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
	@OneToMany(mappedBy="frdFraudCaseStatus")
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases;

    public FrdFraudCaseStatus() {
    }

	public Long getFraudCaseStatusId() {
		return this.fraudCaseStatusId;
	}

	public void setFraudCaseStatusId(Long fraudCaseStatusId) {
		this.fraudCaseStatusId = fraudCaseStatusId;
	}

	public String getFraudCaseStatusDesc() {
		return this.fraudCaseStatusDesc;
	}

	public void setFraudCaseStatusDesc(String fraudCaseStatusDesc) {
		this.fraudCaseStatusDesc = fraudCaseStatusDesc;
	}

	public List<FrdFraudCaseHistory> getFrdFraudCaseHistories() {
		return this.frdFraudCaseHistories;
	}

	public void setFrdFraudCaseHistories(List<FrdFraudCaseHistory> frdFraudCaseHistories) {
		this.frdFraudCaseHistories = frdFraudCaseHistories;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases() {
		return this.frdFraudSuspicionCases;
	}

	public void setFrdFraudSuspicionCases(List<FrdFraudSuspicionCase> frdFraudSuspicionCases) {
		this.frdFraudSuspicionCases = frdFraudSuspicionCases;
	}
	
}