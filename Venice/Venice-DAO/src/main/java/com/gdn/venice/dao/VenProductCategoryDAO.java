package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenProductCategory;

/**
 * 
 * @author yauritux
 *
 */
public interface VenProductCategoryDAO extends JpaRepository<VenProductCategory, Long>{

	public List<VenProductCategory> findByProductCategory(String productCategory);
}
