package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the frd_fraud_action_log database table.
 * 
 */
@Entity
@Table(name="frd_fraud_action_log")
public class FrdFraudActionLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_fraud_action_log")  
	@TableGenerator(name="frd_fraud_action_log", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_action_id", unique=true, nullable=false)
	private Long fraudActionId;

	@Column(name="created_by", nullable=false, length=100)
	private String createdBy;

	@Column(name="created_date", nullable=false)
	private Timestamp createdDate;

	@Column(name="fraud_action_log_date", nullable=false)
	private Timestamp fraudActionLogDate;

	@Column(name="fraud_action_log_notes", nullable=false, length=1000)
	private String fraudActionLogNotes;

	@Column(name="fraud_action_log_type", length=100)
	private String fraudActionLogType;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="modified_by", nullable=false, length=100)
	private String modifiedBy;

	@Column(name="modified_date", nullable=false)
	private Timestamp modifiedDate;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
    @ManyToOne
	@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
	private FrdFraudSuspicionCase frdFraudSuspicionCase;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

    public FrdFraudActionLog() {
    }

	public Long getFraudActionId() {
		return this.fraudActionId;
	}

	public void setFraudActionId(Long fraudActionId) {
		this.fraudActionId = fraudActionId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getFraudActionLogDate() {
		return this.fraudActionLogDate;
	}

	public void setFraudActionLogDate(Timestamp fraudActionLogDate) {
		this.fraudActionLogDate = fraudActionLogDate;
	}

	public String getFraudActionLogNotes() {
		return this.fraudActionLogNotes;
	}

	public void setFraudActionLogNotes(String fraudActionLogNotes) {
		this.fraudActionLogNotes = fraudActionLogNotes;
	}

	public String getFraudActionLogType() {
		return this.fraudActionLogType;
	}

	public void setFraudActionLogType(String fraudActionLogType) {
		this.fraudActionLogType = fraudActionLogType;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public FrdFraudSuspicionCase getFrdFraudSuspicionCase() {
		return this.frdFraudSuspicionCase;
	}

	public void setFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase) {
		this.frdFraudSuspicionCase = frdFraudSuspicionCase;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}