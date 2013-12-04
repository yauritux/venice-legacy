package com.gdn.venice.client.app.finance.widgets;

import java.util.HashMap;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.data.RafDataSource;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.SummaryFunction;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A VLayout widget builder used to build the bottom of the journal screen,
 * the bottom of the manual journal screen and the ToDoListView bottom
 * layout when a journal approval happens.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class JournalVLayoutBottomWidgetBuilder {
	
	/**
	 * Builds the journal detail listgrid with the journal detail (transaction) data
	 * based on the selected journal approval group
	 * @param record		Selected record in Journal Approval Group
	 */
	public ListGrid buildJournalDetailListGrid(ListGridRecord record) {
		ListGrid journalDetailList = new ListGrid();
		
		journalDetailList.setShowAllRecords(true);
		journalDetailList.setSortField(0);
		
		journalDetailList.setAutoFetchData(true);
		
		journalDetailList.setCanResizeFields(true);
		
		DataSource dataSource = FinanceData.getJournalDetailData(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		journalDetailList.setDataSource(dataSource);
		journalDetailList.setAutoFetchData(true);
		
		journalDetailList.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		journalDetailList.setShowFilterEditor(true);
		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONID).setWidth("5%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONID).setHidden(true);
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC).setHidden(true);
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONTIMESTAMP).setWidth("10%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_REFF).setWidth("10%");		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_PAYMENT_TYPE).setWidth("15%");		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTDESC).setWidth("25%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setWidth("10%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setWidth("10%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC).setWidth("10%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_COMMENTS).setWidth("35%");
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_GROUP_JOURNAL).setHidden(true);
		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setSummaryFunction(SummaryFunctionType.SUM);
		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setCanFilter(false);
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setCanFilter(false);

		Util.formatListGridFieldAsCurrency(journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT));
		Util.formatListGridFieldAsCurrency(journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT));
		
		journalDetailList.getField(DataNameTokens.FINJOURNALTRANSACTION_REFF).setSummaryFunction(new SummaryFunction(){

			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				String wcsOrderId="";
				int count=0;
				for(Record item:records){
					if(wcsOrderId.equals("")){
						wcsOrderId=wcsOrderId+item.getAttribute(DataNameTokens.FINJOURNALTRANSACTION_REFF)+",";
						count++;
					}
					if(!wcsOrderId.contains(item.getAttribute(DataNameTokens.FINJOURNALTRANSACTION_REFF)) && 
							!item.getAttribute(DataNameTokens.FINJOURNALTRANSACTION_REFF).equals("") &&
							!item.getAttribute(DataNameTokens.FINJOURNALTRANSACTION_REFF).equals(null)){
						
						wcsOrderId=wcsOrderId+item.getAttribute(DataNameTokens.FINJOURNALTRANSACTION_REFF)+",";
						count++;
					}					
				}
				return count+" Orders";
			}
			
		});
		
		journalDetailList.setGroupStartOpen(GroupStartOpen.FIRST);
		journalDetailList.setShowGroupSummary(true);
		journalDetailList.setShowGridSummary(true);
		
		journalDetailList.groupBy(DataNameTokens.FINJOURNALTRANSACTION_GROUP_JOURNAL);

		return journalDetailList;
		
	}
	
	/**
	 * Build the widget with the details of Funds-In Record supporting Cash Receive Journal (this is for type of Cash Receive Journal)
	 * @param record	Selected record in Journal Approval Group
	 * @return the new FundInReconciliationListGridWidget
	 */
	public FundInReconciliationListGridWidget buildFundsInRecordListGrid(ListGridRecord record) {
	
		DataSource dataSource = getFundInReconciliationDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		
		FundInReconciliationListGridWidget fundInReconciliationListGridWidget = new FundInReconciliationListGridWidget()	{

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#getCellCSSText(com.smartgwt.client.widgets.grid.ListGridRecord, int, int)
			 */
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
		fundInReconciliationListGridWidget.setShowFilterEditor(false);
		fundInReconciliationListGridWidget.setDataSource(dataSource);
		fundInReconciliationListGridWidget.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		fundInReconciliationListGridWidget.setSelectionType(SelectionStyle.NONE);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).setWidth(75);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setWidth(75);
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
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT).setCanEdit(true);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCanFilter(false);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).setCanFilter(false);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID).setHidden(true);
	
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID).setHidden(true);
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID).setHidden(true);

		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT));
		
		fundInReconciliationListGridWidget.setShowGridSummary(true);
		
		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).setSummaryFunction(new SummaryFunction(){

			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				String wcsOrderId="";
				int count=0;
				for(Record item:records){
					if(wcsOrderId.equals("")){
						wcsOrderId=wcsOrderId+item.getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID)+",";
						count++;
					}
					if(!wcsOrderId.contains(item.getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID)) && 
							!item.getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).equals("") &&
							!item.getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID).equals(null)){
						
						wcsOrderId=wcsOrderId+item.getAttribute(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID)+",";
						count++;
					}					
				}
				return count+" Orders";
			}
			
		});
			
