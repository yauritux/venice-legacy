package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_user_permission database table.
 * 
 */
@Entity
@Table(name="raf_user_permission")
public class RafUserPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RafUserPermissionPK id;

	//bi-directional many-to-one association to RafApplicationObject
    @ManyToOne
	@JoinColumn(name="application_object_id", nullable=false, insertable=false, updatable=false)
	private RafApplicationObject rafApplicationObject;

	//bi-directional many-to-one association to RafPermissionType
    @ManyToOne
	@JoinColumn(name="permission_type_id", nullable=false, insertable=false, updatable=false)
	private RafPermissionType rafPermissionType;

	//bi-directional many-to-one association to RafUser
    @ManyToOne
	@JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
	private RafUser rafUser;

    public RafUserPermission() {
    }

	public RafUserPermissionPK getId() {
		return this.id;
	}

	public void setId(RafUserPermissionPK id) {
		this.id = id;
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
	
	public RafUser getRafUser() {
		return this.rafUser;
	}

	public void setRafUser(RafUser rafUser) {
		this.rafUser = rafUser;
	}
	
}