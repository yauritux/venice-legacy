package com.gdn.venice.client.app.task.view.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.data.RafDataSource;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSOperationType;

public class FraudCaseManagementTaskDetail {
	
	public static List<DataSource> getFraudCaseDetailDataSources(String taskId, String caseId, String orderId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		
		RafDataSource totalRiskScoreData = FraudData.getFraudCaseTotalRiskScoreData(null);
		HashMap<String, String> totalRiskScoreParams = new HashMap<String, String>();
		totalRiskScoreParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, caseId);
		totalRiskScoreData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(totalRiskScoreParams);		
		dataSources.add(totalRiskScoreData);
		
		RafDataSource customerSummaryData = FraudData.getFraudCaseCustomerSummaryData(orderId);
		HashMap<String, String> customerSummaryParams = new HashMap<String, String>();
		customerSummaryParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		customerSummaryData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(customerSummaryParams);		
		dataSources.add(customerSummaryData);
		
		RafDataSource customerData = FraudData.getFraudCaseCustomerData(caseId,orderId);
		HashMap<String, String> customerParams = new HashMap<String, String>();
		customerParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		customerData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(customerParams);		
		dataSources.add(customerData);
		
		RafDataSource customerAddressData = FraudData.getFraudCaseCustomerAddressData(orderId);
		HashMap<String, String> customerAddressParams = new HashMap<String, String>();
		customerAddressParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		customerAddressData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(customerAddressParams);		
		dataSources.add(customerAddressData);
		
		RafDataSource customerContactData = FraudData.getFraudCaseCustomerContactData(orderId);
		HashMap<String, String> customerContactParams = new HashMap<String, String>();
		customerContactParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		customerContactData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(customerContactParams);		
		dataSources.add(customerContactData);
		
