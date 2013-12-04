package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_10 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_10")
public class FrdParameterRule10 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_10")  
	@TableGenerator(name="frd_parameter_rule_10", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="description", length=100, nullable=false)
	private String description;
	
	@Column(name="min_percentage_usage", nullable=false)
	private Integer minPercentageUsage;

	@Column(name="max_percentage_usage", nullable=false)
	private Integer maxPercentageUsage;
	
	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule10() {
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

	public Integer getMaxPercentageUsage() {
		return this.maxPercentageUsage;
	}

	public void setMaxPercentageUsage(Integer maxPercentageUsage) {
		this.maxPercentageUsage = maxPercentageUsage;
	}

	public Integer getMinPercentageUsage() {
		return this.minPercentageUsage;
	}

	public void setMinPercentageUsage(Integer minPercentageUsage) {
		this.minPercentageUsage = minPercentageUsage;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}