package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_report_status database table.
 * 
 */
@Entity
@Table(name="log_report_status")
public class LogReportStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_report_status")  
	@TableGenerator(name="log_report_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="report_status_id", unique=true, nullable=false)
	private Long reportStatusId;

	@Column(name="report_status_desc", nullable=false, length=100)
	private String reportStatusDesc;

	//bi-directional many-to-one association to LogActivityReportUpload
	@OneToMany(mappedBy="logReportStatus")
	private List<LogActivityReportUpload> logActivityReportUploads;

	//bi-directional many-to-one association to LogInvoiceReportUpload
	@OneToMany(mappedBy="logReportStatus")
	private List<LogInvoiceReportUpload> logInvoiceReportUploads;

    public LogReportStatus() {
    }

	public Long getReportStatusId() {
		return this.reportStatusId;
	}

	public void setReportStatusId(Long reportStatusId) {
		this.reportStatusId = reportStatusId;
	}

	public String getReportStatusDesc() {
		return this.reportStatusDesc;
	}

	public void setReportStatusDesc(String reportStatusDesc) {
		this.reportStatusDesc = reportStatusDesc;
	}

	public List<LogActivityReportUpload> getLogActivityReportUploads() {
		return this.logActivityReportUploads;
	}

	public void setLogActivityReportUploads(List<LogActivityReportUpload> logActivityReportUploads) {
		this.logActivityReportUploads = logActivityReportUploads;
	}
	
	public List<LogInvoiceReportUpload> getLogInvoiceReportUploads() {
		return this.logInvoiceReportUploads;
	}

	public void setLogInvoiceReportUploads(List<LogInvoiceReportUpload> logInvoiceReportUploads) {
		this.logInvoiceReportUploads = logInvoiceReportUploads;
	}
	
}