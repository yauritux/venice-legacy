package com.gdn.venice.client.app.task.presenter;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.task.data.TaskManagementData;
import com.gdn.venice.client.app.task.view.handlers.ToDoListUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;

/**
 * @author Henry Chandra
 */
public class ToDoListPresenter extends
Presenter<ToDoListPresenter.MyView, ToDoListPresenter.MyProxy> implements ToDoListUiHandlers{
	public final static String toDoListServlet = "ToDoListPresenterServlet";
		
	/**
	 * {@link ToDoListPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.toDoListPage)
	public interface MyProxy extends Proxy<ToDoListPresenter>, Place {
	}

	/**
	 * {@link ToDoListPresenter}'s view.
	 */ 
	public interface MyView extends View, HasUiHandlers<ToDoListUiHandlers> {
		public void loadToDoListData(DataSource dataSource, String userRole);
		public void refreshToDoListData();
		public void refreshFundInData();
	}

	@Inject
	public ToDoListPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onGetUserRoleData();
	}


	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, 
				this);
	}
	
	@Override
	public void onGetUserRoleData() {	
		RPCRequest request=new RPCRequest();				
		request.setActionURL(GWT.getHostPageBaseURL() + "ToDoListPresenterServlet?method=getUserRole&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String userRole = rawData.toString().trim();
						getView().loadToDoListData(TaskManagementData.getToDoListData(), userRole);
					}
    	});  
	}
	
	@Override
	public void onClaimTask(Long taskType, List<String> taskIds) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<taskIds.size();i++) {
			map.put(DataNameTokens.TASKID+(i+1), taskIds.get(i));
		}
		
		/*
		 * Added task type to the map to pass to the back end command
		 */
		map.put(DataNameTokens.TASKTYPEID, taskType.toString());
		
		String taskData = map.toString();

		request.setData(taskData);
		request.setActionURL(GWT.getHostPageBaseURL() + toDoListServlet + "?method=claimToDoListTask&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Claiming tasks...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshToDoListData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
					}
		}
		);
		
	}
	
	@Override
	public void onUnclaimTask(Long taskType, List<String> taskIds) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<taskIds.size();i++) {
			map.put(DataNameTokens.TASKID+(i+1), taskIds.get(i));
		}
		
		/*
		 * Added task type to the map to pass to the back end command
		 */
		map.put(DataNameTokens.TASKTYPEID, taskType.toString());
		
		String taskData = map.toString();

		request.setData(taskData);
		request.setActionURL(GWT.getHostPageBaseURL() + toDoListServlet + "?method=unclaimToDoListTask&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Releasing tasks...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshToDoListData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
					}
		}
		);
		
	}



	@Override
	public void onCompleteTask(Long taskType, List<String> taskIds) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<taskIds.size();i++) {
			map.put(DataNameTokens.TASKID+(i+1), taskIds.get(i));
		}
		
		/*
		 * Added task type to the map to pass to the back end command
		 */
		map.put(DataNameTokens.TASKTYPEID, taskType.toString());
		
		String taskData = map.toString();
		
		request.setData(taskData);
		request.setActionURL(GWT.getHostPageBaseURL() + toDoListServlet + "?method=completeToDoListTask&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Completing tasks...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshToDoListData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
					}
		}
		);
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onRefundButtonClicked(java.util.HashMap)
	 */
	@Override
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(refundDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + "FundInReconciliationPresenterServlet?method=refund&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing refunds...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						
						if (rpcResponse.startsWith("0")) {
							getView().refreshFundInData();
						} else {
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onAllocate(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onAllocate(String sourceVenWCSOrderId,String sourceVenOrderPaymentId,
			String fundsInReconRecordId,
			String destinationVenOrderPaymentId, String allocationAmount,
			String destinationVenOrderId) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>();
		if (sourceVenOrderPaymentId!=null) {
			map.put("sourceVenOrderPaymentId", sourceVenOrderPaymentId);
		}
		if (fundsInReconRecordId!=null) {
			map.put("fundsInReconRecordId", fundsInReconRecordId);
		}
		if (destinationVenOrderPaymentId!=null) {
			map.put("destinationVenOrderPaymentId", destinationVenOrderPaymentId);
		}
		if (allocationAmount!=null) {
			map.put("allocationAmount", allocationAmount);
		}
		if (destinationVenOrderId!=null) {
			map.put("destinationVenOrderId", destinationVenOrderId);
		}
		if (sourceVenWCSOrderId!=null) {
			map.put("sourceVenWCSOrderId", sourceVenWCSOrderId);
		}
		
		String allocationData = Util.formXMLfromHashMap(map);
		
		request.setData(allocationData);
		
		request.setActionURL(GWT.getHostPageBaseURL() + "FundInReconciliationPresenterServlet?method=allocateFundIn&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Allocating payments...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshFundInData();
							SC.say(DataMessageTokens.FIN_ALLOCATION_SUCCESSFUL);
						} else {
							/*
							 * Use the 4th positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[4]);
							}else{
								SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		}
		);
	}	
}
