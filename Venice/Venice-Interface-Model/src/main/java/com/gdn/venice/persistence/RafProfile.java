package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_profile database table.
 * 
 */
@Entity
@Table(name="raf_profile")
public class RafProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_profile")  
	@TableGenerator(name="raf_profile", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="profile_id")
	private Long profileId;

	@Column(name="profile_desc", nullable=false, length=300)
	private String profileDesc;

	@Column(name="profile_name", nullable=false, length=100)
	private String profileName;

	//bi-directional many-to-one association to RafProfilePermission
	@OneToMany(mappedBy="rafProfile")
	private List<RafProfilePermission> rafProfilePermissions;

    public RafProfile() {
    }

	public Long getProfileId() {
		return this.profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getProfileDesc() {
		return this.profileDesc;
	}

	public void setProfileDesc(String profileDesc) {
		this.profileDesc = profileDesc;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public List<RafProfilePermission> getRafProfilePermissions() {
		return this.rafProfilePermissions;
	}

	public void setRafProfilePermissions(List<RafProfilePermission> rafProfilePermissions) {
		this.rafProfilePermissions = rafProfilePermissions;
	}
	
}