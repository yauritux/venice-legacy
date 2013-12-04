package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_retur_item_status_history database table.
 * 
 */
@Entity
@Table(name="ven_retur_item_status_history")
public class VenReturItemStatusHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenReturItemStatusHistoryPK id;

	@Column(name="status_change_reason")
	private String statusChangeReason;
	
	//bi-directional many-to-one association to VenReturItem
    @ManyToOne
	@JoinColumn(name="retur_item_id", nullable=false, insertable=false, updatable=false)
	private VenReturItem venReturItem;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="retur_status_id", nullable=false)
	private VenOrderStatus venReturStatus;

    public VenReturItemStatusHistory() {
    }

	public VenReturItemStatusHistoryPK getId() {
		return this.id;
	}

	public void setId(VenReturItemStatusHistoryPK id) {
		this.id = id;
	}

	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}
	
	public VenReturItem getVenReturItem() {
		return this.venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}
	
	public VenOrderStatus getVenReturStatus() {
		return this.venReturStatus;
	}

	public void setVenReturStatus(VenOrderStatus venReturStatus) {
		this.venReturStatus = venReturStatus;
	}

}