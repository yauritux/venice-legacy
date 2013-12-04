package com.gdn.venice.facade.spring;

import com.gdn.venice.persistence.VenOrderItem;

public interface PublisherService {

	public void publishUpdateOrderItemStatus(VenOrderItem newVenOrderItem);

}
