package com.gdn.venice.persistence;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the log_file_upload_log database table.
 * 
 */
@Entity
@Table(name="log_invoice_upload_log")
public class LogInvoiceUploadLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_invoice_upload_log")  
	@TableGenerator(name="log_invoice_upload_log", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="invoice_upload_log_id", unique=true, nullable=false)
	private Long invoiceUploadLogId;

	@Column(name="actual_file_upload_name", length=100)
	private String actualFileUploadName;

	@Column(name="failed_file_upload_name", length=100)
	private String failedFileUploadName;

	@Column(name="failed_file_upload_name_and_loc", length=1000)
	private String failedFileUploadNameAndLoc;

	@Column(name="file_upload_format", length=10)
	private String fileUploadFormat;

	@Column(name="invoice_number", length=100)
	private String invoiceNumber;

	@Column(name="file_upload_name", length=100)
	private String fileUploadName;
	
	@Column(name="file_upload_name_and_loc", length=1000)
	private String fileUploadNameAndLoc;

	@Column(name="upload_status", length=10)
	private String uploadStatus;

	@Column(name="upload_timestamp")
	private Timestamp uploadTimestamp;

	@Column(name="uploaded_by", length=20)
	private String uploadedBy;

    public void setInvoiceUploadLogId(Long invoiceUploadLogId) {
		this.invoiceUploadLogId = invoiceUploadLogId;
	}

	public Long getInvoiceUploadLogId() {
		return invoiceUploadLogId;
	}

	public LogInvoiceUploadLog() {
    }

	public String getActualFileUploadName() {
		return this.actualFileUploadName;
	}

	public void setActualFileUploadName(String actualFileUploadName) {
		this.actualFileUploadName = actualFileUploadName;
	}

	public String getFailedFileUploadName() {
		return this.failedFileUploadName;
	}

	public void setFailedFileUploadName(String failedFileUploadName) {
		this.failedFileUploadName = failedFileUploadName;
	}

	public String getFailedFileUploadNameAndLoc() {
		return this.failedFileUploadNameAndLoc;
	}

	public void setFailedFileUploadNameAndLoc(String failedFileUploadNameAndLoc) {
		this.failedFileUploadNameAndLoc = failedFileUploadNameAndLoc;
	}

	public String getFileUploadFormat() {
		return this.fileUploadFormat;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setFileUploadFormat(String fileUploadFormat) {
		this.fileUploadFormat = fileUploadFormat;
	}

	public String getFileUploadName() {
		return this.fileUploadName;
	}

	public void setFileUploadName(String fileUploadName) {
		this.fileUploadName = fileUploadName;
	}

	public String getFileUploadNameAndLoc() {
		return this.fileUploadNameAndLoc;
	}

	public void setFileUploadNameAndLoc(String fileUploadNameAndLoc) {
		this.fileUploadNameAndLoc = fileUploadNameAndLoc;
	}

	public String getUploadStatus() {
		return this.uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public Timestamp getUploadTimestamp() {
		return this.uploadTimestamp;
	}

	public void setUploadTimestamp(Timestamp uploadTimestamp) {
		this.uploadTimestamp = uploadTimestamp;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

}