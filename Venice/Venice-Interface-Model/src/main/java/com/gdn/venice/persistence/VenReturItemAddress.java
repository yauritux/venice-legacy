package com.gdn.venice.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the ven_retur_item_address database table.
 * 
 */
@Entity
@Table(name="ven_retur_item_address")
public class VenReturItemAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur_item_address")  
	@TableGenerator(name="ven_retur_item_address", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_item_address_id")
	private Long returItemAddressId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="retur_item_id", nullable=false)
	private VenReturItem venReturItem;

    public VenReturItemAddress() {
    }

	public Long getReturItemAddressId() {
		return this.returItemAddressId;
	}

	public void setReturItemAddressId(Long returItemAddressId) {
		this.returItemAddressId = returItemAddressId;
	}

	public VenAddress getVenAddress() {
		return venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}

	public VenReturItem getVenReturItem() {
		return venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}
}