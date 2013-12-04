package com.gdn.venice.facade.logistics.notification;
import javax.ejb.Local;

@Local
public interface LogisticsPickupReportSessionEJBBeanLocal {

	public void createChangeShippingNotification(String wcsOrderItemId,
			String newLogisticProvider);

	public void createChangeShippingNotification(String wcsOrderItemId);

	public void createChangeShippingNotification(String[] wcsOrderItemIds);

}
