package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrderItemStatusHistory;

public interface VenOrderItemStatusHistoryDAO extends JpaRepository<VenOrderItemStatusHistory, Long>{

}
