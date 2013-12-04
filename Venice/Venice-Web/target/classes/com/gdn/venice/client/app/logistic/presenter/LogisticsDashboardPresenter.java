package com.gdn.venice.client.app.logistic.presenter;

import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.logistic.view.handlers.LogisticsDashboardUiHandlers;
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
 * Presenter for Logistics Dashboard
 * 
 * @author Henry Chandra
 */
public class LogisticsDashboardPresenter
		extends
		Presenter<LogisticsDashboardPresenter.MyView, LogisticsDashboardPresenter.MyProxy>
		implements LogisticsDashboardUiHandlers {
	private final DispatchAsync dispatcher;
	public final static String logisticsDashboardPresenterServlet = "LogisticsDashboardPresenterServlet";


	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticDashboardPage)
	public interface MyProxy extends Proxy<LogisticsDashboardPresenter>, Place {
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<LogisticsDashboardUiHandlers> {
		public void OrderProcessingHistory(Map mapFussion,String idMap,boolean action,String nerxOrBack);

	}

	@Inject
	public LogisticsDashboardPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	
	public void FetchOrderProcessingHistory(final String idMap, final String countNerxOrBack) {
		RPCRequest request = new RPCRequest();
		request.setData(idMap+"&"+countNerxOrBack);
		request.setActionURL(GWT.getHostPageBaseURL()+ logisticsDashboardPresenterServlet+ "?method=fetchOrderProcessingHistory&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
				getView().OrderProcessingHistory(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)),idMap,true,countNerxOrBack);
			}
		});

	}

}
