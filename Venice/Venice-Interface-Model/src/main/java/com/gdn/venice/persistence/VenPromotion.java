package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_promotion database table.
 * 
 */
@Entity
@Table(name="ven_promotion")
public class VenPromotion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_promotion")  
	@TableGenerator(name="ven_promotion", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="promotion_id", unique=true, nullable=false)
	private Long promotionId;

	@Column(name="promotion_code", nullable=false, length=100)
	private String promotionCode;

	@Column(name="promotion_name", nullable=true, length=500)
	private String promotionName;

	//bi-directional many-to-one association to VenOrderItemAdjustment
	@OneToMany(mappedBy="venPromotion")
	private List<VenOrderItemAdjustment> venOrderItemAdjustments;

	//bi-directional many-to-one association to VenPartyPromotionShare
	@OneToMany(mappedBy="venPromotion")
	private List<VenPartyPromotionShare> venPartyPromotionShares;
		
	@ManyToOne
	@JoinColumn(name="promotion_type")
	private VenPromotionType venPromotionType;
	
	@Column(name="gdn_margin")
	private Integer gdnMargin;
	
	@Column(name="merchant_margin")
	private Integer merchantMargin;
	
	@Column(name="others_margin")
	private Integer othersMargin;

    public VenPromotion() {
    }

	public Long getPromotionId() {
		return this.promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionCode() {
		return this.promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionName() {
		return this.promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public List<VenOrderItemAdjustment> getVenOrderItemAdjustments() {
		return this.venOrderItemAdjustments;
	}

	public void setVenOrderItemAdjustments(List<VenOrderItemAdjustment> venOrderItemAdjustments) {
		this.venOrderItemAdjustments = venOrderItemAdjustments;
	}
	
	public List<VenPartyPromotionShare> getVenPartyPromotionShares() {
		return this.venPartyPromotionShares;
	}

	public void setVenPartyPromotionShares(List<VenPartyPromotionShare> venPartyPromotionShares) {
		this.venPartyPromotionShares = venPartyPromotionShares;
	}

	public VenPromotionType getVenPromotionType() {
		return venPromotionType;
	}

	public void setVenPromotionType(VenPromotionType venPromotionType) {
		this.venPromotionType = venPromotionType;
	}

	public Integer getGdnMargin() {
		return gdnMargin;
	}

	public void setGdnMargin(Integer gdnMargin) {
		this.gdnMargin = gdnMargin;
	}

	public Integer getMerchantMargin() {
		return merchantMargin;
	}

	public void setMerchantMargin(Integer merchantMargin) {
		this.merchantMargin = merchantMargin;
	}

	public Integer getOthersMargin() {
		return othersMargin;
	}

	public void setOthersMargin(Integer othersMargin) {
		this.othersMargin = othersMargin;
	}
	
}