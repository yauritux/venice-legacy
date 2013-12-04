package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_11 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_11")
public class FrdParameterRule11 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_11")  
	@TableGenerator(name="frd_parameter_rule_11", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;
	
	@Column(name="shipping_address", length=100, nullable=false)
	private String shippingAddress;

	@Column(name="billing_address", length=100, nullable=false)
	private String billingAddress;

	@Column(name="code", length=2, nullable=false)
	private String code;

	@Column(name="risk_point", nullable=false)
	private Integer riskPoint;

    public FrdParameterRule11() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillingAddress() {
		return this.billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getRiskPoint() {
		return this.riskPoint;
	}

	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}

	public String getShippingAddress() {
		return this.shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

}