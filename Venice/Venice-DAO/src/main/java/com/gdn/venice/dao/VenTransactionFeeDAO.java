package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenTransactionFee;

public interface VenTransactionFeeDAO extends JpaRepository<VenTransactionFee, Long> {
	public List<VenTransactionFee> findByVenOrder(VenOrder venOrder);
}
