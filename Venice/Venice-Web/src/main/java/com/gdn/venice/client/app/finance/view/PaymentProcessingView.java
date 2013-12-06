package com.gdn.venice.client.app.finance.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter;
import com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers;
import com.gdn.venice.client.app.finance.widgets.ArAmountManualJournalTransactionPickerWindowWidget;
import com.gdn.venice.client.app.finance.widgets.PaymentProcessingTabWidget;
import com.gdn.venice.client.app.finance.widgets.PaymentTabWidget;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * View for Payment Processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PaymentProcessingView extends
ViewWithUiHandlers<PaymentProcessingUiHandlers> implements
PaymentProcessingPresenter.MyView {

	private RafViewLayout paymentProcessingLayout;
	private PaymentProcessingTabWidget merchantPaymentProcessingTab;
	private PaymentProcessingTabWidget logisticsPaymentProcessingTab;
	private PaymentProcessingTabWidget refundPaymentProcessingTab;
	private PaymentTabWidget paymentTabWidget;
	
	private ListGrid merchantPaymentDetailListGrid;
	private ListGrid logisticsPaymentDetailListGrid;
	private ListGrid refundPaymentDetailListGrid;

	@Inject
	public PaymentProcessingView() {

		paymentProcessingLayout = new RafViewLayout();
		
		TabSet paymentProcessingTabSet = new TabSet();
		paymentProcessingTabSet.setTabBarPosition(Side.TOP);
		paymentProcessingTabSet.setWidth100();
		paymentProcessingTabSet.setHeight100();
		
		merchantPaymentProcessingTab = buildMerchantPaymentProcessingTab();
		logisticsPaymentProcessingTab = buildLogisticsPaymentProcessingTab();
		refundPaymentProcessingTab = buildRefundPaymentProcessingTab();
		paymentTabWidget = buildPaymentTab();
		
		paymentProcessingTabSet.addTab(merchantPaymentProcessingTab);
		paymentProcessingTabSet.addTab(logisticsPaymentProcessingTab);
		paymentProcessingTabSet.addTab(refundPaymentProcessingTab);
		paymentProcessingTabSet.addTab(paymentTabWidget);
		
		paymentProcessingLayout.setMembers(paymentProcessingTabSet);

		bindCustomUiHandlers();
	}

	
	/**
	 * Builds the Tab for Merchant Payment
	 * @return	Payment processing tab for merchant payment
	 */
	private PaymentProcessingTabWidget buildMerchantPaymentProcessingTab() {
        ClickHandler buttonProcessDetailPaymentClickHandler = new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = merchantPaymentDetailListGrid.getSelection();
				
				HashMap<String, String> selectedSalesRecords = new HashMap<String, String>();
				for (int i=0;i<selectedRecords.length;i++) {
					selectedSalesRecords.put("SalesRecord"+i, selectedRecords[i].getAttributeAsString(DataNameTokens.FINSALESRECORD_SALESRECORDID));
				}
				getUiHandlers().onProcessPaymentButtonClicked(FinanceData.getMerchantPaymentData(Util.formXMLfromHashMap(selectedSalesRecords)), "MERCHANT");
			}
		};
		
		
        return new PaymentProcessingTabWidget("Merchant Payment", 
        		buttonProcessDetailPaymentClickHandler
				);
		
	}
	
	/**
	 * Builds the Tab for Logistics Payment
	 * @return	Payment processing tab for logistics payment
	 */
	private PaymentProcessingTabWidget buildLogisticsPaymentProcessingTab() {
        ClickHandler buttonProcessDetailPaymentClickHandler = new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = logisticsPaymentDetailListGrid.getSelection();
				
				HashMap<String, String> selectedInvoices = new HashMap<String, String>();
				for (int i=0;i<selectedRecords.length;i++) {
					selectedInvoices.put("Invoice"+i, selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPINVOICE_APINVOICEID));
				}
				getUiHandlers().onProcessPaymentButtonClicked(FinanceData.getLogisticsPaymentData(Util.formXMLfromHashMap(selectedInvoices)), "LOGISTICS");
			}
		};
		return new PaymentProcessingTabWidget("Logistics Payment", 	buttonProcessDetailPaymentClickHandler);
	}
	
	/**
	 * Builds the Tab for Refund Payment
	 * @return	Payment processing tab for refund payment
	 */
	private PaymentProcessingTabWidget buildRefundPaymentProcessingTab() {
		ClickHandler buttonProcessDetailPaymentClickHandler = new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = refundPaymentDetailListGrid.getSelection();
				
				HashMap<String, String> selectedRefunds = new HashMap<String, String>();
				for (int i=0;i<selectedRecords.length;i++) {
					selectedRefunds.put("Refund"+i, selectedRecords[i].getAttributeAsString(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID));
				}
				getUiHandlers().onProcessPaymentButtonClicked(FinanceData.getRefundPaymentData(Util.formXMLfromHashMap(selectedRefunds)), "REFUND");
			}
		};
		
		return new PaymentProcessingTabWidget("Refund Payment", 
				buttonProcessDetailPaymentClickHandler
				);
		
	}
	
	/**
	 * Builds the Tab for List of Payments
	 * @return	Tab containing list of payments
	 */
	private PaymentTabWidget buildPaymentTab() {
        return new PaymentTabWidget(this, "Payment");
		
	}


	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return paymentProcessingLayout;
	}

	protected void bindCustomUiHandlers() {

	}
	
	/**
	 * Loads the data for merchant payment (based on Sales Record), this is for the top table in Merchant Payment Tab
	 */
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#loadMerchantPaymentProcessingData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadMerchantPaymentProcessingData(DataSource dataSource) {
		merchantPaymentDetailListGrid = new ListGrid();
		
		merchantPaymentDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		merchantPaymentDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		merchantPaymentDetailListGrid.setShowRowNumbers(true);
		
		merchantPaymentDetailListGrid.setShowFilterEditor(true);
		
		merchantPaymentDetailListGrid.setDataSource(dataSource);
		merchantPaymentDetailListGrid.setAutoFetchData(false);
		merchantPaymentDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setWidth("5%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setHidden(true);
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_MCX_DATE).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_CXF_DATE).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_MERCHANTPAYMENTAMOUNT).setWidth("20%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG).setWidth("10%");
		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT).setWidth("20%");

