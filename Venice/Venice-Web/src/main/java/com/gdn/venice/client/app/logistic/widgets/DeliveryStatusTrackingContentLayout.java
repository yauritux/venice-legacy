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
		TabSet deliveryStatusTrackingDetailTabSet = new TabSet();

		deliveryStatusTrackingDetailTabSet.setTabBarPosition(Side.TOP);
		deliveryStatusTrackingDetailTabSet.setWidth100();
		deliveryStatusTrackingDetailTabSet.setHeight100();
		
		deliveryStatusTrackingDetailTabSet.addTab(new DeliveryStatusTrackingDetailTab("Delivery Status Tracking Detail", airwayBillData, historyOrderItemData));	
		deliveryStatusTrackingDetailTabSet.addTabSelectedHandler(new TabSelectedHandler() {			
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
		setMembers(deliveryStatusTrackingDetailTabSet);
	}	
}