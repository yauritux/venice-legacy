package com.gdn.venice.client.app.task.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.widgets.FundInReconciliationListGridWidget;
import com.gdn.venice.client.app.finance.widgets.JournalVLayoutBottomWidgetBuilder;
import com.gdn.venice.client.app.finance.widgets.JournalsListGridWidget;
import com.gdn.venice.client.app.finance.widgets.OrderItemListGridWidget;
import com.gdn.venice.client.app.finance.widgets.PaymentsListGridWidget;
import com.gdn.venice.client.app.fraud.ui.widgets.FraudCaseManagementLayout;
import com.gdn.venice.client.app.fraud.ui.widgets.FraudCaseViewerLayout;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.app.general.widgets.OrderDetailContentLayout;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliation;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliationProblem;
import com.gdn.venice.client.app.logistic.widgets.InvoiceReconciliation;
import com.gdn.venice.client.app.logistic.widgets.InvoiceReconciliationProblem;
import com.gdn.venice.client.app.logistic.widgets.MerchantPickupDetailWindow;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.StatusNameTokens;
import com.gdn.venice.client.app.task.presenter.ToDoListPresenter;
import com.gdn.venice.client.app.task.view.command.ActivityReportReconciliationTaskDetail;
import com.gdn.venice.client.app.task.view.command.CancelledOrderItemTaskDetail;
import com.gdn.venice.client.app.task.view.command.CashReceiveSalesJournalReconciliationTaskDetail;
import com.gdn.venice.client.app.task.view.command.FraudCaseManagementTaskDetail;
import com.gdn.venice.client.app.task.view.command.FundInReconciliationTaskDetail;
import com.gdn.venice.client.app.task.view.command.InvoiceReconciliationTaskDetail;
import com.gdn.venice.client.app.task.view.command.JournalsTaskDetail;
import com.gdn.venice.client.app.task.view.command.PaymentsTaskDetail;
import com.gdn.venice.client.app.task.view.command.PickupProblemInvestigationTaskDetail;
import com.gdn.venice.client.app.task.view.handlers.ToDoListUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.ui.data.ListGridDataRecord;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Criteria;
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
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Task detail implementation for AP Payment processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <b>originally:</b> by henry but has been pretty much rewritten
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ToDoListView extends ViewWithUiHandlers<ToDoListUiHandlers> implements ToDoListPresenter.MyView {
	private static final int LIST_HEIGHT = 120;

	RafViewLayout toDoListLayout;
	
	VLayout taskListLayout;
	VLayout taskDetailLayout;
	VLayout journalDetailLayoutHeader;
	VLayout journalDetailLayoutFooter;

	ListGrid toDoListGrid;
	ToolStripButton claimButton;
	ToolStripButton unclaimButton;
	ToolStripButton completeButton;
	
	private FundInReconciliationListGridWidget fundInReconciliationListGridWidget;
	private ListGrid allocateToOrderPaymentListGrid;
	private Window refundWindow;
	private Window allocateToOrderWindow;
	private ListGrid refundListGrid;
	private ListGrid allocateToOrderListGrid;
	
	/**
	 * Constructor for view that builds the view
	 */
	@Inject
	public ToDoListView() {
		toDoListLayout = new RafViewLayout();
		
		taskListLayout = new VLayout();
		taskListLayout.setHeight(LIST_HEIGHT);
		taskListLayout.setShowResizeBar(true);
		taskListLayout.setHeight(150);
		
		ToolStrip toDoListToolStrip = new ToolStrip();
		toDoListToolStrip.setWidth100();
		
		claimButton = new ToolStripButton();
		claimButton.setIcon("[SKIN]/icons/accept.png");
		claimButton.setTooltip("Claim Task");
		claimButton.setTitle("Claim");
		
		unclaimButton = new ToolStripButton();
		unclaimButton.setIcon("[SKIN]/icons/delete.png");
		unclaimButton.setTooltip("Unclaim Task");
		unclaimButton.setTitle("Unclaim");
		
		completeButton = new ToolStripButton();
		completeButton.setIcon("[SKIN]/icons/next.png");
		completeButton.setTooltip("Complete Task");
		completeButton.setTitle("Complete");
		
		toDoListToolStrip.addButton(claimButton);
		toDoListToolStrip.addButton(unclaimButton);
		toDoListToolStrip.addSeparator();
		toDoListToolStrip.addButton(completeButton);
		
		claimButton.setDisabled(true);
		unclaimButton.setDisabled(true);
		completeButton.setDisabled(true);
		
		taskListLayout.setMembers(toDoListToolStrip);

		taskDetailLayout = new VLayout();
		taskDetailLayout.setWidth100();
		taskDetailLayout.setHeight(175);
		taskDetailLayout.setShowResizeBar(true);
		
		journalDetailLayoutHeader = new VLayout();
		journalDetailLayoutHeader.setHeight(100);
		journalDetailLayoutHeader.setVisibility(Visibility.HIDDEN);
		journalDetailLayoutHeader.setWidth(100);
		
		journalDetailLayoutFooter = new VLayout();
		journalDetailLayoutFooter.setAutoHeight();
		journalDetailLayoutFooter.setWidth(100);
		journalDetailLayoutFooter.setVisibility(Visibility.HIDDEN);
		
		HTMLFlow taskDetailFlow = new HTMLFlow();
		taskDetailFlow.setAlign(Alignment.CENTER);
		taskDetailFlow.setWidth100();
		taskDetailFlow.setContents("<h2 align=\"center\">Please select a task to show the task detail</h2>");
		taskDetailLayout.setMembers(taskDetailFlow);

		toDoListLayout.setMembers(taskListLayout, taskDetailLayout, journalDetailLayoutHeader, journalDetailLayoutFooter);
		buildToDoListGrid();
		bindCustomUiHandlers();
	}

	/**
	 * Builds the ToDo ListGrid in the UI
	 * @return the new ListGrid object
	 */
	private ListGrid buildToDoListGrid() {
		toDoListGrid = new ListGrid();
		toDoListGrid.setWidth100();
		toDoListGrid.setHeight100();
		toDoListGrid.setShowAllRecords(true);
		toDoListGrid.setSortField(0);
		toDoListGrid.setCanResizeFields(true);
		toDoListGrid.setShowRowNumbers(true);
		toDoListGrid.setAutoFetchData(false);		
		toDoListGrid.setSelectionType(SelectionStyle.SIMPLE);
		toDoListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);		
		toDoListGrid.setShowFilterEditor(true);

		return toDoListGrid;
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return toDoListLayout;
	}
	
	/**
	 * Binds the custom UI handlers to the view objects
	 */
	protected void bindCustomUiHandlers() {		
		claimButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to claim this case?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
						ListGridRecord[] selectedRecords = toDoListGrid.getSelection();
						
						/*
						 * Added this in to differentiate between fraud and 
						 * other task types because for some reason fraud requires 
						 * different parameters on completion of tasks.
						 * 
						 * taskType will need to be passed all the way down to the
						 * command. - DF
						 */
						Long taskType = getTaskType(selectedRecords);

						ArrayList<String> taskIds = new ArrayList<String>();
						for (int i=0;i<selectedRecords.length;i++) {
							/*
							 * Modified this to only pass fraud data to fraud task types
							 */
							if(taskType == ProcessNameTokens.TASK_TYPE_FRAUD){
								taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID) + "~" +
										selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID) + "~" +
										selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID));
							}else{
								taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID));
							}
						}						
						getUiHandlers().onClaimTask(taskType, taskIds);	
						}
					}
				});
			}
		});
		
		unclaimButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to unclaim this case?", new BooleanCallback() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.util.BooleanCallback#execute(java.lang.Boolean)
					 */
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
								ListGridRecord[] selectedRecords = toDoListGrid.getSelection();

								/*
								 * Added this in to differentiate between fraud and 
								 * other task types because for some reason fraud requires 
								 * different parameters on completion of tasks.
								 * 
								 * taskType will need to be passed all the way down to the
								 * command. - DF
								 */
								Long taskType = getTaskType(selectedRecords);

								ArrayList<String> taskIds = new ArrayList<String>();
								for (int i=0;i<selectedRecords.length;i++) {
									/*
									 * Modified this to only pass fraud data to fraud task type
									 */
									if(taskType == ProcessNameTokens.TASK_TYPE_FRAUD){
										taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID) + "~" +
												selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID) + "~" +
												selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID));
									}else{
										taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID));
									}
								}				
								getUiHandlers().onUnclaimTask(taskType, taskIds);
								}
					}
				});
			}
		});
		
		completeButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to complete this case?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
						ListGridRecord[] selectedRecords = toDoListGrid.getSelection();
						
						/*
						 * Added this in to differentiate between fraud and 
						 * other task types because for some reason fraud requires 
						 * different parameters on completion of tasks.
						 * 
						 * taskType will need to be passed all the way down to the
						 * command. - DF
						 */
						Long taskType = getTaskType(selectedRecords);
						
						ArrayList<String> taskIds = new ArrayList<String>();
						for (int i=0;i<selectedRecords.length;i++) {
							/*
							 * Modified this to only pass fraud data to fraud task types
							 */
							if(taskType == ProcessNameTokens.TASK_TYPE_FRAUD){
								taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID) + "~" +
										selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID) + "~" +
										selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID));
							}else{
								taskIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.TASKID));
							}
						}	
						
						getUiHandlers().onCompleteTask(taskType, taskIds);
						}
					}
				});
			}						
		});
		
		toDoListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = toDoListGrid.getSelection();
				
				Long taskType = getTaskType(selectedRecords);
				
				if (selectedRecords.length==0) {
					claimButton.setDisabled(true);
					unclaimButton.setDisabled(true);
					completeButton.setDisabled(true);
				} else {
					claimButton.setDisabled(false);
					unclaimButton.setDisabled(false);
					completeButton.setDisabled(false);
					
					for (int i=0;i<selectedRecords.length;i++) {
						if (selectedRecords[i].getAttributeAsString(DataNameTokens.TASKSTATUS)!=null && selectedRecords[i].getAttributeAsString(DataNameTokens.TASKSTATUS).equals(StatusNameTokens.OPEN)) {
							unclaimButton.setDisabled(true);
							completeButton.setDisabled(true);
						}
						if (selectedRecords[i].getAttributeAsString(DataNameTokens.TASKSTATUS)!=null && selectedRecords[i].getAttributeAsString(DataNameTokens.TASKSTATUS).equals(StatusNameTokens.INPROCESS)) {
							claimButton.setDisabled(true);
						}
					}
				}
				if(taskType == ProcessNameTokens.TASK_TYPE_FINANCE){
					completeButton.setDisabled(true);
				}
				if (selectedRecords.length==1) {
					ListGridRecord record = selectedRecords[0];
					showTaskDetail(record);
				} else {
					HTMLFlow taskDetailFlow = new HTMLFlow();
					taskDetailFlow.setAlign(Alignment.CENTER);
					taskDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						taskDetailFlow.setContents("<h2 align=\"center\">Please select a task to show the task detail</h2>");
					} else if (selectedRecords.length>1) {
						taskDetailFlow.setContents("<h2 align=\"center\">More than one task selected, please select only one task to show the task detail</h2>");
					}
					taskDetailLayout.setMembers(taskDetailFlow);
					journalDetailLayoutHeader = new VLayout();
					journalDetailLayoutHeader.setHeight(100);
					journalDetailLayoutHeader.setVisibility(Visibility.HIDDEN);
					journalDetailLayoutHeader.setWidth(100);
					journalDetailLayoutFooter = new VLayout();
					journalDetailLayoutFooter.setAutoHeight();
					journalDetailLayoutFooter.setWidth(100);
					journalDetailLayoutFooter.setVisibility(Visibility.HIDDEN);
					toDoListLayout.setMembers(taskListLayout, taskDetailLayout, journalDetailLayoutHeader, journalDetailLayoutFooter);
				}
			}
		});
		
		toDoListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshToDoListData();
			}
		});
	}
	
	/**
	 * Shows the task detail screen based on the task type embedded in
	 * the record
	 * @param record is the task record from the ListGrid
	 */
	private void showTaskDetail(ListGridRecord record) {
		final String type = record.getAttributeAsString(DataNameTokens.TASKTYPE);
		String taskId = record.getAttributeAsString(DataNameTokens.TASKID);
		
		if(type.equals(ProcessNameTokens.FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL)){
			this.showFundsInReconciliationTaskDetail(record, taskId);			
		}else if (type.equals(ProcessNameTokens.LOGISTICSINVOICEAPPROVAL)) {
			this.showLogisticsInvoiceApprovalTaskDetail(record, taskId);
		}else if (type.equals(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL)) {
			this.showLogisticsActivityApprovalTaskDetail(record, taskId);
		} else if (type.equals(ProcessNameTokens.LOGISTICSPICKUPPROBLEMINVESTIGATION)) {
			this.showLogisticsPickupProblemInvestigationTaskDetail(record, taskId);
		} else if (type.startsWith(ProcessNameTokens.FRAUDMANAGEMENTPROCESS)) {
			this.showFraudManagementProcessTaskDetail(record, taskId);
		} else if (type.startsWith(ProcessNameTokens.FINANCEAPPAYMENTRESULTAPPROVAL)){
			this.showApPaymentApprovalTaskDetail(record, taskId);
		} else if (type.startsWith(ProcessNameTokens.FINANCEJOURNALRESULTAPPROVAL)){
			this.showJournalApprovalTaskDetail(record, taskId);
		} else if (type.equals(ProcessNameTokens.LOGISTICSMTADATAACTIVITYRECONCILIATION) || (type.equals(ProcessNameTokens.LOGISTICSMTADATAINVOICERECONCILIATION))){
			this.showDeliveryStatusTracking(record, taskId);
		} else if (type.equals(ProcessNameTokens.FINANCECASHRECEIVEANDSALESJOURNALRECONCILIATION)){
			this.showCashReceiveSalesJournalReconciliationTaskDetail(record, taskId);
		} else if(type.equals(ProcessNameTokens.FINANCEORDERITEMCANCELLEDNOTIFICATION)){
			this.showCancelledOrderItemNotification(record, taskId);
		}
	}	

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.task.presenter.ToDoListPresenter.MyView#loadToDoListData(com.smartgwt.client.data.DataSource)
	 */
	public void loadToDoListData(DataSource dataSource, String userRole) {
		toDoListGrid.setDataSource(dataSource);		
		toDoListGrid.setSortField(DataNameTokens.TASKCREATEDDATE);
		toDoListGrid.setSortDirection(SortDirection.DESCENDING);
		toDoListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));		
		toDoListGrid.getField(DataNameTokens.TASKASSIGNEE).setWidth("150");
		toDoListGrid.getField(DataNameTokens.TASKCREATEDDATE).setWidth("100");
		toDoListGrid.getField(DataNameTokens.TASKDUEDATE).setWidth("100");
		toDoListGrid.getField(DataNameTokens.TASKSTATUS).setWidth("65");
		toDoListGrid.getField(DataNameTokens.TASKTYPE).setWidth("250");
		toDoListGrid.getField(DataNameTokens.TASKID).setHidden(true);
		toDoListGrid.getField(DataNameTokens.TASKDUEDATE).setHidden(true);
		toDoListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).setHidden(true);
		toDoListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID).setHidden(true);		
		toDoListGrid.getField(DataNameTokens.TASKTYPE).setFilterOperator(OperatorId.ICONTAINS);
		
		LinkedHashMap<String, String> taskType = new LinkedHashMap<String, String>();  
		
		if(userRole.contains("logistic")){
			taskType.put("Logistics Activity Report Approval", "Logistics Activity Report Approval");
			taskType.put("Logistics Invoice Approval", "Logistics Invoice Approval");
			taskType.put("Logistics Pickup Problem Investigation", "Logistics Pickup Problem Investigation");
		}
		
		if(userRole.contains("finance")){
			taskType.put("Finance Fund In Reconciliation Result Approval", "Finance Fund In Reconciliation Result Approval");
			taskType.put("Finance AP Payment Approval Process", "Finance AP Payment Approval Process");
			taskType.put("Finance Journal Approval Process", "Finance Journal Approval Process");
			taskType.put("Finance Cash Receive and Sales Journal Reconciliation", "Finance Cash Receive and Sales Journal Reconciliation");
			taskType.put("Finance Order Item Cancelled Notification", "Finance Order Item Cancelled Notification");
		}
		
		if(userRole.contains("fraud")){
			taskType.put("Fraud Management Process", "Fraud Management Process");
		}
		
		toDoListGrid.getField(DataNameTokens.TASKTYPE).setValueMap(taskType);
		
		if(userRole.contains("finance")){
			toDoListGrid.getField(DataNameTokens.TASKTYPE).setDefaultFilterValue("Finance Fund In Reconciliation Result Approval");
		}else if(userRole.contains("logistic")){
			toDoListGrid.getField(DataNameTokens.TASKTYPE).setDefaultFilterValue("Logistics Activity Report Approval");
		}else if(userRole.contains("fraud")){
			toDoListGrid.getField(DataNameTokens.TASKTYPE).setDefaultFilterValue("Fraud Management Process");
		}
		
		taskListLayout.addMember(toDoListGrid);
	}
	
	/**
	 * Shows the funds in reconciliation approval task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showFundsInReconciliationTaskDetail(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");
		approveButton.setTitle("Approve");

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		rejectButton.setTitle("Reject");
		
		taskDetailToolStrip.addButton(approveButton);
		taskDetailToolStrip.addButton(rejectButton);
		
		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
		approveButton.setDisabled(true);
		rejectButton.setDisabled(true);

		DataSource dataSource = FundInReconciliationTaskDetail.getFundInReconciliationDataSource(taskId); 
		final ListGrid fundsInListGrid = new FundInReconciliationListGridWidget(){
			
		};
		
		fundsInListGrid.setDataSource(dataSource);
		fundsInListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
				
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID).setWidth(75);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF).setWidth(120);
		//fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setWidth(120);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setWidth(50);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setWidth(140);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).setWidth(100);		
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setWidth(100);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setCanEdit(false);
		
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setHidden(true);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setHidden(true);
		
//		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCanFilter(false);
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setCanFilter(false);

		Util.formatListGridFieldAsCurrency(fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT));
		Util.formatListGridFieldAsCurrency(fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT));
		
		fundsInListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.CellFormatter#format(java.lang.Object, com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
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
				
		fundsInListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				if (fundsInListGrid instanceof FundInReconciliationListGridWidget) {
					((FundInReconciliationListGridWidget) fundsInListGrid).refreshFundInReconData();
				}
			}
		});
		
		fundsInListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bSelected = fundsInListGrid.getSelection().length > 0 && status.equals(StatusNameTokens.INPROCESS);  
				
				ListGridRecord[] selectRecords = fundsInListGrid.getSelection();
				/*
				 * If any of the selected records are not status submitted then disable the buttons
				 */
				int countApproved=0;
				for(int i = 0; i < selectRecords.length; ++i){
					if(!selectRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equalsIgnoreCase(StatusNameTokens.STATUS_SUBMITTED)){
						bSelected = false;
					}
					if(selectRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equalsIgnoreCase(StatusNameTokens.STATUS_APPROVED)){
						countApproved++;
					}
					
				}
				
				approveButton.setDisabled(!bSelected);
				rejectButton.setDisabled(!bSelected);
				completeButton.setDisabled(!(countApproved==fundsInListGrid.getRecords().length));
			}
		});
					
		approveButton.addClickHandler(new ClickHandler() {		
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to approve this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = fundsInListGrid.getSelection();
							ArrayList<String> fundsInIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								fundsInIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
							}
							FundInReconciliationTaskDetail.onApprovalDecision(fundsInIds, (FundInReconciliationListGridWidget) fundsInListGrid, "approve");
							completeButton.setDisabled(!(selectedRecords.length==fundsInListGrid.getSelection().length));
						}						
					}
				});				
			}
		});
			
		rejectButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to reject this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = fundsInListGrid.getSelection();
							ArrayList<String> fundsInIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								fundsInIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
							}							
							FundInReconciliationTaskDetail.onApprovalDecision(fundsInIds, (FundInReconciliationListGridWidget) fundsInListGrid, "reject");
						}						
					}
				});				
			}
		});

		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).contains(ProcessNameTokens.FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL_APPROVALACTIVITYNAME)) {
			taskDetailLayout.setMembers(taskDetailToolStrip, fundsInListGrid);
		} else {
			taskDetailLayout.setMembers(fundsInListGrid);
		}				
	}
	
	/**
	 * Shows the logistics invoice approval process task detail screen
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showLogisticsInvoiceApprovalTaskDetail(final ListGridRecord record, final String taskId){
		final ListGrid invoiceReportUploadGrid = new ListGrid() {
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
		
		RPCRequest requestApproval=new RPCRequest();
		requestApproval = new RPCRequest();
		requestApproval.setActionURL(GWT.getHostPageBaseURL() + "InvoiceReconciliationPresenterServlet?method=fetchApprovalStatusComboBoxData&type=RPC");
		requestApproval.setHttpMethod("POST");
		requestApproval.setUseSimpleHttp(true);
		requestApproval.setShowPrompt(false);
		RPCManager.sendRequest(requestApproval, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseApproval = rawData.toString();
						String xmlDataApproval = rpcResponseApproval;
						final Map<String, String> approvalMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataApproval));						
						loadInvoiceReportUploadData(invoiceReportUploadGrid, record, LogisticsData.getInvoiceReportUploadData(taskId), approvalMap, taskId); 						
				}
		});
	}
	
	public void loadInvoiceReportUploadData(final ListGrid invoiceReportUploadGrid, final ListGridRecord record, DataSource dataSource, final Map<String, String> approval, final String taskId) {
		final ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");
		approveButton.setTitle("Approve");
		approveButton.setDisabled(true);

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		rejectButton.setTitle("Reject");
		rejectButton.setDisabled(true);
		
		taskDetailToolStrip.addButton(approveButton);
		taskDetailToolStrip.addButton(rejectButton);
		

		approveButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to approve this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						approveButton.setDisabled(true);
						rejectButton.setDisabled(true);
						if(value != null && value){
							ListGridRecord[] selectedRecords = invoiceReportUploadGrid.getSelection();
							ArrayList<String> invoiceNumbers = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								invoiceNumbers.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER));
							}				
							InvoiceReconciliationTaskDetail.onApprovalDecision(invoiceNumbers, (ListGrid) invoiceReportUploadGrid, "Approve");
							loadInvoiceReportUploadData(invoiceReportUploadGrid, record, LogisticsData.getInvoiceReportUploadData(taskId), approval, taskId);
						}						
					}
				});				
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to reject this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						approveButton.setDisabled(true);
						rejectButton.setDisabled(true);
						if(value != null && value){
							ListGridRecord[] selectedRecords = invoiceReportUploadGrid.getSelection();
							ArrayList<String> invoiceNumbers = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								invoiceNumbers.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER));
							}				
							InvoiceReconciliationTaskDetail.onApprovalDecision(invoiceNumbers, (ListGrid) invoiceReportUploadGrid, "Reject");
							loadInvoiceReportUploadData(invoiceReportUploadGrid, record, LogisticsData.getInvoiceReportUploadData(taskId), approval, taskId);
						}						
					}
				});
			}
		});
		
		invoiceReportUploadGrid.setWidth100();
		invoiceReportUploadGrid.setHeight100();
		invoiceReportUploadGrid.setShowAllRecords(true);
		invoiceReportUploadGrid.setSortField(3);
		invoiceReportUploadGrid.setSortDirection(SortDirection.DESCENDING);
		
		invoiceReportUploadGrid.setSelectionType(SelectionStyle.SIMPLE);
		invoiceReportUploadGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		invoiceReportUploadGrid.setShowFilterEditor(true);
		invoiceReportUploadGrid.setAutoFetchData(true);
		
		invoiceReportUploadGrid.setCanResizeFields(true);
		invoiceReportUploadGrid.setShowRowNumbers(true);		
		
		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
		invoiceReportUploadGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selected = invoiceReportUploadGrid.getSelection();
				boolean bSelected = selected.length > 0 && status.equals(StatusNameTokens.INPROCESS); 
				
				for (ListGridRecord listGridRecord : selected) {
					if(!listGridRecord.getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC).startsWith("Submitted")){
						bSelected = false;
						break;
					}
				}
				
				approveButton.setDisabled(!bSelected);
				rejectButton.setDisabled(!bSelected);
				
				HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
				invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
				invoiceAirwaybillRecordFlow.setWidth100();
				if (selected.length==1) {
					if(bSelected){
						loadInvoiceAirwayRecordData(LogisticsData.getInvoiceAirwaybillRecordData(selected[0].getAttributeAsString(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID)), approval);
//						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a airwaybill number to show the list of airwaybills</h2>");
//						journalDetailLayoutFooter.setMembers(invoiceAirwaybillRecordFlow);
					} else {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please claim the task first to see the detail</h2>");
						journalDetailLayoutHeader.setMembers(invoiceAirwaybillRecordFlow);
					}
				} else {
					if (selected.length==0) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select a report to show the list of airwaybill numbers</h2>");
					} else if (selected.length>1) {
						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">More than one reports selected, please select only one reports to show the list of airwaybill numbers</h2>");
					}
					journalDetailLayoutHeader.setMembers(invoiceAirwaybillRecordFlow);
				}
			}
		});
			
		dataSource.getField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID).setValueMap(approval);
		
		invoiceReportUploadGrid.setDataSource(dataSource);
		invoiceReportUploadGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
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
		
		journalDetailLayoutHeader.setShowResizeBar(true);
		journalDetailLayoutHeader.setWidth100();
		journalDetailLayoutHeader.setVisibility(Visibility.VISIBLE);
		journalDetailLayoutFooter.setVisibility(Visibility.HIDDEN);
		
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).contains(ProcessNameTokens.LOGISTICSINVOICEREPORTAPPROVAL_APPROVALACTIVITYNAME)) {
			taskDetailLayout.setMembers(taskDetailToolStrip, invoiceReportUploadGrid);
		} else {
			taskDetailLayout.setMembers(invoiceReportUploadGrid);
		}
	}
	
	public void loadInvoiceAirwayRecordData(DataSource dataSource, Map<String, String> approvalStatusMap) {
		final InvoiceReconciliation invoiceReconciliationListGrid = new InvoiceReconciliation() {
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				String invoiceAirwaybillId = record.getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
				String airwayBillApprovalStatus = record.getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC);
				DataSource reconciliationProblemDataSource = LogisticsData.getInvoiceReconciliationProblemData(invoiceAirwaybillId);
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

		invoiceReconciliationListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				// TODO Auto-generated method stub

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
		});
		
		invoiceReconciliationListGrid.setSelectionType(SelectionStyle.NONE);
		
		invoiceReconciliationListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = invoiceReconciliationListGrid.getSelection();
				if (selectedRecords.length == 1) {
					DataSource ds = LogisticsData.getInvoiceAirwayBillsData(selectedRecords[0].getAttributeAsString(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
					loadAirwayBillData(ds);
				}
				else
				{
					HTMLFlow invoiceAirwaybillRecordFlow = new HTMLFlow();
					invoiceAirwaybillRecordFlow.setAlign(Alignment.CENTER);
					invoiceAirwaybillRecordFlow.setWidth100();
//					if (selectedRecords.length==0) {
//						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">Please select an airwaybill number to show the list of airwaybills</h2>");
//					} else if (selectedRecords.length>1) {
//						invoiceAirwaybillRecordFlow.setContents("<h2 align=\"center\">More than one airwaybill numbers selected, please select only one airwaybill number to show the list of airwaybills</h2>");
//					}
//					journalDetailLayoutFooter.setMembers(invoiceAirwaybillRecordFlow);
				}
			}
		});
		
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
		invoiceReconciliationListGrid.getField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS).setCanEdit(true);
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
		
		journalDetailLayoutHeader.setMembers(invoiceReconciliationListGrid);
	}
	
	public void loadAirwayBillData(DataSource dataSource){
		ListGrid logAirwayBillListGrid = new ListGrid();
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
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		logAirwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setHidden(true);
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
		
		journalDetailLayoutHeader.setMembers(logAirwayBillListGrid);	
	}
	
	/**
	 * Shows the logistics activity approval process task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showLogisticsActivityApprovalTaskDetail(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");
		approveButton.setTitle("Approve");

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		rejectButton.setTitle("Reject");
		
		taskDetailToolStrip.addButton(approveButton);
		taskDetailToolStrip.addButton(rejectButton);
		
		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
		approveButton.setDisabled(true);
		rejectButton.setDisabled(true);

		DataSource dataSource = ActivityReportReconciliationTaskDetail.getActivityReportReconciliationDataSource(taskId); 
		final ListGrid airwayBillListGrid = new ActivityReportReconciliation() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionComponent(com.smartgwt.client.widgets.grid.ListGridRecord)
			 */
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				String airwayBillId = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
				String airwayBillApprovalStatus = record.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC);
				DataSource activityReportReconciliationProblemDs = LogisticsData.getActivityReportsReconciliationProblemData(airwayBillId);
				return new ActivityReportReconciliationProblem(airwayBillId, activityReportReconciliationProblemDs, airwayBillApprovalStatus);
			}			
		};
		
		airwayBillListGrid.setDataSource(dataSource);
		airwayBillListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth(75);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth(75);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth(120);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setHidden(true);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER).setWidth(120);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID).setHidden(true);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID).setWidth(125);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DESTINATION).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ZIP).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setWidth(100);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setWidth(100);
