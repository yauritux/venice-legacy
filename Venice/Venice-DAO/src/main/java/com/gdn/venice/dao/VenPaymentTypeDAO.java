package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenPaymentType;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPaymentTypeDAO extends JpaRepository<VenPaymentType, Long>{

	public List<VenPaymentType> findByPaymentTypeCode(String paymentTypeCode);
}
