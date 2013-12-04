package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_recon_invoice_record_result database table.
 * 
 */
@Entity
@Table(name="log_recon_invoice_record_result")
public class LogReconInvoiceRecordResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_recon_invoice_record_result")  
	@TableGenerator(name="log_recon_invoice_record_result", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="recon_record_result_id", unique=true, nullable=false)
	private Long reconRecordResultId;

	@Column(name="recon_record_result_desc", nullable=false, length=100)
	private String reconRecordResultDesc;

	//bi-directional many-to-one association to LogInvoiceReconRecord
	@OneToMany(mappedBy="logReconInvoiceRecordResult")
	private List<LogInvoiceReconRecord> logInvoiceReconRecords;

    public LogReconInvoiceRecordResult() {
    }

	public Long getReconRecordResultId() {
		return this.reconRecordResultId;
	}

	public void setReconRecordResultId(Long reconRecordResultId) {
		this.reconRecordResultId = reconRecordResultId;
	}

	public String getReconRecordResultDesc() {
		return this.reconRecordResultDesc;
	}

	public void setReconRecordResultDesc(String reconRecordResultDesc) {
		this.reconRecordResultDesc = reconRecordResultDesc;
	}

	public List<LogInvoiceReconRecord> getLogInvoiceReconRecords() {
		return this.logInvoiceReconRecords;
	}

	public void setLogInvoiceReconRecords(List<LogInvoiceReconRecord> logInvoiceReconRecords) {
		this.logInvoiceReconRecords = logInvoiceReconRecords;
	}
	
}