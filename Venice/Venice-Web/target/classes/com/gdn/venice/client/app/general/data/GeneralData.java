package com.gdn.venice.client.app.general.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.presenter.OrderDataViewerPresenter;
import com.gdn.venice.client.app.general.presenter.ReturDataViewerPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;

public class GeneralData {
	
	
	public static RafDataSource getOrderData(int firstResult) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, "Customer Username"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSID, "Order Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_FPDATE, "FP Date"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_BLOCKEDFLAG, "Blocked Flag")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		return new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderData&type=DataSource&firstResult="+firstResult,
				null,
				null,
				null,
				dataSourceFields); 
		
	}
	
	public static RafDataSource getOrderDetailData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Order&nbsp;Status"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_RMAFLAG, "RMA"),
				new DataSourceTextField(DataNameTokens.VENORDER_RMAACTION, "RMA&nbsp;Action"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, "Cust. Username"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT, "Order&nbsp;Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_DELIVEREDDATETIME, "Delivered&nbsp;Date/Time"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP&nbsp;Address"),
				new DataSourceTextField(DataNameTokens.VENORDER_FULFILLMENTSTATUS, "Fulfillment&nbsp;Status"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_BLOCKEDFLAG, "Blocked"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_BLOCKEDTIMESTAMP, "Blocked&nbsp;Date/Time"),
				new DataSourceTextField(DataNameTokens.VENORDER_BLOCKEDREASON, "Blocked&nbsp;Reason"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_VENFRAUDCHECKSTATUS_FRAUDCHECKSTATUSDESC, "Fraud&nbsp;Checked"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_FINANCERECONCILEFLAG, "Finance&nbsp;Reconciled")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getOrderItemData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_ORDERITEMID, "Ven. Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, "Order Item Status"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_QUANTITY, "Quantity"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_TOTAL, "Total"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getOrderCustomerData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERID, "Ven. Cust. ID"),				
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, "Cust. Username"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_USERTYPE, "Cust. Type"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_WCSCUSTOMERID, "Customer ID"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYFIRSTNAME, "First Name"),
				new DataSourceBooleanField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, "First Time"),			
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYLASTNAME, "Last Name"),
				new DataSourceDateField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH, "Date of Birth"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Full/Legal Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderCustomerData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderCustomerAddressData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, "Ven. Address ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, "Street Address 1"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, "Street Address 2"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, "Kecamatan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, "Kelurahan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, "City"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, "Province"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, "Post Code"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "Country")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderCustomerAddressData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderCustomerContactData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, "ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Type"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, "Detail")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderCustomerContactData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderLogisticsAirwayBillData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, "Airway Bill ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, "Sequence"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILL_TRACKINGNUMBER, "Tracking No."),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, "Pick Up Date/Time"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILL_RECEIVED, "Delivery Date/Time"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderLogisticsAirwayBillData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderFinancePaymentData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, "Ven. Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "Bank"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE, "Method"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "Payment Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID, "VA No"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_WCSORDERID, "Old Order Id"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "Old Payment Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER, "Account"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID, "Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, "Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderFinancePaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderFinanceReconciliationData(String paymentId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, "Reconciliation ID"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, "Report"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, "Result")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderFinanceReconciliationData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		if (paymentId!=null) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, paymentId);
			retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		
		return retVal;
	}
	
	public static RafDataSource getOrderHistoryOrderData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERSTATUSHISTORY_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERSTATUSHISTORY_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDERSTATUSHISTORY_TIMESTAMP, "Date/Time"),
				new DataSourceTextField(DataNameTokens.VENORDERSTATUSHISTORY_STATUSCHANGEREASON, "Notes")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderHistoryOrderData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderHistoryOrderItemData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID, "Ven. Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP, "Timestamp"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON, "Notes")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderHistoryOrderItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getOrderHistoryOrderItemDataByOrderItemId(String orderItemId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID, "Ven. Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID, "Order Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP, "Timestamp"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON, "Notes")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + OrderDataViewerPresenter.orderDataViewerServlet + "?method=fetchOrderItemHistoryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENORDERITEM_ORDERITEMID, orderItemId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getReturData(int firstResult) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENRETUR_RETURID, "Ven. Retur ID"),
				new DataSourceTextField(DataNameTokens.VENRETUR_WCSRETURID, "Retur ID"),
				new DataSourceDateTimeField(DataNameTokens.VENRETUR_RETURDATE, "Retur Date"),
				new DataSourceTextField(DataNameTokens.VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME, "Customer Username"),
				new DataSourceTextField(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSID, "Retur Status"),				
				new DataSourceTextField(DataNameTokens.VENRETUR_RMAACTION, "Retur Action")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		return new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturData&type=DataSource&firstResult="+firstResult,
				null,
				null,
				null,
				dataSourceFields); 
		
	}
	
	public static RafDataSource getReturDetailData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENRETUR_RETURID, "Ven. Retur ID"),
				new DataSourceTextField(DataNameTokens.VENRETUR_WCSRETURID, "Retur ID"),
				new DataSourceDateTimeField(DataNameTokens.VENRETUR_RETURDATE, "Retur Date"),
				new DataSourceTextField(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSCODE, "Retur&nbsp;Status"),
				new DataSourceTextField(DataNameTokens.VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME, "Cust. Username"),				
				new DataSourceTextField(DataNameTokens.VENRETUR_RMAACTION, "Retur&nbsp;Action"),
				new DataSourceTextField(DataNameTokens.VENRETUR_AMOUNT, "Retur Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENRETUR_DELIVEREDDATETIME, "Delivered&nbsp;Date/Time")
				
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getReturItemData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENRETURITEM_RETURITEMID, "Ven. Retur Item ID"),
				new DataSourceTextField(DataNameTokens.VENRETURITEM_WCSRETURITEMID, "Retur Item ID"),
				new DataSourceTextField(DataNameTokens.VENRETURITEM_VENRETURSTATUS_ORDERSTATUSCODE, "Retur Item Status"),
				new DataSourceTextField(DataNameTokens.VENRETURITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product"),
				new DataSourceTextField(DataNameTokens.VENRETURITEM_QUANTITY, "Quantity"),
				new DataSourceTextField(DataNameTokens.VENRETURITEM_TOTAL, "Total"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getReturCustomerData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERID, "Ven. Cust. ID"),				
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, "Cust. Username"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_USERTYPE, "Cust. Type"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_WCSCUSTOMERID, "Customer ID"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYFIRSTNAME, "First Name"),
				new DataSourceBooleanField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, "First Time"),			
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYLASTNAME, "Last Name"),
				new DataSourceDateField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH, "Date of Birth"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Full/Legal Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturCustomerData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getReturCustomerAddressData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, "Ven. Address ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, "Street Address 1"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, "Street Address 2"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, "Kecamatan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, "Kelurahan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, "City"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, "Province"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, "Post Code"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "Country")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturCustomerAddressData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getReturCustomerContactData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, "ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Type"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, "Detail")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturCustomerContactData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getReturLogisticsAirwayBillData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLID, "Airway Bill ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_VENRETUR_WCSRETURID, "Retur ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_WCSRETURITEMID, "Retur Item ID"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILLRETUR_GDNREFERENCE, "Sequence"),
				new DataSourceTextField(DataNameTokens.LOGAIRWAYBILLRETUR_TRACKINGNUMBER, "Tracking No."),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLPICKUPDATETIME, "Pick Up Date/Time"),
				new DataSourceDateTimeField(DataNameTokens.LOGAIRWAYBILLRETUR_RECEIVED, "Delivery Date/Time"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturLogisticsAirwayBillData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		if (returId!=null) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(DataNameTokens.VENRETUR_RETURID, returId);
			retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		
		return retVal;
	}
	
	public static RafDataSource getReturHistoryReturData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENRETURSTATUSHISTORY_RETURID, "Ven. Retur ID"),
				new DataSourceTextField(DataNameTokens.VENRETURSTATUSHISTORY_WCSRETURID, "Retur ID"),
				new DataSourceTextField(DataNameTokens.VENRETURSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENRETURSTATUSHISTORY_TIMESTAMP, "Date/Time"),
				new DataSourceTextField(DataNameTokens.VENRETURSTATUSHISTORY_STATUSCHANGEREASON, "Notes")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturHistoryReturData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getReturHistoryReturItemData(String returId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENRETURITEMSTATUSHISTORY_RETURITEMID, "Ven. Retur Item ID"),
				new DataSourceTextField(DataNameTokens.VENRETURITEMSTATUSHISTORY_WCSRETURITEMID, "Retur Item ID"),
				new DataSourceTextField(DataNameTokens.VENRETURITEMSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENRETURITEMSTATUSHISTORY_TIMESTAMP, "Timestamp"),				
				new DataSourceTextField(DataNameTokens.VENRETURITEMSTATUSHISTORY_STATUSCHANGEREASON, "Notes")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + ReturDataViewerPresenter.returDataViewerServlet + "?method=fetchReturHistoryReturItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.VENRETUR_RETURID, returId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
}
