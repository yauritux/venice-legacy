package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the raf_application_object_type database table.
 * 
 */
@Entity
@Table(name="raf_application_object_type")
public class RafApplicationObjectType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_application_object_type")  
	@TableGenerator(name="raf_application_object_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="application_object_type_id")
	private Long applicationObjectTypeId;

	@Column(name="application_object_type_desc", nullable=false, length=32)
	private String applicationObjectTypeDesc;

	//bi-directional many-to-one association to RafApplicationObject
	@OneToMany(mappedBy="rafApplicationObjectType")
	private List<RafApplicationObject> rafApplicationObjects;

    public RafApplicationObjectType() {
    }

	public Long getApplicationObjectTypeId() {
		return this.applicationObjectTypeId;
	}

	public void setApplicationObjectTypeId(Long applicationObjectTypeId) {
		this.applicationObjectTypeId = applicationObjectTypeId;
	}

	public String getApplicationObjectTypeDesc() {
		return this.applicationObjectTypeDesc;
	}

	public void setApplicationObjectTypeDesc(String applicationObjectTypeDesc) {
		this.applicationObjectTypeDesc = applicationObjectTypeDesc;
	}

	public List<RafApplicationObject> getRafApplicationObjects() {
		return this.rafApplicationObjects;
	}

	public void setRafApplicationObjects(List<RafApplicationObject> rafApplicationObjects) {
		this.rafApplicationObjects = rafApplicationObjects;
	}
	
}