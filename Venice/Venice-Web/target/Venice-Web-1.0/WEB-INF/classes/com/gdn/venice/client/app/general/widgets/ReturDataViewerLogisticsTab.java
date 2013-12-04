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

public class ReturDataViewerLogisticsTab extends Tab {
	ListGrid returLogisticsAirwayBillListGrid;

	public ReturDataViewerLogisticsTab(String title, DataSource logisticsAirwayBillData) {
		super(title);
		
		HLayout returLogisticsLayout  = new HLayout();
		
		Label returLogisticsAirwayBillLabel = new Label("<b>Airway Bill:</b>");
		returLogisticsAirwayBillLabel.setHeight(10);
		
		returLogisticsAirwayBillListGrid = new ListGrid();
		
		returLogisticsAirwayBillListGrid.setWidth100();
		returLogisticsAirwayBillListGrid.setHeight100();
		returLogisticsAirwayBillListGrid.setShowAllRecords(true);
		returLogisticsAirwayBillListGrid.setSortField(0);

		returLogisticsAirwayBillListGrid.setCanResizeFields(true);
		returLogisticsAirwayBillListGrid.setShowRowNumbers(true);
	
		returLogisticsAirwayBillListGrid.setShowFilterEditor(true);	
		returLogisticsAirwayBillListGrid.setAutoFetchData(true);	
		
		returLogisticsAirwayBillListGrid.setDataSource(logisticsAirwayBillData);
		returLogisticsAirwayBillListGrid.setFields(Util.getListGridFieldsFromDataSource(logisticsAirwayBillData));
		
	
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLID).setWidth("10%");
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLID).setHidden(true);
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_VENRETUR_WCSRETURID).setWidth("10%");
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_WCSRETURITEMID).setWidth("10%");		
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_GDNREFERENCE).setWidth("10%");
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_TRACKINGNUMBER).setWidth("10%");
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLPICKUPDATETIME).setWidth("10%");
		returLogisticsAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILLRETUR_RECEIVED).setWidth("10%");
		
		VLayout orderLogisticsAirwayBillLayout = new VLayout();
		orderLogisticsAirwayBillLayout.setMembers(returLogisticsAirwayBillLabel, returLogisticsAirwayBillListGrid);
		
		returLogisticsLayout.setPadding(5);
		returLogisticsLayout.setMembersMargin(5);
		returLogisticsLayout.setMembers(orderLogisticsAirwayBillLayout);
		
		setPane(returLogisticsLayout);
		
		bindCustomUiHandlers();
	}
	
	public void loadLogisticsAirwayBillData(DataSource logisticsAirwayBillData) {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				returLogisticsAirwayBillListGrid.setData(response.getData());
			}
		};
		
		logisticsAirwayBillData.fetchData(returLogisticsAirwayBillListGrid.getFilterEditorCriteria(), callBack);

	}
	
	protected void bindCustomUiHandlers() {		
		returLogisticsAirwayBillListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
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
				returLogisticsAirwayBillListGrid.setData(response.getData());
			}
		};
		
		returLogisticsAirwayBillListGrid.getDataSource().fetchData(returLogisticsAirwayBillListGrid.getFilterEditorCriteria(), callBack);

	}
}
