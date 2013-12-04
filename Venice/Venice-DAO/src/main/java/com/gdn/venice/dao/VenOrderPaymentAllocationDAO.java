package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * 
 * @author yauritux
 *
 */
public interface VenOrderPaymentAllocationDAO extends JpaRepository<VenOrderPaymentAllocation, Long>{

	String FIND_BY_VEN_ORDER 
	  = "select o from VenOrderPaymentAllocation o where o.venOrder =:venOrder";
	
	@Query(FIND_BY_VEN_ORDER)
	public List<VenOrderPaymentAllocation> findByVenOrder(VenOrder venOrder);
}