//		fundInReconciliationListGridWidget.getField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
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
		
		return fundInReconciliationListGridWidget;
		
	}
	
	/**
	 * Build the ListGrid widget with the details of Sales Record supporting Sales Journal (this is for type of Sales Journal)
	 * @param record is the selected record in Journal Approval Group
	 * @return the new ListGrid
	 */
	public ListGrid buildSalesRecordListGrid(ListGridRecord record, String taskId) {
		DataSource dataSource = getSalesRecordDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID), taskId);
		
		ListGrid salesRecordListGrid = new ListGrid();
		
		salesRecordListGrid.setAutoFetchData(true);
		salesRecordListGrid.setShowFilterEditor(false);
		salesRecordListGrid.setDataSource(dataSource);
		salesRecordListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		salesRecordListGrid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);

		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_SALESRECORDID).setHidden(true);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID).setWidth(75);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE).setWidth(75);
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
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT).setWidth(120);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT).setWidth(150);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_FINAPPAYMENT).setWidth(100);
		salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL).setWidth(300);
		
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_PRICE));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_TOTAL));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNCOMMISIONAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_INSURANCECOST));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT));
		Util.formatListGridFieldAsCurrency(salesRecordListGrid.getField(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT));
		
		salesRecordListGrid.setShowGridSummary(true);
		return salesRecordListGrid;
	}
	
	/**
	 * Builds the payment list grid to display payments related to the journal
	 * @param record the journal approval group record
	 * @return the payment list grid
	 */
	public ListGrid buildPaymentListGrid(ListGridRecord record) {
		DataSource dataSource = getPaymentDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		
		ListGrid paymentListGrid = new ListGrid();
		
		paymentListGrid.setAutoFetchData(true);
		paymentListGrid.setShowFilterEditor(false);
		paymentListGrid.setDataSource(dataSource);
		paymentListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setWidth("5%");
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_APPAYMENTID).setHidden(true);
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC).setWidth("20%");
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT).setWidth("20%");
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT).setWidth("20%");
		paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("20%");
		

		Util.formatListGridFieldAsCurrency(paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(paymentListGrid.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));
		
		return paymentListGrid;

	}
	
	/**
	 * Details of Payment form supporting Payment Journal (this is for type of Payment Journal)
	 * 		NOTE: This is not used presently as it is only valid for single relationship to journal
	 * @param record	Selected record in Journal Approval Group
	 */
	public VLayout buildPaymentForm(ListGridRecord record) {
		
		DataSource dataSource = getPaymentDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		
		Label paymentLabel = new Label();
		paymentLabel.setHeight(10);
		paymentLabel.setAlign(Alignment.LEFT);
		paymentLabel.setContents("<font size=\"4\"><b>Payment Information:</b></font>");
		
		DynamicForm paymentForm = new DynamicForm();
		paymentForm.setDataSource(dataSource);  
		paymentForm.setUseAllDataSourceFields(true);
		
		paymentForm.setFields(Util.getReadOnlyFormItemFromDataSource(dataSource));
		paymentForm.fetchData();
		
		Util.formatFormItemAsCurrency(paymentForm.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
		Util.formatFormItemAsCurrency(paymentForm.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));

		
		VLayout paymentVLayout = new VLayout();
		
		paymentVLayout.setPadding(5);
		paymentVLayout.setMembers(paymentLabel, paymentForm);
		
		return paymentVLayout;
		
	}
	
	/**
	 * Builds the invoice list grid to display payments related to the journal
	 * @param record the journal approval group record
	 * @return the invoice list grid
	 */
	public ListGrid buildInvoiceListGrid(ListGridRecord record) {
		DataSource dataSource = getInvoiceDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		
		ListGrid invoiceListGrid = new ListGrid();
		
		invoiceListGrid.setAutoFetchData(true);
		invoiceListGrid.setShowFilterEditor(false);
		invoiceListGrid.setDataSource(dataSource);
		invoiceListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_APINVOICEID).setWidth("5%");
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_APINVOICEID).setHidden(true);
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEDATE).setWidth("20%");
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER).setWidth("20%");
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT).setWidth("20%");
		invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEARAMOUNT).setWidth("20%");
		

		Util.formatListGridFieldAsCurrency(invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT));
		Util.formatListGridFieldAsCurrency(invoiceListGrid.getField(DataNameTokens.FINAPINVOICE_INVOICEARAMOUNT));
		
		return invoiceListGrid;

	}


	/**
	 * Details of Invoice form supporting LDA Journal (this is for type of LDA Journal)
	 * 		NOTE: This is not used presently as it is only valid for single relationship to journal
	 * @param record	Selected record in Journal Approval Group
	 */
	public VLayout buildInvoiceForm(ListGridRecord record) {
		
		DataSource dataSource = getInvoiceDataSource(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
		
		Label invoiceLabel = new Label();
		invoiceLabel.setHeight(10);
		invoiceLabel.setAlign(Alignment.LEFT);
		invoiceLabel.setContents("<font size=\"4\"><b>Invoice Information:</b></font>");
		
		DynamicForm invoiceForm = new DynamicForm();
		invoiceForm.setDataSource(dataSource);  
		invoiceForm.setUseAllDataSourceFields(true);
		
		invoiceForm.setFields(Util.getReadOnlyFormItemFromDataSource(dataSource));
		invoiceForm.fetchData();
		
		Util.formatFormItemAsCurrency(invoiceForm.getField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT));
		Util.formatFormItemAsCurrency(invoiceForm.getField(DataNameTokens.FINAPINVOICE_INVOICEARAMOUNT));
		
		VLayout invoiceVLayout = new VLayout();
		
		invoiceVLayout.setPadding(5);
		invoiceVLayout.setMembers(invoiceLabel, invoiceForm);
		
		return invoiceVLayout;
		
	}


	/**
	 * Returns data source for Funds-In Records used for Cash Receive Journal details
	 * @param journalGroupId	the selected Journal Approval Group Id
	 * @return					Data source for Funds-In Records
	 */
	private DataSource getFundInReconciliationDataSource(String journalGroupId) {
		RafDataSource fundInReconciliationData = FinanceData.getFundInReconciliationData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
		fundInReconciliationData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return fundInReconciliationData;
	}
	
	/**
	 * Returns data source for Sales Records used for Sales Journal details
	 * @param journalGroupId	the selected Journal Approval Group Id
	 * @return					Data source for Sales Records
	 */
	private DataSource getSalesRecordDataSource(String journalGroupId, String taskId) {
		RafDataSource salesRecordData = FinanceData.getSalesRecordData();
		HashMap<String, String> params = new HashMap<String, String>();
		if(journalGroupId!=null){
			params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
		}
		if(taskId!=null){
			params.put(DataNameTokens.TASKID, taskId);
		}
		salesRecordData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return salesRecordData;
	}

	/**
	 * Returns data source for Payment used for Payment Journal details
	 * @param journalGroupId	the selected Journal Approval Group Id
	 * @return					Data source for Payment
	 */
	private DataSource getPaymentDataSource(String journalGroupId) {
		RafDataSource paymentData = FinanceData.getJournalRelatedPaymentData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
		paymentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return paymentData;
	}
	
	/**
	 * Returns data source for Invoice used for LDA Journal details
	 * @param journalGroupId	the selected Journal Approval Group Id
	 * @return					Data source for Invoice
	 */
	private DataSource getInvoiceDataSource(String journalGroupId) {
		RafDataSource logisticsPaymentDetailData = FinanceData.getJournalRelatedLogisticsInvoiceData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
		logisticsPaymentDetailData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return logisticsPaymentDetailData;
	}

}
