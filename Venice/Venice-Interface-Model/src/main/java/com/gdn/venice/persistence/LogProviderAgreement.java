package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the log_provider_agreement database table.
 * 
 */
@Entity
@Table(name="log_provider_agreement")
public class LogProviderAgreement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_provider_agreement")  
	@TableGenerator(name="log_provider_agreement", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="provider_agreement_id", unique=true, nullable=false)
	private Long providerAgreementId;

    @Temporal( TemporalType.DATE)
	@Column(name="agreement_date", nullable=false)
	private Date agreementDate;

	@Column(name="agreement_desc", nullable=false, length=100)
	private String agreementDesc;

	@Column(name="delivery_time_commitment", nullable=false)
	private Integer deliveryTimeCommitment;

	@Column(name="discount_level_pct", nullable=false, precision=2)
	private BigDecimal discountLevelPct;

    @Temporal( TemporalType.DATE)
	@Column(name="expiry_date", nullable=false)
	private Date expiryDate;

	@Column(name="pickup_time_commitment", nullable=false)
	private Integer pickupTimeCommitment;

	@Column(name="ppn_percentage", nullable=false, precision=2)
	private BigDecimal ppnPercentage;

	//bi-directional many-to-one association to KpiPartySla
    @ManyToOne
	@JoinColumn(name="party_sla_id", nullable=false)
	private KpiPartySla kpiPartySla;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

    public LogProviderAgreement() {
    }

	public Long getProviderAgreementId() {
		return this.providerAgreementId;
	}

	public void setProviderAgreementId(Long providerAgreementId) {
		this.providerAgreementId = providerAgreementId;
	}

	public Date getAgreementDate() {
		return this.agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public String getAgreementDesc() {
		return this.agreementDesc;
	}

	public void setAgreementDesc(String agreementDesc) {
		this.agreementDesc = agreementDesc;
	}

	public Integer getDeliveryTimeCommitment() {
		return this.deliveryTimeCommitment;
	}

	public void setDeliveryTimeCommitment(Integer deliveryTimeCommitment) {
		this.deliveryTimeCommitment = deliveryTimeCommitment;
	}

	public BigDecimal getDiscountLevelPct() {
		return this.discountLevelPct;
	}

	public void setDiscountLevelPct(BigDecimal discountLevelPct) {
		this.discountLevelPct = discountLevelPct;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getPickupTimeCommitment() {
		return this.pickupTimeCommitment;
	}

	public void setPickupTimeCommitment(Integer pickupTimeCommitment) {
		this.pickupTimeCommitment = pickupTimeCommitment;
	}

	public BigDecimal getPpnPercentage() {
		return this.ppnPercentage;
	}

	public void setPpnPercentage(BigDecimal ppnPercentage) {
		this.ppnPercentage = ppnPercentage;
	}

	public KpiPartySla getKpiPartySla() {
		return this.kpiPartySla;
	}

	public void setKpiPartySla(KpiPartySla kpiPartySla) {
		this.kpiPartySla = kpiPartySla;
	}
	
	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
}