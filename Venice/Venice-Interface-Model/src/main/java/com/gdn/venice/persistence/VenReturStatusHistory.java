package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_retur_status_history database table.
 * 
 */
@Entity
@Table(name="ven_retur_status_history")
public class VenReturStatusHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VenReturStatusHistoryPK id;

	//bi-directional many-to-one association to VenRetur
    @ManyToOne
	@JoinColumn(name="retur_id", nullable=false, insertable=false, updatable=false)
	private VenRetur venRetur;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="retur_status_id", nullable=false)
	private VenOrderStatus venReturStatus;

	@Column(name="status_change_reason")
	private String statusChangeReason;

    public VenReturStatusHistory() {
    }

	public VenReturStatusHistoryPK getId() {
		return this.id;
	}

	public void setId(VenReturStatusHistoryPK id) {
		this.id = id;
	}
	
	public VenRetur getVenRetur() {
		return this.venRetur;
	}

	public void setVenRetur(VenRetur venRetur) {
		this.venRetur = venRetur;
	}

	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}
	
	public VenOrderStatus getVenReturStatus() {
		return this.venReturStatus;
	}

	public void setVenReturStatus(VenOrderStatus venReturStatus) {
		this.venReturStatus = venReturStatus;
	}

}