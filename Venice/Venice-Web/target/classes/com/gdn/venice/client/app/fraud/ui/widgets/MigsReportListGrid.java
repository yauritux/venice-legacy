package com.gdn.venice.client.app.fraud.ui.widgets;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;

public class MigsReportListGrid extends ListGrid {
	public MigsReportListGrid() {
		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setSortField(0);
		setShowFilterEditor(true);
		setAutoFetchData(true);
		setCanResizeFields(true);
		setShowRowNumbers(true);
		
		addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshUploadedData();
			}
		});
	}
	
	public void refreshUploadedData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				setData(response.getData());
			}
		};
		
		getDataSource().fetchData(getFilterEditorCriteria(), callBack);
	}
}
