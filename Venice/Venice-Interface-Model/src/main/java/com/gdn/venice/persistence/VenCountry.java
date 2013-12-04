package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_country database table.
 * 
 */
@Entity
@Table(name="ven_country")
public class VenCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_country")  
	@TableGenerator(name="ven_country", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="country_id", unique=true, nullable=false)
	private Long countryId;

	@Column(name="country_code", nullable=false, length=100)
	private String countryCode;

	@Column(name="country_name", nullable=false, length=100)
	private String countryName;

	//bi-directional many-to-one association to VenAddress
	@OneToMany(mappedBy="venCountry")
	private List<VenAddress> venAddresses;

    public VenCountry() {
    }

	public Long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<VenAddress> getVenAddresses() {
		return this.venAddresses;
	}

	public void setVenAddresses(List<VenAddress> venAddresses) {
		this.venAddresses = venAddresses;
	}
	
}