		RafDataSource orderDetailData = FraudData.getFraudCaseOrderDetailData(null,null);
		HashMap<String, String> orderDetailParams = new HashMap<String, String>();
		orderDetailParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		orderDetailParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		orderDetailData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderDetailParams);		
		dataSources.add(orderDetailData);
		
		RafDataSource orderItemData = FraudData.getFraudCaseOrderItemData(null);
		HashMap<String, String> orderItemParams = new HashMap<String, String>();
		orderItemParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		orderItemData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderItemParams);		
		dataSources.add(orderItemData);		
				
		RafDataSource paymentData = FraudData.getFraudCasePaymentData(null,null);
		HashMap<String, String> paymentParams = new HashMap<String, String>();
		paymentParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		paymentParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		paymentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(paymentParams);		
		dataSources.add(paymentData);
		
		RafDataSource riskScoreData = FraudData.getFraudCaseRiskScoreData(null);
		HashMap<String, String> riskScoreParams = new HashMap<String, String>();
		riskScoreParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID, caseId);
		riskScoreData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(riskScoreParams);		
		dataSources.add(riskScoreData);		
		
		RafDataSource relatedOrderData = FraudData.getFraudCaseRelatedOrderData(null);
		HashMap<String, String> relatedOrderParams = new HashMap<String, String>();
		relatedOrderParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		relatedOrderData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(relatedOrderParams);		
		dataSources.add(relatedOrderData);
		
		RafDataSource fraudManagementData = FraudData.getFraudCaseFraudManagementData(null);
		HashMap<String, String> fraudManagementParams = new HashMap<String, String>();
		fraudManagementParams.put(DataNameTokens.TASKID, taskId);
		fraudManagementParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		fraudManagementData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(fraudManagementParams);		
		dataSources.add(fraudManagementData);
		
		RafDataSource actionLogData = FraudData.getFraudCaseActionLogData(null);
		HashMap<String, String> actionLogParams = new HashMap<String, String>();
		actionLogParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		actionLogData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(actionLogParams);		
		dataSources.add(actionLogData);
		
		RafDataSource historyLogData = FraudData.getFraudCaseHistoryLogData(null);
		HashMap<String, String> historyLogParams = new HashMap<String, String>();
		historyLogParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		historyLogData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(historyLogParams);		
		dataSources.add(historyLogData);
		
		RafDataSource paymentTypeData = FraudData.getFraudCasePaymentTypeData(null);
		HashMap<String, String> paymentTypeParams = new HashMap<String, String>();
		paymentTypeParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		paymentTypeData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(paymentTypeParams);		
		dataSources.add(paymentTypeData);
		
		RafDataSource moreInfoData = FraudData.getFraudCaseMoreInfoOrderData(null);
		HashMap<String, String> moreInfoParams = new HashMap<String, String>();
		moreInfoParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		moreInfoData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(moreInfoParams);		
		dataSources.add(moreInfoData);
		
		RafDataSource ilogFraudStatusData = FraudData.getFraudCaseIlogFraudStatusData(null);
		HashMap<String, String> ilogFraudStatusParams = new HashMap<String, String>();
		ilogFraudStatusParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		ilogFraudStatusData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(ilogFraudStatusParams);		
		dataSources.add(ilogFraudStatusData);
		
		RafDataSource attachmentData = FraudData.getFraudCaseAttachmentData(null);
		HashMap<String, String> attachmentParams = new HashMap<String, String>();
		attachmentParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		attachmentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(attachmentParams);		
		dataSources.add(attachmentData);
		
		RafDataSource summaryPaymentData = FraudData.getFraudCasePaymentSummaryData(null);
		HashMap<String, String> summaryPaymentParams = new HashMap<String, String>();
		summaryPaymentParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		summaryPaymentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(summaryPaymentParams);		
		dataSources.add(summaryPaymentData);
		
		RafDataSource detailPaymentData = FraudData.getFraudCasePaymentDetailData(null);
		HashMap<String, String> detailPaymentParams = new HashMap<String, String>();
		detailPaymentParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		detailPaymentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(detailPaymentParams);		
		dataSources.add(detailPaymentData);
		
		RafDataSource relatedOrderItemData = FraudData.getFraudCaseRelatedOrderItemData(null);
		HashMap<String, String> relatedOrderItemParams = new HashMap<String, String>();
		relatedOrderItemParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		relatedOrderItemData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(relatedOrderItemParams);		
		dataSources.add(relatedOrderItemData);
		
		RafDataSource relatedFraudData = FraudData.getFraudCaseRelatedData(null);
		HashMap<String, String> relatedFraudParams = new HashMap<String, String>();
		relatedFraudParams.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, caseId);
		relatedFraudData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(relatedFraudParams);		
		dataSources.add(relatedFraudData);
		
		RafDataSource whiteCustData = FraudData.getWhiteListOrderHistoryData(null);
		HashMap<String, String>whiteFraudParams = new HashMap<String, String>();
		whiteFraudParams.put(DataNameTokens.VENORDER_WCSORDERID, orderId);
		whiteCustData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(whiteFraudParams);		
		dataSources.add(whiteCustData);
		
		RafDataSource filterOrderHistData = FraudData.getOrderHistoryFilterData(null);
		HashMap<String, String> filterOrderHisParams = new HashMap<String, String>();
		filterOrderHisParams.put(DataNameTokens.VENORDER_WCSORDERID, orderId);
		filterOrderHistData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(filterOrderHisParams);		
		dataSources.add(filterOrderHistData);
		
		RafDataSource contactDetailData = FraudData.getFraudCaseCustomerContactData(orderId);
		HashMap<String, String> filterContactDetailParams = new HashMap<String, String>();
		filterContactDetailParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		contactDetailData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(filterContactDetailParams);
		dataSources.add(contactDetailData);
		
		RafDataSource categoryData = FraudData.getFraudCaseCategoryData(orderId);
		HashMap<String, String> categoryParams = new HashMap<String, String>();
		categoryParams.put(DataNameTokens.VENORDER_ORDERID, orderId);
		categoryData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(categoryParams);		
		dataSources.add(categoryData);		
		
		return dataSources;
	}

}
