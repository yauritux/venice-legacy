package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_3 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_3")
public class FrdParameterRule3 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_3")  
	@TableGenerator(name="frd_parameter_rule_3", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="category", length=100, nullable=false)
	private String category;

	@Column(name="description", length=100, nullable=false)
	private String description;

	@Column(name="min_qty", nullable=false)
	private Integer minQty;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule3() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMinQty() {
		return this.minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

}