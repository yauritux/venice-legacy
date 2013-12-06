package com.gdn.venice.client.app.logistic.widgets;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;

public class ActivityReportReconciliation extends ListGrid {
	public ActivityReportReconciliation() {
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
				refreshAirwayBillData();
			}
		});
		
	}
	
	public void refreshAirwayBillData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				setData(response.getData());
//				setHilites(hilites);
			}
		};
		
		getDataSource().fetchData(getFilterEditorCriteria(), callBack);
	}
	

}
