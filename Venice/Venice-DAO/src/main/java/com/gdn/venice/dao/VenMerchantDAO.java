package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenMerchant;

/**
 * 
 * @author yauritux
 *
 */
public interface VenMerchantDAO extends JpaRepository<VenMerchant, Long>{

	public List<VenMerchant> findByWcsMerchantId(String wcsMerchantId);
}
