package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_retur_address database table.
 * 
 */
@Entity
@Table(name="ven_retur_address")
public class VenReturAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur_address")  
	@TableGenerator(name="ven_retur_address", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_address_id")
	private Long returAddressId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="retur_id", nullable=false)
	private VenRetur venRetur;
	
    public VenReturAddress() {
    }

	public Long getReturAddressId() {
		return this.returAddressId;
	}

	public void setReturAddressId(Long returAddressId) {
		this.returAddressId = returAddressId;
	}

	public VenAddress getVenAddress() {
		return venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}

	public VenRetur getVenRetur() {
		return venRetur;
	}

	public void setVenRetur(VenRetur venRetur) {
		this.venRetur = venRetur;
	}
	

}