//		merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		Util.formatListGridFieldAsCurrency(merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_MERCHANTPAYMENTAMOUNT));
		Util.formatListGridFieldAsCurrency(merchantPaymentDetailListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT));
		
		merchantPaymentDetailListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				merchantPaymentProcessingTab.setButtonProcessDetailPaymentDisabled(merchantPaymentDetailListGrid.getSelection().length==0);
				merchantPaymentProcessingTab.getPaymentLayout().removeMembers(merchantPaymentProcessingTab.getPaymentLayout().getMembers());
			}
		});


		merchantPaymentProcessingTab.getPaymentDetailLayout().setMembers(merchantPaymentDetailListGrid);
		merchantPaymentProcessingTab.setPaymentListGrid(merchantPaymentDetailListGrid);
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#refreshMerchantPaymentProcessingData()
	 */
	@Override
	public void refreshMerchantPaymentProcessingData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				merchantPaymentDetailListGrid.setData(response.getData());
			}
		};
		
		merchantPaymentDetailListGrid.getDataSource().fetchData(merchantPaymentDetailListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Loads the data for logistics payment (based on Invoice), this is for the top table in Logistics Payment Tab
	 */
	@Override
	public void loadLogisticsPaymentProcessingData(DataSource dataSource) {
		logisticsPaymentDetailListGrid = new ListGrid();
		
		logisticsPaymentDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		logisticsPaymentDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		logisticsPaymentDetailListGrid.setShowRowNumbers(true);
		logisticsPaymentDetailListGrid.setShowFilterEditor(true);
		
		logisticsPaymentDetailListGrid.setDataSource(dataSource);
		logisticsPaymentDetailListGrid.setAutoFetchData(false);
		logisticsPaymentDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_APINVOICEID).setWidth("5%");
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_APINVOICEID).setHidden(true);
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setWidth("20%");
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER).setWidth("20%");
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEDATE).setWidth("20%");
		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT).setWidth("20%");
		
