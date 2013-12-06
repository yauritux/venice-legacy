package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_address_type database table.
 * 
 */
@Entity
@Table(name="ven_address_type")
public class VenAddressType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_address_type")  
	@TableGenerator(name="ven_address_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="address_type_id", unique=true, nullable=false)
	private Long addressTypeId;

	@Column(name="address_type_desc", nullable=false, length=100)
	private String addressTypeDesc;

	//bi-directional many-to-one association to VenPartyAddress
	@OneToMany(mappedBy="venAddressType")
	private List<VenPartyAddress> venPartyAddresses;

    public VenAddressType() {
    }

	public Long getAddressTypeId() {
		return this.addressTypeId;
	}

	public void setAddressTypeId(Long addressTypeId) {
		this.addressTypeId = addressTypeId;
	}

	public String getAddressTypeDesc() {
		return this.addressTypeDesc;
	}

	public void setAddressTypeDesc(String addressTypeDesc) {
		this.addressTypeDesc = addressTypeDesc;
	}

	public List<VenPartyAddress> getVenPartyAddresses() {
		return this.venPartyAddresses;
	}

	public void setVenPartyAddresses(List<VenPartyAddress> venPartyAddresses) {
		this.venPartyAddresses = venPartyAddresses;
	}
	
}