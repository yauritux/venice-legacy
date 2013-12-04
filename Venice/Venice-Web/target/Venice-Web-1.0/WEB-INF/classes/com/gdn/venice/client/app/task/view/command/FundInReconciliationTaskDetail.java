package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter;
import com.gdn.venice.client.app.finance.widgets.FundInReconciliationListGridWidget;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.presenter.ToDoListPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;

public class FundInReconciliationTaskDetail {
	ToDoListPresenter.MyView view;
	
	public static DataSource getFundInReconciliationDataSource(String taskId) {
		RafDataSource fundInReconciliationData = FinanceData.getFundInReconciliationData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		fundInReconciliationData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return fundInReconciliationData;
	}
	
	public static void onApprovalDecision(List<String> fundInReconRecordIds, final FundInReconciliationListGridWidget fundInReconciliationListGridWidget, String decision) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<fundInReconRecordIds.size();i++) {
			map.put(ProcessNameTokens.FUNDINRECONRECORDID+(i+1), fundInReconRecordIds.get(i));
		}
		String fundInReconRecordId = map.toString();
		
		request.setData(fundInReconRecordId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + FundInReconciliationPresenter.fundIdReconciliationServlet + "?method="+decision+"FundInReconRecord&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing approvals and updating reconciliation records...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (!rpcResponse.startsWith("0")) {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						fundInReconciliationListGridWidget.refreshFundInReconData();
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
						RPCManager.setShowPrompt(false);
					}
		}
		);
		
	}
	
	
}
