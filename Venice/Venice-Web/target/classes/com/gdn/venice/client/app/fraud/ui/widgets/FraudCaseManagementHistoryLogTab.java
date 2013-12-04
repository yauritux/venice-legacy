package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Widget for History Log
 * 
 * @author Roland
 */

public class FraudCaseManagementHistoryLogTab extends Tab {
	ListGrid fraudHistoryLogListGrid = null;

	public FraudCaseManagementHistoryLogTab(String title, DataSource historyLogData) {
		super(title);		

		fraudHistoryLogListGrid = new ListGrid();
		fraudHistoryLogListGrid.setWidth100();
		fraudHistoryLogListGrid.setHeight100();
		fraudHistoryLogListGrid.setShowAllRecords(true);
		fraudHistoryLogListGrid.setSortField(0);
		fraudHistoryLogListGrid.setAutoFetchData(true);
		fraudHistoryLogListGrid.setDataSource(historyLogData);
		fraudHistoryLogListGrid.setShowFilterEditor(true);
		fraudHistoryLogListGrid.setFields(Util.getListGridFieldsFromDataSource(historyLogData));
		fraudHistoryLogListGrid.setCanSort(true);
		fraudHistoryLogListGrid.getField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID).setHidden(true);
		fraudHistoryLogListGrid.getField(DataNameTokens.FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSDESC).setWidth(120);
		fraudHistoryLogListGrid.getField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE).setWidth(120);
//		fraudHistoryLogListGrid.getField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		fraudHistoryLogListGrid.setCanResizeFields(true);
		fraudHistoryLogListGrid.setShowRowNumbers(true);
		VLayout historyLogLayout = new VLayout();
		historyLogLayout.setMembers(fraudHistoryLogListGrid);
		setPane(historyLogLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		fraudHistoryLogListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseHistoryLogData();
			}
		});
	}
	
	public void refreshFraudCaseHistoryLogData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudHistoryLogListGrid.setData(response.getData());
			}
		};
		
		fraudHistoryLogListGrid.getDataSource().fetchData(fraudHistoryLogListGrid.getFilterEditorCriteria(), callBack);
	}		
}
