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

public class ReturDataViewerReturItemTab extends Tab {
	ListGrid returItemListGrid = null;
	
	public ReturDataViewerReturItemTab(String title, DataSource returItemData) {
		super(title);
		
		returItemListGrid = new ListGrid();
		
		returItemListGrid.setWidth100();
		returItemListGrid.setHeight100();
		returItemListGrid.setShowAllRecords(true);
		returItemListGrid.setSortField(0);

		returItemListGrid.setCanResizeFields(true);
		returItemListGrid.setShowRowNumbers(true);
		returItemListGrid.setAutoFetchData(true);
	
		returItemListGrid.setSelectionType(SelectionStyle.SIMPLE);
		returItemListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		returItemListGrid.setShowFilterEditor(true);	
		
		returItemListGrid.setDataSource(returItemData);
		returItemListGrid.setFields(Util.getListGridFieldsFromDataSource(returItemData));
		
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_RETURITEMID).setWidth(100);
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_RETURITEMID).setHidden(true);
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_WCSRETURITEMID).setWidth(150);
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_VENRETURSTATUS_ORDERSTATUSCODE).setWidth("10%");		
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("20%");
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_QUANTITY).setWidth(150);
		returItemListGrid.getField(DataNameTokens.VENRETURITEM_TOTAL).setWidth(200);
		Util.formatListGridFieldAsCurrency(returItemListGrid.getField(DataNameTokens.VENRETURITEM_TOTAL));
		
		setPane(returItemListGrid);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		returItemListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshReturItemData();
			}
		});
		
	}
	
	public void refreshReturItemData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				returItemListGrid.setData(response.getData());
			}
		};
		
		returItemListGrid.getDataSource().fetchData(returItemListGrid.getFilterEditorCriteria(), callBack);

	}
}
