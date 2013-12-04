package com.gdn.venice.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the fin_ar_funds_id_report_time database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_id_report_time")
public class FinArFundsIdReportTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_id_report_time")  
	@TableGenerator(name="fin_ar_funds_id_report_time", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="report_time_id", unique=true, nullable=false)
	private Long reportTimeId;

	@Column(name="report_time_desc",length=20)
	private String reportTimeDesc;	
	
	@OneToMany(mappedBy="finArFundsIdReportTime")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

    public FinArFundsIdReportTime() {
    }

	public Long getReportTimeId() {
		return this.reportTimeId;
	}

	public void setReportTimeId(Long reportTimeId) {
		this.reportTimeId = reportTimeId;
	}

	public String getReportTimeDesc() {
		return this.reportTimeDesc;
	}

	public void setReportTimeDesc(String reportTimeDesc) {
		this.reportTimeDesc = reportTimeDesc;
	}
	
	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(
			List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}

}