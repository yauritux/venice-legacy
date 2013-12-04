package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.LogLogisticsProvider;

/**
 * 
 * @author yauritux
 *
 */
public interface LogLogisticsProviderDAO extends JpaRepository<LogLogisticsProvider, Long>{

	public List<LogLogisticsProvider> findByLogisticsProviderCode(String logisticsProviderCode);
}
