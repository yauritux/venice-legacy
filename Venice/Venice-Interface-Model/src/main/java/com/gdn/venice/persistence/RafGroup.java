package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_group database table.
 * 
 */
@Entity
@Table(name="raf_group")
public class RafGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_group")  
	@TableGenerator(name="raf_group", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="group_id")
	private Long groupId;

	@Column(name="group_desc", nullable=false, length=200)
	private String groupDesc;

	@Column(name="group_name", nullable=false, length=100)
	private String groupName;

	//bi-directional many-to-one association to RafUserGroupMembership
	@OneToMany(mappedBy="rafGroup")
	private List<RafUserGroupMembership> rafUserGroupMemberships;

    public RafGroup() {
    }

	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupDesc() {
		return this.groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<RafUserGroupMembership> getRafUserGroupMemberships() {
		return this.rafUserGroupMemberships;
	}

	public void setRafUserGroupMemberships(List<RafUserGroupMembership> rafUserGroupMemberships) {
		this.rafUserGroupMemberships = rafUserGroupMemberships;
	}
	
}