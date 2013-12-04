package com.gdn.venice.client.app.kpi.presenter;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.kpi.view.handlers.KpiSetupUiHandlers;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;

/**
 * Presenter for KPI Setup
 * 
 * @author Henry Chandra
 */
public class KpiSetupPresenter	extends
		Presenter<KpiSetupPresenter.MyView, KpiSetupPresenter.MyProxy>
		implements KpiSetupUiHandlers {
	private final DispatchAsync dispatcher;
	//create new file KpiMaintenancePresenterServlet and declare to the file web.xml
	public final static String kpiMaintenancePresenterServlet = "KpiMaintenancePresenterServlet";//+
	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.kpiSetupPage)
	public interface MyProxy extends Proxy<KpiSetupPresenter>, Place {
		
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<KpiSetupUiHandlers> {
		public void setComboBoxKpi(Map <String,String> map);
		public void setComboBoxPeriod(Map <String,String> map);
		public void setComboBoxBaseline(Map <String,String> map);
		public void refreshKpiSetupPartySlaData();
		}

	@Inject
	public KpiSetupPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {		
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
			
		onFetchKpiSetupPartySlaDataCommandComboBoxKpi();		
		
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	public void onFetchKpiSetupPartySlaDataCommandComboBoxKpi() {
		
		//final String temp;
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
						getView().setComboBoxKpi(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
						onFetchPeriodKpiDetailViewerCommandComboBox();
						}
		});

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
						getView().setComboBoxPeriod(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));
						onFetchKpiSetupPartySlaDataCommandComboBoxBaseline();
					}
		});

	}

		
		public void onFetchKpiSetupPartySlaDataCommandComboBoxBaseline() {
			RPCRequest request=new RPCRequest();
			request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=fetchKpiSetupPartySlaDataCommandComboBoxBaseline&type=RPC");
			request.setHttpMethod("POST");
			request.setUseSimpleHttp(true);
			request.setShowPrompt(false);
			RPCManager.sendRequest(request, 
					new RPCCallback () {
				
						public void execute(RPCResponse response,
								Object rawData, RPCRequest request) {
							String rpcResponse = rawData.toString();						
							String xmlData = rpcResponse;					
							getView().setComboBoxBaseline(Util.formComboBoxMap(Util.formHashMapfromXML(xmlData)));						
						}
			});
			

		}
		
	
public void addKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap) {
		RPCRequest request=new RPCRequest();
		request.setData(Util.formXMLfromHashMap(DataMap));
		
		request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=addKpiSetupPartySlaDataCommand&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		RPCManager.sendRequest(request, 
				new RPCCallback () {			
					public void execute(RPCResponse response,Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();						
						String xmlData = rpcResponse;							
						if (rpcResponse.startsWith("0")) {
							getView().refreshKpiSetupPartySlaData();
							SC.say("Data Added");			
						} if (rpcResponse.startsWith("2")) {
							SC.say("Data already exist, Please check again of data is selected to be saved");			
						}
						if (rpcResponse.startsWith("3")) {
							SC.say("Value of KPI and  Party does not match, Please check again of data is selected to be saved");			
						}
					}
		});

	}

public void updateKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap) {
	RPCRequest request=new RPCRequest();
	request.setData(Util.formXMLfromHashMap(DataMap));
	
	request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=updateKpiSetupPartySlaDataCommand&type=RPC");
	request.setHttpMethod("POST");
	request.setUseSimpleHttp(true);
	request.setShowPrompt(false);
	RPCManager.sendRequest(request, 
			new RPCCallback () {
		
				public void execute(RPCResponse response,
						Object rawData, RPCRequest request) {
					String rpcResponse = rawData.toString();						
					String xmlData = rpcResponse;	
					
					if (rpcResponse.startsWith("0")) {
						getView().refreshKpiSetupPartySlaData();
						SC.say("Data Updated");			
					} if (rpcResponse.startsWith("2")) {
						SC.say("Data already exist/Data has not changed, Please check again of data is selected to be Updated");	
					} if (rpcResponse.startsWith("3")) {
						SC.say("Value of KPI and  Party does not match, Please check again of data is selected to be Updated");			
					}
				}
	});

}

public void deleteKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap) {
	RPCRequest request=new RPCRequest();
	request.setData(Util.formXMLfromHashMap(DataMap));
	request.setActionURL(GWT.getHostPageBaseURL() + kpiMaintenancePresenterServlet + "?method=deleteKpiSetupPartySlaDataCommand&type=RPC");
	request.setHttpMethod("POST");
	request.setUseSimpleHttp(true);
	request.setShowPrompt(false);
	RPCManager.sendRequest(request, 
			new RPCCallback () {
		
				public void execute(RPCResponse response,
						Object rawData, RPCRequest request) {
					String rpcResponse = rawData.toString();						
					String xmlData = rpcResponse;	
					
					if (rpcResponse.startsWith("0")) {
					} else {
						SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
					}
				}
	});

}



}