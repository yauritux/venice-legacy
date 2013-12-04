package com.gdn.venice.client.app.general.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.app.general.view.handlers.ReturDataViewerUiHandlers;
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
 * Presenter for Retur Data Viewer Screen
 * 
 * @author Roland
 */
public class ReturDataViewerPresenter extends Presenter<ReturDataViewerPresenter.MyView, ReturDataViewerPresenter.MyProxy> implements ReturDataViewerUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String returDataViewerServlet = "ReturDataViewerPresenterServlet";

	/**
	 * {@link ReturDataViewerPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.generalReturDataViewer)
	public interface MyProxy extends Proxy<ReturDataViewerPresenter>, Place {
		
	}

	/**
	 * {@link ReturDataViewerPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<ReturDataViewerUiHandlers> {
		public void loadReturData(DataSource dataSource, Map<String,String> status);
		void refreshReturData();
//		public void setLastPage(int totalRows);
	}

	@Inject
	public ReturDataViewerPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData(0);
//		onCountTotalData();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}

	@Override
	public List<DataSource> onShowReturDetailData(String returId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		dataSources.add(GeneralData.getReturDetailData(returId));
		dataSources.add(GeneralData.getReturItemData(returId));
		dataSources.add(GeneralData.getReturCustomerData(returId));
		dataSources.add(GeneralData.getReturCustomerAddressData(returId));
		dataSources.add(GeneralData.getReturCustomerContactData(returId));
		dataSources.add(GeneralData.getReturLogisticsAirwayBillData(returId));
		dataSources.add(GeneralData.getReturHistoryReturData(returId));
		dataSources.add(GeneralData.getReturHistoryReturItemData(returId));
		return dataSources;
	}

	@Override
	public void onFetchComboBoxData(int startRow) {	
		//Request retur status combo
		RPCRequest requestStatus=new RPCRequest();
		requestStatus = new RPCRequest();
		requestStatus.setActionURL(GWT.getHostPageBaseURL() + returDataViewerServlet+"?method=fetchOrderStatusComboBoxData&type=RPC");
		requestStatus.setHttpMethod("POST");
		requestStatus.setUseSimpleHttp(true);
		requestStatus.setShowPrompt(false);
		final int start = startRow;
		RPCManager.sendRequest(requestStatus, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseStatus = rawData.toString();
						String xmlDataStatus = rpcResponseStatus;
						final Map<String, String> statusMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));
						getView().loadReturData(GeneralData.getReturData(start), statusMap);
//						getView().refreshReturData();
					}
		});
	}

	@Override
	public void onCountTotalData() {
		RPCRequest rpcRequest=new RPCRequest();
    	rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  ReturDataViewerPresenter.returDataViewerServlet + "?method=getTotalOrderData&type=RPC");
		rpcRequest.setHttpMethod("POST");
		rpcRequest.setUseSimpleHttp(true);
		rpcRequest.setShowPrompt(false);
		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
				public void execute(RPCResponse response, Object rawData, RPCRequest request) {
					String total = rawData.toString().substring(14, rawData.toString().length()-18);
//					getView().setLastPage(Integer.parseInt(total));
				}
		});
	}
}
