package com.gdn.venice.facade.spring;

import com.gdn.venice.persistence.VenOrder;

public interface VenOrderStatusHistoryService {

	public void savePartialPartialFulfillmentVenOrderStatusHistory(VenOrder venOrder);

	public void saveVenOrderStatusHistory(VenOrder venOrder);

}
