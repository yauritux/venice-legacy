package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.data.RafDataSource;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSOperationType;

/**
 * Task detail implementation for Cash Receive and Sales Journal Reconciliation processing
 * 
 * @author Roland
 * 
 */
public class CashReceiveSalesJournalReconciliationTaskDetail {
	
	/**
	 * Returns the data source for journals for the given task id
	 * @param taskId the task id to use for the query
	 * @return the data source
	 */
	public static DataSource getFundsInAndSalesRecordDataSource(String taskId) {
		RafDataSource fundInData = FinanceData.getFundInReconciliationData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		fundInData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return fundInData;
	}
	
//	/**
//	 * Implements the actions for the various approval decisions to call servlet methods and commands
//	 * @param journalGroupIds the journal group ids used in the action
//	 * @param journalsListGridWidget the list grid that displays the journal records
//	 * @param decision the action decision taken by the user
//	 */
//	public static void onApprovalDecision(List<String> journalGroupIds, final JournalsListGridWidget journalsListGridWidget, String decision) {
//		/*
//		 * An RPC request instance to use
//		 */
//		RPCRequest request=new RPCRequest();
//		
//		/*
//		 * Map the payment ids into a HashMap from the list
//		 */
//		HashMap<String,String>map = new HashMap<String,String>( 11 );
//		for (int i=0;i<journalGroupIds.size();i++) {
//			map.put(ProcessNameTokens.JOURNALGROUPID+(i+1), journalGroupIds.get(i));
//		}
//		String apPaymentId = map.toString();
//		
//		/*
//		 * Set the data for the request
//		 */
//		request.setData(apPaymentId);
//		
//		/*
//		 * Set the action URL to use for the request
//		 */
//		request.setActionURL(GWT.getHostPageBaseURL() + JournalPresenter.journalPresenterServlet + "?method="+decision+"Journal&type=RPC");
//		request.setHttpMethod("POST");
//		request.setUseSimpleHttp(true);
//		
//		RPCManager.setPromptStyle(PromptStyle.DIALOG);
//		RPCManager.setDefaultPrompt("Processing approvals and updating journal records...");
//		RPCManager.setShowPrompt(true);
//		
//		RPCManager.sendRequest(request, 
//				new RPCCallback () {
//					/* (non-Javadoc)
//					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
//					 */
//					public void execute(RPCResponse response,
//							Object rawData, RPCRequest request) {
//						String rpcResponse = rawData.toString();
//						if (!rpcResponse.startsWith("0")) {
//							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
//						}
//						journalsListGridWidget.refreshData();
//						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
//						RPCManager.setShowPrompt(false);
//					}
//		}
//		);	
//	}
}
