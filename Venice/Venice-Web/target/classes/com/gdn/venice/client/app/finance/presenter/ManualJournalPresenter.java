package com.gdn.venice.client.app.finance.presenter;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.handlers.ManualJournalUiHandlers;
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
 * Presenter for Manual Journal
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ManualJournalPresenter
		extends
		Presenter<ManualJournalPresenter.MyView, ManualJournalPresenter.MyProxy>
		implements ManualJournalUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String journalPresenterServlet = "JournalPresenterServlet";
	
	/**
	 * {@link ManualJournalPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeManualJournalPage)
	public interface MyProxy extends Proxy<ManualJournalPresenter>, Place {
	}

	/**
	 * {@link ManualJournalPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ManualJournalUiHandlers> {

		void loadJournalData(DataSource dataSource);
		void loadManualJournalDetail(DataSource dataSource, LinkedHashMap<String, String> accountMap, ListGridRecord record);
		void refreshManualJournalData();
	}

	/**
	 * Links the presenter to the view, event bus, proxy and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public ManualJournalPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		getView().loadJournalData(FinanceData.getManualJournalData());
		
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
	 * @see com.gdn.venice.client.app.finance.view.handlers.ManualJournalUiHandlers#onNewManualJournal(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void onNewManualJournal(final DataSource dataSource) {
		RPCRequest request=new RPCRequest();
		
		request.setActionURL(GWT.getHostPageBaseURL() + journalPresenterServlet + "?method=fetchManualJournalAccountComboBoxData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing new manual journals...");
		RPCManager.setShowPrompt(true);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						
						String xmlData = rpcResponse;
						LinkedHashMap<String, String> accountMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlData));
						
						getView().loadManualJournalDetail(dataSource, accountMap, null);
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		});
	}
	
	@Override
	public void onManualJournalDetail(final DataSource dataSource, final ListGridRecord record) {
		RPCRequest request=new RPCRequest();
		
		request.setActionURL(GWT.getHostPageBaseURL() + journalPresenterServlet + "?method=fetchManualJournalAccountComboBoxData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Processing manual journal details...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						
						String xmlData = rpcResponse;
						LinkedHashMap<String, String> accountMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlData));
						
						getView().loadManualJournalDetail(dataSource, accountMap, record);
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		});
	}
	

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.ManualJournalUiHandlers#onSaveManualJournalClicked(java.util.HashMap)
	 */
	@Override
	public void onSaveManualJournalClicked(final HashMap<String, String> manualJournalDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(manualJournalDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + journalPresenterServlet + "?method=saveManualJournalDetail&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving manual journals...");
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
							getView().refreshManualJournalData();
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
	 * @see com.gdn.venice.client.app.finance.view.handlers.ManualJournalUiHandlers#onDeleteManualJournalClicked(java.util.HashMap)
	 */
	@Override
	public void onDeleteManualJournalClicked(final HashMap<String, String> manualJournalMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(manualJournalMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + journalPresenterServlet + "?method=deleteManualJournalDetail&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting manual journals...");
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
							getView().refreshManualJournalData();
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
