package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_promotion_share_calc_method database table.
 * 
 */
@Entity
@Table(name="ven_promotion_share_calc_method")
public class VenPromotionShareCalcMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_promotion_share_calc_method")  
	@TableGenerator(name="ven_promotion_share_calc_method", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="promotion_calc_method_id", unique=true, nullable=false)
	private Long promotionCalcMethodId;

	@Column(name="promotion_calc_method_desc", nullable=false, length=100)
	private String promotionCalcMethodDesc;

	//bi-directional many-to-one association to VenPartyPromotionShare
	@OneToMany(mappedBy="venPromotionShareCalcMethod")
	private List<VenPartyPromotionShare> venPartyPromotionShares;

    public VenPromotionShareCalcMethod() {
    }

	public Long getPromotionCalcMethodId() {
		return this.promotionCalcMethodId;
	}

	public void setPromotionCalcMethodId(Long promotionCalcMethodId) {
		this.promotionCalcMethodId = promotionCalcMethodId;
	}

	public String getPromotionCalcMethodDesc() {
		return this.promotionCalcMethodDesc;
	}

	public void setPromotionCalcMethodDesc(String promotionCalcMethodDesc) {
		this.promotionCalcMethodDesc = promotionCalcMethodDesc;
	}

	public List<VenPartyPromotionShare> getVenPartyPromotionShares() {
		return this.venPartyPromotionShares;
	}

	public void setVenPartyPromotionShares(List<VenPartyPromotionShare> venPartyPromotionShares) {
		this.venPartyPromotionShares = venPartyPromotionShares;
	}
	
}