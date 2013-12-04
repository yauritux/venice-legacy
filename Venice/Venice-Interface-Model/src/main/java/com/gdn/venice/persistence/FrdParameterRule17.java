package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_17 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_17")
public class FrdParameterRule17 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_17")  
	@TableGenerator(name="frd_parameter_rule_17", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="city_name", length=100, nullable=false)
	private String cityName;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule17() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}