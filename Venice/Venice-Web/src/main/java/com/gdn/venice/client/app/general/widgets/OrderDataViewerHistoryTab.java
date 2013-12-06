package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerHistoryTab extends Tab {
	ListGrid orderHistoryOrderListGrid;
	ListGrid orderHistoryOrderItemListGrid;

	public OrderDataViewerHistoryTab(String title, DataSource historyOrderData, DataSource historyOrderItemData) {
		super(title);
		
		HLayout orderHistoryLayout  = new HLayout();
		
		Label orderHistoryOrderLabel = new Label("<b>Order:</b>");
		orderHistoryOrderLabel.setHeight(10);
		
		orderHistoryOrderListGrid = new ListGrid();		
		orderHistoryOrderListGrid.setWidth100();
		orderHistoryOrderListGrid.setHeight100();
		orderHistoryOrderListGrid.setShowAllRecords(true);
		orderHistoryOrderListGrid.setCanResizeFields(true);
		orderHistoryOrderListGrid.setShowRowNumbers(true);
		orderHistoryOrderListGrid.setAutoFetchData(true);
			
		orderHistoryOrderListGrid.setDataSource(historyOrderData);
		orderHistoryOrderListGrid.setFields(Util.getListGridFieldsFromDataSource(historyOrderData));
		
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_ORDERID).setWidth(100);
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_WCSORDERID).setWidth(100);
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_TIMESTAMP).setWidth(120);
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);		
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_STATUSCHANGEREASON).setWidth(200);
		
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_ORDERID).setHidden(true);
		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_WCSORDERID).setHidden(true);
		orderHistoryOrderListGrid.groupBy(DataNameTokens.VENORDERSTATUSHISTORY_WCSORDERID);
//		orderHistoryOrderListGrid.getField(DataNameTokens.VENORDERSTATUSHISTORY_TIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		VLayout orderHistoryOrderLayout = new VLayout();
		orderHistoryOrderLayout.setMembers(orderHistoryOrderLabel, orderHistoryOrderListGrid);
		
		Label orderHistoryOrderItemLabel = new Label("<b>Order Item:</b>");
		orderHistoryOrderItemLabel.setHeight(10);
		
		orderHistoryOrderItemListGrid = new ListGrid();		
		orderHistoryOrderItemListGrid.setWidth100();
		orderHistoryOrderItemListGrid.setHeight100();
		orderHistoryOrderItemListGrid.setShowAllRecords(true);

		orderHistoryOrderItemListGrid.setCanResizeFields(true);
		orderHistoryOrderItemListGrid.setShowRowNumbers(true);
		orderHistoryOrderItemListGrid.setAutoFetchData(true);			
		orderHistoryOrderItemListGrid.setDataSource(historyOrderItemData);
		orderHistoryOrderItemListGrid.setFields(Util.getListGridFieldsFromDataSource(historyOrderItemData));
		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID).setWidth(100);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID).setWidth(100);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP).setWidth(120);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON).setWidth(200);
		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID).setHidden(true);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID).setHidden(true);
		orderHistoryOrderItemListGrid.groupBy(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID);
		
//		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
				
		VLayout orderHistoryOrderItemLayout = new VLayout();
		orderHistoryOrderItemLayout.setMembers(orderHistoryOrderItemLabel, orderHistoryOrderItemListGrid);
		
		orderHistoryLayout.setPadding(5);
		orderHistoryLayout.setMembersMargin(5);
		orderHistoryLayout.setMembers(orderHistoryOrderLayout, orderHistoryOrderItemLayout);
		
		setPane(orderHistoryLayout);
	}
}
