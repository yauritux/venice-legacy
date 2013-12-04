package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_settlement_record database table.
 * 
 */
@Entity
@Table(name="ven_settlement_record")
public class VenSettlementRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_settlement_record")  
	@TableGenerator(name="ven_settlement_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="settlement_record_id", unique=true, nullable=false)
	private Long settlementRecordId;

	@Column(name="commission_type", nullable=false, length=100)
	private String commissionType;

	@Column(name="commission_value", nullable=false, precision=20, scale=2)
	private BigDecimal commissionValue;

	@Column(name="settlement_code", nullable=false, length=100)
	private String settlementCode;

	@Column(name="settlement_record_timestamp", nullable=false)
	private Timestamp settlementRecordTimestamp;

	@Column(name="settlement_record_type", nullable=false, length=100)
	private String settlementRecordType;
	
	@Column(name="pph23")
	private Boolean pph23;

	//bi-directional many-to-one association to VenOrderItem
    @ManyToOne
	@JoinColumn(name="order_item_id")
	private VenOrderItem venOrderItem;
    
	//bi-directional many-to-one association to VenReturItem
    @ManyToOne
	@JoinColumn(name="retur_item_id")
	private VenReturItem venReturItem;
    
    public VenSettlementRecord() {
    }

	public Long getSettlementRecordId() {
		return this.settlementRecordId;
	}

	public void setSettlementRecordId(Long settlementRecordId) {
		this.settlementRecordId = settlementRecordId;
	}

	public String getCommissionType() {
		return this.commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public BigDecimal getCommissionValue() {
		return this.commissionValue;
	}

	public void setCommissionValue(BigDecimal commissionValue) {
		this.commissionValue = commissionValue;
	}

	public String getSettlementCode() {
		return this.settlementCode;
	}

	public void setSettlementCode(String settlementCode) {
		this.settlementCode = settlementCode;
	}

	public Timestamp getSettlementRecordTimestamp() {
		return this.settlementRecordTimestamp;
	}

	public void setSettlementRecordTimestamp(Timestamp settlementRecordTimestamp) {
		this.settlementRecordTimestamp = settlementRecordTimestamp;
	}

	public String getSettlementRecordType() {
		return this.settlementRecordType;
	}

	public void setSettlementRecordType(String settlementRecordType) {
		this.settlementRecordType = settlementRecordType;
	}

	public VenOrderItem getVenOrderItem() {
		return this.venOrderItem;
	}

	public void setVenOrderItem(VenOrderItem venOrderItem) {
		this.venOrderItem = venOrderItem;
	}

	public void setPph23(Boolean pph23) {
		this.pph23 = pph23;
	}

	public Boolean getPph23() {
		return pph23;
	}

	public VenReturItem getVenReturItem() {
		return venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}
}