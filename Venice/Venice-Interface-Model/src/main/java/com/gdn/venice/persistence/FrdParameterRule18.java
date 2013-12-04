package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_18 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_18")
public class FrdParameterRule18 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_18")  
	@TableGenerator(name="frd_parameter_rule_18", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;
	@Column(name="code", length=10)
	private String code;
	
	@Column(name="description", length=100)
	private String description;

    public FrdParameterRule18() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}