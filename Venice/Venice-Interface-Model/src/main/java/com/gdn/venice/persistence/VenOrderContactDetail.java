package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_contact_detail database table.
 * 
 */
@Entity
@Table(name="ven_order_contact_detail")
public class VenOrderContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_contact_detail")  
	@TableGenerator(name="ven_order_contact_detail", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_contact_detail_id")
	private Long orderContactDetailId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="contact_detail_id", nullable=false)
	private VenContactDetail venContactDetail;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="order_id", nullable=false)
	private VenOrder venOrder;

    public VenOrderContactDetail() {
    }

	public Long getOrderContactDetailId() {
		return this.orderContactDetailId;
	}

	public void setOrderContactDetailId(Long orderContactDetailId) {
		this.orderContactDetailId = orderContactDetailId;
	}

	public VenContactDetail getVenContactDetail() {
		return venContactDetail;
	}

	public void setVenContactDetail(VenContactDetail venContactDetail) {
		this.venContactDetail = venContactDetail;
	}

	public VenOrder getVenOrder() {
		return venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}

}