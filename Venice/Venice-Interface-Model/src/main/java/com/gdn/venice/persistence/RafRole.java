package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_role database table.
 * 
 */
@Entity
@Table(name="raf_role")
public class RafRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_role")  
	@TableGenerator(name="raf_role", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="raf_role_seq")
	//@SequenceGenerator(name="raf_role_seq", sequenceName="raf_role_seq")//,allocationSize=1)
@Column(name="role_id")
	private Long roleId;

	@Column(name="role_desc", nullable=false, length=200)
	private String roleDesc;

	@Column(name="role_name", nullable=false, length=100)
	private String roleName;

	//bi-directional many-to-one association to RafRole
    @ManyToOne
	@JoinColumn(name="parent_role_id")
	private RafRole rafRole;

	//bi-directional many-to-one association to RafRole
	@OneToMany(mappedBy="rafRole")
	private List<RafRole> rafRoles;

	//bi-directional many-to-one association to RafRoleProfile
	@OneToMany(mappedBy="rafRole")
	private List<RafRoleProfile> rafRoleProfiles;

    public RafRole() {
    }

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public RafRole getRafRole() {
		return this.rafRole;
	}

	public void setRafRole(RafRole rafRole) {
		this.rafRole = rafRole;
	}
	
	public List<RafRole> getRafRoles() {
		return this.rafRoles;
	}

	public void setRafRoles(List<RafRole> rafRoles) {
		this.rafRoles = rafRoles;
	}
	
	public List<RafRoleProfile> getRafRoleProfiles() {
		return this.rafRoleProfiles;
	}

	public void setRafRoleProfiles(List<RafRoleProfile> rafRoleProfiles) {
		this.rafRoleProfiles = rafRoleProfiles;
	}
	
}