package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_user_group_membership database table.
 * 
 */
@Entity
@Table(name="raf_user_group_membership")
public class RafUserGroupMembership implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_user_group_membership")  
	@TableGenerator(name="raf_user_group_membership", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="raf_user_group_membership_id")
	private Long rafUserGroupMembershipId;

	//bi-directional many-to-one association to RafGroup
    @ManyToOne
	@JoinColumn(name="group_id")
	private RafGroup rafGroup;

	//bi-directional many-to-one association to RafUser
    @ManyToOne
	@JoinColumn(name="user_id")
	private RafUser rafUser;

    public RafUserGroupMembership() {
    }

	public Long getRafUserGroupMembershipId() {
		return this.rafUserGroupMembershipId;
	}

	public void setRafUserGroupMembershipId(Long rafUserGroupMembershipId) {
		this.rafUserGroupMembershipId = rafUserGroupMembershipId;
	}

	public RafGroup getRafGroup() {
		return this.rafGroup;
	}

	public void setRafGroup(RafGroup rafGroup) {
		this.rafGroup = rafGroup;
	}
	
	public RafUser getRafUser() {
		return this.rafUser;
	}

	public void setRafUser(RafUser rafUser) {
		this.rafUser = rafUser;
	}
	
}