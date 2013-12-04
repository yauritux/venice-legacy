package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Widget for Fraud Attachment Viewer
 * 
 * @author Roland
 */

public class FraudCaseViewerManagementAttachmentTab extends Tab {
	ListGrid attachmentListGrid = null;

	public FraudCaseViewerManagementAttachmentTab(String title, DataSource attachmentData) {
		super(title);		

		attachmentListGrid = new ListGrid();
		attachmentListGrid.setWidth100();
		attachmentListGrid.setHeight100();
		attachmentListGrid.setShowAllRecords(true);
		attachmentListGrid.setSortField(0);
		attachmentListGrid.setAutoFetchData(true);
		attachmentListGrid.setDataSource(attachmentData);
		attachmentListGrid.setShowFilterEditor(true);
		attachmentListGrid.setFields(Util.getListGridFieldsFromDataSource(attachmentData));
		attachmentListGrid.setCanSort(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID).setHidden(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID).setHidden(true);
		attachmentListGrid.setCanResizeFields(true);
		attachmentListGrid.setShowRowNumbers(true);
		attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION).setCellFormatter(new CellFormatter() {
			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				String cellFormat = (String) value;
				cellFormat = cellFormat.substring(cellFormat.lastIndexOf("/")+1, cellFormat.length());
				return "<a href='" + GWT.getHostPageBaseURL() +
					MainPagePresenter.fileDownloadPresenterServlet +
					"?filename=" + value + "' target='_blank'>" + 
					cellFormat + "</a>";
			}
		});
		
		VLayout attachmentLayout = new VLayout();
		attachmentLayout.setMembers(attachmentListGrid);
		setPane(attachmentLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		attachmentListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseViewerAttachmentData();
			}
		});
	}
	
	public void refreshFraudCaseViewerAttachmentData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				attachmentListGrid.setData(response.getData());
			}
		};
		attachmentListGrid.getDataSource().fetchData(attachmentListGrid.getFilterEditorCriteria(), callBack);
	}		
}
