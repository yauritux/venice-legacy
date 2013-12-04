package com.gdn.venice.client.app.logistic.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.presenter.DeliveryStatusTrackingPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.DeliveryStatusTrackingUiHandlers;
import com.gdn.venice.client.app.logistic.widgets.DeliveryStatusTrackingDetailWindow;
import com.gdn.venice.client.app.logistic.widgets.MerchantPickupDetailWindow;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Delivery Status Tracking
 * 
 * @author Henry Chandra
 */
public class DeliveryStatusTrackingView extends
		ViewWithUiHandlers<DeliveryStatusTrackingUiHandlers> implements
		DeliveryStatusTrackingPresenter.MyView {

	RafViewLayout deliveryStatusTrackingLayout;	
	ListGrid deliveryStatusTrackingListGrid;
	Window exportWindow;
	DynamicForm exportForm = new DynamicForm();
	
	/*
	 * The toolstrip objects for the header
	 */
	ToolStrip deliveryStatusTrackingToolStrip;
	ToolStripButton printButton;
	ToolStripButton exportButton;
	
	
	@Inject
	public DeliveryStatusTrackingView() {		
		deliveryStatusTrackingToolStrip = new ToolStrip();
		deliveryStatusTrackingToolStrip.setWidth100();
		deliveryStatusTrackingToolStrip.setPadding(2);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Journal Voucher Details");
		printButton.setTitle("Print");
		
		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export Delivery Status Tracking Details");
		exportButton.setTitle("Export");
		
		deliveryStatusTrackingToolStrip.addButton(printButton);
		deliveryStatusTrackingToolStrip.addSeparator();
		deliveryStatusTrackingToolStrip.addButton(exportButton);

		deliveryStatusTrackingLayout = new RafViewLayout();
		
		deliveryStatusTrackingListGrid = new ListGrid();		
		deliveryStatusTrackingListGrid.setWidth100();
		deliveryStatusTrackingListGrid.setHeight100();
		deliveryStatusTrackingListGrid.setShowAllRecords(true);
		deliveryStatusTrackingListGrid.setSortField(0);
		
		deliveryStatusTrackingListGrid.setShowFilterEditor(true);		
//		deliveryStatusTrackingListGrid.setAutoFetchData(true);	
		deliveryStatusTrackingListGrid.setCanResizeFields(true);
		deliveryStatusTrackingListGrid.setShowRowNumbers(true);
		deliveryStatusTrackingListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		
		deliveryStatusTrackingListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshDeliveryStatusTrackingData();
			}
		});
	
		deliveryStatusTrackingListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				deliveryStatusTrackingListGrid.saveAllEdits();
//				refreshDeliveryStatusTrackingData();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}				
			}
		});
		
		bindCustomUiHandlers();
	}
	
	@Override
	public Widget asWidget() {
		return deliveryStatusTrackingLayout;
	}
	
	protected void bindCustomUiHandlers() {
		deliveryStatusTrackingListGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				if (deliveryStatusTrackingListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME)) {
					String airwayBillId = event.getRecord().getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
					Window merchantPickupDetailWindow = new MerchantPickupDetailWindow(getUiHandlers().onShowMerchantPickUpDetail(airwayBillId));
					merchantPickupDetailWindow.show();
				}
				
			}
		});
		
		deliveryStatusTrackingListGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {				
				if (deliveryStatusTrackingListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.LOGAIRWAYBILL_DETAIL)) {
					String airwayBillId = event.getRecord().getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
					String orderItemId = event.getRecord().getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID);
					Window deliveryStatusTrackingDetailWindow = new DeliveryStatusTrackingDetailWindow(getUiHandlers().onShowDeliveryStatusTrackingDetail(airwayBillId, orderItemId));
					deliveryStatusTrackingDetailWindow.show();					
				}				
			}
		});
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(deliveryStatusTrackingListGrid);
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
	}
	
	private Window buildExportReportFileWindow() {
		exportWindow = new Window();
		exportWindow.setWidth(360);
		exportWindow.setHeight(170);
		exportWindow.setTitle("Export Delivery Status Tracking");
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
		
		final SelectItem orderStatusItem = new SelectItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID);
		orderStatusItem.setTitle("Order Status");
		orderStatusItem.setWidth("120");
		
		//Request approval combo
		RPCRequest requestStatus=new RPCRequest();
		requestStatus = new RPCRequest();
		requestStatus.setActionURL(GWT.getHostPageBaseURL() + "DeliveryStatusTrackingPresenterServlet?method=fetchOrderStatusComboBoxData&type=RPC");
		requestStatus.setHttpMethod("POST");
		requestStatus.setUseSimpleHttp(true);
		requestStatus.setShowPrompt(false);
		RPCManager.sendRequest(requestStatus, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseStatus = rawData.toString();
						String xmlDataStatus = rpcResponseStatus;
						final LinkedHashMap<String, String> statusMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));
						statusMap.put("all", "All");
						
						//Request logistic combo
						RPCRequest requestLogistic = new RPCRequest();
						requestLogistic.setActionURL(GWT.getHostPageBaseURL() + "DeliveryStatusTrackingPresenterServlet?method=fetchLogisticProviderComboBoxData&type=RPC");
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
										
										orderStatusItem.setValueMap(statusMap);
										logisticProviderItem.setValueMap(logisticMap);
									}
						});
				}
		});
		
        final DateTimeItem exportDateItem = new DateTimeItem(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP);
        exportDateItem.setTitle("Date From");
        exportDateItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        exportDateItem.setWidth("140");
		exportForm.setFields(
				logisticProviderItem,
				orderStatusItem,
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
					
				exportForm.setAction(host + "Venice/DeliveryStatusTrackingExportServlet?logistic="+logisticProviderItem.getValueAsString()+"&status="+orderStatusItem.getValueAsString()+"&date="+exportDateItem.getValue().toString());
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
	public void loadDeliveryStatusTrackingData(DataSource dataSource,  Map<String,String> service, Map<String,String> status) {
		dataSource.getField(DataNameTokens.LOGAIRWAYBILL_SERVICE).setValueMap(service);
		dataSource.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setValueMap(status);
		
		deliveryStatusTrackingListGrid.setDataSource(dataSource);
		deliveryStatusTrackingListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth(75);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DETAIL).setWidth(50);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth(75);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID).setWidth(75);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID).setHidden(true);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth(120);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT).setWidth(75);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setHidden(true);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DESTINATION).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_SERVICE).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER).setWidth(120);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_STATUS).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECIPIENT).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RELATION).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_SERVICE).setCanEdit(true);	
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC).setHidden(true);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		
		
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				return "<span style='color:blue;text-decoration:underline;cursor:hand;cursor:pointer'>"+value+"</span>";
			}
		});
		
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DETAIL).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				return "<span style='color:blue;text-decoration:underline;cursor:hand;cursor:pointer'>"+value+"</span>";
			}
		});
		
		deliveryStatusTrackingLayout.addMember(deliveryStatusTrackingToolStrip);
		deliveryStatusTrackingLayout.addMember(deliveryStatusTrackingListGrid);
		
	}

	@Override
	public void refreshDeliveryStatusTrackingData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				deliveryStatusTrackingListGrid.setData(response.getData());
			}
		};
		
		deliveryStatusTrackingListGrid.getDataSource().fetchData(deliveryStatusTrackingListGrid.getFilterEditorCriteria(), callBack);

	}
}
