package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerOrderItemTab extends Tab {
	ListGrid orderItemListGrid = null;
	
	public OrderDataViewerOrderItemTab(String title, DataSource orderItemData) {
		super(title);
		
		orderItemListGrid = new ListGrid();
		
		orderItemListGrid.setWidth100();
		orderItemListGrid.setHeight100();
		orderItemListGrid.setShowAllRecords(true);
		orderItemListGrid.setSortField(0);

		orderItemListGrid.setCanResizeFields(true);
		orderItemListGrid.setShowRowNumbers(true);
		orderItemListGrid.setAutoFetchData(true);
	
		orderItemListGrid.setSelectionType(SelectionStyle.SIMPLE);
		orderItemListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		orderItemListGrid.setShowFilterEditor(true);	
		
		orderItemListGrid.setDataSource(orderItemData);
		orderItemListGrid.setFields(Util.getListGridFieldsFromDataSource(orderItemData));
		
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setWidth(100);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setHidden(true);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_WCSORDERITEMID).setWidth(150);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("10%");		
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("20%");
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setWidth(150);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth(200);
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));
		
		setPane(orderItemListGrid);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		orderItemListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshOrderItemData();
			}
		});
		
	}
	
	public void refreshOrderItemData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderItemListGrid.setData(response.getData());
			}
		};
		
		orderItemListGrid.getDataSource().fetchData(orderItemListGrid.getFilterEditorCriteria(), callBack);

	}
	
}
