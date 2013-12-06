package com.gdn.venice.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the log_airway_bill database table.
 * 
 */
@Entity
@Table(name="log_airway_bill")
public class LogAirwayBill implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_airway_bill")  
	@TableGenerator(name="log_airway_bill", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="airway_bill_id", unique=true, nullable=false)
	private Long airwayBillId;

	@Column(name="activity_approved_by_user_id", length=100)
	private String activityApprovedByUserId;

	@Column(name="activity_file_name_and_loc", length=1000)
	private String activityFileNameAndLoc;

	@Column(name="activity_result_status", length=100)
	private String activityResultStatus;

    @Temporal( TemporalType.DATE)
	@Column(name="actual_pickup_date")
	private Date actualPickupDate;

	@Column(length=300)
	private String address;

	@Column(name="airway_bill_number", length=200)
	private String airwayBillNumber;

	@Column(name="airway_bill_pickup_date_time")
	private Timestamp airwayBillPickupDateTime;

	@Column(name="airway_bill_timestamp")
	private Timestamp airwayBillTimestamp;

	@Column(length=100)
	private String consignee;

	@Column(name="contact_person", length=100)
	private String contactPerson;

	@Column(length=1000)
	private String content;

    @Temporal( TemporalType.DATE)
	@Column(name="date_of_return")
	private Date dateOfReturn;

	@Column(name="delivery_order", length=100)
	private String deliveryOrder;

	@Column(name="dest_code", length=100)
	private String destCode;

	@Column(length=100)
	private String destination;

	@Column(name="gdn_reference", length=100)
	private String gdnReference;

	@Column(name="gift_wrap_charge", precision=20, scale=2)
	private BigDecimal giftWrapCharge;

	@Column(name="insurance_charge", precision=20, scale=2)
	private BigDecimal insuranceCharge;
	
	@Column(name="insured_amount", precision=20, scale=2)
	private BigDecimal insuredAmount;

	@Column(name="invoice_approved_by_user_id", length=100)
	private String invoiceApprovedByUserId;

	@Column(name="invoice_file_name_and_loc", length=1000)
	private String invoiceFileNameAndLoc;

	@Column(name="invoice_result_status", length=100)
	private String invoiceResultStatus;

	@Column(name="mta_data", nullable=false)
	private Boolean mtaData;

	@Column(name="note_return", length=1000)
	private String noteReturn;

	@Column(name="note_undelivered", length=1000)
	private String noteUndelivered;

	@Column(name="num_packages")
	private Integer numPackages;

	@Column(length=100)
	private String origin;

	@Column(name="other_charge", precision=20, scale=2)
	private BigDecimal otherCharge;

	@Column(name="package_weight", precision=20, scale=2)
	private BigDecimal packageWeight;

	@Column(name="price_per_kg", precision=20, scale=2)
	private BigDecimal pricePerKg;

	@Column(name="provider_total_charge", precision=20, scale=2)
	private BigDecimal providerTotalCharge;

    @Temporal( TemporalType.DATE)
	private Date received;

	@Column(length=100)
	private String recipient;

	@Column(length=100)
	private String relation;

	@Column(name="return", length=100)
	private String return_;

	@Column(length=100)
	private String service;

	@Column(length=200)
	private String shipper;

	@Column(length=100)
	private String status;

	@Column(length=100)
	private String tariff;

	@Column(name="total_charge", precision=20, scale=2)
	private BigDecimal totalCharge;

	@Column(name="tracking_number", length=100)
	private String trackingNumber;

	@Column(length=100)
	private String type;

    @Temporal( TemporalType.DATE)
	private Date undelivered;

	@Column(length=100)
	private String zip;
	
	@Column(name="sequence")
	private Integer sequence;
	
	@Column(name="kpi_pickup_perf_clocked")
	private Boolean kpiPickupPerfClocked;
	
	@Column(name="kpi_delivery_perf_clocked")
	private Boolean kpiDeliveryPerfClocked;
	
	@Column(name="kpi_invoice_accuracy_clocked")
	private Boolean kpiInvoiceAccuracyClocked;
	
	//bi-directional many-to-one association to LogActivityReconRecord
	@OneToMany(mappedBy="logAirwayBill")
	private List<LogActivityReconRecord> logActivityReconRecords;

	//bi-directional many-to-one association to LogApprovalStatus
    @ManyToOne
	@JoinColumn(name="invoice_approval_status_id")
	private LogApprovalStatus logApprovalStatus1;

	//bi-directional many-to-one association to LogApprovalStatus
    @ManyToOne
	@JoinColumn(name="activity_approval_status_id")
	private LogApprovalStatus logApprovalStatus2;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-one association to VenDistributionCart
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="distribution_cart_id")
	private VenDistributionCart venDistributionCart;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id")
	private VenOrderItem venOrderItem;

    //bi-directional many-to-one association to LogInvoiceAirwaybillRecord
    @ManyToOne
	@JoinColumn(name="invoice_airwaybill_record_id")
	private LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord;

    public LogAirwayBill() {
    }

	public Long getAirwayBillId() {
		return this.airwayBillId;
	}

	public void setAirwayBillId(Long airwayBillId) {
		this.airwayBillId = airwayBillId;
	}

	public String getActivityApprovedByUserId() {
		return this.activityApprovedByUserId;
	}

	public void setActivityApprovedByUserId(String activityApprovedByUserId) {
		this.activityApprovedByUserId = activityApprovedByUserId;
	}

	public String getActivityFileNameAndLoc() {
		return this.activityFileNameAndLoc;
	}

	public void setActivityFileNameAndLoc(String activityFileNameAndLoc) {
		this.activityFileNameAndLoc = activityFileNameAndLoc;
	}

	public String getActivityResultStatus() {
		return this.activityResultStatus;
	}

	public void setActivityResultStatus(String activityResultStatus) {
		this.activityResultStatus = activityResultStatus;
	}

	public Date getActualPickupDate() {
		return this.actualPickupDate;
	}

	public void setActualPickupDate(Date actualPickupDate) {
		this.actualPickupDate = actualPickupDate;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAirwayBillNumber() {
		return this.airwayBillNumber;
	}

	public void setAirwayBillNumber(String airwayBillNumber) {
		this.airwayBillNumber = airwayBillNumber;
	}

	public Timestamp getAirwayBillPickupDateTime() {
		return this.airwayBillPickupDateTime;
	}

	public void setAirwayBillPickupDateTime(Timestamp airwayBillPickupDateTime) {
		this.airwayBillPickupDateTime = airwayBillPickupDateTime;
	}

	public Timestamp getAirwayBillTimestamp() {
		return this.airwayBillTimestamp;
	}

	public void setAirwayBillTimestamp(Timestamp airwayBillTimestamp) {
		this.airwayBillTimestamp = airwayBillTimestamp;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateOfReturn() {
		return this.dateOfReturn;
	}

	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	public String getDeliveryOrder() {
		return this.deliveryOrder;
	}

	public void setDeliveryOrder(String deliveryOrder) {
		this.deliveryOrder = deliveryOrder;
	}

	public String getDestCode() {
		return this.destCode;
	}

	public void setDestCode(String destCode) {
		this.destCode = destCode;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getGdnReference() {
		return this.gdnReference;
	}

	public void setGdnReference(String gdnReference) {
		this.gdnReference = gdnReference;
	}

	public BigDecimal getGiftWrapCharge() {
		return this.giftWrapCharge;
	}

	public void setGiftWrapCharge(BigDecimal giftWrapCharge) {
		this.giftWrapCharge = giftWrapCharge;
	}

	public BigDecimal getInsuranceCharge() {
		return this.insuranceCharge;
	}

	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}
	
	public void setInsuredAmount(BigDecimal insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public BigDecimal getInsuredAmount() {
		return insuredAmount;
	}

	public String getInvoiceApprovedByUserId() {
		return this.invoiceApprovedByUserId;
	}

	public void setInvoiceApprovedByUserId(String invoiceApprovedByUserId) {
		this.invoiceApprovedByUserId = invoiceApprovedByUserId;
	}

	public String getInvoiceFileNameAndLoc() {
		return this.invoiceFileNameAndLoc;
	}

	public void setInvoiceFileNameAndLoc(String invoiceFileNameAndLoc) {
		this.invoiceFileNameAndLoc = invoiceFileNameAndLoc;
	}

	public String getInvoiceResultStatus() {
		return this.invoiceResultStatus;
	}

	public void setInvoiceResultStatus(String invoiceResultStatus) {
		this.invoiceResultStatus = invoiceResultStatus;
	}

	public Boolean getMtaData() {
		return this.mtaData;
	}

	public void setMtaData(Boolean mtaData) {
		this.mtaData = mtaData;
	}

	public String getNoteReturn() {
		return this.noteReturn;
	}

	public void setNoteReturn(String noteReturn) {
		this.noteReturn = noteReturn;
	}

	public String getNoteUndelivered() {
		return this.noteUndelivered;
	}

	public void setNoteUndelivered(String noteUndelivered) {
		this.noteUndelivered = noteUndelivered;
	}

	public Integer getNumPackages() {
		return this.numPackages;
	}

	public void setNumPackages(Integer numPackages) {
		this.numPackages = numPackages;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public BigDecimal getOtherCharge() {
		return this.otherCharge;
	}

	public void setOtherCharge(BigDecimal otherCharge) {
		this.otherCharge = otherCharge;
	}

	public BigDecimal getPackageWeight() {
		return this.packageWeight;
	}

	public void setPackageWeight(BigDecimal packageWeight) {
		this.packageWeight = packageWeight;
	}

	public BigDecimal getPricePerKg() {
		return this.pricePerKg;
	}

	public void setPricePerKg(BigDecimal pricePerKg) {
		this.pricePerKg = pricePerKg;
	}

	public BigDecimal getProviderTotalCharge() {
		return this.providerTotalCharge;
	}

	public void setProviderTotalCharge(BigDecimal providerTotalCharge) {
		this.providerTotalCharge = providerTotalCharge;
	}

	public Date getReceived() {
		return this.received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getReturn_() {
		return this.return_;
	}

	public void setReturn_(String return_) {
		this.return_ = return_;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getShipper() {
		return this.shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTariff() {
		return this.tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public BigDecimal getTotalCharge() {
		return this.totalCharge;
	}

	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}

	public String getTrackingNumber() {
		return this.trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUndelivered() {
		return this.undelivered;
	}

	public void setUndelivered(Date undelivered) {
		this.undelivered = undelivered;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<LogActivityReconRecord> getLogActivityReconRecords() {
		return this.logActivityReconRecords;
	}

	public void setLogActivityReconRecords(List<LogActivityReconRecord> logActivityReconRecords) {
		this.logActivityReconRecords = logActivityReconRecords;
	}
	
	public LogApprovalStatus getLogApprovalStatus1() {
		return this.logApprovalStatus1;
	}

	public void setLogApprovalStatus1(LogApprovalStatus logApprovalStatus1) {
		this.logApprovalStatus1 = logApprovalStatus1;
	}
	
	public LogApprovalStatus getLogApprovalStatus2() {
		return this.logApprovalStatus2;
	}

	public void setLogApprovalStatus2(LogApprovalStatus logApprovalStatus2) {
		this.logApprovalStatus2 = logApprovalStatus2;
	}
	
	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
	public VenDistributionCart getVenDistributionCart() {
		return this.venDistributionCart;
	}

	public void setVenDistributionCart(VenDistributionCart venDistributionCart) {
		this.venDistributionCart = venDistributionCart;
	}
	
	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}

	public LogInvoiceAirwaybillRecord getLogInvoiceAirwaybillRecord() {
		return logInvoiceAirwaybillRecord;
	}

	public void setLogInvoiceAirwaybillRecord(
			LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord) {
		this.logInvoiceAirwaybillRecord = logInvoiceAirwaybillRecord;
	}

	/**
	 * @return the kpiPickupPerfClocked
	 */
	public Boolean getKpiPickupPerfClocked() {
		return kpiPickupPerfClocked;
	}

	/**
	 * @param kpiPickupPerfClocked the kpiPickupPerfClocked to set
	 */
	public void setKpiPickupPerfClocked(Boolean kpiPickupPerfClocked) {
		this.kpiPickupPerfClocked = kpiPickupPerfClocked;
	}

	/**
	 * @return the kpiDeliveryPerfClocked
	 */
	public Boolean getKpiDeliveryPerfClocked() {
		return kpiDeliveryPerfClocked;
	}

	/**
	 * @param kpiDeliveryPerfClocked the kpiDeliveryPerfClocked to set
	 */
	public void setKpiDeliveryPerfClocked(Boolean kpiDeliveryPerfClocked) {
		this.kpiDeliveryPerfClocked = kpiDeliveryPerfClocked;
	}

	/**
	 * @return the kpiInvoiceAccuracyClocked
	 */
	public Boolean getKpiInvoiceAccuracyClocked() {
		return kpiInvoiceAccuracyClocked;
	}

	/**
	 * @param kpiInvoiceAccuracyClocked the kpiInvoiceAccuracyClocked to set
	 */
	public void setKpiInvoiceAccuracyClocked(Boolean kpiInvoiceAccuracyClocked) {
		this.kpiInvoiceAccuracyClocked = kpiInvoiceAccuracyClocked;
	}
}