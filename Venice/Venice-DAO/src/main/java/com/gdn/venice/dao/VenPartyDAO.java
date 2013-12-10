package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenParty;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPartyDAO extends JpaRepository<VenParty, Long>{
	String FIND_BY_LEGAL_NAME 
	   = "select o from VenParty o where o.fullOrLegalName = ?1";
	
	@Query(FIND_BY_LEGAL_NAME)
	public List<VenParty> findByLegalName(String fullOrLegalName);
}
