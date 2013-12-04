package com.gdn.venice.client.app.fraud.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.BlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.presenter.CustomerBlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudBinCreditLimitPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCaseManagementPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCustomerWhitelistPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudParameterRule31Presenter;
import com.gdn.venice.client.app.fraud.presenter.MigsMasterPresenter;
import com.gdn.venice.client.app.fraud.presenter.MigsUploadPresenter;
import com.gdn.venice.client.app.fraud.presenter.UncalculatedCreditCardOrderPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.FieldType;

public class FraudData {
	private static DataSource fraudCaseDataDs= null;
	private static DataSource blackListDataDs = null;
	private static DataSource customerBlackListDataDs = null;

	public static DataSource getFraudCaseData() {
		if (fraudCaseDataDs != null) {
			return fraudCaseDataDs;
		} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, "Order ID"),					
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),
					new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, FieldType.DATETIME, "Fraud Case Date"), 
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "Fraud Case Description"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, "Fraud Status"),
					new DataSourceTextField(DataNameTokens.TASKSTATUS, "Task Status"),
					new DataSourceBooleanField(DataNameTokens.FRDFRAUDSUSPICIONCASE_ENABLEMODIFYAFTERCOMPLETED, "Enable Modify"),
					new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE, FieldType.DATETIME, "Order Date"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, "Total Risk Score"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_LASTACTION, "Last Action")
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource fraudCaseDataDs = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseData&type=DataSource",
					null,
					null,
					null,
					dataSourceFields);		
	
			return fraudCaseDataDs;
		}    
	}
	
	public static DataSource getClaimedFraudCaseData() {
		if (fraudCaseDataDs != null) {
			return fraudCaseDataDs;
		} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, "Order ID"),
					new DataSourceTextField(DataNameTokens.TASKID, "Task ID"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),					
					new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, FieldType.DATETIME, "Fraud Case Date"), 
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "Fraud Case Description"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, "Fraud Status"),
					new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE, FieldType.DATETIME, "Order Date"),
					new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, "Total Risk Score"),
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource fraudCaseDataDs = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchClaimedFraudCaseData&type=DataSource",
					null,
					null,
					null,
					dataSourceFields);		
	
			return fraudCaseDataDs;
		}    
	}
	
	public static RafDataSource getFraudCaseRelatedData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),
				new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, FieldType.DATETIME, "Fraud Case Date"), 
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "Fraud Case Description"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, "Total Score"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, "Reason"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, "Notes"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, "Fraud Status")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseRelatedData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);		

		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseCustomerSummaryData(String orderId) {
		DataSourceField[] dataSourceFields = {								
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Name"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, "User Name"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseCustomerSummaryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCasePaymentDetailData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_PRODUCTPRICE, "Product Price"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_SHIPPINGCOST, "Shipping Cost"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_INSURANCECOST, "Insurance Cost"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, "Adjustment"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, "Total Handling Fee"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENT_TOTALTRANSACTION, "Total Transaction")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCasePaymentDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCasePaymentSummaryData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_PAYMENTADDRESSSIMILARITY, "At Least 1 Shipping & Billing Address Match"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_MULTIPLESHIPMENT, "Multiple Shipment Address")
				
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCasePaymentSummaryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseCustomerData(String caseId,String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_WCSCUSTOMERID, "Customer ID"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Customer Name"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, "User Name"),
				new DataSourceDateField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH, "Date of Birth"),
				new DataSourceBooleanField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, "Is First Time?")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseCustomerData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (caseId != null) {			
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		} else {
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "");
		}	
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
		
	public static RafDataSource getFraudCaseCustomerAddressData(String orderId) {		
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS, "Address")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseCustomerAddressData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseCustomerContactData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, "ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Contact Type"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, "Detail")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseCustomerContactData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseOrderDetailData(String caseId, String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order/RMA ID"),
				new DataSourceBooleanField(DataNameTokens.VENORDER_RMAFLAG, "Is RMA"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT, "Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address"),
				new DataSourceTextField(DataNameTokens.VENORDER_FULFILLMENTSTATUS, "Is Partial Fulfillment?"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "Is First Time?"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_PRODUCTPRICE, "Product Price")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseOrderDetailData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (caseId != null) {			
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		} else {
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "");
		}	
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getFraudCaseRelatedOrderData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Case ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, "Customer Name"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT, "Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "First Time?"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address")				
		};
		dataSourceFields[0].setPrimaryKey(true);
		dataSourceFields[1].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseRelatedOrderData&type=DataSource",
				null,
				null,
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=deleteFraudCaseRelatedOrderData&type=DataSource",
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getFraudCaseOrderItemData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_ORDERITEMID, "Ven. Order Item ID"),				
				new DataSourceTextField(DataNameTokens.VENORDERITEM_WCSORDERITEMID, "Item ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENORDER_ORDERID, "Order ID"),	
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, "Product Type"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product Name"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, "Product SKU"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_SUMMARY, "Product Summary"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_QUANTITY, "Qty"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_PRICE, "Price"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_TOTAL, "Total"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_SHIPPINGCOST, "Shipping"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_INSURANCECOST, "Insurance"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, "Adjustment"),
				new DataSourceTextField(DataNameTokens.VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE, "Promo Code"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS, "Recipient")
				
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseOrderItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseCategoryData(String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, "Product Category"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_TOTAL, "Total")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseCategoryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseRelatedOrderItemData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_ORDERITEMID, "Ven. Order Item ID"),				
				new DataSourceTextField(DataNameTokens.VENORDERITEM_WCSORDERITEMID, "ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, "Product Type"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product Name"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, "Product SKU"),
				new DataSourceTextField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, "Product Category"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_QUANTITY, "Quantity"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_PRICE, "Price"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_TOTAL, "Total Price"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS, "Recipient")
				
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseRelatedOrderItemData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCasePaymentData(String caseId,String orderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, "Ven. Order Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "Payment Type"),				
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO, "Other Information"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSCODE, "Status"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "Acquiring Bank"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK, "Issuing Bank"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION, "Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT, "Limit"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, "Handling Fee"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENADDRESS, "Billing Address")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseFinancePaymentData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (caseId != null) {			
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		} else {
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "");
		}	
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
		
	public static DataSource getBlackListData() {
		if (blackListDataDs != null) {
			return blackListDataDs;
		} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID, "ID"),
					new DataSourceTextField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING, "String"), 
					new DataSourceTextField(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST, "Black or White List"),
					new DataSourceTextField(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION, "Description"),
					new DataSourceTextField(DataNameTokens.FRDENTITYBLACKLIST_CREATEDBY, "Created By"),
					new DataSourceField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP, FieldType.DATETIME, "Timestamp")
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource blackListDataDs = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + BlackListMaintenancePresenter.blackListMaintenancePresenterServlet + "?method=fetchBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + BlackListMaintenancePresenter.blackListMaintenancePresenterServlet + "?method=addBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + BlackListMaintenancePresenter.blackListMaintenancePresenterServlet + "?method=updateBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + BlackListMaintenancePresenter.blackListMaintenancePresenterServlet + "?method=deleteBlackListData&type=DataSource",
					dataSourceFields);	
			
			return blackListDataDs;
		}    		
	}
	
	public static DataSource getCustomerBlackListData() {
		if (customerBlackListDataDs != null) {
			return customerBlackListDataDs;
		} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID, "ID"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME, "Customer Full Name"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS, "Address"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER, "Phone Number"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_HANDPHONENUMBER, "HandPhone Number"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL, "Email"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER, "Phone Number"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER, "HandPhone Number"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS, "Shipping Address"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_CCNUMBER, "Credit Card Number"),
					new DataSourceField(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP, FieldType.DATETIME, "Order Date"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION, "Description"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERBLACKLIST_CREATEDBY, "Created By"),
					new DataSourceField(DataNameTokens.FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP, FieldType.DATETIME, "Timestamp")
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource customerBlackListDataDs = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + CustomerBlackListMaintenancePresenter.customerBlackListMaintenancePresenterServlet + "?method=fetchCustomerBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + CustomerBlackListMaintenancePresenter.customerBlackListMaintenancePresenterServlet + "?method=addCustomerBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + CustomerBlackListMaintenancePresenter.customerBlackListMaintenancePresenterServlet + "?method=updateCustomerBlackListData&type=DataSource",
					GWT.getHostPageBaseURL() + CustomerBlackListMaintenancePresenter.customerBlackListMaintenancePresenterServlet + "?method=deleteCustomerBlackListData&type=DataSource",
					dataSourceFields);	
			
			return customerBlackListDataDs;
		}    		
	}
	
	public static DataSource getCustomerWhiteListData() {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID, "ID"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID, "Order ID"),
					new DataSourceField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP, FieldType.DATETIME,"Order Date"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME, "Name Customer"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS, "Shipping Address"), 
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL, "Email"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER, "CC Number"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK, "Issuer Bank"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_ECI, "ECI"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE, "Exp Month/Years"),
					new DataSourceField(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE, FieldType.DATETIME,"Genuine Date"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK, "Remark"),
					new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATEDBY, "Created By"),
					new DataSourceField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATED, FieldType.DATETIME,"Created Datetime")
			};
			dataSourceFields[0].setPrimaryKey(true);
			RafDataSource customerBlackListDataDs = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + FraudCustomerWhitelistPresenter.customerBlackListMaintenancePresenterServlet + "?method=fetchCustomerWhiteListData&type=DataSource",
					GWT.getHostPageBaseURL() + FraudCustomerWhitelistPresenter.customerBlackListMaintenancePresenterServlet + "?method=addCustomerWhiteListData&type=DataSource",
					GWT.getHostPageBaseURL() + FraudCustomerWhitelistPresenter.customerBlackListMaintenancePresenterServlet + "?method=updateCustomerWhiteListData&type=DataSource",
					GWT.getHostPageBaseURL() + FraudCustomerWhitelistPresenter.customerBlackListMaintenancePresenterServlet + "?method=deleteCustomerWhiteListData&type=DataSource",
					dataSourceFields);	
			
			return customerBlackListDataDs;
		   		
	}

	public static RafDataSource getFraudCaseRiskScoreData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID, "ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDRULENAME, "Rule Name"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS, "Risk Points"),
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseRiskScoreData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseTotalRiskScoreData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE, "Total Risk Score"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE, "Genuine")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseTotalRiskScoreData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseFraudManagementData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.TASKID, "Task ID"),
				new DataSourceTextField(DataNameTokens.TASKABLETOESCALATE, "Able to Escalate"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),				
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "Description (Read Only)"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, "Status"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, "Status"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, "Reason (Read Only)"),
				new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, FieldType.DATETIME, "Fraud Case Date"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, "Notes")
				};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseFraudManagementData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=updateFraudCaseViewerFraudManagementData&type=DataSource",
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseClaimedFraudManagementData(String taskId, String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.TASKID, "Task ID"),
				new DataSourceTextField(DataNameTokens.TASKABLETOESCALATE, "Able to Escalate"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),				
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, "Description"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, "Status"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, "Reason"),
				new DataSourceField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, FieldType.DATETIME, "Fraud Case Date"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, "Notes"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Order Status ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_TYPE_FC, "Type FC")		
				
				};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseFraudManagementData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=updateFraudCaseFraudManagementData&type=DataSource",
				null,
				dataSourceFields); 
		
		HashMap<String, String> paramsFetch = new HashMap<String, String>();
		paramsFetch.put(DataNameTokens.TASKID, taskId);
		paramsFetch.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(paramsFetch);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseActionLogData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, "ID"),						
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE, "Action Type"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYNAME, "Party"),
				new DataSourceField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME, FieldType.DATETIME, "Action Log Date"),		
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES, "Notes")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseActionLogData&type=DataSource",
				null,
				null,
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=deleteFraudCaseActionLogData&type=DataSource",
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseActionLogDetailData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, "ID"),						
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE, "Action Type"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYNAME, "Party"),
				new DataSourceField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME, FieldType.DATETIME, "Action Log Date"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES, "Notes")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseActionLogDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=addFraudCaseActionLogData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=updateFraudCaseActionLogData&type=DataSource",
				null,
				dataSourceFields); 
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseHistoryLogData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID, "Fraud Case ID"),	
				new DataSourceField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE, FieldType.DATETIME, "History Date"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSDESC, "Status"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYNOTES, "Notes")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseHistoryLogData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}

	public static RafDataSource getFraudCasePaymentTypeData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, "Payment Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO, "Other Info"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, "Acquiring Bank"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK, "Issuing Bank"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION, "Type"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT, "Limit")				
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCasePaymentTypeData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getFraudCaseMoreInfoOrderData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Suspicion Case ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT, "Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),				
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "Is First Time?"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseMoreInfoOrderData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=addFraudCaseRelatedOrderData&type=DataSource",
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getFraudCaseMoreInfoOrderData(String caseId, String ipAddress, String wcsOrderId, String orderAmtFrom, String orderAmtTo, String orderStatus, 
			String customerName, String customerMobile, String customerEmail, String prodCat, String product, String creditCardNumber, String recipientName, String recipientHp, 
			String recipientEmail, String dateFrom, String dateTo, String mercName, String userName, String shippingAdd, String logisticSvc) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_ORDERID, "Ven. Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT, "Amount"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE, "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Status"),				
				new DataSourceTextField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, "Is First Time?"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseMoreInfoOrderData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=addFraudCaseRelatedOrderData&type=DataSource",
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		params.put(DataNameTokens.VENORDER_ORDERDATE, dateFrom + "~" + dateTo);
		if (ipAddress != null) {
			params.put(DataNameTokens.VENORDER_IPADDRESS, ipAddress);
		}		
		if (wcsOrderId != null) {
			params.put(DataNameTokens.VENORDER_WCSORDERID, wcsOrderId);
		}
		if (orderAmtFrom != null) {
			if (orderAmtTo != null)
				params.put(DataNameTokens.VENORDER_AMOUNT, orderAmtFrom + "-" + orderAmtTo);
			else
				params.put(DataNameTokens.VENORDER_AMOUNT, orderAmtFrom);
		} else {
			if (orderAmtTo != null)
				params.put(DataNameTokens.VENORDER_AMOUNT, "0-" + orderAmtTo);
		}
		if (orderStatus != null) {
			params.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, orderStatus);
		}
		if (customerName != null) {
			params.put(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, customerName);
		}	
		if (customerMobile != null) {
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMERMOBILEPHONE, customerMobile);
		}	
		if (customerEmail != null) {
			params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_CUSTOMEREMAIL, customerEmail);
		}	
		if (prodCat != null) {
			params.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, prodCat);
		}	
		if (product != null) {
			params.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, product);
		}	
		if (creditCardNumber != null) {
			params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VAORIBORCCNUMBER, creditCardNumber);
		}	
		if (recipientName != null) {
			params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME, recipientName);
		}
		if (recipientHp != null) {
			params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTHP, recipientHp);
		}
		if (recipientEmail != null) {
			params.put(DataNameTokens.VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_RECIPIENTEMAIL, recipientEmail);
		}		
		if (mercName != null) {
			params.put(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME, mercName);
		}	
		if (userName != null) {
			params.put(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, userName);
		}	
		if (shippingAdd != null) {
			params.put(DataNameTokens.VENORDERITEM_VENADDRESS, shippingAdd);
		}
		if (logisticSvc != null) {
			params.put(DataNameTokens.VENORDERITEM_LOGLOGISTICSERVICE_LOGLOGISTICSERVICETYPE_LOGISTICSERVICETYPEDESC, logisticSvc);
		}
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;

	}
	
	public static RafDataSource getProductCategory() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORYID, "Product Category ID"),
				new DataSourceTextField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, "Product Category")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseProductCategoryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return retVal;

	}
	
	public static RafDataSource getProduct(String ProductCategory) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_PRODUCTID, "Product ID"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, "Product Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseProductData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 

		HashMap<String, String> params = new HashMap<String, String>();
		if (ProductCategory != null) {			
			params.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, ProductCategory);			
		} else {
			params.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, "");
		}
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;

	}
	
	public static RafDataSource getVenMerchant() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENMERCHANT_MERCHANTID, "Ven. Merchant ID"),
				new DataSourceTextField(DataNameTokens.VENMERCHANT_WCSMERCHANTID, "Merchant ID"),
				new DataSourceTextField(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME, "Merchant Name")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseMerchantData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return retVal;

	}

	public static RafDataSource getFraudCaseIlogFraudStatusData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_ILOGRECOMENDATION, "Ilog Recomendation"),
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseIlogRecomendationData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
	}

	public static RafDataSource getFraudCaseAttachmentData(String caseId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID, "ID"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILENAME, "File Name"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION, "Description"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_CREATEDBY, "Created By"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION, "Download"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID, "Case ID")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchFraudCaseAttachmentData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=addFraudCaseAttachmentData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=updateFraudCaseAttachmentData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=deleteFraudCaseAttachmentData&type=DataSource",
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		retVal.getOperationBinding(DSOperationType.UPDATE).setDefaultParams(params);
		
		return retVal;
	}
	
	public static RafDataSource getMigsUploadData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_MIGSID, "ID"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE, "Order ID"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_AUTHORISATIONCODE, "Auth. Code"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_CARDNUMBER, "Masked Card No."),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_CARDTYPE, "Card Type"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION, "Problem"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_TRANSACTIONTYPE, "Transaction Type"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_RESPONSECODE, "Response Code"),
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_ECOMMERCEINDICATOR, "ECI"),				
				new DataSourceTextField(DataNameTokens.MIGSUPLOAD_ACTION, "Action Taken")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + MigsUploadPresenter.migsUploadPresenterServlet + "?method=fetchMigsUploadData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + MigsUploadPresenter.migsUploadPresenterServlet + "?method=updateMigsUploadData&type=DataSource",
				null,
				dataSourceFields); 
		
		return retVal;
	}
	
	public static RafDataSource getMigsMasterData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.MIGSMASTER_MIGSID, "ID"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_TRANSACTIONID, "Transaction ID"),
				new DataSourceDateTimeField(DataNameTokens.MIGSMASTER_TRANSACTIONDATE, "Transaction Date"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_MERCHANTID, "Mechant ID"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ORDERREFERENCE, "Order Reference"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONREFERENCE, "Merc. Trans. Ref."),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_TRANSACTIONTYPE, "Trans. Type"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ACQUIRERID, "Acquire ID"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_BATCHNUMBER, "Batch Number"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_CURRENCY, "Currency"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_RRN, "RRN"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_RESPONSECODE, "Res. code"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ACQUIRERRESPONSECODE, "Auq. Resp. Code"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_AUTHORISATIONCODE, "Auth. Code"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_OPERATOR, "Operator"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONSOURCE, "Merch. Trans. Source"),
				new DataSourceDateTimeField(DataNameTokens.MIGSMASTER_ORDERDATE, "OrderDate"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_CARDTYPE, "Card Type"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_CARDNUMBER, "Card Number"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_CARDEXPIRYMONTH, "Exp. Month"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_CARDEXPIRYYEAR, "Exp. Year"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_DIALECTCSCRESULTCODE, "Dialect CSC Result Code"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ECOMMERCEINDICATOR, "ECI"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_PROBLEMDESCRIPTION, "Problem"),
				new DataSourceTextField(DataNameTokens.MIGSMASTER_ACTION, "Action Taken")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + MigsMasterPresenter.migsUploadPresenterServlet + "?method=fetchMasterMigsData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return retVal;
	}
	public static RafDataSource getUncalculatedCreditCardOrderData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, "Ven Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, "Ven Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID, "Order ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, "Payment ID"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, "Payment Amount"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER, "Auth. Code")
				};
		dataSourceFields[0].setPrimaryKey(true);
		dataSourceFields[1].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + UncalculatedCreditCardOrderPresenter.uncalculatedCreditCardOrderPresenterServlet + "?method=fetchUncalculatedCreditCardOrderData&type=DataSource",
				null,
				GWT.getHostPageBaseURL() + UncalculatedCreditCardOrderPresenter.uncalculatedCreditCardOrderPresenterServlet + "?method=updateUncalculatedCreditCardOrderData&type=DataSource",
				null,
				dataSourceFields); 
		
		return retVal;
	}

	public static RafDataSource getFraudBinCreditLimitData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID, "ID"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME, "Bank"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID, "Card Type"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_DESCRIPTION, "Description"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY, "Severity"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER, "BIN Number"),
				new DataSourceTextField(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE, "Credit Limit Estimate"),