//		logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

		Util.formatListGridFieldAsCurrency(logisticsPaymentDetailListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT));
		
		logisticsPaymentDetailListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				logisticsPaymentProcessingTab.setButtonProcessDetailPaymentDisabled(logisticsPaymentDetailListGrid.getSelection().length==0);
				logisticsPaymentProcessingTab.getPaymentLayout().removeMembers(logisticsPaymentProcessingTab.getPaymentLayout().getMembers());
			}
		});


		logisticsPaymentProcessingTab.getPaymentDetailLayout().setMembers(logisticsPaymentDetailListGrid);
		logisticsPaymentProcessingTab.setPaymentListGrid(logisticsPaymentDetailListGrid);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#refreshLogisticsPaymentProcessingData()
	 */
	@Override
	public void refreshLogisticsPaymentProcessingData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				logisticsPaymentDetailListGrid.setData(response.getData());
			}
		};
		
		logisticsPaymentDetailListGrid.getDataSource().fetchData(logisticsPaymentDetailListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#loadRefundPaymentProcessingData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadRefundPaymentProcessingData(DataSource dataSource) {
		refundPaymentDetailListGrid = new ListGrid();
		
		refundPaymentDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		refundPaymentDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		refundPaymentDetailListGrid.setShowRowNumbers(true);
		refundPaymentDetailListGrid.setShowFilterEditor(true);
		
		refundPaymentDetailListGrid.setDataSource(dataSource);
		refundPaymentDetailListGrid.setAutoFetchData(false);
		refundPaymentDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
				
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID).setWidth("5%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID).setHidden(true);
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME).setWidth("25%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_WCSORDERID).setWidth("10%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE).setWidth("10%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT).setWidth("25%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP).setWidth("10%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_ACTION_TAKEN).setWidth("25%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE).setWidth("25%");
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_REASON).setWidth("25%");
		
