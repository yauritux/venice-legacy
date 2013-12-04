package com.gdn.venice.client.app.finance.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter;
import com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers;
import com.gdn.venice.client.app.finance.widgets.FundInReconciliationListGridWidget;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.ui.data.ListGridDataRecord;
import com.gdn.venice.client.util.PrintUtility;
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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
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
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Funds-In Reconciliation
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FundInReconciliationView extends ViewWithUiHandlers<FundInReconciliationUiHandlers> implements FundInReconciliationPresenter.MyView {
	/*
	 * View layout to use for the main screen layout
	 */
	private RafViewLayout fundInReconciliationLayout;

	private Window refundWindow;
	private Window allocateToOrderWindow;
	private Window uploadWindow;
	private Window commentHistoryWindow;
	private Window cancelAllocateWindow;
	private Window cancelRefundWindow;

	private FundInReconciliationListGridWidget fundInReconciliationListGridWidget;
	
	private String[] fundInReconciliationGroupField;
	
	/*
	 * The list grid objects used to display the records
	 */
	private ListGrid refundListGrid;
	private ListGrid allocateToOrderListGrid;
	private ListGrid allocateToOrderPaymentListGrid;
	private ListGrid allocaterListGrid;
	private ListGrid cancelRefundListGrid;

	/*
	 * Buttons for the various form functions
	 */
	private ToolStripButton submitForApprovalButton;
	private ToolStripButton refundButton;
	private ToolStripButton allocateToOrderButton;
	private ToolStripButton deleteFundsInReportButton;
	private ToolStripButton printButton;
	private ToolStripButton cancelPayment;	
	private ToolStripButton buttonCancelAllocate ;
	private ToolStripButton buttonCancelRefund ;
	private ToolStripButton createJournalForVAButton ;


	/**
	 * Basic constructor used to build the view
	 */
	@Inject
	public FundInReconciliationView() {
		fundInReconciliationLayout = new RafViewLayout();

		/*
		 * Setup the toolstrip
		 */
		ToolStrip fundInReconciliationDetailItemToolStrip = new ToolStrip();
		fundInReconciliationDetailItemToolStrip.setWidth100();
		fundInReconciliationDetailItemToolStrip.setPadding(2);

		ToolStripButton uploadButton = new ToolStripButton();
		uploadButton.setIcon("[SKIN]/icons/up.png");
		uploadButton.setTooltip("Upload New Reconciliation Report");
		uploadButton.setTitle("Upload");
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildUploadWindow().show();
			}
		});

		refundButton = new ToolStripButton();
		refundButton.setIcon("[SKIN]/icons/refund.png");
		refundButton.setTooltip("Refund");
		refundButton.setTitle("Refund");

		allocateToOrderButton = new ToolStripButton();
		allocateToOrderButton.setIcon("[SKIN]/icons/allocateorder.png");
		allocateToOrderButton.setTooltip("Allocate to Order");
		allocateToOrderButton.setTitle("Allocate");

		submitForApprovalButton = new ToolStripButton();
		submitForApprovalButton.setIcon("[SKIN]/icons/process.png");
		submitForApprovalButton.setTooltip("Submit For Approval");
		submitForApprovalButton.setTitle("Submit");

		deleteFundsInReportButton = new ToolStripButton();
		deleteFundsInReportButton.setIcon("[SKIN]/icons/delete.png");
		deleteFundsInReportButton.setTooltip("Delete Funds-In Report");
		deleteFundsInReportButton.setTitle("Delete Report");
		
		cancelPayment = new ToolStripButton();
		cancelPayment.setIcon("[SKIN]/icons/delete.png");
		cancelPayment.setTooltip("Cancel Payment");
		cancelPayment.setTitle("Cancel Payment");
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Reconciliation Record List");
		printButton.setTitle("Print");
		
		buttonCancelAllocate = new ToolStripButton();
		buttonCancelAllocate.setIcon("[SKIN]/icons/allocateorder.png");
		buttonCancelAllocate.setTooltip("Cancel Allocate to Order");
		buttonCancelAllocate.setTitle("Cancel Allocate");
		
		buttonCancelRefund = new ToolStripButton();
		buttonCancelRefund.setIcon("[SKIN]/icons/refund.png");
		buttonCancelRefund.setTooltip("Cancel Refund");
		buttonCancelRefund.setTitle("Cancel Refund");
		
		createJournalForVAButton = new ToolStripButton();
		createJournalForVAButton.setIcon("[SKIN]/icons/process.png");
		createJournalForVAButton.setTooltip("Create Journal For VA");
		createJournalForVAButton.setTitle("Create Journal For VA");
		

		refundButton.setVisible(false);
		allocateToOrderButton.setVisible(false);
		submitForApprovalButton.setVisible(false);
		cancelPayment.setDisabled(true);
		buttonCancelAllocate.setVisible(true);
		buttonCancelRefund.setVisible(true);
		deleteFundsInReportButton.setVisible(false);
		printButton.setVisible(false);
		createJournalForVAButton.setVisible(false);

		fundInReconciliationDetailItemToolStrip.addButton(uploadButton);
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(refundButton);
		fundInReconciliationDetailItemToolStrip.addButton(buttonCancelRefund);		
		fundInReconciliationDetailItemToolStrip.addButton(allocateToOrderButton);
		fundInReconciliationDetailItemToolStrip.addButton(buttonCancelAllocate);		
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(submitForApprovalButton);
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(deleteFundsInReportButton);
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(cancelPayment);
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(printButton);
		fundInReconciliationDetailItemToolStrip.addSeparator();
		fundInReconciliationDetailItemToolStrip.addButton(createJournalForVAButton);
		
		fundInReconciliationListGridWidget = new FundInReconciliationListGridWidget() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#getCellCSSText(com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
			@Override
			protected String getCellCSSText(ListGridRecord record, int rowNum,
					int colNum) {
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
			
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {  
				
				final String fieldName = this.getFieldName(colNum);  
				
				if (fieldName.equals(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC)) {  		
		
					HLayout layout = new HLayout();
					layout.setWidth100();
					layout.setHeight(18);
					
					Label label  = new Label();
					label.setHeight(18);
					label.setWidth100();
					
					IButton button = new IButton();  
					button.setHeight(18);
					button.setWidth(18);
					button.setIcon("[SKIN]/icons/search.png");  
					
					button.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) { 		
							buildSelectActionTakenHistoryWindow(record.getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)).show();		

						}						
					});
					
					layout.setMembers(label, button);
					return layout; 
					
				} else
					return null;
			}
	       
		};

		fundInReconciliationListGridWidget.addCellClickHandler(new CellClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.CellClickHandler#onCellClick(com.smartgwt.client.widgets.grid.events.CellClickEvent)
			 */
			@Override
			public void onCellClick(CellClickEvent event) {
				/*
				 * Overridden to instantiate the comments window on cell click
				 */
				if (fundInReconciliationListGridWidget.getField(event.getColNum()).getName().equals(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENTHISTORY)) {
					buildCommentHistoryWindow(event.getRecord().getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)).show();
				}
			}
		});
		
		fundInReconciliationListGridWidget.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				fundInReconciliationListGridWidget.saveAllEdits();
				refreshFundInData();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}				
			}
		});
		
		fundInReconciliationLayout	.setMembers(fundInReconciliationDetailItemToolStrip);
		
		bindCustomUiHandlers();
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter.MyView#loadSalesRecordData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadFundInReconciliationData(DataSource dataSource, Map<String,String> paymentTypeMap, Map<String,String> bankMap,Map<String,String> parentPaymentReportMap) {
		fundInReconciliationListGridWidget.setDataSource(dataSource);
		fundInReconciliationListGridWidget.setAutoFetchData(false);
		fundInReconciliationListGridWidget.setShowRowNumbers(true);
		fundInReconciliationListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));

		fundInReconciliationListGridWidget.addCellSavedHandler(new CellSavedHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.CellSavedHandler#onCellSaved(com.smartgwt.client.widgets.grid.events.CellSavedEvent)
			 */
			@Override
			public void onCellSaved(CellSavedEvent event) {
				String[] groupFieldTemp = fundInReconciliationGroupField;
				fundInReconciliationListGridWidget.ungroup();
				fundInReconciliationGroupField = groupFieldTemp;
				if (fundInReconciliationGroupField != null) {
					fundInReconciliationListGridWidget.groupBy(fundInReconciliationGroupField);
				}
			}
		});

		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setValueMap(paymentTypeMap);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setValueMap(bankMap);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setValueMap(parentPaymentReportMap);
		
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
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF).setWidth(120);
	
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setWidth(120);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setWidth(50);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_STATUSORDER).setWidth(50);		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setWidth(140);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).setWidth(100);		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).setWidth(130);		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setWidth(100);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setCanEdit(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FETCH).setWidth(10);		
		

		fundInReconciliationListGridWidget.setShowRecordComponents(true);          
		fundInReconciliationListGridWidget.setShowRecordComponentsByCell(true);  
		fundInReconciliationListGridWidget.setSelectionType(SelectionStyle.SIMPLE);
		        
		//fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FETCH).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setHidden(true);
		
