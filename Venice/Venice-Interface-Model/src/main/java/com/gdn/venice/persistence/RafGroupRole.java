package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_group_role database table.
 * 
 */
@Entity
@Table(name="raf_group_role")
public class RafGroupRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_group_role")  
	@TableGenerator(name="raf_group_role", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="raf_group_role_id")
	private Long rafGroupRoleId;

	//bi-directional many-to-one association to RafGroup
    @ManyToOne
	@JoinColumn(name="group_id")
	private RafGroup rafGroup;

	//bi-directional many-to-one association to RafRole
    @ManyToOne
	@JoinColumn(name="role_id")
	private RafRole rafRole;

    public RafGroupRole() {
    }

	public Long getRafGroupRoleId() {
		return this.rafGroupRoleId;
	}

	public void setRafGroupRoleId(Long rafGroupRoleId) {
		this.rafGroupRoleId = rafGroupRoleId;
	}

	public RafGroup getRafGroup() {
		return this.rafGroup;
	}

	public void setRafGroup(RafGroup rafGroup) {
		this.rafGroup = rafGroup;
	}
	
	public RafRole getRafRole() {
		return this.rafRole;
	}

	public void setRafRole(RafRole rafRole) {
		this.rafRole = rafRole;
	}
	
}