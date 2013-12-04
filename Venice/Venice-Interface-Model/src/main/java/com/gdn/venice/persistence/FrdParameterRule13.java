package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_13 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_13")
public class FrdParameterRule13 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_13")  
	@TableGenerator(name="frd_parameter_rule_13", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="description", length=100, nullable=false)
	private String description;

	@Column(name="min_cc_number", nullable=false)
	private Integer minCcNumber;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

	@Column(name="timespan", nullable=false)
	private Integer timespan;

    public FrdParameterRule13() {
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

	public Integer getMinCcNumber() {
		return this.minCcNumber;
	}

	public void setMinCcNumber(Integer minCcNumber) {
		this.minCcNumber = minCcNumber;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

	public Integer getTimespan() {
		return this.timespan;
	}

	public void setTimespan(Integer timespan) {
		this.timespan = timespan;
	}

}