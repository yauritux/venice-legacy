package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_role_profile database table.
 * 
 */
@Entity
@Table(name="raf_role_profile")
public class RafRoleProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_role_profile")  
	@TableGenerator(name="raf_role_profile", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="raf_role_profile_id")
	private Long rafRoleProfileId;

	//bi-directional many-to-one association to RafProfile
    @ManyToOne
	@JoinColumn(name="profile_id")
	private RafProfile rafProfile;

	//bi-directional many-to-one association to RafRole
    @ManyToOne
	@JoinColumn(name="role_id")
	private RafRole rafRole;

    public RafRoleProfile() {
    }

	public Long getRafRoleProfileId() {
		return this.rafRoleProfileId;
	}

	public void setRafRoleProfileId(Long rafRoleProfileId) {
		this.rafRoleProfileId = rafRoleProfileId;
	}

	public RafProfile getRafProfile() {
		return this.rafProfile;
	}

	public void setRafProfile(RafProfile rafProfile) {
		this.rafProfile = rafProfile;
	}
	
	public RafRole getRafRole() {
		return this.rafRole;
	}

	public void setRafRole(RafRole rafRole) {
		this.rafRole = rafRole;
	}
	
}