package com.gdn.venice.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the ven_retur_contact_detail database table.
 * 
 */
@Entity
@Table(name="ven_retur_contact_detail")
public class VenReturContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur_contact_detail")  
	@TableGenerator(name="ven_retur_contact_detail", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_contact_detail_id")
	private Long returContactDetailId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="contact_detail_id", nullable=false)
	private VenContactDetail venContactDetail;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="retur_id", nullable=false)
	private VenRetur venRetur;

    public VenReturContactDetail() {
    }

	public Long getReturContactDetailId() {
		return this.returContactDetailId;
	}

	public void setReturContactDetailId(Long returContactDetailId) {
		this.returContactDetailId = returContactDetailId;
	}

	public VenContactDetail getVenContactDetail() {
		return venContactDetail;
	}

	public void setVenContactDetail(VenContactDetail venContactDetail) {
		this.venContactDetail = venContactDetail;
	}

	public VenRetur getVenRetur() {
		return venRetur;
	}

	public void setVenRetur(VenRetur venRetur) {
		this.venRetur = venRetur;
	}
}