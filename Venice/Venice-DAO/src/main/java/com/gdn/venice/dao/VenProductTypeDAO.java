package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenProductType;

/**
 * 
 * @author yauritux
 *
 */
public interface VenProductTypeDAO extends JpaRepository<VenProductType, Long>{
	
	public List<VenProductType> findByProductTypeCode(String productTypeCode);
}
