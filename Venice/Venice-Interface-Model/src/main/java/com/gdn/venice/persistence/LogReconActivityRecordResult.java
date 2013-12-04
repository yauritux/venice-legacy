package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_recon_activity_record_result database table.
 * 
 */
@Entity
@Table(name="log_recon_activity_record_result")
public class LogReconActivityRecordResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_recon_activity_record_result")  
	@TableGenerator(name="log_recon_activity_record_result", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="recon_record_result_id", unique=true, nullable=false)
	private Long reconRecordResultId;

	@Column(name="recon_record_result_desc", nullable=false, length=100)
	private String reconRecordResultDesc;

	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logReconActivityRecordResult")
	private List<LogActivityReconRecord> logActivityReconRecords;

    public LogReconActivityRecordResult() {
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

	public List<LogActivityReconRecord> getLogActivityReconRecords() {
		return this.logActivityReconRecords;
	}

	public void setLogActivityReconRecords(List<LogActivityReconRecord> logActivityReconRecords) {
		this.logActivityReconRecords = logActivityReconRecords;
	}
	
}