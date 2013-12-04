package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_item_address database table.
 * 
 */
@Entity
@Table(name="ven_order_item_address")
public class VenOrderItemAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_item_address")  
	@TableGenerator(name="ven_order_item_address", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_item_address_id")
	private Long orderItemAddressId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="address_id", nullable=false)
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="order_item_id", nullable=false)
	private VenOrderItem venOrderItem;

    public VenOrderItemAddress() {
    }

	public Long getOrderItemAddressId() {
		return this.orderItemAddressId;
	}

	public void setOrderItemAddressId(Long orderItemAddressId) {
		this.orderItemAddressId = orderItemAddressId;
	}

	public VenAddress getVenAddress() {
		return venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}

	public VenOrderItem getVenOrderItem() {
		return venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}
	
}