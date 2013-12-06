package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class OrderDetailContentLayout extends VLayout {
	OrderDataViewerOrderDetailTab orderDataViewerOrderDetailTab;
	OrderDataViewerOrderItemTab orderDataViewerOrderItemTab;
	OrderDataViewerCustomerTab orderDataViewerCustomerTab;
	OrderDataViewerLogisticsTab orderDataViewerLogisticsTab;
	OrderDataViewerFinanceTab orderDataViewerFinanceTab;  
	OrderDataViewerHistoryTab orderDataViewerHistoryTab;
	
	public OrderDetailContentLayout(final DataSource orderDetailData, 
			final DataSource orderItemData,
			final DataSource customerData,
			final DataSource customerAddressData,
			final DataSource customerContactData,
			final DataSource logisticsAirwayBillData,
			final DataSource financePaymentData,
			final DataSource financeReconciliationData,
			final DataSource historyOrderData,
			final DataSource historyOrderItemData) {
		TabSet orderDetailTabSet = new TabSet();

		orderDetailTabSet.setTabBarPosition(Side.TOP);
		orderDetailTabSet.setWidth100();
		orderDetailTabSet.setHeight100();
		
		orderDataViewerOrderDetailTab = new OrderDataViewerOrderDetailTab("Order Detail", orderDetailData);
		orderDataViewerOrderItemTab = new OrderDataViewerOrderItemTab("Order Item", orderItemData);
		orderDataViewerCustomerTab = new OrderDataViewerCustomerTab("Customer", customerData, customerAddressData, customerContactData);
		orderDataViewerLogisticsTab = new OrderDataViewerLogisticsTab("Logistics", logisticsAirwayBillData);
		orderDataViewerFinanceTab = new OrderDataViewerFinanceTab("Finance", financePaymentData, financeReconciliationData);
		orderDataViewerHistoryTab = new OrderDataViewerHistoryTab("History", historyOrderData, historyOrderItemData);
		
		orderDetailTabSet.addTab(orderDataViewerOrderDetailTab);
		orderDetailTabSet.addTab(orderDataViewerOrderItemTab);
		orderDetailTabSet.addTab(orderDataViewerCustomerTab);
		orderDetailTabSet.addTab(orderDataViewerLogisticsTab);
		orderDetailTabSet.addTab(orderDataViewerFinanceTab);
		orderDetailTabSet.addTab(orderDataViewerHistoryTab);
		
		
		
		orderDetailTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (event.getTab() instanceof OrderDataViewerOrderDetailTab) {
					DSCallback orderStatusButtonCallBack = new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							updateOrderStatusButton(response);
						}
					};
					((OrderDataViewerOrderDetailTab) event.getTab()).getOrderDetailForm().fetchData(null, orderStatusButtonCallBack);
				} else if (event.getTab() instanceof OrderDataViewerCustomerTab) {
					((OrderDataViewerCustomerTab) event.getTab()).getOrderCustomerForm().fetchData();
					((OrderDataViewerCustomerTab) event.getTab()).refreshCustomerContactData();
				}
			}
		});
		
		setWidth100();
		setHeight100();
		
		DSCallback orderStatusButtonCallBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				updateOrderStatusButton(response);
			}
		};
		
		orderDetailData.fetchData(null, orderStatusButtonCallBack);
		
		setMembers(orderDetailTabSet);
	}	
	
	private void updateOrderStatusButton(DSResponse response) {
		if (response.getData()!= null && response.getData().length > 0) {
			orderDataViewerOrderDetailTab.getButtonSuspiciousFraud().setDisabled(true);
			orderDataViewerOrderDetailTab.getButtonFraudConfirmed().setDisabled(true);
			orderDataViewerOrderDetailTab.getButtonFraudPassed().setDisabled(true);
			orderDataViewerOrderDetailTab.getButtonBlock().setDisabled(false);
			
			String orderStatus = response.getData()[0].getAttributeAsString(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE);
			if (orderStatus.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_C)) {
				orderDataViewerOrderDetailTab.getButtonSuspiciousFraud().setDisabled(false);
				orderDataViewerOrderDetailTab.getButtonFraudPassed().setDisabled(false);
			} else if (orderStatus.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_SF)) {
				orderDataViewerOrderDetailTab.getButtonFraudConfirmed().setDisabled(false);
				orderDataViewerOrderDetailTab.getButtonFraudPassed().setDisabled(false);
			} 
			
			if(orderStatus.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_X) || orderStatus.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSCODE_VA)){
				orderDataViewerOrderDetailTab.getButtonBlock().setDisabled(true);
			}
		}
		
	}
	
	public OrderDataViewerOrderDetailTab getOrderDataOrderDetailTab() {
		return orderDataViewerOrderDetailTab;
	}
	
	public OrderDataViewerLogisticsTab getOrderDataLogisticsTab() {
		return orderDataViewerLogisticsTab;
	}
	
	public OrderDataViewerFinanceTab getOrderDataFinanceTab() {
		return orderDataViewerFinanceTab;
	}
}
