package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_order_status database table.
 * 
 */
@Entity
@Table(name="ven_order_status")
public class VenOrderStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_status")  
	@TableGenerator(name="ven_order_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_status_id", unique=true, nullable=false)
	private Long orderStatusId;

	@Column(name="order_status_code", nullable=false, length=5)
	private String orderStatusCode;

	@Column(name="order_status_short_desc", nullable=false, length=100)
	private String orderStatusShortDesc;

	//bi-directional many-to-one association to VenOrder
	@OneToMany(mappedBy="venOrderStatus")
	private List<VenOrder> venOrders;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrderStatus")
	private List<VenOrderItem> venOrderItems;

	//bi-directional many-to-one association to VenOrderItemStatusHistory
	@OneToMany(mappedBy="venOrderStatus")
	private List<VenOrderItemStatusHistory> venOrderItemStatusHistories;

	//bi-directional many-to-one association to VenOrderStatusHistory
	@OneToMany(mappedBy="venOrderStatus")
	private List<VenOrderStatusHistory> venOrderStatusHistories;

    public VenOrderStatus() {
    }

	public Long getOrderStatusId() {
		return this.orderStatusId;
	}

	public void setOrderStatusId(Long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getOrderStatusCode() {
		return this.orderStatusCode;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}

	public String getOrderStatusShortDesc() {
		return this.orderStatusShortDesc;
	}

	public void setOrderStatusShortDesc(String orderStatusShortDesc) {
		this.orderStatusShortDesc = orderStatusShortDesc;
	}

	public List<VenOrder> getVenOrders() {
		return this.venOrders;
	}

	public void setVenOrders(List<VenOrder> venOrders) {
		this.venOrders = venOrders;
	}
	
	public List<VenOrderItem> getVenOrderItems() {
		return this.venOrderItems;
	}

	public void setVenOrderItems(List<VenOrderItem> venOrderItems) {
		this.venOrderItems = venOrderItems;
	}
	
	public List<VenOrderItemStatusHistory> getVenOrderItemStatusHistories() {
		return this.venOrderItemStatusHistories;
	}

	public void setVenOrderItemStatusHistories(List<VenOrderItemStatusHistory> venOrderItemStatusHistories) {
		this.venOrderItemStatusHistories = venOrderItemStatusHistories;
	}
	
	public List<VenOrderStatusHistory> getVenOrderStatusHistories() {
		return this.venOrderStatusHistories;
	}

	public void setVenOrderStatusHistories(List<VenOrderStatusHistory> venOrderStatusHistories) {
		this.venOrderStatusHistories = venOrderStatusHistories;
	}
	
}