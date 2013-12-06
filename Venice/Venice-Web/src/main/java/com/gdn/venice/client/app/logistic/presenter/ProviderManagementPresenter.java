package com.gdn.venice.client.app.logistic.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.logistic.data.ProviderData;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers;
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
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Presenter for Logistics Provider Management Screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderManagementPresenter
		extends
		Presenter<ProviderManagementPresenter.MyView, ProviderManagementPresenter.MyProxy>
		implements ProviderManagementUiHandlers {
	
	ProviderManagementView providerView;
	
	public static final String providerManagementPresenterServlet = "ProviderManagementPresenterServlet";
	
	protected final DispatchAsync dispatcher;

	/**
	 * {@link ProviderManagementPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticProviderManagementPage)
	public interface MyProxy extends Proxy<ProviderManagementPresenter>, Place {
	}

	/**
	 * {@link ProviderManagementPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ProviderManagementUiHandlers> {
		public void loadLogisticsProviderData(DataSource dataSource);
		public void showProviderDetail(final ListGridRecord record);
		public void refreshProviderData();
		public void refreshAddressData();
		public void refreshContactData();
		public void refreshServiceData();
		public void refreshAgreementData();
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public ProviderManagementPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadLogisticsProviderData(ProviderData.getLogisticsProviderData());
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
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onShowProviderDetailData(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DataSource> onShowProviderDetailData(String partyId, String logProviderId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		if(partyId != null && logProviderId !=null){
			dataSources.add(ProviderData.getLogisticsProviderAddressData(partyId));
			dataSources.add(ProviderData.getLogisticsProviderContactData(partyId));
			dataSources.add(ProviderData.getLogisticsProviderServiceData(logProviderId));
			dataSources.add(ProviderData.getLogisticsProviderScheduleData(logProviderId));
			dataSources.add(ProviderData.getLogisticsProviderAgreementData(logProviderId));
		}else if(partyId == null && logProviderId ==null){
			dataSources.add(ProviderData.getLogisticsProviderAddressData(null));
			dataSources.add(ProviderData.getLogisticsProviderContactData(null));
			dataSources.add(ProviderData.getLogisticsProviderServiceData(null));
			dataSources.add(ProviderData.getLogisticsProviderScheduleData(null));
			dataSources.add(ProviderData.getLogisticsProviderAgreementData(null));
		}
		
		return dataSources;

		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveAddressClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveAddressClicked(HashMap<String, String> addressDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(addressDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveAddressData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshAddressData();
						} else if (rpcResponse.startsWith("-1")){
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}else if (rpcResponse.startsWith("-2")){
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn("Wrong Entry ! Address Type must be filled");
							}
						}

					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onRemoveAddressClicked(java.util.HashMap)
	 */
	@Override
	public void onRemoveAddressClicked(final HashMap<String, String> addressDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(addressDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=removeAddressData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Removing records...");
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
							getView().refreshAddressData();
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
					}
		});
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveContactClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveContactClicked(HashMap<String, String> contactDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(contactDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveContactData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshContactData();
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

					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onRemoveContactClicked(java.util.HashMap)
	 */
	@Override
	public void onRemoveContactClicked(HashMap<String, String> contactDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(contactDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=removeContactData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting records...");
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
							getView().refreshContactData();
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
					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveServiceClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveServiceClicked(HashMap<String, String> serviceDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(serviceDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveServiceData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshServiceData();
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

					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onRemoveServiceClicked(java.util.HashMap)
	 */
	@Override
	public void onRemoveServiceClicked(HashMap<String, String> serviceDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(serviceDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=removeServiceData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting records...");
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
							getView().refreshContactData();
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
					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveScheduleClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveScheduleClicked(HashMap<String, String> scheduleDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(scheduleDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveScheduleData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshServiceData();
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

					}
		});
		
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveAgreementClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveAgreementClicked(HashMap<String, String> agreementDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(agreementDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveAgreementData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshAgreementData();
						} else if (rpcResponse.startsWith("-1")){
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}else if (rpcResponse.startsWith("-2")){
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn("Wrong Entry ! Expiry Date must bigger than Agreement Date");
							}
						}

					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onRemoveAgreementClicked(java.util.HashMap)
	 */
	@Override
	public void onRemoveAgreementClicked(
			HashMap<String, String> agreementDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(agreementDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=removeAgreementData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting records...");
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
							getView().refreshAgreementData();
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
					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onSaveProviderClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveProviderClicked(HashMap<String, String> providerDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(providerDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=saveProviderData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving records...");
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
							getView().refreshProviderData();
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

					}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers#onDeleteProviderClicked(java.util.HashMap)
	 */
	@Override
	public void onDeleteProviderClicked(HashMap<String, String> providerDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(providerDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + providerManagementPresenterServlet + "?method=deleteProviderData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting records...");
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
							getView().refreshProviderData();
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
					}
		});
		
		
	}


}