package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.LogLogisticService;

/**
 * 
 * @author yauritux
 *
 */
public interface LogLogisticServiceDAO extends JpaRepository<LogLogisticService, Long>{

	public List<LogLogisticService> findByServiceCode(String serviceCode);
}
