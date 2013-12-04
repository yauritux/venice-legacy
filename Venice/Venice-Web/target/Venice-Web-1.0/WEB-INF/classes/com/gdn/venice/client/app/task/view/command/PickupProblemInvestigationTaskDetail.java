package com.gdn.venice.client.app.task.view.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.data.RafDataSource;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSOperationType;

public class PickupProblemInvestigationTaskDetail {
	
	public static List<DataSource> getOrderDetailDataSources(String taskId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		
		RafDataSource orderDetailData = GeneralData.getOrderDetailData(null);
		HashMap<String, String> orderDetailParams = new HashMap<String, String>();
		orderDetailParams.put(DataNameTokens.TASKID, taskId);
		orderDetailData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderDetailParams);
		
		dataSources.add(orderDetailData);
		
		RafDataSource orderItemData = GeneralData.getOrderItemData(null);
		HashMap<String, String> orderItemParams = new HashMap<String, String>();
		orderItemParams.put(DataNameTokens.TASKID, taskId);
		orderItemData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderItemParams);
		
		dataSources.add(orderItemData);
		
		RafDataSource orderCustomerData = GeneralData.getOrderCustomerData(null);
		HashMap<String, String> orderCustomerParams = new HashMap<String, String>();
		orderCustomerParams.put(DataNameTokens.TASKID, taskId);
		orderCustomerData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderCustomerParams);
		
		dataSources.add(orderCustomerData);
		
		RafDataSource orderCustomerAddressData = GeneralData.getOrderCustomerAddressData(null);
		HashMap<String, String> orderCustomerAddressParams = new HashMap<String, String>();
		orderCustomerAddressParams.put(DataNameTokens.TASKID, taskId);
		orderCustomerAddressData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderCustomerAddressParams);
		
		dataSources.add(orderCustomerAddressData);
		
		RafDataSource orderCustomerContactData = GeneralData.getOrderCustomerContactData(null);
		HashMap<String, String> orderCustomerContactParams = new HashMap<String, String>();
		orderCustomerContactParams.put(DataNameTokens.TASKID, taskId);
		orderCustomerContactData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderCustomerContactParams);
		
		dataSources.add(orderCustomerContactData);
			
		RafDataSource orderLogisticsAirwayBillData = GeneralData.getOrderLogisticsAirwayBillData(null);
		HashMap<String, String> orderLogisticsAirwayBillParams = new HashMap<String, String>();
		orderLogisticsAirwayBillParams.put(DataNameTokens.TASKID, taskId);
		orderLogisticsAirwayBillData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderLogisticsAirwayBillParams);
		
		dataSources.add(orderLogisticsAirwayBillData);
		
		RafDataSource orderFinancePaymentData = GeneralData.getOrderFinancePaymentData(null);
		HashMap<String, String> orderFinancePaymentParams = new HashMap<String, String>();
		orderFinancePaymentParams.put(DataNameTokens.TASKID, taskId);
		orderFinancePaymentData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderFinancePaymentParams);
		
		dataSources.add(orderFinancePaymentData);
		
		RafDataSource orderFinanceReconciliationData = GeneralData.getOrderFinanceReconciliationData(null);
		HashMap<String, String> orderFinanceReconciliationParams = new HashMap<String, String>();
		orderFinanceReconciliationParams.put(DataNameTokens.TASKID, taskId);
		orderFinanceReconciliationData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderFinanceReconciliationParams);
		
		dataSources.add(orderFinanceReconciliationData);
		
		RafDataSource orderHistoryData = GeneralData.getOrderHistoryOrderData(null);
		HashMap<String, String> orderHistoryParams = new HashMap<String, String>();
		orderHistoryParams.put(DataNameTokens.TASKID, taskId);
		orderHistoryData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderHistoryParams);
		
		dataSources.add(orderHistoryData);

		RafDataSource orderHistoryOrderItemData = GeneralData.getOrderHistoryOrderItemData(null);
		HashMap<String, String> orderHistoryOrderItemParams = new HashMap<String, String>();
		orderHistoryOrderItemParams.put(DataNameTokens.TASKID, taskId);
		orderHistoryOrderItemData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(orderHistoryOrderItemParams);
		
		dataSources.add(orderHistoryOrderItemData);

		return dataSources;
	}

}