//		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setCellFormatter(new CellFormatter() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.CellFormatter#format(java.lang.Object, com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				return "<span style='color:blue;text-decoration:underline;cursor:hand;cursor:pointer'>"+value+"</span>";
			}
		});
		
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setCellFormatter(new CellFormatter() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.CellFormatter#format(java.lang.Object, com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
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
		
		airwayBillListGrid.addCellClickHandler(new CellClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.CellClickHandler#onCellClick(com.smartgwt.client.widgets.grid.events.CellClickEvent)
			 */
			@Override
			public void onCellClick(CellClickEvent event) {
				if (airwayBillListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME)) {
					String airwayBillId = event.getRecord().getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
					Window merchantPickupDetailWindow = new MerchantPickupDetailWindow(LogisticsData.getMerchantPickUpInstructionData(airwayBillId));
					merchantPickupDetailWindow.show();
				} 
			}
		});
		
		airwayBillListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				if (airwayBillListGrid instanceof ActivityReportReconciliation) {
					((ActivityReportReconciliation) airwayBillListGrid).refreshAirwayBillData();
				}
			}
		});
		
		airwayBillListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bSelected = airwayBillListGrid.getSelection().length > 0 && status.equals(StatusNameTokens.INPROCESS);  				
				Record[] records = airwayBillListGrid.getSelection();
				
				/*
				 * Disable the buttons if there are selected records that are any status other than "Submitted" 
				 */
				for(int i=0; i < records.length; ++i){
					if(!records[i].getAttribute(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).equalsIgnoreCase("Submitted")){
						bSelected = false;
					}
				}				
				approveButton.setDisabled(!bSelected);
				rejectButton.setDisabled(!bSelected);
			}
		});
			
		
		approveButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to approve this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = airwayBillListGrid.getSelection();
							ArrayList<String> airwayBillIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
							}				
							ActivityReportReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (ActivityReportReconciliation) airwayBillListGrid, "approve");
						}						
					}
				});
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to reject this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = airwayBillListGrid.getSelection();
							ArrayList<String> airwayBillIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
							}				
							ActivityReportReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (ActivityReportReconciliation) airwayBillListGrid, "reject");
						}
					}
				});
			}
		});
		
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).contains(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL_APPROVALACTIVITYNAME)) {
			taskDetailLayout.setMembers(taskDetailToolStrip, airwayBillListGrid);
		} else {
			taskDetailLayout.setMembers(airwayBillListGrid);
		}		
	}
	
	/**
	 * Shows thwe logistics pickup problem investigation process task detail 
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showLogisticsPickupProblemInvestigationTaskDetail(ListGridRecord record, String taskId){
		List<DataSource> dataSources = PickupProblemInvestigationTaskDetail.getOrderDetailDataSources(taskId);
		
		final OrderDetailContentLayout orderDetailContentLayout = new OrderDetailContentLayout(
				dataSources.get(0), //Order Detail
				dataSources.get(1), //Order Item
				dataSources.get(2), //Order Customer
				dataSources.get(3), //Order Customer Address
				dataSources.get(4), //Order Customer Contact
				dataSources.get(5), //Order Logistics Airway Bill
				dataSources.get(6), //Order Finance Payment
				dataSources.get(7), //Order Finance Reconciliation
				dataSources.get(8), //Order History Order
				dataSources.get(9) //Order History Order Item
				);
		
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonSuspiciousFraud().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudPassed().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudConfirmed().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonBlock().setVisibility(Visibility.HIDDEN);
		
		orderDetailContentLayout.getOrderDataFinanceTab().getOrderFinancePaymentListGrid().addCellClickHandler(new CellClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.CellClickHandler#onCellClick(com.smartgwt.client.widgets.grid.events.CellClickEvent)
			 */
			@Override
			public void onCellClick(CellClickEvent event) {
				String paymentId = event.getRecord().getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID);
				orderDetailContentLayout.getOrderDataFinanceTab().loadFinanceReconciliationData(GeneralData.getOrderFinanceReconciliationData(paymentId));
			}
		});
		
		orderDetailContentLayout.getOrderDataFinanceTab().getButtonApproveVA().setVisibility(Visibility.HIDDEN);		
		taskDetailLayout.setMembers(orderDetailContentLayout);
	}
	
	/**
	 * Shows the fraud management process task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showFraudManagementProcessTaskDetail(ListGridRecord record, String taskId){
		String caseId = record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
		String orderId = record.getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID);
		List<DataSource> dataSources = FraudCaseManagementTaskDetail.getFraudCaseDetailDataSources(taskId, caseId, orderId);
		
		if (record.getAttributeAsString(DataNameTokens.TASKSTATUS) != null
				&& record.getAttributeAsString(DataNameTokens.TASKSTATUS).equals(StatusNameTokens.OPEN)) {
			
			final FraudCaseViewerLayout fraudCaseViewerLayout = new FraudCaseViewerLayout(
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
					dataSources.get(15), //Ilog fraud status data
					dataSources.get(16), //Attachment data
					dataSources.get(17), //Payment Summary data
					dataSources.get(18), //Payment detail
					dataSources.get(21), //White List Data,
					dataSources.get(22), //Filter Order History	
					caseId,
					dataSources.get(23),//contact detail data
					dataSources.get(24));//category data
			
			taskDetailLayout.setMembers(fraudCaseViewerLayout);
		}
		else if (record.getAttributeAsString(DataNameTokens.TASKSTATUS) != null
				&& record.getAttributeAsString(DataNameTokens.TASKSTATUS).equals(StatusNameTokens.INPROCESS)) {
			
			final FraudCaseManagementLayout fraudCaseManagementLayout = new FraudCaseManagementLayout(
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
			taskDetailLayout.setMembers(fraudCaseManagementLayout);
		}		
	}
		
	/**
	 * Shows the AP Payment approval process task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showApPaymentApprovalTaskDetail(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");
		approveButton.setTitle("Approve");

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		rejectButton.setTitle("Reject");
		
		taskDetailToolStrip.addButton(approveButton);
		taskDetailToolStrip.addButton(rejectButton);
		
		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
		approveButton.setDisabled(true);
		rejectButton.setDisabled(true);

		DataSource dataSource = PaymentsTaskDetail.getPaymentsDataSource(taskId); 
		final ListGrid paymentsListGridWidget = new PaymentsListGridWidget();
		paymentsListGridWidget.setDataSource(dataSource);
		
		paymentsListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("20%");							
		
		Util.formatListGridFieldAsCurrency(paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));

		paymentsListGridWidget.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				if (paymentsListGridWidget instanceof PaymentsListGridWidget) {
					((PaymentsListGridWidget) paymentsListGridWidget).refreshData();
				}
			}
		});
		
		paymentsListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bDisabled = false;
				ListGridRecord[] selected = paymentsListGridWidget.getSelection();
				
				if(selected.length <= 0 || !status.equals(StatusNameTokens.INPROCESS)){
					bDisabled = true;
				}
				int countApproved=0;
				for(int i=0; i < selected.length; ++i){
					if(!selected[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("Submitted")){
						bDisabled = true;
					}
					if(selected[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equalsIgnoreCase(StatusNameTokens.STATUS_APPROVED)){
						countApproved++;
					}
					
				}
				
				approveButton.setDisabled(bDisabled);
				rejectButton.setDisabled(bDisabled);
				completeButton.setDisabled(!(countApproved==paymentsListGridWidget.getSelection().length));
			}
		});
		
		approveButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to approve this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
							ArrayList<String> apPaymentIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								apPaymentIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
							}							
							PaymentsTaskDetail.onApprovalDecision(apPaymentIds, (PaymentsListGridWidget) paymentsListGridWidget, "approve");

							completeButton.setDisabled(!(selectedRecords.length==paymentsListGridWidget.getSelection().length));
						}
					}
				});
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to reject this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
							ArrayList<String> apPaymentIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								apPaymentIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
							}							
							PaymentsTaskDetail.onApprovalDecision(apPaymentIds, (PaymentsListGridWidget) paymentsListGridWidget, "reject");
						}
					}
				});
			}
		});
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).contains(ProcessNameTokens.FINANCEAPPAYMENTAPPROVAL_APPROVALACTIVITYNAME)) {
			taskDetailLayout.setMembers(taskDetailToolStrip, paymentsListGridWidget);
		} else {
			taskDetailLayout.setMembers(paymentsListGridWidget);
		}		
	}
	
	private void showCancelledOrderItemNotification(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		DataSource dataSource = CancelledOrderItemTaskDetail.getJournalsDataSource(taskId);
		final ListGrid orderItemListGridWidget = new OrderItemListGridWidget();
		orderItemListGridWidget.setDataSource(dataSource);
		orderItemListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
//		orderItemListGridWidget.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);
		orderItemListGridWidget.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth("5%");
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setHidden(true);
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_WCSORDERITEMID).setWidth("5%");
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("20%");
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("20%");
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_QUANTITY).setWidth("20%");
		orderItemListGridWidget.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth("20%");
		
		taskDetailLayout.setMembers(orderItemListGridWidget);
	}

	/**
	 * Shows the journal approval process task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showJournalApprovalTaskDetail(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");
		approveButton.setTitle("Approve");

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		rejectButton.setTitle("Reject");
		
		taskDetailToolStrip.addButton(approveButton);
		taskDetailToolStrip.addButton(rejectButton);
		approveButton.setDisabled(true);
		rejectButton.setDisabled(true);

		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
		approveButton.setDisabled(true);
		rejectButton.setDisabled(true);

		DataSource dataSource = JournalsTaskDetail.getJournalsDataSource(taskId); 
		final ListGrid journalsListGridWidget = new JournalsListGridWidget();
		journalsListGridWidget.setDataSource(dataSource);
		journalsListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID).setWidth("5%");
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID).setHidden(true);
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).setWidth("20%");
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC).setWidth("20%");
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP).setWidth("20%");
		journalsListGridWidget.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("20%");
					
		journalsListGridWidget.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				if (journalsListGridWidget instanceof JournalsListGridWidget) {
					((JournalsListGridWidget) journalsListGridWidget).refreshData();
				}
			}
		});
		
		journalsListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler() {	
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bDisabled = false;  				
				ListGridRecord[] selected = journalsListGridWidget.getSelection();
				
				if(selected.length <= 0 || !status.equals(StatusNameTokens.INPROCESS)){
					bDisabled = true;
				}
				
				int countApproved=0;
				for(int i=0; i < selected.length; ++i){
					if(!selected[i].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("Submitted")){
						bDisabled = true;
					}
					if(selected[i].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(StatusNameTokens.STATUS_APPROVED)){
						countApproved++;
					}
				}
				
				if(selected.length <= 0){
					bDisabled = true;
				}
				
				approveButton.setDisabled(bDisabled);
				rejectButton.setDisabled(bDisabled);

				completeButton.setDisabled(!(countApproved==journalsListGridWidget.getSelection().length));
				
				/*
				 * Setup the journal detail VLayout with the members
				 * build by the widget builder
				 */			
				Canvas[] newMembers = buildJournalDetailVLayout(selected[0]).getMembers();
				
				journalDetailLayoutHeader.setShowResizeBar(true);
				journalDetailLayoutHeader.setWidth100();
				journalDetailLayoutFooter.setWidth100();
				journalDetailLayoutHeader.setVisibility(Visibility.VISIBLE);
				journalDetailLayoutFooter.setVisibility(Visibility.VISIBLE);
				
				//TODO the bottom grid is not shown
				journalDetailLayoutHeader.setMembers(newMembers[0]);
				journalDetailLayoutFooter.setMembers(newMembers[1]);
			}
		});
		
		approveButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to approve this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = journalsListGridWidget.getSelection();
							ArrayList<String> journalGroupIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								journalGroupIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
							}							
							JournalsTaskDetail.onApprovalDecision(journalGroupIds, (JournalsListGridWidget) journalsListGridWidget, "approve");

							completeButton.setDisabled(!(selectedRecords.length==journalsListGridWidget.getSelection().length));
						}
					}
				});
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to reject this record?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							ListGridRecord[] selectedRecords = journalsListGridWidget.getSelection();
							ArrayList<String> journalGroupIds = new ArrayList<String>();
							for (int i=0;i<selectedRecords.length;i++) {
								journalGroupIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
							}							
							JournalsTaskDetail.onApprovalDecision(journalGroupIds, (JournalsListGridWidget) journalsListGridWidget, "reject");
						}
					}
				});
			}
		});
		
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).contains(ProcessNameTokens.FINANCEJOURNALAPPROVAL_APPROVALACTIVITYNAME)) {
			taskDetailLayout.setMembers(taskDetailToolStrip, journalsListGridWidget);
		} else {
			taskDetailLayout.setMembers(journalsListGridWidget);
		}		
	}
	
	/**
	 * Shows the Cash Receive and Sales Journal Reconciliation task detail
	 * @param record is the list grid record
	 * @param taskId is the identifier of the task
	 */
	private void showCashReceiveSalesJournalReconciliationTaskDetail(ListGridRecord record, String taskId){
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		final String taskId2 = taskId;
		final ToolStripButton refundButton;
		final ToolStripButton allocateToOrderButton;
		
		refundButton = new ToolStripButton();
		refundButton.setIcon("[SKIN]/icons/refund.png");
		refundButton.setTooltip("Refund");
		refundButton.setTitle("Refund");

		allocateToOrderButton = new ToolStripButton();
		allocateToOrderButton.setIcon("[SKIN]/icons/allocateorder.png");
		allocateToOrderButton.setTooltip("Allocate to Order");
		allocateToOrderButton.setTitle("Allocate");
		
		taskDetailToolStrip.addButton(refundButton);
		taskDetailToolStrip.addButton(allocateToOrderButton);
		
		refundButton.setDisabled(true);
		allocateToOrderButton.setDisabled(true);
		
		DataSource dataSource = CashReceiveSalesJournalReconciliationTaskDetail.getFundsInAndSalesRecordDataSource(taskId); 
		fundInReconciliationListGridWidget = new FundInReconciliationListGridWidget(){
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
				/*
				 * This has been overridden to handle the fancy cell text required for funds in 
				 */
				
				// Only color the status and approval status
				if (getFieldName(colNum).equals(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
						|| getFieldName(colNum).equals(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC)) {
					String resultStatus = record.getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC);
					String approvalStatus = record.getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC);

					if (resultStatus == null || resultStatus.isEmpty()) {
						return super.getCellCSSText(record, rowNum, colNum);
					}
					// if OK, color it light green
					if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED.toUpperCase())) {
						return "background-color:#00FF00;";
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PARTIALFUNDSRECEIVED.toUpperCase())) {
						// if problem exists...
						if (approvalStatus != null	&& approvalStatus.toUpperCase().contains(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							// ...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							// ...and not approved, color it yellow
							return "background-color:#ece355;";
						}
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT.toUpperCase())
							|| resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT.toUpperCase())) {
						// if no data from MTA and invalid GDN Ref
						if (approvalStatus != null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							// ...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							// ...and not approved, color it red
							return "color:#FFFFFF;background-color:#FF0000;";
						} 
					}  else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED.toUpperCase())) {
						// if payment not recognized
						if (approvalStatus != null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							// ...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							// ...and not approved, color it black
							return "color:#FFFFFF;background-color:#000000;";
						} 
					}
				} 
				return super.getCellCSSText(record, rowNum, colNum);
			}
		};
		final String status = record.getAttributeAsString(DataNameTokens.TASKSTATUS);
		fundInReconciliationListGridWidget.setDataSource(dataSource);
		fundInReconciliationListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF).setWidth(120);
		//fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setWidth(50);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setWidth(140);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).setWidth(100);		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setCanEdit(false);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setHidden(true);
		
