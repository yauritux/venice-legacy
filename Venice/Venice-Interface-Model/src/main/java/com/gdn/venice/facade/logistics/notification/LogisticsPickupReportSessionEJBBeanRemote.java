package com.gdn.venice.facade.logistics.notification;
import javax.ejb.Remote;

@Remote
public interface LogisticsPickupReportSessionEJBBeanRemote {
	
	public  void createChangeShippingNotification(String wcsOrderItemId,
			String newLogisticProvider);

	public  void createChangeShippingNotification(String wcsOrderItemId);

	public  void createChangeShippingNotification(String[] wcsOrderItemIds);
	

}
