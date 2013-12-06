package com.gdn.venice.client.app.task.view.command;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.presenter.ActivityReportReconciliationPresenter;
import com.gdn.venice.client.app.logistic.widgets.ActivityReportReconciliation;
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

/**
 * Task detail implementation for activity report reconciliation processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ActivityReportReconciliationTaskDetail {
	ToDoListPresenter.MyView view;
	
	/**
	 * Gets the data source for the activity reconciliation records
	 * @param taskId
	 * @return the data source
	 */
	public static DataSource getActivityReportReconciliationDataSource(String taskId) {
		RafDataSource activityReportReconciliationData = LogisticsData.getActivityReportAirwayBillsData(0, 20);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(DataNameTokens.TASKID, taskId);
		activityReportReconciliationData.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		
		return activityReportReconciliationData;
	}
	
	/**
	 * Implements the action on the approval decision to call the various servlet methods and commands
	 * @param airwayBillIds the airwazy bill ids to act on
	 * @param airwayBillListGrid the list grid of airway bills
	 * @param decision the approval decision (approve/reject etc.)
	 */
	public static void onApprovalDecision(List<String> airwayBillIds, final ActivityReportReconciliation airwayBillListGrid, String decision) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<airwayBillIds.size();i++) {
			map.put(ProcessNameTokens.AIRWAYBILLID+(i+1), airwayBillIds.get(i));
		}
		String airwayBillId = map.toString();
		
		request.setData(airwayBillId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + ActivityReportReconciliationPresenter.activityReportReconciliationServlet + "?method="+decision+"ActivityReportAirwayBill&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing approvals and updating activity reconciliation records...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (!rpcResponse.startsWith("0")) {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						airwayBillListGrid.refreshAirwayBillData();
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
						RPCManager.setShowPrompt(false);
					}
		}
		);
		
	}
	
}
