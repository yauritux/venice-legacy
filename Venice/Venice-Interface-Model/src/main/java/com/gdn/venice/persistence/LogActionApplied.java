package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_action_applied database table.
 * 
 */
@Entity
@Table(name="log_action_applied")
public class LogActionApplied implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_action_applied")  
	@TableGenerator(name="log_action_applied", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="action_applied_id", unique=true, nullable=false)
	private Long actionAppliedId;

	@Column(name="action_applied_desc", nullable=false, length=100)
	private String actionAppliedDesc;

	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logActionApplied")
	private List<LogActivityReconRecord> logActivityReconRecords;

	//bi-directional many-to-one association to LogInvoiceReconRecord
	@OneToMany(mappedBy="logActionApplied")
	private List<LogInvoiceReconRecord> logInvoiceReconRecords;

    public LogActionApplied() {
    }

	public Long getActionAppliedId() {
		return this.actionAppliedId;
	}

	public void setActionAppliedId(Long actionAppliedId) {
		this.actionAppliedId = actionAppliedId;
	}

	public String getActionAppliedDesc() {
		return this.actionAppliedDesc;
	}

	public void setActionAppliedDesc(String actionAppliedDesc) {
		this.actionAppliedDesc = actionAppliedDesc;
	}

	public List<LogActivityReconRecord> getLogActivityReconRecords() {
		return this.logActivityReconRecords;
	}

	public void setLogActivityReconRecords(List<LogActivityReconRecord> logActivityReconRecords) {
		this.logActivityReconRecords = logActivityReconRecords;
	}
	
	public List<LogInvoiceReconRecord> getLogInvoiceReconRecords() {
		return this.logInvoiceReconRecords;
	}

	public void setLogInvoiceReconRecords(List<LogInvoiceReconRecord> logInvoiceReconRecords) {
		this.logInvoiceReconRecords = logInvoiceReconRecords;
	}
	
}