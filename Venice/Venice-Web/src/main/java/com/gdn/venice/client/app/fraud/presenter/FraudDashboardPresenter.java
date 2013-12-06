package com.gdn.venice.client.app.fraud.presenter;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.view.handlers.FraudDashboardUiHandlers;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;

/**
 * Presenter for Fraud Dashboard
 * 
 * @author Henry Chandra
 */

public class FraudDashboardPresenter
		extends
		Presenter<FraudDashboardPresenter.MyView, FraudDashboardPresenter.MyProxy>
		implements FraudDashboardUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	public final static String fraudCaseMaintenancePresenterServlet = "FraudCaseMaintenancePresenterServlet";

	/**
	 * {@link FraudDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudDashboardPage)
	public interface MyProxy extends Proxy<FraudDashboardPresenter>, Place {
	}

	/**
	 * {@link FraudDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudDashboardUiHandlers> {
		void showFussionChart(HashMap<String,String> map);
		void ShowFussionProcessingHistory(Map mapFussion,String idMap,boolean action,String nerxOrBack);
	}

	@Inject
	public FraudDashboardPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		onFetchValueToFussionChart();
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());

		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,this);
	}

	public void onFetchValueToFussionChart() {
		RPCRequest request = new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL()+ fraudCaseMaintenancePresenterServlet+ "?method=fetchValueToFussionCharToFraudCaset&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
				getView().showFussionChart(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
			}
		});

	}
	
	public void onFectFraudProcessingHistory(final String idMap, final String countNerxOrBack) {
		RPCRequest request = new RPCRequest();
		request.setData(idMap+"&"+countNerxOrBack);
		request.setActionURL(GWT.getHostPageBaseURL()+ fraudCaseMaintenancePresenterServlet+ "?method=onFectFraudProcessingHistory&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
				getView().ShowFussionProcessingHistory(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)),idMap,true,countNerxOrBack);
			}
		});

	}
}
