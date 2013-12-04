package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_card_type database table.
 * 
 */
@Entity
@Table(name="ven_card_type")
public class VenCardType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_card_type")  
	@TableGenerator(name="ven_card_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="card_type_id", unique=true, nullable=false)
	private Long cardTypeId;

	@Column(name="card_type_desc", nullable=false, length=100)
	private String cardTypeDesc;

	//bi-directional many-to-one association to VenBinCreditLimitEstimate
	@OneToMany(mappedBy="venCardType")
	private List<VenBinCreditLimitEstimate> venBinCreditLimitEstimates;

    public VenCardType() {
    }

	public Long getCardTypeId() {
		return this.cardTypeId;
	}

	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardTypeDesc() {
		return this.cardTypeDesc;
	}

	public void setCardTypeDesc(String cardTypeDesc) {
		this.cardTypeDesc = cardTypeDesc;
	}

	public List<VenBinCreditLimitEstimate> getVenBinCreditLimitEstimates() {
		return this.venBinCreditLimitEstimates;
	}

	public void setVenBinCreditLimitEstimates(List<VenBinCreditLimitEstimate> venBinCreditLimitEstimates) {
		this.venBinCreditLimitEstimates = venBinCreditLimitEstimates;
	}
	
}