package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_profile_permission database table.
 * 
 */
@Entity
@Table(name="raf_profile_permission")
public class RafProfilePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_profile_permission")  
	@TableGenerator(name="raf_profile_permission", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="raf_profile_permission_id")
	private Long rafProfilePermissionId;

	//bi-directional many-to-one association to RafApplicationObject
    @ManyToOne
	@JoinColumn(name="application_object_id")
	private RafApplicationObject rafApplicationObject;

	//bi-directional many-to-one association to RafPermissionType
    @ManyToOne
	@JoinColumn(name="permission_type_id")
	private RafPermissionType rafPermissionType;

	//bi-directional many-to-one association to RafProfile
    @ManyToOne
	@JoinColumn(name="profile_id")
	private RafProfile rafProfile;

    public RafProfilePermission() {
    }

	public Long getRafProfilePermissionId() {
		return this.rafProfilePermissionId;
	}

	public void setRafProfilePermissionId(Long rafProfilePermissionId) {
		this.rafProfilePermissionId = rafProfilePermissionId;
	}

	public RafApplicationObject getRafApplicationObject() {
		return this.rafApplicationObject;
	}

	public void setRafApplicationObject(RafApplicationObject rafApplicationObject) {
		this.rafApplicationObject = rafApplicationObject;
	}
	
	public RafPermissionType getRafPermissionType() {
		return this.rafPermissionType;
	}

	public void setRafPermissionType(RafPermissionType rafPermissionType) {
		this.rafPermissionType = rafPermissionType;
	}
	
	public RafProfile getRafProfile() {
		return this.rafProfile;
	}

	public void setRafProfile(RafProfile rafProfile) {
		this.rafProfile = rafProfile;
	}
	
}