//		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
//		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCanFilter(false);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setCanFilter(false);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_STATUSORDER).setCanFilter(false);
		
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT));

		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT).setSummaryFunction(SummaryFunctionType.SUM);

		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setShowGroupSummary(false);
		fundInReconciliationListGridWidget.setShowGroupSummary(true);
		fundInReconciliationListGridWidget.setShowGridSummary(true);

		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {
					@Override
					public String format(Object value, ListGridRecord record,
							int rowNum, int colNum) {
						if (value != null) {
							String cellFormat = (String) value;
							cellFormat = cellFormat.substring(
									cellFormat.lastIndexOf("/") + 1,
									cellFormat.length());
							return "<a href='"
									+ GWT.getHostPageBaseURL()
									+ MainPagePresenter.fileDownloadPresenterServlet
									+ "?filename=" + value
									+ "' target='_blank'>" + cellFormat
									+ "</a>";
						}
						return (String) value;
					}
				});

		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setGroupTitleRenderer(new GroupTitleRenderer() {
					@Override
					public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
						String groupTitle = (String) groupValue;

						if (!groupTitle.startsWith("/")) {
							return groupTitle;
						}

						groupTitle = groupTitle.substring(
								groupTitle.lastIndexOf("/") + 1,
								groupTitle.length());
						return "<a href='"
								+ GWT.getHostPageBaseURL()
								+ MainPagePresenter.fileDownloadPresenterServlet
								+ "?filename=" + groupValue
								+ "' target='_blank'>" + groupTitle + "</a>";
					}
				});		
		fundInReconciliationLayout.addMember(fundInReconciliationListGridWidget);
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
	 * Builds the upload window as a modal dialog
	 * @return the window once it is built
	 */
	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(360);
		uploadWindow.setHeight(140);
		uploadWindow.setTitle("Upload Bank Report");
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

		final SelectItem reportTypeSelectItem = new SelectItem();
		reportTypeSelectItem.setTitle("Report Type");
		reportTypeSelectItem.setValueMap("BCA Virtual Account", "Mandiri Virtual Account", "KlikBCA", "Mandiri Klikpay", "CIMBClicks", "MIGS Credit Card", "KlikPay Full Payment IB", "KlikPay Full Payment CC", "KlikPay Installment CC", "XL Tunai","Mandiri Installment","BRI");

		// FileItem reportFileItem = new FileItem();
		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("Bank Report");
		uploadForm.setItems(reportTypeSelectItem, reportFileItem);

		HLayout uploadButtons = new HLayout(5);

		IButton buttonUpload = new IButton("Upload");
		IButton buttonCancel = new IButton("Cancel");

		buttonUpload.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();
				/*
				 * Swap the host URL in development so that the 
				 * form connects to the right servlet
				 */
				if(host.contains(":8889")){
					host = "http://localhost:8090/";
				}else{
					host = host.substring(0, host.lastIndexOf("/", host.length() - 2) + 1);
				}

				if (reportTypeSelectItem.getValueAsString().equals("BCA Virtual Account")) {
					uploadForm.setAction(host + "Venice/BCA_VA_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("Mandiri Virtual Account")) {
					uploadForm.setAction(host + "Venice/Mandiri_VA_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("KlikBCA") ){ //payment disendirikan agar tidak terbentur unik dari reportnya
					uploadForm.setAction(host + "Venice/BCA_IB_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("Mandiri Klikpay")) {
					uploadForm.setAction(host + "Venice/Mandiri_IB_ReportImportServlet");
				}  else if (reportTypeSelectItem.getValueAsString().equals("CIMBClicks")) {
					uploadForm.setAction(host + "Venice/Niaga_IB_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("MIGS Credit Card") ) { //payment disendirikan agar tidak terbentur unik dari reportnya
					uploadForm.setAction(host + "Venice/BCA_CC_ReportImportServlet");
				}  else if ( reportTypeSelectItem.getValueAsString().equals("KlikPay Full Payment IB")) {
					uploadForm.setAction(host + "Venice/KlikPay_IB_ReportImportServlet");
				}  else if (reportTypeSelectItem.getValueAsString().equals("KlikPay Full Payment CC")) {
					uploadForm.setAction(host + "Venice/KlikPay_CC_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("KlikPay Installment CC")) {
					uploadForm.setAction(host + "Venice/KlikPayInst_CC_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("XL Tunai")) {
					uploadForm.setAction(host + "Venice/XL_IB_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("Mandiri Installment")) {
					uploadForm.setAction(host + "Venice/Mandiri_Installment_ReportImportServlet");
				} else if (reportTypeSelectItem.getValueAsString().equals("BRI")) {
					uploadForm.setAction(host + "Venice/BRI_IB_ReportImportServlet");
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
		uploadButtons.setAlign(Alignment.CENTER);
		uploadButtons.setMembers(buttonUpload, buttonCancel);

		uploadLayout.setMembers(uploadForm, uploadButtons);
		uploadWindow.addItem(uploadLayout);
		return uploadWindow;
	}
	
	private Window buildCancelRefundWindow(){
		cancelRefundWindow = new Window();
		cancelRefundWindow.setWidth(500);
		cancelRefundWindow.setHeight(300);
		cancelRefundWindow.setTitle("Allocation Order");
		cancelRefundWindow.setShowMinimizeButton(false);
		cancelRefundWindow.setIsModal(true);
		cancelRefundWindow.setShowModalMask(true);
		cancelRefundWindow.centerInPage();
		cancelRefundWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				cancelRefundWindow.destroy();
			}
		});

		VLayout allocateToOrderLayout = new VLayout();
		allocateToOrderLayout.setHeight100();
		allocateToOrderLayout.setWidth100();

		cancelRefundListGrid = new ListGrid();
		cancelRefundListGrid.setHeight(150);
		cancelRefundListGrid.setSelectionType(SelectionStyle.SIMPLE);
		cancelRefundListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		cancelRefundListGrid.setShowFilterEditor(false);
		cancelRefundListGrid.setAutoFetchData(true);

		cancelRefundListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshFundInRefundToOrderData();
					}
				});
		ListGridRecord[] selectedFundsInRecords = fundInReconciliationListGridWidget.getSelection();
		
		DataSource allocateToOrderDs = FinanceData.getRefundData(selectedFundsInRecords[0].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
		cancelRefundListGrid.setDataSource(allocateToOrderDs);
		cancelRefundListGrid.setFields(Util.getListGridFieldsFromDataSource(allocateToOrderDs));
		cancelRefundListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID).setHidden(true);
		cancelRefundListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setHidden(true);
		
		HLayout canceloOrderButtons = new HLayout(5);

		final IButton buttonAllocateToOrder = new IButton("Cancel Refund");
		IButton buttonCancel = new IButton("Cancel");
		
		cancelRefundListGrid.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] itemRecords = cancelRefundListGrid.getSelection();
				if(itemRecords.length>0){
					buttonAllocateToOrder.setDisabled(false);
				}else{
					buttonAllocateToOrder.setDisabled(true);
				}
				
			}
		});

		buttonAllocateToOrder.setDisabled(true);
		buttonAllocateToOrder.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] itemRecords = cancelRefundListGrid.getSelection();
				HashMap<String, String> refundDataMap = new HashMap<String, String>();
				
				for (int i = 0; i < itemRecords.length; i++) {
					HashMap<String, String> refundMap = new HashMap<String, String>();
					refundMap.put("RECONCILIATIONRECORDID",	itemRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
					refundMap.put("ACCOUNT",	"Customer");
					refundMap.put("FEE",	"0");
					refundMap.put("REFUNDAMOUNT",	itemRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT));
					refundMap.put("CANCEL","TRUE");
					refundMap.put("IDREFUND",itemRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID));
					
					refundDataMap.put("REFUNDDATA" + i, refundMap.toString());
				}

				getUiHandlers().onRefundButtonClicked(refundDataMap);			
				cancelRefundWindow.destroy();					
			}
		});		
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				cancelRefundWindow.destroy();
			}
		});
		
		canceloOrderButtons.setAlign(Alignment.CENTER);
		canceloOrderButtons.setMembers(buttonAllocateToOrder, buttonCancel);	

		allocateToOrderLayout.setMembers(cancelRefundListGrid,  canceloOrderButtons);
		cancelRefundWindow.addItem(allocateToOrderLayout);	

		return cancelRefundWindow;
	}

	/**
	 * Builds the refund window as a modal dialog
	 * @return the window once built
	 */
	private Window buildAllocationWindow() {
		cancelAllocateWindow = new Window();
		cancelAllocateWindow.setWidth(500);
		cancelAllocateWindow.setHeight(300);
		cancelAllocateWindow.setTitle("Allocation Order");
		cancelAllocateWindow.setShowMinimizeButton(false);
		cancelAllocateWindow.setIsModal(true);
		cancelAllocateWindow.setShowModalMask(true);
		cancelAllocateWindow.centerInPage();
		cancelAllocateWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				cancelAllocateWindow.destroy();
			}
		});

		VLayout allocateToOrderLayout = new VLayout();
		allocateToOrderLayout.setHeight100();
		allocateToOrderLayout.setWidth100();

		allocaterListGrid = new ListGrid();
		allocaterListGrid.setHeight(150);
		allocaterListGrid.setSelectionType(SelectionStyle.SIMPLE);
		allocaterListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		allocaterListGrid.setShowFilterEditor(false);
		allocaterListGrid.setAutoFetchData(true);

		allocaterListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshFundInAllocateToOrderData();
					}
				});
		ListGridRecord[] selectedFundsInRecords = fundInReconciliationListGridWidget.getSelection();
		
		DataSource allocateToOrderDs = FinanceData.getAllocateData(selectedFundsInRecords[0].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
		allocaterListGrid.setDataSource(allocateToOrderDs);
		allocaterListGrid.setFields(Util.getListGridFieldsFromDataSource(allocateToOrderDs));
		allocaterListGrid.getField(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD).setHidden(true);
		
		HLayout allocateToOrderButtons = new HLayout(5);

		final IButton buttonAllocateToOrder = new IButton("Cancel Allocate");
		IButton buttonCancel = new IButton("Cancel");
		
		allocaterListGrid.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] itemRecords = allocaterListGrid.getSelection();
				if(itemRecords.length>0){
					buttonAllocateToOrder.setDisabled(false);
				}else{
					buttonAllocateToOrder.setDisabled(true);
				}
				
			}
		});

		buttonAllocateToOrder.setDisabled(true);
		buttonAllocateToOrder.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] itemRecords = allocaterListGrid.getSelection();
				HashMap<String, String> cancelDataMap = new HashMap<String, String>();

				for (int i = 0; i < itemRecords.length; i++) {
					HashMap<String, String> cancelMap = new HashMap<String, String>();
					cancelMap.put("RECONCILIATIONRECORDID",	itemRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD));					
					cancelDataMap.put("CANCELDATA" + i, cancelMap.toString());
				}
				getUiHandlers().onCancelAllocationButtonClicked(cancelDataMap);				
				cancelAllocateWindow.destroy();					
			}
		});		
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				cancelAllocateWindow.destroy();
			}
		});
		
		allocateToOrderButtons.setAlign(Alignment.CENTER);
		allocateToOrderButtons.setMembers(buttonAllocateToOrder, buttonCancel);	

		allocateToOrderLayout.setMembers(allocaterListGrid,  allocateToOrderButtons);
		cancelAllocateWindow.addItem(allocateToOrderLayout);	

		return cancelAllocateWindow;
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

		IButton buttonRefund = new IButton("Refund");
		IButton buttonCancel = new IButton("Close");
		
		for (int i = 0; i < selectedFundsInRecords.length; i++) {
			String refundTo = selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).contains("Customer")?"Customer":(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).contains("Bank")?"Bank":"Customer");
			if(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).contains("Refunded") && selectedFundsInRecords.length<2
				&& !selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)	){			

			}else if(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)){
				buttonRefund.setDisabled(true);
			}
			refundRecords[i] = new ListGridDataRecord(
					new String[] {
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID),
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID),	refundTo,
							selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT), "0",
							//Note that substring is used because the remaining balance amount is shown as -ve
							new BigDecimal(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT)).compareTo(new BigDecimal("0"))<0?new BigDecimal(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT)).abs().toString():new BigDecimal(selectedFundsInRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT)).abs().toString()});
		}

		refundListGrid.setData(refundRecords);
		HLayout refundButtons = new HLayout(5);
	
		
		refundListGrid.addEditCompleteHandler(new EditCompleteHandler(){
			@Override
			public void onEditComplete(EditCompleteEvent event) {			
						ListGridRecord record = refundListGrid.getSelectedRecord();
						if(new BigDecimal(record.getAttributeAsString("column5")).compareTo(new BigDecimal(record.getAttributeAsString("column3")))>0){
							SC.say("Refund Amount should be less than Paid Amount or same with Paid Amount");	
							record.setAttribute("column5", "0");
					}				
			}
		});

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
					refundMap.put("CANCEL","FALSE");
					refundMap.put("IDREFUND","0");
					
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
		refundButtons.setMembers(buttonRefund,buttonCancel);

		refundLayout.setMembers(refundListGrid, refundButtons);
		refundWindow.addItem(refundLayout);

		return refundWindow;
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
						allocationAmount= allocateToOrderPaymentListGrid.getSelectedRecord().getAttributeAsString("AllocationAmount");
						sourceVenWCSOrderId= selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID);
									
					} else {
						fundsInReconRecordId = selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
						allocationAmount= allocateToOrderPaymentListGrid.getSelectedRecord().getAttributeAsString("AllocationAmount");
					}
					boolean trueOrFalse = new BigDecimal(allocationAmount).compareTo(new BigDecimal(selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT)).subtract(new BigDecimal(selection[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT)))) > 0 ;
					if (!trueOrFalse){
						getUiHandlers().onAllocate(sourceVenWCSOrderId,sourceVenOrderPaymentId, fundsInReconRecordId, destinationVenOrderPaymentId, allocationAmount, destinationVenOrderId);
						allocateToOrderWindow.destroy();
					}else{
						SC.say("Allocation Amount more than Paid Amount! Please check against Allocation Amount");
					}
					
				}
				
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
								amountAllocation=new BigDecimal(selection[0].getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT)).subtract(new BigDecimal(selection[0].getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT))).toString();
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
	 * Builds the commeht history window as a modal dialog to display the history of comments
	 * @param reconciliationRecordId the reconcilliation id to use for lookup of comments
	 * @return the window once built
	 */
	private Window buildCommentHistoryWindow(String reconciliationRecordId) {
		commentHistoryWindow = new Window();
		commentHistoryWindow.setWidth(400);
		commentHistoryWindow.setHeight(400);
		commentHistoryWindow.setTitle("Comment History");
		commentHistoryWindow.setShowMinimizeButton(false);
		commentHistoryWindow.setIsModal(true);
		commentHistoryWindow.setShowModalMask(true);
		commentHistoryWindow.centerInPage();
		commentHistoryWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				commentHistoryWindow.destroy();
			}
		});

		VLayout commentHistoryLayout = new VLayout(5);
		commentHistoryLayout.setHeight100();
		commentHistoryLayout.setWidth100();

		ListGrid commentHistoryListGrid = new ListGrid();
		commentHistoryListGrid.setHeight100();
		commentHistoryListGrid.setWidth100();

		DataSource dataSource = FinanceData.getFundInReconRecordCommentHistoryData(reconciliationRecordId);
		commentHistoryListGrid.setDataSource(dataSource);
		commentHistoryListGrid.setAutoFetchData(true);
		commentHistoryListGrid.setSortField(0);
		commentHistoryListGrid.setSortDirection(SortDirection.DESCENDING);

		commentHistoryListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));

		HLayout closeButtonLayout = new HLayout(0);
		closeButtonLayout.setHeight(30);

		IButton buttonClose = new IButton("Close");

		buttonClose.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				commentHistoryWindow.destroy();
			}
		});
		closeButtonLayout.setAlign(Alignment.CENTER);
		closeButtonLayout.setMembers(buttonClose);

		commentHistoryLayout.setMembers(commentHistoryListGrid, closeButtonLayout);
		commentHistoryWindow.addItem(commentHistoryLayout);
		return commentHistoryWindow;
	}
	
	/**
	 * Builds the commeht history window as a modal dialog to display the history of comments
	 * @param reconciliationRecordId the reconcilliation id to use for lookup of comments
	 * @return the window once built
	 */
	private Window buildSelectActionTakenHistoryWindow(String reconciliationRecordId) {
		final Window actionTakenHistoryWindow = new Window();
		actionTakenHistoryWindow.setWidth(400);
		actionTakenHistoryWindow.setHeight(400);
		actionTakenHistoryWindow.setTitle("Action Taken History");
		actionTakenHistoryWindow.setShowMinimizeButton(false);
		actionTakenHistoryWindow.setIsModal(true);
		actionTakenHistoryWindow.setShowModalMask(true);
		actionTakenHistoryWindow.centerInPage();
		actionTakenHistoryWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				actionTakenHistoryWindow.destroy();
			}
		});

		VLayout actionHistoryLayout = new VLayout(5);
		actionHistoryLayout.setHeight100();
		actionHistoryLayout.setWidth100();

		ListGrid actionHistoryListGrid = new ListGrid();
		actionHistoryListGrid.setHeight100();
		actionHistoryListGrid.setWidth100();
		System.out.println(reconciliationRecordId);
		DataSource dataSource = FinanceData.getActionTakenHistoryData(reconciliationRecordId);		
		actionHistoryListGrid.setDataSource(dataSource);		
		actionHistoryListGrid.setAutoFetchData(true);
		actionHistoryListGrid.setSortField(0);
		actionHistoryListGrid.setSortDirection(SortDirection.DESCENDING);
		actionHistoryListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		actionHistoryListGrid.getField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_ID).setHidden(true);
		Util.formatListGridFieldAsCurrency(actionHistoryListGrid.getField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_AMOUNT));
	

		HLayout closeButtonLayout = new HLayout(0);
		closeButtonLayout.setHeight(30);

		IButton buttonClose = new IButton("Close");

		buttonClose.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				actionTakenHistoryWindow.destroy();
			}
		});
		closeButtonLayout.setAlign(Alignment.CENTER);
		closeButtonLayout.setMembers(buttonClose);

		actionHistoryLayout.setMembers(actionHistoryListGrid, closeButtonLayout);
		actionTakenHistoryWindow.addItem(actionHistoryLayout);
		return actionTakenHistoryWindow;
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
	
	private void refreshFundInRefundToOrderData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData,	DSRequest request) {
				cancelRefundListGrid.setData(response.getData());
			}
		};

		cancelRefundListGrid.getDataSource().fetchData(cancelRefundListGrid.getFilterEditorCriteria(), callBack);
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return fundInReconciliationLayout;
	}

	/**
	 * Binds the custom UI handlers to the view
	 */
	protected void bindCustomUiHandlers() {
		refundButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildRefundWindow().show();
			}
		});
		
		buttonCancelAllocate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildAllocationWindow().show();
			}
		});
		
		printButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(fundInReconciliationListGridWidget);	
			}
		});
		
		createJournalForVAButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
