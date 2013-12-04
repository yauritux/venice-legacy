package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrderStatus;

public interface VenOrderStatusDAO extends JpaRepository<VenOrderStatus, Long>{
	VenOrderStatus findByOrderStatusCode(String orderStatusCode);
}
