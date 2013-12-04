package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusES {
	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("ES");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_ES);
		return status;
	}
}
