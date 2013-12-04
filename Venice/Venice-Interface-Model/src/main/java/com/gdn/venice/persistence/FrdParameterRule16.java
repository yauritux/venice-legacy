package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_16 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_16")
public class FrdParameterRule16 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_16")  
	@TableGenerator(name="frd_parameter_rule_16", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="description", length=100, nullable=false)
	private String description;
	
	@Column(name="code", length=100, nullable=false)
	private String code;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule16() {
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

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}