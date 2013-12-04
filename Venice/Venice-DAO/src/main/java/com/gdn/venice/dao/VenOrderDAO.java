package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrder;

public interface VenOrderDAO extends JpaRepository<VenOrder, Long>{
	public List<VenOrder> findByWcsOrderId(String wcsOrderId);
}
