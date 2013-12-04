package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenAddress;

/**
 * 
 * @author yauritux
 *
 */
public interface VenAddressDAO extends JpaRepository<VenAddress, Long>{

}
