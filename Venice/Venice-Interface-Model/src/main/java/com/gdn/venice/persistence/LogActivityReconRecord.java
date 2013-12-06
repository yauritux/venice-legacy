package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_activity_recon_record database table.
 * 
 */
@Entity
@Table(name="log_activity_recon_record")
public class LogActivityReconRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_activity_recon_record")  
	@TableGenerator(name="log_activity_recon_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="activity_recon_record_id", unique=true, nullable=false)
	private Long activityReconRecordId;

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

	//bi-directional many-to-one association to LogActivityReconCommentHistory
	@OneToMany(mappedBy="logActivityReconRecord")
	private List<LogActivityReconCommentHistory> logActivityReconCommentHistories;

	//bi-directional many-to-one association to LogActionApplied
    @ManyToOne
	@JoinColumn(name="action_applied_id")
	private LogActionApplied logActionApplied;

	//bi-directional many-to-one association to LogActivityReportUpload
    @ManyToOne
	@JoinColumn(name="activity_report_upload_id", nullable=false)
	private LogActivityReportUpload logActivityReportUpload;

	//bi-directional many-to-one association to LogAirwayBill
    @ManyToOne
	@JoinColumn(name="airway_bill_id")
	private LogAirwayBill logAirwayBill;

	//bi-directional many-to-one association to LogReconActivityRecordResult
    @ManyToOne
	@JoinColumn(name="recon_record_result_id", nullable=false)
	private LogReconActivityRecordResult logReconActivityRecordResult;

	//bi-directional many-to-one association to LogReconRecordStatus
    @ManyToOne
	@JoinColumn(name="record_status_id")
	private LogReconRecordStatus logReconRecordStatus;

    public LogActivityReconRecord() {
    }

	public Long getActivityReconRecordId() {
		return this.activityReconRecordId;
	}

	public void setActivityReconRecordId(Long activityReconRecordId) {
		this.activityReconRecordId = activityReconRecordId;
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

	public List<LogActivityReconCommentHistory> getLogActivityReconCommentHistories() {
		return this.logActivityReconCommentHistories;
	}

	public void setLogActivityReconCommentHistories(List<LogActivityReconCommentHistory> logActivityReconCommentHistories) {
		this.logActivityReconCommentHistories = logActivityReconCommentHistories;
	}
	
	public LogActionApplied getLogActionApplied() {
		return this.logActionApplied;
	}

	public void setLogActionApplied(LogActionApplied logActionApplied) {
		this.logActionApplied = logActionApplied;
	}
	
	public LogActivityReportUpload getLogActivityReportUpload() {
		return this.logActivityReportUpload;
	}

	public void setLogActivityReportUpload(LogActivityReportUpload logActivityReportUpload) {
		this.logActivityReportUpload = logActivityReportUpload;
	}
	
	public LogAirwayBill getLogAirwayBill() {
		return this.logAirwayBill;
	}

	public void setLogAirwayBill(LogAirwayBill logAirwayBill) {
		this.logAirwayBill = logAirwayBill;
	}
	
	public LogReconActivityRecordResult getLogReconActivityRecordResult() {
		return this.logReconActivityRecordResult;
	}

	public void setLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult) {
		this.logReconActivityRecordResult = logReconActivityRecordResult;
	}
	
	public LogReconRecordStatus getLogReconRecordStatus() {
		return this.logReconRecordStatus;
	}

	public void setLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus) {
		this.logReconRecordStatus = logReconRecordStatus;
	}
	
}