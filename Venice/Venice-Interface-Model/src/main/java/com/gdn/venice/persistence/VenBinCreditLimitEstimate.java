package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the ven_bin_credit_limit_estimate database table.
 * 
 */
@Entity
@Table(name="ven_bin_credit_limit_estimate")
public class VenBinCreditLimitEstimate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_bin_credit_limit_estimate")  
	@TableGenerator(name="ven_bin_credit_limit_estimate", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="bin_credit_limit_estimate_id", unique=true, nullable=false)
	private Long binCreditLimitEstimateId;
	
	@Column(name="bank_name",nullable=false, length=100)
	private String bankName;

	@Column(name="bin_number", nullable=false, length=6)
	private String binNumber;

	@Column(name="credit_limit_estimate", nullable=false, precision=20)
	private BigDecimal creditLimitEstimate;

	@Column(nullable=false, length=1000)
	private String description;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(nullable=false, length=100)
	private String severity;

	//bi-directional many-to-one association to VenCardType
    @ManyToOne
	@JoinColumn(name="card_type_id", nullable=false)
	private VenCardType venCardType;

    public VenBinCreditLimitEstimate() {
    }

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getBinCreditLimitEstimateId() {
		return this.binCreditLimitEstimateId;
	}

	public void setBinCreditLimitEstimateId(Long binCreditLimitEstimateId) {
		this.binCreditLimitEstimateId = binCreditLimitEstimateId;
	}

	public String getBinNumber() {
		return this.binNumber;
	}

	public void setBinNumber(String binNumber) {
		this.binNumber = binNumber;
	}

	public BigDecimal getCreditLimitEstimate() {
		return this.creditLimitEstimate;
	}

	public void setCreditLimitEstimate(BigDecimal creditLimitEstimate) {
		this.creditLimitEstimate = creditLimitEstimate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getSeverity() {
		return this.severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public VenCardType getVenCardType() {
		return this.venCardType;
	}

	public void setVenCardType(VenCardType venCardType) {
		this.venCardType = venCardType;
	}
	
}