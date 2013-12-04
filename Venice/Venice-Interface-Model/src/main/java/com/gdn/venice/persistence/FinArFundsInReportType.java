package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_ar_funds_in_report_type database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_report_type")
public class FinArFundsInReportType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_report_type")  
	@TableGenerator(name="fin_ar_funds_in_report_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="payment_report_type_id", unique=true, nullable=false)
	private Long paymentReportTypeId;

	@Column(name="payment_report_type_desc", nullable=false, length=100)
	private String paymentReportTypeDesc;

	//bi-directional many-to-one association to FinArFundsInReport
	@OneToMany(mappedBy="finArFundsInReportType")
	private List<FinArFundsInReport> finArFundsInReports;

	//bi-directional many-to-one association to VenBank
    @ManyToOne
	@JoinColumn(name="bank_id", nullable=false)
	private VenBank venBank;

    public FinArFundsInReportType() {
    }

	public Long getPaymentReportTypeId() {
		return this.paymentReportTypeId;
	}

	public void setPaymentReportTypeId(Long paymentReportTypeId) {
		this.paymentReportTypeId = paymentReportTypeId;
	}

	public String getPaymentReportTypeDesc() {
		return this.paymentReportTypeDesc;
	}

	public void setPaymentReportTypeDesc(String paymentReportTypeDesc) {
		this.paymentReportTypeDesc = paymentReportTypeDesc;
	}

	public List<FinArFundsInReport> getFinArFundsInReports() {
		return this.finArFundsInReports;
	}

	public void setFinArFundsInReports(List<FinArFundsInReport> finArFundsInReports) {
		this.finArFundsInReports = finArFundsInReports;
	}
	
	public VenBank getVenBank() {
		return this.venBank;
	}

	public void setVenBank(VenBank venBank) {
		this.venBank = venBank;
	}
	
}