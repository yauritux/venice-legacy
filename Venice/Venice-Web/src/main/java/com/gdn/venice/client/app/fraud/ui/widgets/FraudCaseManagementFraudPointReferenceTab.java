package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class FraudCaseManagementFraudPointReferenceTab extends Tab {
	ListGrid fraudPointListGrid = null;
	
	public FraudCaseManagementFraudPointReferenceTab(String title, DataSource riskScoreData) {
		super(title);		
		
		fraudPointListGrid = new ListGrid();
		fraudPointListGrid.setAutoFetchData(true);
		fraudPointListGrid.setWidth100();
		fraudPointListGrid.setHeight100();
		fraudPointListGrid.setShowAllRecords(true);
		fraudPointListGrid.setSortField(0);
		fraudPointListGrid.setCanResizeFields(true);
		fraudPointListGrid.setDataSource(riskScoreData);
		fraudPointListGrid.setFields(Util.getListGridFieldsFromDataSource(riskScoreData));
		fraudPointListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID).setWidth("10%");
		fraudPointListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS).setWidth("10%");
		fraudPointListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID).setHidden(true);
		fraudPointListGrid.setCanResizeFields(true);
		fraudPointListGrid.setShowRowNumbers(true);
		
		VLayout fraudCaseFraudPointReferenceLayout = new VLayout();
		fraudCaseFraudPointReferenceLayout.setMembers(fraudPointListGrid);		
		setPane(fraudCaseFraudPointReferenceLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
				
	}
	
	public void refreshFraudCaseFraudPointData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudPointListGrid.setData(response.getData());
			}
		};
		fraudPointListGrid.getDataSource().fetchData(fraudPointListGrid.getFilterEditorCriteria(), callBack);
	}	
}
