package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ven_order_item database table.
 * 
 */
@Entity
@Table(name="ven_order_item")
public class VenOrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_item")  
	@TableGenerator(name="ven_order_item", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_item_id", unique=true, nullable=false)
	private Long orderItemId;

	@Column(name="gift_card_flag", nullable=false)
	private Boolean giftCardFlag;

	@Column(name="gift_card_note", length=1000)
	private String giftCardNote;

	@Column(name="gift_wrap_flag", nullable=false)
	private Boolean giftWrapFlag;

	@Column(name="gift_wrap_price", precision=20, scale=2)
	private BigDecimal giftWrapPrice;

	@Column(name="insurance_cost", nullable=false, precision=20, scale=2)
	private BigDecimal insuranceCost;

	@Column(name="logistics_price_per_kg", nullable=false, precision=20, scale=2)
	private BigDecimal logisticsPricePerKg;

	@Column(name="merchant_settlement_flag")
	private Boolean merchantSettlementFlag;

	@Column(name="package_count")
	private Integer packageCount;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal price;

	@Column(nullable=false)
	private Integer quantity;

	@Column(name="salt_code", length=100)
	private String saltCode;

	@Column(name="shipping_cost", nullable=false, precision=20, scale=2)
	private BigDecimal shippingCost;

	@Column(name="shipping_weight", nullable=false, precision=20)
	private BigDecimal shippingWeight;

	@Column(name="special_handling_instructions", length=1000)
	private String specialHandlingInstructions;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal total;

	@Column(name="wcs_order_item_id", nullable=false, length=100)
	private String wcsOrderItemId;
	
	@Column(name="etd")
	private Integer etd;
	
	@Column(name="min_est_date")
	private Timestamp minEstDate;
	
	@Column(name="max_est_date")
	private Timestamp maxEstDate;
	
	@Column(name="logistic_discount_percentage", precision=2, scale=2)
	private BigDecimal logisticDiscountPercentage;
	
	@Column(name="logistic_discount_amount", precision=20, scale=2)
	private BigDecimal logisticDiscountAmount;
	
	@Column(name="delivery_recipient_name")
	private String deliveryRecipientName;
	
	@Column(name="delivery_recipient_status")
	private String deliveryRecipientStatus;
	
	@Column(name="delivery_received_date")
	private Date deliveryReceivedDate;
	
	@Column(name="merchant_courier")
	private String merchantCourier;
	
	@Column(name="merchant_delivered_date_start")
	private Date merchantDeliveredDateStart;
	
	@Column(name="merchant_delivered_date_end")
	private Date merchantDeliveredDateEnd;
	
	@Column(name="merchant_installation_date_start")
	private Date merchantInstallationDateStart;
	
	@Column(name="merchant_installation_date_end")
	private Date merchantInstallationDateEnd;
	
	@Column(name="merchant_install_officer")
	private String merchantInstallOfficer;
	
	@Column(name="merchant_install_mobile")
	private String merchantInstallMobile;
	
	@Column(name="merchant_install_note", length=1000)
	private String merchantInstallNote;
	
	@Column(name="tracking_number", length=1000)
	private String trackingNumber;
	
	@Column(name="sales_batch_status", length=20)
	private String salesBatchStatus;

	//bi-directional many-to-one association to FinSalesRecord
	@OneToMany(mappedBy="venOrderItem")
	private List<FinSalesRecord> finSalesRecords;

	//bi-directional many-to-one association to LogAirwayBill
	@OneToMany(mappedBy="venOrderItem")
	private List<LogAirwayBill> logAirwayBills;

	//bi-directional many-to-one association to LogMerchantPickupInstruction
	@OneToMany(mappedBy="venOrderItem")
	private List<LogMerchantPickupInstruction> logMerchantPickupInstructions;

	//bi-directional many-to-one association to VenDeliveryDocket
	@OneToMany(mappedBy="venOrderItem")
	private List<VenDeliveryDocket> venDeliveryDockets;

	//bi-directional many-to-one association to VenDistributionCart
	@OneToMany(mappedBy="venOrderItem")
	private List<VenDistributionCart> venDistributionCarts;

	//bi-directional many-to-one association to LogLogisticService
    @ManyToOne
	@JoinColumn(name="logistics_service_id", nullable=false)
	private LogLogisticService logLogisticService;

	//bi-directional many-to-one association to VenAddress
    @ManyToOne
	@JoinColumn(name="shipping_address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenMerchantProduct
    @ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private VenMerchantProduct venMerchantProduct;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false)
	private VenOrder venOrder;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="order_status_id", nullable=false)
	private VenOrderStatus venOrderStatus;

	//bi-directional many-to-one association to VenRecipient
    @ManyToOne
	@JoinColumn(name="recipient_id", nullable=false)
	private VenRecipient venRecipient;

	//bi-directional many-to-one association to VenOrderItemAdjustment
	@OneToMany(mappedBy="venOrderItem")
	private List<VenOrderItemAdjustment> venOrderItemAdjustments;

	//bi-directional many-to-one association to VenOrderItemStatusHistory
	@OneToMany(mappedBy="venOrderItem")
	private List<VenOrderItemStatusHistory> venOrderItemStatusHistories;

	//bi-directional many-to-one association to VenSettlementRecord
	@OneToMany(mappedBy="venOrderItem")
	private List<VenSettlementRecord> venSettlementRecords;
	
	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrderItem")
	private List<VenOrderItemAddress> venOrderItemAddresses;
	
	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrderItem")
	private List<VenOrderItemContactDetail> venOrderItemContactDetails;

    public VenOrderItem() {
    }

	public Long getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
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

	public BigDecimal getLogisticsPricePerKg() {
		return this.logisticsPricePerKg;
	}

	public void setLogisticsPricePerKg(BigDecimal logisticsPricePerKg) {
		this.logisticsPricePerKg = logisticsPricePerKg;
	}

	public Boolean getMerchantSettlementFlag() {
		return this.merchantSettlementFlag;
	}

	public void setMerchantSettlementFlag(Boolean merchantSettlementFlag) {
		this.merchantSettlementFlag = merchantSettlementFlag;
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

	public String getWcsOrderItemId() {
		return this.wcsOrderItemId;
	}

	public void setWcsOrderItemId(String wcsOrderItemId) {
		this.wcsOrderItemId = wcsOrderItemId;
	}

	public List<FinSalesRecord> getFinSalesRecords() {
		return this.finSalesRecords;
	}

	public void setFinSalesRecords(List<FinSalesRecord> finSalesRecords) {
		this.finSalesRecords = finSalesRecords;
	}
	
	public List<LogAirwayBill> getLogAirwayBills() {
		return this.logAirwayBills;
	}

	public void setLogAirwayBills(List<LogAirwayBill> logAirwayBills) {
		this.logAirwayBills = logAirwayBills;
	}
	
	public List<LogMerchantPickupInstruction> getLogMerchantPickupInstructions() {
		return this.logMerchantPickupInstructions;
	}

	public void setLogMerchantPickupInstructions(List<LogMerchantPickupInstruction> logMerchantPickupInstructions) {
		this.logMerchantPickupInstructions = logMerchantPickupInstructions;
	}
	
	public List<VenDeliveryDocket> getVenDeliveryDockets() {
		return this.venDeliveryDockets;
	}

	public void setVenDeliveryDockets(List<VenDeliveryDocket> venDeliveryDockets) {
		this.venDeliveryDockets = venDeliveryDockets;
	}
	
	public List<VenDistributionCart> getVenDistributionCarts() {
		return this.venDistributionCarts;
	}

	public void setVenDistributionCarts(List<VenDistributionCart> venDistributionCarts) {
		this.venDistributionCarts = venDistributionCarts;
	}
	
	public LogLogisticService getLogLogisticService() {
		return this.logLogisticService;
	}

	public void setLogLogisticService(LogLogisticService logLogisticService) {
		this.logLogisticService = logLogisticService;
	}
	
	public VenAddress getVenAddress() {
		return this.venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}
	
	public VenMerchantProduct getVenMerchantProduct() {
		return this.venMerchantProduct;
	}

	public void setVenMerchantProduct(VenMerchantProduct venMerchantProduct) {
		this.venMerchantProduct = venMerchantProduct;
	}
	
	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
	public VenOrderStatus getVenOrderStatus() {
		return this.venOrderStatus;
	}

	public void setVenOrderStatus(VenOrderStatus venOrderStatus) {
		this.venOrderStatus = venOrderStatus;
	}
	
	public VenRecipient getVenRecipient() {
		return this.venRecipient;
	}

	public void setVenRecipient(VenRecipient venRecipient) {
		this.venRecipient = venRecipient;
	}
	
	public List<VenOrderItemAdjustment> getVenOrderItemAdjustments() {
		return this.venOrderItemAdjustments;
	}

	public void setVenOrderItemAdjustments(List<VenOrderItemAdjustment> venOrderItemAdjustments) {
		this.venOrderItemAdjustments = venOrderItemAdjustments;
	}
	
	public List<VenOrderItemStatusHistory> getVenOrderItemStatusHistories() {
		return this.venOrderItemStatusHistories;
	}

	public void setVenOrderItemStatusHistories(List<VenOrderItemStatusHistory> venOrderItemStatusHistories) {
		this.venOrderItemStatusHistories = venOrderItemStatusHistories;
	}
	
	public List<VenSettlementRecord> getVenSettlementRecords() {
		return this.venSettlementRecords;
	}

	public void setVenSettlementRecords(List<VenSettlementRecord> venSettlementRecords) {
		this.venSettlementRecords = venSettlementRecords;
	}

	public List<VenOrderItemAddress> getVenOrderItemAddresses() {
		return venOrderItemAddresses;
	}

	public void setVenOrderItemAddresses(
			List<VenOrderItemAddress> venOrderItemAddresses) {
		this.venOrderItemAddresses = venOrderItemAddresses;
	}

	public List<VenOrderItemContactDetail> getVenOrderItemContactDetails() {
		return venOrderItemContactDetails;
	}

	public void setVenOrderItemContactDetails(
			List<VenOrderItemContactDetail> venOrderItemContactDetails) {
		this.venOrderItemContactDetails = venOrderItemContactDetails;
	}

	public Integer getEtd() {
		return etd;
	}

	public void setEtd(Integer etd) {
		this.etd = etd;
	}

	public Timestamp getMinEstDate() {
		return minEstDate;
	}

	public void setMinEstDate(Timestamp minEstDate) {
		this.minEstDate = minEstDate;
	}

	public Timestamp getMaxEstDate() {
		return maxEstDate;
	}

	public void setMaxEstDate(Timestamp maxEstDate) {
		this.maxEstDate = maxEstDate;
	}

	public BigDecimal getLogisticDiscountPercentage() {
		return logisticDiscountPercentage;
	}

	public void setLogisticDiscountPercentage(BigDecimal logisticDiscountPercentage) {
		this.logisticDiscountPercentage = logisticDiscountPercentage;
	}

	public BigDecimal getLogisticDiscountAmount() {
		return logisticDiscountAmount;
	}

	public void setLogisticDiscountAmount(BigDecimal logisticDiscountAmount) {
		this.logisticDiscountAmount = logisticDiscountAmount;
	}

	public String getDeliveryRecipientName() {
		return deliveryRecipientName;
	}

	public void setDeliveryRecipientName(String deliveryRecipientName) {
		this.deliveryRecipientName = deliveryRecipientName;
	}

	public String getDeliveryRecipientStatus() {
		return deliveryRecipientStatus;
	}

	public void setDeliveryRecipientStatus(String deliveryRecipientStatus) {
		this.deliveryRecipientStatus = deliveryRecipientStatus;
	}

	public Date getDeliveryReceivedDate() {
		return deliveryReceivedDate;
	}

	public void setDeliveryReceivedDate(Date deliveryReceivedDate) {
		this.deliveryReceivedDate = deliveryReceivedDate;
	}

	public String getMerchantCourier() {
		return merchantCourier;
	}

	public void setMerchantCourier(String merchantCourier) {
		this.merchantCourier = merchantCourier;
	}

	public Date getMerchantDeliveredDateStart() {
		return merchantDeliveredDateStart;
	}

	public void setMerchantDeliveredDateStart(Date merchantDeliveredDateStart) {
		this.merchantDeliveredDateStart = merchantDeliveredDateStart;
	}

	public Date getMerchantDeliveredDateEnd() {
		return merchantDeliveredDateEnd;
	}

	public void setMerchantDeliveredDateEnd(Date merchantDeliveredDateEnd) {
		this.merchantDeliveredDateEnd = merchantDeliveredDateEnd;
	}

	public Date getMerchantInstallationDateStart() {
		return merchantInstallationDateStart;
	}

	public void setMerchantInstallationDateStart(Date merchantInstallationDateStart) {
		this.merchantInstallationDateStart = merchantInstallationDateStart;
	}

	public Date getMerchantInstallationDateEnd() {
		return merchantInstallationDateEnd;
	}

	public void setMerchantInstallationDateEnd(Date merchantInstallationDateEnd) {
		this.merchantInstallationDateEnd = merchantInstallationDateEnd;
	}

	public String getMerchantInstallOfficer() {
		return merchantInstallOfficer;
	}

	public void setMerchantInstallOfficer(String merchantInstallOfficer) {
		this.merchantInstallOfficer = merchantInstallOfficer;
	}

	public String getMerchantInstallMobile() {
		return merchantInstallMobile;
	}

	public void setMerchantInstallMobile(String merchantInstallMobile) {
		this.merchantInstallMobile = merchantInstallMobile;
	}

	public String getMerchantInstallNote() {
		return merchantInstallNote;
	}

	public void setMerchantInstallNote(String merchantInstallNote) {
		this.merchantInstallNote = merchantInstallNote;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getSalesBatchStatus() {
		return salesBatchStatus;
	}

	public void setSalesBatchStatus(String salesBatchStatus) {
		this.salesBatchStatus = salesBatchStatus;
	}
}