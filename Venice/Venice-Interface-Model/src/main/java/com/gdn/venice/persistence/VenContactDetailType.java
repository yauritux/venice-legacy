package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_contact_detail_type database table.
 * 
 */
@Entity
@Table(name="ven_contact_detail_type")
public class VenContactDetailType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_contact_detail_type")  
	@TableGenerator(name="ven_contact_detail_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="contact_detail_type_id", unique=true, nullable=false)
	private Long contactDetailTypeId;

	@Column(name="contact_detail_type_desc", nullable=false, length=100)
	private String contactDetailTypeDesc;

	//bi-directional many-to-one association to VenContactDetail
	@OneToMany(mappedBy="venContactDetailType")
	private List<VenContactDetail> venContactDetails;

    public VenContactDetailType() {
    }

	public Long getContactDetailTypeId() {
		return this.contactDetailTypeId;
	}

	public void setContactDetailTypeId(Long contactDetailTypeId) {
		this.contactDetailTypeId = contactDetailTypeId;
	}

	public String getContactDetailTypeDesc() {
		return this.contactDetailTypeDesc;
	}

	public void setContactDetailTypeDesc(String contactDetailTypeDesc) {
		this.contactDetailTypeDesc = contactDetailTypeDesc;
	}

	public List<VenContactDetail> getVenContactDetails() {
		return this.venContactDetails;
	}

	public void setVenContactDetails(List<VenContactDetail> venContactDetails) {
		this.venContactDetails = venContactDetails;
	}
	
}