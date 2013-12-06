package com.gdn.venice.client.app.finance.presenter;

import java.util.HashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.handlers.SalesRecordUiHandlers;
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
 * Presenter for Sales Record Screen
 * 
 * @author Henry Chandra
 */
public class SalesRecordPresenter
		extends
		Presenter<SalesRecordPresenter.MyView, SalesRecordPresenter.MyProxy>
		implements SalesRecordUiHandlers {
	private final DispatchAsync dispatcher;
	
	public final static String salesRecordServlet = "SalesRecordPresenterServlet";

	/**
	 * {@link SalesRecordPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.salesRecordPage)
	public interface MyProxy extends Proxy<SalesRecordPresenter>, Place {
	}

	/**
	 * {@link SalesRecordPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<SalesRecordUiHandlers> {
		public void loadSalesRecordData(DataSource dataSource);
		public void refreshSalesRecordData();
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public SalesRecordPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadSalesRecordData(FinanceData.getSalesRecordData());
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

	@Override
	public DataSource onExpandSalesRecordRow(String wcsOrderItemId) {
		return FinanceData.getSalesRecordDetailAdjustmentData(wcsOrderItemId);
	}

	@Override
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap) {
		RPCRequest request=new RPCRequest();
		
		request.setData(Util.formXMLfromHashMap(refundDataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + salesRecordServlet + "?method=refund&type=RPC");
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
//							getView().refreshFundInData();
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
