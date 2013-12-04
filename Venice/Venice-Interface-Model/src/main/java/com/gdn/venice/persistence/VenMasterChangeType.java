package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_master_change_type database table.
 * 
 */
@Entity
@Table(name="ven_master_change_type")
public class VenMasterChangeType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_master_change_type")  
	@TableGenerator(name="ven_master_change_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="master_change_id", unique=true, nullable=false)
	private Long masterChangeId;

	@Column(name="master_change_desc", nullable=false, length=100)
	private String masterChangeDesc;

	//bi-directional many-to-one association to FrdEntityBlacklistHistory
	@OneToMany(mappedBy="venMasterChangeType")
	private List<FrdEntityBlacklistHistory> frdEntityBlacklistHistories;

	//bi-directional many-to-one association to LogAirwayBillHistory
	@OneToMany(mappedBy="venMasterChangeType")
	private List<LogAirwayBillHistory> logAirwayBillHistories;

	//bi-directional many-to-one association to VenOrderHistory
	@OneToMany(mappedBy="venMasterChangeType")
	private List<VenOrderHistory> venOrderHistories;

	//bi-directional many-to-one association to VenOrderItemHistory
	@OneToMany(mappedBy="venMasterChangeType")
	private List<VenOrderItemHistory> venOrderItemHistories;

	//bi-directional many-to-one association to VenOrderPaymentHistory
	@OneToMany(mappedBy="venMasterChangeType")
	private List<VenOrderPaymentHistory> venOrderPaymentHistories;

    public VenMasterChangeType() {
    }

	public Long getMasterChangeId() {
		return this.masterChangeId;
	}

	public void setMasterChangeId(Long masterChangeId) {
		this.masterChangeId = masterChangeId;
	}

	public String getMasterChangeDesc() {
		return this.masterChangeDesc;
	}

	public void setMasterChangeDesc(String masterChangeDesc) {
		this.masterChangeDesc = masterChangeDesc;
	}

	public List<FrdEntityBlacklistHistory> getFrdEntityBlacklistHistories() {
		return this.frdEntityBlacklistHistories;
	}

	public void setFrdEntityBlacklistHistories(List<FrdEntityBlacklistHistory> frdEntityBlacklistHistories) {
		this.frdEntityBlacklistHistories = frdEntityBlacklistHistories;
	}
	
	public List<LogAirwayBillHistory> getLogAirwayBillHistories() {
		return this.logAirwayBillHistories;
	}

	public void setLogAirwayBillHistories(List<LogAirwayBillHistory> logAirwayBillHistories) {
		this.logAirwayBillHistories = logAirwayBillHistories;
	}
	
	public List<VenOrderHistory> getVenOrderHistories() {
		return this.venOrderHistories;
	}

	public void setVenOrderHistories(List<VenOrderHistory> venOrderHistories) {
		this.venOrderHistories = venOrderHistories;
	}
	
	public List<VenOrderItemHistory> getVenOrderItemHistories() {
		return this.venOrderItemHistories;
	}

	public void setVenOrderItemHistories(List<VenOrderItemHistory> venOrderItemHistories) {
		this.venOrderItemHistories = venOrderItemHistories;
	}
	
	public List<VenOrderPaymentHistory> getVenOrderPaymentHistories() {
		return this.venOrderPaymentHistories;
	}

	public void setVenOrderPaymentHistories(List<VenOrderPaymentHistory> venOrderPaymentHistories) {
		this.venOrderPaymentHistories = venOrderPaymentHistories;
	}
	
}