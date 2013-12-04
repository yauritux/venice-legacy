package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrderStatusHistory;

public interface VenOrderStatusHistoryDAO extends JpaRepository<VenOrderStatusHistory, Long> {

}
