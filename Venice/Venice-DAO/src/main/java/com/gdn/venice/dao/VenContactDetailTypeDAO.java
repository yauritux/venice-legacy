package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenContactDetailType;

/**
 * 
 * @author yauritux
 *
 */
public interface VenContactDetailTypeDAO extends JpaRepository<VenContactDetailType, Long>{
	
	public List<VenContactDetailType> findByContactDetailTypeDesc(String contactDetailTypeDesc);

}
