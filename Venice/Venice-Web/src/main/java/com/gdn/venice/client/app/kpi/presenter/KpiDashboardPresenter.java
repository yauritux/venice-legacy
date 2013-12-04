package com.gdn.venice.client.app.kpi.presenter;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.kpi.data.KpiData;
import com.gdn.venice.client.app.kpi.view.handlers.KpiDashboardUiHandlers;
import com.gdn.venice.client.app.logistic.presenter.LogisticsDashboardPresenter;
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
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Presenter for KPI Dashboard
 * 
 * @author Henry Chandra
 */
public class KpiDashboardPresenter extends	Presenter<KpiDashboardPresenter.MyView, KpiDashboardPresenter.MyProxy>implements KpiDashboardUiHandlers {
	private final DispatchAsync dispatcher;
	public final static String kpiMaintenancePresenterServlet = "KpiMaintenancePresenterServlet";

	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.kpiDashboardPage)
	public interface MyProxy extends Proxy<KpiDashboardPresenter>, Place {
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<KpiDashboardUiHandlers> {
		public void SetUpTabDashboardView(Map<String, String> map,	DataSource dataSourceMerchants, DataSource dataSourceLogistics);
		public void showGauges(Map<String,String> map,ListGridRecord listGridRecord,ListGridRecord[] gridRecord,boolean bol); 
	}

	@Inject
	public KpiDashboardPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchPeriodKpiDetailViewerCommandComboBox();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

	public void onFetchPeriodKpiDetailViewerCommandComboBox() {
		RPCRequest request = new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL()+ kpiMaintenancePresenterServlet+ "?method=fetchKpiSetupPartySlaDataCommandComboBoxPeriod&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
				getView().SetUpTabDashboardView(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)),KpiData.getKpiDashboardData("1", "", ""),KpiData.getKpiDashboardData("2", "", ""));
			}
		});

	}
	
	public void onFetchValueBaselineFromPartyTarget(HashMap<String, String> DataMap,final ListGridRecord listGridRecord,final ListGridRecord[] record, final boolean bol) {
		RPCRequest request = new RPCRequest();
		request.setData(Util.formXMLfromHashMap(DataMap));
		request.setActionURL(GWT.getHostPageBaseURL()+ kpiMaintenancePresenterServlet+ "?method=fetchValueBaselineFromPartyTarget&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, new RPCCallback() {

			public void execute(RPCResponse response, Object rawData,
					RPCRequest request) {
				String rpcResponse = rawData.toString();
				String xmlData = rpcResponse;
				getView().showGauges(Util.formHashMapfromXML(xmlData),listGridRecord,record,bol);
			}
		});

	}


}
