package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_recipient database table.
 * 
 */
@Entity
@Table(name="ven_recipient")
public class VenRecipient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_recipient")  
	@TableGenerator(name="ven_recipient", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="recipient_id", unique=true, nullable=false)
	private Long recipientId;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venRecipient")
	private List<VenOrderItem> venOrderItems;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

    public VenRecipient() {
    }

	public Long getRecipientId() {
		return this.recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public List<VenOrderItem> getVenOrderItems() {
		return this.venOrderItems;
	}

	public void setVenOrderItems(List<VenOrderItem> venOrderItems) {
		this.venOrderItems = venOrderItems;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}