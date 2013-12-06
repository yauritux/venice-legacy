package com.gdn.venice.client.app.logistic.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.presenter.InvoiceReconciliationPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.InvoiceReconciliationUiHandlers;
import com.gdn.venice.client.app.logistic.widgets.InvoiceReconciliation;
import com.gdn.venice.client.app.logistic.widgets.InvoiceReconciliationProblem;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.PrintUtility;
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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Invoice Reconciliation
 * 
 * @author Henry Chandra
 */
public class InvoiceReconciliationView extends
		ViewWithUiHandlers<InvoiceReconciliationUiHandlers> implements
		InvoiceReconciliationPresenter.MyView {

	RafViewLayout invoiceReconciliationLayout;
	
	VLayout uploadLogVLayout;
	VLayout invoiceReportVLayout;
	VLayout invoiceReconVLayout;
	VLayout airwaybillVLayout;
	
	ListGrid uploadLogListGrid;
	ToolStrip invoiceReconciliationToolStrip;
	
	ListGrid invoiceReportUploadGrid;
	ListGrid invoiceReconciliationListGrid;
	ListGrid logAirwayBillListGrid;
	ToolStripButton submitForApprovalButton;
	
	boolean bDisableSubmitForApprovalButton;
	
	ToolStripButton printButton;
	ToolStripButton exportButton;

	Window uploadWindow;
	Window exportWindow;
	
	DynamicForm exportForm = new DynamicForm();
	private Map<String, String> approvalStatusMap;
	
	@Inject
	public InvoiceReconciliationView() {
		uploadLogVLayout = new VLayout();
		uploadLogVLayout.setHeight(120);
		uploadLogVLayout.setShowResizeBar(true);
		uploadLogVLayout.setHeight(150);
		
		invoiceReportVLayout = new VLayout();
		invoiceReportVLayout.setHeight(120);
		invoiceReportVLayout.setShowResizeBar(true);
		invoiceReportVLayout.setHeight(150);
		
		invoiceReconVLayout = new VLayout();
		invoiceReconVLayout.setHeight(120);
		invoiceReconVLayout.setShowResizeBar(true);
		invoiceReconVLayout.setHeight(150);
		
		airwaybillVLayout = new VLayout();
		airwaybillVLayout.setHeight(120);
		airwaybillVLayout.setShowResizeBar(true);
		airwaybillVLayout.setHeight(150);
		
		invoiceReconciliationLayout = new RafViewLayout();

		invoiceReconciliationToolStrip = new ToolStrip();
		invoiceReconciliationToolStrip.setWidth100();
		
		ToolStripButton uploadInvoice = new ToolStripButton();
		uploadInvoice.setIcon("[SKIN]/icons/up.png");
		uploadInvoice.setTooltip("Upload New Invoice");
		uploadInvoice.setTitle("Upload");
		uploadInvoice.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				buildUploadWindow().show();
			}
		});
		

		submitForApprovalButton = new ToolStripButton();
		submitForApprovalButton.setIcon("[SKIN]/icons/process.png");
		submitForApprovalButton.setTooltip("Submit for Approval");
		submitForApprovalButton.setTitle("Submit");
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Invoice Reconciliation Details");
		printButton.setTitle("Print");

		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export Invoice Reconciliation Details");
		exportButton.setTitle("Export");
		
		invoiceReconciliationToolStrip.addButton(uploadInvoice);
		invoiceReconciliationToolStrip.addSeparator();
		invoiceReconciliationToolStrip.addButton(submitForApprovalButton);
		invoiceReconciliationToolStrip.addSeparator();
		invoiceReconciliationToolStrip.addButton(printButton);
		invoiceReconciliationToolStrip.addSeparator();
		invoiceReconciliationToolStrip.addButton(exportButton);
		
		submitForApprovalButton.setDisabled(true);

		HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
		invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
		invoiceAirwaybillRecordFlow.setWidth100();
		invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a report to show the list of airwaybill numbers</h2>");
		
		invoiceReconVLayout.setMembers(invoiceAirwaybillRecordFlow);
		
		invoiceReconciliationLayout.setMembers(uploadLogVLayout, invoiceReportVLayout, invoiceReconVLayout, airwaybillVLayout);
		
		buildUploadLogListGrid();
		buildInvoiceReportUploadGrid();
		bindCustomUiHandlers();
	}
	
	private void buildInvoiceReportUploadGrid(){
		invoiceReportUploadGrid = new ListGrid() {
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {				
				//Only color the approval status
				if (getFieldName(colNum).equals(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID)) {
					String approvalStatus = record.getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC);
					if (approvalStatus!=null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
						//...and approved, color it dark green
						return "color:#FFFFFF;background-color:#0e7365;";
					} else {
						//...and not approved, color it red
						return "color:#FFFFFF;background-color:#FF0000;";
					}
				}
				return super.getCellCSSText(record, rowNum, colNum);
			}			
		};
		
		invoiceReportUploadGrid.setWidth100();
		invoiceReportUploadGrid.setHeight100();
		invoiceReportUploadGrid.setShowAllRecords(true);
		invoiceReportUploadGrid.setSortField(4);
		invoiceReportUploadGrid.setSortDirection(SortDirection.DESCENDING);
		
		invoiceReportUploadGrid.setSelectionType(SelectionStyle.SIMPLE);
		invoiceReportUploadGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		invoiceReportUploadGrid.setShowFilterEditor(true);
			
		invoiceReportUploadGrid.setCanResizeFields(true);
		invoiceReportUploadGrid.setShowRowNumbers(true);		
	}
	
	private void buildInvoiceReconciliationGrid(){
		invoiceReconciliationListGrid = new InvoiceReconciliation() {
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				String invoiceAirwaybillId = record.getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
				String airwayBillApprovalStatus = record.getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC);
				DataSource reconciliationProblemDataSource = getUiHandlers().onExpandAirwayBillRow(invoiceAirwaybillId);
				return new InvoiceReconciliationProblem(invoiceAirwaybillId, reconciliationProblemDataSource, airwayBillApprovalStatus);
			}
			
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {				
				//Only color the status
				if (getFieldName(colNum).equals(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS)) {
					String resultStatus = record.getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS);
					
					if (resultStatus == null || resultStatus.isEmpty()) {
						return super.getCellCSSText(record, rowNum, colNum);
					}
					//if OK, color it light green					
					if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_OK.toUpperCase())) {
						return "background-color:#00FF00;";
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS.toUpperCase())) {
						//if problem exists...
							return "background-color:#ece355;";
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_NODATAFROMMTA.toUpperCase()) ||
							resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase())) {
						//if no data from MTA and invalid GDN Ref
							return "color:#FFFFFF;background-color:#FF0000;";
					}
				}
				return super.getCellCSSText(record, rowNum, colNum);
			}
		};		
		
		invoiceReconciliationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = invoiceReconciliationListGrid.getSelection();
				if (selectedRecords.length == 1) {
					loadAirwayBillData(LogisticsData.getInvoiceAirwayBillsData(selectedRecords[0].getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID)));
				}
				else
				{
					HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
					invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
					invoiceAirwaybillRecordFlow.setWidth100();
					if (selectedRecords.length==0) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a airwaybill number to show the list of airwaybills</h2>");
					} else if (selectedRecords.length>1) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">More than one airwaybill numbers selected, please select only one airwaybill number to show the list of airwaybills</h2>");
					}
					airwaybillVLayout.setMembers(invoiceAirwaybillRecordFlow);
					invoiceReconciliationLayout.setMembers(uploadLogVLayout, invoiceReportVLayout, invoiceReconVLayout, airwaybillVLayout);
				}
			}
		});
		
		invoiceReconciliationListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				// TODO Auto-generated method stub
				refreshInvoiceAirwayRecordData();
			}
		});
	}
	
	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(360);
		uploadWindow.setHeight(150);
		uploadWindow.setTitle("Upload Invoice");
		uploadWindow.setShowMinimizeButton(false);
		uploadWindow.setIsModal(true);
		uploadWindow.setShowModalMask(true);
		uploadWindow.centerInPage();
		uploadWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				uploadWindow.destroy();
			}
		});
		
		VLayout uploadLayout = new VLayout();
		uploadLayout.setHeight100();
		uploadLayout.setWidth100();

		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setPadding(5);
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setTarget("upload_frame");
		
		final SelectItem providerSelectItem = new SelectItem("Provider");
		providerSelectItem.setTitle("Provider");  
		providerSelectItem.setValueMap(DataConstantNameTokens.LOGISTICPROVIDER_JNE, DataConstantNameTokens.LOGISTICPROVIDER_NCS, DataConstantNameTokens.LOGISTICPROVIDER_RPX);
		
		final TextItem invoiceId = new TextItem("InvoiceNo");
		invoiceId.setTitle("Invoice ID:");
		
		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("Invoice");
		uploadForm.setItems(invoiceId, providerSelectItem, reportFileItem);
		
		HLayout refundButtons = new HLayout(5);
		
		IButton buttonUpload = new IButton("Upload");
		IButton buttonCancel = new IButton("Cancel");
		
		buttonUpload.addClickHandler(new ClickHandler() {			
			@Override	
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();
				/*
				 * Change host to use Geronimo servlet URL in development
				 */
				if(host.contains(":8889")){
					host = "http://localhost:8090/";
				}else{
					host = host.substring(0, host.lastIndexOf("/", host.length()-2)+1);
				}
				String username = MainPagePresenter.signedInUser;
				if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_RPX)) {
					uploadForm.setAction( host + "Venice/InvoiceReportImportRPXServlet?invoiceNumber="+invoiceId.getValueAsString()+"&username=" + username);
				} else if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_NCS)) {
					uploadForm.setAction( host + "Venice/InvoiceReportImportNCSServlet?invoiceNumber="+invoiceId.getValueAsString()+"&username=" + username);
				} else if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_JNE)) {
					uploadForm.setAction( host + "Venice/InvoiceReportImportJNEServlet?invoiceNumber="+invoiceId.getValueAsString()+"&username=" + username);
				}
				uploadForm.submitForm();
				uploadWindow.destroy();
			}
		});		
		buttonCancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				uploadWindow.destroy();
			}
		});
		refundButtons.setAlign(Alignment.CENTER);
		refundButtons.setMembers(buttonUpload, buttonCancel);
		
		uploadLayout.setMembers(uploadForm, refundButtons);
		uploadWindow.addItem(uploadLayout);
		return uploadWindow;
	}
	
	private Window buildExportReportFileWindow() {
		exportWindow = new Window();
		exportWindow.setWidth(360);
		exportWindow.setHeight(200);
		exportWindow.setTitle("Export Invoice Reconciliation");
		exportWindow.setShowMinimizeButton(false);
		exportWindow.setIsModal(true);
		exportWindow.setShowModalMask(true);
		exportWindow.centerInPage();
		
		exportWindow.addCloseClickHandler(new CloseClickHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
					 */
					public void onCloseClick(CloseClientEvent event) {
						exportWindow.destroy();
					}
				});

		VLayout exportLayout = new VLayout();
		exportLayout.setHeight100();
		exportLayout.setWidth100();

		exportForm.setPadding(5);

		final SelectItem logisticProviderItem = new SelectItem(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID);
		logisticProviderItem.setTitle("Logistic Provider");
		logisticProviderItem.setWidth("120");
		final LinkedHashMap<String, String> logisticMap = new LinkedHashMap<String, String>();
		logisticMap.put("JNE", "JNE");
		logisticMap.put("NCS", "NCS");
		logisticMap.put("RPX", "RPX");
		logisticProviderItem.setValueMap(logisticMap);
		logisticProviderItem.setDefaultValue("JNE");

		
		final SelectItem reconStatusItem = new SelectItem(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS);
		reconStatusItem.setTitle("Recon Status");
		LinkedHashMap<String, String> reconMap = new LinkedHashMap<String, String>();
		reconMap.put("All", "All");
		reconMap.put("Problem Exists", "Problem Exists");
		reconStatusItem.setWidth("120");
		reconStatusItem.setValueMap(reconMap);

		final TextItem invoiceNo = new TextItem("InvoiceNo");
		invoiceNo.setTitle("Invoice No:");
		
		exportForm.setFields(
				logisticProviderItem,
				invoiceNo,
				reconStatusItem
			);

		HLayout exportButtons = new HLayout(5);

		IButton buttonExport = new IButton("Export");
		IButton buttonCancel = new IButton("Cancel");

		buttonExport.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				//If in debug mode then change the host URL to the servlet in the server side
				String host = GWT.getHostPageBaseURL();
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
					
				exportForm.setAction(host + "Venice/InvoiceReportExportServlet?logistic="+logisticProviderItem.getValueAsString()+"&invoiceNumber="+invoiceNo.getValueAsString()+"&recon="+reconStatusItem.getValueAsString());
				exportForm.submitForm();
				exportWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				exportWindow.destroy();
			}
		});
		
		exportButtons.setAlign(Alignment.CENTER);
		exportButtons.setMembers(buttonExport, buttonCancel);

		exportLayout.setMembers(exportForm, exportButtons);
		exportWindow.addItem(exportLayout);

		return exportWindow;
	}
	
	@Override
	public Widget asWidget() {
		return invoiceReconciliationLayout;
	}

	private ListGrid buildUploadLogListGrid() {
		uploadLogListGrid = new ListGrid();
		uploadLogListGrid.setWidth100();
		uploadLogListGrid.setHeight100();
		uploadLogListGrid.setShowAllRecords(true);
		uploadLogListGrid.setSortField(9);
		uploadLogListGrid.setSortDirection(SortDirection.DESCENDING);
		uploadLogListGrid.setCanResizeFields(true);
		uploadLogListGrid.setShowRowNumbers(true);
		uploadLogListGrid.setShowFilterEditor(true);
		
		return uploadLogListGrid;
	}
	
	protected void bindCustomUiHandlers() {
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(invoiceReconciliationListGrid);
			}
		});
		
		submitForApprovalButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = invoiceReportUploadGrid.getSelection();
				ArrayList<String> invoiceNumbers = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					invoiceNumbers.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER));
				}
				
				getUiHandlers().onSubmitForApproval(invoiceNumbers);
			}
		});		
		
		invoiceReportUploadGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = invoiceReportUploadGrid.getSelection();
				boolean isNotOK;
				if (selectedRecords.length==0) {
					submitForApprovalButton.setDisabled(true);
				} else {
					isNotOK = false;
					for (ListGridRecord listGridRecord : selectedRecords) {
						String status = listGridRecord.getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC);
						if(status.toUpperCase().contains(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase()) || 
								status.toUpperCase().contains(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED.toUpperCase())){
							isNotOK = true;
							break;
						}							
					}
					submitForApprovalButton.setDisabled(isNotOK);
				}
				
				if (selectedRecords.length==1) {
					loadInvoiceAirwayRecordData(LogisticsData.getInvoiceAirwaybillRecordData(selectedRecords[0].getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID)));
					HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
					invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
					invoiceAirwaybillRecordFlow.setWidth100();
					invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a airwaybill number to show the list of airwaybills</h2>");
					airwaybillVLayout.setMembers(invoiceAirwaybillRecordFlow);
					airwaybillVLayout.setVisibility(Visibility.VISIBLE);
				} else {
					HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
					invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
					invoiceAirwaybillRecordFlow.setWidth100();
					if (selectedRecords.length==0) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a report to show the list of airwaybill numbers</h2>");
					} else if (selectedRecords.length>1) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">More than one reports selected, please select only one reports to show the list of airwaybill numbers</h2>");
					}
					invoiceReconVLayout.setMembers(invoiceAirwaybillRecordFlow);
					airwaybillVLayout.setVisibility(Visibility.HIDDEN);
					invoiceReconciliationLayout.setMembers(uploadLogVLayout, invoiceReportVLayout, invoiceReconVLayout, airwaybillVLayout);
				}
			}
		});
		
		exportButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				buildExportReportFileWindow().show();
			}
		});
		
		uploadLogListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshUploadLogListGridData();
			}
		});
		
		invoiceReportUploadGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshInvoiceReportUploadData();
			}
		});
	}

	@Override
	public void refreshUploadLogListGridData() {
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				uploadLogListGrid.setData(response.getData());
			}
		};
		
		uploadLogListGrid.getDataSource().fetchData(uploadLogListGrid.getFilterEditorCriteria(), callBack);
	}
	
	@Override
	public void loadUploadLogData(DataSource dataSource) {
		
		uploadLogListGrid.setDataSource(dataSource);
		uploadLogListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		uploadLogListGrid.setAutoFetchData(false);
		
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICEUPLOADLOGID).setWidth(75);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICENUMBER).setWidth(75);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAME).setHidden(true);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC).setWidth(120);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_ACTUALFILEUPLOADNAME).setWidth(200);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADFORMAT).setWidth(80);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAME).setHidden(true);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC).setWidth(200);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADSTATUS).setWidth(100);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_TIMESTAMP).setWidth(100);
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADEDBY).setWidth(100);
		
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC).setCellFormatter(new CellFormatter() {			
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
		
		uploadLogListGrid.getField(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC).setCellFormatter(new CellFormatter() {			
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
		
		uploadLogVLayout.addMember(uploadLogListGrid);
	}
	
	public void loadAirwayBillData(DataSource dataSource){
		logAirwayBillListGrid = new ListGrid();
		logAirwayBillListGrid.setAutoFetchData(true);		
		logAirwayBillListGrid.setFilterByCell(false);
		logAirwayBillListGrid.setDataSource(dataSource);
		logAirwayBillListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth(75);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth(75);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth(120);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_WEIGHT).setWidth(120);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT).setWidth(120);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setHidden(true);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DESTINATION).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ZIP).setWidth(100);
		
		Util.formatListGridFieldAsCurrency(logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT));
		Util.formatListGridFieldAsCurrency(logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU));
		Util.formatListGridFieldAsCurrency(logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE));
		Util.formatListGridFieldAsCurrency(logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE));
		Util.formatListGridFieldAsCurrency(logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE));
		
		airwaybillVLayout.setMembers(logAirwayBillListGrid);		
	}

	public void loadInvoiceAirwayRecordData(DataSource dataSource) {
		buildInvoiceReconciliationGrid();
		
		LinkedHashMap<String, String> reconStatus= new LinkedHashMap<String, String>();
		reconStatus.put("OK", "OK");
		reconStatus.put("Problem Exists", "Problem Exists");
		dataSource.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS).setValueMap(reconStatus);
		dataSource.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID).setValueMap(approvalStatusMap);
		
		invoiceReconciliationListGrid.setDataSource(dataSource);
		invoiceReconciliationListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID).setWidth(75);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID).setHidden(true);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER).setWidth(75);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC).setHidden(true);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(75);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT).setWidth(120);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPACKAGEWEIGHT).setWidth(120);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE).setWidth(100);
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE).setWidth(100); 

		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE));
		Util.formatListGridFieldAsCurrency(invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE));
		
		invoiceReconVLayout.setMembers(invoiceReconciliationListGrid);
	}

	public void refreshInvoiceAirwayRecordData() {
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				invoiceReconciliationListGrid.setData(response.getData());
			}
		};
		
		invoiceReconciliationListGrid.getDataSource().fetchData(invoiceReconciliationListGrid.getFilterEditorCriteria(), callBack);
	}
	
	@Override
	public void loadInvoiceReportUploadData(DataSource dataSource, Map<String, String> approval) {
		approvalStatusMap = approval;
		dataSource.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID).setValueMap(approvalStatusMap);
				
		invoiceReportUploadGrid.setDataSource(dataSource);
		invoiceReportUploadGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		invoiceReportUploadGrid.setAutoFetchData(false);
		
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID).setWidth(75);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID).setHidden(true);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER).setWidth(75);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setWidth(75);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION).setWidth(100);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_REPORTRECONCILIATIONTIMESTAMP).setWidth(100);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_USERLOGONNAME).setWidth(100);		
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID).setWidth(100);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC).setHidden(true);
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_APPROVEDBY).setWidth(100);
		
		invoiceReportUploadGrid.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {			
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
		
		invoiceReportVLayout.setMembers(invoiceReconciliationToolStrip, invoiceReportUploadGrid);		
	}

	@Override
	public void refreshInvoiceReportUploadData() {
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				invoiceReportUploadGrid.setData(response.getData());
			}
		};
		
		invoiceReportUploadGrid.getDataSource().fetchData(invoiceReportUploadGrid.getFilterEditorCriteria(), callBack);
	}
}