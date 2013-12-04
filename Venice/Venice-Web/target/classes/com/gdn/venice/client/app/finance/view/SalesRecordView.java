package com.gdn.venice.client.app.finance.view;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.SalesRecordPresenter;
import com.gdn.venice.client.app.finance.view.handlers.SalesRecordUiHandlers;
import com.gdn.venice.client.app.finance.widgets.SalesRecordDetailAdjustmentWidget;
import com.gdn.venice.client.app.finance.widgets.SalesRecordDetailWidget;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Sales Record Screen
 * 
 * @author Henry Chandra
 */
public class SalesRecordView extends
ViewWithUiHandlers<SalesRecordUiHandlers> implements
SalesRecordPresenter.MyView {
	RafViewLayout salesRecordLayout;
	
	ListGrid salesRecordListGrid;

	private ToolStripButton printButton;
	private ToolStripButton returnRefundButton;
	private ToolStripButton exportButton;
	
	@Inject
	public SalesRecordView() {

		salesRecordLayout = new RafViewLayout();

		ToolStrip salesRecordToolStrip = new ToolStrip();
		salesRecordToolStrip.setWidth100();
		salesRecordToolStrip.setPadding(2);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Sales Record List");
		printButton.setTitle("Print");
		
		printButton.disable();
		
		returnRefundButton = new ToolStripButton();
		returnRefundButton.setIcon("[SKIN]/icons/refund.png");
		returnRefundButton.setTooltip("Return & Refund");
		returnRefundButton.setTitle("Return & Refund");
		returnRefundButton.disable();
		
		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export to Excel");
		exportButton.setTitle("Export");
		exportButton.disable();
		
		salesRecordToolStrip.addButton(printButton);
		salesRecordToolStrip.addSeparator();
		salesRecordToolStrip.addButton(returnRefundButton);
		salesRecordToolStrip.addSeparator();
		salesRecordToolStrip.addButton(exportButton);
		
		//add detail for adjustment
		salesRecordListGrid = new SalesRecordDetailWidget() {
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				String wcsOrderItemId = record.getAttributeAsString(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID);
				DataSource adjustmentDataSource = getUiHandlers().onExpandSalesRecordRow(wcsOrderItemId);
				return new SalesRecordDetailAdjustmentWidget(adjustmentDataSource);
			}
		};

		salesRecordLayout.setMembers(salesRecordToolStrip);

		bindCustomUiHandlers();
	}

	@Override
	public void loadSalesRecordData(DataSource dataSource) {
		
		
		salesRecordListGrid.setDataSource(dataSource);
		salesRecordListGrid.setAutoFetchData(false);
		salesRecordListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setHidden(true);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_RECONCILEDATE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_MCX_DATE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_CXF_DATE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE).setCanFilter(false);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE).setCanFilter(false);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth(150);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_QUANTITY).setWidth(50);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_PRICE).setWidth(150);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_TOTAL).setWidth(150);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNCOMMISIONAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_PROVIDER_CODE).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_INSURANCECOST).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST).setWidth(160);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT).setWidth(150);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_PAYMENT_STATUS).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL).setWidth(300);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL).setCanFilter(false);
		
//		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_PRICE));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_TOTAL));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNCOMMISIONAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_INSURANCECOST));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT));

		salesRecordLayout.addMember(salesRecordListGrid);
	}

	@Override
	public void refreshSalesRecordData() {
		
	}

	@Override
	public Widget asWidget() {
		return salesRecordLayout;
	}

	protected void bindCustomUiHandlers() {
		// Print Button click handler
		printButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(salesRecordListGrid);	
			}
		});
		
		// Grid record selection handler
		salesRecordListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				// Get selected record
				ListGridRecord[] selectedRecords = salesRecordListGrid.getSelection();
				
				// when no record is selected, top buttons are disabled
				if(selectedRecords.length == 0){
					returnRefundButton.disable();
					exportButton.disable();
					printButton.disable();
				// enable top buttons after a record is selected	
				}else{
					returnRefundButton.enable();
					exportButton.enable();
					printButton.enable();
				}
				
			}
		});
		
		// Return refund click handler
		returnRefundButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to Return & Refund this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									ListGridRecord[] selectedRecords = salesRecordListGrid.getSelection();
									
									HashMap<String, String> refundDataMap = new HashMap<String, String>();
									
									for (int i = 0; i < selectedRecords.length; i++) {
										ListGridRecord selectedRecord = selectedRecords[i];
										
										HashMap<String, String> refundMap = new HashMap<String, String>();
										refundMap.put("SALESRECORDID", selectedRecord.getAttributeAsString(DataNameTokens.FINSALESRECORD_SALESRECORDID));
										refundMap.put("WCSORDERITEMID", selectedRecord.getAttributeAsString(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID));
										
										refundDataMap.put("REFUNDDATA" + i, refundMap.toString());
										
									}
									
									getUiHandlers().onRefundButtonClicked(refundDataMap);									
									SC.say("Data Returned & Refunded");									
									refreshSalesRecordData();
									
								} 
							}
						});
			}
		});
		
		
		// export click handler
		exportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = salesRecordListGrid.getSelection();
				
				StringBuilder sbSelectedRecords = new StringBuilder();
				
				for (int i = 0; i < selectedRecords.length; i++) {
					ListGridRecord selectedRecord = selectedRecords[i];
					
					sbSelectedRecords.append(selectedRecord.getAttributeAsString(DataNameTokens.FINSALESRECORD_SALESRECORDID));
					
					if(i != selectedRecords.length -1)
						sbSelectedRecords.append(";");
				}
				
				String host = GWT.getHostPageBaseURL();

				//If in debug mode then change the host URL to the servlet in the server side
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
												
				Window.open(host + "Venice/SalesRecordReportLauncherServlet?salesRecordIds=" + sbSelectedRecords.toString(), "_blank", null);
							
			}
		});
		
		
	}

}
