package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ven_party_address database table.
 * 
 */
@Embeddable
public class VenPartyAddressPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="party_id", unique=true, nullable=false)
	private Long partyId;

	@Column(name="address_id", unique=true, nullable=false)
	private Long addressId;

	@Column(name="address_type_id", unique=true, nullable=false)
	private Long addressTypeId;

    public VenPartyAddressPK() {
    }
	public Long getPartyId() {
		return this.partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public Long getAddressId() {
		return this.addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getAddressTypeId() {
		return this.addressTypeId;
	}
	public void setAddressTypeId(Long addressTypeId) {
		this.addressTypeId = addressTypeId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VenPartyAddressPK)) {
			return false;
		}
		VenPartyAddressPK castOther = (VenPartyAddressPK)other;
		return 
			this.partyId.equals(castOther.partyId)
			&& this.addressId.equals(castOther.addressId)
			&& this.addressTypeId.equals(castOther.addressTypeId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partyId.hashCode();
		hash = hash * prime + this.addressId.hashCode();
		hash = hash * prime + this.addressTypeId.hashCode();
		
		return hash;
    }
}