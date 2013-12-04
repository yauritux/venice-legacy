package com.gdn.venice.client.app.finance.presenter;

import java.util.HashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.PromotionData;
import com.gdn.venice.client.app.finance.view.handlers.PromotionUiHandlers;
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
 * Presenter for promotions
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PromotionPresenter
		extends
		Presenter<PromotionPresenter.MyView, PromotionPresenter.MyProxy>
		implements PromotionUiHandlers {
	public final static String promotionPresenterServlet = "PromotionPresenterServlet";
	
	/**
	 * {@link PromotionPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financePromotionPage)
	public interface MyProxy extends Proxy<PromotionPresenter>, Place {
	}

	/**
	 * {@link PromotionPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<PromotionUiHandlers> {

		void loadPromotionData(DataSource dataSource);
		void refreshPromotionData();
		void refreshPromotionShareData();
		
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public PromotionPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		getView().loadPromotionData(PromotionData.getPromotionData());
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
	 * @see com.gdn.venice.client.app.finance.view.handlers.PromotionUiHandlers#onSavePromotionShareClicked(java.util.HashMap)
	 */
	@Override
	public void onSavePromotionShareClicked(final HashMap<String, String> promotionShareDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(promotionShareDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + promotionPresenterServlet + "?method=savePromotionShare&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Saving promotion share records...");
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
							getView().refreshPromotionShareData();
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
	 * @see com.gdn.venice.client.app.finance.view.handlers.PromotionUiHandlers#onDeletePromotionShareClicked(java.util.HashMap)
	 */
	@Override
	public void onDeletePromotionShareClicked(final HashMap<String, String> promotionShareDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(promotionShareDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + promotionPresenterServlet + "?method=deletePromotionShare&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Deleting promotion share records...");
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
							getView().refreshPromotionShareData();
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
