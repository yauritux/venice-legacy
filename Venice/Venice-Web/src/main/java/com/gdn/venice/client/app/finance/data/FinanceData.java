package com.gdn.venice.client.app.finance.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter;
import com.gdn.venice.client.app.finance.presenter.JournalPresenter;
import com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter;
import com.gdn.venice.client.app.finance.presenter.SalesRecordPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;

public class FinanceData {
	
	private static DataSource dashboardPaymentDs = null;
	private static DataSource dashboardReconciliationDs = null;
	
	/**
	 * Returns the fund in reconciliation data
	 * @return
	 */
	public static RafDataSource getFundInReconciliationData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID, "Order ID"),
				new DataSourceDateField(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE, "Order Date"),
				new DataSourceDateTimeField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE, "Recon Date"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID, "Bank"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "Bank Name"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, "Report"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID, "Order Payment ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),			
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID, "Report ID"),	
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID, "Report ID"),					
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC, "Time"),	
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID, "Payment Type"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, "Payment Type Desc"),							
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID, "Payment Report ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC, "Payment Report"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF, "Nomor Reff"),
				//new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT, "Payment Amount"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT, "Payment Amount"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT, "Paid Amount"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT, "Refund Amount"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT, "Balance"),
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINRECONRECORD_AGING, "Aging"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_STATUSORDER, "Status Order"),				
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, "Status"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID, "Approval Status ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, "Recon. Status"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID, "Action ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC, "Action Status"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT, "Bank Fee"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENTHISTORY, " "),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FETCH,"Fetch")
				
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchFundInReconciliationData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=updateFundInReconciliationData&type=DataSource",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet  + "?method=deleteFundInReconciliationData&type=DataSource",
				dataSourceFields); 
		
		dataSource.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setValueMap(
				DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_NEW,
				DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_SUBMITTED,
				DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_APPROVED,
				DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_REJECTED);
		
		dataSource.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setValueMap(
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_ALLFUNDSRECEIVED,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PARTIALFUNDSRECEIVED,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_OVERPAIDFUNDSRECEIVED,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTTIMEOUT,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_REFUNDED,
				DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED);
		
		dataSource.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).setValueMap(
				DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_RECONCILED,
				DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_UNRECONCILED);
		
		dataSource.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC).setValueMap(
				DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_NONE,
				DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_CUSTOMER,
				DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_REFUNDED_BANK,
				DataConstantNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIED_ALLOCATED);
		
		return dataSource;
		
	}
	
	/**
	 * Returns the allocate to order data
	 * @return the allocate to order data
	 */
	public static DataSource getAllocateToOrderData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENORDER_ORDERID, "ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Order Status"),
				new DataSourceDateField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceIntegerField(DataNameTokens.VENORDER_FULFILLMENTSTATUS, "Fulfillment Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchFundInAllocateToOrderData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
		
	}
	
	/**
	 * Returns the allocate to order payment data
	 * @param orderId the order id to use
	 * @return the allocate to order payment data
	 */
	public static DataSource getAllocateToOrderPaymentData(String orderId, String allocationAmount) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENORDERPAYMENT_ORDERPAYMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),
				new DataSourceFloatField(DataNameTokens.VENORDERPAYMENT_AMOUNT, "Amount"),
				new DataSourceFloatField("AllocationAmount", "Allocation Amount"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, "Payment Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, "Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchFundInAllocateToOrderPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, orderId);
		params.put("AllocationAmount", allocationAmount);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
		
	}
	
	/**
	 * Returns the funds in reconcuiliation record comment history data
	 * @param reconciliationRecordId
	 * @return the funds in reconcuiliation record comment history data
	 */
	public static RafDataSource getFundInReconRecordCommentHistoryData(String reconciliationRecordId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceDateTimeField(DataNameTokens.FINARFUNDSINRECONCOMMENT_ID_COMMENTTIMESTAMP, "Date"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONCOMMENT_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONCOMMENT_USERLOGONNAME, "User")
		};
		
		RafDataSource retVal =  new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchFundInReconRecordCommentHistory&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, reconciliationRecordId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	/**
	 * Returns the sales record data
	 * @return the sales record data
	 */
	public static RafDataSource getSalesRecordData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINSALESRECORD_SALESRECORDID, "ID"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_RECONCILEDATE, "Reconcile Date"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_ORDERDATE, "Order Date"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_MCX_DATE, "CX Merchant Date"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_CXF_DATE, "CX Finance Date"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID, "Merchant ID"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant Name"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_COMMISIONTYPE, "Merchant Status"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_PAYMENTTYPECODE, "Payment Type"),			
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Item Desc."),
				new DataSourceIntegerField(DataNameTokens.FINSALESRECORD_VENORDERITEM_QUANTITY, "Quantity"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_PRICE, "Price Per Unit"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_TOTAL, "Order Item Amount"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_GDNCOMMISIONAMOUNT, "Commission"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_GDNTRANSACTIONFEEAMOUNT, "Transaction Fee"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_PROVIDER_CODE, "Logistic Vendor"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST, "Shipping Cost"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_INSURANCECOST, "Insurance Cost"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_SHIPPINGCOST_INSURANCECOST, "Total Shipping & Insurance Cost"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_GDNHANDLINGFEEAMOUNT, "Handling Fee"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_AMOUNT, "Adjustment"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_GDNGIFTWRAPCHARGEAMOUNT, "Gift Wrap"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_CUSTOMERDOWNPAYMENT, "Customer Down Payment"),		
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status"),				
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG, "PPh 23"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, "PPh 23 Amount"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENORDERITEMADJUSTMENT_DETAIL, "Promo"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_PAYMENT_STATUS, "Payment Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + SalesRecordPresenter.salesRecordServlet + "?method=fetchSalesRecordData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
		
	}
	
	/**
	 * Returns the adjustment data
	 * @return the adjustment data
	 */
	public static RafDataSource getSalesRecordDetailAdjustmentData(String wcsOrderItemId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE, "Promotion Name"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_PROMOTIONVOUCHERCODE, "Promotion Voucher Code"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_VENORDERITEM_ORDERITEMID, "Order Item ID"),				
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, "Amount"), 				
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_ADMINDESC, "Admin Desc.")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + SalesRecordPresenter.salesRecordServlet + "?method=fetchSalesRecordDetailAdjustmentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, wcsOrderItemId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;
				
	}
	
	/**
	 * Returns the journal data
	 * @return the journal data
	 */
	public static RafDataSource getJournalData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, "ID"),
				new DataSourceIntegerField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID, "Journal Type ID"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC, "Journal Type"),
				new DataSourceDateField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP, "Journal Date"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, "Journal Description"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchJournalData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	/**
	 * Returns the journal detail data
	 * @param journalGroupId the journal group id to use
	 * @return the journal detail data
	 */
	public static DataSource getJournalDetailData(String journalGroupId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONID, "ID"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, "Group Account"),
				new DataSourceDateField(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONTIMESTAMP, "Date"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_REFF, "Reff"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_PAYMENT_TYPE, "Payment Type"), 
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTDESC, "Account"),
				new DataSourceFloatField(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "Debit"),
				new DataSourceFloatField(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "Credit"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, "Status"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_COMMENTS, "Comments"),
				new DataSourceTextField(DataNameTokens.FINJOURNALTRANSACTION_GROUP_JOURNAL, "Group Desc")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchJournalDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		
		return dataSource;
	}
	
	/**
	 * Returns the manual journal header data
	 * @return the manual journal header data
	 */
	public static DataSource getManualJournalData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, "ID"),
				new DataSourceIntegerField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID, "Journal Type ID"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC, "Journal Type"),
				new DataSourceDateField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP, "Journal Date"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, "Journal Description"),
				new DataSourceTextField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchManualJournalData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	/**
	 * Returns the manual journal detail data
	 * @param journalGroupId is the journal group id to use
	 * @return the manual journal detail data
	 */
	public static DataSource getManualJournalDetailData(String journalGroupId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC, "Account No."),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID, "Party"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID, "Party"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME, "Party"),
				new DataSourceFloatField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "Debit"),
				new DataSourceFloatField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "Credit"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, "Status"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS, "Comments")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchManualJournalDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		if (journalGroupId!=null) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, journalGroupId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		
		return dataSource;
	}
	
	/**
	 * Returns the manual journal order data
	 * @return the manual journal order data
	 */
	public static DataSource getManualJournalOrderData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENORDER_ORDERID, "ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Order Status"),
				new DataSourceDateField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceIntegerField(DataNameTokens.VENORDER_FULFILLMENTSTATUS, "Fulfillment Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchManualJournalOrderData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}	
	
	/**
	 * Returns the manual journal party data
	 * @return the manual journal party data
	 */
	public static DataSource getManualJournalPartyData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENPARTY_PARTYID, "ID"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Party Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method=fetchManualJournalPartyData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	/**
	 * Returns the logistics payment detail data
	 * @return the logistics payment detail data
	 */
	public static RafDataSource getLogisticsPaymentDetailData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPINVOICE_APINVOICEID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, "Logistics ID"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME, "Logistics Name"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER, "Invoice ID"),
				new DataSourceDateField(DataNameTokens.FINAPINVOICE_INVOICEDATE, "A/P Date"),
				new DataSourceFloatField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT, "A/P Amount")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchLogisticsPaymentProcessingData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	/**
	 * A data source that returns the journal related invoice detail data
	 * @return the journal related invoice details
	 */
	public static RafDataSource getJournalRelatedLogisticsInvoiceData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPINVOICE_APINVOICEID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME, "Logistics"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER, "Invoice No"),
				new DataSourceDateField(DataNameTokens.FINAPINVOICE_INVOICEDATE, "A/P Date"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT, "A/P Amount"),
				new DataSourceTextField(DataNameTokens.FINAPINVOICE_INVOICEARAMOUNT, "A/R Amount")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchJournalRelatedLogisticsInvoiceData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}

	/**
	 * Returns the logistics payment data
	 * @param invoiceIdXml the invoice id
	 * @return the logistics payment data
	 */
	public static DataSource getLogisticsPaymentData(String invoiceIdXml) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPPAYMENT_APPAYMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "Logistics Name"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "A/R Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_BALANCE, "Balance"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC, "Bank Account"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, "FinApManualJournalTransactions"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPINVOICES, "FinApInvoices")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchLogisticsPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINAPINVOICE_APINVOICEID, invoiceIdXml);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
	}
	
	/**
	 * Returns the merchant payment detail data
	 * @return the merchant payment detail data
	 */
	public static DataSource getMerchantPaymentDetailData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINSALESRECORD_SALESRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_WCSMERCHANTID, "Merchant ID"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant Name"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_MCX_DATE, "CX Merchant Date"),
				new DataSourceDateField(DataNameTokens.FINSALESRECORD_CXF_DATE, "CX Finance Date"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_MERCHANTPAYMENTAMOUNT, "A/P Amount"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_VENORDERITEM_VENSETTLEMENTRECORDS_PPH23_FLAG, "PPh 23"),
				new DataSourceTextField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, "PPh 23 Amount")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchMerchantPaymentProcessingData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		
		return dataSource;
		
	}
	
	/**
	 * Returns the merchant payment data
	 * @param salesRecordIdXml the sales record id to use
	 * @return the merchant payment data
	 */
	public static DataSource getMerchantPaymentData(String salesRecordIdXml) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPPAYMENT_APPAYMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "Merchant Name"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amount"),
				new DataSourceFloatField(DataNameTokens.FINSALESRECORD_PPH23_AMOUNT, "PPh 23"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "A/R Amount"),			
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_BALANCE, "Balance"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID, "Bank Account"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, "FinApManualJournalTransactions"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS, "FinSalesRecords"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchMerchantPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINSALESRECORD_SALESRECORDID, salesRecordIdXml);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
	}
	
	/**
	 * returns the refund payment detail data
	 * @return the detail data
	 */
	public static DataSource getRefundPaymentDetailData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_WCSORDERID, "Order ID"),
				new DataSourceDateField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_ORDERDATE, "Order Date"),
				new DataSourceDateField(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, "Refund Date"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_VENORDER_VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Customer Name"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_ACTION_TAKEN, "Action Taken"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, "A/P Amount"),
				new DataSourceFloatField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_BANKFEE, "Bank Fee"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_REASON, "Reason")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchRefundPaymentProcessingData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource; 
	}
	
	/**
	 * Returns the refund payment data
	 * @param refundRecordIdXml the refund record id
	 * @return the refund payment data
	 */
	public static DataSource getRefundPaymentData(String refundRecordIdXml) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPPAYMENT_APPAYMENTID, "ID"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "A/R Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_BALANCE, "Balance"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTID, "Bank Account"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, "FinApManualJournalTransactions"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINARFUNDSINREFUNDS, "FinArFundsInRefunds")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchRefundPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, refundRecordIdXml);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
	}
	
	/**
	 * Gets the manual transaction payment data based on the sales record ids and invoice ids
	 * @param salesRecordIds the sales record ids to use
	 * @param invoiceIds the invoice ids to use
	 * @return the manaul transaction payment data
	 */
	public static DataSource getFinApManualTransactionPaymentData(String salesRecordIds, String invoiceIds) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, "ID"),
				new DataSourceFloatField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT, "A/R Amount"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME, "Party Name"),
				new DataSourceTextField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS, "Comments"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchManualJournalTransactionPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (salesRecordIds!=null) {
			params.put(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS, salesRecordIds);
		}
		if (invoiceIds!=null) {
			params.put(DataNameTokens.FINAPPAYMENT_FINAPINVOICES, invoiceIds);
		}
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
	}
	
	/**
	 * Getsw the payment data for the payment list grid
	 * @return the payment data
	 */
	public static RafDataSource getPaymentData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPPAYMENT_APPAYMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC, "Bank Account"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "Party Name"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "A/R Amount"),
				new DataSourceFloatField(DataNameTokens.FINAPPAYMENT_PPH23_AMOUNT, "Pph 23 Amount"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		return dataSource;
	}

	/**
	 * Gets the payment data related to the journal for the journal view
	 * @return
	 */
	public static RafDataSource getJournalRelatedPaymentData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINAPPAYMENT_APPAYMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC, "Bank Account"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "Party Name"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amount"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "A/R Amount"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method=fetchPaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		return dataSource;
	}

	/**
	 * Returns the dashboard data for payments
	 * @return the dashboard data for payments
	 */
	public static DataSource getDashboardPaymentData() {
		if (dashboardPaymentDs != null) {
			return dashboardPaymentDs;
		} else {
			dashboardPaymentDs = new DataSource();
			
			
			dashboardPaymentDs.setRecordXPath("/finance/dashboardpayment/payment");
			
			DataSourceTextField entityNameField = new DataSourceTextField("entityname", "Entitiy");
			entityNameField.setPrimaryKey(true);
			DataSourceTextField apAmountField = new DataSourceTextField("apamount", "A/P Amount");
			DataSourceTextField typeField = new DataSourceTextField("type", "Type");
	
			dashboardPaymentDs.setFields(entityNameField, apAmountField, typeField);
			dashboardPaymentDs.setDataURL("ds/test_data/finance.data.xml");
			dashboardPaymentDs.setClientOnly(true);
			
	        return dashboardPaymentDs;
		}   
	}
	
	/**
	 * Returns the allocate to order data
	 * @return the allocate to order data
	 */
	public static DataSource getAllocateData(String idAllocation) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD, "ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_DEST_ORDER, "To Order")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchAllocateData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (idAllocation!=null) {
			params.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE, idAllocation);
		}else{
			params.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE, null);
		}
			
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
		
	}
	
	/**
	 * Returns the allocate to order data
	 * @return the allocate to order data
	 */
	public static DataSource getRefundData(String idRefund) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, "Refund Amount"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "Id Fund In"),				
				new DataSourceDateField(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, "Refund Date")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchRefundData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (idRefund!=null) {
			params.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, idRefund);
		}else{
			params.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, null);
		}
			
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
		
	}
	
	/**
	 * Returns the Action Taken History
	 * @return the Action Taken History
	 */
	public static DataSource getActionTakenHistoryData(String idrecon) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_ID, "ID"),
				new DataSourceDateField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_DATE, "Action Date"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_DESC, "Action Taken"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_NOMORREFF, "Reff"),	
				new DataSourceTextField(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_AMOUNT, "Amount")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method=fetchActionTakenHistoryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (idrecon!=null) {
			params.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, idrecon);
		}else{
			params.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, null);
		}
			
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
		
	}
	
	public static DataSource getDashboardReconciliationData() {
		if (dashboardReconciliationDs != null) {
			return dashboardReconciliationDs;
		} else {
			dashboardReconciliationDs = new DataSource();
			
			dashboardReconciliationDs.setRecordXPath("/finance/dashboardreconciliation/reconciliationitem");
			DataSourceTextField orderIdField = new DataSourceTextField("orderid", "Order ID");
			
	        DataSourceTextField paymentAmountField = new DataSourceTextField("paymentamount", "Payment Amount");
	        
	        DataSourceTextField paidAmountField = new DataSourceTextField("paidamount", "Paid Amount");
	        
	        DataSourceTextField balanceField = new DataSourceTextField("balance", "Balance");
	        
	        DataSourceTextField statusField = new DataSourceTextField("status", "Status");
	        
	        
	        dashboardReconciliationDs.setFields(orderIdField,  paymentAmountField, paidAmountField, balanceField, statusField);
	        dashboardReconciliationDs.setDataURL("ds/test_data/finance.data.xml");
	        

	        dashboardReconciliationDs.setClientOnly(true);
	        
	        return dashboardReconciliationDs;
		}    
		
	}
	

}
