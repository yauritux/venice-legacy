package com.gdn.venice.client.app.administration.presenter;

import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.administration.data.AdministrationData;
import com.gdn.venice.client.app.administration.view.handlers.ModuleConfigurationUiHandlers;
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
 * Presenter for Module Configuration
 * 
 * @author Roland
 */
public class ModuleConfigurationPresenter
		extends
		Presenter<ModuleConfigurationPresenter.MyView, ModuleConfigurationPresenter.MyProxy>
		implements ModuleConfigurationUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String moduleConfigurationPresenterServlet = "ModuleConfigurationPresenterServlet";
	
	/**
	 * {@link ModuleConfigurationPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.adminModuleConfiguration)
	public interface MyProxy extends Proxy<ModuleConfigurationPresenter>, Place {
	}

	/**
	 * {@link ModuleConfigurationPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ModuleConfigurationUiHandlers> {
		public void loadModuleData(DataSource dataSource, Map<String,String> moduleTypeMap, Map<String,String> parentModuleMap);
		void refreshModuleData();
		void getComboBoxData();
	}

	@Inject
	public ModuleConfigurationPresenter(EventBus eventBus, MyView view,
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
		//Request Module Type Combo
		RPCRequest requestModuleType = new RPCRequest();
		requestModuleType.setActionURL(GWT.getHostPageBaseURL() + moduleConfigurationPresenterServlet + "?method=fetchModuleTypeComboBoxData&type=RPC");
		requestModuleType.setHttpMethod("POST");
		requestModuleType.setUseSimpleHttp(true);
		requestModuleType.setShowPrompt(false);
		
		RPCManager.sendRequest(requestModuleType, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseModuleType = rawData.toString();
						String xmlDataModuleType = rpcResponseModuleType;
						final Map<String, String> moduleTypeMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataModuleType));
						
						//Request Parent Module Combo
						RPCRequest requestParentModule = new RPCRequest();
						requestParentModule.setActionURL(GWT.getHostPageBaseURL() + moduleConfigurationPresenterServlet + "?method=fetchParentModuleComboBoxData&type=RPC");
						requestParentModule.setHttpMethod("POST");
						requestParentModule.setUseSimpleHttp(true);
						requestParentModule.setShowPrompt(false);
						
						RPCManager.sendRequest(requestParentModule, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseParentModule = rawData.toString();
										String xmlDataParentModule = rpcResponseParentModule;
										final Map<String, String> parentModuleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataParentModule));
										getView().loadModuleData(AdministrationData.getModuleConfigurationData(), moduleTypeMap, parentModuleMap);
									}
						});
				}
		});
	}
}
