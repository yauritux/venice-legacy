package com.gdn.venice.client.app.logistic.widgets;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class DeliveryStatusTrackingContentLayout extends HLayout {

	public DeliveryStatusTrackingContentLayout(
			final DataSource airwayBillData,
			final DataSource historyOrderItemData
			) {
		TabSet fraudCaseManagementTabSet = new TabSet();

		fraudCaseManagementTabSet.setTabBarPosition(Side.TOP);
		fraudCaseManagementTabSet.setWidth100();
		fraudCaseManagementTabSet.setHeight100();
		
		fraudCaseManagementTabSet.addTab(new DeliveryStatusTrackingDetailTab("Delivery Status Tracking Detail", airwayBillData, historyOrderItemData));	
		fraudCaseManagementTabSet.addTabSelectedHandler(new TabSelectedHandler() {			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (event.getTab() instanceof DeliveryStatusTrackingDetailTab) {
					((DeliveryStatusTrackingDetailTab) event.getTab()).getOrderDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getOrderItemDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getRecipientDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getSenderDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getAirwayBillDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getServiceDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getChargeDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getOrderItemStatusDetailForm().fetchData();
					((DeliveryStatusTrackingDetailTab) event.getTab()).getMerchantDetailForm().fetchData();
				} 
			}
		});
		
		setWidth100();
		setHeight100();		
		setMembers(fraudCaseManagementTabSet);
	}	
}