//		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCanFilter(false);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setCanFilter(false);
		
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT));
		
		fundInReconciliationListGridWidget.setShowGridSummary(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.CellFormatter#format(java.lang.Object, com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
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
					
		fundInReconciliationListGridWidget.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				if (fundInReconciliationListGridWidget instanceof FundInReconciliationListGridWidget) {
					((FundInReconciliationListGridWidget) fundInReconciliationListGridWidget).refreshFundInReconData();
				}
			}
		});
		
		fundInReconciliationListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {				
				ListGridRecord[] selected = fundInReconciliationListGridWidget.getSelection();
				@SuppressWarnings("unused")
				boolean bDisabled = false;  
				
				if (selected.length == 0 || !status.equals(StatusNameTokens.INPROCESS)) {
					refundButton.setDisabled(true);
					allocateToOrderButton.setDisabled(true);
				}
								
				ListGridRecord[] selectedRecords = fundInReconciliationListGridWidget.getSelection();
				if (selectedRecords.length == 0) {
					refundButton.setDisabled(true);
					allocateToOrderButton.setDisabled(true);
				} else {
					/*
					 * Disable the refund button if the record has already been refunded, no payment, all funds received, payment timeout, submitted/approved
					 */
					refundButton.setDisabled(false);
					for (int i = 0; i < selectedRecords.length; i++) {
					/*	boolean bDisableRefundButton = selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_REFUNDED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED);
						*/
						boolean bDisableRefundButton = (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT) ||
								(!selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT) &&
								(!selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_NEW)	
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER )
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK) ||
								selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK ))
								));
						
						refundButton.setDisabled(bDisableRefundButton);
						
						if (bDisableRefundButton) {
							break;
						}
					}
					
					/*
					 * Disable the allocate to order button if the funds in record is no payment, all funds received, payment timeout, already submitted/approved
					 */
					for (int i = 0; i < selectedRecords.length; i++) {
						/*boolean bDisableAllocateButton = 
								(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT))
								|| (!selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED)
								&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PARTIALFUNDSRECEIVED)
								&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC)
										.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_OVERPAIDFUNDSRECEIVED)
										|| (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC)
												.equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
										|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC)
												.equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED)));
						*/
						boolean bDisableAllocateButton = (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT) ||
								(!selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT) &&
								(!selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_NEW)	
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER )
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK))
								));
						bDisableAllocateButton = selectedRecords.length>1;
						
						allocateToOrderButton.setDisabled(bDisableAllocateButton);
						
						if (bDisableAllocateButton) {
							break;
						}
					}
				}
				
				refundButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						buildRefundWindow().show();
					}
				});			

				allocateToOrderButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						buildAllocateToOrderWindow().show();
					}
				});
				
				/*
				 * Setup the detail VLayout with the members
				 * build by the widget builder
				 */			
				Canvas[] newMembers = buildCashReceiveSalesJournalReconciliationDetailVLayout(selected[0], taskId2).getMembers();
				
				journalDetailLayoutHeader.setShowResizeBar(true);
				journalDetailLayoutHeader.setWidth100();
				journalDetailLayoutHeader.setHeight(150);
				journalDetailLayoutFooter.setWidth100();
				journalDetailLayoutFooter.setHeight(150);
				journalDetailLayoutHeader.setVisibility(Visibility.VISIBLE);
				journalDetailLayoutFooter.setVisibility(Visibility.VISIBLE);
				
