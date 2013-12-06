package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrder;

public interface VenOrderDAO extends JpaRepository<VenOrder, Long>{
	public VenOrder findByWcsOrderId(String wcsOrderId);
}
