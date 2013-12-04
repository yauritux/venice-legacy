package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_retur_item_contact_detail database table.
 * 
 */
@Entity
@Table(name="ven_retur_item_contact_detail")
public class VenReturItemContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur_item_contact_detail")  
	@TableGenerator(name="ven_retur_item_contact_detail", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_item_contact_detail_id")
	private Long returItemContactDetailId;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="contact_detail_id", nullable=false)
	private VenContactDetail venContactDetail;

	//bi-directional many-to-one association to VenAddress
	@ManyToOne
	@JoinColumn(name="retur_item_id", nullable=false)
	private VenReturItem venReturItem;


    public VenReturItemContactDetail() {
    }

	public Long getReturItemContactDetailId() {
		return this.returItemContactDetailId;
	}

	public void setReturItemContactDetailId(Long returItemContactDetailId) {
		this.returItemContactDetailId = returItemContactDetailId;
	}

	public VenContactDetail getVenContactDetail() {
		return venContactDetail;
	}

	public void setVenContactDetail(VenContactDetail venContactDetail) {
		this.venContactDetail = venContactDetail;
	}

	public VenReturItem getVenReturItem() {
		return venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}

}