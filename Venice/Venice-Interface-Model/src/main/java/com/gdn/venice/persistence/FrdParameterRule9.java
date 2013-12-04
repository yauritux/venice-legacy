package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_9 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_9")
public class FrdParameterRule9 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_9")  
	@TableGenerator(name="frd_parameter_rule_9", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="shipping", length=100, nullable=false)
	private String shipping;

	@Column(name="payment", length=100, nullable=false)
	private String payment;

	@Column(name="code", length=2, nullable=false)
	private String code;
	
	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;
	
    public FrdParameterRule9() {
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

	public String getPayment() {
		return this.payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

	public String getShipping() {
		return this.shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

}