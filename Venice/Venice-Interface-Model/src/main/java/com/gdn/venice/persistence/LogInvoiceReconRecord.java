package com.gdn.venice.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the log_invoice_recon_record database table.
 * 
 */
@Entity
@Table(name="log_invoice_recon_record")
public class LogInvoiceReconRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_invoice_recon_record")  
	@TableGenerator(name="log_invoice_recon_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="invoice_recon_record_id", unique=true, nullable=false)
	private Long invoiceReconRecordId;

	@Column(length=1000)
	private String comment;

	@Column(name="manually_entered_data", length=200)
	private String manuallyEnteredData;

	@Column(name="provider_data", nullable=false, length=200)
	private String providerData;

	@Column(name="user_logon_name", length=100)
	private String userLogonName;

	@Column(name="venice_data", length=200)
	private String veniceData;
	
	//bi-directional many-to-one association to LogInvoiceReconCommentHistory
	@OneToMany(mappedBy="logInvoiceReconRecord")
	private List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistories;

	//bi-directional many-to-one association to LogActionApplied
    @ManyToOne
	@JoinColumn(name="action_applied_id")
	private LogActionApplied logActionApplied;

    //bi-directional many-to-one association to LogActionApplied
    @ManyToOne
	@JoinColumn(name="invoice_airwaybill_record_id")
	private LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord;
    
	//bi-directional many-to-one association to LogReconInvoiceRecordResult
    @ManyToOne
	@JoinColumn(name="recon_record_result_id", nullable=false)
	private LogReconInvoiceRecordResult logReconInvoiceRecordResult;

	//bi-directional many-to-one association to LogReconRecordStatus
    @ManyToOne
	@JoinColumn(name="record_status_id")
	private LogReconRecordStatus logReconRecordStatus;

    public LogInvoiceReconRecord() {
    }

	public Long getInvoiceReconRecordId() {
		return this.invoiceReconRecordId;
	}

	public void setInvoiceReconRecordId(Long invoiceReconRecordId) {
		this.invoiceReconRecordId = invoiceReconRecordId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getManuallyEnteredData() {
		return this.manuallyEnteredData;
	}

	public void setManuallyEnteredData(String manuallyEnteredData) {
		this.manuallyEnteredData = manuallyEnteredData;
	}

	public String getProviderData() {
		return this.providerData;
	}

	public void setProviderData(String providerData) {
		this.providerData = providerData;
	}

	public String getUserLogonName() {
		return this.userLogonName;
	}

	public void setUserLogonName(String userLogonName) {
		this.userLogonName = userLogonName;
	}

	public String getVeniceData() {
		return this.veniceData;
	}

	public void setVeniceData(String veniceData) {
		this.veniceData = veniceData;
	}

	public List<LogInvoiceReconCommentHistory> getLogInvoiceReconCommentHistories() {
		return this.logInvoiceReconCommentHistories;
	}

	public void setLogInvoiceReconCommentHistories(List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistories) {
		this.logInvoiceReconCommentHistories = logInvoiceReconCommentHistories;
	}
	
	public LogActionApplied getLogActionApplied() {
		return this.logActionApplied;
	}

	public void setLogActionApplied(LogActionApplied logActionApplied) {
		this.logActionApplied = logActionApplied;
	}
	
	public LogInvoiceAirwaybillRecord getLogInvoiceAirwaybillRecord() {
		return logInvoiceAirwaybillRecord;
	}

	public void setLogInvoiceAirwaybillRecord(
			LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord) {
		this.logInvoiceAirwaybillRecord = logInvoiceAirwaybillRecord;
	}

	public LogReconInvoiceRecordResult getLogReconInvoiceRecordResult() {
		return this.logReconInvoiceRecordResult;
	}

	public void setLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult) {
		this.logReconInvoiceRecordResult = logReconInvoiceRecordResult;
	}
	
	public LogReconRecordStatus getLogReconRecordStatus() {
		return this.logReconRecordStatus;
	}

	public void setLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus) {
		this.logReconRecordStatus = logReconRecordStatus;
	}
	
}