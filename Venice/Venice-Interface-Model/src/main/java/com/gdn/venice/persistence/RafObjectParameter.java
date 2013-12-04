package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the raf_object_parameter database table.
 * 
 */
@Entity
@Table(name="raf_object_parameter")
public class RafObjectParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="raf_object_parameter")  
	@TableGenerator(name="raf_object_parameter", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="object_parameter_id", unique=true, nullable=false)
	private Long objectParameterId;

	@Column(name="parameter_name", nullable=false, length=100)
	private String parameterName;

	@Column(name="parameter_value", nullable=false, length=1000)
	private String parameterValue;

	//bi-directional many-to-one association to RafApplicationObject
    @ManyToOne
	@JoinColumn(name="application_object_id", nullable=false)
	private RafApplicationObject rafApplicationObject;

    public RafObjectParameter() {
    }

	public Long getObjectParameterId() {
		return this.objectParameterId;
	}

	public void setObjectParameterId(Long objectParameterId) {
		this.objectParameterId = objectParameterId;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public RafApplicationObject getRafApplicationObject() {
		return this.rafApplicationObject;
	}

	public void setRafApplicationObject(RafApplicationObject rafApplicationObject) {
		this.rafApplicationObject = rafApplicationObject;
	}
	
}