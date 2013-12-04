package com.gdn.venice.client.app.general.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.general.data.PartyMaintenanceData;
import com.gdn.venice.client.app.general.view.handlers.PartyMaintenanceUiHandlers;
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
 * Presenter for Party Maintenance screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PartyMaintenancePresenter
		extends
		Presenter<PartyMaintenancePresenter.MyView, PartyMaintenancePresenter.MyProxy>
		implements PartyMaintenanceUiHandlers{

	/*
	 * The servlet to handle the back end side of the presenter
	 */
	public final static String partyMaintenanceServlet = "PartyMaintenancePresenterServlet";
	
	/*
	 * An async dispatcher
	 */
	protected final DispatchAsync dispatcher;
	
	/**
	 * {@link PartyMaintenancePresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.generalPartyMaintenance)
	public interface MyProxy extends Proxy<PartyMaintenancePresenter>, Place {
		
	}

	/**
	 * {@link PartyMaintenancePresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<PartyMaintenanceUiHandlers> {
		
		/**
		 * Load Party data which is appears at top screen
		 * 
		 * @param dataSource 
		 */
		void loadPartyMaintenanceData(DataSource dataSource);
		void refreshPartyMaintenanceData();
		public void refreshAddressData();
	}

	/**
	 * The constructor for the presenter that sets up the 
	 * links between the presenter, view and event bus plus
	 * links the UI handlers to the view.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public PartyMaintenancePresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadPartyMaintenanceData(PartyMaintenanceData.getPartyMaintenanceData());
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
	 * @see com.gdn.venice.client.app.general.view.handlers.PartyMaintenanceUiHandlers#onShowPartyDetailData(java.lang.String)
	 */
	@Override
	public List<DataSource> onShowPartyDetailData(String partyId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		dataSources.add(PartyMaintenanceData.getPartyContactDetailData(partyId));
		dataSources.add(PartyMaintenanceData.getAddressData(partyId));
		return dataSources;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.general.view.handlers.PartyMaintenanceUiHandlers#onSaveAddressClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveAddressClicked(HashMap<String, String> addressDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(addressDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + partyMaintenanceServlet + "?method=saveAddressData&type=RPC");
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
	 * @see com.gdn.venice.client.app.general.view.handlers.PartyMaintenanceUiHandlers#onRemoveAddressClicked(java.util.HashMap)
	 */
	@Override
	public void onRemoveAddressClicked(HashMap<String, String> addressDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(addressDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + partyMaintenanceServlet + "?method=removeAddressData&type=RPC");
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
}