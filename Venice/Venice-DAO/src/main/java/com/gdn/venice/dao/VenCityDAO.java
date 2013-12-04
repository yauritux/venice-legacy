package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenCity;

/**
 * 
 * @author yauritux
 *
 */
public interface VenCityDAO extends JpaRepository<VenCity, Long>{
	
	public List<VenCity> findByCityCode(String cityCode);

}
