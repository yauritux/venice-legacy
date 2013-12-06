package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_party_promotion_share database table.
 * 
 */
@Embeddable
public class VenPartyPromotionSharePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="party_id", unique=true, nullable=false)
	private Long partyId;

	@Column(name="promotion_id", unique=true, nullable=false)
	private Long promotionId;

    public VenPartyPromotionSharePK() {
    }
	public Long getPartyId() {
		return this.partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public Long getPromotionId() {
		return this.promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VenPartyPromotionSharePK)) {
			return false;
		}
		VenPartyPromotionSharePK castOther = (VenPartyPromotionSharePK)other;
		return 
			this.partyId.equals(castOther.partyId)
			&& this.promotionId.equals(castOther.promotionId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partyId.hashCode();
		hash = hash * prime + this.promotionId.hashCode();
		
		return hash;
    }
}