package com.gdn.venice.client.app.logistic.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.presenter.ActivityReportReconciliationPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.ActivityReportReconciliationUiHandlers;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliation;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliationProblem;
import com.gdn.venice.client.app.logistic.widgets.MerchantPickupDetailWindow;
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
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Activity Report Reconciliation
 * 
 * @author Henry Chandra
 */
public class ActivityReportReconciliationView extends
	ViewWithUiHandlers<ActivityReportReconciliationUiHandlers> implements
	ActivityReportReconciliationPresenter.MyView {

	RafViewLayout activityReportReconciliationLayout;

	VLayout uploadLogVLayout;
	
	ListGrid uploadLogListGrid;
	ListGrid activityReportReconciliationListGrid;
	ToolStripButton submitForApprovalButton;

	ToolStripButton printButton;
	ToolStripButton exportButton;
	
	private ToolStripButton firstButton;
	private ToolStripButton nextButton;
	private ToolStripButton previousButton;
	private ToolStripButton lastButton;
	private final Label pageNumber;
	
	boolean bDisableSubmitForApprovalButton;

	Window uploadWindow;
	Window exportWindow;

	DynamicForm exportForm = new DynamicForm();

	@Inject
	public ActivityReportReconciliationView() {

		uploadLogVLayout = new VLayout();
		uploadLogVLayout.setHeight(120);
		uploadLogVLayout.setShowResizeBar(true);
		uploadLogVLayout.setHeight(150);
		
		activityReportReconciliationLayout = new RafViewLayout();
	
		ToolStrip activityReportReconciliationToolStrip = new ToolStrip();
		activityReportReconciliationToolStrip.setWidth100();
		
		ToolStripButton uploadReport = new ToolStripButton();
		uploadReport.setIcon("[SKIN]/icons/up.png");
		uploadReport.setTooltip("Upload New Report");
		uploadReport.setTitle("Upload");
		uploadReport.addClickHandler(new ClickHandler() {			
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
		printButton.setTooltip("Print Activity Reconciliation Details");
		printButton.setTitle("Print");
		
		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export Activity Reconciliation Details");
		exportButton.setTitle("Export");
		
		firstButton = new ToolStripButton("1");
		firstButton.setTooltip("Current page");
		firstButton.setDisabled(true);
		
		previousButton = new ToolStripButton("Prev");
		previousButton.setTooltip("Go to previous page");
		previousButton.setDisabled(true);
		
		nextButton = new ToolStripButton("Next");
		nextButton.setTooltip("Go to next page");
		
		lastButton = new ToolStripButton();
		lastButton.setTooltip("Go to last page");
	
		pageNumber = new Label(" 1 ");
		pageNumber.setTooltip("Current page");
		pageNumber.setWidth(2);
		pageNumber.setAutoWidth();
		pageNumber.setVisible(false);
		
		submitForApprovalButton.setDisabled(true);
		     
		activityReportReconciliationToolStrip.addButton(uploadReport);
		activityReportReconciliationToolStrip.addSeparator();
		activityReportReconciliationToolStrip.addButton(submitForApprovalButton);
		activityReportReconciliationToolStrip.addSeparator();
		activityReportReconciliationToolStrip.addButton(printButton);
		activityReportReconciliationToolStrip.addSeparator();
		activityReportReconciliationToolStrip.addButton(exportButton);
		
//		activityReportReconciliationToolStrip.addButton(previousButton);
//		activityReportReconciliationToolStrip.addButton(firstButton);
//		activityReportReconciliationToolStrip.addMember(pageNumber);
//		activityReportReconciliationToolStrip.addButton(lastButton);
//		activityReportReconciliationToolStrip.addButton(nextButton);
		
		activityReportReconciliationListGrid = new ActivityReportReconciliation() {
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				String airwayBillId = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
				String airwayBillApprovalStatus = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC);
				DataSource reconciliationProblemDataSource = getUiHandlers().onExpandAirwayBillRow(airwayBillId);
				return new ActivityReportReconciliationProblem(airwayBillId, reconciliationProblemDataSource, airwayBillApprovalStatus);
			}
		
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {				
				//Only color the status and approval status
				if (getFieldName(colNum).equals(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS) ||
						getFieldName(colNum).equals(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID)) {
					String resultStatus = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS);
					String approvalStatus = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC);
					
					if (resultStatus == null || resultStatus.isEmpty()) {
						return super.getCellCSSText(record, rowNum, colNum);
					}
					//if OK, color it light green
					if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_OK.toUpperCase())) {
						return "background-color:#00FF00;";
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS.toUpperCase())) {
						//if problem exists...
						if (approvalStatus!=null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							//...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							//...and not approved, color it yellow
							return "background-color:#ece355;";
						}
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_NODATAFROMMTA.toUpperCase()) ||
							resultStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase())) {
						//if no data from MTA and invalid GDN Ref
						if (approvalStatus!=null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							//...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							//...and not approved, color it red
							return "color:#FFFFFF;background-color:#FF0000;";
						}
					}
				}
				return super.getCellCSSText(record, rowNum, colNum);
			}
		};
	
		activityReportReconciliationLayout.setMembers(uploadLogVLayout, activityReportReconciliationToolStrip);
		buildUploadLogListGrid();
		bindCustomUiHandlers();
	}

	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(360);
		uploadWindow.setHeight(120);
		uploadWindow.setTitle("Upload Activity Report");
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
		providerSelectItem.setValueMap(DataConstantNameTokens.LOGISTICPROVIDER_JNE, DataConstantNameTokens.LOGISTICPROVIDER_NCS, DataConstantNameTokens.LOGISTICPROVIDER_RPX, DataConstantNameTokens.LOGISTICPROVIDER_MSG);
		
		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("Activity Report");
		uploadForm.setItems(providerSelectItem, reportFileItem);
		
		HLayout uploadCancelButtons = new HLayout(5);
		
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
				if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_RPX)) {
						uploadForm.setAction(host + "Venice/ActivityReportImportRPXServlet?username=" + MainPagePresenter.signedInUser);
				} else if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_NCS)) {
						uploadForm.setAction(host + "Venice/ActivityReportImportNCSServlet?username=" + MainPagePresenter.signedInUser);
				} else if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_JNE)) {
						uploadForm.setAction(host + "Venice/ActivityReportImportJNEServlet?username=" + MainPagePresenter.signedInUser);
				} else if (providerSelectItem.getValueAsString().equals(DataConstantNameTokens.LOGISTICPROVIDER_MSG)) {
						uploadForm.setAction(host + "Venice/ActivityReportImportMSGServlet?username=" + MainPagePresenter.signedInUser);
				}
				uploadForm.submitForm();
				uploadWindow.destroy();
				
	//			activityReportReconciliationListGrid.fetchData(new Criteria(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID,"382532"));  
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				uploadWindow.destroy();
			}
		});
		uploadCancelButtons.setAlign(Alignment.CENTER);
		uploadCancelButtons.setMembers(buttonUpload, buttonCancel);
		
		uploadLayout.setMembers(uploadForm, uploadCancelButtons);
		uploadWindow.addItem(uploadLayout);
		return uploadWindow;
	}

	private Window buildExportReportFileWindow() {
		exportWindow = new Window();
		exportWindow.setWidth(360);
		exportWindow.setHeight(170);
		exportWindow.setTitle("Export Activity Reconciliation");
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
				
		final SelectItem approvalStatusItem = new SelectItem(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID);
		approvalStatusItem.setTitle("Approval Status");
		approvalStatusItem.setWidth("120");
		
		//Request approval combo
		RPCRequest requestApproval=new RPCRequest();
		requestApproval = new RPCRequest();
		requestApproval.setActionURL(GWT.getHostPageBaseURL() + "ActivityReportReconciliationPresenterServlet?method=fetchApprovalStatusComboBoxData&type=RPC");
		requestApproval.setHttpMethod("POST");
		requestApproval.setUseSimpleHttp(true);
		requestApproval.setShowPrompt(false);
		RPCManager.sendRequest(requestApproval, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseApproval = rawData.toString();
						String xmlDataApproval = rpcResponseApproval;
						final LinkedHashMap<String, String> approvalMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataApproval));
						approvalMap.put("all", "All");
						
						//Request logistic combo
						RPCRequest requestLogistic = new RPCRequest();
						requestLogistic.setActionURL(GWT.getHostPageBaseURL() + "ActivityReportReconciliationPresenterServlet?method=fetchLogisticProviderComboBoxData&type=RPC");
						requestLogistic.setHttpMethod("POST");
						requestLogistic.setUseSimpleHttp(true);
						requestLogistic.setShowPrompt(false);
						
						RPCManager.sendRequest(requestLogistic, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseLogistic = rawData.toString();
										String xmlDataLogistic = rpcResponseLogistic;
										final LinkedHashMap<String, String> logisticMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataLogistic));
										logisticMap.put("all", "All");
										
										approvalStatusItem.setValueMap(approvalMap);
										logisticProviderItem.setValueMap(logisticMap);
									}
						});
				}
		});
		
		final SelectItem reconStatusItem = new SelectItem(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS);
		reconStatusItem.setTitle("Recon Status");
		LinkedHashMap<String, String> reconMap = new LinkedHashMap<String, String>();  
		reconMap.put("OK", "OK");
		reconMap.put("Problem Exists", "Problem Exists");
		reconMap.put("No Data from MTA", "No Data from MTA");		
		reconMap.put("None", "None");
		reconMap.put("all", "All");
		reconStatusItem.setWidth("120");
		reconStatusItem.setValueMap(reconMap);
	
	    final DateTimeItem exportDateItem = new DateTimeItem(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP);
	    exportDateItem.setTitle("Order Date From");
	    exportDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
	    exportDateItem.setWidth("140");
		exportForm.setFields(
				logisticProviderItem,
				approvalStatusItem,
				reconStatusItem,
				exportDateItem
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
					
				exportForm.setAction(host + "Venice/ActivityReportExportServlet?logistic="+logisticProviderItem.getValueAsString()+"&approval="+approvalStatusItem.getValueAsString()+"&recon="+reconStatusItem.getValueAsString()+"&date="+exportDateItem.getValue().toString());
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
		return activityReportReconciliationLayout;
	}
	

	private ListGrid buildUploadLogListGrid() {
		uploadLogListGrid = new ListGrid();
		uploadLogListGrid.setWidth100();
		uploadLogListGrid.setHeight100();
		uploadLogListGrid.setShowAllRecords(true);
		uploadLogListGrid.setSortField(0);
		uploadLogListGrid.setCanResizeFields(true);
		uploadLogListGrid.setShowRowNumbers(true);
		uploadLogListGrid.setShowFilterEditor(true);
//		uploadLogListGrid.setAutoFetchData(true);
		
		return uploadLogListGrid;
	}
		
	protected void bindCustomUiHandlers() {
		printButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(activityReportReconciliationListGrid);
			}
		});
		
		submitForApprovalButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = activityReportReconciliationListGrid.getSelection();
				ArrayList<String> airwayBillIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				}				
				getUiHandlers().onSubmitForApproval(airwayBillIds);
			}
		});
				
		activityReportReconciliationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = activityReportReconciliationListGrid.getSelection();
				if (selectedRecords.length==0) {
					//If no records selected, disable Submit For Approval Button
					submitForApprovalButton.setDisabled(true);
				} else {
					for (int i=0;i<selectedRecords.length;i++) {
						//If it has been submitted or "No Data from MTA", disable Submit For Approval Button
						//also disable Submit For Approval Button if it has been approved
						bDisableSubmitForApprovalButton = selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED) || 
							selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS).equals(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_NODATAFROMMTA)||
							selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED);
						submitForApprovalButton.setDisabled(bDisableSubmitForApprovalButton);
						if (bDisableSubmitForApprovalButton) {
							break;
						} else {
							//If it hasn't been submitted, 
							//If the status is "Problem Exists", get the Problem's DataSource and fetch the data
							String resultStatus =  selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS);
							if (resultStatus!=null && resultStatus.equals(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_PROBLEMEXISTS)) {
								String airwayBillId =  selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
								
								DataSource reconciliationProblemDataSource = getUiHandlers().onExpandAirwayBillRow(airwayBillId);
								reconciliationProblemDataSource.fetchData(null, new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if (bDisableSubmitForApprovalButton) {
											//if already disabled for one record, no need to check the rest
											return;
										}
										Record[] records = response.getData();
										for (int j=0;j<records.length;j++) {
											//If there are problems with no "Action Applied" (null or empty), disable Submit For Approval Button
											bDisableSubmitForApprovalButton = records[j].getAttributeAsString(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID) == null || 
												records[j].getAttributeAsString(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID).isEmpty();
											submitForApprovalButton.setDisabled(bDisableSubmitForApprovalButton);
											if (bDisableSubmitForApprovalButton) {
												break;
											}
										}
									}
								});
							}
							if (bDisableSubmitForApprovalButton) {
								break;
							}
						}
					}
				}
			}
		});
		
		activityReportReconciliationListGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				if (activityReportReconciliationListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME)) {
					String airwayBillId = event.getRecord().getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
					Window merchantPickupDetailWindow = new MerchantPickupDetailWindow(getUiHandlers().onShowMerchantPickUpDetail(airwayBillId));
					merchantPickupDetailWindow.show();
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

		firstButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				goToPage(1);
			}
		});
		
		nextButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				goToPage(Integer.parseInt(pageNumber.getContents().trim()) + 1);
			}
		});
		
		lastButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				goToPage(Integer.parseInt(lastButton.getTitle()));
			}
		});
		
		previousButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				goToPage(Integer.parseInt(pageNumber.getContents().trim()) - 1);
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
	}

	
	@Override
	public void loadUploadLogData(DataSource dataSource) {
		
		uploadLogListGrid.setDataSource(dataSource);
		uploadLogListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		uploadLogListGrid.setAutoFetchData(false);
		
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADLOGID).setWidth(75);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAME).setHidden(true);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC).setWidth(120);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_ACTUALFILEUPLOADNAME).setWidth(200);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADFORMAT).setWidth(80);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC).setWidth(200);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_UPLOADSTATUS).setWidth(100);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_TIMESTAMP).setWidth(100);
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_USERNAME).setWidth(100);
		
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC).setCellFormatter(new CellFormatter() {			
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
		
		uploadLogListGrid.getField(DataNameTokens.LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC).setCellFormatter(new CellFormatter() {			
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
	public void loadAirwayBillData(DataSource dataSource, Map<String,String> approval, Map<String,String> status) {
		dataSource.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setValueMap(status);
		dataSource.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID).setValueMap(approval);
		
		LinkedHashMap<String, String> reconStatus= new LinkedHashMap<String, String>();
		reconStatus.put("OK", "OK");
		reconStatus.put("Problem Exists", "Problem Exists");
		reconStatus.put("No Data from MTA", "No Data from MTA");
		dataSource.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS).setValueMap(reconStatus);
		
		activityReportReconciliationListGrid.setDataSource(dataSource);
		activityReportReconciliationListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		activityReportReconciliationListGrid.setAutoFetchData(false);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth(75);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth(75);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth(120);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setHidden(true);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER).setWidth(120);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).setHidden(true);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID).setWidth(125);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME).setCanFilter(false);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setCanFilter(false);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DESTINATION).setWidth(100);
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ZIP).setWidth(100);
		
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				return "<span style='color:blue;text-decoration:underline;cursor:hand;cursor:pointer'>"+value+"</span>";
			}
		});
		
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setCellFormatter(new CellFormatter() {			
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
		
		activityReportReconciliationListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setGroupTitleRenderer(new GroupTitleRenderer() {			
			@Override
			public String getGroupTitle(Object groupValue, GroupNode groupNode,
					ListGridField field, String fieldName, ListGrid grid) {
				String groupTitle = (String) groupValue;
				
				if (!groupTitle.startsWith("/")) {
					return groupTitle;
				}
				
				groupTitle = groupTitle.substring(groupTitle.lastIndexOf("/")+1, groupTitle.length());
				return "<a href='" + GWT.getHostPageBaseURL() +
				MainPagePresenter.fileDownloadPresenterServlet +
				"?filename=" + groupValue + "' target='_blank'>" + 
				groupTitle + "</a>";
			}
		});
			
		activityReportReconciliationListGrid.groupBy(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC);		
		activityReportReconciliationLayout.addMember(activityReportReconciliationListGrid);
	}

	@Override
	public void refreshAirwayBillData() {
		if (activityReportReconciliationListGrid instanceof ActivityReportReconciliation) {
			((ActivityReportReconciliation) activityReportReconciliationListGrid).refreshAirwayBillData();
		}
	}
	
	public void goToPage(int pageNum) {
        if (pageNum < 1)
                pageNum = 1;
        pageNumber.setContents(" " + pageNum + " ");
        updatePage(pageNum);
	}
	
	public void updatePage(int pageNum) {
		getUiHandlers().onFetchComboBoxData((pageNum - 1) * 50, 50);
		
		if(pageNum == 1 && pageNum == Integer.parseInt(lastButton.getTitle().trim())){
   			previousButton.setDisabled(true);
   			firstButton.setDisabled(true);
   			lastButton.setDisabled(true);
   			nextButton.setDisabled(true);
   			pageNumber.setVisible(false);
		}
		else if(pageNum == 1){
   			previousButton.setDisabled(true);
   			firstButton.setDisabled(true);
   			lastButton.setDisabled(false);
   			nextButton.setDisabled(false);
   			pageNumber.setVisible(false);
   		}
   		else{ 
   			if(pageNum == Integer.parseInt(lastButton.getTitle().trim())){
   			previousButton.setDisabled(false);
   			firstButton.setDisabled(false);
   			lastButton.setDisabled(true);
   			nextButton.setDisabled(true);
   			pageNumber.setVisible(false);
	   		}
	   		else{
	   			previousButton.setDisabled(false);
	   			firstButton.setDisabled(false);
	   			lastButton.setDisabled(false);
	   			nextButton.setDisabled(false);
	   			pageNumber.setVisible(true);
	   		}
   		}
	}
	
	@Override
	public void setLastPage(int totalRows){
		int lastPage = totalRows/50+1;
		lastButton.setTitle(" "+lastPage+" ");
		if(Integer.parseInt(lastButton.getTitle().trim()) == 1)
		{
			lastButton.setVisible(false);
			nextButton.setDisabled(true);
		}
		else 
		{
			lastButton.setVisible(true);
			nextButton.setDisabled(false);
		}
	}
}
