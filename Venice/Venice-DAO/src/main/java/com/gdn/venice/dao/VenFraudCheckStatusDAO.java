package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenFraudCheckStatus;

/**
 * 
 * @author yauritux
 *
 */
public interface VenFraudCheckStatusDAO extends JpaRepository<VenFraudCheckStatus, Long>{

	public List<VenFraudCheckStatus> findByFraudCheckStatusDesc(String fraudCheckStatusDesc);
}
