package com.gdn.venice.client.app.general.widgets;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class ReturDetailContentLayout extends VLayout {
	ReturDataViewerReturDetailTab returDataViewerReturDetailTab;
	ReturDataViewerReturItemTab returDataViewerReturItemTab;
	ReturDataViewerCustomerTab returDataViewerCustomerTab;
	ReturDataViewerLogisticsTab returDataViewerLogisticsTab;
	ReturDataViewerHistoryTab returDataViewerHistoryTab;
	
	public ReturDetailContentLayout(final DataSource returDetailData, 
			final DataSource returItemData,
			final DataSource customerData,
			final DataSource customerAddressData,
			final DataSource customerContactData,
			final DataSource logisticsAirwayBillData,
			final DataSource historyReturData,
			final DataSource historyReturItemData) {
		TabSet returDetailTabSet = new TabSet();

		returDetailTabSet.setTabBarPosition(Side.TOP);
		returDetailTabSet.setWidth100();
		returDetailTabSet.setHeight100();
		
		returDataViewerReturDetailTab = new ReturDataViewerReturDetailTab("Retur Detail", returDetailData);
		returDataViewerReturItemTab = new ReturDataViewerReturItemTab("Retur Item", returItemData);
		returDataViewerCustomerTab = new ReturDataViewerCustomerTab("Customer", customerData, customerAddressData, customerContactData);
		returDataViewerLogisticsTab = new ReturDataViewerLogisticsTab("Logistics", logisticsAirwayBillData);
		returDataViewerHistoryTab = new ReturDataViewerHistoryTab("History", historyReturData, historyReturItemData);
		
		returDetailTabSet.addTab(returDataViewerReturDetailTab);
		returDetailTabSet.addTab(returDataViewerReturItemTab);
		returDetailTabSet.addTab(returDataViewerCustomerTab);
		returDetailTabSet.addTab(returDataViewerLogisticsTab);
		returDetailTabSet.addTab(returDataViewerHistoryTab);
		
		returDetailTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (event.getTab() instanceof ReturDataViewerReturDetailTab) {
					((ReturDataViewerReturDetailTab) event.getTab()).getReturDetailForm().fetchData();
				}else if (event.getTab() instanceof ReturDataViewerCustomerTab) {
					((ReturDataViewerCustomerTab) event.getTab()).getReturCustomerForm().fetchData();
					((ReturDataViewerCustomerTab) event.getTab()).refreshCustomerContactData();
				}
			}
		});
		
		setWidth100();
		setHeight100();
		
		returDetailData.fetchData(null);
		
		setMembers(returDetailTabSet);
	}	
	
	public ReturDataViewerReturDetailTab getReturDataReturDetailTab() {
		return returDataViewerReturDetailTab;
	}
	
	public ReturDataViewerLogisticsTab getReturDataLogisticsTab() {
		return returDataViewerLogisticsTab;
	}
}
