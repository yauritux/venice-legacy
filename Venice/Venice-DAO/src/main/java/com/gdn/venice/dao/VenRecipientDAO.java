package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenRecipient;

/**
 * 
 * @author yauritux
 *
 */
public interface VenRecipientDAO extends JpaRepository<VenRecipient, Long>{

}
