package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the frd_parameter_rule_21 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_21")
public class FrdParameterRule21 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_21")  
	@TableGenerator(name="frd_parameter_rule_21", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="max_time")
	private Time maxTime;

	@Column(name="min_time")
	private Time minTime;

	@Column(name="risk_point")
	private Integer riskPoint;

    public FrdParameterRule21() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getMaxTime() {
		return this.maxTime;
	}

	public void setMaxTime(Time maxTime) {
		this.maxTime = maxTime;
	}

	public Time getMinTime() {
		return this.minTime;
	}

	public void setMinTime(Time minTime) {
		this.minTime = minTime;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}