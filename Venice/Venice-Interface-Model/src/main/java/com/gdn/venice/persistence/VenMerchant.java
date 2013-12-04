package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_merchant database table.
 * 
 */
@Entity
@Table(name="ven_merchant")
public class VenMerchant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_merchant")  
	@TableGenerator(name="ven_merchant", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="merchant_id", unique=true, nullable=false)
	private Long merchantId;

	@Column(name="wcs_merchant_id", nullable=false, length=100)
	private String wcsMerchantId;

	//bi-directional many-to-one association to LogMerchantPickupInstruction
	@OneToMany(mappedBy="venMerchant")
	private List<LogMerchantPickupInstruction> logMerchantPickupInstructions;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id")
	private VenParty venParty;

	//bi-directional many-to-one association to VenMerchantAgreement
	@OneToMany(mappedBy="venMerchant")
	private List<VenMerchantAgreement> venMerchantAgreements;

	//bi-directional many-to-one association to VenMerchantProduct
	@OneToMany(mappedBy="venMerchant")
	private List<VenMerchantProduct> venMerchantProducts;

	//bi-directional many-to-one association to VenTransactionFee
	@OneToMany(mappedBy="venMerchant")
	private List<VenTransactionFee> venTransactionFees;

    public VenMerchant() {
    }

	public Long getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getWcsMerchantId() {
		return this.wcsMerchantId;
	}

	public void setWcsMerchantId(String wcsMerchantId) {
		this.wcsMerchantId = wcsMerchantId;
	}

	public List<LogMerchantPickupInstruction> getLogMerchantPickupInstructions() {
		return this.logMerchantPickupInstructions;
	}

	public void setLogMerchantPickupInstructions(List<LogMerchantPickupInstruction> logMerchantPickupInstructions) {
		this.logMerchantPickupInstructions = logMerchantPickupInstructions;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<VenMerchantAgreement> getVenMerchantAgreements() {
		return this.venMerchantAgreements;
	}

	public void setVenMerchantAgreements(List<VenMerchantAgreement> venMerchantAgreements) {
		this.venMerchantAgreements = venMerchantAgreements;
	}
	
	public List<VenMerchantProduct> getVenMerchantProducts() {
		return this.venMerchantProducts;
	}

	public void setVenMerchantProducts(List<VenMerchantProduct> venMerchantProducts) {
		this.venMerchantProducts = venMerchantProducts;
	}
	
	public List<VenTransactionFee> getVenTransactionFees() {
		return this.venTransactionFees;
	}

	public void setVenTransactionFees(List<VenTransactionFee> venTransactionFees) {
		this.venTransactionFees = venTransactionFees;
	}
	
}