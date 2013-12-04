package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the frd_parameter_rule_22 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_22")
public class FrdParameterRule22 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_22")  
	@TableGenerator(name="frd_parameter_rule_22", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="description", length=100, nullable=false)
	private String description;

	@Column(name="max_value")
	private BigDecimal maxValue;

	@Column(name="min_value")
	private BigDecimal minValue;

	@Column(name="risk_point")
	private Integer riskPoint;

    public FrdParameterRule22() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public BigDecimal getMinValue() {
		return this.minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}