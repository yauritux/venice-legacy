package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the raf_user_permission database table.
 * 
 */
@Embeddable
public class RafUserPermissionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", unique=true, nullable=false)
	private Long userId;

	@Column(name="permission_type_id", unique=true, nullable=false)
	private Long permissionTypeId;

	@Column(name="application_object_id", unique=true, nullable=false)
	private Long applicationObjectId;

    public RafUserPermissionPK() {
    }
	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getPermissionTypeId() {
		return this.permissionTypeId;
	}
	public void setPermissionTypeId(Long permissionTypeId) {
		this.permissionTypeId = permissionTypeId;
	}
	public Long getApplicationObjectId() {
		return this.applicationObjectId;
	}
	public void setApplicationObjectId(Long applicationObjectId) {
		this.applicationObjectId = applicationObjectId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RafUserPermissionPK)) {
			return false;
		}
		RafUserPermissionPK castOther = (RafUserPermissionPK)other;
		return 
			this.userId.equals(castOther.userId)
			&& this.permissionTypeId.equals(castOther.permissionTypeId)
			&& this.applicationObjectId.equals(castOther.applicationObjectId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.permissionTypeId.hashCode();
		hash = hash * prime + this.applicationObjectId.hashCode();
		
		return hash;
    }
}