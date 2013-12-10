package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenCustomer;

/**
 * 
 * @author yauritux
 *
 */
public interface VenCustomerDAO extends JpaRepository<VenCustomer, Long>{
	
	String FIND_BY_CUSTOMER_NAME = 
			  "select o from VenCustomer o where o.customerUserName = ?1 order by o.customerId desc";
	
	public List<VenCustomer> findByWcsCustomerId(String wcsCustomerId);
	
	@Query(FIND_BY_CUSTOMER_NAME)
	public List<VenCustomer> findByCustomerName(String customerName);
}
