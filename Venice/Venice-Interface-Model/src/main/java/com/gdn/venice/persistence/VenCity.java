package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_city database table.
 * 
 */
@Entity
@Table(name="ven_city")
public class VenCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_city")  
	@TableGenerator(name="ven_city", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="city_id", unique=true, nullable=false)
	private Long cityId;

	@Column(name="city_code", nullable=false, length=100)
	private String cityCode;

	@Column(name="city_name", nullable=false, length=100)
	private String cityName;

	//bi-directional many-to-one association to VenAddress
	@OneToMany(mappedBy="venCity")
	private List<VenAddress> venAddresses;

    public VenCity() {
    }

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<VenAddress> getVenAddresses() {
		return this.venAddresses;
	}

	public void setVenAddresses(List<VenAddress> venAddresses) {
		this.venAddresses = venAddresses;
	}
	
}