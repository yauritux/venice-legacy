package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerLogisticsTab extends Tab {
	ListGrid orderLogisticsAirwayBillListGrid;

	public OrderDataViewerLogisticsTab(String title, DataSource logisticsAirwayBillData) {
		super(title);
		
		HLayout orderLogisticsLayout  = new HLayout();
		
		
		Label orderLogisticsAirwayBillLabel = new Label("<b>Airway Bill:</b>");
		orderLogisticsAirwayBillLabel.setHeight(10);
		
		orderLogisticsAirwayBillListGrid = new ListGrid();
		
		orderLogisticsAirwayBillListGrid.setWidth100();
		orderLogisticsAirwayBillListGrid.setHeight100();
		orderLogisticsAirwayBillListGrid.setShowAllRecords(true);
		orderLogisticsAirwayBillListGrid.setSortField(0);

		orderLogisticsAirwayBillListGrid.setCanResizeFields(true);
		orderLogisticsAirwayBillListGrid.setShowRowNumbers(true);
		orderLogisticsAirwayBillListGrid.setAutoFetchData(true);
	
		orderLogisticsAirwayBillListGrid.setShowFilterEditor(true);	
		
		orderLogisticsAirwayBillListGrid.setDataSource(logisticsAirwayBillData);
		orderLogisticsAirwayBillListGrid.setFields(Util.getListGridFieldsFromDataSource(logisticsAirwayBillData));
		
	
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth("10%");
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth("10%");
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth("10%");		
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth("10%");
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_TRACKINGNUMBER).setWidth("10%");
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setWidth("10%");
		orderLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setWidth("10%");

		
		VLayout orderLogisticsAirwayBillLayout = new VLayout();
		orderLogisticsAirwayBillLayout.setMembers(orderLogisticsAirwayBillLabel, orderLogisticsAirwayBillListGrid);
		
		orderLogisticsLayout.setPadding(5);
		orderLogisticsLayout.setMembersMargin(5);
		orderLogisticsLayout.setMembers(orderLogisticsAirwayBillLayout);
		
		setPane(orderLogisticsLayout);
		
		bindCustomUiHandlers();
	}
	
	public void loadLogisticsAirwayBillData(DataSource logisticsAirwayBillData) {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderLogisticsAirwayBillListGrid.setData(response.getData());
			}
		};
		
		logisticsAirwayBillData.fetchData(orderLogisticsAirwayBillListGrid.getFilterEditorCriteria(), callBack);

	}
	
	protected void bindCustomUiHandlers() {	
		orderLogisticsAirwayBillListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshLogisticsAirwayBilltData();
			}
		});
		
	}
	
	public void refreshLogisticsAirwayBilltData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderLogisticsAirwayBillListGrid.setData(response.getData());
			}
		};
		
		orderLogisticsAirwayBillListGrid.getDataSource().fetchData(orderLogisticsAirwayBillListGrid.getFilterEditorCriteria(), callBack);

	}
}
