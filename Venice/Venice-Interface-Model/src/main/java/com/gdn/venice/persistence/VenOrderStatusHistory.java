package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_order_status_history database table.
 * 
 */
@Entity
@Table(name="ven_order_status_history")
public class VenOrderStatusHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenOrderStatusHistoryPK id;

	@Column(name="status_change_reason", nullable=false, length=1000)
	private String statusChangeReason;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false, insertable=false, updatable=false)
	private VenOrder venOrder;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="order_status_id", nullable=false)
	private VenOrderStatus venOrderStatus;

    public VenOrderStatusHistory() {
    }

	public VenOrderStatusHistoryPK getId() {
		return this.id;
	}

	public void setId(VenOrderStatusHistoryPK id) {
		this.id = id;
	}
	
	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
	public VenOrderStatus getVenOrderStatus() {
		return this.venOrderStatus;
	}

	public void setVenOrderStatus(VenOrderStatus venOrderStatus) {
		this.venOrderStatus = venOrderStatus;
	}
	
}