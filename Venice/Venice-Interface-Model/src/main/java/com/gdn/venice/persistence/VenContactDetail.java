package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ven_contact_detail database table.
 * 
 */
@Entity
@Table(name="ven_contact_detail")
public class VenContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_contact_detail")  
	@TableGenerator(name="ven_contact_detail", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="contact_detail_id", unique=true, nullable=false)
	private Long contactDetailId;

	@Column(name="contact_detail", nullable=false, length=200)
	private String contactDetail;

	//bi-directional many-to-one association to VenContactDetailType
    @ManyToOne
	@JoinColumn(name="contact_detail_type_id", nullable=false)
	private VenContactDetailType venContactDetailType;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

    public VenContactDetail() {
    }

	public Long getContactDetailId() {
		return this.contactDetailId;
	}

	public void setContactDetailId(Long contactDetailId) {
		this.contactDetailId = contactDetailId;
	}

	public String getContactDetail() {
		return this.contactDetail;
	}

	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}

	public VenContactDetailType getVenContactDetailType() {
		return this.venContactDetailType;
	}

	public void setVenContactDetailType(VenContactDetailType venContactDetailType) {
		this.venContactDetailType = venContactDetailType;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}