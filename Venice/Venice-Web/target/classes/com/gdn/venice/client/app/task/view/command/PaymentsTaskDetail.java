package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter;
import com.gdn.venice.client.app.finance.widgets.PaymentsListGridWidget;
import com.gdn.venice.client.app.task.ProcessNameTokens;
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

/**
 * Task detail implementation for AP Payment processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PaymentsTaskDetail {
	
	/**
	 * Returns the data source for AP payments for the given task id
	 * @param taskId the task id to use for the query
	 * @return the data source
	 */
	public static DataSource getPaymentsDataSource(String taskId) {
		RafDataSource paymentsData = FinanceData.getPaymentData();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		paymentsData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return paymentsData;
	}
	
	/**
	 * Implements the actions for the various approval decisions to call servlet methods and commands
	 * @param apPaymentIds the payment ids used in the action
	 * @param paymentsListGridWidget the list grid that displays the payment records
	 * @param decision the action decision taken by the user
	 */
	public static void onApprovalDecision(List<String> apPaymentIds, final PaymentsListGridWidget paymentsListGridWidget, String decision) {
		/*
		 * An RPC request instance to use
		 */
		RPCRequest request=new RPCRequest();
		
		/*
		 * Map the payment ids into a HashMap from the list
		 */
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<apPaymentIds.size();i++) {
			map.put(ProcessNameTokens.APPAYMENTID+(i+1), apPaymentIds.get(i));
		}
		String apPaymentId = map.toString();
		
		/*
		 * Set the data for the request
		 */
		request.setData(apPaymentId);
		
		/*
		 * Set the action URL to use for the request
		 */
		request.setActionURL(GWT.getHostPageBaseURL() + PaymentProcessingPresenter.paymentProcessingPresenterServlet + "?method="+decision+"ApPayment&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing approvals and updating payment records...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (!rpcResponse.startsWith("0")) {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						paymentsListGridWidget.refreshData();
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
						RPCManager.setShowPrompt(false);
					}
		}
		);	
	}
}
