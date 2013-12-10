package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenBank;

/**
 * 
 * @author yauritux
 *
 */
public interface VenBankDAO extends JpaRepository<VenBank, Long>{

	public VenBank findByBankCode(String bankCode);
}
