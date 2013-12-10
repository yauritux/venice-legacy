package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenWcsPaymentType;

/**
 * 
 * @author yauritux
 *
 */
public interface VenWcsPaymentTypeDAO extends JpaRepository<VenWcsPaymentType, Long>{

	public VenWcsPaymentType findByWcsPaymentTypeCode(String wcsPaymentTypeCode);
}
