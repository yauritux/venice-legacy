package com.gdn.venice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenPromotion;

public interface VenOrderItemAdjustmentDAO extends JpaRepository<VenOrderItemAdjustment, Long> {
	String FIND_BY_ORDERITEM_AND_PROMOTION 
		= "select o from VenOrderItemAdjustment o where o.venPromotion = :venPromotion and o.venOrderItem = :venOrderItem";
	
//	select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId
	public List<VenOrderItemAdjustment> findByVenOrderItem(VenOrderItem venOrderItem);
	
	
	@Query(FIND_BY_ORDERITEM_AND_PROMOTION)
	public List<VenOrderItemAdjustment> findByOrderItemAndPromotion(VenOrderItem venOrderItem, VenPromotion venPromotion);
}
