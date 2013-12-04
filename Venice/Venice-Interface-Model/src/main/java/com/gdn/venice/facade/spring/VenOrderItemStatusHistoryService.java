package com.gdn.venice.facade.spring;

import com.gdn.venice.persistence.VenOrderItem;

public interface VenOrderItemStatusHistoryService {
	public void saveVenOrderItemStatusHistory(VenOrderItem venOrderItem);

	public void savePartialPartialFulfillmentVenOrderItemStatusHistory(VenOrderItem venOrderItem);
}
