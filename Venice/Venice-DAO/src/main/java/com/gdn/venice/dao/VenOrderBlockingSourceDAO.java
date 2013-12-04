package com.gdn.venice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenOrderBlockingSource;

public interface VenOrderBlockingSourceDAO extends JpaRepository<VenOrderBlockingSource, Long>{
	public VenOrderBlockingSource findByBlockingSourceDesc(String blockingSourceDesc);
}
