package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdn.venice.persistence.VenMerchantProduct;

/**
 * 
 * @author yauritux
 *
 */
public interface VenMerchantProductDAO extends JpaRepository<VenMerchantProduct, Long>{

	public List<VenMerchantProduct> findByWcsProductSku(String wcsProductSku);
}
