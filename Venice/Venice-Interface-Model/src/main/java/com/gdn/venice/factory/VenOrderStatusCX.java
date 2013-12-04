package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusCX {
	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("CX");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
		return status;
	}
}
