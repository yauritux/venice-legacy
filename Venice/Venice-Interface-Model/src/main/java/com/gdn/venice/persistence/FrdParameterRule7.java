package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_7 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_7")
public class FrdParameterRule7 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_7")  
	@TableGenerator(name="frd_parameter_rule_7", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="day_span", nullable=false)
	private Integer daySpan;

	@Column(name="description", length=100, nullable=false)
	private String description;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule7() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDaySpan() {
		return this.daySpan;
	}

	public void setDaySpan(Integer daySpan) {
		this.daySpan = daySpan;
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