package com.gdn.venice.factory;

import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

public class VenOrderStatusFP{

	public static VenOrderStatus createVenOrderStatus(){
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusCode("FP");
		status.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_FP);
		return status;
	}
	
}
