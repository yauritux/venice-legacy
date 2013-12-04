package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_40 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_40")
public class FrdParameterRule40 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_40")  
	@TableGenerator(name="frd_parameter_rule_40", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;
	
	@Column(name="city_name", length=100, nullable=false)
	private String cityName;

	@Column(name="no_hp", length=100, nullable=false)
	private String noHp;

    public FrdParameterRule40() {
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

	public void setCityName(String cityname) {
		this.cityName = cityname;
	}

	public String getNoHp() {
		return this.noHp;
	}

	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
}