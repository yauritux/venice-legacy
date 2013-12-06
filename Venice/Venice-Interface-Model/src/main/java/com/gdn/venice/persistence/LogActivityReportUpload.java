package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the log_activity_report_upload database table.
 * 
 */
@Entity
@Table(name="log_activity_report_upload")
public class LogActivityReportUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_activity_report_upload")  
	@TableGenerator(name="log_activity_report_upload", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="activity_report_upload_id", unique=true, nullable=false)
	private Long activityReportUploadId;

	@Column(name="file_name_and_location", nullable=false, length=1000)
	private String fileNameAndLocation;

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

	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logActivityReportUpload")
	private List<LogActivityReconRecord> logActivityReconRecords;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-one association to LogReportStatus
    @ManyToOne
	@JoinColumn(name="report_status_id")
	private LogReportStatus logReportStatus;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="template_id", nullable=false)
	private LogReportTemplate logReportTemplate;

    public LogActivityReportUpload() {
    }

	public Long getActivityReportUploadId() {
		return this.activityReportUploadId;
	}

	public void setActivityReportUploadId(Long activityReportUploadId) {
		this.activityReportUploadId = activityReportUploadId;
	}

	public String getFileNameAndLocation() {
		return this.fileNameAndLocation;
	}

	public void setFileNameAndLocation(String fileNameAndLocation) {
		this.fileNameAndLocation = fileNameAndLocation;
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

	public List<LogActivityReconRecord> getLogActivityReconRecords() {
		return this.logActivityReconRecords;
	}

	public void setLogActivityReconRecords(List<LogActivityReconRecord> logActivityReconRecords) {
		this.logActivityReconRecords = logActivityReconRecords;
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
	
}