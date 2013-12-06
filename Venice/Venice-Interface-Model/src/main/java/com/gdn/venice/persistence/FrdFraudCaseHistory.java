package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_fraud_case_history database table.
 * 
 */
@Entity
@Table(name="frd_fraud_case_history")
public class FrdFraudCaseHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FrdFraudCaseHistoryPK id;

	@Column(name="fraud_case_history_notes", nullable=false, length=1000)
	private String fraudCaseHistoryNotes;

	//bi-directional many-to-one association to FrdFraudCaseStatus
    @ManyToOne
	@JoinColumn(name="fraud_case_status_id", nullable=false)
	private FrdFraudCaseStatus frdFraudCaseStatus;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
    @ManyToOne
	@JoinColumn(name="fraud_suspicion_case_id", nullable=false, insertable=false, updatable=false)
	private FrdFraudSuspicionCase frdFraudSuspicionCase;

    public FrdFraudCaseHistory() {
    }

	public FrdFraudCaseHistoryPK getId() {
		return this.id;
	}

	public void setId(FrdFraudCaseHistoryPK id) {
		this.id = id;
	}
	
	public String getFraudCaseHistoryNotes() {
		return this.fraudCaseHistoryNotes;
	}

	public void setFraudCaseHistoryNotes(String fraudCaseHistoryNotes) {
		this.fraudCaseHistoryNotes = fraudCaseHistoryNotes;
	}

	public FrdFraudCaseStatus getFrdFraudCaseStatus() {
		return this.frdFraudCaseStatus;
	}

	public void setFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus) {
		this.frdFraudCaseStatus = frdFraudCaseStatus;
	}
	
	public FrdFraudSuspicionCase getFrdFraudSuspicionCase() {
		return this.frdFraudSuspicionCase;
	}

	public void setFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase) {
		this.frdFraudSuspicionCase = frdFraudSuspicionCase;
	}
	
}