package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenState;

/**
 * 
 * @author yauritux
 *
 */
public interface VenStateDAO extends JpaRepository<VenState, Long>{

	public List<VenState> findByStateCode(String stateCode);
}
