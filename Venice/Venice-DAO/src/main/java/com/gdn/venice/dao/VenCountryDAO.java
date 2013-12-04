package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenCountry;

/**
 * 
 * @author yauritux
 *
 */
public interface VenCountryDAO extends JpaRepository<VenCountry, Long>{

	public List<VenCountry> findByCountryCode(String countryCode);
}
