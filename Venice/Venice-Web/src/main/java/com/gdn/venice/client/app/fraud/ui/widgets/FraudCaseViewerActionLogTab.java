package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Widget for Action Log Viewer
 * 
 * @author Roland
 */

public class FraudCaseViewerActionLogTab extends Tab {
	ListGrid fraudActionLogListGrid = new ListGrid();
	
	public FraudCaseViewerActionLogTab(String title, DataSource actionLogData) {
		super(title);		
		
		fraudActionLogListGrid.setWidth100();
		fraudActionLogListGrid.setHeight100();
		fraudActionLogListGrid.setShowAllRecords(true);
		fraudActionLogListGrid.setSortField(0);
		fraudActionLogListGrid.setAutoFetchData(true);
		fraudActionLogListGrid.setShowFilterEditor(true);
		fraudActionLogListGrid.setDataSource(actionLogData);
		fraudActionLogListGrid.setFields(Util.getListGridFieldsFromDataSource(actionLogData));
//		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID).setHidden(true);
		fraudActionLogListGrid.setCanResizeFields(true);
		fraudActionLogListGrid.setShowRowNumbers(true);
		fraudActionLogListGrid.setSelectionType(SelectionStyle.SIMPLE);
		fraudActionLogListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID).setHidden(true);		
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME).setWidth(120);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE).setWidth(100);
		
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();  
		map.put("CALL", "Call");
		map.put("MEET", "Meet");
		map.put("SENDDOCUMENT", "Send Document");
		map.put("OTHER", "Other");
		actionLogData.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE).setValueMap(map);

		VLayout actionLogLayout = new VLayout();
		actionLogLayout.setMembers( fraudActionLogListGrid);
		setPane(actionLogLayout);
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
				
	}
	
	public void refreshFraudCaseActionLogData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudActionLogListGrid.setData(response.getData());
			}
		};
		fraudActionLogListGrid.getDataSource().fetchData(fraudActionLogListGrid.getFilterEditorCriteria(), callBack);
	}		
}