//				new DataSourceBooleanField(DataNameTokens.VENBINCREDITLIMITESTIMATE_ISACTIVE, "Is Active")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudBinCreditLimitPresenter.fraudBinCreditLimitPresenterServlet + "?method=fetchBinCreditLimitData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudBinCreditLimitPresenter.fraudBinCreditLimitPresenterServlet + "?method=addBinCreditLimitData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudBinCreditLimitPresenter.fraudBinCreditLimitPresenterServlet + "?method=updateBinCreditLimitData&type=DataSource",
				GWT.getHostPageBaseURL() + FraudBinCreditLimitPresenter.fraudBinCreditLimitPresenterServlet + "?method=removeBinCreditLimitData&type=DataSource",
				dataSourceFields); 
		
		return retVal;
	}

	public static DataSource getPartyData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTY_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Party Name"),
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchPartyData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return retVal;
	}
	
	public static RafDataSource getWhiteListOrderHistoryData(String orderId) {	
				DataSourceField[] dataSourceFields = {
						new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL,  "Email"),
						new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS,  "Shipping Address"),
						new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER, "CC Number"),				
						new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK, "Issuer Bank"),	
						new DataSourceTextField(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE, "Exp Date")
									
				};
				dataSourceFields[0].setPrimaryKey(true);
				
				RafDataSource retVal = new RafDataSource(
						"/response/data/*",
						GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchWHiteListOrderHistory&type=DataSource",
						null,null,null,	
						dataSourceFields);		
		
				HashMap<String, String> params = new HashMap<String, String>();		
				if (orderId != null) {			
					params.put(DataNameTokens.VENORDER_WCSORDERID, orderId);
				} else {
					params.put(DataNameTokens.VENORDER_WCSORDERID, "");
				}				
				retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
				return retVal;
	}    
			

	public static RafDataSource getOrderHistoryFilterData(String orderId) {	
				DataSourceField[] dataSourceFields = {
						new DataSourceTextField(DataNameTokens.ORDERHISTORY_ID,  "Id"),
						new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID,  "Order ID"),	
						new DataSourceTextField(DataNameTokens.ORDERHISTORY_STRINGFILTER,  "FilterBy"),						
						new DataSourceTextField(DataNameTokens.ORDERHISTORY_DESCRIPTION,  "Description"),
						new DataSourceTextField(DataNameTokens.ORDERHISTORY_TOTALORDERHISTORY, "Total Order History")
				};
				dataSourceFields[0].setPrimaryKey(true);
				
				RafDataSource retVal = new RafDataSource(
						"/response/data/*",
						GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchOrderHistoryFilter&type=DataSource",
						null,null,null,	
						dataSourceFields);		
		
				HashMap<String, String> params = new HashMap<String, String>();		
				if (orderId != null) {			
					params.put(DataNameTokens.VENORDER_WCSORDERID, orderId);
				} else {
					params.put(DataNameTokens.VENORDER_WCSORDERID, "");
				}				
				retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
				return retVal;
	}    
	
	public static RafDataSource getSameOrderHistoryData(String orderId,String type,String filter,int start,int end) {	
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDER_ORDERID,  "Id "),	
				new DataSourceTextField(DataNameTokens.VENORDER_WCSORDERID,  "Order ID"),	
				new DataSourceTextField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE,  "Status"),
				new DataSourceDateTimeField(DataNameTokens.VENORDER_ORDERDATE,  "Order Date"),
				new DataSourceTextField(DataNameTokens.VENORDER_AMOUNT,  "Amount"),
				new DataSourceTextField(DataNameTokens.VENORDER_IPADDRESS, "IP Address"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Customer Full Name"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_PHONE_NUMBER, "Customer Phone Number"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_MOBILE_NUMBER, "Customer Mobile Number"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_EMAIL, "Customer Email"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_PRODUCT_NAME, "Product Name"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_QTY, "Order Qty"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_SHPIPPING_ADDRESS, "Shipping Address"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_BILLING_METHOD, "Billing Method"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_STATUS, "Payment Status"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_CC, "Credit Card Number"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_ISSUER, "Issuer Bank"),
				new DataSourceTextField(DataNameTokens.ORDERHISTORY_ECI, "ECI")	
						
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchSameOrderHistory&type=DataSource&start="+start+"&end="+end,
				null,null,null,	
				dataSourceFields);		

		HashMap<String, String> params = new HashMap<String, String>();		
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_WCSORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_WCSORDERID, "");
		}				
		if (type!= null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, type);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		if (filter!= null) {			
			params.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, filter);
		} else {
			params.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, "");
		}		
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;
}    
	public static RafDataSource getOrderItemInformation(String orderId) {	
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_STREETADDRESS1,  "Shipping Address "),			
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_KECAMATAN,  "District"),						
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_VENCITY_CITYNAME,  "City"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_VENCOUNTRY_STATE,  "State"),				
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_VENCOUNTRY_COUNTRYNAME,  "Country"),
				new DataSourceTextField(DataNameTokens.VENORDERITEM_VENADDRESS_POSTALCODE, "Postal Code"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Shipping Type")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchOrderItemInformation&type=DataSource",
				null,null,null,	
				dataSourceFields);		

		HashMap<String, String> params = new HashMap<String, String>();		
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}						
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;
}    
	
	public static RafDataSource getTotalRiskPoint(String caseId) {	
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "id"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, "Total Point")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchTotalRiskPoint&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
}    
	public static RafDataSource getWhyBlackList(String orderId) {	
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FRDBLACKLIST_ORDERID, "id"),
				new DataSourceTextField(DataNameTokens.FRDBLACKLIST_BLACKLIST_REASON, "Reason BlackList")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchWhyBlackList&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if (orderId != null) {			
			params.put(DataNameTokens.VENORDER_ORDERID, orderId);
		} else {
			params.put(DataNameTokens.VENORDER_ORDERID, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
}    
	public static RafDataSource getOrderCalculateFraud(String dateOrder) {	

		DataSourceField[] dataSourceFields = {				
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID, "Order Id"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE, "Date"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_AMOUNT, "Amount"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, "Customer Username"),
				new DataSourceTextField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, "Status")
				};
		dataSourceFields[0].setPrimaryKey(true);		
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FraudCaseManagementPresenter.fraudCaseMaintenancePresenterServlet + "?method=fetchOrderCalculateFraud&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 

		HashMap<String, String> params = new HashMap<String, String>();		
		if (dateOrder != null) {						
			params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE, dateOrder);
		} else {
			params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERDATE, "");
		}	
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return retVal;
}  
	
	public static RafDataSource getFraudParameterRule31Data() {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.FRDPARAMETERRULE31_ID, "ID"),
					new DataSourceTextField(DataNameTokens.FRDPARAMETERRULE31_EMAIL, "Email"),
					new DataSourceTextField(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER, "Credit Card Number"),
					};
			dataSourceFields[0].setPrimaryKey(true);		
			
			RafDataSource retVal = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + FraudParameterRule31Presenter.fraudParameterRulePresenterServlet + "?method=fetchFraudParameterRule31Data&type=DataSource",
					GWT.getHostPageBaseURL() + FraudParameterRule31Presenter.fraudParameterRulePresenterServlet + "?method=addFraudParameterRule31Data&type=DataSource",
					GWT.getHostPageBaseURL() + FraudParameterRule31Presenter.fraudParameterRulePresenterServlet + "?method=updateFraudParameterRule31Data&type=DataSource",
					GWT.getHostPageBaseURL() + FraudParameterRule31Presenter.fraudParameterRulePresenterServlet + "?method=deleteFraudParameterRule31Data&type=DataSource",
					dataSourceFields); 
			
			return retVal;
		}
	
}