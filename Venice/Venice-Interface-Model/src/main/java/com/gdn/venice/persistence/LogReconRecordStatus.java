package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_recon_record_status database table.
 * 
 */
@Entity
@Table(name="log_recon_record_status")
public class LogReconRecordStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_recon_record_status")  
	@TableGenerator(name="log_recon_record_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="record_status_id", unique=true, nullable=false)
	private Long recordStatusId;

	@Column(name="record_status_desc", nullable=false, length=100)
	private String recordStatusDesc;

	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logReconRecordStatus")
	private List<LogActivityReconRecord> logActivityReconRecords;

	//bi-directional many-to-one association to LogInvoiceReconRecord
	@OneToMany(mappedBy="logReconRecordStatus")
	private List<LogInvoiceReconRecord> logInvoiceReconRecords;

    public LogReconRecordStatus() {
    }

	public Long getRecordStatusId() {
		return this.recordStatusId;
	}

	public void setRecordStatusId(Long recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getRecordStatusDesc() {
		return this.recordStatusDesc;
	}

	public void setRecordStatusDesc(String recordStatusDesc) {
		this.recordStatusDesc = recordStatusDesc;
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