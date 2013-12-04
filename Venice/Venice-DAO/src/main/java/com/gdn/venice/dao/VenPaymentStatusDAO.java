package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenPaymentStatus;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPaymentStatusDAO extends JpaRepository<VenPaymentStatus, Long>{

	public List<VenPaymentStatus> findByPaymentStatusCode(String paymentStatusCode);
}
