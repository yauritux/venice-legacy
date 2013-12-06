package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_party_promotion_share database table.
 * 
 */
@Entity
@Table(name="ven_party_promotion_share")
public class VenPartyPromotionShare implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenPartyPromotionSharePK id;

	@Column(name="promotion_calc_value", nullable=false, length=1000)
	private String promotionCalcValue;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false, insertable=false, updatable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to VenPromotion
    @ManyToOne
	@JoinColumn(name="promotion_id", nullable=false, insertable=false, updatable=false)
	private VenPromotion venPromotion;

	//bi-directional many-to-one association to VenPromotionShareCalcMethod
    @ManyToOne
	@JoinColumn(name="promotion_calc_method_id", nullable=false)
	private VenPromotionShareCalcMethod venPromotionShareCalcMethod;
    
    public VenPartyPromotionShare() {
    }

	public VenPartyPromotionSharePK getId() {
		return this.id;
	}

	public void setId(VenPartyPromotionSharePK id) {
		this.id = id;
	}
	
	public String getPromotionCalcValue() {
		return this.promotionCalcValue;
	}

	public void setPromotionCalcValue(String promotionCalcValue) {
		this.promotionCalcValue = promotionCalcValue;
	}

	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public VenPromotion getVenPromotion() {
		return this.venPromotion;
	}

	public void setVenPromotion(VenPromotion venPromotion) {
		this.venPromotion = venPromotion;
	}
	
	public VenPromotionShareCalcMethod getVenPromotionShareCalcMethod() {
		return this.venPromotionShareCalcMethod;
	}

	public void setVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod) {
		this.venPromotionShareCalcMethod = venPromotionShareCalcMethod;
	}
}