package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_address database table.
 * 
 */
@Entity
@Table(name="ven_order_address")
public class VenOrderAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_address")  
	@TableGenerator(name="ven_order_address", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_address_id")
	private Long orderAddressId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="order_id", nullable=false)
	private VenOrder venOrder;
	
    public VenOrderAddress() {
    }

	public Long getOrderAddressId() {
		return this.orderAddressId;
	}

	public void setOrderAddressId(Long orderAddressId) {
		this.orderAddressId = orderAddressId;
	}

	public VenAddress getVenAddress() {
		return venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}

	public VenOrder getVenOrder() {
		return venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
}