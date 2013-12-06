package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.presenter.InvoiceReconciliationPresenter;
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
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * Task detail implementation for invoice reconciliation processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class InvoiceReconciliationTaskDetail {
	ToDoListPresenter.MyView view;
	
	/**
	 * Returns the invoice reconciliation data source
	 * @param taskId
	 * @return
	 */
	public static DataSource getInvoiceReconciliationDataSource(String taskId) {
		RafDataSource invoiceReconciliationData = LogisticsData.getInvoiceReportUploadData(taskId);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		invoiceReconciliationData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return invoiceReconciliationData;
	}
	
	/**
	 * Implements the action on the approval decision to call the various servlet methods and commands
	 * @param airwayBillIds the airway bill ids to process
	 * @param invoiceReconciliationListGrid the list grid of reconciliation records
	 * @param decision the approval decision (approve, reject etc.)
	 */
	public static void onApprovalDecision(List<String> invoiceNumbers, final ListGrid invoiceReconciliation, final String decision) {
		String ids = "";
		
		for (int i=0;i<invoiceNumbers.size();i++) {
			ids += "'"+invoiceNumbers.get(i)+"'";
			if(i != invoiceNumbers.size()-1)
				ids += ",";
		}	
		
		RPCRequest rpcRequest=new RPCRequest();
    	rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method=getInvoiceReportUploadIdTo"+decision+"&type=RPC&invoiceNumbers="+ids);
		rpcRequest.setHttpMethod("POST");
		rpcRequest.setUseSimpleHttp(true);
		rpcRequest.setShowPrompt(false);
		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
			public void execute(RPCResponse response,	Object rawData, RPCRequest request) {
				String invoiceReportUploadId = rawData.toString().replace("\r\n", "");
				if(!invoiceReportUploadId.equals("{}")){
					request.setData(invoiceReportUploadId);			
					request.setActionURL(GWT.getHostPageBaseURL() + InvoiceReconciliationPresenter.invoiceReconciliationServlet + "?method="+decision+"InvoiceAirwayBill&type=RPC");
					request.setHttpMethod("POST");
					request.setUseSimpleHttp(true);		
					RPCManager.setPromptStyle(PromptStyle.DIALOG);
					RPCManager.setDefaultPrompt("Processing approvals and updating invoice reconciliation records...");
					RPCManager.setShowPrompt(true);
					RPCManager.sendRequest(request, 
							new RPCCallback () {
								public void execute(RPCResponse response,
										Object rawData, RPCRequest request) {
									String rpcResponse = rawData.toString();
									if (rpcResponse.startsWith("0")) {
										SC.say(DataMessageTokens.GENERAL_SUCCESS_MESSAGE);
									} else {
										SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
									}
								}
							}
						);		
				} else {
					if(decision.equals("Approve"))
						SC.warn(DataMessageTokens.NO_RECORD_APPROVED);
					else
						SC.warn(DataMessageTokens.NO_RECORD_REJECTED);
				}
			}
		});	
	}
}
