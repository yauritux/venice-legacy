package com.gdn.venice.persistence;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the frd_entity_blacklist database table.
 * 
 */
@Entity
@Table(name="frd_entity_blacklist")
public class FrdEntityBlacklist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_entity_blacklist")  
	@TableGenerator(name="frd_entity_blacklist", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="entity_blacklist_id", unique=true, nullable=false)
	private Long entityBlacklistId;

	@Column(name="black_or_white_list", nullable=false, length=100)
	private String blackOrWhiteList;

	@Column(name="blacklist_string", nullable=false, length=200)
	private String blacklistString;
	
	@Column(name="description", nullable=false, length=1000)
	private String description;
	
	@Column(name="created_by", nullable=false, length=100)
	private String createdBy;

	@Column(name="blacklist_timestamp", nullable=false)
	private Timestamp blacklistTimestamp;

    public FrdEntityBlacklist() {
    }

	public Long getEntityBlacklistId() {
		return this.entityBlacklistId;
	}

	public void setEntityBlacklistId(Long entityBlacklistId) {
		this.entityBlacklistId = entityBlacklistId;
	}

	public String getBlackOrWhiteList() {
		return this.blackOrWhiteList;
	}

	public void setBlackOrWhiteList(String blackOrWhiteList) {
		this.blackOrWhiteList = blackOrWhiteList;
	}

	public String getBlacklistString() {
		return this.blacklistString;
	}

	public void setBlacklistString(String blacklistString) {
		this.blacklistString = blacklistString;
	}

	public Timestamp getBlacklistTimestamp() {
		return this.blacklistTimestamp;
	}

	public void setBlacklistTimestamp(Timestamp blacklistTimestamp) {
		this.blacklistTimestamp = blacklistTimestamp;
	}

	public String getCreatedby() {
		return this.createdBy;
	}

	public void setCreatedby(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}