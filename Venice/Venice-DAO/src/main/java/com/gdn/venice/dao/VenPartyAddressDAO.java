package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyAddress;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPartyAddressDAO extends JpaRepository<VenPartyAddress, Long>{

	public List<VenPartyAddress> findByVenParty(VenParty venParty);
}
