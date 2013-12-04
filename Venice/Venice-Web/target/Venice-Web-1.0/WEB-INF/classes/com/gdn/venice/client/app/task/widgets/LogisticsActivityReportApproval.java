package com.gdn.venice.client.app.task.widgets;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliation;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliationProblem;
import com.gdn.venice.client.app.logistic.widgets.MerchantPickupDetailWindow;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.StatusNameTokens;
import com.gdn.venice.client.app.task.view.command.ActivityReportReconciliationTaskDetail;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class LogisticsActivityReportApproval extends VLayout {
	public LogisticsActivityReportApproval(Record record) {
		String taskId = record.getAttributeAsString(DataNameTokens.TASKID);
		
		ToolStrip taskDetailToolStrip = new ToolStrip();
		taskDetailToolStrip.setWidth100();
		
		final ToolStripButton approveButton = new ToolStripButton();
		approveButton.setIcon("[SKIN]/icons/process_accept.png");
		approveButton.setTooltip("Approve");

		final ToolStripButton rejectButton = new ToolStripButton();
		rejectButton.setIcon("[SKIN]/icons/process_delete.png");
		rejectButton.setTooltip("Reject");
		
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
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#getCellCSSText(com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
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
		airwayBillListGrid.setDataSource(dataSource);
		airwayBillListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setWidth("5%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID).setHidden(true);
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE).setWidth("5%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID).setWidth("10%");
		airwayBillListGrid.getField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC).setWidth("10%");
		
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
		
//		airwayBillListGrid.setHilites(ActivityReportReconciliation.hilites);
		
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
				ListGridRecord[] selectedRecords = airwayBillListGrid.getSelection();
				ArrayList<String> airwayBillIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				}
				
				ActivityReportReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (ActivityReportReconciliation) airwayBillListGrid, "approve");
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = airwayBillListGrid.getSelection();
				ArrayList<String> airwayBillIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				}
				
				ActivityReportReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (ActivityReportReconciliation) airwayBillListGrid, "reject");
			}
		});
		
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).equals(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL_APPROVALACTIVITYNAME)) {
			setMembers(taskDetailToolStrip, airwayBillListGrid);
		} else {
			setMembers(airwayBillListGrid);
		}
	}

}
