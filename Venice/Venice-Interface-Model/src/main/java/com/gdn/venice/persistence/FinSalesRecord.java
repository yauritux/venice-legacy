package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the fin_sales_record database table.
 * 
 */
@Entity
@Table(name="fin_sales_record")
public class FinSalesRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_sales_record")  
	@TableGenerator(name="fin_sales_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="sales_record_id", unique=true, nullable=false)
	private Long salesRecordId;

	@Column(name="ar_amount", precision=20, scale=2)
	private BigDecimal arAmount;

	@Column(name="customer_downpayment", nullable=false, precision=20, scale=2)
	private BigDecimal customerDownpayment;

	@Column(name="gdn_commission_amount", nullable=false, precision=20, scale=2)
	private BigDecimal gdnCommissionAmount;

	@Column(name="gdn_gift_wrap_charge_amount", nullable=false, precision=20, scale=2)
	private BigDecimal gdnGiftWrapChargeAmount;

	@Column(name="gdn_handling_fee_amount", nullable=false, precision=20, scale=2)
	private BigDecimal gdnHandlingFeeAmount;

	@Column(name="gdn_promotion_amount", nullable=false, precision=20, scale=2)
	private BigDecimal gdnPromotionAmount;

	@Column(name="gdn_transaction_fee_amount", nullable=false, precision=20, scale=2)
	private BigDecimal gdnTransactionFeeAmount;

	@Column(name="merchant_payment_amount", nullable=false, precision=20, scale=2)
	private BigDecimal merchantPaymentAmount;

	@Column(name="merchant_promotion_amount", nullable=false, precision=20, scale=2)
	private BigDecimal merchantPromotionAmount;

	@Column(name="sales_timestamp", nullable=false)
	private Timestamp salesTimestamp;

	@Column(name="third_party_promotion_amount", nullable=false, precision=20, scale=2)
	private BigDecimal thirdPartyPromotionAmount;

	@Column(name="total_logistics_related_amount", nullable=false, precision=20, scale=2)
	private BigDecimal totalLogisticsRelatedAmount;

	@Column(name="vat_amount", nullable=false, precision=20, scale=2)
	private BigDecimal vatAmount;
	
	@Column(name="payment_status", length=20)
	private String paymentStatus;
		
	@Column(name="pph23_amount", precision=20, scale=2)
	private BigDecimal pph23Amount;
	
	//bi-directional many-to-one association to FinApPayment
    @ManyToOne
	@JoinColumn(name="ap_payment_id")
	private FinApPayment finApPayment;

	//bi-directional many-to-one association to FinApprovalStatus
    @ManyToOne
	@JoinColumn(name="approval_status_id", nullable=false)
	private FinApprovalStatus finApprovalStatus;

	//bi-directional many-to-many association to FinJournalTransaction
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="fin_sales_record_journal_transaction"
		, joinColumns={
			@JoinColumn(name="sales_record_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="transaction_id", nullable=false)
			}
		)
	private List<FinJournalTransaction> finJournalTransactions;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id", nullable=false)
	private VenOrderItem venOrderItem;
    
    @Column(name="reconcile_date")
	private Timestamp reconcileDate;
    
    @Column(name="cx_finance_date")
	private Timestamp cxFinanceDate;
    
    @Column(name="cx_mta_date")
	private Timestamp cxMtaDate;

    public FinSalesRecord() {
    }

	public Long getSalesRecordId() {
		return this.salesRecordId;
	}

	public void setSalesRecordId(Long salesRecordId) {
		this.salesRecordId = salesRecordId;
	}

	public BigDecimal getArAmount() {
		return this.arAmount;
	}

	public void setArAmount(BigDecimal arAmount) {
		this.arAmount = arAmount;
	}

	public BigDecimal getCustomerDownpayment() {
		return this.customerDownpayment;
	}

	public void setCustomerDownpayment(BigDecimal customerDownpayment) {
		this.customerDownpayment = customerDownpayment;
	}

	public BigDecimal getGdnCommissionAmount() {
		return this.gdnCommissionAmount;
	}

	public void setGdnCommissionAmount(BigDecimal gdnCommissionAmount) {
		this.gdnCommissionAmount = gdnCommissionAmount;
	}

	public BigDecimal getGdnGiftWrapChargeAmount() {
		return this.gdnGiftWrapChargeAmount;
	}

	public void setGdnGiftWrapChargeAmount(BigDecimal gdnGiftWrapChargeAmount) {
		this.gdnGiftWrapChargeAmount = gdnGiftWrapChargeAmount;
	}

	public BigDecimal getGdnHandlingFeeAmount() {
		return this.gdnHandlingFeeAmount;
	}

	public void setGdnHandlingFeeAmount(BigDecimal gdnHandlingFeeAmount) {
		this.gdnHandlingFeeAmount = gdnHandlingFeeAmount;
	}

	public BigDecimal getGdnPromotionAmount() {
		return this.gdnPromotionAmount;
	}

	public void setGdnPromotionAmount(BigDecimal gdnPromotionAmount) {
		this.gdnPromotionAmount = gdnPromotionAmount;
	}

	public BigDecimal getGdnTransactionFeeAmount() {
		return this.gdnTransactionFeeAmount;
	}

	public void setGdnTransactionFeeAmount(BigDecimal gdnTransactionFeeAmount) {
		this.gdnTransactionFeeAmount = gdnTransactionFeeAmount;
	}

	public BigDecimal getMerchantPaymentAmount() {
		return this.merchantPaymentAmount;
	}

	public void setMerchantPaymentAmount(BigDecimal merchantPaymentAmount) {
		this.merchantPaymentAmount = merchantPaymentAmount;
	}

	public BigDecimal getMerchantPromotionAmount() {
		return this.merchantPromotionAmount;
	}

	public void setMerchantPromotionAmount(BigDecimal merchantPromotionAmount) {
		this.merchantPromotionAmount = merchantPromotionAmount;
	}

	public Timestamp getSalesTimestamp() {
		return this.salesTimestamp;
	}

	public void setSalesTimestamp(Timestamp salesTimestamp) {
		this.salesTimestamp = salesTimestamp;
	}

	public BigDecimal getThirdPartyPromotionAmount() {
		return this.thirdPartyPromotionAmount;
	}

	public void setThirdPartyPromotionAmount(BigDecimal thirdPartyPromotionAmount) {
		this.thirdPartyPromotionAmount = thirdPartyPromotionAmount;
	}

	public BigDecimal getTotalLogisticsRelatedAmount() {
		return this.totalLogisticsRelatedAmount;
	}

	public void setTotalLogisticsRelatedAmount(BigDecimal totalLogisticsRelatedAmount) {
		this.totalLogisticsRelatedAmount = totalLogisticsRelatedAmount;
	}

	public BigDecimal getVatAmount() {
		return this.vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public FinApPayment getFinApPayment() {
		return this.finApPayment;
	}

	public void setFinApPayment(FinApPayment finApPayment) {
		this.finApPayment = finApPayment;
	}
	
	public FinApprovalStatus getFinApprovalStatus() {
		return this.finApprovalStatus;
	}

	public void setFinApprovalStatus(FinApprovalStatus finApprovalStatus) {
		this.finApprovalStatus = finApprovalStatus;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}

	public Timestamp getReconcileDate() {
		return reconcileDate;
	}

	public void setReconcileDate(Timestamp reconcileDate) {
		this.reconcileDate = reconcileDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BigDecimal getPph23Amount() {
		return pph23Amount;
	}

	public void setPph23Amount(BigDecimal pph23Amount) {
		this.pph23Amount = pph23Amount;
	}

	public Timestamp getCxFinanceDate() {
		return cxFinanceDate;
	}

	public void setCxFinanceDate(Timestamp cxFinanceDate) {
		this.cxFinanceDate = cxFinanceDate;
	}

	public Timestamp getCxMtaDate() {
		return cxMtaDate;
	}

	public void setCxMtaDate(Timestamp cxMtaDate) {
		this.cxMtaDate = cxMtaDate;
	}
}