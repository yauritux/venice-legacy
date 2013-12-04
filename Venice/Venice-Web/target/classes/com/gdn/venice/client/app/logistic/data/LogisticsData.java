package com.gdn.venice.client.app.logistic.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.presenter.ActivityReportReconciliationPresenter;
import com.gdn.venice.client.app.logistic.presenter.DeliveryStatusTrackingPresenter;
import com.gdn.venice.client.app.logistic.presenter.InventoryPresenter;
import com.gdn.venice.client.app.logistic.presenter.InvoiceReconciliationPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
public class LogisticsData {

	private static DataSource providerManagementDs = null;
	
	public static DataSource getProviderData() {
		if (providerManagementDs != null) {
			return providerManagementDs;
		} else {
			providerManagementDs = new DataSource();
			
			providerManagementDs.setRecordXPath("/logistics/providers/provider");
	        DataSourceIntegerField idField = new DataSourceIntegerField("id");
	        idField.setPrimaryKey(true);
	
	        DataSourceTextField nameField = new DataSourceTextField("name", "Name");
	        nameField.setRequired(true);
	        
	        
	        providerManagementDs.setFields(idField, nameField);
	        providerManagementDs.setDataURL("ds/test_data/logistics.data.xml");
	        providerManagementDs.setClientOnly(true);
	        
	        return providerManagementDs;
		}    
	}
	
