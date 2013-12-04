package com.gdn.venice.client.app.finance.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers;
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
 * Presenter for Fund In Reconciliation
 * 
 * @author Henry Chandra
 */
public class FundInReconciliationPresenter extends Presenter<FundInReconciliationPresenter.MyView, FundInReconciliationPresenter.MyProxy> implements FundInReconciliationUiHandlers {
	private final DispatchAsync dispatcher;
	
	public final static String fundIdReconciliationServlet = "FundInReconciliationPresenterServlet";

	/**
	 * {@link FundInReconciliationPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeFundInReconciliationPage)
	public interface MyProxy extends Proxy<FundInReconciliationPresenter>, Place {
	}

	/**
	 * {@link FundInReconciliationPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<FundInReconciliationUiHandlers> {
		public void loadFundInReconciliationData(DataSource dataSource, Map<String,String> paymentTypeMap, Map<String,String> bankMap,Map<String, String> parentPaymentReportMap);
		public void refreshFundInData();
		
	}

	/**
	 * Links the presenter to the event bus, view, proxy and dispatcher 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public FundInReconciliationPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData();
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void onFetchComboBoxData() {
		//Request Module Type Combo
		RPCRequest requestPaymentType = new RPCRequest();
		requestPaymentType.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=fetchPaymentTypeComboBoxData&type=RPC");
		requestPaymentType.setHttpMethod("POST");
		requestPaymentType.setUseSimpleHttp(true);
		requestPaymentType.setShowPrompt(false);
		
		RPCManager.sendRequest(requestPaymentType, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponsePaymentType = rawData.toString();
						String xmlDataPaymentType = rpcResponsePaymentType;
						final Map<String, String> paymentTypeMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataPaymentType));
						
						//Request Parent Module Combo
						RPCRequest requestBank = new RPCRequest();
						requestBank.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=fetchBankComboBoxData&type=RPC");
						requestBank.setHttpMethod("POST");
						requestBank.setUseSimpleHttp(true);
						requestBank.setShowPrompt(false);
						
						RPCManager.sendRequest(requestBank, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseBank = rawData.toString();
										String xmlDataBank = rpcResponseBank;
										final Map<String, String> parentBankMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataBank));
										
														//Request Parent Module Combo
														RPCRequest requestBank = new RPCRequest();
														requestBank.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=fetchPaymentReportComboBoxData&type=RPC");
														requestBank.setHttpMethod("POST");
														requestBank.setUseSimpleHttp(true);
														requestBank.setShowPrompt(false);
														
														RPCManager.sendRequest(requestBank, 
																new RPCCallback () {
																	public void execute(RPCResponse response,
																			Object rawData, RPCRequest request) {
																		String rpcResponseBank = rawData.toString();
																		String xmlDataBank = rpcResponseBank;
																		final Map<String, String> parentPaymentReportMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataBank));
														
																		getView().loadFundInReconciliationData(FinanceData.getFundInReconciliationData(), paymentTypeMap, parentBankMap,parentPaymentReportMap);
																		}
															});
									}
						});
				}
		});
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onSubmitForApproval(java.util.ArrayList)
	 */
	@Override
	public void onSubmitForApproval(ArrayList<String> fundInReconRecordIds) {
		RPCRequest request=new RPCRequest();
		
		/*
		 * Stuff each of the funds in record ids into the map for the request
		 */
		HashMap<String,String>map = new HashMap<String,String>();
		for (int i=0;i<fundInReconRecordIds.size();i++) {
			map.put(ProcessNameTokens.FUNDINRECONRECORDID+(i+1), fundInReconRecordIds.get(i));
		}
		String fundInReconRecordId = map.toString();
		
		request.setData(fundInReconRecordId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=submitForApproval&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Submitting funds-in records for approval...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshFundInData();
							SC.say(DataMessageTokens.SUBMITTED_FOR_APPROVAL);
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
		}
		);		
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onSubmitForApproval(java.util.ArrayList)
	 */
	@Override
	public void onCreateJournalForVA(ArrayList<String> fundInReconRecordIds) {
		RPCRequest request=new RPCRequest();
		
		/*
		 * Stuff each of the funds in record ids into the map for the request
		 */
		HashMap<String,String>map = new HashMap<String,String>();
		for (int i=0;i<fundInReconRecordIds.size();i++) {
			map.put(ProcessNameTokens.FUNDINRECONRECORDID+(i+1), fundInReconRecordIds.get(i));
		}
		String fundInReconRecordId = map.toString();
		
		request.setData(fundInReconRecordId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=createJournalForVA&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Create Journal VA...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshFundInData();
							SC.say(DataMessageTokens.GENERAL_SUCCESS_MESSAGE);
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
		}
		);		
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
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=allocateFundIn&type=RPC");
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
								SC.warn(split[1]);
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

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onRefundButtonClicked(java.util.HashMap)
	 */
	@Override
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(refundDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=refund&type=RPC");
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
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
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
	
	@Override
	public void onCancelAllocationButtonClicked(HashMap<String, String> allocationDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(allocationDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=cancelAllocation&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing Cancel Allocation...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
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
								SC.warn(split + "    " +DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.FundInReconciliationUiHandlers#onDelete(java.util.HashMap)
	 */
	@Override
	public void onDelete(HashMap<String, String> deleteDataMap){
		
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(deleteDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + fundIdReconciliationServlet + "?method=delete&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting funds-in reconciliation records...");
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
}