//				journalDetailLayoutFooter.setMembers(newMembers[1]);
				journalDetailLayoutHeader.setMembers(newMembers[0]);
			}
		});		
		taskDetailLayout.setMembers(taskDetailToolStrip, fundInReconciliationListGridWidget);
	}
	
	/**
	 * Builds the allocate to oder window as a modal dialog
	 * @return the window once it is built
	 */
	private Window buildAllocateToOrderWindow() {
		allocateToOrderWindow = new Window();
		allocateToOrderWindow.setWidth(500);
		allocateToOrderWindow.setHeight(350);
		allocateToOrderWindow.setTitle("Allocate To Order");
		allocateToOrderWindow.setShowMinimizeButton(false);
		allocateToOrderWindow.setIsModal(true);
		allocateToOrderWindow.setShowModalMask(true);
		allocateToOrderWindow.centerInPage();
		allocateToOrderWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				allocateToOrderWindow.destroy();
			}
		});

		VLayout allocateToOrderLayout = new VLayout();
		allocateToOrderLayout.setHeight100();
		allocateToOrderLayout.setWidth100();

		allocateToOrderListGrid = new ListGrid();
		allocateToOrderListGrid.setHeight(150);
		allocateToOrderListGrid.setSelectionType(SelectionStyle.SIMPLE);
		allocateToOrderListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		allocateToOrderListGrid.setShowFilterEditor(true);
		allocateToOrderListGrid.setAutoFetchData(true);

		allocateToOrderListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshFundInAllocateToOrderData();
					}
				});

		DataSource allocateToOrderDs = FinanceData.getAllocateToOrderData();
		allocateToOrderListGrid.setDataSource(allocateToOrderDs);
		allocateToOrderListGrid.setFields(Util.getListGridFieldsFromDataSource(allocateToOrderDs));
		allocateToOrderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);

		HLayout allocateToOrderButtons = new HLayout(5);

		final IButton buttonAllocateToOrder = new IButton("Allocate");
		IButton buttonCancel = new IButton("Cancel");

		buttonAllocateToOrder.setDisabled(true);
		buttonAllocateToOrder.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String destinationVenOrderId = allocateToOrderListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDER_ORDERID);
				String destinationVenOrderPaymentId = (allocateToOrderPaymentListGrid != null && allocateToOrderPaymentListGrid.getSelectedRecord() != null) ? allocateToOrderPaymentListGrid
						.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDERPAYMENT_ORDERPAYMENTID): null;

				ListGridRecord[] selection = fundInReconciliationListGridWidget.getSelection();

				for (int i = 0; i < selection.length; i++) {
					/*
					 * Either the source VenOrderPayment or the FinFundsInReconRecord
					 * must be provided for the allocation. The reason is that 
					 * payments which are yet to be allocated to VenOrderPayment
					 * and payments that are already allocated can be allocated.
					 */
					String sourceVenOrderPaymentId = null;
					String fundsInReconRecordId = null;
					String allocationAmount = null;
					String sourceVenWCSOrderId = null;
					if (selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID) != null 
							&& !selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID).isEmpty()) {
						sourceVenOrderPaymentId = selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID);
						//allocationAmount = selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT);
						allocationAmount= allocateToOrderPaymentListGrid.getSelectedRecord().getAttributeAsString("AllocationAmount");
						sourceVenWCSOrderId= selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID);
					} else {
						fundsInReconRecordId = selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
						allocationAmount= allocateToOrderPaymentListGrid.getSelectedRecord().getAttributeAsString("AllocationAmount");
					}
					getUiHandlers().onAllocate(sourceVenWCSOrderId,sourceVenOrderPaymentId, fundsInReconRecordId, destinationVenOrderPaymentId, allocationAmount, destinationVenOrderId);
					
					//getUiHandlers().onAllocate(sourceVenOrderPaymentId, fundsInReconRecordId, destinationVenOrderPaymentId, allocationAmount, destinationVenOrderId);
				}
				allocateToOrderWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				allocateToOrderWindow.destroy();
			}
		});
		
		allocateToOrderButtons.setAlign(Alignment.CENTER);
		allocateToOrderButtons.setMembers(buttonAllocateToOrder, buttonCancel);

		final VLayout allocateToOrderPaymentLayout = new VLayout();
		allocateToOrderPaymentLayout.setHeight(150);

		allocateToOrderLayout.setMembers(allocateToOrderListGrid, allocateToOrderPaymentLayout, allocateToOrderButtons);
		allocateToOrderWindow.addItem(allocateToOrderLayout);

		Criteria criteria = new Criteria();
		criteria.addCriteria(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "FP");
		criteria.addCriteria(DataNameTokens.VENORDER_FULFILLMENTSTATUS, 2);
		allocateToOrderListGrid.setCriteria(criteria);

		allocateToOrderListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
					 */
					@Override
					public void onSelectionChanged(SelectionEvent event) {
						buttonAllocateToOrder.setDisabled(allocateToOrderListGrid.getSelection().length != 1);

						if (allocateToOrderListGrid.getSelection().length == 1) {
							String orderId = allocateToOrderListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDER_ORDERID);
							
							ListGridRecord[] selection = fundInReconciliationListGridWidget.getSelection();
							String amountAllocation =null;							
							if (selection[0].getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_OVERPAIDFUNDSRECEIVED )){
								amountAllocation=""+ new BigDecimal(selection[0].getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT)).abs();
							}else{
								amountAllocation=selection[0].getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT);
							}
							System.out.println("Amount Allocation = "+amountAllocation);		
							DataSource allocateToOrderPaymentDs = FinanceData.getAllocateToOrderPaymentData(orderId,amountAllocation);
							allocateToOrderPaymentListGrid = new ListGrid();
							allocateToOrderPaymentListGrid.setHeight(150);
							allocateToOrderPaymentListGrid.setSelectionType(SelectionStyle.SIMPLE);
							allocateToOrderPaymentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
							allocateToOrderPaymentListGrid.setAutoFetchData(true);

							allocateToOrderPaymentListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
										@Override
										public void onSelectionChanged(SelectionEvent event) {
											buttonAllocateToOrder.setDisabled(allocateToOrderPaymentListGrid.getSelection().length > 1);
										}
									});
							allocateToOrderPaymentListGrid.setSaveLocally(true);
							allocateToOrderPaymentListGrid.setDataSource(allocateToOrderPaymentDs);
							allocateToOrderPaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(allocateToOrderPaymentDs));
							allocateToOrderPaymentListGrid.getField(DataNameTokens.VENORDERPAYMENT_ORDERPAYMENTID).setHidden(true);
							allocateToOrderPaymentListGrid.getField("AllocationAmount").setCanEdit(true);

							allocateToOrderPaymentLayout.setMembers(allocateToOrderPaymentListGrid);
						} else {
							allocateToOrderPaymentLayout.removeMember(allocateToOrderPaymentListGrid);
						}
					}
				});

		return allocateToOrderWindow;
	}
	
	/**
	 * Builds the refund window as a modal dialog
	 * @return the window once built
	 */
	private Window buildRefundWindow() {
		refundWindow = new Window();
		refundWindow.setWidth(500);
		refundWindow.setHeight(300);
		refundWindow.setTitle("Refund");
		refundWindow.setShowMinimizeButton(false);
		refundWindow.setIsModal(true);
		refundWindow.setShowModalMask(true);
		refundWindow.centerInPage();
		refundWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				refundWindow.destroy();
			}
		});

		VLayout refundLayout = new VLayout();
		refundLayout.setHeight100();
		refundLayout.setWidth100();

		refundListGrid = new ListGrid();
		refundListGrid.setHeight100();

		// Column0 is for FundInReconciliationId
		ListGridField fundInReconciliationRecordIdField = new ListGridField("column0", "Reconciliation Record Id");
		// Column1 is for OrderId
		ListGridField orderIdField = new ListGridField("column1", "Order Id");
		// Column2 is for Account
		ListGridField accountField = new ListGridField("column2", "Account");
		// Column3 is for Paid Amount
		ListGridField paidAmountField = new ListGridField("column3", "Paid Amount");
		// Column4 is for Fee
		ListGridField feeAmountField = new ListGridField("column4", "Fee");
		// Column5 is for Refund Amount
		ListGridField refundAmountField = new ListGridField("column5", "Refund Amount");

		fundInReconciliationRecordIdField.setHidden(true);
		accountField.setValueMap("Customer", "Bank");
		accountField.setDefaultValue("Customer");
		Util.formatListGridFieldAsCurrency(paidAmountField);
		Util.formatListGridFieldAsCurrency(feeAmountField);
		Util.formatListGridFieldAsCurrency(refundAmountField);

		accountField.setCanEdit(true);
		feeAmountField.setCanEdit(true);	
		refundAmountField.setCanEdit(true);	
		
		feeAmountField.addCellSavedHandler(new CellSavedHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.CellSavedHandler#onCellSaved(com.smartgwt.client.widgets.grid.events.CellSavedEvent)
			 */
			@Override
			public void onCellSaved(CellSavedEvent event) {
				Record record = event.getRecord();

				String fee = (String) event.getNewValue();
				try {
					// try to set into Double, if fails, reset column4(Fee) to
					// previous value
					@SuppressWarnings("unused")
					Double dblFee = new Double(fee);
				} catch (Exception e) {
					record.setAttribute("column4", (String) event.getOldValue());
					return;
				}

				String paidAmountField = record.getAttributeAsString("column3");
				if (paidAmountField != null && !paidAmountField.isEmpty() && fee != null && !fee.isEmpty()) {
					// sets column5/Refund fee as Paid Amount minus Fee
					record.setAttribute("column5", new Double((new Double(
							paidAmountField) - new Double(fee))).toString());
				}
			}
		});
		refundListGrid.setFields(fundInReconciliationRecordIdField, orderIdField, accountField, paidAmountField, feeAmountField,	refundAmountField);

		ListGridRecord[] selectedFundsInRecords = fundInReconciliationListGridWidget.getSelection();
		ListGridDataRecord[] refundRecords = new ListGridDataRecord[selectedFundsInRecords.length];

		for (int i = 0; i < selectedFundsInRecords.length; i++) {
			refundRecords[i] = new ListGridDataRecord(
					new String[] {
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID),
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID),	"Customer",
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT), "0",
							//Note that substring is used because the remaining balance amount is shown as -ve
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT)});
		}

		refundListGrid.setData(refundRecords);
		HLayout refundButtons = new HLayout(5);
		IButton buttonRefund = new IButton("Refund");
		IButton buttonCancel = new IButton("Cancel");

		buttonRefund.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] records = refundListGrid.getRecords();
				HashMap<String, String> refundDataMap = new HashMap<String, String>();

				for (int i = 0; i < records.length; i++) {
					HashMap<String, String> refundMap = new HashMap<String, String>();
					refundMap.put("RECONCILIATIONRECORDID",	records[i].getAttributeAsString("column0"));
					refundMap.put("ACCOUNT",	records[i].getAttributeAsString("column2"));
					refundMap.put("FEE",	records[i].getAttributeAsString("column4"));
					refundMap.put("REFUNDAMOUNT",	records[i].getAttributeAsString("column5"));
					
					refundDataMap.put("REFUNDDATA" + i, refundMap.toString());
				}

				getUiHandlers().onRefundButtonClicked(refundDataMap);
				refundWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				refundWindow.destroy();
			}
		});
		refundButtons.setAlign(Alignment.CENTER);
		refundButtons.setMembers(buttonRefund, buttonCancel);

		refundLayout.setMembers(refundListGrid, refundButtons);
		refundWindow.addItem(refundLayout);

		return refundWindow;
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter.MyView#refreshSalesRecordData()
	 */
	@Override
	public void refreshFundInData() {
		if (fundInReconciliationListGridWidget instanceof FundInReconciliationListGridWidget) {
			((FundInReconciliationListGridWidget) fundInReconciliationListGridWidget).refreshFundInReconData();
		}
	}
	
	/**
	 * Refeshes the funds in allocate to order data in the list grid
	 */
	private void refreshFundInAllocateToOrderData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData,	DSRequest request) {
				allocateToOrderListGrid.setData(response.getData());
			}
		};

		allocateToOrderListGrid.getDataSource().fetchData(allocateToOrderListGrid.getFilterEditorCriteria(), callBack);
	}
	
	private void showDeliveryStatusTracking(ListGridRecord record, String taskId){
		ListGrid deliveryStatusTrackingListGrid;
		
		deliveryStatusTrackingListGrid = new ListGrid();
		
		deliveryStatusTrackingListGrid.setWidth100();
		deliveryStatusTrackingListGrid.setHeight100();
		deliveryStatusTrackingListGrid.setShowAllRecords(true);
		deliveryStatusTrackingListGrid.setSortField(0);
		
		deliveryStatusTrackingListGrid.setShowFilterEditor(true);
		
		deliveryStatusTrackingListGrid.setAutoFetchData(true);
	
		deliveryStatusTrackingListGrid.setCanResizeFields(true);
		deliveryStatusTrackingListGrid.setShowRowNumbers(true);
		
		DataSource dataSource = LogisticsData.getDeliveryStatusTrackingData(taskId);
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
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID).setHidden(true);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_DESTINATION).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER).setWidth(120);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_STATUS).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECIPIENT).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RELATION).setWidth(100);
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_SERVICE).setHidden(true);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		deliveryStatusTrackingListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setCellFormatter(new CellFormatter() {
			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				return "<span style='color:blue;text-decoration:underline;cursor:hand;cursor:pointer'>"+value+"</span>";
			}
		});
		
		taskDetailLayout.setMembers(deliveryStatusTrackingListGrid);

	}
		
	/**
	 * Returns a VLayout widget with the details of the journal for viewing during approval.
	 * @param record the ListGridRecord selected from the journal approval group
	 * @return the new VLayout
	 */
	private VLayout buildJournalDetailVLayout(ListGridRecord record){
		/*
		 * This is where we build the middle and bottom layouts
		 * for the journal details
		 */
		
		ListGrid journalDetailList = new ListGrid();
		journalDetailList.setWidth(100);
		
		Canvas bottomMember = new VLayout();
		
		/*
		 * Instantiate the bottom layout widget builder
		 */
		JournalVLayoutBottomWidgetBuilder jvlbwb = new JournalVLayoutBottomWidgetBuilder();
		
		/*
		 * Journal Detail Records
		 * Use the widget builder to build the middle table 
		 * showing the journal transactions
		 */
		journalDetailList = jvlbwb.buildJournalDetailListGrid(record);
		journalDetailList.setWidth100();
		
		if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).contains("Cash Receive Journal")) {

			/*
			 * Cash Receive Journal Type
			 * Use the widget builder to build the bottom 
			 * layout for cash received/funds-in journal types.
			 */
			ListGrid fundsInRecordListGrid = jvlbwb.buildFundsInRecordListGrid(record);
			fundsInRecordListGrid.setWidth100();
			bottomMember = fundsInRecordListGrid;
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).contains("Sales Journal")) {			
			/*
			 * Sales Journal Type
			 * Use the widget builder to build the bottom
			 * layout for sales journal types.
			 */
			ListGrid salesRecordListGrid = jvlbwb.buildSalesRecordListGrid(record, null);
			salesRecordListGrid.setWidth100();
			bottomMember = salesRecordListGrid;
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).contains("Payment Journal")) {
			/*
			 * Payment Journal Type
			 * Use the widget builder to build the bottom
			 * layout for payment journal types
			 */
			
			VLayout paymentForm = jvlbwb.buildPaymentForm(record);
			paymentForm.setWidth100();
			bottomMember = paymentForm;
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).contains("Logistics Debt Acknowledgement Journal")) {
			/*
			 * Logistics Debt Acknowledgement Journal Type
			 * Use the widget builder to build the bottom
			 * layout for the the LDA journal type (shows invoice)
			 */
			VLayout invoiceForm = jvlbwb.buildInvoiceForm(record);
			invoiceForm.setWidth100();
			bottomMember = invoiceForm; 
		} 

		/*
		 * This layout is simply used to pass the members back to the caller
		 */
		VLayout journalDetailVLayout = new VLayout();
		journalDetailVLayout.setWidth100();
		
		journalDetailVLayout.setMembers(journalDetailList, bottomMember);
		
		return journalDetailVLayout;
	}

	private VLayout buildCashReceiveSalesJournalReconciliationDetailVLayout(ListGridRecord record, String taskId){
		/*
		 * This is where we build the middle and bottom layouts for the details
		 */
		
		JournalVLayoutBottomWidgetBuilder jvlbwb = new JournalVLayoutBottomWidgetBuilder();
				
		ListGrid salesRecordListGrid = jvlbwb.buildSalesRecordListGrid(record, taskId);
		salesRecordListGrid.setWidth100();

		/*
		 * This layout is simply used to pass the members back to the caller
		 */
		VLayout cashReceiveSalesJournalReconciliationVLayout = new VLayout();
		cashReceiveSalesJournalReconciliationVLayout.setWidth100();
		
		cashReceiveSalesJournalReconciliationVLayout.setMembers(salesRecordListGrid);
		
		return cashReceiveSalesJournalReconciliationVLayout;
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.task.presenter.ToDoListPresenter.MyView#refreshToDoListData()
	 */
	@Override
	public void refreshToDoListData() {
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				toDoListGrid.setData(response.getData());
			}
		};
		
		toDoListGrid.getDataSource().fetchData(toDoListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Gets the type of task based on the selected list grid record(s)
	 * @param selectedRecords is the selected list grid records
	 * @return the task type
	 */
	private Long getTaskType(ListGridRecord[] selectedRecords){
		Long taskType = null;
		for(int i=0; i < selectedRecords.length; i++){
			String sTaskDesc = selectedRecords[i].getAttribute(DataNameTokens.TASKTYPE);
			if(sTaskDesc.startsWith("Finance")){
				taskType = ProcessNameTokens.TASK_TYPE_FINANCE;
			}else if(sTaskDesc.startsWith("Fraud")){
				taskType = ProcessNameTokens.TASK_TYPE_FRAUD;
			}else if(sTaskDesc.startsWith("Logistic")){
				taskType = ProcessNameTokens.TASK_TYPE_LOGISTICS;
			}else if(sTaskDesc.startsWith("KPI")){
				taskType = ProcessNameTokens.TASK_TYPE_KPI;
			}else{
				taskType = ProcessNameTokens.TASK_TYPE_UNKNOWN;
			}
		}
		return taskType;
	}
}
