package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_user_role database table.
 * 
 */
@Entity
@Table(name="raf_user_role")
public class RafUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_user_role")  
	@TableGenerator(name="raf_user_role", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="raf_user_role_id")
	private Long rafUserRoleId;

	//bi-directional many-to-one association to RafRole
    @ManyToOne
	@JoinColumn(name="role_id")
	private RafRole rafRole;

	//bi-directional many-to-one association to RafUser
    @ManyToOne
	@JoinColumn(name="user_id")
	private RafUser rafUser;

    public RafUserRole() {
    }

	public Long getRafUserRoleId() {
		return this.rafUserRoleId;
	}

	public void setRafUserRoleId(Long rafUserRoleId) {
		this.rafUserRoleId = rafUserRoleId;
	}

	public RafRole getRafRole() {
		return this.rafRole;
	}

	public void setRafRole(RafRole rafRole) {
		this.rafRole = rafRole;
	}
	
	public RafUser getRafUser() {
		return this.rafUser;
	}

	public void setRafUser(RafUser rafUser) {
		this.rafUser = rafUser;
	}
	
}