package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_party_address database table.
 * 
 */
@Entity
@Table(name="ven_party_address")
public class VenPartyAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenPartyAddressPK id;

	//bi-directional many-to-one association to VenAddress
    @ManyToOne
	@JoinColumn(name="address_id", nullable=false, insertable=false, updatable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenAddressType
    @ManyToOne
	@JoinColumn(name="address_type_id", nullable=false, insertable=false, updatable=false)
	private VenAddressType venAddressType;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false, insertable=false, updatable=false)
	private VenParty venParty;

    public VenPartyAddress() {
    }

	public VenPartyAddressPK getId() {
		return this.id;
	}

	public void setId(VenPartyAddressPK id) {
		this.id = id;
	}
	
	public VenAddress getVenAddress() {
		return this.venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}
	
	public VenAddressType getVenAddressType() {
		return this.venAddressType;
	}

	public void setVenAddressType(VenAddressType venAddressType) {
		this.venAddressType = venAddressType;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}