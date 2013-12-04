package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_party_type database table.
 * 
 */
@Entity
@Table(name="ven_party_type")
public class VenPartyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_party_type")  
	@TableGenerator(name="ven_party_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="party_type_id", unique=true, nullable=false)
	private Long partyTypeId;

	@Column(name="party_type_desc", nullable=false, length=100)
	private String partyTypeDesc;

	//bi-directional many-to-one association to VenParty
	@OneToMany(mappedBy="venPartyType")
	private List<VenParty> venParties;

    public VenPartyType() {
    }

	public Long getPartyTypeId() {
		return this.partyTypeId;
	}

	public void setPartyTypeId(Long partyTypeId) {
		this.partyTypeId = partyTypeId;
	}

	public String getPartyTypeDesc() {
		return this.partyTypeDesc;
	}

	public void setPartyTypeDesc(String partyTypeDesc) {
		this.partyTypeDesc = partyTypeDesc;
	}

	public List<VenParty> getVenParties() {
		return this.venParties;
	}

	public void setVenParties(List<VenParty> venParties) {
		this.venParties = venParties;
	}
	
}