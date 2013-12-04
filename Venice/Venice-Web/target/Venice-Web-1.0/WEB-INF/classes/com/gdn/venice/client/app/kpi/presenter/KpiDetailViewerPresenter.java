package com.gdn.venice.client.app.kpi.presenter;

import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.kpi.data.KpiData;
import com.gdn.venice.client.app.kpi.view.handlers.KpiDetailViewerUiHandlers;
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

/**
 * Presenter for KPI Detail Viewer
 * 
 * @author Henry Chandra
 */
public class KpiDetailViewerPresenter
		extends
		Presenter<KpiDetailViewerPresenter.MyView, KpiDetailViewerPresenter.MyProxy>
		implements KpiDetailViewerUiHandlers {
	private final DispatchAsync dispatcher;
	public final static String kpiMaintenancePresenterServlet = "KpiMaintenancePresenterServlet";//+

	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.kpiDetailViewerPage)
	public interface MyProxy extends Proxy<KpiDetailViewerPresenter>, Place {
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<KpiDetailViewerUiHandlers> {
		public void loadDataKpiDetailViewerView(DataSource dataSource);
		public void  setPeriodCombobox(Map <String,String> mapPeriod);
		public void  setKpiCombobox(Map <String,String> mapKpi);
		public void setPartyCombobox(Map<String, String> mapParty);
	}

	@Inject
	public KpiDetailViewerPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
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
		RPCRequest request=new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=fetchKpiSetupPartySlaDataCommandComboBoxPeriod&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, 
				new RPCCallback () {
			
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();						
						String xmlData = rpcResponse;						
						getView().setPeriodCombobox(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
						onFetchPartyKpiDetailViewerCommandComboBox();
					}
		});

	}
	
	public void onFetchPartyKpiDetailViewerCommandComboBox() {
		RPCRequest request=new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=fetchKpiSetupPartySlaDataCommandComboBoxParty&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, 
				new RPCCallback () {
			
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();						
						String xmlData = rpcResponse;		
						getView().setPartyCombobox(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
						onFetchKPIKpiDetailViewerCommandComboBox();
					}
		});

	}
	public void onFetchKPIKpiDetailViewerCommandComboBox() {
		RPCRequest request=new RPCRequest();
		request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=fetchKpiSetupPartySlaDataCommandComboBoxKpi&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, 
				new RPCCallback () {
			
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();						
						String xmlData = rpcResponse;		
						getView().setKpiCombobox(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
						getView().loadDataKpiDetailViewerView(KpiData.getKpiDetailViewerData());
					}
		});

	}

}
