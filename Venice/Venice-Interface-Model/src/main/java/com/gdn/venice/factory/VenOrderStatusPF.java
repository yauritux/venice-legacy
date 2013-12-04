package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusPF {
	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("PF");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_PF);
		return status;
	}
}
