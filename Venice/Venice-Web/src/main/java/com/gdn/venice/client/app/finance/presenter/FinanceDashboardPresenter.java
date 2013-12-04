package com.gdn.venice.client.app.finance.presenter;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.view.handlers.FinanceDashboardUiHandlers;
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
 * Presenter for Finance Dashboard
 * 
 * @author Henry Chandra
 */
public class FinanceDashboardPresenter
		extends
		Presenter<FinanceDashboardPresenter.MyView, FinanceDashboardPresenter.MyProxy>
		implements FinanceDashboardUiHandlers {
	private final DispatchAsync dispatcher;
	public final static String financeDashboardPresenterServlet = "FinanceDashboardPresenterServlet";

	/**
	 * {@link FinanceDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeDashboardPage)
	public interface MyProxy extends Proxy<FinanceDashboardPresenter>, Place {
	}

	/**
	 * {@link FinanceDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FinanceDashboardUiHandlers> {
		void onLoadFinanceDashboard(Map<String,String> map);
		void ShowFussionChartRevenueStatus(Map mapPeriod,Map mapFussion,String str,boolean action);
		void ShowFussionChartRevenueHistory(Map mapFussion,String idMap,boolean action,String countNerxOrBack);

	}

	@Inject
	public FinanceDashboardPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchPeriodFinanceDashboardCommandComboBox();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	
	public void onFetchPeriodFinanceDashboardCommandComboBox() {
		RPCRequest request = new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL()+ financeDashboardPresenterServlet+ "?method=fetchPeriodFinanceDashboardCommandComboBox&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
			getView().onLoadFinanceDashboard(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
			}
		});

	}
	
	public void onFetchFussionChartRevenueStatus(final Map mapPeriod,final String IdPeriod) {
		RPCRequest request = new RPCRequest();
		request.setData(IdPeriod);
		request.setActionURL(GWT.getHostPageBaseURL()+ financeDashboardPresenterServlet+ "?method=fetchFussionChartRevenueStatus&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;				
				getView().ShowFussionChartRevenueStatus(mapPeriod,Util.formHashMapfromXML(xmlData),IdPeriod,true);
			}
		});

	}

		public void onFetchFussionChartRevenueHistory(final String IdMap, final String countNerxOrBack) {
			
			RPCRequest request = new RPCRequest();			
			request.setData(IdMap+"&"+countNerxOrBack);
			request.setActionURL(GWT.getHostPageBaseURL()+ financeDashboardPresenterServlet+ "?method=fetchFussionChartRevenueHistory&type=RPC");
			request.setHttpMethod("POST");
			request.setUseSimpleHttp(true);
			request.setShowPrompt(false);
			RPCManager.sendRequest(request, new RPCCallback() {

				public void execute(RPCResponse response, Object rawData,
						RPCRequest request) {
					String rpcResponse = rawData.toString();
					String xmlData = rpcResponse;				
					getView().ShowFussionChartRevenueHistory(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)),IdMap,true,countNerxOrBack);
				}
			});

		}
	
}
