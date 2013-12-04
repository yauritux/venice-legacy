package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenPromotion;

/**
 * 
 * @author yauritux
 *
 */
public interface VenOrderItemAdjustmentDAO extends JpaRepository<VenOrderItemAdjustment, Long>{
	
	String FIND_BY_ORDERITEM_AND_PROMOTION 
	   = "select o from VenOrderItemAdjustment o where o.venPromotion = :venPromotion and o.venOrderItem = :venOrderItem";
	
	@Query(FIND_BY_ORDERITEM_AND_PROMOTION)
	public List<VenOrderItemAdjustment> findByOrderItemAndPromotion(VenOrderItem venOrderItem, VenPromotion venPromotion);
}
