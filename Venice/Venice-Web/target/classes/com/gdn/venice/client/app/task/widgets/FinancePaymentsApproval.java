package com.gdn.venice.client.app.task.widgets;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.widgets.PaymentsListGridWidget;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.StatusNameTokens;
import com.gdn.venice.client.app.task.view.command.PaymentsTaskDetail;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * AP Payment approval and rejection processing widget
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinancePaymentsApproval extends VLayout {
	
	/**
	 * Constructor to build the widget based on the task record
	 * @param taskRecord the record with the task id
	 */
	public FinancePaymentsApproval(Record taskRecord) {
		String taskId = taskRecord.getAttributeAsString(DataNameTokens.TASKID);
		
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
		
		final String status = taskRecord.getAttributeAsString(DataNameTokens.TASKSTATUS);
		
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

		paymentsListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				boolean bSelected = paymentsListGridWidget.getSelection().length > 0 && status.equals(StatusNameTokens.INPROCESS); 
				
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
				ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
				ArrayList<String> apPaymentIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					apPaymentIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
				}
				
				PaymentsTaskDetail.onApprovalDecision(apPaymentIds, (PaymentsListGridWidget) paymentsListGridWidget, "approve");
			}
		});
		
		rejectButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
				ArrayList<String> apPaymentIds = new ArrayList<String>();
				for (int i=0;i<selectedRecords.length;i++) {
					apPaymentIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
				}
				
				PaymentsTaskDetail.onApprovalDecision(apPaymentIds, (PaymentsListGridWidget) paymentsListGridWidget, "reject");
			}
		});
		
		if (taskRecord.getAttributeAsString(DataNameTokens.TASKDESCRIPTION).equals(ProcessNameTokens.FINANCEAPPAYMENTAPPROVAL_APPROVALACTIVITYNAME)) {
			setMembers(taskDetailToolStrip, paymentsListGridWidget);
		} else {
			setMembers(paymentsListGridWidget);
		}
	}
}