//				PrintUtility.printComponent(fundInReconciliationListGridWidget);	
			}
		});


		allocateToOrderButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/*
				 * Get the selected funds-in record id and 
				 * pass it to the allocate to order window
				 * becaue it is needed in the server side 
				 * call to create the journal
				 */
				buildAllocateToOrderWindow().show();
			}
		});

		submitForApprovalButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = fundInReconciliationListGridWidget.getSelection();
				ArrayList<String> fundInReconRecordIds = new ArrayList<String>();
				for (int i = 0; i < selectedRecords.length; i++) {
					fundInReconRecordIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)+"&"+selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID));
				}
				getUiHandlers().onSubmitForApproval(fundInReconRecordIds);
			}
		});
		
		createJournalForVAButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = fundInReconciliationListGridWidget.getSelection();
				ArrayList<String> fundInReconRecordIds = new ArrayList<String>();
				for (int i = 0; i < selectedRecords.length; i++) {
					fundInReconRecordIds.add(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				}
				getUiHandlers().onCreateJournalForVA(fundInReconRecordIds);
			}
		});

		deleteFundsInReportButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				if(fundInReconciliationListGridWidget.getSelection().length!=0){
					SC.ask("Are you sure you want to delete this Report ?", new BooleanCallback() {					
						@Override
						public void execute(Boolean value) {
							if(value != null && value){

								ListGridRecord[] records = fundInReconciliationListGridWidget.getSelection();

								HashMap<String, String> deleteDataMap = new HashMap<String, String>();
								for (int i = 0; i < records.length; i++) {
									
									HashMap<String, String> deleteMap = new HashMap<String, String>();
									deleteMap.put("RECONCILIATIONRECORDID", records[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));	
									deleteDataMap.put("DELETEDATA" + i, deleteMap.toString());		
								}
								getUiHandlers().onDelete(deleteDataMap);
								SC.say("Data Removed");							
								refreshFundInData();
								}
							}
						});
					}else
						SC.say("Please select the data to be Removed");
			}
		});
		
		cancelPayment.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(fundInReconciliationListGridWidget.getSelection().length!=0){
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							fundInReconciliationListGridWidget.removeSelectedData();
							SC.say("Data Removed");							
							refreshFundInData();
							}
						}
					});
				}else
					SC.say("Please select the data to be Removed");
				}
			});

		buttonCancelRefund.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				buildCancelRefundWindow().show();
			}
		});	
		
		fundInReconciliationListGridWidget.addSelectionChangedHandler(new SelectionChangedHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
					 */
					@Override
					public void onSelectionChanged(SelectionEvent event) {				
						/*
						 * Most of the code here is used to enable/disable the
						 * buttons on the form appropriately
						 */
						ListGridRecord[] selectedRecords = fundInReconciliationListGridWidget.getSelection();
						if (selectedRecords.length == 0) {
							// If no records selected, disable Submit For
							// Approval Button
							submitForApprovalButton.setVisible(false);
							refundButton.setVisible(false);
							allocateToOrderButton.setVisible(false);
							cancelPayment.setVisible(false);
							buttonCancelAllocate.setVisible(false);
							buttonCancelRefund.setVisible(false);
							deleteFundsInReportButton.setVisible(false);
							printButton.setVisible(false);
							createJournalForVAButton.setVisible(false);
						} else {
							printButton.setVisible(true);
							deleteFundsInReportButton.setVisible(true);
							for (int i = 0; i < selectedRecords.length; i++) {
								
								/* If it has been submitted, approved, no payment, payment not recognized, or No Data from MTA, disable Submit For Approval Button
								 */
								boolean bDisableSubmitForApprovalButton = selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
								 || selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT);

								submitForApprovalButton.setVisible(!bDisableSubmitForApprovalButton);

								if (bDisableSubmitForApprovalButton) {
									break;
								}
							}
							/*
							 * Disable the refund button if the record has already been refunded, no payment, all funds received, payment timeout, submitted/approved
							 */
							refundButton.setVisible(false);
							for (int i = 0; i < selectedRecords.length; i++) {
								/*
								 * Hanya yang bukan order no paymet dan order yang belum di approve yang bisa di refund
								 */								
								boolean bDisableRefundButton = selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT) 
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
								|| (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_ALLOCATED) 
								&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED))
								|| (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER)
								&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED))
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK)
								&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED)
								|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_REALTIME);

								refundButton.setVisible(!bDisableRefundButton);							
								if (bDisableRefundButton) {
									break;
								}
							}
							/*
							 * Disable the allocate to order button if the funds in record is no payment, all funds received, payment timeout, already submitted/approved
							 */
							allocateToOrderButton.setVisible(false);
							for (int i = 0; i < selectedRecords.length; i++) {
								/*
								 * Hanya Order yang statusnya baru dan bukan order no payment yang bisa di allocate
								 */		
									boolean bDisableAllocateButton = (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
											|| (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_ALLOCATED) 
											&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED))
											|| (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER)
											&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED))
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK)
											&& !selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED)
											||	selectedRecords.length>1
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_REALTIME));							
								
								
								allocateToOrderButton.setVisible(!bDisableAllocateButton);
								
								if (bDisableAllocateButton) {
									break;
								}
							}

							buttonCancelAllocate.setVisible(false);	
							for (int i = 0; i < selectedRecords.length; i++) {
								/*
								 * Hanya Order yang statusnya baru dan bukan order no payment yang bisa di allocate
								 */		
									boolean bCancleAllocateButton = (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_REALTIME));							
								
								
									buttonCancelAllocate.setVisible(!bCancleAllocateButton);
								
								if (bCancleAllocateButton) {
									break;
								}
							}
							buttonCancelRefund.setVisible(false);		
							for (int i = 0; i < selectedRecords.length; i++) {
								/*
								 * Hanya Order yang statusnya baru dan bukan order no payment yang bisa di allocate
								 */		
									boolean bCancelRefundButton = (selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED)
											|| selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_REALTIME));							
								
								
									buttonCancelRefund.setVisible(!bCancelRefundButton);
								
								if (bCancelRefundButton) {
									break;
								}
							}
							
							cancelPayment.setDisabled(false);
							for (int i = 0; i < selectedRecords.length; i++) {
										/*
								 * Hanya payment not recoqnized yang bisa di delete paymentnya
								 */								
								boolean bDisablecancelPaymentButton =!(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED) );
										
								
								cancelPayment.setDisabled(bDisablecancelPaymentButton);
								
								if (bDisablecancelPaymentButton) {
									break;
								}
							}
							
							createJournalForVAButton.setVisible(false);
							for (int i = 0; i < selectedRecords.length; i++) {
								/*
								 * Hanya payment not recoqnized yang bisa di delete paymentnya
								 */								
								boolean bVisibleCreateJournalForVAButton =(selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC).equals(DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC_H1));
								createJournalForVAButton.setVisible(bVisibleCreateJournalForVAButton);
								
								if (!bVisibleCreateJournalForVAButton) {
									break;
								}
							}
						}
					}
				});
	}

}