//		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		Util.formatListGridFieldAsCurrency(refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT));
		Util.formatListGridFieldAsCurrency(refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE));
		refundPaymentDetailListGrid.getField(DataNameTokens.FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME).setCanFilter(false);
		
		refundPaymentDetailListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				refundPaymentProcessingTab.setButtonProcessDetailPaymentDisabled(refundPaymentDetailListGrid.getSelection().length==0);
				refundPaymentProcessingTab.getPaymentLayout().removeMembers(refundPaymentProcessingTab.getPaymentLayout().getMembers());
			}
		});

		refundPaymentProcessingTab.getPaymentDetailLayout().setMembers(refundPaymentDetailListGrid);
		refundPaymentProcessingTab.setPaymentListGrid(refundPaymentDetailListGrid);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#refreshRefundPaymentProcessingData()
	 */
	@Override
	public void refreshRefundPaymentProcessingData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				refundPaymentDetailListGrid.setData(response.getData());
			}
		};
		
		refundPaymentDetailListGrid.getDataSource().fetchData(refundPaymentDetailListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#loadPaymentData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadPaymentData(DataSource dataSource) {
		paymentTabWidget.loadPaymentData(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#loadPaymentDataForPaymentProcessing(com.smartgwt.client.data.DataSource, java.lang.String)
	 */
	@Override
	public void loadPaymentDataForPaymentProcessing(DataSource dataSource, String paymentData) {
		if (paymentData.equals("MERCHANT")) {
			//Merchant payment			
			final ListGrid merchantPaymentListGrid = new ListGrid();
			
			merchantPaymentListGrid.setSelectionType(SelectionStyle.SIMPLE);
			merchantPaymentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			merchantPaymentListGrid.setShowRowNumbers(true);
			
			merchantPaymentListGrid.setDataSource(dataSource);

			merchantPaymentListGrid.setGroupByField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME);
			merchantPaymentListGrid.setAutoFetchData(true);
			merchantPaymentListGrid.setSaveLocally(true);
			merchantPaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
			
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
			merchantPaymentListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT).setWidth("20%");			
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE).setWidth("20%");
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).setWidth("20%");
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).setCanEdit(true);
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).setHidden(true);
			merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS).setHidden(true);
			
			merchantPaymentListGrid.addCellClickHandler(new CellClickHandler() {
				
				@Override
				public void onCellClick(CellClickEvent event) {
					if (merchantPaymentListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT)) {
						String finApManualJournalTransctionIds = event.getRecord().getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS);
						
						String salesRecordIds = event.getRecord().getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS);
				
						ArAmountManualJournalTransactionPickerWindowWidget manualJournalWindow = new ArAmountManualJournalTransactionPickerWindowWidget(FinanceData.getFinApManualTransactionPaymentData(salesRecordIds, null), finApManualJournalTransctionIds, merchantPaymentListGrid, event.getRecord());
						manualJournalWindow.show();
					} 					
				}
			});
			
			Util.formatListGridFieldAsCurrency(merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
			Util.formatListGridFieldAsCurrency(merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
			Util.formatListGridFieldAsCurrency(merchantPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE));
			Util.formatListGridFieldAsCurrency(merchantPaymentListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT));
			
			HLayout makePaymentButtonLayout = new HLayout();
			makePaymentButtonLayout.setHeight(20);
			makePaymentButtonLayout.setPadding(2);
			makePaymentButtonLayout.setAlign(Alignment.CENTER);
			
			final IButton makePaymentButton = new IButton();
			makePaymentButton.setIcon("[SKIN]/icons/dollar_gold.png");
			makePaymentButton.setWidth(150);
			makePaymentButton.setTitle("Make Payment");
			makePaymentButton.setDisabled(true);
			makePaymentButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					ListGridRecord[] merchantPaymentRecords = merchantPaymentListGrid.getSelection();
					
					boolean bExistsRecordWithNoBankAccountId = false;
					for (int i=0;i<merchantPaymentRecords.length;i++) {
						if (merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID) == null || 
								merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).isEmpty()) {
							bExistsRecordWithNoBankAccountId = true;
							break;
						}
					}
					
					if (bExistsRecordWithNoBankAccountId) {
						SC.warn("Please select Bank Account Id for all Payments");
						
						return;
					}
					
					HashMap<String, String> paymentDataMap = new HashMap<String, String>();
					
					for (int i=0;i<merchantPaymentRecords.length;i++) {
						HashMap<String, String> merchantPaymentMap = new HashMap<String, String>();
						
						merchantPaymentMap.put("SALESRECORDIDS", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS));
						if (merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS)!=null && 
								!merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).isEmpty()) {
							merchantPaymentMap.put("FINAPMANUALJOURNALIDS", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS));
						}
						merchantPaymentMap.put("BANKACCOUNTID", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID));
						merchantPaymentMap.put("AMOUNT", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_AMOUNT));
						merchantPaymentMap.put("PENALTYAMOUNT", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
						merchantPaymentMap.put("PPH23AMOUNT", merchantPaymentRecords[i].getAttributeAsString(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT));
						
						paymentDataMap.put("PAYMENTDATA" + i, merchantPaymentMap.toString());
					}
					paymentDataMap.put("PAYMENTTO", "MERCHANT");
					getUiHandlers().onMakePaymentButtonClicked(paymentDataMap);
				}
			});
			
			makePaymentButtonLayout.setMembers(makePaymentButton);
			
			merchantPaymentListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					makePaymentButton.setDisabled(merchantPaymentListGrid.getSelection().length==0);
				}
			});

			merchantPaymentProcessingTab.getPaymentLayout().setMembers(merchantPaymentListGrid, makePaymentButtonLayout);
		} else if (paymentData.equals("LOGISTICS")) {
			//Logistics payment
			final ListGrid logisticsPaymentListGrid = new ListGrid();
			
			logisticsPaymentListGrid.setSelectionType(SelectionStyle.SIMPLE);
			logisticsPaymentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			logisticsPaymentListGrid.setShowRowNumbers(true);
			logisticsPaymentListGrid.setDataSource(dataSource);
			logisticsPaymentListGrid.setAutoFetchData(true);
			logisticsPaymentListGrid.setSaveLocally(true);
			logisticsPaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
			
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE).setWidth("20%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).setWidth("20%");
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).setCanEdit(true);
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).setHidden(true);
			logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINAPINVOICES).setHidden(true);
			
			logisticsPaymentListGrid.addCellClickHandler(new CellClickHandler() {
				
				@Override
				public void onCellClick(CellClickEvent event) {
					if (logisticsPaymentListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT)) {
						String finApManualJournalTransctionIds = event.getRecord().getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS);
						
						String invoiceIds = event.getRecord().getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPINVOICES);
				
						ArAmountManualJournalTransactionPickerWindowWidget manualJournalWindow = new ArAmountManualJournalTransactionPickerWindowWidget(FinanceData.getFinApManualTransactionPaymentData(null, invoiceIds), finApManualJournalTransctionIds, logisticsPaymentListGrid, event.getRecord());
						manualJournalWindow.show();
					} 
					
				}
			});
			

			Util.formatListGridFieldAsCurrency(logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
			Util.formatListGridFieldAsCurrency(logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
			Util.formatListGridFieldAsCurrency(logisticsPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE));
			
			HLayout makePaymentButtonLayout = new HLayout();
			makePaymentButtonLayout.setHeight(20);
			makePaymentButtonLayout.setPadding(2);
			makePaymentButtonLayout.setAlign(Alignment.CENTER);
			
			final IButton makePaymentButton = new IButton();
			makePaymentButton.setIcon("[SKIN]/icons/dollar_gold.png");
			makePaymentButton.setWidth(150);
			makePaymentButton.setTitle("Make Payment");
			makePaymentButton.setDisabled(true);
			makePaymentButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					ListGridRecord[] logisticsPaymentRecords = logisticsPaymentListGrid.getSelection();
					
					boolean bExistsRecordWithNoBankAccountId = false;
					for (int i=0;i<logisticsPaymentRecords.length;i++) {
						if (logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC) == null || 
								logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).isEmpty()) {
							bExistsRecordWithNoBankAccountId = true;
							break;
						}
					}
					
					if (bExistsRecordWithNoBankAccountId) {
						SC.warn("Please select Bank Account Id for all Payments");						
						return;
					}
					
					HashMap<String, String> paymentDataMap = new HashMap<String, String>();
					
					HashMap<String, String> logisticsPaymentMap = new HashMap<String, String>();
					
					for (int i=0;i<logisticsPaymentRecords.length;i++) {
						logisticsPaymentMap.put("INVOICEIDS", logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPINVOICES));
						if (logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS)!=null && 
								!logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).isEmpty()) {
							logisticsPaymentMap.put("FINAPMANUALJOURNALIDS", logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS));
						}
						logisticsPaymentMap.put("BANKACCOUNTID", logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC));
						logisticsPaymentMap.put("AMOUNT", logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_AMOUNT));
						logisticsPaymentMap.put("PENALTYAMOUNT", logisticsPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
						
						paymentDataMap.put("PAYMENTDATA" + i, logisticsPaymentMap.toString());
					}
					paymentDataMap.put("PAYMENTTO", "LOGISTICS");
					getUiHandlers().onMakePaymentButtonClicked(paymentDataMap);
				}
			});
			
			logisticsPaymentListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					makePaymentButton.setDisabled(logisticsPaymentListGrid.getSelection().length==0);
				}
			});
			
			makePaymentButtonLayout.setMembers(makePaymentButton);

			logisticsPaymentProcessingTab.getPaymentLayout().setMembers(logisticsPaymentListGrid, makePaymentButtonLayout);

		} else if (paymentData.equals("REFUND")) {
			//Refund Payment
			final ListGrid refundPaymentListGrid = new ListGrid();
			
			refundPaymentListGrid.setSelectionType(SelectionStyle.SIMPLE);
			refundPaymentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			
			refundPaymentListGrid.setDataSource(dataSource);
			refundPaymentListGrid.setAutoFetchData(true);
			refundPaymentListGrid.setSaveLocally(true);
			refundPaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
			
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE).setWidth("20%");
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).setWidth("20%");
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).setCanEdit(true);
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).setHidden(true);
			refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINARFUNDSINREFUNDS).setHidden(true);
			
			Util.formatListGridFieldAsCurrency(refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
			Util.formatListGridFieldAsCurrency(refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
			Util.formatListGridFieldAsCurrency(refundPaymentListGrid.getField(DataNameTokens.FINAPPAYMENT_BALANCE));
			
			HLayout makePaymentButtonLayout = new HLayout();
			makePaymentButtonLayout.setHeight(20);
			makePaymentButtonLayout.setPadding(2);
			makePaymentButtonLayout.setAlign(Alignment.CENTER);
			
			final IButton makePaymentButton = new IButton();
			makePaymentButton.setIcon("[SKIN]/icons/dollar_gold.png");
			makePaymentButton.setWidth(150);
			makePaymentButton.setTitle("Make Payment");
			makePaymentButton.setDisabled(true);
			makePaymentButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					ListGridRecord[] refundPaymentRecords = refundPaymentListGrid.getSelection();
					
					boolean bExistsRecordWithNoBankAccountId = false;
					for (int i=0;i<refundPaymentRecords.length;i++) {
						if (refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID) == null || 
								refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID).isEmpty()) {
							bExistsRecordWithNoBankAccountId = true;
							break;
						}
					}
					
					if (bExistsRecordWithNoBankAccountId) {
						SC.warn("Please select Bank Account Id for all Payments");						
						return;
					}
					
					HashMap<String, String> paymentDataMap = new HashMap<String, String>();
					
					HashMap<String, String> refundPaymentMap = new HashMap<String, String>();
					
					for (int i=0;i<refundPaymentRecords.length;i++) {
						refundPaymentMap.put("REFUNDRECORDIDS", refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINARFUNDSINREFUNDS));
						if (refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS)!=null && 
								!refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS).isEmpty()) {
							refundPaymentMap.put("FINAPMANUALJOURNALIDS", refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS));
						}
						refundPaymentMap.put("BANKACCOUNTID", refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID));
						refundPaymentMap.put("AMOUNT", refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_AMOUNT));
						refundPaymentMap.put("PENALTYAMOUNT", refundPaymentRecords[i].getAttributeAsString(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
						
						paymentDataMap.put("PAYMENTDATA" + i, refundPaymentMap.toString());
					}
					paymentDataMap.put("PAYMENTTO", "REFUND");
					getUiHandlers().onMakePaymentButtonClicked(paymentDataMap);
				}
			});
			
			refundPaymentListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					makePaymentButton.setDisabled(refundPaymentListGrid.getSelection().length==0);
				}
			});
			
			makePaymentButtonLayout.setMembers(makePaymentButton);

			refundPaymentProcessingTab.getPaymentLayout().setMembers(refundPaymentListGrid, makePaymentButtonLayout);

		}
	}

	/**
	 * A local stub to bind the approval submission to the widget
	 * (called by widget using view)
	 * @param apPaymentIds
	 */
	public void onSubmitForApproval(ArrayList<String> apPaymentIds) {
		getUiHandlers().onSubmitForApproval(apPaymentIds);
		
	}

	public void onDoneButtonClicked(ArrayList<String> apPaymentIds) {
		getUiHandlers().onDoneButtonClicked(apPaymentIds);
		
	}
	
	public void onExportButtonClicked(ArrayList<String> apPaymentIds) {
		getUiHandlers().onExportButtonClicked(apPaymentIds);
		
	}
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter.MyView#refreshPaymentData()
	 */
	@Override
	public void refreshPaymentData() {
	}
}
