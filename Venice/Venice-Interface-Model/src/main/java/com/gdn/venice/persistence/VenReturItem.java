package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ven_retur_item database table.
 * 
 */
@Entity
@Table(name="ven_retur_item")
public class VenReturItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur_item")  
	@TableGenerator(name="ven_retur_item", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_item_id")
	private Long returItemId;

    @Temporal( TemporalType.DATE)
	@Column(name="delivery_received_date")
	private Date deliveryReceivedDate;

	@Column(name="delivery_recipient_name")
	private String deliveryRecipientName;

	@Column(name="delivery_recipient_status")
	private String deliveryRecipientStatus;

	private Integer etd;

	@Column(name="gift_card_flag")
	private Boolean giftCardFlag;

	@Column(name="gift_card_note")
	private String giftCardNote;

	@Column(name="gift_wrap_flag")
	private Boolean giftWrapFlag;

	@Column(name="gift_wrap_price")
	private BigDecimal giftWrapPrice;

	@Column(name="insurance_cost")
	private BigDecimal insuranceCost;

	@Column(name="logistic_discount_amount")
	private BigDecimal logisticDiscountAmount;

	@Column(name="logistic_discount_percentage")
	private BigDecimal logisticDiscountPercentage;

	@Column(name="logistics_price_per_kg")
	private BigDecimal logisticsPricePerKg;

	@Column(name="max_est_date")
	private Timestamp maxEstDate;

	@Column(name="merchant_courier")
	private String merchantCourier;

    @Temporal( TemporalType.DATE)
	@Column(name="merchant_delivered_date_end")
	private Date merchantDeliveredDateEnd;

    @Temporal( TemporalType.DATE)
	@Column(name="merchant_delivered_date_start")
	private Date merchantDeliveredDateStart;

	@Column(name="merchant_install_mobile")
	private String merchantInstallMobile;

	@Column(name="merchant_install_note")
	private String merchantInstallNote;

	@Column(name="merchant_install_officer")
	private String merchantInstallOfficer;

    @Temporal( TemporalType.DATE)
	@Column(name="merchant_installation_date_end")
	private Date merchantInstallationDateEnd;

    @Temporal( TemporalType.DATE)
	@Column(name="merchant_installation_date_start")
	private Date merchantInstallationDateStart;

	@Column(name="merchant_settlement_flag")
	private Boolean merchantSettlementFlag;

	@Column(name="min_est_date")
	private Timestamp minEstDate;

	@Column(name="package_count")
	private Integer packageCount;

	private BigDecimal price;

	private Integer quantity;

	@Column(name="salt_code")
	private String saltCode;

	@Column(name="shipping_cost")
	private BigDecimal shippingCost;

	@Column(name="shipping_weight")
	private BigDecimal shippingWeight;

	@Column(name="special_handling_instructions")
	private String specialHandlingInstructions;

	private BigDecimal total;

	@Column(name="tracking_number")
	private String trackingNumber;

	@Column(name="wcs_retur_item_id")
	private String wcsReturItemId;

	//bi-directional many-to-one association to LogAirwayBillRetur
	@OneToMany(mappedBy="venReturItem")
	private List<LogAirwayBillRetur> logAirwayBillReturs;
	
	//bi-directional many-to-one association to LogMerchantPickupInstruction
	@OneToMany(mappedBy="venReturItem")
	private List<VenSettlementRecord> venSettlementRecords;
	
	//bi-directional many-to-one association to LogMerchantPickupInstruction
	@OneToMany(mappedBy="venReturItem")
	private List<LogMerchantPickupInstruction> logMerchantPickupInstructions;

	//bi-directional many-to-one association to VenRetur
    @ManyToOne
	@JoinColumn(name="retur_id")
	private VenRetur venRetur;
    
	//bi-directional many-to-one association to VenReturStatus
    @ManyToOne
	@JoinColumn(name="retur_status_id", nullable=false)
	private VenOrderStatus venReturStatus;
    
	//bi-directional many-to-one association to VenMerchantProduct
    @ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private VenMerchantProduct venMerchantProduct;
    
	//bi-directional many-to-one association to VenAddress
    @ManyToOne
	@JoinColumn(name="shipping_address_id", nullable=false)
	private VenAddress venAddress;
    
	//bi-directional many-to-one association to VenRecipient
    @ManyToOne
	@JoinColumn(name="recipient_id", nullable=false)
	private VenRecipient venRecipient;
    
	//bi-directional many-to-one association to LogLogisticService
    @ManyToOne
	@JoinColumn(name="logistics_service_id", nullable=false)
	private LogLogisticService logLogisticService;
    
  	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venReturItem")
	private List<VenReturItemAddress> venReturItemAddresses;
	
	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venReturItem")
	private List<VenReturItemContactDetail> venReturItemContactDetails;

    public VenReturItem() {
    }

	public Long getReturItemId() {
		return this.returItemId;
	}

	public void setReturItemId(Long returItemId) {
		this.returItemId = returItemId;
	}

	public Date getDeliveryReceivedDate() {
		return this.deliveryReceivedDate;
	}

	public void setDeliveryReceivedDate(Date deliveryReceivedDate) {
		this.deliveryReceivedDate = deliveryReceivedDate;
	}

	public String getDeliveryRecipientName() {
		return this.deliveryRecipientName;
	}

	public void setDeliveryRecipientName(String deliveryRecipientName) {
		this.deliveryRecipientName = deliveryRecipientName;
	}

	public String getDeliveryRecipientStatus() {
		return this.deliveryRecipientStatus;
	}

	public void setDeliveryRecipientStatus(String deliveryRecipientStatus) {
		this.deliveryRecipientStatus = deliveryRecipientStatus;
	}

	public Integer getEtd() {
		return this.etd;
	}

	public void setEtd(Integer etd) {
		this.etd = etd;
	}

	public Boolean getGiftCardFlag() {
		return this.giftCardFlag;
	}

	public void setGiftCardFlag(Boolean giftCardFlag) {
		this.giftCardFlag = giftCardFlag;
	}

	public String getGiftCardNote() {
		return this.giftCardNote;
	}

	public void setGiftCardNote(String giftCardNote) {
		this.giftCardNote = giftCardNote;
	}

	public Boolean getGiftWrapFlag() {
		return this.giftWrapFlag;
	}

	public void setGiftWrapFlag(Boolean giftWrapFlag) {
		this.giftWrapFlag = giftWrapFlag;
	}

	public BigDecimal getGiftWrapPrice() {
		return this.giftWrapPrice;
	}

	public void setGiftWrapPrice(BigDecimal giftWrapPrice) {
		this.giftWrapPrice = giftWrapPrice;
	}

	public BigDecimal getInsuranceCost() {
		return this.insuranceCost;
	}

	public void setInsuranceCost(BigDecimal insuranceCost) {
		this.insuranceCost = insuranceCost;
	}

	public BigDecimal getLogisticDiscountAmount() {
		return this.logisticDiscountAmount;
	}

	public void setLogisticDiscountAmount(BigDecimal logisticDiscountAmount) {
		this.logisticDiscountAmount = logisticDiscountAmount;
	}

	public BigDecimal getLogisticDiscountPercentage() {
		return this.logisticDiscountPercentage;
	}

	public void setLogisticDiscountPercentage(BigDecimal logisticDiscountPercentage) {
		this.logisticDiscountPercentage = logisticDiscountPercentage;
	}

	public BigDecimal getLogisticsPricePerKg() {
		return this.logisticsPricePerKg;
	}

	public void setLogisticsPricePerKg(BigDecimal logisticsPricePerKg) {
		this.logisticsPricePerKg = logisticsPricePerKg;
	}

	public Timestamp getMaxEstDate() {
		return this.maxEstDate;
	}

	public void setMaxEstDate(Timestamp maxEstDate) {
		this.maxEstDate = maxEstDate;
	}

	public String getMerchantCourier() {
		return this.merchantCourier;
	}

	public void setMerchantCourier(String merchantCourier) {
		this.merchantCourier = merchantCourier;
	}

	public Date getMerchantDeliveredDateEnd() {
		return this.merchantDeliveredDateEnd;
	}

	public void setMerchantDeliveredDateEnd(Date merchantDeliveredDateEnd) {
		this.merchantDeliveredDateEnd = merchantDeliveredDateEnd;
	}

	public Date getMerchantDeliveredDateStart() {
		return this.merchantDeliveredDateStart;
	}

	public void setMerchantDeliveredDateStart(Date merchantDeliveredDateStart) {
		this.merchantDeliveredDateStart = merchantDeliveredDateStart;
	}

	public String getMerchantInstallMobile() {
		return this.merchantInstallMobile;
	}

	public void setMerchantInstallMobile(String merchantInstallMobile) {
		this.merchantInstallMobile = merchantInstallMobile;
	}

	public String getMerchantInstallNote() {
		return this.merchantInstallNote;
	}

	public void setMerchantInstallNote(String merchantInstallNote) {
		this.merchantInstallNote = merchantInstallNote;
	}

	public String getMerchantInstallOfficer() {
		return this.merchantInstallOfficer;
	}

	public void setMerchantInstallOfficer(String merchantInstallOfficer) {
		this.merchantInstallOfficer = merchantInstallOfficer;
	}

	public Date getMerchantInstallationDateEnd() {
		return this.merchantInstallationDateEnd;
	}

	public void setMerchantInstallationDateEnd(Date merchantInstallationDateEnd) {
		this.merchantInstallationDateEnd = merchantInstallationDateEnd;
	}

	public Date getMerchantInstallationDateStart() {
		return this.merchantInstallationDateStart;
	}

	public void setMerchantInstallationDateStart(Date merchantInstallationDateStart) {
		this.merchantInstallationDateStart = merchantInstallationDateStart;
	}

	public Boolean getMerchantSettlementFlag() {
		return this.merchantSettlementFlag;
	}

	public void setMerchantSettlementFlag(Boolean merchantSettlementFlag) {
		this.merchantSettlementFlag = merchantSettlementFlag;
	}

	public Timestamp getMinEstDate() {
		return this.minEstDate;
	}

	public void setMinEstDate(Timestamp minEstDate) {
		this.minEstDate = minEstDate;
	}

	public Integer getPackageCount() {
		return this.packageCount;
	}

	public void setPackageCount(Integer packageCount) {
		this.packageCount = packageCount;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSaltCode() {
		return this.saltCode;
	}

	public void setSaltCode(String saltCode) {
		this.saltCode = saltCode;
	}

	public BigDecimal getShippingCost() {
		return this.shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	public BigDecimal getShippingWeight() {
		return this.shippingWeight;
	}

	public void setShippingWeight(BigDecimal shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public String getSpecialHandlingInstructions() {
		return this.specialHandlingInstructions;
	}

	public void setSpecialHandlingInstructions(String specialHandlingInstructions) {
		this.specialHandlingInstructions = specialHandlingInstructions;
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getTrackingNumber() {
		return this.trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getWcsReturItemId() {
		return this.wcsReturItemId;
	}

	public void setWcsReturItemId(String wcsReturItemId) {
		this.wcsReturItemId = wcsReturItemId;
	}

	public List<LogAirwayBillRetur> getLogAirwayBillReturs() {
		return this.logAirwayBillReturs;
	}

	public void setLogAirwayBillReturs(List<LogAirwayBillRetur> logAirwayBillReturs) {
		this.logAirwayBillReturs = logAirwayBillReturs;
	}
	
	public VenRetur getVenRetur() {
		return this.venRetur;
	}

	public void setVenRetur(VenRetur venRetur) {
		this.venRetur = venRetur;
	}

	public VenOrderStatus getVenReturStatus() {
		return venReturStatus;
	}

	public void setVenReturStatus(VenOrderStatus venReturStatus) {
		this.venReturStatus = venReturStatus;
	}

	public VenMerchantProduct getVenMerchantProduct() {
		return venMerchantProduct;
	}

	public void setVenMerchantProduct(VenMerchantProduct venMerchantProduct) {
		this.venMerchantProduct = venMerchantProduct;
	}

	public VenAddress getVenAddress() {
		return venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}

	public VenRecipient getVenRecipient() {
		return venRecipient;
	}

	public void setVenRecipient(VenRecipient venRecipient) {
		this.venRecipient = venRecipient;
	}

	public LogLogisticService getLogLogisticService() {
		return logLogisticService;
	}

	public void setLogLogisticService(LogLogisticService logLogisticService) {
		this.logLogisticService = logLogisticService;
	}

	public List<LogMerchantPickupInstruction> getLogMerchantPickupInstructions() {
		return logMerchantPickupInstructions;
	}

	public void setLogMerchantPickupInstructions(
			List<LogMerchantPickupInstruction> logMerchantPickupInstructions) {
		this.logMerchantPickupInstructions = logMerchantPickupInstructions;
	}
	
	public List<VenReturItemAddress> getVenReturItemAddresses() {
		return venReturItemAddresses;
	}

	public void setVenReturItemAddresses(
			List<VenReturItemAddress> venReturItemAddresses) {
		this.venReturItemAddresses = venReturItemAddresses;
	}

	public List<VenReturItemContactDetail> getVenReturItemContactDetails() {
		return venReturItemContactDetails;
	}

	public void setVenReturItemContactDetails(
			List<VenReturItemContactDetail> venReturItemContactDetails) {
		this.venReturItemContactDetails = venReturItemContactDetails;
	}

	public List<VenSettlementRecord> getVenSettlementRecords() {
		return venSettlementRecords;
	}

	public void setVenSettlementRecords(
			List<VenSettlementRecord> venSettlementRecords) {
		this.venSettlementRecords = venSettlementRecords;
	}
}