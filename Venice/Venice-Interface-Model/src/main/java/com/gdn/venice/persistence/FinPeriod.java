package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the fin_period database table.
 * 
 */
@Entity
@Table(name="fin_period")
public class FinPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_period")  
	@TableGenerator(name="fin_period", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="period_id", unique=true, nullable=false)
	private Long periodId;

	@Column(name="period_desc", nullable=false, length=100)
	private String periodDesc;
	
	@Column(name="from_datetime", nullable=false)
	private Timestamp fromDatetime;

	@Column(name="to_datetime", nullable=false)
	private Timestamp toDatetime;

	//bi-directional many-to-one association to FinApPayment
	@OneToMany(mappedBy="finPeriod")
	private List<FinApPayment> finApPayments;

	//bi-directional many-to-one association to FinArFundsInReport
	@OneToMany(mappedBy="finPeriod")
	private List<FinArFundsInReport> finArFundsInReports;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finPeriod")
	private List<FinJournalTransaction> finJournalTransactions;

	//bi-directional many-to-one association to FinRolledUpJournalHeader
	@OneToMany(mappedBy="finPeriod")
	private List<FinRolledUpJournalHeader> finRolledUpJournalHeaders;

    public FinPeriod() {
    }

	public Long getPeriodId() {
		return this.periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}
	
	public String getPeriodDesc() {
		return periodDesc;
	}

	public void setPeriodDesc(String periodDesc) {
		this.periodDesc = periodDesc;
	}

	public Timestamp getFromDatetime() {
		return this.fromDatetime;
	}

	public void setFromDatetime(Timestamp fromDatetime) {
		this.fromDatetime = fromDatetime;
	}

	public Timestamp getToDatetime() {
		return this.toDatetime;
	}

	public void setToDatetime(Timestamp toDatetime) {
		this.toDatetime = toDatetime;
	}

	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
	public List<FinArFundsInReport> getFinArFundsInReports() {
		return this.finArFundsInReports;
	}

	public void setFinArFundsInReports(List<FinArFundsInReport> finArFundsInReports) {
		this.finArFundsInReports = finArFundsInReports;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
	public List<FinRolledUpJournalHeader> getFinRolledUpJournalHeaders() {
		return this.finRolledUpJournalHeaders;
	}

	public void setFinRolledUpJournalHeaders(List<FinRolledUpJournalHeader> finRolledUpJournalHeaders) {
		this.finRolledUpJournalHeaders = finRolledUpJournalHeaders;
	}
	
}