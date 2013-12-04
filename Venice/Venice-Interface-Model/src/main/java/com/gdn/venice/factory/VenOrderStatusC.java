package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusC {
	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("C");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_C);
		return status;
	}
}
