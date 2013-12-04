package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the fin_ar_funds_in_report database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_report")
public class FinArFundsInReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_report")  
	@TableGenerator(name="fin_ar_funds_in_report", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_report_id", unique=true, nullable=false)
	private Long paymentReportId;

	@Column(name="file_name_and_location", nullable=false, length=1000)
	private String fileNameAndLocation;

	@Column(name="report_desc", nullable=false, length=1000)
	private String reportDesc;

	@Column(name="report_timestamp", nullable=false)
	private Timestamp reportTimestamp;

	@Column(name="user_logon_name", length=100)
	private String userLogonName;

	//bi-directional many-to-one association to FinArFundsInReconRecord
	@OneToMany(mappedBy="finArFundsInReport")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

	//bi-directional many-to-one association to FinArFundsInReportType
    @ManyToOne
	@JoinColumn(name="payment_report_type_id", nullable=false)
	private FinArFundsInReportType finArFundsInReportType;

	//bi-directional many-to-one association to FinPeriod
    @ManyToOne
	@JoinColumn(name="period_id", nullable=false)
	private FinPeriod finPeriod;

    public FinArFundsInReport() {
    }

	public Long getPaymentReportId() {
		return this.paymentReportId;
	}

	public void setPaymentReportId(Long paymentReportId) {
		this.paymentReportId = paymentReportId;
	}

	public String getFileNameAndLocation() {
		return this.fileNameAndLocation;
	}

	public void setFileNameAndLocation(String fileNameAndLocation) {
		this.fileNameAndLocation = fileNameAndLocation;
	}

	public String getReportDesc() {
		return this.reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
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

	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
	public FinArFundsInReportType getFinArFundsInReportType() {
		return this.finArFundsInReportType;
	}

	public void setFinArFundsInReportType(FinArFundsInReportType finArFundsInReportType) {
		this.finArFundsInReportType = finArFundsInReportType;
	}
	
	public FinPeriod getFinPeriod() {
		return this.finPeriod;
	}

	public void setFinPeriod(FinPeriod finPeriod) {
		this.finPeriod = finPeriod;
	}
	
}