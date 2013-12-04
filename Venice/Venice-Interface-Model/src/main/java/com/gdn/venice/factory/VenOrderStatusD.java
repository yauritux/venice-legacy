package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusD {
	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("D");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_D);
		return status;
	}
}
