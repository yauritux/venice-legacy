package com.gdn.venice.client.app.fraud.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudCaseManagementPresenter;
import com.gdn.venice.client.app.fraud.ui.widgets.FraudCaseManagementLayout;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCaseManagementUiHandlers;
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
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Fraud Case Management
 * 
 * @author Firdaus
 */
public class FraudCaseManagementView extends
ViewWithUiHandlers<FraudCaseManagementUiHandlers> implements
FraudCaseManagementPresenter.MyView {
	private static final int LIST_HEIGHT = 200;
	
	RafViewLayout fraudCaseManagementLayout;
	VLayout fraudCaseManagementDetailLayout;
	
	FraudCaseManagementLayout fraudCaseManagementContentLayout;
	
	ListGrid fraudCaseManagementListGrid;
	VLayout fraudCaseManagementListLayout = new VLayout();
	ToolStripButton closeButton;
	
	@Inject
	public FraudCaseManagementView() {
		fraudCaseManagementLayout = new RafViewLayout();
		fraudCaseManagementListLayout.setHeight(LIST_HEIGHT);
		fraudCaseManagementListLayout.setShowResizeBar(true);		
		fraudCaseManagementDetailLayout = new VLayout();
		fraudCaseManagementDetailLayout.setWidth100();
		
		ToolStrip fraudCaseToolStrip = new ToolStrip();
		fraudCaseToolStrip.setWidth100();
		
		closeButton = new ToolStripButton();
		closeButton.setIcon("[SKIN]/icons/next.png");  
		closeButton.setTooltip("Click here to close multiple cases."); 
		closeButton.setTitle("Close Cases");			
		
		fraudCaseToolStrip.addButton(closeButton);
		closeButton.setDisabled(true);
		fraudCaseManagementListLayout.addMember(fraudCaseToolStrip);
		
		HTMLFlow fraudCaseDetailFlow = new HTMLFlow();
		fraudCaseDetailFlow.setAlign(Alignment.CENTER);
		fraudCaseDetailFlow.setWidth100();
		fraudCaseDetailFlow.setContents("");
		fraudCaseManagementDetailLayout.setMembers(fraudCaseDetailFlow);
		fraudCaseManagementLayout.setMembers(fraudCaseManagementListLayout, fraudCaseManagementDetailLayout);
		buildFraudCaseManagementGrid();
		
		bindCustomUiHandlers();
	}

	private ListGrid buildFraudCaseManagementGrid() {
		fraudCaseManagementListGrid = new ListGrid();
		fraudCaseManagementListGrid.setWidth100();
		fraudCaseManagementListGrid.setHeight100();
		fraudCaseManagementListGrid.setShowAllRecords(true);
		fraudCaseManagementListGrid.setShowRowNumbers(true);
		fraudCaseManagementListGrid.setSortField(0);
		fraudCaseManagementListGrid.setCanHover(true);
//		fraudCaseManagementListGrid.setAutoFetchData(true);	
		fraudCaseManagementListGrid.setCanResizeFields(true);		
		fraudCaseManagementListGrid.setShowFilterEditor(true);
		fraudCaseManagementListGrid.setSelectionType(SelectionStyle.SIMPLE);
		fraudCaseManagementListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
				
		return fraudCaseManagementListGrid;
	}
	
	private void showFraudCaseDetails(Record record) {
		String taskId = record.getAttributeAsString(DataNameTokens.TASKID);
		String caseId = record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
		String orderId =  record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID);
		List<DataSource> dataSources = getUiHandlers().onShowFraudCaseDetailData(taskId, caseId, orderId);
		
		fraudCaseManagementContentLayout = new FraudCaseManagementLayout(
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
				dataSources.get(14), //More Info data
				dataSources.get(15), //Ilog fraud status data
				dataSources.get(16), //Attachment data
				dataSources.get(17), //Payment Summary data
				dataSources.get(18), //Payment Detail data
				dataSources.get(19), //Related order item data
				dataSources.get(20), //Related fraud data		
				dataSources.get(21), //White List data
				dataSources.get(22), //Filter Order History		
				caseId,
				dataSources.get(23),//contact detail data
				dataSources.get(24)//category data
				);
		fraudCaseManagementDetailLayout.setMembers(fraudCaseManagementContentLayout);
	}

	@Override
	public Widget asWidget() {
		return fraudCaseManagementLayout;
	}

	protected void bindCustomUiHandlers() {
		closeButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to close this case?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = fraudCaseManagementListGrid.getSelection();
							ArrayList<String> taskIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								String caseId=selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
								String statusId=selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID);
								taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID) + "~" + caseId + "~" + statusId
										);
								
								//Kalau close case, pastikan tidak boleh statusnya SF (Suspected Fraud)	
								String wcsOrderId=selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID);
								if (statusId != null && statusId.toString().equalsIgnoreCase("2")) {
									SC.say("Can not close fraud case with SF status for Order ID: "+wcsOrderId);
									return;
								}
								//update order status and publish to esb
				            	RPCRequest rpcRequest=new RPCRequest();
				        		String method = "";
				        		if (statusId.toString().equalsIgnoreCase("2")) {
				        			method = "updateOrderStatusToSF";
				        		} else if (statusId.toString().equalsIgnoreCase("3")) {
				        			method = "updateOrderStatusToFC";
				        		} else if (statusId.toString().equalsIgnoreCase("4")) {
				        			method = "updateOrderStatusToFP";
				        		}
				        		rpcRequest.setData(caseId);
				        		rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  "FraudCaseMaintenancePresenterServlet?method="+method+"&type=RPC");
				        		rpcRequest.setHttpMethod("POST");
				        		rpcRequest.setUseSimpleHttp(true);
				        		rpcRequest.setShowPrompt(false);
				        		
				        		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
				    					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
				    						String rpcResponse = rawData.toString();
				    						if (!rpcResponse.startsWith("0")) {							    					
				    							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
				    						} 
				    					}
				        		});
							}				
							getUiHandlers().onCloseTask(taskIds);
							SC.say("Case successfully saved and closed. <br> Order status successfully published to WCS and MTA.");
						}
					}
				});
			}						
		});
		
		fraudCaseManagementListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = fraudCaseManagementListGrid.getSelection();
				if (selectedRecords.length <= 1)
					closeButton.setDisabled(true);
				else
					closeButton.setDisabled(false);
				
				if (selectedRecords.length==1) {
					Record record = selectedRecords[0];
					showFraudCaseDetails(record);
				} else {
					HTMLFlow fraudCaseDetailFlow = new HTMLFlow();
					fraudCaseDetailFlow.setAlign(Alignment.CENTER);
					fraudCaseDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						fraudCaseDetailFlow.setContents("<h2 align=\"center\">Please select fraud case to show the task detail</h2>");
					} else if (selectedRecords.length>1) {
						fraudCaseDetailFlow.setContents("<h2 align=\"center\">More than one fraud case selected, please select only one fraud case to show the fraud case detail</h2>");
					}
					fraudCaseManagementDetailLayout.setMembers(fraudCaseDetailFlow);
				}
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
		ListGridField finalListGridField[] = {listGridField[0], listGridField[6], listGridField[3], listGridField[4], listGridField[7], listGridField[5]};
		
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
		if(fraudCaseManagementContentLayout != null) {
			if(fraudCaseManagementDetailLayout.hasMember(fraudCaseManagementContentLayout)) {
				fraudCaseManagementDetailLayout.removeMember(fraudCaseManagementContentLayout);
			}
		}
	}
}