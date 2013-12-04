package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_item_contact_detail database table.
 * 
 */
@Entity
@Table(name="ven_order_item_contact_detail")
public class VenOrderItemContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_item_contact_detail")  
	@TableGenerator(name="ven_order_item_contact_detail", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_item_contact_detail_id")
	private Long orderItemContactDetailId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="contact_detail_id", nullable=false)
	private VenContactDetail venContactDetail;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="order_item_id", nullable=false)
	private VenOrderItem venOrderItem;

    public VenOrderItemContactDetail() {
    }

	public Long getOrderItemContactDetailId() {
		return this.orderItemContactDetailId;
	}

	public void setOrderItemContactDetailId(Long orderItemContactDetailId) {
		this.orderItemContactDetailId = orderItemContactDetailId;
	}

	public VenContactDetail getVenContactDetail() {
		return venContactDetail;
	}

	public void setVenContactDetail(VenContactDetail venContactDetail) {
		this.venContactDetail = venContactDetail;
	}

	public VenOrderItem getVenOrderItem() {
		return venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}

}