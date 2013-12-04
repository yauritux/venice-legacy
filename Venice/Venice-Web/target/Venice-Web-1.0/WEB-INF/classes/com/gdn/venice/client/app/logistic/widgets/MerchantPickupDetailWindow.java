package com.gdn.venice.client.app.logistic.widgets;

import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MerchantPickupDetailWindow extends Window {
	public MerchantPickupDetailWindow(DataSource merchantPickupDetailData) {
		VLayout merchantDetailLayout = new VLayout();
		merchantDetailLayout.setHeight100();
		merchantDetailLayout.setWidth100();
		merchantDetailLayout.setPadding(5);
		
		
		setWidth(400);
		setHeight(290);
		setTitle("Merchant Detail");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				MerchantPickupDetailWindow.this.destroy();
			}
		});
		
		DynamicForm merchantPickupDetailForm = new DynamicForm();
		merchantPickupDetailForm.setDataSource(merchantPickupDetailData);  
		merchantPickupDetailForm.setUseAllDataSourceFields(true);
		
		merchantPickupDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(merchantPickupDetailData));
		merchantPickupDetailForm.fetchData();
		
		HLayout okButtonLayout = new HLayout(5);
		
		IButton buttonOK = new IButton("OK");
		
		buttonOK.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MerchantPickupDetailWindow.this.destroy();
			}
		});
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.setMembers(buttonOK);
		
		merchantDetailLayout.setMembers(merchantPickupDetailForm,okButtonLayout);
		
		addItem(merchantDetailLayout);
		
	}
}
