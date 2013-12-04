package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_blacklist_reason database table.
 * 
 */
@Entity
@Table(name="frd_blacklist_reason")
public class FrdBlacklistReason implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_blacklist_reason")  
	@TableGenerator(name="frd_blacklist_reason", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="blacklist_reason", length=1000, nullable=false)
	private String blacklistReason;

	@Column(name="order_id", nullable=false)
	private Long orderId;

	@Column(name="wcs_order_id", length=100, nullable=false)
	private String wcsOrderId;

    public FrdBlacklistReason() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBlacklistReason() {
		return this.blacklistReason;
	}

	public void setBlacklistReason(String blacklistReason) {
		this.blacklistReason = blacklistReason;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getWcsOrderId() {
		return this.wcsOrderId;
	}

	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

}