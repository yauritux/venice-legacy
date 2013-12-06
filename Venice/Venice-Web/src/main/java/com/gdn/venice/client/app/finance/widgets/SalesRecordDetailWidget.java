package com.gdn.venice.client.app.finance.widgets;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;

/**
 * Widget for Sales Record Detail Adjustment
 * 
 * @author Roland
 */

public class SalesRecordDetailWidget extends ListGrid {
	public SalesRecordDetailWidget() {
		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setCanExpandRecords(true);
		setSortField(0);
		
		setSelectionType(SelectionStyle.SIMPLE);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setShowFilterEditor(true);		
		setAutoFetchData(true);		
		setCanResizeFields(true);
		setShowRowNumbers(true);
		
		addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSalesRecordDetailData();
			}
		});
		
	}
	
	public void refreshSalesRecordDetailData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				setData(response.getData());
			}
		};		
		getDataSource().fetchData(getFilterEditorCriteria(), callBack);
	}
}
