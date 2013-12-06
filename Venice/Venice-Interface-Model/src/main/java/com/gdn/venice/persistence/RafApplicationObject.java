package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_application_object database table.
 * 
 */
@Entity
@Table(name="raf_application_object")
public class RafApplicationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_application_object")  
	@TableGenerator(name="raf_application_object", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="application_object_id")
	private Long applicationObjectId;

	@Column(name="application_object_canonical_name", nullable=false, length=1000)
	private String applicationObjectCanonicalName;

	@Column(name="application_object_uuid", nullable=false, length=36)
	private String applicationObjectUuid;

	//bi-directional many-to-one association to RafApplicationObject
    @ManyToOne
	@JoinColumn(name="parent_application_object_id")
	private RafApplicationObject rafApplicationObject;

	//bi-directional many-to-one association to RafApplicationObject
	@OneToMany(mappedBy="rafApplicationObject")
	private List<RafApplicationObject> rafApplicationObjects;

	//bi-directional many-to-one association to RafProfilePermission
	@OneToMany(mappedBy="rafApplicationObject")
	private List<RafProfilePermission> rafProfilePermissions;

	//bi-directional many-to-one association to RafApplicationObjectType
    @ManyToOne
	@JoinColumn(name="application_object_type_id")
	private RafApplicationObjectType rafApplicationObjectType;

    public RafApplicationObject() {
    }

	public Long getApplicationObjectId() {
		return this.applicationObjectId;
	}

	public void setApplicationObjectId(Long applicationObjectId) {
		this.applicationObjectId = applicationObjectId;
	}

	public String getApplicationObjectCanonicalName() {
		return this.applicationObjectCanonicalName;
	}

	public void setApplicationObjectCanonicalName(String applicationObjectCanonicalName) {
		this.applicationObjectCanonicalName = applicationObjectCanonicalName;
	}

	public String getApplicationObjectUuid() {
		return this.applicationObjectUuid;
	}

	public void setApplicationObjectUuid(String applicationObjectUuid) {
		this.applicationObjectUuid = applicationObjectUuid;
	}

	public RafApplicationObject getRafApplicationObject() {
		return this.rafApplicationObject;
	}

	public void setRafApplicationObject(RafApplicationObject rafApplicationObject) {
		this.rafApplicationObject = rafApplicationObject;
	}
	
	public List<RafApplicationObject> getRafApplicationObjects() {
		return this.rafApplicationObjects;
	}

	public void setRafApplicationObjects(List<RafApplicationObject> rafApplicationObjects) {
		this.rafApplicationObjects = rafApplicationObjects;
	}
	
	public List<RafProfilePermission> getRafProfilePermissions() {
		return this.rafProfilePermissions;
	}

	public void setRafProfilePermissions(List<RafProfilePermission> rafProfilePermissions) {
		this.rafProfilePermissions = rafProfilePermissions;
	}
	
	public RafApplicationObjectType getRafApplicationObjectType() {
		return this.rafApplicationObjectType;
	}

	public void setRafApplicationObjectType(RafApplicationObjectType rafApplicationObjectType) {
		this.rafApplicationObjectType = rafApplicationObjectType;
	}
	
}