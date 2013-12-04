package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_object_attribute database table.
 * 
 */
@Entity
@Table(name="raf_object_attribute")
public class RafObjectAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_object_attribute")  
	@TableGenerator(name="raf_object_attribute", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="object_attribute_id", unique=true, nullable=false)
	private Long objectAttributeId;

	@Column(name="object_attribute_name", nullable=false, length=100)
	private String objectAttributeName;

	@Column(name="object_attribute_value", nullable=false, length=1000)
	private String objectAttributeValue;

	//bi-directional many-to-one association to RafApplicationObject
    @ManyToOne
	@JoinColumn(name="application_object_id", nullable=false)
	private RafApplicationObject rafApplicationObject;

    public RafObjectAttribute() {
    }

	public Long getObjectAttributeId() {
		return this.objectAttributeId;
	}

	public void setObjectAttributeId(Long objectAttributeId) {
		this.objectAttributeId = objectAttributeId;
	}

	public String getObjectAttributeName() {
		return this.objectAttributeName;
	}

	public void setObjectAttributeName(String objectAttributeName) {
		this.objectAttributeName = objectAttributeName;
	}

	public String getObjectAttributeValue() {
		return this.objectAttributeValue;
	}

	public void setObjectAttributeValue(String objectAttributeValue) {
		this.objectAttributeValue = objectAttributeValue;
	}

	public RafApplicationObject getRafApplicationObject() {
		return this.rafApplicationObject;
	}

	public void setRafApplicationObject(RafApplicationObject rafApplicationObject) {
		this.rafApplicationObject = rafApplicationObject;
	}
	
}