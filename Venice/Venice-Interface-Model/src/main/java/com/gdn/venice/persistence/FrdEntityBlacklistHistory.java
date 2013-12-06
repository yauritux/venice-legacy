package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the frd_entity_blacklist_history database table.
 * 
 */
@Entity
@Table(name="frd_entity_blacklist_history")
public class FrdEntityBlacklistHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_entity_blacklist_history")  
	@TableGenerator(name="frd_entity_blacklist_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="entity_blacklist_history_id", unique=true, nullable=false)
	private Long entityBlacklistHistoryId;

	@Column(name="black_list_occurence", nullable=false)
	private Integer blackListOccurence;

	@Column(name="blacklist_source_id", nullable=false)
	private Long blacklistSourceId;

	@Column(name="blacklisted_entity_type", nullable=false)
	private Integer blacklistedEntityType;

	@Column(name="grey_flag", nullable=false)
	private Boolean greyFlag;

	@Column(name="grey_list_fraud_points_value", nullable=false)
	private Integer greyListFraudPointsValue;

	@Column(name="history_timestamp", nullable=false)
	private Timestamp historyTimestamp;

	@Column(name="partial_match_flag", nullable=false)
	private Boolean partialMatchFlag;

	//bi-directional many-to-one association to VenMasterChangeType
    @ManyToOne
	@JoinColumn(name="master_change_id", nullable=false)
	private VenMasterChangeType venMasterChangeType;

    public FrdEntityBlacklistHistory() {
    }

	public Long getEntityBlacklistHistoryId() {
		return this.entityBlacklistHistoryId;
	}

	public void setEntityBlacklistHistoryId(Long entityBlacklistHistoryId) {
		this.entityBlacklistHistoryId = entityBlacklistHistoryId;
	}

	public Integer getBlackListOccurence() {
		return this.blackListOccurence;
	}

	public void setBlackListOccurence(Integer blackListOccurence) {
		this.blackListOccurence = blackListOccurence;
	}

	public Long getBlacklistSourceId() {
		return this.blacklistSourceId;
	}

	public void setBlacklistSourceId(Long blacklistSourceId) {
		this.blacklistSourceId = blacklistSourceId;
	}

	public Integer getBlacklistedEntityType() {
		return this.blacklistedEntityType;
	}

	public void setBlacklistedEntityType(Integer blacklistedEntityType) {
		this.blacklistedEntityType = blacklistedEntityType;
	}

	public Boolean getGreyFlag() {
		return this.greyFlag;
	}

	public void setGreyFlag(Boolean greyFlag) {
		this.greyFlag = greyFlag;
	}

	public Integer getGreyListFraudPointsValue() {
		return this.greyListFraudPointsValue;
	}

	public void setGreyListFraudPointsValue(Integer greyListFraudPointsValue) {
		this.greyListFraudPointsValue = greyListFraudPointsValue;
	}

	public Timestamp getHistoryTimestamp() {
		return this.historyTimestamp;
	}

	public void setHistoryTimestamp(Timestamp historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public Boolean getPartialMatchFlag() {
		return this.partialMatchFlag;
	}

	public void setPartialMatchFlag(Boolean partialMatchFlag) {
		this.partialMatchFlag = partialMatchFlag;
	}

	public VenMasterChangeType getVenMasterChangeType() {
		return this.venMasterChangeType;
	}

	public void setVenMasterChangeType(VenMasterChangeType venMasterChangeType) {
		this.venMasterChangeType = venMasterChangeType;
	}
	
}