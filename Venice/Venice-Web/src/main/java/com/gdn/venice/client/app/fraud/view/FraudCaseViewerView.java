package com.gdn.venice.client.app.fraud.view;

import java.util.LinkedHashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudCaseViewerPresenter;
import com.gdn.venice.client.app.fraud.ui.widgets.FraudCaseViewerLayout;
import com.gdn.venice.client.app.fraud.ui.widgets.FraudCaseViewerModifyLayout;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCaseViewerUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Fraud Case Viewer
 * 
 * @author Roland
 */
public class FraudCaseViewerView extends
ViewWithUiHandlers<FraudCaseViewerUiHandlers> implements
FraudCaseViewerPresenter.MyView {
	private static final int LIST_HEIGHT = 200;
	
	RafViewLayout fraudCaseManagementLayout;
	VLayout fraudCaseManagementDetailLayout;
	
	FraudCaseViewerLayout fraudCaseCustomerContentLayout;
	FraudCaseViewerModifyLayout fraudCaseCustomerContentModifyLayout;
	
	ListGrid fraudCaseManagementListGrid;
	VLayout fraudCaseManagementListLayout = new VLayout();
	
	Window reportParameterWindow;
	
	@Inject
	public FraudCaseViewerView() {
		fraudCaseManagementLayout = new RafViewLayout();
		fraudCaseManagementListLayout.setHeight(LIST_HEIGHT);
		fraudCaseManagementListLayout.setShowResizeBar(true);		
		fraudCaseManagementDetailLayout = new VLayout();
		fraudCaseManagementDetailLayout.setWidth100();
		
		HTMLFlow fraudCaseDetailFlow = new HTMLFlow();
		fraudCaseDetailFlow.setAlign(Alignment.CENTER);
		fraudCaseDetailFlow.setWidth100();
		fraudCaseDetailFlow.setContents("");
		
		ToolStrip uncalculatedCreditCardOrderToolStrip = new ToolStrip();
		uncalculatedCreditCardOrderToolStrip.setWidth100();
		
		//button for print report
		ToolStripButton printCaseButton = new ToolStripButton();
		printCaseButton.setIcon("[SKIN]/icons/notes_accept.png");  
		printCaseButton.setTooltip("Click here to Export Order detail By Status"); 
		printCaseButton.setTitle("Export Order to Excel By Status");
		
		printCaseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	
				buildReportParameterWindow().show();
			}
		});
		
		uncalculatedCreditCardOrderToolStrip.addButton(printCaseButton);
		
		fraudCaseManagementDetailLayout.setMembers(fraudCaseDetailFlow);
		fraudCaseManagementLayout.setMembers(uncalculatedCreditCardOrderToolStrip,fraudCaseManagementListLayout, fraudCaseManagementDetailLayout);
		buildFraudCaseManagementGrid();
		
		bindCustomUiHandlers();
	}

	private ListGrid buildFraudCaseManagementGrid() {
		fraudCaseManagementListGrid = new ListGrid();
		fraudCaseManagementListGrid.setWidth100();
		fraudCaseManagementListGrid.setHeight100();
		fraudCaseManagementListGrid.setShowAllRecords(false);
		fraudCaseManagementListGrid.setShowRowNumbers(true);
		fraudCaseManagementListGrid.setSortField(0);
		fraudCaseManagementListGrid.setCanHover(true);
//		fraudCaseManagementListGrid.setAutoFetchData(true);	
		fraudCaseManagementListGrid.setCanResizeFields(true);		
		fraudCaseManagementListGrid.setShowFilterEditor(true);
		
		return fraudCaseManagementListGrid;
	}
	
	private void showFraudCaseDetails(Record record) {
		String caseId = record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
		String orderId = record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID);
		List<DataSource> dataSources = getUiHandlers().onShowFraudCaseDetailData(caseId,orderId);
		
		if (record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_ENABLEMODIFYAFTERCOMPLETED).equalsIgnoreCase("false")) {
			fraudCaseCustomerContentLayout = new FraudCaseViewerLayout(
					dataSources.get(0), //Total risk score data
					dataSources.get(1), //Customer summary data
					dataSources.get(2), //Customer data
					dataSources.get(3), //Customer address data 
					dataSources.get(4), //Customer contact detail data
					dataSources.get(5), //Order detail data 
					dataSources.get(6), //Order item data
					dataSources.get(7), //Payment data
					dataSources.get(8), //Risk score data
					dataSources.get(9), //Related order data
					dataSources.get(10), //Fraud management data
					dataSources.get(11), //Action log data
					dataSources.get(12), //History log data
					dataSources.get(13), //Payment type data
					dataSources.get(14), //Ilog fraud status data
					dataSources.get(15), //attachment data
					dataSources.get(16), //Payment Summary data
					dataSources.get(17), //Payment Detail data
					dataSources.get(18), //White List data
					dataSources.get(19), //Filter Order History
					caseId,
					dataSources.get(20),//contact detail data
					dataSources.get(21)//category data
					);
			
			fraudCaseManagementDetailLayout.setMembers(fraudCaseCustomerContentLayout);
		} else {
			fraudCaseCustomerContentModifyLayout = new FraudCaseViewerModifyLayout(
					dataSources.get(0), //Total risk score data
					dataSources.get(1), //Customer summary data
					dataSources.get(2), //Customer data
					dataSources.get(3), //Customer address data 
					dataSources.get(4), //Customer contact detail data
					dataSources.get(5), //Order detail data 
					dataSources.get(6), //Order item data
					dataSources.get(7), //Payment data
					dataSources.get(8), //Risk score data
					dataSources.get(9), //Related order data
					dataSources.get(10), //Fraud management data
					dataSources.get(11), //Action log data
					dataSources.get(12), //History log data
					dataSources.get(13), //Payment type data
					dataSources.get(14), //Ilog fraud status data
					dataSources.get(15), //attachment data
					dataSources.get(16), //Payment Summary data
					dataSources.get(17), //Payment Detail data
					dataSources.get(18), //White List data
					dataSources.get(19), //Filter Order History
					caseId,
					dataSources.get(20),//contact detail data
					dataSources.get(21)//category data
					);
			
			fraudCaseManagementDetailLayout.setMembers(fraudCaseCustomerContentModifyLayout);
		}	
	}

	@Override
	public Widget asWidget() {
		return fraudCaseManagementLayout;
	}

	protected void bindCustomUiHandlers() {	
		fraudCaseManagementListGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				Record record = event.getRecord();
				showFraudCaseDetails(record);
			}
		});
		
		fraudCaseManagementListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseData();
			}
		});
	}
	
	@Override
	public void loadFraudCaseData(DataSource dataSource) {
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(dataSource);
		ListGridField finalListGridField[] = {listGridField[0], listGridField[7], listGridField[2], listGridField[3], listGridField[8], listGridField[4], listGridField[5], listGridField[9]};
		
		fraudCaseManagementListGrid.setDataSource(dataSource);
		fraudCaseManagementListGrid.setFields(finalListGridField);
		
		LinkedHashMap<String, String> statusMap = new LinkedHashMap<String, String>();  
		statusMap.put("SF", "SF");
		statusMap.put("FP", "FP");
		statusMap.put("FC", "FC");		
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC).setValueMap(statusMap);
		
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID).setWidth(90);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).setWidth(120);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).setAlign(Alignment.LEFT);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC).setWidth(75);
//		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE).setWidth(120);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE).setAlign(Alignment.LEFT);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS).setWidth(100);
		fraudCaseManagementListGrid.getField(DataNameTokens.TASKSTATUS).setWidth(75);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_LASTACTION).setWidth(160);
		fraudCaseManagementListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_LASTACTION).setCanFilter(false);
		
		//can not be filter because this field not from database
		fraudCaseManagementListGrid.getField(DataNameTokens.TASKSTATUS).setCanFilter(false); 
		fraudCaseManagementListLayout.addMember(fraudCaseManagementListGrid);
	}

	@Override
	public void refreshFraudCaseData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudCaseManagementListGrid.setData(response.getData());
			}
		};

		fraudCaseManagementListGrid.getDataSource().fetchData(fraudCaseManagementListGrid.getFilterEditorCriteria(), callBack);
		if(fraudCaseCustomerContentLayout != null) {
			if(fraudCaseManagementDetailLayout.hasMember(fraudCaseCustomerContentLayout)) {
				fraudCaseManagementDetailLayout.removeMember(fraudCaseCustomerContentLayout);
			}
		}
	}
	
	
	private Window buildReportParameterWindow() {
		reportParameterWindow = new Window();
		reportParameterWindow.setWidth(360);
		reportParameterWindow.setHeight(200);
		reportParameterWindow.setShowMinimizeButton(false);
		reportParameterWindow.setIsModal(true);
		reportParameterWindow.setShowModalMask(true);
		reportParameterWindow.centerInPage();
		reportParameterWindow.setTitle("Export to Excel ?");
		reportParameterWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				reportParameterWindow.destroy();
			}
		});
		VLayout reportParametersLayout = new VLayout();
		reportParametersLayout.setHeight100();
		reportParametersLayout.setWidth100();

		final DynamicForm reportParametersForm = new DynamicForm();
		reportParametersForm.setPadding(5);
		reportParametersForm.setEncoding(Encoding.MULTIPART);
		reportParametersForm.setTarget("upload_frame");	
		    
		VLayout all = new VLayout();		
				
		final SelectItem statusItem = new SelectItem(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE);
		statusItem.setTitle("Pilih Status");	
		LinkedHashMap<String, String> reconStatusMap = new LinkedHashMap<String, String>();  
		reconStatusMap.put("0", "VA");
		reconStatusMap.put("1", "C");
		reconStatusMap.put("2", "SF");
		reconStatusMap.put("3", "FC");
		reconStatusMap.put("4", "FP");
		reconStatusMap.put("6", "X");
		reconStatusMap.put("all", "All");
		statusItem.setValueMap(reconStatusMap);
		
		final DateTimeItem exportFromDateItem = new DateTimeItem(DataNameTokens.VENORDER_ORDERDATE);
        exportFromDateItem.setTitle("Date From");
        exportFromDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        exportFromDateItem.setWidth("140");
        
        final DateTimeItem exportEndDateItem = new DateTimeItem(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE);
        exportEndDateItem.setTitle("Date End");
        exportEndDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        exportEndDateItem.setWidth("140");
        
        reportParametersForm.setFields(
        		statusItem,
        		exportFromDateItem,
        		exportEndDateItem
			);
		
		HLayout dialogButtons = new HLayout(5);

		Label label = new Label();
		label.setContents("Mohon Tunggu Beberapa Detik untuk Export Data Ke file Excel!! ");
		label.setAlign(Alignment.CENTER);
		label.setOverflow(Overflow.HIDDEN);
		
		IButton buttonLaunch = new IButton("Launch Report");
		IButton buttonCancel = new IButton("Cancel");
		
		buttonLaunch.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();

				//If in debug mode then change the host URL to the servlet in the server side
				if(host.contains("8889")){
					host = "http://localhost:8090/";
				}
				
				/* 
				 * Somehow when the app is deployed in Geronimo the getHostPageBaseURL call
				 * adds the context root of "Venice/" as it is the web application.
				 * This does not happen in development mode as it is running in the root
				 * of the Jetty servlet container.
				 * 
				 * Consequently the context root needs to be removed because the servlet 
				 * being called has its own context root in a different web application.
				 */
				if(host.contains("Venice/")){
					host = host.substring(0, host.indexOf("Venice/"));
				}
												
				//Comment in to see the values of the various properties of the date fields
