package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenParty;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPartyDAO extends JpaRepository<VenParty, Long>{

	public List<VenParty> findByFullOrLegalName(String fullOrLegalName);
}
