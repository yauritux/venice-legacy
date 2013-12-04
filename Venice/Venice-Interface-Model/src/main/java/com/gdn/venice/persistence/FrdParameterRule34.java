package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the frd_parameter_rule_34 database table.
 * 
 */
@Entity
@Table(name="frd_parameter_rule_34")
public class FrdParameterRule34 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_parameter_rule_34")  
	@TableGenerator(name="frd_parameter_rule_34", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	private Long id;

	@Column(name="city_name", length=100, nullable=false)
	private String cityName;

	@Column(name="ip_address", length=100, nullable=false)
	private String ipAddress;

    public FrdParameterRule34() {
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

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}