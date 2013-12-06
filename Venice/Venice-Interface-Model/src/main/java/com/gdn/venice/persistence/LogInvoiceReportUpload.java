package com.gdn.venice.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The persistent class for the log_invoice_report_upload database table.
 * 
 */
@Entity
@Table(name="log_invoice_report_upload")
public class LogInvoiceReportUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_invoice_report_upload")  
	@TableGenerator(name="log_invoice_report_upload", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="invoice_report_upload_id", unique=true, nullable=false)
	private Long invoiceReportUploadId;

	@Column(name="file_name_and_location", nullable=false, length=1000)
	private String fileNameAndLocation;

	@Column(name="invoice_number", length=100)
	private String invoiceNumber;

	@Column(name="number_of_records", nullable=false)
	private Long numberOfRecords;

	@Column(name="report_desc", nullable=false, length=100)
	private String reportDesc;

	@Column(name="report_reconciliation_timestamp")
	private Timestamp reportReconciliationTimestamp;

	@Column(name="report_timestamp", nullable=false)
	private Timestamp reportTimestamp;

	@Column(name="user_logon_name", length=100)
	private String userLogonName;

	@Column(name="invoice_recon_tolerance", length=1000)
	private String invoiceReconTolerance;

	@Column(name="approved_by", length=100)
	private String approvedBy;
	
	//bi-directional many-to-one association to LogApprovalStatus
    @ManyToOne
	@JoinColumn(name="invoice_approval_status_id")
	private LogApprovalStatus logApprovalStatus;
    
	//bi-directional many-to-one association to LogInvoiceReconRecord
	@OneToMany(mappedBy="logInvoiceReportUpload")
	private List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecords;

	//bi-directional many-to-one association to FinApInvoice
    @ManyToOne
	@JoinColumn(name="ap_invoice_id")
	private FinApInvoice finApInvoice;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-one association to LogReportStatus
    @ManyToOne
	@JoinColumn(name="report_status_id", nullable=false)
	private LogReportStatus logReportStatus;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="template_id", nullable=false)
	private LogReportTemplate logReportTemplate;

    public LogInvoiceReportUpload() {
    }

	public Long getInvoiceReportUploadId() {
		return this.invoiceReportUploadId;
	}

	public void setInvoiceReportUploadId(Long invoiceReportUploadId) {
		this.invoiceReportUploadId = invoiceReportUploadId;
	}

	public String getFileNameAndLocation() {
		return this.fileNameAndLocation;
	}

	public void setFileNameAndLocation(String fileNameAndLocation) {
		this.fileNameAndLocation = fileNameAndLocation;
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getNumberOfRecords() {
		return this.numberOfRecords;
	}

	public void setNumberOfRecords(Long numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public String getReportDesc() {
		return this.reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	public Timestamp getReportReconciliationTimestamp() {
		return this.reportReconciliationTimestamp;
	}

	public void setReportReconciliationTimestamp(Timestamp reportReconciliationTimestamp) {
		this.reportReconciliationTimestamp = reportReconciliationTimestamp;
	}

	public Timestamp getReportTimestamp() {
		return this.reportTimestamp;
	}

	public void setReportTimestamp(Timestamp reportTimestamp) {
		this.reportTimestamp = reportTimestamp;
	}

	public String getUserLogonName() {
		return this.userLogonName;
	}

	public void setUserLogonName(String userLogonName) {
		this.userLogonName = userLogonName;
	}
	
	public FinApInvoice getFinApInvoice() {
		return this.finApInvoice;
	}

	public void setFinApInvoice(FinApInvoice finApInvoice) {
		this.finApInvoice = finApInvoice;
	}
	
	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
	public LogReportStatus getLogReportStatus() {
		return this.logReportStatus;
	}

	public void setLogReportStatus(LogReportStatus logReportStatus) {
		this.logReportStatus = logReportStatus;
	}
	
	public LogReportTemplate getLogReportTemplate() {
		return this.logReportTemplate;
	}

	public void setLogReportTemplate(LogReportTemplate logReportTemplate) {
		this.logReportTemplate = logReportTemplate;
	}

	public String getInvoiceReconTolerance() {
		return invoiceReconTolerance;
	}

	public void setInvoiceReconTolerance(String invoiceReconTolerance) {
		this.invoiceReconTolerance = invoiceReconTolerance;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LogApprovalStatus getLogApprovalStatus() {
		return logApprovalStatus;
	}

	public void setLogApprovalStatus(LogApprovalStatus logApprovalStatus) {
		this.logApprovalStatus = logApprovalStatus;
	}

	public List<LogInvoiceAirwaybillRecord> getLogInvoiceAirwaybillRecords() {
		return logInvoiceAirwaybillRecords;
	}

	public void setLogInvoiceAirwaybillRecords(
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecords) {
		this.logInvoiceAirwaybillRecords = logInvoiceAirwaybillRecords;
	}
	
}