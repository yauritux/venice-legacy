package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_item_status_history database table.
 * 
 */
@Entity
@Table(name="ven_order_item_status_history")
public class VenOrderItemStatusHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenOrderItemStatusHistoryPK id;

	@Column(name="status_change_reason", nullable=false, length=1000)
	private String statusChangeReason;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id", nullable=false, insertable=false, updatable=false)
	private VenOrderItem venOrderItem;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="order_status_id", nullable=false)
	private VenOrderStatus venOrderStatus;

    public VenOrderItemStatusHistory() {
    }

	public VenOrderItemStatusHistoryPK getId() {
		return this.id;
	}

	public void setId(VenOrderItemStatusHistoryPK id) {
		this.id = id;
	}
	
	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}
	
	public VenOrderStatus getVenOrderStatus() {
		return this.venOrderStatus;
	}

	public void setVenOrderStatus(VenOrderStatus venOrderStatus) {
		this.venOrderStatus = venOrderStatus;
	}
	
}