//				System.out.println("fromDateItem.getValueAsDate()" + fromDateItem.getValueAsDate());
//				System.out.println("fromDateItem.getDisplayField" + fromDateItem.getDisplayField());
//				System.out.println("fromDateItem.getDisplayValue" + fromDateItem.getDisplayValue());
//				System.out.println("fromDateItem.getValueField" + fromDateItem.getValueField());
//				System.out.println("fromDateItem.getValue" + fromDateItem.getValue());
				
						
				/*
				 * Call the relevent report servlet based ont he value of reportId
				 */
				reportParametersForm.setAction(GWT.getHostPageBaseURL()  + "FraudCaseMaintenancePresenterServlet?type=downloadFraudAllOrderHistory&statusId="+statusItem.getValueAsString()+"&from="+exportFromDateItem.getValue().toString()+"&end="+exportEndDateItem.getValue().toString());								
				System.out.println("Form Action:" + reportParametersForm.getAction());
				reportParametersForm.submitForm();
				
				//Comment in to check the action on the form
				//SC.say("Request submitted:" + reportParametersForm.getAction());

				reportParameterWindow.destroy();
			}		
		});
		
		
		/*
		 * The click handler for the cancel button just destroys the window
		 */
		buttonCancel.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				reportParameterWindow.destroy();
			}
		});
		
		//Align and add the members to the strip and the layout
		
		dialogButtons.setAlign(Alignment.CENTER);
		dialogButtons.setMembers(buttonLaunch, buttonCancel);
		all.setAlign(Alignment.LEFT);
		all.setMembers(reportParametersForm,label,dialogButtons);
		reportParametersLayout.setMembers(all);
		reportParameterWindow.addItem(reportParametersLayout);
		return reportParameterWindow;	
				
	}
}
