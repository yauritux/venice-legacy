package com.gdn.venice.client.app.finance.widgets;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.view.PaymentProcessingView;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * The payment tab from the payment processing screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PaymentTabWidget extends Tab{
	
	/*
	 * The VLayout used to position the tab
	 */
	VLayout paymentVLayout;
	
	/*
	 * The button used to submit for approval
	 */
	ToolStripButton submitForApprovalButton;
	
	ToolStripButton exportButton, doneButton, printButton;
	
	/*
	 * The paymentsListGridWidget ListGrid widget
	 */
	PaymentsListGridWidget paymentsListGridWidget;

	/**
	 * Constructor to build the tab view
	 * @param title is the title of the tab
	 */
	public PaymentTabWidget(final PaymentProcessingView view, String title) {
		super(title);
		
		paymentVLayout = new VLayout();
		
		ToolStrip paymentToolStrip = new ToolStrip();
		paymentToolStrip.setWidth100();
		paymentToolStrip.setPadding(2);

		submitForApprovalButton = new ToolStripButton();
		submitForApprovalButton.setIcon("[SKIN]/icons/process.png");
		submitForApprovalButton.setTooltip("Submit For Approval");
		submitForApprovalButton.setTitle("Submit");
		submitForApprovalButton.setDisabled(true);

		paymentToolStrip.addButton(submitForApprovalButton);

		doneButton = new ToolStripButton();
		doneButton.setIcon("[SKIN]/icons/accept.png");
		doneButton.setTooltip("Finish Payment");
		doneButton.setTitle("Done");
		doneButton.setDisabled(true);

		paymentToolStrip.addButton(doneButton);

		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export Merchant Payment Report");
		exportButton.setTitle("Export");
		exportButton.setDisabled(true);

		paymentToolStrip.addButton(exportButton);

		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Payment List");
		printButton.setTitle("Print");
		
		paymentToolStrip.addSeparator();
		
		paymentToolStrip.addButton(printButton);

		paymentsListGridWidget = new PaymentsListGridWidget();

		paymentVLayout.setMembers(paymentToolStrip);
		
		setPane(paymentVLayout);
		
		submitForApprovalButton.addClickHandler(new ClickHandler(){

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = paymentsListGridWidget
						.getSelection();
				ArrayList<String> paymentRecordIds = new ArrayList<String>();
				for (int i = 0; i < selectedRecords.length; i++) {
					paymentRecordIds.add(selectedRecords[i]
							.getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
				}

				/*
				 * Link this to the view and the view will call
				 * the presenter UI handler
				 */
				view.onSubmitForApproval(paymentRecordIds);
			}
		});
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(paymentsListGridWidget);	
			}
		});

		doneButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
				ArrayList<String> paymentRecordIds = new ArrayList<String>();
				for (int i = 0; i < selectedRecords.length; i++) {
					paymentRecordIds.add(selectedRecords[i]
							.getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
				}
		
				view.onDoneButtonClicked(paymentRecordIds);
			}
		});
		
		exportButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = paymentsListGridWidget.getSelection();
				ArrayList<String> paymentRecordIds = new ArrayList<String>();
				for (int i = 0; i < selectedRecords.length; i++) {
					paymentRecordIds.add(selectedRecords[i]
							.getAttributeAsString(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
				}
		
				/*
				 * Link this to the view and the view will call
				 * the presenter UI handler
				 */
				view.onExportButtonClicked(paymentRecordIds);
			}
		});
	}
	
	/**
	 * Loads the paymentsListGridWidget data from the data source into the PaymentsListGridWidget widget's list grid
	 * @param dataSource for loading the payments data
	 */
	public void loadPaymentData(DataSource dataSource) {
		paymentsListGridWidget.setDataSource(dataSource);
		paymentsListGridWidget.setAutoFetchData(true);
		paymentsListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		paymentsListGridWidget.setShowRowNumbers(true);
		
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PPH23_AMOUNT).setWidth("20%");		
		paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("20%");
		
		Util.formatListGridFieldAsCurrency(paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
		Util.formatListGridFieldAsCurrency(paymentsListGridWidget.getField(DataNameTokens.FINAPPAYMENT_PPH23_AMOUNT));

		paymentVLayout.addMember(paymentsListGridWidget);
		
		/*
		 * manage the controls on selection changed for the list grid
		 */
		paymentsListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler(){

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				
				// Disable the submit for approval button unless there are selected records 
				if(paymentsListGridWidget.getSelection().length == 0){
					submitForApprovalButton.setDisabled(true);
					doneButton.setDisabled(true);
					exportButton.setDisabled(true);
					return;
				}
				
				exportButton.setDisabled(false);
				
				/*
				 * Disable the submit for approval button if any of the
				 * selected records is not status New or Rejected
				 */
				
				ListGridRecord[] selection = paymentsListGridWidget.getSelection();
				
				for(int i=0; i<selection.length; ++i){
					if(selection[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("New")
						|| selection[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("Rejected")){
						submitForApprovalButton.setDisabled(false);
					}else{
						submitForApprovalButton.setDisabled(true);						
						break;
					}
				}
				
				for(int i=0; i<selection.length; ++i){
					if(selection[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("Approved")){
						doneButton.setDisabled(false);
					} else{
						doneButton.setDisabled(true);
						break;
					}
				}
			}});		
	}
	
	/**
	 * Returns the VLayout for the payment
	 * @return the payment VLayout
	 */
	public VLayout getPaymentLayout() {
		return paymentVLayout;
	}
	
	
}
