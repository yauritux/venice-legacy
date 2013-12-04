package com.gdn.venice.client.app.logistic.widgets;

import java.util.List;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class DeliveryStatusTrackingDetailWindow extends Window {
	public DeliveryStatusTrackingDetailWindow(List<DataSource> dataSources) {
		DeliveryStatusTrackingContentLayout deliveryStatusTrackingContentLayout;		
		VLayout deliveryStatusTrackingDetailLayout = new VLayout();
		deliveryStatusTrackingDetailLayout.setHeight100();
		deliveryStatusTrackingDetailLayout.setWidth100();
		deliveryStatusTrackingDetailLayout.setPadding(5);
		
		setHeight(550);
		setWidth(1150);
		setTitle("Delivery Status Tracking Detail");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				DeliveryStatusTrackingDetailWindow.this.destroy();
			}
		});	

		deliveryStatusTrackingContentLayout = new DeliveryStatusTrackingContentLayout(
				dataSources.get(0), //Order data
				dataSources.get(1) //History order item data
		);
		deliveryStatusTrackingDetailLayout.setMembers(deliveryStatusTrackingContentLayout);
		
		HLayout closeButtonLayout = new HLayout(5);		
		IButton buttonClose = new IButton("Close");		
		buttonClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DeliveryStatusTrackingDetailWindow.this.destroy();
			}
		});
		closeButtonLayout.setAlign(Alignment.CENTER);
		closeButtonLayout.setMembers(buttonClose);
		
		deliveryStatusTrackingDetailLayout.setMembers(deliveryStatusTrackingContentLayout,closeButtonLayout);		
		addItem(deliveryStatusTrackingDetailLayout);		
	}
}
