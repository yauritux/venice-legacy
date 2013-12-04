package com.gdn.venice.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the log_invoice_recon_record database table.
 * 
 */
@Entity
@Table(name="log_invoice_airwaybill_record")
public class LogInvoiceAirwaybillRecord implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_invoice_airwaybill_record")  
	@TableGenerator(name="log_invoice_airwaybill_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="invoice_airwaybill_record_id", unique=true, nullable=false)
	private Long invoiceAirwaybillRecordId;
	
	@Column(name="airway_bill_number", length=200, nullable=false)
	private String airwayBillNumber;
	
	@Column(name="venice_other_charge", precision=20, scale=2)
	private BigDecimal veniceOtherCharge;

	@Column(name="provider_other_charge", precision=20, scale=2)
	private BigDecimal providerOtherCharge;
	
	@Column(name="approved_other_charge", precision=20, scale=2)
	private BigDecimal approvedOtherCharge;

	@Column(name="venice_package_weight", precision=20, scale=2)
	private BigDecimal venicePackageWeight;

	@Column(name="provider_package_weight", precision=20, scale=2)
	private BigDecimal providerPackageWeight;

	@Column(name="approved_package_weight", precision=20, scale=2)
	private BigDecimal approvedPackageWeight;

	@Column(name="venice_price_per_kg", precision=20, scale=2)
	private BigDecimal venicePricePerKg;

	@Column(name="provider_price_per_kg", precision=20, scale=2)
	private BigDecimal providerPricePerKg;

	@Column(name="approved_price_per_kg", precision=20, scale=2)
	private BigDecimal approvedPricePerKg;

	@Column(name="venice_gift_wrap_charge", precision=20, scale=2)
	private BigDecimal veniceGiftWrapCharge;

	@Column(name="provider_gift_wrap_charge", precision=20, scale=2)
	private BigDecimal providerGiftWrapCharge;

	@Column(name="approved_gift_wrap_charge", precision=20, scale=2)
	private BigDecimal approvedGiftWrapCharge;

	@Column(name="venice_insurance_charge", precision=20, scale=2)
	private BigDecimal veniceInsuranceCharge;

	@Column(name="provider_insurance_charge", precision=20, scale=2)
	private BigDecimal providerInsuranceCharge;

	@Column(name="approved_insurance_charge", precision=20, scale=2)
	private BigDecimal approvedInsuranceCharge;
	
	@Column(name="provider_total_charge", precision=20, scale=2)
	private BigDecimal providerTotalCharge;
	
	@Column(name="venice_total_charge", precision=20, scale=2)
	private BigDecimal veniceTotalCharge;

	@Column(name="approved_total_charge", precision=20, scale=2)
	private BigDecimal approvedTotalCharge;

	@Column(name="invoice_result_status", length=100)
	private String invoiceResultStatus;
	
	//bi-directional many-to-one association to LogInvoiceReportUpload
    @ManyToOne
	@JoinColumn(name="invoice_report_upload_id", nullable=false)
	private LogInvoiceReportUpload logInvoiceReportUpload;
	
	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logInvoiceAirwaybillRecord")
	private List<LogInvoiceReconRecord> logInvoiceReconRecords;
	
//	bi-directional many-to-one association to LogAirwayBill
    @OneToMany(mappedBy="logInvoiceAirwaybillRecord")
	private List<LogAirwayBill> logAirwayBills;

	public Long getInvoiceAirwaybillRecordId() {
		return invoiceAirwaybillRecordId;
	}

	public void setInvoiceAirwaybillRecordId(Long invoiceAirwaybillRecordId) {
		this.invoiceAirwaybillRecordId = invoiceAirwaybillRecordId;
	}

	public String getAirwayBillNumber() {
		return airwayBillNumber;
	}

	public void setAirwayBillNumber(String airwayBillNumber) {
		this.airwayBillNumber = airwayBillNumber;
	}

	public BigDecimal getVeniceOtherCharge() {
		return veniceOtherCharge;
	}

	public void setVeniceOtherCharge(BigDecimal veniceOtherCharge) {
		this.veniceOtherCharge = veniceOtherCharge;
	}

	public BigDecimal getProviderOtherCharge() {
		return providerOtherCharge;
	}

	public void setProviderOtherCharge(BigDecimal providerOtherCharge) {
		this.providerOtherCharge = providerOtherCharge;
	}

	public BigDecimal getVenicePackageWeight() {
		return venicePackageWeight;
	}

	public void setVenicePackageWeight(BigDecimal venicePackageWeight) {
		this.venicePackageWeight = venicePackageWeight;
	}

	public BigDecimal getProviderPackageWeight() {
		return providerPackageWeight;
	}

	public void setProviderPackageWeight(BigDecimal providerPackageWeight) {
		this.providerPackageWeight = providerPackageWeight;
	}

	public BigDecimal getVenicePricePerKg() {
		return venicePricePerKg;
	}

	public void setVenicePricePerKg(BigDecimal venicePricePerKg) {
		this.venicePricePerKg = venicePricePerKg;
	}

	public BigDecimal getProviderPricePerKg() {
		return providerPricePerKg;
	}

	public void setProviderPricePerKg(BigDecimal providerPricePerKg) {
		this.providerPricePerKg = providerPricePerKg;
	}

	public BigDecimal getVeniceGiftWrapCharge() {
		return veniceGiftWrapCharge;
	}

	public void setVeniceGiftWrapCharge(BigDecimal veniceGiftWrapCharge) {
		this.veniceGiftWrapCharge = veniceGiftWrapCharge;
	}

	public BigDecimal getProviderGiftWrapCharge() {
		return providerGiftWrapCharge;
	}

	public void setProviderGiftWrapCharge(BigDecimal providerGiftWrapCharge) {
		this.providerGiftWrapCharge = providerGiftWrapCharge;
	}

	public BigDecimal getVeniceInsuranceCharge() {
		return veniceInsuranceCharge;
	}

	public void setVeniceInsuranceCharge(BigDecimal veniceInsuranceCharge) {
		this.veniceInsuranceCharge = veniceInsuranceCharge;
	}

	public BigDecimal getProviderInsuranceCharge() {
		return providerInsuranceCharge;
	}

	public void setProviderInsuranceCharge(BigDecimal providerInsuranceCharge) {
		this.providerInsuranceCharge = providerInsuranceCharge;
	}

	public BigDecimal getProviderTotalCharge() {
		return providerTotalCharge;
	}

	public void setProviderTotalCharge(BigDecimal providerTotalCharge) {
		this.providerTotalCharge = providerTotalCharge;
	}

	public BigDecimal getVeniceTotalCharge() {
		return veniceTotalCharge;
	}

	public void setVeniceTotalCharge(BigDecimal veniceTotalCharge) {
		this.veniceTotalCharge = veniceTotalCharge;
	}

	public BigDecimal getApprovedOtherCharge() {
		return approvedOtherCharge;
	}

	public void setApprovedOtherCharge(BigDecimal approvedOtherCharge) {
		this.approvedOtherCharge = approvedOtherCharge;
	}

	public BigDecimal getApprovedPackageWeight() {
		return approvedPackageWeight;
	}

	public void setApprovedPackageWeight(BigDecimal approvedPackageWeight) {
		this.approvedPackageWeight = approvedPackageWeight;
	}

	public BigDecimal getApprovedPricePerKg() {
		return approvedPricePerKg;
	}

	public void setApprovedPricePerKg(BigDecimal approvedPricePerKg) {
		this.approvedPricePerKg = approvedPricePerKg;
	}

	public BigDecimal getApprovedGiftWrapCharge() {
		return approvedGiftWrapCharge;
	}

	public void setApprovedGiftWrapCharge(BigDecimal approvedGiftWrapCharge) {
		this.approvedGiftWrapCharge = approvedGiftWrapCharge;
	}

	public BigDecimal getApprovedInsuranceCharge() {
		return approvedInsuranceCharge;
	}

	public void setApprovedInsuranceCharge(BigDecimal approvedInsuranceCharge) {
		this.approvedInsuranceCharge = approvedInsuranceCharge;
	}

	public BigDecimal getApprovedTotalCharge() {
		return approvedTotalCharge;
	}

	public void setApprovedTotalCharge(BigDecimal approvedTotalCharge) {
		this.approvedTotalCharge = approvedTotalCharge;
	}

	public String getInvoiceResultStatus() {
		return invoiceResultStatus;
	}

	public void setInvoiceResultStatus(String invoiceResultStatus) {
		this.invoiceResultStatus = invoiceResultStatus;
	}

	public LogInvoiceReportUpload getLogInvoiceReportUpload() {
		return logInvoiceReportUpload;
	}

	public void setLogInvoiceReportUpload(
			LogInvoiceReportUpload logInvoiceReportUpload) {
		this.logInvoiceReportUpload = logInvoiceReportUpload;
	}

	public List<LogInvoiceReconRecord> getLogInvoiceReconRecords() {
		return logInvoiceReconRecords;
	}

	public void setLogInvoiceReconRecords(
			List<LogInvoiceReconRecord> logInvoiceReconRecords) {
		this.logInvoiceReconRecords = logInvoiceReconRecords;
	}

	public List<LogAirwayBill> getLogAirwayBills() {
		return logAirwayBills;
	}

	public void setLogAirwayBills(List<LogAirwayBill> logAirwayBills) {
		this.logAirwayBills = logAirwayBills;
	}
}
