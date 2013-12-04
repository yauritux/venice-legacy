package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the frd_parameter_rule_20 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_20")
public class FrdParameterRule20 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_20")  
	@TableGenerator(name="frd_parameter_rule_20", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="provinsi", length=100, nullable=false)
	private String provinsi;

	@Column(name="umr", length=20, nullable=false)
	private BigDecimal umr;

    public FrdParameterRule20() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvinsi() {
		return this.provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	public BigDecimal getUmr() {
		return this.umr;
	}

	public void setUmr(BigDecimal umr) {
		this.umr = umr;
	}

}