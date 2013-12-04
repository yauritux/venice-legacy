package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_order_item_history database table.
 * 
 */
@Entity
@Table(name="ven_order_item_history")
public class VenOrderItemHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_item_history")  
	@TableGenerator(name="ven_order_item_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_item_history_id", unique=true, nullable=false)
	private Long orderItemHistoryId;

	@Column(name="gift_card_flag", nullable=false)
	private Boolean giftCardFlag;

	@Column(name="gift_card_note", nullable=false, length=1000)
	private String giftCardNote;

	@Column(name="gift_card_price", nullable=false, precision=20, scale=2)
	private BigDecimal giftCardPrice;

	@Column(name="gift_wrap_flag", nullable=false)
	private Boolean giftWrapFlag;

	@Column(name="gift_wrap_price", nullable=false, precision=20, scale=2)
	private BigDecimal giftWrapPrice;

	@Column(name="history_timestamp", nullable=false)
	private Timestamp historyTimestamp;

	@Column(name="insurance_cost", nullable=false, precision=20, scale=2)
	private BigDecimal insuranceCost;

	@Column(name="logistics_pickup_time")
	private Timestamp logisticsPickupTime;

	@Column(name="logistics_service_id", nullable=false)
	private Long logisticsServiceId;

	@Column(name="margin_value", nullable=false, precision=20, scale=2)
	private BigDecimal marginValue;

	@Column(name="merchant_settlement_flag", nullable=false)
	private Boolean merchantSettlementFlag;

	@Column(name="order_id", nullable=false)
	private Long orderId;

	@Column(name="order_item_id", nullable=false)
	private Long orderItemId;

	@Column(name="order_item_price", nullable=false, precision=20, scale=2)
	private BigDecimal orderItemPrice;

	@Column(name="order_item_qty", nullable=false)
	private Integer orderItemQty;

	@Column(name="order_item_total", nullable=false, precision=20, scale=2)
	private BigDecimal orderItemTotal;

	@Column(name="order_status_id", nullable=false)
	private Long orderStatusId;

	@Column(name="package_count", nullable=false)
	private Integer packageCount;

	@Column(name="product_id", nullable=false)
	private Long productId;

	@Column(name="recipient_id", nullable=false)
	private Long recipientId;

	@Column(name="salt_code", length=100)
	private String saltCode;

	@Column(name="shipping_address_id", nullable=false)
	private Long shippingAddressId;

	@Column(name="shipping_cost", nullable=false, precision=20, scale=2)
	private BigDecimal shippingCost;

	@Column(name="shipping_weight", nullable=false, precision=20, scale=2)
	private BigDecimal shippingWeight;

	@Column(name="wcs_order_item_id", nullable=false, length=100)
	private String wcsOrderItemId;

	@Column(name="wcs_rma_qty")
	private Integer wcsRmaQty;

	//bi-directional many-to-one association to VenMasterChangeType
    @ManyToOne
	@JoinColumn(name="master_change_id", nullable=false)
	private VenMasterChangeType venMasterChangeType;

    public VenOrderItemHistory() {
    }

	public Long getOrderItemHistoryId() {
		return this.orderItemHistoryId;
	}

	public void setOrderItemHistoryId(Long orderItemHistoryId) {
		this.orderItemHistoryId = orderItemHistoryId;
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

	public BigDecimal getGiftCardPrice() {
		return this.giftCardPrice;
	}

	public void setGiftCardPrice(BigDecimal giftCardPrice) {
		this.giftCardPrice = giftCardPrice;
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

	public Timestamp getHistoryTimestamp() {
		return this.historyTimestamp;
	}

	public void setHistoryTimestamp(Timestamp historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public BigDecimal getInsuranceCost() {
		return this.insuranceCost;
	}

	public void setInsuranceCost(BigDecimal insuranceCost) {
		this.insuranceCost = insuranceCost;
	}

	public Timestamp getLogisticsPickupTime() {
		return this.logisticsPickupTime;
	}

	public void setLogisticsPickupTime(Timestamp logisticsPickupTime) {
		this.logisticsPickupTime = logisticsPickupTime;
	}

	public Long getLogisticsServiceId() {
		return this.logisticsServiceId;
	}

	public void setLogisticsServiceId(Long logisticsServiceId) {
		this.logisticsServiceId = logisticsServiceId;
	}

	public BigDecimal getMarginValue() {
		return this.marginValue;
	}

	public void setMarginValue(BigDecimal marginValue) {
		this.marginValue = marginValue;
	}

	public Boolean getMerchantSettlementFlag() {
		return this.merchantSettlementFlag;
	}

	public void setMerchantSettlementFlag(Boolean merchantSettlementFlag) {
		this.merchantSettlementFlag = merchantSettlementFlag;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public BigDecimal getOrderItemPrice() {
		return this.orderItemPrice;
	}

	public void setOrderItemPrice(BigDecimal orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}

	public Integer getOrderItemQty() {
		return this.orderItemQty;
	}

	public void setOrderItemQty(Integer orderItemQty) {
		this.orderItemQty = orderItemQty;
	}

	public BigDecimal getOrderItemTotal() {
		return this.orderItemTotal;
	}

	public void setOrderItemTotal(BigDecimal orderItemTotal) {
		this.orderItemTotal = orderItemTotal;
	}

	public Long getOrderStatusId() {
		return this.orderStatusId;
	}

	public void setOrderStatusId(Long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getPackageCount() {
		return this.packageCount;
	}

	public void setPackageCount(Integer packageCount) {
		this.packageCount = packageCount;
	}

	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getRecipientId() {
		return this.recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public String getSaltCode() {
		return this.saltCode;
	}

	public void setSaltCode(String saltCode) {
		this.saltCode = saltCode;
	}

	public Long getShippingAddressId() {
		return this.shippingAddressId;
	}

	public void setShippingAddressId(Long shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
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

	public String getWcsOrderItemId() {
		return this.wcsOrderItemId;
	}

	public void setWcsOrderItemId(String wcsOrderItemId) {
		this.wcsOrderItemId = wcsOrderItemId;
	}

	public Integer getWcsRmaQty() {
		return this.wcsRmaQty;
	}

	public void setWcsRmaQty(Integer wcsRmaQty) {
		this.wcsRmaQty = wcsRmaQty;
	}

	public VenMasterChangeType getVenMasterChangeType() {
		return this.venMasterChangeType;
	}

	public void setVenMasterChangeType(VenMasterChangeType venMasterChangeType) {
		this.venMasterChangeType = venMasterChangeType;
	}
	
}