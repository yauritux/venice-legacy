package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the raf_permission_type database table.
 * 
 */
@Entity
@Table(name="raf_permission_type")
public class RafPermissionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_permission_type")  
	@TableGenerator(name="raf_permission_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="permission_type_id")
	private Long permissionTypeId;

	@Column(name="permission_type_desc", nullable=false, length=100)
	private String permissionTypeDesc;

	//bi-directional many-to-one association to RafProfilePermission
	@OneToMany(mappedBy="rafPermissionType")
	private List<RafProfilePermission> rafProfilePermissions;

    public RafPermissionType() {
    }

	public Long getPermissionTypeId() {
		return this.permissionTypeId;
	}

	public void setPermissionTypeId(Long permissionTypeId) {
		this.permissionTypeId = permissionTypeId;
	}

	public String getPermissionTypeDesc() {
		return this.permissionTypeDesc;
	}

	public void setPermissionTypeDesc(String permissionTypeDesc) {
		this.permissionTypeDesc = permissionTypeDesc;
	}

	public List<RafProfilePermission> getRafProfilePermissions() {
		return this.rafProfilePermissions;
	}

	public void setRafProfilePermissions(List<RafProfilePermission> rafProfilePermissions) {
		this.rafProfilePermissions = rafProfilePermissions;
	}
	
}