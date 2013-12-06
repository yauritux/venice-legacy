package com.gdn.venice.client.app.logistic.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.general.presenter.OrderDataViewerPresenter;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.view.handlers.ActivityReportReconciliationUiHandlers;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
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
 * Presenter for Activity Report Reconciliation
 * 
 * @author Henry Chandra
 */
public class ActivityReportReconciliationPresenter
		extends
		Presenter<ActivityReportReconciliationPresenter.MyView, ActivityReportReconciliationPresenter.MyProxy>
		implements ActivityReportReconciliationUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String activityReportReconciliationServlet = "ActivityReportReconciliationPresenterServlet";

	/**
	 * {@link ActivityReportReconciliationPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticActivityReportReconciliationPage)
	public interface MyProxy extends Proxy<ActivityReportReconciliationPresenter>, Place {
	}

	/**
	 * {@link ActivityReportReconciliationPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ActivityReportReconciliationUiHandlers> {
		public void loadAirwayBillData(DataSource dataSource, Map<String,String> approval, Map<String,String> status);
		public void loadUploadLogData(DataSource dataSource);
		public void refreshAirwayBillData();
		public void setLastPage(int totalRows);
		public void refreshUploadLogListGridData();
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public ActivityReportReconciliationPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData(0, 0);
//		onCountTotalData();
		this.dispatcher = dispatcher;
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ActivityReportReconciliationUiHandlers#onExpandAirwayBillRow(java.lang.String)
	 */
	@Override
	public DataSource onExpandAirwayBillRow(String airwayBillId) {
		return LogisticsData.getActivityReportsReconciliationProblemData(airwayBillId);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ActivityReportReconciliationUiHandlers#onSubmitForApproval(java.util.List)
	 */
	@Override
	public void onSubmitForApproval(List<String> airwayBillIds) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>();
		for (int i=0;i<airwayBillIds.size();i++) {
			map.put(ProcessNameTokens.AIRWAYBILLID+(i+1), airwayBillIds.get(i));
		}
		String airwayBillId = map.toString();
		
		request.setData(airwayBillId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + activityReportReconciliationServlet + "?method=submitForApproval&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Submitting activity reports for approval...");
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
							getView().refreshAirwayBillData();
							SC.say(DataMessageTokens.SUBMITTED_FOR_APPROVAL);
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
						RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
						RPCManager.setShowPrompt(false);
					}
		}
		);
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ActivityReportReconciliationUiHandlers#onShowMerchantPickUpDetail(java.lang.String)
	 */
	@Override
	public DataSource onShowMerchantPickUpDetail(String airwayBillId) {
		return LogisticsData.getMerchantPickUpInstructionData(airwayBillId);
	}

	@Override
	public void onFetchComboBoxData(int startRow, int totalRow) {	
		//Request approval status combo
		RPCRequest requestApproval=new RPCRequest();
		requestApproval = new RPCRequest();
		requestApproval.setActionURL(GWT.getHostPageBaseURL() + activityReportReconciliationServlet+"?method=fetchApprovalStatusComboBoxData&type=RPC");
		requestApproval.setHttpMethod("POST");
		requestApproval.setUseSimpleHttp(true);
		requestApproval.setShowPrompt(false);
		final int start = startRow;
		final int total = totalRow;
		RPCManager.sendRequest(requestApproval, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseApproval = rawData.toString();
						String xmlDataApproval = rpcResponseApproval;
						final Map<String, String> approvalMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataApproval));
						
						//Request order item status combo
						RPCRequest requestStatus = new RPCRequest();
						requestStatus.setActionURL(GWT.getHostPageBaseURL() + activityReportReconciliationServlet+"?method=fetchOrderStatusComboBoxData&type=RPC");
						requestStatus.setHttpMethod("POST");
						requestStatus.setUseSimpleHttp(true);
						requestStatus.setShowPrompt(false);
						
						RPCManager.sendRequest(requestStatus, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseStatus = rawData.toString();
										String xmlDataStatus = rpcResponseStatus;
										final Map<String, String> statusMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));
										getView().loadUploadLogData(LogisticsData.getFileUploadLogData());
										getView().loadAirwayBillData(LogisticsData.getActivityReportAirwayBillsData(start, total), approvalMap, statusMap);
//										getView().refreshAirwayBillData();
									}
						});
				}
		});
	}
	
	@Override
	public void onCountTotalData() {
		RPCRequest rpcRequest=new RPCRequest();
    	rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  activityReportReconciliationServlet + "?method=getTotalLogAirwaybillData&type=RPC");
		rpcRequest.setHttpMethod("POST");
		rpcRequest.setUseSimpleHttp(true);
		rpcRequest.setShowPrompt(false);
		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
				public void execute(RPCResponse response, Object rawData, RPCRequest request) {
					String total = rawData.toString().substring(14, rawData.toString().length()-18);
					getView().setLastPage(Integer.parseInt(total));
				}
		});
	}
}
