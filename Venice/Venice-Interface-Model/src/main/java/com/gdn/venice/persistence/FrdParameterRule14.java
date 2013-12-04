package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_14 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_14")
public class FrdParameterRule14 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_14")  
	@TableGenerator(name="frd_parameter_rule_14", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="description", length=100, nullable=false)
	private String description;

	@Column(name="email_type", length=100, nullable=false)
	private String emailType;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule14() {
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

	public String getEmailType() {
		return this.emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}