	public static RafDataSource getDeliveryStatusTrackingDetailData(String airwayBillId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, "GDN Ref No."),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT, "Weight"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_DESTINATION, "Destination"),				
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC, "Service"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, "Airwaybill No."),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, "AWB Timestamp"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_STATUS, "Delivery Status"),
				new DataSourceDateField(DataNameTokens.LOGAIRWAYBILL_RECEIVED, "Delivered Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_RECIPIENT, "Receiver Name"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_RELATION, "Relation"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, "SKU ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product Name"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_QUANTITY, "Quantity"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT, "Insured Amount"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME, "Recipient Name"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENPARTYADDRESS_VENADDRESS, "Address"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_PHONE, "Phone"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_MOBILE, "Mobile"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Sender Name"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENPARTYADDRESS_VENADDRESS, "Address"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_PHONE, "Phone"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_MOBILE, "Mobile"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_EMAIL, "Email"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE, "Pickup Date"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_ACTUALPICKUPDATE, "Actual Pickup Date"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE, "Min ETD Date"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE, "Max ETD Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTWRAP, "Gift Wrap"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTCARD, "Gift Card"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTNOTE, "Gift Note"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_SPECIALHANDLING, "Special Handling Instruction"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTCOURIER, "Merchant/3PL Name"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTDELIVEREDDATESTARTEND, "Delivered Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALATIONDATESTARTEND, "Instalation Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLOFFICER, "Install Officer"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLMOBILE, "Install Officer Mobile"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLNOTE, "Installation Note"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE, "Total Charge"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_SHIPPINGCHARGE, "Shipping Charge"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE, "Insurance Charge"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU, "Other Charge (Packaging Kayu)"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE, "Gift Wrap Charge"),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS, "Pickup Address"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENPARTYADDRESS_VENADDRESS, "Merchant Address"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENCONTACTDETAIL_PHONE, "Phone"),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPIC, "PIC Name"),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPICPHONE, "PIC Phone"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSDESC, "Latest Venice Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP, "Timestamp")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + DeliveryStatusTrackingPresenter.deliveryStatusTrackingServlet + "?method=fetchDeliveryStatusTrackingDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBillId);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
				
		return dataSource;
	}
	
	public static RafDataSource getDeliveryStatusTrackingData(String taskId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_DETAIL, "Detail"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID, "Ven Order Item ID"),				
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, "GDN Ref No."),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT, "Weight"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_DESTINATION, "Destination"),				
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_SERVICE, "Service"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC, "Service"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, "Airwaybill No."),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, "AWB Timestamp"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_STATUS, "Delivery Status"),
				new DataSourceDateField(DataNameTokens.LOGAIRWAYBILL_RECEIVED, "Received"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_RECIPIENT, "Reciever"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_RELATION, "Relation")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + DeliveryStatusTrackingPresenter.deliveryStatusTrackingServlet + "?method=fetchDeliveryStatusTrackingData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + DeliveryStatusTrackingPresenter.deliveryStatusTrackingServlet + "?method=updateDeliveryStatusTrackingData&type=DataSource",
				null,
				dataSourceFields); 
				
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return dataSource;
	}
	
	public static RafDataSource getActivityReportCommentHistoryData(String activityReconRecordId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceDateTimeField(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, "Date"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_USERLOGONNAME, "User")
		};
		
		RafDataSource retVal =  new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method=fetchActivityCommentHistory&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, activityReconRecordId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getInvoiceCommentHistoryData(String invoiceReconRecordId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceDateTimeField(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, "Date"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_USERLOGONNAME, "User")
		};
		
		RafDataSource retVal =  new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchInvoiceCommentHistory&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID, invoiceReconRecordId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}

	public static RafDataSource getActivityReportAirwayBillsData(int startRow, int totalRow) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, "GDN Ref No."),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS, "Recon Status"),				
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME, "Origin Merchant"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, "Logistic Provider"),
				new DataSourceDateField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, "Pick Up Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_DESTINATION, "Destination City"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_ZIP, "Destination Zip Code"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, "Airway Bill No"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, "AWB Timestamp"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID, "Approved/Submitted By"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC, "Report File"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method=fetchAirwayBillData&type=DataSource&startRow="+startRow+"&totalRow="+totalRow,
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;

	}
	
	public static RafDataSource getFileUploadLogData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADLOGID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAME, "File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC, "File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_ACTUALFILEUPLOADNAME, "Actual File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADFORMAT, "Provider"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC, "Fail Data"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_UPLOADSTATUS, "Upload Status"),
				new DataSourceDateTimeField(DataNameTokens.LOGFILEUPLOADLOG_TIMESTAMP, "Upload Time"),
				new DataSourceTextField(DataNameTokens.LOGFILEUPLOADLOG_USERNAME, "Username")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method=fetchFileUploadLogData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;

	}
	
	public static RafDataSource getInvoiceUploadLogData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICEUPLOADLOGID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICENUMBER, "Invoice Number"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAME, "File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC, "File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_ACTUALFILEUPLOADNAME, "Actual File Upload Name"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADFORMAT, "Provider"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAME, "Fail Data"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC, "Fail Data"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADSTATUS, "Upload Status"),
				new DataSourceDateTimeField(DataNameTokens.LOGINVOICEUPLOADLOG_TIMESTAMP, "Upload Time"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADEDBY, "Uploaded By"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchInvoiceUploadLogData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	public static RafDataSource getInvoiceReportUploadData(String taskId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER, "Invoice Number"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, "Logistic Provider"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION, "Report File"),
				new DataSourceDateTimeField(DataNameTokens.LOGINVOICEREPORTUPLOAD_REPORTRECONCILIATIONTIMESTAMP, "Reconciliation Time"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_USERLOGONNAME, "Uploaded By"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICERECONTOLERANCE, "Reconciliation Tolerance"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEREPORTUPLOAD_APPROVEDBY, "Submitted/Approved By"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchInvoiceReportUploadData&type=DataSource&taskId="+taskId,
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	public static RafDataSource getInvoiceAirwaybillRecordData(String reportUploadId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER, "Airwaybill Number"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS, "Recon Status"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC, "Approval Status"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT, "Venice Total Weight"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPACKAGEWEIGHT, "Logistic Total Weight"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG, "Venice Price per Kg"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG, "Logistic Price per Kg"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE, "Venice Total Insurance Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE, "Logistic Total Insurance Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE, "Venice Total Other Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE, "Logistic Total Other Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE, "Venice Total Gift Wrap Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE, "Logistic Total Gift Wrap Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE, "Venice Total Shipping Charge"),
				new DataSourceTextField(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE, "Logistic Total Shipping Charge")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchInvoiceAirwaybillRecordData&type=DataSource&reportUploadId="+reportUploadId,
				null,
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=updateInvoiceAirwaybillRecordData&type=DataSource",
				null,
				dataSourceFields); 
		
		return dataSource;
	}
	
	public static RafDataSource getActivityReportsReconciliationProblemData(String airwayBillId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID, "Failure"), 
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC, "Failure"), 
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_VENICEDATA, "MTA Data"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_PROVIDERDATA, "Logistic Data"), 
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA, "Manual Data"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_USERLOGONNAME, "PIC"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, "Action Applied"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, "Action Applied"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENTHISTORY, " ")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method=fetchAirwayBillReconciliationProblemData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method=updateAirwayBillReconciliationProblemData&type=DataSource",
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBillId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;
				
	}
	
	public static RafDataSource getInvoiceAirwayBillsData(String invoiceAirwaybillRecordId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, "GDN Ref No."),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_WEIGHT, "Weight"),	
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU, "Packaging Charge"),	
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE, "Gift Wrap Charge"),	
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT, "Insured Amount"),	
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE, "Insurance Charge"),	
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE, "Total Charge"),	
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, "AWB Timestamp"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME, "Origin Merchant"),
				new DataSourceDateField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, "Pick Up Date"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_DESTINATION, "Destination City"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_ZIP, "Destination ZIP Code")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchAirwayBillData&type=DataSource&invoiceAirwaybillRecordId="+invoiceAirwaybillRecordId,
				null,
				null,
				null,
				dataSourceFields); 
		
		System.out.println("dataSource.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID): "+dataSource.getAttributeAsString(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
		
		return dataSource;

	}
	
	public static RafDataSource getInvoiceReconciliationProblemData(String invoiceAirwaybillId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID, "Failure"), 
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC, "Failure"), 
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_VENICEDATA, "Venice Data"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_PROVIDERDATA, "Logistic Data"), 
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA, "Manual Data"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_USERLOGONNAME, "PIC"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, "Action Applied"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, "Action Applied"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, "Comment"),
				new DataSourceTextField(DataNameTokens.LOGINVOICERECONRECORD_COMMENTHISTORY, " ")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=fetchAirwayBillReconciliationProblemData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=updateAirwayBillReconciliationProblemData&type=DataSource",
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, invoiceAirwaybillId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
		
	}
	
	public static RafDataSource getMerchantPickUpInstructionData(String airwayBillId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICKUPDETAILSID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant Name"), 
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS1, "Pick Up Address"), 
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_STREETADDRESS2, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KECAMATAN, ""), 
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_KELURAHAN, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCITY_CITYNAME, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENSTATE_STATENAME, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_POSTALCODE, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_VENADDRESS_VENCOUNTRY_COUNTRYNAME, ""),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPIC, "PIC"),
				new DataSourceTextField(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTION_MERCHANTPICPHONE, "PIC Phone")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + DeliveryStatusTrackingPresenter.deliveryStatusTrackingServlet + "?method=fetchMerchantPickUpInstructionData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBillId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;		
	}
	
	public static RafDataSource getInventoryData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID, "ID"),
				new DataSourceTextField(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTSKU, "SKU"), 
				new DataSourceTextField(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product Name"), 
				new DataSourceTextField(DataNameTokens.VENMERCHANTPRODUCT_COSTOFGOODSSOLD, "Cost of Goods Sold")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + InventoryPresenter.inventoryServlet + "?method=fetchInventoryData&type=DataSource",
				null,
				null,
				GWT.getHostPageBaseURL() + InventoryPresenter.inventoryServlet + "?method=removeInventoryData&type=DataSource",
				dataSourceFields);
		
		return retVal;		
	}
}
