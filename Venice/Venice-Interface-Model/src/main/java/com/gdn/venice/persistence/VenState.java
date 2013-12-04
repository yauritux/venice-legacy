package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_state database table.
 * 
 */
@Entity
@Table(name="ven_state")
public class VenState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_state")  
	@TableGenerator(name="ven_state", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="state_id", unique=true, nullable=false)
	private Long stateId;

	@Column(name="state_code", nullable=false, length=100)
	private String stateCode;

	@Column(name="state_name", nullable=false, length=100)
	private String stateName;

	//bi-directional many-to-one association to VenAddress
	@OneToMany(mappedBy="venState")
	private List<VenAddress> venAddresses;

    public VenState() {
    }

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<VenAddress> getVenAddresses() {
		return this.venAddresses;
	}

	public void setVenAddresses(List<VenAddress> venAddresses) {
		this.venAddresses = venAddresses;
	}
	
}