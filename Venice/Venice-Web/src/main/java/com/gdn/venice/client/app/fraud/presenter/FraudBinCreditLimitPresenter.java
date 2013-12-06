package com.gdn.venice.client.app.fraud.presenter;

import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudBinCreditLimitUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.gdn.venice.client.util.Util;
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

/**
 * Presenter for Bin Credit Limit
 * 
 * @author Roland
 */

public class FraudBinCreditLimitPresenter
		extends
		Presenter<FraudBinCreditLimitPresenter.MyView, FraudBinCreditLimitPresenter.MyProxy>
		implements FraudBinCreditLimitUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String fraudBinCreditLimitPresenterServlet = "FraudBinCreditLimitPresenterServlet";
	/**
	 * {@link FraudBinCreditLimitPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudBinCreditLimitPage)
	public interface MyProxy extends Proxy<FraudBinCreditLimitPresenter>, Place {
	}

	/**
	 * {@link FraudBinCreditLimitPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudBinCreditLimitUiHandlers> {
		public void loadFraudBinCreditLimitData(DataSource dataSource, Map<String,String> card);
		void refreshBinCreditLimitData();
	}

	@Inject
	public FraudBinCreditLimitPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	
	@Override
	public void onFetchComboBoxData() {					
		//Request Card Type Combo
		RPCRequest requestCard = new RPCRequest();
		requestCard.setActionURL(GWT.getHostPageBaseURL() + fraudBinCreditLimitPresenterServlet + "?method=fetchCardComboBoxData&type=RPC");
		requestCard.setHttpMethod("POST");
		requestCard.setUseSimpleHttp(true);
		requestCard.setShowPrompt(false);
		
		RPCManager.sendRequest(requestCard, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
								String rpcResponseCard = rawData.toString();
								String xmlDataCard = rpcResponseCard;
								final Map<String, String> cardMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataCard));
								getView().loadFraudBinCreditLimitData(FraudData.getFraudBinCreditLimitData(), cardMap);
							}
				});
	}
}
