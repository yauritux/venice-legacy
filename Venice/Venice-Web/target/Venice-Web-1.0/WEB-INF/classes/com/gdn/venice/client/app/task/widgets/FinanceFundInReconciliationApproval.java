package com.gdn.venice.client.app.task.widgets;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.widgets.FundInReconciliationListGridWidget;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.StatusNameTokens;
import com.gdn.venice.client.app.task.view.command.FundInReconciliationTaskDetail;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.grid.events.GroupByEvent;
import com.smartgwt.client.widgets.grid.events.GroupByHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class FinanceFundInReconciliationApproval extends VLayout {
	String[] fundInReconciliationGroupField;
	
	public FinanceFundInReconciliationApproval(Record record) {
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

		DataSource dataSource = FundInReconciliationTaskDetail.getFundInReconciliationDataSource(taskId); 
		final ListGrid fundInReconciliation = new FundInReconciliationListGridWidget() {

			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {

				//Only color the status and approval status
				if (getFieldName(colNum).equals(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC) ||
						getFieldName(colNum).equals(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC)) {
					String resultStatus = record.getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC);
					String approvalStatus = record.getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC);

					if (resultStatus == null || resultStatus.isEmpty()) {
						return super.getCellCSSText(record, rowNum, colNum);
					}
					//if OK, color it light green
					if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED.toUpperCase())) {
						return "background-color:#00FF00;";
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PARTIALFUNDSRECEIVED.toUpperCase())) {
						//if problem exists...
						if (approvalStatus!=null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							//...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							//...and not approved, color it yellow
							return "background-color:#ece355;";
						}
					} else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT.toUpperCase()) ||
							resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT.toUpperCase())) {
						//if no data from MTA and invalid GDN Ref
						if (approvalStatus!=null && approvalStatus.toUpperCase().contains(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED.toUpperCase())) {
							//...and approved, color it dark green
							return "color:#FFFFFF;background-color:#0e7365;";
						} else {
							//...and not approved, color it red
							return "color:#FFFFFF;background-color:#FF0000;";
						}
					}else if (resultStatus.toUpperCase().contains(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED.toUpperCase())) {
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
		fundInReconciliation.setDataSource(dataSource);
		fundInReconciliation.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		fundInReconciliation.addGroupByHandler(new GroupByHandler() {
			
			@Override
			public void onGroupBy(GroupByEvent event) {
				fundInReconciliationGroupField = event.getFields();
			}
		});
		
		fundInReconciliation.addCellSavedHandler(new CellSavedHandler() {
			
			@Override
			public void onCellSaved(CellSavedEvent event) {
				String[] groupFieldTemp = fundInReconciliationGroupField;
				fundInReconciliation.ungroup();
				fundInReconciliationGroupField = groupFieldTemp;
				if (fundInReconciliationGroupField!=null) {
					fundInReconciliation.groupBy(fundInReconciliationGroupField);
				}
			}
		});
		
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setHidden(true);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setWidth(120);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC).setWidth(120);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF).setWidth(120);
		//fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).setWidth("5%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setWidth(100);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).setWidth(100);		
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setWidth("10%");
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setCanEdit(true);
		
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setHidden(true);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setHidden(true);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setHidden(true);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID).setHidden(true);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setHidden( true);
		
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCanFilter(false);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setCanFilter(false);

		Util.formatListGridFieldAsCurrency(fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT));

		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);

		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setShowGroupSummary(false);

		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				if (value!=null) {
					String cellFormat = (String) value;
					cellFormat = cellFormat.substring(cellFormat.lastIndexOf("/")+1, cellFormat.length());
					return "<a href='" + GWT.getHostPageBaseURL() +
					MainPagePresenter.fileDownloadPresenterServlet +
					"?filename=" + value + "' target='_blank'>" + 
					cellFormat + "</a>";
				}
				return (String) value;
			}
		});

		fundInReconciliation.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setGroupTitleRenderer(new GroupTitleRenderer() {

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

		fundInReconciliation.groupBy(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS);

		fundInReconciliation.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bSelected = fundInReconciliation.getSelection().length > 0 && status.equals(StatusNameTokens.INPROCESS); 
				
				approveButton.setDisabled(!bSelected);
				rejectButton.setDisabled(!bSelected);
			}
		});
			
		
		approveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = fundInReconciliation.getSelection();
				ArrayList<String> airwayBillIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				}
				
				FundInReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (FundInReconciliationListGridWidget) fundInReconciliation, "approve");
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = fundInReconciliation.getSelection();
				ArrayList<String> airwayBillIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					airwayBillIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				}
				
				FundInReconciliationTaskDetail.onApprovalDecision(airwayBillIds, (FundInReconciliationListGridWidget) fundInReconciliation, "reject");
			}
		});
		
		if (record.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).equals(ProcessNameTokens.FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL_APPROVALACTIVITYNAME)) {
			setMembers(taskDetailToolStrip, fundInReconciliation);
		} else {
			setMembers(fundInReconciliation);
		}
	}
}
