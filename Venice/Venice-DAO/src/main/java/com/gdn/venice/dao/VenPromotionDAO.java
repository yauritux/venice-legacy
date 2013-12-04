package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenPromotion;

/**
 * 
 * @author yauritux
 *
 */
public interface VenPromotionDAO extends JpaRepository<VenPromotion, Long>{

	String FIND_BY_PROMOTION_AND_MARGIN 
	  = "select o from VenPromotion o where o.promotionCode =:promotionCode and o.promotionName =:promotionName "
	  		+ "and o.gdnMargin =:gdnMargin and o.merchantMargin =:merchantMargin and o.othersMargin =:othersMargin";
	
	@Query(FIND_BY_PROMOTION_AND_MARGIN)
	public List<VenPromotion> findByPromotionAndMargin(String promotionCode, String promotionName
			, Integer gdnMargin, Integer merchantMargin, Integer othersMargin);	
}
